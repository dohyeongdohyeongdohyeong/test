package kr.or.visitkorea.admin.server.application.modules.sns;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_SNS_COURSE_DETAIL")
public class UpdateSnsCourseDetail extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		paramterMap.put("crsId", parameterObject.get("crsId"));
		paramterMap.put("isOpen", parameterObject.get("isOpen"));
		paramterMap.put("desc", parameterObject.get("desc"));
		
		int procResult = -1;
		procResult = sqlSession.update("kr.or.visitkorea.system.SnsMapper.updateSnsCourseDetail", paramterMap);
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("procResult", procResult);
		
		String convertJSONString = resultObj.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", resultObj);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

}
