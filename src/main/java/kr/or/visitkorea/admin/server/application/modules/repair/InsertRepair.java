package kr.or.visitkorea.admin.server.application.modules.repair;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_REPAIR")
public class InsertRepair extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("repid", parameterObject.getString("repid"));
		paramterMap.put("title", parameterObject.getString("title"));
		paramterMap.put("stfid", parameterObject.getString("stfid"));
		paramterMap.put("reqbody", parameterObject.getString("reqbody"));
		
		if(parameterObject.has("reqfile"))
			paramterMap.put("reqfile", parameterObject.getString("reqfile"));
		if(parameterObject.has("reqfilepath"))
			paramterMap.put("reqfilepath", parameterObject.getString("reqfilepath"));
		
		int insertResult = sqlSession.update( 
				"kr.or.visitkorea.system.RepairMapper.insertRepair",
				paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("insert", insertResult);

		if (insertResult == 1) {
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
