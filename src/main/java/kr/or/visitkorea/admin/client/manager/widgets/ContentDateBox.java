package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.user.client.ui.TextBox;

import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialValueBox;

public class ContentDateBox extends MaterialValueBox<String> {

    public ContentDateBox() {
        super(new TextBox());
        setType(InputType.DATE);
    }

    public ContentDateBox(String placeholder) {
        this();
        setPlaceholder(placeholder);
    }

    public void setMaxLength(int length) {
        asTextBox().setMaxLength(length);
    }

    public int getMaxLength() {
        return asTextBox().getMaxLength();
    }

    public void setVisibleLength(int length) {
        asTextBox().setVisibleLength(length);
    }

    public int getVisibleLength() {
        return asTextBox().getVisibleLength();
    }

    @Ignore
    public TextBox asTextBox() {
        return (TextBox) valueBoxBase;
    }
}