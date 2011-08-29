package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.Receipt;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiptForm extends FormView<Receipt> {

	protected TextBoxFormField number;
	protected ExpandableListBoxFormField type;
	protected TextBoxFormField totalPrize;
	protected TextBoxFormField salesPrize;
	protected TextBoxFormField commission;
	protected ExpandableListBoxFormField fat;
	protected ExpandableListBoxFormField retro; //TODO
	protected DatePickerFormField emissionDate;
	protected DatePickerFormField coverageStart;
	protected DatePickerFormField coverageEnd;
	protected DatePickerFormField paymentLimit;
	protected ExpandableListBoxFormField manager;
	protected ExpandableListBoxFormField mediator;	
	
	public ReceiptForm(){
		number = new TextBoxFormField("Número");
		type = new ExpandableListBoxFormField("Tipo");
		totalPrize = new TextBoxFormField("Prémio Total");
		salesPrize = new  TextBoxFormField("Pŕemio Comercial");
		commission = new TextBoxFormField("Commissão");
		fat = new ExpandableListBoxFormField("FAT");
		retro = new ExpandableListBoxFormField("Retrocessões");
		emissionDate = new DatePickerFormField("Data de Emissão");
		coverageStart = new DatePickerFormField("Início");
		coverageEnd = new DatePickerFormField("Fim");
		paymentLimit = new DatePickerFormField("Limite de Pagamento");
		mediator = new ExpandableListBoxFormField("Mediador");
		manager = new ExpandableListBoxFormField("Gestor");
		
		addSection("Informação Geral");
		addFormField(number);
		addFormField(type);
		addFormField(manager);
		addFormField(mediator);
		addFormField(totalPrize);
		addFormField(salesPrize);
		addFormField(commission);
		addFormField(fat);
		addFormField(retro);
		addFormField(emissionDate);
		addFormField(paymentLimit);
		
		addSection("Período Coberto");
		addFormField(coverageStart);
		addFormField(coverageEnd);
		
	}
	
	@Override
	public Receipt getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(Receipt info) {
		// TODO Auto-generated method stub
		
	}

}
