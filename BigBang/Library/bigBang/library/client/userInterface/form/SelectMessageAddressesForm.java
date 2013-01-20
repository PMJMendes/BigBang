package bigBang.library.client.userInterface.form;

import java.util.ArrayList;
import java.util.List;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.Message.MsgAddress;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectMessageAddressesForm extends FormField<MsgAddress[]>{

	protected boolean hasDummyValue = false;
	protected HorizontalPanel wrapper;
	ListBoxFormField contacts;
	List<Contact> list;
	private TextBoxFormField textField;
	private Button clear, cancel, add;
	private Contact[] currentAddresses;
	private boolean isReadOnly;

	public SelectMessageAddressesForm(String label,FieldValidator<MsgAddress[]> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}

	SelectMessageAddressesForm(FieldValidator<MsgAddress[]> validator){
		this();
		setValidator(validator);
	}

	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			this.label.setText("");

		}else{
			this.label.setText(label);
		}
	}

	public SelectMessageAddressesForm(String string) {
		this();
		setLabel(string);
	}

	public SelectMessageAddressesForm() {
		super();

		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		mainWrapper.add(this.label);

		contacts = new ListBoxFormField("");
		list = new ArrayList<Contact>();

		HorizontalPanel textWrapper = new HorizontalPanel();
		mainWrapper.add(textWrapper);

		textField = new TextBoxFormField(){
			@Override
			public void setReadOnly(boolean readonly) {
				return;
			}
		};
		clear = new Button("Limpar");
		add = new Button("Adicionar");
		cancel = new Button("Cancelar");

		textWrapper.add(textField);
		textWrapper.add(contacts);
		textWrapper.add(add);
		textWrapper.add(cancel);
		textWrapper.add(clear);

		textWrapper.setSpacing(4);

		contacts.setVisible(false);
		cancel.setVisible(false);


		textField.setEditable(false);		
		textField.getElement().getStyle().setBackgroundColor("white");
		textField.getElement().getStyle().clearMargin();
		textField.getParent().getElement().getStyle().clearMargin();
		textField.getNativeField().getElement().getStyle().clearMargin();

		add.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				switchContext(true);
			}
		});

		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				switchContext(false);

			}
		});

		clear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				textField.clear();
				contacts.setValue(null);
				list.clear();
			}
		});

		contacts.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(!(event.getValue() == null) && !event.getValue().isEmpty()){
					for(Contact contact : currentAddresses){
						if(contact.info[0].id.equals(event.getValue())){
							textField.setValue((textField.getValue() != null ? textField.getValue() : "") + contact.info[0].value + ", ");
							list.add(contact);
							switchContext(false);
							break;
						}
					}
				}
			}
		});

		contacts.setFieldWidth("400px");
	}

	protected void switchContext(boolean b) {
		add.setVisible(!b);
		textField.setVisible(!b);
		contacts.setVisible(b);
		cancel.setVisible(b);
		clear.setVisible(!b);		
		contacts.setValue(null);
	}

	@Override
	public void clear() {
		contacts.clearValues();
		list.clear();
	}

	@Override
	protected void setReadOnlyInternal(boolean readonly) {
		clear.setEnabled(!readonly);
		contacts.setReadOnly(readonly);
		isReadOnly = readonly;
	}

	@Override
	public boolean isReadOnly() {
		return isReadOnly;
	}

	@Override
	public void focus() {
		contacts.focus();
	}

	@Override
	public void setLabelWidth(String width) {
		this.label.setWidth(width);		
	}

	public void setContacts(Contact[] addresses){
		contacts.clearValues();
		currentAddresses = addresses;
		for(Contact add : addresses){
			ContactInfo info = add.info[0];
			contacts.addItem(add.name + " <" + info.value + ">", info.id);
		}
	}

	@Override
	public MsgAddress[] getValue() {

		MsgAddress[] toReturn = new MsgAddress[list.size()];

		int i = 0;
		for(Contact cont : list){
			toReturn[i] = new MsgAddress();
			toReturn[i].usage = MsgAddress.Usage.CC;
			toReturn[i].contactInfoId = cont.info[0].id;
			i++;
		}

		return toReturn;
	}


	@Override
	public void setValue(MsgAddress[] value) {

		for(MsgAddress add : value){
			Contact cont = new Contact();
			cont.info = new ContactInfo[1];
			cont.info[0] = new ContactInfo();
			cont.info[0].id = add.contactInfoId;
			cont.info[0].value = add.address;
			list.add(cont);
			textField.setValue((textField.getValue() != null ? textField.getValue() : "") + cont.info[0].value + ", ");
		}		
	}


}
