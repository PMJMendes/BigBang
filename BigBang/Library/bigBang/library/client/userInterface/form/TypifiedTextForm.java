package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class TypifiedTextForm extends FormView<TypifiedText>{
	
	
	protected RichTextAreaFormField textBody;
	protected TextBoxFormField subject;
	protected TextBoxFormField label;
	
	public TypifiedTextForm() {
		
		addSection("Texto");
		
		label = new TextBoxFormField("Etiqueta");
		subject = new TextBoxFormField("Assunto");
		textBody = new RichTextAreaFormField();
		
		subject.setFieldWidth("640px");
		textBody.setFieldWidth("640px");
		
		addFormField(label);
		addFormField(subject);
		addFormField(textBody);
		
		this.setValidator(new TypifiedTextFormValidator(this));
		
	}

	@Override
	public TypifiedText getInfo() {
		
		TypifiedText text = value;
		text.text = textBody.getValue();
		text.subject = subject.getValue();
		text.label = label.getValue();
		return text;
	}

	@Override
	public void setInfo(TypifiedText info) {
		label.setValue(info.label);
		subject.setValue(info.subject);
		textBody.setValue(info.text);
	}
	
	public void setLabelVisible(boolean visible){
		label.setVisible(visible);
	}

	@Override
	public void focus() {
		if(label.isVisible()){
			label.focus();
		}else{
			subject.focus();
		}
	}

}
