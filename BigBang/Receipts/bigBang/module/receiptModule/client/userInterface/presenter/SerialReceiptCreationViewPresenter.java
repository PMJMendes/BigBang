package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;
import java.util.UUID;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.ScanHandle;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
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
import bigBang.library.shared.ScanItem;
import bigBang.module.receiptModule.client.userInterface.view.PolicyChoiceFromListView;
import bigBang.module.receiptModule.client.userInterface.view.ReceiptChoiceFromListView;
import bigBang.module.receiptModule.client.userInterface.view.SubPolicyChoiceFromListView;
import bigBang.module.receiptModule.shared.ReceiptOwnerWrapper;

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
		CANCEL, NO_IMAGE, IMAGE, NEW_RECEIPT, CHANGED_RECEIPT_NUMBER,
		CHANGED_POLICY_NUMBER,
		CHANGED_OWNER_TYPE,
		CHANGED_SUB_POLICY,
		CHANGED_SUB_CASUALTY
	}

	private Display view;
	private boolean bound = false;
	private InsurancePolicyBroker policyBroker;
	private InsuranceSubPolicyBroker subPolicyBroker;
	private SubCasualtyDataBroker subCasualtyBroker;
	private ReceiptDataBroker receiptBroker;
	private ReceiptChoiceFromListViewPresenter receiptPresenter;
	private ReceiptChoiceFromListView receiptView;
	private boolean editing = false;

	private PopupPanel popup;
	private ReceiptOwnerWrapper receiptOwnerWrapper;
	private PopupPanel popupPolicy;
	private PolicyChoiceFromListView policyView;
	private PolicyChoiceFromListViewPresenter policyPresenter;
	private PopupPanel popupSubPolicy;
	private SubPolicyChoiceFromListView subPolicyView;
	private SubPolicyChoiceFromListViewPresenter subPolicyPresenter;
	private boolean editingOwner = false;
	private boolean hasReceiptFile = true;
	private String subPolicyId;
	private String subCasualtyId;

	public interface Display{

		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		HasNavigationStateChangedHandlers getNavigationPanel();

		HasEditableValue<ReceiptOwnerWrapper> getForm();

		void enableReceiptNumber(boolean enable);

		void clear();

		void lockReceiptView(boolean b);

		String getReceiptNumber();

		void enableNewReceipt(boolean enable);

		void setReceiptNumber(String id, boolean keepCursorPos);

		void hideMarkAsEnable(boolean b);

		void enableToolbar(boolean b);

		void setReceiptReadOnly(boolean b);

		void enableOwner(boolean b);

		void setFocusOnPolicy();

		void setFocusOnReceipt();

		ScanItem getSelectedScanItem();

		void removeScanItem(ScanHandle handle);

		void panelNavigateBack();

		void markReceipt(ScanItem currentItem);

		void clearOwner();

		String getPolicyNumber();

		String getSubPolicyId();

		String getSubCasualtyId();

		void setPolicyNumber(String policyNumber, boolean keepCursorPos);

		void enableMarkReceipt(boolean b);

		void showImageAlreadyDefinedWarning(boolean hasImage);

		void enableOwnerProblem(boolean b);

		void setOwnerNotAvailable(boolean b);
	}

	public SerialReceiptCreationViewPresenter(Display view){
		receiptBroker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
		policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		subCasualtyBroker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
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

		popupSubPolicy = new PopupPanel();

		subPolicyView = (SubPolicyChoiceFromListView) GWT.create(SubPolicyChoiceFromListView.class);
		subPolicyPresenter = new SubPolicyChoiceFromListViewPresenter(subPolicyView){

			@Override
			protected void onCancel() {
				SerialReceiptCreationViewPresenter.this.view.enableMarkReceipt(true);
				popupSubPolicy.hidePopup();
			}

			@Override
			public void setSubPolicies(Collection<SubPolicyStub> stubs) {
				subPolicyView.fillList(stubs);

			}

			@Override
			public void getSelectedSubPolicy() {
				SerialReceiptCreationViewPresenter.this.getSubPolicy(subPolicyView.getForm().getValue().id);
			}

			@Override
			protected void onMark() {
				SerialReceiptCreationViewPresenter.this.onMarkReceipt();
			}
		};

		subPolicyPresenter.go(popupSubPolicy);
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
				case CHANGED_OWNER_TYPE:
					changedOwnerType();
					break;
				case CHANGED_SUB_CASUALTY:
					changedSubCasualty();
					break;
				case CHANGED_SUB_POLICY:
					changedSubPolicy();
					break;
				default:
					break;
				}
			}

		});

		view.getNavigationPanel().registerNavigationStateChangedHandler(new NavigationStateChangedEventHandler() {

			@Override
			public void onNavigationStateChanged(NavigationStateChangedEvent event) {
				if(event.getObject() instanceof ImageHandlerPanel) {
					view.enableReceiptNumber(true);
					view.setFocusOnReceipt();
				}
				else {
					view.clear();
					view.setReceiptReadOnly(true);
					view.enableReceiptNumber(false);
				}
			}
		});

		bound = true;
	}

	protected void changedSubPolicy() {
		if ( editingOwner && (subPolicyId != null) && subPolicyId.equals(view.getSubPolicyId()) )
			return;

		editingOwner = true;
		subPolicyId = view.getSubPolicyId();
		view.clearOwner();
		view.enableMarkReceipt(false);
		view.enableOwnerProblem(false);
		view.setOwnerNotAvailable(false);
		if ( subPolicyId != null )
			getSubCasualty(subPolicyId);
	}

	protected void changedSubCasualty() {
		if ( editingOwner && (subCasualtyId != null) && subCasualtyId.equals(view.getSubCasualtyId()) )
			return;

		editingOwner = true;
		subCasualtyId = view.getSubCasualtyId();
		view.clearOwner();
		view.enableMarkReceipt(false);
		view.enableOwnerProblem(false);
		view.setOwnerNotAvailable(false);
		if ( subCasualtyId != null )
			getSubCasualty(subCasualtyId);
	}

	protected void changedOwnerType() {
		if(editingOwner){
			receiptOwnerWrapper = new ReceiptOwnerWrapper();
			view.clearOwner();
			view.enableMarkReceipt(false);
			view.enableOwnerProblem(false);
			view.setOwnerNotAvailable(false);
			editingOwner = false;
		}
	}

	protected void changedPolicyNumber() {
		if(editingOwner){
			receiptOwnerWrapper = new ReceiptOwnerWrapper();
			String tempOwner = view.getPolicyNumber();
			view.clearOwner();
			view.setPolicyNumber(tempOwner, true);
			editingOwner = false;
		}
	}

	protected void changedReceiptNumber() {
		if(editing){
			receiptOwnerWrapper = new ReceiptOwnerWrapper();
			String tempReceipt = view.getReceiptNumber();
			view.clear();
			view.setReceiptNumber(tempReceipt, true);
			editing = false;
		}
	}

	protected void newReceipt() {

		receiptOwnerWrapper.receipt = null;
		String tempReceiptNumber = view.getReceiptNumber();
		view.clear();
		view.setReceiptNumber(tempReceiptNumber, false);
		view.enableNewReceipt(true);
		view.enableOwner(true);
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
		view.enableOwnerProblem(false);
		view.setOwnerNotAvailable(false);
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
					view.enableOwner(true);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os recibos."), TYPE.ALERT_NOTIFICATION));				
			}
		});
	}

	protected void getReceipt(String id) {
		receiptOwnerWrapper = new ReceiptOwnerWrapper();
		receiptBroker.getReceipt(id, new ResponseHandler<Receipt>() {
			@Override
			public void onResponse(Receipt response) {
				receiptOwnerWrapper.receipt = response;

				boolean hasImage = !PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ReceiptProcess.SET_ORIGINAL_IMAGE);
				view.setReceiptReadOnly(hasImage);
				view.showImageAlreadyDefinedWarning(hasImage);

				if ( (null == response.ownerTypeId) || BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(response.ownerTypeId) )
					getPolicy(response.ownerId);
				else if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(response.ownerTypeId) )
					getSubPolicy(response.ownerId);
				else if ( BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(response.ownerTypeId) )
					getSubCasualty(response.ownerId);
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

					if(receiptOwnerWrapper.receipt == null)
						receiptOwnerWrapper.receipt = new Receipt();

					receiptOwnerWrapper.receipt.number = view.getReceiptNumber();
					receiptOwnerWrapper.receipt.ownerTypeId = BigBangConstants.EntityIds.INSURANCE_POLICY;

					receiptOwnerWrapper.policy = response;
					receiptOwnerWrapper.insuranceAgencyName = response.insuranceAgencyName;
					view.getForm().setValue(receiptOwnerWrapper);
					view.enableToolbar(true);
					view.setReceiptReadOnly(false);
					if(popup.isAttached())
						popup.hidePopup();

					if(popupPolicy.isAttached())
						popupPolicy.hidePopup();

				} else {
					if(popupPolicy.isAttached()){
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar Recibos para a Apólice especificada"), TYPE.ALERT_NOTIFICATION));
					}
					else{
						view.setOwnerNotAvailable(true);
					}
					view.enableMarkReceipt(true);
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Apólice."), TYPE.ALERT_NOTIFICATION));
				view.enableMarkReceipt(true);
				view.enableOwnerProblem(true);
			}
		});

	}

	protected void getSubPolicy(String id) {

		subPolicyBroker.getSubPolicy(id, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {

				if(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT)){

					if(receiptOwnerWrapper.receipt == null)
						receiptOwnerWrapper.receipt = new Receipt();

					receiptOwnerWrapper.receipt.number = view.getReceiptNumber();
					receiptOwnerWrapper.receipt.ownerTypeId = BigBangConstants.EntityIds.INSURANCE_SUB_POLICY;

					receiptOwnerWrapper.subPolicy = response;
					receiptOwnerWrapper.insuranceAgencyName = response.inheritCompanyName;
					view.getForm().setValue(receiptOwnerWrapper);
					view.enableToolbar(true);
					view.setReceiptReadOnly(false);
					if(popup.isAttached())
						popup.hidePopup();

					if(popupPolicy.isAttached())
						popupPolicy.hidePopup();

				} else {
					if(popupPolicy.isAttached()){
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar Recibos para a Apólice Adesão especificada"), TYPE.ALERT_NOTIFICATION));
					}
					else{
						view.setOwnerNotAvailable(true);
					}
					view.enableMarkReceipt(true);
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Apólice Adesão."), TYPE.ALERT_NOTIFICATION));
				view.enableMarkReceipt(true);
				view.enableOwnerProblem(true);
			}
		});
	}

	protected void getSubCasualty(String id) {

		subCasualtyBroker.getSubCasualty(id, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {

				if(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_RECEIPT)){

					if(receiptOwnerWrapper.receipt == null)
						receiptOwnerWrapper.receipt = new Receipt();

					receiptOwnerWrapper.receipt.number = view.getReceiptNumber();
					receiptOwnerWrapper.receipt.ownerTypeId = BigBangConstants.EntityIds.SUB_CASUALTY;

					receiptOwnerWrapper.subCasualty = response;
					receiptOwnerWrapper.insuranceAgencyName = response.inheritInsurerName;
					view.getForm().setValue(receiptOwnerWrapper);
					view.enableToolbar(true);
					view.setReceiptReadOnly(false);
					if(popup.isAttached())
						popup.hidePopup();

					if(popupPolicy.isAttached())
						popupPolicy.hidePopup();

				} else {
					if(popupPolicy.isAttached()){
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar Recibos para o Sub-Sinistro especificada"), TYPE.ALERT_NOTIFICATION));
					}
					else{
						view.setOwnerNotAvailable(true);
					}
					view.enableMarkReceipt(true);
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sub-Sinistro."), TYPE.ALERT_NOTIFICATION));
				view.enableMarkReceipt(true);
				view.enableOwnerProblem(true);
			}
		});
	}

	protected void onVerifyPolicy() {
		editingOwner = true;
		final String policyNumber = view.getPolicyNumber();
		view.clearOwner();
		view.enableMarkReceipt(false);
		view.enableOwnerProblem(false);
		view.setOwnerNotAvailable(false);
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
					view.enableOwnerProblem(true);
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
			ReceiptOwnerWrapper toSend = view.getForm().getInfo();
			final ScanHandle handle = new ScanHandle();

			if(hasReceiptFile){
				handle.docushare = view.getSelectedScanItem().docushare;
				handle.handle = view.getSelectedScanItem().handle;
			}

			if(toSend.receipt.id == null){
				receiptBroker.serialCreateReceipt(toSend.receipt, handle.handle != null ? handle : null, new ResponseHandler<Receipt>() {

					@Override
					public void onResponse(Receipt response) {
						if (BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.CONTINUING.equalsIgnoreCase(response.typeId)) {
							updatePolicyContinuingReceipt(view.getForm().getInfo().policy, response);
						}
						editing = false;
						editingOwner = false;
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recibo criado com sucesso."), TYPE.TRAY_NOTIFICATION));
						if(hasReceiptFile){
							view.removeScanItem(handle);
							view.panelNavigateBack();
							receiptOwnerWrapper = new ReceiptOwnerWrapper();
						}
						view.clear();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o recibo."), TYPE.ALERT_NOTIFICATION));				
					}
				});
			}else if(handle.handle != null){
				receiptBroker.receiveImage(toSend.receipt, handle, new ResponseHandler<Receipt>() {

					@Override
					public void onResponse(Receipt response) {
						if (BigBangConstants.OperationIds.ReceiptProcess.ReceiptType.CONTINUING.equalsIgnoreCase(response.typeId)) {
							updatePolicyContinuingReceipt(view.getForm().getInfo().policy, response);
						}
						editing = false;
						editingOwner = false;
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recibo gravado com sucesso."), TYPE.TRAY_NOTIFICATION));
						view.clear();
						if(hasReceiptFile){
							view.removeScanItem(handle);
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar o recibo."), TYPE.ALERT_NOTIFICATION));				
					}
				});
			}
			else{
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível gravar a imagem de um recibo existente sem indicar a imagem."), TYPE.ALERT_NOTIFICATION));				
			}
		}
	}

	protected void onMarkReceipt() {

		ScanItem currentItem = view.getSelectedScanItem();
		view.enableReceiptNumber(false);
		view.markReceipt(currentItem);
		view.enableMarkReceipt(false);
		view.enableOwnerProblem(false);
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
		receiptOwnerWrapper = new ReceiptOwnerWrapper();
		view.clear();
	}
	
	/**
	 * Updates policy's premiums when a continuing receipt is saved.
	 * 
	 * @whotoblame jcamilo
	 * @param policy - The policy to update
	 */
	private void updatePolicyContinuingReceipt(InsurancePolicy policy, Receipt receipt) {

		// Previous verifications for mandatory data for calculations
		if (policy.fractioningId == null) {
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", 
					"Erro no update de prémios de apólice associada ao recibo: o tipo de fraccionamento não está definido na apólice."), 
					TYPE.ALERT_NOTIFICATION));
			return;
		}
		
		if (receipt.totalPremium == null) {
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", 
					"Erro no update de prémios de apólice associada ao recibo: o recibo não tem prémio total definido."), 
					TYPE.ALERT_NOTIFICATION));
			return;
		}
		
		if (receipt.salesPremium == null) {
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", 
					"Erro no update de prémios de apólice associada ao recibo: o recibo não tem prémio comercial definido."), 
					TYPE.ALERT_NOTIFICATION));
			return;
		}

		// Defines the coefficient according to the policy fractioning
		int coefficient;
		if (BigBangConstants.OperationIds.InsurancePolicyProcess.PolicyFractioning.YEAR.equalsIgnoreCase(policy.fractioningId)) {
			coefficient = 1;
		} else if (BigBangConstants.OperationIds.InsurancePolicyProcess.PolicyFractioning.SEMESTER.equalsIgnoreCase(policy.fractioningId)) {
			coefficient = 2;
		} else if (BigBangConstants.OperationIds.InsurancePolicyProcess.PolicyFractioning.QUARTER.equalsIgnoreCase(policy.fractioningId)) {
			coefficient = 4;
		} else if (BigBangConstants.OperationIds.InsurancePolicyProcess.PolicyFractioning.MONTH.equalsIgnoreCase(policy.fractioningId)) {
			coefficient = 12;
		} else {
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", 
					"Erro no update de prémios de apólice associada ao recibo: o tipo de fraccionamento não foi considerado no cálculo de coeficiente."), 
					TYPE.ALERT_NOTIFICATION));		
			return;
		}
		
		// Calculates the premiums to update the policy, according to the receipt's premiums
		double newTotalPremium = receipt.totalPremium * coefficient;
		double newSalesPremium = receipt.salesPremium * coefficient;
		
		policy.totalPremium = newTotalPremium;
		policy.premium = newSalesPremium;
		
		policyBroker.updatePolicyHeader(policy);
		
		// Persists the modifications in the database
		policyBroker.persistPolicy(policy.id, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", 
						"Foram actualizados os prémios da apólice."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", 
						"Erro no update de prémios de apólice associada ao recibo: o tipo de fraccionamento não foi considerado no cálculo de coeficiente."), 
						TYPE.ALERT_NOTIFICATION));		
			}
		});
	}
}
