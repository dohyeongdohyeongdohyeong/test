package kr.or.visitkorea.admin.server.application.modules.permission;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="PERMISSION_INSERT")
public class PermissionInsert extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String usrId = parameterObject.getString("usrId");
		String permissionId = parameterObject.getString("permissionId");
		int permission = parameterObject.getInt("permission");
		String editUsrId = parameterObject.getString("editUsrId");
		String caption = parameterObject.getString("caption");
		String parentCaption = parameterObject.getString("parentCaption");

		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("usrId", usrId);
		parameterMap.put("permissionId", permissionId);
		parameterMap.put("permission", permission);
		parameterMap.put("editUsrId", editUsrId);
		parameterMap.put("caption", caption);
		parameterMap.put("parentCaption", parentCaption);
		
		int insertResult = 
				sqlSession.insert("kr.or.visitkorea.system.PermissionMapper.permissionInsert",	parameterMap);
		sqlSession.insert("kr.or.visitkorea.system.PermissionMapper.insertPermissionHistory", parameterMap);
		
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
