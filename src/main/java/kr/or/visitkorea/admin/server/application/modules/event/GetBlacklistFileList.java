package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_BLACK_LIST_FILE_LIST")
public class GetBlacklistFileList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		List<HashMap<String, Object>> resultMap = 
				sqlSession.selectList("kr.or.visitkorea.system.EventBlacklistMapper.GetBlacklistFileList");
		int resultCnt = 
				sqlSession.selectOne("kr.or.visitkorea.system.EventBlacklistMapper.GetBlacklistFileListCnt");
		
		JSONArray resultArr = new JSONArray(resultMap);
		
		if (resultArr.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultCnt", resultCnt);
		}
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		// TODO Auto-generated method stub
		
	}

}
