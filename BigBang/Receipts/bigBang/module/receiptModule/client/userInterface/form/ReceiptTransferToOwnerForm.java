package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.OwnerRef;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.InsuranceSubPolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.SubCasualtySelectionViewPresenter;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ReceiptTransferToOwnerForm extends FormView<OwnerRef> {

	protected ListBoxFormField referenceType;
	protected ExpandableSelectionFormField policyReference;
	protected ExpandableSelectionFormField subPolicyReference;
	protected ExpandableSelectionFormField subCasualtyReference;
	
	public ReceiptTransferToOwnerForm(){

		HorizontalPanel referenceWrapper = new HorizontalPanel();

		referenceType = new ListBoxFormField("");
		referenceType.addItem("Apólice nº", BigBangConstants.EntityIds.INSURANCE_POLICY);
		referenceType.addItem("Apólice Adesão nº", BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		referenceType.addItem("Sub-Sinistro nº", BigBangConstants.EntityIds.SUB_CASUALTY);
		referenceType.setMandatory(true);
		referenceType.removeItem(0); //Removes the empty value

		//POLICY REFERENCE
		InsurancePolicySelectionViewPresenter policySelectionPanel = (InsurancePolicySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("RECEIPT_POLICY_SELECTION");
		//InsurancePolicySelectionViewPresenter policySelectionPanel = new InsurancePolicySelectionViewPresenter((InsurancePolicySelectionView) GWT.create(InsurancePolicySelectionView.class));
		policySelectionPanel.setOperationId(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT);
		policySelectionPanel.go();
		policyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, "", policySelectionPanel);
		policyReference.setMandatory(true);

		//SUB POLICY REFERENCE
		InsuranceSubPolicySelectionViewPresenter subPolicySelectionPanel = (InsuranceSubPolicySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("RECEIPT_SUBPOLICY_SELECTION");
		//InsuranceSubPolicySelectionViewPresenter subPolicySelectionPanel = new InsuranceSubPolicySelectionViewPresenter((InsuranceSubPolicySelectionView) GWT.create(InsuranceSubPolicySelectionView.class));
		subPolicySelectionPanel.setOperationId(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT);
		subPolicySelectionPanel.go();
		subPolicyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, "", subPolicySelectionPanel);
		subPolicyReference.setMandatory(true);

		//SUB CASUALTY REFERENCE
		SubCasualtySelectionViewPresenter subCasualtySelectionPanel = (SubCasualtySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("RECEIPT_SUBCASUALTY_SELECTION");
		//InsuranceSubPolicySelectionViewPresenter subPolicySelectionPanel = new InsuranceSubPolicySelectionViewPresenter((InsuranceSubPolicySelectionView) GWT.create(InsuranceSubPolicySelectionView.class));
		subCasualtySelectionPanel.setOperationId(BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_RECEIPT);
		subCasualtySelectionPanel.go();
		subCasualtyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.SUB_CASUALTY, "", subCasualtySelectionPanel);
		subCasualtyReference.setMandatory(true);

		referenceWrapper.add(referenceType);
		referenceWrapper.add(policyReference);
		referenceWrapper.add(subPolicyReference);
		referenceWrapper.add(subCasualtyReference);
		referenceWrapper.setCellVerticalAlignment(referenceType, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(policyReference, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(subPolicyReference, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(subCasualtyReference, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setHeight("45px");

		addSection("Transferência de Pasta");

		registerFormField(referenceType);
		registerFormField(policyReference);
		registerFormField(subPolicyReference);
		registerFormField(subCasualtyReference);

		addWidget(referenceWrapper);

		referenceType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String type = event.getValue();

				if(type == null || type.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
					policyReference.setVisible(true);
					subPolicyReference.setVisible(false);
					subCasualtyReference.setVisible(false);
				}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
					policyReference.setVisible(false);
					subPolicyReference.setVisible(true);
					subCasualtyReference.setVisible(false);
				}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
					policyReference.setVisible(false);
					subPolicyReference.setVisible(false);
					subCasualtyReference.setVisible(true);
				}
			}
		});

		subPolicyReference.setVisible(false);
		subCasualtyReference.setVisible(false);
		referenceType.setValue(BigBangConstants.EntityIds.INSURANCE_POLICY, true);

		setValidator(new ReceiptTransferToOwnerFormValidator(this));
	}
	
	@Override
	public OwnerRef getInfo() {
		OwnerRef owner = value;
		owner.ownerTypeId = referenceType.getValue();

		if ( BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(owner.ownerTypeId) )
			owner.ownerId = policyReference.getValue();
		else if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(owner.ownerTypeId) )
			owner.ownerId = subPolicyReference.getValue();
		else if ( BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(owner.ownerTypeId) )
			owner.ownerId = subCasualtyReference.getValue();
		else
			owner.ownerId = null;

		return owner;
	}

	@Override
	public void setInfo(OwnerRef info) {
		referenceType.setValue(info.ownerTypeId);

		if ( BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(info.ownerTypeId) )
			policyReference.setValue(info.ownerId);
		else if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(info.ownerTypeId) )
			subPolicyReference.setValue(info.ownerId);
		else if ( BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(info.ownerTypeId) )
			subCasualtyReference.setValue(info.ownerId);
	}
}
