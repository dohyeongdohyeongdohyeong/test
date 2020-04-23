package kr.or.visitkorea.admin.server.application.modules.stamp;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_STAMP_STATS_XLS")
public class SelectStampListXls extends AbstractModule {
	
	private XSSFWorkbook workBook = new XSSFWorkbook();
	private XSSFSheet sheet = workBook.createSheet("통계");
	private int index = 1;
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HttpServletRequest request = this.getRequest();
		HttpServletResponse response = this.getResponse();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (this.parameterObject.has("evntId"))
			params.put("evntId", this.parameterObject.getString("evntId"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		if (this.parameterObject.has("startDate") && !this.parameterObject.getString("startDate").equals(""))
			params.put("startDate", this.parameterObject.getString("startDate"));
		if (this.parameterObject.has("endDate") && !this.parameterObject.getString("endDate").equals(""))
			params.put("endDate", this.parameterObject.getString("endDate"));
		if (this.parameterObject.has("ordered"))
			params.put("ordered", this.parameterObject.getString("ordered"));
		
		List<HashMap<String, Object>> resultMap =
				sqlSession.selectList("kr.or.visitkorea.system.StampMapper.selectStampStats", params);
		List<HashMap<String, Object>> resultCnt =
				sqlSession.selectList("kr.or.visitkorea.system.StampMapper.selectStampStatsCnt", params);
		List<HashMap<String, Object>> resultTot =
				sqlSession.selectList("kr.or.visitkorea.system.StampMapper.selectStampStatsTotal", params);
		
		final String CLIENT = request.getHeader("User-Agent");
		final String FILENAME = "발도장이벤트_통계.xlsx";
		
		HSSFColorPredefined bgColor = HSSFColorPredefined.YELLOW;
		XSSFRow row = this.sheet.createRow((short) 0);
		this.addCell(row, 0, 20, "일자", bgColor);
		this.addCell(row, 1, 25, "발도장 획득수", bgColor);
		this.addCell(row, 2, 25, "1차 이벤트 미션 완료수", bgColor);
		this.addCell(row, 3, 25, "1차 이벤트 참여 완료수", bgColor);
		this.addCell(row, 4, 25, "2차 이벤트 미션 완료수", bgColor);
		this.addCell(row, 5, 25, "2차 이벤트 참여 완료수", bgColor);
		
		resultMap.forEach(this::addRowData);
		resultTot.forEach(this::addTotalRowData);
		
		try {
			String SFILENAME = new String(FILENAME.getBytes("KSC5601"), "8859_1");
			
			if (CLIENT.indexOf("MSIE 5.5") > -1) {
				response.setHeader("Content-Disposition", "filename=" + SFILENAME + ";");
			} else {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename=" + SFILENAME + ";");
			}
			this.workBook.write(response.getOutputStream());
			this.workBook.close();

			resultObject.put("return.type", "response-end");
			response.getOutputStream().close();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
	
	private XSSFRow addRowData(HashMap<String, Object> item) {
		XSSFRow row = this.sheet.createRow((short) index++);
		HSSFColorPredefined bgColor = HSSFColorPredefined.WHITE;
		this.addCell(row, 0, 20, item.containsKey("CREATE_DATE") ? item.get("CREATE_DATE").toString() : "", bgColor);
		this.addCell(row, 1, 25, item.containsKey("STP_CNT") ? item.get("STP_CNT").toString() : "", bgColor);
		this.addCell(row, 2, 25, item.containsKey("ONE_MISSION_CNT") ? item.get("ONE_MISSION_CNT").toString() : "", bgColor);
		this.addCell(row, 3, 25, item.containsKey("ONE_ATTEND_CNT") ? item.get("ONE_ATTEND_CNT").toString() : "", bgColor);
		this.addCell(row, 4, 25, item.containsKey("TWO_MISSION_CNT") ? item.get("TWO_MISSION_CNT").toString() : "", bgColor);
		this.addCell(row, 5, 25, item.containsKey("TWO_ATTEND_CNT") ? item.get("TWO_ATTEND_CNT").toString() : "", bgColor);
		return row;
	}

	private XSSFRow addTotalRowData(HashMap<String, Object> item) {
		XSSFRow row = this.sheet.createRow((short) index);
		HSSFColorPredefined bgColor = HSSFColorPredefined.GREY_25_PERCENT;
		this.addCell(row, 0, 20, "합계", bgColor);
		this.addCell(row, 1, 25, item.containsKey("STP_SUM") ? item.get("STP_SUM").toString() : "", bgColor);
		this.addCell(row, 2, 25, item.containsKey("ONE_MISSION_SUM") ? item.get("ONE_MISSION_SUM").toString() : "", bgColor);
		this.addCell(row, 3, 25, item.containsKey("ONE_ATTEND_SUM") ? item.get("ONE_ATTEND_SUM").toString() : "", bgColor);
		this.addCell(row, 4, 25, item.containsKey("TWO_MISSION_SUM") ? item.get("TWO_MISSION_SUM").toString() : "", bgColor);
		this.addCell(row, 5, 25, item.containsKey("TWO_ATTEND_SUM") ? item.get("TWO_ATTEND_SUM").toString() : "", bgColor);
		return row;
	}
	
	private XSSFCell addCell(XSSFRow row, int columnIndex, int width, String value, HSSFColorPredefined bgColor) {
		this.sheet.setColumnWidth(columnIndex, width * 256);
		XSSFCell cell = row.createCell(columnIndex);
		cell.setCellStyle(this.setCellStyle(bgColor));
		cell.setCellValue(value);
		return cell;
	}

	private CellStyle setCellStyle(HSSFColorPredefined bgColor) {
		CellStyle style = this.workBook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setFillForegroundColor(bgColor.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    return style;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
