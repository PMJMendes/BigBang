package bigBang.module.clientModule.client.userInterface;

import bigBang.library.client.userInterface.ContextMenu;
import bigBang.library.client.userInterface.SearchPanelListEntry;
import bigBang.module.clientModule.shared.ClientProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;

public class ClientSearchPanelListEntry extends SearchPanelListEntry<ClientProxy> {

	private String rightWidgetImageDefaultUrl = "images/clientListIcon1.png";
	private String rightWidgetImageSelectedUrl = "images/clientListIcon1Selected.png";
	
	public ClientSearchPanelListEntry(ClientProxy value) {
		super(value);
		Image rightImage = new Image("images/clientListIcon1.png");
		rightImage.setSize("20px", "20px");
		rightImage.setTitle("Cliente");
		this.setRightWidget(rightImage);
		this.setHeight("40px");
		
		this.setRightClickable(true);
	}
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		this.setRightWidget(new Image(selected ? rightWidgetImageSelectedUrl : rightWidgetImageDefaultUrl));
	}
	
	@Override
	public void onRightClick(Event event){
		GWT.log("click");
		super.onRightClick(event);
		event.stopPropagation();
		event.preventDefault();
		ContextMenu contextMenu = new ContextMenu();
		contextMenu.show(event.getClientX(), event.getClientY());
		
		//PopupPanel popup = new PopupPanel();
		//popup.setSize("300px", "250px");
		//popup.center();
	}
	
}
