package kr.or.visitkorea.admin.server.application.modules.partners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_PARTNERS_REQUESTHIST_FILEUPLOAD")
public class SelectPartnersReqHistMultiFileUpload extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		Map<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("pahid"))
			paramterMap.put("pahid", parameterObject.getString("pahid"));
			
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.PartnersMapper.selectPartnersReqHistMultiFileUpload", paramterMap);
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else
			resultBodyObject.put("result", json);
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
