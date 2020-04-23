package kr.or.visitkorea.admin.client.manager.analysis.department;


import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepAnalysisMain extends AbstractContentPanel {

//	private MainAnalysisApplication appview;
//	private MemberQnaMain self;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> ctype, mode, contenttype;
	private MaterialComboBox<Object> areatype, sigungutype;
	private SelectionPanel spcategory;
	private int offset;
	private MaterialPanel mrbottom;
	private MaterialInput sdate, edate;
	private MaterialInput edname;
	private int totcnt;
	
	private int index;
	private MaterialPanel ConnectUserPanel;
	
//	private String sort = "";
	
	public OtherDepAnalysisMain(MaterialExtentsWindow materialExtentsWindow, OtherDepAnalysisApplication pa) {
		super(materialExtentsWindow);
//		appview = pa;
//		self = this;
	}
	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		MaterialLabel lbrange = new MaterialLabel("컨텐츠명 : ");

		spcategory = new SelectionPanel();
		spcategory.setLayoutPosition(Position.ABSOLUTE);
		spcategory.setTop(10);
		spcategory.setLeft(0);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("메인통계", 0);
		map.put("컨텐츠통계", 1);
		map.put("태그통계", 2);
		spcategory.setValues(map);
		spcategory.setWidth(300+"px");
		spcategory.setSelectionOnSingleMode("메인통계");
		spcategory.setSingleSelection(true);
		spcategory.addStatusChangeEvent(event->{
			int mtype = (int)spcategory.getSelectedValue();
			table.clearRows();
			countLabel.setText("0 건");
			table.removeTitleAll();
			if(mtype==0) {
				table.appendTitle("메뉴명", 700, TextAlign.CENTER);
				table.appendTitle("클릭수", 300, TextAlign.CENTER);
				table.appendTitle("비율", 300, TextAlign.CENTER);
				mrbottom.setVisible(false);
			} else if(mtype==1) {
				lbrange.setVisible(true);
				areatype.setVisible(true);
				sigungutype.setVisible(true);
				edname.setVisible(true);
				contenttype.setVisible(true);
				mode.clear();
				mode.addItem("전체", 0);
				mode.addItem("기사", 1);
				mode.addItem("DB", 2);
				mode.addValueChangeHandler(e->{
					contenttype.clear();
					contenttype.addItem("전체",0);
					if(mode.getSelectedIndex() == 1) {
						contenttype.addItem("맛여행",10100);
						contenttype.addItem("풍경여행",10200);
						contenttype.addItem("영화드라마여행",10300);
						contenttype.addItem("레포츠여행",10400);
						contenttype.addItem("체험여행",10500);
						contenttype.addItem("네티즌선정베스트그곳",10600);
						contenttype.addItem("교과서속여행",10700);
						contenttype.addItem("테마여행",10800);
						contenttype.addItem("문화유산여행",10900);
						contenttype.addItem("추천가볼만한곳",11000);
						contenttype.addItem("무장애여행",11100);
						contenttype.addItem("장애인여행추천",11101);
						contenttype.addItem("영유아가족",11102);
						contenttype.addItem("어르신여행",11103);
						contenttype.addItem("생태관광",11200);
						contenttype.addItem("생태관광_추천",11201);
						contenttype.addItem("생태관광_테마",11202);
						contenttype.addItem("테마10선",11300);
						contenttype.addItem("웰니스25선",11400);
						contenttype.addItem("관광의별",11500);
						contenttype.addItem("관광의별_수상내역",11501);
						contenttype.addItem("관광의별_수상지소개칼럼",11502);
						contenttype.addItem("품질인증(KQ)",11600);
						contenttype.addItem("일반기사",14000);
						contenttype.addItem("테마기사",15000);
					} else if(mode.getSelectedIndex() == 2) {
						contenttype.addItem("관광지",12);
						contenttype.addItem("문화시설",14);
						contenttype.addItem("축제행사공연",15);
						contenttype.addItem("여행코스",25);
						contenttype.addItem("레포츠",28);
						contenttype.addItem("숙박",32);
						contenttype.addItem("쇼핑",38);
						contenttype.addItem("음식점",39);
						contenttype.addItem("생태관광",2000);
					}
					contenttype.setSelectedIndex(0);
					qryList(true);
				});
				table.appendTitle("번호", 100, TextAlign.CENTER);
				table.appendTitle("컨텐츠명", 400, TextAlign.CENTER);
				table.appendTitle("페이지뷰", 110, TextAlign.CENTER);
				table.appendTitle("코스등록", 110, TextAlign.CENTER);
				table.appendTitle("즐겨찾기", 110, TextAlign.CENTER);
				table.appendTitle("공유하기", 110, TextAlign.CENTER);
				table.appendTitle("좋아요", 110, TextAlign.CENTER);
				table.appendTitle("관광정보지킴이", 110, TextAlign.CENTER);
				table.appendTitle("댓글수", 110, TextAlign.CENTER);
				table.appendTitle("인쇄수", 110, TextAlign.CENTER);
				mrbottom.setVisible(true);
			} else {
//				mode.clear();
//				mode.addItem("전체", 0);
//				mode.addItem("메인", 1);
//				mode.addItem("목록", 2);
//				mode.addValueChangeHandler(e->{
//					qryList(true);
//				});
//				lbrange.setVisible(false);
//				areatype.setVisible(false);
//				sigungutype.setVisible(false);
//				edname.setVisible(false);
//				contenttype.setVisible(false);
				
				mrbottom.setVisible(false);
				table.appendTitle("순위", 300, TextAlign.CENTER);
				table.appendTitle("태그명", 700, TextAlign.CENTER);
				table.appendTitle("클릭수", 300, TextAlign.CENTER);
			}
//			qryList(true);
		});
		
		this.add(spcategory);
		
		buildTableTab();
		
		mrbottom = new MaterialPanel();
		mrbottom.setLayoutPosition(Position.ABSOLUTE);
		mrbottom.setBorder("2px solid #DDDDDD");
		mrbottom.setTop(550);
		mrbottom.setLeft(0);
		mrbottom.setWidth("98.5%");
		mrbottom.setMargin(10);
		mrbottom.setHeight("90px");
		mrbottom.setVisible(false);
		
		areatype = new MaterialComboBox<>();
		areatype.setWidth("120px");
		areatype.setLayoutPosition(Position.ABSOLUTE);
		areatype.setTop(0);
		areatype.setLeft(10);
		mrbottom.add(areatype);
		areatype.addItem("전체", 0);
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AREA"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						areatype.addItem(obj.get("name").isString().stringValue(), obj.get("code").isString().stringValue());
					}
					areatype.setSelectedIndex(0);
				}
			}
		});
		areatype.addValueChangeHandler(ee->{
			sigungutype.clear();
			JSONObject paramJSON = new JSONObject();
			paramJSON.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
			paramJSON.put("areacode", new JSONString(areatype.getValues().get(areatype.getSelectedIndex())+""));
			VisitKoreaBusiness.post("call", paramJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject bodyObj = (JSONObject) resultObj.get("body");
						JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
						int usrCnt = bodyResultObj.size();
						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							if(obj.get("code").isString().stringValue().equals("0")) {
								sigungutype.addItem("전체", 0);
							} else
								sigungutype.addItem(obj.get("sigungu").isString().stringValue(), obj.get("code").isString().stringValue());
						}
						sigungutype.setSelectedIndex(0);
						qryList(true);
					}
				}
			});
		});
		
		sigungutype = new MaterialComboBox<>();
		sigungutype.setWidth("100px");
		sigungutype.setLayoutPosition(Position.ABSOLUTE);
		sigungutype.setTop(0);
		sigungutype.setLeft(140);
		mrbottom.add(sigungutype);
		sigungutype.addValueChangeHandler(ee->{
			qryList(true);
		});
		
		mode = new MaterialComboBox<>();
		mode.setWidth("100px");
		mode.setLayoutPosition(Position.ABSOLUTE);
		mode.setTop(0);
		mode.setLeft(260);
		mode.setSelectedIndex(0);
		mrbottom.add(mode);
		
		contenttype = new MaterialComboBox<>();
		contenttype.setWidth("200px");
		contenttype.setLayoutPosition(Position.ABSOLUTE);
		contenttype.setTop(0);
		contenttype.setLeft(370);
		mrbottom.add(contenttype);
		contenttype.addValueChangeHandler(e->{
			qryList(true);
		});
		contenttype.setSelectedIndex(0);
		lbrange.setLayoutPosition(Position.ABSOLUTE);
		lbrange.setTop(30);
		lbrange.setLeft(580);
		lbrange.setWidth("100px");
		mrbottom.add(lbrange);
		edname = new MaterialInput();
		edname.setWidth("400px");
		edname.setLayoutPosition(Position.ABSOLUTE);
		edname.setTop(15);
		edname.setLeft(680);
		
		mrbottom.add(edname);
		
		this.add(mrbottom);
		
	}

	private MaterialPanel mptable;
	private String otdid= null;
	private MaterialLabel AllConnectLabel;
	private MaterialLabel InConnectLabel;
	private MaterialLabel OutConnectLabel;
	private void buildTableTab() {
		mptable = new MaterialPanel();
		mptable.setWidth("100%");
		mptable.setHeight("600px");
		mptable.setBackgroundColor(Color.WHITE);
		
		MaterialPanel mrtop = new MaterialPanel();
		mrtop.setWidth("100%");
		mrtop.setHeight("80px");
		mrtop.setPadding(10);
		ctype = new MaterialComboBox<>();
		ctype.setWidth("250px");
		ctype.setLayoutPosition(Position.ABSOLUTE);
		ctype.setTop(0);
		ctype.setRight(500);
		mrtop.add(ctype);
		
		if(Registry.getStfId().equals("admin") || Registry.getStfId().equals("ktoadmin")) {
			ctype.addItem("전체", "0");
			otdid = null;
		} else {
//			otdid = Registry.getOtdIds();
			if(Registry.getStfId().equals("ktobf")) otdid ="b55ffe10-84c3-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("ktokq")) otdid ="456a84d1-84c4-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("ktoti")) otdid ="114b23a6-84c4-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("kto100")) otdid ="622bcd99-84fa-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("ktostar")) otdid ="64e29192-8939-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("kto25")) otdid ="287776d6-8939-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("ktocity")) otdid ="81f62fd1-8939-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("ktort")) otdid ="7ff670df-84fa-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("kto10")) otdid ="a711ab41-8939-11e8-8165-020027310001";
			else if(Registry.getStfId().equals("ktoeco")) otdid ="27f7a2ca-84c4-11e8-8165-020027310001";
		}

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_OTHERDEP_SERVICE"));
		if(otdid != null)
			parameterJSON.put("otdid", new JSONString(otdid));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						ctype.setEnabled(false);
					} else {
						ctype.setEnabled(true);
						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							ctype.addItem(obj.get("title").isString().stringValue(), obj.get("otdid").isString().stringValue());
						}
						ctype.setSelectedIndex(0);
					}
				}
			}
		});
		ctype.addValueChangeHandler(ee->{
			qryList(true);
		});
		
		MaterialLabel lbrange = new MaterialLabel("기간 : ");
		lbrange.setLayoutPosition(Position.ABSOLUTE);
		lbrange.setTop(30);
		lbrange.setRight(360);
		lbrange.setWidth("70px");
		mrtop.add(lbrange);
		sdate = new MaterialInput(InputType.DATE);
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setTop(15);
		sdate.setRight(220);
		sdate.setWidth("130px");
		mrtop.add(sdate);
		edate = new MaterialInput(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(15);
		edate.setRight(70);
		edate.setWidth("130px");
		mrtop.add(edate);
		
		mptable.add(mrtop);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setHeight(500);
		table.setWidth("98.5%");
		table.setMargin(10);
		table.setTop(40);
		table.appendTitle("메뉴명", 700, TextAlign.CENTER);
		table.appendTitle("클릭수", 300, TextAlign.CENTER);
		table.appendTitle("비율", 300, TextAlign.CENTER);
		table.setTagVisible(true);
		
		mptable.add(table);

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

		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		
		table.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setLayoutPosition(Position.ABSOLUTE);
		countLabel.setRight(50);
		countLabel.setTop(528);
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		mptable.add(countLabel);
		
		this.add(mptable);

		MaterialButton btnXlsDn = new MaterialButton("결과 XLS 다운로드");
		btnXlsDn.setLayoutPosition(Position.ABSOLUTE);
		btnXlsDn.setTop(10);
		btnXlsDn.setLeft(300);
		this.add(btnXlsDn);
		
		btnXlsDn.addClickHandler(e->{
			if(countLabel.getText().startsWith("0")) {
				getMaterialExtentsWindow().alert("결과 목록이 없습니다.", 500);
				return;
			}
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			
			if(!ctype.getValues().get(ctype.getSelectedIndex()).toString().equals("0")) 
				downurl += "&otdid="+ctype.getValues().get(ctype.getSelectedIndex()).toString();
			String strsdate = sdate.getText().toString();
			String stredate = edate.getText().toString();
			int mtype = (int)spcategory.getSelectedValue();
			switch(mtype) {
			case 0: // 메인통계
				if(!"".equals(strsdate)) downurl += "&sdate="+strsdate.replaceAll("-", "");
				if(!"".equals(stredate)) downurl += "&edate="+stredate.replaceAll("-", "");
				downurl += "&select_type=analysis_other_main";
				break;
			case 1:// 컨텐츠통계
				downurl += "&select_type=analysis_other_contents";
				if(!"".equals(strsdate)) downurl += "&sdate="+strsdate.replaceAll("-", "");
				if(!"".equals(stredate)) downurl += "&edate="+stredate.replaceAll("-", "")+ " 23:59:59";
				downurl += "&mode="+mode.getSelectedIndex();
				String dbname1 = "ARTICLE_MASTER";
				String dbname2 = "DATABASE_MASTER";
				if(mode.getSelectedIndex() == 2) {
					dbname1 = "DATABASE_MASTER";
					dbname2 = "ARTICLE_MASTER";
				}
				downurl += "&dbname1="+dbname1;
				downurl += "&dbname2="+dbname2;
				
				if(areatype.getSelectedIndex()>0 && !areatype.getValues().get(areatype.getSelectedIndex()).toString().equals("0")) {
					downurl += "&areacode="+areatype.getValues().get(areatype.getSelectedIndex()).toString();
					if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
						downurl += "&sigungucode="+sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString();
					}
				}
				if(contenttype.getSelectedIndex()>0 && !contenttype.getValues().get(contenttype.getSelectedIndex()).toString().equals("0")) 
					downurl += "&contenttype="+contenttype.getValues().get(contenttype.getSelectedIndex()).toString();
				if(!edname.getText().trim().equals("")) 
					downurl += "&title="+edname.getText().trim();
				break;
			case 2: //태그통계
				if(!"".equals(strsdate)) downurl += "&sdate="+strsdate.replaceAll("-", "");
				if(!"".equals(stredate)) downurl += "&edate="+stredate.replaceAll("-", "");
				downurl += "&select_type=analysis_other_tag";
				break;
			}
				
			Window.open(downurl,"_self", "enabled");
		});
		
		
	}

	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		table.loading(true);
		JSONObject parameterJSON = new JSONObject();
		
		if(!ctype.getValues().get(ctype.getSelectedIndex()).toString().equals("0")) 
			parameterJSON.put("otdid", new JSONString(ctype.getValues().get(ctype.getSelectedIndex()).toString()));
		
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("sdate", new JSONString(strsdate.replaceAll("-", "")));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("edate", new JSONString(stredate.replaceAll("-", "")));
		}
		int mtype = (int)spcategory.getSelectedValue();
		switch(mtype) {
		case 0: // 메인통계
			parameterJSON.put("cmd", new JSONString("SELECT_ANALYSIS_OTHERMAIN"));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject bodyObj = (JSONObject) resultObj.get("body");
						JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
						JSONObject bodyResulttot = (JSONObject) bodyObj.get("resultTot");
						int usrCnt = bodyResultObj.size();
						if (usrCnt == 0) {
							MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
						}
						double nTot = bodyResulttot.get("tot")!=null?bodyResulttot.get("tot").isNumber().doubleValue():0; //.toString()
//						Console.log(nTot+" 건");
						countLabel.setText(usrCnt+" 건");
						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							int cnt = (int)(obj.get("cnt")!=null?obj.get("cnt").isNumber().doubleValue():0);
							ContentTableRow tableRow = table.addRow(Color.WHITE 
									,obj.get("name")!=null?obj.get("name").isString().stringValue()+"":""
									,cnt+""
									,Math.round((cnt/nTot*100)*100)/100.0+""
								);
							tableRow.addClickHandler(e->{});
						}
						CreateConnectUserBuild();
						JSONArray bodyMainObj = (JSONArray) bodyObj.get("resultmain");
						int incnt = bodyMainObj.get(0).isObject().get("cnt") != null ?
								(int) bodyMainObj.get(0).isObject().get("cnt").isNumber().doubleValue() : 0;
						int outcnt = bodyMainObj.get(1).isObject().get("cnt") != null ?
								(int) bodyMainObj.get(1).isObject().get("cnt").isNumber().doubleValue() : 0;
						int allcnt = incnt+outcnt;
						
						InConnectLabel.setText(incnt+"");
						OutConnectLabel.setText(outcnt+"");
						AllConnectLabel.setText(allcnt+"");
						
					} else getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			});
			break;
		case 1:// 컨텐츠통계
			if(!"".equals(strsdate)) {
				parameterJSON.put("sdate", new JSONString(strsdate));
			}
			if(!"".equals(stredate)) {
				parameterJSON.put("edate", new JSONString(stredate+ " 23:59:59"));
			}
			
			parameterJSON.put("cmd", new JSONString("SELECT_ANALYSIS_OTHERCONTENTS"));
			parameterJSON.put("mode", new JSONString(mode.getSelectedIndex()+""));
			String dbname1 = "ARTICLE_MASTER";
			String dbname2 = "DATABASE_MASTER";
			if(mode.getSelectedIndex() == 2) {
				dbname1 = "DATABASE_MASTER";
				dbname2 = "ARTICLE_MASTER";
			}
			parameterJSON.put("dbname1", new JSONString(dbname1));
			parameterJSON.put("dbname2", new JSONString(dbname2));
			
			if(areatype.getSelectedIndex()>0 && !areatype.getValues().get(areatype.getSelectedIndex()).toString().equals("0")) {
				parameterJSON.put("areacode", new JSONString(areatype.getValues().get(areatype.getSelectedIndex()).toString()));
				if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
					parameterJSON.put("sigungucode", new JSONString(sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString()));
				}
			}
			if(contenttype.getSelectedIndex()>0 && !contenttype.getValues().get(contenttype.getSelectedIndex()).toString().equals("0")) 
				parameterJSON.put("contenttype", new JSONString(contenttype.getValues().get(contenttype.getSelectedIndex()).toString()));
			if(!edname.getText().trim().equals("")) 
				parameterJSON.put("title", new JSONString(edname.getText().trim()));
			parameterJSON.put("offset", new JSONString(offset+""));
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
						int usrCnt = bodyResultObj.size();
						if (usrCnt == 0) {
							MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
						}
						
						countLabel.setText(bodyResultcnt.get("CNT").toString()+" 건");
						totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							ContentTableRow tableRow = table.addRow(Color.WHITE 
									,""+(index++)
									,obj.get("title")!=null?obj.get("title").isString().stringValue():""
									,obj.get("pageview")!=null?obj.get("pageview").isNumber().doubleValue()+"":"0"
									,obj.get("course")!=null?obj.get("course").isNumber().doubleValue()+"":"0"
									,obj.get("fav")!=null?obj.get("fav").isNumber().doubleValue()+"":"0"
									,obj.get("share")!=null?obj.get("share").isNumber().doubleValue()+"":"0"
									,obj.get("likes")!=null?obj.get("likes").isNumber().doubleValue()+"":"0"
									,obj.get("zikimi")!=null?obj.get("zikimi").isNumber().doubleValue()+"":"0"
									,obj.get("comment")!=null?obj.get("comment").isNumber().doubleValue()+"":"0"
									,obj.get("print")!=null?obj.get("print").isNumber().doubleValue()+"":"0"
								);
							tableRow.addClickHandler(e->{});
						}
						JSONArray bodyResulttot = (JSONArray) bodyObj.get("resultTot");
						int pageview=0, course=0, fav=0, share=0, likes=0, zikimi=0, comment=0, print=0;
						
//						Console.log(bodyResulttot.size() + "건");
						for(int i= 0;i< bodyResulttot.size();i++) {
							if(bodyResulttot.get(i) == null || bodyResulttot.get(i).toString().equals("null")) continue;
							JSONObject obj = (JSONObject)bodyResulttot.get(i);
							pageview += obj.get("pageview")!=null?obj.get("pageview").isNumber().doubleValue():0;
							course += obj.get("course")!=null?obj.get("course").isNumber().doubleValue():0;
							fav += obj.get("fav")!=null?obj.get("fav").isNumber().doubleValue():0;
							share += obj.get("share")!=null?obj.get("share").isNumber().doubleValue():0;
							likes += obj.get("likes")!=null?obj.get("likes").isNumber().doubleValue():0;
							zikimi += obj.get("zikimi")!=null?obj.get("zikimi").isNumber().doubleValue():0;
							comment += obj.get("comment")!=null?obj.get("comment").isNumber().doubleValue():0;
							print += obj.get("print")!=null?obj.get("print").isNumber().doubleValue():0;
						}
						table.addTagRow(Color.GREY_LIGHTEN_2, "","합 계"
								,pageview+""
								,course+""
								,fav+""
								,share+""
								,likes+""
								,zikimi+""
								,comment+""
								,print+""
							);
					} else getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			});
			break;
		case 2: //태그통계
			parameterJSON.put("cmd", new JSONString("SELECT_ANALYSIS_OTHERTAG"));
			parameterJSON.put("offset", new JSONString(offset+""));
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
						countLabel.setText(bodyResultcnt.get("CNT").toString()+" 건");
						totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
						
						int usrCnt = bodyResultObj.size();
						if (usrCnt == 0) {
							MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
						}
						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							ContentTableRow tableRow = table.addRow(Color.WHITE
									,""+(index++)
									,"#"+obj.get("name")!=null?obj.get("name").isString().stringValue()+"":""
									,obj.get("cnt")!=null?obj.get("cnt").isNumber().doubleValue()+"":"0"
									);
							tableRow.addClickHandler(e->{});
						}
						JSONObject bodyResulttot = (JSONObject) bodyObj.get("resultTot");
						table.addTagRow(Color.GREY_LIGHTEN_2, "","합 계"
								,(bodyResulttot.get("tot")!=null?bodyResulttot.get("tot").isNumber().doubleValue():0)+""
								);
					} else getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			});
			break;
		}
		table.loading(false);
	}
	
	private void CreateConnectUserBuild() {
		
		table.getTag().clear();
		
		MaterialRow row1 = new MaterialRow();
		
		
		addLabel(row1,"내부 접속자 수 ",TextAlign.CENTER,Color.GREY_LIGHTEN_2,"s2",FontWeight.BOLD);
		InConnectLabel = addLabel(row1,"",TextAlign.CENTER,Color.GREY_LIGHTEN_2,"s2",FontWeight.NORMAL);
		
		addLabel(row1,"외부 접속자 수 ",TextAlign.CENTER,Color.GREY_LIGHTEN_2,"s2",FontWeight.BOLD);
		OutConnectLabel = addLabel(row1,"",TextAlign.CENTER,Color.GREY_LIGHTEN_2,"s2",FontWeight.NORMAL);
		
		addLabel(row1,"총 접속자 수 ",TextAlign.CENTER,Color.GREY_LIGHTEN_2,"s2",FontWeight.BOLD);
		AllConnectLabel = addLabel(row1,"",TextAlign.CENTER,Color.GREY_LIGHTEN_2,"s2",FontWeight.NORMAL);
		table.getTag().add(row1);
	}
	
	private MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid,FontWeight fontWeight) {
		
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setFontWeight(fontWeight);
		tmpLabel.setLineHeight(38);
		tmpLabel.setHeight("38px");
		tmpLabel.setBackgroundColor(bgColor);
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}

}
