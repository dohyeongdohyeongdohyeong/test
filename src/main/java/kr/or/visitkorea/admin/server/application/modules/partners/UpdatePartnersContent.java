package kr.or.visitkorea.admin.server.application.modules.partners;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_PARTNERS_CONTENT")
public class UpdatePartnersContent extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("pacid", parameterObject.getString("pacid"));
		if(parameterObject.has("status"))
			paramterMap.put("status", parameterObject.getInt("status"));
		if(parameterObject.has("cmt"))
			paramterMap.put("cmt", parameterObject.getString("cmt"));
		if(parameterObject.has("url"))
			paramterMap.put("url", parameterObject.getString("url"));
		if(parameterObject.has("dnoffer"))
			paramterMap.put("dnoffer", parameterObject.getInt("dnoffer"));
		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.PartnersMapper.updatePartnersContent",
				paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("update", updateResult);

		if (updateResult >= 1) {
			resultBodyObject.put("result", retJson);
		}else{
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "정보가 없습니다.");
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
