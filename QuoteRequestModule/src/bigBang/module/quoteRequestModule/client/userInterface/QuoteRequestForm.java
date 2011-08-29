package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class QuoteRequestForm extends FormView<QuoteRequest> {

	protected ExpandableListBoxFormField processManager;
	protected TextBoxFormField daysUntilReply;
	protected CheckBoxFormField caseStudyFlag;
	protected TextAreaFormField notes;
	
	protected TextBoxFormField clientName;
	protected TextBoxFormField clientTaxNumber;
	protected TextBoxFormField clientGroup;
	
	public QuoteRequestForm(){
		processManager = new ExpandableListBoxFormField("Gestor do Processo");
		daysUntilReply = new TextBoxFormField("Prazo de resposta em dias");
		caseStudyFlag = new CheckBoxFormField("Estudo de caso");
		notes = new TextAreaFormField("Observações internas");
		
		addSection("Consulta de Mercado");
		
		addFormField(processManager);
		addFormField(daysUntilReply);
		addFormField(caseStudyFlag);
		addFormField(notes);
		
		clientName = new TextBoxFormField("Nome");
		clientTaxNumber = new TextBoxFormField("NIF");
		clientGroup = new TextBoxFormField("Grupo");
		
		clientName.setEditable(false);
		clientTaxNumber.setEditable(false);
		clientGroup.setEditable(false);
		
		addSection("Cliente");
		
		addFormField(clientName);
		addFormField(clientTaxNumber);
		addFormField(clientGroup);
		
		daysUntilReply.setFieldWidth("50");
		notes.setFieldHeight("150px");
	}
	
	@Override
	public QuoteRequest getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(QuoteRequest info) {
		// TODO Auto-generated method stub
		
	}

}
