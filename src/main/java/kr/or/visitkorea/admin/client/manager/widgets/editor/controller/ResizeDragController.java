package kr.or.visitkorea.admin.client.manager.widgets.editor.controller;

import java.util.HashMap;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.AbstractDragController;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.drop.BoundaryDropController;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox.DirectionConstant;

public final class ResizeDragController extends AbstractDragController {

  private static final int MIN_WIDGET_SIZE = 10;

  private HashMap<Widget, DirectionConstant> directionMap = new HashMap<Widget, DirectionConstant>();

  private ItemBox windowPanel = null;

  public ResizeDragController(AbsolutePanel boundaryPanel) {
    super(boundaryPanel);
  }

  @Override
  public void dragMove() {
    int direction = ((ResizeDragController) context.dragController).getDirection(context.draggable).directionBits;
    if ((direction & ItemBox.DIRECTION_NORTH) != 0) {
      int delta = context.draggable.getAbsoluteTop() - context.desiredDraggableY;
      if (delta != 0) {
        int contentHeight = windowPanel.getContentHeight();
        int newHeight = Math.max(contentHeight + delta, MIN_WIDGET_SIZE);
        if (newHeight != contentHeight) {
          windowPanel.moveBy(0, contentHeight - newHeight);
        }
        windowPanel.setContentSize(windowPanel.getContentWidth(), newHeight);
      }
    } else if ((direction & ItemBox.DIRECTION_SOUTH) != 0) {
      int delta = context.desiredDraggableY - context.draggable.getAbsoluteTop();
      if (delta != 0) {
        windowPanel.setContentSize(windowPanel.getContentWidth(), windowPanel.getContentHeight()
            + delta);
      }
    }
    if ((direction & ItemBox.DIRECTION_WEST) != 0) {
      int delta = context.draggable.getAbsoluteLeft() - context.desiredDraggableX;
      if (delta != 0) {
        int contentWidth = windowPanel.getContentWidth();
        int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
        if (newWidth != contentWidth) {
          windowPanel.moveBy(contentWidth - newWidth, 0);
        }
        windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
      }
    } else if ((direction & ItemBox.DIRECTION_EAST) != 0) {
      int delta = context.desiredDraggableX - context.draggable.getAbsoluteLeft();
      if (delta != 0) {
        windowPanel.setContentSize(windowPanel.getContentWidth() + delta,
            windowPanel.getContentHeight());
      }
    }
  }

  @Override
  public void dragStart() {
    super.dragStart();
    windowPanel = (ItemBox) context.draggable.getParent().getParent();
  }

  public void makeDraggable(Widget widget, ItemBox.DirectionConstant direction) {
    super.makeDraggable(widget);
    directionMap.put(widget, direction);
  }

  protected BoundaryDropController newBoundaryDropController(AbsolutePanel boundaryPanel,
      boolean allowDroppingOnBoundaryPanel) {
    if (allowDroppingOnBoundaryPanel) {
      throw new IllegalArgumentException();
    }
    return new BoundaryDropController(boundaryPanel, false);
  }

  private DirectionConstant getDirection(Widget draggable) {
    return directionMap.get(draggable);
  }
}