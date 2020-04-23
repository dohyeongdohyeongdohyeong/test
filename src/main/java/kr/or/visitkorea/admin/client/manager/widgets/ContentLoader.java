package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.constants.LoaderType;
import gwt.material.design.client.constants.SpinnerColor;
import gwt.material.design.client.ui.MaterialPreLoader;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.MaterialSpinner;
import gwt.material.design.client.ui.html.Div;

public class ContentLoader {


    private static ContentLoader loader = new ContentLoader(LoaderType.CIRCULAR);

    private boolean scrollDisabled;
    private Div div = new Div();
    private MaterialPreLoader preLoader = new MaterialPreLoader();
    private MaterialProgress progress = new MaterialProgress();
    private Panel container = RootPanel.get();
    private LoaderType type = LoaderType.CIRCULAR;

    public ContentLoader(LoaderType type) {
        this();
        setType(type);
    }

    public ContentLoader() {
        build();
    }

    protected void build() {
        div.setStyleName(CssName.VALIGN_WRAPPER + " " + CssName.LOADER_WRAPPER);
        preLoader.getElement().getStyle().setProperty("margin", "auto");
        preLoader.add(new MaterialSpinner(SpinnerColor.BLUE));
        preLoader.add(new MaterialSpinner(SpinnerColor.RED));
        preLoader.add(new MaterialSpinner(SpinnerColor.YELLOW));
        preLoader.add(new MaterialSpinner(SpinnerColor.GREEN));
    }

    /**
     * Shows the Loader component
     */
    public void show() {
        if (!(container instanceof RootPanel)) {
  //          container.getElement().getStyle().setPosition(Style.Position.RELATIVE);
            div.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        }
        if (scrollDisabled) {
            RootPanel.get().getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        }
        if (type == LoaderType.CIRCULAR) {
            div.setStyleName(CssName.VALIGN_WRAPPER + " " + CssName.LOADER_WRAPPER);
            div.add(preLoader);
        } else if (type == LoaderType.PROGRESS) {
            div.setStyleName(CssName.VALIGN_WRAPPER + " " + CssName.PROGRESS_WRAPPER);
            progress.getElement().getStyle().setProperty("margin", "auto");
            div.add(progress);
        }
        container.add(div);
    }

    /**
     * Hides the Loader component
     */
    public void hide() {
        div.removeFromParent();
        if (scrollDisabled) {
            RootPanel.get().getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        }
        if (type == LoaderType.CIRCULAR) {
            preLoader.removeFromParent();
        } else if (type == LoaderType.PROGRESS) {
            progress.removeFromParent();
        }
    }

    /**
     * Static helper class that shows / hides a circular loader
     */
    public static void loading(boolean visible) {
        loading(visible, RootPanel.get());
    }

    /**
     * Static helper class that shows / hides a circular loader within a container
     */
    public static void loading(boolean visible, Panel container) {
        loader.setType(LoaderType.CIRCULAR);
        loader.setContainer(container);
        if (visible) {
            loader.show();
        } else {
            loader.hide();
        }
    }

    /**
     * Static helper class that shows / hides a progress loader
     */
    public static void progress(boolean visible) {
        progress(visible, RootPanel.get());
    }

    /**
     * Static helper class that shows / hides a progress loader within a container
     */
    public static void progress(boolean visible, Panel container) {
        loader.setType(LoaderType.PROGRESS);
        loader.setContainer(container);
        if (visible) {
            loader.show();
        } else {
            loader.hide();
        }
    }

    /**
     * Get the type of the MaterialLoader (Default CIRCULAR)
     */
    public LoaderType getType() {
        return type;
    }

    /**
     * Set the type of the MaterialLoader
     *
     * @param type There are two types of Loader (CIRCULAR and PROGRESS)
     */
    public void setType(LoaderType type) {
        this.type = type;
    }

    /**
     * Get the Container that wraps the MaterialLoader (Default RootPanel)
     */
    public Panel getContainer() {
        return container;
    }

    /**
     * Set the Container of the MaterialLoader
     */
    public void setContainer(Panel container) {
        this.container = container;
    }

    /**
     * Get the value whether the scroll is enabled or disabled (Default false)
     */
    public boolean isScrollDisabled() {
        return scrollDisabled;
    }

    /**
     * Set whether the loader will allow a body scroll when it is shown
     */
    public void setScrollDisabled(boolean scrollDisabled) {
        this.scrollDisabled = scrollDisabled;
    }
}
