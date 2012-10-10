package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ViewExternalInfoRequestForm extends FormView<ExternalInfoRequest> {

	private TextBoxFormField subject;
	private TextBoxFormField from;
	private NumericTextBoxFormField replyLimit;
	private RichTextAreaFormField body;
	
	public ViewExternalInfoRequestForm(){
		subject = new TextBoxFormField("Assunto");
		from = new TextBoxFormField("De");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta (dias)", false);
		replyLimit.setFieldWidth("72px");
		body = new RichTextAreaFormField();
		body.showToolbar(false);
		body.setWidth("100%");
		body.field.setWidth("100%");
		
		subject.setEditable(false);
		from.setEditable(false);
		replyLimit.setEditable(false);
		
		addSection("Informação do Pedido Externo");
		addFormField(from, true);
		addFormField(replyLimit, false);
		addFormField(subject);
		addFormField(body);
	}
	
	@Override
	public ExternalInfoRequest getInfo() {
		ExternalInfoRequest result = getValue();
		if(result != null) {
			result.subject = subject.getValue();
			result.replylimit = replyLimit.getValue().intValue();
			result.originalFrom = from.getValue();
		}
		return result;
	}

	@Override
	public void setInfo(ExternalInfoRequest info) {
		if(info == null){
			clearInfo();
		} else {
			subject.setValue(info.subject);
			from.setValue(info.originalFrom);
			replyLimit.setValue(info.replylimit == null ? null : (double)info.replylimit);
			body.setValue(info.message.notes);
		}
	}

}
