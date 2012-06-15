package bigBang.library.client.userInterface;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Address;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

public class AddressFormField extends FormField<Address> {
	
	private TextBox street1;
	private TextBox street2;	
	
	private ZipCodeFormField zipCode;

	protected Grid streetWrapper;
	private VerticalPanel wrapper;
	
	public AddressFormField(FieldValidator<Address> validator) {
		this();
		setValidator(validator);
	}
	
	public AddressFormField(String label){
		this();
	}
	
	public AddressFormField(){
		wrapper = new VerticalPanel();
		initWidget(wrapper);
	}
	
	protected void initializeView() {
		
		super.initializeView();
		streetWrapper = new Grid(2, 3);
		street1 = new TextBox();
		street2 = new TextBox();
		
		zipCode = new ZipCodeFormField();
		
		street1.setWidth("100%");
		street2.setWidth("100%");
		
		Label street = new Label("Rua:");
		street.setWidth("83px");
		streetWrapper.getColumnFormatter().setWidth(1, "100%");
		streetWrapper.setWidget(0, 0, street);
		streetWrapper.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		streetWrapper.setWidget(0, 1, street1);
		streetWrapper.setWidget(1, 1, street2);
		streetWrapper.getCellFormatter().setWidth(0, 0, "83px");
		
		wrapper.add(streetWrapper);
		wrapper.add(zipCode);
		wrapper.setWidth("550px");
	};

	@Override
	public void setReadOnly(boolean readOnly) {				
		
		street1.setEnabled(!readOnly);
		street2.setEnabled(!readOnly);
		zipCode.setReadOnly(readOnly);
		
		if(readOnly){
			street1.addStyleName("readonly");
			street2.addStyleName("readonly");
		}else{
			street1.removeStyleName("readonly");
			street2.removeStyleName("readonly");
		}
		
		street1.getElement().getStyle().setBackgroundColor(readOnly ? "transparent" : "white");
		street2.getElement().getStyle().setBackgroundColor(readOnly ? "transparent" : "white");
		street1.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "gray");
		street2.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "gray");
	}

	@Override
	public boolean isReadOnly() {
		return this.street1.isReadOnly();
	}
	
	@Override
	public void setValue(Address address){
		if(address == null){
			clear();
			return;
		}
		street1.setValue(address.street1 == null ? "": address.street1);
		street2.setValue(address.street2 == null ? "": address.street2);
		zipCode.setValue(address.zipCode);
	}
	
	@Override
	public Address getValue(){
		Address result = new Address();
		result.street1 = street1.getValue();
		result.street2 = street2.getValue();
		result.zipCode = zipCode.getValue();
		return result;
	}
	
	public void clear(){
		street1.setValue("");
		street2.setValue("");
		zipCode.clear();
	}

	@Override
	public void setLabelWidth(String width) {
		streetWrapper.getColumnFormatter().setWidth(0, width);
		zipCode.setLabelWidth(width);
	}


	@Override
	public void focus() {
		street1.setFocus(true);
	}

}
