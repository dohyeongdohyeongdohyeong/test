package kr.or.visitkorea.admin.client.manager.analysis.tag;


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

public class TagAnalysisMain extends AbstractContentPanel {

//	private MainAnalysisApplication appview;
//	private MemberQnaMain self;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> cbmtype;
	private int offset;

	private MaterialInput sdate, edate;
	private MaterialInput edname;
	private int totcnt;
	
	private int index;
	
//	private String sort = "";
	
	public TagAnalysisMain(MaterialExtentsWindow materialExtentsWindow, TagAnalysisApplication pa) {
		super(materialExtentsWindow);
//		appview = pa;
//		self = this;
	}
	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		
		cbmtype = new MaterialComboBox<>();
		cbmtype.setWidth("200px");
		cbmtype.setLayoutPosition(Position.ABSOLUTE);
		cbmtype.setTop(0);
		cbmtype.setLeft(10);
		
		cbmtype.addItem("전체", "-");
		cbmtype.addItem("전체메인", "0");
		cbmtype.addItem("기사메인", "1");
		cbmtype.addItem("코스메인", "2");
		cbmtype.addItem("명소메인", "3");
		cbmtype.addItem("축제메인", "4");
		
		cbmtype.setSelectedIndex(0);
		this.add(cbmtype);
		cbmtype.addValueChangeHandler(e->{
			qryList(true);
		});
		
		MaterialLabel lbrange = new MaterialLabel("태그명 : ");
		lbrange.setLayoutPosition(Position.ABSOLUTE);
		lbrange.setTop(30);
		lbrange.setLeft(270);
		lbrange.setWidth("100px");
		this.add(lbrange);
		edname = new MaterialInput();
		edname.setWidth("500px");
		edname.setLayoutPosition(Position.ABSOLUTE);
		edname.setTop(20);
		edname.setLeft(370);
		
		this.add(edname);
		
		MaterialButton btnXlsDn = new MaterialButton("결과 XLS 다운로드");
		btnXlsDn.setLayoutPosition(Position.ABSOLUTE);
		btnXlsDn.setTop(610);
		btnXlsDn.setLeft(10);
		this.add(btnXlsDn);
		
		btnXlsDn.addClickHandler(e->{
			if(totcnt < 1) {
				getMaterialExtentsWindow().alert("결과 목록이 없습니다.", 500);
				return;
			}
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS&select_type=analysis_tag";
			String mtype = cbmtype.getValues().get(cbmtype.getSelectedIndex()).toString();
			if(mtype != null && !"-".equals(mtype))
				downurl += "&mtype="+mtype;
			
			if(!"".equals(edname.getText())) {
				downurl += "&name="+edname.getText();
			}
			
			String strsdate = sdate.getText().toString();
			if(!"".equals(strsdate)) {
				downurl += "&sdate="+strsdate.replaceAll("-", "");
			}
			String stredate = edate.getText().toString();
			if(!"".equals(stredate)) {
				downurl += "&edate="+stredate.replaceAll("-", "");
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
		table.setHeight(550); table.setWidth("98.5%");
		table.setTagVisible(true);
		table.setMargin(10);
		table.setTop(40);
		table.appendTitle("순위", 300, TextAlign.CENTER);
		table.appendTitle("태그명", 700, TextAlign.CENTER);
		table.appendTitle("클릭수", 300, TextAlign.CENTER);
		
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
		countLabel.setTop(580);
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		mptable.add(countLabel);
		
		this.add(mptable);

	}

	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_ANALYSIS_TAG"));
		
		String mtype = cbmtype.getValues().get(cbmtype.getSelectedIndex()).toString();
		if(mtype != null && !"-".equals(mtype))
			parameterJSON.put("mtype", new JSONString(mtype));
		
		if(!"".equals(edname.getText())) {
			parameterJSON.put("name", new JSONString(edname.getText()));
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
					countLabel.setText(bodyResultcnt.get("CNT")+" 건");
					totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						ContentTableRow tableRow = table.addRow(Color.WHITE 
								,""+(index++)
								,"#"+obj.get("name")!=null?obj.get("name").isString().stringValue()+"":"[null]"
								,obj.get("cnt")!=null?obj.get("cnt").isNumber().doubleValue()+"":"0"
								);
						
//						tableRow.put("qnaid", obj.get("qnaid").isString().stringValue());
						tableRow.addClickHandler(e->{
								
						});
					}
					JSONObject bodyResulttot = (JSONObject) bodyObj.get("resultTot");
					table.addTagRow(Color.GREY_LIGHTEN_2, "", "합 계"
							,(bodyResulttot.get("tot")!=null?bodyResulttot.get("tot").isNumber().doubleValue():0)+""
							);
				} else {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
//					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}
}
