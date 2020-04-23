package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_EVENT_STATUS")
public class UpdateEventStatus extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (this.parameterObject.has("evtId"))
			params.put("evtId", this.parameterObject.getString("evtId"));
		if (this.parameterObject.has("status"))
			params.put("status", this.parameterObject.getInt("status"));
		
		sqlSession.update("kr.or.visitkorea.system.EventMapper.updateEventStatus", params);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
