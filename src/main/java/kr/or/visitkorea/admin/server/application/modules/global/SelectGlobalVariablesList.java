package kr.or.visitkorea.admin.server.application.modules.global;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_GLOBAL_VARIABLES_LIST")
public class SelectGlobalVariablesList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		List<HashMap<String, Object>> returnMap = 
				sqlSession.selectList("kr.or.visitkorea.system.GlobalVariablesMapper.selectAll");
		
		if (returnMap != null) {
			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", new JSONArray(returnMap));
		} else {
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
