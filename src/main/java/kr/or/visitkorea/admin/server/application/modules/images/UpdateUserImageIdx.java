package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_USER_IMAGE_IDX")
public class UpdateUserImageIdx extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONArray array = new JSONArray();
		if (this.parameterObject.has("imgList"))
			array = this.parameterObject.getJSONArray("imgList");
			
		int size = array.length();
		for (int i = 0; i < size; i++) {
			JSONObject obj = array.getJSONObject(i);
			HashMap<String, Object> params = new HashMap<>();
			params.put("imgId", obj.getString("imgId"));
			params.put("umgId", obj.getString("umgId"));
			params.put("idx", obj.getInt("idx"));
			params.put("visiable", obj.getInt("visiable"));
			sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateUserImageIdx", params);
			
			if ((int) params.get("visiable") == 0) {
				sqlSession.update("kr.or.visitkorea.system.ImageMapper.setupImageVisiableFalse", params);
			} else {
				sqlSession.update("kr.or.visitkorea.system.ImageMapper.setupImageVisiableTrue", params);
			}
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
