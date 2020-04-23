package kr.or.visitkorea.admin.client.manager.appVersion.dialog;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.*;
import kr.or.visitkorea.admin.client.manager.appVersion.table.Version;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class InputDialog extends DialogContent {

    private final String id;
    private OnCompletedListener listener;

    private MaterialTextBox inputMarket;
    private MaterialTextBox inputLocation;
    private MaterialTextBox inputVersion;
    private MaterialTextBox inputMinimum;

    private Version data;
    private MaterialLabel dialogTitle;

    public interface OnCompletedListener {
        void onCompleted(String id, Version data);
    }

    public InputDialog(String id, MaterialExtentsWindow window, OnCompletedListener listener) {
        super(window);
        this.id = id;
        this.listener = listener;
    }

    public void setData(Version data) {
        this.data = data;
        if (data != null) {
            inputMarket.setText(data.getApId());
            inputMarket.setEnabled(false);
            inputLocation.setText(data.getUrl());
            inputVersion.setText(data.getVersion());
            inputMinimum.setText(data.getMinVersion());
            dialogTitle.setText("수정");
        } else {
            inputMarket.setText("");
            inputMarket.setEnabled(true);
            inputLocation.setText("");
            inputVersion.setText("");
            inputMinimum.setText("");
            dialogTitle.setText("등록");
        }
    }

    public void setOnCompletedListener(OnCompletedListener listener) {
        this.listener = listener;
    }

    static MaterialColumn wrapWithColumn(Widget widget, String grid) {
        return wrapWithColumn(widget, grid, "");
    }

    private static MaterialColumn wrapWithColumn(Widget widget, String grid, String offset) {
        MaterialColumn col = new MaterialColumn();
        col.add(widget);
        col.setGrid(grid);
        col.setOffset(offset);
        return col;
    }

    @Override
    public void init() {
        dialogTitle = new MaterialLabel("");
        dialogTitle.setFontSize("1.4em");
        dialogTitle.setFontWeight(Style.FontWeight.BOLD);
        dialogTitle.setTextColor(Color.BLUE);
        dialogTitle.setPaddingTop(20);
        dialogTitle.setPaddingLeft(30);
        dialogTitle.setText("등록");
        add(dialogTitle);

        inputMarket = new MaterialTextBox();
        inputMarket.setLabel("MARKET ID");
        MaterialRow row = new MaterialRow();
        row.setMarginTop(10);
        row.add(wrapWithColumn(inputMarket, "s10", "s1"));
        add(row);

        inputLocation = new MaterialTextBox();
        inputLocation.setMarginTop(0);
        inputLocation.setLabel("LOCATION");
        row = new MaterialRow();
        row.add(wrapWithColumn(inputLocation, "s10", "s1"));
        add(row);

        inputVersion = new MaterialTextBox();
        inputVersion.setMarginTop(0);
        inputVersion.setLabel("CUR. VERSION");
        row = new MaterialRow();
        row.add(wrapWithColumn(inputVersion, "s10", "s1"));
        add(row);

        inputMinimum = new MaterialTextBox();
        inputMinimum.setMarginTop(0);
        inputMinimum.setLabel("MIN. VERSION");
        row = new MaterialRow();
        row.add(wrapWithColumn(inputMinimum, "s10", "s1"));
        add(row);

        addDefaultButtons();

        MaterialButton button = new MaterialButton("저장");
        button.setBackgroundColor(Color.RED_LIGHTEN_2);
        button.addClickHandler(event -> {
            if (listener != null) {
                String apId = inputMarket.getText();
                String url = inputLocation.getText();
                String ver = inputVersion.getText();
                String min = inputMinimum.getText();

                if (!isValid(apId)) {
                    MaterialToast.fireToast("MARKET ID를 입력하세요.", 3000);
                    return;
                }
                if (!isValid(url)) {
                    MaterialToast.fireToast("LOCATION을 입력하세요.", 3000);
                    return;
                }
                if (!isValid(ver)) {
                    MaterialToast.fireToast("현재 버전을 입력하세요.", 3000);
                    return;
                }
                if (!isValid(min)) {
                    MaterialToast.fireToast("최소 버전을 입력하세요.", 3000);
                    return;
                }

                Version version = Version.valueOf(apId, ver, min, url);
                listener.onCompleted(id, version);
            }
            getMaterialExtentsWindow().closeDialog();
        });
        addButton(button);
    }

    private static boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public int getHeight() {
        return 500;
    }

	public InputDialog getDialog(String string) {
		return null;
	}
}
