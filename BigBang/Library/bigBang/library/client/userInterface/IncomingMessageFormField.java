package bigBang.library.client.userInterface;


import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.IncomingMessage.AttachmentUpgrade;
import bigBang.definitions.shared.IncomingMessage.Kind;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.presenter.ExchangeItemSelectionViewPresenter;
import bigBang.library.client.userInterface.view.ExchangeItemSelectionView;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;
import bigBang.library.shared.ExchangeItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IncomingMessageFormField extends FormField<IncomingMessage>{

	private TextAreaFormField notes = new TextAreaFormField();
	private IncomingMessage message;
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
	private boolean readOnly;
	private ExchangeServiceAsync service;
	private ExchangeItemSelectionViewPresenter presenter;

	public IncomingMessageFormField(){

		service = ExchangeService.Util.getInstance();
		//POSITIONING
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);

		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel choices = new VerticalPanel();

		noteOrEmailRadioButton = new RadioButtonFormField();
		noteOrEmailRadioButton.addOption(Kind.EMAIL.name(), "E-mail");
		noteOrEmailRadioButton.addOption(Kind.NOTE.name(), "Nota");
		noteOrEmailRadioButton.setValue(Kind.EMAIL.name());

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

		subject = new TextBoxFormField("Assunto");
		from = new TextBoxFormField("De");
		body = new RichTextAreaFormField("Corpo da Mensagem");
		body.showToolbar(false);
		attachList = new List<IncomingMessage.AttachmentUpgrade>();
		selectEmail = new Button("Seleccionar E-mail");
		emailPanel.add(selectEmail);
		emailPanel.add(from);
		from.setReadOnly(true);
		emailPanel.add(subject);
		subject.setReadOnly(true);
		body.setReadOnly(true);
		noteOrEmail.add(emailPanel);
		noteOrEmail.add(notePanel);
		noteOrEmail.add(right);
		noteOrEmail.setCellHeight(emailPanel, "100%");
		noteOrEmail.setCellHeight(notePanel, "100%");

		noteOrEmail.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		emailPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		notePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

		notePanel.setSize("100%","100%");
		emailPanel.setSize("100%", "100%");

		attachList = new List<IncomingMessage.AttachmentUpgrade>();
		attachList.setSize("100%", "100%");
		ListHeader header = new ListHeader("Novos Documentos");
		attachList.setHeaderWidget(header);
		HorizontalPanel emailAndDocs = new HorizontalPanel();
		emailAndDocs.setSize("100%", "100%");
		emailAndDocs.add(body);
		emailAndDocs.add(attachList);
		emailAndDocs.setCellHeight(body, "100%");
		emailAndDocs.setCellWidth(body, "500px");
		emailAndDocs.setCellWidth(attachList, "100%");
		emailAndDocs.setCellHeight(attachList, "100%");
		emailPanel.add(emailAndDocs);
		wrapper.add(noteOrEmail);

		popup = new PopupPanel();
		ExchangeItemSelectionView itemView = (ExchangeItemSelectionView) GWT.create(ExchangeItemSelectionView.class);
		presenter = new ExchangeItemSelectionViewPresenter(itemView);
		presenter.go(popup);


		presenter.addValueChangeHandler(new ValueChangeHandler<IncomingMessage>() {

			@Override
			public void onValueChange(ValueChangeEvent<IncomingMessage> event) {
				if(event.getValue() == null){
					popup.hidePopup();
				}
				else{
					IncomingMessageFormField.this.setValue(event.getValue());
					message = event.getValue();
					popup.hidePopup();
				}

			}
		});

		selectEmail.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.center();
				presenter.setParameters(null);
			}
		});

		noteOrEmailRadioButton.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				notePanel.setVisible(!event.getValue().equalsIgnoreCase(Kind.EMAIL.name()));
				emailPanel.setVisible(event.getValue().equalsIgnoreCase(Kind.EMAIL.name()));
				message.kind = event.getValue().equals(Kind.EMAIL.name()) ? Kind.EMAIL : Kind.NOTE;
			}
		});

	}

	@Override
	public void setValue(final IncomingMessage value, final boolean fireEvents) {

		message = value;

		if(value == null || value.kind == null){
			clear();
			return;
		}

		attachList.clear();

		noteOrEmailRadioButton.setValue(value.kind.name());
		if(value.emailId != null){
			service.getItem(value.emailId, new BigBangAsyncCallback<ExchangeItem>() {

				@Override
				public void onResponseSuccess(ExchangeItem result) {

					from.setValue(result.from);
					subject.setValue(result.subject);
					body.setValue(result.body);

					for(int i = 0; i<value.upgrades.length; i++){
						ExpandableListBoxFormField type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, value.upgrades[i].name);
						type.setReadOnly(true);
						ListEntry<AttachmentUpgrade> temp = new ListEntry<IncomingMessage.AttachmentUpgrade>(value.upgrades[i]);
						type.setValue(value.upgrades[i].docTypeId);
						temp.setWidget(type);
						temp.setHeight("55px");
						attachList.add(temp);
					}

					if(fireEvents){
						ValueChangeEvent.fire(IncomingMessageFormField.this, value);
					}
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					super.onResponseFailure(caught);
				};
			});
		}
		else{
			notes.setValue(value.notes);

			if(fireEvents){
				ValueChangeEvent.fire(IncomingMessageFormField.this, value);

			}
		}

	}

	@Override
	public IncomingMessage getValue() {
		message.notes = notes.getValue();
		return message;
	}

	@Override
	public void clear() {
		message = new IncomingMessage();
		message.kind = Kind.EMAIL;
		from.clear();
		subject.clear();
		body.clear();
		notes.clear();
		setValue(message);
		
	}


	@Override
	public void setReadOnly(boolean readonly) {

		readOnly = readonly;
		notes.setReadOnly(readonly);
		selectEmail.setEnabled(!readonly);



	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setLabelWidth(String width) {
		return;

	}

}


