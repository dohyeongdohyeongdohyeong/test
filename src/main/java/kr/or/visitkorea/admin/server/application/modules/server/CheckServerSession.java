package kr.or.visitkorea.admin.server.application.modules.server;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.CheckServerSession
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="CHECK_SERVER_SESSION")
public class CheckServerSession extends AbstractModule{
 
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		if (this.getRequest() != null && 
			this.getRequest().getSession() != null && 
			this.getRequest().getSession().getAttribute("LOGIN_CHK") == null) {
			
			if (this.getRequest().getSession().getAttribute("usrId") != null) {
				this.getRequest().getSession().removeAttribute("usrId");
			}
			resultHeaderObject.put("process", "fail");
		} else{
			resultHeaderObject.put("process", "success");
		}

	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {}
}
