package kr.or.visitkorea.admin.server.application.modules.history;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


/**
 * 
 * history 검색 키워드에 따라 해당 정보를 받아서 전달하여 주는 모듈.
 * 
 * @author 		dohyeong
 * @authorDate	2020-03-20
 * @see			kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.HistoryManagementDialog
 *
 */
@BusinessMapping (id="GET_HISTORY_LIST")
public class SelectHistoryList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		
		final String mapperLocation = "kr.or.visitkorea.system.HistoryMapper";
		final String selectListSqlId = "historyList";
		final String selectCountSqlId = "historyListAmount";
		
		
		
		HashMap<String, Object> sqlArguments = null;
		if(super.parameterObject.has("data")) {
			JSONObject parameterData = super.parameterObject.getJSONObject("data");
			sqlArguments = convertJSONToMap(parameterData);
		}
		
		
		int dataAmount = super.sqlSession.selectOne(mapperLocation + "." + selectCountSqlId, sqlArguments);
		if(dataAmount == 0) {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "저장된 히스토리 데이터가 없습니다.");
			
			return;
		}
		
		
		List<HashMap<String, Object>> historyList = super.sqlSession.selectList(mapperLocation + "." +selectListSqlId, sqlArguments);
		
		
		JSONObject result = new JSONObject();
		result.put("resultAmount", dataAmount);
		result.put("resultData", historyList);
		
		
		resultBodyObject.put("result", result);
	}
	
	
	/**
	 * JSON 형식의 데이터를 HashMap<key, value> 쌍의 데이터 형식으로 변환하여 반환하는 메서드.
	 * 
	 * @param JSONObject
	 * @return HashMap<String, Object>
	 */
	private HashMap<String, Object> convertJSONToMap(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			Object value = jsonObject.get(key);
			
			map.put(key, value);
		}
		
		return map;
	}
	
	
	
	
	
	

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

}
