package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.random.ValueServer;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;


/**
 * 
 * history 데이터의 추가/제거/관리용 다이얼로그 페이지를 생성하는 클래스.
 * 
 * 메인 다이얼로그 화면인 HistoryManagementDialog 클래스, 
 * 다이얼로그 중앙의 가변영역 컴포넌트인 DatabaseContentAddComponent (내부)클래스와  ManualHistoryInformAddComponent (내부)클래스, 
 * 히스토리 데이터 테이블인 SpotListTable (내부)클래스 로 구성됨.
 * 
 * 
 * @author 		dohyeong
 * @authorDate	2020-03-16
 * @see			kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree
 *
 */
public class HistoryManagementDialog extends DialogContent {
	
	private MaterialRow container;
	private SelectionPanel historyManagementMode;
	private DatabaseContentAddComponent databaseContentArea;
	private ManualHistoryInformAddComponent manualHistoryInformAddArea;
	private SpotListTable historyListTable;
	
	private List<String> removeHistoryIdList;	// 최종 적용 시에 DB에서 삭제할 history들의 고유 ID들을 담고있는 리스트.
	private List<String> historyCotIdList;	// 화면 하단 공통영역 history 테이블에 추가되어 있는 spot 컨텐츠(업소)의 COT_ID 리스트. 
											// 실제 DB에 저장된 목록과 메모리 상 추가되어 있는 목록을 모두 포함함.
											// 화면 중앙 영역의 DB 검색 테이블에 나타나는 정보를 제한하기 위하여 사용함.
	
	private final String OTD_ID;
	private String MASTER_COT_ID;
	private boolean hideAuthCode; //인증번호 관련 입/출력 창의 노출 여부. true 시 화면에서 숨긴다.

	

	public HistoryManagementDialog(MaterialExtentsWindow window) {
		super(window);
		
		this.removeHistoryIdList = new ArrayList<String>();
		this.historyCotIdList = new ArrayList<String>();
		this.OTD_ID = window.getValueMap().get("OTD_ID").toString();
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void init() {
		
		hideAuthCodeData();////////// 만약 모든 부서에서 인증코드 관련 항목들을 노출시키게 하려면 이 명령어를 지워버리면 됩니다..
		
		this.container = new MaterialRow();	this.add(container);
		
		initTopSelectionArea();
		initMiddleSwitchingBodyArea();
		initBottomHistoryListArea();
		
		initBottomButtonArea();
		
	}
	
	
			/** 
			 * history 관리 다이얼로그 창 최상단의, 
			 * history 입력 모드를 설정하는 select버튼이 있는 영역을 초기화 하는 메서드.
			 */
			private void initTopSelectionArea() {
				MaterialColumn selectionArea = new MaterialColumn(12, 12, 12);	this.container.add(selectionArea);
				selectionArea.setMarginTop(25);
				selectionArea.setMarginBottom(-10);
				
				
				
				String[] selectionTitles = {"대구석 컨텐츠 검색", "직접입력"};
				
				
				HashMap<String, Object> selectionParameters = new HashMap<String, Object>();
				int i = 0;
				for(String tmp : selectionTitles) {
					selectionParameters.put(tmp, i);
					i++;
				}
				
				
				this.historyManagementMode = new SelectionPanel();
				this.historyManagementMode.setValues(selectionParameters);
				this.historyManagementMode.setFloat(Float.LEFT);
				this.historyManagementMode.addStatusChangeEvent(e -> {
					
					int modeValue = (int) this.historyManagementMode.getSelectedValue();
					
					if(modeValue == 0) {
						this.databaseContentArea.setDisplay(Display.BLOCK);
						this.databaseContentArea.onLoad();
						this.manualHistoryInformAddArea.setDisplay(Display.NONE);
					} else if(modeValue == 1) {
						this.manualHistoryInformAddArea.setDisplay(Display.BLOCK);
						this.manualHistoryInformAddArea.onLoad();
						this.databaseContentArea.setDisplay(Display.NONE);
					}
					
				});
				this.historyManagementMode.setSelectionOnSingleMode(selectionTitles[0]);
				
				selectionArea.add(this.historyManagementMode);
			}
			
			
			/** 
			 * history 관리 다이얼로그 창 중앙의, 
			 * history 입력 모드 선택에 따라 실제 데이터를 추가하도록 화면이 변하는, 다이얼로그 중앙의 영역을 만드는 메서드.
			 */
			private void initMiddleSwitchingBodyArea() {
				
				this.container.add(this.databaseContentArea = new DatabaseContentAddComponent());
				this.container.add(this.manualHistoryInformAddArea = new ManualHistoryInformAddComponent());
				this.manualHistoryInformAddArea.setDisplay(Display.NONE);
				
			}
			
			
			/** 
			 * history 관리 다이얼로그 창 하단의, 
			 * 추가한 history 정보를 리스트 형식으로 화면에 보여주는 테이블 영역을 생성하는 메서드.
			 */
			private void initBottomHistoryListArea() {
				MaterialColumn tmp = new MaterialColumn(12, 12, 12); this.container.add(tmp);
				MaterialColumn historyListArea = new MaterialColumn(12, 12, 12);	tmp.add(historyListArea);
				historyListArea.setMarginTop(30);
		
				MaterialLabel historyTableTitle = new MaterialLabel("추가된 스팟 리스트");
				historyListArea.add(historyTableTitle);
				historyTableTitle.setFontWeight(FontWeight.BOLD);
				historyTableTitle.setMarginBottom(-30);
		
				this.historyListTable = new SpotListTable(TABLE_SELECT_TYPE.SELECT_SINGLE); historyListArea.add(this.historyListTable);
				this.historyListTable.initTableHeader(false);
			}
			
			
			/** 
			 * history 관리 다이얼로그 창 가장 아래의 버튼영역을 초기화 해 주는 메서드.
			 */
			private void initBottomButtonArea() {
				super.addDefaultButtons();
				
				MaterialButton close = super.getCloseButton();
				close.addClickHandler(handler -> {
					clearInputs();
				});
				
				
				MaterialButton save = new MaterialButton("저장");
				save.setFloat(Float.RIGHT);
				save.setMarginRight(15);
				save.addClickHandler(handler -> {
					submit();
				});
				
				
				MaterialButton refresh = new MaterialButton("재설정");
				refresh.setFloat(Float.RIGHT);
				refresh.setMarginRight(15);
				refresh.addClickHandler(handler -> {
					resetSettings();
				});
				

				super.getButtonArea().add(save);
				super.getButtonArea().add(refresh);
			}
			
			
	/**
	 * 기존에 DB에 저장되어 있던 history 데이터들의 리스트를 받아와서 
	 * 화면 하단의 테이블 영역에 추가시켜 줌.
	 */
	private void fetchHistoryList() {
		JSONObject arguments = new JSONObject();
		JSONObject data = new JSONObject();
		
		data.put("OTD_ID", new JSONString(this.OTD_ID));
		data.put("MCOT_ID", new JSONString(this.MASTER_COT_ID));
		
		arguments.put("data", data);
		arguments.put("cmd", new JSONString("GET_HISTORY_LIST"));
		VisitKoreaBusiness.post("call", arguments.toString(), (o1, s1, o2) -> {
			this.historyListTable.clearRows();
			
			JSONValue historyValue = fetchJSONValueData(o1);
			if(historyValue == null) return;
			
			JSONObject historyObject = historyValue.isObject();
			JSONArray historyArray = historyObject.get("resultData").isArray();
			for(int i=0; i<historyArray.size(); i++) {
				JSONObject historyData = historyArray.get(i).isObject();
				
				this.historyListTable.addRow(historyData);
			}
			
			this.historyListTable.setDataAmount(historyArray.size());
		});
	}
	
	
	/**
	 * 화면의 미 저장한 모든 입력 데이터를 서버로 전송하여 최종 저장함.
	 */
	private void submit() {
		JSONArray argumentDataList = new JSONArray();
		
		
		int i=0;
		for(String historyId : this.removeHistoryIdList) {
			JSONObject removeId = new JSONObject();
			
			removeId.put("HIST_ID", new JSONString(historyId));
			argumentDataList.set(i++, removeId);
		}
		
		
		List<ContentTableRow> historyRowList = this.historyListTable.getRowsList();
		
		for(ContentTableRow row : historyRowList) {
			JSONObject historyData = new JSONObject();
			
			
			String spotCotId = row.getColumnObject(9).getElement().getInnerText();
			int isNewData = Integer.parseInt(row.getColumnObject(8).getElement().getInnerText());
			int areaCode = Integer.parseInt(row.getColumnObject(11).getElement().getInnerText());
			int sigugunCode = Integer.parseInt(row.getColumnObject(12).getElement().getInnerText());
			String address = row.getColumnObject(10).getElement().getInnerText();
			String title = row.getColumnObject(5).getElement().getInnerText();
			String inquiry = row.getColumnObject(13).getElement().getInnerText();
			String authCode = row.getColumnObject(1).getElement().getInnerText();
			

			historyData.put("COT_ID", new JSONString(this.MASTER_COT_ID));
			if(!spotCotId.equals("-")) {
				historyData.put("SCOT_ID", new JSONString(spotCotId));
			}
			if(isNewData == 0) {// 삭제 예정 데이터.
				continue;
			} else if(isNewData == 1) {// DB 검색 추가 데이터.
				historyData.put("DI_YN", new JSONNumber(0));
			} else if(isNewData == 2) {// 직접 입력 데이터.
				historyData.put("DI_YN", new JSONNumber(1));
				historyData.put("DI_AREA_CODE", new JSONNumber(areaCode));
				historyData.put("DI_SIGUGUN_CODE", new JSONNumber(sigugunCode));
				if(!address.equals("-")) {
					historyData.put("DI_ADDR", new JSONString(address));
				}
				if(!title.equals("-")) {
					historyData.put("DI_BSSH_NM", new JSONString(title));
				}
				if(!inquiry.equals("-")) {
					historyData.put("DI_TELNO", new JSONString(inquiry));
				}
				if(!authCode.equals("-")) {
					historyData.put("DI_AUTH_CODE", new JSONString(authCode));
				}
			}
			historyData.put("OTD_ID", new JSONString(this.OTD_ID));
			
			
			argumentDataList.set(i++, historyData);
		}
		
		
		JSONObject arguments = new JSONObject();
		arguments.put("cmd", new JSONString("POST_HISTORY_DATA"));
		arguments.put("data", argumentDataList);
		
		VisitKoreaBusiness.post("call", arguments.toString(), (o1, s1, o2) -> {
			JSONValue resultValue = fetchJSONValueData(o1);
			if(resultValue == null) return;
			
			String successMessage = resultValue.isString().stringValue();
			
			MaterialToast.fireToast(successMessage);
			
			resetSettings();
			this.manualHistoryInformAddArea.resetSettings();
		});
		

	}
	
	
	/**
	 * 모든 하위 클래스/메서드의 비동기 처리에서 사용하기 위한 메서드.
	 * 
	 * 비동기 처리 시, 콜백 함수의 파라미터 데이터(Object)를 사용해서 
	 * 서버에서 가져온 data를 return 해 주고, 
	 * 서버에서 전송된 data가 없으면 null 값을 return 하는 동시에 
	 * message를 사용해서 오류의 내용을 fireToast 출력 처리함.
	 * 
	 * @return (JSONValue) resultData;
	 */
	private JSONValue fetchJSONValueData(Object parameter1) {
		JSONValue resultData = null;
		
		
		JSONObject data = JSONParser.parseStrict(JSON.stringify((JsObject) parameter1)).isObject();
		
		JSONObject resultHeader = data.get("header").isObject();
		JSONObject resultBody = data.get("body").isObject();
		
		String processResult = resultHeader.get("process").isString().stringValue();
		
		if(processResult.equals("success")) {
			resultData = resultBody.get("result");
			
		} else {// 데이터가 없으면 이유(message)를 fireToast로 출력함.
			String reason = resultBody.get("message").isString().stringValue();
			MaterialToast.fireToast(reason);
			
		}
		
		return resultData;
	}
	
	
	/**
	 * 인증번호 관련 항목들을 숨겨놓을 지 설정하는 메서드.
	 * 품질인증 이외의 부서에서는 hideAuthCode를 true로 설정하여 인증번호 관련 항목을 숨기도록 합니다.
	 * 
	 * 만약 모든 부서에서 authCode 관련 항목을 보이게 설정하려면, 이 메서드는 그대로 놔두고, 
	 * hideAuthCode(); -> 메서드를 실행한 명령어 한 줄 만 찾아서 지워주면 됩니다.
	 */
	private void hideAuthCodeData() {
		String OTD_ID = super.getMaterialExtentsWindow().getValueMap().get("OTD_ID").toString();
		if(OTD_ID.equals("456a84d1-84c4-11e8-8165-020027310001")) {
			this.hideAuthCode = false;
		} else {
			this.hideAuthCode = true;
			
			this.setPaddingTop(5);
			this.setPaddingLeft(60);// 테이블의 인증코드 컬럼 크기의 1/2.
			this.setPaddingRight(60);
		}
	}
	
	
	/**
	 * 화면의 모든 사용자 입력 데이터를 초기화하고 처음 상태로 되돌리는 메서드.
	 */
	protected void clearInputs() {
		this.historyManagementMode.setSelectionOnSingleMode("대구석 컨텐츠 검색");
		
		this.databaseContentArea.setDisplay(Display.BLOCK);
		this.manualHistoryInformAddArea.setDisplay(Display.NONE);
		
		this.databaseContentArea.clearInputs();
		this.manualHistoryInformAddArea.clearInputs();
	}
	
	
	/**
	 * 화면의 미 저장한 모든 입력 데이터를 초기화하고, 
	 * 처음 화면 구현 시의 상태로 되돌리는 메서드.
	 * (각각의 컴포넌트(= 화면 중앙의 가변 영역)는 초기화 되지 않고, 
	 *   공통 영역(= 화면 하단 영역)만 초기화 된다.)
	 */
	protected void resetSettings() {
		onLoad();
		
		this.databaseContentArea.resetSettings();
	}
	
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		this.MASTER_COT_ID = super.getParameters().get("COT_ID").toString();
		
		this.removeHistoryIdList = new ArrayList<String>();
		this.historyCotIdList = new ArrayList<String>();
		
		this.databaseContentArea.databaseListTable.clearRows();
		
		fetchHistoryList();
	}
	
	
	
	
	
	
//////////    //////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    	
	/**
	 * 
	 * HistoryManagementDialog 중앙 부분의 대구석 DB 컨텐츠 검색 컴포넌트.
	 * 
	 */
	class DatabaseContentAddComponent extends MaterialColumn {
		
		private MaterialComboBox<Integer> stateProvince;// 지역.
		private MaterialComboBox<Integer> city;// 시군구.
		private MaterialComboBox<String> searchType;// 검색 방법.
		private MaterialTextBox keyword;
		private MaterialButton search;
		
		private SpotListTable databaseListTable;
		
		DatabaseContentAddComponent() {
			super(12, 12, 12);
			
			initArea();
		}
		
		
		
		private void initArea() {
			super.setHeight("275px");
			
			initSearchToolsArea();
			initSpotListTable();
		}
		
		
		
				private void initSearchToolsArea() {
					MaterialColumn stateArea = new MaterialColumn(2, 2, 2);			this.add(stateArea);
					MaterialColumn cityArea = new MaterialColumn(2, 2, 2);			this.add(cityArea);
					MaterialColumn searchTypeArea = new MaterialColumn(2, 2, 2);	this.add(searchTypeArea);
					MaterialColumn keywordArea = new MaterialColumn(4, 4, 4);		this.add(keywordArea);
					MaterialColumn searchButtonArea = new MaterialColumn(2, 2, 2);	this.add(searchButtonArea);
					
					initStateArea(stateArea);
					initCityArea(cityArea);
					initSearchTypeArea(searchTypeArea);
					initKeywordArea(keywordArea);
					initSearchButtonArea(searchButtonArea);
				};
				
				
				
						private void initStateArea(MaterialColumn stateArea) {
							stateArea.add(this.stateProvince = new MaterialComboBox<Integer>());
		
							this.stateProvince.addItem("전체 지역", 0);
							
							JSONObject arguments = new JSONObject();
							arguments.put("cmd", new JSONString("SELECT_AREA"));
							VisitKoreaBusiness.post("call", arguments.toString(), (o1, s1, o2) -> {
								JSONValue resultValue = fetchJSONValueData(o1);
								if(resultValue == null) return;
								
								JSONArray areaList = resultValue.isArray();
								
								for(int i=0; i<areaList.size(); i++) {
									JSONObject areaData = areaList.get(i).isObject();
									
									this.stateProvince.addItem(areaData.get("name").isString().stringValue(), Integer.parseInt(areaData.get("code").isString().stringValue()));
								}
								this.stateProvince.setSelectedIndex(0);
							});
							
							
							this.stateProvince.addValueChangeHandler(handler -> {
								this.city.clear();
								this.city.addItem("전체 시군구", 0);
								
								if(this.stateProvince.getSelectedValue().get(0) == 0) {
									return;
								}
								
								JSONObject data = new JSONObject();
								data.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
								data.put("areacode", new JSONString(this.stateProvince.getSelectedValue().get(0).toString()));
								VisitKoreaBusiness.post("call", data.toString(), (o1, s1, o2) -> {
									JSONValue resultValue = fetchJSONValueData(o1);
									if(resultValue == null) return;
									
									JSONArray sigugunList = resultValue.isArray();

									for(int i=0; i<sigugunList.size(); i++) {
										JSONObject sigugunData = sigugunList.get(i).isObject();
										
										if(!sigugunData.get("code").isString().stringValue().equals("0")) {
											this.city.addItem(sigugunData.get("sigungu").isString().stringValue(), Integer.parseInt(sigugunData.get("code").isString().stringValue()));											
										}
									}
									this.city.setSelectedIndex(0);
								});
							});
						}
						
						
						private void initCityArea(MaterialColumn cityArea) {
							cityArea.add(this.city = new MaterialComboBox<Integer>());
							
							this.city.addItem("전체 시군구", 0);
							
							this.city.addValueChangeHandler(handler -> {
								
							});
						}
						
						
						private void initSearchTypeArea(MaterialColumn searchTypeArea) {
							searchTypeArea.add(this.searchType = new MaterialComboBox<String>());
		
							this.searchType.addItem("업소명");
							this.searchType.addItem("CID");
							if(! hideAuthCode) {
								this.searchType.addItem("인증번호");
							}
						}
						
						
						private void initKeywordArea(MaterialColumn keywordArea) {
							keywordArea.add(this.keyword = new MaterialTextBox("검색어를 입력해주세요."));
							
							this.keyword.addKeyDownHandler(handler -> {
								if(handler.getNativeKeyCode() == 13) {
									resetSettings();
								}
							});
						}
						
						
						private void initSearchButtonArea(MaterialColumn searchButtonArea) {
							searchButtonArea.add(this.search = new MaterialButton("검색"));
							this.search.setFloat(Float.RIGHT);
							this.search.setMarginTop(20);
							
							this.search.addClickHandler(handler -> {
								resetSettings();
							});
						}
				
				
				
				private void initSpotListTable() {
					MaterialColumn tableArea = new MaterialColumn(12, 12, 12);	this.add(tableArea);
					tableArea.setMarginTop(-30);
					
					this.databaseListTable = new SpotListTable(TABLE_SELECT_TYPE.SELECT_SINGLE);	tableArea.add(this.databaseListTable);
					this.databaseListTable.initTableHeader(true);
//					search(new JSONObject());	// history로 사용중인 정보(List<String> historyCotIdList;)에 대한 초기화가 이루어진 후에 검색되어야 함.
												// 따라서 하단 historyTable 영역이 초기화 된 뒤에 실행되어야 정상적인 작동함.
				};
		
		
		
		private void search() {
			JSONObject arguments = new JSONObject();
			
			arguments.put("AREA_CODE", new JSONNumber(this.stateProvince.getSelectedValue().get(0)));
			arguments.put("SIGUGUN_CODE", new JSONNumber(this.city.getSelectedValue().get(0)));
			arguments.put("searchType", new JSONString(this.searchType.getSelectedValue().get(0)));
			arguments.put("keyword", new JSONString(this.keyword.getText()));
			
			search(arguments);
		}
		
		
		
		public void search(JSONObject data) {
			data.put("OTD_ID", new JSONString(OTD_ID));
			data.put("MCOT_ID", new JSONString(MASTER_COT_ID));
			
			JSONObject arguments = new JSONObject();
			arguments.put("cmd", new JSONString("GET_SPOT_LIST"));
			arguments.put("data", data);
			
			VisitKoreaBusiness.post("call", arguments.toString(), (o1, s1, o2) -> {
				this.databaseListTable.clearRows();
				
				JSONValue resultValue = fetchJSONValueData(o1);
				if(resultValue == null)	return;
				
				JSONObject result = resultValue.isObject();
				JSONArray resultArray = new JSONArray();
				int dataAmount = 0;
				int excludeAmount = (int) result.get("excludeAmount").isNumber().doubleValue();
				if(result.containsKey("resultData")) {
					resultArray = result.get("resultData").isArray();
					dataAmount = (int) result.get("resultAmount").isNumber().doubleValue();					
				}
				
				for(int i=0; i<resultArray.size(); i++) {
					JSONObject resultData = resultArray.get(i).isObject();
					
					if(validateListHasCotId(resultData)) {
						dataAmount--;
						continue;
					}
					
					ContentTableRow row = this.databaseListTable.addRow(resultData);
					MaterialWidget dateColumn = row.getColumnObject(6);
					MaterialWidget buttonColumn = row.getColumnObject(7);
					MaterialWidget isNewDataColumn = row.getColumnObject(8);
					MaterialWidget cotIdColumn = row.getColumnObject(9);
					
					buttonColumn.addClickHandler(handler -> {
						
						dateColumn.getElement().setInnerText("(생성 대기중)");
						buttonColumn.getElement().setInnerText("제거");
						isNewDataColumn.getElement().setInnerText("1");
						
						historyListTable.getRowsList().add(row);
						historyListTable.getContainer().add(row);
						historyCotIdList.add(cotIdColumn.getElement().getInnerText());

						int addAmount = historyListTable.getRowsList().stream()
								.filter(item -> !item.getColumnObject(8).getElement().getInnerText().equals("0"))
								.collect(Collectors.toList()).size();
						historyListTable.setAddAmount(addAmount);
						
						row.getColumnObject(7).addClickHandler(addHandler -> {

							historyListTable.getContainer().remove(row);
							historyListTable.getRowsList().remove(row);
							historyListTable.getContainer().remove(row);
							historyListTable.getRowsList().remove(row);
							historyCotIdList = historyCotIdList.stream()
									.filter(item -> !item.equals(cotIdColumn.getElement().getInnerText()))
									.collect(Collectors.toList());

							int amount = historyListTable.getRowsList().stream()
									.filter(item -> !item.getColumnObject(8).getElement().getInnerText().equals("0"))
									.collect(Collectors.toList()).size();
							historyListTable.setAddAmount(amount);
							
						});
					});
				}
				
				if(dataAmount > 100) {
					this.databaseListTable.setDataAmount("100 건 이상");
				} else {
					this.databaseListTable.setDataAmount(dataAmount);
				}
				if(excludeAmount != 0 && (int) historyManagementMode.getSelectedValue() == 0) {
					MaterialToast.fireToast("이미 추가된 데이터가 (" + excludeAmount + ") 건 있습니다.");
				}
			});
		}
		
		
		/**
		 * 검색된 스팟 정보가 이미 history 데이터로 사용중인(DB에 저장된) 정보인지를 검증하는 메서드.
		 * @param (JSONObject) tablerow에 추가 될 spot 정보 데이터.
		 * @return (boolean) 데이터가 history로 사용중인지를 나타내는 검증 값. 사용중이면 true.
		 */
		private boolean validateListHasCotId(JSONObject rowData) {
			boolean result = false;
			for(String cotId : historyCotIdList) {
				if(rowData.get("COT_ID").isString().stringValue().equals(cotId)) {
					result = true;
				}
			}
			return result;
		}
		
		
		/**
		 * 화면의 모든 사용자 입력 데이터를 초기화하고 처음 상태로 되돌리는 메서드.
		 */
		protected void clearInputs() {
			this.stateProvince.setSelectedIndex(0);
			
			this.city.clear();
			this.city.addItem("전체 시군구", 0);
			
			this.searchType.setSelectedIndex(0);
			
			this.keyword.setValue("");
		}
		
		
		
		protected void resetSettings() {
			search();
			
			onLoad();
		}
		
		
		
		@Override
		protected void onLoad() {
			super.onLoad();
			
			this.databaseListTable.setSmall();
			historyListTable.setSmall();
			
			this.keyword.setFocus(true);
		}
		
		
		
	}
	
	
	
	
	
	
//////////    //////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    	
	/**
	 * 
	 * HistoryManagementDialog 중앙 부분의 직접입력 컴포넌트.
	 *
	 */
	class ManualHistoryInformAddComponent extends MaterialColumn {
		
		boolean authorizationFlag = true;// 품질인증 부서 자체 관리 코드 입력값이 중복되는 지 확인할 때 사용. 입력값이 없거나 중복되지 않았으면 true.
		
		private MaterialComboBox<HashMap<String, Object>> stateProvince;// 지역.
		private MaterialComboBox<HashMap<String, Object>> city;// 시군구.
		private MaterialTextBox addressLine;// 상세주소.
		private MaterialTextBox spotName;// 업소명(관광지 스팟).
		private MaterialTextBox inquiry;// 문의.
		private MaterialTextBox authorizationCode;// 품질인증 부서 자체 관리 코드(인증번호).
		
		private MaterialButton addHistory;
		
		
		ManualHistoryInformAddComponent() {
			super(10, 10, 10);
			super.setOffset("s1, m1, l1");
			
			initArea();
		}
		
		
		
		private void initArea() {
			super.setHeight("275px");
			
			devideArea();
		}
		
		
		
				private void devideArea() {
					MaterialColumn leftLabelArea = new MaterialColumn(4, 4, 4);		this.add(leftLabelArea);
					leftLabelArea.setMarginTop(25);
					
					MaterialColumn rightInputsArea = new MaterialColumn(8, 8, 8);	this.add(rightInputsArea);
					rightInputsArea.setMarginTop(5);
					
					
					
					initLabels(leftLabelArea);
					
					initLocationSelections(rightInputsArea);
					initInputs(rightInputsArea);
					
					initAddHistoryButton();
		 		}
				
				
				
						private void initLabels(MaterialColumn leftLabelArea) {
							String[] labels = {"*　지　역", "*　상세주소", "*　업소명", "　 문　의"};
							if(! hideAuthCode) {
								labels[labels.length] = "　 인증번호";
							}
							
							Arrays.asList(labels).forEach(item -> {
								MaterialLabel label = new MaterialLabel(item);	leftLabelArea.add(label);
								label.setMarginTop(10);
								label.setFontSize("20px");
								label.setFontWeight(FontWeight.BOLD);
							});
						}
						
						
						
						private void initLocationSelections(MaterialColumn rightInputsArea) {
							
							rightInputsArea.add(this.stateProvince = new MaterialComboBox<HashMap<String,Object>>());
							this.stateProvince.setDisplay(Display.INLINE_BLOCK);
							this.stateProvince.setWidth("45%");
							this.stateProvince.setMarginRight(30);
							
							this.stateProvince.setLabel("지자체 선택");
							HashMap<String, Object> optionMap = new HashMap<String, Object>();
							optionMap.put("code", 0);
							optionMap.put("value", "선택해주세요.");
							this.stateProvince.addItem("선택해주세요.", optionMap);
							
							
							
							rightInputsArea.add(this.city = new MaterialComboBox<HashMap<String,Object>>());
							this.city.setDisplay(Display.INLINE_BLOCK);
							this.city.setWidth("45%");
							
							this.city.setLabel("시군구 선택");
							optionMap = new HashMap<String, Object>();
							optionMap.put("code", 0);
							optionMap.put("value", "선택해주세요.");
							this.city.addItem("선택해주세요.", optionMap);
							
							
							
							JSONObject parameterJSON = new JSONObject();
							parameterJSON.put("cmd", new JSONString("SELECT_AREA"));
							VisitKoreaBusiness.post("call", parameterJSON.toString(), (o1, s1, o2) -> {
								JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)o1));
								JSONObject headerObj = (JSONObject) resultObj.get("header");
								String processResult = headerObj.get("process").isString().stringValue();
								if (processResult.equals("success")) {
									JSONObject bodyObj = (JSONObject) resultObj.get("body");
									JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
									
									int usrCnt = bodyResultObj.size();
									
									for(int i= 0;i< usrCnt;i++) {
										JSONObject obj = (JSONObject)bodyResultObj.get(i);
										HashMap<String, Object> option = new HashMap<>();
										option.put("code", Integer.parseInt(obj.get("code").isString().stringValue()));
										option.put("value", obj.get("name").isString().stringValue());
										this.stateProvince.addItem(obj.get("name").isString().stringValue(), option);
									}
									this.stateProvince.setSelectedIndex(0);
								}
							});
							this.stateProvince.addValueChangeHandler(event -> {
								this.city.clear();
								this.addHistory.setEnabled(false);// 처음 화면 구현 시에는 history 추가 버튼을 숨겨놓기. (위의 input 값에 따라서 검증 후에 추가 버튼이 보이게 설정)
								
								HashMap<String, Object> selectedOption = this.stateProvince.getSelectedValue().get(0);
								if((int) selectedOption.get("code") == 0) {
									HashMap<String, Object> option = new HashMap<>();
									option.put("code", 0);
									option.put("value", "선택해주세요");
									this.city.addItem("선택해주세요.", option);
									return;
								}
								JSONObject paramJSON = new JSONObject();
								paramJSON.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
								HashMap<String, Object> selectedStateOption = this.stateProvince.getSelectedValue().get(0);
								String areacode = selectedStateOption.get("code").toString();
								paramJSON.put("areacode", new JSONString(areacode));
								VisitKoreaBusiness.post("call", paramJSON.toString(), (o1, s1, o2) -> {
									JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)o1));
									JSONObject headerObj = (JSONObject) resultObj.get("header");
									String processResult = headerObj.get("process").isString().stringValue();
									if (processResult.equals("success")) {
										JSONObject bodyObj = (JSONObject) resultObj.get("body");
										JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
										int usrCnt = bodyResultObj.size();
										for(int i= 0;i< usrCnt;i++) {
											JSONObject obj = (JSONObject)bodyResultObj.get(i);
											HashMap<String, Object> cityOption = new HashMap<>();
											if(obj.get("code").isString().stringValue().equals("0")) {
												cityOption.put("code", 0);
												cityOption.put("value", "선택해주세요.");
												this.city.addItem("선택해주세요.", cityOption);
											} else {
												cityOption.put("code", Integer.parseInt(obj.get("code").isString().stringValue()));
												cityOption.put("value", obj.get("sigungu").isString().stringValue());
												this.city.addItem(obj.get("sigungu").isString().stringValue(), cityOption);
											}
										}
										this.city.setSelectedIndex(0);
									}
								});
							});
							this.city.addValueChangeHandler(event -> {
								HashMap<String, Object> selectedStateOption = this.stateProvince.getSelectedValue().get(0);
								HashMap<String, Object> selectedCityOption = this.city.getSelectedValue().get(0);
								if((int) selectedStateOption.get("code") == 0 || (int) selectedCityOption.get("code") == 0) {
									this.addHistory.setEnabled(false);
								} else {
									this.addHistory.setEnabled(validateInputs());
								}
							});
							
							
							
						}
						
						
						
						private void initInputs(MaterialColumn rightInputsArea) {
							List<MaterialTextBox> inputList = Arrays.asList(
									this.addressLine = new MaterialTextBox("(필수) 나머지 주소를 입력해주세요."), 
									this.spotName = new MaterialTextBox("(필수) 업소명을 입력해주세요."), 
									this.inquiry = new MaterialTextBox("(선택) 연락처를 입력해주세요."), 
									this.authorizationCode = new MaterialTextBox("(선택) 인증번호를 입력해주세요.")
									);
							
							inputList.forEach(inputItem -> {
								rightInputsArea.add(inputItem);
								inputItem.setWidth("100%");
								inputItem.setHeight("26px");
								
								inputItem.addKeyUpHandler(event -> {
									if(inputItem.getValue() == null || inputItem.getValue().equals("")) {
										this.addHistory.setEnabled(false);
									}
									this.addHistory.setEnabled(validateInputs());
								});
								inputItem.addValueChangeHandler(event -> {
									if(inputItem.getValue() == null || inputItem.getValue().equals("")) {
										this.addHistory.setEnabled(false);
									}
									this.addHistory.setEnabled(validateInputs());
								});
							});
							
							
							
							this.addressLine.setMarginTop(-28);
							
							
							this.inquiry.setWidth("65%");
							
							
							this.authorizationCode.setDisplay(Display.INLINE_BLOCK);
							this.authorizationCode.setWidth("65%");
							
							this.authorizationCode.addKeyUpHandler(handler -> {
								if(handler.isAnyModifierKeyDown() || handler.isLeftArrow() || handler.isRightArrow()) {
									return;
								}
								
								String value = this.authorizationCode.getValue();
								String result = convertAuthCodeToForm(value);
								
								this.authorizationCode.setValue(result.toUpperCase());
								
								
								
								if(this.authorizationCode.getValue() == null || this.authorizationCode.getValue().equals("")) {
									this.authorizationFlag = true;
								} else {
									this.authorizationFlag = false;
								}
								this.addHistory.setEnabled(validateInputs());
							});
							this.authorizationCode.addValueChangeHandler(handler -> {
								if(authorizationCode.getValue() == null || this.authorizationCode.getValue().equals("")) {
									this.authorizationFlag = true;
								} else {
									this.authorizationFlag = false;
								}
								this.addHistory.setEnabled(validateInputs());
							});
							
							
							MaterialButton authorizationButton = new MaterialButton("중복확인");	rightInputsArea.add(authorizationButton);
							authorizationButton.setTextColor(Color.BLUE);
							authorizationButton.setType(ButtonType.FLAT);
							authorizationButton.setHeight("20px");
							
							authorizationButton.addClickHandler(handler -> {
								if(this.authorizationCode.getValue() == null || this.authorizationCode.getValue().equals("")) return;
								JSONObject arguments = new JSONObject();
								arguments.put("cmd", new JSONString("VALIDATE_AUTH_CODE"));
								arguments.put("authCode", new JSONString(this.authorizationCode.getValue()));
								VisitKoreaBusiness.post("call", arguments.toString(), (o1, s1, o2) -> {
									JSONValue value = fetchJSONValueData(o1);
									// DB 상의 데이터의 중복 검증.
									if(value == null) return;
									
									this.authorizationFlag = true;
									// 메모리 상의 데이터의 중복 검증.
									List<ContentTableRow> memoryRows = historyListTable.getRowsList();
									memoryRows.forEach(row -> {
										MaterialWidget authCodeColumn = row.getColumnObject(1);
										if(authCodeColumn.getElement().getInnerText().equals(this.authorizationCode.getValue())) {
											MaterialToast.fireToast("코드와 중복되는 값이 존재하여 해당 코드를 사용할 수 없습니다.");
											this.authorizationFlag = false;
											return;
										}
									});
									
									if(this.authorizationFlag) {
										String result = value.isString().stringValue();
										MaterialToast.fireToast(result);
										this.addHistory.setEnabled(validateInputs());
									}
								});
							});
							
							
							
							if(hideAuthCode) {
								this.authorizationCode.setVisible(false);
								authorizationButton.setVisible(false);
								
								super.setMarginTop(25);
								super.setHeight("250px");// 원래 이 Component의 높이 설정 - marginTop ==> 275px - 25
							}
						}
						
						
						
						private void initAddHistoryButton() {
							this.addHistory = new MaterialButton("추가");		this.add(this.addHistory);
							this.addHistory.setMarginTop(40);
							this.addHistory.setMarginLeft(100);
							
							this.addHistory.setEnabled(false);// 처음 화면 구현 시에는 history 추가 버튼을 숨겨놓기. (위의 input 값에 따라서 검증 후에 추가 버튼이 보이게 설정)
							
							this.addHistory.addClickHandler(handler -> {
								if(!validateInputs()) {
									this.addHistory.setEnabled(false);
									return;
								}
								
								// 테이블 데이터
								String authCode = "";
								String area = "";
								String sigugun = "";
								String title = "";
								String modifiedDate = "(생성 대기중)";
								int isNewData = 2;
								
								// 파라미터 데이터(테이블의 보이지 않는 영역에 추가);
								String addressLine = "";
								int areaCode;
								int sigugunCode;
								String inquiry = "";// TELNO, 문의 전화번호.
								
								if(this.authorizationCode.getValue() != null & !this.authorizationCode.getValue().equals("")) {
									authCode = this.authorizationCode.getValue();
								}
								area = this.stateProvince.getSelectedValue().get(0).get("value").toString();
								sigugun = this.city.getSelectedValue().get(0).get("value").toString();
								title = this.spotName.getValue();
								
								addressLine = this.addressLine.getValue();
								areaCode = (int) this.stateProvince.getSelectedValue().get(0).get("code");
								sigugunCode = (int) this.city.getSelectedValue().get(0).get("code");
								if(this.inquiry.getValue() != null && !this.authorizationCode.getValue().equals("")) {
									inquiry = this.inquiry.getValue();
								}
								
								
								JSONObject arguments = new JSONObject();
				
								arguments.put("AUTH_CODE", new JSONString(authCode));
								arguments.put("AREA", new JSONString(area));
								arguments.put("SIGUGUN", new JSONString(sigugun));
								arguments.put("TITLE", new JSONString(title));
								arguments.put("MODIFIED_DATE", new JSONString(modifiedDate));
								arguments.put("isNewData", new JSONNumber(isNewData));

								arguments.put("addressLine", new JSONString(addressLine));
								arguments.put("AREA_CODE", new JSONNumber(areaCode));
								arguments.put("SIGUGUN_CODE", new JSONNumber(sigugunCode));
								arguments.put("inquiry", new JSONString(inquiry));
								
								
								
								historyListTable.addRow(arguments);
								
								int addAmount = historyListTable.getRowsList().stream()
										.filter(item -> !item.getColumnObject(8).getElement().getInnerText().equals("0"))
										.collect(Collectors.toList()).size();
								historyListTable.setAddAmount(addAmount);
								
								resetSettings();
							});
						}
						
						
		/**
		 * input 받은 authCode를 확인하여 자동으로 공백을 제거하고, 
		 * 하이픈을 추가한 후, 대문자로 변환하여 주어진 형식의 code를 완성하여 반환하는 메서드.
		 * 
		 * @param (String) input 받은 authCode.
		 * @return (String) 주어진 형식에 따라 하이픈을 추가한 code 데이터.			ex) AA-11-B-2-3333
		 */
		private String convertAuthCodeToForm(String value) {
			String valueRemoveHyphen = value.replaceAll("-", "");
			valueRemoveHyphen = valueRemoveHyphen.replaceAll(" ", "");
			
			if(valueRemoveHyphen.length() > 10) {
				valueRemoveHyphen = valueRemoveHyphen.substring(0, 10);
			}
			
			if(value.length() != 0 && value.charAt(value.length() - 1) != '-') {
				for(int i=1; i<=value.length(); i++) {
					if(i == 3 || i == 6 || i == 8 || i == 10) {
                        String front = valueRemoveHyphen.substring(0, i-1);
                        String back = valueRemoveHyphen.substring(i-1, valueRemoveHyphen.length());
						
                        valueRemoveHyphen = front + "-" + back;
					}
				}
			} else {
				for(int i=1; i<value.length()-1; i++) {
					if(value.length() >= i) {
						if(i == 2 || i == 5 || i == 7 || i == 9) {
							String front = valueRemoveHyphen.substring(0, i);
							String back = valueRemoveHyphen.substring(i, valueRemoveHyphen.length());
							
							valueRemoveHyphen = front + "-" + back;
						}
					}
				}
			}
			
			return valueRemoveHyphen;
		}
		
		
		/**
		 * input 데이터 가운데에 필수 입력 데이터가 모두 입력되었는지를 검증하는 메서드.
		 * 
		 * @return 검증 성공 여부.
		 */
		private boolean validateInputs() {
			int stateProvinceCheck = 0;
			int cityCheck = 0;
			int addressLineCheck = 0;
			int spotNameCheck = 0;
			int authorizationCheck = 0;

			
			HashMap<String, Object> selectedStateOption = this.stateProvince.getSelectedValue().get(0);
			HashMap<String, Object> selectedCityOption = this.city.getSelectedValue().get(0);
			if((int) selectedStateOption.get("code") != 0) 
				stateProvinceCheck++;
			if((int) selectedCityOption.get("code") != 0) 
				cityCheck++;
			if(this.addressLine.getValue() != null && !this.addressLine.getValue().equals("")) 
				addressLineCheck++;
			if(this.spotName.getValue() != null && !this.spotName.getValue().equals("")) 
				spotNameCheck++;
			if(this.authorizationFlag) 
				authorizationCheck++;
			
			
			if(stateProvinceCheck + cityCheck + addressLineCheck + spotNameCheck + authorizationCheck == 5) {
				this.addHistory.setEnabled(true);
				return true;
			}
			else {
				this.addHistory.setEnabled(false);
				return false;
			}
		}
		
		
		/**
		 * 화면의 모든 사용자 입력 데이터를 초기화하고 처음 상태로 되돌리는 메서드.
		 */
		protected void clearInputs() {
			this.authorizationFlag = true;
			this.addHistory.setEnabled(false);// 처음 화면 구현 시에는 history 추가 버튼을 숨겨놓기. (위의 input 값에 따라서 검증 후에 추가 버튼이 보이게 설정)
			
			
			this.stateProvince.setSelectedIndex(0);
			
			this.city.clear();
			HashMap<String, Object> option = new HashMap<>();
			option.put("code", 0);
			option.put("value", "선택해주세요");
			
			this.city.addItem("선택해주세요.", option);
			this.addressLine.setValue("");
			this.spotName.setValue("");
			this.inquiry.setValue("");
			this.authorizationCode.setValue("");
		}
		
		
		
		protected void resetSettings() {
			this.authorizationFlag = true;
			this.addHistory.setEnabled(false);// 처음 화면 구현 시에는 history 추가 버튼을 숨겨놓기. (위의 input 값에 따라서 검증 후에 추가 버튼이 보이게 설정)
			
			
			this.stateProvince.setSelectedIndex(0);
			
			this.city.clear();
			HashMap<String, Object> option = new HashMap<>();
			option.put("code", 0);
			option.put("value", "선택해주세요");
			
			this.city.addItem("선택해주세요.", option);
			this.addressLine.setValue("");
			this.spotName.setValue("");
			this.inquiry.setValue("");
			this.authorizationCode.setValue("");
			
			
			onLoad();
		}
		
		
		
		@Override
		protected void onLoad() {
			super.onLoad();
			
			historyListTable.setSmall();
		}
	}
	
	
	
	
	
	
//////////    //////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    		
	/**
	 * 
	 * 입력방법 선택에 따라 구성될  화면의 각 Component들이 
	 *  각각의 설정에 따라 가지고 있는 Table.
	 *
	 */
	class SpotListTable extends ContentTable {
		
		
		
		private MaterialLabel dataAmount;
		private MaterialLabel addAmount;
		private MaterialLabel removeAmount;
		
		private boolean modeFlag;// true: spot 추가 테이블, false: history 리스트 테이블
		private int height;
		
		
		
		public SpotListTable(boolean modeFlag) {
			super(TABLE_SELECT_TYPE.SELECT_SINGLE, Position.RELATIVE);

			this.modeFlag = modeFlag;
			initTableHeader(modeFlag);
		}
		public SpotListTable(TABLE_SELECT_TYPE selectType) {
			this(selectType, Position.RELATIVE);
		}
		public SpotListTable(TABLE_SELECT_TYPE selectType, Position position) {
			super(selectType, position);
			
		}
		
		
		@Override
		public void init() {
			super.init();
			
			super.setHeight(this.height = 250);
		}
		
		
		/**
		 * 테이블을 소유한 컴포넌트에서 이 메서드를 직접 실행하여 테이블 헤더를 생성(초기화)하게 함. 
		 * 테이블을 소유한 컴포넌트의 종류에 따라 해당 테이블을 다르게 생성할 수 있도록, boolean 값을 입력받음.
		 * 
		 * @param (boolean) 테이블의 종류 flag => true: 다이얼로그 상단의 spot 추가 테이블, false: 다이얼로그 하단의 history 리스트 테이블
		 */
		public void initTableHeader(boolean modeFlag) {
			this.modeFlag = modeFlag;
			

			super.appendTitle("CID", 105, TextAlign.CENTER);// (0)번 컬럼
			super.appendTitle("인증번호", 120, TextAlign.CENTER);
			super.appendTitle("분류", 120, TextAlign.CENTER);
			super.appendTitle("지역", 85, TextAlign.CENTER);
			super.appendTitle("시군구", 85, TextAlign.CENTER);
			super.appendTitle("업소명", 250, TextAlign.CENTER);
			if(modeFlag) {
				super.appendTitle("최근 수정일", 110, TextAlign.CENTER);
				super.appendTitle("추가", 65, TextAlign.CENTER);// (7)번 컬럼
			} else {
				super.appendTitle("히스토리 생성일", 110, TextAlign.CENTER);
				super.appendTitle("제거", 65, TextAlign.CENTER);// (7)번 컬럼
			}
			
			
			super.getHeader().getWidget(1).setVisible(! hideAuthCode);// 설정에 따라 인증번호 항목을 노출/비노출 시킴.
			
			
			// 테이블 창 크기 조절 icon
			MaterialIcon tmp = new MaterialIcon(IconType.ASPECT_RATIO);
			tmp.setTextColor(Color.GREY_DARKEN_1);
			tmp.setMarginLeft(-1);
			super.getHeader().add(tmp);
			tmp.addClickHandler(handler -> {
				if(this.height < 300) {
					setLarge();
				} else {
					setSmall();
				}
			});
			
			
			// 화면에는 보이지 않는 데이터 영역.
			super.appendTitle("isNewData", 500, TextAlign.RIGHT);// (8)번 컬럼
			super.appendTitle("cotId", 100, TextAlign.RIGHT);
			super.appendTitle("addressLine", 100, TextAlign.RIGHT);
			super.appendTitle("areaCode", 100, TextAlign.RIGHT);
			super.appendTitle("sigugunCode", 100, TextAlign.RIGHT);
			super.appendTitle("inquiry", 100, TextAlign.RIGHT);
			super.appendTitle("timeMemory", 100, TextAlign.RIGHT);
			super.appendTitle("historyId", 100, TextAlign.RIGHT);// (15)번 컬럼
			
			
			initBottomMenuArea();
		}
		
		
		
		private void initBottomMenuArea() {
			if(!this.modeFlag) {
				MaterialButton xlsDownload = new MaterialButton("엑셀 다운로드");
				xlsDownload.addClickHandler(handler -> {
					JSONObject data = new JSONObject();

					StringBuilder downloadUrl = new StringBuilder("./call?cmd=FILE_DOWNLOAD_XLS");
					
					data.put("OTD_ID", new JSONString(OTD_ID));
					data.put("MCOT_ID", new JSONString(MASTER_COT_ID));
					downloadUrl.append("&select_type=articleRelatedHistoryList");
					downloadUrl.append("&data=" + data);
					
					Window.open(downloadUrl.toString(), "_self", "enable");
				});
				
				super.getButtomMenu().addButton(xlsDownload, Float.LEFT);
			}
			
			
			this.dataAmount = new MaterialLabel();
			this.dataAmount.setPaddingRight(25);
			this.dataAmount.setFontWeight(FontWeight.BOLD);
			setDataAmount(0);
			
			super.getButtomMenu().addLabel(this.dataAmount, Float.RIGHT);
			
			
			if(!this.modeFlag) {// 히스토리 테이블일 때; 
				this.removeAmount = new MaterialLabel();
				super.getButtomMenu().addLabel(this.removeAmount, Float.RIGHT);
				this.removeAmount.setFontSize("0.8em");
				
				this.addAmount = new MaterialLabel();
				super.getButtomMenu().addLabel(this.addAmount, Float.RIGHT);
				this.addAmount.setFontSize("0.8em");
			}
		}
		
		
		
		public ContentTableRow addRow(JSONObject rowData) {
			String contentId = "";
			String authCode = "";
			String contentType = "";
			String area = "";
			String sigugun = "";
			String title = "";
			String modifiedDate = "";
			int isNewData = 0;
			String cotId = "";
			String addressLine = "";
			int areaCode = 0;
			int sigugunCode = 0;
			String inquiry = "";
			String historyId = "";

			if(rowData.containsKey("CONTENT_ID")) {
				contentId = rowData.get("CONTENT_ID").isString().stringValue();
			}
			if(rowData.containsKey("SCONTENT_ID")) {
				contentId = rowData.get("SCONTENT_ID").isString().stringValue();
			}
			if(rowData.containsKey("AUTH_CODE")) {
				authCode = rowData.get("AUTH_CODE").isString().stringValue();
			}
			if(rowData.containsKey("CONTENT_TYPE")) {
				int contentTypeCode = (int) rowData.get("CONTENT_TYPE").isNumber().doubleValue();
				contentType = Registry.getContentType(contentTypeCode);
			} else if(rowData.containsKey("SCONTENT_TYPE")) {
				int contentTypeCode = (int) rowData.get("SCONTENT_TYPE").isNumber().doubleValue();
				contentType = Registry.getContentType(contentTypeCode);
			} else {
				contentType = "(직접입력)";
			}
			if(rowData.containsKey("AREA")) {
				area = rowData.get("AREA").isString().stringValue();
			}
			if(rowData.containsKey("SIGUGUN")) {
				sigugun = rowData.get("SIGUGUN").isString().stringValue();
			}
			if(rowData.containsKey("TITLE")) {
				title = rowData.get("TITLE").isString().stringValue();
			}
			if(rowData.containsKey("STITLE")) {
				title = rowData.get("STITLE").isString().stringValue();
			}
			if(rowData.containsKey("MODIFIED_DATE")) {
				modifiedDate = rowData.get("MODIFIED_DATE").isString().stringValue();
				modifiedDate = modifiedDate.substring(0, 10);
			}
			if(rowData.containsKey("CREATE_DATE")) {
				modifiedDate = rowData.get("CREATE_DATE").isString().stringValue();
				modifiedDate = modifiedDate.substring(0, 10);
			}
			if(rowData.containsKey("isNewData")) {
				isNewData = (int) rowData.get("isNewData").isNumber().doubleValue();
			}
			if(rowData.containsKey("DI_YN")) {
				isNewData = 0;
			}
			if(rowData.containsKey("COT_ID")) {
				cotId = rowData.get("COT_ID").isString().stringValue();
			}
			if(rowData.containsKey("SCOT_ID")) {
				cotId = rowData.get("SCOT_ID").isString().stringValue();
			}
			if(rowData.containsKey("addressLine")) {
				addressLine = rowData.get("addressLine").isString().stringValue();
			}
			if(rowData.containsKey("ADDR")) {
				addressLine = rowData.get("ADDR").isString().stringValue();
			}
			if(rowData.containsKey("AREA_CODE")) {
				areaCode = (int) rowData.get("AREA_CODE").isNumber().doubleValue();
			}
			if(rowData.containsKey("SIGUGUN_CODE")) {
				sigugunCode = (int) rowData.get("SIGUGUN_CODE").isNumber().doubleValue();
			}
			if(rowData.containsKey("inquiry")) {
				inquiry = rowData.get("inquiry").isString().stringValue();
			}
			if(rowData.containsKey("INQUIRY")) {
				inquiry = rowData.get("INQUIRY").isString().stringValue();
			}
			if(rowData.containsKey("HIST_ID")) {
				historyId = rowData.get("HIST_ID").isString().stringValue();
			}
			
			ContentTableRow row = super.addRow(Color.WHITE, 
					// 화면에 출력되는 컬럼.
					contentId, // (0)번 컬럼
					authCode, 
					contentType, 
					area, 
					sigugun, 
					title, 
					modifiedDate, 
					this.modeFlag? "추가" : "제거", // (7)번 컬럼
							
					// 화면에 보이지 않는 데이터 영역.		
					isNewData, 
					cotId, 
					addressLine,  // (10)번 컬럼
					areaCode, 
					sigugunCode, 
					inquiry, 
					modifiedDate, 
					historyId
					);
			
			row.getColumnObject(1).setVisible(! hideAuthCode);// 설정에 따라 인증번호 항목을 노출/비노출 시킴.
			
			addRowStyle(row);
			addRowEvent(row);
			
			return row;
		}
		
		
		
		private void addRowStyle(ContentTableRow row) {
			MaterialWidget titleColumn = row.getColumnObject(5);
			titleColumn.setTextAlign(TextAlign.LEFT);
			
			MaterialWidget buttonColumn = row.getColumnObject(7);
			buttonColumn.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
		}
		
		
		
		private void addRowEvent(ContentTableRow row) {
			row.getColumnObject(7).addClickHandler(handler -> {
				// 데이터가 새로 추가한(직접 입력한.. ) 메모리상의 데이터일 때,
				if (Integer.parseInt(row.getColumnObject(8).getElement().getInnerText()) != 0) {
					super.getRowsList().remove(row);
					super.getContainer().remove(row);
					int addAmount = super.getRowsList().stream()
							.filter(item -> !item.getColumnObject(8).getElement().getInnerText().equals("0"))
							.collect(Collectors.toList()).size();
					setAddAmount(addAmount);
					// 데이터가 기존에 DB에 저장된 정보를 불러온(대구석 컨텐츠 검색을 통한.. ) 데이터일 때,
				} else {
					if(row.getColumnObject(7).getElement().getInnerText().equals("제거")) {
						row.getColumnObject(6).getElement().setInnerText("(삭제 대기중)");
						row.getColumnObject(7).getElement().setInnerText("취소");
						removeHistoryIdList.add(row.getColumnObject(15).getElement().getInnerText());
					} else {
						row.getColumnObject(6).getElement().setInnerText(row.getColumnObject(14).getElement().getInnerText());
						row.getColumnObject(7).getElement().setInnerText("제거");
						removeHistoryIdList = removeHistoryIdList.stream()
							.filter(item -> !item.equals(row.getColumnObject(15).getElement().getInnerText()))
							.collect(Collectors.toList());
					}
					if(!this.modeFlag) {
						setRemoveAmount(removeHistoryIdList.size());
					}
				}
			});
		}
		
		
		
		protected void setLarge() {
			if(this.modeFlag) {
				this.setHeight(460);
				this.setLayoutPosition(Position.RELATIVE);
				this.getElement().getStyle().setZIndex(1);
				
				historyListTable.setSmall();
			} else {
				this.setHeight(445);
				this.setMarginTop(-225);// 445 - 250 - (-30) ==> Large 설정 시의 테이블 크기 - 원래 테이블 크기 - 테이블이 있는 영역의 marginTop 설정
			}
		}
		
		
		
		protected void setSmall() {
			if(this.modeFlag) {
				this.setHeight(250);
//				this.setLayoutPosition(Position.STATIC);
				this.getElement().getStyle().clearZIndex();
			} else {
				this.setHeight(250);
				this.setMarginTop(0);
			}
		}
		
		
		
		@Override
		public void clearRows() {
			super.clearRows();
			setDataAmount(0);
			this.setAddAmount(0);
			this.setRemoveAmount(0);
		}
		
		

		public MaterialLabel getDataAmount() {
			return this.dataAmount;
		}
		
		

		public void setDataAmount(MaterialLabel dataAmount) {
			this.dataAmount = dataAmount;
		}
		public void setDataAmount(String dataAmount) {
			this.dataAmount.setText(dataAmount);
		}
		public void setDataAmount(int dataAmount) {
			this.dataAmount.setText(dataAmount + " 건");
		}
		public void setAddAmount(int dataAmount) {
			if(this.modeFlag)	return;// 테이블이 다이얼로그 하단의 history 목록 용 테이블 일 경우에만 아래의 메서드가 적용됨.
			if(dataAmount == 0) {
				this.addAmount.setText("");
			} else {
				this.addAmount.setText("(추가 대기: " + dataAmount + " 건)　");
			}
		}
		public void setRemoveAmount(int dataAmount) {
			if(this.modeFlag)	return;// 테이블이 다이얼로그 하단의 history 목록 용 테이블 일 경우에만 아래의 메서드가 적용됨.
			if(dataAmount == 0) {
				this.removeAmount.setText("");
			} else {
				this.removeAmount.setText("(제거 대기: " + dataAmount + " 건)　");
			}
		}
		@Override
		public void setHeight(int height) {
			this.height = height;
			super.setHeight(this.height);
		}
		
		
		
//		protected void resetSettings() {
//			
//		}
		
		
		
//		@Override
//		protected void onLoad() {
//			super.onLoad();
//		}
	}
		
}
