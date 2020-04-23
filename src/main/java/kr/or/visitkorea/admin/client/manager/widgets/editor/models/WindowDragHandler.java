package kr.or.visitkorea.admin.client.manager.widgets.editor.models;

import com.google.gwt.user.client.ui.Widget;

import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.DragEndEvent;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.DragHandler;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.DragStartEvent;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.VetoDragException;

public class WindowDragHandler implements DragHandler {

	  /**
	   * CSS blue.
	   */
	  public static final String BLUE = "#4444BB";

	  /**
	   * CSS green.
	   */
	  public static final String GREEN = "#44BB44";

	  /**
	   * CSS red.
	   */
	  public static final String RED = "#BB4444";

	  /**
	   * Text area where event messages are shown.
	   */
	  private final Widget eventTextArea;

	  WindowDragHandler(Widget dragHandlerHTML) {
	    eventTextArea = dragHandlerHTML;
	  }

	  /**
	   * Log the drag end event.
	   * 
	   * @param event the event to log
	   */
	  @Override
	  public void onDragEnd(DragEndEvent event) {
	    log("onDragEnd: " + event, RED);
	  }

	  /**
	   * Log the drag start event.
	   * 
	   * @param event the event to log
	   */
	  @Override
	  public void onDragStart(DragStartEvent event) {
	    log("onDragStart: " + event, GREEN);
	  }

	  /**
	   * Log the preview drag end event.
	   * 
	   * @param event the event to log
	   * @throws VetoDragException exception which may be thrown by any drag handler
	   */
	  @Override
	  public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
	    log("<br>onPreviewDragEnd: " + event, BLUE);
	  }

	  /**
	   * Log the preview drag start event.
	   * 
	   * @param event the event to log
	   * @throws VetoDragException exception which may be thrown by any drag handler
	   */
	  @Override
	  public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
	    log("onPreviewDragStart: " + event, BLUE);
	  }

	  public void log(String text, String color) {
		  /**
		   * 
	        + (eventTextArea.getHTML().length() == 0 ? "" : "<br>") + "<span style='color: " + color
	        eventTextArea.setHTML(eventTextArea.getHTML()
	        + "'>" + text + "</span>");
		   */
	  }
}
