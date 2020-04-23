package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_OTHER_DEPARTMENT_COMPONENT")
public class UpdateOtherDepartmentComponent extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if (parameterObject.has("cotId")) paramterMap.put("cotId", parameterObject.getString("cotId"));
		paramterMap.put("imgId", parameterObject.getString("imgId"));
		paramterMap.put("imgPath", parameterObject.getString("imgPath"));
		paramterMap.put("imgDescription", parameterObject.getString("imgDesc")); 
		
		paramterMap.put("ODM_ID", parameterObject.getString("odmId"));
		if (parameterObject.has("imgId")) {
			paramterMap.put("IMG_ID", parameterObject.getString("imgId"));
		}
		if (parameterObject.has("LINK_URL")) {
			paramterMap.put("LINK_URL", parameterObject.getString("LINK_URL")); 
		}
		
		int imgResult = sqlSession.update("kr.or.visitkorea.system.ImageMapper.insertWithCotId", paramterMap);
		
		int odmResult = sqlSession.insert("kr.or.visitkorea.system.DepartmentMapper.updateImageIdWithOdmId", paramterMap);
		
		
		JSONObject json = new JSONObject();
		json.put("imgResult", imgResult);
		json.put("odmResult", odmResult);
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
