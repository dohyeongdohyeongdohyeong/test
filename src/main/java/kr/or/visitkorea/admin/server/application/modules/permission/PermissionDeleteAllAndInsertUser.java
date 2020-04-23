package kr.or.visitkorea.admin.server.application.modules.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="PERMISSION_INS_ALL_USER")
public class PermissionDeleteAllAndInsertUser extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String usrId = parameterObject.getString("usrId");
		int permission = parameterObject.getInt("permission");
		String editUsrId = parameterObject.getString("editUsrId");

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("usrId", usrId);

		// all permission delete~!
		int deleteResult = sqlSession.update( 
				"kr.or.visitkorea.system.PermissionMapper.permissionDeleteForTgrUser",
				paramterMap);
		
		List<String> systemPermission = new ArrayList<String>();
		systemPermission.add("1b958c46-e9d2-410f-af52-c6c16783b4ca");
		systemPermission.add("b1c21dfc-0400-4853-a841-ef87957b1d8e");
		systemPermission.add("04c8092c-d5e7-4682-bef5-ee1983e509c9");
		systemPermission.add("2c0a4cf2-9988-40d1-ada4-d28cefecd279");
		systemPermission.add("69807087-dd72-4857-8825-522902971146");
		systemPermission.add("b867863b-8ab8-46af-a8be-2f1e888d42f7");
		systemPermission.add("89f97334-c9f5-4c45-b9e9-136523465e77");
		systemPermission.add("9d6a68f8-b061-4f54-9b4f-566a7230102b");
		systemPermission.add("e910c29e-412e-49ab-9a1b-2af2fe8209b3");
		systemPermission.add("e273c487-3696-11ea-b70a-020027310001");
		systemPermission.add("d9b68d4b-3696-11ea-b70a-020027310001");
		systemPermission.add("df923938-3696-11ea-b70a-020027310001");
		
		// all permission insert~!
	    XPathFactory xFactory = XPathFactory.instance();
	    List<Element> uuidElements = xFactory.compile(
	    		"//permission[@uuid]", 
	    		Filters.element()).evaluate(Call.getPermissionDocument());

	    int[] insertResult = new int[uuidElements.size()];
	    int startIndex = 0;
	    for (Element element : uuidElements) {

	    	String perm = element.getAttributeValue("uuid") ;
	    	
	    	if (!systemPermission.contains(perm)) {
				paramterMap = new HashMap<String, Object>();
				paramterMap.put("usrId", parameterObject.getString("usrId"));
				paramterMap.put("permissionId", element.getAttributeValue("uuid"));
				paramterMap.put("permission", 1);
	
				// all permission delete~!
				insertResult[startIndex] = sqlSession.insert( 
					"kr.or.visitkorea.system.PermissionMapper.permissionInsert",
					paramterMap);
	    	}

	    }
	    
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("usrId", usrId);
		parameterMap.put("permissionId", "-");
		parameterMap.put("permission", permission);
		parameterMap.put("editUsrId", editUsrId);
		parameterMap.put("parentCaption", "전체");
		parameterMap.put("caption", "시스템 제외");
		
		sqlSession.insert("kr.or.visitkorea.system.PermissionMapper.insertPermissionHistory", parameterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("delete", deleteResult);
		retJson.append("insert", insertResult);
		
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
