package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="GET_OTHER_DEPARTMENT_CONTENT")
public class GetOtherDepartmentContentsList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("otdId", parameterObject.getString("otdId"));
		paramterMap.put("keyword", parameterObject.getString("keyword"));
		paramterMap.put("mode", parameterObject.getInt("mode"));
		
		if(parameterObject.has("section")) {
			paramterMap.put("section", parameterObject.getString("section"));
		}

		List<HashMap<String, Object>> contentList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getContents" , 
				paramterMap );

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
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
