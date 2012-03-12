package bigBang.library.client.userInterface;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import bigBang.definitions.shared.IncomingMessage;
import bigBang.library.client.FormField;

public class IncomingMessageFormField extends FormField<IncomingMessage>{

	
	private static final RadioButtonFormField HorizontalPanel = null;
	private TextAreaFormField notes = new TextAreaFormField();
	private IncomingMessage message;
	private RadioButtonFormField noteOrEmailRadioButton;
	
	public IncomingMessageFormField(){
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		VerticalPanel choices = new VerticalPanel();
		
		noteOrEmailRadioButton = new RadioButtonFormField();
		noteOrEmailRadioButton.addOption("EMAIL", "E-mail");
		noteOrEmailRadioButton.addOption("NOTA", "Nota");
		
		HorizontalPanel noteOrEmail = new HorizontalPanel();
		VerticalPanel left = new VerticalPanel();
		VerticalPanel right = new VerticalPanel();
		
		
		
		notes = new TextAreaFormField("Notas");
		notes.setSize("100%", "300px");
		
		
	}
	
	@Override
	public void setValue(IncomingMessage value) {
		
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


