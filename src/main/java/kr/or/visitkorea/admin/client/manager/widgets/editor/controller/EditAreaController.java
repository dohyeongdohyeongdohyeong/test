package kr.or.visitkorea.admin.client.manager.widgets.editor.controller;

import com.google.gwt.user.client.ui.AbsolutePanel;

import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.PickupDragController;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox;

public class EditAreaController {

  private final AbsolutePanel boundaryPanel;

  private PickupDragController pickupDragController;

  private ResizeDragController resizeDragController;
  
  public EditAreaController(AbsolutePanel boundaryPanel) {
    this.boundaryPanel = boundaryPanel;

    pickupDragController = new PickupDragController(boundaryPanel, true);
    pickupDragController.setBehaviorConstrainedToBoundaryPanel(true);
    pickupDragController.setBehaviorMultipleSelection(false);

    resizeDragController = new ResizeDragController(boundaryPanel);
    resizeDragController.setBehaviorConstrainedToBoundaryPanel(true);
    resizeDragController.setBehaviorMultipleSelection(false);
  }

  public AbsolutePanel getBoundaryPanel() {
    return boundaryPanel;
  }

  public PickupDragController getPickupDragController() {
    return pickupDragController;
  }

  public ResizeDragController getResizeDragController() {
    return resizeDragController;
  }
}