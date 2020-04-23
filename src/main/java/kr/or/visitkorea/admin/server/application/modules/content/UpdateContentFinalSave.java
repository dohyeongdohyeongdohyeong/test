package kr.or.visitkorea.admin.server.application.modules.content;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_CONTENT_FINAL_SAVE")
public class UpdateContentFinalSave extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		else {
			resultHeaderObject.put("process", "fail");
			return;
		}
		
		
		int result = sqlSession.update("kr.or.visitkorea.system.ContentMasterMapper.updateContentFinalSave", params);
	
		if(result > 1)
		resultHeaderObject.put("process", "success");
		else
		resultHeaderObject.put("process", "fail");
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
