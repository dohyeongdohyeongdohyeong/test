package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_COMP_CONNECT_MORE")
public class UpdateConnectMore extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("COMP_ID", parameterObject.getString("COMP_ID"));
		paramterMap.put("ODM_ID", parameterObject.getString("ODM_ID"));
		paramterMap.put("CONNECT_MORE", parameterObject.getString("CONNECT_MORE"));
			
		int insertResult = sqlSession.insert("kr.or.visitkorea.system.DepartmentMapper.updateDeptConnectMore", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("insertResult", insertResult);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", json);
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
