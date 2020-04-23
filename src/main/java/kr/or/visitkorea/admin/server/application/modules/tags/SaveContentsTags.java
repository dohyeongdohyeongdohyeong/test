package kr.or.visitkorea.admin.server.application.modules.tags;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_CONTENTS_TAGS")
public class SaveContentsTags extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONArray model = this.parameterObject.getJSONObject("model").getJSONArray("TAGS");

		for (int i = 0; i < model.length(); i++) {
			JSONObject obj = model.getJSONObject(i);

			HashMap<String, Object> params = this.buildTagParameter(obj);

			//	STATUS :: 해당 값은 실제 DB에는 존재하지 않으며 메모리상에서 특정 태그의 상태를 구분하기위해 존재하는 값으로
			//			   수정, 삭제, 추가하는 각 시점에 임의로 부여되는 값입니다.
			//	-1. 삭제한 항목, 0. 기존에 존재했던 항목, 1. 신규로 생성된 항목
			int status = obj.getInt("STATUS");
			if (status == -1)
				sqlSession.delete("kr.or.visitkorea.system.TagsMapper.removeMemberTag", params);
			else if (status == 0)
				sqlSession.update("kr.or.visitkorea.system.TagsMapper.updateMemberTag", params);
			else if (status == 1)
				sqlSession.insert("kr.or.visitkorea.system.TagsMapper.insertMemberTag", params);
		}
	}

	private HashMap<String, Object> buildTagParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("tagId", obj.getString("tagId"));
		params.put("cotId", obj.getString("cotId"));
		params.put("isMasterTag", obj.getInt("isMasterTag"));
		return params;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
