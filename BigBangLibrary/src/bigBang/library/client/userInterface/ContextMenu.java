package bigBang.library.client.userInterface;

import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class ContextMenu extends View {

	private static ContextMenu showingContext;
	private PopupPanel popup;
	
	public ContextMenu(){
		popup = new PopupPanel();
		SimplePanel panel = new SimplePanel();
		initWidget(panel);
		popup.setWidget(this);
	
		if(showingContext == null)
			showingContext = this; 
		
		final ContextMenu thisMenu = this;
		
		this.addDomHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				if(showingContext != thisMenu){
					if(showingContext != null)
						showingContext.hide();
				}
				event.stopPropagation();
			}
		}, ClickEvent.getType());
	}
	
	public void show(int x, int y){
		popup.setPopupPosition(x, y);
		popup.show();
	}
	
	public void hide() {
		popup.hide();
	}
}
