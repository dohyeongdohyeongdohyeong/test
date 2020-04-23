package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.LoaderType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;

public class ContentTable extends AbstractContentPanel{

	public static enum TABLE_SELECT_TYPE{
		SELECT_SINGLE, SELECT_MULTIPLE
	}
	
	private MaterialPanel header, tag;
	private MaterialPanel rowsContainer;
	private List<ContentTableRow> rowsList = new ArrayList<ContentTableRow>();
	private List<ContentTableRow> selectedRows = new ArrayList<ContentTableRow>();
	private List<ContentTableRow> selectedPanelSelectedRows = new ArrayList<ContentTableRow>();
	private List<Integer> widthList = new ArrayList<Integer>();
	private List<TextAlign> textAlignList = new ArrayList<TextAlign>();
	private int totalWidth;
	private TABLE_SELECT_TYPE selectType;
	private ContentMenu pager;
	private int dataOffset = 0;
	private int dataRowCount = 20;
	private JSONObject parameterJSON;
	private Func3<Object, String, Object> func3;
	private MaterialLoader loader;
	private MaterialPanel rowsContainerCover;
	private ContentMenu topArea;
	private MaterialPanel selectedContainer;
	private int contentHeight;
	private int newPosition = 510;
	
	public ContentTable(TABLE_SELECT_TYPE selectType) {
		this.selectType = selectType;
		this.setLayoutPosition(Position.RELATIVE);
	}

	public ContentTable(TABLE_SELECT_TYPE selectType, Position position) {
		this.selectType = selectType;
		this.setLayoutPosition(position);
	}

	@Override
	public void init() {
		rowcol = Color.WHITE;
		this.setOverflow(Overflow.HIDDEN);
		
		// 0, 27
		topArea = new ContentMenu();
		topArea.setLayoutPosition(Position.ABSOLUTE);
		topArea.setWidth("100%");
		topArea.setHeight("27px");
		topArea.setLeft(0);
		topArea.setTop(0);
		
		// 30, 40
		header = new MaterialPanel();
		header.setLayoutPosition(Position.ABSOLUTE);
		header.setHeight("40px");
		header.setWidth("100%");
		header.setLeft(0);
		header.setTop(28);
		header.setBorderLeft("1px solid #aaaaaa");
		header.setBorderRight("1px solid #aaaaaa");
		header.setBackgroundColor(Color.GREY_LIGHTEN_1);
		
		// 70, calculate height
		rowsContainer = new MaterialPanel();
		rowsContainer.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		rowsContainer.getElement().getStyle().setOverflowY(Overflow.AUTO);
		rowsContainer.setWidth("100%");
		rowsContainer.setLayoutPosition(Position.ABSOLUTE);
		rowsContainer.setPaddingLeft(2);
		rowsContainer.setPaddingRight(2);
		rowsContainer.setLeft(0);
		rowsContainer.setTop(68);
		rowsContainer.setBorderLeft("1px solid #aaaaaa");
		rowsContainer.setBorderRight("1px solid #aaaaaa");
		rowsContainer.setBackgroundColor(Color.WHITE);
	
		// 30, 40
		tag = new MaterialPanel();
		tag.setLayoutPosition(Position.ABSOLUTE);
		tag.setHeight("38px");
		tag.setWidth("100%");
		tag.setLeft(0);
		tag.setBottom(28);
		tag.setBorderLeft("1px solid #aaaaaa");
		tag.setBorderRight("1px solid #aaaaaa");
		tag.setBackgroundColor(Color.GREY_LIGHTEN_2);
		tag.setVisible(false);
		
		// 70 + calulate height, 27 
		pager = new ContentMenu();
		pager.setBorder("1px solid #aaaaaa");
		pager.setLayoutPosition(Position.ABSOLUTE);
		pager.setLeft(0);
		pager.setBottom(0);
		pager.setBackgroundColor(Color.WHITE);
		pager.setWidth("100%");
		
		rowsContainerCover = new MaterialPanel();
		rowsContainerCover.getElement().getStyle().setProperty("background", "rgba( 255, 255, 255, 0.5 )");
		rowsContainerCover.setLayoutPosition(Position.ABSOLUTE);
		rowsContainerCover.setLeft(1);
		rowsContainerCover.setTop(70);
		rowsContainerCover.setVisible(false);
		
		selectedContainer = new MaterialPanel();
		selectedContainer.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		selectedContainer.getElement().getStyle().setOverflowY(Overflow.AUTO);
		selectedContainer.setLayoutPosition(Position.ABSOLUTE);
		selectedContainer.setLeft(1);
		selectedContainer.setRight(2);
		selectedContainer.setTop(510);
		selectedContainer.setBackgroundColor(Color.GREY_LIGHTEN_2);
		selectedContainer.setVisible(false);

		this.add(topArea);
		this.add(header);
		this.add(rowsContainer);
		this.add(rowsContainerCover);
		this.add(selectedContainer);
		this.add(tag);
		this.add(pager);
		
		loader = new MaterialLoader();
		loader.setContainer(rowsContainerCover);
		loader.setType(LoaderType.CIRCULAR);

	}
	
	public ContentMenu getButtomMenu() {
		return this.pager;
	}
	
	public ContentMenu getTopMenu() {
		return this.topArea;
	}
	
	public void setTagVisible(boolean bb) {
		if(bb) {
			rowsContainer.setHeight((contentHeight-131) + "px");
		} else {
			rowsContainer.setHeight((contentHeight-93) + "px");
		}
		tag.setVisible(bb);
	}
	public void setHeight(int height) {
		this.contentHeight = height;
		this.setHeight(height +"px" );
		rowsContainer.setHeight((height-93) + "px");
		rowsContainerCover.setHeight((height-40) + "px");
		rowsContainerCover.setTop((height-14)*-1);
		selectedContainer.setTop(this.contentHeight + 100);
		selectedContainer.setHeight((height-93) + "px");
	}
	
	public void setSelectedContainerVisible(boolean visible) {
		selectedContainer.setVisible(visible);
	}
	
	public void setSelectedPanelLink(boolean visible) {
		
		MaterialIcon visiblePanelButton = new MaterialIcon(IconType.DELETE);
		visiblePanelButton.setTextAlign(TextAlign.CENTER);
		visiblePanelButton.addClickHandler(event->{
			
			if (newPosition == 68) {
				newPosition = this.contentHeight + 100;
			}else {
				newPosition = 68;
			}
			
			TransitionConfig cfg = new TransitionConfig();
			cfg.setProperty("top");
			cfg.setDuration(100);
			selectedContainer.setTransition(cfg);
			selectedContainer.setTransform("translate("+ ( newPosition )+"px,0);");
			selectedContainer.setTop(newPosition);

		});
		
		this.getButtomMenu().addIcon(visiblePanelButton, "선택 레코드 패널 보기 / 닫기", com.google.gwt.dom.client.Style.Float.RIGHT);

	}
	
	
	public void appendTitle(String title, int width, TextAlign textAlign) {
		appendTitle(title, width, textAlign, null);
	}
	
	public void appendTitle(String title, int width, TextAlign textAlign, String tooltip) {
		
		MaterialLabel __title = new MaterialLabel(title);
		__title.setTextAlign(TextAlign.CENTER);
		__title.setWidth(width + "px");
		__title.setFontWeight(FontWeight.BOLD);
		__title.setLineHeight(40);
		__title.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		if (tooltip !=null) __title.setTooltip(tooltip);
		
		header.add(__title);
		textAlignList.add(textAlign);
		this.setPixcelWidth(header, width);
	}
	public void removeTitleAll() {
		header.clear();
		widthList.clear();
		textAlignList.clear();
		tag.clear();
		rowcol = Color.WHITE;
	}
	public MaterialLabel getColumn(int index) {
		return (MaterialLabel) header.getChildrenList().get(index);
	}
	private void setPixcelWidth(MaterialPanel panel, int width) {
		this.totalWidth += width;
		widthList.add(width);
	}

    @Override
    protected void onLoad() {
        super.onLoad();
    }

	public void addSelctedRow(int idx) {
		ContentTableRow tableRow = rowsList.get(idx);
		rowsContainer.remove(tableRow);
		selectedContainer.add(tableRow);
	}

	public void setTopMenuVisible(boolean visible) {
		this.topArea.setVisibility(Visibility.HIDDEN);
		this.topArea.setVisible(visible);
	}

	public void setTopMenuDisplay(Display display) {
		this.topArea.setDisplay(display);
	}
	
	public void setHeaderVisiable(boolean visiable) {
		this.header.setVisible(visiable);
		if (visiable == false)
			this.header.setBorderTop("1px solid #aaaaaa");
	}
	
	public ContentTableRow addFirstRow(Color bgColor, Object ... values) {
		
		ContentTableRow rows = new ContentTableRow(widthList, textAlignList, bgColor, values);
		rows.setTotalWidth(this.totalWidth);

		rows.addMouseOverHandler(event->{
			ContentTableRow tgrRowPanel = (ContentTableRow)event.getSource();
			if (!this.selectedRows.contains(tgrRowPanel)) {
				tgrRowPanel.setBackgroundColor(Color.BLUE_LIGHTEN_4);
			}
		});

		rows.addMouseOutHandler(event->{
			ContentTableRow tgrRowPanel = (ContentTableRow)event.getSource();
			if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_SINGLE)) {
				if (!this.selectedRows.contains(tgrRowPanel)) {
					tgrRowPanel.restoreBackgroundColor();
				}
			}else if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_MULTIPLE)) {
				if (!this.selectedRows.contains(tgrRowPanel)) {
					tgrRowPanel.restoreBackgroundColor();
				}
			}
		});

		rows.addClickHandler(event->{
			
			ContentTableRow tgrRowPanel = (ContentTableRow)event.getSource();
			tgrRowPanel.setBackgroundColor(Color.RED_LIGHTEN_2);
			tgrRowPanel.setTextColor(Color.WHITE);
			
			if (this.selectedRows.isEmpty()) {
				this.selectedRows.add(tgrRowPanel);
			}else {
				if (this.selectedRows.contains(tgrRowPanel)) {
					//tgrRowPanel.restoreBackgroundColor();
					//this.selectedRows.remove(tgrRowPanel);
				}else {
					if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_SINGLE)) {
						this.selectedRows.remove(0);
						this.selectedRows.add(tgrRowPanel);
						List<Widget> children = rowsContainer.getChildrenList();
						for(Widget singleMember : children) {
							if (singleMember instanceof ContentTableRow && !singleMember.equals(tgrRowPanel)) {
								((ContentTableRow)singleMember).restoreBackgroundColor();
							}
						}
					}else if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_MULTIPLE)) {
						this.selectedRows.add(tgrRowPanel);
					}
				}
			}
			
		});
		
		rowsList.add(0, rows);
		rowsContainer.add(rows);
		return rows;
	}
	public ContentTableRow addTagRow(Color bgColor, Object ... values) {
		ContentTableRow rows = new ContentTableRow(widthList, textAlignList, bgColor, values);
		rows.setTotalWidth(this.totalWidth);
		tag.add(rows);
		return rows;
	}
	private Color rowcol = Color.WHITE;
	public ContentTableRow addRow(Color bgColor, Object ... values) {
		return addRow(bgColor, null, values);
	}
	public ContentTableRow addRow(Color bgColor, int[] link, Object ... values) {
		
		ContentTableRow rows = new ContentTableRow(widthList, textAlignList, rowcol, link, values);
		if(rowcol == Color.WHITE) rowcol = Color.GREY_LIGHTEN_4; else rowcol =Color.WHITE;
		rows.setTotalWidth(this.totalWidth);
		
		rows.addMouseOverHandler(event->{
			ContentTableRow tgrRowPanel = (ContentTableRow)event.getSource();
			if (!this.selectedRows.contains(tgrRowPanel)) {
				tgrRowPanel.setBackgroundColor(Color.BLUE_LIGHTEN_4);
			}
		});

		rows.addMouseOutHandler(event->{
			ContentTableRow tgrRowPanel = (ContentTableRow)event.getSource();
			if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_SINGLE)) {
				if (!this.selectedRows.contains(tgrRowPanel)) {
					tgrRowPanel.restoreBackgroundColor();
				}
			}else if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_MULTIPLE)) {
				if (!this.selectedRows.contains(tgrRowPanel)) {
					tgrRowPanel.restoreBackgroundColor();
				}
			}
		});

		rows.addClickHandler(event->{
			ContentTableRow tgrRowPanel = (ContentTableRow)event.getSource();
			tgrRowPanel.setBackgroundColor(Color.RED_LIGHTEN_4);
//			tgrRowPanel.setTextColor(Color.WHITE);
			if (this.selectedRows.isEmpty()) {
				this.selectedRows.add(tgrRowPanel);
			}else {
				if(this.selectedRows.contains(tgrRowPanel)) {
//					tgrRowPanel.restoreBackgroundColor();
					//this.selectedRows.remove(tgrRowPanel);
				} else {
					if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_SINGLE)) {
						this.selectedRows.remove(0);
						this.selectedRows.add(tgrRowPanel);
						List<Widget> children = rowsContainer.getChildrenList();
						for(Widget singleMember : children) {
							if (singleMember instanceof ContentTableRow && !singleMember.equals(tgrRowPanel)) {
								((ContentTableRow)singleMember).restoreBackgroundColor();
							}
						}
					}else if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_MULTIPLE)) {
						this.selectedRows.add(tgrRowPanel);
					}
				}
			}
			
		});
		
		rowsList.add(rows);
		rowsContainer.add(rows);
		return rows;
	}
	
	public void clearRows() {
		this.dataOffset = 0;
		this.dataRowCount = 20;
		if (this.parameterJSON != null) this.parameterJSON.put("offset", new JSONNumber(this.dataOffset));
		rowsContainer.clear();
		rowsList.clear();
		tag.clear();
		rowcol = Color.WHITE;
	}
	
	public boolean checkDuplicate(String targetStr, int compIndex) {
		boolean retBoolean = false;
		for (ContentTableRow tableRow : rowsList) {
			if (tableRow.getChildrenList().size() > compIndex) {
				Widget widget = tableRow.getChildrenList().get(compIndex);
				if (widget != null && widget instanceof MaterialLabel) {
					MaterialLabel tgrLabel = (MaterialLabel)widget;
					retBoolean = tgrLabel.getText().equals(targetStr);
					if(retBoolean) break;
				}
			}
		}
		return retBoolean;
	}
	

	public MaterialPanel getContainer() {
		return rowsContainer;
	}

	public void setQueryParamter(JSONObject parameterJSON) {
		this.parameterJSON = parameterJSON;
		this.parameterJSON.put("offset", new JSONNumber(this.dataOffset));
	}

	public void setCallbackFunction(Func3<Object, String, Object> func3) {
		if (this.func3 == null) this.func3 = func3;
	}

	public void getData() {
		VisitKoreaBusiness.post("call", parameterJSON.toString(), this.func3);
	}
	
	public void getNextData() {
		this.loading(true);
		this.dataOffset = this.dataOffset + this.dataRowCount;
		this.parameterJSON.put("offset", new JSONNumber(this.dataOffset));
		getData();
	}
	
	@Override
	public void loading(boolean loadFlag) {
		if (loadFlag) {
			rowsContainerCover.setVisible(loadFlag);
			loader.show();
		}else {
			Timer timer = new Timer() {
				@Override
				public void run() {
					loader.hide();
					rowsContainerCover.setVisible(loadFlag);
				}
			};
			timer.schedule(1000);
		}
	}

	public List<ContentTableRow> getSelectedRows() {
		return selectedRows;
	}

	public JSONObject getQueryParamter() {
		return this.parameterJSON;
	}

	public MaterialPanel getRowContainer() {
		return rowsContainer;
	}

	public void setSelectedIndex(int nowIndex) {
		
		ContentTableRow tgrRowPanel = null;
		
		if (rowsContainer.getChildren().size() <= nowIndex) {
			tgrRowPanel = (ContentTableRow) rowsContainer.getChildren().get(nowIndex-1);
		}else if (rowsContainer.getChildren().size() > nowIndex && nowIndex > -1) {
			tgrRowPanel = (ContentTableRow) rowsContainer.getChildren().get(nowIndex);
		}
		
		tgrRowPanel.setBackgroundColor(Color.RED_LIGHTEN_4);
		tgrRowPanel.setTextColor(Color.BLACK);
		
		if (this.selectedRows.isEmpty()) {
			this.selectedRows.add(tgrRowPanel);
		}else {
			if (this.selectedRows.contains(tgrRowPanel)) {
				
			}else {
				if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_SINGLE)) {
					this.selectedRows.remove(0);
					this.selectedRows.add(tgrRowPanel);
					List<Widget> children = rowsContainer.getChildrenList();
					for(Widget singleMember : children) {
						if (singleMember instanceof ContentTableRow && !singleMember.equals(tgrRowPanel)) {
							((ContentTableRow)singleMember).restoreBackgroundColor();
						}
					}
				}else if (this.selectType.equals(TABLE_SELECT_TYPE.SELECT_MULTIPLE)) {
					this.selectedRows.add(tgrRowPanel);
				}
			}
		}
	}

	public void setSelectedMoveLink(boolean b) {
		
		MaterialIcon visiblePanelButton = new MaterialIcon(IconType.MOVIE);
		visiblePanelButton.setTextAlign(TextAlign.CENTER);
		visiblePanelButton.addClickHandler(event->{
			if (newPosition == 40) {
				
				for (ContentTableRow ctr : selectedRows) {
					selectedPanelSelectedRows.add(ctr);
					selectedRows.remove(ctr);
				}
				
			}else {
				
				for (ContentTableRow ctr : selectedPanelSelectedRows) {
					selectedRows.add(ctr);
					selectedPanelSelectedRows.remove(ctr);
					
				}
			}

			redraw();
			
		});
		
		this.getButtomMenu().addIcon(visiblePanelButton, "선택된  레코드 옮기기", com.google.gwt.dom.client.Style.Float.RIGHT);
		
	}
	
	public List<ContentTableRow> getRowsList() {
		return rowsList;
	}

	private void redraw() {
		
	}

	public MaterialPanel getHeader() {
		return header;
	}
	
	public MaterialPanel getTag() {
		return tag;
	}
	
	
	public void setTableBorder(String value) {
		header.setBorderTop(value);
		header.setBorderLeft(value);
		header.setBorderRight(value);
		rowsContainer.setBorderLeft(value);
		rowsContainer.setBorderRight(value);
		tag.setBorderLeft(value);
		tag.setBorderRight(value);
		pager.setBorder(value);
	}
	
}
