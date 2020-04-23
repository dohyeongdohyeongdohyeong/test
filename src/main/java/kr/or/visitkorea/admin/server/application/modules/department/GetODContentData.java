package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_OD_CONTENT_DATA")
public class GetODContentData extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("COT_ID", parameterObject.getString("cotId"));

		// check image
		HashMap<String, Object> checkImageMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.DepartmentMapper.checkImage" , 
				paramterMap );
		
		//@TODO setup default image
		String IMG_ID = "a9158773-bd4d-49f2-a901-d3a7e204a773";
	
		if (checkImageMap != null) {
			if (checkImageMap.get("FIRST_IMAGE") != null) {
				IMG_ID = (String) checkImageMap.get("FIRST_IMAGE");
			}
			else if (checkImageMap.get("IMG_ID") != null) {
				IMG_ID = (String) checkImageMap.get("IMG_ID");
			}
		}
		
		// ckeckup master tag
		HashMap<String, Object> masterTagMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.DepartmentMapper.getMaseterTag" , 
				paramterMap );
		
		
		String MasterTag = "";
		
		if (checkImageMap != null) {
			MasterTag = (String) checkImageMap.get("MASTER_TAG");
		}
		

		// content master
		HashMap<String, Object> contentMasterMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.DepartmentMapper.getContentInfo" , 
				paramterMap );

		JSONObject contentMasterObj = new JSONObject(contentMasterMap);
		
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("IMG_ID", IMG_ID);
		resultObj.put("MASTER_TAG", MasterTag);
		resultObj.put("CONTENT", contentMasterObj);
		
		String convertJSONString = resultObj.toString();

		if (convertJSONString.equals("null")) {
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
