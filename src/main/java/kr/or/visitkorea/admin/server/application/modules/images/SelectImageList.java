package kr.or.visitkorea.admin.server.application.modules.images;

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
@BusinessMapping(id="SELECT_IMAGE_LIST")
public class SelectImageList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cotId", parameterObject.getString("cotId"));
		String Type = parameterObject.getString("Type");
		List<HashMap<String, Object>> returnMap = null;
		if(Type.equals("DB")) {
			returnMap = sqlSession.selectList( 
					"kr.or.visitkorea.system.ImageMapper.selectContentImage" , 
					paramterMap );
		} else {
			returnMap = sqlSession.selectList( 
					"kr.or.visitkorea.system.ImageMapper.selectArticleContentImage" , 
					paramterMap );
		}
		
		
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		//System.out.println("SelectImageList.result.JSONArray :: " + convertJSONString);

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
