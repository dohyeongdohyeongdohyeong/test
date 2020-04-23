package kr.or.visitkorea.admin.server.application.modules.guidebook;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.GuideBookMapper
@BusinessMapping(id="SELECT_GUIDE_BOOK")
public class SelectGuideBook extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();

		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		if (this.parameterObject.has("rowCount"))
			params.put("rowCount", this.parameterObject.getInt("rowCount"));
		if (this.parameterObject.has("themeCode") && !this.parameterObject.getString("themeCode").equals(""))
			params.put("themeCode", this.parameterObject.getString("themeCode"));
		if (this.parameterObject.has("areaCode") && !this.parameterObject.getString("areaCode").equals(""))
			params.put("areaCode", this.parameterObject.getString("areaCode"));
		if (this.parameterObject.has("sigunguCode") && !this.parameterObject.getString("sigunguCode").equals(""))
			params.put("sigunguCode", this.parameterObject.getString("sigunguCode"));
		if (this.parameterObject.has("keyword"))
			params.put("keyword", this.parameterObject.getString("keyword"));

		List<HashMap<String, Object>> returnMap = sqlSession.selectList(
				"kr.or.visitkorea.system.GuideBookMapper.select",
				params);

		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", new JSONArray(convertJSONString));
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
