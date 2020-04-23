package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_CATCH_WITH_COTID")
public class GetCatchWithCotId extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> parameterMap = new HashMap<>();
		if (parameterObject.has("cotId"))
			parameterMap.put("cotId", parameterObject.getString("cotId"));

		HashMap<String, Object> result = sqlSession.selectOne(
				"kr.or.visitkorea.system.DatabaseMapper.selectCatchplaceWithCotId", parameterMap);
		
		JSONObject json = new JSONObject(result);
		
		if (json.length() == 0) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", json);
		}
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
