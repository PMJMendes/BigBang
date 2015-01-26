package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.ClientGroup;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ClientGroupForm extends FormView<ClientGroup> {

	protected ExpandableListBoxFormField parentGroup;
	protected TextBoxFormField name;
	protected ExpandableListBoxFormField mediator;
	
	public ClientGroupForm(){
		name = new TextBoxFormField("Nome");
		parentGroup = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CLIENT_GROUP, "Grupo pai");
		parentGroup.allowEdition(false);
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		mediator.allowEdition(false);
		
		addSection("Grupo de Clientes");
		
		addFormField(name);
		addFormField(parentGroup);
		addFormField(mediator);
		setValidator(new ClientGroupFormValidator(this));
	}
	
	@Override
	public ClientGroup getInfo() {
		ClientGroup info = value == null ? new ClientGroup() : new ClientGroup(value);
		info.name = name.getValue();
		info.parentGroupId = parentGroup.getValue();
		info.mediatorId = mediator.getValue();
		return info;
	}

	@Override
	public void setInfo(ClientGroup info) {
		if(info == null){
			clearInfo();
			for(ValueSelectable <TipifiedListItem> e : parentGroup.getListItems()) {
				ListEntry<TipifiedListItem> entry = (ListEntry<TipifiedListItem>) e;
				entry.setVisible(true);
			}
			return;
		}
		name.setValue(info.name);
		parentGroup.setValue(info.parentGroupId);
		mediator.setValue(info.mediatorId);
		for(ValueSelectable <TipifiedListItem> e : parentGroup.getListItems()) {
			ListEntry<TipifiedListItem> entry = (ListEntry<TipifiedListItem>) e;
			entry.setVisible(!e.getValue().id.equalsIgnoreCase(info.id));
		}
	}

}
