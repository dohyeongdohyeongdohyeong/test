package kr.or.visitkorea.admin.client.manager.fcm.dialog;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.fcm.FcmApplication;
import kr.or.visitkorea.admin.client.manager.fcm.msg.FcmMessage;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SendMessageDialog extends DialogContent {
    private OnCompletedListener listener;
    private MaterialTextBox inputTitle;
    private MaterialTextBox inputUrl;
    private MaterialListValueBox<String> dropDown;
    private MaterialTextArea inputMessage;
    private MaterialLabel dialogTitle;
    private MODE mode;

    public static enum MODE {
    	SEND,
    	VIEW
    }
    
    @Override
	protected void onLoad() {
		super.onLoad();
		this.mode = (MODE) this.getParameters().get("mode");
		boolean isSend = this.mode == MODE.SEND;
		this.dialogTitle.setValue(isSend ? "푸시알림 보내기" : "푸시내역 상세");
		this.inputTitle.setValue(isSend ? "" : this.getParameters().get("title").toString());
		this.inputUrl.setValue(isSend ? "" : this.getParameters().get("url").toString());
		this.inputMessage.setValue(isSend ? "" : this.getParameters().get("message").toString());
		this.dropDown.setSelectedValue(isSend ? "" : this.getParameters().get("target").toString());
		this.inputTitle.setEnabled(isSend);
		this.inputUrl.setEnabled(isSend);
		this.dropDown.setEnabled(isSend);
		this.inputMessage.setEnabled(isSend);
	}

	public interface OnCompletedListener {
        void onCompleted(FcmMessage data);
    }

    public SendMessageDialog(MaterialExtentsWindow window, OnCompletedListener listener) {
        super(window);
        this.listener = listener;
    }

    public void setOnCompletedListener(OnCompletedListener listener) {
        this.listener = listener;
    }

    @Override
    public void init() {
    	this.setPadding(10);
    	
        dialogTitle = new MaterialLabel("");
        dialogTitle.setFontSize("1.4em");
        dialogTitle.setFontWeight(Style.FontWeight.BOLD);
        dialogTitle.setTextColor(Color.BLUE);
        dialogTitle.setPadding(10);
        dialogTitle.setText("푸시알림 보내기");
        add(dialogTitle);

        MaterialRow row = null;

        dropDown = new MaterialListValueBox<>();
        dropDown.setPlaceholder("OS유형");
        dropDown.addItem("PROD_IOS", "운영계 iOS");
        dropDown.addItem("PROD_ANDROID", "운영계 Android");
        dropDown.addItem("DEV_IOS", "테스트 iOS");
        dropDown.addItem("DEV_ANDROID", "테스트 Android");
        dropDown.addItem("PROD_ALL", "운영계 All");
        dropDown.addItem("DEV_ALL", "테스트 All");
        row = new MaterialRow();
        row.add(wrapWithColumn(dropDown, "s4"));
        add(row);
        
        inputTitle = new MaterialTextBox();
        inputTitle.setLabel("제목");
        row = new MaterialRow();
        row.add(wrapWithColumn(inputTitle, "s12"));
        add(row);
        
        inputUrl = new MaterialTextBox();
        inputUrl.setLabel("링크 URL");
        row = new MaterialRow();
        row.add(wrapWithColumn(inputUrl, "s12"));
        add(row);

        inputMessage = new MaterialTextArea();
        inputMessage.setLabel("내용");
        inputMessage.getWidget(0).getElement().getStyle().setProperty("maxHeight", "100px");
        row = new MaterialRow();
        row.add(wrapWithColumn(inputMessage, "s12"));
        add(row);

        addDefaultButtons();

        MaterialButton button = new MaterialButton("전송");
        button.setBackgroundColor(Color.RED_LIGHTEN_2);
        button.addClickHandler(event -> {
            if (listener != null) {
                String target = dropDown.getSelectedValue();
                String title = inputTitle.getText();
                String message = inputMessage.getText();
                String url = inputUrl.getText();

                if (!isValid(title)) {
                    MaterialToast.fireToast("제목을 입력하세요.", 3000);
                    return;
                }

                if (!isValid(message)) {
                    MaterialToast.fireToast("내용을 입력하세요.", 3000);
                    return;
                }
                
                if (!isValid(url)) {
                    MaterialToast.fireToast("URL을 입력하세요.", 3000);
                    return;
                }

                FcmMessage msgObject = new FcmMessage(-1, target, title, message, url);
                FcmApplication.sendFcmMessage(msgObject, result -> {
                    listener.onCompleted(msgObject);
                });
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
        return 600;
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
}
