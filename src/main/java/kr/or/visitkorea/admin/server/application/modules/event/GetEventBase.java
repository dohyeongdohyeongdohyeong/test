package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EVENT_BASE")
public class GetEventBase extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("evtId"))
			params.put("evtId", this.parameterObject.getString("evtId"));
		
		HashMap<String, Object> resultMap = 
				sqlSession.selectOne("kr.or.visitkorea.system.EventMapper.getEventBase", params);
		
		JSONObject resultObj = new JSONObject(resultMap);
		
		if (resultObj.length() == 0) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "fail");
		} else {
			resultBodyObject.put("result", resultObj);
		}
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
