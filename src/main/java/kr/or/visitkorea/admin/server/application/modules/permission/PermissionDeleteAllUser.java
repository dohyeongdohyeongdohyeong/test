package kr.or.visitkorea.admin.server.application.modules.permission;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="PERMISSION_DEL_ALL_USER")
public class PermissionDeleteAllUser extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String usrId = parameterObject.getString("usrId");
		int permission = parameterObject.getInt("permission");
		String editUsrId = parameterObject.getString("editUsrId");
		
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("usrId", usrId);
		parameterMap.put("permissionId", "-");
		parameterMap.put("permission", permission);
		parameterMap.put("editUsrId", editUsrId);
		parameterMap.put("parentCaption", "전체");
		parameterMap.put("caption", "시스템 제외");

		int deleteResult = 
				sqlSession.update("kr.or.visitkorea.system.PermissionMapper.permissionDeleteForTgrUserNotSystem", parameterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("delete", deleteResult);
		
		if (deleteResult == 1) {
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
