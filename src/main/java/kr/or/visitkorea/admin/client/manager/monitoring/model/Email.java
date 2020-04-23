package kr.or.visitkorea.admin.client.manager.monitoring.model;

import com.google.gwt.json.client.JSONObject;

public class Email  extends JSONObject{

	private String enable;
	private String content;
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
