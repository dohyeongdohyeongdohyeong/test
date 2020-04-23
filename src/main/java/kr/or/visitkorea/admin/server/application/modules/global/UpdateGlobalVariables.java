package kr.or.visitkorea.admin.server.application.modules.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_GLOBAL_VARIABLES")
public class UpdateGlobalVariables extends AbstractModule {

	private int result = 0;
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONArray array = this.parameterObject.getJSONArray("array");
		
		List<HashMap<String, Object>> list = this.convertListFromJson(array);
		
		list.forEach(item -> {
			result += sqlSession.update("kr.or.visitkorea.system.GlobalVariablesMapper.updateByName", item);
		});
		
		resultHeaderObject.put("process", "success");
		resultBodyObject.put("result", result);
	}
	
	private List<HashMap<String, Object>> convertListFromJson(JSONArray array) {
		List<HashMap<String, Object>> list = new ArrayList<>();
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			
			HashMap<String, Object> property = new HashMap<>();
			property.put("name", obj.getString("NAME"));
			property.put("value", obj.getInt("VALUE"));
			property.put("comment", obj.getString("COMMENT"));
			
			list.add(property);
		}
		return list;
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
