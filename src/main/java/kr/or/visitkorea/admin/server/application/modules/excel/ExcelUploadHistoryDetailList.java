package kr.or.visitkorea.admin.server.application.modules.excel;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="EXCEL_UPLOAD_HIST_DETAIL_LIST")
public class ExcelUploadHistoryDetailList extends AbstractModule {
	
    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    	
    	try {
    		
    		String eihId = null;
    		String xmlFilePath = null;
    		
    		if (parameterObject.getString("eihId") != null || parameterObject.getString("eihId").trim().length() != 0)
    			eihId = parameterObject.getString("eihId");
    		if (parameterObject.getString("xmlFilePath") != null || parameterObject.getString("xmlFilePath").trim().length() != 0)
    			xmlFilePath = parameterObject.getString("xmlFilePath");
			
    		HashMap<String, Object> paramMap = new HashMap<String, Object>();
    		paramMap.put("eihId", eihId);
    		
    		List<Map<String, String>> list = sqlSession.selectList("kr.or.visitkorea.system.ExcelUploadMapper.selectHistoryDetail", paramMap);
    		
    		if (list == null || list.size() == 0) {
    			resultHeaderObject.put("process", "fail");
    			resultHeaderObject.put("ment", "");
    		}
    		else {
    			List<Map<String, String>> xmlResult = xmlLoader(eihId, xmlFilePath);
    			
    			resultBodyObject.put("result", new JSONArray(list));
    			resultBodyObject.put("xmlResult", new JSONArray(xmlResult));
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private List<Map<String, String>> xmlLoader(String eihId, String xmlFilePath) {
		SAXBuilder builder = new SAXBuilder();
		List<Map<String, String>> xmlData = new ArrayList<Map<String,String>>();
		
		try {
			String filePath = Call.getProperty("image.xml.path") + "/" + xmlFilePath.replace("master", "check");
			
			Document doc = builder.build(new InputSource(new FileReader(filePath)));
			
			Element checksum = doc.getRootElement();
			List checksumList = checksum.getChildren();
			Iterator it = checksumList.iterator();
			xmlData = new ArrayList<Map<String,String>>();
			int cnt = 0;
			
			while (it.hasNext()) {
				
				Element checksumElement = (Element) it.next();
				if (checksumElement.getName().equals("imageContent")) {
					
					if (checksumElement.getAttributeValue("contentId").toString() == null || checksumElement.getAttributeValue("contentId").toString().trim().length() == 0)
						continue;
					if (checksumElement.getAttributeValue("reason").toString() == null || checksumElement.getAttributeValue("reason").toString().trim().length() == 0)
						continue;
					
					HashMap<String, String> xmlList = new HashMap<String, String>();
					xmlList.put("contentId", checksumElement.getAttributeValue("contentId"));
					xmlList.put("reason", checksumElement.getAttributeValue("reason"));
					xmlData.add(cnt, xmlList);
				}
				cnt++;
			}
			return xmlData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
//        System.out.println("request object :: " + parameterObject);
    }

    @Override
    protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
//        System.out.println("result object :: " + resultBodyObject);
    }
}