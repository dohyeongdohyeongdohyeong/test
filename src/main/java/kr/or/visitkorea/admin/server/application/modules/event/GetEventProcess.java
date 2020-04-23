package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EVENT_PROCESS")
public class GetEventProcess extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("evtId"))
			params.put("evtId", this.parameterObject.getString("evtId"));
		
		List<HashMap<String, Object>> resultEventInfo = 
				sqlSession.selectList("kr.or.visitkorea.system.EventMapper.getSubEventInfo", params);
		List<HashMap<String, Object>> resultTypeInfo =
				sqlSession.selectList("kr.or.visitkorea.system.EventMapper.getEventTypeCode", params);
		
		JSONArray resultInfoArr = new JSONArray(resultEventInfo);
		JSONArray resultTypeArr = new JSONArray(resultTypeInfo);
		
		for (int i = 0; i < resultInfoArr.length(); i++) {
			JSONObject resultInfoObj = resultInfoArr.getJSONObject(i);
			
			params.put("subEvtId", resultInfoObj.getString("SUB_EVT_ID"));
			List<HashMap<String, Object>> resultEventOXQuiz = 
					sqlSession.selectList("kr.or.visitkorea.system.EventMapper.getOXQuizComponent", params);

			JSONArray resultOXQuizArr = new JSONArray(resultEventOXQuiz);
			resultInfoObj.put("OXQUIZS", resultOXQuizArr);
		}
		
		for (int i = 0; i < resultInfoArr.length(); i++) {
			JSONObject resultInfoObj = resultInfoArr.getJSONObject(i);
			
			params.put("subEvtId", resultInfoObj.getString("SUB_EVT_ID"));
			List<HashMap<String, Object>> resultEventGift = 
					sqlSession.selectList("kr.or.visitkorea.system.EventMapper.getSubEventGift", params);

			JSONArray resultGiftArr = new JSONArray(resultEventGift);
			resultInfoObj.put("GIFTS", resultGiftArr);
		}
		
		String convertJSONString = resultInfoArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", resultInfoArr);
			resultBodyObject.put("resultType", resultTypeArr);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
