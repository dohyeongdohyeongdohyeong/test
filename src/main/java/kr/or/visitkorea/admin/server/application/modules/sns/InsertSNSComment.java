package kr.or.visitkorea.admin.server.application.modules.sns;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_SNS_COMMENT")
public class InsertSNSComment extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (this.parameterObject.has("cmtId"))
			params.put("cmtId", this.parameterObject.getString("cmtId"));
		if (this.parameterObject.has("imgId") && !this.parameterObject.getString("imgId").equals(""))
			params.put("imgId", this.parameterObject.getString("imgId"));
		if (this.parameterObject.has("imgPath"))
			params.put("imgPath", this.parameterObject.getString("imgPath"));
		if (this.parameterObject.has("comment"))
			params.put("comment", this.parameterObject.getString("comment"));
		if (this.parameterObject.has("parentId"))
			params.put("parentId", this.parameterObject.getString("parentId"));
		if (this.parameterObject.has("snsId"))
			params.put("snsId", this.parameterObject.getString("snsId"));
		if (this.parameterObject.has("depth"))
			params.put("depth", this.parameterObject.getInt("depth"));
		if (this.parameterObject.has("isView"))
			params.put("isView", this.parameterObject.getInt("isView"));
		
		if (params.containsKey("imgId"))
			sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", params);
		sqlSession.insert("kr.or.visitkorea.system.SnsMapper.insertSnsComment", params);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

	}

}
