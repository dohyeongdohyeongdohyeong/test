package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_RELATED_CONTENT")
public class InsertRelatedContent extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("tbl", parameterObject.getString("tbl"));
		paramterMap.put("cotId", parameterObject.getString("cotId"));
		paramterMap.put("subCotId", parameterObject.getString("subCotId"));

		int retInt = sqlSession.update( 
				"kr.or.visitkorea.system.ArticleMapper.insertRelatedContent" , 
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
