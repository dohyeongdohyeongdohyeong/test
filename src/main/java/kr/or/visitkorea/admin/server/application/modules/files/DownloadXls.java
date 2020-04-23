package kr.or.visitkorea.admin.server.application.modules.files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.javassist.bytecode.stackmap.Tracer;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import com.google.gwt.json.client.JSONNumber;

import gwt.material.design.client.ui.MaterialCheckBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


@BusinessMapping(id="FILE_DOWNLOAD_XLS")
public class DownloadXls extends AbstractModule {
    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    	HttpServletRequest request = this.getRequest();
		HttpServletResponse response = this.getResponse();
		String nameString = this.parameterObject.getString("select_type");
		String sFileName = "COMPANY_ORDER" + ".xlsx";

		System.out.println("nameString :: " + nameString);
		
		String strClient = request.getHeader("User-Agent");
    	try {
			response.reset();  //엑셀 한글 깨짐 방지
    		XSSFWorkbook objWorkBook = new XSSFWorkbook();
    		XSSFSheet objSheet = objWorkBook.createSheet("통계");
    		XSSFRow objRow = null;
    		XSSFCell objCell = null;
    		
    		// 테이블 헤더용 스타일
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

    	    // 마지막 합계용 테두리만 지정
    	    CellStyle tagStyle = objWorkBook.createCellStyle();
    	    tagStyle.setBorderTop(BorderStyle.THIN);
    	    tagStyle.setBorderBottom(BorderStyle.THIN);
    	    tagStyle.setBorderLeft(BorderStyle.THIN);
    	    tagStyle.setBorderRight(BorderStyle.THIN);
    	    tagStyle.setFillForegroundColor(HSSFColorPredefined.GREY_40_PERCENT.getIndex());
    	    if(nameString.equals("analysis_main")) {
    	    	analysis_main(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-메인" + ".xlsx";
    	    } else if(nameString.equals("analysis_tag")) {
    	    	analysis_tag(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-태그" + ".xlsx";
    		} else if(nameString.equals("analysis_sec_logo")) {
    			analysis_sec_logo(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-지자체섹션-로고" + ".xlsx";
			} else if(nameString.equals("analysis_sec_banner")) {
				analysis_sec_banner(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-지자체섹션-배너" + ".xlsx";
    		} else if(nameString.equals("analysis_sec_sigungu")) {
    			analysis_sec_sigungu(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-지자체섹션-소지역" + ".xlsx";
    		} else if(nameString.equals("analysis_contents")) {
    			analysis_contents(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-콘텐츠" + ".xlsx";
    		} else if(nameString.equals("analysis_banner")) {
    			analysis_banner(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-홍보배너" + ".xlsx";
    		} else if(nameString.equals("analysis_connect")) {
    			analysis_connect(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-접속" + ".xlsx";
    		} else if(nameString.equals("analysis_other_main")) {
    			analysis_other_main(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-타부서-메인" + ".xlsx";
    		} else if(nameString.equals("analysis_other_contents")) {
    			analysis_other_contents(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-타부서-콘텐츠" + ".xlsx";
    		} else if(nameString.equals("analysis_other_tag")) {
    			analysis_other_tag(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "통계-타부서-태그" + ".xlsx";
    		} else if(nameString.equals("zikimi_manager")) {
    			zikimi_manager(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "회원관리-활동관리-관광정보지킴이" + ".xlsx";
    		} else if(nameString.equals("repair")) {
    			repair(objSheet, objRow, objCell, headStyle,tagStyle);
    			sFileName = "유지보수요청관리" + ".xlsx";
    		} else if("comment".equals(nameString)) {
    			comment(objSheet, objRow, objCell, headStyle);
    			sFileName = "댓글 내역" + ".xlsx";
    		} else if("event_result_join".equals(nameString)) {
    			event_result_join(objSheet, objRow, objCell, headStyle);
    			sFileName = "이벤트참여통계" + ".xlsx";
    		} else if("event_result_join_user".equals(nameString)) {
    			event_result_join_user(objSheet, objRow, objCell, headStyle);
    			sFileName = "이벤트참여정보" + ".xlsx";
    		} else if("event_win_Sample".equals(nameString)) {
    			sFileName = "이벤트 경품 선정" + ".xlsx";
    			objSheet.setColumnWidth(0, 3000);
    			objSheet.setColumnWidth(1, 6000);
    			objRow=objSheet.createRow((short)0);
    			objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("순번");
    			objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("경품키");
    		} else if("BlackList_Sample".equals(nameString)) {
    			sFileName = "블랙리스트 양식" + ".xlsx";
    			objSheet.setColumnWidth(0, 7000);
    			objRow=objSheet.createRow((short)0);
    			objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("블랙리스트 전화번호");
    		} else if("codeCategoryXls".equals(nameString)) {
    			codeCategoryXls(objSheet, objRow, objCell, headStyle);
    			sFileName = "코드분류관리" + ".xlsx";
    		} else if("codeRecomm".equals(nameString)) {
    			codeRecommed(objSheet, objRow, objCell, headStyle);
    			sFileName = "분류관리-추천컨텐츠" + ".xlsx";
    		} else if("RecommandList".equals(nameString)) {
    			RecommandList(objSheet, objRow, objCell, headStyle);
    			sFileName = "추천컨텐츠-목록" + ".xlsx";
    		}  else if("DatabaseList".equals(nameString)) {
    			DatabaseList(objSheet, objRow, objCell, headStyle);
    			sFileName = "데이터베이스컨텐츠-목록" + ".xlsx";
    		}  else if("UserImageList".equals(nameString)) {
    			UserImageList(objSheet, objRow, objCell, headStyle);
    			sFileName = "사용자 사진목록" + ".xlsx";
    		} else if(nameString.equals("spotHistorySearchList")) {
    			selectSpotHistoryList(objSheet, objRow, objCell, headStyle);
    			sFileName = "히스토리검색(업소검색)-리스트" + ".xlsx";
    		} else if(nameString.equals("articleHistorySearchList")) {
    			selectArticleHistoryList(objSheet, objRow, objCell, headStyle);
    			sFileName = "히스토리검색(기사검색)-리스트" + ".xlsx";
    		} else if(nameString.equals("wholeSpotHistoryList")) {
    			selectSpotHistoryList(objSheet, objRow, objCell, headStyle);
    			sFileName = "히스토리검색(업소검색)-전체리스트" + ".xlsx";
    		} else if(nameString.equals("wholeArticleHistoryList")) {
    			selectArticleHistoryList(objSheet, objRow, objCell, headStyle);
    			sFileName = "히스토리검색(기사검색)-전체리스트" + ".xlsx";
    		} else if(nameString.equals("articleRelatedHistoryList")) {
    			selectRelatedHistoryList(objSheet, objRow, objCell, headStyle);
    			sFileName = "기사관련-히스토리리스트" + ".xlsx";
    		} else if(nameString.equals("PartnersContent")) {
    			PartnersContent(objSheet, objRow, objCell, headStyle);
    			sFileName = "파트너스-컨텐츠 사용 신청" + ".xlsx";
    		} else if(nameString.equals("PartnersMember")) {
    			PartnersMember(objSheet, objRow, objCell, headStyle);
    			sFileName = "파트너스-회원 정보" + ".xlsx";
    		} else if(nameString.equals("PartnersMemberActivity")) {
    			PartnersMemberActivity(objSheet, objRow, objCell, headStyle);
    			sFileName = "파트너스-회원활동분석" + ".xlsx";
    		} else if(nameString.equals("PartnersChannel")) {
    			PartnersChannel(objSheet, objRow, objCell, headStyle);
    			sFileName = "파트너스-홍보 신청" + ".xlsx";
    		} else if(nameString.equals("MemberInfo")) {
    			MemberInfo(objWorkBook);
    			sFileName = "회원관리-일반 정보" + ".xlsx";
    		} else if(nameString.equals("SnsCourse")) {
    			SnsCourse(objSheet, objRow, objCell, headStyle);
    			sFileName = "회원관리-활동관리-코스관리 " + ".xlsx";
    		}
    				
    	    
			sFileName = new String ( sFileName.getBytes("KSC5601"), "8859_1");
    		if (strClient.indexOf("MSIE 5.5") > -1) {
    		    response.setHeader("Content-Disposition", "filename=" + sFileName + ";");
    		} else {
    		    response.setContentType("application/vnd.ms-excel");
    		    response.setHeader("Content-Disposition", "attachment; filename=" + sFileName + ";");
    		}
		    objWorkBook.write(response.getOutputStream());
		    objWorkBook.close();
		    response.getOutputStream().close();
        } catch(Exception caught) {
        	System.out.println(caught.toString());
        	caught.printStackTrace();
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
    }

	private void SnsCourse(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.SnsMapper.SNSCourseExcelDownload" , 
				paramterMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] CheckListName = {"코스제목","코스수","작성자","등록일시","공개여부","지역값(코스 등록된 지역)"};
		objRow=objSheet.createRow((short)0);
		for (int i = 0; i < CheckListName.length; i++) {
			objSheet.setColumnWidth(i, 6000);
			objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue(CheckListName[i]);
		}
		
		for (int j = 0; j < returnMap.size(); j++) {
			objRow=objSheet.createRow((short)j+1);
			for (int i = 0; i < CheckListName.length; i++) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("Chk"+i)==null?"":returnMap.get(j).get("Chk"+i).toString());
			}
		}
	}

	private void MemberInfo(XSSFWorkbook objWorkBook) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.SnsMapper.MemberInfoExcelDownload" , 
				paramterMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] CheckListName = {"아이디","유형","가입일","로그인 횟수"};
		
		int sheetcnt = (int) Math.ceil(returnMap.size() / 30000.0);
		objWorkBook.removeSheetAt(0);
		int cnt = 0;
		for (int i = 0; i < sheetcnt; i++) {
			
			XSSFSheet objSheet = objWorkBook.createSheet("통계"+i);
    		XSSFRow objRow = null;
    		XSSFCell objCell = null;
    		
    		// 테이블 헤더용 스타일
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

    	    // 마지막 합계용 테두리만 지정
    	    CellStyle tagStyle = objWorkBook.createCellStyle();
    	    tagStyle.setBorderTop(BorderStyle.THIN);
    	    tagStyle.setBorderBottom(BorderStyle.THIN);
    	    tagStyle.setBorderLeft(BorderStyle.THIN);
    	    tagStyle.setBorderRight(BorderStyle.THIN);
    	    tagStyle.setFillForegroundColor(HSSFColorPredefined.GREY_40_PERCENT.getIndex());
		
		
    	    objRow=objSheet.createRow((short)0);
			for (int h = 0; h < CheckListName.length; h++) {
				objSheet.setColumnWidth(h, 6000);
				objCell = objRow.createCell(h);	objCell.setCellStyle(headStyle); objCell.setCellValue(CheckListName[h]);
			}
		
			int allcnt = 0;
			for (int j = cnt; j < returnMap.size(); j++) {
				if(allcnt < 30000) {
					objRow=objSheet.createRow((short)allcnt+1);
					allcnt++;
					for (int h = 0; h < CheckListName.length; h++) {
							objRow.createCell(h).setCellValue(returnMap.get(j).get("Chk"+h)==null?"":returnMap.get(j).get("Chk"+h).toString());
					}
				} else
					break;
			}
			
			cnt+= allcnt;
		}
		
		
		
		
	}

	private void PartnersMemberActivity(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.PartnersMapper.PartnersMemberActivity" , 
				paramterMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] CheckListName = {"구분","아이디","기관명","담당자명","콘텐츠 사용신청",
				"관광정보 지킴이(신규)","관광정보 지킴이(수정)","홍보 신청","제휴제안","SNS 고유키"};
		objRow=objSheet.createRow((short)0);
		for (int i = 0; i < CheckListName.length; i++) {
			objSheet.setColumnWidth(i, 6000);
			objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue(CheckListName[i]);
		}
		
		for (int j = 0; j < returnMap.size(); j++) {
			objRow=objSheet.createRow((short)j+1);
			for (int i = 0; i < CheckListName.length; i++) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("Chk"+i)==null?"":returnMap.get(j).get("Chk"+i).toString());
			}
		}
	}

	private void PartnersChannel(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.PartnersMapper.PartnersChanelExcelDownload" , 
				paramterMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] CheckListName = {"파트너스 ID","기관 명","분류","신청제목","홍보신청 고유 키",
				"등록일시","처리상태","처리일시","댓글확인","SNS 고유키"};
		objRow=objSheet.createRow((short)0);
		for (int i = 0; i < CheckListName.length; i++) {
			objSheet.setColumnWidth(i, 6000);
			objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue(CheckListName[i]);
		}
		
		for (int j = 0; j < returnMap.size(); j++) {
			objRow=objSheet.createRow((short)j+1);
			for (int i = 0; i < CheckListName.length; i++) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("Chk"+i)==null?"":returnMap.get(j).get("Chk"+i).toString());
			}
		}
		
	}

	private void PartnersMember(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.PartnersMapper.PartnersMember" , 
				paramterMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		objRow=objSheet.createRow((short)0);
		
		String[] CheckListName = {"구분","기관 명","담당자 명","파트너스 ID","등록일시","SNS 고유키","사용여부"};
		
		for (int i = 0; i < CheckListName.length; i++) {
				objSheet.setColumnWidth(i, 6000);
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue(CheckListName[i]);
		}
		
		for (int j = 0; j < returnMap.size(); j++) {
			objRow=objSheet.createRow((short)j+1);
			objRow.createCell(0).setCellValue(returnMap.get(j).get("DIVISION")==null?"":returnMap.get(j).get("DIVISION").toString());
			objRow.createCell(1).setCellValue(returnMap.get(j).get("SNS_USR_NAME")==null?"":returnMap.get(j).get("SNS_USR_NAME").toString());
			objRow.createCell(2).setCellValue(returnMap.get(j).get("MAG_NAME")==null?"":returnMap.get(j).get("MAG_NAME").toString());
			objRow.createCell(3).setCellValue(returnMap.get(j).get("SNS_IDENTIFY")==null?"":returnMap.get(j).get("SNS_IDENTIFY").toString());
			objRow.createCell(4).setCellValue(returnMap.get(j).get("CREATE_DATE")==null?"":returnMap.get(j).get("CREATE_DATE").toString());
			objRow.createCell(5).setCellValue(returnMap.get(j).get("SNS_ID")==null?"":returnMap.get(j).get("SNS_ID").toString());
			objRow.createCell(6).setCellValue(returnMap.get(j).get("IS_USE")==null?"":returnMap.get(j).get("IS_USE").toString());
		}
	}

	private void PartnersContent(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
    	paramterMap.put("mode", parameterObject.getInt("mode"));
		if(parameterObject.has("dateType"))
			paramterMap.put("dateType", parameterObject.getInt("dateType"));
		if(parameterObject.has("startInput"))
			paramterMap.put("startInput", parameterObject.getString("startInput"));
		if(parameterObject.has("endInput"))
			paramterMap.put("endInput", parameterObject.getString("endInput"));
		if(parameterObject.has("CategoryCode"))
			paramterMap.put("CategoryCode", parameterObject.getInt("CategoryCode"));
		if(parameterObject.has("keyword"))
			paramterMap.put("keyword", parameterObject.getString("keyword"));
		if(parameterObject.has("StatusSearch"))
			paramterMap.put("StatusSearch", parameterObject.getInt("StatusSearch"));
		if(parameterObject.has("Is_Use"))
			paramterMap.put("Is_Use", parameterObject.getInt("Is_Use"));
		if(parameterObject.has("Apply_type"))
			paramterMap.put("Apply_type", parameterObject.getInt("Apply_type"));
		
		int CheckListsize = parameterObject.getInt("CheckList");
		int CheckList[] = new int[CheckListsize];
		
		
		for (int i = 0; i < CheckListsize; i++) {
			CheckList[i] = parameterObject.getInt("Chk"+i);
			paramterMap.put("Chk"+i,CheckList[i]);
		}
		
		String[] CheckListName = {"SNS 고유 키","파트너스 계정","기관 명", "담당자 명", "COTID", "신청 컨텐츠",
				"컨텐츠 카테고리", "사용처", "반려 사유", "사용 용도", "기타 문의사항", "미제공 사유 또는 다운로드 URL", "다운로드 제공여부",
				"사용내역 등록 URL", "사용내역 등록 상세내용 또는 미사용 사유", "사용내역 등록 파일명", "사용내역 등록 파일ID", "사용정보 사용여부", "처리 상태", "신청일", "처리완료일"};
		
		String[] UseListName = new String[CheckListsize];
		
		int Chkchk =0;
		for (int i = 0; i < CheckListName.length; i++) {
			if(CheckList[i] == 1) {
				UseListName[Chkchk] = CheckListName[i];
				Chkchk ++;
			}
		}
		
		
		
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.PartnersMapper.PartnersContentExcel" , 
				paramterMap );
			   
			   System.out.println(returnMap.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Chkchk = 0;
		objRow=objSheet.createRow((short)0);
		for (int i = 0; i < CheckListName.length; i++) {
			if(CheckList[i] == 1) {
				objSheet.setColumnWidth(Chkchk, 6000);
				objCell = objRow.createCell(Chkchk);	objCell.setCellStyle(headStyle); objCell.setCellValue(UseListName[Chkchk]);
				CheckList[i] = -1;
				Chkchk ++;
			}
		}
		Chkchk = 0;
			for (int j = 0; j < returnMap.size(); j++) {
				objRow=objSheet.createRow((short)j+1);
				Chkchk = 0;
				for (int i = 0; i < CheckListName.length; i++) {
					if(CheckList[i] == j+3 || CheckList[i]== -1) {
						objRow.createCell(Chkchk).setCellValue(returnMap.get(j).get("Chk"+i)==null?"":returnMap.get(j).get("Chk"+i).toString());
						CheckList[i] = j+4;
						Chkchk ++;
					}
				}
			}
	}

	private void UserImageList(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
    	if(parameterObject.has("startInput"))
			paramterMap.put("startInput", parameterObject.getString("startInput"));
		if(parameterObject.has("endInput"))
			paramterMap.put("endInput", parameterObject.getString("endInput"));
		
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.ImageMapper.SelectUserImageExcel" , 
				paramterMap );
			   
			   System.out.println(returnMap.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		objRow=objSheet.createRow((short)0);
		String[] Title = {"번호","사진 파일명","노출 여부","여행지 명","여행지 COT_ID","등록자","등록일"};
		
		for (int j = 0; j < Title.length; j++) {
			if(j == 0) objSheet.setColumnWidth(0, 3000);
			else objSheet.setColumnWidth(j, 10000);
			objCell = objRow.createCell(j);	objCell.setCellStyle(headStyle); objCell.setCellValue(Title[j]);
		}
		
		String[] Column = {"filename","visible","title","COT_ID","Identify","CREATE_DATE"};
		for (int j = 0; j < returnMap.size(); j++) {
			objRow=objSheet.createRow((short)j+1);
			
			for (int i = 1; i < Title.length; i++) {
				objRow.createCell(0).setCellValue(j);
				objRow.createCell(i).setCellValue(returnMap.get(j).get(Column[i-1])==null?"":returnMap.get(j).get(Column[i-1]).toString());
			}
		}
		
		
	}

	private void DatabaseList(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
    	paramterMap.put("mode", parameterObject.getInt("mode"));
		if(parameterObject.has("dateType"))
			paramterMap.put("dateType", parameterObject.getInt("dateType"));
		if(parameterObject.has("startInput"))
			paramterMap.put("startInput", parameterObject.getString("startInput"));
		if(parameterObject.has("endInput"))
			paramterMap.put("endInput", parameterObject.getString("endInput"));
		if(parameterObject.has("areaCode"))
			paramterMap.put("areaCode", parameterObject.getInt("areaCode"));
		if(parameterObject.has("sigunguname"))
			paramterMap.put("sigunguname", parameterObject.getString("sigunguname"));
		if(parameterObject.has("contentPart"))
			paramterMap.put("contentPart", parameterObject.getInt("contentPart"));
		if(parameterObject.has("keyword"))
			paramterMap.put("keyword", parameterObject.getString("keyword"));
		if(parameterObject.has("OTD_ID"))
			paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		if(parameterObject.has("StatusSearch"))
			paramterMap.put("StatusSearch", parameterObject.getInt("StatusSearch"));
		
		int cId= parameterObject.getInt("cId");
		int cotId= parameterObject.getInt("cotId");
		int title= parameterObject.getInt("title");
		int typename= parameterObject.getInt("typename");
		int contenttype= parameterObject.getInt("contenttype");
		int cat1= parameterObject.getInt("cat1");
		int cat2= parameterObject.getInt("cat2");
		int cat3= parameterObject.getInt("cat3");
		int Sigungu= parameterObject.getInt("Sigungu");
		int Area= parameterObject.getInt("Area");
		int status= parameterObject.getInt("status");
		int create_date= parameterObject.getInt("create_date");
		int update_date= parameterObject.getInt("update_date");
		int stfId= parameterObject.getInt("stfId");
		int otdId= parameterObject.getInt("otdId");
		int catchphrase= parameterObject.getInt("catchphrase");
		int setstatus= parameterObject.getInt("setstatus");
		int fesSdate= parameterObject.getInt("fesSdate");
		int fesEdate= parameterObject.getInt("fesEdate");
		int authcode= parameterObject.getInt("authcode");
		
		int columnsize =cId+cotId+title+typename+contenttype+cat1+cat2+otdId+cat3+Sigungu
				+Area+status+create_date+update_date+stfId+ catchphrase + authcode + 
				setstatus + setstatus + fesSdate+ fesEdate;
		if(otdId == 1)
			columnsize+= 2;
		
		paramterMap.put("cId", cId);
		paramterMap.put("cotId", cotId);
		paramterMap.put("title", title);
		paramterMap.put("typename", typename);
		paramterMap.put("contenttype", contenttype);
		paramterMap.put("cat1", cat1);
		paramterMap.put("cat2", cat2);
		paramterMap.put("cat3", cat3);
		paramterMap.put("Sigungu", Sigungu);
		paramterMap.put("Area", Area);
		paramterMap.put("status", status);
		paramterMap.put("create_date", create_date);
		paramterMap.put("update_date", update_date);
		paramterMap.put("stfId", stfId);
		paramterMap.put("otdId", otdId);
		paramterMap.put("catchphrase", catchphrase);
		paramterMap.put("setstatus", setstatus);
		paramterMap.put("fesSdate", fesSdate);
		paramterMap.put("fesEdate", fesEdate);
		paramterMap.put("authcode", authcode);
		
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.SelectListExcelDownload" , 
				paramterMap );
			   
			   System.out.println(returnMap.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		objRow=objSheet.createRow((short)0);
		for (int i = 0; i < columnsize; i++) {
			objSheet.setColumnWidth(i, 6000);
			if(cId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("CONTENT_ID");
				cId = -1;
			} else if(cotId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("COT_ID");
				cotId = -1;
			} else if(title == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("제목");
				title = -1;
			} else if(typename == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("타입 이름");
				typename = -1;
			} else if(contenttype == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("타입 코드");
				contenttype = -1;
			} else if(cat1 == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("카테고리 1분류");
				cat1 = -1;
			} else if(cat2 == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("카테고리 2분류");
				cat2 = -1;
			} else if(cat3 == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("카테고리 3분류");
				cat3 = -1;
			} else if(Sigungu == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역(시/군/구)");
				Sigungu = -1;
			} else if(Area == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역(시/도)");
				Area = -1;
			} else if(status == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리 상태");
				status = -1;
			} else if(create_date == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("생성일");
				create_date = -1;
			} else if(update_date == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("수정일");
				update_date = -1;
			} else if(stfId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("작성자");
				stfId = -1;
			} else if(otdId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("적용된 타부서1");
				i++;
				objSheet.setColumnWidth(i, 6000);
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("적용된 타부서2");
				i++;
				objSheet.setColumnWidth(i, 6000);
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("적용된 타부서3");
				otdId = -1;
			} else if(catchphrase == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("캐치 프레이즈 여부");
				catchphrase = -1;
			} else if(authcode == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("인증번호");
				authcode = -1;
			} else if(setstatus == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("여행지 - 지정현황");
				setstatus = -1;
			} else if(fesSdate == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("축제 - 시작일");
				fesSdate = -1;
			} else if(fesEdate == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("축제 - 종료일");
				fesEdate = -1;
			}
		}
			for (int j = 0; j < returnMap.size(); j++) {
				objRow=objSheet.createRow((short)j+1);
				for (int i = 0; i < columnsize; i++) {
					if(cId == j+3 || cId== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("CONTENT_ID")==null?"":returnMap.get(j).get("CONTENT_ID").toString());
						cId = j+4;
					} else if(cotId == j+3 || cotId== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("COT_ID")==null?"":returnMap.get(j).get("COT_ID").toString());
						cotId = j+4;
					} else if(title == j+3 || title== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("TITLE")==null?"":returnMap.get(j).get("TITLE").toString());
						title = j+4;
					} else if(typename == j+3 || typename== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("TYPE_NAME")==null?"":returnMap.get(j).get("TYPE_NAME").toString());
						typename = j+4;
					} else if(contenttype == j+3 || contenttype== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("CONTENT_TYPE")==null?"":returnMap.get(j).get("CONTENT_TYPE").toString());
						contenttype = j+4;
					} else if(cat1 == j+3 || cat1== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("CAT1")==null?"":returnMap.get(j).get("CAT1").toString());
						cat1 = j+4;
					} else if(cat2 == j+3 || cat2== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("CAT2")==null?"":returnMap.get(j).get("CAT2").toString());
						cat2 = j+4;
					} else if(cat3 == j+3 || cat3== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("CAT3")==null?"":returnMap.get(j).get("CAT3").toString());
						cat3 = j+4;
					} else if(Sigungu == j+3 || Sigungu== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("SIGUNGU")==null?"":returnMap.get(j).get("SIGUNGU").toString());
						Sigungu = j+4;
					} else if(Area == j+3 || Area== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("AREA")==null?"":returnMap.get(j).get("AREA").toString());
						Area = j+4;
					} else if(status == j+3 || status== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("STATUS")==null?"":returnMap.get(j).get("STATUS").toString());
						status = j+4;
					} else if(create_date == j+3 || create_date== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("CREATE_DATE")==null?"":returnMap.get(j).get("CREATE_DATE").toString());
						create_date = j+4;
					} else if(update_date == j+3 || update_date== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("MODIFIED_DATE")==null?"":returnMap.get(j).get("MODIFIED_DATE").toString());
						update_date = j+4;
					} else if(stfId == j+3 || stfId== -1) {
						objRow.createCell(i).setCellValue(returnMap.get(j).get("STF_ID")==null?"":returnMap.get(j).get("STF_ID").toString());
						stfId = j+4;
					}  else if(otdId == j+3 || otdId== -1) {
						otdId = j+4;
						String OTD_ID = returnMap.get(j).get("OTD_ID") ==null?"":returnMap.get(j).get("OTD_ID").toString();
						String[]OTD_IDs = OTD_ID.split(",");
						objRow.createCell(i).setCellValue(OtherDepartmentService(OTD_IDs[0]));
						i++;
						if(OTD_IDs.length> 1) {
							objRow.createCell(i).setCellValue(OtherDepartmentService(OTD_IDs[1]));
						} else {
							objRow.createCell(i).setCellValue("");
						}
						i++;
						if(OTD_IDs.length> 2) {
							objRow.createCell(i).setCellValue(OtherDepartmentService(OTD_IDs[2]));
						} else {
							objRow.createCell(i).setCellValue("");
						}
						
					}  else if(catchphrase == j+3 || catchphrase== -1) {
						catchphrase = j+4;
						objRow.createCell(i).setCellValue(returnMap.get(j).get("CATCH_PLACE")==null?"":returnMap.get(j).get("CATCH_PLACE").toString());
					}  else if(authcode == j+3 || authcode== -1) {
						authcode = j+4;
						objRow.createCell(i).setCellValue(returnMap.get(j).get("AUTH_CODE")==null?"":returnMap.get(j).get("AUTH_CODE").toString());
					} else if(setstatus == j+3 || setstatus== -1) {
						setstatus = j+4;
						objRow.createCell(i).setCellValue(returnMap.get(j).get("SET_STATUS")==null?"":returnMap.get(j).get("SET_STATUS").toString().replace("null", ""));
					} else if(fesSdate == j+3 || fesSdate== -1) {
						fesSdate = j+4;
						objRow.createCell(i).setCellValue(returnMap.get(j).get("FES_SDATE")==null?"":returnMap.get(j).get("FES_SDATE").toString());
					} else if(fesEdate == j+3 || fesEdate== -1) {
						fesEdate = j+4;
						objRow.createCell(i).setCellValue(returnMap.get(j).get("FES_EDATE")==null?"":returnMap.get(j).get("FES_EDATE").toString());
					}
				}
		}
		
	}
    
    private String OtherDepartmentService(String Code) {
    	
    	if(Code.equals("0a01eb7b-96de-11e8-8165-020027310001"))
			return "국문관광정보";
		else if(Code.equals("114b23a6-84c4-11e8-8165-020027310001"))
			return"산업관광";
		else if(Code.equals("27a4afa8-57a6-11ea-b70a-020027310001"))
			return"휴가문화개선";
		else if(Code.equals("27f7a2ca-84c4-11e8-8165-020027310001"))
			return"생태관광";
		else if(Code.equals("287776d6-8939-11e8-8165-020027310001"))
			return"추천! 웰니스 관광지";
		else if(Code.equals("38239583-02e1-4011-89b5-27902f1461d5"))
			return"투자관광";
		else if(Code.equals("456a84d1-84c4-11e8-8165-020027310001"))
			return"한국관광품질인증";
		else if(Code.equals("46a412aa-0b3b-11ea-869b-020027310001"))
			return"여행주간";
		else if(Code.equals("622bcd99-84fa-11e8-8165-020027310001"))
			return"한국관광100선";
		else if(Code.equals("64e29192-8939-11e8-8165-020027310001"))
			return"한국관광의별";
		else if(Code.equals("7ff670df-84fa-11e8-8165-020027310001"))
			return"추천! 가볼만한곳";
		else if(Code.equals("81f62fd1-8939-11e8-8165-020027310001"))
			return"시티투어";
		else if(Code.equals("90f43a28-fec6-4dd4-bfc4-64b95efe0220"))
			return"한류관광";
		else if(Code.equals("96e3097b-1193-4397-be16-b57d2a0f147f"))
			return"투자관광";
		else if(Code.equals("9d9ebc5e-36bd-423b-abaa-c07496015432"))
			return"전통문화 관광";
		else if(Code.equals("a711ab41-8939-11e8-8165-020027310001"))
			return"대한민국 테마여행 10선";
		else if(Code.equals("ab097fc9-daa6-423d-8fcb-50aec7852e21"))
			return"국문관광정보";
		else if(Code.equals("b55ffe10-84c3-11e8-8165-020027310001"))
			return"'열린 관광' 모두의 여행";
		else if(Code.equals("19b62fbc-e2a2-4964-8848-2de011548c87"))
			return" 테스트 서버";
    	
		return "";
    }

	private void RecommandList(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
    	paramterMap.put("mode", parameterObject.getInt("mode"));
		if(parameterObject.has("dateType"))
			paramterMap.put("dateType", parameterObject.getInt("dateType"));
		if(parameterObject.has("startInput"))
			paramterMap.put("startInput", parameterObject.getString("startInput"));
		if(parameterObject.has("endInput"))
			paramterMap.put("endInput", parameterObject.getString("endInput"));
		if(parameterObject.has("areaCode"))
			paramterMap.put("areaCode", parameterObject.getInt("areaCode"));
		if(parameterObject.has("sigunguname"))
			paramterMap.put("sigunguname", parameterObject.getInt("sigunguname"));
		if(parameterObject.has("CategoryCode"))
			paramterMap.put("CategoryCode", parameterObject.getString("CategoryCode"));
		if(parameterObject.has("CategoryCode_two"))
			paramterMap.put("CategoryCode_two", parameterObject.getString("CategoryCode_two"));
		if(parameterObject.has("keyword"))
			paramterMap.put("keyword", parameterObject.getString("keyword"));
		if(parameterObject.has("OTD_ID"))
			paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		if(parameterObject.has("StatusSearch"))
			paramterMap.put("StatusSearch", parameterObject.getInt("StatusSearch"));
		if(parameterObject.has("Division"))
			paramterMap.put("Division", parameterObject.getInt("Division"));
		
		int cId= parameterObject.getInt("cId");
		int cotId= parameterObject.getInt("cotId");
		int title= parameterObject.getInt("title");
		int create_date= parameterObject.getInt("create_date");
		int status= parameterObject.getInt("status");
		int finalupdate_date= parameterObject.getInt("finalupdate_date");
		int stfId= parameterObject.getInt("stfId");
		int otdId= parameterObject.getInt("otdId");
		int Kogl= parameterObject.getInt("Kogl");
		int update_date= parameterObject.getInt("update_date");
		int Sigungu= parameterObject.getInt("Sigungu");
		int Area= parameterObject.getInt("Area");
		int secondcategory= parameterObject.getInt("secondcategory");
		int firstcategory= parameterObject.getInt("firstcategory");
		int categorytype= parameterObject.getInt("categorytype");
		int contentdiv= parameterObject.getInt("contentdiv");
		int columnsize =cId+cotId+title+create_date+status+finalupdate_date+stfId+otdId+Kogl+update_date+Sigungu+Area+secondcategory+firstcategory+categorytype+contentdiv;		
		if(otdId == 1)
			columnsize+= 2;
		
		paramterMap.put("cId", cId);
		paramterMap.put("cotId", cotId);
		paramterMap.put("title", title);
		paramterMap.put("create_date", create_date);
		paramterMap.put("status", status);
		paramterMap.put("finalupdate_date", finalupdate_date);
		paramterMap.put("stfId", stfId);
		paramterMap.put("otdId", otdId);
		paramterMap.put("Kogl", Kogl);
		paramterMap.put("update_date", update_date);
		paramterMap.put("Sigungu", Sigungu);
		paramterMap.put("Area", Area);
		paramterMap.put("secondcategory", secondcategory);
		paramterMap.put("firstcategory", firstcategory);
		paramterMap.put("categorytype", categorytype);
		paramterMap.put("contentdiv", contentdiv);
		
		List<HashMap<String, Object>> returnMap =null;
		try {
			   returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.ArticleMapper.ArticleExcelDownload" , 
				paramterMap );
			   
			   System.out.println(returnMap.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		objRow=objSheet.createRow((short)0);
		for (int i = 0; i < columnsize; i++) {
			objSheet.setColumnWidth(i, 6000);
			if(cId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("CONTENT_ID");
				cId = -1;
			} else if(cotId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("COT_ID");
				cotId = -1;
			} else if(title == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("제목");
				title = -1;
			} else if(categorytype == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("타입 코드");
				categorytype = -1;
			} else if(firstcategory == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류 1순위");
				firstcategory = -1;
			} else if(secondcategory == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류 2순위");
				secondcategory = -1;
			} else if(contentdiv == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("구분");
				contentdiv = -1;
			} else if(Area == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역(시/도)");
				Area = -1;
			} else if(Sigungu == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역(시/군/구)");
				Sigungu = -1;
			} else if(status == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리 상태");
				status = -1;
			} else if(create_date == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("생성일");
				create_date = -1;
			} else if(update_date == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("수정일");
				update_date = -1;
			} else if(finalupdate_date == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("최종 수정일");
				finalupdate_date = -1;
			} else if(stfId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("작성자");
				stfId = -1;
			} else if(otdId == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("적용된 타부서1");
				i++;
				objSheet.setColumnWidth(i, 6000);
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("적용된 타부서2");
				i++;
				objSheet.setColumnWidth(i, 6000);
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("적용된 타부서3");
				otdId = -1;
			}  else if(Kogl == 1) {
				objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue("공공누리 적용여부");
				Kogl = -1;
			}
		}
		
		for (int j = 0; j < returnMap.size(); j++) {
			objRow=objSheet.createRow((short)j+1);
			for (int i = 0; i < columnsize; i++) {
				if(cId == j+3 || cId== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("CONTENT_ID")==null?"":returnMap.get(j).get("CONTENT_ID").toString());
					cId = j+4;
				} else if(cotId == j+3 || cotId== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("COT_ID")==null?"":returnMap.get(j).get("COT_ID").toString());
					cotId = j+4;
				} else if(title == j+3 || title== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("TITLE")==null?"":returnMap.get(j).get("TITLE").toString());
					title = j+4;
				} else if(categorytype == j+3 || categorytype== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("CONTENT_TYPE")==null?"":returnMap.get(j).get("CONTENT_TYPE").toString());
					categorytype = j+4;
				} else if(firstcategory == j+3 || firstcategory== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("CONTENT_CATEGORY")==null?"":returnMap.get(j).get("CONTENT_CATEGORY").toString());
					firstcategory = j+4;
				} else if(secondcategory == j+3 || secondcategory== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("CONTENT_CATEGORY_TWO")==null?"":returnMap.get(j).get("CONTENT_CATEGORY_TWO").toString());
					secondcategory = j+4;
				} else if(contentdiv == j+3 || contentdiv== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("CONTENT_DIV")==null?"":returnMap.get(j).get("CONTENT_DIV").toString());
					contentdiv = j+4;
				} else if(Area == j+3 || Area== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("AREA")==null?"":returnMap.get(j).get("AREA").toString());
					Area = j+4;
				} else if(Sigungu == j+3 || Sigungu== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("SIGUNGU")==null?"":returnMap.get(j).get("SIGUNGU").toString());
					Sigungu = j+4;
				} else if(status == j+3 || status== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("STATUS")==null?"":returnMap.get(j).get("STATUS").toString());
					status = j+4;
				} else if(create_date == j+3 || create_date== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("CREATE_DATE")==null?"":returnMap.get(j).get("CREATE_DATE").toString());
					create_date = j+4;
				} else if(update_date == j+3 || update_date== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("MODIFIED_DATE")==null?"":returnMap.get(j).get("MODIFIED_DATE").toString());
					update_date = j+4;
				} else if(finalupdate_date == j+3 || finalupdate_date== -1) {
					objRow.createCell(i).setCellValue(returnMap.get(j).get("FINAL_MODIFIED_DATE")==null?"":returnMap.get(j).get("FINAL_MODIFIED_DATE").toString());
					finalupdate_date = j+4;
				} else if(stfId == j+3 || stfId== -1) {
					String usrId = returnMap.get(j).get("STF_ID")==null? "" : returnMap.get(j).get("STF_ID").toString();
					objRow.createCell(i).setCellValue(usrId);
					stfId = j+4;
				} else if(otdId == j+3 || otdId== -1) {
					otdId = j+4;
					String OTD_ID = returnMap.get(j).get("OTD_ID") ==null?"":returnMap.get(j).get("OTD_ID").toString();
					String[]OTD_IDs = OTD_ID.split(",");
					objRow.createCell(i).setCellValue(OtherDepartmentService(OTD_IDs[0]));
					i++;
					if(OTD_IDs.length> 1) {
						objRow.createCell(i).setCellValue(OtherDepartmentService(OTD_IDs[1]));
					} else {
						objRow.createCell(i).setCellValue("");
					}
					i++;
					if(OTD_IDs.length> 2) {
						objRow.createCell(i).setCellValue(OtherDepartmentService(OTD_IDs[2]));
					} else {
						objRow.createCell(i).setCellValue("");
					}
					
				}  else if(Kogl == j+3 || Kogl== -1) {
					Kogl = j+4;
					objRow.createCell(i).setCellValue(returnMap.get(j).get("IS_KOGL")==null?"":returnMap.get(j).get("IS_KOGL").toString());
				}
			}
		}
		
	}

	private void repair(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle, CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("rtype"))
			paramterMap.put("rtype", parameterObject.getInt("rtype"));
		if(parameterObject.has("status"))
			paramterMap.put("status", parameterObject.getInt("status"));
		if(parameterObject.has("remid"))
			paramterMap.put("remid", parameterObject.getString("remid"));
		
		if(parameterObject.has("datetype")) {
			if(parameterObject.has("datetype"))
				paramterMap.put("datetype", parameterObject.getString("datetype"));
			if(parameterObject.has("sdate"))
				paramterMap.put("sdate", parameterObject.getString("sdate"));
			if(parameterObject.has("edate"))
				paramterMap.put("edate", parameterObject.getString("edate"));
		}
		if(parameterObject.has("searchtype"))
			paramterMap.put("searchtype", parameterObject.getInt("searchtype"));
		if(parameterObject.has("word"))
			paramterMap.put("word", parameterObject.getString("word"));
		
		if(parameterObject.has("offset"))
			paramterMap.put("offset", parameterObject.getInt("offset"));
			
//		System.out.println("klsdjflksdjflkjdslkfjsdlfjldskjflkdsjfkdsfkljsdlkfjdslf");
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.RepairMapper.selectRepairList" , 
			paramterMap );
		objSheet.setColumnWidth(1, 6000);
		objSheet.setColumnWidth(2, 3000);
		objSheet.setColumnWidth(3, 3000);
		objSheet.setColumnWidth(4, 3000);
		objSheet.setColumnWidth(5, 3000);
		objSheet.setColumnWidth(6, 3000);
		objSheet.setColumnWidth(7, 3000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("제목");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("요청분류");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("등록자");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("진행상태");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("담당자");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("요청일");
		objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리일");
//		System.out.println("klsdjflksdjflkjdslkfjsdlfjldskjflkdsjfkdsfkljsdlkfjdslf");
	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
			String sstatus = "접수";
			switch(returnMap.get(i).get("status")!=null?Integer.parseInt(returnMap.get(i).get("status").toString()):0) {
			case 0: sstatus = "접수"; break;
			case 1: sstatus = "처리중"; break;
			case 2: sstatus = "처리완료"; break;
			case 3: sstatus = "처리불가"; break;
			}
			String rtype = "신규요청";
			switch (returnMap.get(i).get("rtype")!=null?Integer.parseInt(returnMap.get(i).get("rtype").toString()):0) {
			case 1 : rtype= "기능오류"; break;
			case 2 : rtype= "수정요청"; break;
			}
			objRow.createCell(0).setCellValue(i+1);
	        objRow.createCell(1).setCellValue(returnMap.get(i).get("title")==null?"":returnMap.get(i).get("title").toString());
	        objRow.createCell(2).setCellValue(rtype);
	        objRow.createCell(3).setCellValue(returnMap.get(i).get("stfid")==null?"":returnMap.get(i).get("stfid").toString());
	        objRow.createCell(4).setCellValue(sstatus);
	        objRow.createCell(5).setCellValue(returnMap.get(i).get("resname")==null?"":returnMap.get(i).get("resname").toString());
	        objRow.createCell(6).setCellValue(returnMap.get(i).get("reqdate")==null?"":returnMap.get(i).get("reqdate").toString());
	        objRow.createCell(7).setCellValue(returnMap.get(i).get("resdate")==null?"":returnMap.get(i).get("resdate").toString());
	    }		
	}

	private void zikimi_manager(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
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
		if(parameterObject.has("utype"))
			paramterMap.put("utype", parameterObject.getInt("utype"));
		if(parameterObject.has("contenttype"))
			paramterMap.put("contenttype", parameterObject.getInt("contenttype"));
		if(parameterObject.has("status"))
			paramterMap.put("status", parameterObject.getInt("status"));
		System.out.println("zikimi : paramterMap ::" + paramterMap);
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.ZikimiMapper.selectZikimiListAll", paramterMap );
		objSheet.setColumnWidth(1, 6000);
		objSheet.setColumnWidth(2, 6000);
		objSheet.setColumnWidth(3, 8000);
		objSheet.setColumnWidth(4, 6000);
		objSheet.setColumnWidth(5, 5000);
		objSheet.setColumnWidth(6, 5000);
		objSheet.setColumnWidth(7, 4000);
		objSheet.setColumnWidth(8, 4000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("사용자구분");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("카테고리");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("콘텐츠명");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("작성자");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("등록일시");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리일시");
		objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리상태");
		objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("댓글확인");
		
	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	    	
	        String ctype = UI.getContentType(returnMap.get(i).get("contenttype")!=null?Integer.parseInt(returnMap.get(i).get("contenttype").toString()):0);
			String sstatus = "접수";
			switch(returnMap.get(i).get("status")!=null?Integer.parseInt(returnMap.get(i).get("status").toString()):0) {
			case 0: sstatus = "접수"; break;
			case 1: sstatus = "처리중"; break;
			case 2: sstatus = "승인완료"; break;
			case 3: sstatus = "승인거절"; break;
			}
			String utype = "일반사용자";
			switch(returnMap.get(i).get("utype")!=null?Integer.parseInt(returnMap.get(i).get("utype").toString()):0) {
			case 10: utype = "Partners"; break;
			}
//	        objRow.createCell(3).setCellValue(returnMap.get(i).get("pageview")==null?0:Integer.parseInt(returnMap.get(i).get("pageview").toString()));
			objRow.createCell(0).setCellValue(i+1);
	        objRow.createCell(1).setCellValue(utype);
	        objRow.createCell(2).setCellValue(ctype);
	        objRow.createCell(3).setCellValue(returnMap.get(i).get("title")==null?"":returnMap.get(i).get("title").toString());
	        objRow.createCell(4).setCellValue(returnMap.get(i).get("snsid")==null?"":returnMap.get(i).get("snsid").toString());
	        objRow.createCell(5).setCellValue(returnMap.get(i).get("cdate")==null?"":returnMap.get(i).get("cdate").toString());
	        objRow.createCell(6).setCellValue(returnMap.get(i).get("udate")==null?"":returnMap.get(i).get("udate").toString());
	        objRow.createCell(7).setCellValue(sstatus);
	        objRow.createCell(8).setCellValue(returnMap.get(i).get("res")==null?"":returnMap.get(i).get("res").toString());
	    }		
	}

	private void analysis_other_tag(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("otdid"))
			paramterMap.put("otdid", parameterObject.getString("otdid"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.AnalysisMapper.selectOtherTag" , paramterMap );
		
		objSheet.setColumnWidth(1, 7000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("순위");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("태그명");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("클릭수");

	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	        objRow.createCell(0).setCellValue(String.valueOf(i+1));
	        objRow.createCell(1).setCellValue(returnMap.get(i).get("name").toString());
	        objRow.createCell(2).setCellValue(Integer.parseInt(returnMap.get(i).get("cnt").toString()));
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectOtherTagTot" , paramterMap );
	    objRow=objSheet.createRow(returnMap.size()+1);
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue(Integer.parseInt(returnMapTot.get(0).get("tot").toString()));		
	}

	private void analysis_other_contents(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("mode"))
			paramterMap.put("mode", parameterObject.getInt("mode"));
		
		if(parameterObject.has("dbname1"))
			paramterMap.put("dbname1", parameterObject.getString("dbname1"));
		if(parameterObject.has("dbname2"))
			paramterMap.put("dbname2", parameterObject.getString("dbname2"));
		
		if(parameterObject.has("otdid"))
			paramterMap.put("otdid", parameterObject.getString("otdid"));
		if(parameterObject.has("contenttype"))
			paramterMap.put("contenttype", parameterObject.getString("contenttype"));
		if(parameterObject.has("areacode"))
			paramterMap.put("areacode", parameterObject.getInt("areacode"));
		if(parameterObject.has("sigungucode"))
			paramterMap.put("sigungucode", parameterObject.getInt("sigungucode"));
		if(parameterObject.has("title"))
			paramterMap.put("title", parameterObject.getString("title"));
		
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getString("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getString("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.AnalysisMapper.selectOtherContents", paramterMap );
		
		objSheet.setColumnWidth(1, 15000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("콘텐츠명");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("페이지뷰");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("코스등록");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("즐겨찾기");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("공유하기");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("종아요");
		objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("관광정보지킴이");
		objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("댓글수");
		objCell = objRow.createCell(9);	objCell.setCellStyle(headStyle); objCell.setCellValue("인쇄수");

	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	    	objRow.createCell(0).setCellValue(String.valueOf(i+1));
	        objRow.createCell(1).setCellValue(returnMap.get(i).get("title")==null?"":returnMap.get(i).get("title").toString());
	        objRow.createCell(2).setCellValue(returnMap.get(i).get("pageview")==null?0:Integer.parseInt(returnMap.get(i).get("pageview").toString()));
	        objRow.createCell(3).setCellValue(returnMap.get(i).get("course")==null?0:Integer.parseInt(returnMap.get(i).get("course").toString()));
	        objRow.createCell(4).setCellValue(returnMap.get(i).get("fav")==null?0:Integer.parseInt(returnMap.get(i).get("fav").toString()));
	        objRow.createCell(5).setCellValue(returnMap.get(i).get("share")==null?0:Integer.parseInt(returnMap.get(i).get("share").toString()));
	        objRow.createCell(6).setCellValue(returnMap.get(i).get("likes")==null?0:Integer.parseInt(returnMap.get(i).get("likes").toString()));
	        objRow.createCell(7).setCellValue(returnMap.get(i).get("zikimi")==null?0:Integer.parseInt(returnMap.get(i).get("zikimi").toString()));
	        objRow.createCell(8).setCellValue(returnMap.get(i).get("comment")==null?0:Integer.parseInt(returnMap.get(i).get("comment").toString()));
	        objRow.createCell(9).setCellValue(returnMap.get(i).get("print")==null?0:Integer.parseInt(returnMap.get(i).get("print").toString()));
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectOtherContentsTot" , paramterMap );
	    int pageview=0, course=0, fav=0, share=0, likes=0, zikimi=0, comment=0, print=0;
		for(int i= 0;i< returnMapTot.size();i++) {
			if(returnMapTot.get(i) == null) continue;
			pageview += returnMapTot.get(i).get("pageview")!=null?Integer.parseInt(returnMapTot.get(i).get("pageview").toString()):0;
			course += returnMapTot.get(i).get("course")!=null?Integer.parseInt(returnMapTot.get(i).get("course").toString()):0;
			fav += returnMapTot.get(i).get("fav")!=null?Integer.parseInt(returnMapTot.get(i).get("fav").toString()):0;
			share += returnMapTot.get(i).get("share")!=null?Integer.parseInt(returnMapTot.get(i).get("share").toString()):0;
			likes += returnMapTot.get(i).get("likes")!=null?Integer.parseInt(returnMapTot.get(i).get("likes").toString()):0;
			zikimi += returnMapTot.get(i).get("zikimi")!=null?Integer.parseInt(returnMapTot.get(i).get("zikimi").toString()):0;
			comment += returnMapTot.get(i).get("comment")!=null?Integer.parseInt(returnMapTot.get(i).get("comment").toString()):0;
			print += returnMapTot.get(i).get("print")!=null?Integer.parseInt(returnMapTot.get(i).get("print").toString()):0;
		}
	    objRow=objSheet.createRow(returnMap.size()+1);
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue(pageview);
	    objCell = objRow.createCell(3); objCell.setCellStyle(tagStyle); objCell.setCellValue(course);
	    objCell = objRow.createCell(4); objCell.setCellStyle(tagStyle); objCell.setCellValue(fav);
	    objCell = objRow.createCell(5); objCell.setCellStyle(tagStyle); objCell.setCellValue(share);
	    objCell = objRow.createCell(6); objCell.setCellStyle(tagStyle); objCell.setCellValue(likes);
	    objCell = objRow.createCell(7); objCell.setCellStyle(tagStyle); objCell.setCellValue(zikimi);
	    objCell = objRow.createCell(8); objCell.setCellStyle(tagStyle); objCell.setCellValue(comment);
	    objCell = objRow.createCell(9); objCell.setCellStyle(tagStyle); objCell.setCellValue(print);		
	}

	private void analysis_other_main(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("otdid"))
			paramterMap.put("otdid", parameterObject.getString("otdid"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.AnalysisMapper.selectOtherMain" , paramterMap );
		List<HashMap<String, Object>> returnTot = sqlSession.selectList( 
				"kr.or.visitkorea.system.AnalysisMapper.selectOtherMainTot" , 
				paramterMap );
		objSheet.setColumnWidth(0, 5000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("메뉴명");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("클릭수");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("비율");

		double nTot = returnTot.get(0).get("tot")!=null?Integer.parseInt(returnTot.get(0).get("tot").toString()):0; //.toString()
	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	    	
	    	int cnt = (int)(returnMap.get(i).get("cnt")!=null?Integer.parseInt(returnMap.get(i).get("cnt").toString()):0);
						
	        objRow.createCell(0).setCellValue(returnMap.get(i).get("name")==null?"":returnMap.get(i).get("name").toString());
	        objRow.createCell(1).setCellValue(cnt+"");
	        objRow.createCell(2).setCellValue(Math.round((cnt/nTot*100)*100)/100.0+"");
	    }		
	}

	private void analysis_connect(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("stype"))
			paramterMap.put("stype", parameterObject.getInt("stype"));
		if(parameterObject.has("areaname"))
			paramterMap.put("areaname", parameterObject.getString("areaname"));
		if(parameterObject.has("sigunguname"))
			paramterMap.put("sigunguname", parameterObject.getString("sigunguname"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectConnect" , paramterMap );
		
		objSheet.setColumnWidth(0, 3000);
		objSheet.setColumnWidth(1, 5000);
		objSheet.setColumnWidth(2, 5000);
//		objSheet.setColumnWidth(3, 2000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("매체");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("시도");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("시군구");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("접속횟수");
					
	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	    	String sstype = "미로그인";
			//SNS 타입 : 000 :: FACEBOOK,		001 :: TWITTER,		002 :: INSTAGRAM,		003 :: DAUM,		004 :: NAVER,		005 :: KAKAOTALK,		006 :: GOOGLE
			int nstype = (int)(returnMap.get(i).get("stype")!=null?Integer.parseInt(returnMap.get(i).get("stype").toString()):-1);
			if(nstype == 0) sstype="FACEBOOK";
			else if(nstype == 1) sstype="TWITTER";
			else if(nstype == 2) sstype= "INSTAGRAM";
			else if(nstype == 3) sstype = "DAUM";
			else if(nstype == 4) sstype = "NAVER";
			else if(nstype == 5) sstype = "KAKAOTALK";
			else if(nstype == 6) sstype = "GOOGLE";
			else if(nstype == 10) sstype = "Partners";
	    	objRow.createCell(0).setCellValue(sstype);
	        objRow.createCell(1).setCellValue(returnMap.get(i).get("areaname")==null?"":returnMap.get(i).get("areaname").toString());
	        objRow.createCell(2).setCellValue(returnMap.get(i).get("sigunguname")==null?"":returnMap.get(i).get("sigunguname").toString());
	        objRow.createCell(3).setCellValue(returnMap.get(i).get("cnt").toString());
	    }		
	}

	private void analysis_banner(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		if(parameterObject.has("section"))
			paramterMap.put("section", parameterObject.getString("section"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectBanner" , paramterMap );
		
		objSheet.setColumnWidth(0, 8000);
		objSheet.setColumnWidth(2, 7000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("제목");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("클릭수");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("링크정보");

	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	        objRow.createCell(0).setCellValue(returnMap.get(i).get("title")==null?"":returnMap.get(i).get("title").toString());
	        objRow.createCell(1).setCellValue(Integer.parseInt(returnMap.get(i).get("cnt").toString()));
	        objRow.createCell(2).setCellValue(returnMap.get(i).get("link")==null?"":returnMap.get(i).get("link").toString());
	    } 
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectBannerTot" , paramterMap );
	    objRow=objSheet.createRow(returnMap.size()+1);
	    objCell = objRow.createCell(0); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue(Integer.parseInt(returnMapTot.get(0).get("tot").toString()));		
	}

	private void analysis_contents(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("maintype", parameterObject.getString("maintype"));
		if(parameterObject.has("contenttype"))
			paramterMap.put("contenttype", parameterObject.getString("contenttype"));
		if(parameterObject.has("areacode"))
			paramterMap.put("areacode", parameterObject.getInt("areacode"));
		if(parameterObject.has("sigungucode"))
			paramterMap.put("sigungucode", parameterObject.getInt("sigungucode"));
		if(parameterObject.has("title"))
			paramterMap.put("title", parameterObject.getString("title"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( "kr.or.visitkorea.system.AnalysisMapper.selectContents" , paramterMap );
		
		objSheet.setColumnWidth(1, 15000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("컨텐츠명");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("페이지뷰");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("즐겨찾기");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("공유하기");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("좋아요");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("관광정보지킴이");
		objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("댓글수");
		objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("인쇄수");
		objCell = objRow.createCell(9);	objCell.setCellStyle(headStyle); objCell.setCellValue("발도장");

	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	    	objRow.createCell(0).setCellValue(String.valueOf(i+1));
	        objRow.createCell(1).setCellValue(returnMap.get(i).get("title")==null?"":returnMap.get(i).get("title").toString());
	        objRow.createCell(2).setCellValue(returnMap.get(i).get("pageview")==null?0:Integer.parseInt(returnMap.get(i).get("pageview").toString()));
	        objRow.createCell(3).setCellValue(returnMap.get(i).get("fav")==null?0:Integer.parseInt(returnMap.get(i).get("fav").toString()));
	        objRow.createCell(4).setCellValue(returnMap.get(i).get("share")==null?0:Integer.parseInt(returnMap.get(i).get("share").toString()));
	        objRow.createCell(5).setCellValue(returnMap.get(i).get("likes")==null?0:Integer.parseInt(returnMap.get(i).get("likes").toString()));
	        objRow.createCell(6).setCellValue(returnMap.get(i).get("zikimi")==null?0:Integer.parseInt(returnMap.get(i).get("zikimi").toString()));
	        objRow.createCell(7).setCellValue(returnMap.get(i).get("comment")==null?0:Integer.parseInt(returnMap.get(i).get("comment").toString()));
	        objRow.createCell(8).setCellValue(returnMap.get(i).get("print")==null?0:Integer.parseInt(returnMap.get(i).get("print").toString()));
	        objRow.createCell(9).setCellValue(returnMap.get(i).get("stamp")==null?0:Integer.parseInt(returnMap.get(i).get("stamp").toString()));
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectContentsTot" , paramterMap );
	    int pageview=0, fav=0, share=0, likes=0, zikimi=0, comment=0, print=0, stamp=0;
		for(int i= 0;i< returnMapTot.size();i++) {
			if(returnMapTot.get(i) == null) continue;
			pageview += returnMapTot.get(i).get("pageview")!=null?Integer.parseInt(returnMapTot.get(i).get("pageview").toString()):0;
			fav += returnMapTot.get(i).get("fav")!=null?Integer.parseInt(returnMapTot.get(i).get("fav").toString()):0;
			share += returnMapTot.get(i).get("share")!=null?Integer.parseInt(returnMapTot.get(i).get("share").toString()):0;
			likes += returnMapTot.get(i).get("likes")!=null?Integer.parseInt(returnMapTot.get(i).get("likes").toString()):0;
			zikimi += returnMapTot.get(i).get("zikimi")!=null?Integer.parseInt(returnMapTot.get(i).get("zikimi").toString()):0;
			comment += returnMapTot.get(i).get("comment")!=null?Integer.parseInt(returnMapTot.get(i).get("comment").toString()):0;
			print += returnMapTot.get(i).get("print")!=null?Integer.parseInt(returnMapTot.get(i).get("print").toString()):0;
			stamp += returnMapTot.get(i).get("stamp")!=null?Integer.parseInt(returnMapTot.get(i).get("stamp").toString()):0;
		}
	    objRow=objSheet.createRow(returnMap.size()+1);
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue(pageview);
	    objCell = objRow.createCell(3); objCell.setCellStyle(tagStyle); objCell.setCellValue(fav);
	    objCell = objRow.createCell(4); objCell.setCellStyle(tagStyle); objCell.setCellValue(share);
	    objCell = objRow.createCell(5); objCell.setCellStyle(tagStyle); objCell.setCellValue(likes);
	    objCell = objRow.createCell(6); objCell.setCellStyle(tagStyle); objCell.setCellValue(zikimi);
	    objCell = objRow.createCell(7); objCell.setCellStyle(tagStyle); objCell.setCellValue(comment);
	    objCell = objRow.createCell(8); objCell.setCellStyle(tagStyle); objCell.setCellValue(print);
	    objCell = objRow.createCell(9); objCell.setCellStyle(tagStyle); objCell.setCellValue(stamp);		
	}

	private void analysis_sec_sigungu(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		paramterMap.put("areacode", parameterObject.getInt("areacode"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( "kr.or.visitkorea.system.AnalysisMapper.selectSigungu" , paramterMap );
		
		objSheet.setColumnWidth(1, 7000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("순위");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역");

	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	        objRow.createCell(0).setCellValue(returnMap.get(i).get("areaname").toString());
	        objRow.createCell(1).setCellValue(Integer.parseInt(returnMap.get(i).get("cnt").toString()));
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectSigunguTot" , paramterMap );
	    objRow=objSheet.createRow(returnMap.size()+1);
//        objRow.createCell(0).setCellValue(String.valueOf(size+1));
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue(Integer.parseInt(returnMapTot.get(0).get("tot").toString()));		
	}

	private void analysis_sec_banner(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		paramterMap.put("section", parameterObject.getString("section"));
		paramterMap.put("areacode", parameterObject.getInt("areacode"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectSection" , paramterMap );
		
		objSheet.setColumnWidth(0, 8000);
		objSheet.setColumnWidth(1, 10000);
		objSheet.setColumnWidth(2, 3000);
		objSheet.setColumnWidth(3, 7000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("머리말");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("제목");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("클릭수");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("링크정보");

	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	    	objRow.createCell(0).setCellValue(returnMap.get(i).get("header")==null?"":returnMap.get(i).get("header").toString());
	    	objRow.createCell(1).setCellValue(returnMap.get(i).get("title")==null?"":returnMap.get(i).get("title").toString());
	        objRow.createCell(2).setCellValue(Integer.parseInt(returnMap.get(i).get("cnt").toString()));
	        objRow.createCell(3).setCellValue(returnMap.get(i).get("link")==null?"":returnMap.get(i).get("link").toString());
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectSectionTot" , paramterMap );
	    objRow=objSheet.createRow(returnMap.size()+1);
//        objRow.createCell(0).setCellValue(String.valueOf(size+1));
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(3); objCell.setCellStyle(tagStyle); objCell.setCellValue(Integer.parseInt(returnMapTot.get(0).get("tot").toString()));		
	}

	private void analysis_sec_logo(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectLogo" , paramterMap );
		
		objSheet.setColumnWidth(1, 7000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("순위");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("클릭수");

	    for (int i = 0; i < returnMap.size(); i++) {
	    	objRow=objSheet.createRow((short)i+1);
	        objRow.createCell(0).setCellValue(String.valueOf(i+1));
	        objRow.createCell(1).setCellValue(returnMap.get(i).get("areaName").toString());
	        objRow.createCell(2).setCellValue(Integer.parseInt(returnMap.get(i).get("cnt").toString()));
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectLogoTot" , paramterMap );
	    objRow=objSheet.createRow(returnMap.size()+1);
//        objRow.createCell(0).setCellValue(String.valueOf(size+1));
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue(Integer.parseInt(returnMapTot.get(0).get("tot").toString()));
			
	}

	private void analysis_tag(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,
			CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("name"))
			paramterMap.put("name", parameterObject.getString("name"));
		if(parameterObject.has("mtype"))
			paramterMap.put("mtype", parameterObject.getInt("mtype"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectAnalysisTag" , paramterMap );
		
		objSheet.setColumnWidth(1, 7000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("태그명");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("클릭수");

		int size = returnMap.size();
	    for (int i = 0; i < size; i++) {
	    	objRow=objSheet.createRow((short)i+1);
	        objRow.createCell(0).setCellValue(String.valueOf(i+1));
	        objRow.createCell(1).setCellValue(returnMap.get(i).get("name")==null?"":returnMap.get(i).get("name").toString());
	        objRow.createCell(2).setCellValue(Integer.parseInt(returnMap.get(i).get("cnt").toString()));
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectAnalysisTagTot" , paramterMap );
	    objRow=objSheet.createRow(size+1);
//        objRow.createCell(0).setCellValue(String.valueOf(size+1));
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue(Integer.parseInt(returnMapTot.get(0).get("tot").toString()));
	    		
	}

	private void analysis_main(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle,CellStyle tagStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectAnalysisMain" , paramterMap );
		
		objSheet.setColumnWidth(0, 7000);
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("메뉴명");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("클릭수");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("비율");

		int size = returnMap.size();
	    for (int i = 0; i < size; i++) {
	    	objRow=objSheet.createRow((short)i+1);
	    	float bi = 0f;
	    	if(returnMap.get(i).get("bi") != null) 
	    		bi = (float) (Float.parseFloat(returnMap.get(i).get("bi").toString())*100/100.0);
	        objRow.createCell(0).setCellValue(returnMap.get(i).get("name")==null?"":returnMap.get(i).get("name").toString());
	        objRow.createCell(1).setCellValue(Integer.parseInt(returnMap.get(i).get("mcnt").toString()));
	        objRow.createCell(2).setCellValue(bi);
	    }
	    List<HashMap<String, Object>> returnMapTot = sqlSession.selectList("kr.or.visitkorea.system.AnalysisMapper.selectAnalysisMainTot" , paramterMap );
	    objRow=objSheet.createRow(returnMap.size()+1);
	    objCell = objRow.createCell(0); objCell.setCellStyle(tagStyle); objCell.setCellValue("합   계");
	    objCell = objRow.createCell(1); objCell.setCellStyle(tagStyle); objCell.setCellValue(Integer.parseInt(returnMapTot.get(0).get("tot").toString()));
	    objCell = objRow.createCell(2); objCell.setCellStyle(tagStyle); objCell.setCellValue(100);		
	}

	private void comment(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("rtype"))
			paramterMap.put("searchType", parameterObject.getString("rtype"));
		if(parameterObject.has("sdate"))
			paramterMap.put("startDate", parameterObject.getString("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("endDate", parameterObject.getString("edate"));
		if(parameterObject.has("idname"))
			paramterMap.put("keyword", parameterObject.getString("idname"));
		else 
			paramterMap.put("keyword", "");
		
		List<HashMap<String, Object>> list = sqlSession.selectList( "kr.or.visitkorea.system.ContentMasterMapper.XLSDownload" , paramterMap );
		
		System.out.println("list.size() :: " + list.size());
		objSheet.setColumnWidth(0, 3000);
		objSheet.setColumnWidth(1, 3000);
		objSheet.setColumnWidth(2, 3000);
		objSheet.setColumnWidth(3, 3000);
		objSheet.setColumnWidth(4, 3000);
		objSheet.setColumnWidth(5, 3000);
		objSheet.setColumnWidth(6, 3000);
		objSheet.setColumnWidth(7, 3000);
		objSheet.setColumnWidth(8, 3000);
			
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("COT_ID");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("CID");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("콘텐츠명");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("작성자");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("댓글내용");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("등록일시");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("답글수");
		objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("상태");
		objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("노출여부");
		
    	 for (int i = 0; i < list.size(); i++) {
    		HashMap<String, Object> dataMap = list.get(i);
		    	objRow=objSheet.createRow((short)i+1);
		        objRow.createCell(0).setCellValue(String.valueOf(dataMap.get("COT_ID")));
		        objRow.createCell(1).setCellValue(String.valueOf(dataMap.get("CONTENT_ID") != null ? dataMap.get("CONTENT_ID") : ""));
		        objRow.createCell(2).setCellValue(String.valueOf(dataMap.get("TITLE")));
		        objRow.createCell(3).setCellValue(String.valueOf(dataMap.get("SNSNAME") != null ? dataMap.get("SNSNAME") : ""));
		        objRow.createCell(4).setCellValue(String.valueOf(dataMap.get("COMMENT")));
		        objRow.createCell(5).setCellValue(String.valueOf(dataMap.get("CREATE_DATE")));
		        objRow.createCell(6).setCellValue(String.valueOf(dataMap.get("REPLY_CNT")));
		        objRow.createCell(7).setCellValue(String.valueOf((int)dataMap.get("IS_DELETE") == 0 ? "미삭제" : (int)dataMap.get("IS_DELETE") == 1 ? "사용자 삭제" : "관리자 삭제"));
		        objRow.createCell(8).setCellValue(String.valueOf((int)dataMap.get("IS_VIEW") == 0 ? "미노출" : "노출"));

		 }		
	}

	private void event_result_join_user(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		try {
		if(parameterObject.has("evtId"))
			paramterMap.put("evtId", parameterObject.getString("evtId"));
		if(parameterObject.has("subEvtId"))
			paramterMap.put("subEvtId", parameterObject.getString("subEvtId"));
		if(parameterObject.has("limit"))
			paramterMap.put("limit", parameterObject.getInt("limit"));
		
		List<HashMap<String, Object>> list = sqlSession.selectList( "kr.or.visitkorea.system.EventResultMapper.selectEventDashboardJoinUserList" , paramterMap );
		
		HashMap<String,Object> userinfo = sqlSession.selectOne("kr.or.visitkorea.system.EventResultMapper.SelectUserInfoCollect",paramterMap);
		
		
		List<String> CellValue = new ArrayList<String>();
		CellValue.add("번호");
		CellValue.add("참여일시");
		if((int)userinfo.get("IS_NAME") == 1)CellValue.add("이름");
		if((int)userinfo.get("IS_TEL") == 1)CellValue.add("HP");
		if((int)userinfo.get("IS_GENDER") == 1)CellValue.add("성별");
		if((int)userinfo.get("IS_AGE") == 1)CellValue.add("나이");
		if((int)userinfo.get("IS_ADDR") == 1)CellValue.add("주소");
		if((int)userinfo.get("IS_JOB") == 1)CellValue.add("직업");
		if((int)userinfo.get("IS_EMAIL") == 1)CellValue.add("이메일");
		if((int)userinfo.get("IS_REGION") == 1)CellValue.add("지역");
		if((int)userinfo.get("IS_ETC") == 1)CellValue.add(userinfo.get("ETC_NM").toString());
		CellValue.add("당첨여부");
		CellValue.add("경품명");
		CellValue.add("참여방법");
		
		for (int i = 0; i < userinfo.size(); i++) {
			objSheet.setColumnWidth(i,3000);
		}
		
		objRow=objSheet.createRow((short)0);
		
		for (int i = 0; i < CellValue.size(); i++) {
			objCell = objRow.createCell(i);	objCell.setCellStyle(headStyle); objCell.setCellValue(CellValue.get(i));
		}

		
			
		
    	 for (int i = 0; i < list.size(); i++) {
    		HashMap<String, Object> dataMap = list.get(i);
		    	objRow=objSheet.createRow((short)i+1);
				objRow.createCell(0).setCellValue(i+1);
		        objRow.createCell(1).setCellValue(String.valueOf(dataMap.get("CREATE_DATE")));
		        for (int j = 2; j < CellValue.size(); j++) {
		        	
		        	if(CellValue.get(j).equals("이름"))
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("NAME")));
		        	else if(CellValue.get(j).equals("HP"))
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("TEL")));
		        	else if(CellValue.get(j).equals("성별")) {
		        		
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("GENDER")));
		        	}
		        		
		        	else if(CellValue.get(j).equals("나이")) {
		        		int Age = (int) dataMap.get("AGE");
		        		if(Age < 10) {
		        			Age = Age * 10;
		        		}
		        		objRow.createCell(j).setCellValue(Age);
		        	}
		        	else if(CellValue.get(j).equals("주소"))
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("ZIP_CODE") + " " + dataMap.get("ADDR") + " " +dataMap.get("ADDR_DETAIL")));
		        	else if(CellValue.get(j).equals("직업"))
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("JOB")));
		        	else if(CellValue.get(j).equals("이메일"))
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("EMAIL")));
		        	else if(CellValue.get(j).equals("지역"))
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("REGION")));
		        	else if(CellValue.get(j).equals(userinfo.get("ETC_NM").toString()))
		        		objRow.createCell(j).setCellValue(String.valueOf(dataMap.get("ETC")));
		        	else if(CellValue.get(j).equals("당첨여부"))
		        		objRow.createCell(j).setCellValue((dataMap.get("GFT_ID") == null)? "미당첨":"당첨");
		        	else if(CellValue.get(j).equals("경품명"))
		        		 objRow.createCell(j).setCellValue((dataMap.get("GFT_NM") == null)? "-": String.valueOf(dataMap.get("GFT_NM")) );
		        	else if(CellValue.get(j).equals("참여방법"))
		        		 objRow.createCell(j).setCellValue(("M".equals(dataMap.get("DEVICE_TYPE")) ? "모바일" : "PC" ));
			}
		 }
    	 
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void event_result_join(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		if(parameterObject.has("evtId"))
			paramterMap.put("evtId", parameterObject.getString("evtId"));
		if(parameterObject.has("subEvtId"))
			paramterMap.put("subEvtId", parameterObject.getString("subEvtId"));

		
		List<HashMap<String, Object>> list = sqlSession.selectList( "kr.or.visitkorea.system.EventResultMapper.selectEventDashboardJoinList" , paramterMap );
		
		objSheet.setColumnWidth(1, 3000);
		objSheet.setColumnWidth(2, 3000);
		objSheet.setColumnWidth(3, 3000);
		objSheet.setColumnWidth(4, 3000);
		objSheet.setColumnWidth(5, 3000);
		objSheet.setColumnWidth(6, 3000);
			
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("일자");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("참여건수");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("실참여건수");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("당첨건수");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("미당첨건수");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("PC");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("모바일");
    	 for (int i = 0; i < list.size(); i++) {
    		HashMap<String, Object> dataMap = list.get(i);
		    	objRow=objSheet.createRow((short)i+1);
		        objRow.createCell(0).setCellValue(String.valueOf(dataMap.get("DT")));
		        objRow.createCell(1).setCellValue(String.valueOf(dataMap.get("CNT")));
		        objRow.createCell(2).setCellValue(String.valueOf(dataMap.get("DEDUP_CNT")));
		        objRow.createCell(3).setCellValue(String.valueOf(dataMap.get("WIN_CNT")));
		        objRow.createCell(4).setCellValue(Integer.parseInt(String.valueOf(dataMap.get("CNT"))) - Integer.parseInt(String.valueOf(dataMap.get("WIN_CNT"))));
		        objRow.createCell(5).setCellValue(String.valueOf(dataMap.get("MCNT")));
		        objRow.createCell(6).setCellValue(String.valueOf(dataMap.get("PCNT")));

		 }		
	}

	private void codeCategoryXls(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if (this.parameterObject.has("SearchType"))
			paramterMap.put("SearchType", this.parameterObject.getInt("SearchType"));
		if (this.parameterObject.has("keyword"))
			paramterMap.put("Keyword", this.parameterObject.getString("keyword"));
		else paramterMap.put("Keyword", "");
		paramterMap.put("offset", null);
		
		List<HashMap<String, Object>> list = sqlSession.selectList( "kr.or.visitkorea.system.CodeMapper.selectCodeList" , paramterMap );
		
		objSheet.setColumnWidth(0, 2000);
		objSheet.setColumnWidth(1, 10000);
		objSheet.setColumnWidth(2, 20000);
		objSheet.setColumnWidth(3, 4000);
		objSheet.setColumnWidth(4, 4000);
		objSheet.setColumnWidth(5, 4000);
		objSheet.setColumnWidth(6, 4000);
		objSheet.setColumnWidth(7, 4000);
		objSheet.setColumnWidth(8, 4000);
		
		objRow=objSheet.createRow((short)0);
		objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
		objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류명");
		objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("가이드명");
		objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류별 컨텐츠 수(전체/표출)");
		objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("");
		objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("1순위(전체/표출)");
		objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("");
		objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("2순위(전체/표출)");
		objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("");
		objSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
		objSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
		objSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
		
		for (int i = 0; i < list.size(); i++) {
    		HashMap<String, Object> dataMap = list.get(i);
		    	objRow=objSheet.createRow((short)i+1);
				objRow.createCell(0).setCellValue(i+1);
				
				int CATEONE_ALL = 0;
			int CATEONE_OPEN = 0;
			int CATETWO_ALL = 0;
			int CATETWO_OPEN = 0;
			
			if(dataMap.containsKey("CATE_ALL"))
				CATEONE_ALL = Integer.parseInt(String.valueOf(dataMap.get("CATE_ALL")));
			if(dataMap.containsKey("CATE_OPEN"))
				CATEONE_OPEN = Integer.parseInt(String.valueOf(dataMap.get("CATE_OPEN")));
			if(dataMap.containsKey("CATETWO_ALL"))
				CATETWO_ALL = Integer.parseInt(String.valueOf(dataMap.get("CATETWO_ALL")));
			if(dataMap.containsKey("CATETWO_OPEN"))
				CATETWO_OPEN = Integer.parseInt(String.valueOf(dataMap.get("CATETWO_OPEN")));
			
			int CATE_ALL = CATEONE_ALL+CATETWO_ALL;
			int CATE_OPEN = CATEONE_OPEN+CATETWO_OPEN;
			
		        objRow.createCell(1).setCellValue(String.valueOf(dataMap.get("VALUE") != null ? dataMap.get("VALUE") : ""));
		        objRow.createCell(2).setCellValue(String.valueOf(dataMap.get("FILE_DESCRIPTION") != null ? dataMap.get("FILE_DESCRIPTION") : ""));
		        objRow.createCell(3).setCellValue(String.valueOf(CATE_ALL));
		        objRow.createCell(4).setCellValue(String.valueOf(CATE_OPEN));
		        objRow.createCell(5).setCellValue(String.valueOf(CATEONE_ALL));
		        objRow.createCell(6).setCellValue(String.valueOf(CATEONE_OPEN));
		        objRow.createCell(7).setCellValue(String.valueOf(CATETWO_ALL));
		        objRow.createCell(8).setCellValue(String.valueOf(CATETWO_OPEN));
		        
		        objRow = objSheet.createRow((short) 0);
				
				objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
				objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류명");
				objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("가이드명");
				objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류별 컨텐츠 수(전체/표출)");
				objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("");
				objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("1순위(전체/표출)");
				objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("");
				objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("2순위(전체/표출)");
				objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("");
		}
	}
	 
	
	
    private void selectSpotHistoryList(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		if(super.parameterObject.has("data")) {
			String dataString = super.parameterObject.getString("data");
			JSONObject data = new JSONObject(dataString);
			
			List<HashMap<String, Object>> spotHistoryList = super.sqlSession.selectList("kr.or.visitkorea.system.HistoryMapper.historyList", data.toMap());
			
			objSheet.setColumnWidth(0, 3000);
			objSheet.setColumnWidth(1, 4000);
			objSheet.setColumnWidth(2, 3000);
			objSheet.setColumnWidth(3, 3000);
			objSheet.setColumnWidth(4, 10000);
			objSheet.setColumnWidth(5, 4500);
			objSheet.setColumnWidth(6, 10000);
			objSheet.setColumnWidth(7, 10000);
			objSheet.setColumnWidth(8, 3000);
			objSheet.setColumnWidth(9, 3000);
			objSheet.setColumnWidth(10, 10000);
			
			objRow = objSheet.createRow((short) 0);
			
			objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("업소 CID");
			objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("인증번호");
			objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역");
			objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("시군구");
			objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("상세주소");
			objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("업소 전화번호");
			objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("업소명");
			objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("언급된 컨텐츠 COT");
			objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("컨텐츠 CID");
			objCell = objRow.createCell(9);	objCell.setCellStyle(headStyle); objCell.setCellValue("컨텐츠 구분");
			objCell = objRow.createCell(10);	objCell.setCellStyle(headStyle); objCell.setCellValue("언급된 컨텐츠명");
			
			int i = 0;
			for(HashMap<String, Object> spotData : spotHistoryList) {
				
				objRow = objSheet.createRow((short) 1+i++);

				if(spotData.get("SCONTENT_ID") != null) {
					objRow.createCell(0).setCellValue(spotData.get("SCONTENT_ID").toString());
				}
				if(spotData.get("AUTH_CODE") != null) {
					objRow.createCell(1).setCellValue(spotData.get("AUTH_CODE").toString());
				}
				if(spotData.get("AREA") != null) {
					objRow.createCell(2).setCellValue(spotData.get("AREA").toString());
				}
				if(spotData.get("SIGUGUN") != null) {
					objRow.createCell(3).setCellValue(spotData.get("SIGUGUN").toString());
				}
				if(spotData.get("ADDR") != null) {
					objRow.createCell(4).setCellValue(spotData.get("ADDR").toString());
				}
				if(spotData.get("INQUIRY") != null) {
					objRow.createCell(5).setCellValue(spotData.get("INQUIRY").toString());
				}
				if(spotData.get("STITLE") != null) {
					objRow.createCell(6).setCellValue(spotData.get("STITLE").toString());
				}
				if(spotData.get("MCOT_ID") != null) {
					objRow.createCell(7).setCellValue(spotData.get("MCOT_ID").toString());
				}
				if(spotData.get("MCONTENT_ID") != null) {
					objRow.createCell(8).setCellValue(spotData.get("MCONTENT_ID").toString());
				}
				if(spotData.get("MCONTENT_DIV") != null) {
					int contentDiv = Integer.parseInt(spotData.get("MCONTENT_DIV").toString());
					if(contentDiv == 1) {
						objRow.createCell(9).setCellValue("테마기사");
					} else {
						objRow.createCell(9).setCellValue("일반기사");
					}
				}
				if(spotData.get("MTITLE") != null) {
					objRow.createCell(10).setCellValue(spotData.get("MTITLE").toString());
				}
			}
		}
	}
	
	
    private void selectArticleHistoryList(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		if(super.parameterObject.has("data")) {
			String dataString = super.parameterObject.getString("data");
			JSONObject data = new JSONObject(dataString);
			
			List<HashMap<String, Object>> articleHistoryList = super.sqlSession.selectList("kr.or.visitkorea.system.HistoryMapper.historyList", data.toMap());

			objSheet.setColumnWidth(0, 3000);
			objSheet.setColumnWidth(1, 10000);
			objSheet.setColumnWidth(2, 3000);
			objSheet.setColumnWidth(3, 10000);
			objSheet.setColumnWidth(4, 3000);
			objSheet.setColumnWidth(5, 4000);
			objSheet.setColumnWidth(6, 3000);
			objSheet.setColumnWidth(7, 3000);
			objSheet.setColumnWidth(8, 4500);
			objSheet.setColumnWidth(9, 10000);
			
			objRow = objSheet.createRow((short) 0);
			
			objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("기사 CID");
			objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("기사 COT");
			objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("기사 구분");
			objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("기사명");
			objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("업소 CID");
			objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("인증번호");
			objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역");
			objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("시군구");
			objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("업소 전화번호");
			objCell = objRow.createCell(9);	objCell.setCellStyle(headStyle); objCell.setCellValue("업소명");
			
			int i = 0;
			for(HashMap<String, Object> articleData : articleHistoryList) {
				
				objRow = objSheet.createRow((short) 1+i++);

				if(articleData.get("MCONTENT_ID") != null) {
					objRow.createCell(0).setCellValue(articleData.get("MCONTENT_ID").toString());
				}
				if(articleData.get("MCOT_ID") != null) {
					objRow.createCell(1).setCellValue(articleData.get("MCOT_ID").toString());
				}
				if(articleData.get("MCONTENT_DIV") != null) {
					int contentDiv = Integer.parseInt(articleData.get("MCONTENT_DIV").toString());
					if(contentDiv == 1) {
						objRow.createCell(2).setCellValue("테마기사");
					} else {
						objRow.createCell(2).setCellValue("일반기사");
					}
				}
				if(articleData.get("MTITLE") != null) {
					objRow.createCell(3).setCellValue(articleData.get("MTITLE").toString());
				}
				if(articleData.get("SCONTENT_ID") != null) {
					objRow.createCell(4).setCellValue(articleData.get("SCONTENT_ID").toString());
				}
				if(articleData.get("AUTH_CODE") != null) {
					objRow.createCell(5).setCellValue(articleData.get("AUTH_CODE").toString());
				}
				if(articleData.get("AREA") != null) {
					objRow.createCell(6).setCellValue(articleData.get("AREA").toString());
				}
				if(articleData.get("SIGUGUN") != null) {
					objRow.createCell(7).setCellValue(articleData.get("SIGUGUN").toString());
				}
				if(articleData.get("INQUIRY") != null) {
					objRow.createCell(8).setCellValue(articleData.get("INQUIRY").toString());
				}
				if(articleData.get("STITLE") != null) {
					objRow.createCell(9).setCellValue(articleData.get("STITLE").toString());
				}
			}
		}
	}
    
    
    private void selectRelatedHistoryList(XSSFSheet objSheet, XSSFRow objRow, XSSFCell objCell, CellStyle headStyle) {
		if(super.parameterObject.has("data")) {
			String dataString = super.parameterObject.getString("data");
			JSONObject data = new JSONObject(dataString);
			
			List<HashMap<String, Object>> articleHistoryList = super.sqlSession.selectList("kr.or.visitkorea.system.HistoryMapper.historyList", data.toMap());

			objSheet.setColumnWidth(0, 3000);
			objSheet.setColumnWidth(1, 4000);
			objSheet.setColumnWidth(2, 2500);
			objSheet.setColumnWidth(3, 3000);
			objSheet.setColumnWidth(4, 3000);
			objSheet.setColumnWidth(5, 10000);
			objSheet.setColumnWidth(6, 6000);
			objSheet.setColumnWidth(7, 10000);
			objSheet.setColumnWidth(8, 5000);
			
			objRow = objSheet.createRow((short) 2);
			
			objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("CID");
			objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("인증번호");
			objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류");
			objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역");
			objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("시군구");
			objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("상세주소");
			objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("문의");
			objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("업소명");
			objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("히스토리 생성일");
			
			int i = 0;
			for(HashMap<String, Object> articleData : articleHistoryList) {
				
				if(i == 0) {
					objSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
					
					objRow = objSheet.createRow((short) 0);

					objCell = objRow.createCell(0);
					objCell.setCellValue("관련 컨텐츠명:　　" + articleData.get("MTITLE").toString() + 
							"　　(COT: " + articleData.get("MCOT_ID").toString() + ")");
				}
				
				objRow = objSheet.createRow((short) 3+i++);


				if(articleData.get("SCONTENT_ID") != null) {
					objRow.createCell(0).setCellValue(articleData.get("SCONTENT_ID").toString());
				}
				if(articleData.get("AUTH_CODE") != null) {
					objRow.createCell(1).setCellValue(articleData.get("AUTH_CODE").toString());
				}
				if(articleData.get("SCONTENT_TYPE") == null) {
					objRow.createCell(2).setCellValue("(직접입력)");
				} else {
					int contentType = Integer.parseInt(articleData.get("SCONTENT_TYPE").toString());
					String type = Registry.getContentType(contentType);
					objRow.createCell(2).setCellValue(type);
				}
				if(articleData.get("AREA") != null) {
					objRow.createCell(3).setCellValue(articleData.get("AREA").toString());
				}
				if(articleData.get("SIGUGUN") != null) {
					objRow.createCell(4).setCellValue(articleData.get("SIGUGUN").toString());
				}
				if(articleData.get("ADDR") != null) {
					objRow.createCell(5).setCellValue(articleData.get("ADDR").toString());
				}
				if(articleData.get("INQUIRY") != null) {
					objRow.createCell(6).setCellValue(articleData.get("INQUIRY").toString());
				}
				if(articleData.get("STITLE") != null) {
					objRow.createCell(7).setCellValue(articleData.get("STITLE").toString());
				}
				if(articleData.get("CREATE_DATE") != null) {
					objRow.createCell(8).setCellValue(articleData.get("CREATE_DATE").toString());
				}
			}
		}
    };
    
    
	private String getByte(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
//        System.out.println("request object :: " + parameterObject);
    }

    @Override
    protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
//        resultBodyObject.put("result", parameterObject.get("files"));
//        resultObject.put("return.type", "text/plain");
    }
    
    private void codeRecommed(XSSFSheet objSheet,XSSFRow objRow ,XSSFCell objCell, CellStyle headStyle) {
    	
    	HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("dateType"))
			paramterMap.put("dateType", parameterObject.getInt("dateType"));
		if(parameterObject.has("startInput"))
			paramterMap.put("startInput", parameterObject.getString("startInput"));
		if(parameterObject.has("endInput"))
			paramterMap.put("endInput", parameterObject.getString("endInput"));
		if(parameterObject.has("areaCode"))
			paramterMap.put("areaCode", parameterObject.getInt("areaCode"));
		if(parameterObject.has("keyword"))
			paramterMap.put("keyword", parameterObject.getString("keyword"));
		if(parameterObject.has("CODE"))
			paramterMap.put("CODE", parameterObject.getString("CODE"));
		if(parameterObject.has("IS_OPEN"))
			paramterMap.put("IS_OPEN", parameterObject.getInt("IS_OPEN"));
		if(parameterObject.has("IS_RANK"))
			paramterMap.put("IS_RANK", parameterObject.getInt("IS_RANK"));
		if(parameterObject.has("mode")) {
			if(parameterObject.getString("keyword").length() < 1)
				paramterMap.put("mode", -1);
			else
				paramterMap.put("mode", parameterObject.getInt("mode"));

		}
		paramterMap.put("offset", null);
		
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.ArticleMapper.list" , 
				paramterMap );
		
		objSheet.setColumnWidth(0, 3000);
		objSheet.setColumnWidth(1, 4000);
		objSheet.setColumnWidth(2, 4000);
		objSheet.setColumnWidth(3, 4000);
		objSheet.setColumnWidth(4, 15000);
		objSheet.setColumnWidth(5, 4000);
		objSheet.setColumnWidth(6, 4000);
		objSheet.setColumnWidth(7, 7000);
		objSheet.setColumnWidth(8, 7000);
		objSheet.setColumnWidth(9, 7000);
		
		objRow=objSheet.createRow((short)0);
	        objCell = objRow.createCell(0);	objCell.setCellStyle(headStyle); objCell.setCellValue("번호");
	        objCell = objRow.createCell(1);	objCell.setCellStyle(headStyle); objCell.setCellValue("CID/COT");
	        objCell = objRow.createCell(2);	objCell.setCellStyle(headStyle); objCell.setCellValue("분류");
	        objCell = objRow.createCell(3);	objCell.setCellStyle(headStyle); objCell.setCellValue("지역명");
	        objCell = objRow.createCell(4);	objCell.setCellStyle(headStyle); objCell.setCellValue("제목");
	        objCell = objRow.createCell(5);	objCell.setCellStyle(headStyle); objCell.setCellValue("대표태그");
	        objCell = objRow.createCell(6);	objCell.setCellStyle(headStyle); objCell.setCellValue("처리상태");
	        objCell = objRow.createCell(7);	objCell.setCellStyle(headStyle); objCell.setCellValue("최종 수정일");
	        objCell = objRow.createCell(8);	objCell.setCellStyle(headStyle); objCell.setCellValue("생성일");
	        objCell = objRow.createCell(9);	objCell.setCellStyle(headStyle); objCell.setCellValue("작성자");
		
		      for (int i = 0; i < returnMap.size(); i++) {
	    		HashMap<String, Object> dataMap = returnMap.get(i);
		    	objRow=objSheet.createRow((short)i+1);
				objRow.createCell(0).setCellValue(i+1);
		        objRow.createCell(1).setCellValue(String.valueOf(dataMap.get("CONTENT_ID") != null ? dataMap.get("CONTENT_ID") : ""));
		        objRow.createCell(2).setCellValue(String.valueOf(dataMap.get("CONTENT_CATEGORYK") != null ? dataMap.get("CONTENT_CATEGORYK") : ""));
		        objRow.createCell(3).setCellValue(String.valueOf(dataMap.get("AREA_NAME") != null ? dataMap.get("AREA_NAME") : ""));
		        objRow.createCell(4).setCellValue(String.valueOf(dataMap.get("TITLE") != null ? dataMap.get("TITLE") : ""));
		        objRow.createCell(5).setCellValue(String.valueOf(dataMap.get("MASTER_TAG") != null ? dataMap.get("MASTER_TAG") : ""));
		        objRow.createCell(6).setCellValue(String.valueOf(dataMap.get("CONTENT_STATUS") != null ? dataMap.get("CONTENT_STATUS") : ""));
		        objRow.createCell(7).setCellValue(String.valueOf(dataMap.get("FINAL_MODIFIED_DATE") != null ? dataMap.get("FINAL_MODIFIED_DATE") : ""));
		        objRow.createCell(8).setCellValue(String.valueOf(dataMap.get("CREATE_DATE") != null ? dataMap.get("CREATE_DATE") : ""));
		        objRow.createCell(9).setCellValue(String.valueOf(dataMap.get("USER_NAME") != null ? dataMap.get("USER_NAME") : ""));
			
		      }
		      
    }
}
