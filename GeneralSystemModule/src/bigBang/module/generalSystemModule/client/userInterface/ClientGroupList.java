package bigBang.module.generalSystemModule.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.ClientGroupBroker;
import bigBang.definitions.client.brokerClient.ClientGroupDataBrokerClient;
import bigBang.definitions.client.types.ClientGroup;
import bigBang.definitions.client.types.CostCenter;
import bigBang.library.client.HasNavigationHandlers;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NavigationEventHandler;
import bigBang.library.client.response.ResponseError;
import bigBang.library.client.response.ResponseHandler;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListItem;

public class ClientGroupList extends FilterableList<ClientGroup> implements
		ClientGroupDataBrokerClient, HasNavigationHandlers {

	public static class ClientGroupListEntry extends ListEntry<ClientGroup> {

		public ClientGroupListEntry(ClientGroup group) {
			super(group);
			setHeight("40px");
			HorizontalPanel labelWrapper = new HorizontalPanel();
			labelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			labelWrapper.setSize("150px", "100%");
			setRightWidget(labelWrapper);
			
			setInfo(group);
		}
		
		@Override
		public <I extends Object> void setInfo(I infoGeneric){
			ClientGroup info	= (ClientGroup) infoGeneric;
			
			if(info.id == null) {
				setTitle("Novo Centro de Custo");
				return;
			}

			setTitle(info.name);
		}
		
	}
	
	
	protected int clientGroupDataVersion;
	protected ClientGroupBroker clientGroupBroker;
	protected ListHeader header;
	
	protected ClientGroup currentGroup;

	public ClientGroupList(){
		super();
		header = new ListHeader();
		header.setText("Grupos de Clientes");
		header.showNewButton("Novo");
		header.showRefreshButton();
		setHeaderWidget(header);
		onSizeChanged();
		
		this.clientGroupBroker = (ClientGroupBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT_GROUP);
		this.clientGroupBroker.registerClient(this);
		/*this.clientGroupBroker.registerClient(this);
		this.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(!event.isAttached()){
					clientGroupBroker.unregisterClient(ClientGroupList.this);
				}
			}
		});
		
		this.clientGroupBroker.getClientGroup(currentGroupId, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				currentGroup = response;
				showGroupDetails();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});*/
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
	
	protected void showGroupDetails(){
		ArrayList<ClientGroup> subGroups = new ArrayList<ClientGroup>();
		
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
			addGroup(clientGroups[i]);
		}
	}

	@Override
	public void addGroup(ClientGroup clientGroup) {
		add(new ClientGroupListEntry(clientGroup));
	}

	@Override
	public void updateGroup(ClientGroup clientGroup) {
		for(ValueSelectable<ClientGroup> s : this) {
			if(s.getValue().id.equals(clientGroup.id)){
				s.setValue(clientGroup);
				break;
			}
		}
	}

	@Override
	public void removeGroup(String clientGroupId) {
		for(ValueSelectable<ClientGroup> s : this) {
			if(s.getValue().id.equals(clientGroupId)){
				//remove(s);
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
	public HandlerRegistration addNavigationHandler(
			NavigationEventHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

}
