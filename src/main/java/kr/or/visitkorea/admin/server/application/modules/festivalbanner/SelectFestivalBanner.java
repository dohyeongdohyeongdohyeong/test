package kr.or.visitkorea.admin.server.application.modules.festivalbanner;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.FestivalBannerMapper
@BusinessMapping(id="SELECT_FESTIVAL_BANNER")
public class SelectFestivalBanner extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String cotId;
		try {
			cotId = parameterObject.getString("cotId");
		} catch(JSONException e) {
			cotId = null;
			// ignore. handles below.
		}
		
		if (cotId == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "BAD REQUEST. cotId is empty." );
			return;
		}
		
		HashMap<String, Object> returnMap = sqlSession.selectOne(
				"kr.or.visitkorea.system.FestivalBannerMapper.select",
				cotId);
		
		JSONObject json = new JSONObject(returnMap);
		String convertJSONString = json.toString();
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", new JSONObject(convertJSONString));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		System.out.println(">>> resultHeaderObject : " + resultHeaderObject.toString());
		System.out.println(">>> resultBodyObject : " + resultBodyObject.toString());
	}
}
