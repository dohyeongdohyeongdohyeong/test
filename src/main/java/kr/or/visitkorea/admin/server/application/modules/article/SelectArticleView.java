package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_ARTICLE_VIEW")
public class SelectArticleView extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("ATM_ID"))
			params.put("ATM_ID", this.parameterObject.getString("ATM_ID"));
		
		HashMap<String, Object> resultMap =
				sqlSession.selectOne("kr.or.visitkorea.system.ArticleMapper.selectArticleView", params);
		
		JSONObject resultObj = new JSONObject(resultMap);
		
		if (resultObj.length() == 0) {
			sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.insertArticleView", params);
			resultObj.put("ATM_ID", (String) params.get("ATM_ID"));
		}
		resultBodyObject.put("result", resultObj);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
