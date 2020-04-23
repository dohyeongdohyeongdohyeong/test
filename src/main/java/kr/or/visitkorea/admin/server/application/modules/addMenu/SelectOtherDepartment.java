package kr.or.visitkorea.admin.server.application.modules.addMenu;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_OTHER_DEPARTMENT")
public class SelectOtherDepartment extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> params = new HashMap<>();
		if (parameterObject.get("OTD_ID") != null)
			params.put("otdId", parameterObject.getString("OTD_ID"));
		
		List<HashMap<String, Object>> resultOtd = sqlSession.selectList("kr.or.visitkorea.system.DepartmentMapper.getService", params);
		JSONArray resultObj = new JSONArray(resultOtd);
		
		if (resultObj.length() == 0) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", resultObj.get(0));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
