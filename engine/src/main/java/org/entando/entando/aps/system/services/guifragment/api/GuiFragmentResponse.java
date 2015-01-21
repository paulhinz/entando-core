/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.guifragment.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "response")
public class GuiFragmentResponse extends AbstractApiResponse {
    
    @Override
    @XmlElement(name = "result", required = true)
    public GuiFragmentResponseResult getResult() {
        return (GuiFragmentResponseResult) super.getResult();
    }
	
	public void setResult(GuiFragmentResponseResult result) {
		super.setResult(result);
	}
    
    @Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        return new GuiFragmentResponseResult();
    }
    
}