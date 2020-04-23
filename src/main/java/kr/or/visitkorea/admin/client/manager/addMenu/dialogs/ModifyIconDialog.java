package kr.or.visitkorea.admin.client.manager.addMenu.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexDirection;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.FlexWrap;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.RadioButtonType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRadioButton;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.account.composite.MaterialContentTreeItem;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuMain;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ModifyIconDialog extends DialogContent {
	private MaterialRadioButton iconBtns;
	private MaterialRow selectedIconRow;
	private AddMenuMain host;
	private MaterialContentTreeItem selectedMenuItem;
	private MaterialIcon selectedIcon;
	
	public ModifyIconDialog(MaterialExtentsWindow tgrWindow, AddMenuMain amm) {
		super(tgrWindow);
		this.host = amm;
	}

	@Override
	public void init() {
		buildHeader();
		buildContent();
		buildFooter();
	}

	@Override
	public int getHeight() {
		return 600;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.selectedMenuItem = (MaterialContentTreeItem) this.getParameters().get("selectedMenuItem");
		this.selectedIcon = new MaterialIcon(IconType.valueOf(selectedMenuItem.get("icon").toString()));
		setupIcon(this.selectedIcon);
	}

	public void buildHeader() {
		MaterialLabel title = new MaterialLabel("아이콘 수정하기");
		title.setTextColor(Color.BLUE);
		title.setFontSize("1.4em");
		title.setTextAlign(TextAlign.LEFT);
		title.setFontWeight(FontWeight.BOLD);
		title.setPadding(15);
		this.add(title);
	}
	
	public void buildContent() {
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setHeight("395px");
		panel.setDisplay(Display.FLEX);
		panel.setFlexWrap(FlexWrap.WRAP);
		panel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
		selectedIconRow = new MaterialRow();
		selectedIconRow.setWidth("100%");
		selectedIconRow.setHeight("50px");
		selectedIconRow.setTextAlign(TextAlign.CENTER);
		selectedIconRow.setMargin(15);
		
		for (IconType i : IconType.values()) {
			if (i.equals(IconType.values()[0])) {
				
				continue;
			} 
			appendIcon(panel, i);
		};

		this.add(selectedIconRow);
		this.add(panel);
	}
	
	public void appendIcon(MaterialPanel panel, IconType i) {
		MaterialLabel iconLabel = new MaterialLabel();
		iconLabel.setWidth("10%");
		iconLabel.setDisplay(Display.FLEX);
		iconLabel.setFlexDirection(FlexDirection.COLUMN);
		iconLabel.setFlexJustifyContent(FlexJustifyContent.CENTER);
		iconLabel.setFlexAlignItems(FlexAlignItems.CENTER);
		iconLabel.setPadding(5);
		
		MaterialIcon icon = new MaterialIcon(i);
		icon.setFontSize("35px");
		
		iconBtns = new MaterialRadioButton();
		iconBtns.setName("icon");
		iconBtns.setType(RadioButtonType.GAP);
		iconBtns.setWidth("24px");
		iconBtns.addClickHandler(e -> {
			this.selectedIcon.setIconType(i);
		});
		
		iconLabel.add(icon);
		iconLabel.add(iconBtns);
		panel.add(iconLabel);
	}
	
	public void buildFooter() {
		MaterialRow btnRow = new MaterialRow();
		btnRow.setMarginTop(10);
		btnRow.setWidth("100%");
		btnRow.setTextAlign(TextAlign.RIGHT);
		
		MaterialButton submitBtn = new MaterialButton("확인");
		submitBtn.setMarginRight(10);
		submitBtn.addClickHandler(e -> {
			modifyAction();
		});
		
		MaterialButton cancelBtn = new MaterialButton("취소");
		cancelBtn.setMarginRight(10);
		cancelBtn.addClickHandler(e -> {
			getMaterialExtentsWindow().closeDialog();
		});
		
		btnRow.add(submitBtn);
		btnRow.add(cancelBtn);
		this.add(btnRow);
	}
	
	public void setupIcon(MaterialIcon icon) {
		selectedIconRow.clear();
		icon.setTextColor(Color.BLUE);
		icon.setFontSize("50px");
		selectedIconRow.add(icon);
	}
	
	public void modifyAction() {
		this.host.getIcon().setIconType(selectedIcon.getIconType());

		JSONObject selectedMenuObj = (JSONObject) this.selectedMenuItem.get("obj");
		selectedMenuObj.put("icon", new JSONString(selectedIcon.getIconType().name()));
		this.selectedMenuItem.setIconType(selectedIcon.getIconType());

		alert("변경 성공", 350, 250, new String[]{"아이콘이 변경되었습니다."});
	}
}
