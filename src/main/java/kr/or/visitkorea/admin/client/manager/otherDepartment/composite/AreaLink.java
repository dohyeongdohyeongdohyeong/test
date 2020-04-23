package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;

public class AreaLink extends MaterialLink {

	private JSONObject infoObject;
	private String key;
	private String linkUrl;

	public JSONObject getInfoObject() {
		return infoObject;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String getLinkUrl() {
		return linkUrl;
	}
	
	public void getInformation(String OTD_ID) {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_MAIN_LOCAL_GOV"));
		parameterJSON.put("AREA_CODE", new JSONString(key));
		parameterJSON.put("OTD_ID", new JSONString(OTD_ID));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				if (processResult.equals("success")) {
					infoObject = resultObj.get("body").isObject().get("result").isObject();
					if (infoObject.toString().equals("{}")) {
						
						String LOG_ID = IDUtil.uuid();
						JSONArray banner = new JSONArray();
						for (int i=0; i<3; i++) {
							JSONObject index0 = new JSONObject();
							index0.put("CONTENT_TYPE_NAME", new JSONString("TEXT"));
							index0.put("TITLE", new JSONString("TITLE"));
							index0.put("LINK_TYPE", new JSONNumber(0));
							index0.put("LOG_ID", new JSONString(LOG_ID));
							index0.put("LINK_URL", new JSONString("http://m.visitkorea.or.kr"));
							index0.put("IMG_ID", new JSONString("a99c43ca-0cb8-482d-82e6-9454f983bb69"));
							banner.set(i, index0);
						}
						
						infoObject.put("CREATE_DATE", new JSONString("2018-08-13 22:36:11.0"));
						infoObject.put("CONNECT_URL", new JSONString(""));
						infoObject.put("SUB_TITLE", new JSONString(""));
						infoObject.put("INFORMATION", new JSONString(""));
						infoObject.put("BANNER_STYLE", new JSONNumber(3));
						infoObject.put("IMG_ID", new JSONString("a99c43ca-0cb8-482d-82e6-9454f983bb69"));
						infoObject.put("COT_ID", new JSONString(""));
						infoObject.put("TITLE", new JSONString(""));
						infoObject.put("AREA_CODE", new JSONNumber(Integer.parseInt(key)));
						infoObject.put("LOG_ID", new JSONString(LOG_ID));
						infoObject.put("LOG_IMG_ID", new JSONString("a99c43ca-0cb8-482d-82e6-9454f983bb69"));
						infoObject.put("LINK_URL", new JSONString(""));
						infoObject.put("banner", banner);
						
					}
					
				}
			}
		});
		
	};

	public void getInformation(AreaLinkCallback linkCallback, String AREA_CODE) {
		String OTD_ID = "";
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_MAIN_LOCAL_GOV"));
		parameterJSON.put("SIGUNGU_CODE", new JSONString(key));
		parameterJSON.put("AREA_CODE", new JSONString(AREA_CODE));
		parameterJSON.put("OTD_ID", new JSONString(OTD_ID));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				if (processResult.equals("success")) {
					infoObject = resultObj.get("body").isObject().get("result").isObject();
					if (infoObject.toString().equals("{}")) {
						
						String LOG_ID = IDUtil.uuid();
						JSONArray banner = new JSONArray();
						for (int i=0; i<3; i++) {
							JSONObject index0 = new JSONObject();
							index0.put("CONTENT_TYPE_NAME", new JSONString("TEXT"));
							index0.put("TITLE", new JSONString("TITLE"));
							index0.put("LINK_TYPE", new JSONNumber(0));
							index0.put("LOG_ID", new JSONString(LOG_ID));
							index0.put("LINK_URL", new JSONString("http://m.visitkorea.or.kr"));
							index0.put("IMG_ID", new JSONString(""));
							banner.set(i, index0);
						}
						
						infoObject.put("CREATE_DATE", new JSONString("2018-08-13 22:36:11.0"));
						infoObject.put("CONNECT_URL", new JSONString(""));
						infoObject.put("SUB_TITLE", new JSONString(""));
						infoObject.put("INFORMATION", new JSONString(""));
						infoObject.put("IMG_ID", new JSONString(""));
						infoObject.put("COT_ID", new JSONString(""));
						infoObject.put("TITLE", new JSONString(""));
						infoObject.put("AREA_CODE", new JSONNumber(Integer.parseInt(key)));
						infoObject.put("LOG_ID", new JSONString(LOG_ID));
						infoObject.put("LOG_IMG_ID", new JSONString(""));
						infoObject.put("LINK_URL", new JSONString(""));
						infoObject.put("banner", banner);
						
					}
					
					linkCallback.callback(infoObject);
				}
			}
		});
		
		
	}

	public void setLinkUrl(String value) {
		this.linkUrl = value;
	}

	public void save(String areaCode, String sigunguCode, String homepage, String otdId) {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_SIGUNGU_URL"));
		parameterJSON.put("AREA_CODE", new JSONString(areaCode));
		parameterJSON.put("SIGUNGU_CODE", new JSONString(sigunguCode));
		parameterJSON.put("HOMEPAGE", new JSONString(homepage));
		parameterJSON.put("OTD_ID", new JSONString(otdId));
		
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
		
	}
	
}
