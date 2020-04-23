package kr.or.visitkorea.admin.client.manager.event;

public enum EventStatus {
	  WRITING(1)			//	작성중
	, APPROVAL_WAIT(2)		//	승인대기중
	, PROGRESS_WAIT(3)		//	진행대기중
	, PROGRESSING(4)		//	진행중
	, END(5)				//	이벤트종료
	, ANNOUNCE(6)			//	당첨자발표
	, NEGATIVE(7);			//	승인거절

	private int value;
	
	private EventStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
}