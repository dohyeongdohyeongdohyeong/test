package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="DELETE_ARTICLE_COUPON_INFO")
public class DeleteArticleCouponInfo extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("CP_ID"))
			params.put("CP_ID", this.parameterObject.getString("CP_ID"));
		
		int cnt = 
				sqlSession.selectOne("kr.or.visitkorea.system.ArticleMapper.selectPutCoupon", params);
		
			resultBodyObject.put("result", cnt);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
