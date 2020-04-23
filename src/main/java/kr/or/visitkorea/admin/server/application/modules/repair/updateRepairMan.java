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
@BusinessMapping(id="UPDATE_REPAIRMAN")
public class updateRepairMan extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("remid", parameterObject.getString("remid"));
		
		if(parameterObject.has("name"))
			paramterMap.put("name", parameterObject.getString("name"));
		if(parameterObject.has("tel"))
			paramterMap.put("tel", parameterObject.getString("tel"));
		if(parameterObject.has("mail"))
			paramterMap.put("mail", parameterObject.getString("mail"));
		
		int updateResult = sqlSession.update( 
			"kr.or.visitkorea.system.RepairMapper.updateRepairMan",
			paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("insert", updateResult);

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
