package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_USER_WIN")
public class SaveEventResultUserWin extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("evtId", this.parameterObject.getString("evtId"));
		params.put("subEvtId", this.parameterObject.getString("subEvtId"));
		params.put("eeuId", this.parameterObject.getString("eeuId"));
		params.put("gftId", this.parameterObject.getString("gftId"));
		params.put("eapId", this.parameterObject.getString("eapId"));
		
		int result1 = 0;
		if("insert".equals(this.parameterObject.getString("mode"))) {
			result1 = sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.saveEventUserWin", params);
		}else {
			result1 = sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.deleteEventUserWin", params);
		}
		if (result1 != 1 ) {
			resultHeaderObject.put("process", "fail");
		}
	
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
	
}
