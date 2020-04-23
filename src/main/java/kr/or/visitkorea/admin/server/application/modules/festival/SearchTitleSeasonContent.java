package kr.or.visitkorea.admin.server.application.modules.festival;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.FestivalBannerMapper
@BusinessMapping(id="SEARCH_TITLE_SEASON_CONTENT")
public class SearchTitleSeasonContent extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String keyword = parameterObject.getString("keyword");
		String title = parameterObject.getString("title");
		
		if (keyword == null || title == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "BAD REQUEST. condition is not valid." );
			return;
		}
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("keyword", keyword);
		paramterMap.put("title", title);

		List<HashMap<String, Object>> returnMap = sqlSession.selectList(
				"kr.or.visitkorea.system.FestivalMapper.searchTitleSeasonContent", 
				paramterMap);
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", json);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {}
}
