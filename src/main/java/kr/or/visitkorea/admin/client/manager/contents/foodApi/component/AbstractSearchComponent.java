package kr.or.visitkorea.admin.client.manager.contents.foodApi.component;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class AbstractSearchComponent extends AbstractContentPanel {
	
	protected MaterialComboBox<String> searchType;
	protected MaterialTextBox keyword;
	protected MaterialButton searchButton;
	protected ContentTable table;
	protected JSONObject recentFetchParameterData;// 최근 fetch 시에 서버로 전송했던 파라미터 데이터. 
													// 테이블의 더 보기 버튼 클릭, 엑셀 다운로드 버튼 클릭 시에 사용됨.
	
	
	protected String fetchCmd;
	protected MaterialLabel dataAmount;
	protected int offset = 0;
	protected int limit;
	
	
	
	public AbstractSearchComponent(MaterialExtentsWindow window) {
		super(window);
	}
	

	@Override
	public void init() {
		this.setPaddingTop(50);
		this.setPaddingLeft(15);
		this.setPaddingRight(15);
		
		setFetchCmd();
		
		initSearchBarArea();
		initListTableArea();
	}
	
	
	/**
	 * 이 컴포넌트가 fetch() 메서드를 사용 할 때, 
	 * 비동기 요청을 보낼 cmd 주소(String 문자)를 정의한다.
	 * 
	 * (정의하지 않으면 테이블 리스트가 제대로 조회되지 않습니다.)
	 */
	abstract protected void setFetchCmd();
	
	
	
	private void initSearchBarArea() {
		MaterialPanel searchBarArea = new MaterialPanel();
		searchBarArea.setWidth("100%");
		searchBarArea.setDisplay(Display.FLEX);
		searchBarArea.setMarginTop(-50);
		searchBarArea.setMarginBottom(-40);
		this.add(searchBarArea);
		
		initSearchToolsArea(searchBarArea);
		initKewordsArea(searchBarArea);
	}
	
	
	
	abstract protected void initSearchToolsArea(MaterialPanel searchBarArea);
	
	
	
	private void initKewordsArea(MaterialPanel searchBarArea) {
		this.searchType = new MaterialComboBox<String>();
		this.searchType.setWidth("150px");
		this.searchType.setMarginRight(30);
		this.searchType.setLabel("검색 방법");
		initSearchType(this.searchType);
		searchBarArea.add(this.searchType);
		
		
		this.keyword = new MaterialTextBox();
		this.keyword.setWidth("300px");
		this.keyword.setMarginRight(30);
		this.keyword.setLabel(this.searchType.getValues().get(0) + "(으)로 검색");
		this.keyword.addKeyUpHandler(handler -> {
			if(handler.getNativeKeyCode() == 13) {
				fetch(gatherSubmitData());
			}
		});
		searchBarArea.add(this.keyword);
		

		this.searchType.addValueChangeHandler(item -> {
			this.keyword.setLabel(this.searchType.getSelectedValue().get(0) + "(으)로 검색");
		});
		
		
		this.searchButton = new MaterialButton("검색");
		this.searchButton.setWidth("100px");
		this.searchButton.setMarginTop(20);
		this.searchButton.setMarginRight(30);
		this.searchButton.addClickHandler(handler -> {
			fetch(gatherSubmitData());
		});
		searchBarArea.add(this.searchButton);
	}
	
	
	
	abstract protected void initSearchType(MaterialComboBox<String> searchType);
	
	
	
	private void initListTableArea() {
		MaterialPanel tableArea = new MaterialPanel();
		this.add(tableArea);
		
		this.table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		this.table.setHeight(550);
		tableArea.add(this.table);
		
		initTable(this.table);
		initTableBottom();
	}
	
	
	
	abstract protected void initTable(ContentTable table);
	
	
	
	private void initTableBottom() {
//		MaterialButton excelDownload = new MaterialButton("엑셀 다운로드");
//		this.table.getButtomMenu().addButton(excelDownload, Float.LEFT);;
		
		
		MaterialIcon moreData = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreData.addClickHandler(handler -> {
			String dataAmountString = this.dataAmount.getText().replace(" 건", "");
			int count = Integer.parseInt(dataAmountString);
			if(count < this.offset + this.limit || count == this.offset + limit) {
				MaterialToast.fireToast("더 이상의 데이터가 없습니다.");
				return;
			}
			
			this.offset += this.limit;
			this.recentFetchParameterData.put("offset", new JSONNumber(this.offset));
			fetch(this.recentFetchParameterData);
		});
		this.table.getButtomMenu().addIcon(moreData, (this.limit = 20) + " 개 더 보기", Float.RIGHT);
		
		
		this.dataAmount = new MaterialLabel();
		this.dataAmount.setFontWeight(FontWeight.BOLD);
		setDataAmount(0);
		this.table.getButtomMenu().addLabel(this.dataAmount, Float.RIGHT);
	};
	
	
	
	abstract protected JSONObject gatherSubmitData();
	
	
	
	abstract protected void addTableRow(JSONObject rowData);
	
	
	
	protected void fetch(JSONObject data) {
		JSONObject argumentData = new JSONObject();
		argumentData.put("cmd", new JSONString(this.fetchCmd));
		argumentData.put("data", data);
		
		VisitKoreaBusiness.post("call", argumentData.toString(), (o1, s1, o2) -> {
			this.recentFetchParameterData = data;
			if(this.offset == 0) {
				this.table.clearRows();
				setDataAmount(0);
			}
			
			
			JSONValue resultValue = VisitKoreaBusiness.simpleFireToast(o1);
			if(resultValue == null)	return;
			
			
			JSONObject result = resultValue.isObject();
			if(this.offset == 0) {
				Console.log("여기에 왔다!!!" + result.toString());
				int dataAmount = (int) result.get("resultAmount").isNumber().doubleValue();
				setDataAmount(dataAmount);
			}
			JSONArray rowDataList = result.get("resultData").isArray();
			for(int i=0; i<rowDataList.size(); i++) {
				JSONObject rowData = rowDataList.get(i).isObject();
				
				addTableRow(rowData);
			}
		});
	};
	
	
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		fetch(gatherSubmitData());
	}
	
	
	
	public void setDataAmount(int dataAmount) {
		this.dataAmount.setValue(dataAmount + " 건");
	};

}
