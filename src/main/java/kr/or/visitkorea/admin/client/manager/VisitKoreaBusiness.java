package kr.or.visitkorea.admin.client.manager;

import java.io.IOException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;

public class VisitKoreaBusiness {

	public static void post(String url, String data, Func3<Object, String, Object> success) {

		Console.log(data);
		try {
			
			if (!data.contains("\"cmd\":\"LOGIN\"")) {
				
		    	JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("CHECK_SERVER_SESSION"));
				
				JQuery.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().toString();
						processResult = processResult.replaceAll("\"", "");
						
						if (processResult.equals("success")) {
							if (Registry.globalMap.containsKey(Registry.GLOBAL_SESSION_TIME_LIMIT)) {
								Registry.SESSION_CURRENT_TIME = (int) Registry.globalMap.get(Registry.GLOBAL_SESSION_TIME_LIMIT) * 60;
							}
							JQuery.post(url, data, success);
							
						}else if (processResult.equals("fail")) {
		
							String serviceServer = GWT.getHostPageBaseURL();
							String redirectUrl = "info_page.html";
							
							if (serviceServer.contains("kor.uniess.co.kr") || serviceServer.contains("support.visitkorea.or.kr") || serviceServer.contains("192.168.0.30") || serviceServer.contains("stage.uniess.co.kr")) {
								redirectUrl = serviceServer + "info_page.do";
							}
		
							Window.Location.replace(redirectUrl); 
						
						}
						
					}
				
				});
			
			}else {
				JQuery.post(url, data, success);
			}
			
    	} catch(Exception e) {
    		if(e.getMessage().indexOf("Broken Pipe") == -1) {
    			MaterialToast.fireToast("새로고침 후 다시 시도해주세요");
    		} else {
				String serviceServer = GWT.getHostPageBaseURL();
				String redirectUrl =  "/login.html";
				if (serviceServer.contains("kor.uniess.co.kr") || serviceServer.contains("korean.visitkorea.or.kr") || serviceServer.contains("192.168.0.30") || serviceServer.contains("stage.uniess.co.kr")) {
					redirectUrl = serviceServer + "login.do";
				}
				Window.Location.replace(redirectUrl);
    		}
    	}
		
	}
	
	
	
	/**
	 * VisitKorea.post() 비동기 요청 시의 콜백 메서드에서 데이터 처리를 간단히 하기 위한 목적으로 만든 메서드. 
	 * 서버로부터의 응답 여부에 따라서 data를 리턴시켜 주거나, 
	 * data가 없으면 null 값을 리턴시키면서 서버로 부터 받은 오류 메시지를 FireToast 출력 시킴.
	 * 
	 * @author 	dohyeong, 2020-04-17
	 * @param 	(Object) 콜백 메서드의 파라미터 데이터 param1.
	 * @return 	(JSONValue) resultData.
	 */
	public static JSONValue simpleFireToast(Object callbackParam1) {
		JSONValue result = null;
		
		JSONObject resultObject = JSONParser.parseStrict(JSON.stringify((JsObject) callbackParam1)).isObject();
		JSONObject headerObject = resultObject.get("header").isObject();
		JSONObject bodyObject = resultObject.get("body").isObject();
		String processResult = headerObject.get("process").isString().stringValue();
		
		if(! processResult.equals("success")) {
			String message = bodyObject.get("message").isString().stringValue();
			
			MaterialToast.fireToast(message);
		} else {
			result = bodyObject.get("result");
		}
		
		return result;
	}

}
