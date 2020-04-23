package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.LoaderType;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class LoadingPanel extends AbstractContentPanel{

	private MaterialPanel rowsContainer;
	private MaterialPanel rowsContainerCover;
	private ContentLoader loader;

	public LoadingPanel() {
		super();
	}

	public LoadingPanel(AbstractContentPanel panel) {
		super(panel);
	}

	public LoadingPanel(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public LoadingPanel(String... initialClass) {
		super(initialClass);
	}

	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);

		this.rowsContainer = new MaterialPanel();
		this.rowsContainer.setStyle("overflow-x:hidden; overflow-y:visible");
		this.rowsContainer.setLayoutPosition(Position.ABSOLUTE);
		this.rowsContainer.setLeft(0);
		this.rowsContainer.setTop(0);
		this.rowsContainer.setHeight("100%");
		this.rowsContainer.setWidth("100%");

		this.rowsContainerCover = new MaterialPanel();
		this.rowsContainerCover.getElement().getStyle().setProperty("background", "rgba( 255, 255, 255, 0.5 )");
		this.rowsContainerCover.setLayoutPosition(Position.ABSOLUTE);
		this.rowsContainerCover.setLeft(0);
		this.rowsContainerCover.setTop(0);
		this.rowsContainerCover.setVisible(false);
		this.rowsContainerCover.setHeight("100%");
		this.rowsContainerCover.setWidth("100%");

		this.add(this.rowsContainer);
		this.add(this.rowsContainerCover);
		
		this.loader = new ContentLoader();
		this.loader.setContainer(this.rowsContainerCover);
		this.loader.setType(LoaderType.CIRCULAR);
		
	}
	
	@Override
	public void add(Widget child) {
		this.rowsContainer.add(child);
	}

	public void loading(boolean loadFlag) {

		if (loadFlag) {
			rowsContainerCover.setVisible(loadFlag);
			loader.show();
		}else {
			Timer timer = new Timer() {
				@Override
				public void run() {
					loader.hide();
					rowsContainerCover.setVisible(loadFlag);
				}
			};
			timer.schedule(1000);
		}
	}


}
