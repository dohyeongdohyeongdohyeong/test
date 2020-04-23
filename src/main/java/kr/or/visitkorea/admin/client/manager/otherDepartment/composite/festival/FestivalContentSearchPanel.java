package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SeasonContentDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class FestivalContentSearchPanel extends MaterialPanel {

	private MaterialPanel cp;
	private MaterialTextBox box;
	private FestivalContentRight rightArea;
	private MaterialExtentsWindow window;
	private SeasonContentDialog dialog;
	private String keyword;

	public FestivalContentSearchPanel(MaterialExtentsWindow materialExtentsWindow, SeasonContentDialog dialog) {
		super();
		this.window = materialExtentsWindow;
		this.dialog = dialog;
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		box = new MaterialTextBox();
		box.setPlaceholder("검색어를 입력해 주세요.");
		this.add(box);
		
		box.getElement().getStyle().setMarginTop(0, Unit.PX);
		
		MaterialPanel contentViewPanel = new MaterialPanel();
		contentViewPanel.setLayoutPosition(Position.RELATIVE);
		contentViewPanel.setLeft(0);
		contentViewPanel.setRight(0);
		contentViewPanel.setBottom(0);
		contentViewPanel.setHeight("138px");
		contentViewPanel.setBorder("1px solid #c8c8c8");
		contentViewPanel.setOverflow(Overflow.AUTO);
		this.add(contentViewPanel);
		
		cp = new MaterialPanel();
		cp.setLayoutPosition(Position.RELATIVE);
		cp.setLeft(0);
		cp.setRight(1);
		cp.setTop(0);

		box.addKeyUpHandler(event->{
			if (event.getNativeKeyCode() == 13 && box.getText().length() >= 1) {
				
				cp.clear();
				
				JSONObject jObj = new JSONObject();
				jObj.put("cmd", new JSONString("SEARCH_TITLE_SEASON_CONTENT"));
				jObj.put("keyword", new JSONString(keyword));
				jObj.put("title", new JSONString("%"+box.getText()+"%"));
				
				VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {

						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().toString();
						processResult = processResult.replaceAll("\"", "");
						
						if (processResult.equals("success")) {
							
							Console.log(resultObj.toString());
							
							JSONArray bodyArray = resultObj.get("body").isObject().get("result").isArray();
							int bodyArrayLength = bodyArray.size();
							
							if (bodyArray.size() == 0) {
								
								dialog.alert(					
										"안내", 600, 250,
										new String[] {
											"검색한 결과가 없습니다.",
											"지정 태그["+keyword+"]와 타이틀["+box.getText()+"]을 포함하는 축제 컨텐츠의 조회 결과 ", 
											"컨텐츠를 찾을 수 없었습니다."
										});
								
							}else if (bodyArray.size() > 0) {
								
								for (int i=0; i<bodyArrayLength; i++) {
									
									JSONObject targetObject = bodyArray.get(i).isObject();
									String cotIdString = targetObject.get("COT_ID").isString().stringValue();
									String titleString = targetObject.get("TITLE").isString().stringValue();
									String startString = targetObject.get("START_DATE").isString().stringValue();
									String endString = targetObject.get("END_DATE").isString().stringValue();
									String sigugunString = targetObject.get("SIGUGUN").isString().stringValue();
									String areaString = targetObject.get("AREA").isString().stringValue();

									MaterialPanel recPanel = new MaterialPanel();
									recPanel.setWidth("100%");
									recPanel.setHeight("30px");
									recPanel.setLayoutPosition(Position.RELATIVE);
									
									recPanel.addMouseOverHandler(ev->{
										recPanel.setBackgroundColor(Color.BLUE_LIGHTEN_4);
									});
									
									recPanel.addMouseOutHandler(ev->{
										recPanel.setBackgroundColor(Color.TRANSPARENT);
									});
									
									MaterialLabel rec = new MaterialLabel(titleString.trim());
									
									rec.setTooltip(titleString.trim() + " [ " + startString + " ~ " + endString + " ]");
									rec.getElement().setAttribute("cotid", cotIdString);
									rec.getElement().setAttribute("area", cotIdString);
									rec.setFloat(Float.LEFT);
									rec.setLineHeight(30);
									rec.setMarginLeft(10);
									rec.setWidth("250px");
									
									MaterialLink addBtn = new MaterialLink();
									addBtn.setDisplay(Display.INLINE_BLOCK);
									addBtn.setPaddingTop(3);
									addBtn.setFloat(Float.RIGHT);
									addBtn.setLineHeight(30);
									addBtn.setIconType(IconType.ADD);
									addBtn.setTooltip("컨텐츠 추가");
									
									MaterialLink prevBtn = new MaterialLink();
									prevBtn.setDisplay(Display.INLINE_BLOCK);
									prevBtn.setPaddingTop(3);
									prevBtn.setFloat(Float.RIGHT);
									prevBtn.setLineHeight(30);
									prevBtn.setIconType(IconType.PAGEVIEW);
									prevBtn.setTooltip("미리 보기");
									prevBtn.addClickHandler(event->{
										Registry.openPreview(prevBtn, (String) Registry.get("service.server")  + "/detail/fes_detail.do?cotid=" + rec.getElement().getAttribute("cotid"));
									});
									
									recPanel.add(rec);
									recPanel.add(addBtn);
									recPanel.add(prevBtn);
									
									rec.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
									rec.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
									rec.getElement().getStyle().setOverflow(Overflow.HIDDEN);
									
									addBtn.getElement().getElementsByTagName("i").getItem(0).getStyle().setMarginRight(5, Unit.PX);
									prevBtn.getElement().getElementsByTagName("i").getItem(0).getStyle().setMarginRight(5, Unit.PX);
									
									addBtn.addClickHandler(ev->{
										
										SearchBodyWidget bodyWidget = (SearchBodyWidget)rightArea.getValueMap().get("LIST");
										
										List<VisitKoreaListCell> rowInfoList = new ArrayList<VisitKoreaListCell>();
										rowInfoList.add(new TagListLabelCell(titleString, Float.LEFT, "268px",  "50", 50, FontWeight.BOLDER, true, TextAlign.LEFT));
										rowInfoList.add(new TagListLabelCell(areaString + " " + sigugunString, Float.LEFT, "200px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
										rowInfoList.add(new TagListLabelCell(startString.substring(4) + " ~ " + endString.substring(4), Float.LEFT, "100px",  "50", 50, FontWeight.BOLDER, false, TextAlign.CENTER));
										
										TagListRow tagListRow = new TagListRow(rowInfoList);
										tagListRow.setTagName(cotIdString);
										tagListRow.setCount(0);
										
										tagListRow.put("SAT_ID", IDUtil.uuid());
										tagListRow.put("AREA", areaString);
										tagListRow.put("SIGUGUN", sigugunString);
										tagListRow.put("START_DATE", startString.substring(4));
										tagListRow.put("END_DATE", endString.substring(4));
										
										if (bodyWidget.getChildrenList().size() < 10) {
											bodyWidget.addRow(dialog, tagListRow);
										}else {
											dialog.alert(					
													"안내", 600, 250,
													new String[] {
															"선택한 컨텐츠가 10개가 넘었습니다.",
															"태그당 컨텐츠의 수는 최대 10개를 넘을 수 없습니다. ", 
															"신규 데이터를 선택해야 하는 경우 기존 데이터를 삭제하여 공간을 확보해야 합니다."
													});
										}
										
									});
									
									prevBtn.addClickHandler(ev->{
									});
									
									cp.add(recPanel);
								}
							}
						}
					}
					
				});
				
			}
		});
		
		contentViewPanel.add(cp);
	}

	private MaterialExtentsWindow getWindow() {
		return this.window;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		box.setEnabled(enabled);
		box.setText("");
		if (enabled) this.box.setFocus(enabled);
		cp.clear();
		for (Widget widget : cp.getChildrenList()) {
			if (widget instanceof MaterialWidget) {
				MaterialWidget mWidget = (MaterialWidget)widget;
				mWidget.setEnabled(enabled);
				for (Widget cWidget : mWidget.getChildrenList()) {
					if (cWidget instanceof MaterialWidget) {
						MaterialWidget mcWidget = (MaterialWidget)cWidget;
						mcWidget.setEnabled(enabled);
					}
				}
			}
		}
	}

	public void setFocus(boolean isFocus) {
		this.box.setFocus(isFocus);
	}

	public void setRightArea(FestivalContentRight rightArea) {
		this.rightArea = rightArea;
	}
	
	public void clearContentArea() {
		cp.clear();
	}

	public void setKeyword(String word) {
		this.keyword = word;
	}
}
