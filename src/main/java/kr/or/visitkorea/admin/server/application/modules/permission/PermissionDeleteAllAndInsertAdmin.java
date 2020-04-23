package kr.or.visitkorea.admin.server.application.modules.permission;

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
@BusinessMapping(id="PERMISSION_INS_ALL_ADMIN")
public class PermissionDeleteAllAndInsertAdmin extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String usrId = parameterObject.getString("usrId");
		int permission = parameterObject.getInt("permission");
		String editUsrId = parameterObject.getString("editUsrId");
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("usrId", usrId);

		int deleteResult = sqlSession.update( 
				"kr.or.visitkorea.system.PermissionMapper.permissionDeleteForTgrUser",
				paramterMap);
		
	    XPathFactory xFactory = XPathFactory.instance();
	    List<Element> uuidElements = xFactory.compile(
	    		"//permission[@uuid]", 
	    		Filters.element()).evaluate(Call.getPermissionDocument());

	    int[] insertResult = new int[uuidElements.size()];
	    int startIndex = 0;
	    for (Element element : uuidElements) {

			paramterMap = new HashMap<String, Object>();
			paramterMap.put("usrId", parameterObject.getString("usrId"));
			paramterMap.put("permissionId", element.getAttributeValue("uuid"));
			paramterMap.put("permission", 1);

			// all permission delete~!
			insertResult[startIndex] = sqlSession.insert( 
				"kr.or.visitkorea.system.PermissionMapper.permissionInsert",
				paramterMap);

	    }
	    
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("usrId", usrId);
		parameterMap.put("permissionId", "-");
		parameterMap.put("permission", permission);
		parameterMap.put("editUsrId", editUsrId);
		parameterMap.put("parentCaption", "전체");
		parameterMap.put("caption", "시스템 포함");
		
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
