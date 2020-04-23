package kr.or.visitkorea.admin.client.manager.widgets.editor.items;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.Location;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.WidgetLocation;
import kr.or.visitkorea.admin.client.manager.widgets.editor.controller.ResizeDragController;
import kr.or.visitkorea.admin.client.manager.widgets.editor.controller.EditAreaController;
import kr.or.visitkorea.admin.client.manager.widgets.editor.panel.ContentDetailPanel;

public class ItemBox extends MaterialPanel implements HasDoubleClickHandlers{

	/**
	 * WindowPanel direction constant, used in
	 * {@link ResizeDragController#makeDraggable(Widget, DirectionConstant)}.
	 */
	public static class DirectionConstant {

		public final int directionBits;

		public final String directionLetters;

		private DirectionConstant(int directionBits, String directionLetters) {
			this.directionBits = directionBits;
			this.directionLetters = directionLetters;
		}
	}

	/**
	 * Specifies that resizing occur at the east edge.
	 */
	public static final int DIRECTION_EAST = 0x0001;

	/**
	 * Specifies that resizing occur at the both edge.
	 */
	public static final int DIRECTION_NORTH = 0x0002;

	/**
	 * Specifies that resizing occur at the south edge.
	 */
	public static final int DIRECTION_SOUTH = 0x0004;

	/**
	 * Specifies that resizing occur at the west edge.
	 */
	public static final int DIRECTION_WEST = 0x0008;

	/**
	 * Specifies that resizing occur at the east edge.
	 */
	public static final DirectionConstant EAST = new DirectionConstant(DIRECTION_EAST, "e");

	/**
	 * Specifies that resizing occur at the both edge.
	 */
	public static final DirectionConstant NORTH = new DirectionConstant(DIRECTION_NORTH, "n");

	/**
	 * Specifies that resizing occur at the north-east edge.
	 */
	public static final DirectionConstant NORTH_EAST = new DirectionConstant(DIRECTION_NORTH | DIRECTION_EAST, "ne");

	/**
	 * Specifies that resizing occur at the north-west edge.
	 */
	public static final DirectionConstant NORTH_WEST = new DirectionConstant(DIRECTION_NORTH | DIRECTION_WEST, "nw");

	/**
	 * Specifies that resizing occur at the south edge.
	 */
	public static final DirectionConstant SOUTH = new DirectionConstant(DIRECTION_SOUTH, "s");

	/**
	 * Specifies that resizing occur at the south-east edge.
	 */
	public static final DirectionConstant SOUTH_EAST = new DirectionConstant(DIRECTION_SOUTH | DIRECTION_EAST, "se");

	/**
	 * Specifies that resizing occur at the south-west edge.
	 */
	public static final DirectionConstant SOUTH_WEST = new DirectionConstant(DIRECTION_SOUTH | DIRECTION_WEST, "sw");

	/**
	 * Specifies that resizing occur at the west edge.
	 */
	public static final DirectionConstant WEST = new DirectionConstant(DIRECTION_WEST, "w");

	private static final int BORDER_THICKNESS = 6;

	private static final String CSS_DEMO_RESIZE_EDGE = "demo-resize-edge";

	private static final String CSS_DEMO_RESIZE_PANEL = "demo-WindowPanel";

	private static final String CSS_DEMO_RESIZE_PANEL_HEADER = "demo-WindowPanel-header";

	private int contentHeight;

	private Widget contentOrScrollPanelWidget;

	private int contentWidth;

	private Grid grid = new Grid(3, 3);

	private MenuItemForItemBox closeButton;
	
	private boolean initialLoad = false;

	private boolean isVisible = false;
	
	

	private Widget eastWidget;

	private Widget northWidget;

	private Widget southWidget;

	private Widget westWidget;
	
	private ContentDetailPanel contentPanel;
	

	private final EditAreaController windowController;

	private JSONObject componentData;

	private MaterialPanel menuPanel;

	public ItemBox(final EditAreaController windowController, Widget contentWidget, boolean wrapContentInScrollPanel, ContentDetailPanel contentPanel) {
		
		this.windowController = windowController;
		this.contentPanel = contentPanel;
		this.menuPanel = new MaterialPanel();
		this.menuPanel.setLayoutPosition(Position.ABSOLUTE);
		this.menuPanel.setBackgroundColor(Color.WHITE);
		this.menuPanel.setBorder("1px solid #EFEFEF");
		this.menuPanel.setBorderRadius("4px");
		this.menuPanel.setTop(-24);
		this.menuPanel.setRight(-24);
		this.menuPanel.setWidth("26px");
		this.menuPanel.setVisible(false);
		
		this.closeButton = new MenuItemForItemBox();
		this.closeButton.setLayoutPosition(Position.RELATIVE);
		this.closeButton.setIconType(IconType.REMOVE_CIRCLE);
		
		this.menuPanel.add(closeButton);
		
		addStyleName(CSS_DEMO_RESIZE_PANEL);

		contentOrScrollPanelWidget = wrapContentInScrollPanel ? new ScrollPanel(contentWidget) : contentWidget;
		
		windowController.getPickupDragController().makeDraggable(this, this);

		
		//setup component event~!
		setupEvent();
		
		grid.setCellSpacing(0);
		grid.setCellPadding(0);
		add(grid);

		setupCell(0, 0, NORTH_WEST);
		northWidget = setupCell(0, 1, NORTH);
		setupCell(0, 2, NORTH_EAST);

		westWidget = setupCell(1, 0, WEST);
		grid.setWidget(1, 1, contentOrScrollPanelWidget);
		eastWidget = setupCell(1, 2, EAST);

		setupCell(2, 0, SOUTH_WEST);
		southWidget = setupCell(2, 1, SOUTH);
		setupCell(2, 2, SOUTH_EAST);
		
		add(menuPanel);
		
		this.closeButton.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		
		setVisibleBoarder(false);
	}

	private void setupEvent() {
		
		MaterialWidget CTSPanel = (MaterialWidget)contentOrScrollPanelWidget;
		
		if (CTSPanel != null) {
			
			// mouse down handler for target widget
			CTSPanel.addMouseDownHandler(event->{
	
				if (this.contentPanel.getSelectedItemBox() == null || !this.contentPanel.getSelectedItemBox().equals(this)) {
					
					if (this.contentPanel.getSelectedItemBox() != null) {
						this.contentPanel.getSelectedItemBox().setVisibleBoarder(false);
					}
					
					this.contentPanel.setSelectedItemBox(this);
					setVisibleBoarder(true);
				}
				
				this.contentPanel.getEditor().getPropertiesPanel().setContent(this);
				
			});
			
		}
		
		// keyboard down handler for taget widget
		addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				MaterialToast.fireToast(event.toDebugString());
				
			}
		});
		
		addClickHandler(event->{
			
			AbsolutePanel boundaryPanel = windowController.getBoundaryPanel();
			
			if (boundaryPanel.getWidgetIndex(ItemBox.this) < boundaryPanel.getWidgetCount() - 1) {
				
				WidgetLocation location = new WidgetLocation(ItemBox.this, boundaryPanel);
				boundaryPanel.add(ItemBox.this, location.getLeft(), location.getTop());
				
			}

		});
		
		
		this.closeButton.addMouseDownHandler(event->{
			
			JSONObject paramJSONObject = EditorContentDialog.buildDiagParameters(
				new String[] {"삭제"}, 	// title define
				new String[] {"해당 아이템을 삭제하시겠습니까?"},   // dialog header define
				new String[] {"삭제되면 되돌릴 수 없으니 신중하게 선택 해 주세요."}   // dialog body define
			);
			
			contentPanel.getEditor().openDialog(EditorContentDialog.TYPE.CONFIRM, this, paramJSONObject);
			
		});
		
	}

	private void setupContentPanelProperties() {
		
	}
	
	public void setSelected() {
		this.contentOrScrollPanelWidget.fireEvent(new MouseDownEvent() {});
	}

	public void setVisibleBoarder(boolean visible) {
		
		setVisible(0, 0, visible);
		setVisible(0, 1, visible);
		setVisible(0, 2, visible);
		setVisible(1, 0, visible);
		setVisible(1, 2, visible);
		setVisible(2, 0, visible);
		setVisible(2, 1, visible);
		setVisible(2, 2, visible);
		
		this.menuPanel.setVisible(visible);

	}

	public int getContentHeight() {
		return contentHeight;
	}

	public int getContentWidth() {
		return contentWidth;
	}

	public void moveBy(int right, int down) {
		AbsolutePanel parent = (AbsolutePanel) getParent();
		Location location = new WidgetLocation(this, parent);
		int left = location.getLeft() + right;
		int top = location.getTop() + down;
		parent.setWidgetPosition(this, left, top);
	}

	public void setContentSize(int width, int height) {
		
		if (width != contentWidth) {
			contentWidth = width;
			northWidget.setPixelSize(contentWidth, BORDER_THICKNESS);
			southWidget.setPixelSize(contentWidth, BORDER_THICKNESS);
		}
		
		if (height != contentHeight) {
			contentHeight = height;
			westWidget.setPixelSize(BORDER_THICKNESS, contentHeight);
			eastWidget.setPixelSize(BORDER_THICKNESS, contentHeight);
		}
		
		contentOrScrollPanelWidget.setPixelSize(contentWidth, contentHeight);
		
	}

	private Widget setupCell(int row, int col, DirectionConstant direction) {
		
		final FocusPanel widget = new FocusPanel();
		
		widget.setPixelSize(BORDER_THICKNESS, BORDER_THICKNESS);
		
		grid.setWidget(row, col, widget);
		
		windowController.getResizeDragController().makeDraggable(widget, direction);
		
		grid.getCellFormatter().addStyleName(row, col, CSS_DEMO_RESIZE_EDGE + " demo-resize-" + direction.directionLetters);
		
		return widget;
		
	}
	
	private void setVisible(int row, int col, boolean visible) {
		if (visible) {
			grid.getCellFormatter().getElement(row, col).getStyle().setVisibility(Visibility.VISIBLE);
		}else {
			grid.getCellFormatter().getElement(row, col).getStyle().setVisibility(Visibility.HIDDEN);
		}
		
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!initialLoad && contentOrScrollPanelWidget.getOffsetHeight() != 0) {
			initialLoad = true;
			setContentSize(contentOrScrollPanelWidget.getOffsetWidth(), contentOrScrollPanelWidget.getOffsetHeight());
		}
	}

	public void setComponentData(JSONObject targetObject) {
		this.componentData = targetObject;
	}
	
	public JSONObject getComponentData() {
		return this.componentData;
	}
}