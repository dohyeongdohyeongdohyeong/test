package kr.or.visitkorea.admin.server.application.modules.department_info;

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
@BusinessMapping(id="SELECT_DEPARTMENT_LIST")
public class SelectDepartmentList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
//		System.out.println("SparameterObject.getString(\"des\") :: " + parameterObject.get("des"));
		if(parameterObject.has("dtype"))
			paramterMap.put("dtype", parameterObject.getInt("dtype"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.DepartmentInfoMapper.selectDepartmentList" , 
			paramterMap );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentInfoMapper.selectDepartmentListCnt" , 
				paramterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		System.out.println("selectDepartmentList.result.JSONArray :: " + convertJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
//			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", json);
			resultBodyObject.put("resultCnt", returnCnt.get(0));
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
