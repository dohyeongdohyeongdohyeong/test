package kr.or.visitkorea.admin.client.manager.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface LoginContentBundle extends ClientBundle{

	LoginContentBundle INSTANCE = GWT.create(LoginContentBundle.class);

    @Source("resources/css/login.css")
    TextResource login();

}
