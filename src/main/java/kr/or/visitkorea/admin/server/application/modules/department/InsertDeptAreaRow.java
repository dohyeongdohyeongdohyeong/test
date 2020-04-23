package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_DEPT_AREA_ROW")
public class InsertDeptAreaRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		boolean type = true;
		paramterMap.put("COMP_ORDER", parameterObject.getInt("COMP_ORDER"));
		paramterMap.put("VIEW_TITLE", parameterObject.getInt("VIEW_TITLE"));
		paramterMap.put("COT_ORDER", parameterObject.getInt("COT_ORDER"));
		
		paramterMap.put("TEMPLATE_ID", parameterObject.getString("TEMPLATE_ID"));
		paramterMap.put("MAIN_AREA",  parameterObject.getString("MAIN_AREA"));
		paramterMap.put("COMP_ID", parameterObject.getString("COMP_ID"));
		paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		paramterMap.put("TITLE", parameterObject.getString("TITLE")); 
		
		if (parameterObject.has("COT_ID")) {
			paramterMap.put("COT_ID", parameterObject.getString("COT_ID"));
		}
		
		if (parameterObject.has("ODM_ID")) {
			paramterMap.put("ODM_ID", parameterObject.getString("ODM_ID")); 
		}else {
			paramterMap.put("ODM_ID", UUID.randomUUID().toString()); 
		}
		
		if (parameterObject.has("LINK_FILE_URL")) {
			paramterMap.put("LINK_FILE_URL", parameterObject.getString("LINK_FILE_URL")); 
			paramterMap.put("FILE_DESCRIPTION", parameterObject.getString("FILE_DESCRIPTION")); 
		}
		
		if (parameterObject.has("LINK_URL")) {
			paramterMap.put("LINK_URL", parameterObject.getString("LINK_URL")); 
		}
		
		if (parameterObject.has("AREA_CODE")) {
			paramterMap.put("AREA_CODE", parameterObject.getInt("AREA_CODE")); 
		}
		
		if (parameterObject.has("SIGUNGU_CODE")) {
			paramterMap.put("SIGUNGU_CODE", parameterObject.getInt("SIGUNGU_CODE")); 
		}
		
		
		if (parameterObject.has("TYPE")) {
			type = false;
		}
		
		String IMG_ID = null;
		if (parameterObject.has("IMG_ID")) {
			IMG_ID = parameterObject.getString("IMG_ID");
		}else {
			Map<String, String> imageList = sqlSession.selectOne("kr.or.visitkorea.system.DepartmentMapper.checkImage", paramterMap);
			if (imageList != null) {
				if ( imageList.get("FIRST_IMAGE") != null ) IMG_ID =  imageList.get("FIRST_IMAGE");
				else if ( imageList.get("IMG_ID") != null ) IMG_ID =  imageList.get("IMG_ID");
			}
		}
		
		if (IMG_ID != null) paramterMap.put("IMG_ID", IMG_ID);
		
		String mybatisQueryId = "kr.or.visitkorea.system.DepartmentMapper.insertDeptAreaRow";
		if (!parameterObject.has("COT_ID")) {
			mybatisQueryId = "kr.or.visitkorea.system.DepartmentMapper.insertDeptAreaRow2";
		}
		
		// insert other department area content
		int insertResult = sqlSession.insert(mybatisQueryId, paramterMap);
		
		System.out.println("InsertDeptAreaRow.query :: " + mybatisQueryId + " ==> " + insertResult);

		// update area content 
		if(type == true) {
			sqlSession.update("kr.or.visitkorea.system.DepartmentMapper.updateContentTitle", paramterMap);
		}
		Map<String, String> retMap = sqlSession.selectOne("kr.or.visitkorea.system.DepartmentMapper.getContentInfo", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("insertResult", insertResult);
		json.put("ODM_ID", paramterMap.get("ODM_ID"));
		json.put("COT_ID", paramterMap.get("COT_ID"));
		json.put("IMG_ID", IMG_ID);
		if (retMap != null) {
			json.put("CONTENT_TITLE", retMap.get("TITLE"));
			json.put("CREATE_DATE", retMap.get("CREATE_DATE"));
		}
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", json);
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
