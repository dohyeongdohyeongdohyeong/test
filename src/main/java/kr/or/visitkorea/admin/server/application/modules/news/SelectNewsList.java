package kr.or.visitkorea.admin.server.application.modules.news;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */ 
@BusinessMapping(id="SELECT_NEWS_LIST")
public class SelectNewsList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getString("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getString("edate"));

		if(parameterObject.has("mode"))
			paramterMap.put("mode", parameterObject.getInt("mode"));
		if(parameterObject.has("keyword"))
			paramterMap.put("keyword", parameterObject.getString("keyword"));
		
		paramterMap.put("offset", parameterObject.getInt("offset"));
			
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.NewsMapper.selectNewsList" , 
			paramterMap );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
			"kr.or.visitkorea.system.NewsMapper.selectNewsListCnt" , 
			paramterMap );
		
		
		BoundSql boundSql = sqlSession.getConfiguration()
				.getMappedStatement("kr.or.visitkorea.system.NewsMapper.selectNewsList").getSqlSource().getBoundSql(paramterMap);

		

		String query1 = boundSql.getSql();

		

		Object paramObj = boundSql.getParameterObject();

		 

		if(paramObj != null){              // 파라미터가 아무것도 없을 경우

			List<ParameterMapping> paramMapping = boundSql.getParameterMappings();

			 

			for(ParameterMapping mapping : paramMapping){

				String propValue = mapping.getProperty();       

				query1=query1.replaceFirst("\\?", "#{"+propValue+"}");

			}

		}
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();
		System.out.println("selectNewsList 쿼리 :: " + query1);
		System.out.println("selectNewsList.result.JSONArray :: " + convertJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
//			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", json);
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
