package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_IMAGE_VISIABLE")
public class UpdateImageVisiable extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String reqSql = null;
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (this.parameterObject.has("visiable")) {
			int visiable = this.parameterObject.getInt("visiable");
			if (visiable == 0) {
				reqSql = "kr.or.visitkorea.system.ImageMapper.setupImageVisiableFalse";
			} else {
				reqSql = "kr.or.visitkorea.system.ImageMapper.setupImageVisiableTrue";
			}
		}
		if (this.parameterObject.has("imgId"))
			params.put("imgId", this.parameterObject.getString("imgId"));
		
		int result = sqlSession.update(reqSql, params);
		
		if (result != 1) {
			resultHeaderObject.put("process", "fail");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
