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
@BusinessMapping(id="INSERT_RECOMMAND_MAIN")
public class InsertRecommandMain extends AbstractModule{

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
			System.out.println("InsertRecommandMain :: "+contentsObj.toString());

			int COMP_ORDER = contentsObj.getInt("COMP_ORDER");
			int MAIN_TYPE = 0;
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
			
			if (deleteflag) {
				sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteSubMain", parameterMap);
				sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteRecommandMain");
				deleteflag = false;
			}
			
			sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertSubMain", parameterMap);
			
			JSONArray cArray = contentsObj.getJSONArray("CONTENTS");
			int cLength = cArray.length();

			String MAN_ID = "0d77108d-a30a-11e8-8165-020027310001";
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
					parameterMap.put("IMG_ID", jObj.getString("IMG_ID"));
					
					sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertRecommandMain", parameterMap);
					contentOrder++;
				}
			}
			// insert all records
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
