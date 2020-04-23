package kr.or.visitkorea.admin.server.application.modules.foodApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EXCEPTION_LIST")
public class SelectExceptionList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String mapperLocation = "kr.or.visitkorea.system.FoodApiMapper";
		String dataListSql = "ExceptionList";
		String dataCountingSql = "ExceptionListAmount";
		
		
		if(super.parameterObject.isNull("data")) {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "검색 파라미터 데이터가 제대로 전달되지 않습니다.");
			return;
		}
		
		
		JSONObject argumentData = super.parameterObject.getJSONObject("data");
		Map<String, Object> sqlArgumentData = argumentData.toMap();
		JSONObject result = new JSONObject();

		
		if(argumentData.getInt("offset") == 0) {
			int sqlResultAmount = super.sqlSession.selectOne(mapperLocation + "." + dataCountingSql, sqlArgumentData);
			if(sqlResultAmount == 0) {
				resultHeaderObject.put("process", "fail");
				resultBodyObject.put("message", "검색된 데이터가 없습니다.");
				return;
			} else {
				result.put("resultAmount", sqlResultAmount);
			}
		}
			
			
		List<HashMap<String, Object>> sqlResult = super.sqlSession.selectList(mapperLocation + "." + dataListSql, sqlArgumentData);
		JSONArray resultData = new JSONArray(sqlResult);
		result.put("resultData", resultData);
		
		
		resultBodyObject.put("result", result);
	}
	
	
	
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

}
