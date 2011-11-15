package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.receiptModule.shared.ModuleConstants;

import com.google.gwt.i18n.client.DateTimeFormat;

public class ReceiptForm extends FormView<Receipt> implements ReceiptDataBrokerClient {

	protected TextBoxFormField number;
	protected TextBoxFormField clientName;
	protected TextBoxFormField clientNumber;
	protected TextBoxFormField policyNumber;
	protected TextBoxFormField policyDescription;
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

	protected int dataVersion = 0;

	public ReceiptForm(){
		number = new TextBoxFormField("Número");
		number.setFieldWidth("100px");
		type = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.RECEIPT_TYPE, "Tipo");
		clientName = new TextBoxFormField("Nome");
		clientNumber = new TextBoxFormField("Número");
		clientNumber.setFieldWidth("100px");
		policyDescription = new TextBoxFormField("Categoria, Ramo e Modalidade");
		policyNumber = new TextBoxFormField("Número");
		policyNumber.setFieldWidth("100px");
		totalPremium = new TextBoxFormField("Prémio Total");
		totalPremium.setUnitsLabel("€");
		totalPremium.setFieldWidth("100px");
		salesPremium = new  TextBoxFormField("Prémio Comercial");
		salesPremium.setUnitsLabel("€");
		salesPremium.setFieldWidth("100px");
		commission = new TextBoxFormField("Commissão (€)");
		commission.setUnitsLabel("€");
		commission.setFieldWidth("100px");
		retro = new TextBoxFormField("Retrocessões (€)");
		retro.setUnitsLabel("€");
		retro.setFieldWidth("100px");
		fat = new TextBoxFormField("FAT (€)");
		fat.setFieldWidth("100px");
		issueDate = new DatePickerFormField("Data de Emissão");
		coverageStart = new DatePickerFormField("Vencimento");
		coverageEnd = new DatePickerFormField("Até");
		dueDate = new DatePickerFormField("Limite de Pagamento");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		description = new TextAreaFormField();
		notes = new TextAreaFormField();

		addSection("Informação Geral");
		addFormFieldGroup(new FormField<?>[]{
				number,
				type
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				manager,
				mediator
		}, true);

		addSection("Cliente");
		addFormField(clientNumber, true);
		clientNumber.setEditable(false);
		addFormField(clientName, true);
		clientName.setEditable(false);

		addSection("Apólice");
		addFormField(policyNumber, true);
		policyNumber.setEditable(false);
		addFormField(policyDescription, true);
		policyDescription.setEditable(false);

		addSection("Valores");
		addFormFieldGroup(new FormField<?>[]{
				totalPremium,
				salesPremium
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				commission,
				retro
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				fat
		}, true);

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

		return result;
	}

	@Override
	public void setInfo(final Receipt info) {
		if(info == null) {
			clearInfo();
			return;
		}

		if(info.clientId != null){
			clientNumber.setValue(info.clientNumber);
			clientName.setValue(info.clientName);
		}else{
			clientNumber.clear();
			clientName.clear();
		}
		if(info.policyId != null) {
			policyNumber.setValue(info.policyNumber);
			policyDescription.setValue(info.categoryName+"/"+info.lineName+"/"+info.subLineName);
		}else{
			policyNumber.clear();
			policyDescription.clear();
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
		if(receipt.id != null && this.value.id != null) {
			if(receipt.id.equalsIgnoreCase(this.value.id)){
				this.setValue(receipt);
			}
		}
	}

	@Override
	public void removeReceipt(String id) {
		if(id != null && this.value.id != null) {
			if(id.equalsIgnoreCase(this.value.id)){
				this.setValue(null);
			}
		}
	}

}
