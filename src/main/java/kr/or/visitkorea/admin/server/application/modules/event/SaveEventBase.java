package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id = "SAVE_EVENT_BASE")
public class SaveEventBase extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject model = this.parameterObject.getJSONObject("model");

		int result0 = sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveContentMaster",
				buildBaseParameter(model));
		int result1 = sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventBase", buildBaseParameter(model));
		int result2 = sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventBaseUserInfoCollect",
				buildUserInfoCollectParameter(model));
		int result3 = sqlSession.insert("kr.or.visitkorea.system.EventMapper.saveEventBaseStaffInfo",
				buildStaffParameter(model));
		buildBlacklistParameter(model);
		if (result0 != 1 || result1 != 1 || result2 != 1 || result3 != 1) {
			resultHeaderObject.put("process", "fail");
		}
	}

	private HashMap<String, Object> buildBaseParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		if (obj.has("evtId"))
			params.put("evtId", obj.getString("evtId"));
		if (obj.has("cotId"))
			params.put("cotId", obj.getString("cotId"));
		if (obj.has("usrId"))
			params.put("usrId", obj.getString("usrId"));
		if (obj.has("title"))
			params.put("title", obj.getString("title"));
		if (obj.has("startDate") && !obj.getString("startDate").equals(""))
			params.put("startDate", obj.getString("startDate"));
		if (obj.has("endDate") && !obj.getString("endDate").equals(""))
			params.put("endDate", obj.getString("endDate"));
		if (obj.has("contents"))
			params.put("contents", obj.getString("contents"));
		if (obj.has("caution"))
			params.put("caution", obj.getString("caution"));
		if (obj.has("imgId"))
			params.put("imgId", obj.getString("imgId"));
		if (obj.has("reject"))
			params.put("reject", obj.getString("reject"));
		if (obj.has("announceDate") && !obj.getString("announceDate").equals(""))
			params.put("announceDate", obj.getString("announceDate"));
		if (obj.has("announceType"))
			params.put("announceType", obj.getInt("announceType"));
		if (obj.has("status"))
			params.put("status", obj.getInt("status"));
		if (obj.has("templateType"))
			params.put("templateType", obj.getInt("templateType"));
		if (obj.has("isCaution"))
			params.put("isCaution", obj.getBoolean("isCaution"));
		if (obj.has("isStaff"))
			params.put("isStaff", obj.getBoolean("isStaff"));
		if (obj.has("isCollect"))
			params.put("isCollect", obj.getBoolean("isCollect"));
		if (obj.has("isLogin"))
			params.put("isLogin", obj.getBoolean("isLogin"));
		if (obj.has("cautionTemplate"))
			params.put("cautionTemplate", obj.getInt("cautionTemplate"));
		if (obj.has("staffTemplate"))
			params.put("staffTemplate", obj.getInt("staffTemplate"));
		if (obj.has("infoCollectView"))
			params.put("infoCollectView", obj.getInt("infoCollectView"));
		if (obj.has("terms"))
			params.put("terms", obj.getString("terms"));
		if (obj.has("isBlacklist"))
			params.put("isBlacklist", obj.getNumber("isBlacklist"));
		return params;
	}

	private HashMap<String, Object> buildUserInfoCollectParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		JSONObject cObj = obj.getJSONObject("userInfoCollect");
		if (cObj.has("evtId"))
			params.put("evtId", cObj.getString("evtId"));
		if (cObj.has("isName"))
			params.put("isName", cObj.getInt("isName"));
		if (cObj.has("isTel"))
			params.put("isTel", cObj.getInt("isTel"));
		if (cObj.has("isGender"))
			params.put("isGender", cObj.getInt("isGender"));
		if (cObj.has("isAge"))
			params.put("isAge", cObj.getInt("isAge"));
		if (cObj.has("isAddr"))
			params.put("isAddr", cObj.getInt("isAddr"));
		if (cObj.has("isJob"))
			params.put("isJob", cObj.getInt("isJob"));
		if (cObj.has("isEmail"))
			params.put("isEmail", cObj.getInt("isEmail"));
		if (cObj.has("isRegion"))
			params.put("isRegion", cObj.getInt("isRegion"));
		if (cObj.has("isEtc"))
			params.put("isEtc", cObj.getInt("isEtc"));
		if (cObj.has("EtcName"))
			params.put("EtcName", cObj.getString("EtcName"));
		return params;
	}

	private HashMap<String, Object> buildStaffParameter(JSONObject obj) {
		HashMap<String, Object> params = new HashMap<>();
		JSONObject sObj = obj.getJSONObject("staffInfo");
		if (sObj.has("evtId"))
			params.put("evtId", sObj.getString("evtId"));
		if (sObj.has("name"))
			params.put("name", sObj.getString("name"));
		if (sObj.has("tel"))
			params.put("tel", sObj.getString("tel"));
		if (sObj.has("email"))
			params.put("email", sObj.getString("email"));
		if (sObj.has("dept"))
			params.put("dept", sObj.getString("dept"));
		return params;
	}

	private void buildBlacklistParameter(JSONObject obj) {
		int delete = 99999;

		HashMap<String, Object> params = new HashMap<>();
		if (obj.has("blacklist")) {

			JSONArray blacklist = obj.getJSONArray("blacklist");
			for (int i = 0; i < blacklist.length(); i++) {
				String blacklistrow = blacklist.getString(i);
				params.put("HP", blacklistrow);
				params.put("evtId", obj.getString("evtId"));
				params.put("FILE_NAME", obj.getString("blacklistFile"));

				if (i == 0) {
					delete = sqlSession.delete("kr.or.visitkorea.system.EventBlacklistMapper.DeleteEvtExcel", params);
					sqlSession.update("kr.or.visitkorea.system.EventBlacklistMapper.UpdateFileName", params);
				}

				if (delete != 99999) {
					sqlSession.insert("kr.or.visitkorea.system.EventBlacklistMapper.InsertEvtExcel", params);
				}
			}

		}

	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

	}

}
