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
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class ClientMergeView extends View {

	public ClientFormView formOriginal;
	public ClientFormView formReceptor;
	public ClientSearchPanel searchPanel;

	public ClientMergeView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
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
		formOriginal = new ClientFormView();
		formReceptor = new ClientFormView();

		VerticalPanel originalFormWrapper = new VerticalPanel();
		originalFormWrapper.setSize("100%", "100%");
		ListHeader originalHeader = new ListHeader();
		originalHeader.setText("Cliente a Fundir");
		Button backButton = new Button("Voltar");
		backButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onBackButtonPressed();
			}
		});
		originalHeader.setLeftWidget(backButton);
		formReceptor.lock(true);
		originalFormWrapper.add(originalHeader);
		originalFormWrapper.add(formOriginal);
		originalFormWrapper.setCellHeight(formOriginal, "100%");

		VerticalPanel searchWrapper = new VerticalPanel();
		searchWrapper.setSize("100%", "100%");
		ListHeader searchHeader = new ListHeader();
		searchHeader.setText("Seleccionar cliente Receptor");
		searchWrapper.add(searchHeader);
		searchWrapper.add(searchPanel);
		searchWrapper.setCellHeight(searchPanel, "100%");

		VerticalPanel receptorFormWrapper = new VerticalPanel();
		receptorFormWrapper.setSize("100%", "100%");
		ListHeader receptorHeader = new ListHeader();
		receptorHeader.setText("Cliente Receptor");
		BigBangOperationsToolBar toolbar = new BigBangOperationsToolBar() {

			@Override
			public void onSaveRequest() {}

			@Override
			public void onEditRequest() {}

			@Override
			public void onCancelRequest() {}
		};
		toolbar.hideAll();
		toolbar.addItem(new MenuItem("Fundir Clientes", new Command() {

			@Override
			public void execute() {
				onMergeButtonPressed();
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
		
		initWidget(wrapper);
	}

	public HasEditableValue<Client> getOriginalForm(){
		return this.formOriginal;
	}

	public HasEditableValue<Client> getReceptorForm(){
		return this.formReceptor;
	}

	public HasValueSelectables<?> getClientList(){
		return this.searchPanel;
	}

	public abstract void onBackButtonPressed();

	public abstract void onMergeButtonPressed();

	public void confirmMerge(final ResponseHandler<Boolean> handler){
		MessageBox.confirm("Confirmar Fusão de Clientes", "Esta operação transfere toda a informação do cliente a fundir para o cliente receptor seleccionado. Os dados do cliente receptor não serão substituídos. Tem certeza que pretende prosseguir?", new ConfirmationCallback() {

			@Override
			public void onResult(boolean result) {
				handler.onResponse(result);
			}
		});
	}
}
