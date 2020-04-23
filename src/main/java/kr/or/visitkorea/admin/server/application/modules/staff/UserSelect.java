package kr.or.visitkorea.admin.server.application.modules.staff;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="USER_SELECT")
public class UserSelect extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String usrId = parameterObject.getString("usrId");

		HashMap<String, Object> returnMap = 
				sqlSession.selectOne("kr.or.visitkorea.system.StaffMapper.userSelect", usrId);
		
		JSONObject returnObj = new JSONObject(returnMap);
		
		if (returnObj.toString() == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "등록된 사용자가 아닙니다. 관리자에게 문의해 주세요.");
		} else {
			resultBodyObject.put("result", returnObj);
		}
	}

	private Document buildUserPermission(List<HashMap<String, Object>> permitReturnMap) {

		Document userPermissionDocument = Call.getPermissionDocument().clone();
		
		for (HashMap<String, Object> map : permitReturnMap) {

			String PERMISSION_ID = (String) map.get("PERMISSION_ID");
			
			XPathFactory xFactory = XPathFactory.instance();
			Element tgrElement = xFactory.compile("//permission[@uuid='"+PERMISSION_ID+"']", Filters.element()).evaluateFirst(userPermissionDocument);
			if (tgrElement != null) {
				tgrElement.setText("true");
			}
			
		}

		return userPermissionDocument;
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
