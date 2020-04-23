package kr.or.visitkorea.admin.client.manager.event.widgets;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialDatePicker;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialDatePicker.MaterialDatePickerType;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.event.model.EventGift;
import kr.or.visitkorea.admin.client.manager.event.model.EventProcess;

public class GiftContentPanel extends MaterialRow {
	private String gftId;
	private String evtId;
	private String subEvtId;
	private MaterialLabel indexLabel;
	private MaterialLabel KeyLabel;
	private MaterialTextBox giftTitle;
	private MaterialTextBox numbers;
	private int order;
	private EventGift gift;
	private MaterialRow row;
	private MaterialLabel KeyName;
	private MaterialPanel giftContentArea;
	private MaterialIcon removeIcon;
	private EventProcess selectedProcess;
	public GiftContentPanel(EventGift gift, EventProcess selectedProcess) {
		super();
		this.gift = gift;
		this.selectedProcess = selectedProcess;
		init();
	}
	
	public void init() {
		this.setMargin(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s2");

		indexLabel = new MaterialLabel("경품");
		indexLabel.setBackgroundColor(Color.BLUE);
		indexLabel.setLineHeight(46.5);
		indexLabel.setFontSize("20px");
		indexLabel.setTextColor(Color.WHITE);
		col1.add(indexLabel);
		this.add(col1);
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s10");
		col2.setPadding(0);
		this.add(col2);
		
		row = null;
		
		row = addRow(col2);
		giftTitle = addInputBox(row, "경품명을 입력하세요", "s3");
		giftTitle.setLabel("경품명");
		giftTitle.addValueChangeHandler(event -> {
			this.gift.setTitle(event.getValue());
		});
		numbers = addInputBox(row, "수량입력", "s1");
		numbers.setLabel("수량");
		numbers.addValueChangeHandler(event -> {	
			this.gift.setCount(Integer.parseInt(event.getValue()));
		});
		
		
		KeyName = addLabel(row, "경품키", Color.GREY_LIGHTEN_3, "s2");
		
		KeyLabel = addLabel(row, "" ,Color.WHITE,"s5");
		removeIcon = new MaterialIcon(IconType.REMOVE);
		removeIcon.setLineHeight(46.25);
		removeIcon.setFontSize("30px");
		MaterialColumn col = new MaterialColumn();
		col.setGrid("s1");
		col.add(removeIcon);
		row.add(col);
		
		removeIcon.addClickHandler(e -> {
			
			gift.setDelete(true);
			selectedProcess.getGiftDeleteList().add(gift);
			giftContentArea.remove(gift.getOrder());
			selectedProcess.getGiftList().remove(gift.getOrder());
			for (int i = 0; i < giftContentArea.getChildrenList().size(); i++) {
				GiftContentPanel gift = (GiftContentPanel)giftContentArea.getChildrenList().get(i);
				gift.setIndex(gift.getIndex()=="꽝" ? "꽝" : "경품" +(i+1));
				
			}
			
			for (int i = 0; i < selectedProcess.getGiftList().size(); i++) {
				selectedProcess.getGiftList().get(i).setOrder(i);
			}
			
		});
		
		
		this.KeyLabel.addClickHandler(event -> {
			this.copy(this.getGftId());
			MaterialToast.fireToast("경품키가 복사되었습니다.");
		});
		
	}

	public void formEnabled(boolean isEnable) {
		this.giftTitle.setEnabled(isEnable);
		this.numbers.setEnabled(isEnable);
	}
	
	public String getGftId() {
		return gftId;
	}

	public void setGftId(String gftId) {
		KeyLabel.setText(gftId);
		this.gftId = gftId;
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public String getSubEvtId() {
		return subEvtId;
	}

	public void setSubEvtId(String subEvtId) {
		this.subEvtId = subEvtId;
	}

	public String getIndex() {
		return this.indexLabel.getText();
	}

	public void setIndex(String index) {
		this.indexLabel.setText(index);
		if(index == "꽝") {
			KeyLabel.setVisible(false);
			KeyName.setVisible(false);
			removeIcon.setVisible(false);
		}
		
	}

	public String getTitle() {
		return this.giftTitle.getValue();
	}

	public void setTitle(String giftTitle) {
		this.giftTitle.setValue(giftTitle);
	}

	public String getNumbers() {
		return this.numbers.getValue();
	}

	public void setNumbers(Object numbers) {
		this.numbers.setValue(numbers + "");
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public void setParent(MaterialPanel giftContentArea) {
		this.giftContentArea = giftContentArea;
	}
	private MaterialTextBox addInputBox(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox tBox = new MaterialTextBox();
		tBox.setPlaceholder(placeholder);
		tBox.setMargin(0);
		tBox.setHeight("46.25px");
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tBox);
		row.add(col);
		return tBox;
	}
	
	private MaterialRow addRow(MaterialWidget widget) {
		MaterialRow row = new MaterialRow();
		widget.add(row);
		return row;
	}
	
	private MaterialLabel addLabel(MaterialRow row, String text, Color color, String grid) {
		MaterialLabel label = new MaterialLabel(text);
		label.setBackgroundColor(color);
		label.setLineHeight(46.25);
		label.setFontWeight(FontWeight.BOLD);
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(label);
		row.add(col);
		return label;
	}

	protected MaterialDatePicker addDatePicker(MaterialRow row, String grid) {
		MaterialDatePicker picker = new MaterialDatePicker();
		picker.setSelectionType(MaterialDatePickerType.YEAR_MONTH_DAY);
		picker.setAutoClose(false);  
		picker.setPlaceholder("날짜를 선턱하세요.");
		picker.setFormat("yyyy-MM-dd");
		picker.setMargin(0);
		picker.addClickHandler(e -> {
			picker.open();
    	});
		picker.addValueChangeHandler(e -> {
			picker.close();
    	});
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(picker);
		row.add(col);
		return picker;
	}
	
	public native void copy(String cotId) /*-{
	var dummy = document.createElement("textarea");
	document.body.appendChild(dummy);
	dummy.value = cotId;
	dummy.select();
	document.execCommand("copy");
	document.body.removeChild(dummy);
	}-*/;
	
}
