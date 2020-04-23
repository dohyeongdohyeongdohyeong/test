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
@BusinessMapping(id="INSERT_SM_CURATION")
public class InsertSMCuration extends AbstractModule{

	private String CONTENT_TITLE;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		String MAN_ID = (String) parameterObject.get("MAN_ID");
		paramterMap.put("MAN_ID", MAN_ID);
		
		// delete all SHOWCASE records
		sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteAllCuration", paramterMap);
		
		// insert SHOWCASE
		JSONArray jArray = parameterObject.getJSONArray("CONTENT");
		String USR_ID = parameterObject.getString("USR_ID");
		
		int arrayLength = jArray.length();
		int insertResult = -1;
		
		if (USR_ID != null) { paramterMap.put("CREATE_USR_ID", USR_ID); }

		for (int i=0; i<arrayLength; i++) {
			
			JSONObject recObj= jArray.getJSONObject(i);
			

			if (recObj.has("COT_ID") && recObj.getString("COT_ID").length() > 0) {
				paramterMap.put("COT_ID", recObj.getString("COT_ID"));
		    }

			if (recObj.has("DISPLAY_TITLE") && recObj.getString("DISPLAY_TITLE").length() > 0) {
				paramterMap.put("DISPLAY_TITLE", recObj.getString("DISPLAY_TITLE"));
		    }

			if (recObj.has("DISPLAY_HEADER_YN")) {
				paramterMap.put("DISPLAY_HEADER_YN", recObj.getInt("DISPLAY_HEADER_YN"));
		    }

			if (recObj.has("DISPLAY_HEADER_COLOR") && recObj.getString("DISPLAY_HEADER_COLOR").length() > 0) {
				paramterMap.put("DISPLAY_HEADER_COLOR", recObj.getString("DISPLAY_HEADER_COLOR"));
			}

			if (recObj.has("DISPLAY_HEADER_TITLE") && recObj.getString("DISPLAY_HEADER_TITLE").length() > 0) {
				paramterMap.put("DISPLAY_HEADER_TITLE", recObj.getString("DISPLAY_HEADER_TITLE"));
			}
			
			if (recObj.has("LINK_URL") && recObj.getString("LINK_URL").length() > 0) {
				paramterMap.put("LINK_URL", recObj.getString("LINK_URL"));
			}

			paramterMap.put("CONTENT_ORDER", i);
			
//			if (recObj.has("CONTENT_ORDER")) {
//				paramterMap.put("CONTENT_ORDER", recObj.getInt("CONTENT_ORDER"));
//			}

			insertResult =  sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertCuration",  paramterMap);
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("insertResults", insertResult);

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
