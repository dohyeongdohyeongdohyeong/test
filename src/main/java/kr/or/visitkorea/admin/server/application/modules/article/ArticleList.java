package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="ARTICLE_LIST")
public class ArticleList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("mode", parameterObject.getInt("mode"));
		if(parameterObject.has("dateType"))
			paramterMap.put("dateType", parameterObject.getInt("dateType"));
		if(parameterObject.has("startInput"))
			paramterMap.put("startInput", parameterObject.getString("startInput"));
		if(parameterObject.has("endInput"))
			paramterMap.put("endInput", parameterObject.getString("endInput"));
		if(parameterObject.has("areaCode"))
			paramterMap.put("areaCode", parameterObject.getInt("areaCode"));
		if(parameterObject.has("CategoryCode"))
			paramterMap.put("CategoryCode", parameterObject.getString("CategoryCode"));
		if(parameterObject.has("Division"))
			paramterMap.put("Division", parameterObject.getString("Division"));
		if(parameterObject.has("keyword"))
			paramterMap.put("keyword", parameterObject.getString("keyword"));
		if(parameterObject.has("offset"))
			paramterMap.put("offset", parameterObject.getInt("offset"));
		if (parameterObject.has("OTD_ID")) {
			String OTD_ID = parameterObject.getString("OTD_ID");
			if (!OTD_ID.equals("0a01eb7b-96de-11e8-8165-020027310001")) {
				paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
			}
		}
		if(parameterObject.has("CODE"))
			paramterMap.put("CODE", parameterObject.getString("CODE"));
		if(parameterObject.has("IS_OPEN"))
			paramterMap.put("IS_OPEN", parameterObject.getInt("IS_OPEN"));
		if(parameterObject.has("IS_RANK"))
			paramterMap.put("IS_RANK", parameterObject.getInt("IS_RANK"));

		System.out.println("paramterMap :: " +paramterMap);

		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.ArticleMapper.list" , 
				paramterMap );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.ArticleMapper.listCnt" , 
				paramterMap );
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", new JSONArray(convertJSONString));
			resultBodyObject.put("resultCnt", returnCnt.get(0));
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
