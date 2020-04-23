package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_CATCH_PLACE")
public class InsertCatchplace extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String mode = null;
		HashMap<String, Object> parameterMap = new HashMap<>();
		if (this.parameterObject.has("mode"))
			mode = this.parameterObject.getString("mode");
		if (this.parameterObject.has("COT_ID"))
			parameterMap.put("COT_ID", this.parameterObject.get("COT_ID"));
		if (this.parameterObject.has("CONTENTS"))
			parameterMap.put("CONTENTS", this.parameterObject.get("CONTENTS"));
		if (this.parameterObject.has("IS_USE"))
			parameterMap.put("IS_USE", this.parameterObject.get("IS_USE"));
		if (this.parameterObject.has("SELECT_BG"))
			parameterMap.put("SELECT_BG", this.parameterObject.get("SELECT_BG"));
		if (this.parameterObject.has("TEXT_COLOR"))
			parameterMap.put("TEXT_COLOR", this.parameterObject.get("TEXT_COLOR"));
		if (this.parameterObject.has("IS_FLOW"))
			parameterMap.put("IS_FLOW", this.parameterObject.get("IS_FLOW"));
		if (this.parameterObject.has("imgId")) {
			parameterMap.put("imgId", this.parameterObject.getString("imgId"));
			parameterMap.put("cotId", this.parameterObject.get("COT_ID"));
			parameterMap.put("imgPath", this.parameterObject.get("imgPath"));
			
			HashMap<String, Object> deleteImageMap = new HashMap<>();
			deleteImageMap.put("imgId", this.parameterObject.getString("originImgId"));
			
			if (!this.parameterObject.getString("originImgId").equals(this.parameterObject.getString("imgId"))) {
				sqlSession.update("kr.or.visitkorea.system.ImageMapper.setupImageVisiableFalse", deleteImageMap);
				sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", parameterMap);
			}
		}
		
		int result;
		if (mode.equals("add")) 
			result = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertCatchplace", parameterMap);
		else 
			result = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.updateCatchplace", parameterMap);
		
		if (result != 1) {
			resultHeaderObject.put("process", "fail");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
