package kr.or.visitkorea.admin.client.manager.main.sights;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.main.recommand.RecommMainApplication;
import kr.or.visitkorea.admin.client.manager.main.recommand.RecommMainContentBundle;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMenu;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SightMainTags extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(RecommMainContentBundle.INSTANCE.contentCss());
	}

	private MaterialLink autoSelectionLabel;
	private MaterialSwitch switchComponent;
	private MaterialTextBox inputBox;
	private ContentTable table;
	private ContentTable searchTable;
	private MaterialLabel selectTargetTag;
	private ContentMenu searchPager;
	private ContentMenu pager;
	private MaterialIcon icon2;
	private MaterialIcon icon1;
	private MaterialPanel enablePanel;

	public SightMainTags(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 명소 메인 :: 태그");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		this.add(titleLabel);
		
		autoSelectionLabel = new MaterialLink("적용할 빈도를 선택해 주세요.");
		autoSelectionLabel.setIconType(IconType.CHECK_CIRCLE);
		autoSelectionLabel.setLayoutPosition(Position.ABSOLUTE);
		autoSelectionLabel.setRight(180);
		autoSelectionLabel.setTop(25);
		autoSelectionLabel.setFontSize("1.2em");
		autoSelectionLabel.setFontWeight(FontWeight.BOLD);
		autoSelectionLabel.setVisible(false);
//		autoSelectionLabel.setWidth("");
//		autoSelectionLabel.setBorder("1px solid #e0e0e0");
		autoSelectionLabel.addClickHandler(event->{
			if (switchComponent.getValue() == false) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("AUTO_SELECTION_LABEL", autoSelectionLabel);
				getMaterialExtentsWindow().openDialog(RecommMainApplication.SELECT_AUTO_TAG_TYPE, parameters, 720);
			}
		});

		this.add(autoSelectionLabel);
		
		switchComponent =  new MaterialSwitch();
		switchComponent.setOnLabel("수동");
		switchComponent.setOffLabel("자동");
		switchComponent.setValue(true);
		switchComponent.setLayoutPosition(Position.ABSOLUTE);
		switchComponent.setRight(30);
		switchComponent.setTop(25);
//		switchComponent.setWidth("24");
//		switchComponent.setBorder("1px solid #e0e0e0");
		switchComponent.addValueChangeHandler(event->{
			if (event.getValue()) {
				autoSelectionLabel.setVisible(false);
				visibleContent(true);
			}else {
				autoSelectionLabel.setVisible(true);
				visibleContent(false);
			}
		});

		this.add(switchComponent);

		
        // dialog title define
		inputBox = new MaterialTextBox();
		inputBox.setLabel("멤버 태그 검색");
		
		inputBox.addKeyUpHandler(event->{
			if (event.getNativeKeyCode() == 13) {
				searchTag();
			}
		}); 
		
		inputBox.setIconType(IconType.SEARCH);
		inputBox.setLayoutPosition(Position.ABSOLUTE);
		inputBox.setRight(60);
		inputBox.setLeft(350);
		inputBox.setTop(70);
		
		this.add(inputBox);

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(370);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setTop(140);
		table.appendTitle("멤버 태그", 300, TextAlign.LEFT);

		this.add(table);
/*
		// 멤버 태그 pager
		pager = new ContentBottomMenu();
		pager.setLayoutPosition(Position.ABSOLUTE);
		pager.setLeft(30);
		pager.setTop(510);
		pager.setWidth("302px");
*/	
		icon1 = new MaterialIcon(IconType.DELETE);
		icon1.setTextAlign(TextAlign.CENTER);
		icon1.addClickHandler(event->{
		});

		table.getButtomMenu().addIcon(icon1, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
	
//		this.add(pager);

		
		searchTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		searchTable.setHeight(370);
		searchTable.setLayoutPosition(Position.ABSOLUTE);
		searchTable.setLeft(350);
		searchTable.setTop(140);
		searchTable.appendTitle("검색 태그", 420, TextAlign.LEFT);
	
		this.add(searchTable);
/*
		// 멤버 태그 pager
		searchPager = new ContentBottomMenu();
		searchPager.setLayoutPosition(Position.ABSOLUTE);
		searchPager.setLeft(350);
		searchPager.setTop(510);
		searchPager.setWidth("422px");
*/	
		icon2 = new MaterialIcon(IconType.ADD);
		icon2.setTextAlign(TextAlign.CENTER);
		icon2.addClickHandler(event->{
		});

		searchTable.getButtomMenu().addIcon(icon2, "등록", com.google.gwt.dom.client.Style.Float.LEFT);
	
//		this.add(searchPager);
		
		enablePanel = new MaterialPanel();
		enablePanel.setLayoutPosition(Position.ABSOLUTE);
		enablePanel.setLeft(30);
		enablePanel.setRight(27);
		enablePanel.setTop(70);
		enablePanel.setBottom(26);
		enablePanel.setBackgroundColor(Color.BLACK);
		enablePanel.setOpacity(0.2);
		enablePanel.setVisibility(Visibility.HIDDEN);
		this.add(enablePanel);	
	}

	private void visibleContent(boolean enableFlag) {
		
		if (!enableFlag) {
			enablePanel.setVisibility(Visibility.VISIBLE);
		}else {
			enablePanel.setVisibility(Visibility.HIDDEN);
		}
		
		inputBox.setEnabled(enableFlag);
		table.setEnabled(enableFlag);
		pager.setEnabled(enableFlag);
		searchTable.setEnabled(enableFlag);
		searchPager.setEnabled(enableFlag);
		icon1.setEnabled(enableFlag);
		icon2.setEnabled(enableFlag);
	}

	private void searchTag() {
		
	       searchTable.clearRows();
	       for (int i=0; i<10; i++) {
				if (i%2 == 0) {
					searchTable.addRow(Color.WHITE,  "#바다");
				}else {
					searchTable.addRow(Color.GREY_LIGHTEN_4, "#3월 ( #나무, #초록, #입학식, #생일, #나무, #초록, #입학식, #생일, #나무, #초록, #입학식, #생일 )");
				}
	       }
		
	}

}
