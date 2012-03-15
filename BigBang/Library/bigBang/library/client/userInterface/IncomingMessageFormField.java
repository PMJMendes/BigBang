package bigBang.library.client.userInterface;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.IncomingMessage.AttachmentUpgrade;
import bigBang.definitions.shared.IncomingMessage.Kind;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.presenter.ExchangeItemSelectionViewPresenter;
import bigBang.library.client.userInterface.view.ExchangeItemSelectionView;
import bigBang.library.client.userInterface.view.PopupPanel;
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
	VerticalPanel notePanel;
	VerticalPanel emailPanel;
	VerticalPanel right;
	
	PopupPanel popup = new PopupPanel();
	
	public IncomingMessageFormField(){
		
		//POSITIONING
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		VerticalPanel choices = new VerticalPanel();
		
		noteOrEmailRadioButton = new RadioButtonFormField();
		noteOrEmailRadioButton.addOption(Kind.EMAIL.name(), "E-mail");
		noteOrEmailRadioButton.addOption(Kind.NOTE.name(), "Nota");
		
		noteOrEmailRadioButton.setValue("EMAIL");
		
		choices.add(noteOrEmailRadioButton);
		wrapper.add(choices);
		
		VerticalPanel noteOrEmail = new VerticalPanel();
		
		emailPanel  = new VerticalPanel();
		right =  new VerticalPanel();
		notePanel = new VerticalPanel();
		
		notePanel.setVisible(false);
		
		
		notes = new TextAreaFormField("Nota");
		notes.setSize("300px", "100%");
		notePanel.add(notes);
		
		mail = new ExchangeItem();
		subject = new TextBoxFormField("Assunto");
		from = new TextBoxFormField("De");
		body = new RichTextAreaFormField();
		attachList = new List<IncomingMessage.AttachmentUpgrade>();
		selectEmail = new Button("Seleccionar E-mail");
		emailPanel.add(selectEmail);
		emailPanel.add(from);
		emailPanel.add(subject);
		emailPanel.add(body);
		body.field.setWidth("100%");
		noteOrEmail.add(emailPanel);
		noteOrEmail.add(notePanel);
		noteOrEmail.add(right);
		
		attachList = new List<IncomingMessage.AttachmentUpgrade>();
		ListHeader header = new ListHeader("Anexos");
		attachList.setHeaderWidget(header);
		emailPanel.add(attachList);
		emailPanel.setCellHeight(attachList, "100%");
		wrapper.add(noteOrEmail);
		popup = new PopupPanel();
		ExchangeItemSelectionView itemView = (ExchangeItemSelectionView) GWT.create(ExchangeItemSelectionView.class);
		ExchangeItemSelectionViewPresenter presenter = new ExchangeItemSelectionViewPresenter(itemView);
		presenter.go(popup);
		presenter.setParameters(null);
		
		presenter.addValueChangeHandler(new ValueChangeHandler<IncomingMessage>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<IncomingMessage> event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		selectEmail.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popup.center();
			}
		});
		
		noteOrEmailRadioButton.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				notePanel.setVisible(!event.getValue().equalsIgnoreCase(Kind.EMAIL.name()));
				emailPanel.setVisible(event.getValue().equalsIgnoreCase(Kind.EMAIL.name()));
			}
		});
		
	}
	
	@Override
	public void setValue(IncomingMessage value) {
		
		
		//TODO
		message = value;
		notes.setValue(value.notes);
		
		
		super.setValue(value);

	}
	
	@Override
	public IncomingMessage getValue() {
		IncomingMessage newMessage = super.getValue();
		
		newMessage.kind = Kind.valueOf(noteOrEmailRadioButton.getValue());
		
		return newMessage;
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


