package bigBang.library.client.userInterface;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.IncomingMessage.AttachmentUpgrade;
import bigBang.library.client.FormField;
import bigBang.library.shared.ExchangeItem;

public class IncomingMessageFormField extends FormField<IncomingMessage>{

	
	private static final RadioButtonFormField HorizontalPanel = null;
	private TextAreaFormField notes = new TextAreaFormField();
	private IncomingMessage message;
	private ExchangeItem mail;
	private RadioButtonFormField noteOrEmailRadioButton;
	private Button selectEmail;
	private TextBoxFormField subject;
	private TextBoxFormField from;
	private RichTextAreaFormField body;
	private List<AttachmentUpgrade> attachList;
	
	public IncomingMessageFormField(){
		
		//POSITIONING
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		VerticalPanel choices = new VerticalPanel();
		
		noteOrEmailRadioButton = new RadioButtonFormField();
		noteOrEmailRadioButton.addOption("EMAIL", "E-mail");
		noteOrEmailRadioButton.addOption("NOTA", "Nota");
		
		noteOrEmailRadioButton.setValue("EMAIL");
		choices.add(noteOrEmailRadioButton);
		wrapper.add(choices);
		
		HorizontalPanel noteOrEmail = new HorizontalPanel();
		VerticalPanel leftNote = new VerticalPanel();
		VerticalPanel leftEmail = new VerticalPanel();
		VerticalPanel right = new VerticalPanel();
		
		notes = new TextAreaFormField("Notas");
		notes.setSize("100%", "300px");
		leftNote.add(notes);
		
		
		mail = new ExchangeItem();
		subject = new TextBoxFormField("Assunto");
		from = new TextBoxFormField("De");
		body = new RichTextAreaFormField();
		attachList = new List<IncomingMessage.AttachmentUpgrade>();
		selectEmail = new Button("Seleccionar E-mail");
		leftEmail.add(selectEmail);
		leftEmail.add(from);
		leftEmail.add(subject);
		leftEmail.add(body);
		
		right.add(attachList);
		
		noteOrEmail.add(leftEmail);
		noteOrEmail.add(leftNote);
		noteOrEmail.add(right);
		
		attachList = new List<IncomingMessage.AttachmentUpgrade>();
		
		
	}
	
	@Override
	public void setValue(IncomingMessage value) {
		
		
		//TODO
		message = value;
		notes.setValue(value.notes);
		
		
		super.setValue(value);

	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReadOnly(boolean readonly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLabelWidth(String width) {
		// TODO Auto-generated method stub
		
	}
		
}


