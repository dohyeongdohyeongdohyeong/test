package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.HasActiveParent;
import gwt.material.design.client.base.HasClearActiveHandler;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.helper.UiHelper;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.events.ClearActiveEvent;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.ListItem;

public class ContentCollection extends MaterialWidget implements HasActiveParent, HasClearActiveHandler {

    private int index;
    private Heading headerLabel = new Heading(HeadingSize.H4);

    /**
     * Creates an empty collection component.
     */
    public ContentCollection() {
        super(Document.get().createULElement(), CssName.COLLECTION);
    }

    @Override
    public void clearActive() {
        clearActiveClass(this);
    }

    /**
     * Sets the header of the collection component.
     */
    public void setHeader(String header) {
        headerLabel.getElement().setInnerSafeHtml(SafeHtmlUtils.fromString(header));
        addStyleName(CssName.WITH_HEADER);
        ListItem item = new ListItem(headerLabel);
        UiHelper.addMousePressedHandlers(item);
        item.setStyleName(CssName.COLLECTION_HEADER);
        insert(item, 0);
    }

    @Override
    public void setActive(int index) {
        setActive(index, true);
    }

    @Override
    public void setActive(int index, boolean value) {
        this.index = index;
        Widget activeWidget = getActive();
        if (activeWidget != null) {
            if (index <= getWidgetCount()) {
                if (index != 0) {
                    clearActiveClass(this);
                    if (activeWidget instanceof MaterialCollectionItem) {
                        ((MaterialCollectionItem) activeWidget).setActive(value);
                    }
                } else {
                    throw new IllegalArgumentException("The active index must be a one-base index to mark as active.");
                }
            }
        }
    }

    @Override
    public Widget getActive() {
        try {
            return getWidget(index - 1);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Heading getHeaderLabel() {
        return headerLabel;
    }

    @Override
    public HandlerRegistration addClearActiveHandler(final ClearActiveEvent.ClearActiveHandler handler) {
        return addHandler(handler, ClearActiveEvent.TYPE);
    }

	public void complete() {
		((ContentImageListItem)((MaterialCollectionItem)this.getWidget(this.getWidgetCount()-1)).getWidget(0)).notDisplayDown();
	}

}
