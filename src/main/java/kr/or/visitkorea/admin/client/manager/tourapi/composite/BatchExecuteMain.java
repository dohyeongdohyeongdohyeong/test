package kr.or.visitkorea.admin.client.manager.tourapi.composite;

import com.google.gwt.dom.client.Style;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.tourapi.BatchExecuteApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMultilineText;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class BatchExecuteMain extends AbstractContentPanel {

    public BatchExecuteMain(MaterialExtentsWindow materialExtentsWindow) {
        super(materialExtentsWindow);
    }

    @Override
    public void init() {
        this.setLayoutPosition(Style.Position.RELATIVE);

        Console.log("BatchExecuteMain init()");

        buildBatchHistoryList();
        buildBatchHistoryDetail();
    }

    private void buildBatchHistoryList() {
        ContentTable table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
        table.setLayoutPosition(Style.Position.ABSOLUTE);
        table.setHeight(575);
        table.setWidth("600px");
        table.setLeft(30);
        table.setTop(55);
        table.appendTitle("CID", 125, TextAlign.CENTER);
        table.appendTitle("카테고리", 125, TextAlign.CENTER);
        table.appendTitle("콘텐츠명", 200, TextAlign.CENTER);
        table.appendTitle("결과", 125, TextAlign.CENTER);

        MaterialIcon executeIcon = new MaterialIcon(IconType.FORWARD);
        executeIcon.setTextAlign(TextAlign.CENTER);
        executeIcon.addClickHandler(event-> {
            getMaterialExtentsWindow().openDialog(BatchExecuteApplication.CONFIRM_EXECUTE_BATCH_DIALOG, 720);
        });

        MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
        searchIcon.setTextAlign(TextAlign.CENTER);
        searchIcon.addClickHandler(event-> {
            getMaterialExtentsWindow().openDialog(BatchExecuteApplication.SEARCH_WITH_CALENDAR_DIALOG, 720);
        });
        table.getTopMenu().addIcon(executeIcon, "실행", com.google.gwt.dom.client.Style.Float.LEFT, "1.8em", "26px", 24, false);
        table.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);

        MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
        nextRowsIcon.setTextAlign(TextAlign.CENTER);
        nextRowsIcon.addClickHandler(event-> {
        });
        table.getButtomMenu()
                .addIcon(nextRowsIcon, "다음 20개", com.google.gwt.dom.client.Style.Float.RIGHT);

        this.add(table);
    }

    private void buildBatchHistoryDetail() {
        MaterialLabel titleLabel = new MaterialLabel("- 로그분석");
        titleLabel.setLayoutPosition(Style.Position.ABSOLUTE);
        titleLabel.setTop(50);
        titleLabel.setTextAlign(TextAlign.LEFT);
        titleLabel.setFontWeight(Style.FontWeight.BOLD);
        titleLabel.setTextColor(Color.BLUE);
        titleLabel.setFontSize("1.2em");
        titleLabel.setWidth("800px");
        titleLabel.setRight(40);
        this.add(titleLabel);

        ContentMultilineText detailPanel = new ContentMultilineText();
        detailPanel.setTitle("로그분석");
        detailPanel.setLayoutPosition(Style.Position.ABSOLUTE);
        detailPanel.setWidth("800px");
        detailPanel.setHeight("550px");
        detailPanel.setRight(40);
        detailPanel.setTop(80);
        detailPanel.setBorder("solid 1px black");
        this.add(detailPanel);
    }
}
