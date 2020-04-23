package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EVENT_USER_WIN_LIST")
public class GetEventUserWinList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		List<HashMap<String, Object> > resultList = new ArrayList<HashMap<String, Object> >();
		params.put("subEvtId", this.parameterObject.getString("subEvtId"));
		params.put("gftId", this.parameterObject.getString("gftId"));
		params.put("eapId", this.parameterObject.getString("eapId"));
		if(this.parameterObject.has("tel")) {
			params.put("tel", this.parameterObject.getString("tel"));
		}
		resultList = sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventUserWinList", params);
		
		
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
