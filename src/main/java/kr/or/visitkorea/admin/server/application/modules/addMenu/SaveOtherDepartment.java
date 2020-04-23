package kr.or.visitkorea.admin.server.application.modules.addMenu;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_OTHER_DEPARTMENT")
public class SaveOtherDepartment extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		Object paramObj = parameterObject.get("otd");
		
		if (paramObj instanceof JSONObject) {
			JSONObject obj = (JSONObject) paramObj;
			int result = insertOtd(obj);

			if (result != 1) {
				resultHeaderObject.put("process", "fail");
				resultHeaderObject.put("ment", "정보가 없습니다.");
			}
			
		} else if (paramObj instanceof JSONArray) {
			JSONArray arr = (JSONArray) paramObj;
			
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				int result = insertOtd(obj);

				if (result != 1) {
					resultHeaderObject.put("process", "fail");
					resultHeaderObject.put("ment", "정보가 없습니다.");
					return;
				}
			}
		}
	}

	public int insertOtd(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		
		if (obj.get("OTD_ID") != null)
			params.put("OTD_ID", obj.getString("OTD_ID"));
		if (obj.get("TITLE") != null)
			params.put("TITLE", obj.getString("TITLE"));
		if (obj.get("TEMPLATE_TYPE") != null)
			params.put("TEMPLATE_TYPE", obj.getString("TEMPLATE_TYPE"));
		if (obj.get("USE_ARTICLE_CONTENTS") != null)
			params.put("USE_ARTICLE_CONTENTS", obj.getBoolean("USE_ARTICLE_CONTENTS") ? 1 : 0);
		if (obj.get("USE_DB_CONTENTS") != null)
			params.put("USE_DB_CONTENTS", obj.getBoolean("USE_DB_CONTENTS") ? 1 : 0);
		
		return sqlSession.insert("kr.or.visitkorea.system.DepartmentMapper.insertDeptService", params);
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
