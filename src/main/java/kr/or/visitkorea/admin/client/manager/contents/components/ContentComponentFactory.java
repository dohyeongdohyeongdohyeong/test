package kr.or.visitkorea.admin.client.manager.contents.components;

public class ContentComponentFactory {
	
	public static ContentComponent getInstance(ContentComponentType componentType) {
		ContentComponent component = null;
		
		switch (componentType) {
			case TEXT: {
				component = new ContentComponentText(componentType);
			} break;
	
			case IMAGE: {
				component = new ContentComponentImage(componentType);
			} break;
		}
		
		return component;
	}
}
