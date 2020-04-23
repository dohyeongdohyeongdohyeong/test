package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="ARTICLE_ETC_SELECT_WITH_COTID")
public class ArticleEtcSelect extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cotId", parameterObject.getString("cotId"));

		HashMap<String, Object> returnSelectCotIdMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.ArticleMapper.selectEtcCotId" , 
				paramterMap );
		List<HashMap<String, Object>> returnRelatedMap =
				sqlSession.selectList("kr.or.visitkorea.system.ArticleMapper.selectEtcRelatedView", paramterMap);
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("selectCotId", new JSONObject(returnSelectCotIdMap));
		
		JSONArray resultRelatedArr = new JSONArray(returnRelatedMap);
		
		String convertJSONString = resultObj.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{

			resultBodyObject.put("result", resultObj);
			resultBodyObject.put("resultRelatedView", resultRelatedArr);
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
