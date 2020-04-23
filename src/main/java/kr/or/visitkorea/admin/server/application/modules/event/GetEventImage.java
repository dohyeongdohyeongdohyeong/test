package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EVENT_IMAGE")
public class GetEventImage extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (this.parameterObject.has("evtId"))
			params.put("evtId", this.parameterObject.getString("evtId"));
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		
		List<HashMap<String, Object>> resultMap =
				sqlSession.selectList("kr.or.visitkorea.system.EventMapper.getEventImage", params);
		
		JSONArray resultArr = new JSONArray(resultMap);
		
		if (resultArr.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", resultArr);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
