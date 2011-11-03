package bigBang.module.clientModule.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.User;
import bigBang.library.client.Checkable;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ValueWrapper;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.CheckedSelectionChangedEvent;
import bigBang.library.client.event.CheckedSelectionChangedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanelListEntry;
import bigBang.module.clientModule.client.userInterface.presenter.ClientManagerTransferOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientManagerTransferOperationViewPresenter.Action;
import bigBang.module.clientModule.shared.operation.ClientManagerTransferOperation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientManagerTransferOperationView extends View implements ClientManagerTransferOperationViewPresenter.Display {

	protected static class SelectedList extends FilterableList<ClientStub> {

		public void addEntry(ClientStub client){
			ListEntry<ClientStub> entry = new ClientSearchPanelListEntry(new ValueWrapper<ClientStub>(client));
			entry.setChecked(true, false);
			this.add(entry);		
		}
	}

	protected abstract class CheckableClientSearchPanel extends ClientSearchPanel{

		@Override
		protected ClientSearchPanelListEntry addSearchResult(SearchResult r) {
			ClientSearchPanelListEntry entry = super.addSearchResult(r);
			entry.setChecked(isClientSelected(entry.getValue()), false);
			return entry;
		}

		protected abstract boolean isClientSelected(ClientStub client); 
	}

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX

	protected CheckableClientSearchPanel searchPanel;
	protected SelectedList selectedList; 
	protected ClientManagerFormView managerForm;
	protected ClientFormView clientForm;
	protected BigBangOperationsToolBar toolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;

	public ClientManagerTransferOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");

		this.searchPanel = new CheckableClientSearchPanel(){

			@Override
			protected boolean isClientSelected(ClientStub client) {
				for(ValueSelectable<ClientStub> c : ClientManagerTransferOperationView.this.selectedList) {
					if(c.getValue().id.equalsIgnoreCase(client.id))
						return true;
				}
				return false;
			}
		};
		this.searchPanel.setOperationId(ClientManagerTransferOperation.ID);
		this.searchPanel.addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable check = event.getChangedCheckable();
				if(check.isChecked()){
					selectedList.addEntry(((HasValue<ClientStub>)check).getValue());
				}else{
					for(ValueSelectable<ClientStub> c : ClientManagerTransferOperationView.this.selectedList) {
						if(c.getValue().id.equalsIgnoreCase(((HasValue<ClientStub>)check).getValue().id)){
							selectedList.remove(c);
							break;
						}
					}
				}
			}
		});

		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		this.searchPanel.setCheckable(true);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		this.managerForm = new ClientManagerFormView();
		this.clientForm = new ClientFormView();
		this.clientForm.lock(true);
		this.selectedList = new SelectedList();
		this.selectedList.addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				ListEntry<ClientStub> checkable = (ListEntry<ClientStub>) event.getChangedCheckable();
				if(!checkable.isChecked()){
					for(ListEntry<ClientStub> s : searchPanel){
						if(s.getValue().id.equalsIgnoreCase(checkable.getValue().id)){
							s.setChecked(false, true);
							if(selectedList.contains(checkable)){
								selectedList.remove(checkable);
							}
							break;
						}
					}
					selectedList.remove(checkable);
				}
			}
		});
		this.selectedList.setCheckable(true);
		this.selectedList.showFilterField(false);
		ListHeader selectedListHeader = new ListHeader("Clientes a Transferir");
		this.selectedList.setHeaderWidget(selectedListHeader);

		toolbar = new BigBangOperationsToolBar() {

			@Override
			public void onSaveRequest() {}

			@Override
			public void onEditRequest() {}

			@Override
			public void onCancelRequest() {}
		};
		toolbar.hideAll();
		toolbar.addItem(new MenuItem("Transferir", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientManagerTransferOperationViewPresenter.Action>(Action.TRANSFER));
			}
		}));

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");
		formWrapper.add(this.managerForm);
		formWrapper.setCellHeight(this.managerForm, "100%");

		contentWrapper.addNorth(formWrapper, 250);

		SplitLayoutPanel clientWrapper = new SplitLayoutPanel();
		clientWrapper.setSize("100%", "100%");

		clientWrapper.addWest(this.selectedList, 350);

		VerticalPanel clientFormWrapper = new VerticalPanel();
		clientFormWrapper.setSize("100%", "100%");

		clientFormWrapper.add(this.clientForm);
		clientWrapper.add(clientFormWrapper);
		contentWrapper.add(clientWrapper);
		mainWrapper.add(contentWrapper);

		initWidget(mainWrapper);
	}

	@Override
	public void clear() {
		this.managerForm.clearInfo();
		this.searchPanel.clearSelection();
		for(Checkable c : this.searchPanel) {
			c.setChecked(false, false);
		}
		this.clientForm.clearInfo();
		this.selectedList.clear();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.managerForm.setReadOnly(readOnly);
		this.clientForm.setReadOnly(readOnly);
		this.toolbar.setEditionAvailable(!readOnly);
		this.searchPanel.setCheckable(false);
	}

	@Override
	public HasEditableValue<String> getForm() {
		return this.managerForm;
	}

	@Override
	public boolean isFormValid() {
		return this.managerForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.managerForm.lock(lock);
	}

	@Override
	public Collection<ClientStub> getSelectedClientStubs() {
		Collection<ClientStub> result = new ArrayList<ClientStub>();
		for(ValueSelectable<ClientStub> c : this.selectedList){
			result.add(c.getValue());
		}
		return result;
	}

	@Override
	public HasValueSelectables<?> getList() {
		return this.searchPanel;
	}

	@Override
	public HasValueSelectables<?> getSelectedList() {
		return this.selectedList;
	}

	@Override
	public HasValue<Client> getClientForm() {
		return this.clientForm;
	}

	@Override
	public void showMessage(String string) {
		MessageBox.info("", string);
	}

}
