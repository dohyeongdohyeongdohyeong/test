package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="ARTICLE_CONTENT_ROW")
public class ArticleContentRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("ACI_ID", parameterObject.getString("ACI_ID"));
		paramterMap.put("ATM_ID", parameterObject.getString("ATM_ID"));
		paramterMap.put("CONTENT_ORDER", parameterObject.getInt("CONTENT_ORDER"));
		paramterMap.put("ARTICLE_ORDER", parameterObject.getInt("ARTICLE_ORDER"));
		paramterMap.put("ARTICLE_SUB_ORDER", parameterObject.getInt("ARTICLE_SUB_ORDER"));
		paramterMap.put("ARTICLE_TYPE", parameterObject.getInt("ARTICLE_TYPE"));

		if (parameterObject.has("IS_VERTICAL"))
			paramterMap.put("IS_VERTICAL", parameterObject.getInt("IS_VERTICAL"));
		if (parameterObject.has("IS_BOX"))
			paramterMap.put("IS_BOX", parameterObject.getInt("IS_BOX"));
		if (parameterObject.has("ARTICLE_TITLE")) paramterMap.put("ARTICLE_TITLE", parameterObject.getString("ARTICLE_TITLE"));
		if (parameterObject.has("ARTICLE_BODY")) {
			System.out.println(parameterObject.getString("ARTICLE_BODY"));
			paramterMap.put("ARTICLE_BODY", parameterObject.getString("ARTICLE_BODY"));
		}else {
			System.out.println("ARTICLE_BODY 가 정의되어 있지 않습니다.");
			System.out.println("ARTICLE_BODY 가 정의되어 있지 않습니다.");
		}
		if (parameterObject.has("IMG_ID")) paramterMap.put("IMG_ID", parameterObject.getString("IMG_ID"));

		HashMap<String, Object> articleCountMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.ArticleMapper.articleContentCount" , 
				paramterMap );
		
		long cntInt = (Long) articleCountMap.get("cnt");
		
		System.out.println("record count :: " + cntInt);
		
		// in case of not exist record
		int procResult = -1;
		if (cntInt == 0) {
			sqlSession.insert( 
					"kr.or.visitkorea.system.ArticleMapper.articleContentRowInsert" , 
					paramterMap );
		}else {
			sqlSession.update( 
					"kr.or.visitkorea.system.ArticleMapper.articleContentRowUpdate" , 
					paramterMap );
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("procResult", procResult);
		
		String convertJSONString = resultObj.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", resultObj);
		}
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
