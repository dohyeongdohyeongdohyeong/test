package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EVENT_USER_RANDOM_LIST")
public class GetEventUserRandomList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		try {
		
			HashMap<String, Object> params = new HashMap<>();
			List<HashMap<String, Object> > resultList = new ArrayList<HashMap<String, Object> >();
			params.put("subEvtId", this.parameterObject.getString("subEvtId"));
			params.put("gftId", this.parameterObject.getString("gftId"));
			params.put("evtId", this.parameterObject.getString("evtId")); 
			String mode = parameterObject.has("mode") ? this.parameterObject.getString("mode") : "";
			if(!"add".equals(mode)) {
				//기존 당첨자 삭제
				sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.deleteEventUserWin", params);
			}

			//당첨자 수 구하기
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap = sqlSession.selectOne("kr.or.visitkorea.system.EventResultMapper.selectEventGift", params);
			
			//이벤트 마스터에서 블랙리스트 사용여부 확인
			HashMap<String, Object> resultMasterMap = sqlSession.selectOne("kr.or.visitkorea.system.EventResultMapper.selectEventMaster", params);
			if(resultMasterMap != null) {
				if(Boolean.parseBoolean(String.valueOf(resultMasterMap.get("BLACKLIST_USE_YN")))) {
					params.put("blacklistUseYn", "Y");
				}
			}else {
				throw new Exception("이벤트 정보를 찾을 수 없습니다.");
			}
			
			if(resultMap != null) {
				int limit = 0;
				if(resultMap.get("COUNT") != null) {
				 limit = Integer.parseInt(String.valueOf(resultMap.get("COUNT")));
				}
				//추가 모드일경우 당첨자 갯수를 빼줌.
				if("add".equals(mode)) {
					if(resultMap.get("WIN_CNT") != null) {
						 limit = limit - Integer.parseInt(String.valueOf(resultMap.get("WIN_CNT")));
					}
				}
				if(limit > 0) {
					params.put("limit", limit); 
					
					
					
					HashMap<String, Object> resultSubEventMap = sqlSession.selectOne("kr.or.visitkorea.system.EventResultMapper.selectSubEvent", params);
					//OX퀴즈일경우 
					if(Integer.parseInt(String.valueOf(resultSubEventMap.get("EVT_TYPE_CD"))) == 4) {
						//정답조건이 설정된경우 정답수 체크
						if(Boolean.parseBoolean(String.valueOf(resultSubEventMap.get("WIN_COND_USE_YN")))) {
							
							params.put("winCondUseYn","Y");
							params.put("winCondValue", resultSubEventMap.get("WIN_COND_VALUE"));
						}
					}
					
					resultList = sqlSession.selectList("kr.or.visitkorea.system.EventResultMapper.selectEventUserRandomList", params);
					if(resultList.size() > 0) {
						// 당첨자 등록
						for(int i=0; i < resultList.size(); i++) {
							HashMap<String, Object> userMap = resultList.get(i);
							params.put("eeuId", userMap.get("EEU_ID"));
							params.put("eapId", this.parameterObject.getString("eapId"));
							sqlSession.insert("kr.or.visitkorea.system.EventResultMapper.saveEventUserWin", params);
						}
					}else {
						throw new Exception("선정 가능한 당첨자가 없습니다.");
					}
				}else {
					throw new Exception("당첨자 선정이 완료된 경품입니다.");
				}
			}
	
			JSONArray json = new JSONArray(resultList);
			String convertJSONString = json.toString();
			if (convertJSONString.equals("null")) {
				resultHeaderObject.put("process", "fail");
				resultHeaderObject.put("ment", "에러 발생..");
			}else{
				resultBodyObject.put("result", new JSONArray(convertJSONString));
			}
		}catch(Exception e) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
