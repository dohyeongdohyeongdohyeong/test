package kr.or.visitkorea.admin.client.manager.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface AccountContentBundle extends ClientBundle{

	AccountContentBundle INSTANCE = GWT.create(AccountContentBundle.class);

    @Source("resources/css/accountContent.css")
    TextResource accountContentCss();

}
