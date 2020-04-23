package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_PAMPHLET")
public class InsertPamphlet extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONArray pamphletArr = null;
		String cotId = null;
		if (this.parameterObject.has("pamphletList"))
			pamphletArr = this.parameterObject.getJSONArray("pamphletList");
		if (this.parameterObject.has("cotId"))
			cotId = this.parameterObject.getString("cotId");

		HashMap<String, Object> deleteParams = new HashMap<>();
		deleteParams.put("cotId", cotId);
		
		sqlSession.delete("kr.or.visitkorea.system.DatabaseMapper.deletePamphletWithCotId", deleteParams);
		
		List<HashMap<String, Object>> pamphletList = new ArrayList<>();
		List<HashMap<String, Object>> pamphletContentsList = new ArrayList<>();
		for (int i = 0; i < pamphletArr.length(); i++) {
			JSONObject obj = pamphletArr.getJSONObject(i);
			HashMap<String, Object> params = new HashMap<>();
			String pplId = obj.has("pplId") ? obj.getString("pplId") : UUID.randomUUID().toString();
			params.put("pplId", pplId);
			params.put("cotId", obj.getString("cotId"));
			params.put("view", obj.getInt("view"));
			params.put("idx", obj.getInt("idx"));
			if (obj.has("title"))
				params.put("title", obj.getString("title"));
			if (obj.has("textColor"))
				params.put("textColor", obj.getString("textColor"));
			pamphletList.add(params);
			sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertPamphlet", params);
			sqlSession.delete("kr.or.visitkorea.system.DatabaseMapper.deletePamphletContent", params);
			
			JSONArray contentsArr = obj.getJSONArray("contents");
			for (int j = 0; j < contentsArr.length(); j++) {
				JSONObject contentsObj = contentsArr.getJSONObject(j);
				HashMap<String, Object> contentParams = new HashMap<String, Object>();
				contentParams.put("cotId", cotId);
				contentParams.put("pplId", pplId);
				contentParams.put("ppcId", contentsObj.getString("ppcId"));
				contentParams.put("imgId", contentsObj.getString("imgId"));
				contentParams.put("idx", contentsObj.getInt("idx"));
				contentParams.put("imgPath", contentsObj.getString("imgPath"));
				if (contentsObj.has("imgDesc"))
					contentParams.put("imgDescription", contentsObj.getString("imgDesc"));
				else
					contentParams.put("imgDescription", "-");
				pamphletContentsList.add(contentParams);
				
				HashMap<String, Object> imgId = new HashMap<String, Object>();
				imgId.put("imgId", contentsObj.getString("imgId"));
				
				sqlSession.delete("kr.or.visitkorea.system.ImageMapper.deleteWithImgId", imgId);
				sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", contentParams);

				sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertPamphletContent", contentParams);
			}
		}

//		if (pamphletList.size() != 0)
//			sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertPamphlet", pamphletList);
//		if (pamphletContentsList.size() != 0) {
//			sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertPamphletContent", pamphletContentsList);
//		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
