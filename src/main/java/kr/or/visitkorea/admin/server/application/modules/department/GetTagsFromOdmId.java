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
@BusinessMapping(id="GET_TAGS_FROM_ODM_ID")
public class GetTagsFromOdmId extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		String odmId = parameterObject.getString("odmId");
		
		List<HashMap<String, Object>> contentList = sqlSession.selectList( 
				"kr.or.visitkorea.system.DepartmentMapper.getTagsFromOdmId" , 
				odmId );

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
