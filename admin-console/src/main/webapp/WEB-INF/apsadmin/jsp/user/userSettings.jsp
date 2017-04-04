<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<style>
    .text-left{
        text-align: left !important;
    }
</style>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userDesigner" /></li>
    <li class="page-title-container"><s:text name="title.userSettings" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.userSettings" /></h1>

<div id="main" role="main">
    <s:form action="updateSystemParams">
        <s:if test="hasActionMessages()">
            <div class="alert alert-success alert-dismissable fade in">
                <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
                <ul class="margin-base-top">
                    <s:iterator value="actionMessages">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        
        <fieldset class="col-xs-12 settings-form">
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2">
                        <div class="form-group-label"><s:text name="names.users" /></div>
                    </div>
                    <div class="col-xs-10">
                        <div class="form-group-separator"><s:text name="label.requiredFields" /></div>
                    </div>
                </div>
            </div>
            
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-2 col-label">
                        <span class="display-block"><s:text name="label.active" /></span>
                    </div>
                    <div class="col-xs-4 text-left">
                        <s:set var="paramName" value="'extendedPrivacyModuleEnabled'" />
                        <input type="hidden" 
                               value="<s:property value="systemParams[#paramName]" />"
                               id="<s:property value="#paramName"/>" 
                               name="<s:property value="#paramName"/>" />
                        <input 
                            type="checkbox" 	
                            value="<s:property value="systemParams[#paramName]" />"
                            id="ch_<s:property value="#paramName"/>" 
                            class="bootstrap-switch" 
                            <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> >
                    </div>
                            
                    <div class="col-xs-2 col-label">
                            <span class="display-block"><s:text name="sysconfig.gravatarIntegrationEnabled" /></span>
                    </div>
                    <div class="col-xs-4 text-left">
                        <s:set var="paramName" value="'gravatarIntegrationEnabled'" />
                        <input type="hidden" 
                               value="<s:property value="systemParams[#paramName]" />"
                               id="<s:property value="#paramName"/>" 
                               name="<s:property value="#paramName"/>" />
                        <input 
                            type="checkbox" 	
                            value="<s:property value="systemParams[#paramName]" />"
                            id="ch_<s:property value="#paramName"/>" 
                            class="bootstrap-switch" 
                            <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if> >
                    </div>
                </div>
            </div>                
                
                <div class="form-group">
                    <div class="row">
                    <s:set var="paramName" value="'maxMonthsSinceLastAccess'" />
                    <div class="col-xs-2 col-label">
                        <span class="display-block"><s:text name="sysconfig.maxMonthsSinceLastAccess" /></span>
                    </div>
                    <div class="col-xs-4 text-left">
                        <input type="text" id="admin-settings-area-<s:property value="#paramName"/>_input" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" style="display: none"/>
                        <div class="btn-group" data-toggle="buttons">                            
                            <label class="btn btn-default <s:if test="systemParams[#paramName] != 0"> active</s:if>" onclick="setCustomValue('<s:property value="#paramName"/>')" id="admin-settings-area-<s:property value="#paramName"/>_label">
                                <input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_value" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" <s:if test="systemParams[#paramName] != 0">checked="checked"</s:if> />
                                <s:property value="systemParams[#paramName]"/>
                            </label>
                            <label class="btn btn-default <s:if test="systemParams[#paramName] == 0"> active</s:if>" onclick="hideCustomValue('<s:property value="#paramName"/>')">
                                <input type="radio" class="radiocheck" id="admin-settings-area-<s:property value="#paramName"/>_false" name="<s:property value="#paramName"/>" value="0" <s:if test="systemParams[#paramName] == 0">checked="checked"</s:if> />
                                false
                            </label>
                        </div>
                    </div>
                    
                    <s:set var="paramName" value="'maxMonthsSinceLastPasswordChange'" />
                    <div class="col-xs-2 col-label">
                        <span class="display-block"><s:text name="sysconfig.maxMonthsSinceLastPasswordChange" /></span>
                    </div>
                    <div class="col-xs-4 text-left">
                        <input type="text" id="admin-settings-area-<s:property value="#paramName"/>_input" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" style="display: none"/>
                        <div class="btn-group" data-toggle="buttons">                            
                            <label class="btn btn-default <s:if test="systemParams[#paramName] != 0"> active</s:if>" onclick="setCustomValue('<s:property value="#paramName"/>')" id="admin-settings-area-<s:property value="#paramName"/>_label">
                                <input type="radio" class="" id="admin-settings-area-<s:property value="#paramName"/>_value" name="<s:property value="#paramName"/>" value="<s:property value="systemParams[#paramName]"/>" <s:if test="systemParams[#paramName] != 0">checked="checked"</s:if> />
                                <s:property value="systemParams[#paramName]"/>
                            </label>
                            <label class="btn btn-default <s:if test="systemParams[#paramName] == 0"> active</s:if>" onclick="hideCustomValue('<s:property value="#paramName"/>')">
                                <input type="radio" class="" id="admin-settings-area-<s:property value="#paramName"/>_false" name="<s:property value="#paramName"/>" value="0" <s:if test="systemParams[#paramName] == 0">checked="checked"</s:if> />
                                false
                            </label>
                        </div>
                    </div>
                    
                </div>
            </div>
        </fieldset>
        
        <div class="form-group bottom-row">
            <div class="row">
                <div class="col-xs-12">
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-block pull-right">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
        
    </s:form>
    
</div>

<script type="application/javascript" >
    $('input[type="checkbox"][id^="ch_"]').on('switchChange.bootstrapSwitch', function (ev, data) {
    var id = ev.target.id.substring(3);
    console.log("id", id);
    var $element = $('#'+id);
    $element.attr('value', ''+data);
    });
</script>

<script type="application/javascript" >
    function setCustomValue(idPar){
        $('#admin-settings-area-'+idPar+'_input').show();
        $('#admin-settings-area-'+idPar+'_value').hide();
        $('#admin-settings-area-'+idPar+'_label').hide();
    }
    function hideCustomValue(idPar){
        $('#admin-settings-area-'+idPar+'_input').hide();
        $('#admin-settings-area-'+idPar+'_value').show();
        $('#admin-settings-area-'+idPar+'_label').show();
        $("#admin-settings-area-"+idPar+"_input").val("0");
    }
</script>