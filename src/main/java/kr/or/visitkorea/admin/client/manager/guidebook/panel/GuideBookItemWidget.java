package kr.or.visitkorea.admin.client.manager.guidebook.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardAction;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.guidebook.model.GuideBook;

public class GuideBookItemWidget extends Composite implements ClickHandler {

	private ClickHandler actionEditListener;
	private ClickHandler actionDeleteListener;

	GuideBookItemWidget(GuideBook item) {
		MaterialCard card = new MaterialCard();
		card.setHoverable(true);
		card.add(createImage(item.imagePath.substring(item.imagePath.lastIndexOf("/") + 1)));
		card.add(createBottomSummary(item));
		card.add(createAction(this));

		initWidget(card);
	}

	void setActionEditListener(ClickHandler handler) {
		actionEditListener = handler;
	}

	void setActionDeleteListener(ClickHandler handler) {
		actionDeleteListener = handler;
	}

	@Override
	public void onClick(ClickEvent event) {
		MaterialLink link = (MaterialLink) event.getSource();
		if (link.getId().equals("edit")) {
			if (actionEditListener != null) {
				actionEditListener.onClick(event);
			}
		} else {
			if (actionDeleteListener != null) {
				actionDeleteListener.onClick(event);
			}
		}
	}

	private static MaterialCardAction createAction(ClickHandler handler) {
		MaterialCardAction action = new MaterialCardAction();
		MaterialLink deleteAction = actionLabel("삭제", IconType.DELETE, Color.BLACK);
		MaterialLink editAction = actionLabel("수정", IconType.EDIT, Color.BLUE);
		deleteAction.addClickHandler(handler);
		editAction.addClickHandler(handler);
		deleteAction.setId("delete");
		editAction.setId("edit");
		action.add(deleteAction);
		action.add(editAction);
		return action;
	}

	private static MaterialLink actionLabel(String text, IconType iconType, Color color) {
		MaterialLink label = new MaterialLink(text);
		label.setTextColor(color);
		label.setFontSize(1.2, Style.Unit.EM);
		label.setIconType(iconType);
		return label;
	}

	private static Widget createBottomSummary(GuideBook item) {
		List<MaterialRow> rowList = new ArrayList<>();
		MaterialRow row = null;
		
		row = new MaterialRow();
		row.setMarginBottom(10);
		row.setTextAlign(TextAlign.RIGHT);
		row.add(wrapWithColumn(label("제목"), "s4"));
		row.add(wrapWithColumn(label(item.title), ""));
		rowList.add(row);
		
		row = new MaterialRow();
		row.setMarginBottom(10);
		row.setTextAlign(TextAlign.RIGHT);
		row.add(wrapWithColumn(label("발행일"), "s4"));
		row.add(wrapWithColumn(label(item.publishDate), ""));
		rowList.add(row);

		row = new MaterialRow();
		row.setMarginBottom(10);
		row.setTextAlign(TextAlign.RIGHT);
		row.add(wrapWithColumn(label("제작처"), "s4"));
		row.add(wrapWithColumn(label(item.publisher), ""));
		rowList.add(row);
		
		row = new MaterialRow();
		row.setMarginBottom(10);
		row.setTextAlign(TextAlign.RIGHT);
		row.add(wrapWithColumn(label("작성자"), "s4"));
		row.add(wrapWithColumn(label(item.author), ""));
		rowList.add(row);

		row = new MaterialRow();
		row.setMarginBottom(10);
		row.setTextAlign(TextAlign.RIGHT);
		row.add(wrapWithColumn(label("등록일시"), "s4"));
		row.add(wrapWithColumn(label(item.createDate), ""));
		rowList.add(row);

		row = new MaterialRow();
		row.setMarginBottom(10);
		row.setTextAlign(TextAlign.RIGHT);
		row.add(wrapWithColumn(label("지역"), "s4"));
		row.add(wrapWithColumn(label(item.areaName + " " + item.sigunguName), ""));
		rowList.add(row);

		return createCardContent(rowList.toArray(new MaterialRow[] {}));
	}

	private static MaterialLabel label(String text) {
		MaterialLabel label = new MaterialLabel(text);
		label.setFontSize(1.1, Style.Unit.EM);
		return label;
	}

	private static MaterialColumn wrapWithColumn(Widget widget, String grid) {
		return SearchPanel.wrapWithColumn(widget, grid);
	}

	private static MaterialWidget createCardContent(MaterialRow... rows) {
		MaterialCardContent content = new MaterialCardContent();
		for (MaterialRow row : rows) {
			content.add(row);
		}
		return content;
	}

	private static Widget createImage(String filename) {
		MaterialImage image = new MaterialImage();
		image.setSize("340px", "220px");
		image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=TEMP_VIEW&name=" + filename);
		return image;
	}
}