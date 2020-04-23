package kr.or.visitkorea.admin.client.manager.contents.foodApi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;

import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

/**
 * 컨텐츠 > 데이터베이스 컨텐츠 > 컨텐츠 목록 항목 하위의 식약처 DB 관리 화면.
 * 
 * 어플리케이션을 실행시키면 가장 상단에 나오는 검색 모드 셀렉터를 화면에 추가하고, 
 * 사용자의 셀렉터 선택 값에 따라 해당하는 화면으로 이동시킨다.
 * 
 * @author 	dohyeong
 * @date	20-04-16
 * @see		
 */
public class FoodApiManagementMain extends AbstractContentPanel {
	
	protected String[] searchModes;
	protected SelectionPanel searchModeSelector;
	protected MaterialPanel changeableComponent;

	public FoodApiManagementMain(MaterialExtentsWindow window) {
		super(window);
	}
	
	
	
	@Override
	public void init() {
		super.setTitle("식약처 DB 관리");
		
		this.setFloat(Float.LEFT);
		
		initSearchModeSelectorArea();
		initChangeableComponentArea();
	}
	
	
	
	private void initSearchModeSelectorArea() {
		this.searchModeSelector = new SelectionPanel();
		this.searchModeSelector.setFloat(Float.LEFT);
		this.searchModeSelector.setMarginTop(20);
		this.searchModeSelector.setMarginLeft(10);
		this.add(this.searchModeSelector);
		
		this.searchModes = new String[] {"신규 음식점 DB 리스트", "식약처 DB 조회", "예외 처리 리스트"};
		
		Map<String, Object> selectionValue = new HashMap<String, Object>();
		for(int i=0; i<this.searchModes.length; i++) {
			selectionValue.put(this.searchModes[i], i);
		}
		
		
		this.searchModeSelector.setValues(selectionValue);
		this.searchModeSelector.setSelectionOnSingleMode(this.searchModes[0]);// 첫 번째 셀렉션 선택.
		
		addSelectModeChangeEvent();
	}
	
	
	
	private void addSelectModeChangeEvent() {
		this.searchModeSelector.addStatusChangeEvent(event -> {
			this.changeableComponent.setMarginLeft((int) this.searchModeSelector.getSelectedValue() * -1500);
		});
	}
	
	
	
	private void initChangeableComponentArea() {
		
		this.changeableComponent = new MaterialPanel();
		this.add(this.changeableComponent);
		
		
		this.changeableComponent.setWidth(1500 * this.searchModes.length + "px");
		this.changeableComponent.setMarginTop(-630);
		this.changeableComponent.setFloat(Float.LEFT);
		NewRestaurantList rrl = new NewRestaurantList(super.getMaterialExtentsWindow());
		SearchKFDAInformation kfda = new SearchKFDAInformation(super.getMaterialExtentsWindow());
		ExceptionRestaurantList erl = new ExceptionRestaurantList(super.getMaterialExtentsWindow());
		
		
		Arrays.asList(rrl, kfda, erl).forEach(item -> {
			item.setFloat(Float.LEFT);
			item.setWidth("1500px");
			this.changeableComponent.add(item);
		});
		
	}

}
