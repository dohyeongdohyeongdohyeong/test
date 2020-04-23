package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="ARTICLE_INSERT_IMAGE")
public class ArticleInsertImage extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("cotId", parameterObject.getString("cotId"));
		if (parameterObject.has("imgPath")) map1.put("imgPath", parameterObject.getString("imgPath"));
		if (parameterObject.has("imgDescription")) map1.put("imgDescription", parameterObject.getString("imgDescription"));
		map1.put("contentOrder", parameterObject.getInt("contentOrder"));
		map1.put("articleOrder", parameterObject.getInt("articleOrder"));
		map1.put("articleSubOrder", parameterObject.getInt("articleSubOrder"));
		map1.put("articleType", parameterObject.getInt("articleType"));
		
		if (parameterObject.has("imgId")) {
			map1.put("imgId", parameterObject.getString("imgId"));
		}else {
			map1.put("imgId", UUID.randomUUID().toString());
		}
		
		if (parameterObject.has("aciId")) {
			map1.put("aciId", parameterObject.getString("aciId"));
		}else {
			map1.put("aciId", UUID.randomUUID().toString());
		}

		int insertImageResult = sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertWithCotId", map1);
		int insertArticleContentResult = sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.insertArticleContent", map1);

		JSONObject retObj = new JSONObject();
		retObj.put("insert.image.result", insertImageResult);
		retObj.put("insert.article.content.result", insertArticleContentResult);
		retObj.put("insert.image.id", map1.get("imgId"));
		
		resultBodyObject.put("result", retObj);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
