package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_IMAGE_NOT_COTID")
public class InsertImageNotCotId extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
 
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("imgId"))
			paramterMap.put("imgId", parameterObject.getString("imgId"));
		if(parameterObject.has("imgPath"))
			paramterMap.put("imgPath", parameterObject.getString("imgPath"));
		paramterMap.put("imgDescription", parameterObject.getString("imgDesc")); 

		int insertResult = sqlSession.update("kr.or.visitkorea.system.ImageMapper.insertNotCotId", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("insertResult", insertResult);
		
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
