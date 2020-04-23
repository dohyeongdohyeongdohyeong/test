package kr.or.visitkorea.admin.client.manager.partners.affiliateProposal.dialogs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.FlexWrap;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.partners.affiliateProposal.PartnersAffiliateProposalMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.ScrollContentPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

/**
 * @author Admin
 * 제휴/제안 신청 Detail
 */
public class SelectContentDetail extends DialogContent {
	protected PartnersAffiliateProposalMain host;
	private Map<String, Object> dataSet;
	
	// 레이아웃 표현 시 사용될 각각의 멤버 변수를 지정
	private MaterialLabel title;
	private MaterialLabel message;
	private ScrollPanel sp;
	private ScrollContentPanel scp;
	private MaterialButton prevButton;
	private MaterialTextBox type;
	private MaterialComboBox<Object> status;
	private MaterialLabel reqHist;
	private MaterialTextBox reqTitle;
	private MaterialButton[] reqHistFile;
	private MaterialTextArea reqContent;
	private MaterialLabel reqHistFileList;
	private MaterialPanel answerlist;
	private int answerListHeight;

	public SelectContentDetail(MaterialExtentsWindow window, PartnersAffiliateProposalMain host) {
		super(window);
		this.host = host;
	}
	
	@Override
	public void init() {
		buildContent();
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		onInitData();
		onInitFilesUpload();
		onInitAnswer();
	}
	
	private void buildContent() {
		this.setBackgroundColor(Color.GREY_LIGHTEN_5);
		
		// 다이어로그 상단 제목 셋팅
		title = new MaterialLabel("제휴/제안 신청");
		title.setFontSize("1.4em");
		title.setFontWeight(FontWeight.BOLD);
		title.setTextColor(Color.BLUE);
		title.setPaddingTop(10);
		title.setPaddingLeft(30);
		this.add(title);
		
		message = new MaterialLabel("");
		message.setLayoutPosition(Position.ABSOLUTE);
		message.setFontSize("1.1em");
		message.setFontWeight(FontWeight.BOLD);
		message.setTextColor(Color.GREY_DARKEN_3);
		message.setTop(10);
		message.setLeft(250);
		this.add(message);
		
		// 스크롤을 제공하는 패널 셋팅
		sp = new ScrollPanel();
		sp.setHeight("600px");
		scp = new ScrollContentPanel(this);
		scp.setWidth("100%");
		scp.setBackgroundColor(Color.WHITE);
		scp.setOverflow(Overflow.HIDDEN);
		sp.add(scp);
		this.add(sp);
		
		// 목록 이동 버튼을 셋팅
		prevButton = new MaterialButton("목록 이동");
		prevButton.setLayoutPosition(Position.ABSOLUTE);
		prevButton.setBackgroundColor(Color.GREY_DARKEN_1);
		prevButton.setTop(20);
		prevButton.setRight(80);
		// 목록 이동 버튼 클릭 시 실행될 이벤트 지정
		prevButton.addClickHandler(event -> {
			getMaterialExtentsWindow().closeDialog();
		});
		// 스크롤콘텐츠패널에 버튼 등록
		scp.add(prevButton);
		
		// 목록 이동에 대한 권한 여부를 판별하여 기능 제어
		prevButton.setEnabled(Registry.getPermission("bc0c9fde-3d5d-414a-9ef7-43758eff52f2"));
		
		// 신청구분에 대한 콤보박스를 셋팅
		type = new MaterialTextBox();
		type.setLabel("신청 구분");
		type.setLayoutPosition(Position.ABSOLUTE);
		type.setTop(20); 
		type.setLeft(40); 
		type.setWidth("300px");
		type.setHeight("35px");
		type.setFontSize("16px");
		type.setBackgroundColor(Color.GREY_LIGHTEN_5);
		type.setBorderRadius("1px");
		type.setPaddingLeft(15);
		type.setReadOnly(true);
		scp.add(type);
		
		// 처리 상태 콤보박스를 셋팅
		status = new MaterialComboBox<Object>();
		status.setLabel("처리 상태");
		status.setLayoutPosition(Position.ABSOLUTE);
		status.setTop(20);
		status.setLeft(370);
		status.setWidth("300px");
		status.setHeight("35px");
		status.setFontSize("16px");
		status.setBackgroundColor(Color.GREY_LIGHTEN_5);
		status.setBorderRadius("1px");
		status.setPaddingLeft(15);
		status.addItem("접수", 0);
		status.addItem("확인 중", 1);
		status.addItem("답변 완료", 2);
		// 처리 상태 콤보박스의 선택된 아이템이 변경되었을 경우 실행될 이벤트
		status.addValueChangeHandler(event -> {
			JSONObject parameterJSON = new JSONObject();
			// 서버쪽 비지니스 로직을 호출 및 필요한 데이터를 각각 셋팅
			parameterJSON.put("cmd", new JSONString("UPDATE_PARTNERS_PROPOSAL"));
			parameterJSON.put("ppsId", new JSONString(dataSet.get("ppsId").toString()));
			parameterJSON.put("status", new JSONString(status.getSelectedValue().get(0).toString()));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					
					// 서버쪽에서 처리한 결과의 성공 여부를 판별
					if (processResult.equals("success")) {
						// 상세 페이지에서 변경된 값을 리스트 페이지에 반영하기 위해 해당 row 객체를 가져와 캐스팅
						ContentTableRow row = (ContentTableRow) getParameters().get("row");
						// 상세 페이지에서 변경된 처리 일시를 반영하기 위한 변수
						MaterialLabel rowUpdateDate = (MaterialLabel) row.getColumnObject(5);
						// 상세 페이지에서 변경된 처리 상태를 반영하기 위한 변수
						MaterialLabel rowStatus = (MaterialLabel) row.getColumnObject(6);
						
						// 상세 페이지에서 변경된 일자를 반영하기 위해 클라이언트에서 일자 생성
						Date date = new Date();
						String currentDate = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(date);
						
						// 변경된 처리 일자 값을 등록
						rowUpdateDate.setText(currentDate + ".0");
						
						// 변경된 처리 상태 값을 등록
						rowStatus.setText(
								status.getSelectedValue().get(0).toString().equals("0") ? "접수" :
									status.getSelectedValue().get(0).toString().equals("1") ? "확인 중" :
										status.getSelectedValue().get(0).toString().equals("2") ? "답변 완료" : "-"
								);
					} else {
						// 서버쪽에서 처리에 실패했을 경우 사용자에게 표시할 alert 창
						getMaterialExtentsWindow().alert("상태변경에 실패했습니다. 관리자에게 문의하세요.", 500);
					}
				}
			});
		});
		scp.add(status);
		
		// 처리 상태에 대한 권한 여부를 판별하여 기능 제어
		status.setEnabled(Registry.getPermission("41aa1e00-f26e-4b53-af9b-6ab4ced544c7"));
		
		// 신청 내역 영역 구분을 표기하기 위한 라벨을 셋팅
		reqHist = new MaterialLabel("신청 내역");
		reqHist.setLayoutPosition(Position.ABSOLUTE);
		reqHist.setTop(110);
		reqHist.setLeft(40);
		reqHist.setWidth("1100px");
		reqHist.setHeight("10px");
		reqHist.setFontWeight(FontWeight.BOLD);
		reqHist.setFontSize("16px");
		reqHist.setTextAlign(TextAlign.LEFT);
		reqHist.setVerticalAlign(VerticalAlign.MIDDLE);
		reqHist.setBackgroundColor(Color.GREY_LIGHTEN_2);
		reqHist.setBorderRadius("3px");
		scp.add(reqHist);
		
		// 신청 내역 제목을 표기하기 위한 텍스트박스 셋팅
		reqTitle = new MaterialTextBox();
		reqTitle.setLayoutPosition(Position.ABSOLUTE);
		reqTitle.setTop(140); 
		reqTitle.setLeft(40); 
		reqTitle.setWidth("1100px"); 
		reqTitle.setHeight("10px");
		reqTitle.setFontSize("16px");
		reqTitle.setBackgroundColor(Color.GREY_LIGHTEN_5);
		reqTitle.setBorderRadius("1px");
		reqTitle.setPaddingLeft(15);
		reqTitle.setLabel("신청 제목");
		// 읽기 전용
		reqTitle.setReadOnly(true);
		scp.add(reqTitle);
		
		// 신청 내역의 내용을 표기하기 위한 텍스트에어리어 셋팅
		reqContent = new MaterialTextArea();
		reqContent.setLayoutPosition(Position.ABSOLUTE);
		reqContent.setTop(210);
		reqContent.setLeft(40);
		reqContent.setWidth("1100px");
//		reqContent.setHeight("110px");
		reqContent.setFontSize("16px");
		reqContent.setBackgroundColor(Color.GREY_LIGHTEN_5);
		reqContent.setBorderRadius("1px");
//		reqContent.setOverflow(Overflow.HIDDEN);
		reqContent.setPaddingLeft(15);
		reqContent.setLabel("신청 내용");
		reqContent.setReadOnly(true);
		
		scp.add(reqContent);
		
		// 첨부 파일 라벨을 셋팅
		reqHistFileList = new MaterialLabel("첨부파일");
		reqHistFileList.setLayoutPosition(Position.ABSOLUTE);
		reqHistFileList.setTop(360); reqHistFileList.setLeft(40); reqHistFileList.setWidth("180px"); reqHistFileList.setHeight("35px");
		reqHistFileList.setTextAlign(TextAlign.CENTER);
		reqHistFileList.setVerticalAlign(VerticalAlign.MIDDLE);
		reqHistFileList.setBackgroundColor(Color.GREY_LIGHTEN_2);
		reqHistFileList.setBorderRadius("3px");
		scp.add(reqHistFileList);
		
		// 첨부 파일의 권한 여부를 판별하여 기능 제어
		boolean histFilePermission = Registry.getPermission("67a95aca-c935-4d3a-8e87-a7573f07464c");
		
		// 첨부 파일은 현재 최대 5개까지 제공
		reqHistFile = new MaterialButton[5];
		reqHistFile[0] = new MaterialButton("다운로드"); reqHistFile[0].setFontSize("9px");
		reqHistFile[0].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[0].setTop(360); reqHistFile[0].setLeft(230); reqHistFile[0].setWidth("165px"); reqHistFile[0].setHeight("35px");
		reqHistFile[0].setVisible(false);
		reqHistFile[0].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[0].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[0].setOverflow(Overflow.HIDDEN);
		reqHistFile[0].setEnabled(histFilePermission);
		scp.add(reqHistFile[0]);
		
		reqHistFile[1] = new MaterialButton("다운로드"); reqHistFile[1].setFontSize("9px");
		reqHistFile[1].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[1].setTop(360); reqHistFile[1].setLeft(400); reqHistFile[1].setWidth("165px"); reqHistFile[1].setHeight("35px");
		reqHistFile[1].setVisible(false);
		reqHistFile[1].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[1].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[1].setOverflow(Overflow.HIDDEN);
		reqHistFile[1].setEnabled(histFilePermission);
		scp.add(reqHistFile[1]);
		
		reqHistFile[2] = new MaterialButton("다운로드"); reqHistFile[2].setFontSize("9px");
		reqHistFile[2].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[2].setTop(360); reqHistFile[2].setLeft(570); reqHistFile[2].setWidth("165px"); reqHistFile[2].setHeight("35px");
		reqHistFile[2].setVisible(false);
		reqHistFile[2].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[2].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[2].setOverflow(Overflow.HIDDEN);
		reqHistFile[2].setEnabled(histFilePermission);
		scp.add(reqHistFile[2]);
		
		reqHistFile[3] = new MaterialButton("다운로드"); reqHistFile[3].setFontSize("9px");
		reqHistFile[3].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[3].setTop(360); reqHistFile[3].setLeft(740); reqHistFile[3].setWidth("165px"); reqHistFile[3].setHeight("35px");
		reqHistFile[3].setVisible(false);
		reqHistFile[3].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[3].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[3].setOverflow(Overflow.HIDDEN);
		reqHistFile[3].setEnabled(histFilePermission);
		scp.add(reqHistFile[3]);
		
		reqHistFile[4] = new MaterialButton("다운로드"); reqHistFile[4].setFontSize("9px");
		reqHistFile[4].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[4].setTop(360); reqHistFile[4].setLeft(910); reqHistFile[4].setWidth("165px"); reqHistFile[4].setHeight("35px");
		reqHistFile[4].setVisible(false);
		reqHistFile[4].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[4].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[4].setOverflow(Overflow.HIDDEN);
		reqHistFile[4].setEnabled(histFilePermission);
		scp.add(reqHistFile[4]);
		
		answerlist = new MaterialPanel();
		answerlist.setLayoutPosition(Position.ABSOLUTE);
		answerlist.setTop(450);
		answerlist.setLeft(40);
		answerlist.setWidth("1300px");
		scp.add(answerlist);
	}
	
	// 상세 페이지에서 댓글 유무를 판별 및 해당 레이아웃을 제공
	private void onInitAnswer() {
		JSONObject parameterJSON = new JSONObject();
		// 기존에 댓글이 있는지 유무를 판별하기 위해 서버 사이드 모듈을 호출
		parameterJSON.put("cmd", new JSONString("SELECT_PARTNERS_PROPOSAL_ANSWER"));
		// 댓글 유무를 조회할 수 있는 ppsId 키를 파라미터로 전달
		parameterJSON.put("ppsId", new JSONString(dataSet.get("ppsId").toString()));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				parameterJSON.put("ppsId", new JSONString(dataSet.get("ppsId").toString()));
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().stringValue();
						// 서버쪽 처리의 성공 여부를 판별
						if (processResult.equals("success")) {
							// 기존에 댓글 리스트가 존재할 수 있으므로 클리어
							answerlist.clear();
							// 댓글 관리 라벨을 셋팅
							MaterialLabel mlsp3 = new MaterialLabel("댓글 관리"); 
							mlsp3.setWidth("1100px"); mlsp3.setHeight("10px");
							mlsp3.setFontSize("16px"); mlsp3.setFontWeight(FontWeight.BOLD);
							mlsp3.setTextAlign(TextAlign.LEFT);
							mlsp3.setVerticalAlign(VerticalAlign.MIDDLE);
							mlsp3.setBackgroundColor(Color.GREY_LIGHTEN_2);
							mlsp3.setBorderRadius("3px");
							answerlist.add(mlsp3);
							answerListHeight = 0;
							answerlist.setHeight(answerListHeight+"px");
							// 서버쪽에서 받은 object 데이터를 JSONObject 타입의 객체로 캐스팅
							JSONObject bodyObj = (JSONObject) resultObj.get("body");
							JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
							
							// 데이터의 사이즈 만큼 반복문 실행
							int usrCnt = bodyResultObj.size();
							for(int i= 0;i< usrCnt;i++) {
								JSONObject obj = (JSONObject)bodyResultObj.get(i);
								// row 객체를 선언 및 초기화
								MaterialRow mrq = new MaterialRow();
								mrq.setLayoutPosition(Position.RELATIVE);
								// image 객체를 선언 및 초기화
								MaterialImage mlq = new MaterialImage();
								mlq.setLayoutPosition(Position.ABSOLUTE);
								mlq.setLeft(10); mlq.setTop(10); mlq.setWidth("90px"); mlq.setHeight("90px");
								mlq.setBorderRadius("2px");
								mlq.setPixelSize(90, 90);
								mrq.add(mlq);
								// 이미지 경로의 값이 없을 경우 이미지 서비스를 통해 제공되는 디폴트 값으로 셋팅
								if(obj.get("imgpath") == null) {
									if(obj.get("usrid") == null)
										mlq.setValue((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=df2b1934-d39e-455f-8f91-1ad9915da901");
									else mlq.setValue((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=df2b1934-d39e-455f-8f91-1ad9915da901");
								} else mlq.setValue(obj.get("imgpath").isString().stringValue());
								
								// 댓글을 표현할 텍스트에어리 컴포넌트를 셋팅 
								MaterialTextArea mtaq = new MaterialTextArea();
								mtaq.setLayoutPosition(Position.ABSOLUTE);
								mtaq.setLeft(130); mtaq.setWidth("890px");
								mrq.add(mtaq);
								// 댓글을 삭제하기 위한 버튼을 셋팅
								MaterialButton mbq = new MaterialButton("삭제");
								mbq.setLayoutPosition(Position.ABSOLUTE);
								mbq.setTop(15); mbq.setLeft(1030); mbq.setWidth("100px"); mbq.setHeight("85px");
								mbq.addClickHandler(e-> {
									JSONObject parameterJSON = new JSONObject();
									// 댓글 삭제 처리를 위해 서버쪽 모듈을 호출
									parameterJSON.put("cmd", new JSONString("DELETE_PARTNERS_PROPOSAL_ANSWER"));
									parameterJSON.put("ppsId", new JSONString(obj.get("ppsId").isString().stringValue()));
									VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
										@Override
										public void call(Object param1, String param2, Object param3) {
											JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
											JSONObject headerObj = (JSONObject) resultObj.get("header");
											String processResult = headerObj.get("process").isString().stringValue();
											if (processResult.equals("success")) {
												// 서버쪽에서 요청이 성공적으로 처리되었을 경우 토스트 알림을 띄워 사용자에게 알림
												MaterialToast.fireToast("삭제 되었습니다.", 3000);
												// 처리된 로직을 갱신하기 위해 재귀 호출
												onInitAnswer();
											} else {
												// 실패했을 경우는 관리자에게 문의를 요청하는 토스트 알림을 띄움
												MaterialToast.fireToast("삭제에 실패했습니다. 관리자에게 문의하세요.", 3000);
											}
										}
									});
								});
								mrq.add(mbq);
								
								// 삭제 기능에 대한 권한 여부를 판별하여 기능을 제어
								mbq.setEnabled(Registry.getPermission("e74d13f3-4c9f-4cf8-97d9-332a355c3fdb"));
								// 서버쪽에서 넘겨받은 body 키에 대한 값의 널 체크를 함과 동시에 체크 결과에 따라 각각의 값을 셋팅 
								mtaq.setText(obj.get("body")!=null?obj.get("body").isString().stringValue():"");
								if(i == 0) 
									mrq.setMarginTop(answerListHeight+15);
								else mrq.setMarginTop(answerListHeight);
								// 각 처리된 row를 댓글 리스트에 추가
								answerlist.add(mrq);
								if(answerListHeight == 0)
									answerListHeight+=110;
							}
							// 댓글 달기를 표현하기 위한 row를 선언 및 초기화
							MaterialRow mrAnswer = new MaterialRow();
							mrAnswer.setLayoutPosition(Position.RELATIVE);
							// 댓글 내용을 입력할 수 있는 텍스트에어리어 선언 및 초기화
							MaterialTextArea mtaAnswer = new MaterialTextArea();
							mtaAnswer.setLabel("댓글달기");
							mtaAnswer.setLayoutPosition(Position.ABSOLUTE);
							mtaAnswer.setLeft(10); mtaAnswer.setWidth("1000px");
							mrAnswer.add(mtaAnswer);
							// 댓글을 달기 위한 기능을 표현하기 위한 버튼을 선언 및 초기화
							MaterialButton mbAnswer = new MaterialButton("답변달기");
							mbAnswer.setLayoutPosition(Position.ABSOLUTE);
							mbAnswer.setTop(15); mbAnswer.setLeft(1030); mbAnswer.setWidth("100px"); mbAnswer.setHeight("85px");
							mrAnswer.add(mbAnswer);
							
							// 댓글 달기에 대한 권한 여부를 판별하여 기능을 제어
							mbAnswer.setEnabled(Registry.getPermission("9d4d0d56-bb1d-4753-a97b-4053631f5ef8"));
							
							// 댓글 달기 버튼을 클릭했을 경우 처리될 로직
							mbAnswer.addClickHandler(e->{
								// 텍스트 길이를 기준으로 입력 여부를 판별
								if(mtaAnswer.getText().trim().equals("")) {
									getMaterialExtentsWindow().alert("댓글 내용을 입력해 주세요.", 500);
									return;
								}
								// 데이터 처리를 위한 서버쪽 모듈을 호출
								JSONObject parameterJSON = new JSONObject();
								parameterJSON.put("cmd", new JSONString("INSERT_PARTNERS_PROPOSAL_ANSWER"));
								parameterJSON.put("ppsId", new JSONString(IDUtil.uuid()));
								parameterJSON.put("parentId", new JSONString(dataSet.get("ppsId").toString()));
								parameterJSON.put("usrId", new JSONString(Registry.getUserId()));
								parameterJSON.put("body", new JSONString(mtaAnswer.getText()));
								parameterJSON.put("phId", new JSONString(IDUtil.uuid()));
								
								VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
									@Override
									public void call(Object param1, String param2, Object param3) {
										JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
										JSONObject headerObj = (JSONObject) resultObj.get("header");
										String processResult = headerObj.get("process").isString().stringValue();
										// 서버쪽에서 넘겨받은 결과 값 성공 여부를 판별
										if (processResult.equals("success")) {
											// 상세 페이지에서 처리되어 변경된 결과 값을 리스트 페이지에서 적용
											ContentTableRow row = (ContentTableRow) getParameters().get("row");
											MaterialLabel rowReply = (MaterialLabel) row.getColumnObject(7);
											rowReply.setText("-");
											// 처리된 댓글 부분을 갱신하기 위해 댓글 목록을 조회하는 메소드 호출
											onInitAnswer();
										} else {
											// 실패 시 사용자에게 alert 창을 띄워 알림
											getMaterialExtentsWindow().alert("답변달기에 실패했습니다. 관리자에게 문의하세요.", 500);
										}
									}
								});
							});
							if(answerListHeight == 0) answerListHeight = 10;
							mrAnswer.setMarginTop(answerListHeight);
							// 처리된 댓글 관련 row를 answerlist에 추가
							answerlist.add(mrAnswer);

						}else if (processResult.equals("fail")) {
							getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
						}
					}
				});
			}
		});
	}
	
	private void onInitFilesUpload() {
		JSONObject parameterJSON = new JSONObject();
		// 파일 업로드를 초기화하기 서버쪽 모듈을 호출
		parameterJSON.put("cmd", new JSONString("SELECT_PARTNERS_PROPOSAL_FILESUPLOAD"));
		parameterJSON.put("ppsId", new JSONString(dataSet.get("ppsId").toString()));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				
				if (processResult.equals("success")) {
					for(int i = 0; i < 5; i++) {
						// 최대 5개까지 제공하는 파일 첨부 버튼은 disabled로 초기화
						reqHistFile[i].setVisible(false);
					}
					
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					
					// 서버쪽에서 처리하여 존재하는 데이터 수만큼 파일첨부을 셋팅하고 활성화
					for(int i = 0; i < bodyResultObj.size(); i++) {
						JSONObject obj = (JSONObject) bodyResultObj.get(i);
						final String fileUrl = obj.get("FILE_ID") != null ? obj.get("FILE_ID").isString().stringValue() : "";
						reqHistFile[i].setText(obj.get("FILE_ORG_NAME") != null ? obj.get("FILE_ORG_NAME").isString().stringValue() : "");
						reqHistFile[i].setVisible(true);
						reqHistFile[i].getHandlerRegistry().clearHandlers();
						reqHistFile[i].getHandlerRegistry().registerHandler(
								reqHistFile[i].addClickHandler(e->{
							if(fileUrl.lastIndexOf(".") > -1)
								Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + fileUrl, "", "");
							else
								Window.open((String) Registry.get("image.server") + "/img/call?cmd=VIEW&mode=raw&id=" + fileUrl, "", "");
						}));
					}
				} else {
					getMaterialExtentsWindow().alert("상태변경에 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			}
		});
	}
	
	private void onInitData() {
		// load 시에 데이터를 바인딩 처리를 담당하는 부분, 키와 값, map 형태로 주고 받음
		dataSet = new HashMap<String, Object>();
		dataSet.put("ppsId", this.getParameters().get("ppsId"));
		dataSet.put("title", this.getParameters().get("title"));
		dataSet.put("body", this.getParameters().get("body"));
		dataSet.put("status", this.getParameters().get("status"));
		dataSet.put("type", this.getParameters().get("type"));
		
		type.setText(
				dataSet.get("type").toString() == "1" ? "제휴"
						: dataSet.get("type").toString() == "2" ? "제안"
								: "-"
				);
		if (dataSet.get("status").toString().equals("0"))
			status.setSelectedIndex(0);
		if (dataSet.get("status").toString().equals("1"))
			status.setSelectedIndex(1);
		if (dataSet.get("status").toString().equals("2"))
			status.setSelectedIndex(2);
		
		reqTitle.setValue(dataSet.get("title").toString());
		reqContent.setValue(dataSet.get("body").toString());
	}

	@Override
	public int getHeight() {
		return 670;
	}
}