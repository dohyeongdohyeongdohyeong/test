package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_COMPONENT")
public class SaveEventComponent extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject model = this.parameterObject.getJSONObject("model");
		JSONArray components = model.getJSONArray("COMPONENTS");
		
		for (int i = 0; i < components.length(); i++) {
			JSONObject obj = components.getJSONObject(i);
			HashMap<String, Object> params = this.buildComponentParam(obj);
			
			if (obj.getBoolean("IS_DELETE")) {
				sqlSession.delete("kr.or.visitkorea.system.EventMapper.deleteEventComponent", params);
			} else {
				sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventComponent", params);
				
				if (params.containsKey("desc")) 
					sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImageDescription", params);
			}
		}
	}

	private HashMap<String, Object> buildComponentParam(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (obj.has("EVC_ID"))
			params.put("EVC_ID", obj.getString("EVC_ID"));
		if (obj.has("EVT_ID"))
			params.put("EVT_ID", obj.getString("EVT_ID"));
		if (obj.has("COMP_TYPE"))
			params.put("COMP_TYPE", obj.getInt("COMP_TYPE"));
		if (obj.has("SUB_EVT_ID"))
			params.put("SUB_EVT_ID", obj.getString("SUB_EVT_ID"));
		if (obj.has("COMP_IDX"))
			params.put("COMP_IDX", obj.getInt("COMP_IDX"));
		if (obj.has("IS_BOX"))
			params.put("IS_BOX", obj.getBoolean("IS_BOX"));
		if (obj.has("IS_VERTICAL"))
			params.put("IS_VERTICAL", obj.getBoolean("IS_VERTICAL"));
		if (obj.has("IMG_ID"))
			params.put("IMG_ID", obj.getString("IMG_ID"));
		if (obj.has("IMG_ID"))
			params.put("imgId", obj.getString("IMG_ID"));
		if (obj.has("COMP_BODY"))
			params.put("COMP_BODY", obj.getString("COMP_BODY"));
		if (obj.has("IMG_LINK"))
			params.put("IMG_LINK", obj.getString("IMG_LINK"));
		if (obj.has("SIZE_TYPE"))
			params.put("SIZE_TYPE", obj.getInt("SIZE_TYPE"));
		if (obj.has("IMG_DESC"))
			params.put("desc", obj.getString("IMG_DESC"));
		return params;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
