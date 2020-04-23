package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_ANNOUNCE_PAGE_SORT")
public class SaveEventAnnouncePageSort extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> params = new HashMap<>();
		try {
			params.put("eapId", this.parameterObject.getString("currEapId")); 
			params.put("sort", this.parameterObject.getNumber("currSort")); 
			
			sqlSession.update("kr.or.visitkorea.system.EventResultMapper.saveEventResultAnnouncePageSort", params);
			
			params = new HashMap<>();
			params.put("eapId", this.parameterObject.getString("prevEapId")); 
			params.put("sort", this.parameterObject.getNumber("prevSort")); 
			sqlSession.update("kr.or.visitkorea.system.EventResultMapper.saveEventResultAnnouncePageSort", params);
		
		}catch(Exception e) {
			e.printStackTrace();
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
