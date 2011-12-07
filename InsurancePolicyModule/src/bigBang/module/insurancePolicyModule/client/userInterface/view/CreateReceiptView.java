package bigBang.module.insurancePolicyModule.client.userInterface.view;

import java.util.Collection;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
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
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.OperationsToolBar;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ReceiptImagePanel;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateReceiptViewPresenter.Action;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;

public class CreateReceiptView extends View implements CreateReceiptViewPresenter.Display {
	
	protected ReceiptImagePanel imagePanel;
	protected ReceiptForm form;
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public CreateReceiptView(){
		
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		VerticalPanel imageWrapper = new VerticalPanel();
		imageWrapper.setSize("100%", "100%");
		
		imagePanel = new ReceiptImagePanel();
		imagePanel.setSize("100%", "100%");
		imageWrapper.add(imagePanel);
		imageWrapper.setCellHeight(imagePanel, "100%");
		
		mainWrapper.addWest(imageWrapper, 600);
		
		VerticalPanel wrapper = new VerticalPanel();
		mainWrapper.add(wrapper);
		wrapper.setSize("100%", "100%");

		ListHeader receiptHeader = new ListHeader("Recibo");
		receiptHeader.setHeight("30px");
		wrapper.add(receiptHeader);
		
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
		
		initWidget(mainWrapper);
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
		receipt.categoryName = policy.categoryName;
		receipt.lineName = policy.lineName;
		receipt.subLineName = policy.subLineName;

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
