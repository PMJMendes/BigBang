package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.TextAreaFormField;


public class QuoteRequestForm extends QuoteRequestHeaderForm {

	protected TextAreaFormField notes;

	public QuoteRequestForm() {
		super();
		addSection("Notas");
		notes = new TextAreaFormField("Notas");
		addFormField(notes);

		setValidator(new QuoteRequestFormValidator(this));
	}

	@Override
	public void setInfo(QuoteRequest value) {
		super.setInfo(value);
		if(value == null){
			notes.clear();
			return;
		}
		notes.setValue(value.notes);
	}

	@Override
	public QuoteRequest getInfo() {
		QuoteRequest value = super.getInfo();

		value.notes = notes.getValue();

		return value;
	};

}
