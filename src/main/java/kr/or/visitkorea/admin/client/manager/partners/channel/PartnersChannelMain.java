package kr.or.visitkorea.admin.client.manager.partners.channel;


import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.FlexWrap;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
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
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.ScrollContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersChannelMain extends AbstractContentPanel {

	private ContentTable table;
	private MaterialLabel countLabel;
	private int offset;
	private MaterialTextBox sdate, edate;
	private MaterialComboBox<Object> cbidname;
	private MaterialComboBox<Object> category, status, edstatus, edposition;
	private MaterialTextBox edidname;
	private int totcnt;
	private int index;
	private MaterialLabel reqHistFileList;
	private MaterialButton[] reqHistFile;
	protected String pahid;
	private MaterialTextBox typeTextBox;
	
	public PartnersChannelMain(MaterialExtentsWindow materialExtentsWindow, PartnersChannelApplication pa) {
		super(materialExtentsWindow);
	}
	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		buildTableTab();
		buildDetailTab();
	}
	private ScrollContentPanel mpdetail;
	private MaterialPanel mptable, mpfile;
	private MaterialTextBox mttitle,mtchum, term;
	private MaterialTextArea mtbody;
	private MaterialPanel mpAnswerlist;
	private ScrollPanel scrollpanel;
	private MaterialTextBox txtFTitle, txtFSUBTitle, txtLinkUrl, txtFName, txtPDFName;
	private MaterialButton btnDN, btnFDN, btnPDFDN;
	private MaterialTextBox publisherTitle;
	private MaterialTextBox publisherDate;
	private int Order;
	private MaterialButton PartnersExcelButton;
	
	private void onLoadMultiFileUpload(String pahid) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_PARTNERS_REQUESTHIST_FILEUPLOAD"));
		parameterJSON.put("pahid", new JSONString(pahid));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					for(int i = 0; i < 5; i++) {
						reqHistFile[i].setVisible(false);
					}
					
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					
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
	
	
	private void buildDetailTab() {
		scrollpanel = new ScrollPanel();
		mpdetail = new ScrollContentPanel(this);
		mpdetail.setLeft(500);
		mpdetail.setWidth("60%");
		scrollpanel.setHeight("650px");
		mpdetail.setBackgroundColor(Color.WHITE);
		mpdetail.setOverflow(Overflow.HIDDEN);
		mpdetail.setVisible(false);
		
		scrollpanel.add(mpdetail);
		MaterialButton mbprev = new MaterialButton("목록이동");
		mbprev.setLayoutPosition(Position.ABSOLUTE);
		mbprev.setBackgroundColor(Color.GREY_DARKEN_1);
		mbprev.setTop(20); mbprev.setRight(80);
		mbprev.addClickHandler(e->{
			mpdetail.setVisible(false);
			mptable.setVisible(true);
			qryList(true);
		});
		String labelfontsize = "16px", labelheight="35px";
		String inputwidth = "300px";
		mpdetail.add(mbprev);
		
		typeTextBox = new MaterialTextBox();
		typeTextBox.setLabel("신청구분");
		typeTextBox.setLayoutPosition(Position.ABSOLUTE);
		typeTextBox.setTop(20); typeTextBox.setLeft(40); typeTextBox.setWidth(inputwidth); typeTextBox.setHeight(labelheight);
		typeTextBox.setFontSize(labelfontsize);
		typeTextBox.setBackgroundColor(Color.GREY_LIGHTEN_5);
		typeTextBox.setBorderRadius("1px");
		typeTextBox.setPaddingLeft(15);
		typeTextBox.setReadOnly(true);
		mpdetail.add(typeTextBox);
		
		edstatus = new MaterialComboBox<Object>();
		edstatus.setLabel("처리상태");
		edstatus.setLayoutPosition(Position.ABSOLUTE);
		edstatus.setTop(20); edstatus.setLeft(200); edstatus.setWidth(inputwidth); edstatus.setHeight(labelheight);
		edstatus.setFontSize(labelfontsize);
		edstatus.setBackgroundColor(Color.GREY_LIGHTEN_5);
		edstatus.setBorderRadius("1px");
		edstatus.setPaddingLeft(15);
		mpdetail.add(edstatus);
		edstatus.addItem("접수", 0);
		edstatus.addItem("처리중", 1);
		edstatus.addItem("승인", 2);
		edstatus.addItem("처리완료", 3);
		edstatus.addItem("반려", 4);
		edstatus.addValueChangeHandler(ee->{
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_PARTNERS_CHANNEL"));
			parameterJSON.put("pahid", new JSONString(pahid));
			parameterJSON.put("status", new JSONString(edstatus.getSelectedIndex()+""));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
					} else {
						getMaterialExtentsWindow().alert("상태변경에 실패했습니다. 관리자에게 문의하세요.", 500);
					}
				}
			});
		});
		
		edposition = new MaterialComboBox<Object>();
		edposition.setLayoutPosition(Position.ABSOLUTE);
		edposition.setTop(20); edposition.setLeft(560); edposition.setWidth(inputwidth); edposition.setHeight(labelheight);
		edposition.setFontSize(labelfontsize);
		edposition.setBackgroundColor(Color.GREY_LIGHTEN_5);
		edposition.setBorderRadius("1px");
		edposition.setPaddingLeft(15);
		edposition.setLabel("홍보위치");
		edposition.addItem("미선택", 0);
		edposition.addItem("메인-지자체 축제배너", 1);
		edposition.addItem("메인-지자체 일반배너", 2);
		edposition.addItem("추천메인배너", 3);
		edposition.addItem("코스메인배너", 4);
		edposition.addItem("여행지목록배너", 5);
		edposition.addItem("소식목록배너", 6);
		mpdetail.add(edposition);
		edposition.addValueChangeHandler(ee->{
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_PARTNERS_CHANNEL"));
			parameterJSON.put("pahid", new JSONString(pahid));
			parameterJSON.put("position", new JSONString(edposition.getSelectedIndex()+""));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
					} else {
						getMaterialExtentsWindow().alert("상태변경에 실패했습니다. 관리자에게 문의하세요.", 500);
					}
				}
			});
		});
		
		MaterialLabel mlsp1 = new MaterialLabel("신청내역");
		mlsp1.setLayoutPosition(Position.ABSOLUTE);
		mlsp1.setTop(110); mlsp1.setLeft(40); mlsp1.setWidth("1100px");	mlsp1.setHeight("10px");
		mlsp1.setFontWeight(FontWeight.BOLD);
		mlsp1.setFontSize(labelfontsize);
		mlsp1.setTextAlign(TextAlign.LEFT);
		mlsp1.setVerticalAlign(VerticalAlign.MIDDLE);
		mlsp1.setBackgroundColor(Color.GREY_LIGHTEN_2);
		mlsp1.setBorderRadius("3px");
		mpdetail.add(mlsp1);
		
		mttitle = new MaterialTextBox();
		mttitle.setLayoutPosition(Position.ABSOLUTE);
		mttitle.setTop(140); mttitle.setLeft(40); mttitle.setWidth("600px"); mttitle.setHeight(labelheight);
		mttitle.setFontSize(labelfontsize);
		mttitle.setBackgroundColor(Color.GREY_LIGHTEN_5);
		mttitle.setBorderRadius("1px");
		mttitle.setPaddingLeft(15);
		mttitle.setLabel("신청제목");
		mpdetail.add(mttitle);
		
		mtchum = new MaterialTextBox();
		mtchum.setLayoutPosition(Position.ABSOLUTE);
		mtchum.setTop(140); mtchum.setLeft(740); mtchum.setWidth("250px");	mtchum.setHeight(labelheight);
		mtchum.setFontSize(labelfontsize);
		mtchum.setBackgroundColor(Color.GREY_LIGHTEN_5);
		mtchum.setPaddingLeft(15);
		mtchum.setBorderRadius("1px");
		mtchum.setLabel("첨부파일");
//		mpdetail.add(mtchum);
		
		btnDN = new MaterialButton("다운로드"); btnDN.setFontSize("9px");
		btnDN.setLayoutPosition(Position.ABSOLUTE);
		btnDN.setTop(160); btnDN.setLeft(1020); btnDN.setWidth("110px");
		btnDN.setHeight(labelheight);
		btnDN.setBackgroundColor(Color.GREY_LIGHTEN_1);
		btnDN.setEnabled(false);
//		mpdetail.add(btnDN);
		
		mtbody = new MaterialTextArea();
		mtbody.setLayoutPosition(Position.ABSOLUTE);
		mtbody.setTop(210);	mtbody.setLeft(40); mtbody.setWidth("1090px"); mtbody.setHeight("120px");
		mtbody.setFontSize(labelfontsize);
		mtbody.setBackgroundColor(Color.GREY_LIGHTEN_5);
		mtbody.setBorderRadius("1px");
		mtbody.setPaddingLeft(15);
		mtbody.setLabel("신청내용");
		mtbody.getChildrenList().get(0).getElement().getStyle().setPadding(5, Unit.PX);
		mtbody.getChildrenList().get(0).getElement().getStyle().setTop(0, Unit.PX);
		mtbody.getWidget(0).getElement().getStyle().setProperty("maxHeight", "35px");
		mtbody.getWidget(0).getElement().getStyle().setProperty("overflow", "auto");
//		mtbody.getChildrenList().get(0).getElement().getStyle().setHeight(115, Unit.PX);
//		mtbody.getChildrenList().get(0).getElement().getStyle().setOverflow(Overflow.AUTO);
		mpdetail.add(mtbody);
		
		// 첨부파일
		reqHistFileList = new MaterialLabel("첨부파일");
		
		reqHistFileList.setLayoutPosition(Position.ABSOLUTE);
		reqHistFileList.setTop(300); reqHistFileList.setLeft(40); reqHistFileList.setWidth("180px"); reqHistFileList.setHeight("35px");
		reqHistFileList.setTextAlign(TextAlign.CENTER);
		reqHistFileList.setVerticalAlign(VerticalAlign.MIDDLE);
		reqHistFileList.setBackgroundColor(Color.GREY_LIGHTEN_2);
		reqHistFileList.setBorderRadius("3px");
		mpdetail.add(reqHistFileList);
		
		reqHistFile = new MaterialButton[5];
		reqHistFile[0] = new MaterialButton("다운로드"); reqHistFile[0].setFontSize("9px");
		reqHistFile[0].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[0].setTop(300); reqHistFile[0].setLeft(230); reqHistFile[0].setWidth("165px"); reqHistFile[0].setHeight("35px");
		reqHistFile[0].setVisible(false);
		reqHistFile[0].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[0].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[0].setOverflow(Overflow.HIDDEN);
		mpdetail.add(reqHistFile[0]);
		
		reqHistFile[1] = new MaterialButton("다운로드"); reqHistFile[1].setFontSize("9px");
		reqHistFile[1].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[1].setTop(300); reqHistFile[1].setLeft(400); reqHistFile[1].setWidth("165px"); reqHistFile[1].setHeight("35px");
		reqHistFile[1].setVisible(false);
		reqHistFile[1].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[1].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[1].setOverflow(Overflow.HIDDEN);
		mpdetail.add(reqHistFile[1]);
		
		reqHistFile[2] = new MaterialButton("다운로드"); reqHistFile[2].setFontSize("9px");
		reqHistFile[2].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[2].setTop(300); reqHistFile[2].setLeft(570); reqHistFile[2].setWidth("165px"); reqHistFile[2].setHeight("35px");
		reqHistFile[2].setVisible(false);
		reqHistFile[2].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[2].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[2].setOverflow(Overflow.HIDDEN);
		mpdetail.add(reqHistFile[2]);
		
		reqHistFile[3] = new MaterialButton("다운로드"); reqHistFile[3].setFontSize("9px");
		reqHistFile[3].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[3].setTop(300); reqHistFile[3].setLeft(740); reqHistFile[3].setWidth("165px"); reqHistFile[3].setHeight("35px");
		reqHistFile[3].setVisible(false);
		reqHistFile[3].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[3].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[3].setOverflow(Overflow.HIDDEN);
		mpdetail.add(reqHistFile[3]);
		
		reqHistFile[4] = new MaterialButton("다운로드"); reqHistFile[4].setFontSize("9px");
		reqHistFile[4].setLayoutPosition(Position.ABSOLUTE);
		reqHistFile[4].setTop(300); reqHistFile[4].setLeft(910); reqHistFile[4].setWidth("165px"); reqHistFile[4].setHeight("35px");
		reqHistFile[4].setVisible(false);
		reqHistFile[4].setFlexWrap(FlexWrap.WRAP_REVERSE);
		reqHistFile[4].setBackgroundColor(Color.GREY_LIGHTEN_1);
		reqHistFile[4].setOverflow(Overflow.HIDDEN);
		mpdetail.add(reqHistFile[4]);
		
		mpfile = new MaterialPanel();
		mpfile.setLayoutPosition(Position.ABSOLUTE);
		mpfile.setTop(360);	mpfile.setLeft(40);	mpfile.setWidth("1330px");
		mpdetail.add(mpfile);
		
		MaterialLabel mlsp2 = new MaterialLabel("파일등록내역");
		mlsp2.setLayoutPosition(Position.ABSOLUTE);
		mlsp2.setTop(0); mlsp2.setLeft(0); mlsp2.setWidth("1100px"); mlsp2.setHeight("10px");
		mlsp2.setFontSize(labelfontsize); mlsp2.setFontWeight(FontWeight.BOLD);
		mlsp2.setTextAlign(TextAlign.LEFT);
		mlsp2.setVerticalAlign(VerticalAlign.MIDDLE);
		mlsp2.setBackgroundColor(Color.GREY_LIGHTEN_2);
		mlsp2.setBorderRadius("3px");
		mpfile.add(mlsp2);
		
		txtFTitle = new MaterialTextBox();
		txtFTitle.setLayoutPosition(Position.ABSOLUTE);
		txtFTitle.setTop(30); txtFTitle.setLeft(0);	txtFTitle.setWidth("600px"); txtFTitle.setHeight(labelheight);
		txtFTitle.setFontSize(labelfontsize);
		txtFTitle.setBackgroundColor(Color.GREY_LIGHTEN_5);
		txtFTitle.setPaddingLeft(15);
		txtFTitle.setBorderRadius("1px");
		txtFTitle.setLabel("파일제목");
		mpfile.add(txtFTitle);
		
		txtFSUBTitle = new MaterialTextBox();
		txtFSUBTitle.setLayoutPosition(Position.ABSOLUTE);
		txtFSUBTitle.setTop(30); txtFSUBTitle.setLeft(700);	txtFSUBTitle.setWidth("400px"); txtFSUBTitle.setHeight(labelheight);
		txtFSUBTitle.setFontSize(labelfontsize);
		txtFSUBTitle.setBackgroundColor(Color.GREY_LIGHTEN_5);
		txtFSUBTitle.setPaddingLeft(15);
		txtFSUBTitle.setBorderRadius("1px");
		txtFSUBTitle.setLabel("소제목");
		mpfile.add(txtFSUBTitle);
		
		publisherTitle = new MaterialTextBox();
		publisherTitle.setLayoutPosition(Position.ABSOLUTE);
		publisherTitle.setTop(100); publisherTitle.setLeft(0);	publisherTitle.setWidth("400px"); publisherTitle.setHeight(labelheight);
		publisherTitle.setFontSize(labelfontsize);
		publisherTitle.setBackgroundColor(Color.GREY_LIGHTEN_5);
		publisherTitle.setPaddingLeft(15);
		publisherTitle.setBorderRadius("1px");
		publisherTitle.setLabel("제작처");
		mpfile.add(publisherTitle);
		
		publisherDate = new MaterialTextBox();
		publisherDate.setType(InputType.DATE);
		publisherDate.setLabel("발행일");
		publisherDate.setText("-");
		publisherDate.setLayoutPosition(Position.ABSOLUTE);
		publisherDate.setTop(100); publisherDate.setLeft(700); publisherDate.setWidth("140px");
		mpfile.add(publisherDate);
		
		txtLinkUrl = new MaterialTextBox();
		txtLinkUrl.setLabel("연결URL");
		txtLinkUrl.setLayoutPosition(Position.ABSOLUTE);
		txtLinkUrl.setTop(170); txtLinkUrl.setLeft(0); txtLinkUrl.setWidth("600px"); txtLinkUrl.setHeight(labelheight);
		txtLinkUrl.setFontSize(labelfontsize);
		txtLinkUrl.setBackgroundColor(Color.GREY_LIGHTEN_5);
		txtLinkUrl.setPaddingLeft(15);
		txtLinkUrl.setBorderRadius("1px");
		mpfile.add(txtLinkUrl);
		
		term = new MaterialTextBox();
		term.setLabel("홍보희망기간");
		term.setLayoutPosition(Position.ABSOLUTE);
		term.setTop(170); term.setLeft(700);	term.setWidth("400px");
		term.setFontWeight(FontWeight.BOLD);
		mpfile.add(term);
		
		txtFName = new MaterialTextBox();
		txtFName.setLabel("첨부파일");
		txtFName.setLayoutPosition(Position.ABSOLUTE);
		txtFName.setTop(240); txtFName.setLeft(0); txtFName.setWidth("400px"); txtFName.setHeight(labelheight);
		txtFName.setFontSize(labelfontsize);
		txtFName.setBackgroundColor(Color.GREY_LIGHTEN_5);
		txtFName.setPaddingLeft(15);
		txtFName.setBorderRadius("1px");
		mpfile.add(txtFName);
		btnFDN = new MaterialButton("다운로드"); 
		btnFDN.setLayoutPosition(Position.ABSOLUTE);
		btnFDN.setTop(260);	btnFDN.setLeft(400); btnFDN.setWidth("110px"); btnFDN.setHeight(labelheight);
		btnFDN.setBackgroundColor(Color.GREY_LIGHTEN_1);
		btnFDN.setFontSize("9px");
		btnFDN.setEnabled(false);
		mpfile.add(btnFDN);
		
		txtPDFName = new MaterialTextBox();
		txtPDFName.setLabel("PDF파일");
		txtPDFName.setLayoutPosition(Position.ABSOLUTE);
		txtPDFName.setTop(240);	txtPDFName.setLeft(580); txtPDFName.setWidth("400px"); txtPDFName.setHeight(labelheight);
		txtPDFName.setFontSize(labelfontsize);
		txtPDFName.setBackgroundColor(Color.GREY_LIGHTEN_5);
		txtPDFName.setPaddingLeft(15);
		txtPDFName.setBorderRadius("1px");
		mpfile.add(txtPDFName);
		btnPDFDN = new MaterialButton("다운로드"); btnPDFDN.setFontSize("9px");
		btnPDFDN.setLayoutPosition(Position.ABSOLUTE);
		btnPDFDN.setTop(260); btnPDFDN.setLeft(990); btnPDFDN.setWidth("110px"); btnPDFDN.setHeight(labelheight);
		btnPDFDN.setBackgroundColor(Color.GREY_LIGHTEN_1);
		btnPDFDN.setEnabled(false);
		mpfile.add(btnPDFDN);
		
		mpAnswerlist = new MaterialPanel();
		mpAnswerlist.setLayoutPosition(Position.ABSOLUTE);
		mpAnswerlist.setTop(700);
		mpAnswerlist.setLeft(40);
		mpAnswerlist.setWidth("1300px");
		mpdetail.add(mpAnswerlist);
		
		this.add(scrollpanel);
	}
	
	// 지킴이 메인글 리스트
	private void buildTableTab() {
		MaterialPanel mrtop = new MaterialPanel();
		mrtop.setWidth("100%");
		mrtop.setHeight("80px");
		mrtop.setPadding(10);
		
		category = new MaterialComboBox<>();
		category.setLabel("채널정보");
		category.setLayoutPosition(Position.ABSOLUTE);
		category.setTop(5);category.setLeft(10); category.setWidth("150px");
		mrtop.add(category);
		category.addItem("전체", -1);
		category.addItem("배너홍보",0);
		category.addItem("관광가이드북",1);
		category.addItem("기타문의",2);
		category.addValueChangeHandler(ee->{
			qryList(true);
		});
		category.setSelectedIndex(0);
		
		status = new MaterialComboBox<>();
		status.setLabel("처리상태");
		status.setLayoutPosition(Position.ABSOLUTE);
		status.setTop(5); status.setLeft(190); status.setWidth("120px");
		status.addItem("전체", -1);
		status.addItem("접수", 0);
		status.addItem("처리중", 1);
		status.addItem("승인", 2);
		status.addItem("처리완료", 3);
		status.addItem("반려", 4);
		status.setSelectedIndex(0);
		mrtop.add(status);
		status.addValueChangeHandler(e->{
			qryList(true);
		});
		cbidname = new MaterialComboBox<>();
		cbidname.setLayoutPosition(Position.ABSOLUTE);
		cbidname.setLabel("검색조건");
		cbidname.setTop(5);	cbidname.setLeft(340); cbidname.setWidth("150px");
		cbidname.addItem("신청제목", 0);
		cbidname.addItem("아이디", 1);
		cbidname.addItem("기관명", 2);
		mrtop.add(cbidname);
		
		edidname = new MaterialTextBox();
		edidname.setLabel("검색어입력");
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setTop(5);	edidname.setLeft(500); edidname.setWidth("500px");
		mrtop.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLabel("기간");
		sdate.setText("-");
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setTop(5); sdate.setRight(230); sdate.setWidth("140px");
		mrtop.add(sdate);
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(5); edate.setRight(80); edate.setWidth("140px");
		mrtop.add(edate);
		
		this.add(mrtop);
		mptable = new MaterialPanel();
		mptable.setWidth("100%"); mptable.setHeight("670px");
		mptable.setBackgroundColor(Color.WHITE);
		mptable.add(mrtop);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(40);table.setWidth("98.5%"); table.setHeight(595); table.setMargin(10);
		table.appendTitle("번호", 70, TextAlign.CENTER);
		table.appendTitle("아이디", 150, TextAlign.CENTER);
		table.appendTitle("기관명", 150, TextAlign.CENTER);
		table.appendTitle("분류", 150, TextAlign.CENTER);
		table.appendTitle("신청제목", 350, TextAlign.CENTER);
		table.appendTitle("등록일시", 150, TextAlign.CENTER);
		table.appendTitle("처리상태", 150, TextAlign.CENTER);
		table.appendTitle("댓글확인", 130, TextAlign.CENTER);
		table.appendTitle("처리일시", 150, TextAlign.CENTER);
		
		PartnersExcelButton = new MaterialButton("엑셀 다운로드");
		PartnersExcelButton.addClickHandler(event->{
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			downurl += "&select_type=PartnersChannel";
			Window.open(downurl,"_self", "enabled");
		});
		table.getButtomMenu().addButton(PartnersExcelButton, com.google.gwt.dom.client.Style.Float.LEFT);
		
		
		Order = 0;
		MaterialLabel UpdateLabel = (MaterialLabel)table.getHeader().getChildrenList().get(8);
		MaterialLabel CreateLabel = (MaterialLabel)table.getHeader().getChildrenList().get(5);
		CreateLabel.addClickHandler(event->{
			if(Order != 2) {
				Order = 2;
				CreateLabel.setText("등록일시↓");
				UpdateLabel.setText("처리일시");
			}else if(Order != 1){
				CreateLabel.setText("등록일시↑");
				UpdateLabel.setText("처리일시");
				Order = 1;
			}
			qryList(true);
		});
		
	
		UpdateLabel.addClickHandler(event->{
			if(Order != 4) {
				Order = 4;
				UpdateLabel.setText("처리일시↓");
				CreateLabel.setText("등록일시");
			}else if(Order != 3){
				UpdateLabel.setText("처리일시↑");
				CreateLabel.setText("등록일시");
				Order = 3;
			}
			qryList(true);
		});
		
		
		mptable.add(table);

		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		table.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event->{
			if((offset+20) >= totcnt) {
				MaterialToast.fireToast("더이상의 결과 값이 없습니다.", 3000);
			} else {
				qryList(false);
			}
		});
		table.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", com.google.gwt.dom.client.Style.Float.RIGHT);

		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, com.google.gwt.dom.client.Style.Float.RIGHT);
		this.add(mptable);
	}

	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0;
			index = 1;
			table.clearRows();
		} else offset += 20;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_PARTNERS_CHANNEL_LIST"));
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("sdate", new JSONString(strsdate));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("edate", new JSONString(stredate+" 23:59:59"));
		}
		String idname = edidname.getText();
		if(!"".equals(idname)) {
			if(cbidname.getSelectedIndex()== 0)
				parameterJSON.put("title", new JSONString(idname));
			else if(cbidname.getSelectedIndex() == 1)
				parameterJSON.put("id", new JSONString(idname));
			else if(cbidname.getSelectedIndex() == 2)
				parameterJSON.put("name", new JSONString(idname));
		}
		
		if(category.getSelectedIndex() > 0) {
			parameterJSON.put("ctype", new JSONString(category.getValues().get(category.getSelectedIndex())+""));
		}
		if(status.getSelectedIndex() > 0) {
			parameterJSON.put("status", new JSONString(status.getValues().get(status.getSelectedIndex())+""));
		}
		
		if(Order > 0) {
			parameterJSON.put("order", new JSONString(Order+""));
		}
		
		
		parameterJSON.put("offset", new JSONString(offset+""));
		table.loading(true);
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					JSONObject bodyResultcnt = (JSONObject) bodyObj.get("resultCnt");
					Console.log("bodyResultObj :" +bodyResultObj);
//					Console.log(" - rjsdsfdsf");
					countLabel.setText(bodyResultcnt.get("CNT")+" 건");
//					Console.log(" - 4532423");
					totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						String ctype = "[없음???]";
						switch((int)(obj.get("ctype")!=null?obj.get("ctype").isNumber().doubleValue():0)) {
						case 0: ctype = "베너홍보"; break;
						case 1: ctype = "관광가이드북"; break;
						case 2: ctype = "기타문의"; break;
						}
//						final String fctype = ctype;
						String sstatus = "접수";
						switch((int)(obj.get("status")!=null?obj.get("status").isNumber().doubleValue():0)) {
						case 0: sstatus = "접수"; break;
						case 1: sstatus = "처리중"; break;
						case 2: sstatus = "승인"; break;
						case 3: sstatus = "처리완료"; break;
						case 4: sstatus = "반려"; break;
						}
						ContentTableRow tableRow = table.addRow(Color.WHITE, new int[]{4}
								,""+(index++)
								,obj.get("id")!=null?obj.get("id").isString().stringValue():""
								,obj.get("name")!=null?obj.get("name").isString().stringValue():""
								,ctype
								,obj.get("title")!=null?obj.get("title").isString().stringValue():""
								,obj.get("cdate")!=null?obj.get("cdate").isString().stringValue():""
								,sstatus
								,obj.get("res").isString().stringValue().equals("")?"[답변완료]":obj.get("res").isString().stringValue()
								,obj.get("udate")!=null?obj.get("udate").isString().stringValue():""
								);
						
						tableRow.addClickHandler(e->{
							ContentTableRow ctr = (ContentTableRow)e.getSource();
							if(ctr.getSelectedColumn() == 4) {//지킴이 정보 상세 보기
								mptable.setVisible(false);
								mpdetail.setVisible(true);
								int ctypeindex = (int)obj.get("ctype").isNumber().doubleValue();
								edstatus.setSelectedIndex((int)obj.get("status").isNumber().doubleValue());
								
								if(ctypeindex == 0) { //베너홍보
									mpfile.setVisible(true);
									edposition.setVisible(true);
									publisherTitle.setVisible(false);
									publisherDate.setVisible(false);
									edposition.setSelectedIndex(obj.get("position")!=null?(int)obj.get("position").isNumber().doubleValue():0);
									mpAnswerlist.setTop(620);
									txtLinkUrl.setTop(100);
									term.setTop(100);
									txtFName.setTop(170);
									btnFDN.setTop(190);
									txtPDFName.setTop(170);
								} else if(ctypeindex == 1) { //관광가이드북
									mpfile.setVisible(true);
									edposition.setVisible(false);
									mpAnswerlist.setTop(700);
									publisherTitle.setVisible(true);
									publisherDate.setVisible(true);
									txtLinkUrl.setTop(170);
									term.setTop(170);
									txtFName.setTop(240);
									btnFDN.setTop(260);
									txtPDFName.setTop(240);
									
								} else { //기타 제휴 문의
									mpfile.setVisible(false);
									edposition.setVisible(false);
									mpAnswerlist.setTop(370);
									publisherTitle.setVisible(false);
									publisherDate.setVisible(false);
									txtLinkUrl.setTop(100);
									term.setTop(100);
									txtFName.setTop(170);
									btnFDN.setTop(190);
									txtPDFName.setTop(170);
								}
								typeTextBox.setText(
										ctypeindex == 0 ? "배너홍보" : ctypeindex == 1 ? "관광가이드북" : ctypeindex == 2 ? "기타문의" : "-"
										);
								edstatus.setSelectedIndex((int)obj.get("status").isNumber().doubleValue());
								mttitle.setText(obj.get("title")!=null?obj.get("title").isString().stringValue():"");
								mtbody.setText(obj.get("body")!=null?obj.get("body").isString().stringValue():"");
								mtchum.setText(obj.get("downname")!=null?obj.get("downname").isString().stringValue():"");
								final String fileUrl = obj.get("url")!=null?obj.get("url").isString().stringValue():"";
								String tt = mtchum.getText().trim();
								final String ext = tt.substring(tt.lastIndexOf("."));
								
								if(fileUrl.equals("")){
									btnDN.setBackgroundColor(Color.GREY);
									btnDN.setEnabled(false);
								} else {
									btnDN.setEnabled(true);
									btnDN.setBackgroundColor(Color.RED);
									btnDN.getHandlerRegistry().clearHandlers();
									btnDN.getHandlerRegistry().registerHandler(
									btnDN.addClickHandler(ee->{
										if(fileUrl.lastIndexOf(".") > -1) {
											Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl,"","");
										} else { 
											Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl+ext,"","");
										}
									}));
								}
								txtFTitle.setText(obj.get("ftitle")!=null?obj.get("ftitle").isString().stringValue():"");
								txtFSUBTitle.setText(obj.get("fsubtitle")!=null?obj.get("fsubtitle").isString().stringValue():"");
								txtLinkUrl.setText(obj.get("linkurl")!=null?obj.get("linkurl").isString().stringValue():"");
								txtFName.setText(obj.get("fdownname")!=null?obj.get("fdownname").isString().stringValue():"");
								publisherTitle.setText(obj.get("publ")!=null?obj.get("publ").isString().stringValue():"");
								publisherDate.setText(obj.get("publdate")!=null?obj.get("publdate").isString().stringValue():"-");
								
								final String fileUrl1 = obj.get("fdownurl")!=null?obj.get("fdownurl").isString().stringValue():"";
								tt = txtFName.getText().trim();
								final String ext2 = tt.substring(tt.lastIndexOf("."));
								if(fileUrl1.equals("")) {
									btnFDN.setBackgroundColor(Color.GREY);
									btnFDN.setEnabled(false);
								} else {
									btnFDN.setEnabled(true);
									btnFDN.setBackgroundColor(Color.RED);
									btnFDN.getHandlerRegistry().clearHandlers();
									btnFDN.getHandlerRegistry().registerHandler(
									btnFDN.addClickHandler(ee->{
										if(fileUrl1.lastIndexOf(".") > -1)
											Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl1,"","");
										else 
											Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl1+ext2,"","");
									}));
								}
								txtPDFName.setText(obj.get("pdfname")!=null?obj.get("pdfname").isString().stringValue():"");
								final String fileUrl2 = obj.get("pdfurl")!=null?obj.get("pdfurl").isString().stringValue():"";
								tt = txtPDFName.getText().trim();
								final String ext3 = tt.substring(tt.lastIndexOf("."));
								if(fileUrl2.equals("")) {
									btnPDFDN.setBackgroundColor(Color.GREY);
									btnPDFDN.setEnabled(false);
								} else {
									btnPDFDN.setBackgroundColor(Color.RED);
									btnPDFDN.setEnabled(true);
									btnPDFDN.getHandlerRegistry().clearHandlers();
									btnPDFDN.getHandlerRegistry().registerHandler(
									btnPDFDN.addClickHandler(ee->{
										if(fileUrl2.lastIndexOf(".") > -1)
											Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl2,"","");
										else 
											Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl2+ext3,"","");
									}));
								}
								String st =obj.get("sdate")!=null?obj.get("sdate").isString().stringValue().split(" ")[0]:"";
								String et =obj.get("edate")!=null?obj.get("edate").isString().stringValue().split(" ")[0]:"";
								if(!st.equals("")) {
									st = st.split("-")[0] +"년 " + st.split("-")[1] +"월 " + st.split("-")[2] +"일";
								}
								if(!et.equals("")) {
									et = et.split("-")[0] +"년 " + et.split("-")[1] +"월 " + et.split("-")[2] +"일";
								}
								term.setText(st+"  ~  "	+et);
								pahid = obj.get("pahid").isString().stringValue();
								buildAnswerList(pahid);
//							} else if(ctr.getSelectedColumn() == 4) {// 회원 화면 이동
//					        	MaterialLink tgrLink = new MaterialLink("회원관리");
//					        	tgrLink.setIconType(IconType.CARD_MEMBERSHIP);
//					        	tgrLink.setWaves(WavesType.DEFAULT);
//					        	tgrLink.setIconPosition(IconPosition.LEFT);
//					        	tgrLink.setTextColor(Color.BLUE);
//								WindowParamter winParam = new WindowParamter(tgrLink, ApplicationView.WINDOW_MEMBER, "회원관리", 1500, 700);
//								Map<String, Object> paramMap = new HashMap<String, Object>();
//				    			paramMap.put("SNS_ID", obj.get("snsid").isString().stringValue());
//				    			winParam.setParams(paramMap);
//								Registry.put("TARGET_LINK", winParam);
//			        			appview.getAppView().openTargetWindow(paramMap);
								onLoadMultiFileUpload(obj.get("pahid") != null ? obj.get("pahid").isString().stringValue() : "");
							}
						});
					}
					
				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}
	
	private int answerListHeight = 0;
	private void buildAnswerList(String pahid) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_PARTNERS_CHANNEL_ANSWER_LIST"));
		parameterJSON.put("pahid", new JSONString(pahid));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					mpAnswerlist.clear();
					MaterialLabel mlsp3 = new MaterialLabel("댓글 관리");
//					mlsp3.setLayoutPosition(Position.ABSOLUTE);
//					mlsp3.setTop(0); mlsp3.setLeft(0); 
					mlsp3.setWidth("1100px"); mlsp3.setHeight("10px");
					mlsp3.setFontSize("16px"); mlsp3.setFontWeight(FontWeight.BOLD);
					mlsp3.setTextAlign(TextAlign.LEFT);
					mlsp3.setVerticalAlign(VerticalAlign.MIDDLE);
					mlsp3.setBackgroundColor(Color.GREY_LIGHTEN_2);
					mlsp3.setBorderRadius("3px");
					mpAnswerlist.add(mlsp3);
					answerListHeight = 0;
					mpAnswerlist.setHeight(answerListHeight+"px");
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
//					JSONArray bodyResultEtc = (JSONArray) bodyObj.get("resultEtc");
					
//					if(bodyResultEtc.size() > 0) {
//						JSONObject obj = (JSONObject)bodyResultEtc.get(0);
//						mtchum.setText(obj.get("title")!=null?obj.get("title").isString().stringValue():"");
//						String fileUrl = obj.get("lnfileurl")!=null?obj.get("lnfileurl").isString().stringValue():"";
//						btnDN.setEnabled(true);
//						btnDN.addClickHandler(e->{
//							if(fileUrl.lastIndexOf(".") > -1)
//								Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl,"","");
//							else 
//								Window.open((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" +fileUrl,"","");
//						});
//					} else {
//						mtchum.setText("");
//						btnDN.setEnabled(false);
//					}
					
					int usrCnt = bodyResultObj.size();
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						MaterialRow mrq = new MaterialRow();
						mrq.setLayoutPosition(Position.RELATIVE);
						MaterialImage mlq = new MaterialImage();
						mlq.setLayoutPosition(Position.ABSOLUTE);
						mlq.setLeft(10); mlq.setTop(10); mlq.setWidth("90px"); mlq.setHeight("90px");
						mlq.setBorderRadius("2px");
						mlq.setPixelSize(90, 90);
						mrq.add(mlq);
						if(obj.get("imgpath") == null) {
							if(obj.get("usrid") == null)
								mlq.setValue((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=df2b1934-d39e-455f-8f91-1ad9915da901");
							else mlq.setValue((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=df2b1934-d39e-455f-8f91-1ad9915da901");
						} else mlq.setValue(obj.get("imgpath").isString().stringValue());
						
						MaterialTextArea mtaq = new MaterialTextArea();
						mtaq.setLayoutPosition(Position.ABSOLUTE);
						mtaq.setLeft(130); mtaq.setWidth("890px");
						mrq.add(mtaq);
						MaterialButton mbq = new MaterialButton("삭제");
						mbq.setLayoutPosition(Position.ABSOLUTE);
						mbq.setTop(15); mbq.setLeft(1030); mbq.setWidth("100px"); mbq.setHeight("85px");
						mbq.addClickHandler(e-> {
							getMaterialExtentsWindow().confirm("정말 삭제 하겠습니까?", 500, e2-> {
								if(((MaterialButton)e2.getSource()).getId().equals("yes")) {
									JSONObject parameterJSON = new JSONObject();
									parameterJSON.put("cmd", new JSONString("DELETE_PARTNERS_CHANNEL_ANSWER"));
									parameterJSON.put("pahid", new JSONString(obj.get("pahid").isString().stringValue()));
									VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
										@Override
										public void call(Object param1, String param2, Object param3) {
											JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
											JSONObject headerObj = (JSONObject) resultObj.get("header");
											String processResult = headerObj.get("process").isString().stringValue();
											if (processResult.equals("success")) {
												getMaterialExtentsWindow().alert("삭제 되었습니다.", 500);
												buildAnswerList(pahid);
											} else {
												getMaterialExtentsWindow().alert("삭제에 실패했습니다. 관리자에게 문의하세요.", 500);
											}
										}
									});
								
								} 
//								else
//									Console.log("NO");
							});
						});
						mrq.add(mbq);
						mtaq.setText(obj.get("body")!=null?obj.get("body").isString().stringValue():"");
						if(i == 0) 
							mrq.setMarginTop(answerListHeight+15);
						else mrq.setMarginTop(answerListHeight);
						mpAnswerlist.add(mrq);
						if(answerListHeight == 0)
							answerListHeight+=110;
					}
					MaterialRow mrAnswer = new MaterialRow();
					mrAnswer.setLayoutPosition(Position.RELATIVE);
					MaterialTextArea mtaAnswer = new MaterialTextArea();
					mtaAnswer.setLabel("댓글달기");
					mtaAnswer.setLayoutPosition(Position.ABSOLUTE);
					mtaAnswer.setLeft(10); mtaAnswer.setWidth("1000px");
					mrAnswer.add(mtaAnswer);
					MaterialButton mbAnswer = new MaterialButton("답변달기");
					mbAnswer.setLayoutPosition(Position.ABSOLUTE);
					mbAnswer.setTop(15); mbAnswer.setLeft(1030); mbAnswer.setWidth("100px"); mbAnswer.setHeight("85px");
					mrAnswer.add(mbAnswer);
					mbAnswer.addClickHandler(e->{
						if(mtaAnswer.getText().trim().equals("")) {
							getMaterialExtentsWindow().alert("댓글 내용을 입력해 주세요.", 500);
							return;
						}
						JSONObject parameterJSON = new JSONObject();
						parameterJSON.put("cmd", new JSONString("INSERT_PARTNERS_CHANNEL_ANSWER"));
						parameterJSON.put("pahid", new JSONString(IDUtil.uuid()));
						parameterJSON.put("parentid", new JSONString(pahid));
						
						parameterJSON.put("usrid", new JSONString(Registry.getUserId()));
						parameterJSON.put("body", new JSONString(mtaAnswer.getText()));
						VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
							@Override
							public void call(Object param1, String param2, Object param3) {
								JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
								JSONObject headerObj = (JSONObject) resultObj.get("header");
								String processResult = headerObj.get("process").isString().stringValue();
								if (processResult.equals("success")) {
									buildAnswerList(pahid);
								} else {
									getMaterialExtentsWindow().alert("답변달기에 실패했습니다. 관리자에게 문의하세요.", 500);
								}
							}
						});
					});
					if(answerListHeight == 0) answerListHeight = 10;
					mrAnswer.setMarginTop(answerListHeight);
					mpAnswerlist.add(mrAnswer);

				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			}
		});
	}
}
