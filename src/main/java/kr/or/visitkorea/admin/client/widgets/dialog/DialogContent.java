package kr.or.visitkorea.admin.client.widgets.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.widgets.ConfirmDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class DialogContent extends MaterialPanel {

    private MaterialPanel internalDialogOverlay = new MaterialPanel();
    private MaterialPanel internalDialog = new MaterialPanel();

	private MaterialExtentsWindow tgrWindow;
	private Map<String, Object> parameters;
	protected MaterialPanel buttonAreaPanel;
	protected MaterialButton okButton;
	protected int diagTop;
	private int contentHeight;
	private int diagWidth;
	
	public DialogContent() {
		super();
		init();

	}

	public DialogContent(String... initialClass) {
		super(initialClass);
		init();
	}

	protected DialogContent getPanel() {
		return this;
	}

	abstract public void init();

	public DialogContent(MaterialExtentsWindow tgrWindow) {
		super();
		this.tgrWindow = tgrWindow;
		this.parameters = new HashMap<String, Object>();
		init();
	}

	@Override
	protected void onLoad() {
        super.onLoad();
    }

	public MaterialExtentsWindow getMaterialExtentsWindow() {
		return this.tgrWindow;
	}
	
	abstract public int getHeight();

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Object> getParameters(){
		return this.parameters;
	}

	public MaterialButton getCloseButton() {
		return this.okButton;
	}
	
	protected void addDefaultButtons() {
		
		if (buttonAreaPanel == null) {
			buttonAreaPanel = new MaterialPanel();
			buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
			buttonAreaPanel.setWidth("100%");
			buttonAreaPanel.setPaddingLeft(30);
			buttonAreaPanel.setPaddingRight(30);
			buttonAreaPanel.setLeft(0); 
			buttonAreaPanel.setBottom(30); 
			this.add(buttonAreaPanel); 
		} else{
			remove(buttonAreaPanel);
			buttonAreaPanel.clear();
			this.add(buttonAreaPanel);
			
		}

		okButton = new MaterialButton("닫기");
		okButton.setLayoutPosition(Position.RELATIVE);
		okButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		okButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		
		buttonAreaPanel.add(okButton);
	}

	public void addButton(MaterialButton button) {
		button.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		button.setMarginRight(10);
		buttonAreaPanel.add(button);
	}

	public void open(DialogContent dContent, Map<String, Object> parameters, int width) {
		internalDialog.clear();
		internalDialog.setTop(this.getHeight() * -2);
		
		dContent.setParameters(parameters);
		dContent.setHeight("100%");
      	addContent(dContent);

      	if (dContent.getButtonArea() != null)
      		Console.log("buttons :: " + dContent.getButtonArea().getChildrenList().size());
		
      	internalDialogOverlay.setVisibility(Visibility.VISIBLE);
      	internalDialogOverlay.setVisible(true);

		diagWidth = width;
		contentHeight = dContent.getHeight();
		diagTop = contentHeight * -2 ;
		internalDialog.setLayoutPosition(Position.ABSOLUTE);
		internalDialog.setHeight(contentHeight+"px");
		internalDialog.getElement().setAttribute("id", "dialog");
        internalDialog.setWidth(diagWidth+"px");
        internalDialog.setLeft((this.getWidth() - diagWidth) / 2);
		internalDialog.setVisibility(Visibility.VISIBLE);
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(300);
		internalDialog.setTransition(cfg);
		internalDialog.setTransform("translate(0,0);");
		internalDialog.setTop(0);
	}
	
	public void open(String title, int width, int height, String[] strings, List<ButtonInfo> btnInfos) {
		
		internalDialog.clear();
		internalDialog.setTop(this.getHeight() * -2);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("title", title);
		parameters.put("messages", strings);
		parameters.put("width", width);
		parameters.put("buttonInfos", btnInfos);
		
      	DialogContent dContent = new NormalDialogContent();
      	addContent(dContent);
      	dContent.setParameters(parameters);
      	dContent.setHeight("100%");
      	
      	internalDialogOverlay.setVisibility(Visibility.VISIBLE);
      	internalDialogOverlay.setVisible(true);

		diagWidth = (int)parameters.get("width");
		contentHeight = height;
		diagTop = contentHeight * -2 ;
		internalDialog.setLayoutPosition(Position.ABSOLUTE);
		internalDialog.setHeight(contentHeight+"px");
		internalDialog.getElement().setAttribute("id", "dialog");
        internalDialog.setWidth(diagWidth+"px");
        internalDialog.setLeft((this.getWidth() - diagWidth) / 2);
		internalDialog.setVisibility(Visibility.VISIBLE);
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(300);
		internalDialog.setTransition(cfg);
		internalDialog.setTransform("translate(0,0);");
		internalDialog.setTop(0);
		
	}
	
public void open(String title, int width, int height, String[] strings, List<ButtonInfo> btnInfos,int zindex) {
		
		internalDialog.clear();
		internalDialog.setTop(this.getHeight() * -2);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("title", title);
		parameters.put("messages", strings);
		parameters.put("width", width);
		parameters.put("buttonInfos", btnInfos);
		
      	DialogContent dContent = new NormalDialogContent();
      	addContent(dContent);
      	dContent.setParameters(parameters);
      	dContent.setHeight("100%");
      	
      	internalDialogOverlay.setVisibility(Visibility.VISIBLE);
      	internalDialogOverlay.setVisible(true);

		diagWidth = (int)parameters.get("width");
		contentHeight = height;
		diagTop = contentHeight * -2 ;
		internalDialog.setLayoutPosition(Position.ABSOLUTE);
		internalDialog.setHeight(contentHeight+"px");
		internalDialog.getElement().setAttribute("id", "dialog");
        internalDialog.setWidth(diagWidth+"px");
        internalDialog.setLeft((this.getWidth() - diagWidth) / 2);
		internalDialog.setVisibility(Visibility.VISIBLE);
		internalDialog.getElement().getStyle().setZIndex(zindex);
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(300);
		internalDialog.setTransition(cfg);
		internalDialog.setTransform("translate(0,0);");
		internalDialog.setTop(0);
		
	}

	public void openImage(String title, int width, int height, MaterialImage widget, List<ButtonInfo> btnInfos, Map<String, Object> params) {
		
		internalDialog.clear();
		internalDialog.setTop(this.getHeight() * -2);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("title", title);
		parameters.put("width", width);
		parameters.put("materialImage", widget);
		parameters.put("buttonInfos", btnInfos);
		
		Set<String> keys = params.keySet();
		for (String key : keys) {
			parameters.put(key, params.get(key));
		}

		ImageDialogContent dContent = new ImageDialogContent();
		
      	addContent(dContent);
		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED);
		selectButton.setLayoutPosition(Position.RELATIVE);
		selectButton.setMarginRight(10);
		selectButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		selectButton.addClickHandler(event->{
			dContent.apply();
			closeDialog();
		});
		
		dContent.getButtonArea().add(selectButton);
      	dContent.setParameters(parameters);
      	dContent.setHeight("100%");
      	dContent.setBounds(width, height);
      	dContent.setGuideLabelVisible(true);
      	
      	internalDialogOverlay.setVisibility(Visibility.VISIBLE);
      	internalDialogOverlay.setVisible(true);

		diagWidth = (int)parameters.get("width");
		contentHeight = height;
		diagTop = contentHeight * -2 ;
		internalDialog.setLayoutPosition(Position.ABSOLUTE);
		internalDialog.setHeight(contentHeight+"px");
		internalDialog.getElement().setAttribute("id", "dialog");
        internalDialog.setWidth(diagWidth+"px");
        internalDialog.setLeft((this.getWidth() - diagWidth) / 2);
		internalDialog.setVisibility(Visibility.VISIBLE);
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(300);
		internalDialog.setTransition(cfg);
		internalDialog.setTransform("translate(0,0);");
		internalDialog.setTop(0);
	}

	public void alert(String title, int width, int height, String[] messages) {
		open(title, width, height, messages, new ArrayList<ButtonInfo>() );		
	}
	public void alert(String title, int width, int height, String[] messages,int zindex) {
		open(title, width, height, messages, new ArrayList<ButtonInfo>(),zindex);		
	}
	

	protected void addButton(DialogContent targetPanel, String title, Color btnColor, ClickHandler handler, Float btnFloat) {
		
		MaterialButton btn = new MaterialButton(title);
		btn.setBackgroundColor(btnColor);
		btn.setLayoutPosition(Position.RELATIVE);
		btn.setFloat(btnFloat);
		btn.addClickHandler(handler);
		
		if (btnFloat.equals(Float.LEFT)) {
			btn.setMarginLeft(10);
			
		}else if (btnFloat.equals(Float.RIGHT)) {
			btn.setMarginRight(10);
		}
		
		targetPanel.getButtonArea().add(btn);
	}
	
	protected MaterialPanel getButtonArea() {
		return this.buttonAreaPanel;
	}

	public void setButtonArea(MaterialPanel areaPanel) {
		buttonAreaPanel = areaPanel;
	}
	
	public void addContent(DialogContent dContent) {
		
		this.add(internalDialogOverlay);
		this.add(internalDialog);
		
		if (dContent.getButtonArea() == null) {
			MaterialPanel subButtonAreaPanel = new MaterialPanel();
			subButtonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
			subButtonAreaPanel.setWidth("100%");
			subButtonAreaPanel.setPaddingLeft(30);
			subButtonAreaPanel.setPaddingRight(30);
			subButtonAreaPanel.setLeft(0); 
			subButtonAreaPanel.setBottom(30);
			
			dContent.setButtonArea(subButtonAreaPanel);
			dContent.add(subButtonAreaPanel);
			addCloseButton(dContent);
		}
		
		internalDialog.add(dContent);
		internalDialog.setBackgroundColor(Color.WHITE);
		internalDialog.setShadow(3);

		internalDialogOverlay.setTop(0);
		internalDialogOverlay.setClass("dialogOverlay");
		internalDialogOverlay.getElement().setAttribute("id", "dialogOverlay");
		internalDialogOverlay.setWidth("100%");
		internalDialogOverlay.setHeight("100%");
		internalDialogOverlay.setLayoutPosition(Position.ABSOLUTE);
		internalDialogOverlay.setVisibility(Visibility.HIDDEN);
		internalDialogOverlay.setVisible(false);
		
	}
	
	public void addCloseButton(DialogContent targetPanel) {
		
			
		
		MaterialButton closeButton = new MaterialButton("닫기");
		closeButton.setLayoutPosition(Position.RELATIVE);
		closeButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		closeButton.addClickHandler(event->{
			closeDialog();
		});
		targetPanel.getButtonArea().add(closeButton);
		
	}

	public void closeDialog() {
		
		internalDialogOverlay.setVisibility(Visibility.HIDDEN);
		internalDialogOverlay.setVisible(false);
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(300);

		internalDialog.setTransform("translate(0, "+ diagTop+"px);");
		internalDialog.setTop(diagTop);
	}
	
	// 최진석 추가
	protected ClickHandler handler;
	
	private boolean openDialogFlag;
	
	public void addHandler(final ClickHandler handler) {
		this.handler = handler;
	}
	
	protected String getString(JSONObject recObj, String key) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "";
		else return recObj.get(key).isString().stringValue();
	}
	
	protected Double getNumber(JSONObject jsonValue, String propName) {
		if (jsonValue.get(propName) == null) return -1d;
		else return jsonValue.get(propName).isNumber().doubleValue();
	}
	
	public void confirm(String msg, final ClickHandler handler) {
    	confirm(null, msg, 500, handler);
    }
	
	public void confirm(String title, String msg, int diagWidth, final ClickHandler handler) {
		
			this.openDialogFlag = true;
			
			ConfirmDialog dialog =  new ConfirmDialog(tgrWindow);
	      	
	      	HashMap<String, Object> params = new HashMap<>();
			params.put("host", this);
			
			dialog.setMsg(title,msg);
			dialog.addHandler(handler);
			
	    	this.open(dialog, params, diagWidth);
		}
	
	public void confirm(boolean thirdchk,String msg, final ClickHandler handler) {
    	confirm("경고", msg, 500, handler,thirdchk);
    }
	
	public void confirm(String title, String msg, int diagWidth, final ClickHandler handler,boolean thirdchk) {
		
		this.openDialogFlag = true;
		
		ConfirmDialog dialog =  new ConfirmDialog(tgrWindow);
      	
      	HashMap<String, Object> params = new HashMap<>();
		params.put("host", this);
		
		dialog.setMsg(title,msg);
		dialog.addHandler(handler);
		dialog.setThirdChk(true);
    	this.open(dialog, params, diagWidth);
	}
	
		
	}
