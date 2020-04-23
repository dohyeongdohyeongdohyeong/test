package kr.or.visitkorea.admin.server.application.modules.code;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_CODE_LIST")
public class SelectCodeList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("SearchType"))
			params.put("SearchType", this.parameterObject.getInt("SearchType"));
		if (this.parameterObject.has("keyword"))
			params.put("Keyword", this.parameterObject.getString("keyword"));
		if(parameterObject.has("offset"))
			params.put("offset", parameterObject.getInt("offset"));
		
		
		
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.CodeMapper.selectCodeList" , 
				params );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.CodeMapper.selectCodeListCnt" , 
				params );
		
		List<HashMap<String, Object>> returnAllCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.CodeMapper.selectCodeAllCnt" , 
				params );
		
		JSONArray returnjson = new JSONArray(returnMap);
		JSONArray returnAlljson = new JSONArray(returnAllCnt);
		String convertJSONString = returnjson.toString();
		String convertJSONString2 = returnAlljson.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", new JSONArray(convertJSONString));
			resultBodyObject.put("resultAllCnt", new JSONArray(convertJSONString2));
			resultBodyObject.put("resultCnt", returnCnt.get(0));
		}
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
