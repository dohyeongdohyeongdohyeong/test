/*
 * Copyright 2009 Fred Sauer
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package kr.or.visitkorea.admin.client.manager.widgets.dnd.client.drop;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.DragContext;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.DOMUtil;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.DragClientBundle;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.LocationWidgetComparator;

/**
 * A {@link DropController} for instances of {@link FlowPanel}.
 */
public class FlowPanelDropController extends AbstractInsertPanelDropController {

  /**
   * @param dropTarget the flow panel drop target
   */
  public FlowPanelDropController(FlowPanel dropTarget) {
    super(dropTarget);
  }

  @Override
  protected LocationWidgetComparator getLocationWidgetComparator() {
    return isRtl() ? LocationWidgetComparator.BOTTOM_LEFT_COMPARATOR
        : LocationWidgetComparator.BOTTOM_RIGHT_COMPARATOR;
  }

  @Override
  protected Widget newPositioner(DragContext context) {
    HTML positioner = new HTML("&#x203B;");
    positioner.addStyleName(DragClientBundle.INSTANCE.css().flowPanelPositioner());
    return positioner;
  }
}
