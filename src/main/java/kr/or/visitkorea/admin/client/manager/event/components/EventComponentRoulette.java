package kr.or.visitkorea.admin.client.manager.event.components;

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.components.widget.RouletteGiftRow;
import kr.or.visitkorea.admin.client.manager.event.model.EventGift;
import kr.or.visitkorea.admin.client.manager.event.model.EventProcess;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;

public class EventComponentRoulette extends AbstractEventComponent {
	private EventContentsTree host;
	private MaterialColumn leftCol;
	private MaterialColumn rightCol;
	private EventProcess selectedProcess;
	private MaterialImage rouletteImage;
	private MaterialIcon sizeIcon;
	private String subEvtId;
	private int sizeType;
	
	public EventComponentRoulette(EventComponentType componentType, AbstractContentPanel host) {
		super(componentType, host);
		this.host = (EventContentsTree) host;
	}

	@Override
	protected void init() {
		this.titleLabel.setText("룰렛");
		this.sizeIcon = this.addIcon(this.titleRow, IconType.CROP_7_5, Float.RIGHT);
		this.sizeIcon.setVisible(false);
		this.sizeIcon.setTooltip("와이드 형");
		this.sizeIcon.addClickHandler(e -> {
			if (this.sizeType == 1) {
				this.sizeIcon.setTextColor(Color.BLACK);
				this.titleLabel.setText("룰렛 - " + this.selectedProcess.getTitle());
			} else {
				this.sizeIcon.setTextColor(Color.RED);
				this.titleLabel.setText("룰렛 - "+ this.selectedProcess.getTitle() +"(와이드형)");
			}
			this.sizeType = this.sizeType == 1 ? 0 : 1;
			this.getComponentObj().setSizeType(sizeType);
		});
		
		
		MaterialRow row = new MaterialRow();
		row.setMargin(0);
		row.setWidth("100%");
		row.setPadding(10);
		row.setDisplay(Display.FLEX);
		row.setFlexAlignItems(FlexAlignItems.CENTER);
		
		leftCol = addColumn(row, "s6");
		row.add(leftCol);
		
		rouletteImage = new MaterialImage(GWT.getHostPageBaseURL() + "images/roulette/2.gif");
		rouletteImage.setWidth("70%");
		rouletteImage.setHeight("70%");
		leftCol.add(rouletteImage);
		
		rightCol = addColumn(row, "s6");
		row.add(rightCol);
		
		this.add(row);
	}

	@Override
	protected void modifyClickAction() {
		this.rightCol.clear();
		this.renderRow(this.isEditMode);
		this.sizeIcon.setVisible(true);
	}

	@Override
	protected void saveClickAction() {
		this.rightCol.clear();
		this.renderRow(this.isEditMode);
		this.sizeIcon.setVisible(false);
	}

	@Override
	public void setupContents() {
		this.subEvtId = this.getComponentObj().getSubEvtId();
		this.sizeType = this.getComponentObj().getSizeType();
		this.host.getContentProcess().getProcessList().forEach(item -> {
			if (item.getSubEvtId().equals(this.subEvtId)) {
				this.selectedProcess = item;
				if (this.sizeType == 0) {
					this.sizeIcon.setTextColor(Color.BLACK);
					this.titleLabel.setText("룰렛 - " + this.selectedProcess.getTitle());
				} else {
					this.sizeIcon.setTextColor(Color.RED);
					this.titleLabel.setText("룰렛 - "+ this.selectedProcess.getTitle() +"(와이드형)");
				}
			}
		});
		
		this.rightCol.clear();
		this.renderRow(this.isEditMode);
	}
	
	private void renderRow(boolean isEnable) {
		List<EventGift> giftList = this.selectedProcess.getGiftList();
		long giftCount = giftList.stream()
						.filter(o -> !o.isDelete())
						.count();
		giftList.stream().filter(o -> !o.isDelete())
					 	 .forEach(gift -> {
			this.addGiftRow(gift, isEnable);
		});
		
		this.rouletteImage.setUrl(GWT.getHostPageBaseURL() + "images/roulette/" 
				+ (giftCount <= 2 ? 2 : (int) giftCount) + ".png");
	}
	
	private void addGiftRow(EventGift gift, boolean isEnabled) {
		List<EventGift> list = this.selectedProcess.getGiftList();
		int index = list.indexOf(gift);
		
		RouletteGiftRow giftRow = new RouletteGiftRow(index, gift);
		giftRow.setEnabled(isEnabled);
		giftRow.getTitleBox().addKeyUpHandler(e -> {
			gift.setTitle(giftRow.getTitleBox().getValue());
		});
		
		giftRow.getOrderUpIcon().setFontSize("30px");
		giftRow.getOrderUpIcon().setVisible(index != 0);
		giftRow.getOrderUpIcon().addClickHandler(e -> {
			EventGift curr = list.get(index);
			EventGift prev = list.get(index - 1);
			curr.setOrder(index - 1);
			prev.setOrder(index);
			
			Collections.swap(list, index, index - 1);
			this.refresh();
		});

		giftRow.getOrderDownIcon().setFontSize("30px");
		giftRow.getOrderDownIcon().setVisible(index != list.size() - 1);
		giftRow.getOrderDownIcon().addClickHandler(e -> {
			EventGift curr = list.get(index);
			EventGift next = list.get(index + 1);
			curr.setOrder(index + 1);
			next.setOrder(index);
			
			Collections.swap(list, index, index + 1);
			this.refresh();
		});

		this.rightCol.add(giftRow);
	}
	
	@Override
	public void refresh() {
		this.rightCol.clear();
		this.renderRow(this.isEditMode);
	}
	
	public EventProcess getSelectedProcess() {
		return selectedProcess;
	}

	public void setSelectedProcess(EventProcess selectedProcess) {
		this.selectedProcess = selectedProcess;
	}

	private MaterialRow addRow() {
		MaterialRow row = new MaterialRow();
		row.setMargin(0);
		row.setMarginBottom(5);
		
		return row;
	}
	
}