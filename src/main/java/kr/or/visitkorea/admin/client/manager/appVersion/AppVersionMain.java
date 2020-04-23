package kr.or.visitkorea.admin.client.manager.appVersion;

import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.appVersion.dialog.InputDialog;
import kr.or.visitkorea.admin.client.manager.appVersion.table.Version;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AppVersionMain extends AbstractContentPanel implements InputDialog.OnCompletedListener {

    private final AppVersionApplication application;
    private MaterialDataTable<Version> dataTable;

    private List<Version> versionList;

    private MaterialButton addButton;

    public AppVersionMain(MaterialExtentsWindow window, AppVersionApplication application) {
        super(window);
        this.application = application;
    }

    @Override
    public void init() {

        VerticalPanel panel = new VerticalPanel();
        panel.add(createHeaderRow());

        dataTable = new MaterialDataTable<>();
        dataTable.getScaffolding().getTopPanel().removeFromParent();
        dataTable.getScaffolding().getTable().getHead().setFontSize("13px");
        dataTable.getScaffolding().getTable().getBody().setFontSize("16px");

        // dataTable.setRowFactory(new VersionRowFactory());
        // dataTable.setRenderer(new VersionRenderer<>());

        TextColumn<Version> marketCol = new TextColumn<Version>() {
            @Override
            public String getValue(Version object) {
                return object.getApId();
            }
        };
        marketCol.setWidth("280px");
        dataTable.addColumn(marketCol, "market id".toUpperCase());

        WidgetColumn<Version, MaterialLink> locationCol = new WidgetColumn<Version, MaterialLink>() {
            @Override
            public MaterialLink getValue(Version object) {
                MaterialLink link = new MaterialLink(object.getUrl());
                link.addClickHandler(event -> {
                    event.getNativeEvent().preventDefault();
                    Window.open(object.getUrl(), object.getUrl(), "");
                });
                return link;
            }
        };
        locationCol.setWidth("480px");
        dataTable.addColumn(locationCol, "location".toUpperCase());

        dataTable.addColumn(new TextColumn<Version>() {
            @Override
            public String getValue(Version object) {
                return object.getVersion();
            }
        }, "cur. version".toUpperCase());

        dataTable.addColumn(new TextColumn<Version>() {
            @Override
            public String getValue(Version object) {
                return object.getMinVersion();
            }
        }, "min. version".toUpperCase());

        WidgetColumn<Version, MaterialRow> buttonCol = new WidgetColumn<Version, MaterialRow>() {
            @Override
            public MaterialRow getValue(Version object) {
                MaterialRow row = new MaterialRow();
                row.setMarginBottom(0);

                MaterialColumn col = new MaterialColumn();
                col.setGrid("s4");
                col.setTextAlign(TextAlign.RIGHT);
                row.add(col);

                MaterialButton button = new MaterialButton("수정");
                button.addClickHandler(event -> {
                    application.setVersionData(object);
                    showEditDialog();
                });
                col.add(button);

                col = new MaterialColumn();
                col.setOffset("s1");
                col.setGrid("s7");
                row.add(col);

                button = new MaterialButton("삭제");
                button.setBackgroundColor(Color.RED);
                button.addClickHandler(event -> {
                    getMaterialExtentsWindow().confirm("정말 삭제하시겠습니까?", e -> {
                        MaterialButton source = (MaterialButton) e.getSource();
                        if (source.getText().equals("예")) {
                        	
                        	//application.getWindow().getBusiness("deleteAppVersion").invoke();
                        	
                            AppVersionApplication.deleteAppVersion(object.getApId(), (success) -> {
                                if (success) {
                                    MaterialToast.fireToast("삭제되었습니다.", 3000);
                                    fetchAppVersionList();
                                }
                            });
                        }
                    });
                });
                col.add(button);

                return row;
            }
        };
        dataTable.addColumn(buttonCol);

        dataTable.addRowDoubleClickHandler(event -> {
            // application.setVersionData(event.getModel());
            // showEditDialog();
            Version data = event.getModel();
            getMaterialExtentsWindow().alert(makeApiEndPoint(data));
        });

        MaterialRow row = new MaterialRow();
        row.setPadding(0);
        row.add(wrapWithColumn(dataTable, "s12"));
        panel.add(row);

        add(panel);

        fetchAppVersionList();
    }

    private String makeApiEndPoint(Version data) {
        return Registry.get("image.server").toString()+"/visitKoreaAdmin/call?cmd=SELECT_APP_VERSION&apId=" + data.getApId();
    }

    private Widget createHeaderRow() {
        MaterialLabel title = new MaterialLabel("모바입 앱 버전관리");
        title.setFontSize("1.6em");
        title.setFontWeight(Style.FontWeight.BOLD);
        title.setPaddingLeft(20);
        title.setPaddingTop(10);

        MaterialRow row = new MaterialRow();
        MaterialColumn col = new MaterialColumn();
        col.setMarginTop(10);
        col.setGrid("s4");
        col.add(title);
        row.add(col);

        addButton = new MaterialButton();
        addButton.setEnabled(true);
        addButton.setText("신규등록");
        addButton.setFontSize("1.2em");
        addButton.setMarginTop(10);
        addButton.setIconType(IconType.ADD);
        addButton.addClickHandler(event -> {
            showNewDialog();
        });

        col = new MaterialColumn();
        col.setMarginTop(10);
        col.setOffset("s6");
        col.setGrid("s2");
        col.setPaddingRight(40);
        col.setTextAlign(TextAlign.RIGHT);
        col.add(addButton);
        row.add(col);

        return row;
    }

    private void showNewDialog() {
        application.clearNewDialog();
        getMaterialExtentsWindow().openDialog(AppVersionApplication.APP_VERSION_NEW_DIALOG, 700);
    }

    private void showEditDialog() {
        getMaterialExtentsWindow().openDialog(AppVersionApplication.APP_VERSION_EDIT_DIALOG, 700);
    }

    private void fetchAppVersionList() {
        dataTable.clearRows(true);
        AppVersionApplication.fetchAllAppVersion((List<Version> versionList) -> {
            if (versionList != null) {
                dataTable.setRowData(0, versionList);
            }
        });
    }

    private static MaterialColumn wrapWithColumn(Widget widget, String grid) {
        return wrapWithColumn(widget, grid, "");
    }

    private static MaterialColumn wrapWithColumn(Widget widget, String grid, String offset) {
        MaterialColumn col = new MaterialColumn();
        col.setPadding(0);
        col.add(widget);
        col.setGrid(grid);
        col.setOffset(offset);
        return col;
    }

    @Override
    public void onCompleted(String id, Version data) {
        if (id.equals("new")) {
            AppVersionApplication.insertAppVersion(data, result -> {
                if (result) {
                    MaterialToast.fireToast("저장되었습니다.", 3000);
                    fetchAppVersionList();
                }
            });
        } else {
            AppVersionApplication.updateAppVersion(data, result -> {
                if (result) {
                    MaterialToast.fireToast("저장되었습니다.", 3000);
                    fetchAppVersionList();
                }
            });
        }
    }
}
