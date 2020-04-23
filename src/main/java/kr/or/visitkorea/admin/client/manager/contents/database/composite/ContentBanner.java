package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.Date;

import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.client.constants.ButtonSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRadioButton;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentBanner extends AbtractContents {

    private MaterialRadioButton useRadioYes;
    private MaterialRadioButton useRadioNo;
    private MaterialTextBox inputTitle;
    private MaterialTextBox inputLinkUrl;
    private MaterialFileUploader uploader;
    private MaterialImage image;
    private MaterialTextBox inputImageDesc;
    private MaterialButton saveButton;

    private Banner banner;
    private MaterialInput inputStartDate;
    private MaterialInput inputEndDate;

    private static class Banner {
        public String cotId;
        public String title;
        public int use;
        public String link;
        public String imagePath;
        public String imageDesc;

        public String start;
        public String end;

        public String imageId;
    }

    public ContentBanner(MaterialExtentsWindow materialExtentsWindow) {
        super(materialExtentsWindow);
    }

    @Override
    public void init() {
        super.init();

        banner = new Banner();

        setTitle("홍보 배너 관리");

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setHeight("518px");
        MaterialPanel panel = new MaterialPanel();
        panel.setPadding(30);

        MaterialRow row = new MaterialRow();

        useRadioYes = new MaterialRadioButton("use");
        useRadioYes.setText("사용");
        MaterialColumn col = new MaterialColumn();
        col.add(useRadioYes);
        col.setGrid("s4");
        row.add(col);

        useRadioNo = new MaterialRadioButton("use");
        useRadioNo.setText("미사용");
        useRadioNo.setValue(true, false);
        col = new MaterialColumn();
        col.add(useRadioNo);
        col.setGrid("s4");
        row.add(col);
        panel.add(row);

        row = new MaterialRow();
        inputStartDate =  new MaterialInput(InputType.DATE);
        col = new MaterialColumn();
        col.setGrid("s6");
        col.add(inputStartDate);
        row.add(col);
        inputEndDate =  new MaterialInput(InputType.DATE);
        col = new MaterialColumn();
        col.setGrid("s6");
        col.add(inputEndDate);
        row.add(col);
        panel.add(row);

        row = new MaterialRow();

        inputTitle = new MaterialTextBox();
        inputTitle.setLabel("제목");
        inputTitle.setMargin(0);
        col = new MaterialColumn();
        col.add(inputTitle);
        col.setGrid("s12");
        row.add(col);
        panel.add(row);

        row = new MaterialRow();

        inputLinkUrl = new MaterialTextBox();
        inputLinkUrl.setLabel("연결 URL");
        inputLinkUrl.setMargin(0);
        col = new MaterialColumn();
        col.add(inputLinkUrl);
        col.setGrid("s12");
        row.add(col);
        panel.add(row);

        row = new MaterialRow();

        uploader = new MaterialFileUploader();

        MaterialUploadLabel uploadLabel = new MaterialUploadLabel("이미지 업로드", "위 아이콘을 선택하세요.");
        uploadLabel.getIcon().addClickHandler(event -> {
            if (!image.getUrl().contains("null")) {
                event.preventDefault();
                event.stopPropagation();
                uploader.setEnabled(false);
                getWindow().confirm("이미 등록된 이미지가 있습니다. 삭제하시겠습니까?", clickEvent -> {
                    MaterialButton btn = (MaterialButton) clickEvent.getSource();
                    if (btn.getId().equals("yes")) {
                        image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=null");
                        uploader.reset();
                    }
                    uploader.setEnabled(true);
                });
            }
        });

        uploader.add(uploadLabel);
        uploader.setUrl(Registry.get("image.server") +"/img/call?cmd=FILE_UPLOAD");
        uploader.setAcceptedFiles("image/*");
        uploader.setMaxFiles(1);
        uploader.setShadow(1);
        uploader.setHeight("300px");
        uploader.setMaxFileSize(20 * 1024 * 1024);
        uploader.setPreview(false);
        uploader.addSuccessHandler(event -> {

            // NOTE. 업로드시 서버에서 적절한 파일을 생성하지 못하여(퍼미션 문제 등) 오류가 발생하더라도
            //       업로드가 정상적으로 완료되었다고 판단하는 오류가 있다.
            //       이미지 업로드시 서버 오류가 발생하여도 이에 대한 응답을 받기 어렵다.
            //		 왜냐하면, 응답 상태코드가 200으로 처리되어서 그러해 보인다.
            //  (예) java.io.FileNotFoundException: /data/images/77/cc/47/8f/1e/77f49b88-cced-47ab-8fee-1e8a59c46030.png (그런 파일이나 디렉터리가 없습니다)
            //       Server side code
            //         - @see kr.or.visitkorea.admin.server.application.Call#processUploadImage 에서 확이하기 바란다.
            //         - 발생하는 오류에 대해 콘솔 출력만 할뿐 해결을 위한 코드(오류응답 등)는 어디에도 없다. 
        	//		   - 핑게만 대지 말고. 너가 손을 쓰고 머리를 써서 개선안을 협의해야지

            String body = event.getResponse().getBody();
            JSONObject object = JSONParser.parseStrict(body).isObject()
                    .get("body").isObject()
                    .get("result").isArray().get(0).isObject();

            String path = object.get("fullPath").isString().stringValue();
            Console.log("Upload success " + path);

            if (path.contains("/images")) {
                banner.imagePath = path.substring(path.indexOf("/images") + "/images".length());
            } else {
                banner.imagePath = path;
            }

            String name = object.get("saveName").isString().stringValue();
            String id = name.substring(0, name.lastIndexOf("."));
            image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=TEMP_VIEW&name=" + name);
        });

        col = new MaterialColumn();
        col.add(uploader);
        col.setGrid("s4");
        row.add(col);

        image = new MaterialImage();

        col = new MaterialColumn();
        col.add(image);
        col.setGrid("s8");
        row.add(col);
        panel.add(row);

        row = new MaterialRow();

        inputImageDesc = new MaterialTextBox();
        inputImageDesc.setLabel("이미지 설명");
        inputImageDesc.setMargin(0);
        col = new MaterialColumn();
        col.add(inputImageDesc);
        col.setGrid("s12");
        row.add(col);
        panel.add(row);

        row = new MaterialRow();

        saveButton = new MaterialButton("저장", IconType.SAVE);
        saveButton.setFontSize(1.1, Style.Unit.EM);
        saveButton.setSize(ButtonSize.LARGE);
        saveButton.setMarginRight(10);
        saveButton.addClickHandler(event -> {

            JSONObject authInfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
            String userId = authInfo.get("USR_ID").isString().stringValue();

            banner.title = inputTitle.getText();
            banner.use = useRadioYes.getValue() ? 1 : 0;
            banner.link = inputLinkUrl.getText();
            banner.imageDesc = inputImageDesc.getText();
            banner.start = inputStartDate.getText();
            banner.end = inputEndDate.getText();

            if (!isValidate()) {
                return;
            }

            final Functions.Func2<Boolean, String> callBack = (success, errorMessage) -> {
                if (success) {
                    MaterialToast.fireToast("저장하였습니다.", 3000);
                } else {
                    showAlert(errorMessage);
                }
            };

            saveBanner(callBack);
        });

        col = new MaterialColumn();
        col.add(saveButton);
        col.setTextAlign(TextAlign.RIGHT);
        col.setOffset("s8");
        col.setGrid("s4");
        row.add(col);
        panel.add(row);

        scrollPanel.add(panel);
        add(scrollPanel);

        // Empty image
        image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=null");
    }

    @Override
    public void setCotId(String cotId) {
        super.setCotId(cotId);
        clearUI();
        fetchBanner();
    }

    private void showAlert(String errorMessage) {
        getWindow().alert(errorMessage);
    }

    private boolean isValidate() {
        if (banner.title == null || banner.title.length() == 0) {
            MaterialToast.fireToast("제목은 필수 입력 항목입니다.");
            return false;
        }

        if ((banner.imagePath == null || banner.imagePath.length() == 0) &&
                (banner.imageId == null || banner.imageId.length() == 0)) {
            MaterialToast.fireToast("이미지를 먼저 업로드하세요.");
            return false;
        }

        if (banner.imageDesc == null || banner.imageDesc.length() == 0) {
            MaterialToast.fireToast("이미지 설명은 필수 입력 항목입니다.");
            return false;
        }

        if (banner.start == null || banner.start.length() == 0) {
            MaterialToast.fireToast("노출 시작일은 필수 입력 항목입니다.");
            return false;
        }

        if (banner.end == null || banner.end.length() == 0) {
            MaterialToast.fireToast("노출 종료일은 필수 입력 항목입니다.");
            return false;
        }

        if (banner.link == null || banner.link.length() == 0) {
            MaterialToast.fireToast("LINK는 필수 입력 항목입니다.");
            return false;
        }

        return true;
    }

    private void fetchBanner() {
        JSONObject parameter = new JSONObject();
        parameter.put("cotId", new JSONString(getCotId()));

        invokeQuery("SELECT_FESTIVAL_BANNER", parameter, (param1, param2, param3) -> {
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String success = header.get("process").isString().toString();
            success = success.replaceAll("\"", "");

            if (success.equals("success")) {
                JSONObject body = (JSONObject) result.get("body");
                JSONObject bodyResult = (JSONObject) body.get("result");

                if (banner == null) {
                    banner = new Banner();
                }

                if (bodyResult.containsKey("COT_ID")) {
                    banner.cotId = bodyResult.get("COT_ID").isString().stringValue();
                    banner.title = bodyResult.get("TITLE").isString().stringValue();
                    banner.link = bodyResult.get("LINK").isString().stringValue();
                    banner.imageId = bodyResult.get("IMAGE_ID").isString().stringValue();
                    banner.imageDesc = bodyResult.get("IMAGE_DESCRIPTION").isString().stringValue();
                    banner.use = bodyResult.get("USE").isBoolean().booleanValue() ? 1 : 0;
                    banner.start = bodyResult.get("START_DATE").isString().stringValue();
                    banner.end = bodyResult.get("END_DATE").isString().stringValue();
                    updateUI();
                }
            }
        });
    }

    private void clearUI() {
        banner = new Banner();

        useRadioYes.setValue(false, false);
        useRadioNo.setValue(true, false);

        inputTitle.setText("");
        inputLinkUrl.setText("");
        inputImageDesc.setText("");
        inputStartDate.setText("");
        inputEndDate.setText("");

        uploader.reset();
        image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=null");
    }

    private void updateUI() {
        if (banner.use == 1) {
            useRadioYes.setValue(true, false);
            useRadioNo.setValue(false, false);
        } else {
            useRadioYes.setValue(false, false);
            useRadioNo.setValue(true, false);
        }

        inputTitle.setText(banner.title);
        inputLinkUrl.setText(banner.link);
        inputImageDesc.setText(banner.imageDesc);
        inputStartDate.setText(banner.start);
        inputEndDate.setText(banner.end);
        image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=" + banner.imageId + "&anti-cache=" + new Date().getTime());
    }

    private void saveBanner(Functions.Func2<Boolean, String> callback) {
        JSONObject parameter = new JSONObject();
        parameter.put("cotId", new JSONString(getCotId()));
        parameter.put("title", new JSONString(banner.title));
        parameter.put("link", new JSONString(banner.link));
        parameter.put("use", new JSONNumber(banner.use));
        parameter.put("startDate", new JSONString(banner.start));
        parameter.put("endDate", new JSONString(banner.end));
        if (banner.imageId != null) {
            parameter.put("imageId", new JSONString(banner.imageId));
        }
        if (banner.imagePath != null) {
            parameter.put("imagePath", new JSONString(banner.imagePath));
        }
        parameter.put("imageDesc", new JSONString(banner.imageDesc));

        final String cmd = "UPDATE_FESTIVAL_BANNER";

        invokeQuery(cmd, parameter, (param1, param2, param3) -> {
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String success = header.get("process").isString().toString();
            success = success.replaceAll("\"", "");

            if (success.equals("success")) {
                JSONObject body = (JSONObject) result.get("body");
                JSONBoolean bodyResult = body.get("result").isBoolean();
                if (bodyResult != null && bodyResult.booleanValue()) {
                    if (callback != null) {
                        callback.call(true, null);
                    }
                }
            } else {
                if (callback != null) {
                    callback.call(false, header.get("ment").isString().stringValue());
                }
            }
        });
    }

    @Override
    public void setReadOnly(boolean readFlag) {
    }
}
