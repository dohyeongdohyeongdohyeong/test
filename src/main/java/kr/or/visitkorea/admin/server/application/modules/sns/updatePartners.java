package kr.or.visitkorea.admin.server.application.modules.sns;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_PARTNERS")
public class updatePartners extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("snsid", parameterObject.getString("snsid"));
		if(parameterObject.has("id"))
			paramterMap.put("id", parameterObject.getString("id"));
		if(parameterObject.has("pass"))
			paramterMap.put("pass", parameterObject.getString("pass"));
		paramterMap.put("isuse", parameterObject.getInt("isuse"));
		paramterMap.put("name", parameterObject.getString("name"));
		paramterMap.put("magname", parameterObject.getString("magname"));
		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.SnsMapper.updatePartners",
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
