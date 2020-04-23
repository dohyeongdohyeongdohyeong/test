package kr.or.visitkorea.admin.server.application.modules.staff;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_STAFF_LOGIN_HISTORY")
public class SelectStaffLoginHistory extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		if (this.parameterObject.has("limit"))
			params.put("limit", this.parameterObject.getInt("limit"));
		
		List<HashMap<String, Object>> returnMap =
				sqlSession.selectList("kr.or.visitkorea.system.StaffMapper.selectStaffLoginHistory", params);
		
		int totalCnt = sqlSession.selectOne("kr.or.visitkorea.system.StaffMapper.selectStaffLoginHistoryCnt", params);
		
		resultBodyObject.put("result", new JSONArray(returnMap));
		resultBodyObject.put("resultCnt", totalCnt);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
