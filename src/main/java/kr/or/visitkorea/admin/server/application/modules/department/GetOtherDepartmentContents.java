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
@BusinessMapping(id="GET_OTHER_DEPARTMENT_CONTENT_LIST")
public class GetOtherDepartmentContents extends AbstractModule{

	private List<HashMap<String, Object>> contentList;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		String odmId = UUID.randomUUID().toString();
		String otdId = parameterObject.getString("otdId");
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("odmId", odmId);
		paramterMap.put("otdId", otdId);
		
		contentList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getContentList" , 
				paramterMap );

		if (contentList.isEmpty()) {
			
			sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.insertContentList" , 
				paramterMap );

			contentList = sqlSession.selectList( 
					"kr.or.visitkorea.system.DepartmentMapper.getContentList" , 
					paramterMap );
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("contents", new JSONArray(contentList));
		String convertJSONString = resultObj.toString();

		if (convertJSONString.equals("null")) {
			
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{

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
