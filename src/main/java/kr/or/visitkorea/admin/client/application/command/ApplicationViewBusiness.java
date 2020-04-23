package kr.or.visitkorea.admin.client.application.command;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;

public class ApplicationViewBusiness {
	
	public static void fetchAddressBigWithMidCode(ExecuteCallback<JSONArray> callback) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_ADDRESS_BIG_WITH_MID_CODE"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResultObj = resultObj.get("body").isObject().get("result").isArray();
				callback.run(bodyResultObj);
			}
		});
	}
	
	public static void fetchServerInformation(ExecuteCallback<JSONObject> callback) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_IMAGE_SERVER"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResltObj = resultObj.get("body").isObject().get("result").isObject();
				callback.run(bodyResltObj);
			}
		});
	}
	
	public static void fetchPermissionTemplate(ExecuteCallback<JSONObject> callback) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_PERMIT_TEMPLATE"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResltObj = resultObj.get("body").isObject().get("result").isObject();
				callback.run(bodyResltObj);
			}
		});
	}
	
	public static void fetchAddressBigCode(ExecuteCallback<JSONArray> callback) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_ADDRESS_BIG_CODE"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResltObj = resultObj.get("body").isObject().get("result").isArray();
				callback.run(bodyResltObj);
			}
		});
	}
	
	public static void fetchAllCategoryCode(ExecuteCallback<JSONArray> callback) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_ALL_CATEGORIES"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResultObj = resultObj.get("body").isObject().get("result").isArray();
				callback.run(bodyResultObj);
			}
		});
	}
	
	public static void fetchArticleCategoryCode(ExecuteCallback<JSONArray> callback) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_GET_CATEGORIES"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResultObj = resultObj.get("body").isObject().get("result").isArray();
				callback.run(bodyResultObj);
			}
		});
	}

	public static void fetchGlobalVariables(ExecuteCallback<JSONArray> callback) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_GLOBAL_VARIABLES_LIST"));
		VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResultArr = resultObj.get("body").isObject().get("result").isArray();
				callback.run(bodyResultArr);
			}
		});
	}
}
