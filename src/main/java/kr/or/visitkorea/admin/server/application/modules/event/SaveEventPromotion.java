package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_PROMOTION")
public class SaveEventPromotion extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONArray model = this.parameterObject.getJSONObject("model").getJSONArray("PROMOTION");
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("evtId", this.parameterObject.getString("evtId"));

		sqlSession.delete("kr.or.visitkorea.system.EventMapper.deleteEventPromotion", params);
		
		for (int i = 0; i < model.length(); i++) {
			JSONObject obj = model.getJSONObject(i);
			params = this.buildPromotionParam(obj);

			int result1 = sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertWithCotId", params);
			int result2 = sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventPromotion", params);
			
			if (result1 != 1 || result2 != 1) {
				resultHeaderObject.put("process", "fail");
				resultHeaderObject.put("ment", "");
			}
		}
	}

	private HashMap<String, Object> buildPromotionParam(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (obj.has("banId"))
			params.put("banId", obj.getString("banId"));
		if (obj.has("evtId"))
			params.put("evtId", obj.getString("evtId"));
		if (obj.has("cotId"))
			params.put("cotId", obj.getString("cotId"));
		if (obj.has("imgId"))
			params.put("imgId", obj.getString("imgId"));
		if (obj.has("imgPath"))
			params.put("imgPath", obj.getString("imgPath"));
		return params;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
