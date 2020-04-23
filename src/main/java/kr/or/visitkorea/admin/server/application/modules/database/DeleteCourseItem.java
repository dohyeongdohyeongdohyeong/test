package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="DELETE_COURSE_ITEM")
public class DeleteCourseItem extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		String subContentId = parameterObject.getString("subContentId");
		String cotId = parameterObject.getString("cotId");

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("subContentId", subContentId);
		paramterMap.put("cotId", cotId);
		
		int updateResult = sqlSession.delete( 
				"kr.or.visitkorea.system.DatabaseMapper.deleteCourseItem" , 
				paramterMap );
		
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.getCourseInfo" , 
				paramterMap );

		JSONObject json = new JSONObject();
		
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", returnMap);
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
