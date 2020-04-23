package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_SM_IMAGE_BANNER")
public class InsertSMImageBanner extends AbstractModule{


	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		paramterMap.put("imgDescription", parameterObject.get("img_desc"));
		paramterMap.put("img_link", parameterObject.get("img_link"));
		paramterMap.put("img_title", parameterObject.get("img_title"));
		paramterMap.put("order", parameterObject.get("order"));
		if(parameterObject.has("imgId"))
			paramterMap.put("imgId", parameterObject.get("imgId"));
		if(parameterObject.has("NewimgId")) {
			paramterMap.put("NewimgId", parameterObject.get("NewimgId"));
		}
		int insertResult = -1;
		if(parameterObject.has("imgPath")) {
			paramterMap.put("imgPath", parameterObject.get("imgPath"));
			sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImagePathAndImgId", paramterMap);
			paramterMap.put("imgId", parameterObject.get("NewimgId"));
		}
		
			paramterMap.put("desc", parameterObject.get("img_desc"));
			sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImageDescription", paramterMap);
		
		
		sqlSession.update("kr.or.visitkorea.system.SiteMainMapper.UpdateImageBanner", paramterMap);
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("insertResults", insertResult);

		if (resultObj.toString().equals("null")) {
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
