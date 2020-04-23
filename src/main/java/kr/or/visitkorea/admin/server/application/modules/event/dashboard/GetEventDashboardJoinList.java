package kr.or.visitkorea.admin.server.application.modules.event.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_EVENT_DASHBOARD_JOIN_LIST")
public class GetEventDashboardJoinList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("subEvtId"))
			params.put("subEvtId", this.parameterObject.getString("subEvtId"));
		
		
		List<HashMap<String, Object>> resultMap =
				sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventDashboardJoinList", params);

		
		JSONArray resultArr = new JSONArray(resultMap);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
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
