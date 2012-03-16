package bigBang.library.client.userInterface.view;

import java.sql.Date;

import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.shared.ExchangeItem;

public class ExchangeItemForm extends FormView<ExchangeItem>{

	protected TextBoxFormField from;
	protected TextBoxFormField timestamp;
	protected TextBoxFormField subject;
	protected RichTextAreaFormField body;
	
	
	public ExchangeItemForm() {
		
		from = new TextBoxFormField("De");
		subject = new TextBoxFormField("Assunto");
		timestamp = new TextBoxFormField("Data");
		body = new RichTextAreaFormField("Corpo da Mensagem");
		body.getNativeField().setSize("400px", "310px");
		body.showToolbar(false);
		
		addSection("Detalhes do E-mail");
		addFormField(from);
		addFormField(subject);
		addFormField(timestamp);
		addFormField(body);
	}
	
	@Override
	public ExchangeItem getInfo() {
		ExchangeItem newItem = value;
		
		newItem.body = body.getValue();
		newItem.subject = subject.getValue();
		newItem.timestamp = timestamp.getValue();
		newItem.from = from.getValue();
		
		return newItem;
	}

	@Override
	public void setInfo(ExchangeItem info) {

		value = info;
		from.setValue(info.from);
		subject.setValue(info.subject);
		timestamp.setValue(info.timestamp);
		body.setValue(info.body);
	}


}