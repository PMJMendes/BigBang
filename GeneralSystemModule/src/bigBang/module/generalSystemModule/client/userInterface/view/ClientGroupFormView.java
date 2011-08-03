package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.shared.TipifiedListItem;

public class ClientGroupFormView extends FormView<ClientGroup> {

	protected ExpandableListBoxFormField parentGroup;
	protected TextBoxFormField name;
	
	public ClientGroupFormView(){
		name = new TextBoxFormField("Nome");
		parentGroup = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CLIENT_GROUP, "Grupo pai");
		
		addSection("Grupo de Clientes");
		
		addFormField(name);
		addFormField(parentGroup);
	}
	
	@Override
	public ClientGroup getInfo() {
		ClientGroup info = value == null ? new ClientGroup() : new ClientGroup(value);
		info.name = name.getValue();
		info.parentGroupId = parentGroup.getValue();
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
		for(ValueSelectable <TipifiedListItem> e : parentGroup.getListItems()) {
			ListEntry<TipifiedListItem> entry = (ListEntry<TipifiedListItem>) e;
			entry.setVisible(!e.getValue().id.equalsIgnoreCase(info.id));
		}
	}

}
