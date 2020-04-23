package kr.or.visitkorea.admin.server.application.modules.staff;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;
import kr.or.visitkorea.admin.shared.model.StaffVO;

@BusinessMapping(id="UPDATE_USER_PASSWORD")
public class UpdateUserPassword extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String stfId = null,
			   op = null,
			   np1 = null,
			   np2 = null;
		
		String ip = this.getLoginStaffIp(this.getRequest());
		
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("stfId"))
			stfId = this.parameterObject.getString("stfId");
		if (this.parameterObject.has("op"))
			op = this.parameterObject.getString("op");
		if (this.parameterObject.has("np1"))
			np1 = this.parameterObject.getString("np1");
		if (this.parameterObject.has("np2"))
			np2 = this.parameterObject.getString("np2");
		
		if (op == null || np1 == null || np2 == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "입력되지 않은 항목이 있습니다.");
			return;
		}
		
		if (!np1.equals(np2)) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "비밀번호가 서로 일치하지 않습니다.");
			return;
		}
		
		if (op.equals(np1)) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "변경할 비밀번호를 기존 비밀번호와 다르게 입력해주세요.");
			return;
		}

		params.put("stfId", stfId);
		params.put("auth", op);
		params.put("op", op);
		params.put("np", np1);
		params.put("ip", ip);

		HashMap<String, Object> returnMap = sqlSession.selectOne("kr.or.visitkorea.system.StaffMapper.loginChk", params);
		
		if (returnMap == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
			return;
		}
		
		StaffVO staff = StaffVO.fromJson(new JSONObject(returnMap));
		
		if (staff.getLoginResult() == 0) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "입력한 기존 비밀번호가 틀립니다.");
			return;
		}
		
		int result = sqlSession.update("kr.or.visitkorea.system.StaffMapper.updateUserPassword", params);
		
		if (result == 1) {
			params.put("usrId", staff.getUsrId());
			
			sqlSession.insert("kr.or.visitkorea.system.StaffMapper.insertStaffInfoModifyHistory", params);
			sqlSession.update("kr.or.visitkorea.system.StaffMapper.updatePasswordRefreshDate", params);
			
			resultHeaderObject.put("process", "success");
		} else {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
}
