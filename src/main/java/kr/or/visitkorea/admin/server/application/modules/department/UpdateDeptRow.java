package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDARTE_DEPT_ROW")
public class UpdateDeptRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("COT_ID", parameterObject.getString("cotId"));
		paramterMap.put("ODM_ID", parameterObject.getString("odmId"));
		paramterMap.put("COMP_ID", parameterObject.getString("compId"));
			
		Map<String, String> imageList = sqlSession.selectOne("kr.or.visitkorea.system.DepartmentMapper.checkImage", paramterMap);
		
		String IMG_ID = "";
		
		if (imageList != null) {
			if ( imageList.get("FIRST_IMAGE") != null ) IMG_ID =  imageList.get("FIRST_IMAGE");
			else if ( imageList.get("IMG_ID") != null ) IMG_ID =  imageList.get("IMG_ID");
		}
		if (IMG_ID == null) IMG_ID = "";
		paramterMap.put("IMG_ID", IMG_ID);
		
		Map<String, String> retMap = sqlSession.selectOne("kr.or.visitkorea.system.DepartmentMapper.getContentInfo", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("ODM_ID", paramterMap.get("ODM_ID"));
		json.put("COT_ID", paramterMap.get("COT_ID"));
		json.put("COMP_ID", paramterMap.get("COMP_ID"));
		json.put("IMG_ID", IMG_ID);
		json.put("CONTENT_TITLE", retMap.get("TITLE"));
		json.put("CREATE_DATE", retMap.get("CREATE_DATE"));
		
		paramterMap.put("CONTENT_TITLE", retMap.get("TITLE"));
			
		int updateResult = sqlSession.update("kr.or.visitkorea.system.DepartmentMapper.updateFirstLine", paramterMap);
		
		json.put("updateResult", updateResult);
		
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			json.put("cotId", paramterMap.get("COT_ID"));
			sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateForModifiedDate" , paramterMap );
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
