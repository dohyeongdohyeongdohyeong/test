package kr.or.visitkorea.admin.server.application.modules.zikimi;

import java.io.IOException;
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

import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_ZIKIMI_LIST_XLS")
public class SelectZikimiListXls extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HttpServletRequest request = this.getRequest();
		HttpServletResponse response = this.getResponse();
		
		List<HashMap<String, Object>> returnMap;
		HashMap<String, Object> paramterMap = new HashMap<>();
		if (parameterObject.has("viewType"))
			paramterMap.put("utype", parameterObject.getNumber("utype"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getString("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getString("edate"));
		if(parameterObject.has("title"))
			paramterMap.put("title", parameterObject.getString("title"));
		if(parameterObject.has("id"))
			paramterMap.put("id", parameterObject.getString("id"));
		if(parameterObject.has("name"))
			paramterMap.put("name", parameterObject.getString("name"));
		if(parameterObject.has("reqType"))
			paramterMap.put("reqType", parameterObject.getInt("reqType"));
		if(parameterObject.has("contenttype"))
			paramterMap.put("contenttype", parameterObject.getInt("contenttype"));
		if(parameterObject.has("status"))
			paramterMap.put("status", parameterObject.getInt("status"));
		
		if (parameterObject.has("reqType") && parameterObject.getInt("reqType") == 1) {
			returnMap = sqlSession.selectList("kr.or.visitkorea.system.ZikimiMapper.selectNewZikimiList", paramterMap );
		} else {
			returnMap = sqlSession.selectList("kr.or.visitkorea.system.ZikimiMapper.selectZikimiList", paramterMap );
		}
		
		String strClient = request.getHeader("User-Agent");
		String sFileName = "회원관리-활동관리-관광정보지킴이" + ".xlsx";

		XSSFWorkbook objWorkBook = new XSSFWorkbook();
		XSSFSheet objSheet = objWorkBook.createSheet("통계");
		XSSFRow objRow = null;
		XSSFCell objCell = null;

		response.reset();
	    CellStyle headStyle = objWorkBook.createCellStyle();
	    // 가는 경계선을 가집니다.
	    headStyle.setBorderTop(BorderStyle.THIN);
	    headStyle.setBorderBottom(BorderStyle.THIN);
	    headStyle.setBorderLeft(BorderStyle.THIN);
	    headStyle.setBorderRight(BorderStyle.THIN);

	    // 배경색은 노란색입니다.
	    headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
	    headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    // 데이터는 가운데 정렬합니다.
	    headStyle.setAlignment(HorizontalAlignment.CENTER);
	    
		objSheet.setColumnWidth(0, 8 * 256);
		objSheet.setColumnWidth(1, 40 * 256);
		objSheet.setColumnWidth(2, 15 * 256);
		objSheet.setColumnWidth(3, 15 * 256);
		objSheet.setColumnWidth(4, 40 * 256);
		objSheet.setColumnWidth(5, 40 * 256);
		objSheet.setColumnWidth(6, 15 * 256);
		objSheet.setColumnWidth(7, 20 * 256);
		objSheet.setColumnWidth(8, 20 * 256);
		objSheet.setColumnWidth(9, 15 * 256);
		objSheet.setColumnWidth(10, 20 * 256);
		objRow = objSheet.createRow((short) 0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("Key(Seq)");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("요청구분");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("카테고리");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("콘텐츠명");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("내용");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("작성자");
		objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("SNSKey");
		objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("등록일시");
		objCell = objRow.createCell(9);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리일시");
		objCell = objRow.createCell(10);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리상태");
		objCell = objRow.createCell(11);	objCell.setCellStyle(headStyle); objCell.setCellValue("댓글확인");

		int size = returnMap.size();
	    for (int i = 0; i < size; i++) {
	    	HashMap<String, Object> mapRow = returnMap.get(i);
	    	String ctype = UI.getContentType((int) (mapRow.containsKey("contenttype") ? (int) mapRow.get("contenttype") : -1));
			String requestType = (int) mapRow.get("reqType") == 0 ? "수정요청" 
					: (int) mapRow.get("reqType") == 1 ? "신규등록" 
					: (int) mapRow.get("reqType") == 2 ? "구석피디아season1"
					: (int) mapRow.get("reqType") == 3 ? "구석피디아season2"
					: (int) mapRow.get("reqType") == 4 ? "구석피디아season3" : "-";
			String sstatus = "접수";
			String title = requestType.equals("신규등록") 
					? mapRow.containsKey("reqtitle") ? mapRow.get("reqtitle").toString()
					: ""
					: mapRow.containsKey("title")? mapRow.get("title").toString() : "";
			String Body = mapRow.containsKey("body")? mapRow.get("body").toString() : "";
			
			if((int) mapRow.get("reqType") > 1) {
				
			String[] bodys = Body.split("\\|\\|");
			
			Body = "";
				
			if(bodys.length > 0) Body += "전화번호 : " + bodys[0] + "\r\n";
			if(bodys.length > 1) Body += "주소 : " + bodys[1] + "\r\n";
			if(bodys.length > 2) Body += "홈페이지 : " + bodys[2] + "\r\n";
			if(bodys.length > 3) Body += "캐치프레이즈 : " + bodys[3] + "\r\n";
			if(bodys.length > 4) Body += "기타 : " + bodys[4] + "\r\n";

			}
			
			String Key = mapRow.containsKey("zikid")? mapRow.get("zikid").toString() : "";
			switch (mapRow.containsKey("status") ? (int) mapRow.get("status") : 0) {
				case 0: sstatus = "접수"; break;
				case 1: sstatus = "처리중"; break;
				case 2: sstatus = "승인완료"; break;
				case 3: sstatus = "승인거절"; break;
			}
	    	objRow = objSheet.createRow((short) i + 1);
	    	objRow.createCell(0).setCellValue(i + 1);
	        objRow.createCell(1).setCellValue(Key);
	        objRow.createCell(2).setCellValue(requestType);
	        objRow.createCell(3).setCellValue(ctype);
	        objRow.createCell(4).setCellValue(title);
	        objRow.createCell(5).setCellValue(Body);
	        objRow.createCell(6).setCellValue(mapRow.containsKey("snsid") ? mapRow.get("snsid").toString() : "");
	        objRow.createCell(7).setCellValue(mapRow.containsKey("SNS_ID") ? mapRow.get("SNS_ID").toString() : "");
	        objRow.createCell(8).setCellValue(mapRow.containsKey("cdate") ? mapRow.get("cdate").toString() : "");
	        objRow.createCell(9).setCellValue(mapRow.containsKey("udate") ? mapRow.get("udate").toString() : "");
	        objRow.createCell(10).setCellValue(sstatus);
	        objRow.createCell(11).setCellValue("");
	    }
	    
	    try {
			sFileName = new String ( sFileName.getBytes("KSC5601"), "8859_1");
			if (strClient.indexOf("MSIE 5.5") > -1) {
			    response.setHeader("Content-Disposition", "filename=" + sFileName + ";");
			} else {
			    response.setContentType("application/vnd.ms-excel");
			    response.setHeader("Content-Disposition", "attachment; filename=" + sFileName + ";");
			}
			objWorkBook.write(response.getOutputStream());
		    objWorkBook.close();
		    
			resultObject.put("return.type", "response-end");
		    response.getOutputStream().close();
		} catch (IOException e) {
        	System.out.println(e.toString());
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	return;
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
