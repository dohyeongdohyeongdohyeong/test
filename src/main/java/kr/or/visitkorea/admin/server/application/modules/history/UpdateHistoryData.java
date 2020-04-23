package kr.or.visitkorea.admin.server.application.modules.history;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


/**
 * 
 * history 데이터를 받아서 DB 테이블에 history 데이터를 추가하는 모듈.
 * 
 * @author 		dohyeong
 * @authorDate	2020-03-25
 * @see			kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.HistoryManagementDialog
 *
 */
@BusinessMapping (id="POST_HISTORY_DATA")
public class UpdateHistoryData extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		final String mapperLocation = "kr.or.visitkorea.system.HistoryMapper";
		final String deleteSqlId = "deleteHistoryData";
		final String insertSqlId = "insertHistoryData";
		
		
		
		JSONArray dataArray = super.parameterObject.getJSONArray("data");
		if(dataArray.length() == 0) {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "전송된 데이터가 없습니다.");
			
			return;
		}
		
		
		int removeAmount = 0;
		int insertAmount = 0;
		for(Object tmp : dataArray) {
			JSONObject data = (JSONObject) tmp;
			
			if(data.has("HIST_ID")) {
				removeAmount += super.sqlSession.delete(mapperLocation + "." + deleteSqlId, data.get("HIST_ID").toString());
			} else {
				data.put("HIST_ID", UUID.randomUUID().toString());
				data.put("REG_ID", super.getRequest().getSession().getAttribute("usrId").toString());
				
				HashMap<String, Object> dataMap = convertJSONToMap(data);
				insertAmount += super.sqlSession.update(mapperLocation + "." + insertSqlId, dataMap);
			}
		}
		
		if(removeAmount + insertAmount == dataArray.length()) {
			resultBodyObject.put("result", "히스토리 데이터 편집 성공!! (추가: " + insertAmount + "개, 제거: " + removeAmount + "개)");
		} else if(removeAmount + insertAmount > 0) {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "히스토리 데이터 편집이 일부만 적용되었습니다. 재설정 버튼 클릭 후에 다시 작업해주세요.. (L:" + dataArray.length() + ",  R:" + removeAmount + ",  I:" + insertAmount + ")");
		} else if(removeAmount + insertAmount == 0) {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "DB 데이터를 수정할 수가 없습니다. 계속되면 관리자에게 문의하여주세요.");
		}
		
	}
	
	
	/**
	 * JSON 형식의 데이터를 HashMap<key, value> 쌍의 데이터 형식으로 변환하여 반환하는 메서드.
	 * 
	 * @param JSONObject
	 * @return HashMap<String, Object>
	 */
	private HashMap<String, Object> convertJSONToMap(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			Object value = jsonObject.get(key);
			
			map.put(key, value);
		}
		
		return map;
	}
	
	
	
	
	

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

}
