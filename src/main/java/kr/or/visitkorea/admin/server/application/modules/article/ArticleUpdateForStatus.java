package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_FOR_DELETE_STATUS")
public class ArticleUpdateForStatus extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cotId", parameterObject.getString("cotId"));

		int retInt = sqlSession.update( 
				"kr.or.visitkorea.system.ArticleMapper.updateForStatus" , 
				paramterMap );
		
		JSONObject retObj = new JSONObject();
		retObj.put("update", retInt);
		
		if (retInt == -1) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", retObj);
		}		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
