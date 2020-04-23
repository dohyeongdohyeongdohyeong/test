package kr.or.visitkorea.admin.client.manager.dialogs;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectSeasonDialog extends DialogContent {

	public SelectSeasonDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
	
	}

	@Override
	protected void onLoad() {
	       super.onLoad();
	       clear();
	       addDefaultButtons();
			
			MaterialRow row = new MaterialRow();
			row.setLayoutPosition(Position.RELATIVE);
			row.setTop(40);
			
			MaterialColumn col1 = new MaterialColumn();
			col1.setGrid("s3");
			
			MaterialColumn col2 = new MaterialColumn();
			col2.setGrid("s3");
			
			MaterialColumn col3 = new MaterialColumn();
			col3.setGrid("s3");
			
			MaterialColumn col4 = new MaterialColumn();
			col4.setGrid("s3");
			
			row.add(col1);
			row.add(col2);
			row.add(col3);
			row.add(col4);

	       
	        // dialog title define
			MaterialCheckBox season1 = new MaterialCheckBox("봄");
			MaterialCheckBox season2 = new MaterialCheckBox("여름");
			MaterialCheckBox season3 = new MaterialCheckBox("가을");
			MaterialCheckBox season4 = new MaterialCheckBox("겨울");

			col1.add(season1);
			col2.add(season2);
			col3.add(season3);
			col4.add(season4);
			
			this.add(row);

   }

	@Override
	public int getHeight() {
		return 250;
	}
	
}
