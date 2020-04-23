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
@BusinessMapping(id="DELETE_NEWS_IMAGE")
public class DeleteNewsImage extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
 
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("nsiId", parameterObject.getString("nsiId"));
		paramterMap.put("imgId", parameterObject.getString("imgId"));
		
		int DeleteNewsImageResult = sqlSession.delete("kr.or.visitkorea.system.NewsMapper.deleteNewsImage", paramterMap);
		
		int DeleteImageResult = sqlSession.delete("kr.or.visitkorea.system.ImageMapper.deleteWithImgId", paramterMap);

		
		
		JSONObject json = new JSONObject();
		json.put("DeleteNewsImageResult", DeleteNewsImageResult);
		json.put("DeleteImageResult", DeleteImageResult);
		
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
