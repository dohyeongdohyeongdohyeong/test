package kr.or.visitkorea.admin.server.application.modules.partners;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id = "DELETE_PARTNERS_PROPOSAL_ANSWER")
public class DeletePartnersProposalAnswer extends AbstractModule {
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("ppsId", parameterObject.getString("ppsId"));
		// 매퍼에 등록된 ID를 지정 및 처리 시 필요한 값을 파라미터로 전달
		int deleteResult = sqlSession.update("kr.or.visitkorea.system.PartnersMapper.deletePartnersProposalAnswer", paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("delete", deleteResult);
		
		// 데이터베이스에서 처리 후의 넘겨받은 결과 값으로 성공 여부를 판별 
		if (deleteResult == 1) {
			// 처리 성공 시에 jsonobject 객체에 키와 값을 등록 후 response에 body 라는 키에 담겨서 클라이언트에서 확인 가능
			resultBodyObject.put("result", retJson);
		} else {
			// 처리 실패 시에 jsonobject 객체에 키와 값을 등록 후 response에 body 라는 키에 담겨서 클라이언트에서 확인 가능
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "정보가 없습니다.");
		}
	}
	// run 메소드 처리 전에 실행 되어야 할 부분이 있을 경우 사용되는 메소드
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	// run 메소드 처리 후에 실행 되어야 할 부분이 있을 경우 사용되는 메소드
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}