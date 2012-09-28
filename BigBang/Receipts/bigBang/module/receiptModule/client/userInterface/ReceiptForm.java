package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.receiptModule.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class ReceiptForm extends FormView<Receipt> implements ReceiptDataBrokerClient {

	protected TextBoxFormField number;
	protected NavigationFormField client;
	protected NavigationFormField policy;
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

	protected int dataVersion = 0;

	public ReceiptForm(){
		number = new TextBoxFormField("Número");
		number.setMandatory(true);
		number.setFieldWidth("200px");
		type = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.RECEIPT_TYPE, "Tipo");
		type.allowEdition(false);
		client = new NavigationFormField("Cliente");
		policy = new NavigationFormField("Apólice");
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
		
		bonusMalusOption = new ListBoxFormField("Bonus/Malus");
		bonusMalusOption.setFieldWidth("100%");
		bonusMalusOption.setEmptyValueString("Nenhum");
		bonusMalusOption.addItem("Bonus", "Bonus");
		bonusMalusOption.addItem("Malus", "Malus");
		
		bonusMalusValue = new NumericTextBoxFormField("Valor", true);
		bonusMalusValue.setFieldWidth("100px");
		bonusMalusValue.setUnitsLabel("€");
		issueDate = new DatePickerFormField("Data de Emissão");
		coverageStart = new DatePickerFormField("Vigência");
		coverageEnd = new DatePickerFormField("Até");
		dueDate = new DatePickerFormField("Limite de Pagamento");
		mediator = new ExpandableSelectionFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		mediator.setEditable(false);
		manager = new ExpandableSelectionFormField(BigBangConstants.EntityIds.USER, "Gestor de Recibo");
		manager.setEditable(false);
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

		bonusMalusOption.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bonusMalusValue.setEditable(event.getValue() != null && !event.getValue().isEmpty());
				bonusMalusValue.setReadOnly(isReadOnly());
				bonusMalusValue.setValue(null);
			}
		});
		
		setValue(new Receipt());

		ReceiptDataBroker broker = ((ReceiptDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT));
		broker.registerClient(this);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(mediator != null && manager != null){
			mediator.setReadOnly(true);
			manager.setReadOnly(true);
		}
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
		result.isMalus = bonusMalusOption.getValue() == null ? null : bonusMalusOption.getValue().equalsIgnoreCase("Malus");
		result.bonusMalus = bonusMalusValue.getValue();
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
			NavigationHistoryItem item = new NavigationHistoryItem();
			item.setParameter("section", "client");
			item.setStackParameter("display");
			item.pushIntoStackParameter("display", "search");
			item.setParameter("clientid", info.clientId);
			client.setValue(item);

			client.setValueName("#" + info.clientNumber + " - " + info.clientName);
		}else{
			client.clear();
		}
		if(info.policyId != null) {
			NavigationHistoryItem item = new NavigationHistoryItem();
			item.setParameter("section", "insurancepolicy");
			item.setStackParameter("display");
			item.pushIntoStackParameter("display", "search");
			item.setParameter("policyid", info.policyId);
			policy.setValue(item);

			policy.setValueName("#" + info.policyNumber + " - " + info.categoryName + " / " + info.lineName + " / " + info.subLineName + " (" + info.insurerName + ")");
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
		bonusMalusOption.setValue(info.isMalus == null ? null : info.isMalus ? "Malus" : "Bonus", true);
		bonusMalusValue.setValue(info.bonusMalus);
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
