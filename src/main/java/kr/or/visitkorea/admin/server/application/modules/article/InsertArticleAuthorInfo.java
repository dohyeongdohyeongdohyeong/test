package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_ARTICLE_AUTHOR_INFO")
public class InsertArticleAuthorInfo extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (this.parameterObject.has("authorList")) {
			List<HashMap<String, Object>> authorList = new ArrayList<>();
			
			JSONArray authorArr = this.parameterObject.getJSONArray("authorList");
			for (int i = 0; i < authorArr.length(); i++) {
				JSONObject obj = authorArr.getJSONObject(i);
				HashMap<String, Object> objMap = new HashMap<String, Object>();
				objMap.put("athId", obj.getString("athId"));
				objMap.put("cotId", obj.getString("cotId"));
				authorList.add(objMap);
			}
			params.put("authorList", authorList);
		}
		sqlSession.delete("kr.or.visitkorea.system.AuthorMapper.deleteAuthorContent", params);
		sqlSession.insert("kr.or.visitkorea.system.AuthorMapper.insertAuthorContent", params);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
