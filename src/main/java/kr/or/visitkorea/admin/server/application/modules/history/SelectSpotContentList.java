package kr.or.visitkorea.admin.server.application.modules.history;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.json.client.JSONNumber;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


/**
 * 
 * history 추가에 사용되는 컨텐츠들(추천, 여행지, 축제 컨텐츠)의 목록을 불러와서 전달하는 모듈.
 * 
 * @author 		dohyeong
 * @authorDate	2020-03-20
 * @see			kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.HistoryManagementDialog
 *
 */
@BusinessMapping (id="GET_SPOT_LIST")
public class SelectSpotContentList extends AbstractModule {
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		
		final String mapperLocation = "kr.or.visitkorea.system.HistoryMapper";
		final String selectSpotList = "spotContentList";
		final String selectSpotDataAmount = "spotContentListAmount";
		final String selectAlreadyAddAmount = "alreadyAddSpotAmount";
		
		
		
		HashMap<String, Object> sqlArguments = null;
		if(super.parameterObject.has("data")) {
			JSONObject jsonData = super.parameterObject.getJSONObject("data");			
			sqlArguments = convertJSONToMap(jsonData);
		}
		
		
		
		int dataAmount = super.sqlSession.selectOne(mapperLocation + "." + selectSpotDataAmount, sqlArguments);
		int alreadyAddSpotAmount = super.sqlSession.selectOne(mapperLocation + "." + selectAlreadyAddAmount, sqlArguments);
		
		List<HashMap<String, Object>> spotList = null;
		if(dataAmount == 0 && alreadyAddSpotAmount == 0) {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "검색된 컨텐츠 데이터가 없습니다.");
			
			return;
		}
		
		spotList = super.sqlSession.selectList(mapperLocation + "." + selectSpotList, sqlArguments);
		JSONArray resultData = new JSONArray(spotList);
		
		JSONObject result = new JSONObject();
		if(dataAmount != 0) {
			result.put("resultData", resultData);
			result.put("resultAmount", dataAmount);
		}
		result.put("excludeAmount", alreadyAddSpotAmount);
		
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
