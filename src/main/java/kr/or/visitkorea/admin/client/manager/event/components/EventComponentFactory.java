package kr.or.visitkorea.admin.client.manager.event.components;

import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventComponentFactory {
	
	public static IEventComponent getInstance(EventComponentType componentType, AbstractContentPanel host,MaterialExtentsWindow window, String mode) {
		IEventComponent component = null;
		
		switch (componentType) {
			case TEXT: {
				component = new EventComponentText(componentType, host,mode);
			} break;
	
			case IMAGE: {
				component = new EventComponentImage(componentType, host, mode);
			} break;
			
			case ROULETTE: {
				component = new EventComponentRoulette(componentType, host);
			} break;
			case OXQUIZ: {
				component = new EventComponentOXQuiz(componentType, host, window);
			} break;
		}
		
		return component;
	}
}
