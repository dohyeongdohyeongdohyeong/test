package kr.or.visitkorea.admin.client.manager.upload.excel.dialog;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.dialogs.DialogsBundle;
import kr.or.visitkorea.admin.client.manager.upload.excel.ExcelImageUploadApplication;
import kr.or.visitkorea.admin.client.manager.upload.excel.composite.ExcelImageUploadMain;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class UploadDialog extends DialogContent {

    static {
        MaterialDesignBase.injectCss(DialogsBundle.INSTANCE.contentCss());
    }

    private FileUpload upload;
    
    public UploadDialog(MaterialExtentsWindow window) {
        super(window);
    }

    @Override
    public void init() {
        
        // dialog title define
        MaterialLabel dialogTitle = new MaterialLabel("액셀파일 업로드");
        dialogTitle.setFontSize("1.4em");
        dialogTitle.setFontWeight(Style.FontWeight.BOLD);
        dialogTitle.setTextColor(Color.BLUE);
        dialogTitle.setPaddingTop(10);
        dialogTitle.setPaddingLeft(30);
        this.add(dialogTitle);

        // Create a FormPanel and point it at a service.
        final FormPanel form = new FormPanel();
        form.setAction("./call?cmd=FILE_UPLOAD_XLS");
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        VerticalPanel panel = new VerticalPanel();
        panel.setHeight("200px");
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        form.setWidget(panel);

        // Create a FileUpload widget.
        upload = new FileUpload();
        upload.setName("file");
        upload.setWidth("240");
        upload.getElement().getStyle().setProperty("fontSize", "1.4em");
        upload.getElement().getStyle().setMarginLeft(40, Style.Unit.PX);
        
        panel.add(upload);

        this.add(form);

        addDefaultButtons();

        MaterialButton button = new MaterialButton("실행");
        button.setBackgroundColor(Color.RED_LIGHTEN_2);
        button.addClickHandler(event -> {
            
            Console.log(upload.getFilename());
            
            if (upload.getFilename().length() == 0) {
                showAlert("파일을 선택하세요.");
                return;
            }

            if (!upload.getFilename().endsWith("xls")) {
                showAlert("액셀파일이 아닙니다.");
                return;
            }
            
            form.submit();
            
            MaterialLoader.loading(true);
            
            form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
                public void onSubmitComplete(SubmitCompleteEvent event) {
                    
                    getMaterialExtentsWindow().closeDialog();
                    
                    JSONObject resultXmlInfo = new JSONObject(JsonUtils.safeEval(removeTag(event.getResults())));
                    JSONObject xmlFileInfo = resultXmlInfo.get("body").isObject();
                    String eihId = xmlFileInfo.get("xmlFileInfo").isObject().get("eihId").isString().stringValue();
                    String xmlPath = xmlFileInfo.get("xmlFileInfo").isObject().get("xmlPath").isString().stringValue().replace("master", "check"); 
                    
                    Console.log(xmlFileInfo.get("xmlFileInfo").toString());
                    
                    ExcelImageUploadMain excelImageUploadMain = (ExcelImageUploadMain) Registry.get("ExcelImageUploadMain");
                    excelImageUploadMain.fetchHistoryList(0);
                    excelImageUploadMain.excelUploadHistoryDetail(eihId, xmlPath);
                    
                    MaterialLoader.loading(false);
                }
            });
        });
        
        this.addButton(button);
    }

    private void showAlert(String message) {
        getMaterialExtentsWindow().alert(message, 500, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getMaterialExtentsWindow().openDialog(ExcelImageUploadApplication.UPLOAD_EXCEL_DIALOG, 720);
            }
        });
    }

    @Override
    public int getHeight() {
        return 300;
    }

    protected void onLoad() {
        super.onLoad();
        upload.getElement().setPropertyString("value", "");
    }
    
    public String removeTag(String html) {
    	try {
    		return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "";
    }
}
