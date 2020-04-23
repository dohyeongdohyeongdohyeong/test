package kr.or.visitkorea.admin.server.application.modules.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;
import kr.or.visitkorea.admin.server.application.modules.event.EventRullsetXLS;
import kr.or.visitkorea.admin.server.application.modules.files.upload.excel.model.ExcelData;
import kr.or.visitkorea.admin.server.application.modules.files.upload.excel.model.ImageContents;
import kr.or.visitkorea.admin.server.application.modules.files.upload.excel.model.ImageInfo;

@BusinessMapping(id="FILE_UPLOAD_XLS")
public class UploadXls extends AbstractModule {

	private Document checksumDocument;
	private String fileCheck;
	private List<Map<String, String>> xmlData;

    public UploadXls() {
    	checksumDocument = new Document();
    }

    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    	String requestType = this.getRequest().getParameter("requestType");
    	
    	AbstractModule executeModule = null;
    	if (requestType != null) {
	    	executeModule = new EventRullsetXLS();
	    	executeModule.setRequest(this.getRequest());
	    	executeModule.setParameter(this.parameterObject);
	    	executeModule.setResponse(this.getResponse());
	    	executeModule.run(resultHeaderObject, resultBodyObject);
	    	return;
    	}
    	
    	final String eihId = generateId();
    	final String filePath = getUploadFilePath();
    	final String fileName = FilenameUtils.removeExtension(getUploadFileName());
    	
        HashMap<String, Object> params;
        
		Document masterDocument = loadExcel(filePath, fileName, eihId);
		loadDatabase(masterDocument, fileName, eihId);
		
		xmlLoader(filePath, fileName, eihId);
        
        // 이미 파일 업로드가 종료되었기 때문에 업로드 완료상태와
        // 업로드된 파일의 정보를 `EXCEL_IMG_UPLOAD_HIST`에 등록한다.
        params = new HashMap<>();
        params.put("eihId", eihId);
        params.put("filePath", filePath);
        params.put("fileName", fileName);
        sqlSession.insert("kr.or.visitkorea.system.ExcelUploadMapper.insertUpload", params);

        if(!(filePath == null | filePath.length() == 0)) {
        	
        	int totalRow = masterDocument.getRootElement().getChildren().size() + checksumDocument.getRootElement().getChildren().size();
        	int skipRow = checksumDocument.getRootElement().getChildren().size();
        	
            final String xmlPath = getXmlPath(fileCheck, fileName, eihId);
            
            params = new HashMap<>();
            params.put("eihId", eihId);
            params.put("xmlPath", xmlPath);
            params.put("status", 4);
            params.put("totalRow", totalRow);
            params.put("skipRow", skipRow);
            sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateStatus", params);
            sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateCvsPath", params);
            sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateTotalRow", params);
            sqlSession.update("kr.or.visitkorea.system.ExcelUploadMapper.updateSkipRow", params);
        } else {
            System.out.println("XML 변환 실패: EIH_ID: " + eihId);
        }
        
        if (xmlData != null || xmlData.size() != 0)
        	resultBodyObject.put("xmlData", xmlData);
        
        HashMap<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("eihId", eihId);
        returnMap.put("xmlPath", getXmlPath(fileCheck, fileName, eihId));
        resultBodyObject.put("xmlFileInfo", returnMap);
    }
    
	private void xmlLoader(String filePath, String fileName, String eihId) {
		
		SAXBuilder builder = new SAXBuilder();
		fileCheck = "check";
		
//		System.out.println("fileName :: " + fileName);
		
		try {
			String xmlFilePath = Call.getProperty("image.xml.path") + "/" + fileCheck + "_" + fileName + "_" + eihId + ".xml";
			Document doc = builder.build(new InputSource(new FileReader(xmlFilePath)));
			
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
			
//			System.out.println("xmlData :: " + xmlData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Document loadExcel(String path, String prefix, String eihId) {

		try {
			
			List<ExcelData> retList = new ArrayList<ExcelData>();
			FileInputStream inputStream = new FileInputStream(path);
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			int rows = sheet.getPhysicalNumberOfRows();
			int contetId = -1;
			int contentCheckId = -1;
			int order = 1;
			
			ImageContents iContent = null;
			Element checksumRootElement = new Element("contents");
			checksumDocument.setRootElement(checksumRootElement);

			Document masterDocument = new Document();
			Element contentsElement = new Element("contents");
			masterDocument.setRootElement(contentsElement);
			
			for (int j = 1; j <= rows; j++) {

				if (sheet.getRow(j) != null) {

					Double contentIdRead = (Double) getCellValue(sheet.getRow(j).getCell(1));

					if (contentIdRead.intValue() != contetId) {

						contetId = contentIdRead.intValue();
						
						iContent = new ImageContents();
						iContent.setContentId(getCellValue(sheet.getRow(j).getCell(1)));
						iContent.setContentType(getCellValue(sheet.getRow(j).getCell(2)));
						iContent.setTitle(getCellValue(sheet.getRow(j).getCell(3)));
						iContent.setCategory1(getCellValue(sheet.getRow(j).getCell(4)));
						iContent.setCategory2(getCellValue(sheet.getRow(j).getCell(5)));
						iContent.setCategory3(getCellValue(sheet.getRow(j).getCell(6)));
						iContent.setArea(getCellValue(sheet.getRow(j).getCell(7)));
						iContent.setLocal(getCellValue(sheet.getRow(j).getCell(8)));

						order = 0;
					}
					
					Object cellValue = getCellValue(sheet.getRow(j).getCell(16));
					int newOrder = 0;
					
					if (cellValue.equals("null")) {
						order++;
						newOrder = order;
					} else {
						newOrder = 0;
					}
					
					String pathString = (String) getCellValue(sheet.getRow(j).getCell(15));
					
					if (!isExistsImage(iContent.getElement(), pathString)) {
						ImageInfo imageInfo = new ImageInfo(
								getCellValue(sheet.getRow(j).getCell(10)) + "",
								getCellValue(sheet.getRow(j).getCell(11)) + "", 
								getCellValue(sheet.getRow(j).getCell(12)) + "",
								getCellValue(sheet.getRow(j).getCell(13)) + "", 
								getCellValue(sheet.getRow(j).getCell(14)) + "",
								getCellValue(sheet.getRow(j).getCell(15)) + "", 
								getCellValue(sheet.getRow(j).getCell(16)) + "",
								getCellValue(sheet.getRow(j).getCell(9)) + "",
								newOrder);
						iContent.add(imageInfo);
					} else
						System.out.println(String.format("alredy add image~! :: %s(%s)", contetId, pathString));
					
					
					/**
					 *  1. 다음 행의 첫번째 셀 정보를 받아온다.
					 *  2. 받아온 정보와 현재 셀정보가 다르면 저장
					 **/
					Double cellValueLong = sheet.getRow(j+1) == null? -1 : (Double) getCellValue(sheet.getRow(j+1).getCell(1));
					
					if (j+1 == rows || cellValueLong.doubleValue() != contentIdRead.doubleValue() ) {
	
						Element targetContentElement = iContent.getElement();

						if (checkValidatMainImage(targetContentElement)) {
							
							String[] retStrArr = getCotID(targetContentElement.getAttributeValue("contentId"));
							
							if (retStrArr[0] == null && retStrArr[1] == null) {
								
								targetContentElement.setAttribute("reason", "해당 컨텐츠가 데이터베이스에 존재하지 않습니다.");
								checksumRootElement.addContent(targetContentElement);
								
							} else {
								
								targetContentElement.setAttribute("cotId", retStrArr[0]);
								targetContentElement.setAttribute("contentType", retStrArr[1]);
								contentsElement.addContent(targetContentElement);
								
							}
							
						} else {
							checksumRootElement.addContent(targetContentElement);
//							System.out.println("check_image.size :: " + checksumRootElement.getChildren("imageContent").size());
						}
						
					}
				}
			}
			
			// output check target data
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			
			fileCheck = "check";

			outputter.output(checksumDocument, new FileWriter(String.format(Call.getProperty("image.xml.path") + "/" + fileCheck+ "_%s_" + eihId + ".xml", prefix)));
//			System.out.println("check target data size :: " + checksumDocument.getRootElement().getChildren().size());
//			System.out.println("masterDoc.size :: " + masterDocument.getRootElement().getChildren("imageContent").size());
			
			return masterDocument;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
    
	private boolean checkValidatMainImage(Element targetContentElement) {

		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("image[@main='true']", Filters.element());
		int evaluateSize = xp.evaluate(targetContentElement).size();
		
		if (evaluateSize == 0) {
			
			try {
				String urlString = checkExistsMainImageInDatabase(targetContentElement);
				
				if (urlString == null || urlString.trim().length() == 0) {
					targetContentElement.setAttribute("mainImageCount", evaluateSize+"");
					targetContentElement.setAttribute("reason", "엑셀과 데이터베이스 모두 메인 이미지의 설정이 없습니다.");
					
					return false;
				}else {
					if (isExistsImage(targetContentElement, urlString)) {
						Element tgrImageElement = getExistsImage(targetContentElement, urlString);
						int orderInt = Integer.parseInt(tgrImageElement.getAttributeValue("order"));
						
						tgrImageElement.setAttribute("main", "true");
						tgrImageElement.setAttribute("order", "0");
						targetContentElement.setAttribute("reason", "엑셀에 정의되지 않은 메인이미지 설정을 데이터베이스 기준으로 정의했습니다.");
						reOrderElement(orderInt, targetContentElement);
						
						return true;
						
					}else {
						targetContentElement.setAttribute("reason", "데이터베이스 설정에 해당되는 이미지가 엑셀에 존재하지 않습니다.");
						
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (evaluateSize > 1) {
			targetContentElement.setAttribute("mainImageCount", evaluateSize+"");
			
			return false;
		}

		if (evaluateSize == 1) 
			return true;
		
		return false;
	}
	
	private void loadDatabase(Document masterDocument, String prefix, String eihId) {
		try {
			Element rootElement = masterDocument.getRootElement();
			List<Element> imageContentList = rootElement.getChildren("imageContent");
			
//			System.out.println("list.size :: " + imageContentList.size());
			for (Element imageContentElement : imageContentList) {
				setupContentElement(imageContentElement);
				
				
			}
			
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			fileCheck = "master";
			outputter.output(masterDocument, new FileWriter(String.format(Call.getProperty("image.xml.path") + "/" + fileCheck + "_%s_" + eihId + ".xml",prefix)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupContentElement(Element imageContentElement) {
		String cotId = imageContentElement.getAttributeValue("cotId");
		Map<String, String>  contIdImageURLMap = getDatabaseImageInformation(cotId);
		
		List<Element> images = imageContentElement.getChildren("image");
		for (Element image : images) {
			String targetImgId = contIdImageURLMap.get(image.getAttributeValue("path"));
			
			if (targetImgId == null) {
				// create image id;
				targetImgId = UUID.randomUUID().toString();
				image.setAttribute("imgId", targetImgId);
				image.setAttribute("isNew", "true");
				image.setAttribute("reason", "데이터베이스에 존재 하지 않아 아이디가 새로 생성되었습니다.");
			}else{
				image.setAttribute("imgId", targetImgId);
				image.setAttribute("isNew", "false");
			}
		}
	}
	
	private void reOrderElement(int orderInt, Element targetContentElement) {
		for (Element imageElement : targetContentElement.getChildren("image")) {
			int targetOrder = Integer.parseInt(imageElement.getAttributeValue("order"));

			if (targetOrder >= orderInt) {
				targetOrder--;
				imageElement.setAttribute("order", targetOrder+"");
			}
		}
	}
	
	private Map<String, String> getDatabaseImageInformation(String cotId) {
		Map<String, String> retMap = new HashMap<String, String>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("cotId", cotId);
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.ExcelUploadMapper.selectImageId", params);
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();
		
		if (!convertJSONString.equals("null"))
			for (HashMap<String, Object> result : returnMap) {
				JSONObject jsonObj = new JSONObject(result);
				
				if (!jsonObj.has("URL"))
					jsonObj.put("URL", "");
				
				if (!jsonObj.has("IMG_ID"))
					jsonObj.put("IMG_ID", "");
				
				retMap.put(jsonObj.getString("URL"), jsonObj.getString("IMG_ID"));
			}
		
		return retMap;
	}
	
	private String checkExistsMainImageInDatabase(Element targetContentElement) {
		
		String contentId = targetContentElement.getAttributeValue("contentId");
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("contentId", contentId);
		
		HashMap<String, Object> returnMap = sqlSession.selectOne("kr.or.visitkorea.system.ExcelUploadMapper.selectURL", params);
		
		JSONObject json = new JSONObject(returnMap);
		
		if (json.has("URL"))
			return returnMap.get("URL").toString();
		
		return null;
	}
	
	private String[] getCotID(String contentId) {
		String[] retStringArray = new String[2];
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("contentId", contentId);
		
		HashMap<String, Object> returnMap = sqlSession.selectOne("kr.or.visitkorea.system.ExcelUploadMapper.selectCotId", params);
		
		JSONObject json = new JSONObject(returnMap);
		
		if (json.has("COT_ID"))
			retStringArray[0]=returnMap.get("COT_ID").toString();
		
		if (json.has("CONTENT_TYPE"))
			retStringArray[1]=returnMap.get("CONTENT_TYPE").toString();
		
		return retStringArray;
	}
	
	private static boolean isExistsImage(Element element, String targetString) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile(String.format("image[@path='%s']", targetString), Filters.element());

		if (xp.evaluate(element).size() > 0)
			return true;
		else
			return false;
	}
	
	private static Element getExistsImage(Element element, String targetString) {
		
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile(String.format("image[@path='%s']", targetString), Filters.element());

		return xp.evaluateFirst(element);
	}

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private String getUploadFilePath() {
//    	System.out.println("getUploadFilePath.fullPath :: " + parameterObject.getJSONArray("files").getJSONObject(0).getString("fullPath"));
        return parameterObject.getJSONArray("files").getJSONObject(0).getString("fullPath");
    }

    private String getUploadFileName() {
//    	System.out.println("getUploadFileName :: " + parameterObject.getJSONArray("files").getJSONObject(0).getString("fileName"));
        return parameterObject.getJSONArray("files").getJSONObject(0).getString("fileName");
    }

    private String getXmlPath(String fileCheck, String filePath, String eihId) {
        String xmlFilePath = "master_" + filePath + "_" + eihId + ".xml";
        return xmlFilePath;
    }
    
	private static Object getCellValue(HSSFCell cell) {

		Object value = null;
		if (cell != null) {
			
			CellType cellType = cell.getCellType();
	
			if (cellType.equals(CellType.BLANK)) {
				value = "null";
			} else if (cellType.equals(CellType.FORMULA)) {
				value = cell.getCellFormula();
			} else if (cellType.equals(CellType.NUMERIC)) {
				value = cell.getNumericCellValue();
			} else if (cellType.equals(CellType.BOOLEAN)) {
				value = cell.getBooleanCellValue();
			} else if (cellType.equals(CellType.STRING)) {
				value = cell.getStringCellValue();
			} else if (cellType.equals(CellType.ERROR)) {
				value = cell.getErrorCellValue();
			}
		}else {
			return "null";
		}
		return value;
	}
	
	private void isCreatedDirectory(String directoryName) {
		File directory = new File(directoryName);
		
		if (!directory.exists())
			directory.mkdir();
	}
	
	private String createCurrentDate() {
		
		SimpleDateFormat curDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String curDate = curDateFormat.format(calendar.getTime());
		
		return curDate;
	}

    @Override
    protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
//        System.out.println("request object :: " + parameterObject);
    }

    @Override
    protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        resultBodyObject.put("result", parameterObject.get("files"));
        resultObject.put("return.type", "text/plain");
    }
}
