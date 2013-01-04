package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientGroupBroker;
import bigBang.definitions.client.dataAccess.ClientGroupDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ClientGroup;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NavigationRequestEvent;
import bigBang.library.client.event.NavigationRequestEventHandler;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ClientGroupList extends FilterableList<ClientGroup> implements ClientGroupDataBrokerClient {

	public static class Entry extends ListEntry<ClientGroup>{

		public Entry(ClientGroup value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			ClientGroup g = (ClientGroup) info;
			setText(g.name);
		};
	}


	protected int clientGroupDataVersion;
	protected ClientGroupBroker clientGroupBroker;
	protected ListHeader header;

	public ClientGroupList(){
		this(null);
	}

	public ClientGroupList(ClientGroup currentGroup) {
		super();
		header = new ListHeader("Grupos de Clientes");

		header.showNewButton("Novo");
		header.showRefreshButton();
		setHeaderWidget(header);
		onSizeChanged();
		showFilterField(false);
		
		this.clientGroupBroker = (ClientGroupBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT_GROUP);
		this.clientGroupBroker.registerClient(this);
	}

	@Override
	protected void onSizeChanged(){
		int size = this.size();
		String text;
		switch(size){
		case 0:
			text = "Sem Grupos";
			break;
		case 1:
			text = "1 Grupo";
			break;
		default:
			text = size + " Grupos";
			break;
		}

		setFooterText(text);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equals(BigBangConstants.EntityIds.CLIENT_GROUP)){
			this.clientGroupDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equals(BigBangConstants.EntityIds.CLIENT_GROUP)){
			return this.clientGroupDataVersion;
		}
		return -1;
	}

	@Override
	public void setGroups(ClientGroup[] clientGroups) {
		this.clear();
		for(int i = 0; i < clientGroups.length; i++) {
			add(new ClientGroupListEntry(clientGroups[i]));
		}
	}

	@Override
	public void addGroup(ClientGroup clientGroup) {
		ClientGroupListEntry entry = new ClientGroupListEntry(clientGroup);
		add(0, entry);
	}

	@Override
	protected HandlerRegistration bindEntry(ListEntry<ClientGroup> e) {
		e.addHandler(new NavigationRequestEventHandler() {

			@Override
			public void onNavigationEvent(NavigationRequestEvent event) {

			}
		}, NavigationRequestEvent.TYPE);
		return super.bindEntry(e);

	}

	@Override
	public void updateGroup(ClientGroup clientGroup) {
		for(ValueSelectable<ClientGroup> s : this) {
			if(s.getValue().id.equals(clientGroup.id)){
				s.setValue(clientGroup, true);
			}
		}
	}

	@Override
	public void removeGroup(String clientGroupId) {
		for(ValueSelectable<ClientGroup> s : this) {
			if(s.getValue().id.equals(clientGroupId)){
				remove(s);
				break;
			}
		}
	}

	/**
	 * Gets a reference to the 'new' button
	 * @return The reference to the 'new' button
	 */
	public HasClickHandlers getNewButton(){
		return header.getNewButton();
	}

	/**
	 * Gets a reference to the 'refresh' button
	 * @return The reference to the 'refresh' button
	 */
	public HasClickHandlers getRefreshButton(){
		return header.getRefreshButton();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		clientGroupBroker.requireDataRefresh();
		clientGroupBroker.getClientGroups(new ResponseHandler<ClientGroup[]>() {
			
			@Override
			public void onResponse(ClientGroup[] response) {
				return;
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
}
