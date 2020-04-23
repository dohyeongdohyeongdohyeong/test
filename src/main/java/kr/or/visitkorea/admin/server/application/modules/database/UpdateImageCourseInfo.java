package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
 
@BusinessMapping(id="UPDATE_IMAGE_COURSE_INFO")
public class UpdateImageCourseInfo extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		String cotId = parameterObject.getString("cotId");
		String subContentId = parameterObject.getString("subContentId");
		String uuid = parameterObject.getString("uuid");
		String ext = parameterObject.getString("ext");

		String uuidPath = getUUIDPath(uuid, ext);
		
		System.out.println("uuidPath :: " + uuidPath);
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("imgId", uuid);
		paramterMap.put("cotId", cotId);
		paramterMap.put("imagePath", uuidPath);
		paramterMap.put("subContentId", subContentId);
		
		//insert into IMAGE(IMG_ID, COT_ID, IMAGE_PATH) values( #{imgId}, #{cotId}, #{imagePath});
		 
		int insertResult = sqlSession.insert( 
				"kr.or.visitkorea.system.DatabaseMapper.insertImage" , 
				paramterMap );

		//	update IMAGE SET SUB_DETAIL_IMG=#{imgId} WHERE COT_ID = #{cotId} and SUB_CONTENT_ID = #{subContentId};

		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.DatabaseMapper.updateCourseImage" , 
				paramterMap );
		
		JSONObject json = new JSONObject();
		json.put("insertImage", insertResult);
		json.put("updateResult", updateResult);
		
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateForModifiedDate" , paramterMap );
			resultBodyObject.put("result", json);
		}
	}

	private String getUUIDPath(String uuid, String ext) {
		
		String pathStr = "";
		String[] dashArray = uuid.split("-");
		
		for (String dash : dashArray) {
			pathStr += ("/" + dash.substring(0,  2));
		}
		
		return pathStr + "/" + uuid + "." + ext;
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
