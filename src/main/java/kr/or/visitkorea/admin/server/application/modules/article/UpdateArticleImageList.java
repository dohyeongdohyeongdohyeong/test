package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


@BusinessMapping(id="UPDATE_ARTICLE_IMAGE_LIST")
public class UpdateArticleImageList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		String orderString = parameterObject.getString("ORDER");
		String COT_ID = parameterObject.getString("COT_ID");
		List<Map<String, Integer>> orderList = new ArrayList<Map<String, Integer>>();
		JSONObject json = new JSONObject();
		
		String[] firstSplit = orderString.split(",");
		
		for (String first : firstSplit) {
			
			String[] secondSplit = first.split("_");
			
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("ORDER", new Integer(secondSplit[0]));
			map.put("COT_ID", COT_ID);
			map.put("IMG_ID", secondSplit[1]);
			System.out.println("IMG_ID["+secondSplit[1]+"] , ORDER["+secondSplit[0]+"] ");
			
			int insertResult = sqlSession.update("kr.or.visitkorea.system.ImageMapper.UpdateArticleImageList", map);
			json.put(secondSplit[0], insertResult);
		}

		int retInt = sqlSession.update( 
				"kr.or.visitkorea.system.ArticleMapper.updateOrderImage" , 
				paramterMap );
		
		JSONObject retObj = new JSONObject();
		retObj.put("update", retInt);
		
		if (retInt == -1) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
//			if(parameterObject.has("imgId")) {
//				paramterMap.put("imgId", parameterObject.getNumber("imgId"));
				sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateForModifiedDateImg" , paramterMap );
//			}
			resultBodyObject.put("result", retObj);
		}		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
