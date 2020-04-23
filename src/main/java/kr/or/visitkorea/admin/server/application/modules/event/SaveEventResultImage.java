package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_RESULT_IMG")
public class SaveEventResultImage extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject model = this.parameterObject.getJSONObject("model");
		int result1 = 0;
		int result2 = 0;
		if (model.has("evtId")) {
			HashMap<String, Object> image = this.buildParameter(model);
			
			boolean isDelete = (boolean) image.get("isDelete");
			if (isDelete) {
				sqlSession.delete("kr.or.visitkorea.system.ImageMapper.deleteWithImgId", image);
			} else {
				result1 = sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.saveEventResultImage", image);
				result2 = sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", image);
			}
		}
		
		if (result1 != 1  || result2 != 1) {
			resultHeaderObject.put("process", "fail");
		}
	}

	private HashMap<String, Object> buildParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		if (obj.has("evtId")) 
			params.put("evtId", obj.getString("evtId"));
		if (obj.has("imgId")) 
			params.put("imgId", obj.getString("imgId"));
		if (obj.has("imgPath")) 
			params.put("imgPath", obj.getString("imgPath"));
		if (obj.has("imgDescription")) 
			params.put("imgDescription", obj.getString("imgDescription"));
		if (obj.has("isDelete"))
			params.put("isDelete", obj.getBoolean("isDelete"));
		return params;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
	
}
