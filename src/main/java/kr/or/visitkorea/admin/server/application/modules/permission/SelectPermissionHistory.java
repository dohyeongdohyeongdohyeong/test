package kr.or.visitkorea.admin.server.application.modules.permission;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_PERMISSION_HISTORY")
public class SelectPermissionHistory extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("offset", parameterObject.getInt("offset"));
		params.put("limit", parameterObject.getInt("limit"));

		List<HashMap<String, Object>> returnMap = 
				sqlSession.selectList("kr.or.visitkorea.system.PermissionMapper.selectPermissionHistory", params);

		int totalCnt = sqlSession.selectOne("kr.or.visitkorea.system.PermissionMapper.selectPermissionHistoryCnt");
		
		if (returnMap == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "정보가 없습니다.");
		} else {
			resultBodyObject.put("result", new JSONArray(returnMap));
			resultBodyObject.put("resultCnt", totalCnt);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
