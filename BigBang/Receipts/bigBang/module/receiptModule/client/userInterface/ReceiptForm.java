package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.receiptModule.shared.ModuleConstants;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class ReceiptForm extends FormView<Receipt> implements ReceiptDataBrokerClient {

	protected TextBoxFormField number;
	protected TextBoxFormField client;
	protected TextBoxFormField policy;
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
	protected ExpandableListBoxFormField manager; 
	protected ExpandableListBoxFormField mediator;
	protected TextAreaFormField description;
	protected TextAreaFormField notes;
	
	protected int dataVersion = 0;

	public ReceiptForm(){
		number = new TextBoxFormField("Número");
		number.setFieldWidth("200px");
		type = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.RECEIPT_TYPE, "Tipo");
		type.allowEdition(false);
		client = new TextBoxFormField("Cliente");
		policy = new TextBoxFormField("Apólice");
		totalPremium = new NumericTextBoxFormField("Prémio Total");
		totalPremium.setUnitsLabel("€");
		totalPremium.setFieldWidth("100px");
		totalPremium.setTextAligment(TextAlignment.RIGHT);
		salesPremium = new  NumericTextBoxFormField("Prémio Comercial");
		salesPremium.setUnitsLabel("€");
		salesPremium.setFieldWidth("100px");
		salesPremium.setTextAligment(TextAlignment.RIGHT);
		commission = new NumericTextBoxFormField("Comissão");
		commission.setUnitsLabel("€");
		commission.setFieldWidth("100px");
		commission.setTextAligment(TextAlignment.RIGHT);
		retro = new NumericTextBoxFormField("Retrocessões");
		retro.setUnitsLabel("€");
		retro.setFieldWidth("100px");
		retro.setTextAligment(TextAlignment.RIGHT);
		fat = new NumericTextBoxFormField("FAT");
		fat.setFieldWidth("100px");
		fat.setUnitsLabel("€");
		fat.setTextAligment(TextAlignment.RIGHT);
		issueDate = new DatePickerFormField("Data de Emissão");
		coverageStart = new DatePickerFormField("Vigência");
		coverageEnd = new DatePickerFormField("Até");
		dueDate = new DatePickerFormField("Limite de Pagamento");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		mediator.allowEdition(false);
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		manager.allowEdition(false);
		description = new TextAreaFormField();
		notes = new TextAreaFormField();

		addSection("Informação Geral");
		addFormField(client, false);
		client.setEditable(false);
		addFormField(policy, false);
		addFormFieldGroup(new FormField<?>[]{
				number,
				mediator
		}, true);
		
		policy.setEditable(false);
		addFormFieldGroup(new FormField<?>[]{
				type,
				manager
		}, true);
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

		setValue(new Receipt());
		
		ReceiptDataBroker broker = ((ReceiptDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT));
		broker.registerClient(this);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public Receipt getInfo() {
		Receipt result = getValue();

		result.number = number.getValue();
		result.typeId = type.getValue();
		result.totalPremium = totalPremium.getValue();
		result.salesPremium = salesPremium.getValue();
		result.comissions = commission.getValue();
		result.retrocessions = retro.getValue();
		result.FATValue = fat.getValue();
		result.issueDate = issueDate.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(issueDate.getValue());
		result.maturityDate = coverageStart.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(coverageStart.getValue());
		result.endDate = coverageEnd.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(coverageEnd.getValue());
		result.dueDate = dueDate.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(dueDate.getValue());
		result.mediatorId = mediator.getValue();
		result.managerId = manager.getValue();
		result.notes = notes.getValue();
		result.description = description.getValue();

		return result;
	}

	@Override
	public void setInfo(final Receipt info) {
		if(info == null) {
			clearInfo();
			return;
		}

		if(info.clientId != null){
			client.setValue("#" + info.clientNumber + " - " + info.clientName);
		}else{
			client.clear();
		}
		if(info.policyId != null) {
			policy.setValue("#" + info.policyNumber + " - " + info.categoryName + "/" + info.lineName + "/" + info.subLineName);
		}else{
			policy.clear();
		}

		number.setValue(info.number);
		type.setValue(info.typeId);
		totalPremium.setValue(info.totalPremium);
		salesPremium.setValue(info.salesPremium);
		commission.setValue(info.comissions);
		retro.setValue(info.retrocessions);
		fat.setValue(info.FATValue);
		if(info.issueDate != null){
			issueDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.issueDate));
		}else{issueDate.clear();}
		if(info.maturityDate != null){
			coverageStart.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.maturityDate));
		}else{coverageStart.clear();}
		if(info.endDate != null){
			coverageEnd.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.endDate));
		}else{coverageEnd.clear();}
		if(info.dueDate != null){
			dueDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.dueDate));
		}else{dueDate.clear();}
		mediator.setValue(info.mediatorId);
		manager.setValue(info.managerId);
		
		notes.setValue(info.notes);
		description.setValue(info.description);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
			this.dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
			return this.dataVersion;
		}
		return -1;
	}

	@Override
	public void addReceipt(Receipt receipt) {
		return;
	}

	@Override
	public void updateReceipt(Receipt receipt) {
		if(this.value != null && receipt.id != null && this.value.id != null) {
			if(receipt.id.equalsIgnoreCase(this.value.id)){
				this.setValue(receipt);
			}
		}
	}

	@Override
	public void removeReceipt(String id) {
		if(this.value != null && id != null && this.value.id != null) {
			if(id.equalsIgnoreCase(this.value.id)){
				this.setValue(null);
			}
		}
	}

}
