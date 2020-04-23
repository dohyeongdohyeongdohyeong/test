package kr.or.visitkorea.admin.client.manager.imageManager;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.imageManager.composite.ImageManagerMain;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

/**
 * @author Admin
 * 이미지 관리 Application
 */
public class ImageManagerApplication extends ApplicationBase {

	private ImageManagerMain main;

	public interface OnSearchListener {
		void onSearch(String startDate, String endDate);
	}

	public ImageManagerApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		setDivisionName(divisionName);
		windowLiveFlag = true;
		window = materialExtentsWindow;
		window.addCloseHandler(event -> {
			windowLiveFlag = false;
		});

		main = new ImageManagerMain(window, this);
	}

	@Override
	public void start() {
		start(null);
	}

	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		window.add(main);
		window.open(window);
	}
}