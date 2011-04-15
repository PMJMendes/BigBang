package bigBang.library.client.userInterface.view;

import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.shared.Contact;

public class ContactForm extends FormView<Contact> {

	protected TextBoxFormField name;
	protected TextBoxFormField value;
	protected AddressFormField address;
	protected ExpandableListBoxFormField type;
	
	protected List<Contact> subContactsList;
	
	public ContactForm(){
		addSection("Informação do Contacto");
		name = new TextBoxFormField("Nome");
		value = new TextBoxFormField("Valor");
		address = new AddressFormField();
		type = new ExpandableListBoxFormField("Tipo");
		
		addFormField(type);
		addFormField(name);
		addFormField(value);
		
		name.setFieldWidth("200px");
		value.setFieldWidth("200px");
		
		addSection("Morada");
		addFormField(address);
		
		addSection("Contactos associados");
		subContactsList = new List<Contact> ();
		subContactsList.setSize("100%", "100%");
		addWidget(subContactsList);
	}

	@Override
	public Contact getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(Contact info) {
		// TODO Auto-generated method stub
		
	}

}
