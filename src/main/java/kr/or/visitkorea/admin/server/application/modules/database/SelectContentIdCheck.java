package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_CONTENTID_CHECK")
public class SelectContentIdCheck extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (parameterObject.has("CONTENT_ID"))
			map.put("CONTENT_ID", parameterObject.get("CONTENT_ID").toString());
		
		HashMap<String, Object> result = sqlSession.selectOne("kr.or.visitkorea.system.DatabaseMapper.selectDatabaseContentWithContentId", map);
		JSONObject json = new JSONObject();
		json.put("result", new JSONObject(result));
		String convertJSONString = json.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			int cnt = Integer.parseInt(result.get("CNT").toString());
			
			// 0 : 중복 있음, 1 : 중복 없음
			resultBodyObject.put("result", cnt > 0 ? "0" : "1");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
