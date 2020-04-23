package kr.or.visitkorea.admin.server.application.modules.partners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.json.JSONArray;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * @author Admin
 * 제휴/제안 신청 Module
 */
@BusinessMapping(id="SELECT_PARTNERS_AFFPRO_LIST")
public class SelectPartnersAffProList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		if (parameterObject.has("type"))
			param.put("type", parameterObject.get("type"));
		if (parameterObject.has("status"))
			param.put("status", parameterObject.get("status"));
		if (parameterObject.has("title"))
			param.put("title", parameterObject.get("title"));
		param.put("offset", parameterObject.getInt("offset"));
		
		List<Map<String, Object>> queryResult = sqlSession.selectList("kr.or.visitkorea.system.PartnersMapper.selectProposalList", param);
		List<Map<String, Object>> queryResultCount = sqlSession.selectList("kr.or.visitkorea.system.PartnersMapper.selectProposalListCount", param);
		
		JSONArray jsonArr = new JSONArray(queryResult);
		String convertJson = jsonArr.toString();
		
		if (convertJson.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", jsonArr);
			resultBodyObject.put("resultCount", queryResultCount.get(0));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
}