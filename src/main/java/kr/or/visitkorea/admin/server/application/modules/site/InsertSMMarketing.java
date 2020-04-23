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
@BusinessMapping(id="INSERT_SM_MARKETING")
public class InsertSMMarketing extends AbstractModule{


	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> initMarketingMap = new HashMap<String, Object>();
		initMarketingMap.put("MAN_ID", parameterObject.get("MAN_ID"));
		
		// delete all SHOWCASE records
		sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteAllMarketing", initMarketingMap);
		
		// insert MARKETING
		JSONArray jArray = parameterObject.getJSONArray("CONTENT");
		String USR_ID = parameterObject.getString("USR_ID");
		
		int arrayLength = jArray.length();
		int insertResult = -1;

		String MAN_ID = parameterObject.getString("MAN_ID");
		
		for (int i=0; i<arrayLength; i++) {
			
			String CGP_ID = "";
			
			JSONObject cgpRecObj= jArray.getJSONObject(i);

			JSONArray cgpMemberArray = cgpRecObj.getJSONArray("result");
			CGP_ID = cgpRecObj.getString("CGP_ID");
			
			for (int idex=0; idex<cgpMemberArray.length(); idex++) {
	
				HashMap<String, Object> paramterMap = new HashMap<String, Object>();
				paramterMap.put("MAN_ID", MAN_ID);
				paramterMap.put("CGP_ID", CGP_ID);
				if (USR_ID != null) { paramterMap.put("CREATE_USR_ID", USR_ID); }
			
				JSONObject recObj= cgpMemberArray.getJSONObject(idex);

			
				if (recObj.has("COT_ID") && recObj.getString("COT_ID").length() > 0) {
					paramterMap.put("COT_ID", recObj.getString("COT_ID"));
			    }
	
				if (recObj.has("title") && recObj.getString("title").length() > 0) {
					paramterMap.put("TITLE", recObj.getString("title"));
			    }
				
				if (recObj.has("url") && recObj.getString("url").length() > 0) {
					paramterMap.put("LINK_URL", recObj.getString("url").trim());
				}
				
				if (recObj.has("imageId") && recObj.getString("imageId").length() > 0) {
					paramterMap.put("IMG_ID", recObj.getString("imageId"));
				}
	
				if (recObj.has("contentOrder")) {
					paramterMap.put("CONTENT_ORDER", recObj.getInt("contentOrder"));
				}
	
				insertResult =  sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertMarketing",  paramterMap);
				
			}
			
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
