package kr.or.visitkorea.admin.server.application.modules.event.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.client.manager.event.dashboard.panel.IEventConstant;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_EVENT_DASHBOARD_USER_LIST")
public class GetEventDashboardUserList extends AbstractModule implements IEventConstant {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("limit", DEFAULT_PAGE_NUM_ROWS);

		if (this.parameterObject.has("startDate") && !this.parameterObject.getString("startDate").equals(""))
			params.put("startDate", this.parameterObject.getString("startDate"));
		if (this.parameterObject.has("endDate") && !this.parameterObject.getString("endDate").equals(""))
			params.put("endDate", this.parameterObject.getString("endDate"));
		if (this.parameterObject.has("searchWhere"))
			params.put("searchWhere", this.parameterObject.getInt("searchWhere"));
		if (this.parameterObject.has("searchWord"))
			params.put("searchWord", this.parameterObject.getString("searchWord"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		if (this.parameterObject.has("adminPermission"))
			params.put("adminPermission", this.parameterObject.getString("adminPermission"));
		if (this.parameterObject.has("usrId"))
			params.put("usrId", this.parameterObject.getString("usrId"));
		
		List<HashMap<String, Object>> resultMap =
				sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventDashboardUserList", params);
		List<HashMap<String, Object>> resultCnt =
				sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventDashboardUserListCnt", params);

		
		JSONArray resultArr = new JSONArray(resultMap);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultCnt", resultCnt.get(0));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
