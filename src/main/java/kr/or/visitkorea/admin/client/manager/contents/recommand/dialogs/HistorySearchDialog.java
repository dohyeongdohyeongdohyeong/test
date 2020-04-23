package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.form.Submit;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.window.MaterialWindow;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseApplication;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;


/**
 * 
 * 존재하는 히스토리에 연관된 컨텐츠 등, 히스토리에 관한 정보를 검색하는 다이얼로그 창을 생성하는 클래스.
 * 
 * @author 		dohyeong
 * @authorDate	2020-03-16
 * @see			kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentList
 *
 */
public class HistorySearchDialog extends DialogContent {
	
	
	
	// 업소검색 요소.
	private MaterialComboBox<Integer> stateProvince;// 지역.
	private MaterialComboBox<Integer> city;// 시군구.
	private MaterialComboBox<String> searchType;// 검색 방법.
	private MaterialTextBox keyword;
	
	// 기사검색 요소.
	private MaterialComboBox<String> articleCategory;
	private MaterialComboBox<String> searchType2;
	private MaterialTextBox keyword2;
	
	// 공통 요소.
	private MaterialIcon search;
	
	public final int LIMIT_AMOUNT = 15;
	private JSONObject spotListExcelParameters;
	private JSONObject articleListExcelParameters;
	
	
	private final String OTD_ID;
	private int dataAmount;
	
	
	private MaterialExtentsWindow window;
	private MaterialRow container;
	private SelectionPanel pageMode;
	private HistoryRelatedContensTable spotListTable;
	private HistoryRelatedContensTable articleListTable;
	private boolean hideAuthCode;// true 일 경우 authCode 관련 항목을 숨긴다.
	
	

	public HistorySearchDialog(MaterialExtentsWindow window) {
		super(window);
		this.window = window;
		
		this.OTD_ID = window.getValueMap().get("OTD_ID").toString();
	}


	@Override
	public int getHeight() {
		return 0;
	}
	
	
	@Override
	public void init() {
		hideAuthCodeData();////////// 만약 모든 부서에서 authCode를 볼 수 있게 하려면 이 코드 한줄을 지워버리면 됩니다..
		
		this.container = new MaterialRow();	this.add(this.container);
		
		initTitleArea();
		initSearchToolBarArea();
		initTableArea();
		initBottomButtonArea();
	}
	
	
	private void initTitleArea() {
		MaterialColumn titleArea = new MaterialColumn(12, 12, 12);					this.container.add(titleArea);
		titleArea.setMarginTop(15);
		titleArea.setMarginBottom(10);
		
		
		MaterialLabel dialogTitle = new MaterialLabel("히스토리 검색", Color.BLUE); 	titleArea.add(dialogTitle);
		dialogTitle.setFontSize("22px");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		
		
		String[] pageModes = {"업소검색", "기사검색"};
		HashMap<String, Object> modeValues = new HashMap<String, Object>();
		int i = 0;
		for(String pageMode : pageModes) {
			modeValues.put(pageMode, i++);
		}
		this.pageMode  = new SelectionPanel();										titleArea.add(this.pageMode);
		this.pageMode.setValues(modeValues);
		this.pageMode.setFloat(Float.LEFT);
		this.pageMode.setMarginTop(15);
		this.pageMode.setMarginLeft(-5);
		this.pageMode.setSelectionOnSingleMode(pageModes[0]);
		this.pageMode.addStatusChangeEvent(event -> {
			int pageMode = (int) this.pageMode.getSelectedValue();
			if(pageMode == 0) {
				setSpotSearchMode();
			} else if(pageMode == 1) {
				setArticleSearchMode();
			}
		});
	}
	
	
			private void setSpotSearchMode() {
				this.stateProvince.setVisible(true);
				this.city.setVisible(true);
				this.searchType.setVisible(true);
				this.keyword.setVisible(true);
				
				this.articleCategory.setVisible(false);
				this.searchType2.setVisible(false);
				this.keyword2.setVisible(false);
				
				this.spotListTable.setVisible(true);
				this.articleListTable.setVisible(false);
				
				onLoad();
			}
			
			
			private void setArticleSearchMode() {
				this.stateProvince.setVisible(false);
				this.city.setVisible(false);
				this.searchType.setVisible(false);
				this.keyword.setVisible(false);
				
				this.articleCategory.setVisible(true);
				this.searchType2.setVisible(true);
				this.keyword2.setVisible(true);
				
				this.spotListTable.setVisible(false);
				this.articleListTable.setVisible(true);
				
				onLoad();
			}
	
	
	private void initSearchToolBarArea() {
		MaterialColumn firstSelectBoxArea = new MaterialColumn(2, 2, 2);			this.container.add(firstSelectBoxArea);
		MaterialColumn secondSelectBoxArea = new MaterialColumn(2, 2, 2);			this.container.add(secondSelectBoxArea);
		MaterialColumn searchTypeArea = new MaterialColumn(2, 2, 2);				this.container.add(searchTypeArea);
		MaterialColumn keywordArea = new MaterialColumn(3, 3, 3);					this.container.add(keywordArea);
		MaterialColumn searchButtonArea = new MaterialColumn(1, 1, 1);				this.container.add(searchButtonArea);
		
		initfirstSelectBoxArea(firstSelectBoxArea);
		initsecondSelectBoxArea(secondSelectBoxArea);
		initSearchTypeArea(searchTypeArea);
		initKeywordArea(keywordArea);
		initSearchButtonArea(searchButtonArea);
	}
	
	
			private void initfirstSelectBoxArea(MaterialColumn firstSelectBoxArea) {
				
				
				firstSelectBoxArea.add(this.stateProvince = new MaterialComboBox<Integer>());
				
				this.stateProvince.setLabel("지자체 선택");
				this.stateProvince.addItem("전체", 0);
				
				
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
							this.stateProvince.addItem(obj.get("name").isString().stringValue(), Integer.parseInt(obj.get("code").isString().stringValue()));
						}
						this.stateProvince.setSelectedIndex(0);
					}
				});
				this.stateProvince.addValueChangeHandler(event -> {
					this.city.clear();
					if(stateProvince.getSelectedValue().get(0).intValue() == 0) {
						this.city.addItem("전체", 0);
						searchSpot();
						return;
					}
					JSONObject paramJSON = new JSONObject();
					paramJSON.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
					String areacode = this.stateProvince.getSelectedValue().get(0).toString();
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
								if(obj.get("code").isString().stringValue().equals("0")) {
									this.city.addItem("전체", 0);
								} else {
									this.city.addItem(obj.get("sigungu").isString().stringValue(), Integer.parseInt(obj.get("code").isString().stringValue()));
								}
							}
							this.city.setSelectedIndex(0);
							searchSpot();
						}
					});
				});
				
				
			}
			
			
			private void initsecondSelectBoxArea(MaterialColumn secondSelectBoxArea) {
				secondSelectBoxArea.add(this.city = new MaterialComboBox<Integer>());
				
				this.city.setLabel("시군구 선택");
				this.city.addItem("전체", 0);
				
				this.city.addValueChangeHandler(event -> {
					searchSpot();
				});
				
				
				
				secondSelectBoxArea.add(this.articleCategory = new MaterialComboBox<String>());
				
				this.articleCategory.setLabel("구분 선택");
				this.articleCategory.addItem("전체기사");
				this.articleCategory.addItem("일반기사");
				this.articleCategory.addItem("테마기사");
				
				this.articleCategory.addValueChangeHandler(event -> {
					searchArticle();
				});
				
				
				this.articleCategory.setVisible(false);
			}
			
			
			private void initSearchTypeArea(MaterialColumn searchTypeArea) {
				searchTypeArea.add(this.searchType = new MaterialComboBox<String>());
				
				this.searchType.setLabel("검색 방법");
				this.searchType.addItem("전체");
				this.searchType.addItem("업소명");
				this.searchType.addItem("업소 CID");
				if(!this.hideAuthCode) {
					this.searchType.addItem("인증번호");
				}
				
				this.searchType.addValueChangeHandler(event -> {
					String searchType = this.searchType.getSelectedValue().get(0);
					this.keyword.setLabel(searchType + " (으)로 업소검색");
				});
				
				
				
				searchTypeArea.add(this.searchType2 = new MaterialComboBox<String>());
				
				this.searchType2.setLabel("검색 방법");
				this.searchType2.addItem("전체");
				this.searchType2.addItem("기사명");
				this.searchType2.addItem("CID");
				
				this.searchType2.addValueChangeHandler(event -> {
					String searchType = this.searchType2.getSelectedValue().get(0);
					this.keyword2.setLabel(searchType + " (으)로 기사검색");
				});
				
				
				this.searchType2.setVisible(false);
			}
			
			
			private void initKeywordArea(MaterialColumn keywordArea) {
				keywordArea.add(this.keyword = new MaterialTextBox("검색어를 입력해주세요."));
				
				this.keyword.setLabel("전체 (으)로 업소검색");

				this.keyword.addKeyDownHandler(handler -> {
					if(handler.getNativeKeyCode() == 13) {
						searchSpot();
					}
				});
				
				
				
				keywordArea.add(this.keyword2 = new MaterialTextBox("검색어를 입력해주세요."));
				
				this.keyword2.setLabel("전체 (으)로 기사검색");
				
				this.keyword2.addKeyDownHandler(handler -> {
					if(handler.getNativeKeyCode() == 13) {
						searchArticle();
					}
				});
				
				
				this.keyword2.setVisible(false);
			}
			
			
			private void initSearchButtonArea(MaterialColumn searchButtonArea) {
				searchButtonArea.add(this.search = new MaterialIcon(IconType.SEARCH));
				
				this.search.setMarginTop(35);
				
				this.search.addClickHandler(handler -> {
					int pageMode = (int) this.pageMode.getSelectedValue();
					if(pageMode == 0) {
						searchSpot();
					} else if(pageMode == 1) {
						searchArticle();
					}
				});
			}
	
	
	private void initTableArea() {
		MaterialColumn tableArea = new MaterialColumn(12, 12, 12);	this.container.add(tableArea);
		tableArea.setMarginTop(-25);
		
		this.spotListTable = new HistoryRelatedContensTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		tableArea.add(this.spotListTable);
		this.spotListTable.initTableHeader(false);
		
		
		this.articleListTable = new HistoryRelatedContensTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		tableArea.add(this.articleListTable);
		this.articleListTable.initTableHeader(true);
		
		this.articleListTable.setVisible(false);
	}
	
	
	private void initBottomButtonArea() {
		super.addDefaultButtons();
		
		super.getCloseButton().addClickHandler(handler -> {
			this.pageMode.setSelectionOnSingleMode("업소검색");
			setSpotSearchMode();
		});
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
			
			this.setPaddingTop(10);
			this.setPaddingLeft(60);
			this.setPaddingRight(60);
		}
	}
	
	
	private void searchSpot() {
		JSONObject data = new JSONObject();
		
		data.put("areaCode", new JSONNumber(this.stateProvince.getSelectedValue().get(0).intValue()));
		data.put("sigugunCode", new JSONNumber(this.city.getSelectedValue().get(0).intValue()));
		data.put("searchType", new JSONString(this.searchType.getSelectedValue().get(0).toString()));
		data.put("keyword", new JSONString(this.keyword.getValue()));
		
		this.spotListTable.offset = 0;
		data.put("offset", new JSONNumber(0));
		data.put("amount", new JSONNumber(0 + LIMIT_AMOUNT));
		
		search(data);
	}
	
	
	private void searchArticle() {
		JSONObject data = new JSONObject();
		
		data.put("articleCategory", new JSONString(this.articleCategory.getSelectedValue().get(0)));
		data.put("searchType2", new JSONString(this.searchType2.getSelectedValue().get(0).toString()));
		data.put("keyword", new JSONString(this.keyword2.getValue()));
		
		this.articleListTable.offset = 0;
		data.put("offset", new JSONNumber(0));
		data.put("amount", new JSONNumber(0 + LIMIT_AMOUNT));
		
		search(data);
	}
	
	
	public void search(JSONObject data) {
		data.put("OTD_ID", new JSONString(this.OTD_ID));
		
		JSONObject arguments = new JSONObject();
		arguments.put("cmd", new JSONString("GET_HISTORY_LIST"));
		arguments.put("data", data);
		
		
		int pageMode = (int) this.pageMode.getSelectedValue();
		VisitKoreaBusiness.post("call", arguments.toString(), (o1, s1, o2) -> {
			if(pageMode == 0) {
				this.spotListExcelParameters = data;
				if(!data.containsKey("offset") || (int) data.get("offset").isNumber().doubleValue() == 0) {
					this.spotListTable.clearRows();
				}
			} else if(pageMode == 1) {
				this.articleListExcelParameters = data;
				if(!data.containsKey("offset") || (int) data.get("offset").isNumber().doubleValue() == 0) {
					this.articleListTable.clearRows();
				}
			}
			
			JSONValue historyValue = fetchJSONValueData(o1);
			if(historyValue == null) return;
			
			JSONObject historyObject = historyValue.isObject();
			JSONArray historyDataArray = historyObject.get("resultData").isArray();
			int dataAmount = (int) historyObject.get("resultAmount").isNumber().doubleValue();
			
			for(int i=0; i<historyDataArray.size(); i++) {
				JSONObject historyData = historyDataArray.get(i).isObject();
				
				if(pageMode == 0) {
					this.spotListTable.addRow(historyData);
				} else if(pageMode == 1 ) {
					this.articleListTable.addRow(historyData);
				}
			}
			
			if(pageMode == 0) {
				this.spotListTable.setDataAmount(dataAmount);
			} else {
				this.articleListTable.setDataAmount(dataAmount);
			}
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
	
	
	protected void onLoad() {
		super.onLoad();
		
//		search(new JSONObject() {{put("offset", new JSONNumber(0)); put("amount", new JSONNumber(LIMIT_AMOUNT)); }});	// 업소검색 요소.
		
		this.stateProvince.setSelectedIndex(0);
		this.city.setSelectedIndex(0);
		this.searchType.setSelectedIndex(0);
		this.keyword.setLabel(this.searchType.getSelectedValue().get(0));
		this.keyword.setText("");
		
		this.articleCategory.setSelectedIndex(0);
		this.searchType2.setSelectedIndex(0);
		this.keyword2.setLabel(this.searchType2.getSelectedValue().get(0));
		this.keyword2.setText("");
		
		if((int) this.pageMode.getSelectedValue() == 0) {
			this.keyword.setFocus(true);
			searchSpot();
		} else if((int) this.pageMode.getSelectedValue() == 1) {
			this.keyword2.setFocus(true);
			searchArticle();
		}
	}
	
	
	
	
	
	
////////////////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    	//////////    //////////    //////////    //////////    //////////    		
	/**
	* 
	* History 검색 다이얼로그 모드 선택에 따라서  
	*  각각의 설정에 맞는 업소 리스트나, 혹은 기사 리스트 목록을 담고있는 테이블.
	*
	*/
	class HistoryRelatedContensTable extends ContentTable {
		
		
		
		private MaterialLabel dataAmount;
		private boolean isArticleTableFlag;// false: 업소 리스트 출력용 테이블, true: 기사 리스트 조회용 테이블.
		
		public int offset = 0;
		
		

		public HistoryRelatedContensTable(TABLE_SELECT_TYPE selectType) {
			this(selectType, Position.RELATIVE);
		}
		public HistoryRelatedContensTable(TABLE_SELECT_TYPE selectType, Position position) {
			super(selectType, position);
		}
		
		
		@Override
		public void init() {
			super.init();
			
			super.setHeight(430);
		}
		
		
		
		public void initTableHeader(boolean isArticleTable) {
			
			this.isArticleTableFlag = isArticleTable;
			
			if(!isArticleTableFlag) {// 테이블이 업소 리스트 검색 페이지의 테이블일 때, 
				
				super.appendTitle("업소 CID", 105, TextAlign.CENTER);
				super.appendTitle("인증번호", 120, TextAlign.CENTER);
				super.getHeader().getWidget(1).setVisible(!hideAuthCode);// 품질인증 부서일 경우 컬럼이 보이게 설정.
				super.appendTitle("지역", 85, TextAlign.CENTER);
				super.appendTitle("시군구", 85, TextAlign.CENTER);
				super.appendTitle("상세주소", 275, TextAlign.CENTER);
				super.appendTitle("업소 전화번호", 160, TextAlign.CENTER);
				super.appendTitle("업소명", 260, TextAlign.CENTER);
				super.appendTitle("컨텐츠 구분", 90, TextAlign.CENTER);
				super.appendTitle("언급된 컨텐츠", 265, TextAlign.CENTER);
				
				// 화면에 보이지 않는 영역.
				super.appendTitle("SCOT_ID", 500, TextAlign.RIGHT);
				super.appendTitle("MCOT_ID", 100, TextAlign.RIGHT);
				
			} else {// 테이블이 기사 컨텐츠 검색 페이지의 테이블일 때, 
				
				super.appendTitle("기사 CID", 105, TextAlign.CENTER);
				super.appendTitle("기사 구분", 90, TextAlign.CENTER);
				super.appendTitle("기사명", 335, TextAlign.CENTER);
				super.appendTitle("업소 CID", 105, TextAlign.CENTER);
				super.appendTitle("인증번호", 120, TextAlign.CENTER);
				super.getHeader().getWidget(4).setVisible(!hideAuthCode);// 품질인증 부서일 경우 컬럼이 보이게 설정.
				super.appendTitle("지역", 85, TextAlign.CENTER);
				super.appendTitle("시군구", 85, TextAlign.CENTER);
				super.appendTitle("업소 전화번호", 210, TextAlign.CENTER);
				super.appendTitle("업소명", 310, TextAlign.CENTER);
				
				// 화면에 보이지 않는 영역.
				super.appendTitle("SCOT_ID", 500, TextAlign.RIGHT);
				super.appendTitle("MCOT_ID", 100, TextAlign.RIGHT);
				
			}
			
			initBottomMenuArea();
		}
		
		
		
		private void initBottomMenuArea() {
			MaterialButton downloadXls = new MaterialButton("엑셀(부분) 다운로드");
			downloadXls.addClickHandler(handler -> {
				if(!this.isArticleTableFlag) {
					StringBuilder downloadUrl = new StringBuilder("./call?cmd=FILE_DOWNLOAD_XLS");
					
					spotListExcelParameters.put("offset", new JSONNumber(0));
					downloadUrl.append("&select_type=spotHistorySearchList");
					downloadUrl.append("&data=" + spotListExcelParameters);
					
					Window.open(downloadUrl.toString(), "_self", "enable");
				} else {
					StringBuilder downloadUrl = new StringBuilder("./call?cmd=FILE_DOWNLOAD_XLS");

					articleListExcelParameters.put("offset", new JSONNumber(0));
					downloadUrl.append("&select_type=articleHistorySearchList");
					downloadUrl.append("&data=" + articleListExcelParameters.toString());
					
					Window.open(downloadUrl.toString(), "_self", "enable");
				}
			});
			super.getButtomMenu().addButton(downloadXls, Float.LEFT);
			
			
			
			
			MaterialButton downloadWholeXls = new MaterialButton("엑셀(전체) 다운로드");
			downloadWholeXls.addClickHandler(handler -> {
				JSONObject data = new JSONObject();
				if(!this.isArticleTableFlag) {
					StringBuilder downloadUrl = new StringBuilder("./call?cmd=FILE_DOWNLOAD_XLS");
					
					data.put("OTD_ID", new JSONString(OTD_ID));
					downloadUrl.append("&select_type=wholeSpotHistoryList");
					downloadUrl.append("&data=" + data);
					
					Window.open(downloadUrl.toString(), "_self", "enable");
				} else {
					StringBuilder downloadUrl = new StringBuilder("./call?cmd=FILE_DOWNLOAD_XLS");

					data.put("OTD_ID", new JSONString(OTD_ID));
					downloadUrl.append("&select_type=wholeArticleHistoryList");
					downloadUrl.append("&data=" + data);
					
					Window.open(downloadUrl.toString(), "_self", "enable");
				}
			});
			super.getButtomMenu().addButton(downloadWholeXls, Float.LEFT);
			
			
			
			
			MaterialIcon moreRows = new MaterialIcon(IconType.ARROW_DOWNWARD);
			moreRows.setTextAlign(TextAlign.CENTER);
			moreRows.addClickHandler(handler -> {
				this.offset += LIMIT_AMOUNT;
				int dataAmount = Integer.parseInt(this.dataAmount.getText().substring(0, this.dataAmount.getText().length() - 2));
				
				if(this.offset == dataAmount || this.offset > dataAmount) {
					MaterialToast.fireToast("더 이상의 데이터가 없습니다.");
					return;
				}
				
				if(!this.isArticleTableFlag) {
					JSONObject data = new JSONObject();
					
					data.put("areaCode", new JSONNumber(stateProvince.getSelectedValue().get(0).intValue()));
					data.put("sigugunCode", new JSONNumber(city.getSelectedValue().get(0).intValue()));
					data.put("searchType", new JSONString(searchType.getSelectedValue().get(0).toString()));
					data.put("keyword", new JSONString(keyword.getValue()));
					
					data.put("offset", new JSONNumber(this.offset));
					data.put("amount", new JSONNumber(LIMIT_AMOUNT));
					
					search(data);
				} else {
					JSONObject data = new JSONObject();
					
					data.put("articleCategory", new JSONString(articleCategory.getSelectedValue().get(0)));
					data.put("searchType2", new JSONString(searchType2.getSelectedValue().get(0).toString()));
					data.put("keyword", new JSONString(keyword2.getValue()));

					data.put("offset", new JSONNumber(this.offset));
					data.put("amount", new JSONNumber(LIMIT_AMOUNT));
					
					search(data);
				}
			});
			super.getButtomMenu().addIcon(moreRows, LIMIT_AMOUNT + " 건 더 보기", Float.RIGHT);

			
			
			
			this.dataAmount = new MaterialLabel("0 건");
			this.dataAmount.setFontWeight(FontWeight.BOLD);
			super.getButtomMenu().addLabel(this.dataAmount, Float.RIGHT);
		}
		
		
		
		public ContentTableRow addRow(JSONObject rowData) {
			String masterContentId = "";
			if(rowData.containsKey("MCONTENT_ID")) {
				masterContentId = rowData.get("MCONTENT_ID").isString().stringValue();
			}
			String masterContentDiv = "";
			if(rowData.containsKey("MCONTENT_DIV")) {
				int contentDiv = (int) rowData.get("MCONTENT_DIV").isNumber().doubleValue();
				if(contentDiv == 1) {
					masterContentDiv = "테마기사";
				} else {
					masterContentDiv = "일반기사";
				}
			}
			String spotContentId = "";
			if(rowData.containsKey("SCONTENT_ID")) {
				spotContentId = rowData.get("SCONTENT_ID").isString().stringValue();
			}
			String area = "";
			if(rowData.containsKey("AREA")) {
				area = rowData.get("AREA").isString().stringValue();
			}
			String sigugun = "";
			if(rowData.containsKey("SIGUGUN")) {
				sigugun = rowData.get("SIGUGUN").isString().stringValue();
			}
			String addr = "";
			if(rowData.containsKey("ADDR")) {
				addr = rowData.get("ADDR").isString().stringValue();
			}
			String masterTitle = "";
			if(rowData.containsKey("MTITLE")) {
				masterTitle = rowData.get("MTITLE").isString().stringValue();
			}
			String spotTitle = "";
			if(rowData.containsKey("STITLE")) {
				spotTitle = rowData.get("STITLE").isString().stringValue();
			}
			String inquiry = "";
			if(rowData.containsKey("INQUIRY")) {
				inquiry = rowData.get("INQUIRY").isString().stringValue();
			}
			String authCode = "";
			if(rowData.containsKey("AUTH_CODE")) {
				authCode = rowData.get("AUTH_CODE").isString().stringValue();
			}
			
			
			String historyId = "";
			if(rowData.containsKey("HIST_ID")) {
				historyId = rowData.get("HIST_ID").isString().stringValue();
			}
			String masterCotId = "";
			if(rowData.containsKey("MCOT_ID")) {
				masterCotId = rowData.get("MCOT_ID").isString().stringValue();
			}
			String spotCotId = "";
			if(rowData.containsKey("SCOT_ID")) {
				spotCotId = rowData.get("SCOT_ID").isString().stringValue();
			}
			int spotContentType;
			if(rowData.containsKey("SCONTENT_TYPE")) {
				spotContentType = (int) rowData.get("SCONTENT_TYPE").isNumber().doubleValue();
			}
			boolean isDirectInput;
			if(rowData.containsKey("DI_YN")) {
				String diYn = rowData.get("DI_YN").isString().stringValue();
				isDirectInput = (Integer.parseInt(diYn) == 0? false : true);
			}
			int areaCode;
			if(rowData.containsKey("AREA_CODE")) {
				areaCode = (int) rowData.get("AREA_CODE").isNumber().doubleValue();
			}
			int sigugunCode;
			if(rowData.containsKey("SIGUGUN_CODE")) {
				sigugunCode = (int) rowData.get("SIGUGUN_CODE").isNumber().doubleValue();
			}
			String reg_id = "";
			if(rowData.containsKey("REG_ID")) {
				reg_id = rowData.get("REG_ID").isString().stringValue();
			}
			String createDate = "";
			if(rowData.containsKey("CREATE_DATE")) {
				createDate = rowData.get("CREATE_DATE").isString().stringValue();
			}
						
			
			ContentTableRow row;
			if(!this.isArticleTableFlag) {
				
				row = super.addRow(Color.WHITE, 
						spotContentId, // (0) 번 컬럼
						authCode, 
						area, 
						sigugun, 
						addr, 
						inquiry, // (5) 번 컬럼
						spotTitle, 
						masterContentDiv, 
						masterTitle, 
						
						// parameter 데이터.
						spotCotId, // (9) 번 컬럼
						masterCotId
						);
				
				
				// 품질인증 부서일 경우에만 보이게 하기.
				row.getColumnObject(1).setVisible(!hideAuthCode);
				
			} else {
				
				row = super.addRow(Color.WHITE, 
						masterContentId, // (0) 번 컬럼
						masterContentDiv, 
						masterTitle, 
						spotContentId, 
						authCode, 
						area, // (5) 번 컬럼
						sigugun, 
						inquiry, 
						spotTitle, 
						
						// parameter 데이터.
						spotCotId, // (9) 번 컬럼
						masterCotId
						);
				
				
				// 품질인증 부서일 경우에만 보이게 하기.
				row.getColumnObject(4).setVisible(!hideAuthCode);
				
			}
			
			
			addRowStyle(row);
			addRowEvent(row);
			
			return row;
		}
		
		
		
		private void addRowStyle(ContentTableRow row) {
			MaterialWidget addressColumn;
			MaterialWidget inquiryColumn;
			MaterialWidget spotTitleColumn;
			MaterialWidget masterTitleColumn;
			
			
			if(!this.isArticleTableFlag) {
				addressColumn = row.getColumnObject(4);
				inquiryColumn = row.getColumnObject(5);
				spotTitleColumn = row.getColumnObject(6);
				masterTitleColumn = row.getColumnObject(8);
				
				addressColumn.setFontSize("13px");
			} else {
				inquiryColumn = row.getColumnObject(7);
				spotTitleColumn = row.getColumnObject(8);
				masterTitleColumn = row.getColumnObject(2);
			}
			
			
			inquiryColumn.setFontSize("13px");
			
			spotTitleColumn.setTextAlign(TextAlign.LEFT);
//			spotTitleColumn.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
			
			masterTitleColumn.setTextAlign(TextAlign.LEFT);
			masterTitleColumn.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
		}
		
		
		
		private void addRowEvent(ContentTableRow row) {
			MaterialWidget spotTitleColumn;
			MaterialWidget masterTitleColumn;
			MaterialWidget spotCotIdColumn;
			MaterialWidget masterCotIdColumn;
			
			
			if(!this.isArticleTableFlag) {
				spotTitleColumn = row.getColumnObject(6);
				masterTitleColumn = row.getColumnObject(8);
				
				spotCotIdColumn = row.getColumnObject(9);
				masterCotIdColumn = row.getColumnObject(10);
			} else {
				spotTitleColumn = row.getColumnObject(8);
				masterTitleColumn = row.getColumnObject(2);
				
				spotCotIdColumn = row.getColumnObject(9);
				masterCotIdColumn = row.getColumnObject(10);
			}
			
			
			masterTitleColumn.addClickHandler(handler -> {
				RecommContentsTree cPanel = (RecommContentsTree) window.getContentPanel(1);
				cPanel.setTitle(masterTitleColumn.getElement().getInnerText());
				cPanel.setCotId(masterCotIdColumn.getElement().getInnerText());
				ContentTable table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				table.appendTitle("", 0, TextAlign.CENTER);
				ContentTableRow tableRow = table.addRow(Color.WHITE, 
						"", "", "", "", "", "", "", "", "", "", 
						"");
				cPanel.setRow(tableRow);
//				cPanel.setRow(row);
				cPanel.goTree(1);
				cPanel.go(0);
				cPanel.loading();
				window.goContentSlider(window.getWidth() * 1 * -1);
			});
		}
		
		

		@Override
		public void clearRows() {
			super.clearRows();
			setDataAmount(0);
			this.offset = 0;
		}
		
		

		public MaterialLabel getDataAmount() {
			return this.dataAmount;
		}
		
		

		public void setDataAmount(int dataAmount) {
			this.dataAmount.setText(dataAmount + " 건");
		}
		
		
		
		@Override
		protected void onLoad() {
			super.onLoad();
		}
	}
		
}
