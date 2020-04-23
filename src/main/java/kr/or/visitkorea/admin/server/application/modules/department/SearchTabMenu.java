package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.FestivalBannerMapper
@BusinessMapping(id="SEARCH_TAB_MENU")
public class SearchTabMenu extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String keyword = parameterObject.getString("keyword");
		
		if (keyword == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "BAD REQUEST. search condition is empty." );
			return;
		}
		
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("keyword", keyword);
		paramterMap.put("otdId", parameterObject.getString("otdId"));
		
		Integer searchCount = sqlSession.selectOne(
				"kr.or.visitkorea.system.DepartmentMapper.searchTabMenu", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("count", searchCount);
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
	}
}
