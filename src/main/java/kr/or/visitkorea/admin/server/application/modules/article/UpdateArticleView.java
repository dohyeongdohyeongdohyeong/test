package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_ARTICLE_VIEW")
public class UpdateArticleView extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("ATM_ID"))
			params.put("ATM_ID", this.parameterObject.getString("ATM_ID"));
		if (this.parameterObject.has("RECOMM_VIEW"))
			params.put("RECOMM_VIEW", this.parameterObject.getInt("RECOMM_VIEW"));
		if (this.parameterObject.has("SIGHT_VIEW"))
			params.put("SIGHT_VIEW", this.parameterObject.getInt("SIGHT_VIEW"));
		if (this.parameterObject.has("COURSE_VIEW"))
			params.put("COURSE_VIEW", this.parameterObject.getInt("COURSE_VIEW"));
		if (this.parameterObject.has("FESTIVAL_VIEW"))
			params.put("FESTIVAL_VIEW", this.parameterObject.getInt("FESTIVAL_VIEW"));
		
		int result = sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateArticleView", params);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
