package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceAgencyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.HasNavigationStateChangedHandlers;
import bigBang.library.client.event.NavigationStateChangedEvent;
import bigBang.library.client.event.NavigationStateChangedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.shared.DocuShareItem;
import bigBang.module.receiptModule.client.userInterface.view.PolicyChoiceFromListView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptChoiceFromListView;
import bigBang.module.receiptModule.shared.ReceiptPolicyWrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SerialReceiptCreationViewPresenter implements ViewPresenter{

	public enum Action{
		VERIFY_RECEIPT,
		VERIFY_POLICY,
		MARK_RECEIPT,
		SAVE,
		CANCEL, NO_IMAGE, IMAGE, NEW_RECEIPT, CHANGED_RECEIPT_NUMBER, CHANGED_POLICY_NUMBER
	}

	private Display view;
	private boolean bound = false;
	private InsurancePolicyBroker policyBroker;
	private ReceiptDataBroker receiptBroker;
	private ReceiptChoiceFromListViewPresenter receiptPresenter;
	private InsuranceAgencyBroker agencyBroker;
	private ReceiptChoiceFromListView receiptView;
	private boolean editing = false;

	private PopupPanel popup;
	private ReceiptPolicyWrapper receiptPolicyWrapper;
	private PopupPanel popupPolicy;
	private PolicyChoiceFromListView policyView;
	private PolicyChoiceFromListViewPresenter policyPresenter;
	private boolean editingPolicy = false;
	private boolean hasReceiptFile = true;

	public interface Display{

		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		HasNavigationStateChangedHandlers getNavigationPanel();

		HasEditableValue<ReceiptPolicyWrapper> getForm();

		void enableReceiptNumber(boolean enable);

		void clear();

		void lockReceiptView(boolean b);

		String getReceiptNumber();

		void enableNewReceipt(boolean enable);

		void setReceiptNumber(String id, boolean keepCursorPos);

		void hideMarkAsEnable(boolean b);

		void enableToolbar(boolean b);

		void setReceiptReadOnly(boolean b);

		void enablePolicy(boolean b);

		void setFocusOnPolicy();

		void setFocusOnReceipt();

		DocuShareItem getSelectedDocuShareItem();

		void removeDocuShareItem(DocuShareHandle handle);

		void panelNavigateBack();

		void markReceipt(DocuShareItem currentItem);

		String getPolicyNumber();

		void clearPolicy();

		void setPolicyNumber(String policyNumber, boolean keepCursorPos);

		void enableMarkReceipt(boolean b);

		void showImageAlreadyDefinedWarning(boolean hasImage);

	}

	public SerialReceiptCreationViewPresenter(Display view){
		agencyBroker = (InsuranceAgencyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_AGENCY);
		receiptBroker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
		policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((UIObject) view);

		popup = new PopupPanel();

		receiptView = (ReceiptChoiceFromListView)GWT.create(ReceiptChoiceFromListView.class);
		receiptPresenter = new ReceiptChoiceFromListViewPresenter(receiptView){
			@Override
			public void getSelectedReceipt() {
				SerialReceiptCreationViewPresenter.this.getReceipt(receiptView.getForm().getInfo().id);
			}

			@Override
			protected void onCancel() {
				SerialReceiptCreationViewPresenter.this.view.enableNewReceipt(true);
				popup.hidePopup();

			}
			@Override
			public void setReceipts(Collection<ReceiptStub> stubs) {
				receiptView.fillList(stubs);				
			}

			@Override
			protected void onNewReceipt() {
				newReceipt();
			}
		};

		receiptPresenter.go(popup);

		popupPolicy = new PopupPanel();

		policyView = (PolicyChoiceFromListView) GWT.create(PolicyChoiceFromListView.class);
		policyPresenter = new PolicyChoiceFromListViewPresenter(policyView){

			@Override
			protected void onCancel() {
				SerialReceiptCreationViewPresenter.this.view.enableMarkReceipt(true);
				popupPolicy.hidePopup();
			}

			@Override
			public void setInsurancePolicys(
					Collection<InsurancePolicyStub> stubs) {
				policyView.fillList(stubs);

			}

			@Override
			public void getSelectedInsurancePolicy() {
				SerialReceiptCreationViewPresenter.this.getPolicy(policyView.getForm().getValue().id);
			}

			@Override
			protected void onMark() {
				SerialReceiptCreationViewPresenter.this.onMarkReceipt();
			}


		};

		policyPresenter.go(popupPolicy);
	}



	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {



			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case MARK_RECEIPT:
					onMarkReceipt();
					break;
				case SAVE:
					onSave();
					break;
				case VERIFY_POLICY:
					onVerifyPolicy();
					break;
				case VERIFY_RECEIPT:
					onVerifyReceipt();
					break;
				case IMAGE:
					onHasImage();
					break;
				case NO_IMAGE:
					onNoImage();
					break;
				case NEW_RECEIPT:
					newReceipt();
					break;
				case CHANGED_RECEIPT_NUMBER:
					changedReceiptNumber();
					break;
				case CHANGED_POLICY_NUMBER:
					changedPolicyNumber();
					break;
				}
			}

		});

		view.getNavigationPanel().registerNavigationStateChangedHandler(new NavigationStateChangedEventHandler() {

			@Override
			public void onNavigationStateChanged(NavigationStateChangedEvent event) {
				if(event.getObject() instanceof ImageHandlerPanel){
					view.enableReceiptNumber(true);
					view.setFocusOnReceipt();
				}
				else{
					view.clear();
					view.setReceiptReadOnly(true);
					view.enableReceiptNumber(false);
				}
			}
		});

		bound = true;
	}

	protected void changedPolicyNumber() {
		if(editingPolicy){
			receiptPolicyWrapper = new ReceiptPolicyWrapper();
			String tempPolicy = view.getPolicyNumber();
			view.clearPolicy();
			view.setPolicyNumber(tempPolicy, true);
			editingPolicy = false;
		}
	}

	protected void changedReceiptNumber() {
		if(editing){
			receiptPolicyWrapper = new ReceiptPolicyWrapper();
			String tempReceipt = view.getReceiptNumber();
			view.clear();
			view.setReceiptNumber(tempReceipt, true);
			editing = false;
		}
	}

	protected void newReceipt() {

		receiptPolicyWrapper.receipt = null;
		String tempReceiptNumber = view.getReceiptNumber();
		view.clear();
		view.setReceiptNumber(tempReceiptNumber, false);
		view.enableNewReceipt(true);
		view.enablePolicy(true);
		view.setFocusOnPolicy();
		if(popup.isAttached()){
			popup.hidePopup();
		}
	}

	protected void onNoImage() {
		hasReceiptFile = false;
		view.clear();
		view.enableReceiptNumber(true);
		view.lockReceiptView(true);
		view.hideMarkAsEnable(true);
		view.setFocusOnReceipt();
		policyPresenter.enableMarkReceipt(false);
	}

	protected void onHasImage() {
		hasReceiptFile = true;
		view.clear();
		view.enableReceiptNumber(false);
		view.lockReceiptView(false);
		view.hideMarkAsEnable(false);
		policyPresenter.enableMarkReceipt(true);
	}

	protected void onVerifyReceipt() {
		editing = true;
		view.setFocusOnPolicy();
		final String receiptId = view.getReceiptNumber();

		if(receiptId.length() == 0){return;}

		view.clear();
		view.setReceiptNumber(receiptId, true); 

		receiptBroker.getReceiptsWithNumber(receiptId, new ResponseHandler<Collection<ReceiptStub>>() {

			@Override
			public void onResponse(Collection<ReceiptStub> response) {

				if(response.size() > 1){
					receiptPresenter.setParameters(null);
					receiptPresenter.fillList(response);
					popup.center();
				}
				else if(response.size() == 1){
					getReceipt(((ReceiptStub)response.toArray()[0]).id);
					view.enableNewReceipt(true);
				}
				else{
					view.enablePolicy(true);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os recibos."), TYPE.ALERT_NOTIFICATION));				
			}
		});
	}

	protected void getReceipt(String id) {
		receiptPolicyWrapper = new ReceiptPolicyWrapper();
		receiptBroker.getReceipt(id, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				receiptPolicyWrapper.receipt = response;
				
				boolean hasImage = !PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ReceiptProcess.SET_ORIGINAL_IMAGE);
				view.setReceiptReadOnly(hasImage);
				view.showImageAlreadyDefinedWarning(hasImage);
				
				getPolicy(response.policyId);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o recibo."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void getPolicy(String id) {

		policyBroker.getPolicy(id, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {

				if(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT)){

					if(receiptPolicyWrapper.receipt == null){
						receiptPolicyWrapper.receipt = new Receipt();
						receiptPolicyWrapper.receipt.number = view.getReceiptNumber();
					}
					receiptPolicyWrapper.policy = response;

					agencyBroker.getInsuranceAgencies(new ResponseHandler<InsuranceAgency[]>() {

						@Override
						public void onResponse(InsuranceAgency[] response) {
							agencyBroker.getInsuranceAgency(receiptPolicyWrapper.policy.insuranceAgencyId, new ResponseHandler<InsuranceAgency>() {

								@Override
								public void onResponse(InsuranceAgency response) {
									receiptPolicyWrapper.insuranceAgencyName = response.name;
									view.getForm().setValue(receiptPolicyWrapper);
									view.enableToolbar(true);
									view.setReceiptReadOnly(false);
									if(popup.isAttached())
										popup.hidePopup();

									if(popupPolicy.isAttached())
										popupPolicy.hidePopup();
								}

								@Override
								public void onError(Collection<ResponseError> errors) {
									EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o nome da seguradora."), TYPE.ALERT_NOTIFICATION));
									view.getForm().setValue(receiptPolicyWrapper);
									view.enableToolbar(true);
									view.setReceiptReadOnly(false);
									if(popup.isAttached())
										popup.hidePopup();

									if(popupPolicy.isAttached())
										popupPolicy.hidePopup();
								}
							});
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o nome da seguradora."), TYPE.ALERT_NOTIFICATION));
							view.getForm().setValue(receiptPolicyWrapper);
							view.enableToolbar(true);
							view.setReceiptReadOnly(false);
							if(popupPolicy.isAttached())
								popupPolicy.hidePopup();

							if(popupPolicy.isAttached())
								popupPolicy.hidePopup();
						}
					});

				} else {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar Recibos para a Apólice Especificada"), TYPE.ALERT_NOTIFICATION));
					view.enableMarkReceipt(true);
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice."), TYPE.ALERT_NOTIFICATION));
				view.enableMarkReceipt(true);
			}
		});

	}


	protected void onVerifyPolicy() {
		editingPolicy = true;
		final String policyNumber = view.getPolicyNumber();
		view.clearPolicy();
		view.enableMarkReceipt(false);
		view.setPolicyNumber(policyNumber, true);

		policyBroker.getInsurancePoliciesWithNumber(policyNumber, new ResponseHandler<Collection<InsurancePolicyStub>>() {

			@Override
			public void onResponse(Collection<InsurancePolicyStub> response) {
				if(response.size() > 1){
					policyPresenter.setParameters(null);
					policyPresenter.fillList(response);
					popupPolicy.center();
				}
				else if(response.size() == 1){
					getPolicy(((InsurancePolicyStub)response.toArray()[0]).id);
				}
				else{
					view.enableMarkReceipt(true);
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter as apólices."), TYPE.ALERT_NOTIFICATION));				
			}
		});

	}

	protected void onSave() {
		if(view.getForm().validate()) {
			ReceiptPolicyWrapper toSend = view.getForm().getInfo();
			final DocuShareHandle handle = new DocuShareHandle();

			if(hasReceiptFile){
				handle.handle = view.getSelectedDocuShareItem().handle;
			}

			if(toSend.receipt.id == null){
				receiptBroker.serialCreateReceipt(toSend.receipt, handle.handle != null ? handle : null, new ResponseHandler<Receipt>() {

					@Override
					public void onResponse(Receipt response) {
						editing = false;
						editingPolicy = false;
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recibo criado com sucesso."), TYPE.TRAY_NOTIFICATION));
						if(hasReceiptFile){
							view.removeDocuShareItem(handle);
							view.panelNavigateBack();
							receiptPolicyWrapper = new ReceiptPolicyWrapper();
						}
						view.clear();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o recibo."), TYPE.ALERT_NOTIFICATION));				
					}
				});
			}else{
				receiptBroker.receiveImage(toSend.receipt.id, handle.handle != null ? handle : null, new ResponseHandler<Receipt>() {

					@Override
					public void onResponse(Receipt response) {
						editing = false;
						editingPolicy = false;
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recibo gravado com sucesso."), TYPE.TRAY_NOTIFICATION));
						view.clear();
						if(hasReceiptFile){
							view.removeDocuShareItem(handle);
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar o recibo."), TYPE.ALERT_NOTIFICATION));				
					}
				});
			}
		}
	}


	protected void onMarkReceipt() {

		DocuShareItem currentItem = view.getSelectedDocuShareItem();
		view.enableReceiptNumber(false);
		view.markReceipt(currentItem);
		view.enableMarkReceipt(false);
		view.clear();
		if(popupPolicy.isAttached()){
			popupPolicy.hidePopup();
		}

	}

	protected void onCancel() {

		view.clear();

	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		receiptPolicyWrapper = new ReceiptPolicyWrapper();
		view.clear();
	}
}
