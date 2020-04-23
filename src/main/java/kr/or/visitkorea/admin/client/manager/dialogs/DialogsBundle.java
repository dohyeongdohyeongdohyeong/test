package kr.or.visitkorea.admin.client.manager.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface DialogsBundle extends ClientBundle{

	DialogsBundle INSTANCE = GWT.create(DialogsBundle.class);

    @Source("resources/css/dialogs.css")
    TextResource contentCss();

}
