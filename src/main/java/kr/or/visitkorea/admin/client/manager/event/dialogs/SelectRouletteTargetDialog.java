package kr.or.visitkorea.admin.client.manager.event.dialogs;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.dom.client.Style.FontWeight;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentOXQuiz;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentRoulette;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentType;
import kr.or.visitkorea.admin.client.manager.event.components.IEventComponent;
import kr.or.visitkorea.admin.client.manager.event.model.EventProcess;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectRouletteTargetDialog extends DialogContent {
	private List<?> processList;
	private MaterialLabel titleLabel;
	private EventContentsTree host;
	private MaterialComboBox<EventProcess> combo;
	private String Game;
	private int Gamevalue;
	
	public SelectRouletteTargetDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	public void init() {
		this.addDefaultButtons();
		
		this.titleLabel = new MaterialLabel();
		this.titleLabel.setText("대상 프로그램 선택");
		this.titleLabel.setFontWeight(FontWeight.BOLD);
		this.titleLabel.setTextColor(Color.BLUE);
		this.titleLabel.setFontSize("1.4em");
		this.titleLabel.setPadding(15);
		this.add(this.titleLabel);
		
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setPadding(15);
		panel.setTextAlign(TextAlign.CENTER);
		
		this.combo = new MaterialComboBox<>();
		panel.add(this.combo);

		MaterialButton submitBtn = new MaterialButton();
		submitBtn.setText("확인");
		submitBtn.addClickHandler(e -> {
			if (this.combo.getSelectedValue().size() == 0) {
				this.alert("실패 알림", 350, 250, new String[] {
						"대상 프로그램이 선택되지 않았습니다."
				});
				return;
			}
			if(Game == "Roulette") {
				IEventComponent component = this.host.createComponent(EventComponentType.ROULETTE);
				EventProcess eventProcess = this.combo.getSelectedValue().get(0);
				EventComponentRoulette roulComponent = (EventComponentRoulette) component;
				roulComponent.setSelectedProcess(eventProcess);
				roulComponent.getComponentObj().setSubEvtId(eventProcess.getSubEvtId());
				roulComponent.refresh();
			} else if(Game == "OXQuiz") {
				IEventComponent component = this.host.createComponent(EventComponentType.OXQUIZ);
				EventProcess eventProcess = this.combo.getSelectedValue().get(0);
				EventComponentOXQuiz OXQuizComponent = (EventComponentOXQuiz)component;
				OXQuizComponent.getComponentObj().setSubEvtId(eventProcess.getSubEvtId());
				OXQuizComponent.refresh();
			}
			
			this.getMaterialExtentsWindow().closeDialog();
		});
		this.addButton(submitBtn);
		
		this.add(panel);
	}

	@Override
	public int getHeight() {
		return 250;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.host = (EventContentsTree) this.getParameters().get("host");
		this.processList = (List<?>) this.getParameters().get("processList");
		this.Game = (String) this.getParameters().get("Game");
		
		Gamevalue = 99;
		if(Game =="Roulette") {
			Gamevalue = 2;
		} else if(Game == "OXQuiz") {
			Gamevalue = 4;
		}
		List<EventProcess> list = this.processList.stream()
										.filter(o -> o instanceof EventProcess)
										.map(o -> (EventProcess) o)
										.filter(o -> o.getEvtTypeCd() == Gamevalue)
										.collect(Collectors.toList());
		this.combo.clear();
		list.forEach(item -> {
			this.combo.addItem(item.getTitle(), item);
		});
	}

}
