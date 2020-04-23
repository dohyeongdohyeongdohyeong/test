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
@BusinessMapping(id="INSERT_ALL_TAG_KEYWORDS")
public class InsertAllTagKeywords extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		String querycondition = parameterObject.getString("querycondition");
		String odmId = parameterObject.getString("odmId");
		
		int deleteFlag = sqlSession.delete( 
			"kr.or.visitkorea.system.DepartmentMapper.removeAllTagsFromOdmId" , 
			odmId);
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("querycondition", querycondition);
		
		int insertFlag = sqlSession.insert( 
			"kr.or.visitkorea.system.DepartmentMapper.insertAllTagKeywords" , 
			paramterMap);

		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.put("delete", deleteFlag);
		resultJSONObject.put("insert", insertFlag);
		resultBodyObject.put("result", resultJSONObject);
		
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
