package kr.or.visitkorea.admin.client.manager.contents.components;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsComponents;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class AbstractContentComponent extends MaterialPanel {
	private EventContentsComponents eventContentsComponents;
	private MaterialExtentsWindow window;
	private String evcId;
	private String ewcId;
	private String evtId;
	private ContentComponentType componentType;
	protected MaterialLabel titleLabel;
	private MaterialIcon orderUpIcon;
	private MaterialIcon orderDownIcon;
	private MaterialIcon removeIcon;
	private MaterialIcon modifyIcon;
	private MaterialIcon saveIcon;
	protected int orderIdx;
	protected JSONObject internalObject = new JSONObject();
	protected ContentTreeItem treeItem;			//	컴포넌트 각자 자신의 트리아이템
	protected boolean isEditMode = false;		//	false : 일반모드, true : 수정모드
	protected MaterialRow titleRow;

	//	컴포넌트별 레이아웃 구성
	protected abstract void init();
	
	//	컴포넌트별 수정 버튼 클릭 시
	protected abstract void modifyClickAction();
	
	//	컴포넌트별 저장 버튼 클릭 시
	protected abstract void saveClickAction();
	
	//	컴포넌트 내용 setup
	public abstract void setupContents();
	
	public EventContentsComponents getEventContentsComponents() {
		return eventContentsComponents;
	}

	public void setEventContentsComponents(EventContentsComponents eventContentsComponents) {
		this.eventContentsComponents = eventContentsComponents;
	}

	public AbstractContentComponent(ContentComponentType componentType) {
		super();
		this.componentType = componentType;
		this.buildLayout();
		this.init();
	}
	
	private void buildLayout() {
		this.setMarginBottom(10);
		this.buildHeader();
	}
	
	private void buildHeader() {
		titleRow = new MaterialRow();
		titleRow.setWidth("100%");
		titleRow.setHeight("26px");
		titleRow.setBackgroundColor(Color.GREY_LIGHTEN_3);
		titleRow.setMargin(0);
		
		titleLabel = new MaterialLabel();
		titleLabel.setHeight("26px");
		titleLabel.setTextAlign(TextAlign.CENTER);
		titleLabel.setDisplay(Display.INLINE_BLOCK);
		titleRow.add(titleLabel);
		this.setTitle(this.componentType);

		orderUpIcon = addIcon(titleRow, IconType.ARROW_UPWARD, Float.LEFT);
		orderUpIcon.addClickHandler(e -> {
			if (this.eventContentsComponents != null)
				this.eventContentsComponents.orderUp(this.orderIdx);
		});

		orderDownIcon = addIcon(titleRow, IconType.ARROW_DOWNWARD, Float.LEFT);
		orderDownIcon.addClickHandler(e -> {
			if (this.eventContentsComponents != null)
				this.eventContentsComponents.orderDown(this.orderIdx);
		});
		
		removeIcon = addIcon(titleRow, IconType.REMOVE, Float.RIGHT);
		removeIcon.addClickHandler(e -> {
			this.remove();
		});

		modifyIcon = addIcon(titleRow, IconType.EDIT, Float.RIGHT);
		modifyIcon.addClickHandler(e -> {
			this.switchSaveEditIcon(true);
			this.modifyClickAction();
		});
		
		saveIcon = addIcon(titleRow, IconType.SAVE, Float.RIGHT);
		saveIcon.setVisible(false);
		saveIcon.addClickHandler(e -> {
			this.switchSaveEditIcon(false);
			this.saveClickAction();
		});
		
		this.add(titleRow);
	}
	
	private void remove() {
		this.getWindow().confirm("삭제 경고", "해당 컴포넌트를 삭제시겠습니까?", 450, e -> {
			if (e.getSource().toString().contains("yes")) {
				this.internalObject.put("IS_DELETE", JSONBoolean.getInstance(true));
				this.treeItem.removeFromParent();
				this.removeFromParent();

				if (this.eventContentsComponents != null)
					this.eventContentsComponents.renderComponent();
			}
		});
	}
	
	private void switchSaveEditIcon(boolean saveVisible) {
		this.saveIcon.setVisible(saveVisible);
		this.modifyIcon.setVisible(!saveVisible);
	}
	
	private void setTitle(ContentComponentType componentType) {
		if (componentType ==  ContentComponentType.TEXT)
			this.titleLabel.setText("텍스트");
		if (componentType == ContentComponentType.IMAGE)
			this.titleLabel.setText("이미지");
	}

	public void visibleSaveEditIcon(boolean modifyIcon, boolean saveIcon, boolean removeIcon) {
		this.saveIcon.setVisible(saveIcon);
		this.modifyIcon.setVisible(modifyIcon);
		this.removeIcon.setVisible(removeIcon);
	}
	
	public void visibleOrderIcon(boolean upIcon, boolean downIcon) {
		this.orderUpIcon.setVisible(upIcon);
		this.orderDownIcon.setVisible(downIcon);
	}
	
	public void visibleAllIcons(boolean isVisible) {
		this.orderUpIcon.setVisible(isVisible);
		this.orderDownIcon.setVisible(isVisible);
		this.modifyIcon.setVisible(isVisible);
		this.saveIcon.setVisible(isVisible);
		this.removeIcon.setVisible(isVisible);
	}
	
	public int getOrderIdx() {
		return orderIdx;
	}
	
	public void setOrderIdx(int orderIdx) {
		this.orderIdx = orderIdx;
	}
	
	public JSONObject getInternalObject() {
		return internalObject;
	}
	
	public void setInternalObject(JSONObject internalObject) {
		this.internalObject = internalObject;
	}
	
	public MaterialExtentsWindow getWindow() {
		return window;
	}

	public void setWindow(MaterialExtentsWindow window) {
		this.window = window;
	}

	public ContentTreeItem getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(ContentTreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public MaterialIcon getOrderUpIcon() {
		return orderUpIcon;
	}

	public MaterialIcon getOrderDownIcon() {
		return orderDownIcon;
	}

	public String getEvcId() {
		return evcId;
	}

	public void setEvcId(String evcId) {
		this.evcId = evcId;
	}

	public String getEwcId() {
		return ewcId;
	}

	public void setEwcId(String ewcId) {
		this.ewcId = ewcId;
	}
	
	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public ContentComponentType getComponentType() {
		return componentType;
	}

	protected MaterialIcon addIcon(MaterialWidget parent, IconType iconType, Float floatAlign) {
		MaterialIcon icon = new MaterialIcon(iconType);
		icon.setFloat(floatAlign);
		icon.setMargin(0);
		icon.setFontSize("26px");
		parent.add(icon);
		return icon;
	}
	
}
