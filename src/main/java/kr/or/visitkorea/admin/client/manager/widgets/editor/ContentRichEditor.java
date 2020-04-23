package kr.or.visitkorea.admin.client.manager.widgets.editor;

import static gwt.material.design.addins.client.richeditor.js.JsRichEditor.$;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.addins.client.richeditor.base.ToolBarManager;
import gwt.material.design.addins.client.richeditor.base.constants.RichEditorEvents;
import gwt.material.design.addins.client.richeditor.events.PasteEvent;
import gwt.material.design.addins.client.richeditor.js.JsRichEditor;
import gwt.material.design.addins.client.richeditor.js.JsRichEditorOptions;

public class ContentRichEditor extends MaterialRichEditor{

	public ContentRichEditor() {
		super();
	}

	public ContentRichEditor(String placeholder, String value) {
		super(placeholder, value);
	}

	public ContentRichEditor(String placeholder) {
		super(placeholder);
	}

    private String html;
    private ToolBarManager manager = new ToolBarManager();
    private boolean toggleFullScreen = true;
    private JsRichEditorOptions options = JsRichEditorOptions.create();
    private HandlerRegistration handlerRegistration;

    @Override
    public void load() {

        JsRichEditor jsRichEditor = $(getElement());
        options.toolbar = manager.getToolbars();
        options.placeholder = getPlaceholder();
        options.height = getHeight();

        jsRichEditor.materialnote(options);

        // Events
        jsRichEditor.on(RichEditorEvents.MATERIALNOTE_BLUR, event -> {
            fireEvent(new BlurEvent() {});
            return true;
        });
        jsRichEditor.on(RichEditorEvents.MATERIALNOTE_FOCUS, event -> {
            fireEvent(new FocusEvent() {});
            return true;
        });
        jsRichEditor.on(RichEditorEvents.MATERIALNOTE_KEYUP, event -> {
            fireEvent(new KeyUpEvent() {});
            return true;
        });
        jsRichEditor.on(RichEditorEvents.MATERIALNOTE_KEYDOWN, event -> {
            fireEvent(new KeyDownEvent() {});
            return true;
        });
        jsRichEditor.on(RichEditorEvents.MATERIALNOTE_PASTE, event -> {
            fireEvent(new PasteEvent() {});
            return true;
        });
        jsRichEditor.on(RichEditorEvents.MATERIALNOTE_CHANGE, event -> {
            ValueChangeEvent.fire(ContentRichEditor.this, getHTMLCode(getElement()));
            return true;
        });

    }

}
