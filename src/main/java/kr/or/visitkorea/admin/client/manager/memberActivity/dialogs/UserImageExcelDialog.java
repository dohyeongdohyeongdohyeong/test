package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.Window;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class UserImageExcelDialog extends DialogContent {

	
	private MaterialTextBox sdate;
	private MaterialTextBox edate;

	public UserImageExcelDialog(MaterialExtentsWindow tgrWindow, MemberActivityMain host) {
		super(tgrWindow);
	}

	@Override
	public void init() {
		
		MaterialLabel dialogTitle = new MaterialLabel("사용자 사진 엑셀 다운로드");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		
		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLabel("기간");
		sdate.setText("-");
		sdate.setFloat(Float.LEFT);
		sdate.setGrid("s6");
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setFloat(Float.LEFT);
		edate.setGrid("s6");

		MaterialRow row2 = new MaterialRow();
		row2.setMarginTop(20);
		row2.add(sdate);
		row2.add(edate);
		this.add(row2);
		
		MaterialRow row3 = new MaterialRow();
		
		MaterialLabel info = new MaterialLabel();
		info.setText(" * 기간 미입력시 전체 데이터를 다운로드 합니다.");
		info.setGrid("s12");
		row2.add(info);
		this.add(row3);
		addDefaultButtons();
		
		MaterialButton DownloadButton = new MaterialButton("엑셀 다운로드");
		
		DownloadButton.addClickHandler(event->{
			

			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			downurl += "&select_type=UserImageList";
			
			String strsdate = sdate.getText().toString();
			if (!"".equals(strsdate))
				downurl += "&startInput=" + strsdate;
			String stredate = edate.getText().toString();
			if (!"".equals(stredate)) 
				downurl += "&endInput=" + stredate + " 23:59:59";
			
			Window.open(downurl,"_self", "enabled");
			String[] alertmessages = {"엑셀 다운로드 알림","데이터 양이 많을 경우","최대 2분정도의 시간이 소요 될 수 있습니다."};
			alert("알림", 500, 300, alertmessages);
			
		});
		addButton(DownloadButton);
		
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 260;
	}


}
