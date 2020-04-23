
package kr.or.visitkorea.admin.server.application.modules.staff;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;
import org.json.JSONObject;

import java.util.HashMap;

@BusinessMapping(id = "UPDATE_LOGIN_FAIL_CNT")
public class UpdateLoginFailCnt extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> paramterMap = new HashMap<>();
		if (this.parameterObject.has("stfId")) {
			paramterMap.put("stfId", this.parameterObject.getString("stfId"));
			paramterMap.put("loginFailCnt", -1);
		}
		sqlSession.update("kr.or.visitkorea.system.StaffMapper.updateLoginFailCnt", paramterMap);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

	}
}
