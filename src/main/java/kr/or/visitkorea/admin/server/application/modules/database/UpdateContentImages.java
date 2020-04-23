package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.jdom2.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


/**
 * @author Admin
 * 이미지 업로드 UpdateContentImages Module
 */

@BusinessMapping(id="UPDATE_CONTENT_IMAGES")
public class UpdateContentImages extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		String cotId = parameterObject.getString("cotId");
		
		JSONArray Images = parameterObject.getJSONArray("Images");
		
			
		String DeleteList ="";
		int DeleteResult = -1;
		if(parameterObject.has("DeleteImages")) {
			
			JSONArray DeleteImages = parameterObject.getJSONArray("DeleteImages");
			
			for (int i = 0; i < DeleteImages.length(); i++) {
	
				if (DeleteList.equals("")) {
					DeleteList += "'" + DeleteImages.getString(i) + "'";
				} else {
					DeleteList += ", '" + DeleteImages.getString(i) + "'";
				}
			}
			
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
			paramterMap.put("cotId",cotId);
			paramterMap.put("DeleteList", DeleteList);
			System.out.println(DeleteList);
			DeleteResult = sqlSession.delete( 
					"kr.or.visitkorea.system.DatabaseMapper.DeleteContentImage" , 
					paramterMap );
		
		}
		int updateResult = -1;
		for (int i = 0; i < Images.length(); i++) {
			
			 updateResult = sqlSession.update( 
					"kr.or.visitkorea.system.DatabaseMapper.UpdateContentImages" , 
					Images(i,Images,cotId) );
		}
		
		
		JSONObject json = new JSONObject();
		json.put("DeleteResult", DeleteResult);
		json.put("updateResult", updateResult);
		
		String convertJSONString = json.toString();
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", json);
		}
		
	}
	
	private HashMap<String, Object> Images(int i, JSONArray Images, String cotId ){
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		paramterMap.put("ImgId",Images.getJSONObject(i).get("IMG_ID"));
		paramterMap.put("ImgDesc",Images.getJSONObject(i).get("IMG_DESCRIPTION"));
		paramterMap.put("ImgOrder",Images.getJSONObject(i).get("IMG_ORDER"));
		paramterMap.put("IS_VISIABLE",Images.getJSONObject(i).get("IS_VISIABLE"));
		paramterMap.put("cotId",cotId);
		
		return paramterMap;
	};

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		// TODO Auto-generated method stub
		
	}

}
