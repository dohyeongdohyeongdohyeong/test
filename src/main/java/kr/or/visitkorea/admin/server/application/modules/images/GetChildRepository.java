package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.modules.images.GetChildRepository
 * @author Lim Jin-Seok
 * 
 */
@BusinessMapping(id="GET_CHILD_REPOSITORY")
public class GetChildRepository extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		try {
			String path = parameterObject.getString("path");
			XPathFactory xpfac = XPathFactory.instance();
			Document doc = Call.getFileRepositoryDoc();
			XPathExpression<Element> xp = xpfac.compile(String.format("//dir[@path='%s']", path), Filters.element());
			Element root = (Element) xp.evaluateFirst(doc);
			JSONObject json = new JSONObject();
			JSONArray dirJson = new JSONArray();
			JSONArray fileJson = new JSONArray();
			
			
			for (Element dir : root.getChildren()) {
				
				String name = dir.getName();
				JSONObject val = new JSONObject();
				switch (name) {
				case "dir":
					dir.getAttributes().forEach(event->{
						val.put(event.getName(), event.getValue());
					});
					dirJson.put(val);
					break;
				case "file":
					dir.getAttributes().forEach(event->{
						val.put(event.getName(), event.getValue());
						
						if (event.getName().equals("fileNm")) {
							HashMap<String, Object> paramterMap = new HashMap<String, Object>();
							paramterMap.put("imgId", event.getValue());
							
							HashMap<String, Object> returnMap = sqlSession.selectOne( 
									"kr.or.visitkorea.system.ImageMapper.selectImageWithImgId" , 
									paramterMap );
							
							if (returnMap != null)
								val.put("desc", returnMap.get("IMAGE_DESCRIPTION"));
							else
								val.put("desc", "");
						}
					});
					fileJson.put(val);
					break;
				}
			}
			
			json.put("dir", dirJson);
			json.put("file", fileJson);
			
			resultBodyObject.put("result", json);

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
