package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.LoaderType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentComposite;
import kr.or.visitkorea.admin.client.manager.widgets.ContentLoader;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class AbtractRecommContents extends AbstractContentComposite {

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	private MaterialExtentsWindow window;
//	private MaterialPanel rowsContainer;
//	private MaterialPanel rowsContainerCover;

	public AbtractRecommContents(MaterialExtentsWindow materialExtentsWindow) {
		super();
		this.window = materialExtentsWindow;
	}
	
	public MaterialExtentsWindow getWindow() {
		return this.window;
	}

	protected MaterialLabel titleLabel;
	private MaterialIcon saveIcon;
	private ContentLoader loader;
	private String cotid;

	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		buildTitle();

		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setWidth("959px");
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		this.loader = new ContentLoader();
		this.loader.setContainer(this);
		this.loader.setType(LoaderType.CIRCULAR);
	
	}

	public void buildTitle() {
		titleLabel = new MaterialLabel("");
		titleLabel.setBackgroundColor(Color.BLUE);
		titleLabel.setPaddingLeft(10);
		titleLabel.setPaddingTop(3);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.WHITE);
		titleLabel.setFontSize("1.3em");

		saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setPadding(4);
		saveIcon.setFloat(Float.RIGHT);
		saveIcon.setIconColor(Color.WHITE);
		saveIcon.setDisplay(Display.NONE);
		saveIcon.setTooltip("컨텐츠 저장");
		saveIcon.setMargin(0);

		add(saveIcon);
		add(titleLabel);
	}
	
	public MaterialIcon showSaveIconAndGetIcon() {
		saveIcon.setDisplay(Display.BLOCK);
		return saveIcon;
	}
	
	public void setTitle(String title) {
		this.titleLabel.setText(title);
	}
	
	public String getCotId() {
		return cotid;
	}

	public void setCotId(String cotid) {
		this.cotid = cotid;
	}
	
	abstract public void setReadOnly(boolean readFlag);

	protected SelectionPanel addSelectionViewPanel() {
		HashMap<String, Object> valueMap = new HashMap<>();
		valueMap.put("자동", 1);
		valueMap.put("수동", 0);
		
		SelectionPanel panel = new SelectionPanel();
		panel.setFloat(Float.RIGHT);
		panel.setMarginRight(10);
		panel.setMarginTop(4);
		panel.setDisplay(Display.INLINE_BLOCK);
		panel.setFontSize("0.8em");
		panel.setValues(valueMap);
		return panel;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, HashMap<String, Object> valueMap) {
		return addSelectionPanel(row, grid, align, valueMap, 5, 5, 8, true);
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, String fontSize, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setFontSize(fontSize);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected MaterialTextBox addInputText(MaterialRow row, String string, String grid) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setText(string);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}

	protected MaterialListValueBox<String> addList(MaterialRow row, String[] strings, String grid) {
		MaterialListValueBox<String> box = new MaterialListValueBox<String>();
		box.setMarginTop(0);
		box.setMarginBottom(0);
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		for (String str : strings) {
			box.addItem(str);
		}
		return box;
	}

	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}

	protected MaterialLink addLink(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLink tmpLabel = new MaterialLink(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}

	protected MaterialRow addRow(MaterialPanel panel) {
		MaterialRow row = new MaterialRow();
		panel.add(row);
		return row;
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}


	@Override
	public void buildOutLinkTypeContent() {
		
	}

	@Override
	public void buildDefaultTypeContent() {
		
	}

	protected void invokeQuery(String cmd, JSONObject parameterJSON, Func3<Object, String, Object> callback) {
		
		parameterJSON.put("cmd", new JSONString(cmd));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), callback);
	}

}
