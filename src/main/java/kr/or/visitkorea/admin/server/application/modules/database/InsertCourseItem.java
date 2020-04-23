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
@BusinessMapping(id="INSERT_COURSE_ITEM")
public class InsertCourseItem extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		String cotId = parameterObject.getString("cotId");
		String cid = parameterObject.getString("cid");
		String imgId = parameterObject.getString("imgId");
		String overview = parameterObject.getString("overview");
		String title = parameterObject.getString("title");
/**
 * 			parameterJSON.put("cmd", new JSONString("INSERT_COURSE_ITEM"));
			parameterJSON.put("cotId", new JSONString(cotId));
			parameterJSON.put("cid", new JSONString(getString(recObject, "CID", "")));
			parameterJSON.put("imgId", new JSONString(getString(recObject, "IMG_ID", "")));
			parameterJSON.put("overview", new JSONString(getString(recObject, "OVER_VIEW", "")));
			parameterJSON.put("title", new JSONString(getString(recObject, "TITLE", "")));
 */
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cotId", cotId);
		paramterMap.put("cid", cid);
		if (!imgId.equals("")) paramterMap.put("imgId", imgId);
		paramterMap.put("overview", overview.replaceAll("\'", "\'\'"));
		paramterMap.put("title", title);
		
		int updateResult = sqlSession.delete( 
				"kr.or.visitkorea.system.DatabaseMapper.insertCourseItem" , 
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
