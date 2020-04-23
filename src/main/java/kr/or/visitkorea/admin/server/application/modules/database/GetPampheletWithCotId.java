package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_PAMPHELET_WITH_COTID")
public class GetPampheletWithCotId extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		
		List<HashMap<String, Object>> result =
				sqlSession.selectList("kr.or.visitkorea.system.DatabaseMapper.selectPamphletWithCotId", params);
		
		result.forEach(item -> {
			HashMap<String, Object> params2 = new HashMap<>();
			params2.put("pplId", item.get("PPL_ID").toString());
			
			List<HashMap<String, Object>> resultContents =
					sqlSession.selectList("kr.or.visitkorea.system.DatabaseMapper.getPamphletContents", params2);
			item.put("CONTENTS", resultContents);
		});
		
		JSONArray resultArr = new JSONArray(result);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
