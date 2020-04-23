package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
public class ArticleDetails extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cotId", parameterObject.getString("cotId"));

		HashMap<String, Object> returnSelectCotIdMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.ArticleMapper.selectCotId" , 
				paramterMap );
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("selectCotId", new JSONObject(returnSelectCotIdMap));
		
		String convertJSONString = resultObj.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{

			paramterMap.put("atmId", returnSelectCotIdMap.get("ATM_ID"));

			List<HashMap<String, Object>> returnSelectMonthMap = sqlSession.selectList( 
					"kr.or.visitkorea.system.ArticleMapper.selectMonth" , 
					paramterMap );
			
			resultObj.put("selectMonth", new JSONArray(returnSelectMonthMap));
			
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
