package kr.or.visitkorea.admin.client.manager.tourapi.dialog;

import com.google.gwt.dom.client.Style;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.dialogs.DialogsBundle;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchDialog extends DialogContent {

    static {
        MaterialDesignBase.injectCss(DialogsBundle.INSTANCE.contentCss());
    }

    private MaterialInput startInput;
    private MaterialInput endInput;

    public SearchDialog(MaterialExtentsWindow window) {
        super(window);
    }

    @Override
    public void init() {
        // dialog title define
        MaterialLabel dialogTitle = new MaterialLabel("검색");
        dialogTitle.setFontSize("1.4em");
        dialogTitle.setFontWeight(Style.FontWeight.BOLD);
        dialogTitle.setTextColor(Color.BLUE);
        dialogTitle.setPaddingTop(10);
        dialogTitle.setPaddingLeft(30);
        this.add(dialogTitle);

        /**
         * 기간 패널
         */
        MaterialPanel durationPanel = new MaterialPanel();
        durationPanel.setLayoutPosition(Style.Position.ABSOLUTE);
        durationPanel.setLeft(50);
        durationPanel.setTop(70);
        durationPanel.setRight(50);
        durationPanel.setTextAlign(TextAlign.CENTER);

        startInput = new MaterialInput(InputType.DATE);
        startInput.setMargin(5);
        startInput.setWidth("150px");
        startInput.setPadding(5);
        startInput.setDisplay(Display.INLINE_BLOCK);
        startInput.addValueChangeHandler(event->{
        });
        durationPanel.add(startInput);

        MaterialLabel midTxt = new MaterialLabel("~");
        midTxt.setMargin(10);
        midTxt.setDisplay(Display.INLINE_BLOCK);
        durationPanel.add(midTxt);

        endInput = new MaterialInput(InputType.DATE);
        endInput.setWidth("150px");
        endInput.setMargin(5);
        endInput.setPadding(5);
        endInput.setDisplay(Display.INLINE_BLOCK);
        endInput.addValueChangeHandler(event->{
        });
        durationPanel.add(endInput);
        this.add(durationPanel);

        addDefaultButtons();

        MaterialButton button = new MaterialButton("검색");
        button.setBackgroundColor(Color.RED_LIGHTEN_2);
        button.addClickHandler(event -> {
        });
        this.addButton(button);
    }

    @Override
    public int getHeight() {
        return 260;
    }
}
