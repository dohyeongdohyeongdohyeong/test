package kr.or.visitkorea.admin.client.application;

import java.util.Map;

import gwt.material.design.client.ui.MaterialLink;

public class WindowParamter {
	
	private MaterialLink tgrLink;
	
	private String appKey;
	
	private String divisionName;
	
	private int windowWidth;
	
	private int windowHeight;
	
	private Map<String, Object> params;

	public WindowParamter(MaterialLink tgrLink, String appKey, String divisionName, int windowWidth, int windowHeight) {
		super();
		this.tgrLink = tgrLink;
		this.appKey = appKey;
		this.divisionName = divisionName;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		
		Console.log("tgrLink.getText() :: " + tgrLink.getText());
		Console.log("divisionName :: " + divisionName);
		Console.log("compaire :: " + divisionName.equals(tgrLink.getText()));
		
	}

	public Map<String, Object> getParams() {
		return params;
	}


	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public MaterialLink getTgrLink() {
		return tgrLink;
	}

	public void setTgrLink(MaterialLink tgrLink) {
		this.tgrLink = tgrLink;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}
	
}
