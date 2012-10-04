package bigBang.module.clientModule.client.userInterface.view;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.OperationsToolBar;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeViewPresenter.Action;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientMergeView extends View implements ClientMergeViewPresenter.Display {

	private ClientForm formOriginal;
	private ClientForm formReceptor;
	private ClientSearchPanel searchPanel;
	private ActionInvokedEventHandler<Action> actionHandler;

	public ClientMergeView(){
		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		ListHeader header = new ListHeader("Fusão de Clientes");
		mainWrapper.add(header);
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		mainWrapper.add(wrapper);
		mainWrapper.setCellHeight(wrapper, "100%");
		wrapper.setSize("100%", "100%");

		searchPanel = new ClientSearchPanel(){
			@Override
			public void addClient(Client client) {
				if(!client.id.equals(formOriginal.getValue().id)){
					super.addClient(client);
				}
			}
		};
		searchPanel.setOperationId(ModuleConstants.OpTypeIDs.MERGE_INTO_THIS_CLIENT);
		searchPanel.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> vs = ((ValueSelectable<ClientStub>)event.getFirstSelected());
				if(vs != null){
					ClientStub client = vs.getValue();
					if(client != null) {
						ClientProcessBroker broker = (ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT);
						broker.getClient(client.id, new ResponseHandler<Client>() {

							@Override
							public void onResponse(Client response) {
								formReceptor.setValue(response);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
							}
						});
					}
				}
			}
		});
		formOriginal = new ClientForm();
		formReceptor = new ClientForm();

		VerticalPanel originalFormWrapper = new VerticalPanel();
		originalFormWrapper.setSize("100%", "100%");
		ListHeader originalHeader = new ListHeader();
		originalHeader.setHeight("30px");
		originalHeader.setText("Cliente a Fundir");
		
		formReceptor.lock(true);
		originalFormWrapper.add(originalHeader);
		originalFormWrapper.add(formOriginal);
		originalFormWrapper.setCellHeight(formOriginal, "100%");

		VerticalPanel searchWrapper = new VerticalPanel();
		searchWrapper.setSize("100%", "100%");
		ListHeader searchHeader = new ListHeader();
		searchHeader.setText("Seleccionar cliente Receptor");
		searchHeader.setHeight("30px");
		searchWrapper.add(searchHeader);
		searchWrapper.add(searchPanel);
		searchWrapper.setCellHeight(searchPanel, "100%");

		VerticalPanel receptorFormWrapper = new VerticalPanel();
		receptorFormWrapper.setSize("100%", "100%");
		ListHeader receptorHeader = new ListHeader();
		receptorHeader.setText("Cliente Receptor");
		receptorHeader.setHeight("30px");
		OperationsToolBar toolbar = new OperationsToolBar();
		toolbar.addItem(new MenuItem("Fundir Clientes", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientMergeViewPresenter.Action>(Action.MERGE));
			}
		}));
		toolbar.addItem(new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientMergeViewPresenter.Action>(Action.CANCEL));
			}
		}));
		receptorFormWrapper.add(receptorHeader);
		receptorFormWrapper.add(toolbar);
		receptorFormWrapper.setCellHeight(toolbar, "21px");
		receptorFormWrapper.add(formReceptor);
		receptorFormWrapper.setCellHeight(formReceptor, "100%");
		formReceptor.lock(true);
		formOriginal.lock(true);

		wrapper.addWest(originalFormWrapper, 500);
		wrapper.addWest(searchWrapper, 400);
		wrapper.add(receptorFormWrapper);

		formOriginal.setReadOnly(true);
		formReceptor.setReadOnly(true);
		
		searchPanel.doSearch();
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Client> getSourceClientForm() {
		return this.formOriginal;
	}


	@Override
	public HasEditableValue<Client> getTargetClientForm() {
		return this.formReceptor;
	}


	@Override
	public HasValueSelectables<ClientStub> getList() {
		return this.searchPanel;
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionHandler) {
		this.actionHandler = actionHandler;
	}

	
	public void confirmMerge(final ResponseHandler<Boolean> handler){
		MessageBox.confirm("", "Esta operação transfere toda a informação do cliente a fundir para o cliente receptor seleccionado. Os dados do cliente receptor não serão substituídos. Tem certeza que pretende prosseguir?", new ConfirmationCallback() {

			@Override
			public void onResult(boolean result) {
				handler.onResponse(result);
			}
		});
	}

}
