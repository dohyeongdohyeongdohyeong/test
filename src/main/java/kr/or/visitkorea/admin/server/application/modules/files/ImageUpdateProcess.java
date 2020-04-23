package kr.or.visitkorea.admin.server.application.modules.files;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * @author Admin
 * 이미지 업로드 InsertDialog Module
 */
@BusinessMapping(id="IMAGE_UPDATE_PROCESS")
public class ImageUpdateProcess extends AbstractModule {
	
	private String eihId;
	private String xmlFilePath;
	private Document masterDocument;
	private Map<String, Object> returnResult;
	private int countTotal;
	private int countErrorExistImages;
	private int countErrorExistMainImages;
	private int countErrorNewImages;
	private int countErrorNewMainImages;
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		initProcess();
		String targetFile = initFile();
		
//		System.out.println("xmlFilePath :: " + xmlFilePath);
		
		xmlLoad(targetFile);
		processing();
		
//		System.out.println("countErrorExistImages :: " + countErrorExistImages);
//		System.out.println("countErrorExistMainImages :: " + countErrorExistMainImages);
//		System.out.println("countErrorNewImages :: " + countErrorNewImages);
//		System.out.println("countErrorNewMainImages :: " + countErrorNewMainImages);
		
		procResult();
	}
	
	private void procResult() {
		int insertErrorCount = countErrorExistImages + countErrorNewImages;
		int insertTotalCount = countTotal - insertErrorCount;
		
		Map<String, Object> procResultParams = new HashMap<String, Object>();
		procResultParams.put("eihId", eihId);
		procResultParams.put("saveRow", insertTotalCount);
		procResultParams.put("failRow", insertErrorCount);
		procResultParams.put("status", 8);
		
		int statusResult = sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateStatus", procResultParams);
		int saveRowResult = sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateSaveRow", procResultParams);
		int failRowResult = sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateFailRow", procResultParams);
	}
	
	private void processing() {
		Element rootElement = masterDocument.getRootElement();
		List<Element> imageContentList = rootElement.getChildren("imageContent");
		
		for (Element imageContent:imageContentList) {
			String cotId = imageContent.getAttributeValue("cotId");
			String title = imageContent.getAttributeValue("title");
			String contentType = imageContent.getAttributeValue("contentType");
			String contentId = imageContent.getAttributeValue("contentId");
			
			// contentType = 32, 숙박
//			deleteCotId(contentId, contentType, cotId);
			
			List<Element> imageList = imageContent.getChildren("image");
//			System.out.println("imageList :: " + imageList);
			
			for (Element imageElement:imageList) {
				Map<String, Object> imageParams = new HashMap<String, Object>();
				int order = Integer.parseInt(imageElement.getAttributeValue("order"));
				String path = imageElement.getAttributeValue("path");
				String imgId = imageElement.getAttributeValue("imgId");
				boolean main = Boolean.parseBoolean(imageElement.getAttributeValue("main"));
				String url = imageElement.getAttributeValue("path");
				boolean isNew = Boolean.parseBoolean(imageElement.getAttributeValue("isNew"));
				String surfix = path.substring(path.lastIndexOf("."));
				
				imageParams.put("order", order);
				imageParams.put("imgId", imgId);
				imageParams.put("main", main);
				imageParams.put("url", url);
				imageParams.put("isNew", isNew);
				imageParams.put("cotId", cotId);
				imageParams.put("desc", String.format("%s %2d", title, order));
				
//				System.out.println("imageParams :: " + imageParams);
				
				if (isNew == false){
					String[] imageIds = imgId.split("-");
					path = String.format("/%s/%s/%s/%s/%s/%s%s",
							imageIds[0].substring(0,2),
							imageIds[1].substring(0,2),
							imageIds[2].substring(0,2),
							imageIds[3].substring(0,2),
							imageIds[4].substring(0,2),
							imgId,
							surfix
							);
					imageParams.put("path", path);
					
					insertExistImageInformation(imageParams);
				}else {
					insertNewImageInformation(imageParams);
				}
				
				countTotal += 1;
				if (returnResult.get("existErrorMainImage") != null && returnResult.get("existErrorMainImage").toString().equals("0")) {
					countErrorExistMainImages += 1;
				}
				if (returnResult.get("existErrorImage") != null && returnResult.get("existErrorImage").toString().equals("0")) {
					countErrorExistImages += 1;
				}
				if (returnResult.get("newErrorMainImage") != null && returnResult.get("newErrorMainImage").toString().equals("0")) {
					countErrorNewMainImages += 1;
				}
				if (returnResult.get("newErrorImage") != null && returnResult.get("newErrorImage").toString().equals("0")) {
					countErrorNewImages += 1;
				}
			}
			
			// delete unused image on database
			
				deleteUnusedImage(cotId, imageList,contentType);
		}
	}
	
	private void deleteUnusedImage(String cotId, List<Element> imageList,String contentType) {
		
		// remove image from IMAGE table
		String exceptImageList = "";
		for (Element imageElement:imageList) {
			if (exceptImageList.equals("")) {
				exceptImageList += "'" + imageElement.getAttributeValue("imgId") + "'";
			} else {
				exceptImageList += ", '" + imageElement.getAttributeValue("imgId") + "'";
			}
		}
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("cotId", cotId);
		paramsMap.put("exceptImageList", exceptImageList);
		
		System.out.println("deleteUnusedImage.paramsMap :: " + paramsMap);
		if (!contentType.equals("32")) {
			int result = sqlSession.delete("kr.or.visitkorea.system.ExcelUploadMapper.deleteUnusedImage", paramsMap);
		} else {
			sqlSession.delete("kr.or.visitkorea.system.ExcelUploadMapper.deleteUnusedImage2", paramsMap);
		}
	}

	private void updateMainImageWithDatabaseMaster(String cotId, String imgId, int flag) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cotId", cotId);
		params.put("imgId", imgId);
		
		int result = sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateMainImage", params);
//		System.out.println("updateMainImageWithDatabaseMaster :: " + params);
		if (flag == 0)
			if (result == 0)
				returnResult.put("existErrorMainImage", result);
		if (flag == 1)
			if (result == 0)
				returnResult.put("newErrorMainImage", result);
	}
	
	private void insertNewImageInformation(Map<String, Object> imageParams) {
		int orderInt = (int) imageParams.get("order");
		String imgId = (String) imageParams.get("imgId");
		boolean main = (boolean) imageParams.get("main");
		String url = (String) imageParams.get("url");
		boolean isNew = (boolean) imageParams.get("isNew");
		String cotId = (String) imageParams.get("cotId");
		String desc = (String) imageParams.get("desc");
		
		if (main) {
			
			updateMainImageWithDatabaseMaster(cotId, imgId, 1);
		}
		
		int result = sqlSession.insert("kr.or.visitkorea.system.ExcelUploadMapper.insertNewImage", imageParams);
//		System.out.println("insertNewImageInformation :: " + imageParams);
		
		returnResult.put("newErrorImage", result);
	}
	
	private void insertExistImageInformation(Map<String, Object> imageParams) {
		int orderInt = (int) imageParams.get("order");
		String imgId = (String) imageParams.get("imgId");
		boolean main = (boolean) imageParams.get("main");
		String url = (String) imageParams.get("url");
		boolean isNew = (boolean) imageParams.get("isNew");
		String cotId = (String) imageParams.get("cotId");
		String desc = (String) imageParams.get("desc");
		String path = (String) imageParams.get("path");

		if (main)
			updateMainImageWithDatabaseMaster(cotId, imgId, 0);
		
		int result = sqlSession.insert("kr.or.visitkorea.system.ExcelUploadMapper.insertExistImage", imageParams);
//		System.out.println("insertExistImageInformation :: " + imageParams);
		
		returnResult.put("existErrorImage", result);
	}
	
	private void deleteCotId(String contentId, String contentType, String cotId) {
		String exceptImageList = "";
		Map<String, String> params = new HashMap<String, String>();
		params.put("cotId", cotId);
		params.put("contentId", contentId);
		
		if (contentType.equals("32")) {
			List<HashMap<String, Object>> exceptImageSet = sqlSession.selectList("kr.or.visitkorea.system.ExcelUploadMapper.selectAccommodationInfoImageSet", params);
			for (HashMap<String, Object> exceptImage:exceptImageSet) {
				if (exceptImage.containsKey("ROOM_IMG1")) {
					if (exceptImageList.equals(""))
						exceptImageList += "'" + exceptImage.get("ROOM_IMG1") + "'";
					else
						exceptImageList += ", '" + exceptImage.get("ROOM_IMG1") + "'";
				}
				
				if (exceptImage.containsKey("ROOM_IMG2")) {
					if (exceptImageList.equals(""))
						exceptImageList += "'" + exceptImage.get("ROOM_IMG2") + "'";
					else
						exceptImageList += ", '" + exceptImage.get("ROOM_IMG2") + "'";
				}
				if (exceptImage.containsKey("ROOM_IMG3")) {
					if (exceptImageList.equals(""))
						exceptImageList += "'" + exceptImage.get("ROOM_IMG3") + "'";
					else
						exceptImageList += ", '" + exceptImage.get("ROOM_IMG3") + "'";
				}
				if (exceptImage.containsKey("ROOM_IMG4")) {
					if (exceptImageList.equals(""))
						exceptImageList += "'" + exceptImage.get("ROOM_IMG4") + "'";
					else
						exceptImageList += ", '" + exceptImage.get("ROOM_IMG4") + "'";
				}
				if (exceptImage.containsKey("ROOM_IMG5")) {
					if (exceptImageList.equals(""))
						exceptImageList += "'" + exceptImage.get("ROOM_IMG5") + "'";
					else
						exceptImageList += ", '" + exceptImage.get("ROOM_IMG5") + "'";
				}
			}
		}
		
		params.put("exceptImageList", exceptImageList);
		
		System.out.println("params :: " + params);
		
		int result = sqlSession.delete("kr.or.visitkorea.system.ExcelUploadMapper.deleteCotIdFromImage", params);
		
		returnResult.put("deleteCotId", result);
	}
	
	private void xmlLoad(String targetFile) {
		SAXBuilder builder = new SAXBuilder();
		
		try {
			masterDocument = builder.build(new InputSource(new FileReader(targetFile)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String initFile() {
		String targetFile = Call.getProperty("image.xml.path") + "/" + xmlFilePath;
		
		return targetFile;
	}
	
	private void initProcess() {
		eihId = parameterObject.get("eihId").toString();
		xmlFilePath = parameterObject.get("xmlFilePath").toString();
		returnResult = new HashMap<String, Object>();
		
		countTotal = 0;
		countErrorExistMainImages = 0;
		countErrorExistImages = 0;
		countErrorNewMainImages = 0;
		countErrorNewImages = 0;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}