package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EVENT_ANNOUNCE_PAGE")
public class GetEventAnnouncePageList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		List<HashMap<String, Object> > resultList = new ArrayList<HashMap<String, Object> >();
		params.put("evtId", this.parameterObject.getString("evtId"));
		resultList = sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventAnnouncePageList", params);
		
		Map<String, Object> resultMap =  sqlSession.selectOne("kr.or.visitkorea.system.EventResultMapper.selectEventMaster", params);
		
		
		JSONArray json = new JSONArray(resultList);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", new JSONArray(convertJSONString));
			resultBodyObject.put("resultMaster", new JSONObject(resultMap));
		}

		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
