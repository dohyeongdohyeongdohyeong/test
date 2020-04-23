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
@BusinessMapping(id="UPDATE_REPAIR")
public class updateRepair extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("repid", parameterObject.getString("repid"));
		if(parameterObject.has("title"))
			paramterMap.put("title", parameterObject.getString("title"));
		if(parameterObject.has("status"))
			paramterMap.put("status", parameterObject.getInt("status"));
		if(parameterObject.has("rtype"))
			paramterMap.put("rtype", parameterObject.getInt("rtype"));
		if(parameterObject.has("remid"))
			paramterMap.put("remid", parameterObject.getString("remid"));
		if(parameterObject.has("reqbody"))
			paramterMap.put("reqbody", parameterObject.getString("reqbody"));
		if(parameterObject.has("reqfile"))
			paramterMap.put("reqfile", parameterObject.getString("reqfile"));
		if(parameterObject.has("reqfilepath"))
			paramterMap.put("reqfilepath", parameterObject.getString("reqfilepath"));
		if(parameterObject.has("resbody"))
			paramterMap.put("resbody", parameterObject.getString("resbody"));
		if(parameterObject.has("resfile"))
			paramterMap.put("resfile", parameterObject.getString("resfile"));
		if(parameterObject.has("resfilepath"))
			paramterMap.put("resfilepath", parameterObject.getString("resfilepath"));
		
		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.RepairMapper.updateRepair",
				paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("update", updateResult);

		if (updateResult > 0) {
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
