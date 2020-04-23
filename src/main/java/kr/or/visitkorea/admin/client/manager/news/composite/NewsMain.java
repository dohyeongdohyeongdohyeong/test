package kr.or.visitkorea.admin.client.manager.news.composite;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.news.NewsApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMenuPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class NewsMain extends AbstractContentPanel {


	private ContentTable table;
	private MaterialLabel countLabel;
	private ContentMenuPanel detailPanel;
	private MainContentPanel mainContentPanel;
	private MaterialIcon saveIcon;
	private String nwsid;
	public NewsMain(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
		mainContentPanel.clearDetail();
	}

	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		buildNewsTable();
		buildNewsDetailPanel();
		
	}

	private void buildNewsTable() {

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setHeight(575);
		table.setWidth("505px");
		table.setLeft(30);
		table.setTop(55);
		table.appendTitle("제목", 200, TextAlign.LEFT);
		table.appendTitle("노출 상태", 100, TextAlign.CENTER);
		table.appendTitle("생성일", 100, TextAlign.CENTER);
		table.appendTitle("작성자", 100, TextAlign.CENTER);
		
		this.add(table);

		MaterialIcon previewIcon = new MaterialIcon(IconType.VIEW_STREAM);
		previewIcon.setTextAlign(TextAlign.CENTER);
		previewIcon.addClickHandler(event->{
			if(nwsid == null) return;
			MaterialIcon courseIcon5 = new MaterialIcon(IconType.WEB);
			courseIcon5.setTextAlign(TextAlign.CENTER);
			String tgrUrl = (String) Registry.get("service.server")  + "/notice/news_detail.html?nwsid="+nwsid;
			Registry.openPreview(courseIcon5, tgrUrl);
//			Registry.openPreview(null, (String) Registry.get("service.server") + "/detail/rem_detail.html?cotid=" + nwsid);
		});

		MaterialIcon deleteIcon = new MaterialIcon(IconType.REMOVE);
		deleteIcon.setTextAlign(TextAlign.CENTER);
		deleteIcon.addClickHandler(event->{
			
			getMaterialExtentsWindow().confirm("정말 삭제 하겠습니까?", 500, e2-> {
				if(nwsid == null) return;
				if(((MaterialButton)e2.getSource()).getId().equals("yes")) {
					JSONObject parameterJSON = new JSONObject();
					parameterJSON.put("cmd", new JSONString("DELETE_NEWS"));
					parameterJSON.put("nwsid", new JSONString(nwsid));
					VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
						@Override
						public void call(Object param1, String param2, Object param3) {
							JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
							JSONObject headerObj = (JSONObject) resultObj.get("header");
							String processResult = headerObj.get("process").isString().toString();
							processResult = processResult.replaceAll("\"", "");
							
							if (processResult.equals("success")) {
								getMaterialExtentsWindow().alert("\n\n삭제 되었습니다.", 500);
								mainContentPanel.clearDetail();
								nwsid = null;
								qryList(true);
							} else {
								getMaterialExtentsWindow().alert("삭제 실패. 관리자에게 문의하세요.", 500);
							}
						}
					});
				}
			});
		});

		MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event->{
			if(totcnt <= offset) return;
			qryList(false);
		});
		
		table.getButtomMenu().addIcon(previewIcon, "미리 보기", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(deleteIcon, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", com.google.gwt.dom.client.Style.Float.RIGHT);

		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setTextAlign(TextAlign.CENTER);
		addIcon.addClickHandler(event->{
			nwsid = null;
			mainContentPanel.setNwsid();
			saveIcon.setEnabled(true);
		});

		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			mainContentPanelScroll(0);
//			parametersMap.put("TARGET_TABLE", table);
//			parametersMap.put("TARGET_COUNT", countLabel);
			getMaterialExtentsWindow().openDialog(NewsApplication.SEARCH_TITLE, parametersMap,800,e->{
				qryList(true);
			});
		});
		
		table.getTopMenu().addIcon(addIcon, "소식 추가", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		table.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setLayoutPosition(Position.ABSOLUTE);
		countLabel.setLeft(40);
		countLabel.setTop(55);
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLUE);
		this.add(countLabel);
	}

	private void buildNewsDetailPanel() {
		
		detailPanel = new ContentMenuPanel();
		detailPanel.setTitle("");
		detailPanel.setLayoutPosition(Position.ABSOLUTE);
		detailPanel.setWidth("900px");
		detailPanel.setHeight(575);
		detailPanel.setRight(40);
		detailPanel.setTop(55);
		this.add(detailPanel);
		saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setTextAlign(TextAlign.CENTER);
		saveIcon.setEnabled(false);
		saveIcon.addClickHandler(event->{
			if(nwsid == null) {
				mainContentPanel.insertDetail();
			} else {
				mainContentPanel.updateDetail(nwsid);
			}
		});
		detailPanel.getTopMenu().addIcon(saveIcon, "저장", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);

		mainContentPanel = new MainContentPanel(getMaterialExtentsWindow());
		mainContentPanel.setWidth("878px");
		mainContentPanel.setHeight(1366+"px");
		detailPanel.appendComposite(mainContentPanel);
		
	}

	public void mainContentPanelScroll(double position) {
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(2000);

		mainContentPanel.setTransition(cfg);
		mainContentPanel.setTransform("translate(0, "+position+"px);");
		
	}
	private Map<String, Object> parametersMap = new HashMap<String, Object>();
	private int offset, totcnt; //index
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
//			index = 1;
			table.clearRows();
		} else offset += 20;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_NEWS_LIST"));
		if(parametersMap.get("mode") != null) {
			parameterJSON.put("mode", new JSONString(parametersMap.get("mode").toString()));
			parameterJSON.put("keyword", new JSONString(parametersMap.get("keyword").toString()));
		}
		if(parametersMap.get("sdate") != null)
			parameterJSON.put("sdate", new JSONString(parametersMap.get("sdate").toString()));
		if(parametersMap.get("edate") != null)
			parameterJSON.put("edate", new JSONString(parametersMap.get("edate").toString()));
		parameterJSON.put("offset", new JSONString(offset+""));
		table.loading(true);
		table.clearRows();
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONArray resultArray = bodyObj.get("result").isArray();
					JSONObject bodyResultcnt = (JSONObject) bodyObj.get("resultCnt");
					int usrCnt = resultArray.size();
					if (usrCnt == 0) {
						getMaterialExtentsWindow().alert("결과셋이 없습니다.", 500);
					}
					countLabel.setText(bodyResultcnt.get("CNT")+" 건");
					totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
					for (int i=0; i<usrCnt; i++) {
						JSONObject recObj = resultArray.get(i).isObject();
						ContentTableRow tableRow = table.addRow(Color.WHITE
							,recObj.get("title")!=null?recObj.get("title").isString().stringValue():"[null]"
							,(recObj.get("isview")!=null?recObj.get("isview").isNumber().doubleValue():0)==1?"노출":"미노출"
							,recObj.get("cdate")!=null?recObj.get("cdate").isString().stringValue():"[null]"
							,recObj.get("stfid")!=null?recObj.get("stfid").isString().stringValue():"[null]"
						);
						tableRow.put("nwsid", recObj.get("nwsid").isString().stringValue());
						tableRow.addClickHandler(event->{
							ContentTableRow ctr = (ContentTableRow)event.getSource();
							nwsid = ctr.get("nwsid").toString();
							mainContentPanel.setData(nwsid);
							saveIcon.setEnabled(true);
						});
					}
				} else {
					Console.log("에러냥?");
				}
				table.loading(false);
			}
		});
	}
}
