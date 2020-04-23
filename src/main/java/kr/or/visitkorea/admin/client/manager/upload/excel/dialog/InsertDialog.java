package kr.or.visitkorea.admin.client.manager.upload.excel.dialog;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.dialogs.DialogsBundle;
import kr.or.visitkorea.admin.client.manager.upload.excel.ExcelImageUploadApplication;
import kr.or.visitkorea.admin.client.manager.upload.excel.composite.ExcelImageUploadMain;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

/**
 * @author Admin
 * 이미지 업로드 InsertDialog
 */
public class InsertDialog extends DialogContent {
	
	private String eihId;
	private String xmlFilePath;
	private MaterialTextArea dialogTextInput;

    static {
        MaterialDesignBase.injectCss(DialogsBundle.INSTANCE.contentCss());
    }

    public InsertDialog(MaterialExtentsWindow window) {
        super(window);
    }

    @Override
    public void init() {
    	initLayout();
    }

	private void initLayout() {
    	MaterialLabel dialogTitle = new MaterialLabel("이미지 파일 데이터베이스 등록");
        dialogTitle.setFontSize("1.4em");
        dialogTitle.setFontWeight(Style.FontWeight.BOLD);
        dialogTitle.setTextColor(Color.BLUE);
        dialogTitle.setPaddingTop(10);
        dialogTitle.setPaddingLeft(30);
        this.add(dialogTitle);
        
        String warningText = "선택된 DATA를 기준으로 이미지 정보가 갱신됩니다. \r\n갱신 후에는 재설정이 불가능하므로 신중하게 선택하시기 바랍니다.";
        
        MaterialTextArea dialogText = new MaterialTextArea();
        dialogText.setFontSize("1.4em");
        dialogText.setFontWeight(Style.FontWeight.BOLD);
        dialogText.setTextColor(Color.RED_LIGHTEN_2);
        dialogText.setPaddingTop(15);
        dialogText.setPaddingLeft(30);
        dialogText.setPaddingRight(30);
        dialogText.setReadOnly(true);
        dialogText.setText(warningText);
        
        this.add(dialogText);
        
        MaterialLabel dialogLabel = new MaterialLabel();
        dialogLabel.setText("선택된 XML 파일명 :: ");
        dialogLabel.setFontSize("1.1em");
        dialogLabel.setLayoutPosition(Position.ABSOLUTE);
        dialogLabel.setTextColor(Color.RED_LIGHTEN_1);
        dialogLabel.setPaddingTop(1);
        dialogLabel.setLeft(30);
        
        this.add(dialogLabel);
        
        dialogTextInput = new MaterialTextArea();
        dialogTextInput.setLayoutPosition(Position.ABSOLUTE);
        dialogTextInput.setPaddingTop(11);
        dialogTextInput.setWidth("500px");
        dialogTextInput.setLeft(30);
        dialogTextInput.setReadOnly(true);
        
        this.add(dialogTextInput);
        
        addDefaultButtons();
        
        MaterialButton executeButton = new MaterialButton("DB 처리");
        executeButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		executeButton.setLayoutPosition(Position.RELATIVE);
		executeButton.setFloat(Style.Float.RIGHT);
        this.addButton(executeButton);
        
        executeButton.addClickHandler(event -> {
        	
        	if (eihId == null || eihId.length() == 0) {
        		showAlert("파일을 선택하세요.");
        		return;
        	}
        	
        	MaterialLoader.loading(true, getDialog());
        	
        	JSONObject params = new JSONObject();
        	params.put("cmd", new JSONString("IMAGE_UPDATE_PROCESS"));
        	params.put("eihId", new JSONString(eihId));
        	params.put("xmlFilePath", new JSONString(xmlFilePath));
        	
        	VisitKoreaBusiness.post("call", params.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
					
					if (((JSONObject) result.get("header")).get("process").isString().stringValue().equals("success")) {
						Console.log("DB 처리 정상 종료 :: " + result);
					}
					
					getMaterialExtentsWindow().closeDialog();
					
                    ExcelImageUploadMain excelImageUploadMain = (ExcelImageUploadMain) Registry.get("ExcelImageUploadMain");
                    excelImageUploadMain.fetchHistoryList(0);
                    excelImageUploadMain.excelUploadHistoryDetail(eihId, xmlFilePath.replace("master", "check"));
					
					MaterialLoader.loading(false, getDialog());
				}
			}); 
        });
    }
	
	private InsertDialog getDialog() {
		return this;
	}
	
    private void showAlert(String message) {
        getMaterialExtentsWindow().alert(message, 500, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getMaterialExtentsWindow().openDialog(ExcelImageUploadApplication.INSERT_DB_DIALOG, 720);
            }
        });
    }

    @Override
    public int getHeight() {
        return 300;
    }

    protected void onLoad() {
        super.onLoad();
        initParams();
    }
    
    public void initParams() {
    	if (this.getParameters().get("eihId") != null)
    		eihId = (String) this.getParameters().get("eihId");
    	if (this.getParameters().get("xmlFilePath") != null)
    		xmlFilePath = (String) this.getParameters().get("xmlFilePath");
    	
    	dialogTextInput.setText(xmlFilePath);
	}
}
