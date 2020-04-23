package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_EVENT_LIST")
public class SelectEventList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("usrId"))
			params.put("usrId", this.parameterObject.getString("usrId"));
		if (this.parameterObject.has("keyword"))
			params.put("keyword", this.parameterObject.getString("keyword"));
		if (this.parameterObject.has("createDate") && !this.parameterObject.getString("createDate").equals(""))
			params.put("createDate", this.parameterObject.getString("createDate"));
		if (this.parameterObject.has("startDate") && !this.parameterObject.getString("startDate").equals(""))
			params.put("startDate", this.parameterObject.getString("startDate"));
		if (this.parameterObject.has("endDate") && !this.parameterObject.getString("endDate").equals(""))
			params.put("endDate", this.parameterObject.getString("endDate"));
		if (this.parameterObject.has("type"))
			params.put("type", this.parameterObject.getInt("type"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		if (this.parameterObject.has("status"))
			params.put("status", this.parameterObject.getInt("status"));
		if (this.parameterObject.has("adminPermission"))
			params.put("adminPermission", this.parameterObject.getString("adminPermission"));
		if (this.parameterObject.has("userPermission"))
			params.put("userPermission", this.parameterObject.getString("userPermission"));
		
		List<HashMap<String, Object>> resultMap =
				sqlSession.selectList("kr.or.visitkorea.system.EventMapper.selectEventList", params);
		List<HashMap<String, Object>> resultCnt =
				sqlSession.selectList("kr.or.visitkorea.system.EventMapper.selectEventListCnt", params);
		List<HashMap<String, Object>> resultStatusCnt =
				sqlSession.selectList("kr.or.visitkorea.system.EventMapper.selectEventListWithStatusCnt", params);
		
		JSONArray resultArr = new JSONArray(resultMap);
		JSONArray resultStatusCntArr = new JSONArray(resultStatusCnt);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultCnt", resultCnt.get(0));
			resultBodyObject.put("resultStatusCnt", resultStatusCntArr);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
