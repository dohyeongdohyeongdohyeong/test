package kr.or.visitkorea.admin.server.application.modules.sns;

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
@BusinessMapping(id="SELECT_SNSCOMMENT_LIST")
public class SelectSNSCommentList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		if (parameterObject.has("cotId"))
			params.put("cotId", parameterObject.getString("cotId"));
		if (parameterObject.has("depth"))
			params.put("depth", parameterObject.getInt("depth"));
		if (parameterObject.has("offset"))
			params.put("offset", parameterObject.getInt("offset"));
		if (parameterObject.has("parentId"))
			params.put("parentId", parameterObject.getString("parentId"));
		
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.SnsMapper.selectSnsCommentList" , 
				params );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.SnsMapper.selectSnsCommentListCnt" , 
				params );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		System.out.println("SelectSnsCommentList.result.JSONArray :: " + convertJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
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
