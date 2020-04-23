package kr.or.visitkorea.admin.server.application.modules.banner;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_BANNER_DETAIL")
public class UpdateBannerDetail extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("mkbid", parameterObject.getString("mkbid"));
		paramterMap.put("orderby", parameterObject.getString("orderby"));
		paramterMap.put("title", parameterObject.getString("title"));
//		System.out.println(parameterObject.getString("cotid"));
		if(parameterObject.has("imgid"))
			paramterMap.put("imgid", parameterObject.getString("imgid"));
		if(parameterObject.has("cotid"))
			paramterMap.put("cotid", parameterObject.getString("cotid"));
		if(parameterObject.has("url"))
			paramterMap.put("url", parameterObject.getString("url"));

		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.BannerMapper.updateBannerDetail",
				paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("update", updateResult);

		if (updateResult >= 1) {
			resultBodyObject.put("result", retJson);
		}else{
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "정보가 없습니다.");
		}	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
			//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
