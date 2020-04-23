package kr.or.visitkorea.admin.server.application.modules.author;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_AUTHOR_LIST")
public class SelectAuthorList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("keyword"))
			params.put("keyword", this.parameterObject.getString("keyword"));
		if (this.parameterObject.has("type"))
			params.put("type", this.parameterObject.getString("type"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		
		List<HashMap<String, Object>> resultMap =
				sqlSession.selectList("kr.or.visitkorea.system.AuthorMapper.authorList", params);
		List<HashMap<String, Object>> resultCnt =
				sqlSession.selectList("kr.or.visitkorea.system.AuthorMapper.authorListCnt", params);
		
		JSONArray resultArr = new JSONArray(resultMap);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultCnt", resultCnt.get(0));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
