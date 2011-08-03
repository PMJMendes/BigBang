package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.client.userInterface.ListEntry;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class ClientGroupListEntry extends ListEntry<ClientGroup> {

	public ClientGroupListEntry(ClientGroup value) {
		super(value);
		HorizontalPanel rightWidgetWrapper = new HorizontalPanel();
		rightWidgetWrapper.setSize("100px", "100%");
		
		setHeight("40px");
		
		setInfo(value);
	}
	
	@Override
	public <I extends Object> void setInfo(I infoIn) {
		ClientGroup info = (ClientGroup) infoIn;
		if(info.id == null){
			setTitle("Novo Grupo");
			return;
		}
		setTitle(info.name);
	};

}
