package bigBang.module.receiptModule.client.userInterface.form;


import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.receiptModule.shared.ModuleConstants;
import bigBang.module.receiptModule.shared.ReceiptPolicyWrapper;

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



public abstract class SerialReceiptCreationForm extends FormView<ReceiptPolicyWrapper>{

	protected TextBoxFormField receiptNumber;
	protected TextBoxFormField policyNumber;
	protected TextBoxFormField client;
	protected TextBoxFormField insurer;
	protected TextBoxFormField categoryLineSubline;
	protected TextBoxFormField status;
	protected final static Label policyNumberProblem = new Label("A apólice pretendida não existe");
	protected final static Label imageExistantLabel = new Label("O recibo já tem imagem definida");
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
		policyNumber = new TextBoxFormField("Número da apólice");
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
		buttonPanel.add(policyNumberProblem);
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
		totalPremium.setUnitsLabel("€");
		totalPremium.setFieldWidth("100px");
		totalPremium.setTextAligment(TextAlignment.RIGHT);
		salesPremium = new  NumericTextBoxFormField("Prémio Comercial", true);
		salesPremium.setUnitsLabel("€");
		salesPremium.setFieldWidth("100px");
		salesPremium.setTextAligment(TextAlignment.RIGHT);
		commission = new NumericTextBoxFormField("Comissão", true);
		commission.setUnitsLabel("€");
		commission.setFieldWidth("100px");
		commission.setTextAligment(TextAlignment.RIGHT);
		retro = new NumericTextBoxFormField("Retrocessões", true);
		retro.setUnitsLabel("€");
		retro.setFieldWidth("100px");
		retro.setTextAligment(TextAlignment.RIGHT);
		fat = new NumericTextBoxFormField("FAT", true);
		fat.setFieldWidth("100px");
		fat.setUnitsLabel("€");
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
		bonusMalusValue.setUnitsLabel("€");

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
				bonusMalusValue.setReadOnlyInternal(bonusMalusOption.isReadOnly());
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

	protected abstract void onChangedPolicyNumber();

	protected abstract void onChangedReceiptNumber();

	protected abstract void onClickNewReceipt();

	protected abstract void onClickMarkAsInvalid();

	protected abstract void onClickVerifyPolicyNumber() ;

	protected abstract void onClickVerifyReceiptNumber();

	protected abstract void onEnterKeyReceiptNumber();

	protected abstract void onEnterKeyPolicyNumber();

	@Override
	public void setInfo(ReceiptPolicyWrapper info) {

		receiptNumber.setValue(info.receipt.number);
		policyNumber.setValue(info.policy.number);
		client.setValue("#"+info.policy.clientNumber+"-"+info.policy.clientName);
		categoryLineSubline.setValue(info.policy.categoryName+"/"+info.policy.lineName+"/"+info.policy.subLineName);
		insurer.setValue(info.insuranceAgencyName);
		status.setValue(info.policy.statusText);
		type.setValue(info.receipt.typeId);
		manager.setValue(info.policy.managerId);
		mediator.setValue(info.policy.mediatorId);
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
	public ReceiptPolicyWrapper getInfo() {

		ReceiptPolicyWrapper newWrapper = value;
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
		newWrapper.receipt.policyId = newWrapper.policy.id;
		newWrapper.receipt.isMalus = bonusMalusOption.getValue() == null ? null : bonusMalusOption.getValue().equalsIgnoreCase("Malus");
		newWrapper.receipt.bonusMalus = bonusMalusValue.getValue();

		return newWrapper;
	}

	public void newReceiptEnabled(boolean enabled){
		newReceiptButton.setEnabled(enabled);
	}

	public void setReceiptNumberReadOnly(boolean readOnly){
		receiptNumber.setReadOnlyInternal(readOnly);
		verifyReceiptNumber.setEnabled(!readOnly);
	}

	public void setPolicyNumberReadOnly(boolean readOnly){
		policyNumber.setReadOnlyInternal(readOnly);
		verifyPolicyNumber.setEnabled(!readOnly);
	}

	public void setPolicyReadOnly(boolean readOnly){
		categoryLineSubline.setReadOnlyInternal(readOnly);
		insurer.setReadOnlyInternal(readOnly);
		client.setReadOnlyInternal(readOnly);
		status.setReadOnlyInternal(readOnly);
	}

	public void enableMarkAsInvalid(boolean enabled){
		markAsInvalid.setEnabled(enabled);
	}

	public void setReceiptReadOnly(boolean readonly){
		type.setReadOnlyInternal(readonly);
		totalPremium.setReadOnlyInternal(readonly);
		salesPremium.setReadOnlyInternal(readonly);
		commission.setReadOnlyInternal(readonly);
		retro.setReadOnlyInternal(readonly);
		fat.setReadOnlyInternal(readonly);
		dueDate.setReadOnlyInternal(readonly);
		coverageEnd.setReadOnlyInternal(readonly);
		coverageStart.setReadOnlyInternal(readonly);
		issueDate.setReadOnlyInternal(readonly);
		description.setReadOnlyInternal(readonly);
		notes.setReadOnlyInternal(readonly);
		bonusMalusOption.setReadOnlyInternal(readonly);
		bonusMalusValue.setReadOnlyInternal(readonly);

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

	public void enablePolicy(boolean b) {
		policyNumber.setReadOnlyInternal(!b);
		verifyPolicyNumber.setEnabled(b);

		if(b){
			policyNumber.focus();
		}
	}

	public void setFocusOnPolicy() {
		policyNumber.getNativeField().setFocus(true);

	}

	public void setFocusOnReceipt() {
		receiptNumber.getNativeField().setFocus(true);
	}

	public void clearPolicy() {
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
		return policyNumber.getValue();
	}

	public void isPolicyNumberProblem(boolean b) {
		if(b){
			policyNumber.getNativeField().getElement().getStyle().setColor("RED");
		}
		else{
			policyNumber.getNativeField().getElement().getStyle().setColor("BLACK");

		}

	}

	public void showLabel(boolean b) {
		policyNumberProblem.setVisible(b);
	}

	public void showImageAlreadyDefineWarning(boolean hasImage) {
		imageExistantLabel.setVisible(hasImage);
	}

}
