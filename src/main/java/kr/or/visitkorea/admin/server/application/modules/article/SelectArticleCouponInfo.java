package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_ARTICLE_COUPON_INFO")
public class SelectArticleCouponInfo extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("aciId"))
			params.put("aciId", this.parameterObject.getString("aciId"));
		
		List<HashMap<String, Object>> resultMap = 
				sqlSession.selectList("kr.or.visitkorea.system.ArticleMapper.selectArticleCouponInfo", params);
		JSONArray resultArr = new JSONArray(resultMap);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
