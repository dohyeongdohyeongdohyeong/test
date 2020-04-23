package kr.or.visitkorea.admin.client.manager.event.tree;

@FunctionalInterface
public interface EventTreeSelectHandler {
	void onSelect(SelectEvent event);
}
