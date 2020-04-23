package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="IMAGE_PATH_UPDATE_WITH_IMGID")
public class ImagePathUpdateWithImgId extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
 
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("imgId", parameterObject.getString("imgId"));
		paramterMap.put("NewimgId", parameterObject.getString("NewimgId"));
		paramterMap.put("imgPath", parameterObject.getString("imgPath"));

		
		int updateResult = sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImagePathAndImgId", paramterMap);
		
		int updateResult2 = 0;
		if(updateResult >0)
			updateResult2 = sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateArticleContentImgId", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("updateResult", updateResult);
		json.put("updateResult2", updateResult2);
		
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
