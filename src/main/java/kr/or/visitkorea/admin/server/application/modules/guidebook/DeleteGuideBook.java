package kr.or.visitkorea.admin.server.application.modules.guidebook;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.GuideBookMapper
@BusinessMapping(id="DELETE_GUIDE_BOOK")
public class DeleteGuideBook extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String gbId;
		try {
			gbId = parameterObject.getString("gbId");
		} catch(JSONException e) {
			gbId = null;
			// ignore. handles below.
		}
		
		if (gbId == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "BAD REQUEST. gbId is empty." );
			return;
		}
		
		int result = sqlSession.delete("kr.or.visitkorea.system.GuideBookMapper.delete", gbId);
		if (result == 1) {
			resultBodyObject.put("result", true);
		} else {
			failure(resultHeaderObject, "Occurred an error in the middle of insert operation.");
		}
	}
	
	private void failure(JSONObject result, String message) {
		result.put("process", "fail");
		result.put("ment", message);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
