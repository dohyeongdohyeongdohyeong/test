package kr.or.visitkorea.admin.server.application.modules.server;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.XML;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.ApplicationList
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="APPLIACTION_LIST")
public class ApplicationList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(Call.getApplicationListDocument());

		JSONObject xmlObject = XML.toJSONObject(xmlString);
		
		if (xmlObject.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", xmlObject);
		}
		
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
