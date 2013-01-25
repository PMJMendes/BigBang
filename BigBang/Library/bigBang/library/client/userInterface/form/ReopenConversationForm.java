package bigBang.library.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReopenConversationForm extends FormView<String[]>{

	protected NumericTextBoxFormField days;
	protected ListBoxFormField direction;
	
	public ReopenConversationForm() {
		addSection("Reabertura de Troca de Mensagens");
		
		days = new NumericTextBoxFormField("Prazo limite", false);
		days.setFieldWidth("50px");
		days.setUnitsLabel("dias");
		
		direction = new ListBoxFormField("Direcção da próxima mensagem");
		direction.addItem("Enviar", BigBangConstants.EntityIds.CONVERSATION_DIRECTION_OUTGOING);
		direction.addItem("Receber", BigBangConstants.EntityIds.CONVERSATION_DIRECTION_INCOMING);
		
		addFormField(days);
		addFormField(direction);
	
		setValidator(new ReopenConversationFormValidator(this));
	}
	

	@Override
	public void focus() {
		days.focus();
	}

	@Override
	public String[] getInfo() {
		
		String[] toReturn = new String[2];
		
		toReturn[0] = direction.getValue();
		toReturn[1] = days.getStringValue();
		
		return toReturn;
	}


	@Override
	public void setInfo(String[] info) {
		
		direction.clear();
		days.clear();
		
	}
	
}

