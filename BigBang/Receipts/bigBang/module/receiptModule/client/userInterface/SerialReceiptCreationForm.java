package bigBang.module.receiptModule.client.userInterface;


import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.receiptModule.shared.ModuleConstants;
import bigBang.module.receiptModule.shared.ReceiptPolicyWrapper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;



public abstract class SerialReceiptCreationForm extends FormView<ReceiptPolicyWrapper>{

	private TextBoxFormField receiptNumber;
	private TextBoxFormField policyNumber;
	private TextBoxFormField client;
	private TextBoxFormField insurer;
	private TextBoxFormField categoryLineSubline;
	private TextBoxFormField status;
	
	protected ExpandableListBoxFormField type;
	protected TextBoxFormField totalPremium;
	protected TextBoxFormField salesPremium;
	protected TextBoxFormField commission;
	protected TextBoxFormField retro;
	protected TextBoxFormField fat;
	protected DatePickerFormField issueDate;
	protected DatePickerFormField coverageStart;
	protected DatePickerFormField coverageEnd;
	protected DatePickerFormField dueDate;
	protected ExpandableListBoxFormField manager; 
	protected ExpandableListBoxFormField mediator;
	protected TextAreaFormField description;
	protected TextAreaFormField notes;
	
	Button verifyReceiptNumber;
	Button verifyPolicyNumber;
	private Button markAsInvalid;
	
	
	public SerialReceiptCreationForm(){
		
		addSection("Número do recibo");
		receiptNumber = new TextBoxFormField("Número do recibo"); 
		receiptNumber.setFieldWidth("175px");
		addFormField(receiptNumber, true);
		verifyReceiptNumber = new Button("Verificar");
		verifyReceiptNumber.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onClickVerifyReceiptNumber();
			}
		});
		addWidget(verifyReceiptNumber);
		
		addSection("Apólice");
		policyNumber = new TextBoxFormField("Número da apólice");
		policyNumber.setFieldWidth("175px");
		addFormField(policyNumber, true);
		HorizontalPanel buttonPanel = new HorizontalPanel();
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
		addWidget(buttonPanel, false);
		client = new TextBoxFormField("Cliente");
		client.setFieldWidth("400px");
		insurer = new TextBoxFormField("Seguradora");
		insurer.setFieldWidth("175px");
		status = new TextBoxFormField("Estado");
		status.setFieldWidth("175px");
		categoryLineSubline = new TextBoxFormField( "Categoria/Ramo/Modalidade");
		categoryLineSubline.setFieldWidth("400px");
		addFormField(categoryLineSubline, true);
		addFormField(insurer, false);
		addFormField(client, true);
		addFormField(status, false);
		
		addSection("Recibo");

		type = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.RECEIPT_TYPE, "Tipo");
		totalPremium = new TextBoxFormField("Prémio Total");
		totalPremium.setUnitsLabel("€");
		totalPremium.setFieldWidth("100px");
		totalPremium.setTextAligment(TextAlignment.RIGHT);
		salesPremium = new  TextBoxFormField("Prémio Comercial");
		salesPremium.setUnitsLabel("€");
		salesPremium.setFieldWidth("100px");
		salesPremium.setTextAligment(TextAlignment.RIGHT);
		commission = new TextBoxFormField("Commissão");
		commission.setUnitsLabel("€");
		commission.setFieldWidth("100px");
		commission.setTextAligment(TextAlignment.RIGHT);
		retro = new TextBoxFormField("Retrocessões");
		retro.setUnitsLabel("€");
		retro.setFieldWidth("100px");
		retro.setTextAligment(TextAlignment.RIGHT);
		fat = new TextBoxFormField("FAT");
		fat.setFieldWidth("100px");
		fat.setUnitsLabel("€");
		fat.setTextAligment(TextAlignment.RIGHT);
		issueDate = new DatePickerFormField("Data de Emissão");
		coverageStart = new DatePickerFormField("Vigência");
		coverageEnd = new DatePickerFormField("Até");
		dueDate = new DatePickerFormField("Limite de Pagamento");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		manager.setEditable(false);
		description = new TextAreaFormField();
		notes = new TextAreaFormField();

		addFormField(type, true);

		addFormField(manager, true);
		addFormField(mediator, true);

		addSection("Valores");
		addFormField(totalPremium, true);
		addFormField(salesPremium, true);
		addFormField(commission, true);
		addFormField(retro, true);
		addFormField(fat, true);

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
		
	}
	
	protected abstract void onClickMarkAsInvalid();

	protected abstract void onClickVerifyPolicyNumber() ;
	
	protected abstract void onClickVerifyReceiptNumber();

	@Override
	public void setInfo(ReceiptPolicyWrapper info) {
		
		receiptNumber.setValue(info.receipt.number);
		policyNumber.setValue(info.policy.number);
		client.setValue("#"+info.receipt.clientNumber+"-"+info.receipt.clientName);
		categoryLineSubline.setValue(info.policy.categoryName+"/"+info.policy.lineName+"/"+info.policy.subLineName);
		insurer.setValue(info.insuranceAgencyName);
		status.setValue(info.policy.statusText);
		type.setValue(info.receipt.typeId);
		manager.setValue(info.receipt.managerId);
		mediator.setValue(info.receipt.mediatorId);
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
		
		return newWrapper;
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
		manager.setReadOnly(readonly);
		mediator.setReadOnly(readonly);
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
	}

}
