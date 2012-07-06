package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;

public class ViewInfoOrDocumentRequestForm extends
		FormView<InfoOrDocumentRequest> {

	private NumericTextBoxFormField replyLimit;
	private ExpandableSelectionFormField type;
	private TextBoxFormField subject;
	private RichTextAreaFormField text;
	private TextBoxFormField toUserNames;
	private TextBoxFormField internalBCCs;
	private TextBoxFormField externalCCs;
	
	public ViewInfoOrDocumentRequestForm(){
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("72px");
		type = new ExpandableSelectionFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Pedido");
		subject = new TextBoxFormField("Assunto");
		subject.setFieldWidth("100%");
		text = new RichTextAreaFormField();
		text.showToolbar(false);
		toUserNames = new TextBoxFormField("Forward/Reply");
		internalBCCs = new TextBoxFormField("BCC");
		externalCCs = new TextBoxFormField("CC");
				
		addSection("Detalhes do Pedido");
		addFormField(type, true);
		addFormField(replyLimit, false);
		addFormField(toUserNames);
		addFormField(internalBCCs);
		addFormField(externalCCs);
		addFormField(subject);
		addFormField(text);
		
		setReadOnly(true);
	}
	
	@Override
	public InfoOrDocumentRequest getInfo() {
		return this.value;
	}

	@Override
	public void setInfo(InfoOrDocumentRequest info) {
		if(info == null) {
			clearInfo();
		}else{
			replyLimit.setValue(info.replylimit == null ? null : (double)info.replylimit);
			type.setValue(info.requestTypeId);
			subject.setValue(info.message.subject);
			text.setValue(info.message.text);
			
			StringBuilder userNames = new StringBuilder();
			for(String user : info.message.forwardUserFullNames){
				userNames.append(user).append(",");
			}
			toUserNames.setValue(userNames.toString());
			internalBCCs.setValue(info.message.internalBCCs);
			externalCCs.setValue(info.message.externalCCs);
		}
	}

}
