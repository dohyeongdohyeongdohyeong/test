package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.regexp.shared.RegExp;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_FESTIVAL_MAIN_MONTHLY")
public class InsertFestivalMainMonthly extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		if (parameterObject.has("MAIN_TYPE")) {
			Map<String, Object> initMap = new HashMap<String, Object>();
			initMap.put("MAIN_TYPE", parameterObject.get("MAIN_TYPE"));
			sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteFestivalMain", initMap);
			sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteSubMain", initMap);
		}
		
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		JSONArray contentArray = parameterObject.getJSONArray("CONTENTS");
		int arrayLength = contentArray.length();
		for (int i=0; i<arrayLength; i++) {
			JSONObject contentsObj = contentArray.getJSONObject(i);
			
			int COMP_ORDER = contentsObj.getInt("COMP_ORDER");
			int MAIN_TYPE = 0;
			MAIN_TYPE = contentsObj.getInt("MAIN_TYPE");
			String COMP_TYPE = contentsObj.getString("COMP_TYPE");
			String TITLE = contentsObj.getString("TITLE");
			String SUB_TITLE = contentsObj.getString("SUB_TITLE");
			String COMP_ID = contentsObj.getString("COMP_ID");
			
			parameterMap.put("COMP_ID", COMP_ID);
			parameterMap.put("COMP_TYPE", COMP_TYPE);
			parameterMap.put("COMP_ORDER", COMP_ORDER);
			parameterMap.put("MAIN_TYPE", MAIN_TYPE);
			parameterMap.put("TITLE", TITLE);
			parameterMap.put("SUB_TITLE", SUB_TITLE);
			
			sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertSubMain", parameterMap);
			
			JSONArray cArray = contentsObj.getJSONArray("CONTENTS");
			int cLength = cArray.length();
			String MAN_ID = "130e3c48-a30a-11e8-8165-020027310001";
			RegExp regExp = RegExp.compile("^[0-9A-Za-z]{8}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{12}$");
			parameterMap.put("MAN_ID", MAN_ID);
			
			int contentOrder = 0;
			for (int idx=0; idx<cLength; idx++) {
				if (cArray.get(idx) != null && cArray.get(idx) instanceof JSONObject) {
					JSONObject jObj = cArray.getJSONObject(idx);
					parameterMap.put("CONTENT_ORDER", contentOrder);
					if (jObj.has("COT_ID")) {
						parameterMap.put("COT_ID", jObj.getString("COT_ID"));
					}else {
						parameterMap.put("COT_ID", null);
					}
					
					if (jObj.has("LINK_URL")) {
						parameterMap.put("LINK_URL", jObj.getString("LINK_URL").replaceAll("\"", ""));
					}else {
						parameterMap.put("LINK_URL", null);
					}
					
					if (!jObj.has("TITLE")) {
						jObj.put("TITLE", "");
					}
					
					parameterMap.put("CTITLE", jObj.getString("TITLE"));
					if (jObj.has("IMG_ID")) {
						
						parameterMap.put("IMG_ID", jObj.getString("IMG_ID"));
					}
					
					sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertFestivalMain", parameterMap);
					contentOrder++;
				}
			}
		}
		
		JSONObject resultObj = new JSONObject();
		if (resultObj.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", resultObj);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
