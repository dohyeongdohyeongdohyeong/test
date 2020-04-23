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
@BusinessMapping(id="GET_OTHER_DEPARTMENT_MAIN")
public class GetOtherDepartmentMain extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("otdId", parameterObject.getString("otdId"));
		paramterMap.put("isMain", parameterObject.getString("isMain"));

		List<HashMap<String, Object>> showcaseList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getShowcase" , 
				paramterMap );

		List<HashMap<String, Object>> AList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getAreaA" , 
				paramterMap );

		List<HashMap<String, Object>> BList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getAreaB" , 
				paramterMap );

		List<HashMap<String, Object>> CList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getAreaC" ,  
				paramterMap );

		List<HashMap<String, Object>> ShowcaseWithTitleList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getAreaShowcaseWithTitle" , 
				paramterMap );

		List<HashMap<String, Object>> tagList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getTags" , 
				paramterMap );

		HashMap<String, Object> serviceInfo = sqlSession.selectOne( 
				"kr.or.visitkorea.system.DepartmentMapper.getService" , 
				paramterMap );
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("showcase", new JSONArray(showcaseList));
		resultObj.put("A", new JSONArray(AList));
		resultObj.put("B", new JSONArray(BList));
		resultObj.put("C", new JSONArray(CList));
		resultObj.put("SWT", new JSONArray(ShowcaseWithTitleList));
		resultObj.put("tag", new JSONArray(tagList));
		resultObj.put("service", new JSONObject(serviceInfo));
		
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
