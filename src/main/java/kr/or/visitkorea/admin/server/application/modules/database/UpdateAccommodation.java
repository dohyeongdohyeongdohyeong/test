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

@BusinessMapping(id="UPDATE_ACCOMMODATION")
public class UpdateAccommodation extends AbstractModule{

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
					"kr.or.visitkorea.system.DatabaseMapper.DeleteAccommodation" , 
					paramterMap );
		
		} 
		String VisiableList ="";
		if(parameterObject.has("VisiableImages")) {
					
			JSONArray VisiableImages = parameterObject.getJSONArray("VisiableImages");
			
			for (int i = 0; i < VisiableImages.length(); i++) {
	
				if (VisiableList.equals("")) {
					VisiableList += "'" + VisiableImages.getString(i) + "'";
				} else {
					VisiableList += ", '" + VisiableImages.getString(i) + "'";
				}
			}
			
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
			paramterMap.put("cotId",cotId);
			paramterMap.put("VisiableImages", VisiableList);
				
			 sqlSession.update( 
						"kr.or.visitkorea.system.DatabaseMapper.UpdateAccommodationVisiable" , 
						paramterMap );
			 
		}
		String  NotVisiableList ="";
		if(parameterObject.has("NotVisiableImages")) {
			
			JSONArray NotVisiableImages = parameterObject.getJSONArray("NotVisiableImages");
			
			for (int i = 0; i < NotVisiableImages.length(); i++) {
	
				if (NotVisiableList.equals("")) {
					NotVisiableList += "'" + NotVisiableImages.getString(i) + "'";
				} else {
					NotVisiableList += ", '" + NotVisiableImages.getString(i) + "'";
				}
			}
			
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
			paramterMap.put("cotId",cotId);
			paramterMap.put("NotVisiableImages", NotVisiableList);
				
			 sqlSession.update( 
						"kr.or.visitkorea.system.DatabaseMapper.UpdateAccommodationNotVisiable" , 
						paramterMap );
			 
		}
		
		int updateResult = -1;
		for (int i = 0; i < Images.length(); i++) {
			
			 updateResult = sqlSession.update( 
					"kr.or.visitkorea.system.DatabaseMapper.UpdateAccommodationImages" , 
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
		
		paramterMap.put("ROOM_IMG1",Images.getJSONObject(i).get("ROOM_IMG1").equals("") ? null : Images.getJSONObject(i).get("ROOM_IMG1"));
		paramterMap.put("ROOM_IMG2",Images.getJSONObject(i).get("ROOM_IMG2").equals("") ? null : Images.getJSONObject(i).get("ROOM_IMG2"));
		paramterMap.put("ROOM_IMG3",Images.getJSONObject(i).get("ROOM_IMG3").equals("") ? null : Images.getJSONObject(i).get("ROOM_IMG3"));
		paramterMap.put("ROOM_IMG4",Images.getJSONObject(i).get("ROOM_IMG4").equals("") ? null : Images.getJSONObject(i).get("ROOM_IMG4"));
		paramterMap.put("ROOM_IMG5",Images.getJSONObject(i).get("ROOM_IMG5").equals("") ? null : Images.getJSONObject(i).get("ROOM_IMG5"));
		paramterMap.put("ROOM_CODE",Images.getJSONObject(i).get("ROOM_CODE").equals("") ? null : Images.getJSONObject(i).get("ROOM_CODE"));
		paramterMap.put("ROOM_ORDER",Images.getJSONObject(i).get("ROOM_ORDER").equals("") ? null : Images.getJSONObject(i).get("ROOM_ORDER"));
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
