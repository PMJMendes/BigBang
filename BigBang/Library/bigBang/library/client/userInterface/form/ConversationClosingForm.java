package bigBang.library.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ConversationClosingForm extends FormView<String> {

	protected ExpandableListBoxFormField motive;
	
	public ConversationClosingForm(){
		motive = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONVERSATION_CLOSING_MOTIVES, "Motivo");
		addSection("Encerramento de processo de mensagem");
		addFormField(motive);
		
		setValidator(new ConversationClosingFormValidator(this));
	}
		
	
	@Override
	public String getInfo() {
		return motive.getValue();
	}

	@Override
	public void setInfo(String info) {
		motive.setValue(info);
	}

}
