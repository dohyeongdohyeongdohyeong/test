package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_IMAGE")
public class SaveEventImage extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject model = this.parameterObject.getJSONObject("model");
		JSONArray images = model.getJSONArray("IMAGES");
		
		for (int i = 0; i < images.length(); i++) {
			JSONObject obj = images.getJSONObject(i);
			HashMap<String, Object> params = this.buildComponentParam(obj);
			
			if (params.containsKey("IS_DELETE")) {
				sqlSession.delete("kr.or.visitkorea.system.ImageMapper.deleteWithImgId", params);
			} else if (params.containsKey("IMG_UPDATE")) {
				sqlSession.update("kr.or.visitkorea.system.ImageMapper.UpdateWithImgId", params);
			} else {
				sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertWithCotId", params);
			} 
			
			
		}
	}

	private HashMap<String, Object> buildComponentParam(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (obj.has("IMG_ID"))
			params.put("imgId", obj.getString("IMG_ID"));
		if (obj.has("IMG_DESC"))
			params.put("imgDescription", obj.getString("IMG_DESC"));
		if (obj.has("COT_ID"))
			params.put("cotId", obj.getString("COT_ID"));
		if (obj.has("IMG_PATH"))
			params.put("imgPath", obj.getString("IMG_PATH"));
		if (obj.has("IS_DELETE"))
			params.put("IS_DELETE", obj.getBoolean("IS_DELETE"));
		if (obj.has("IMG_UPDATE"))
			params.put("IMG_UPDATE", obj.getString("IMG_UPDATE"));
		return params;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
