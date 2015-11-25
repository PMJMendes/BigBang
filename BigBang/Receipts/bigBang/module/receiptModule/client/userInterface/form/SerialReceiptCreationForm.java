package bigBang.module.receiptModule.client.userInterface.form;


import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.FormField;
import bigBang.library.client.Session;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.InsuranceSubPolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.SubCasualtySelectionViewPresenter;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.receiptModule.shared.ModuleConstants;
import bigBang.module.receiptModule.shared.ReceiptOwnerWrapper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public abstract class SerialReceiptCreationForm extends FormView<ReceiptOwnerWrapper>{

	protected TextBoxFormField receiptNumber;
	protected ListBoxFormField referenceType;
	protected TextBoxFormField policyNumber;
	protected ExpandableSelectionFormField subPolicyReference;
	protected ExpandableSelectionFormField subCasualtyReference;
	protected TextBoxFormField client;
	protected TextBoxFormField insurer;
	protected TextBoxFormField categoryLineSubline;
	protected TextBoxFormField status;
	protected final static Label policyNumberProblem = new Label("A apólice pretendida não existe.");
	protected final static Label imageExistantLabel = new Label("O recibo já tem imagem definida.");
	protected final static Label policyNotAvailable = new Label("Não é possível criar recibos para esta apólice.");
	protected ExpandableListBoxFormField type;
	protected NumericTextBoxFormField totalPremium;
	protected NumericTextBoxFormField salesPremium;
	protected NumericTextBoxFormField commission;
	protected NumericTextBoxFormField retro;
	protected NumericTextBoxFormField fat;
	protected DatePickerFormField issueDate;
	protected DatePickerFormField coverageStart;
	protected DatePickerFormField coverageEnd;
	protected DatePickerFormField dueDate;
	protected ExpandableSelectionFormField manager; 
	protected ExpandableSelectionFormField mediator;
	protected TextAreaFormField description;
	protected TextAreaFormField notes;
	protected ListBoxFormField bonusMalusOption;
	protected NumericTextBoxFormField bonusMalusValue;

	protected FormField<?> lastFocusedField;

	protected int receiptNumberCursorPos, policyNumberCursorPos;
	
	Button verifyReceiptNumber;
	Button verifyPolicyNumber;
	private Button markAsInvalid;
	private Button newReceiptButton;

	public SerialReceiptCreationForm(){

		addSection("Número do recibo");
		receiptNumber = new TextBoxFormField("Número do recibo"); 

		receiptNumber.setFieldWidth("175px");

		receiptNumber.getNativeField().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				receiptNumberCursorPos = receiptNumber.getNativeField().getCursorPos();
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					onClickVerifyReceiptNumber();
				}
				else if(isEditKey(event.getNativeKeyCode())){
					onChangedReceiptNumber();
				}
			}
		});

		addFormField(receiptNumber, true);
		verifyReceiptNumber = new Button("Verificar");
		verifyReceiptNumber.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClickVerifyReceiptNumber();
			}
		});

		newReceiptButton = new Button("Novo Recibo");

		HorizontalPanel newPanel = new HorizontalPanel();
		newPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		newPanel.add(verifyReceiptNumber);
		newPanel.add(newReceiptButton);
		imageExistantLabel.getElement().getStyle().setColor("RED");
		newPanel.add(imageExistantLabel);

		newReceiptButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClickNewReceipt();			
			}
		});

		newPanel.setSpacing(5);
		newPanel.setHeight("55px");

		addWidget(newPanel, true);

		addSection("Apólice");

		referenceType = new ListBoxFormField("");
		referenceType.addItem("Apólice nº", BigBangConstants.EntityIds.INSURANCE_POLICY);
		referenceType.addItem("Apólice Adesão nº", BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		referenceType.addItem("Sub-Sinistro nº", BigBangConstants.EntityIds.SUB_CASUALTY);
		referenceType.setMandatory(true);
		referenceType.removeItem(0); //Removes the empty value
		addFormField(referenceType, true);

		referenceType.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String type = event.getValue();
				if(type == null || type.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
					policyNumber.setVisible(true);
					verifyPolicyNumber.setVisible(true);
					subPolicyReference.setVisible(false);
					subCasualtyReference.setVisible(false);
				}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
					policyNumber.setVisible(false);
					verifyPolicyNumber.setVisible(false);
					subPolicyReference.setVisible(true);
					subCasualtyReference.setVisible(false);
				}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
					policyNumber.setVisible(false);
					verifyPolicyNumber.setVisible(false);
					subPolicyReference.setVisible(false);
					subCasualtyReference.setVisible(true);
				}
				onOwnerTypeChanged();
			}
		});

		policyNumber = new TextBoxFormField("");
		policyNumber.setFieldWidth("175px");
		policyNumber.getNativeField().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				policyNumberCursorPos = policyNumber.getNativeField().getCursorPos();
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					onClickVerifyPolicyNumber();
				}
				else if(isEditKey(event.getNativeKeyCode())){
					onChangedPolicyNumber();
				}
			}
		});
		addFormField(policyNumber, true);

		InsuranceSubPolicySelectionViewPresenter subPolicySelectionPanel = (InsuranceSubPolicySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("RECEIPT_SUBPOLICY_SELECTION");
		subPolicySelectionPanel.setOperationId(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT);
		subPolicySelectionPanel.go();
		subPolicyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, "", subPolicySelectionPanel);
		subPolicyReference.setMandatory(true);
		addFormField(subPolicyReference, true);
		subPolicyReference.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onSubPolicyChanged();
			}
		});

		SubCasualtySelectionViewPresenter subCasualtySelectionPanel = (SubCasualtySelectionViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("RECEIPT_SUBCASUALTY_SELECTION");
		subCasualtySelectionPanel.setOperationId(BigBangConstants.OperationIds.SubCasualtyProcess.CREATE_RECEIPT);
		subCasualtySelectionPanel.go();
		subCasualtyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.SUB_CASUALTY, "", subCasualtySelectionPanel);
		subCasualtyReference.setMandatory(true);
		addFormField(subCasualtyReference, true);
		subCasualtyReference.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onSubCasualtyChanged();
			}
		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verifyPolicyNumber = new Button("Verificar");
		buttonPanel.add(verifyPolicyNumber);
		verifyPolicyNumber.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClickVerifyPolicyNumber();
			}
		});
		markAsInvalid = new Button("Saltar Recibo");
		buttonPanel.setSpacing(5);
		buttonPanel.setHeight("55px");
		buttonPanel.add(markAsInvalid);
		markAsInvalid.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClickMarkAsInvalid();
			}
		});
		policyNumberProblem.getElement().getStyle().setColor("RED");
		policyNotAvailable.getElement().getStyle().setColor("RED");
		buttonPanel.add(policyNumberProblem);
		buttonPanel.add(policyNotAvailable);
		addWidget(buttonPanel, true);
		client = new TextBoxFormField("Cliente");
		client.setFieldWidth("400px");
		insurer = new TextBoxFormField("Seguradora");
		insurer.setFieldWidth("175px");
		status = new TextBoxFormField("Estado");
		status.setFieldWidth("175px");
		categoryLineSubline = new TextBoxFormField( "Categoria/Ramo/Modalidade");
		categoryLineSubline.setFieldWidth("400px");
		addLineBreak();
		addFormField(categoryLineSubline, true);
		addFormField(insurer, true);
		addLineBreak();
		addFormField(client, true);
		addFormField(status, true);

		addSection("Recibo");

		type = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.RECEIPT_TYPE, "Tipo");
		type.allowEdition(false);
		totalPremium = new NumericTextBoxFormField("Prémio Total", true);
		totalPremium.setUnitsLabel(Session.getCurrency());
		totalPremium.setFieldWidth("100px");
		totalPremium.setTextAligment(TextAlignment.RIGHT);
		salesPremium = new  NumericTextBoxFormField("Prémio Simples/Com", true);
		salesPremium.setUnitsLabel(Session.getCurrency());
		salesPremium.setFieldWidth("100px");
		salesPremium.setTextAligment(TextAlignment.RIGHT);
		commission = new NumericTextBoxFormField("Comissão", true);
		commission.setUnitsLabel(Session.getCurrency());
		commission.setFieldWidth("100px");
		commission.setTextAligment(TextAlignment.RIGHT);
		retro = new NumericTextBoxFormField("Retrocessões", true);
		retro.setUnitsLabel(Session.getCurrency());
		retro.setFieldWidth("100px");
		retro.setTextAligment(TextAlignment.RIGHT);
		fat = new NumericTextBoxFormField("FAT", true);
		fat.setFieldWidth("100px");
		fat.setUnitsLabel(Session.getCurrency());
		fat.setTextAligment(TextAlignment.RIGHT);
		issueDate = new DatePickerFormField("Data de Emissão");
		coverageStart = new DatePickerFormField("Vigência");
		coverageEnd = new DatePickerFormField("Até");
		dueDate = new DatePickerFormField("Limite de Pagamento");
		mediator = new ExpandableSelectionFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		mediator.setEditable(false);
		manager = new ExpandableSelectionFormField(BigBangConstants.EntityIds.USER, "Gestor");
		manager.setEditable(false);
		description = new TextAreaFormField();
		notes = new TextAreaFormField();

		bonusMalusOption = new ListBoxFormField("Bonus/Malus");
		bonusMalusOption.setFieldWidth("100%");
		bonusMalusOption.setEmptyValueString("Nenhum");
		bonusMalusOption.addItem("Bonus", "Bonus");
		bonusMalusOption.addItem("Malus", "Malus");

		bonusMalusValue = new NumericTextBoxFormField("Valor", true);
		bonusMalusValue.setFieldWidth("100px");
		bonusMalusValue.setUnitsLabel(Session.getCurrency());

		addFormField(type, true);

		addFormField(manager, true);
		addFormField(mediator, true);

		addSection("Valores");
		addFormField(totalPremium, true);
		addFormField(salesPremium, true);
		addFormField(commission, true);
		addFormField(retro, true);
		addFormField(fat, true);
		addLineBreak();
		addFormField(bonusMalusOption, true);
		addFormField(bonusMalusValue, true);

		addSection("Datas");
		addFormFieldGroup(new FormField<?>[]{
				issueDate,
				dueDate
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				coverageStart,
				coverageEnd
		}, true);

		addSection("Descrição");
		addFormField(description);

		addSection("Notas Internas");
		addFormField(notes);
		setReceiptNumberReadOnly(true);
		setPolicyNumberReadOnly(true);
		setPolicyReadOnly(true);
		setReceiptReadOnly(true);		
		enableMarkAsInvalid(false);
		newReceiptButton.setEnabled(false);

		bonusMalusOption.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bonusMalusValue.setEditable(event.getValue() != null && !event.getValue().isEmpty());
				bonusMalusValue.setReadOnly(bonusMalusOption.isReadOnly());
				bonusMalusValue.setValue(null);
			}
		});

		type.setMandatory(true);
		totalPremium.setMandatory(true);
		
		setValidator(new SerialReceiptCreationFormValidator(this));
	}

	protected boolean isEditKey(int nativeKeyCode) {

		switch(nativeKeyCode){

		case KeyCodes.KEY_DOWN:
		case KeyCodes.KEY_UP:
		case KeyCodes.KEY_LEFT:
		case KeyCodes.KEY_RIGHT:
		case KeyCodes.KEY_SHIFT:
		case KeyCodes.KEY_CTRL:
		case KeyCodes.KEY_ALT:
		case KeyCodes.KEY_HOME:
		case KeyCodes.KEY_END:
			return false;
		default:
			return true;
		}
	}

	protected abstract void onChangedReceiptNumber();

	protected abstract void onEnterKeyReceiptNumber();

	protected abstract void onClickVerifyReceiptNumber();

	protected abstract void onClickNewReceipt();

	protected abstract void onOwnerTypeChanged();

	protected abstract void onChangedPolicyNumber();

	protected abstract void onEnterKeyPolicyNumber();

	protected abstract void onClickVerifyPolicyNumber() ;

	protected abstract void onClickMarkAsInvalid();

	protected abstract void onSubPolicyChanged();

	protected abstract void onSubCasualtyChanged();

	@Override
	public void setInfo(ReceiptOwnerWrapper info) {

		receiptNumber.setValue(info.receipt.number);

		referenceType.setValue(info.receipt.ownerTypeId);
		if ( BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(info.receipt.ownerTypeId) )
		{
			subPolicyReference.setValue(null);
			subCasualtyReference.setValue(null);

			policyNumber.setValue(info.policy.number);
			client.setValue("#"+info.policy.clientNumber+"-"+info.policy.clientName);
			categoryLineSubline.setValue(info.policy.categoryName+"/"+info.policy.lineName+"/"+info.policy.subLineName);
			status.setValue(info.policy.statusText);
			manager.setValue(info.policy.managerId);
			mediator.setValue(info.policy.mediatorId);
		}
		else if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(info.receipt.ownerTypeId) )
		{
			policyNumber.setValue(null);
			subCasualtyReference.setValue(null);

			subPolicyReference.setValue(info.subPolicy.id);
			client.setValue("#"+info.subPolicy.clientNumber+"-"+info.subPolicy.clientName);
			categoryLineSubline.setValue(info.subPolicy.inheritCategoryName+"/"+info.subPolicy.inheritLineName+"/"+info.subPolicy.inheritSubLineName);
			status.setValue(info.subPolicy.statusText);
			manager.setValue(info.subPolicy.managerId);
			mediator.setValue(info.subPolicy.inheritMediatorId);
		}
		else if ( BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(info.receipt.ownerTypeId) )
		{
			policyNumber.setValue(null);
			subPolicyReference.setValue(null);

			subCasualtyReference.setValue(info.subCasualty.id);
			client.setValue("#"+info.subCasualty.inheritMasterClientNumber+"-"+info.subCasualty.inheritMasterClientName);
			categoryLineSubline.setValue(info.subCasualty.categoryName+"/"+info.subCasualty.lineName+"/"+info.subCasualty.subLineName);
			status.setValue(null);
			manager.setValue(info.subCasualty.managerId);
			mediator.setValue(info.subCasualty.inheritMasterMediatorId);
		}
		else
		{
			policyNumber.setValue(null);
			subPolicyReference.setValue(null);
			subCasualtyReference.setValue(null);

			client.setValue(null);
			categoryLineSubline.setValue(null);
			status.setValue(null);
			manager.setValue(null);
			mediator.setValue(null);
		}

		insurer.setValue(info.insuranceAgencyName);

		type.setValue(info.receipt.typeId);
		totalPremium.setValue(info.receipt.totalPremium);
		salesPremium.setValue(info.receipt.salesPremium);
		commission.setValue(info.receipt.comissions);
		retro.setValue(info.receipt.retrocessions);
		fat.setValue(info.receipt.FATValue);
		issueDate.setValue(info.receipt.issueDate);
		coverageStart.setValue(info.receipt.maturityDate);
		coverageEnd.setValue(info.receipt.endDate);
		dueDate.setValue(info.receipt.dueDate);
		description.setValue(info.receipt.description);
		notes.setValue(info.receipt.notes);
		bonusMalusOption.setValue(info.receipt.isMalus == null ? null : info.receipt.isMalus ? "Malus" : "Bonus", true);
		bonusMalusValue.setValue(info.receipt.bonusMalus);
	}

	@Override
	public ReceiptOwnerWrapper getInfo() {
		ReceiptOwnerWrapper newWrapper = value;

		newWrapper.receipt.ownerTypeId = referenceType.getValue();
		if ( (null == newWrapper.receipt.ownerTypeId) || BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(newWrapper.receipt.ownerTypeId) )
			newWrapper.receipt.ownerId = newWrapper.policy.id;
		else if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(newWrapper.receipt.ownerTypeId) )
			newWrapper.receipt.ownerId = subPolicyReference.getValue();
		else if ( BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(newWrapper.receipt.ownerTypeId) )
			newWrapper.receipt.ownerId = subCasualtyReference.getValue();

		newWrapper.receipt.typeId = type.getValue();
		newWrapper.receipt.managerId = manager.getValue();
		newWrapper.receipt.mediatorId = mediator.getValue();
		newWrapper.receipt.totalPremium = totalPremium.getValue();
		newWrapper.receipt.salesPremium = salesPremium.getValue();
		newWrapper.receipt.comissions = commission.getValue();
		newWrapper.receipt.retrocessions = retro.getValue();
		newWrapper.receipt.FATValue = fat.getValue();
		newWrapper.receipt.issueDate = issueDate.getStringValue();
		newWrapper.receipt.endDate = coverageEnd.getStringValue();
		newWrapper.receipt.dueDate = dueDate.getStringValue();
		newWrapper.receipt.maturityDate = coverageStart.getStringValue();
		newWrapper.receipt.description = description.getValue();
		newWrapper.receipt.notes = notes.getValue();
		newWrapper.receipt.isMalus = bonusMalusOption.getValue() == null ? null : bonusMalusOption.getValue().equalsIgnoreCase("Malus");
		newWrapper.receipt.bonusMalus = bonusMalusValue.getValue();

		return newWrapper;
	}

	public void newReceiptEnabled(boolean enabled){
		newReceiptButton.setEnabled(enabled);
	}

	public void setReceiptNumberReadOnly(boolean readOnly){
		receiptNumber.setReadOnly(readOnly);
		verifyReceiptNumber.setEnabled(!readOnly);
	}

	public void setPolicyNumberReadOnly(boolean readOnly){
		policyNumber.setReadOnly(readOnly);
		verifyPolicyNumber.setEnabled(!readOnly);
	}

	public void setPolicyReadOnly(boolean readOnly){
		categoryLineSubline.setReadOnly(readOnly);
		insurer.setReadOnly(readOnly);
		client.setReadOnly(readOnly);
		status.setReadOnly(readOnly);
	}

	public void enableMarkAsInvalid(boolean enabled){
		markAsInvalid.setEnabled(enabled);
	}

	public void setReceiptReadOnly(boolean readonly){
		type.setReadOnly(readonly);
		totalPremium.setReadOnly(readonly);
		salesPremium.setReadOnly(readonly);
		commission.setReadOnly(readonly);
		retro.setReadOnly(readonly);
		fat.setReadOnly(readonly);
		dueDate.setReadOnly(readonly);
		coverageEnd.setReadOnly(readonly);
		coverageStart.setReadOnly(readonly);
		issueDate.setReadOnly(readonly);
		description.setReadOnly(readonly);
		notes.setReadOnly(readonly);
		bonusMalusOption.setReadOnly(readonly);
		bonusMalusValue.setReadOnly(readonly);

		if(!readonly){
			type.focus();
		}
	}

	public String getReceiptNumber() {
		return receiptNumber.getValue();

	}

	public void setReceiptNumber(String id, boolean keepCursorPos) {
		if(keepCursorPos){
			receiptNumber.setValue(id);
			receiptNumber.getNativeField().setCursorPos(receiptNumberCursorPos);
		}else{
			receiptNumber.setValue(id);
		}
	}

	public void hideMarkAsEnable(boolean b) {

		markAsInvalid.setVisible(!b);

	}

	public void enableOwner(boolean b) {
		referenceType.setReadOnly(!b);
		policyNumber.setReadOnly(!b);
		subPolicyReference.setReadOnly(!b);
		subCasualtyReference.setReadOnly(!b);
		verifyPolicyNumber.setEnabled(b);

		if(b){
			policyNumber.focus();
		}
	}

	public void setFocusOnPolicy() {
		referenceType.setValue(BigBangConstants.EntityIds.INSURANCE_POLICY);
		policyNumber.getNativeField().setFocus(true);

	}

	public void setFocusOnReceipt() {
		receiptNumber.getNativeField().setFocus(true);
	}

	public void clearOwner() {
		categoryLineSubline.clear();
		insurer.clear();
		client.clear();
		status.clear();

	}

	public void setPolicyNumber(String policyNumber2, boolean keepCursorPos) {
		if(keepCursorPos){
			policyNumber.setValue(policyNumber2);
			policyNumber.getNativeField().setCursorPos(policyNumberCursorPos);
		}else{
			policyNumber.setValue(policyNumber2);
		}
	}

	public String getPolicyNumber() {
		if ( BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(referenceType.getValue()) )
			return policyNumber.getValue();
		return null;
	}

	public String getSubPolicyId() {
		if ( BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(referenceType.getValue()) )
			return subPolicyReference.getValue();
		return null;
	}

	public String getSubCasualtyId() {
		if ( BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(referenceType.getValue()) )
			return subCasualtyReference.getValue();
		return null;
	}

	public void isPolicyNumberProblem(boolean b) {
		if(b){
			policyNumber.getNativeField().getElement().getStyle().setColor("RED");
			subPolicyReference.getNativeField().getElement().getStyle().setColor("RED");
			subCasualtyReference.getNativeField().getElement().getStyle().setColor("RED");
		}
		else{
			policyNumber.getNativeField().getElement().getStyle().setColor("BLACK");
			subPolicyReference.getNativeField().getElement().getStyle().setColor("BLACK");
			subCasualtyReference.getNativeField().getElement().getStyle().setColor("BLACK");
		}
	}

	public void showLabel(boolean b) {
		policyNumberProblem.setVisible(b);
	}

	public void showImageAlreadyDefineWarning(boolean hasImage) {
		imageExistantLabel.setVisible(hasImage);
	}

	public void showNotAvailableLabel(boolean b) {
		policyNotAvailable.setVisible(b);
	}

}
