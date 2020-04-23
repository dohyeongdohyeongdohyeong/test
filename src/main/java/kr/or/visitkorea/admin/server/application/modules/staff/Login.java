package kr.or.visitkorea.admin.server.application.modules.staff;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONObject;
import org.json.XML;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;
import kr.or.visitkorea.admin.shared.model.StaffVO;

@BusinessMapping(id="LOGIN")
public class Login extends AbstractModule {
	
	private final String PASSWORD_CHANGE_INTERVAL = "PASSWORD_CHANGE_INTERVAL";
	private final String SESSION_TIME_LIMIT = "SESSION_TIME_LIMIT";
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> paramterMap = new HashMap<>();
		
		if (parameterObject.has("stfId"))
			paramterMap.put("stfId", parameterObject.getString("stfId"));
		if (parameterObject.has("auth"))
			paramterMap.put("auth", parameterObject.getString("auth"));
		
		String ip = this.getLoginStaffIp(this.getRequest());
	    
		HashMap<String, Object> returnMap = 
				sqlSession.selectOne("kr.or.visitkorea.system.StaffMapper.loginChk", paramterMap);

		if (returnMap == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "로그인에 실패하였습니다.\n등록된 사용자가 아니거나 비밀번호가 틀립니다.\n비밀번호를 5회 잘못 입력 시 해당 계정은 자동으로 비활성 처리됩니다.");
			return;
		}
		
		int loginFailCnt = (int) returnMap.get("LOGIN_FAIL_CNT");
		if (loginFailCnt == 5) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("code", 5);
			resultHeaderObject.put("ment", "비밀번호를 5회 잘못입력했습니다. 해당 계정은 비활성화 상태입니다.");
			return;
		}
		
		long loginResult = (long) returnMap.get("LOGIN_RESULT");
		
		paramterMap.put("ip", ip);
		paramterMap.put("usrId", returnMap.get("USR_ID"));
		
		if (loginResult == 0) {
			paramterMap.put("loginFailCnt", loginFailCnt);
			paramterMap.put("type", 1);
			
			sqlSession.update("kr.or.visitkorea.system.StaffMapper.updateLoginFailCnt", paramterMap);
			sqlSession.insert("kr.or.visitkorea.system.StaffMapper.insertStaffLoginHistory", paramterMap);
			
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "로그인에 실패하였습니다.\n등록된 사용자가 아니거나 비밀번호가 틀립니다.\n비밀번호를 5회 잘못 입력 시 해당 계정은 자동으로 비활성 처리됩니다.");
			return;
		} 
		
		HashMap<String, Object> staffMap = 
				sqlSession.selectOne("kr.or.visitkorea.system.StaffMapper.userSelect", returnMap.get("USR_ID"));
		
		StaffVO staff = StaffVO.fromJson(new JSONObject(staffMap));
		
		boolean isChangePw = this.checkPasswordRefresh(staff.getPwRefreshDate());
		if (isChangePw) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("changePw", isChangePw);
			return;
		}
		
		boolean isDupli = this.sessionManager.isContains(staff.getUsrId());
		if (isDupli) {
			paramterMap.put("type", 2);
			
			sqlSession.insert("kr.or.visitkorea.system.StaffMapper.insertStaffLoginHistory", paramterMap);
			
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "다른 PC에서 이미 접속중인 계정입니다.\n동일한 계정으로 중복 로그인은 불가능합니다.");
			return;
		}
		
		int sessionTimeLimit = sqlSession.selectOne("kr.or.visitkorea.system.GlobalVariablesMapper.selectOneValue", SESSION_TIME_LIMIT);
		
		this.getRequest().getSession(true).invalidate();
		
		HttpSession session = this.getRequest().getSession(true);
		session.setAttribute("LOGIN_CHK", true);
		session.setAttribute("usrId", staff.getUsrId());
		session.setAttribute("stfId", staff.getStfId());
		session.setMaxInactiveInterval(sessionTimeLimit * 60);
		
		paramterMap.put("loginFailCnt", -1);
		paramterMap.put("usrId", staff.getUsrId());
		paramterMap.put("type", 0);
		
		sqlSession.insert("kr.or.visitkorea.system.StaffMapper.insertStaffLoginHistory", paramterMap);
		sqlSession.update("kr.or.visitkorea.system.StaffMapper.updateLoginFailCnt", paramterMap);
		
		HashMap<String, Object> permissionMap = new HashMap<>();
		permissionMap.put("usrId", staff.getUsrId());
		
		sqlSession.update("kr.or.visitkorea.system.StaffMapper.userLastLogin", permissionMap);

		Document permissionDoc = this.getTargetMenus(permissionMap);
		
		resultBodyObject.put("result", new JSONObject(returnMap));
		resultBodyObject.put("menus", XML.toJSONObject(new XMLOutputter(Format.getPrettyFormat()).outputString(permissionDoc)));
	}

	/**
	 *	최근 비밀번호 변경일을 조회하여 비밀번호 변경 의무가 존재하는지 확인	
	 */
	private boolean checkPasswordRefresh(LocalDateTime date) {
		if (date == null) {
			return true;
		}
		
		int passwordChangeInterval = sqlSession.selectOne("kr.or.visitkorea.system.GlobalVariablesMapper.selectOneValue", PASSWORD_CHANGE_INTERVAL);
		long period = date.toLocalDate().until(LocalDate.now(),ChronoUnit.DAYS);
		
		if (passwordChangeInterval <= period) {
			return true;
		}
		return false;
	}
	
	private Document getTargetMenus(HashMap<String, Object> permitParamterMap) {
		List<Map<String, Object>> results = sqlSession.selectList( "kr.or.visitkorea.system.PermissionMapper.permissionSelect" , permitParamterMap );
		List<String> personalPermission = new ArrayList<String>();
		convertResults(personalPermission, results);
		Document permissionDocument = Call.getPermissionDocument();
		Document retPersonalPermissionDocument = new Document();
		Element retPersonalPermissionRootElement = new Element("menu");
		retPersonalPermissionDocument.setRootElement(retPersonalPermissionRootElement);
		
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("/menu/menu/permission[@id='use']", Filters.element());
		List<Element> usePermissionList = xp.evaluate(permissionDocument);
		
		for (Element perm : usePermissionList) {
			
			String uuid = perm.getAttributeValue("uuid");
			if (personalPermission.contains(uuid)) {
				Element menuContentXml = perm.getParentElement().clone();
				retPersonalPermissionRootElement.addContent(menuContentXml);
				checkPermission(personalPermission, perm.getParentElement(), menuContentXml);			
			}
			
		}
		return retPersonalPermissionDocument;
	}

	private void checkPermission(List<String> personalPermission, Element target, Element retElement) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("menu/permission[@id='use']", Filters.element());
		List<Element> usePermissionList = xp.evaluate(target);
		
		for (Element perm : usePermissionList) {
			
			String uuid = perm.getAttributeValue("uuid");
			if (personalPermission.contains(perm.getAttributeValue("uuid"))) {
				Element menuContentXml = perm.getParentElement().clone();
				retElement.addContent(menuContentXml);
				checkPermission(personalPermission, perm.getParentElement(), menuContentXml);
			}
			
		}
		
	}

	private void convertResults(List<String> personalPermission, List<Map<String, Object>> results) {
		for (Map<String, Object> result: results) {
			Object permIdObject = result.get("PERMISSION_ID");
			if (permIdObject != null) {
				personalPermission.add((String)permIdObject);
			}
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
