package bigBang.module.insurancePolicyModule.client.userInterface.view;

import java.util.Collection;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.OperationsToolBar;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter.Action;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;

public class CreateReceiptView extends View implements CreateReceiptViewPresenter.Display {
	
	protected ReceiptForm form;
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public CreateReceiptView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		OperationsToolBar toolbar = new OperationsToolBar();
		MenuItem createItem = new MenuItem("Guardar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateReceiptViewPresenter.Action>(Action.CREATE_RECEIPT));
			}
		});
		toolbar.addItem(createItem);
		
		wrapper.add(toolbar);
		
		form = new ReceiptForm();
		wrapper.add(form);
		
		wrapper.setCellHeight(form, "100%");
		
		initWidget(wrapper);
	}

	@Override
	public HasEditableValue<Receipt> getForm() {
		return form;
	}

	@Override
	public void setPolicyInfo(InsurancePolicy policy) {
		final Receipt receipt = form.getValue();
		receipt.policyId = policy.id;
		receipt.policyNumber = policy.number;
		ClientProcessBroker clientBroker = (ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT);
		clientBroker.getClient(policy.clientId, new ResponseHandler<Client>() {
			
			@Override
			public void onResponse(Client response) {
				receipt.clientId = response.id;
				receipt.clientName = response.name;
				receipt.clientNumber = response.clientNumber;
				form.setValue(receipt);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}
	
	@Override
	public boolean isFormValid() {
		return this.form.validate();
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}
}
