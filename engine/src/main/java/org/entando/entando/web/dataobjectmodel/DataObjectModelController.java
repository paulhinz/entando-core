/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.dataobjectmodel;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelService;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.entando.entando.web.dataobjectmodel.validator.DataObjectModelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dataModels")
public class DataObjectModelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JsonPatchPatchConverter jsonPatchPatchConverter;

    @Autowired
    DataModelDtoToRequestConverter dataModelDtoToRequestConverter;

    @Autowired
    private IDataObjectModelService dataObjectModelService;

    @Autowired
    private DataObjectModelValidator dataObjectModelValidator;

    public IDataObjectModelService getDataObjectModelService() {
        return dataObjectModelService;
    }

    public void setDataObjectModelService(IDataObjectModelService dataObjectModelService) {
        this.dataObjectModelService = dataObjectModelService;
    }

    public DataObjectModelValidator getDataObjectModelValidator() {
        return dataObjectModelValidator;
    }

    public void setDataObjectModelValidator(DataObjectModelValidator dataObjectModelValidator) {
        this.dataObjectModelValidator = dataObjectModelValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<DataModelDto>> getDataObjectModels(RestListRequest requestList) {
        this.getDataObjectModelValidator().validateRestListRequest(requestList, DataModelDto.class);
        PagedMetadata<DataModelDto> result = this.getDataObjectModelService().getDataObjectModels(requestList);
        this.getDataObjectModelValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataModelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DataModelDto>> getDataObjectModel(@PathVariable String dataModelId) {
        logger.debug("Requested data object model -> {}", dataModelId);
        MapBindingResult bindingResult = new MapBindingResult(new HashMap<>(), "dataModels");
        int result = this.getDataObjectModelValidator().checkModelId(dataModelId, bindingResult);
        if (bindingResult.hasErrors()) {
            if (404 == result) {
                throw new ResourceNotFoundException(DataObjectModelValidator.ERRCODE_DATAOBJECTMODEL_DOES_NOT_EXIST, "dataObjectModel", dataModelId);
            } else {
                throw new ValidationGenericException(bindingResult);
            }
        }
        DataModelDto dataModelDto = this.getDataObjectModelService().getDataObjectModel(Long.parseLong(dataModelId));
        return new ResponseEntity<>(new SimpleRestResponse<>(dataModelDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DataModelDto>> addDataObjectModel(@Valid @RequestBody DataObjectModelRequest dataObjectModelRequest,
            BindingResult bindingResult) throws JsonProcessingException {
        logger.debug("Adding data object model -> {}", dataObjectModelRequest.getModelId());
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        this.getDataObjectModelValidator().validate(dataObjectModelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        int result = this.getDataObjectModelValidator().validateBody(dataObjectModelRequest, false, bindingResult);
        if (bindingResult.hasErrors()) {
            if (404 == result) {
                throw new ResourceNotFoundException(DataObjectModelValidator.ERRCODE_POST_DATAOBJECTTYPE_DOES_NOT_EXIST, "type", dataObjectModelRequest.getType());
            } else {
                throw new ValidationGenericException(bindingResult);
            }
        }
        DataModelDto dataModelDto = this.getDataObjectModelService().addDataObjectModel(dataObjectModelRequest);
        logger.debug("Main Response -> {}", dataModelDto);
        return new ResponseEntity<>(new SimpleRestResponse<>(dataModelDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataModelId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DataModelDto>> updateDataObjectModel(@PathVariable String dataModelId,
            @Valid @RequestBody DataObjectModelRequest dataObjectModelRequest, BindingResult bindingResult) throws JsonProcessingException {
        logger.debug("Updating data object model -> {}", dataObjectModelRequest.getModelId());
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDataObjectModelValidator().validateBodyName(dataModelId, dataObjectModelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        int result = this.getDataObjectModelValidator().validateBody(dataObjectModelRequest, true, bindingResult);
        if (bindingResult.hasErrors()) {
            if (404 == result) {
                if (1 == bindingResult.getFieldErrorCount("type")) {
                    throw new ResourceNotFoundException(DataObjectModelValidator.ERRCODE_PUT_DATAOBJECTTYPE_DOES_NOT_EXIST, "type", dataObjectModelRequest.getType());
                } else {
                    throw new ResourceNotFoundException(DataObjectModelValidator.ERRCODE_DATAOBJECTMODEL_ALREADY_EXISTS, "modelId", dataObjectModelRequest.getModelId());
                }
            } else {
                throw new ValidationGenericException(bindingResult);
            }
        }
        DataModelDto dataModelDto = this.getDataObjectModelService().updateDataObjectModel(dataObjectModelRequest);
        logger.debug("Main Response -> {}", dataModelDto);
        return new ResponseEntity<>(new SimpleRestResponse<>(dataModelDto), HttpStatus.OK);

    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataModelId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes="application/json-patch+json")
    public ResponseEntity<SimpleRestResponse<DataModelDto>> patchDataObjectModel(@PathVariable Long dataModelId,
                                                                                 @RequestBody JsonNode jsonPatch,
                                                                                 BindingResult bindingResult) throws JsonProcessingException {
        logger.debug("Patching data object model -> {}", dataModelId);

        this.getDataObjectModelValidator().validateDataObjectModelJsonPatch(jsonPatch, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        DataModelDto patchedDataModelDto = this.getDataObjectModelService().getPatchedDataObjectModel(dataModelId, jsonPatch);
        DataObjectModelRequest dataObjectModelRequest = this.dataModelDtoToRequestConverter.convert(patchedDataModelDto);

        return this.updateDataObjectModel(Long.toString(dataModelId), dataObjectModelRequest, bindingResult);

    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataModelId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDataObjectModel(@PathVariable String dataModelId) throws ApsSystemException {
        logger.info("deleting data object model -> {}", dataModelId);
        MapBindingResult bindingResult = new MapBindingResult(new HashMap<>(), "dataModels");
        Long dataId = this.getDataObjectModelValidator().checkValidModelId(dataModelId, bindingResult);
        if (null == dataId) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDataObjectModelService().removeDataObjectModel(Long.parseLong(dataModelId));
        Map<String, String> payload = new HashMap<>();
        payload.put("modelId", dataModelId);
        return new ResponseEntity<>(new SimpleRestResponse<>(payload), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dictionary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<IEntityModelDictionary>> getDictionary(@RequestParam(value = "typeCode", required = false) String typeCode) {
        logger.debug("loading data model dictionary {}", typeCode);
        IEntityModelDictionary dictionary = this.getDataObjectModelService().getDataModelDictionary(typeCode);
        return new ResponseEntity<>(new SimpleRestResponse<>(dictionary), HttpStatus.OK);
    }
}
