package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;


import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.client.constants.Color;
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
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.ScrollContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class QnaDetailDialog extends DialogContent {
	protected MemberActivityMain host;
	private MaterialLabel dialogTitle;
	SelectionPanel selisuse;
	private String qnaid;
	
	private ScrollContentPanel mpdetail;
	private MaterialTextArea mtbody;

	private MaterialTextBox mtno, mttitle,mtchum, mtid, mtdate;
	
	private MaterialPanel mpAnswerlist;
	private ScrollPanel scrollpanel;
	
	private MaterialButton mbchum;
	
	MaterialExtentsWindow window;
	
	public QnaDetailDialog(MaterialExtentsWindow window, MemberActivityMain host) {
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
		qnaid = this.getParameters().get("qnaid").toString();
		mtno.setText(this.getParameters().get("no").toString());
		mttitle.setText(this.getParameters().get("title").toString());
		mtid.setText(this.getParameters().get("id").toString());
		mtdate.setText(this.getParameters().get("date").toString());
		mtbody.setValue(this.getParameters().get("body").toString());
//		mtbody.setCursorPos(mtbody.getText().length());
		buildAnswerList(qnaid);
	}

	public void buildContent() {
		// dialog title define
		dialogTitle = new MaterialLabel("Q&A 댓글관리");
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
		MaterialButton mbprev = new MaterialButton("목록이동");
		mbprev.setLayoutPosition(Position.ABSOLUTE);
		mbprev.setBackgroundColor(Color.GREY_DARKEN_1);
		mbprev.setTop(10); mbprev.setRight(15);
		mbprev.addClickHandler(e->{
			getMaterialExtentsWindow().closeDialog();
		});
		mpdetail.add(mbprev);
		
		String labelfontsize = "18px";
		int sleft = 15;
		mtno = new MaterialTextBox();
		mtno.setLabel("번호");
		mtno.setLayoutPosition(Position.ABSOLUTE);
		mtno.setTop(20); mtno.setLeft(sleft); mtno.setWidth("400px"); mtno.setHeight("35px");
		mtno.setPaddingLeft(15);
		mtno.setReadOnly(true);
		mtno.setFontSize(labelfontsize);
		mpdetail.add(mtno);
		
		mttitle = new MaterialTextBox();
		mttitle.setLabel("제목");
		mttitle.setLayoutPosition(Position.ABSOLUTE);
		mttitle.setTop(80);	mttitle.setLeft(sleft); mttitle.setWidth("700px");	mttitle.setHeight("35px");
		mttitle.setPaddingLeft(15);
		mttitle.setReadOnly(true);
		mttitle.setFontSize(labelfontsize);
		mpdetail.add(mttitle);
		
		mtid = new MaterialTextBox();
		mtid.setLabel("작성자");
		mtid.setLayoutPosition(Position.ABSOLUTE);
		mtid.setTop(80); mtid.setLeft(750); mtid.setWidth("200px"); mtid.setHeight("35px");
		mtid.setPaddingLeft(15);
		mtid.setReadOnly(true);
		mtid.setFontSize(labelfontsize);
		mpdetail.add(mtid);
		
		mtdate = new MaterialTextBox();
		mtdate.setLabel("등록일");
		mtdate.setLayoutPosition(Position.ABSOLUTE);
		mtdate.setTop(140); mtdate.setLeft(sleft); mtdate.setWidth("300px"); mtdate.setHeight("35px");
		mtdate.setPaddingLeft(15);
		mtdate.setReadOnly(true);
		mtdate.setFontSize(labelfontsize);
		mpdetail.add(mtdate);
		
		mtchum = new MaterialTextBox();
		mtchum.setLabel("첨부파일");
		mtchum.setLayoutPosition(Position.ABSOLUTE);
		mtchum.setTop(140);	mtchum.setLeft(750); mtchum.setWidth("300px"); mtchum.setHeight("35px");
		mtchum.setPaddingLeft(15);
		mtchum.setReadOnly(true);
		mtchum.setFontSize(labelfontsize);
		mpdetail.add(mtchum);
		mbchum = new MaterialButton("다운로드");
		mbchum.setLayoutPosition(Position.ABSOLUTE);
		mbchum.setTop(150); mbchum.setRight(50); mbchum.setHeight("35px");
		mbchum.setBackgroundColor(Color.GREY_LIGHTEN_1);
		mbchum.setEnabled(false);
		mpdetail.add(mbchum);
		
		mtbody = new MaterialTextArea();
		mtbody.setLabel("내용");
		mtbody.setLayoutPosition(Position.ABSOLUTE);
		mtbody.setTop(200);	mtbody.setLeft(sleft); mtbody.setWidth("1260px"); mtbody.setHeight("130px");
		mtbody.setBackgroundColor(Color.GREY_LIGHTEN_5);
		mtbody.setBorderRadius("2px");
		mtbody.setReadOnly(true);
		mtbody.setFontSize(labelfontsize);
		mtbody.getChildrenList().get(0).getElement().getStyle().setPadding(5, Unit.PX);
		mtbody.getChildrenList().get(0).getElement().getStyle().setTop(0, Unit.PX);
		mtbody.getChildrenList().get(0).getElement().getStyle().setHeight(115, Unit.PX);
		mtbody.getChildrenList().get(0).getElement().getStyle().setOverflow(Overflow.AUTO);
		mpdetail.add(mtbody);
		
		mpAnswerlist = new MaterialPanel();
		mpAnswerlist.setLayoutPosition(Position.ABSOLUTE);
		mpAnswerlist.setTop(370);
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
	
	private int answerListHeight = 0;
	private void buildAnswerList(String qnaid) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_SNSQNA_ANSWER_LIST"));
		parameterJSON.put("qnaid", new JSONString(qnaid));
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
					
					if(bodyResultEtc.size() > 0) {
						JSONObject obj = (JSONObject)bodyResultEtc.get(0);
						mtchum.setText(obj.get("title").isString().stringValue());
						String fileUrl = obj.get("lnfileurl").isString().stringValue();
						mbchum.setEnabled(true);
						mbchum.getHandlerRegistry().clearHandlers();
						mbchum.getHandlerRegistry().registerHandler(
						mbchum.addClickHandler(e->{
							if(fileUrl.lastIndexOf(".") > -1)
								Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl,"","");
							else 
								Window.open((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" +fileUrl,"","");
						}));
					} else {
						mtchum.setText("");
						mbchum.setEnabled(false);
					}
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
							if(obj.get("usr_id") == null)
								mlq.setValue((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=df2b1934-d39e-455f-8f91-1ad9915da901");
							else mlq.setValue((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=df2b1934-d39e-455f-8f91-1ad9915da901");
						} else mlq.setValue(obj.get("imgpath").isString().stringValue());
						
						MaterialTextArea mtaq = new MaterialTextArea();
						mtaq.setLayoutPosition(Position.ABSOLUTE);
						mtaq.setLeft(130); mtaq.setWidth("950px");
						mrq.add(mtaq);
						MaterialButton mbq = new MaterialButton("삭제");
						mbq.setLayoutPosition(Position.ABSOLUTE);
						mbq.setLeft(1150); mbq.setTop(10); mbq.setWidth("100px"); mbq.setHeight("90px");
						mbq.addClickHandler(e-> {
							boolean ok = UI.confirm("", "정말 삭제 하겠습니까?");
							if (ok) {
								JSONObject parameterJSON = new JSONObject();
								parameterJSON.put("cmd", new JSONString("DELETE_SNSQNA_ANSWER"));
								parameterJSON.put("qnaid", new JSONString(obj.get("qnaid").isString().stringValue()));
								VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
									@Override
									public void call(Object param1, String param2, Object param3) {
										JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
										JSONObject headerObj = (JSONObject) resultObj.get("header");
										String processResult = headerObj.get("process").isString().stringValue();
										if (processResult.equals("success")) {
											new MaterialToast().toast("삭제 되었습니다.", 3000);
											buildAnswerList(qnaid);
										} else {
											new MaterialToast().toast("삭제 실패! 관리자에게 문의하세요.", 3000);
										}
									}
								});
							}
						});
						mrq.add(mbq);
						mtaq.setText(obj.get("body").isString().stringValue());
						mrq.setMarginTop(answerListHeight);
						mpAnswerlist.add(mrq);
						if(answerListHeight == 0)
							answerListHeight+=110;
					}
					MaterialRow mrAnswer = new MaterialRow();
					mrAnswer.setLayoutPosition(Position.RELATIVE);
					MaterialTextArea mtaAnswer = new MaterialTextArea();
					mtaAnswer.setLayoutPosition(Position.ABSOLUTE);
					mtaAnswer.setLeft(10); mtaAnswer.setWidth("1100px");
					mrAnswer.add(mtaAnswer);
					MaterialButton mbAnswer = new MaterialButton("답변달기");
					mbAnswer.setLayoutPosition(Position.ABSOLUTE);
					mbAnswer.setLeft(1150); mbAnswer.setTop(10); mbAnswer.setWidth("100px"); mbAnswer.setHeight("90px");
					mrAnswer.add(mbAnswer);
					mbAnswer.addClickHandler(e->{
						if(mtaAnswer.getText().trim().equals("")) {
							new MaterialToast().toast("댓글 내용을 입력해 주세요.", 3000);
							return;
						}
						JSONObject parameterJSON = new JSONObject();
						parameterJSON.put("cmd", new JSONString("INSERT_SNSQNA_ANSWER"));
						parameterJSON.put("qnaid", new JSONString(IDUtil.uuid()));
						parameterJSON.put("parentid", new JSONString(qnaid));
						
						parameterJSON.put("usrid", new JSONString(Registry.getUserId()));
						parameterJSON.put("body", new JSONString(mtaAnswer.getText()));
						VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
							@Override
							public void call(Object param1, String param2, Object param3) {
								JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
								JSONObject headerObj = (JSONObject) resultObj.get("header");
								String processResult = headerObj.get("process").isString().stringValue();
								if (processResult.equals("success")) {
									buildAnswerList(qnaid);
								} else {
									new MaterialToast().toast("답변달기 실패! 관리자에게 문의하세요..", 3000);
								}
							}
						});
					});
					mrAnswer.setMarginTop(answerListHeight);
					mpAnswerlist.add(mrAnswer);

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
