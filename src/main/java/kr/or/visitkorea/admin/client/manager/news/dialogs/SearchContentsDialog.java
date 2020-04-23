
package kr.or.visitkorea.admin.client.manager.news.dialogs;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchContentsDialog extends DialogContent {


//	private MaterialLink selectedLink;
	private MaterialTextBox searchBox;
//	private ContentTable table;
//	private MaterialLabel countLabel;
//	private List<String> autoTypeTextList;
	private MaterialInput startInput;
	private MaterialInput endInput;
	private SelectionPanel box;
	public SearchContentsDialog(MaterialExtentsWindow window) {
		super(window); 
	}

	@Override
	public void init() {

		addDefaultButtons();

		HashMap<String, Object> valueMap = new HashMap<String,Object>();
		valueMap.put("제목", 0);
		valueMap.put("내용", 1);
		valueMap.put("제목 + 내용", 2);
		
		box = new SelectionPanel();
		box.setLayoutPosition(Position.ABSOLUTE);
		box.setTop(20);
		box.setElementMargin(5);
		box.setElementPadding(5);
		box.setElementRadius(6);
		box.setTextAlign(TextAlign.CENTER);
		box.setSingleSelection(true);
		box.setValues(valueMap);
		box.setFontSize("1.0em");
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		this.add(box);
		
		MaterialPanel durationPanel = new MaterialPanel();
		durationPanel.setLayoutPosition(Position.ABSOLUTE);
		durationPanel.setLeft(50);
		durationPanel.setTop(60);
		durationPanel.setRight(50);
		durationPanel.setTextAlign(TextAlign.CENTER);
		
		startInput = new MaterialInput(InputType.DATE);
		startInput.setMargin(5);
		startInput.setWidth("200px");
		startInput.setPadding(5);
		startInput.setDisplay(Display.INLINE_BLOCK);
		startInput.addValueChangeHandler(event->{});
		durationPanel.add(startInput);
		
		MaterialLabel midTxt = new MaterialLabel("~");
		midTxt.setMargin(10);
		midTxt.setDisplay(Display.INLINE_BLOCK);
		durationPanel.add(midTxt);
		
		endInput = new MaterialInput(InputType.DATE);
		endInput.setWidth("200px");
		endInput.setMargin(5);
		endInput.setPadding(5);
		endInput.setDisplay(Display.INLINE_BLOCK);
		endInput.addValueChangeHandler(event->{});
		durationPanel.add(endInput);
		
		this.add(durationPanel);
		
		searchBox = new MaterialTextBox();
		searchBox.setPlaceholder("검색할 단어를 입력해 주세요.");
		searchBox.setLabel("검색어");
		searchBox.setLayoutPosition(Position.ABSOLUTE);
		searchBox.setLeft(50);
		searchBox.setTop(140);
		searchBox.setRight(50);
		searchBox.setTextAlign(TextAlign.CENTER);
		searchBox.addKeyUpHandler(event->{
//			if (event.getNativeKeyCode() == 13) {
//				appendRecord(event);	
//			}
		});
		
		this.add(searchBox);

		MaterialButton selectButton = new MaterialButton("검색");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			appendRecord(event);
		});
		this.addButton(selectButton);
		
	}

	private void appendRecord(Object event) {
		this.getParameters().clear();
		String keyWord = "";
		if(searchBox.getText() != null)
			keyWord = searchBox.getText(); //.replaceAll("\\\\", "");
		if(box.getSelectedValue() != null) {
			this.getParameters().put("mode", (int)box.getSelectedValue());
			this.getParameters().put("keyword", keyWord);
		}
		String strsdate = startInput.getText().toString();
		if(!"".equals(strsdate)) {
			this.getParameters().put("sdate", strsdate);
		}
		String stredate = endInput.getText().toString();
		if(!"".equals(stredate)) {
			this.getParameters().put("edate", stredate + " 23:59:59");
		}
		if(this.handler != null)
			handler.onClick((ClickEvent)event);
		getMaterialExtentsWindow().closeDialog();
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
//		table = (ContentTable)parameters.get("TARGET_TABLE");
//		countLabel = (MaterialLabel)parameters.get("TARGET_COUNT");
	}

	@Override
	protected void onLoad() {
       super.onLoad();
   }
	
	@Override
	public int getHeight() {
		return 350;
	}
	
}
