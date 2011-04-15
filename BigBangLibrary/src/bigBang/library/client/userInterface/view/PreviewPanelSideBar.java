package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PreviewPanelSideBar extends View {

	protected VerticalPanel wrapper;
	
	public PreviewPanelSideBar(){
		wrapper = new VerticalPanel();
		this.setSize("100%", "100%");
		
		
		initWidget(wrapper);
	}
	
	public void addWidget(Widget w) {
		wrapper.add(w);
	}
	
}
