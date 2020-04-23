package kr.or.visitkorea.admin.server.application.modules.global;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_GLOBAL_VARIABLE")
public class SelectGlobalVariable extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("name"))
			params.put("name", this.parameterObject.getString("name"));
		
		HashMap<String, Object> returnMap = 
				sqlSession.selectOne("kr.or.visitkorea.system.GlobalVariablesMapper.selectOneValue", params);
		
		if (returnMap != null) {
			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", new JSONObject(returnMap));
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
