package kr.or.visitkorea.admin.server.application.modules.department_info;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_DEPARTMENT_INFO")
public class UpdateDepartmentInfo extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("depid", parameterObject.getString("depid"));
		paramterMap.put("tel", parameterObject.getString("tel"));
		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.DepartmentInfoMapper.updateDepartmentInfo",
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
