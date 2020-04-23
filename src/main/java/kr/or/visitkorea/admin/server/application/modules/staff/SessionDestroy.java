package kr.or.visitkorea.admin.server.application.modules.staff;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="SESSION_DESTROY")
public class SessionDestroy extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		Object usrId = this.getRequest().getSession().getAttribute("usrId");
		
		HttpSession session = this.getRequest().getSession();
				
		if (this.parameterObject.has("sessionId")) {
			usrId = this.parameterObject.getString("sessionId");
			session = (HttpSession) this.sessionManager.getSessionMap().get(usrId);
		}
		
		System.out.println(">> Before Session :: " + this.sessionManager.getSessionMap());
		if (usrId != null) {
			session.invalidate();
		}
		System.out.println(">> After Session :: " + this.sessionManager.getSessionMap());
	}


	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
