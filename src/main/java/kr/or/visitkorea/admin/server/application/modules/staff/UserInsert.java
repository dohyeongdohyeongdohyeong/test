package kr.or.visitkorea.admin.server.application.modules.staff;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="USER_INSERT")
public class UserInsert extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("stfId", parameterObject.getString("stfId"));
		paramterMap.put("auth", parameterObject.getString("auth"));
		paramterMap.put("chkUse", parameterObject.getInt("chkUse"));
		paramterMap.put("img", parameterObject.getString("img"));
		paramterMap.put("np", parameterObject.getString("auth"));
		paramterMap.put("ip", this.getLoginStaffIp(this.getRequest()));
		paramterMap.put("usrId", parameterObject.getString("usrId"));
		
		String auth = this.parameterObject.getString("auth");
		
		if (!auth.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%.*^#?&;:\\(\\)\\/\\-\\+=_])[A-Za-z0-9$@$!%.*^#?&;:\\(\\)\\/\\-\\+=_]{8,}$")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "비밀번호를 영문, 숫자, 특수문자 3종류를 조합하여 최소 8자리 이상으로 구성해주세요.");
			return;
		}
		
		int insertResult = sqlSession.insert("kr.or.visitkorea.system.StaffMapper.userInsert", paramterMap);
		sqlSession.insert("kr.or.visitkorea.system.StaffMapper.insertStaffInfoModifyHistory", paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("insert", insertResult);

		if (insertResult != 1) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "정보가 없습니다.");
		}else{
			resultBodyObject.put("result", retJson);
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
