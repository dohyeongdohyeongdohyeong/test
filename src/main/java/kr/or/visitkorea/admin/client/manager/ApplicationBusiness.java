package kr.or.visitkorea.admin.client.manager;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import gwt.material.design.jquery.client.api.JQuery;

abstract public class ApplicationBusiness implements Cloneable {
	
	private Map<String, Object> params;
	private Func3<Object, String, Object> callback;

	public Map<String, Object> getParams() {
		return params;
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public Func3<Object, String, Object> getCallback() {
		return callback;
	}
	
	public void setCallback(Func3<Object, String, Object> callback) {
		this.callback = callback;
	}
	
	abstract public void invoke();
	
	public void post(JSONObject parameter){
        VisitKoreaBusiness.post("call", parameter.toString(), callback);
	}
	
	public void post(JSONObject parameter, Func3<Object, String, Object> callback ){
        VisitKoreaBusiness.post("call", parameter.toString(), callback);
	}
	
	public Object clone() {
		
		Object retObj = null;
		
		try {
			retObj = this.clone();
		}catch(Exception ex) {
			return null;
		}
		
		return retObj;
		
	}
	
	@Override
	public String toString() {
		return "ApplicationBusiness [params=" + params + ", callback=" + callback + "]";
	}

	public static void post(String bizId, String parameter, Func3<Object, String, Object> callback) {
		
        JSONObject chkSessionParameter = new JSONObject();
        chkSessionParameter.put("cmd", new JSONString("CHECK_SESSION"));
		
		VisitKoreaBusiness.post("call", chkSessionParameter.toString(), (Object param1, String param2, Object param3) -> {
			
            
			JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            
			JSONObject header = (JSONObject) result.get("header");
            
            String processResult = header.get("process").isString().stringValue();

            if (processResult.equals("success")) {
            	
            	VisitKoreaBusiness.post("call", parameter, callback);
            
            }else{
            	
            }
            
		});
      
	}

	private static void notifySessionExpire() {
		
	}

	private static boolean checkSession() {
		
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("CHECK_SESSION"));
		
		VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
			
		});

		return false;
	}
	
}
