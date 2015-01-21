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
package com.agiletec.plugins.jacms.aps.system.services.content.parse.extraAttribute;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.attribute.TextAttributeHandler;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;

/**
 * Classe handler per l'interpretazione della porzione di xml 
 * relativo all'attributo di tipo link.
 * @author E.Santoboni
 */
public class LinkAttributeHandler extends TextAttributeHandler {
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("link")) {
			startLink(attributes, qName);
		} else if (qName.equals("urldest")) {
			startUrlDest(attributes, qName);
		} else if (qName.equals("pagedest")) {
			startPageDest(attributes, qName);
		} else if (qName.equals("contentdest")) {
			startContentDest(attributes, qName);
		} else {
			super.startAttribute(attributes, qName);
		}
	}
	
	private void startLink(Attributes attributes, String qName) throws SAXException {
		this._linkType = extractAttribute(attributes, "type", qName, true);
		((LinkAttribute) this.getCurrentAttr()).setSymbolicLink(new SymbolicLink());
	}
	
	private void startUrlDest(Attributes attributes, String qName) throws SAXException {
		return; // niente da fare
	}
	
	private void startPageDest(Attributes attributes, String qName) throws SAXException {
		return; // niente da fare
	}
	
	private void startContentDest(Attributes attributes, String qName) throws SAXException {
		return; // niente da fare
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("link")) {
			endLink();
		} else if (qName.equals("urldest")) {
			endUrlDest(textBuffer);
		} else if (qName.equals("pagedest")){
			endPageDest(textBuffer);
		} else if (qName.equals("contentdest")) {
			endContentDest(textBuffer);
		} else {
			super.endAttribute(qName, textBuffer);
		}
	}
	
	private void endLink() {
		SymbolicLink symLink = 
			((LinkAttribute) this.getCurrentAttr()).getSymbolicLink();
		if (null != symLink && null != _linkType) {
			if (_linkType.equals("content")) {
				symLink.setDestinationToContent(_contentDest);
			} else if (_linkType.equals("external")) {
				symLink.setDestinationToUrl(_urlDest);
			} else if (_linkType.equals("page")) {
				symLink.setDestinationToPage(_pageDest);
			} else if (_linkType.equals("contentonpage")) {
				symLink.setDestinationToContentOnPage(_contentDest, _pageDest);
			}
		}
		_contentDest = null;
		_urlDest = null;
		_pageDest = null;
	}
	
	private void endUrlDest(StringBuffer textBuffer){
		if (null != textBuffer) {
			_urlDest = textBuffer.toString();
		}
	}
	
	private void endPageDest(StringBuffer textBuffer){
		if (null != textBuffer) {
			_pageDest = textBuffer.toString();
		}
	}
	
	private void endContentDest(StringBuffer textBuffer){
		if (null != textBuffer) {
			_contentDest = textBuffer.toString();
		}
	}
	
	private String _linkType;
	private String _urlDest;
	private String _pageDest;
	private String _contentDest;
	
}