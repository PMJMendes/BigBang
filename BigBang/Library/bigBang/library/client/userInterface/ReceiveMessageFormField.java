package bigBang.library.client.userInterface;


import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Attachment;
import bigBang.definitions.shared.Message.Kind;
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

public class ReceiveMessageFormField extends FormField<Message>{

	private RichTextAreaFormField notes;
	private Message message;
	private RadioButtonFormField noteOrEmailRadioButton;
	private Button selectEmail;
	private TextBoxFormField subject;
	private TextBoxFormField from;
	private RichTextAreaFormField body;
	private List<Attachment> attachList;
	VerticalPanel notePanel;
	VerticalPanel emailPanel;
	VerticalPanel right;

	PopupPanel popup = new PopupPanel();
	private boolean readOnly;
	private ExchangeServiceAsync service;
	private ExchangeItemSelectionViewPresenter presenter;
	protected boolean firstTime = true;

	public ReceiveMessageFormField(){

		message = new Message();
		service = ExchangeService.Util.getInstance();
		//POSITIONING
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);

		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		wrapper.setSize("100%", "100%");

		VerticalPanel choices = new VerticalPanel();

		noteOrEmailRadioButton = new RadioButtonFormField();
		noteOrEmailRadioButton.addOption(Kind.EMAIL.toString(), "E-mail");
		noteOrEmailRadioButton.addOption(Kind.NOTE.toString(), "Nota");		

		choices.add(noteOrEmailRadioButton);
		wrapper.add(choices);

		VerticalPanel noteOrEmail = new VerticalPanel();

		emailPanel  = new VerticalPanel();
		right =  new VerticalPanel();
		notePanel = new VerticalPanel();

		notePanel.setVisible(false);


		notes = new RichTextAreaFormField();
		notePanel.add(notes);

		subject = new TextBoxFormField("Assunto");
		from = new TextBoxFormField("De");
		body = new RichTextAreaFormField("Corpo da Mensagem");
		body.showToolbar(false);
		attachList = new List<Message.Attachment>();
		selectEmail = new Button("Seleccionar E-mail");
		emailPanel.add(selectEmail);
		emailPanel.add(from);
		from.setReadOnlyInternal(true);
		emailPanel.add(subject);
		subject.setReadOnlyInternal(true);
		body.setReadOnlyInternal(true);
		body.setWidth("100%");
		body.field.setWidth("100%");
		noteOrEmail.setSize("100%", "100%");
		body.setWidth("100%");
		body.field.setWidth("100%");
		noteOrEmail.setSize("100%", "100%");
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

		attachList = new List<Message.Attachment>();
		attachList.setSize("250px", "100%");
		ListHeader header = new ListHeader("Novos Documentos");
		attachList.setHeaderWidget(header);
		HorizontalPanel emailAndDocs = new HorizontalPanel();
		emailAndDocs.setSize("100%", "100%");
		emailAndDocs.add(body);
		emailAndDocs.add(attachList);
		emailAndDocs.setCellHeight(body, "100%");
		emailAndDocs.setCellWidth(body, "100%");
		emailAndDocs.setCellWidth(attachList, "250px");
		emailAndDocs.setCellHeight(attachList, "100%");
		emailPanel.add(emailAndDocs);
		wrapper.add(noteOrEmail);

		popup = new PopupPanel();
		ExchangeItemSelectionView itemView = (ExchangeItemSelectionView) GWT.create(ExchangeItemSelectionView.class);
		presenter = new ExchangeItemSelectionViewPresenter(itemView);
		presenter.go(popup);


		presenter.addValueChangeHandler(new ValueChangeHandler<Message>() {

			@Override
			public void onValueChange(ValueChangeEvent<Message> event) {
				if(event.getValue() == null){
					popup.hidePopup();
				}
				else{
					ReceiveMessageFormField.this.setValue(event.getValue());
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
				notePanel.setVisible(!event.getValue().equalsIgnoreCase(Kind.EMAIL.toString()));
				emailPanel.setVisible(event.getValue().equalsIgnoreCase(Kind.EMAIL.toString()));
				message.kind = event.getValue().equals(Kind.EMAIL.toString()) ? Kind.EMAIL : Kind.NOTE;
			}
		});

	}

	@Override
	public void setValue(final Message value) {

		message = value;

		if(value == null){
			clear();
			return;
		}

		attachList.clear();

		noteOrEmailRadioButton.setValue(value.kind != null ? value.kind.toString() : Kind.NOTE.toString());
		if(value.emailId != null){
			attachList.setVisible(true);
			service.getItem(value.emailId, new BigBangAsyncCallback<ExchangeItem>() {

				@Override
				public void onResponseSuccess(ExchangeItem result) {

					from.setValue(result.from);
					subject.setValue(result.subject);
					body.setValue(result.body);

					for(int i = 0; i<value.attachments.length; i++){
						ExpandableListBoxFormField type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, value.attachments[i].name);
						type.setReadOnlyInternal(true);
						ListEntry<Attachment> temp = new ListEntry<Message.Attachment>(value.attachments[i]);
						type.setValue(value.attachments[i].docTypeId);
						temp.setWidget(type);
						temp.setHeight("55px");
						attachList.add(temp);
					}

				}

				@Override
				public void onResponseFailure(Throwable caught) {
					super.onResponseFailure(caught);
				};
			});
		}
		else{
			notes.setValue(value.text);
			body.setValue(value.text);
			attachList.setVisible(false);
		}

	}

	@Override
	public Message getValue() {
		message.text = notes.getValue();
		if(message.kind != null){
			message.kind = noteOrEmailRadioButton.getValue().equals(Kind.EMAIL.name()) ? Kind.EMAIL : Kind.NOTE;
		}
		return message;
	}

	@Override
	public void clear() {
		message = new Message();
		from.clear();
		subject.clear();
		body.clear();
		notes.clear();
		setValue(message);
		noteOrEmailRadioButton.setValue(Kind.EMAIL.toString());
	}


	@Override
	protected void setReadOnlyInternal(boolean readonly) {
		readOnly = readonly;
		notes.setReadOnlyInternal(readonly);
		notes.showToolbar(!readonly);
		selectEmail.setEnabled(!readonly);
		noteOrEmailRadioButton.setEditable(!readonly);
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setLabelWidth(String width) {
		return;

	}

	@Override
	public void focus() {
		noteOrEmailRadioButton.focus();
	}

}


