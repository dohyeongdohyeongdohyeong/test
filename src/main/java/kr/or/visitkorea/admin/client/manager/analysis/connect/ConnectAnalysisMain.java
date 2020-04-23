package kr.or.visitkorea.admin.client.manager.analysis.connect;


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
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ConnectAnalysisMain extends AbstractContentPanel {

//	private MainAnalysisApplication appview;
//	private MemberQnaMain self;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> stype, areatype, sigungutype;
	private int offset;

	private MaterialInput sdate, edate;
//	private MaterialInput edname;
	private int totcnt;
	
//	private int index;
	
//	private String sort = "";
	
	public ConnectAnalysisMain(MaterialExtentsWindow materialExtentsWindow, ConnectAnalysisApplication pa) {
		super(materialExtentsWindow);
//		appview = pa;
//		self = this;
	}
	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		stype = new MaterialComboBox<>();
		stype.setWidth("150px");
		stype.setLayoutPosition(Position.ABSOLUTE);
		stype.setTop(0);
		stype.setLeft(10);
		//SNS 타입 : 000 :: FACEBOOK,		001 :: TWITTER,		002 :: INSTAGRAM,		003 :: DAUM,		004 :: NAVER,		005 :: KAKAOTALK,		006 :: GOOGLE
		stype.addItem("전체", "-");
		stype.addItem("FACEBOOK", "0");
		stype.addItem("TWITTER", "1");
		stype.addItem("INSTAGRAM", "2");
		stype.addItem("DAUM", "3");
		stype.addItem("NAVER", "4");
		stype.addItem("KAKAOTALK", "5");
		stype.addItem("GOOGLE", "6");
		stype.addItem("Partners", "10");
		stype.setSelectedIndex(0);
		this.add(stype);
		stype.addValueChangeHandler(e->{
			qryList(true);
		});

		areatype = new MaterialComboBox<>();
		areatype.setWidth("200px");
		areatype.setLayoutPosition(Position.ABSOLUTE);
		areatype.setTop(0);
		areatype.setLeft(220);
		this.add(areatype);
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
						areatype.addItem(obj.get("name").isString().stringValue(), obj.get("code").isString().stringValue()+"^"+obj.get("name").isString().stringValue());
					}
					areatype.setSelectedIndex(0);
				}
			}
		});
		areatype.addValueChangeHandler(ee->{
			sigungutype.clear();
			JSONObject paramJSON = new JSONObject();
			paramJSON.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			paramJSON.put("areacode", new JSONString(areacode));
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
								sigungutype.addItem(obj.get("sigungu").isString().stringValue(), obj.get("code").isString().stringValue()+"^"+obj.get("sigungu").isString().stringValue());
						}
						sigungutype.setSelectedIndex(0);
						qryList(true);
					}
				}
			});
		});
		
		sigungutype = new MaterialComboBox<>();
		sigungutype.setWidth("200px");
		sigungutype.setLayoutPosition(Position.ABSOLUTE);
		sigungutype.setTop(0);
		sigungutype.setLeft(470);
		this.add(sigungutype);
		sigungutype.addValueChangeHandler(ee->{
			qryList(true);
		});
		
		MaterialButton btnXlsDn = new MaterialButton("결과 XLS 다운로드");
		btnXlsDn.setLayoutPosition(Position.ABSOLUTE);
		btnXlsDn.setTop(610);
		btnXlsDn.setLeft(10);
		this.add(btnXlsDn);
		
		btnXlsDn.addClickHandler(e->{
			if(countLabel.getText().startsWith("0")) {
				getMaterialExtentsWindow().alert("결과 목록이 없습니다.", 500);
				return;
			}
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			downurl += "&select_type=analysis_connect";
			
			String strsdate = sdate.getText().toString();
			if(!"".equals(strsdate)) {
				downurl += "&sdate="+strsdate.replaceAll("-", "");
			}
			String stredate = edate.getText().toString();
			if(!"".equals(stredate)) {
				downurl += "&edate="+stredate.replaceAll("-", "");
			}
			String st = stype.getValues().get(stype.getSelectedIndex()).toString();
			if(!st.equals("-"))
				downurl += "&stype="+st;
			
			if(areatype.getSelectedIndex()>0 && !areatype.getValues().get(areatype.getSelectedIndex()).toString().equals("0")) {
				String areaname = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[1];
				downurl += "&areaname="+areaname;
				if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
					String sigunguname = sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().split("\\^")[1];
					downurl += "&sigunguname="+sigunguname;
				}
			}
			Window.open(downurl,"_self", "enabled");
		});
		buildTableTab();
	}

	private MaterialPanel mptable;
	
	private void buildTableTab() {
		mptable = new MaterialPanel();
		mptable.setWidth("100%");
		mptable.setHeight("670px");
		mptable.setBackgroundColor(Color.WHITE);
		
		MaterialPanel mrtop = new MaterialPanel();
		mrtop.setWidth("100%");
		mrtop.setHeight("80px");
		mrtop.setPadding(10);
		
		MaterialLabel lbrange = new MaterialLabel("기간 : ");
		lbrange.setLayoutPosition(Position.ABSOLUTE);
		lbrange.setTop(30);
		lbrange.setRight(430);
		lbrange.setWidth("70px");
		mrtop.add(lbrange);
		sdate = new MaterialInput(InputType.DATE);
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setTop(15);
		sdate.setRight(280);
		sdate.setWidth("150px");
		mrtop.add(sdate);
		edate = new MaterialInput(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(15);
		edate.setRight(80);
		edate.setWidth("150px");
		mrtop.add(edate);
		
		mptable.add(mrtop);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setHeight(550);
		table.setWidth("98.5%");
		table.setMargin(10);
		table.setTop(40);
		table.appendTitle("매체", 300, TextAlign.CENTER);
		table.appendTitle("시도", 400, TextAlign.CENTER);
		table.appendTitle("시군구", 400, TextAlign.CENTER);
		table.appendTitle("접속횟수", 200, TextAlign.CENTER);
		
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
		countLabel.setRight(100);
		countLabel.setTop(578);
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		mptable.add(countLabel);
		
		this.add(mptable);

	}

	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
//			index = 1;
			table.clearRows();
		} else offset += 20;
		table.loading(true);
		JSONObject parameterJSON = new JSONObject();
		String st = stype.getValues().get(stype.getSelectedIndex()).toString();
		if(!st.equals("-"))
			parameterJSON.put("stype", new JSONString(st));
		
//		Console.log(areatype.getLabel().getText());
		if(areatype.getSelectedIndex()>0 && !areatype.getValues().get(areatype.getSelectedIndex()).toString().equals("0")) {
			String areaname = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[1];
			parameterJSON.put("areaname", new JSONString(areaname));
			if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
				String sigunguname = sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().split("\\^")[1];
				parameterJSON.put("sigunguname", new JSONString(sigunguname));
			}
		}
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("sdate", new JSONString(strsdate.replaceAll("-", "")));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("edate", new JSONString(stredate.replaceAll("-", "")));
		}
		parameterJSON.put("offset", new JSONString(offset+""));
		parameterJSON.put("cmd", new JSONString("SELECT_ANALYSIS_CONNECT"));
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
						int nstype = (int)(obj.get("stype")!=null?obj.get("stype").isNumber().doubleValue():-1);
						String sstype = "미로그인";
						//SNS 타입 : 000 :: FACEBOOK,		001 :: TWITTER,		002 :: INSTAGRAM,		003 :: DAUM,		004 :: NAVER,		005 :: KAKAOTALK,		006 :: GOOGLE
						if(nstype == 0) sstype="FACEBOOK";
						else if(nstype == 1) sstype="TWITTER";
						else if(nstype == 2) sstype= "INSTAGRAM";
						else if(nstype == 3) sstype = "DAUM";
						else if(nstype == 4) sstype = "NAVER";
						else if(nstype == 5) sstype = "KAKAOTALK";
						else if(nstype == 6) sstype = "GOOGLE";
						else if(nstype == 10) sstype = "Partners";
						ContentTableRow tableRow = table.addRow(Color.WHITE 
								,sstype
								,obj.get("areaname")!=null?obj.get("areaname").isString().stringValue():"[null]"
								,obj.get("sigunguname")!=null?obj.get("sigunguname").isString().stringValue():"[null]"
								,obj.get("cnt")!=null?obj.get("cnt").isNumber().doubleValue()+"":"0"
							);
						tableRow.addClickHandler(e->{});
					}
					
				} else getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
			}
		});
		table.loading(false);
	}
}
