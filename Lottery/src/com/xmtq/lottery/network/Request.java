package com.xmtq.lottery.network;

import com.xmtq.lottery.parser.BaseParser;

public class Request {

	private ServerInterfaceDefinition serverInterfaceDefinition;
	private BaseParser<?> xmlParser;
	private String mBody;
	private String mUrl;

	public Request() {
	}

	public Request(ServerInterfaceDefinition serverInterfaceDefinition,
			String paramsMap, BaseParser<?> xmlParser) {
		super();
		this.serverInterfaceDefinition = serverInterfaceDefinition;
		this.mBody = paramsMap;
		this.xmlParser = xmlParser;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl1(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String mBody) {
		this.mBody = mBody;
	}

	public ServerInterfaceDefinition getServerInterfaceDefinition() {
		return serverInterfaceDefinition;
	}

	public void setServerInterfaceDefinition(
			ServerInterfaceDefinition serverInterfaceDefinition) {
		this.serverInterfaceDefinition = serverInterfaceDefinition;
	}

	public BaseParser<?> getXmlParser() {
		return xmlParser;
	}

	public void setXmlParser(BaseParser<?> xmlParser) {
		this.xmlParser = xmlParser;
	}
}
