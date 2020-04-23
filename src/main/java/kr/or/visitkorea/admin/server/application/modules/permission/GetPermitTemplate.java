package kr.or.visitkorea.admin.server.application.modules.permission;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.XML;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 * 
 */
@BusinessMapping(id="GET_PERMIT_TEMPLATE")
public class GetPermitTemplate extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(Call.getPermissionDocument());

        resultBodyObject.put("result", XML.toJSONObject(xmlString));
        
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
