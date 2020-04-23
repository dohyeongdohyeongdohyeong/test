package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_DEPT_AREA")
public class InsertDeptArea extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		paramterMap.put("COMP_ORDER", parameterObject.getInt("COMP_ORDER"));
		paramterMap.put("VIEW_TITLE", parameterObject.getInt("VIEW_TITLE"));
		paramterMap.put("TEMPLATE_ID", parameterObject.getString("TEMPLATE_ID"));
		paramterMap.put("MAIN_AREA",  parameterObject.getString("MAIN_AREA"));
		paramterMap.put("COMP_ID", parameterObject.getString("COMP_ID"));
		paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		paramterMap.put("TITLE", parameterObject.getString("TITLE"));
		
		int insertResult = sqlSession.insert("kr.or.visitkorea.system.DepartmentMapper.insertDeptArea", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("insertResult", insertResult);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", json);
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
