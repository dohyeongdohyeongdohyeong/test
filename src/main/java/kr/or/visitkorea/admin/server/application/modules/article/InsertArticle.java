package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_ARTICLE")
public class InsertArticle extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject retObj = new JSONObject();

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cotId", parameterObject.getString("cotId"));
		paramterMap.put("atmId", parameterObject.getString("atmId"));
		paramterMap.put("usrId", parameterObject.getString("usrId"));

		int retInt1 = sqlSession.insert( 
				"kr.or.visitkorea.system.ArticleMapper.insertContentMaster" , 
				paramterMap );

		int retInt2 = sqlSession.insert( 
				"kr.or.visitkorea.system.ArticleMapper.insertArticleMaster" , 
				paramterMap );

		
		if (parameterObject.has("isMain")) {
			paramterMap.put("otdId", parameterObject.getString("otdId"));
			int retInt3 = sqlSession.insert( "kr.or.visitkorea.system.ArticleMapper.insertOtherContent" ,  paramterMap );
			retObj.put("insert3", retInt3);
		}
		
		retObj.put("insert1", retInt1);
		retObj.put("insert2", retInt2);
		
		if (retInt1 == -1) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", retObj);
		}
		
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
