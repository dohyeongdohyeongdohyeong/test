package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="GET_MAIN_MARKETING")
public class GetMainMarketing extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		String MAN_ID = parameterObject.getString("MAN_ID");
		
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("MAN_ID", MAN_ID);
		
		// delete all SHOWCASE records
		List<Map<String, Object>> allShowcaseMap = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getMainMarketing", parameterMap);
		
		JSONArray resultObj = new JSONArray(allShowcaseMap);

		if (resultObj.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", resultObj);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
