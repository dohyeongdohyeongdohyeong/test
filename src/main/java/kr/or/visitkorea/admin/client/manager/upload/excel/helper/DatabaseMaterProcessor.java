package kr.or.visitkorea.admin.client.manager.upload.excel.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class DatabaseMaterProcessor {

	
	private static String driver        = "org.mariadb.jdbc.Driver";
	private static String url           = "jdbc:mariadb://kto.test:3306/KTO";
	private static String uId           = "front";
	private static String uPwd          = "front!@#$";
	
	private static Connection               con;
	private static PreparedStatement        pstmt;
	private static ResultSet                rs;

	private static Document masterDocument;
	private static String xmlFile;

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, JDOMException {

		xmlFile = "/Users/jungwoochoi/IMAGE_FROM_XLS/master.xml";
		List<String> names = new ArrayList<String>();
		names.add("j");

		/** init system **/
		databaseInit();
		xmlInit(xmlFile);

		// processing database
		processDatabase();
		
	}

	private static void databaseInit() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		con = DriverManager.getConnection(url, uId, uPwd);
	}

	private static void xmlInit(String xmlTargetFile) throws JDOMException, IOException  {
		FileInputStream fis = new FileInputStream(xmlTargetFile);
        SAXBuilder builder = new SAXBuilder();
        masterDocument = builder.build(fis);
	}
	
	private static void processDatabase() {

		Element rootElement = masterDocument.getRootElement();
		
		List<Element> imageContentList = rootElement.getChildren("imageContent");
		for (Element imageContent : imageContentList) {
			
			String cotId = imageContent.getAttributeValue("cotId"); 
			String title = imageContent.getAttributeValue("title"); 
			
			// delete image content
			deleteCotId(cotId);
			
			List<Element> imageList = imageContent.getChildren("image");
			
			for (Element imageElement : imageList) {
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				
				int order = Integer.parseInt(imageElement.getAttributeValue("order"));
				String path = imageElement.getAttributeValue("path");
				String imgId = imageElement.getAttributeValue("imgId");
				boolean main = Boolean.parseBoolean(imageElement.getAttributeValue("main"));
				String url = imageElement.getAttributeValue("url");
				boolean isNew = Boolean.parseBoolean(imageElement.getAttributeValue("isNew"));
				String surfix = path.substring(path.lastIndexOf("."));
				
				paramMap.put("order", order);
				paramMap.put("imgId", imgId);
				paramMap.put("main", main);
				paramMap.put("url", url);
				paramMap.put("isNew", isNew);
				paramMap.put("cotId", cotId);
				paramMap.put("desc", String.format("%s %2d", title, order));
				
				if (isNew == false){
					
					String[] imageIds = imgId.split("-");
					
					 path = String.format("/%s/%s/%s/%s/%s%s", 
							imageIds[0].substring(0,2),
							imageIds[1].substring(0,2),
							imageIds[2].substring(0,2),
							imageIds[3].substring(0,2),
							imageIds[4].substring(0,2),
							imgId,
							surfix
							);
					
					 paramMap.put("path", path);
					 
					insetExistImageInformation(paramMap);
					
					
				}else {
					
					insertNewImageInformation(paramMap);
					
				}

			}
			
		}
		
	}

	private static void insetExistImageInformation(Map<String, Object> paramMap) {

		int orderInt = (int) paramMap.get("order");
		String imgId = (String) paramMap.get("imgId");
		boolean main = (boolean) paramMap.get("main");
		String url = (String) paramMap.get("url");
		boolean isNew = (boolean) paramMap.get("isNew");
		String cotId = (String) paramMap.get("cotId");
		String desc = (String) paramMap.get("desc");
		String path = (String) paramMap.get("path");

		// processing insert
		
		
	}

	private static void insertNewImageInformation(Map<String, Object> paramMap) {

		int orderInt = (int) paramMap.get("order");
		String imgId = (String) paramMap.get("imgId");
		boolean main = (boolean) paramMap.get("main");
		String url = (String) paramMap.get("url");
		boolean isNew = (boolean) paramMap.get("isNew");
		String cotId = (String) paramMap.get("cotId");
		String desc = (String) paramMap.get("desc");

		
		// processing insert 
		
	}

	private static void deleteCotId(String cotId) {
		
		// delete content with cotId;

		// String query = String.format("delete from IMAGE where COT_ID='%s'", cotId);

	}

	
}
