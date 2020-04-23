package kr.or.visitkorea.admin.client.manager.event.components;

public enum EventComponentType {
	TEXT(0),
	IMAGE(1),
	ROULETTE(2),
	SHARE(3),
	OXQUIZ(4);
	
	private int value;
	 
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	EventComponentType(int value) {
		this.value = value;
	}
	
}
