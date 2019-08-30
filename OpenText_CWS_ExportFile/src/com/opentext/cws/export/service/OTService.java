package com.opentext.cws.export.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.soap.MTOMFeature;

import com.opentext.cws.export.entity.AppConstant;
import com.opentext.ecm.api.OTAuthentication;
import com.opentext.livelink.service.core.Authentication;
import com.opentext.livelink.service.core.Authentication_Service;
import com.opentext.livelink.service.core.ContentService;
import com.opentext.livelink.service.core.ContentService_Service;
import com.opentext.livelink.service.docman.DocumentManagement;
import com.opentext.livelink.service.docman.DocumentManagement_Service;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Headers;
import com.sun.xml.internal.ws.developer.WSBindingProvider;

public class OTService {

	// Calls AuthenticateUser() and returns the token
	@SuppressWarnings("unused")
	public static String getAuthenticationToken(String username, String password) throws Exception {
		Authentication endpoint;
		Authentication_Service service = new Authentication_Service(new URL(AppConstant.authWsdl));
		endpoint = service.getBasicHttpBindingAuthentication();
		return endpoint.authenticateUser(username, password);
	}

	// Constructs a DocMan client with the provided Token
	@SuppressWarnings({ "unused", "restriction" })
	public static DocumentManagement getDMService(String authToken) throws Exception {
		DocumentManagement_Service service = new DocumentManagement_Service(new URL(AppConstant.dmWsdl));
		DocumentManagement endpoint = service.getBasicHttpBindingDocumentManagement();
		OTAuthentication otAuth = new OTAuthentication();
		otAuth.setAuthenticationToken(authToken);
		setSoapHeader((WSBindingProvider) endpoint, otAuth);
		return endpoint;
	}

	// Constructs a Content Service with the provided token
	@SuppressWarnings({ "unused", "restriction" })
	public static ContentService getCSService(String token) throws Exception {
		ContentService_Service service = new ContentService_Service();
		ContentService endpoint = service.getBasicHttpBindingContentService(new MTOMFeature());
		OTAuthentication otAuth = new OTAuthentication();
		otAuth.setAuthenticationToken(token);
		setSoapHeader((WSBindingProvider) endpoint, otAuth);
		return endpoint;

	}

	// Constructs an Authentication client with the provided token
	@SuppressWarnings({ "restriction", "unused" })
	static Authentication setAdmToken4impersonation(String authToken) throws Exception {
		Authentication_Service service = new Authentication_Service(new URL(AppConstant.authWsdl));
		Authentication endpoint = service.getBasicHttpBindingAuthentication();
		OTAuthentication otAuth = new OTAuthentication();
		otAuth.setAuthenticationToken(authToken);
		setSoapHeader((WSBindingProvider) endpoint, otAuth);
		return endpoint;
	}

	// adds the OTAuthentication Header to the provided endpoint
	@SuppressWarnings("restriction")
	private static void setSoapHeader(WSBindingProvider bindingProvider, OTAuthentication otAuth) throws Exception {
		List<Header> headers = new ArrayList<Header>();
		SOAPMessage message = MessageFactory.newInstance().createMessage();
		SOAPPart part = message.getSOAPPart();
		SOAPEnvelope envelope = part.getEnvelope();
		SOAPHeader header = envelope.getHeader();
		headers.add(getOTAuthenticationHeader(header, otAuth));
		bindingProvider.setOutboundHeaders(headers);
	}

	// Builds the raw SOAP Header Element
	@SuppressWarnings("restriction")
	private static Header getOTAuthenticationHeader(SOAPHeader header, OTAuthentication otAuth) throws Exception {
		SOAPHeaderElement otAuthElement;
		SOAPElement authTokenElement;
		otAuthElement = header.addHeaderElement(new QName("urn:api.ecm.opentext.com", "OTAuthentication"));
		otAuthElement.setPrefix("");
		authTokenElement = otAuthElement.addChildElement(new QName("urn:api.ecm.opentext.com", "AuthenticationToken"));
		authTokenElement.setPrefix("");
		authTokenElement.addTextNode(otAuth.getAuthenticationToken());
		return Headers.create(otAuthElement);

	}
}
