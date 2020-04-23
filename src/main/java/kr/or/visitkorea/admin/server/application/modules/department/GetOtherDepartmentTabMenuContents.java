package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="GET_OTHER_DEPARTMENT_TAB_MENU_CONTENT")
public class GetOtherDepartmentTabMenuContents extends AbstractModule{

	private JSONObject resultObj;
	private List<HashMap<String, Object>>  contentList;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		String odmId = UUID.randomUUID().toString();
		String otdId = parameterObject.getString("otdId");
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("odmId", odmId);
		paramterMap.put("otdId", otdId);
		
		contentList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getTabMenu" , 
				paramterMap );

		if (contentList.isEmpty()) {
			
			sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.insertTabMenu" , 
				paramterMap );

			contentList = sqlSession.selectList( 
					"kr.or.visitkorea.system.DepartmentMapper.getTabMenu" , 
					paramterMap );
		}
		
		resultObj = new JSONObject();
		resultObj.put("contents", new JSONArray(contentList));
		
		String convertJSONString = resultObj.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {

			resultBodyObject.put("result", resultObj);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
