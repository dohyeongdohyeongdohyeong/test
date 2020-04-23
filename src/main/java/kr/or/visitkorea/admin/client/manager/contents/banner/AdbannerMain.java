package kr.or.visitkorea.admin.client.manager.contents.banner;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AdbannerMain extends AbstractContentPanel {

//	private AdbannerApplication appview;
	
	public AdbannerMain(MaterialExtentsWindow materialExtentsWindow, AdbannerApplication pa) {
		super(materialExtentsWindow);
//		appview = pa;
//		self = this;
		qryList();
	}
	private MaterialPanel mpRecom, mpCourse, mpTour, mpNotice;
	private ScrollPanel scrollpanel;
	SelectionPanel selectStatus1, selectStatus2, selectStatus3, selectStatus4, selectStatus5;
	
	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		scrollpanel = new ScrollPanel();
		scrollpanel.setHeight("650px");
		MaterialPanel mpbody = new MaterialPanel();
		scrollpanel.add(mpbody);
		
		// 기관메인 배너 화면
		MaterialRow mp1 = new MaterialRow();
		mp1.setLayoutPosition(Position.RELATIVE);
		mp1.setHeight("25px");
		mpbody.add(mp1);
		
		MaterialLabel mlTitle1 = new MaterialLabel("- 기사메인배너관리");
		mlTitle1.setLayoutPosition(Position.RELATIVE);
		mlTitle1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		mlTitle1.setWidth("280px");
		mlTitle1.setFontSize("18px");
		mlTitle1.setMarginLeft(35);
		mlTitle1.setMarginTop(10);
		mlTitle1.setTextAlign(TextAlign.LEFT);
		mp1.add(mlTitle1);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("노출", 1);
		map.put("비노출", 0);
		selectStatus1 = UI.selectionPanel(mp1, "s2", TextAlign.CENTER, map);
		selectStatus1.setSelectionOnSingleMode("노출");
		selectStatus1.setSingleSelection(true);
		selectStatus1.setMarginTop(20);
		selectStatus1.addStatusChangeEvent(event->{
			setAble("Recom", (Integer)selectStatus1.getSelectedValue());
		});
		
		mpRecom = new MaterialPanel();
		mpRecom.setWidth("100%");
		mpRecom.setBorder("1px solid grey");
		mpRecom.setPadding(10);
		mpbody.add(new MaterialRow());
		mpbody.add(mpRecom);
		
		// 구분자
		mpbody.add(new MaterialRow());
		
		// 코스메인 배너 화면
		MaterialRow mp2 = new MaterialRow();
		mp2.setLayoutPosition(Position.RELATIVE);
		mp2.setHeight("25px");
		mp2.setMarginTop(40);
		mpbody.add(mp2);
		
		MaterialLabel mlTitle2 = new MaterialLabel("- 코스메인배너관리");
		mlTitle2.setLayoutPosition(Position.RELATIVE);
		mlTitle2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		mlTitle2.setWidth("280px");
		mlTitle2.setFontSize("18px");
		mlTitle2.setMarginLeft(35);
		mlTitle2.setMarginTop(10);
		mlTitle2.setTextAlign(TextAlign.LEFT);
		mp2.add(mlTitle2);
		
		selectStatus2 = UI.selectionPanel(mp2, "s2", TextAlign.CENTER, map);
		selectStatus2.setSelectionOnSingleMode("노출");
		selectStatus2.setSingleSelection(true);
		selectStatus2.setMarginTop(20);
		selectStatus2.addStatusChangeEvent(event->{
			setAble("Course", (Integer)selectStatus2.getSelectedValue());
		});
		
		mpCourse = new MaterialPanel();
		mpCourse.setWidth("100%");
		mpCourse.setBorder("1px solid grey");
		mpCourse.setPadding(10);
		mpbody.add(new MaterialRow());
		mpbody.add(mpCourse);
		
		// 구분자
		mpbody.add(new MaterialRow());
		
		// 명소  배너 화면
		MaterialRow mp3 = new MaterialRow();
		mp3.setLayoutPosition(Position.RELATIVE);
		mp3.setHeight("25px");
		mp3.setMarginTop(40);
		mpbody.add(mp3);
		
		MaterialLabel mlTitle3 = new MaterialLabel("- 명소 목록");
		mlTitle3.setLayoutPosition(Position.RELATIVE);
		mlTitle3.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		mlTitle3.setWidth("280px");
		mlTitle3.setFontSize("18px");
		mlTitle3.setMarginLeft(35);
		mlTitle3.setMarginTop(10);
		mlTitle3.setTextAlign(TextAlign.LEFT);
		mp3.add(mlTitle3);
		
		selectStatus3 = UI.selectionPanel(mp3, "s2", TextAlign.CENTER, map);
		selectStatus3.setSelectionOnSingleMode("노출");
		selectStatus3.setSingleSelection(true);
		selectStatus3.setMarginTop(20);
		selectStatus3.addStatusChangeEvent(event->{
			setAble("Tour", (Integer)selectStatus3.getSelectedValue());
		});
		
		mpTour = new MaterialPanel();
		mpTour.setWidth("100%");
		mpTour.setBorder("1px solid grey");
		mpTour.setPadding(10);
		mpbody.add(new MaterialRow());
		mpbody.add(mpTour);
	
		
		// 구분자
		mpbody.add(new MaterialRow());
		
		// 소식  배너 화면
		MaterialRow mp4 = new MaterialRow();
		mp4.setLayoutPosition(Position.RELATIVE);
		mp4.setHeight("25px");
		mp4.setMarginTop(40);
		mpbody.add(mp4);
		
		MaterialLabel mlTitle4 = new MaterialLabel("- 소식 목록");
		mlTitle4.setLayoutPosition(Position.RELATIVE);
		mlTitle4.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		mlTitle4.setWidth("280px");
		mlTitle4.setFontSize("18px");
		mlTitle4.setMarginLeft(35);
		mlTitle4.setMarginTop(10);
		mlTitle4.setTextAlign(TextAlign.LEFT);
		mp4.add(mlTitle4);
		
		selectStatus4 = UI.selectionPanel(mp4, "s2", TextAlign.CENTER, map);
		selectStatus4.setSelectionOnSingleMode("노출");
		selectStatus4.setSingleSelection(true);
		selectStatus4.setMarginTop(20);
		selectStatus4.addStatusChangeEvent(event->{
			setAble("Notice", (Integer)selectStatus4.getSelectedValue());
		});
		
		mpNotice = new MaterialPanel();
		mpNotice.setWidth("100%");
		mpNotice.setBorder("1px solid grey");
		mpNotice.setPadding(10);
		mpbody.add(new MaterialRow());
		mpbody.add(mpNotice);
		
		mpbody.setPaddingLeft(10);
		mpbody.setPaddingRight(10);
		this.add(scrollpanel);
		
	}

	private void qryList() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_BANNER_LIST"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				if (processResult.equals("success")) {
					mpRecom.clear();mpCourse.clear();mpTour.clear();mpNotice.clear();
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					int usrCnt = bodyResultObj.size();
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						String pageid = obj.get("pageid").toString().replaceAll("\"", "");
						
						MaterialRow mr = new MaterialRow();
						mr.setLayoutPosition(Position.RELATIVE);
						MaterialImage mlimg = new MaterialImage();
						mlimg.setLayoutPosition(Position.RELATIVE);
						mlimg.setTop(10);
						mlimg.setLeft(50);
						mlimg.setBottom(10);
						mlimg.setWidth("450px");
						mlimg.setHeight("100px");
						mlimg.setBorderRadius("1px");
						mlimg.setPixelSize(450, 100);
						mlimg.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
						mr.add(mlimg);
						mlimg.setUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+obj.get("imgid").isString().stringValue());
//						mlimg.setUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" +obj.get("imgid").isString().stringValue()+".png");
						if(obj.get("orderby").isNumber().doubleValue() > 0) {
							MaterialButton mbup = new MaterialButton();
							mbup.setLayoutPosition(Position.RELATIVE);
							mbup.setLeft(55);
							mbup.setTop(15);
							mbup.setBorderRadius("2px");
							mbup.setPaddingRight(10);
							mbup.setText("△");
							mbup.setFontSize("25px"); mbup.setFontWeight(FontWeight.BOLD);
							mbup.setWidth("60px");
							mbup.setTextAlign(TextAlign.CENTER);
							mbup.setBackgroundColor(Color.BLUE_DARKEN_3);
							mbup.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
							mr.add(mbup);
							
							mbup.addClickHandler(e->{
								JSONObject parameterJSON = new JSONObject();
								parameterJSON.put("cmd", new JSONString("UPDATE_BANNER_DETAIL_ORDER"));
								parameterJSON.put("mkbid", new JSONString(obj.get("mkbid").isString().stringValue()));
								VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
									@Override
									public void call(Object param1, String param2, Object param3) {
										JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
										JSONObject headerObj = (JSONObject) resultObj.get("header");
										String processResult = headerObj.get("process").isString().stringValue();
										if (processResult.equals("success")) {
											qryList();
										} else {
											getMaterialExtentsWindow().alert("변경 실패했습니다. 관리자에게 문의하세요.", 500);
										}
									}
								});
							});
						} else {
							MaterialLabel mbup = new MaterialLabel();
							mbup.setLayoutPosition(Position.RELATIVE);
							mbup.setLeft(55);
							mbup.setTop(15);
//							mbup.setPaddingRight(20);
//							else mbup.setText("▽");
							mbup.setWidth("60px");
							mbup.setHeight("40px");
							mbup.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
							mr.add(mbup);
						}
						
						MaterialPanel mpinfo = new MaterialPanel();
						mpinfo.setLayoutPosition(Position.RELATIVE);
						mpinfo.setLeft(100);
						mpinfo.setWidth("550px");
						mpinfo.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
						mr.add(mpinfo);
						
						MaterialLabel mltitle = new MaterialLabel("제목 : "+ obj.get("title").isString().stringValue());
						mltitle.setLayoutPosition(Position.ABSOLUTE);
						mltitle.setLeft(10);
						mpinfo.add(mltitle);
						
						MaterialButton mbmodify = new MaterialButton("수정");
						mbmodify.setLayoutPosition(Position.ABSOLUTE);
						mbmodify.setLeft(150);
						mbmodify.setTop(40);
						mbmodify.setBackgroundColor(Color.BLUE_DARKEN_4);
						mbmodify.addClickHandler(e-> {
							Map<String, Object> paramters = new HashMap<String, Object>();
							paramters.put("mkbid", obj.get("mkbid").isString().stringValue());
							paramters.put("orderby", obj.get("orderby").isNumber().doubleValue());
							paramters.put("title", obj.get("title").isString().stringValue());
							paramters.put("imgid", obj.get("imgid").isString().stringValue());
							if(obj.get("url") != null)
								paramters.put("url", obj.get("url").isString().stringValue());
							if(obj.get("cotid") != null)
								paramters.put("cotid", obj.get("cotid").isString().stringValue());
							getMaterialExtentsWindow().openDialog(AdbannerApplication.MODIFY_BANNER_DETIAL, paramters, 720, e2->{
							    Timer t = new Timer() {
							      public void run() {
							    	  qryList();
							      }
							    };
							    t.schedule(500);
								
							});
						});
						mpinfo.add(mbmodify);
						
						MaterialButton mblink = new MaterialButton("연결링크 보기");
						mblink.setLayoutPosition(Position.ABSOLUTE);
						mblink.setLeft(350);
						mblink.setTop(40);
						mblink.setBackgroundColor(Color.BLUE_DARKEN_4);
						mblink.addClickHandler(e-> {
							if(obj.get("cotid") == null) {
								Window.open(obj.get("url").isString().stringValue(), obj.get("title").isString().stringValue(), "");
							} else {
								String tgrUrl = (String) Registry.get("service.server")  + "/detail/detail_view.html?cotid="+obj.get("cotid").isString().stringValue();
								Window.open(tgrUrl, obj.get("title").isString().stringValue(), "");
							}
							
						});
						mpinfo.add(mblink);
						
						if(pageid.equals("Recom")) {
							if(obj.get("able").isNumber().doubleValue() > 0)
								selectStatus1.setSelectionOnSingleMode("노출");
							else selectStatus1.setSelectionOnSingleMode("비노출");
							mpRecom.add(mr);
						} else if(pageid.equals("Course")) {
							if(obj.get("able").isNumber().doubleValue() > 0)
								selectStatus2.setSelectionOnSingleMode("노출");
							else selectStatus2.setSelectionOnSingleMode("비노출");
							mpCourse.add(mr);
						} else if(pageid.equals("Tour")) {
							if(obj.get("able").isNumber().doubleValue() > 0)
								selectStatus3.setSelectionOnSingleMode("노출");
							else selectStatus3.setSelectionOnSingleMode("비노출");
							mpTour.add(mr);
						} else {
							if(obj.get("able").isNumber().doubleValue() > 0)
								selectStatus4.setSelectionOnSingleMode("노출");
							else selectStatus4.setSelectionOnSingleMode("비노출");
							mpNotice.add(mr);
						}
					}
				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			}
		});
	}
	
	public void setAble(String pageid, int isable) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_BANNER"));
		parameterJSON.put("pageid", new JSONString(pageid));
		parameterJSON.put("able", new JSONString(""+isable));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
//					getMaterialExtentsWindow().alert(" 되었습니다.", 500);
				} else {
					getMaterialExtentsWindow().alert("삭제에 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			}
		});
	}
}
