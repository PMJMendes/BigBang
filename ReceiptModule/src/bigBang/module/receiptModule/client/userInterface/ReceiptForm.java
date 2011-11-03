package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.Receipt;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiptForm extends FormView<Receipt> {

	protected TextBoxFormField number;
	protected TextBoxFormField client;
	protected TextBoxFormField policyNumber;
	protected TextBoxFormField policyDetails;
	// |
	// V
	//categoria
	//ramo
	//modalidade
	protected ExpandableListBoxFormField type;
	protected TextBoxFormField totalPremium;
	protected TextBoxFormField salesPremium;
	protected TextBoxFormField commission;
	protected TextBoxFormField retro; //retrocessions
	protected TextBoxFormField fat;
	protected DatePickerFormField issueDate;
	protected DatePickerFormField coverageStart; //maturity date
	protected DatePickerFormField coverageEnd; //end date
	protected DatePickerFormField dueDate; //due date
	protected ExpandableListBoxFormField manager; 
	protected ExpandableListBoxFormField mediator;
	protected TextAreaFormField description;
	protected TextAreaFormField notes;
	
	public ReceiptForm(){
		number = new TextBoxFormField("Número");
		type = new ExpandableListBoxFormField("Tipo");
		client = new TextBoxFormField("Cliente");
		policyNumber = new TextBoxFormField("Apólice");
		policyDetails = new TextBoxFormField();
		totalPremium = new TextBoxFormField("Prémio Total");
		salesPremium = new  TextBoxFormField("Pŕemio Comercial");
		commission = new TextBoxFormField("Commissão");
		retro = new TextBoxFormField("Retrocessões");
		fat = new TextBoxFormField("FAT");
		issueDate = new DatePickerFormField("Data de Emissão");
		coverageStart = new DatePickerFormField("Início");
		coverageEnd = new DatePickerFormField("Fim");
		dueDate = new DatePickerFormField("Limite de Pagamento");
		mediator = new ExpandableListBoxFormField("Mediador");
		manager = new ExpandableListBoxFormField("Gestor");
		
		addSection("Informação Geral");
		addFormField(number);
		addFormField(type);
		addFormField(client);
		client.setEditable(false);
		addFormField(policyNumber);
		policyNumber.setEditable(false);
		addFormField(policyDetails);
		policyDetails.setEditable(false);
		addFormField(manager);
		addFormField(mediator);
		addFormField(totalPremium);
		addFormField(salesPremium);
		addFormField(commission);
		addFormField(fat);
		addFormField(retro);
		addFormField(issueDate);
		addFormField(dueDate);
		addFormField(description);
		addFormField(notes);
		
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
