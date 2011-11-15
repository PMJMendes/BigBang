package bigBang.module.clientModule.client.userInterface;

import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.userInterface.SearchPanelListEntry;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.user.client.ui.Label;

public class ClientSearchPanelListEntry extends SearchPanelListEntry<ClientStub> {

	public ClientSearchPanelListEntry(ClientStub client) {
		super(client);
		this.setHeight("40px");
	}

	public <I extends Object> void setInfo(I info) {
		ClientStub value = (ClientStub)info;
		if(value.id != null){
			Label clientNumberLabel = new Label(value.clientNumber);
			clientNumberLabel.setWidth("40px");
			setLeftWidget(clientNumberLabel);
			setTitle(value.name);
			setText(value.groupName);
			this.textLabel.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
		}else{
			setTitle("Novo Cliente");
		}
	};

}
