package kr.or.visitkorea.admin.server.application.modules.news;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_NEWS_IMAGE")
public class InsertNewsImage extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
 
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("nwsId", parameterObject.getString("nwsId"));
		paramterMap.put("nsiId", parameterObject.getString("nsiId"));
		paramterMap.put("imgId", parameterObject.getString("imgId"));
		paramterMap.put("imgPath", parameterObject.getString("imgPath"));
		paramterMap.put("imgDescription", parameterObject.getString("imgDesc")); 

		int insertImageResult = sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", paramterMap);
		
		int insertNewsImageResult = sqlSession.insert("kr.or.visitkorea.system.NewsMapper.insertNewsImage", paramterMap);
		JSONObject json = new JSONObject();
		json.put("insertNewsImageResult", insertNewsImageResult);
		json.put("insertImageResult", insertImageResult);
		
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", json);
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
