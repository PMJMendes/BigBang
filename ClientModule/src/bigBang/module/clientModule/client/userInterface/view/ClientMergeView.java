package bigBang.module.clientModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Client;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
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
		
		searchPanel = new ClientSearchPanel();
		formOriginal = new ClientFormView();
		formReceptor = new ClientFormView();

		VerticalPanel receptorFormWrapper = new VerticalPanel();
		receptorFormWrapper.setSize("100%", "100%");
		ListHeader receptorHeader = new ListHeader();
		receptorHeader.setText("Cliente Receptor");
		Button backButton = new Button("Voltar");
		backButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onBackButtonPressed();
			}
		});
		receptorHeader.setLeftWidget(backButton);
		formReceptor.lock(true);
		receptorFormWrapper.add(receptorHeader);
		receptorFormWrapper.add(formReceptor);
		receptorFormWrapper.setCellHeight(formReceptor, "100%");

		VerticalPanel searchWrapper = new VerticalPanel();
		searchWrapper.setSize("100%", "100%");
		ListHeader searchHeader = new ListHeader();
		searchHeader.setText("Seleccionar cliente a fundir");
		searchWrapper.add(searchHeader);
		searchWrapper.add(searchPanel);
		searchWrapper.setCellHeight(searchPanel, "100%");

		VerticalPanel originalFormWrapper = new VerticalPanel();
		originalFormWrapper.setSize("100%", "100%");
		ListHeader originalHeader = new ListHeader();
		originalHeader.setText("Cliente a Fundir");
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
		originalFormWrapper.add(originalHeader);
		originalFormWrapper.add(toolbar);
		originalFormWrapper.setCellHeight(toolbar, "21px");
		originalFormWrapper.add(formOriginal);
		originalFormWrapper.setCellHeight(formOriginal, "100%");
		formOriginal.lock(true);
		
		wrapper.addWest(receptorFormWrapper, 500);
		wrapper.addWest(searchWrapper, 400);
		wrapper.add(originalFormWrapper);

		initWidget(wrapper);
	}

	public HasValue<Client> getOriginalForm(){
		return this.formOriginal;
	}
	
	public HasValue<Client> getReceptorForm(){
		return this.formReceptor;
	}
	
	public HasValueSelectables<?> getClientList(){
		return this.searchPanel;
	}

	public abstract void onBackButtonPressed();
	
	public abstract void onMergeButtonPressed();
	
	public void confirmMerge(final ResponseHandler<Boolean> handler){
		MessageBox.confirm("Confirmar Fusão de Clientes", "Esta operação transfere toda a informação do cliente seleccionado para o cliente receptor. Os dados do cliente receptor não serão substituídos. Tem certeza que pretende prosseguir?", new ConfirmationCallback() {
			
			@Override
			public void onResult(boolean result) {
				handler.onResponse(result);
			}
		});
	}
}
