package bigBang.library.client.userInterface.form;

import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.shared.MailItem;

public class MailItemForm extends FormView<MailItem>{

	protected TextBoxFormField from;
	protected TextBoxFormField timestamp;
	protected TextBoxFormField subject;
	protected RichTextAreaFormField body;
	
	
	public MailItemForm() {
		
		from = new TextBoxFormField("De");
		subject = new TextBoxFormField("Assunto");
		subject.getNativeField().setWidth("600px");
		timestamp = new TextBoxFormField("Data");
		body = new RichTextAreaFormField("Corpo da Mensagem");
		body.getNativeField().setSize("600px", "310px");
		body.showToolbar(false);
		
		addSection("Detalhes do E-mail");
		addFormField(from);
		addFormField(subject);
		addFormField(timestamp);
		addFormField(body);
	}
	
	@Override
	public MailItem getInfo() {
		MailItem newItem = value;
		
		newItem.body = body.getValue();
		newItem.subject = subject.getValue();
		newItem.timestamp = timestamp.getValue();
		newItem.from = from.getValue();
		
		return newItem;
	}

	@Override
	public void setInfo(MailItem info) {

		value = info;
		from.setValue(info.from);
		subject.setValue(info.subject);
		timestamp.setValue(info.timestamp);
		body.setValue(info.body);
	}

	public void setTextBoxSize(String string, String string2) {
		body.getNativeField().setSize(string, string2);
	}


}