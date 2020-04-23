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
@BusinessMapping(id="UPDATE_IMAGE_PATH_WITH_ODMID")
public class UpdateImagePathWithOdmId extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("ODM_ID", parameterObject.getString("odmId"));
		if (parameterObject.has("imgId")) {
			paramterMap.put("IMG_ID", parameterObject.getString("imgId"));
		}
		if (parameterObject.has("LINK_URL")) {
			paramterMap.put("LINK_URL", parameterObject.getString("LINK_URL")); 
		}
			
		int insertResult = sqlSession.insert("kr.or.visitkorea.system.DepartmentMapper.updateImageIdWithOdmId", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("insertResult", insertResult);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			paramterMap.put("imgId", parameterObject.getString("imgId"));
			sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateForModifiedDateImg" , paramterMap );
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
