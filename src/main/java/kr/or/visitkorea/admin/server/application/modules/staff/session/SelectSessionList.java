package kr.or.visitkorea.admin.server.application.modules.staff.session;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_SESSION_LIST")
public class SelectSessionList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		System.out.println(this.sessionManager.getSessionMap());
		
		ConcurrentHashMap<String, Object> sessionMap = this.sessionManager.getSessionMap();
		
		List<HashMap<String, Object>> returnList = new ArrayList<>();
		List<HashMap<String, Object>> selectList = 
				sqlSession.selectList("kr.or.visitkorea.system.StaffMapper.userlist");
		
		selectList.forEach(item -> {
			String usrId = (String) item.get("USR_ID"); 
			if (this.sessionManager.getSessionMap().containsKey(usrId)) {
				HttpSession itemSession = (HttpSession) sessionMap.get(usrId);
				Date currentTime = new Date();
				Date createTime = new Date(itemSession.getCreationTime());
				Date lastAccessTime = new Date(itemSession.getLastAccessedTime());
				
				item.put("SESSION_ID", usrId);
				item.put("CREATE_TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
				item.put("LAST_ACCESS_TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastAccessTime));
				item.put("ACTIVE_TIME", currentTime.getTime() - itemSession.getCreationTime());
				item.put("CURRENT_TIME", currentTime.getTime());
				item.put("REMAIN_SESSION_TIME", currentTime.getTime() - itemSession.getLastAccessedTime());
				returnList.add(item);
			}
		});
		
		resultBodyObject.put("result", new JSONArray(returnList));
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
