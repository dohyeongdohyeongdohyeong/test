package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONObject;

import com.google.gwt.json.client.JSONNumber;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_WIN_DESC")
public class SaveEventResultWinDesc extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject model = this.parameterObject.getJSONObject("model");
		
		int result1 = sqlSession.update("kr.or.visitkorea.system.EventResultMapper.saveEventResultWinDesc", buildParameter(model));


		
		if (result1 != 1 ) {
			resultHeaderObject.put("process", "fail");
		}
	}

	private HashMap<String, Object> buildParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		if (obj.has("evtId")) 
			params.put("evtId", obj.getString("evtId"));
		if (obj.has("isWinDesc")) 
			params.put("isWinDesc", obj.getInt("isWinDesc"));
		if (obj.has("winDesc")) 
			params.put("winDesc", obj.getString("winDesc"));
		return params;
	}
	


	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
	
}
