package kr.or.visitkorea.admin.client.widgets.window;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialVideo;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;

public class HelpMovieDialog extends DialogContent {

	private MaterialVideo video;
	private Map<String, Object> diagParam;
	private MaterialPanel bottomButton;

	public HelpMovieDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		
	       addDefaultButtons();
	       
	        // dialog title define
			MaterialLabel dialogTitle = new MaterialLabel("HELP~!");
			dialogTitle.setFontSize("1.4em");
			dialogTitle.setFontWeight(FontWeight.BOLD);
			dialogTitle.setPaddingTop(10);
			dialogTitle.setPaddingLeft(30);
			this.add(dialogTitle);
			
			bottomButton = new MaterialPanel();
			bottomButton.setLayoutPosition(Position.ABSOLUTE);
			bottomButton.setBottom(30);
			bottomButton.setLeft(30);
			bottomButton.setWidth("750px");
			this.add(bottomButton);
			
			video = new MaterialVideo();
			video.setFullscreen(true);
			video.setAllowBlank(true);
			video.setLayoutPosition(Position.ABSOLUTE);
			video.setPaddingBottom(0);
			video.setWidth("840px");
			video.setHeight("450px");
			video.setTop(50);
			video.setLeft(30);
			this.add(video);
			
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		if (parameters.get("HELPS") != null) diagParam = (Map<String, Object>) parameters.get("HELPS");
		
		bottomButton.clear();
		
		int idx = 1;
		for (String keyName : diagParam.keySet()) {
			
			MaterialLink keyLink = new MaterialLink();
			keyLink.setDisplay(Display.INLINE_BLOCK);
			keyLink.setVerticalAlign(VerticalAlign.MIDDLE);
			keyLink.setText(idx + ". " + keyName + "　");
			keyLink.setOverflow(Overflow.HIDDEN);
			bottomButton.add(keyLink);
			
			String tgrUrl = (String) diagParam.get(keyName);
			
			keyLink.addClickHandler(event->{
				if (tgrUrl.contains("&")) {
					video.setUrl((String) diagParam.get(keyName) + "&autoplay=1");
				}else {
					video.setUrl((String) diagParam.get(keyName) + "?autoplay=1");
				}
			});

			if (idx == 1) {
				if (tgrUrl.contains("&")) {
					video.setUrl((String) diagParam.get(keyName) + "&autoplay=1");
				}else {
					video.setUrl((String) diagParam.get(keyName) + "?autoplay=1");
				}
			}

			idx++;
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	public int getHeight() {
		return 600;
	}

}
