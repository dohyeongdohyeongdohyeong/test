package kr.or.visitkorea.admin.client.widgets.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.MaterialAddins;
import gwt.material.design.addins.client.base.constants.AddinsCssName;
import gwt.material.design.addins.client.dnd.MaterialDnd;
import gwt.material.design.addins.client.dnd.constants.Restriction;
import gwt.material.design.addins.client.dnd.js.JsDragOptions;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.base.mixin.ToggleStyleMixin;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.ApplicationBusiness;
import kr.or.visitkorea.admin.client.manager.contents.author.AuthorApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AlertDialog;
import kr.or.visitkorea.admin.client.manager.widgets.ConfirmDialog;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;

//@formatter:off

/**
 * Window is another kind of Modal but it has a header toolbar for maximizing and
 * close the window. Also you can attached a tab component on its content.
 * <p>
 * <h3>XML Namespace Declaration</h3>
 * <pre>
 * {@code
 * xmlns:ma='urn:import:gwt.material.design.addins.client'
 * }
 * </pre>
 * <p>
 * <h3>UiBinder Usage:</h3>
 * <pre>
 * {@code
 *  <ma:window.MaterialWindow ui:field="windowContainer" />
 * }
 * </pre>
 * <p>
 * <h3>UiBinder Usage:</h3>
 * <pre>
 * {@code
 *  // Opening a window
 *  windowContainer.open();
 *
 *  // Closing a window
 *  windowContainer.close();
 * }
 * </pre>
 *
 * @author kevzlou7979
 * @see <a href="http://gwtmaterialdesign.github.io/gwt-material-demo/#window">Material Window</a>
 */
//@formatter:on
public class MaterialExtentsWindow extends MaterialPanel implements HasCloseHandlers<Boolean>, HasOpenHandlers<Boolean> {

    static {
        if (MaterialAddins.isDebug()) {
            MaterialDesignBase.injectCss(MaterialWindowDebugClientBundle.INSTANCE.windowCssDebug());
        } else {
            MaterialDesignBase.injectCss(MaterialWindowClientBundle.INSTANCE.windowCss());
        }
    }

    public static String DEFAULT_ALERT = "DEFAULT_ALERT";
    public static String DEFAULT_CONFIRM = "DEFAULT_CONFIRM";
    
    private static MaterialPanel windowOverlay;
    private static int windowCount = 0;
    private MaterialAnimation openAnimation;
    private MaterialAnimation closeAnimation;
    private WindowDragAndDrop dnd;
    private MaterialPanel content = new MaterialPanel();
    private MaterialPanel dialogOverlay = new MaterialPanel();
    private MaterialPanel dialog = new MaterialPanel();
    
    private Map<String, DialogContent> dialogMap = new HashMap<String, DialogContent>();
    private Map<String, ApplicationBusiness> businessMap = new HashMap<String, ApplicationBusiness>();
    
    private HorizontalContentPanel contentPanel = new HorizontalContentPanel();
    protected boolean preventClose;
    protected MaterialLink labelTitle = new MaterialLink();
    protected MaterialPanel toolbar = new MaterialPanel();
    protected MaterialIcon iconFavorite = new MaterialIcon(IconType.LIVE_TV);
    protected MaterialIcon iconMaximize = new MaterialIcon(IconType.CHECK_BOX_OUTLINE_BLANK);
    protected MaterialIcon iconClose = new MaterialIcon(IconType.CLOSE);
    protected MaterialPanel addinToolbar = new MaterialPanel();
    private HandlerRegistration toolbarAttachHandler;

    private ToggleStyleMixin<MaterialWidget> maximizeMixin;
    private ToggleStyleMixin<MaterialExtentsWindow> openMixin;
	private int dialogTop;
	private int windowWidth;
//	private int windowHeight;
//	private int toolbarTop;
	private boolean openDialogFlag;
	private String actionTarget;
	private ContentsLink shortCut;
	private MaterialPanel shortcutbar;
	private List<AbstractContentPanel> contentList = new ArrayList<AbstractContentPanel>();
	private Map<String, Object> valueMap = new HashMap<String, Object>();

	private Map<String, String> helpUrlMaps;

    public MaterialExtentsWindow(Map<String, Object> inputVM, int paramChk) {
    	
        super(AddinsCssName.WINDOW);
        
        // shortcut
		shortCut = new ContentsLink();
		shortCut.setBorder("1px solid #065ce5");
		shortCut.setBorderRadius("3px");
		shortCut.setWaves(WavesType.DEFAULT);
		shortCut.setPadding(5);
		shortCut.setPaddingRight(10);
		shortCut.setTextColor(Color.WHITE);
		shortCut.setIconType(this.labelTitle.getIcon().getIconType());
		shortCut.setIconPosition(IconPosition.LEFT);
		shortCut.setFontSize("0.8em");
		shortCut.setText(this.labelTitle.getText());
		shortCut.setTargetWindow(this);
        
        dialogMap.put("REGISTRATION_FAVORITE", new HelpMovieDialog(this));
        
        content.setStyleName(AddinsCssName.CONTENT);
        content.setMarginTop(40);
        content.add(contentPanel);
        
        dialogOverlay.setStyleName("dialogOverlay");
        
        dialog.setStyleName("dialog");
        dialog.setShadow(3);
        
       // dialog.setTop(-560);
        toolbar.setStyleName(AddinsCssName.WINDOW_TOOLBAR);
        toolbar.setLayoutPosition(Position.ABSOLUTE);
        toolbar.setWidth("100%");
        addinToolbar.setStyleName(AddinsCssName.WINDOW_ACTION);
        
        labelTitle.setStyleName(AddinsCssName.WINDOW_TITLE);
        labelTitle.setIconType(IconType.ACCESSIBLE);
        labelTitle.setIconPosition(IconPosition.LEFT);
        
        iconClose.addStyleName(AddinsCssName.WINDOW_ACTION);
        iconClose.setCircle(true);
        iconClose.setWaves(WavesType.DEFAULT);

        iconMaximize.addStyleName(AddinsCssName.WINDOW_ACTION);
        iconMaximize.setCircle(true);
        iconMaximize.setWaves(WavesType.DEFAULT);

        
        iconFavorite.addStyleName(AddinsCssName.WINDOW_ACTION);
        iconFavorite.setCircle(true);
        iconFavorite.setWaves(WavesType.DEFAULT);

        addinToolbar.add(iconFavorite);
       	
        toolbar.setTop(0);
        toolbar.setLeft(0);
       	toolbar.add(labelTitle);
        toolbar.add(iconClose);
//        toolbar.add(iconMaximize);
        toolbar.add(addinToolbar);

        super.add(content);
        super.add(dialogOverlay);
        super.add(dialog);
        super.add(toolbar);
        
        dialogOverlay.setVisibility(Visibility.HIDDEN);
        dialog.setVisibility(Visibility.HIDDEN);
        
        setTop(100);

        if (inputVM == null) {
    		iconFavorite.setVisibility(Visibility.HIDDEN);
    		iconFavorite.setVisible(false);
        }else{
        	valueMap = inputVM;
        	if (valueMap.get("HELPS") == null) {
        		iconFavorite.setVisibility(Visibility.HIDDEN);
        		iconFavorite.setVisible(false);
        	}else {
        		iconFavorite.setVisibility(Visibility.VISIBLE);
        		iconFavorite.setVisible(true);
        		helpUrlMaps = (Map<String, String>) valueMap.get("HELPS");
        	}
        }
        
        
        dnd = buildDnd();
        


    }
    
    public void removeDND() {
    	dnd = null;
    }
    
    public Map<String, Object> getValueMap() {
		return valueMap;
	}

	@Override
	public void setHeight(String height) {
		super.setHeight(height);
		int intHeight = Integer.parseInt(height.toLowerCase().replaceAll("px", ""));
		int overlayTop = (intHeight * -1) + 40;
		dialogOverlay.setTop(overlayTop);
		this.dialogTop = (intHeight * -1 * 2) + 80;
		dialog.setTop(dialogTop);

    }
    
    @Override
	public void setWidth(String width) {
		super.setWidth(width);
		dialog.setLeft(40);
	}
    
    @Override
    public void setPixelSize(int width, int height) {
    	
    	this.windowWidth = width;
//    	this.windowHeight = height;
    	
    	if (width >= 0) {
    		setWidth(width + "px");
    	}
    	
    	if (height >= 0) {
    		setHeight(height + "px");
    	}
    	
    }

	public MaterialExtentsWindow(String title) {
        this(null, 0);
        setTitle(title);
    }

    public MaterialExtentsWindow(String title, Color backgroundColor, Color textColor) {
        this(title);
        setBackgroundColor(backgroundColor);
        setTextColor(textColor);
    }

    public MaterialExtentsWindow(String title, Color backgroundColor, Color textColor, Color toolbarColor) {
        this(title, backgroundColor, textColor);
        setToolbarColor(toolbarColor);
    }

	@Override
    protected void onLoad() {
    	
        super.onLoad();

        
        dialog.setTop((dialogTop* 2));
		//toolbar.setTop(this.dialogTop-340);
       
        // Add handlers to action buttons
        registerHandler(iconMaximize.addClickHandler(event -> toggleMaximize()));
/*
        registerHandler(toolbar.addDoubleClickHandler(event -> {
           toggleMaximize();
           Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.DEFAULT);
        }));
*/
        registerHandler(iconClose.addClickHandler(event -> {
            if (!preventClose) {
                if (!isOpen()) {
                    open();
                } else {
                    close();
                }
            }
        }));

        registerHandler(iconFavorite.addClickHandler(event -> {
        	if (helpUrlMaps != null) {
	        	Map<String, Object> diagParam = new HashMap<String, Object>();
	        	diagParam.put("HELPS", helpUrlMaps);
        		openDialog("REGISTRATION_FAVORITE", diagParam, 900);
        	}
        }));

        
        
    }
    
    /**
     * Override to provide custom options for window drag'n'drop
     */
    protected JsDragOptions buildDragOptions() {
    	Restriction restriction = new Restriction("body", true, 0, 0, 1.2, 1);
    	restriction.setRestriction(Restriction.Restrict.SELF);
        return JsDragOptions.create(restriction);
    }

    /**
     * Override to provide custom {@link MaterialDnd} instance. Default implementation will construct {@link MaterialDnd}
     * using options provided by {@link #buildDragOptions()} and will ignore drag events from content portion of
     * the window ({@link AddinsCssName#CONTENT}) as well from action buttons (close, maximize and other {@link AddinsCssName#WINDOW_ACTION}.
     */
    protected WindowDragAndDrop buildDnd() {
    	WindowDragAndDrop dnd = WindowDragAndDrop.draggable(this, buildDragOptions());
        dnd.ignoreFrom(".content, .window-action, .dialog");
        return dnd;
    }
    
    protected void onClose() {

    }

    protected void toggleMaximize() {
        setMaximize(!isMaximized());
    }

    @Override
    public void add(Widget child) {
    	int childCount = contentPanel.getChildren().size() * content.getWidth();
    	((AbstractContentPanel)child).setWindowParameters(this.valueMap);
    	this.contentList.add((AbstractContentPanel)child);
    	contentPanel.setWidth(childCount + "px");
        contentPanel.add(child);
    }

    public AbstractContentPanel getContentPanel(int index) {
    	return this.contentList.get(index);
    }
    
    @Override
    public boolean remove(Widget w) {
        return contentPanel.remove(w);
    }

    @Override
    public void insert(Widget child, int beforeIndex) {
    	contentPanel.insert(child, beforeIndex);
    }

    @Override
    public void clear() {
    	contentPanel.clear();
    }

    @Override
    public String getTitle() {
        return labelTitle.getText();
    }

    @Override
    public void setTitle(String title) {
        labelTitle.setText(title);
        this.shortCut.setText(title);
   }
    
    public void setTitleIconType(IconType iconType) {
        labelTitle.setIconType(iconType);
        this.shortCut.setIconType(iconType);
    }
    
    public void setTitleIconPosition(IconPosition iconPosition) {
    	labelTitle.setIconPosition(iconPosition);
    }
    
    public boolean isMaximized() {
        return getMaximizeMixin().isOn();
    }

    public void setMaximize(boolean maximize) {
        getMaximizeMixin().setOn(maximize);
        if (getMaximizeMixin().isOn()) {
            iconMaximize.setIconType(IconType.FILTER_NONE);
        } else {
            iconMaximize.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
        }
    }
    
    public void appendTitleWidget(Widget widget) {

    	widget.addStyleName(AddinsCssName.WINDOW_ACTION);
    	this.addinToolbar.add(widget);
    	
    }

    public static boolean isOverlay() {
        return windowOverlay != null && windowOverlay.isAttached();
    }

    public static void setOverlay(boolean overlay) {
        if(overlay) {
            if(windowOverlay == null) {
                windowOverlay = new MaterialPanel(AddinsCssName.WINDOW_OVERLAY);
            }
        } else {
            if(windowOverlay != null) {
                windowOverlay.removeFromParent();
                windowOverlay = null;
            }
        }
    }

    /**
     * Open the window.
     */
    public void open() {
    	open(null);
    }
    public void open(MaterialExtentsWindow window) {
    	
    	int childCount = this.contentPanel.getChildrenList().size();
    	this.contentPanel.setWidth(((childCount * windowWidth)) + "px");
    	
        if (!isAttached()) {
            RootPanel.get().add(this);
        }
        windowCount++;
        if(windowOverlay != null && !windowOverlay.isAttached()) {
            RootPanel.get().add(windowOverlay);
        }

        if (openAnimation == null) {
            getOpenMixin().setOn(true);
            OpenEvent.fire(this, true);
        } else {
            setOpacity(0);
            Scheduler.get().scheduleDeferred(() -> {
                getOpenMixin().setOn(true);
                openAnimation.animate(this, () -> OpenEvent.fire(this, true));
            });
        }
     // 최진석 추가
        if(window != null) {
        	addDialog(DEFAULT_ALERT, new AlertDialog(window));
        	addDialog(DEFAULT_CONFIRM, new ConfirmDialog(window));
        }
        
    }
    /**
     * alert with no paramters
     * @param dialogID
     */
    public void alert(String msg) {
    	alert(null, msg, 500);
    }
    public void alert(String msg, int diagWidth) {
    	alert(null, msg, diagWidth);
    }
	public void alert(String title, String msg, int diagWidth) {
		
		this.openDialogFlag = true;
		
      	DialogContent dContent = this.dialogMap.get(DEFAULT_ALERT);
		dContent.setHeight("100%");
		int contentHeight = dContent.getHeight();
		if(title != null)
			((AlertDialog)dContent).setTitle(title);
		((AlertDialog)dContent).setMsg(msg);
		dialog.setHeight(contentHeight+"px");
		dialog.clear();
        dialog.add(dContent);

        dialogOverlay.setVisibility(Visibility.VISIBLE);
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.windowWidth - diagWidth) / 2);
		dialog.setVisibility(Visibility.VISIBLE);		
		dialog.setTransform("translate(0,"+this.dialogTop+"px);");
		dialog.setTop(dialogTop);
		
	}
    public void alert(String msg, int diagWidth, ClickHandler handler) {

        this.openDialogFlag = true;

        DialogContent dContent = this.dialogMap.get(DEFAULT_ALERT);
        dContent.setHeight("100%");
        int contentHeight = dContent.getHeight();
        ((AlertDialog)dContent).setMsg(msg);
        ((AlertDialog)dContent).addClickHandler(handler);
        dialog.setHeight(contentHeight+"px");
        dialog.clear();
        dialog.add(dContent);

        dialogOverlay.setVisibility(Visibility.VISIBLE);
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.windowWidth - diagWidth) / 2);
        dialog.setVisibility(Visibility.VISIBLE);
        dialog.setTransform("translate(0,"+this.dialogTop+"px);");
        dialog.setTop(dialogTop);

    }
	/**
     * alert with no paramters
     * @param dialogID
     */
    public void confirm(String msg, final ClickHandler handler) {
    	confirm(null, msg, 500, handler);
    }
    public void confirm(String msg, int diagWidth, final ClickHandler handler) {
    	confirm(null, msg, diagWidth, handler);
    }
	public void confirm(String title, String msg, int diagWidth, final ClickHandler handler) {
		
		this.openDialogFlag = true;
//		MaterialPanel dialog = new MaterialPanel();
//		super.add(dialog);
//        dialog.setVisibility(Visibility.HIDDEN);
      	DialogContent dContent = this.dialogMap.get(DEFAULT_CONFIRM);
		dContent.setHeight("100%");
		int contentHeight = dContent.getHeight();
		if(title != null)
			((ConfirmDialog)dContent).setTitle(title);
		((ConfirmDialog)dContent).setMsg(msg);
		((ConfirmDialog)dContent).addHandler(handler);
		dialog.setHeight(contentHeight+"px");
		dialog.clear();
        dialog.add(dContent);

        dialogOverlay.setVisibility(Visibility.VISIBLE);
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.windowWidth - diagWidth) / 2);
		dialog.setVisibility(Visibility.VISIBLE);		
		dialog.setTransform("translate(0,"+this.dialogTop+"px);");
		dialog.setTop(dialogTop);
		
	}
	
    /**
     * Close the window.
     */
    public void close() {
        // Turn back the cursor to POINTER
        RootPanel.get().getElement().getStyle().setCursor(Style.Cursor.DEFAULT);

        windowCount--;
        if (closeAnimation == null) {
            getOpenMixin().setOn(false);
            if(windowOverlay != null && windowOverlay.isAttached() && windowCount < 1) {
                windowOverlay.removeFromParent();
            }
            CloseEvent.fire(this, false);
        } else {
            closeAnimation.animate(this, () -> {
                getOpenMixin().setOn(false);
                if(windowOverlay != null && windowOverlay.isAttached() && windowCount < 1) {
                    windowOverlay.removeFromParent();
                }
                CloseEvent.fire(this, false);
            });
        }
    	
        this.shortcutbar.remove(this.shortCut);
        
        RootPanel.get().remove(this);

    }

    public Color getToolbarColor() {
        return toolbar.getBackgroundColor();
    }

    public void setToolbarColor(Color toolbarColor) {
        if (toolbarAttachHandler != null) {
            toolbarAttachHandler.removeHandler();
            toolbarAttachHandler = null;
        }

        if (toolbarColor != null) {
            if (toolbar.isAttached()) {
                toolbar.setBackgroundColor(toolbarColor);
            } else {
                if (toolbarAttachHandler == null) {
                    toolbarAttachHandler = registerHandler(toolbar.addAttachHandler(attachEvent -> toolbar.setBackgroundColor(toolbarColor)));
                }
            }
        }
    }

    @Override
    public void setBackgroundColor(Color bgColor) {
        content.setBackgroundColor(bgColor);
    }

    @Override
    public Color getBackgroundColor() {
        return content.getBackgroundColor();
    }

    public void setOpenAnimation(final MaterialAnimation openAnimation) {
        this.openAnimation = openAnimation;
    }

    public void setCloseAnimation(final MaterialAnimation closeAnimation) {
        this.closeAnimation = closeAnimation;
    }

    @Override
    public HandlerRegistration addCloseHandler(final CloseHandler<Boolean> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    @Override
    public HandlerRegistration addOpenHandler(final OpenHandler<Boolean> handler) {
        return addHandler(handler, OpenEvent.getType());
    }

    public boolean isOpen() {
        return getOpenMixin().isOn();
    }

    /**
     * @deprecated can now reference the {@link MaterialExtentsWindow} directly.
     */
    @Deprecated
    public MaterialWidget getContainer() {
        return this;
    }

    public MaterialWidget getToolbar() {
        return toolbar;
    }

    public MaterialWidget getContent() {
        return content;
    }

    public MaterialIcon getIconMaximize() {
        return iconMaximize;
    }

    public MaterialIcon getIconClose() {
        return iconClose;
    }

    public MaterialLink getLabelTitle() {
        return labelTitle;
    }

    public static MaterialPanel getWindowOverlay() {
        return windowOverlay;
    }

    @Override
    public void setPadding(double padding) {
        content.setPadding(padding);
    }

    @Override
    public void setPaddingTop(double padding) {
        content.setPaddingTop(padding);
    }

    @Override
    public void setPaddingLeft(double padding) {
        content.setPaddingTop(padding);
    }

    @Override
    public void setPaddingRight(double padding) {
        content.setPaddingRight(padding);
    }

    @Override
    public void setPaddingBottom(double padding) {
        content.setPaddingBottom(padding);
    }

    public boolean isPreventClose() {
        return preventClose;
    }

    public void setPreventClose(boolean preventClose) {
        this.preventClose = preventClose;
    }

    /**
     * setup next, prev contentPanel transition.
     */
    public void next(int offset) {
    	if (this.openDialogFlag) closeDialog();
    	this.contentPanel.next(offset);
    }
    
    public void prev(int offset) {
    	if (this.openDialogFlag) closeDialog();
    	this.contentPanel.prev(offset);
    }

	public HorizontalContentPanel getContentPanel() {
		return this.contentPanel;
	}

	public void goContentSlider(int position) {
    	if (this.openDialogFlag) closeDialog();
    	this.contentPanel.go(position);
	}

	public void show(int position) {
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
//		cfg.setDelay(500);
		cfg.setDuration(100);
		this.setTransition(cfg);
   		this.setTransform("translate("+position+"px,0);");
		this.setLeft(position);
		
   		
	}

    
    /**
     * Set the area for the drag and drop, can be an {@link Element}
     * or a {@link String} selector.
     */
    public void setDndArea(Object dndArea) {
        if(dndArea instanceof UIObject) {
            dndArea = ((UIObject) dndArea).getElement();
        }
        if(dnd != null) {
            dnd.draggable(JsDragOptions.create(new Restriction(dndArea, true, 0, 0, 1.2, 1)));
        }
    }

    protected ToggleStyleMixin<MaterialWidget> getMaximizeMixin() {
        if (maximizeMixin == null) {
            maximizeMixin = new ToggleStyleMixin<>(this, AddinsCssName.MAXIMIZE);
        }
        return maximizeMixin;
    }

    protected ToggleStyleMixin<MaterialExtentsWindow> getOpenMixin() {
        if (openMixin == null) {
            openMixin = new ToggleStyleMixin<>(this, AddinsCssName.OPEN);
        }
        return openMixin;
    }

    public WindowDragAndDrop getDnd() {
        return dnd;
    }

    /**
     * open dialog with no paramters
     * @param dialogID
     */
	public void openDialog(String dialogID, int diagWidth) {
		
		this.openDialogFlag = true;
		
      	DialogContent dContent = this.dialogMap.get(dialogID);
		dContent.setHeight("100%");
		int contentHeight = dContent.getHeight();
		dialog.setHeight(contentHeight+"px");
		dialog.clear();
        dialog.add(dContent);

        dialogOverlay.setVisibility(Visibility.VISIBLE);
		
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.windowWidth - diagWidth) / 2);
		dialog.setVisibility(Visibility.VISIBLE);		
		dialog.setTransform("translate(0,"+this.dialogTop+"px);");
		dialog.setTop(dialogTop);
		
	}
	
	/**
	 * open dialog with paramters
	 * 
	 * @param string
	 * @param paramters
	 */
	public void openDialog(String dialogID, Map<String, Object> paramters, int diagWidth) {
		this.openDialogFlag = true;
		
      	DialogContent dContent = this.dialogMap.get(dialogID);
      	dContent.setParameters(paramters);
		dContent.setHeight("100%");
		int contentHeight = dContent.getHeight();
		dialog.setHeight(contentHeight+"px");
		dialog.clear();
        dialog.add(dContent);
     
        dialogOverlay.setVisibility(Visibility.VISIBLE);
		
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.windowWidth - diagWidth) / 2);
		dialog.setVisibility(Visibility.VISIBLE);		
		dialog.setTransform("translate(0,"+this.dialogTop+"px);");
		dialog.setTop(dialogTop);
	}
	
	/**
	 * open dialog with paramters
	 * 
	 * @param string
	 * @param paramters
	 */
	public void openDialog(String dialogID, Map<String, Object> paramters, int diagWidth, int diagHeight) {
		this.openDialogFlag = true;
		
      	DialogContent dContent = this.dialogMap.get(dialogID);
      	dContent.setParameters(paramters);
		dContent.setHeight("100%");
		
		dialog.setHeight(diagHeight+"px");
		dialog.clear();
        dialog.add(dContent);
     
        dialogOverlay.setVisibility(Visibility.VISIBLE);
		
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.windowWidth - diagWidth) / 2);
		dialog.setVisibility(Visibility.VISIBLE);		
		dialog.setTransform("translate(0,"+this.dialogTop+"px);");
		dialog.setTop(dialogTop);
	}
	
	public void openDialog(String dialogID, Map<String, Object> paramters, int diagWidth, ClickHandler handler) {

		this.openDialogFlag = true;
		
      	DialogContent dContent = this.dialogMap.get(dialogID);
      	dContent.setParameters(paramters);
		dContent.setHeight("100%");
		if(handler != null) {
			dContent.addHandler(handler);
		}
		int contentHeight = dContent.getHeight();
		dialog.setHeight(contentHeight+"px");
		dialog.clear();
		
        dialog.add(dContent);
        dialogOverlay.setVisibility(Visibility.VISIBLE);
		
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.windowWidth - diagWidth) / 2);
		dialog.setVisibility(Visibility.VISIBLE);		
		dialog.setTransform("translate(0,"+this.dialogTop+"px);");
		dialog.setTop(dialogTop);
		dialog.getElement().getStyle().setZIndex(9999);
		
	}
	public boolean isOpenDialog() {
		return this.openDialogFlag;
	}

    public void closeDialog() {
//    	this.openDialogFlag = false;
//    	this.dialogMap.clear();
		dialogOverlay.setVisibility(Visibility.HIDDEN);
		dialog.setTransform("translate(0,"+ ((dialogTop* 2))+"px);");
		dialog.setTop((dialogTop* 2));
//		dialog.clear();
    }

	/**
		define dialog area
	*/
	public void addDialog(String key, DialogContent dialogContent) {
		this.dialogMap.put(key, dialogContent);
	}

	public void actionTarget(String actionTarget) {
		this.actionTarget = actionTarget;
	}
	
	public String actionTarget(){
		return this.actionTarget;
	}
	
	/**
	 * append short cut.
	 */
	public ContentsLink getShortCut() {
		return this.shortCut;
	}

	public void setShortcutBar(MaterialPanel shortCutContainer) {
		this.shortcutbar = shortCutContainer;
		this.shortcutbar.add(this.shortCut);
	}

	public void setSelected() {
		this.shortCut.setBackgroundColor(Color.LIGHT_BLUE_DARKEN_3);
		this.toolbar.setBackgroundColor(Color.LIGHT_BLUE_DARKEN_3);
	}

	public void setNotSelected() {
		this.shortCut.setBackgroundColor(Color.BLUE);
		this.toolbar.setBackgroundColor(Color.BLUE);
	}

	public DialogContent getDialog(String key) {
		return this.dialogMap.get(key);
	}

    public void setDialogData(String diagId, String key, Object value) {
    	this.getDialog(diagId).getParameters().put(key, value);
    }
    
    public Object getDialogData(String diagId, String key) {
    	return this.getDialog(diagId).getParameters().get(key);
    }

    /**
     * 
     * append window busienss 
     * 
     */
	public void addBusiness(String key, ApplicationBusiness applicationBusiness) {
		this.businessMap.put(key, applicationBusiness);
	}
	
	public ApplicationBusiness getBusiness(String key) {
		return this.businessMap.get(key);
	}
	
	public void invokeBusiness(String key, JSONObject parameter, Func3<Object, String, Object> callback) {
		this.businessMap.get(key);
	}

    
}

