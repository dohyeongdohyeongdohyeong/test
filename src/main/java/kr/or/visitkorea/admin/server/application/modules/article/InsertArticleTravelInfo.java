package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_ARTICLE_TRAVEL_INFO")
public class InsertArticleTravelInfo extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("triId"))
			params.put("triId", this.parameterObject.getString("triId"));
		if (this.parameterObject.has("aciId"))
			params.put("aciId", this.parameterObject.getString("aciId"));
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (this.parameterObject.has("title"))
			params.put("title", this.parameterObject.getString("title"));
		if (this.parameterObject.has("address"))
			params.put("address", this.parameterObject.getString("address"));
		if (this.parameterObject.has("tel"))
			params.put("tel", this.parameterObject.getString("tel"));
		if (this.parameterObject.has("homepage"))
			params.put("homepage", this.parameterObject.getString("homepage"));
		if (this.parameterObject.has("etc"))
			params.put("etc", this.parameterObject.getString("etc"));
		
		int result = sqlSession.insert("kr.or.visitkorea.ArticleMapper.insertArticleTravelInfo", params);
		
		if (result != 1) {
			resultHeaderObject.put("process", "fail");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
