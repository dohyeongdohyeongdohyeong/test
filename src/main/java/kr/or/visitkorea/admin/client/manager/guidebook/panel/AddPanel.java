package kr.or.visitkorea.admin.client.manager.guidebook.panel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRadioButton;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.guidebook.GuideBookApplication;
import kr.or.visitkorea.admin.client.manager.guidebook.GuideBookMain;
import kr.or.visitkorea.admin.client.manager.guidebook.model.GuideBook;
import kr.or.visitkorea.admin.client.manager.widgets.FileUploadSimplePanel;

/**
 * 편집을 위한 패널
 * - 등록, 수정(삭제)
 */
public class AddPanel extends BasePanel {

	private GuideBook guideBook;

	private final MaterialRadioButton useRadioYes;
	private final MaterialRadioButton useRadioNo;
	private final MaterialTextBox inputTitle;
	private final MaterialInput publishDate;
	private final MaterialTextBox publisher;
//	private final MaterialComboBox<String> themeComboBox;
	private final MaterialComboBox<String> areaComboBox;
	private final MaterialComboBox<String> sigunguComboBox;
	private final MaterialTextBox inputPdfLink;
	private FileUploadSimplePanel btnPDFUP;

	private final MaterialFileUploader uploader;
	private final MaterialImage image;
	private final MaterialTextBox inputImageDesc;
	private final MaterialButton cancelButton;
	private final MaterialButton saveButton;

	public AddPanel(GuideBookMain host) {
		this(host, false); // 등록 모드
	}

	public AddPanel(GuideBookMain host, boolean isEditMode) {
		super(host);
		add(createTitleLabel());
		if (isEditMode) {
			setHeaderTitle("관광가이드북 관리 - 수정");
			guideBook = host.getSelectedGuideBook();
		} else {
			setHeaderTitle("관광가이드북 관리 - 등록");
			guideBook = new GuideBook();
		}

		MaterialPanel leftPanel = new MaterialPanel();

		useRadioYes = new MaterialRadioButton("use");
		useRadioYes.setText("사용");
		useRadioNo = new MaterialRadioButton("use");
		useRadioNo.setText("미사용");
		useRadioNo.setValue(true, false);
		leftPanel.add(wrapWithRow(
				wrapWithColumn(useRadioYes, "s4"),
				wrapWithColumn(useRadioNo, "s4")));

		if (isEditMode) {
			if (guideBook.use.toString().equals("1")) {
				useRadioNo.setValue(false, false);
				useRadioYes.setValue(true, false);
			} else {
				useRadioYes.setValue(false, false);
				useRadioNo.setValue(true, false);
			}
		}

		inputTitle = new MaterialTextBox();
		inputTitle.setLabel("제목");
		leftPanel.add(wrapWithRow(wrapWithColumn(inputTitle, "s12")));

		if (isEditMode) {
			inputTitle.setText(guideBook.title);
		}

		publisher = new MaterialTextBox();
		publisher.setMargin(0);
		publisher.setLabel("제작처");
		if (isEditMode) {
			publisher.setText(guideBook.publisher);
		}
		
		publishDate = new MaterialInput(InputType.DATE);
		publishDate.setMargin(0);
		publishDate.setTooltip("발행일");
		if (isEditMode) {
			publishDate.setText(guideBook.publishDate);
		}
		
		leftPanel.add(wrapWithRow(
				wrapWithColumn(publisher, "s8"),
				wrapWithColumn(publishDate, "s4")));
		
		//2019-12-03 프론트 테마 비노출로 인한 테마 숨김
		/*themeComboBox = new MaterialComboBox<>();
		themeComboBox.setHideSearch(true);
		themeComboBox.setLabel("테마선택");
		themeComboBox.addItem("전체", "");
		GuideBookApplication.fetchThemeCodeValues(themeComboBox, result -> {
			if (result) {
				if (isEditMode) {
					themeComboBox.setSingleValue(guideBook.themeCode, false);
				} else {
					themeComboBox.setSelectedIndex(0);
				}
			}
		});*/
		
		areaComboBox = new MaterialComboBox<>();
		areaComboBox.setHideSearch(true);
		areaComboBox.setLabel("지역선택");
		areaComboBox.addItem("전체", "");
		areaComboBox.addItem("전국", "0");

		sigunguComboBox = new MaterialComboBox<>();
		sigunguComboBox.setHideSearch(true);
		sigunguComboBox.setEnabled(false);
		sigunguComboBox.setLabel("시군구선택");
		sigunguComboBox.addItem("전체", "");

		MaterialRow areaRow = wrapWithRow(
//				wrapWithColumn(themeComboBox, "s4"),
				wrapWithColumn(areaComboBox, "s6"),
				wrapWithColumn(sigunguComboBox, "s6"));
		leftPanel.add(areaRow);

		inputPdfLink = new MaterialTextBox();
		inputPdfLink.setLabel("가이드북 URL");
		leftPanel.add(wrapWithRow(wrapWithColumn(inputPdfLink, "s12")));

		btnPDFUP = new FileUploadSimplePanel(170, 40, Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=a9158773-bd4d-49f2-a901-d3a7e204a773&chk=e1b88981-5050-4e79-989d-7006b8b41714");
		btnPDFUP.getUploader().setAcceptedFiles(".pdf,.PDF"); 
		btnPDFUP.getUploader().setMaxFileSize(20);
		btnPDFUP.setBackgroundColor(Color.BLUE_DARKEN_3);
		btnPDFUP.getUploader().addSuccessHandler(event->{
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			inputPdfLink.setText((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);
		});
		btnPDFUP.getUploader().addErrorHandler(event->{
			new MaterialToast().toast("파일 용량이 초과 또는 기타 오류가 발생했습니다. 20M 이하의 파일만 가능합니다.", 3000);
			btnPDFUP.setEnabled(true);
		});
//		btnPDFUP.getUploader().addCancelHandler(event->{
//			Console.log("addCancelHandler");
//		});
//		btnPDFUP.getUploader().addMaxFilesReachHandler(event->{
//			Console.log("MaxFilesReach");
//		});
//		btnPDFUP.getUploader().addMaxFilesExceededHandler(event->{
//			Console.log("addMaxFilesExceededHandler");
//		});
		leftPanel.add(wrapWithRow(wrapWithColumn(btnPDFUP, "s12")));
		
		MaterialLabel lbtmp = new MaterialLabel("파일크기 20M 이하");
		leftPanel.add(wrapWithRow(wrapWithColumn(lbtmp, "s12")));
		if (isEditMode) {
			inputPdfLink.setText(guideBook.pdfLink);
		}
		
		GuideBookApplication.fetchAreaCodeValues(areaComboBox, (result) -> {
			if (result) {
				if (isEditMode) {
					areaComboBox.setSingleValue(guideBook.areaCode, false);
					GuideBookApplication.fetchSigunguCodeValues(sigunguComboBox, guideBook.areaCode, (result2) -> {
						if (result2) {
							sigunguComboBox.setEnabled(true);
							sigunguComboBox.setSingleValue(guideBook.sigunguCode);
						}
					});
				} else {
					areaComboBox.setSelectedIndex(0);
				}
			}
		});
		
		areaComboBox.addSelectionHandler(event -> {
			String selectedAreaCode = areaComboBox.getSingleValue();
			if (selectedAreaCode.equals("")||selectedAreaCode.equals("0")) {
				sigunguComboBox.clear();
				sigunguComboBox.addItem("전체", "");
				sigunguComboBox.setSelectedIndex(0);
				sigunguComboBox.setEnabled(false);
				return;
			}
			GuideBookApplication.fetchSigunguCodeValues(sigunguComboBox, selectedAreaCode, (result) -> {
				if (result) {
					sigunguComboBox.setEnabled(true);
					sigunguComboBox.setSelectedIndex(0);
				}
			});
		});


		MaterialPanel rightPanel = new MaterialPanel();

		image = new MaterialImage();

		if (isEditMode) {
			image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=TEMP_VIEW&name=" + guideBook.imagePath.substring(guideBook.imagePath.lastIndexOf("/") + 1));
		} else {
			image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=null");
		}

		uploader = new MaterialFileUploader();

		MaterialUploadLabel uploadLabel = new MaterialUploadLabel("이미지 업로드", "위 아이콘을 선택하세요.");
		uploadLabel.getIcon().addClickHandler(event -> {
			if (!image.getUrl().contains("null")) {
				event.preventDefault();
				event.stopPropagation();
				uploader.setEnabled(false);
				host.getMaterialExtentsWindow().confirm("이미 등록된 이미지가 있습니다. 삭제하시겠습니까?", clickEvent -> {
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
		uploader.setMaxFileSize(1);
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

			String body = event.getResponse().getBody();
			JSONObject object = JSONParser.parseStrict(body).isObject()
						.get("body").isObject()
					    .get("result").isArray().get(0).isObject();
			
			String path = object.get("fullPath").isString().stringValue();
			Console.log("Upload success " + path);

			if (path.contains("/images")) {
				guideBook.imagePath = path.substring(path.indexOf("/images") + "/images".length());
			} else {
				guideBook.imagePath = path;
			}

			String name = object.get("saveName").isString().stringValue();
//			String id = name.substring(0, name.lastIndexOf("."));
			image.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=TEMP_VIEW&name=" + name);
		});

		MaterialLabel label = new MaterialLabel("이미지 설정");
		label.setFontSize(1.1, Style.Unit.EM);
		label.setTextAlign(TextAlign.LEFT);
		rightPanel.add(wrapWithRow(wrapWithColumn(label, "s4")));
		rightPanel.add(wrapWithRow(wrapWithColumn(uploader, "s4"), wrapWithColumn(image, "s8")));

		inputImageDesc = new MaterialTextBox();
		inputImageDesc.setLabel("이미지 설명");
		rightPanel.add(wrapWithRow(wrapWithColumn(inputImageDesc, "s12")));

		if (isEditMode) {
			inputImageDesc.setText(guideBook.imageDesc);
		}

		cancelButton = new MaterialButton("취소", IconType.CANCEL);
		cancelButton.setFontSize(1.1, Style.Unit.EM);
		cancelButton.setHeight("40px");
		cancelButton.setMarginRight(10);
		cancelButton.addClickHandler(event -> host.switchToSearchPanel());

		saveButton = new MaterialButton("저장", IconType.SAVE);
		saveButton.setFontSize(1.1, Style.Unit.EM);
		saveButton.setHeight("40px");
		saveButton.setMarginRight(10);
		saveButton.addClickHandler(event -> {

			JSONObject authInfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
			String userId = authInfo.get("USR_ID").isString().stringValue();

			guideBook.title = inputTitle.getText();
			guideBook.use = useRadioYes.getValue() ? 1 : 0;
			guideBook.publisher = publisher.getValue();
			guideBook.publishDate = publishDate.getValue();
//			guideBook.themeCode = themeComboBox.getSingleValue();
			guideBook.areaCode = areaComboBox.getSingleValue();
			guideBook.sigunguCode = sigunguComboBox.getSingleValue();
			guideBook.imageDesc = inputImageDesc.getText();
			guideBook.pdfLink = inputPdfLink.getText();
			guideBook.author = userId;

			if (!isValidate(guideBook)) {
				return;
			}

			final Functions.Func2<Boolean, String> callBack = (success, errorMessage) -> {
				if (success) {
					saveButton.setEnabled(false);
					new MaterialToast(host::switchToSearchPanel).toast("저장하였습니다.", 3000);
					host.refreshSearchPanel();
				} else {
					showAlert(errorMessage);
				}
			};

			if (isEditMode) {
				GuideBookApplication.updateGuideBook(guideBook, callBack);
			} else {
				GuideBookApplication.insertGuideBook(guideBook, callBack);
			}
		});

		MaterialRow actionButtons = wrapWithRow(cancelButton, saveButton);
		actionButtons.setTextAlign(TextAlign.RIGHT);
		rightPanel.add(actionButtons);

		add(wrapScrollPanel(wrapWithRow(
				wrapWithColumn(leftPanel, "s6"),
				wrapWithColumn(rightPanel, "s6")), "560px"));
	}

	private boolean isValidate(GuideBook book) {
		if (book.title == null || book.title.length() == 0) {
			MaterialToast.fireToast("제목은 필수 입력 항목입니다.");
			return false;
		}

		if (book.areaCode == null || book.areaCode.equals("")) {
			MaterialToast.fireToast("시도는 필수 입력 항목입니다.");
			return false;
		}

//		if (book.sigunguCode == null || book.sigunguCode.equals("")) {
//			MaterialToast.fireToast("시군구는 필수 입력 항목입니다.");
//			return false;
//		}

		if ((book.imagePath == null || book.imagePath.length() == 0) &&
				(book.imageId == null || book.imageId.length() == 0)) {
			MaterialToast.fireToast("이미지를 먼저 업로드하세요.");
			return false;
		}

		if (book.imageDesc == null || book.imageDesc.length() == 0) {
			MaterialToast.fireToast("이미지 설명은 필수 입력 항목입니다.");
			return false;
		}

		if (book.pdfLink == null || book.pdfLink.length() == 0) {
			MaterialToast.fireToast("PDF LINK는 필수 입력 항목입니다.");
			return false;
		}

		return true;
	}
}