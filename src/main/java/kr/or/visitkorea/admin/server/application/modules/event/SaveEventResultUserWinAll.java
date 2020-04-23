package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_USER_WIN_ALL")
public class SaveEventResultUserWinAll extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		try {
			
			HashMap<String, Object> params = new HashMap<>();
			params.put("evtId", this.parameterObject.getString("evtId"));
			sqlSession.delete("kr.or.visitkorea.system.EventResultMapper.deleteEventUserWin", params);
			
			JSONArray array =  parameterObject.getJSONArray("model");
			
			for(Object obj : array) {
				params = new HashMap<>();
				params.put("evtId", ((JSONObject)obj).getString("evtId"));
				params.put("subEvtId", ((JSONObject)obj).getString("subEvtId"));
				params.put("eeuId", ((JSONObject)obj).getString("eeuId"));
				params.put("gftId", ((JSONObject)obj).getString("gftId"));
				params.put("eapId", ((JSONObject)obj).getString("eapId"));
				sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.saveEventUserWin", params);
			}
		
		}catch(Exception e) {
			resultHeaderObject.put("process", "fail");
			e.printStackTrace();
		}
		/*
		
		params.put("subEvtId", this.parameterObject.getString("subEvtId"));
		params.put("eeuId", this.parameterObject.getString("eeuId"));
		params.put("gftId", this.parameterObject.getString("gftId"));
		params.put("eapId", this.parameterObject.getString("eapId"));

		int result1 = sqlSession.update("kr.or.visitkorea.system.EventResultMapper.saveEventUserWin", params);
		
		if (result1 != 1 ) {
			resultHeaderObject.put("process", "fail");
		}
		 */
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
	
}
