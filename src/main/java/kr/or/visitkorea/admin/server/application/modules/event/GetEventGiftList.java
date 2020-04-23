package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_EVENT_GIFT_LIST")
public class GetEventGiftList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		List<HashMap<String, Object> > resultList = new ArrayList<HashMap<String, Object> >();
		params.put("subEvtId", this.parameterObject.getString("subEvtId"));
		if(this.parameterObject.has("isNotWin")) {
			params.put("isNotWin", this.parameterObject.getString("isNotWin"));
		}
		resultList = sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventGiftList", params);
		
		
		JSONArray json = new JSONArray(resultList);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", new JSONArray(convertJSONString));
		}

		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
