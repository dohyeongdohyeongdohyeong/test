package kr.or.visitkorea.admin.client.manager.upload.excel.helper;

import java.io.FileInputStream;
import java.io.FileWriter;
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
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import kr.or.visitkorea.admin.client.manager.upload.excel.model.ExcelData;
import kr.or.visitkorea.admin.client.manager.upload.excel.model.ImageContents;
import kr.or.visitkorea.admin.client.manager.upload.excel.model.ImageInfo;

public class ExcelDataGenericProcessor {
	
	private static String driver        = "org.mariadb.jdbc.Driver";
	private static String url           = "jdbc:mariadb://kto.test:3306/KTO";
	private static String uId           = "front";
	private static String uPwd          = "front!@#$";
	
	private static Connection               con;
	private static PreparedStatement        pstmt;
	private static ResultSet                rs;

	private static Document checksumDocument = new Document();

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

		List<String> names = new ArrayList<String>();
/*
		names.add("a");
		names.add("b");
		names.add("c");
		names.add("d");
*/
		names.add("e");
		names.add("f");
		names.add("g");
		names.add("h");
		names.add("i");
		names.add("j");
		
		databaseInit();

		for (String name : names) {
			String path = String.format("/Users/jungwoochoi/Downloads/변환/%s.xls", name);
			Document masterDocument = loadExcel(path, name);
			loadDatabase(masterDocument, name);
		}

	}

	private static void loadDatabase(Document masterDocument, String prefix) throws ClassNotFoundException, SQLException, IOException {
		

		Element rootElement = masterDocument.getRootElement();
		List<Element> imageContentList = rootElement.getChildren("imageContent");
		for (Element imageContentElement : imageContentList) {
			setupContentElement(imageContentElement);
		}
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		outputter.output(masterDocument, new FileWriter(String.format("/Users/jungwoochoi/IMAGE_FROM_XLS/master_%s.xml",prefix)));
		
	}

	private static void setupContentElement(Element imageContentElement) throws SQLException {

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

	private static Map<String, String> getDatabaseImageInformation(String cotId) throws SQLException {
		Map<String, String> retMap = new HashMap<String, String>();
		
		String sql = String.format("select IMG_ID, URL from IMAGE where COT_ID='%s'", cotId);
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			retMap.put(rs.getString("URL"), rs.getString("IMG_ID"));
		}
		
		return retMap;
	}

	private static String getCotID(String contentId) throws SQLException {
	
		String sql = String.format("select cotid(%s) COT_ID", contentId);
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getString("COT_ID");
		}
		
		return null;
	}

	private static void databaseInit() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		con = DriverManager.getConnection(url, uId, uPwd);
	}

	private static Document loadExcel(String path, String prefix) throws IOException, SQLException {

		List<ExcelData> retList = new ArrayList<ExcelData>();

		FileInputStream inputStream = new FileInputStream(path);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		HSSFSheet sheet = workbook.getSheetAt(0);

		int rows = sheet.getPhysicalNumberOfRows();
		int contetId = -1;
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

					if (iContent != null) {
						
						Element targetContentElement = iContent.getElement();
						
						
						if (checkValidatMainImage(targetContentElement)) {
							String cotId = getCotID(targetContentElement.getAttributeValue("contentId"));
							if (cotId == null) {
								targetContentElement.setAttribute("reason", "해당 컨텐츠가 데이터베이스에 존재하지 않습니다.");
								checksumRootElement.addContent(targetContentElement);
							}else {
								targetContentElement.setAttribute("cotId", cotId);
								contentsElement.addContent(targetContentElement);
							}
						}else{
							checksumRootElement.addContent(targetContentElement);
						}
						
					}

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
				}else {
					newOrder = 0;
				}
				
				String pathString = (String)getCellValue(sheet.getRow(j).getCell(15));
				
				if (!isExistsImage(iContent.getElement(), pathString)){
				
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

				}else {
					
					System.out.println(String.format("alredy add image~! :: %s(%s)", contetId, pathString));

				}

			}

		}
		
		// output check target data
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		outputter.output(checksumDocument, new FileWriter(String.format("/Users/jungwoochoi/IMAGE_FROM_XLS/check_%s.xml", prefix)));

		System.out.println("check target data size :: " + checksumDocument.getRootElement().getChildren().size());
		
		return masterDocument;
	}

	private static boolean isExistsImage(Element element, String targetString) {
		
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile(String.format("image[@path='%s']", targetString), Filters.element());

		if (xp.evaluate(element).size() > 0) {
			return true;
		}else {
			return false;
		}
	
	}

	private static Element getExistsImage(Element element, String targetString) {
		
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile(String.format("image[@path='%s']", targetString), Filters.element());

		return xp.evaluateFirst(element);

	}

	private static boolean checkValidatMainImage(Element targetContentElement) throws SQLException {

		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("image[@main='true']", Filters.element());
		int evaluateSize = xp.evaluate(targetContentElement).size();
		
		if (evaluateSize == 0) {
			
			String urlString = checkExistsMainImageInDatabase(targetContentElement);
			
			if (urlString == null) {
			
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
			
		}
		
		if (evaluateSize > 1) {
			targetContentElement.setAttribute("mainImageCount", evaluateSize+"");
			return false;
		}

		if (evaluateSize == 1) return true;
		
		return false;
	}

	private static void reOrderElement(int orderInt, Element targetContentElement) {
		
		for (Element imageElement : targetContentElement.getChildren("image")) {
			
			int targetOrder = Integer.parseInt(imageElement.getAttributeValue("order"));

			if (targetOrder >= orderInt) {
				targetOrder--;
				imageElement.setAttribute("order", targetOrder+"");
			}
		
		}
		
	}

	private static String checkExistsMainImageInDatabase(Element targetContentElement) throws SQLException {
		
		String contentId = targetContentElement.getAttributeValue("contentId");
		String sql = String.format("select URL from IMAGE where IMG_ID in (select FIRST_IMAGE from DATABASE_MASTER where COT_ID = cotid(%s))", contentId);
	
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getString("URL");
		}
		
		return null;
		
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

}
