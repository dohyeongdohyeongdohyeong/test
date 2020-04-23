package kr.or.visitkorea.admin.server.application.modules.content;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_CONTENT_MASTER_LIST")
public class SelectContentMasterList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = this.buildParameter();
		
		List<HashMap<String, Object>> resultMap = 
				sqlSession.selectList("kr.or.visitkorea.system.ContentMasterMapper.contentMasterList", params);
		List<HashMap<String, Object>> resultCnt = 
				sqlSession.selectList("kr.or.visitkorea.system.ContentMasterMapper.contentMasterListCnt", params);
		
		JSONArray resultArr = new JSONArray(resultMap);
		
		if (resultArr.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultCnt", resultCnt.get(0));
		}
	}
	
	private HashMap<String, Object> buildParameter() {
		HashMap<String, Object> params = new HashMap<>();

		if (this.parameterObject.has("table"))
			params.put("table", this.parameterObject.getString("table"));
		if (this.parameterObject.has("searchType"))
			params.put("searchType", this.parameterObject.getInt("searchType"));
		if (this.parameterObject.has("keyword"))
			params.put("keyword", this.parameterObject.getString("keyword"));
		if (this.parameterObject.has("order"))
			params.put("order", this.parameterObject.getString("order"));
		if (this.parameterObject.has("startDate") && !this.parameterObject.getString("startDate").equals(""))
			params.put("startDate", this.parameterObject.getString("startDate"));
		if (this.parameterObject.has("endDate") && !this.parameterObject.getString("endDate").equals(""))
			params.put("endDate", this.parameterObject.getString("endDate"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		if (this.parameterObject.has("limit"))
			params.put("limit", this.parameterObject.getInt("limit"));

		return params;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
	
}
