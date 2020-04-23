package kr.or.visitkorea.admin.server.application.modules.images;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.XML;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.modules.images.GetFileRepositoryTemplate
 * @author Lim Jin-Seok
 * 
 */
@BusinessMapping(id="GET_FILE_REPOSITORY_TEMPLATE")
public class GetFileRepositoryTemplate extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		try {
			// xml 입력
			String path = null;
			Document doc = null;
			SAXBuilder builder = new SAXBuilder();

			if ((path = Call.getProperty("images.xml.path")) == null) {
				path = "/data/FileRepository.xml";
			}

			File file = new File(path);
			doc = builder.build(file);
			Call.setFileRepositoryDoc(doc);
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			String xmlString = outputter.outputString(doc);
			
			resultBodyObject.put("result", XML.toJSONObject(xmlString));
			System.out.println(XML.toJSONObject(xmlString).toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultHeaderObject.put("process", "fail");
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
