package kr.or.visitkorea.admin.server.application.modules.database.killercontent;

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
@BusinessMapping(id="UPDATE_KILLER_CONTENT_BANNER")
public class UpdateKillerContentBanner extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		int status = 0;
		if(parameterObject.has("kcbId")) 
		paramterMap.put("kcbId", parameterObject.getString("kcbId"));
		if(parameterObject.has("desc")) 
			paramterMap.put("imgDescription", parameterObject.getString("desc"));
		if(parameterObject.has("imgId")) 
			paramterMap.put("imgId", parameterObject.getString("imgId"));		
		if(parameterObject.has("ImgPath")) 
			paramterMap.put("imgPath", parameterObject.getString("ImgPath"));
		if(parameterObject.has("linkurl")) 
			paramterMap.put("linkurl", parameterObject.getString("linkurl"));		
		if(parameterObject.has("title")) 
			paramterMap.put("title", parameterObject.getString("title"));
		if(parameterObject.has("status")) {
			paramterMap.put("status", parameterObject.getNumber("status"));		
			status = (int) parameterObject.getNumber("status");
		}
			
			
		
		String imgchk = sqlSession.selectOne( 
				"kr.or.visitkorea.system.BannerMapper.SelectKillerContentImage" , 
				paramterMap);
		
		System.out.println("imgchk :: " + imgchk);
		if(parameterObject.has("imgId")) {
			if(imgchk != null) {
				paramterMap.put("IMG_UPDATE", imgchk);		
				sqlSession.update( 
				"kr.or.visitkorea.system.ImageMapper.UpdateWithImgId" , 
				paramterMap );
			} else {
				sqlSession.insert( 
				"kr.or.visitkorea.system.ImageMapper.insertNotCotId" , 
				paramterMap );
			}
		}
		
		
		if(status > 0 ) {
			sqlSession.update( 
					"kr.or.visitkorea.system.BannerMapper.UpdateKillerContentStatus" , 
					paramterMap );
		}
		
		int Update = sqlSession.update( 
				"kr.or.visitkorea.system.BannerMapper.UpdateKillerContentBanner" , 
				paramterMap );
		

		if (Update == 0) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", Update);
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
