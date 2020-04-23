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
@BusinessMapping(id="INSERT_PARTNERS")
public class InsertPartners extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("snsid", parameterObject.getString("snsid"));
		paramterMap.put("id", parameterObject.getString("id"));
		paramterMap.put("pass", parameterObject.getString("pass"));
		paramterMap.put("isuse", parameterObject.getInt("isuse"));
		paramterMap.put("name", parameterObject.getString("name"));
		paramterMap.put("magname", parameterObject.getString("magname"));
		int insertResult = sqlSession.update( 
				"kr.or.visitkorea.system.SnsMapper.insertPartners",
				paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("insert", insertResult);

		if (insertResult == 1) {
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
