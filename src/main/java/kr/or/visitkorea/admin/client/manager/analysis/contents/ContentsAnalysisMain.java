package kr.or.visitkorea.admin.client.manager.analysis.contents;


import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.inputmask.MaterialInputMask;
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
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsAnalysisMain extends AbstractContentPanel {

//	private MainAnalysisApplication appview;
//	private MemberQnaMain self;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> cbmtype,contenttype, areatype, sigungutype;
	private MaterialInputMask<Object> edname;
	private int offset;

	private MaterialInput sdate, edate;
//	private MaterialInput edname;
	private int totcnt;
	
	private int index;
	
//	private String sort = "";
	
	public ContentsAnalysisMain(MaterialExtentsWindow materialExtentsWindow, ContentsAnalysisApplication pa) {
		super(materialExtentsWindow);
//		appview = pa;
//		self = this;
	}
	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		areatype = new MaterialComboBox<>();
		areatype.setLayoutPosition(Position.ABSOLUTE);
		areatype.setTop(5);areatype.setLeft(10);areatype.setWidth("120px");
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
		sigungutype.setLayoutPosition(Position.ABSOLUTE);
		sigungutype.setTop(5); sigungutype.setLeft(140); sigungutype.setWidth("100px");
		this.add(sigungutype);
		sigungutype.addValueChangeHandler(ee->{
			qryList(true);
		});
		
		cbmtype = new MaterialComboBox<>();
		cbmtype.setLayoutPosition(Position.ABSOLUTE);
		cbmtype.setTop(5); cbmtype.setLeft(260);cbmtype.setWidth("100px");
		cbmtype.addItem("기사", "0");
		cbmtype.addItem("DB", "1");
		cbmtype.setSelectedIndex(0);
		this.add(cbmtype);
		cbmtype.addValueChangeHandler(e->{
			contenttype.clear();
			contenttype.addItem("전체",0);
			if(cbmtype.getSelectedIndex() == 0) {
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
			} else {
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
		
		contenttype = new MaterialComboBox<>();
		contenttype.setLayoutPosition(Position.ABSOLUTE);
		contenttype.setTop(5); contenttype.setLeft(370); contenttype.setWidth("200px");
		contenttype.addItem("전체",0);
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
		this.add(contenttype);
		contenttype.addValueChangeHandler(e->{
			qryList(true);
		});
		contenttype.setSelectedIndex(0);
		
		edname = new MaterialInputMask<Object>();
		edname.setLabel("콘텐츠 명");
		edname.setTop(5); edname.setLeft(610); edname.setWidth("310px"); 
		edname.setLayoutPosition(Position.ABSOLUTE);
		edname.setEnabled(true);
		
		this.add(edname);
		
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
			downurl += "&select_type=analysis_contents";
			
			String strsdate = sdate.getText().toString();
			if(!"".equals(strsdate)) {
				downurl += "&sdate="+strsdate.replaceAll("-", "");
			}
			String stredate = edate.getText().toString();
			if(!"".equals(stredate)) {
				downurl += "&edate="+stredate.replaceAll("-", "");
			}
				
			if(cbmtype.getSelectedIndex() == 0)
				downurl += "&maintype=ARTICLE_MASTER";
			else
				downurl += "&maintype=DATABASE_MASTER";
			if(contenttype.getSelectedIndex()>0 && !contenttype.getValues().get(contenttype.getSelectedIndex()).toString().equals("0")) 
				downurl += "&contenttype="+contenttype.getValues().get(contenttype.getSelectedIndex()).toString();
			
			if(areatype.getSelectedIndex()>0 && !areatype.getValues().get(areatype.getSelectedIndex()).toString().equals("0")) {
				downurl += "&areacode="+areatype.getValues().get(areatype.getSelectedIndex()).toString();
				if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
					downurl += "&sigungucode="+sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString();
				}
			}
			if(!edname.getText().trim().equals("")) 
				downurl += "&title="+edname.getText().trim();
			
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
		sdate.setTop(20); sdate.setRight(280); sdate.setWidth("150px");
		mrtop.add(sdate);
		edate = new MaterialInput(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(20); edate.setRight(80); edate.setWidth("150px");
		mrtop.add(edate);
		
		mptable.add(mrtop);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(40); table.setWidth("98.5%"); table.setHeight(550);
		table.setTagVisible(true);
		table.setMargin(10);
		table.appendTitle("번호", 60, TextAlign.CENTER);
		table.appendTitle("컨텐츠명", 500, TextAlign.CENTER);
		table.appendTitle("페이지뷰", 110, TextAlign.CENTER);
		table.appendTitle("즐겨찾기", 110, TextAlign.CENTER);
		table.appendTitle("공유하기", 110, TextAlign.CENTER);
		table.appendTitle("좋아요", 110, TextAlign.CENTER);
		table.appendTitle("관광정보지킴이", 110, TextAlign.CENTER);
		table.appendTitle("댓글수", 110, TextAlign.CENTER);
		table.appendTitle("인쇄수", 110, TextAlign.CENTER);
		table.appendTitle("발도장", 110, TextAlign.CENTER);
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
		countLabel.setTop(578); countLabel.setRight(100); 
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
		table.loading(true);
		JSONObject parameterJSON = new JSONObject();
		
		if(cbmtype.getSelectedIndex() == 0)
			parameterJSON.put("maintype", new JSONString("ARTICLE_MASTER"));
		else
			parameterJSON.put("maintype", new JSONString("DATABASE_MASTER"));
		if(contenttype.getSelectedIndex()>0 && !contenttype.getValues().get(contenttype.getSelectedIndex()).toString().equals("0")) 
			parameterJSON.put("contenttype", new JSONString(contenttype.getValues().get(contenttype.getSelectedIndex()).toString()));
		
		if(areatype.getSelectedIndex()>0 && !areatype.getValues().get(areatype.getSelectedIndex()).toString().equals("0")) {
			parameterJSON.put("areacode", new JSONString(areatype.getValues().get(areatype.getSelectedIndex()).toString()));
			if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
				parameterJSON.put("sigungucode", new JSONString(sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString()));
			}
		}
		if(!edname.getText().trim().equals("")) 
			parameterJSON.put("title", new JSONString(edname.getText().trim()));
		
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("sdate", new JSONString(strsdate.replaceAll("-", "")));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("edate", new JSONString(stredate.replaceAll("-", "")));
		}
		parameterJSON.put("offset", new JSONString(offset+""));
		parameterJSON.put("cmd", new JSONString("SELECT_ANALYSIS_CONTENTS"));
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
								,obj.get("title")!=null?obj.get("title").isString().stringValue():"[null]"
								,obj.get("pageview")!=null?obj.get("pageview").isNumber().doubleValue()+"":"0"
								,obj.get("fav")!=null?obj.get("fav").isNumber().doubleValue()+"":"0"
								,obj.get("share")!=null?obj.get("share").isNumber().doubleValue()+"":"0"
								,obj.get("likes")!=null?obj.get("likes").isNumber().doubleValue()+"":"0"
								,obj.get("zikimi")!=null?obj.get("zikimi").isNumber().doubleValue()+"":"0"
								,obj.get("comment")!=null?obj.get("comment").isNumber().doubleValue()+"":"0"
								,obj.get("print")!=null?obj.get("print").isNumber().doubleValue()+"":"0"
								,obj.get("stamp")!=null?obj.get("stamp").isNumber().doubleValue()+"":"0"
							);
//						tableRow.put("cotid", obj.get("cotid").isString().stringValue());
//						tableRow.put("title", obj.get("title")!=null?obj.get("title").isString().stringValue():"[null]");
						tableRow.addClickHandler(e->{
							ContentTableRow row = (ContentTableRow)e.getSource();
							if(row.getSelectedColumn() == 1) {
								String cotid = obj.get("cotid").isString().stringValue();
								String tgrUrl = (String) Registry.get("service.server") + "/detail/detail_view.html?cotid="+cotid;
								Window.open(tgrUrl, obj.get("title")!=null?obj.get("title").isString().stringValue():"[null]", "");
							}
						});
					}
					JSONArray bodyResulttot = (JSONArray) bodyObj.get("resultTot");
					int pageview=0, fav=0, share=0, likes=0, zikimi=0, comment=0, print=0, stamp=0;
					for(int i= 0;i< bodyResulttot.size();i++) {
						if(bodyResulttot.get(i) == null || bodyResulttot.get(i).toString().equals("null")) continue;
						JSONObject obj = (JSONObject)bodyResulttot.get(i);
						pageview += obj.get("pageview")!=null?obj.get("pageview").isNumber().doubleValue():0;
						fav += obj.get("fav")!=null?obj.get("fav").isNumber().doubleValue():0;
						share += obj.get("share")!=null?obj.get("share").isNumber().doubleValue():0;
						likes += obj.get("likes")!=null?obj.get("likes").isNumber().doubleValue():0;
						zikimi += obj.get("zikimi")!=null?obj.get("zikimi").isNumber().doubleValue():0;
						comment += obj.get("comment")!=null?obj.get("comment").isNumber().doubleValue():0;
						print += obj.get("print")!=null?obj.get("print").isNumber().doubleValue():0;
						stamp += obj.get("stamp")!=null?obj.get("stamp").isNumber().doubleValue():0;
					}
					table.addTagRow(Color.GREY_LIGHTEN_2, "","합 계"
							,pageview+""
							,fav+""
							,share+""
							,likes+""
							,zikimi+""
							,comment+""
							,print+""
							,stamp+""
						);
				} else getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
			}
		});
		table.loading(false);
	}
}
