package kr.or.visitkorea.admin.client.manager.contents.department;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DepartmentMain extends AbstractContentPanel {

//	private DepartmentApplication appview;
//	private DepartmentMain self;
	SelectionPanel mainTab;
	HashMap<String, Object> deptMap = new HashMap<String, Object>();
	int dtype = 0;
	MaterialPanel serviceCont, category, departName, departTel, save;
	boolean bModifyTel = false;
	public DepartmentMain(MaterialExtentsWindow materialExtentsWindow, DepartmentApplication pa) {
		super(materialExtentsWindow);
//		appview = pa;
//		self = this;
//		Timer t = new Timer() {
//			@Override
//			public void run() {
				qryall();
//			}
//		};
//		t.schedule(500);
		
	}
	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		
		buildTab();
		buildBody();
		buildbottom();
		dtype = 0;
		category.getParent().setVisible(false);
	}
	private void buildbottom() {
		MaterialRow mrbottom = new MaterialRow();
		mrbottom.setWidth("100%");
		mrbottom.setPadding(15);
		MaterialButton mbnew = new MaterialButton("신규부서 등록");
		mbnew.setFloat(Float.RIGHT);
		mbnew.setHeight("40px");
		mbnew.setBackgroundColor(Color.BLUE_DARKEN_3);
		mbnew.addClickHandler(e->{
			getMaterialExtentsWindow().openDialog(DepartmentApplication.MODIFY_DEPARTMENT_INFO, null, 720, e2->{
			    Timer t = new Timer() {
			      public void run() {
			    	  qryall();
			      }
			    };
			    t.schedule(500);
				
			});
		});
		mrbottom.add(mbnew);
		this.add(mrbottom);
	}
	private void buildBody() {
		MaterialRow mrbody = new MaterialRow();
		mrbody.setMarginTop(15);
		ScrollPanel sl = new ScrollPanel();
		serviceCont = makeContentPanel(mrbody, "서비스분류", 350);
		category = makeContentPanel(mrbody, "카테고리", 200);
		departName = makeContentPanel(mrbody, "부서명", 300);
		departTel = makeContentPanel(mrbody, "연락처", 250);
		save = makeContentPanel(mrbody, "저장", 100);
		mrbody.setPadding(15);
		sl.setHeight("550px");
		sl.add(mrbody);
		this.add(sl);
		
	}
	private void buildTab() {
		MaterialRow mp1 = new MaterialRow();
		mp1.setLayoutPosition(Position.RELATIVE);
		mp1.setHeight("25px");
		mp1.setTop(20);
		mp1.setWidth("100%");
		this.add(mp1);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("기사컨텐츠", 0);
		map.put("DB컨텐츠", 1);
		mainTab = addSelectionPanel(mp1, "s2", TextAlign.CENTER, map);
		mainTab.setSelectionOnSingleMode("기사컨텐츠");
		mainTab.addStatusChangeEvent(e->{
			dtype = (Integer)mainTab.getSelectedValue();
			if(dtype == 0) {
				category.getParent().setVisible(false);
			} else {
				category.getParent().setVisible(true);
			}
			qryList();
		});
	}
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, HashMap<String, Object> valueMap) {
		return addSelectionPanel(row, grid, align, valueMap, 5, 5, 8, true);
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
//		box.setLineHeight(46.25);
//		box.setHeight("46.25px");
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		col1.setFloat(Float.RIGHT);
		row.add(col1);
		return box;
	}
	
	// 비슷한 형태의 틀을 반복적으로 만들기 위해서 구성
	public MaterialPanel makeContentPanel(MaterialWidget parent, String title, int width) {
		MaterialPanel main = new MaterialPanel();
//		main.setLayoutPosition(Position.RELATIVE);
		main.setFloat(Float.LEFT);
		main.setHeight("100%");
		main.setWidth(width+"px");
		
		MaterialLabel ml1 = new MaterialLabel(title);
		ml1.setWidth("100%");
		ml1.setBackgroundColor(Color.GREY_LIGHTEN_3);
		ml1.setHeight("40px");
		ml1.setFontWeight(FontWeight.BOLD);
		ml1.setPaddingTop(10);
		main.add(ml1);
		
		MaterialPanel mpservice = new MaterialPanel();
		mpservice.setWidth("100%");
//		mpservice.setHeight("100px");
		mpservice.setBackgroundColor(Color.GREY_LIGHTEN_5);
		main.add(mpservice);
		parent.add(main);
		//최종 리턴되는 판낼은 데이터에 따라 계속 내부 값이 변동되는 패널을 넘긴다.
		return mpservice;
	}
	
	public void qryall() {
		bModifyTel = false;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_DEPARTMENT_INFO_LIST"));
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
						getMaterialExtentsWindow().alert("부서를 하나 이상 등록 해주세요.", 500);
					}
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						deptMap.put(obj.get("depid").isString().stringValue(), obj.get("name").isString().stringValue()+"^"+obj.get("tel").isString().stringValue());
					}
					qryList();
				}
			}
		});
		
	}
	public void qryList() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_DEPARTMENT_LIST"));
		parameterJSON.put("dtype", new JSONString(dtype+""));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					serviceCont.clear();category.clear();departName.clear();departTel.clear();save.clear();
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					for(int i= 0;i< usrCnt;i++) {
						String[] depid = {null};
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						depid[0] = obj.get("depid")!= null?obj.get("depid").isString().stringValue():null;
						String otdid = obj.get("otdid")!= null?obj.get("otdid").isString().stringValue():null;
						if (otdid.equals("ab097fc9-daa6-423d-8fcb-50aec7852e21")) {
							continue;
						}
						int[] ctype = {-1};
						MaterialLabel mlservice = new MaterialLabel(obj.get("title").isString().stringValue());
						mlservice.setWidth("100%");
						mlservice.setHeight("43px");
						mlservice.setPaddingTop(10);
						serviceCont.add(mlservice);
						if(dtype == 1 && obj.get("ctype")!=null) {
							ctype[0] =(int) Math.round(obj.get("ctype")!=null?obj.get("ctype").isNumber().doubleValue():0);
							String cname = "관광지";
							switch(ctype[0]) {
							case 14: cname = "문화시설"; break;
							case 15: cname = "축제행사공연"; break;
							case 25: cname = "여행코스"; break;
							case 28: cname = "레포츠"; break;
							case 32: cname = "숙박"; break;
							case 38: cname = "쇼핑"; break;
							case 39: cname = "음식점"; break;
							}
							MaterialLabel cbcontent = new MaterialLabel(cname);
							cbcontent.setWidth("100%");
							cbcontent.setHeight("40px");
							cbcontent.setPaddingTop(10);
							category.add(cbcontent);
						}
						
						MaterialComboBox<Object> cbname = new MaterialComboBox<>();
						cbname.addItem("선택없음", 0);
						for( String key : deptMap.keySet() ){
							cbname.addItem(deptMap.get(key).toString().split("\\^")[0], key);
				        }
						if(depid[0]!= null)
							cbname.setSelectedIndex(cbname.getValueIndex(depid[0]));
						cbname.setWidth("98%");
						cbname.setHeight("42px");
						cbname.setBackgroundColor(Color.WHITE);
						cbname.setMargin(1);
						
						departName.add(cbname);
						MaterialInput mltel = new MaterialInput();
						mltel.setText(obj.get("name")!=null?obj.get("tel").isString().stringValue():"");
						mltel.setWidth("98%");
						mltel.setHeight("40px");
						mltel.setBackgroundColor(Color.WHITE);
						mltel.setMargin(1);
						departTel.add(mltel);
						MaterialButton mbsave = new MaterialButton("저장");
						mltel.addKeyPressHandler(ee->{
							bModifyTel = true;
							mbsave.setEnabled(true);
						});

						cbname.addValueChangeHandler(ee->{
							if (!cbname.getSelectedValue().get(0).toString().equals("0")) {
								depid[0] = cbname.getSelectedValue().get(0).toString();
								mltel.setText(deptMap.get(cbname.getSelectedValue().get(0).toString()).toString().split("\\^")[1]);
								mbsave.setEnabled(true);
							} else {
								depid[0] = null;
								mltel.setText("");
							}
							
						});
						
						mbsave.setFloat(Float.RIGHT);
						mbsave.setHeight("43px");
						mbsave.setEnabled(false);
						mbsave.setBackgroundColor(Color.BLUE_DARKEN_3);
						mbsave.addClickHandler(e->{
							if(depid[0] == null) {
								getMaterialExtentsWindow().alert("부서를 선택해 주세요.", 500);
								return;
							}
							JSONObject parameterJSON = new JSONObject();
							parameterJSON.put("dtype", new JSONString(dtype+""));
							parameterJSON.put("otdid", new JSONString(otdid));
							if(ctype[0] > 0)
								parameterJSON.put("ctype", new JSONString(ctype[0]+""));
							parameterJSON.put("depid", new JSONString(depid[0]));
							parameterJSON.put("cmd", new JSONString("UPDATE_DEPARTMENT_INFO_DETAIL"));
							VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
								@Override
								public void call(Object param1, String param2, Object param3) {
									JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
									JSONObject headerObj = (JSONObject) resultObj.get("header");
									String processResult = headerObj.get("process").isString().stringValue();
									if (processResult.equals("success")) {
										mbsave.setEnabled(false);
									}
								}
							});
							
							if(bModifyTel && depid[0] != null) {
								parameterJSON = new JSONObject();
								parameterJSON.put("depid", new JSONString(depid[0]));
								parameterJSON.put("tel", new JSONString(mltel.getText().trim()));
								parameterJSON.put("cmd", new JSONString("UPDATE_DEPARTMENT_INFO"));
								VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
									@Override
									public void call(Object param1, String param2, Object param3) {
										JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
										JSONObject headerObj = (JSONObject) resultObj.get("header");
										String processResult = headerObj.get("process").isString().stringValue();
										if (processResult.equals("success")) {
											
										}
									}
								});
							}
						});
						save.add(mbsave);
					}

				} else {
					getMaterialExtentsWindow().alert("검색 오류 입니다. 인터넷을 확인해 주세요.", 500);
				}
			}
		});
	}
}
