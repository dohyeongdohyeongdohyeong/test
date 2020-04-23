package kr.or.visitkorea.admin.client.manager.widgets.editor.items;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class EditorContentDialog {

	public static enum TYPE{
		CONFIRM, ALERT, CUSTOM
	}

	public static JSONObject buildDiagParameters(String[] titles, String[] headers, String[] bodies) {
		
		JSONObject dialogParameter = new JSONObject();
		dialogParameter.put("title", getJSONArray(titles));
		dialogParameter.put("header", getJSONArray(headers));
		dialogParameter.put("body", getJSONArray(bodies));
		
		return dialogParameter;
		
	}

	private static JSONArray getJSONArray(String[] values) {
		
		JSONArray diagJSONArr = new JSONArray();
		
		for (String value : values) {
			JSONObject valueMember = new JSONObject();
			valueMember.put("value", new JSONString(value));
			diagJSONArr.set(diagJSONArr.size(), valueMember);
		}
		
		return diagJSONArr;
	}

}
