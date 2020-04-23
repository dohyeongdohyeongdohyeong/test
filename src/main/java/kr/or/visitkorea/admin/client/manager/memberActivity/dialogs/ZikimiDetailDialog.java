package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;


import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
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
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextArea.ResizeRule;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.ScrollContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ZikimiDetailDialog extends DialogContent {
	protected MemberActivityMain host;
	private MaterialLabel dialogTitle, mlchum;
	private String zikid;
	private ScrollContentPanel mpdetail;
	private MaterialTextArea mtbody;
	private MaterialButton mturlsave;
	private MaterialTextBox mtno, mttitle, mtid, mtdate, mtcategory, mtldate, mtreqtype, mtzikimtitle, mtzikimurl;
	private MaterialButton mbprev, mbAnswer, mbq;
	private MaterialRow mrAnswer;
	private String reqType;
	private int reqTypeVal, viewType;	
	private MaterialPanel mpAnswerlist;
	private ScrollPanel scrollpanel;
	private MaterialButton[] mbchum;
	private int answerListHeight = 0;
	
	private MaterialExtentsWindow window;
	private MaterialComboBox<Object> usertype, category, status, edstatus;
	private SelectionPanel selisuse;
	
	public ZikimiDetailDialog(MaterialExtentsWindow window, MemberActivityMain host) {
		super(window);
		this.host = host;
	}

	@Override
	public void init() {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.clear();
		buildContent();
		
		viewType = Integer.valueOf(this.getParameters().get("viewType").toString());
		reqType = this.getParameters().get("reqType").toString();
		reqTypeVal = this.getParameters().get("reqType").toString().equals("신규등록") ? 1 : 0;
		
		if (viewType == 9 && Registry.getPermission("d53aae61-5f18-42be-aa25-47c876a1578c")) {
			mbprev.setEnabled(true);
		}
		if (viewType == 10 && Registry.getPermission("10f33077-465c-4f8a-a59d-bb67031eb81b")) {
			mbprev.setEnabled(true);
		}
		if (viewType == 9 && Registry.getPermission("11ee4345-4b81-433d-970d-99dea45f00f7")) {
			edstatus.setEnabled(true);
		}
		if (viewType == 10 && Registry.getPermission("2ab91b5c-f8b2-4489-94b9-7d38a5f16931")) {
			edstatus.setEnabled(true);
		}
		if (reqTypeVal == 1 && ((viewType == 9 && Registry.getPermission("cf896fee-4941-4476-8f93-cdbe6d132b68")) ||
				(viewType == 10 && Registry.getPermission("82488900-6997-4613-9ced-7d7744c3f972"))))
			mturlsave.setEnabled(true);
		
		if (reqTypeVal == 0) {
			zikid = this.getParameters().get("zikid").toString();
			mtno.setText(this.getParameters().get("utype").toString());
			mtcategory.setText(this.getParameters().get("fctype").toString());
			mttitle.setText(this.getParameters().get("title").toString());
			mtid.setText(this.getParameters().get("id").toString());
			mtreqtype.setText(this.getParameters().get("reqType").toString());
			mtdate.setText(this.getParameters().get("cdate").toString());
			mtldate.setText(this.getParameters().get("udate").toString());
			mtbody.setLabel("신고내용");
			
			if (reqType.equals("구석피디아1") ||reqType.equals("구석피디아2") ||reqType.equals("구석피디아3")) {
				
				String body = this.getParameters().get("body").toString();
				
				String[] bodys = body.split("\\|\\|");
				Console.log(""+bodys.length);
				Console.log("if :" +body);
				
				body = "";
				
				if(bodys.length > 0) body += "전화번호 : " + bodys[0] + "\r\n";
				if(bodys.length > 1) body += "주소 : " + bodys[1] + "\r\n";
				if(bodys.length > 2) body += "홈페이지 : " + bodys[2] + "\r\n";
				if(bodys.length > 3) body += "캐치프레이즈 : " + bodys[3] + "\r\n";
				if(bodys.length > 4) body += "기타 : " + bodys[4] + "\r\n";
			
				
				body = body.replaceAll("&#63;", "?");
				mtbody.setText(body);
			}
			else {
				String body = this.getParameters().get("body").toString();
				body = body.replaceAll("&#63;", "?");
				mtbody.setText(body);
			}
			edstatus.setSelectedIndex(Integer.parseInt(this.getParameters().get("status").toString()));
						
			buildAnswerList(zikid);
		} else {
			zikid = this.getParameters().get("zikid").toString();
			mtno.setText(this.getParameters().get("utype").toString());
			mtcategory.setText(this.getParameters().get("fctype").toString());
			mttitle.setText(this.getParameters().get("title").toString());
			mtid.setText(this.getParameters().get("id").toString());
			mtreqtype.setText(this.getParameters().get("reqType").toString());
			mtdate.setText(this.getParameters().get("cdate").toString());
			mtldate.setText(this.getParameters().get("udate").toString());
			mtzikimtitle.setText(this.getParameters().get("reqtitle").toString());
			mtbody.setLabel("상세내용");
			String body = this.getParameters().get("body").toString();
			body = body.replaceAll("&#63;", "?");
			mtbody.setText(body);
			mtzikimurl.setText(this.getParameters().get("requrl").toString());
			edstatus.setSelectedIndex(Integer.parseInt(this.getParameters().get("status").toString()));
			
			mtbody.setTop(260);
			mlchum.setTop(410);
			mbchum[0].setTop(410);
			mbchum[1].setTop(410);
			mbchum[2].setTop(410);
			mbchum[3].setTop(410);
			mbchum[4].setTop(410);
			
			mpAnswerlist.setTop(540);
			buildAnswerList(zikid);
			
			this.mpdetail.add(mtzikimtitle);
			this.mpdetail.add(mtzikimurl);
			this.mpdetail.add(mturlsave);
			this.mpdetail.add(mpAnswerlist);
		}
		
		if (reqTypeVal == 1 && viewType == 10)
			mttitle.setLabel("제목");
		if (viewType == 10)
			mtreqtype.setLabel("신청구분");
	}
	
	public void buildContent() {
		// dialog title define
		dialogTitle = new MaterialLabel("관광정보지킴이 댓글관리");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		buildDetailTab();
	}
	
	// QnA상세 및 답변글 목록
	private void buildDetailTab() {
		scrollpanel = new ScrollPanel();
		mpdetail = new ScrollContentPanel(this);
		mpdetail.setWidth("100%");
		scrollpanel.setHeight("545px");
		mpdetail.setBackgroundColor(Color.WHITE);
		mpdetail.setOverflow(Overflow.HIDDEN);
		
		scrollpanel.add(mpdetail);
		mbprev = new MaterialButton("목록이동");
		mbprev.setLayoutPosition(Position.ABSOLUTE);
		mbprev.setBackgroundColor(Color.GREY_DARKEN_1);
		mbprev.setTop(10); mbprev.setRight(15);
		mbprev.addClickHandler(e->{
			getMaterialExtentsWindow().closeDialog();
		});
		mbprev.setEnabled(false);
		mpdetail.add(mbprev);
		
		String labelfontsize = "18px";
		int sleft = 15;
		mtno = new MaterialTextBox();
		mtno.setLabel("사용자");
		mtno.setLayoutPosition(Position.ABSOLUTE);
		mtno.setTop(20); mtno.setLeft(sleft); mtno.setWidth("400px"); mtno.setHeight("35px");
		mtno.setPaddingLeft(15);
		mtno.setReadOnly(true);
		mtno.setFontSize(labelfontsize);
		mpdetail.add(mtno);
		
		mtcategory = new MaterialTextBox();
		mtcategory.setLabel("카테고리");
		mtcategory.setLayoutPosition(Position.ABSOLUTE);
		mtcategory.setTop(20); mtcategory.setLeft(750); mtcategory.setWidth("150px"); mtcategory.setHeight("35px");
		mtcategory.setFontSize(labelfontsize);
		mtcategory.setPaddingLeft(15);
		mtcategory.setReadOnly(true);
		mpdetail.add(mtcategory);
		
		mttitle = new MaterialTextBox();
		mttitle.setLabel("컨텐츠명");
		mttitle.setLayoutPosition(Position.ABSOLUTE);
		mttitle.setTop(80);	mttitle.setLeft(sleft); mttitle.setWidth("700px");	mttitle.setHeight("35px");
		mttitle.setPaddingLeft(15);
		mttitle.setReadOnly(true);
		mttitle.setFontSize(labelfontsize);
		mpdetail.add(mttitle);
		
		mtreqtype = new MaterialTextBox();
		mtreqtype.setLabel("구분");
		mtreqtype.setLayoutPosition(Position.ABSOLUTE);
		mtreqtype.setTop(80); mtreqtype.setLeft(500); mtreqtype.setWidth("700px"); mtreqtype.setHeight("35px");
		mtreqtype.setPaddingLeft(15);
		mtreqtype.setReadOnly(true);
		mtreqtype.setFontSize(labelfontsize);
		mpdetail.add(mtreqtype);
		
		mtid = new MaterialTextBox();
		mtid.setLabel("작성자");
		mtid.setLayoutPosition(Position.ABSOLUTE);
		mtid.setTop(80); mtid.setLeft(750); mtid.setWidth("200px"); mtid.setHeight("35px");
		mtid.setPaddingLeft(15);
		mtid.setReadOnly(true);
		mtid.setFontSize(labelfontsize);
		mpdetail.add(mtid);
		
		mtdate = new MaterialTextBox();
		mtdate.setLabel("등록일시");
		mtdate.setLayoutPosition(Position.ABSOLUTE);
		mtdate.setTop(140); mtdate.setLeft(sleft); mtdate.setWidth("300px"); mtdate.setHeight("35px");
		mtdate.setPaddingLeft(15);
		mtdate.setReadOnly(true);
		mtdate.setFontSize(labelfontsize);
		mpdetail.add(mtdate);
		
		edstatus = new MaterialComboBox<Object>();
		edstatus.setLabel("처리상태");
		edstatus.setLayoutPosition(Position.ABSOLUTE);
		edstatus.addItem("접수", 0);
		edstatus.addItem("처리중", 1);
		edstatus.addItem("승인완료", 2);
		edstatus.addItem("승인거절", 3);
		edstatus.setTop(140); edstatus.setLeft(500); edstatus.setWidth("200px"); edstatus.setHeight("35px");
		edstatus.setFontSize(labelfontsize);
		edstatus.setPaddingLeft(15);
		edstatus.setEnabled(false);
		mpdetail.add(edstatus);
		edstatus.addValueChangeHandler(ee->{
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_ZIKIMI"));
			parameterJSON.put("zikid", new JSONString(zikid));
			parameterJSON.put("status", new JSONString(edstatus.getSelectedIndex()+""));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
							buildAnswerList(zikid);
							
							JSONObject obj = (JSONObject) getParameters().get("obj");
							obj.put("status", new JSONNumber(edstatus.getSelectedIndex()));
							
							ContentTableRow row = (ContentTableRow) getParameters().get("row");
							MaterialLabel rowUpdateStatus = (MaterialLabel) row.getColumnObject(7);
							rowUpdateStatus.setText(
									edstatus.getSelectedValue().get(0).toString().equals("0") ? "접수" 
											: edstatus.getSelectedValue().get(0).toString().equals("1") ? "처리중" 
											: edstatus.getSelectedValue().get(0).toString().equals("2") ? "승인완료"
											: edstatus.getSelectedValue().get(0).toString().equals("3") ? "승인거절" : "-"
									);
					} else {
						host.getMaterialExtentsWindow().alert("상태변경에 실패했습니다. 관리자에게 문의하세요.", 500);
					}
				}
			});
		});
		
		mtldate = new MaterialTextBox();
		mtldate.setLabel("처리일시");
		mtldate.setLayoutPosition(Position.ABSOLUTE);
		mtldate.setTop(140); mtldate.setLeft(750); mtldate.setWidth("300px"); mtldate.setHeight("35px");
		mtldate.setPaddingLeft(15);
		mtldate.setReadOnly(true);
		mpdetail.add(mtldate);
		
		mtzikimtitle = new MaterialTextBox();
		mtzikimtitle.setLabel("제목");
		mtzikimtitle.setLayoutPosition(Position.ABSOLUTE);
		mtzikimtitle.setTop(200); mtzikimtitle.setLeft(sleft); mtzikimtitle.setWidth("400px"); mtzikimtitle.setHeight("35px");
		mtzikimtitle.setPaddingLeft(15);
		mtzikimtitle.setReadOnly(true);
		mtzikimtitle.setFontSize(labelfontsize);
		
		mtbody = new MaterialTextArea();
		mtbody.setLayoutPosition(Position.ABSOLUTE);
		mtbody.setTop(200);	mtbody.setLeft(sleft); mtbody.setWidth("1250px"); mtbody.setHeight("130px");
		mtbody.setFontSize(labelfontsize);
		mtbody.setBackgroundColor(Color.GREY_LIGHTEN_5);
		mtbody.setBorderRadius("2px");
		mtbody.setPaddingLeft(15);
		mtbody.setReadOnly(true);
		mtbody.setFontSize(labelfontsize);
		mtbody.getChildrenList().get(0).getElement().getStyle().setPadding(5, Unit.PX);
		mtbody.getChildrenList().get(0).getElement().getStyle().setTop(0, Unit.PX);
		mtbody.getChildrenList().get(0).getElement().getStyle().setHeight(115, Unit.PX);
		mtbody.getChildrenList().get(0).getElement().getStyle().setOverflow(Overflow.AUTO);
		mpdetail.add(mtbody);
		
		mlchum = new MaterialLabel("첨부파일");
		mlchum.setLayoutPosition(Position.ABSOLUTE);
		mlchum.setTop(350);	mlchum.setLeft(sleft); mlchum.setWidth("180px"); mlchum.setHeight("35px");
		mlchum.setTextAlign(TextAlign.CENTER);
		mlchum.setVerticalAlign(VerticalAlign.MIDDLE);
		mlchum.setBackgroundColor(Color.GREY_LIGHTEN_2);
		mlchum.setBorderRadius("3px");
		mpdetail.add(mlchum);
		mbchum = new MaterialButton[5];
		mbchum[0] = new MaterialButton("다운로드"); mbchum[0].setFontSize("9px");
		mbchum[0].setLayoutPosition(Position.ABSOLUTE);
		mbchum[0].setTop(350); mbchum[0].setLeft(200); mbchum[0].setWidth("165px"); mbchum[0].setHeight("35px");
		mbchum[0].setVisible(false);
		mbchum[0].setFlexWrap(FlexWrap.WRAP_REVERSE);
		mbchum[0].setBackgroundColor(Color.GREY_LIGHTEN_1);
		mbchum[0].setOverflow(Overflow.HIDDEN);
		mpdetail.add(mbchum[0]);
		mbchum[1] = new MaterialButton("다운로드"); mbchum[1].setFontSize("9px");
		mbchum[1].setLayoutPosition(Position.ABSOLUTE);
		mbchum[1].setTop(350); mbchum[1].setLeft(370); mbchum[1].setWidth("165px");mbchum[1].setHeight("35px");
		mbchum[1].setVisible(false);
		mbchum[1].setFlexWrap(FlexWrap.WRAP_REVERSE);
		mbchum[1].setBackgroundColor(Color.GREY_LIGHTEN_1);
		mbchum[1].setOverflow(Overflow.HIDDEN);
		mpdetail.add(mbchum[1]);
		mbchum[2] = new MaterialButton("다운로드");mbchum[2].setFontSize("9px");
		mbchum[2].setLayoutPosition(Position.ABSOLUTE);
		mbchum[2].setTop(350); mbchum[2].setLeft(540); mbchum[2].setWidth("165px");mbchum[2].setHeight("35px");
		mbchum[2].setVisible(false);
		mbchum[2].setFlexWrap(FlexWrap.WRAP_REVERSE);
		mbchum[2].setBackgroundColor(Color.GREY_LIGHTEN_1);
		mbchum[2].setOverflow(Overflow.HIDDEN);
		mpdetail.add(mbchum[2]);
		mbchum[3] = new MaterialButton("다운로드");mbchum[3].setFontSize("9px");
		mbchum[3].setLayoutPosition(Position.ABSOLUTE);
		mbchum[3].setTop(350); mbchum[3].setLeft(710); mbchum[3].setWidth("165px");mbchum[3].setHeight("35px");
		mbchum[3].setVisible(false);
		mbchum[3].setFlexWrap(FlexWrap.WRAP_REVERSE);
		mbchum[3].setBackgroundColor(Color.GREY_LIGHTEN_1);
		mbchum[3].setOverflow(Overflow.HIDDEN);
		mpdetail.add(mbchum[3]);
		mbchum[4] = new MaterialButton("다운로드");mbchum[4].setFontSize("9px");
		mbchum[4].setLayoutPosition(Position.ABSOLUTE);
		mbchum[4].setTop(350); mbchum[4].setLeft(880); mbchum[4].setWidth("165px");mbchum[4].setHeight("35px");
		mbchum[4].setVisible(false);
		mbchum[4].setFlexWrap(FlexWrap.WRAP_REVERSE);
		mbchum[4].setBackgroundColor(Color.GREY_LIGHTEN_1);
		mbchum[4].setOverflow(Overflow.HIDDEN);
		mpdetail.add(mbchum[4]);
		
		mtzikimurl = new MaterialTextBox();
		mtzikimurl.setLabel("반영화면 URL");
		mtzikimurl.setLayoutPosition(Position.ABSOLUTE);
		mtzikimurl.setTop(460); mtzikimurl.setLeft(sleft); mtzikimurl.setWidth("1100px"); mtzikimurl.setHeight("35px");
		mtzikimurl.setPaddingLeft(15);
		mtzikimurl.setFontSize(labelfontsize);
//		mtzikimurl.addKeyUpHandler(event -> {
//			if (event.getNativeKeyCode() == 13) {
//				JSONObject parameterJSON = new JSONObject();
//				parameterJSON.put("cmd", new JSONString("UPDATE_ZIKIMI_REQURL"));
//				parameterJSON.put("zikId", new JSONString(zikid));
//				parameterJSON.put("requrl", new JSONString(mtzikimurl.getText()));
//				
//				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
//					@Override
//					public void call(Object param1, String param2, Object param3) {
//						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
//						JSONObject headerObj = (JSONObject) resultObj.get("header");
//						String processResult = headerObj.get("process").isString().stringValue();
//						if (processResult.equals("success")) {
//							
//						} else {
//							host.getMaterialExtentsWindow().alert("상태변경에 실패했습니다. 관리자에게 문의하세요.", 500);
//						}
//					}
//				});
//			} 
//		});
		
		mturlsave = new MaterialButton("URL 저장");
		mturlsave.setLayoutPosition(Position.ABSOLUTE);
		mturlsave.setBackgroundColor(Color.GREEN_LIGHTEN_2);
		mturlsave.setTop(490); mturlsave.setRight(15);
//		mturlsave.setEnabled(false);
		mturlsave.addClickHandler(e->{
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_ZIKIMI_REQURL"));
			parameterJSON.put("zikId", new JSONString(zikid));
			parameterJSON.put("requrl", new JSONString(mtzikimurl.getText()));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject obj = (JSONObject) getParameters().get("obj");
						obj.put("requrl",  new JSONString(mtzikimurl.getText()));
						MaterialToast.fireToast("처리 되었습니다.");
					} else {
						MaterialToast.fireToast("상태변경에 실패했습니다. 관리자에게 문의하세요.");
					}
				}
			});
		});
		
		mpAnswerlist = new MaterialPanel();
		mpAnswerlist.setLayoutPosition(Position.ABSOLUTE);
		mpAnswerlist.setTop(400);
		mpAnswerlist.setLeft(sleft);
		mpAnswerlist.setWidth("1260px");
		mpAnswerlist.setBackgroundColor(Color.PINK);
		mpdetail.add(mpAnswerlist);
		this.add(scrollpanel);
	}
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}
	
	private void buildAnswerList(String zikid) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_ZIKIMI_ANSWER_LIST"));
		parameterJSON.put("zikid", new JSONString(zikid));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					mpAnswerlist.clear();
					answerListHeight = 0;
					mpAnswerlist.setHeight(answerListHeight+"px");
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					JSONArray bodyResultEtc = (JSONArray) bodyObj.get("resultEtc");
					
					for(int i = 0;i<5;i++) {
						mbchum[i].setVisible(false);
					}
					
					for(int i = 0;i<bodyResultEtc.size();i++) {
						
						
						JSONObject obj = (JSONObject)bodyResultEtc.get(i);
						Console.log(obj+"");
						Console.log(obj.get("title").isString().stringValue());
						Console.log(bodyResultEtc.size()+"사이즈");
						
						mbchum[i].setText(obj.get("title")!=null?obj.get("title").isString().stringValue():"");
						final String fileUrl = obj.get("lnfileurl")!=null?obj.get("lnfileurl").isString().stringValue():"";
						mbchum[i].setVisible(true);
						mbchum[i].getHandlerRegistry().clearHandlers();
						mbchum[i].getHandlerRegistry().registerHandler(
						mbchum[i].addClickHandler(e->{
							if(fileUrl.lastIndexOf(".") > -1){
								
								Console.log((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl);
								Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl,"","");
							}
							else {
								
								Console.log((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl);
								Window.open((String) Registry.get("image.server") + "/img/call?cmd=VIEW&mode=raw&id=" +fileUrl,"","");
							}
						}));
						
						if ((viewType == 9 && Registry.getPermission("eb4edbb2-44db-49ac-ba70-c6ee7ebdc39e")) ||
								(viewType != 9 && Registry.getPermission("d0645c4c-d076-4320-9497-e801a1f91c10"))) {
							mbchum[0].setEnabled(true);
							mbchum[1].setEnabled(true);
							mbchum[2].setEnabled(true);
							mbchum[3].setEnabled(true);
							mbchum[4].setEnabled(true);
						} else {
							mbchum[0].setEnabled(false);
							mbchum[1].setEnabled(false);
							mbchum[2].setEnabled(false);
							mbchum[3].setEnabled(false);
							mbchum[4].setEnabled(false);
						}
					}
					
					int usrCnt = bodyResultObj.size();
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						MaterialRow mrq = new MaterialRow();
						mrq.setLayoutPosition(Position.RELATIVE);
						mrq.setMarginBottom(0);
						MaterialColumn ImageColumn = new MaterialColumn();
						ImageColumn.setGrid("s1");
						ImageColumn.setPadding(0);
						ImageColumn.setTextAlign(TextAlign.CENTER);
						ImageColumn.setHeight("150px");
						ImageColumn.setLineHeight(230);
						MaterialImage mlq = new MaterialImage();
						mlq.setLeft(10); mlq.setTop(10); mlq.setWidth("90px"); mlq.setHeight("90px");
						mlq.setBorderRadius("2px");
						mlq.setPixelSize(90, 90);
						ImageColumn.add(mlq);
						mrq.add(ImageColumn);
						if(obj.get("imgpath") == null) {
							if(obj.get("usrid") == null)
								mlq.setUrl("https://ssl.pstatic.net/static/pwe/address/img_profile.png");
							else mlq.setUrl(GWT.getHostPageBaseURL() + "images/adminanswerlogo.png");
						} else mlq.setValue(obj.get("imgpath").isString().stringValue());
						
						
						MaterialColumn TextColumn = new MaterialColumn();
						TextColumn.setGrid("s9");
						
						MaterialTextArea mtaq = new MaterialTextArea();
						mtaq.getElement().getStyle().setProperty("margin", "auto");
						mtaq.getElement().getFirstChildElement().getStyle().setProperty("maxHeight", "150px");
						mtaq.getElement().getFirstChildElement().getStyle().setProperty("minHeight", "150px");
						mtaq.getElement().getFirstChildElement().getStyle().setOverflowY(Overflow.SCROLL);
						mtaq.getElement().getFirstChildElement().getStyle().setHeight(150, Unit.PX);
						mtaq.getElement().getFirstChildElement().getStyle().setBorderWidth(1, Unit.PX);
						mtaq.getElement().getFirstChildElement().getStyle().setBorderStyle(BorderStyle.SOLID);
						mtaq.getElement().getFirstChildElement().getStyle().setBorderColor("gainsboro");
						mtaq.getElement().getFirstChildElement().getStyle().setMargin(6, Unit.PX);
						mtaq.getElement().getFirstChildElement().getStyle().setPadding(0, Unit.PX);
						if(obj.get("usrid") == null)
							mtaq.getElement().getFirstChildElement().setAttribute("readonly", "true");
							
						TextColumn.add(mtaq);
						
						MaterialColumn ButtonColumn = new MaterialColumn();
						ButtonColumn.setGrid("s2");
						ButtonColumn.setTextAlign(TextAlign.CENTER);
						ButtonColumn.setHeight("150px");
						ButtonColumn.setLineHeight(150);
						mrq.add(TextColumn);
						mbq = new MaterialButton("삭제");
						mbq.setWidth("100px"); mbq.setHeight("90px");
						mbq.addClickHandler(e-> {
							boolean ok = UI.confirm("", "정말 삭제 하겠습니까?");
							if (ok) {
								JSONObject parameterJSON = new JSONObject();
								parameterJSON.put("cmd", new JSONString("DELETE_ZIKIMI_ANSWER"));
								parameterJSON.put("zikid", new JSONString(obj.get("zikid").isString().stringValue()));
								VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
									@Override
									public void call(Object param1, String param2, Object param3) {
										JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
										JSONObject headerObj = (JSONObject) resultObj.get("header");
										String processResult = headerObj.get("process").isString().stringValue();
										if (processResult.equals("success")) {
											new MaterialToast().toast("삭제 되었습니다.", 3000);
											buildAnswerList(zikid);
										} else {
											new MaterialToast().toast("삭제 실패! 관리자에게 문의하세요.", 3000);
										}
									}
								});
							}
						});
						mbq.setEnabled(false);
						mrq.add(ButtonColumn);
						ButtonColumn.add(mbq);

						String body = obj.get("body") != null ? obj.get("body").isString().stringValue() : "";
						body = body.replaceAll("&#63;", "?");
						mtaq.setText(body);
						mpAnswerlist.add(mrq);
						if(answerListHeight == 0)
							answerListHeight+=110;
						
						if ((viewType == 9 && Registry.getPermission("56cbc2f1-b1f5-4d09-b8e0-97e5ee80a8eb")) ||
								(viewType == 10 && Registry.getPermission("dec2284e-1fa2-49a9-bf19-15b6f224d797")))
							mbq.setEnabled(true);
					}
					
					mrAnswer = new MaterialRow();
					mrAnswer.setLayoutPosition(Position.RELATIVE);
					
					
					MaterialColumn MyImageColumn = new MaterialColumn();
					MyImageColumn.setGrid("s1");
					MyImageColumn.setPadding(0);
					MyImageColumn.setTextAlign(TextAlign.CENTER);
					MyImageColumn.setHeight("150px");
					MyImageColumn.setLineHeight(230);
					
					MaterialImage mymlq = new MaterialImage();
					mymlq.setLeft(10); mymlq.setTop(10); mymlq.setWidth("90px"); mymlq.setHeight("90px");
					mymlq.setBorderRadius("2px");
					mymlq.setPixelSize(90, 90);
					MyImageColumn.add(mymlq);
					mrAnswer.add(MyImageColumn);
					mymlq.setUrl(GWT.getHostPageBaseURL() + "images/adminanswerlogo.png");
					
					MaterialColumn MyTextColumn = new MaterialColumn();
					MyTextColumn.setGrid("s9");
					
					MaterialTextArea mtaAnswer = new MaterialTextArea();
					mtaAnswer.getElement().getStyle().setProperty("margin", "auto");
					mtaAnswer.getElement().getFirstChildElement().getStyle().setProperty("maxHeight", "150px");
					mtaAnswer.getElement().getFirstChildElement().getStyle().setProperty("minHeight", "150px");
					mtaAnswer.getElement().getFirstChildElement().getStyle().setOverflowY(Overflow.SCROLL);
					mtaAnswer.getElement().getFirstChildElement().getStyle().setHeight(150, Unit.PX);
					mtaAnswer.getElement().getFirstChildElement().getStyle().setBorderWidth(1, Unit.PX);
					mtaAnswer.getElement().getFirstChildElement().getStyle().setBorderStyle(BorderStyle.SOLID);
					mtaAnswer.getElement().getFirstChildElement().getStyle().setBorderColor("gainsboro");
					mtaAnswer.getElement().getFirstChildElement().getStyle().setMargin(6, Unit.PX);
					mtaAnswer.getElement().getFirstChildElement().getStyle().setPadding(0, Unit.PX);
					
					MyTextColumn.add(mtaAnswer);
					mrAnswer.add(MyTextColumn);
					
					MaterialColumn MyButtonColumn = new MaterialColumn();
					MyButtonColumn.setGrid("s2");
					MyButtonColumn.setTextAlign(TextAlign.CENTER);
					MyButtonColumn.setHeight("150px");
					MyButtonColumn.setLineHeight(150);
					
					mbAnswer = new MaterialButton("답변달기");
					MyButtonColumn.add(mbAnswer);
					mrAnswer.add(MyButtonColumn);
					mbAnswer.setWidth("100px"); mbAnswer.setHeight("90px");
					mbAnswer.addClickHandler(e->{
						if(mtaAnswer.getText().trim().equals("")) {
							new MaterialToast().toast("댓글 내용을 입력해 주세요.", 3000);
							return;
						}
						
						JSONObject parameterJSON = new JSONObject();
						parameterJSON.put("cmd", new JSONString("INSERT_ZIKIMI_ANSWER"));
						parameterJSON.put("zikid", new JSONString(IDUtil.uuid()));
						parameterJSON.put("parentid", new JSONString(zikid));
						
						parameterJSON.put("usrid", new JSONString(Registry.getUserId()));
						parameterJSON.put("body", new JSONString(mtaAnswer.getText()));
						VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
							@Override
							public void call(Object param1, String param2, Object param3) {
								JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
								JSONObject headerObj = (JSONObject) resultObj.get("header");
								String processResult = headerObj.get("process").isString().stringValue();
								if (processResult.equals("success")) {
									buildAnswerList(zikid);
								} else {
									new MaterialToast().toast("답변달기 실패! 관리자에게 문의하세요..", 3000);
								}
							}
						});
					});
					mbAnswer.setEnabled(false);
					mpAnswerlist.add(mrAnswer);
					
					if ((viewType == 9 && Registry.getPermission("30abdfbc-a887-43f7-b34b-468603e549de")) ||
							(viewType == 10 && Registry.getPermission("c570cae3-3933-45cc-961f-1044ce7ea637")))
						mbAnswer.setEnabled(true);
					
				}else if (processResult.equals("fail")) {
					new MaterialToast().toast("검색 실패! 관리자에게 문의하세요..", 3000);
				}
			}
		});
	}
	@Override
	public int getHeight() {
		return 600;
	}
}
