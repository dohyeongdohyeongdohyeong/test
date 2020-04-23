package kr.or.visitkorea.admin.server.application.modules.event;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UNZIP_EVENT_BANNER")
public class UnzipEventBanner extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		this.getResponse().setCharacterEncoding("UTF-8");
		this.getResponse().setContentType("text/plain");
		
		resultObject.put("return.type", "text/plain");
		resultBodyObject.put("files", this.parameterObject.get("files"));
	}
	
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
