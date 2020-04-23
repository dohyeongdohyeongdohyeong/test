package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_ANNOUNCE_PAGE")
public class SaveEventAnnouncePage extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> params = new HashMap<>();
		params.put("eapId", UUID.randomUUID().toString()); 
		params.put("evtId", this.parameterObject.getString("evtId")); 
		params.put("subEvtId", this.parameterObject.getString("subEvtId")); 
		params.put("giftId", this.parameterObject.getString("giftId")); 
		params.put("prizeNm", this.parameterObject.getString("prizeNm")); 
		params.put("giftDesc", this.parameterObject.getString("giftDesc")); 
		
		int result1 = sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.saveEventAnnouncePage", params);

		
		if (result1 != 1) {
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
