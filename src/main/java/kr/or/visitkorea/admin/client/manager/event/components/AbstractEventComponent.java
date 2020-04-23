package kr.or.visitkorea.admin.client.manager.event.components;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsComponents;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsResultComponents;
import kr.or.visitkorea.admin.client.manager.event.model.EventComponent;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class AbstractEventComponent extends MaterialPanel implements IEventComponent {
	private EventContentsComponents eventContentsComponents;
	private EventContentsResultComponents eventContentsResultComponents;
	private AbstractContentPanel host;
	private MaterialExtentsWindow window;
	private MaterialExtentsWindow materialExtendsWindow;
	private EventComponentType componentType;
	protected MaterialLabel titleLabel;
	private MaterialIcon orderUpIcon;
	private MaterialIcon orderDownIcon;
	private MaterialIcon removeIcon;
	private MaterialIcon modifyIcon;
	private MaterialIcon saveIcon;
	protected ContentTreeItem treeItem;			//	컴포넌트 각자 자신의 트리아이템
	protected boolean isEditMode = false;		//	false : 일반모드, true : 수정모드
	protected MaterialRow titleRow;
	protected EventComponent componentObj;

	//	컴포넌트별 레이아웃 구성
	protected abstract void init();
	
	//	컴포넌트별 수정 버튼 클릭 시
	protected abstract void modifyClickAction();
	
	//	컴포넌트별 저장 버튼 클릭 시
	protected abstract void saveClickAction();
	
	//	컴포넌트 내용 setup
	public abstract void setupContents();
	
	public AbstractContentPanel getMainContentPanel() {
		return host;
	}

	public void setMainContentPanel(AbstractContentPanel host) {
		this.host = host;
	}

	public EventContentsComponents getEventContentsComponents() {
		return eventContentsComponents;
	}
	public EventContentsResultComponents getEventContentsResultComponents() {
		return eventContentsResultComponents;
	}

	public void setEventContentsComponents(EventContentsComponents eventContentsComponents) {
		this.eventContentsComponents = eventContentsComponents;
	}
	
	public void setEventContentsResultComponents(EventContentsResultComponents eventContentsResultComponents) {
		this.eventContentsResultComponents = eventContentsResultComponents;
	}

	public AbstractEventComponent(EventComponentType componentType, AbstractContentPanel host) {
		super();
		this.componentType = componentType;
		this.host = host;
		this.buildLayout();
		this.init();
	}
	
	public AbstractEventComponent(EventComponentType componentType, AbstractContentPanel host , MaterialExtentsWindow window) {
		super();
		this.componentType = componentType;
		this.host = host;
		this.buildLayout();
		this.init();
		this.materialExtendsWindow = window;
	}
	
	public MaterialExtentsWindow getMaterialExtendsWindow() {
		return materialExtendsWindow;
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
				this.eventContentsComponents.orderUp(this.getComponentObj().getCompIdx());
			if (this.eventContentsResultComponents != null)
				this.eventContentsResultComponents.orderUp(this.getComponentObj().getCompIdx());
		});

		orderDownIcon = addIcon(titleRow, IconType.ARROW_DOWNWARD, Float.LEFT);
		orderDownIcon.addClickHandler(e -> {
			if (this.eventContentsComponents != null)
				this.eventContentsComponents.orderDown(this.getComponentObj().getCompIdx());
			if (this.eventContentsResultComponents != null)
				this.eventContentsResultComponents.orderDown(this.getComponentObj().getCompIdx());
		});
		
		removeIcon = addIcon(titleRow, IconType.REMOVE, Float.RIGHT);
		removeIcon.addClickHandler(e -> {
			this.remove();
		});

		modifyIcon = addIcon(titleRow, IconType.EDIT, Float.RIGHT);
		modifyIcon.addClickHandler(e -> {
			this.isEditMode = true;
			this.switchSaveEditIcon(true);
			this.modifyClickAction();
		});
		
		saveIcon = addIcon(titleRow, IconType.SAVE, Float.RIGHT);
		saveIcon.setVisible(false);
		saveIcon.addClickHandler(e -> {
			this.isEditMode = false;
			this.switchSaveEditIcon(false);
			this.saveClickAction();
		});
		
		this.add(titleRow);
	}
	
	private void remove() {
		this.getWindow().confirm("삭제 경고", "해당 컴포넌트를 삭제시겠습니까?", 450, e -> {
			if (e.getSource().toString().contains("yes")) {
				this.componentObj.setDelete(true);
				this.treeItem.removeFromParent();
				this.removeFromParent();

				if (this.eventContentsComponents != null)
					this.eventContentsComponents.renderComponent();
				if (this.eventContentsResultComponents != null)
					this.eventContentsResultComponents.renderComponent();
			}
		});
	}
	
	private void switchSaveEditIcon(boolean saveVisible) {
		this.saveIcon.setVisible(saveVisible);
		this.modifyIcon.setVisible(!saveVisible);
	}
	
	private void setTitle(EventComponentType componentType) {
		if (componentType ==  EventComponentType.TEXT)
			this.titleLabel.setText("텍스트");
		if (componentType == EventComponentType.IMAGE)
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


	public EventComponentType getComponentType() {
		return componentType;
	}

	public EventComponent getComponentObj() {
		return componentObj;
	}

	public void setComponentObj(EventComponent componentObj) {
		this.componentObj = componentObj;
	}

	protected MaterialIcon addIcon(MaterialWidget parent, IconType iconType, Float floatAlign) {
		MaterialIcon icon = new MaterialIcon(iconType);
		icon.setFloat(floatAlign);
		icon.setMargin(0);
		icon.setFontSize("26px");
		parent.add(icon);
		return icon;
	}

	protected MaterialColumn addColumn(MaterialWidget parent, String grid) {
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		parent.add(col);
		return col;
	}
	
	protected MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		parent.add(row);
		return row;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialWidget parent, String grid, HashMap<String, Object> valueMap) {
		SelectionPanel panel = new SelectionPanel();
		panel.setValues(valueMap);
		panel.setTextAlign(TextAlign.LEFT);
		panel.setMargin(0);
		panel.setEnabled(false);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(panel);
		col.setLineHeight(46.25);	
		parent.add(col);
		return panel;
	}
	
	protected MaterialTextBox addTextBox(MaterialWidget parent, String grid) {
		MaterialTextBox tbox = new MaterialTextBox();
		tbox.setMargin(0);
		tbox.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		tbox.setEnabled(false);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tbox);
		col.setLineHeight(46.25);	
		parent.add(col);
		return tbox;
	}
	
	protected MaterialLabel addLabel(MaterialWidget parent, String text, String grid, Color bgColor) {
		MaterialLabel label = new MaterialLabel();
		label.setText(text);
		label.setBackgroundColor(bgColor);
		label.setLineHeight(46.25);
		label.setHeight("46.25px");
		label.setFontSize("1.2em");
		label.setFontWeight(FontWeight.BOLD);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(label);
		col.setLineHeight(46.25);
		parent.add(col);
		return label;
	}

}
