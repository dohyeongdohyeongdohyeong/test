package kr.or.visitkorea.admin.client.manager.upload.excel.helper;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;

import kr.or.visitkorea.admin.client.manager.upload.excel.model.ExcelData;
import kr.or.visitkorea.admin.client.manager.upload.excel.model.ImageContents;

public class DatabaseGenericProcessor {
	
	private static String driver        = "org.mariadb.jdbc.Driver";
	private static String url           = "jdbc:mariadb://kto.real:3306/KTO";
	private static String uId           = "front";
	private static String uPwd          = "front!@#$";
	
	private static Connection               con;
	private static PreparedStatement        pstmt;
	private static ResultSet                rs;

	private static Document checksumDocument = new Document();
	private static HashMap<Object, List<Map<String, String>>> tablesInfo;

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

		databaseInit();
		String path = String.format("/Users/jungwoochoi/Downloads/A.xlsx");
		XSSFWorkbook workbook = loadExcel("/Users/jungwoochoi/Downloads/A.xlsx");

	}

	private static void databaseInit() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		con = DriverManager.getConnection(url, uId, uPwd);
	}

	private static XSSFWorkbook loadExcel(String path) throws IOException {

	      BufferedWriter out = new BufferedWriter(new FileWriter("/Users/jungwoochoi/Downloads/A.csv"));

		List<ExcelData> retList = new ArrayList<ExcelData>();

		FileInputStream inputStream = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		int sheetCount = workbook.getNumberOfSheets();
		
		tablesInfo = new HashMap<Object, List<Map<String, String>>>();
		
		for (int i=2; i<sheetCount; i++) {
				
			XSSFSheet sheet = workbook.getSheetAt(i);
	
			int rows = sheet.getPhysicalNumberOfRows();
			int contetId = -1;
			int order = 1;
			
			
			ImageContents iContent = null;
			Element checksumRootElement = new Element("contents");
			checksumDocument.setRootElement(checksumRootElement);
	
			Document masterDocument = new Document();
			Element contentsElement = new Element("contents");
			masterDocument.setRootElement(contentsElement);
			
			for (int j = 2; j <= rows; j++) {
	
				XSSFRow row = sheet.getRow(j);
				
				if (row != null) {

					// regist table information
					String dbKey = getCellValue(row.getCell(0)).toString();
					String tableKey = getCellValue(row.getCell(1)).toString();
					if (tableKey != null && tableKey.length() > 0) {
						List<Map<String, String>> tableRecordList = tablesInfo.get(getCellValue(row.getCell(1)));
						if (tableRecordList == null) {
							try {
								List<Map<String, String>> tableInfo = getTableFields(tableKey);
								tablesInfo.put(tableKey, tableInfo);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
					
					String fieldName = getCellValue(row.getCell(2)).toString();
					String fieldExampleValue = getExampleValue(tableKey, fieldName);
					if (fieldExampleValue != null && fieldExampleValue.length() > 50){
						out.write(String.format("%s, %s, %s, %s, %d, 값이 길어 생략되었습니다.", dbKey, tableKey, fieldName, 
								fieldExampleValue
								.replaceAll("\n", "")
								.replaceAll(",", "&#44;")
								.replaceAll(" ","")
								.substring(0, 50), fieldExampleValue.length()));
						out.newLine();
					}else {
						if (fieldExampleValue == null) fieldExampleValue = "null";
						out.write(String.format("%s, %s, %s, %s", dbKey, tableKey, fieldName, 
								fieldExampleValue
								.replaceAll("\n", "")
								.replaceAll(",", "&#44;")
								.replaceAll(" ","")));
						out.newLine();
					}
					
				}
	
			}
			System.out.println("==========================> [" + i + "] ");
		}
		
	      out.close();

		return workbook;

	}

	private static String getExampleValue(String tableKey, String fieldName) {
		
		if (tablesInfo.get(tableKey) != null) {
			int tableRecordSize = tablesInfo.get(tableKey).size();
			
			if (tablesInfo.get(tableKey) != null && tableRecordSize > 0) {
				String columnValue = null;
				int recIndex = 0;
				while(columnValue == null && recIndex < tableRecordSize) {
					columnValue = tablesInfo.get(tableKey).get(recIndex).get(fieldName);
					recIndex++;
				}
				return columnValue;
			}
		}
		return null;
	}

	private static List<Map<String, String>> getTableFields(String tableName) throws SQLException {
		
		String sql = String.format("select * from %s limit 0, 10", tableName);
	
		//System.out.println("쿼리 :: " + sql);
		
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();

		List<Map<String, String>> resultSet = new ArrayList<Map<String, String>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		while (rs.next()) {

			Map<String, String> recordMap = new HashMap<String, String>();
			int columnCount = rsmd.getColumnCount();
			for (int i=1; i<=columnCount; i++) {
				recordMap.put(rsmd.getColumnName(i), rs.getString(i));
			}
			resultSet.add(recordMap);
		}
		
		return resultSet;
		
	}

	private static Object getCellValue(Cell cell) {

		Object value = null;
		if (cell != null) {
			
			CellType cellType = cell.getCellType();
	
			if (cellType.equals(CellType.BLANK)) {
				value = "";
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
