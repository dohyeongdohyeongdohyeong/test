package kr.or.visitkorea.admin.server.application.modules.festivalbanner;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.FestivalBannerMapper
@BusinessMapping(id="DELETE")
public class DeleteFestivalBanner extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String cotId;
		try {
			cotId = parameterObject.getString("cotid");
		} catch(JSONException e) {
			cotId = null;
			// ignore. handles below.
		}
		
		if (cotId == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "BAD REQUEST. cotid is empty." );
			return;
		}
		
		int result = sqlSession.delete("kr.or.visitkorea.system.FestivalBannerMapper.delete", cotId);
		if (result == 1) {
			resultBodyObject.put("result", true);
		} else {
			failure(resultHeaderObject, "Occured an error in the middle of insert opperation.");
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
