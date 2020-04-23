package kr.or.visitkorea.admin.server.application.modules.event;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="BLACK_LIST_XLS")
public class BlacklistXLS extends AbstractModule {

	private XSSFWorkbook workBook = null;
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		try {
			
		
		String fileName = this.getFileName();
		String filePath = this.getFilePath();
		String saveName = this.getsaveName();
		String uuid = UUID.randomUUID().toString();
		
		
		
		XSSFSheet sheet = this.getWorkSheet(uuid, fileName, filePath);
		
		if (sheet == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "엑셀 워크시트를 찾을 수 없습니다.");
			return;
		}

		JSONArray rowArray = new JSONArray();
		for (int i = sheet.getFirstRowNum(); ; i++) {
			XSSFRow row = sheet.getRow(i);
			
			JSONArray cellArray = new JSONArray();
			if (row == null)
				break;
			
			for (int j = row.getFirstCellNum(); ; j++) {
				XSSFCell cell = row.getCell(j);
				
				if (cell == null)
					break;
				
				cellArray.put(cell);
			}
			rowArray.put(cellArray);
		}
		
		if (rowArray.length() == 0) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "오류가 발생했습니다. 관리자에게 문의해주세요");
		} else {
			resultBodyObject.put("process", "success");
			resultBodyObject.put("resultArr", rowArray);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private XSSFSheet getWorkSheet(String uuid, String fileName, String filePath) {
		try (FileInputStream fis = new FileInputStream(filePath)) {
			this.workBook = new XSSFWorkbook(fis);
			
			return this.workBook.getSheetAt(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getFileName() {
		return parameterObject.getJSONArray("files").getJSONObject(0).getString("fileName");
	}
	
	private String getFilePath() {
		return parameterObject.getJSONArray("files").getJSONObject(0).getString("fullPath");
	}
	private String getsaveName() {
		return parameterObject.getJSONArray("files").getJSONObject(0).getString("saveName");
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
}
