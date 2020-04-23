package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */

@BusinessMapping(id="INSERT_COURSE_MAIN")
public class InsertCourseMain extends AbstractModule{

	private String CONTENT_TITLE;
	private String COURSE_DESCRIPTION;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		boolean deleteflag = true;
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();

		// processing contents
		JSONArray contentArray = parameterObject.getJSONArray("CONTENTS");
		int arrayLength = contentArray.length();

		for (int i=0; i<arrayLength; i++) {
			
			JSONObject contentsObj = contentArray.getJSONObject(i);

			int COMP_ORDER = contentsObj.getInt("COMP_ORDER");
			int MAIN_TYPE = 2;
			String TITLE = "";
			String SUB_TITLE = "";
			String COMP_TYPE = contentsObj.getString("COMP_TYPE");
			
			TITLE = contentsObj.getString("TITLE");
			SUB_TITLE = contentsObj.getString("SUB_TITLE");
			
			String COMP_ID = contentsObj.getString("COMP_ID");
			
			parameterMap.put("COMP_ID", COMP_ID);
			parameterMap.put("COMP_TYPE", COMP_TYPE);
			parameterMap.put("COMP_ORDER", COMP_ORDER);
			parameterMap.put("MAIN_TYPE", MAIN_TYPE);
			parameterMap.put("TITLE", TITLE);
			parameterMap.put("SUB_TITLE", SUB_TITLE);
			
			if (deleteflag) {
				sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteSubMain", parameterMap);
				sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteCourseMain");
				deleteflag = false;
			}
			
			sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertSubMain", parameterMap);
			
			JSONArray cArray = contentsObj.getJSONArray("CONTENTS");
			int cLength = cArray.length();

			String MAN_ID = "11260f3d-a30a-11e8-8165-020027310001";
			parameterMap.put("MAN_ID", MAN_ID);
			
			int contentOrder = 0;
			for (int idx=0; idx<cLength; idx++) {

				if (cArray.get(idx) != null && cArray.get(idx) instanceof JSONObject) {

					JSONObject jObj = cArray.getJSONObject(idx);

					parameterMap.put("CONTENT_ORDER", contentOrder);
					if (jObj.has("COT_ID") && jObj.get("COT_ID").toString().trim().length() > 0) {
						parameterMap.put("COT_ID", jObj.getString("COT_ID"));
					}else {
						parameterMap.put("COT_ID", null);
					}
					
					if (jObj.has("CRS_ID") && jObj.get("CRS_ID").toString().trim().length() > 0) {
						parameterMap.put("CRS_ID", jObj.getString("CRS_ID"));
					}else {
						parameterMap.put("CRS_ID", null);
					}
					
					if (jObj.has("LINK_URL")) {
						parameterMap.put("LINK_URL", jObj.getString("LINK_URL").replaceAll("\"", ""));
					}else {
						parameterMap.put("LINK_URL", null);
					}
					
					if (!jObj.has("TITLE")) {
						jObj.put("TITLE", "");
					}
					
					sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertCourseMain", parameterMap);
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
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
