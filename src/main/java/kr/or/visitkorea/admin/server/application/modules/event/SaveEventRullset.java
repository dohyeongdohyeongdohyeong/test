package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_RULLSET")
public class SaveEventRullset extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject model = this.parameterObject.getJSONObject("model");
		JSONArray rullsets = model.getJSONArray("RULLSET");
		
		try {
			sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventRullset", this.buildRullsetParameter(rullsets));
		} catch (Throwable e) {
			sqlSession.rollback();
		}
	}
	
	private List<HashMap<String, Object>> buildRullsetParameter(JSONArray array) {
		List<HashMap<String, Object>> rullsetList = new ArrayList<>();
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			HashMap<String, Object> params = new HashMap<>();
			params.put("subEvtId", obj.getString("subEvtId"));
			params.put("entryNum", obj.getInt("entryNum"));
			params.put("gftId", obj.getString("gftId"));
			params.put("stfId", obj.getString("stfId"));
			rullsetList.add(params);
			
			if(i == 0) {
				sqlSession.delete("kr.or.visitkorea.system.EventMapper.removeEventRullset", params);
			}
			
		}
		return rullsetList;
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
