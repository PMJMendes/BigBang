package bigBang.module.clientModule.client.userInterface;

import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.userInterface.ListEntry;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientSearchPanelListEntry extends ListEntry<ClientStub> {

	protected Label numberLabel;
	protected Label nameLabel;
	protected Label groupLabel;
	
	protected boolean initialized = false;
	
	public ClientSearchPanelListEntry(ClientStub client) {
		super(client);
		this.setHeight("55px");
		this.setSelected(isSelected(), false);
	}

	public <I extends Object> void setInfo(I info) {
		ClientStub value = (ClientStub)info;
		if(!this.initialized){
			initialize();
		}
		
		if(value.id != null){
			numberLabel.setText("#" + value.clientNumber);
			nameLabel.setText(value.name);
			groupLabel.setText(value.groupName == null ? "-" : value.groupName);
			setMetaData(new String[]{
				value.name,
				value.groupName,
				value.clientNumber
			});
		}else{
			numberLabel.setText("");
			nameLabel.setText("Novo Cliente");
			groupLabel.setText("");
			setMetaData(new String[0]);
		}
	};
	
	protected void initialize(){
		this.numberLabel = getFormatedLabel();
		this.numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
		this.numberLabel.setWordWrap(false);
		this.nameLabel = getFormatedLabel();
		this.nameLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		this.nameLabel.getElement().getStyle().setProperty("whiteSpace", "");
		this.nameLabel.setHeight("1.2em");
		this.groupLabel = getFormatedLabel();
		this.groupLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		
		VerticalPanel container = new VerticalPanel();
		container.setSize("100%", "100%");
		container.add(this.numberLabel);
		container.add(this.nameLabel);
		container.add(this.groupLabel);
		this.setWidget(container);
		this.initialized = true;
	}

	@Override
	public void setSelected(boolean selected, boolean fireEvents) {
		super.setSelected(selected, fireEvents);
		if(!this.initialized){return;}
		if(selected){
			this.groupLabel.getElement().getStyle().setColor("white");
		}else{
			this.groupLabel.getElement().getStyle().setColor("gray");
		}
	}
	
}
