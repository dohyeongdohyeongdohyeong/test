package kr.or.visitkorea.admin.client.manager.preview; 

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.preview.composite.PreviewMainComposite;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PreviewApplication extends ApplicationBase{
	
	public PreviewApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("미리 보기");
		
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
	}

	public void start() {
		start(null);
		
	}
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(new PreviewMainComposite(this.window));
		this.window.open(this.window);
	}
}
