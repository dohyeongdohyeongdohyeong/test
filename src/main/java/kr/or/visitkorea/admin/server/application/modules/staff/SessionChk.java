package kr.or.visitkorea.admin.server.application.modules.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="SESSION_CHK")
public class SessionChk extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String usrid = (String) this.getRequest().getSession().getAttribute("usrId");
		
		if (usrid != null) {
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
			paramterMap.put("usrId", usrid);
			
			System.out.println("SESSION_CHK.run.usrid ::" + usrid);

			HashMap<String, Object> returnMap = sqlSession.selectOne("kr.or.visitkorea.system.StaffMapper.userSelect", usrid);
			
			JSONObject returnObj = new JSONObject(returnMap);
			
			if (returnObj.toString() == null) {
				resultHeaderObject.put("process", "fail");
				resultHeaderObject.put("ment", "등록된 사용자가 아닙니다. 관리자에게 문의해 주세요.");
			} else {
				resultBodyObject.put("result", returnObj);
				
				HashMap<String, Object> permitParamterMap = new HashMap<String, Object>();
				permitParamterMap.put("usrId", returnObj.get("USR_ID"));
				
				sqlSession.update("kr.or.visitkorea.system.StaffMapper.userLastLogin", permitParamterMap);
				
				resultBodyObject.put("menus", XML.toJSONObject(new XMLOutputter(Format.getPrettyFormat()).outputString(getTargetMenus(permitParamterMap))));
				
				HashMap<String, Object> permitParamterMap2 = new HashMap<String, Object>();
				permitParamterMap2.put("usrId", returnObj.get("USR_ID"));
				
				List<HashMap<String, Object>> permitReturnMap = 
						sqlSession.selectList("kr.or.visitkorea.system.PermissionMapper.permission", permitParamterMap2);
				
				Document userPermissionDocument = buildUserPermission(permitReturnMap);
				
		        try {
	
		            JSONObject permitTableObj = new JSONObject();
		            JSONObject permitCaptionObj = new JSONObject();
		            
		    	    XPathFactory xFactory = XPathFactory.instance();
		    	    List<Element> uuidElements = xFactory.compile("//permission[@uuid]", Filters.element()).evaluate(userPermissionDocument);
	
		    	    for (Element element : uuidElements) {
		    	    	String permissionId = element.getAttributeValue("uuid");
		    	    	
		    	    	Element tmpElement = new Element("permission");
		    	    	tmpElement.setAttribute("uuid", permissionId);
		    	    	tmpElement.setAttribute("value", element.getTextTrim());
		    	    	
		    	    	permitTableObj.put(permissionId, element.getTextTrim());
		    	    	
		    	    	JSONObject permitObj = new JSONObject();
		    	    	permitObj.put("caption", element.getAttributeValue("caption"));
		    	    	permitObj.put("parentCaption", element.getParentElement().getAttributeValue("caption"));
		    	    	permitCaptionObj.put(permissionId, permitObj);
		    	    }
		    	    
		            resultBodyObject.put("permission", permitTableObj);
		            resultBodyObject.put("permissionCaption", permitCaptionObj);
		            
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
			}
		} else {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "userid not found~!.");
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
				menuContentXml.removeChildren("menu");
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
			Element permParentElement = perm.getParentElement();
			if (personalPermission.contains(perm.getAttributeValue("uuid"))) {
				Element menuContentXml = permParentElement.clone();
				menuContentXml.removeChildren("menu");
				retElement.addContent(menuContentXml);
				checkPermission(personalPermission, permParentElement, menuContentXml);
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
