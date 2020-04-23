package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.images
 * @author junhohong
 *
 */
@BusinessMapping(id="UPDATE_IMAGE_IS_CAPTION")
public class UpdateImageIsCaption extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
 
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("imgId", parameterObject.getString("imgId"));
		paramterMap.put("iscaption", parameterObject.getNumber("iscaption"));

		int updateResult = sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateisCaption", paramterMap);
		
		JSONObject json = new JSONObject();
		json.put("updateResult", updateResult);
		
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
