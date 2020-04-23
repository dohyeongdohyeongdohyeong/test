package kr.or.visitkorea.admin.server.application.modules.event;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.dev.ModuleTabPanel.Session;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SAVE_BLACK_LIST")
public class SaveBlacklist extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		
		String fileName = this.parameterObject.getString("fileName");
		String SaveName = this.parameterObject.getString("saveName");
		String MainCheck = this.parameterObject.getString("mainCheck");
		int save = 0;
		int update = 0;
		int check = 0;
		HashMap<String, Object> params = new HashMap<>();
		params.put("fileName",fileName);
		params.put("SaveName", SaveName);
		params.put("bfiId",UUID.randomUUID().toString());
		String stfid = (String)this.getRequest().getSession().getAttribute("stfId");
		params.put("stfId", stfid);
		params.put("USE_YN","N");
		
		try {
			
		
		if(!fileName.equals("")) {
			sqlSession.update("kr.or.visitkorea.system.EventBlacklistMapper.UpdateAllUseYn", params);
			
			params.put("USE_YN","Y");
			save = sqlSession.insert("kr.or.visitkorea.system.EventBlacklistMapper.InsertBlacklist", params);
			check ++;
		}
		
		if(!MainCheck.equals("")) {
			params.put("USE_YN","N");
			sqlSession.update("kr.or.visitkorea.system.EventBlacklistMapper.UpdateAllUseYn", params);
			
			params.put("USE_YN","Y");
			params.put("bfiId",MainCheck);
			update = sqlSession.update("kr.or.visitkorea.system.EventBlacklistMapper.UpdateUseYn", params);
			check ++;
		}
		
			if(save != 0 || update != 0) {
				resultHeaderObject.put("process", "success");
				resultHeaderObject.put("ment", "저장에 성공하였습니다");
			} else if( check == 0) {
				resultHeaderObject.put("process", "success");
				resultHeaderObject.put("ment", "저장에 성공하였습니다");
			} else {
				resultHeaderObject.put("process", "fail");
				resultHeaderObject.put("ment", "오류가 발생했습니다. 관리자에게 문의해주세요");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
