package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_EVENT_PROCESS")
public class SaveEventProcess extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject model = this.parameterObject.getJSONObject("model");
		JSONArray processArr = model.getJSONArray("PROCESS");
		
		if (model.has("RULLSET")) {
			JSONArray rullsetArr = model.getJSONArray("RULLSET");
			
			try {
				sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventRullset", this.buildRullsetParameter(rullsetArr));
			} catch (Throwable e) {
				e.printStackTrace();
				
				sqlSession.rollback();
			}
		}
		
		for (int i = 0; i < processArr.length(); i++) {
			JSONObject obj = processArr.getJSONObject(i);
			try {
			HashMap<String, Object> process = buildProcessParameter(obj);

				
			boolean processIsDelete = (boolean) process.get("IS_DELETE");
			if (processIsDelete) {
				sqlSession.delete("kr.or.visitkorea.system.EventMapper.deleteEventProcess", process);
				continue;
			} else {
				sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventProcess", process);
			}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (obj.has("GIFTS")) {
				JSONArray giftsArray = obj.getJSONArray("GIFTS");
				
				for (int j = 0; j < giftsArray.length(); j++) {
					JSONObject giftObj = giftsArray.getJSONObject(j);
					HashMap<String, Object> gift = buildGiftsParameter(giftObj);
						sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventGift", gift);
				}
			}
			if (obj.has("GIFT_DELETE")) {
				JSONArray giftsArray = obj.getJSONArray("GIFT_DELETE");
				
				for (int j = 0; j < giftsArray.length(); j++) {
					JSONObject giftObj = giftsArray.getJSONObject(j);
					HashMap<String, Object> gift = buildGiftsParameter(giftObj);
						sqlSession.delete("kr.or.visitkorea.system.EventMapper.deleteEventGift", gift);
				}
				
			}
			
			if (obj.has("OXQUIZS")) {
				JSONArray OXQuizArr = obj.getJSONArray("OXQUIZS");
				
				for (int j = 0; j < OXQuizArr.length(); j++) {
					JSONObject OXQuizobj = OXQuizArr.getJSONObject(j);
					HashMap<String, Object> OXQuiz = buildOXQuizsParameter(OXQuizobj);
					sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveOXQuiz", OXQuiz);
				}
				
			}
			
			if (obj.has("OXQUIZ_DELETE")) {
				JSONArray OXQuizArr = obj.getJSONArray("OXQUIZ_DELETE");
				
				for (int j = 0; j < OXQuizArr.length(); j++) {
					JSONObject OXQuizobj = OXQuizArr.getJSONObject(j);
					HashMap<String, Object> OXQuiz = buildOXQuizsParameter(OXQuizobj);
					sqlSession.delete("kr.or.visitkorea.system.EventMapper.deleteEventOXQuiz", OXQuiz);
				}
				
			}
			
			
		}
	}

	private List<HashMap<String, Object>> buildRullsetParameter(JSONArray array) {
		List<HashMap<String, Object>> rullsetList = new ArrayList<>();
		
		for (int i = 0; i < array.length(); i++) {
			
			JSONObject obj = array.getJSONObject(i);
			HashMap<String, Object> params = new HashMap<>();
			params.put("subEvtId", obj.getString("SUB_EVT_ID"));
			params.put("entryNum", obj.getInt("ENTRY_NUM"));
			params.put("gftId", obj.getString("GFT_ID"));
			params.put("stfId", obj.getString("STF_ID"));
			rullsetList.add(params);

			if(i == 0) {
				sqlSession.delete("kr.or.visitkorea.system.EventMapper.removeEventRullset", params);
			}
			
		}
		return rullsetList;
	}
	
	private HashMap<String, Object> buildProcessParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		if (obj.has("SUB_EVT_ID"))
			params.put("subEvtId", obj.getString("SUB_EVT_ID"));
		if (obj.has("EVT_ID"))
			params.put("evtId", obj.getString("EVT_ID"));
		if (obj.has("TITLE"))
			params.put("title", obj.getString("TITLE"));
		if (obj.has("START_DATE"))
			params.put("startDate", obj.getString("START_DATE"));
		if (obj.has("END_DATE"))
			params.put("endDate", obj.getString("END_DATE"));
		if (obj.has("EVT_TYPE_CD"))
			params.put("evtTypeCd", obj.getInt("EVT_TYPE_CD"));
		if (obj.has("ELECT_TYPE"))
			params.put("electType", obj.getInt("ELECT_TYPE"));
		if (obj.has("IMMEDI_TYPE"))
			params.put("immediType", obj.getInt("IMMEDI_TYPE"));
		if (obj.has("RANDOM_TYPE"))
			params.put("randomType", obj.getInt("RANDOM_TYPE"));
		if (obj.has("USER_PREDICT_CNT"))
			params.put("userPredictCnt", obj.getInt("USER_PREDICT_CNT"));
		if (obj.has("ANNOUNCE_PER"))
			params.put("announcePer", obj.getInt("ANNOUNCE_PER"));
		if (obj.has("WIN_COND_USE_YN"))
			params.put("winconduse", obj.getInt("WIN_COND_USE_YN"));
		if (obj.has("WIN_COND_VALUE"))
			params.put("wincondvalue", obj.getInt("WIN_COND_VALUE"));
		if (obj.has("EXCEL_FILE_NM"))
			params.put("excelfilenm", obj.getString("EXCEL_FILE_NM"));
		if (obj.has("IS_NOT_WIN"))
			params.put("isNotWin", obj.getBoolean("IS_NOT_WIN"));
		if (obj.has("IS_DELETE"))
			params.put("IS_DELETE", obj.getBoolean("IS_DELETE"));
		
		return params;
	}

	private HashMap<String, Object> buildGiftsParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		if (obj.has("GFT_ID"))
			params.put("gftId", obj.getString("GFT_ID"));
		if (obj.has("SUB_EVT_ID"))
			params.put("subEvtId", obj.getString("SUB_EVT_ID"));
		if (obj.has("EVT_ID"))
			params.put("evtId", obj.getString("EVT_ID"));
		if (obj.has("TITLE"))
			params.put("title", obj.getString("TITLE"));
		if (obj.has("COUNT"))
			params.put("count", obj.getInt("COUNT"));
		if (obj.has("ORDER"))
			params.put("order", obj.getInt("ORDER"));
		if (obj.has("IS_NOT_WIN"))
			params.put("isNotWin", obj.getBoolean("IS_NOT_WIN"));
		if (obj.has("IS_DELETE"))
			params.put("IS_DELETE", obj.getBoolean("IS_DELETE"));
		
		return params;
	}
	
	private HashMap<String, Object> buildOXQuizsParameter(JSONObject obj) {
			
		System.out.println(obj);
		
		HashMap<String, Object> params = new HashMap<>();
		if (obj.has("EQC_ID"))
			params.put("eqcId", obj.getString("EQC_ID"));
		if (obj.has("SUB_EVT_ID"))
			params.put("subEvtId", obj.getString("SUB_EVT_ID"));
		if (obj.has("QUESTION"))
			params.put("question", obj.getString("QUESTION"));
		if (obj.has("QUESTION_TYPE"))
			params.put("questiontype", obj.getString("QUESTION_TYPE"));
		if (obj.has("QUESTION_IMG_ALT"))
			params.put("questionimgAlt", obj.getString("QUESTION_IMG_ALT"));
		if (obj.has("QUESTION_IMG_ID")) {
			params.put("questionimgId", obj.getString("QUESTION_IMG_ID"));
		}
		if (obj.has("QUESTION_IMG_PATH")) {
			params.put("questionimgPath", obj.getString("QUESTION_IMG_PATH"));
			sqlSession.insert("kr.or.visitkorea.system.EventMapper.QuestionImgSave", params);
		}
		if (obj.has("ANSWER"))
			params.put("answer", obj.getString("ANSWER"));
		if (obj.has("HINT_USE_YN"))
			params.put("hintYn", obj.getString("HINT_USE_YN"));
		if (obj.has("HINT_TYPE"))
			params.put("hintType", obj.getString("HINT_TYPE"));
		if (obj.has("HINT_BODY"))
			params.put("hintbody", obj.getString("HINT_BODY"));
		if (obj.has("HINT_IMG_ALT"))
			params.put("hintimgAlt", obj.getString("HINT_IMG_ALT"));
		if (obj.has("HINT_IMG_ID")) {
			params.put("hintimgId", obj.getString("HINT_IMG_ID"));
		}
		if (obj.has("HINT_IMG_PATH")) {
			params.put("hintimgPath", obj.getString("HINT_IMG_PATH"));
			sqlSession.insert("kr.or.visitkorea.system.EventMapper.HintImgSave", params);
		}
		if (obj.has("SORT"))
			params.put("sort", obj.getInt("SORT"));
		return params;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
	
}
