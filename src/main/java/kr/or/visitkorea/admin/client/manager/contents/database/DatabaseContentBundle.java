package kr.or.visitkorea.admin.client.manager.contents.database;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface DatabaseContentBundle extends ClientBundle{

	DatabaseContentBundle INSTANCE = GWT.create(DatabaseContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
