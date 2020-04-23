package kr.or.visitkorea.admin.server.application.modules.tags;

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
@BusinessMapping(id="INSERT_MEMBER_TAG")
public class InsertMemberTag extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("cotId", parameterObject.getString("cotId"));
		parameterMap.put("tagId", parameterObject.getString("tagId"));

		
		HashMap<String, Object> cntMap = sqlSession.selectOne("kr.or.visitkorea.system.TagsMapper.selectMemberTag", parameterMap);
		Long cntLong = Long.parseLong(cntMap.get("CNT").toString());
		
		
		if (cntLong == 0) {
		
			System.out.println(String.format("(%s, %s) :: %s", parameterMap.get("tagId"), parameterMap.get("cotId"), cntLong));
			sqlSession.update("kr.or.visitkorea.system.TagsMapper.insertMemberTag", parameterMap);
			
		}
		
		
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.TagsMapper.searchContentTags" , 
				parameterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", new JSONArray(convertJSONString));
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
