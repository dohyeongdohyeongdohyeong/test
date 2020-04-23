package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="DELETE_EVENT_ANNOUNCE_PAGE")
public class DeleteEventAnnouncePage extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> params = new HashMap<>();
		params.put("eapId", this.parameterObject.getString("eapId")); 
		params.put("evtId", this.parameterObject.getString("evtId")); 
		params.put("subEvtId", this.parameterObject.getString("subEvtId")); 
		params.put("gftId", this.parameterObject.getString("gftId")); 

		
		int result1 = sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.deleteEventResultAnnouncePage", params);
		
		//정렬순서 변경
		List<HashMap<String, Object> > resultList = new ArrayList<HashMap<String, Object> >();
		params.put("evtId", this.parameterObject.getString("evtId"));
		resultList = sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventAnnouncePageList", params);
		
		for(int i=0; i < resultList.size(); i++) {
			HashMap<String, Object> map = resultList.get(i);

			HashMap<String, Object> sortParams = new HashMap<>();
			sortParams.put("eapId", map.get("EAP_ID")); 
			sortParams.put("sort", i); 
			sqlSession.update("kr.or.visitkorea.system.EventResultMapper.saveEventResultAnnouncePageSort", sortParams);
		}
		
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
