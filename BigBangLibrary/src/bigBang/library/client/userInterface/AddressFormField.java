package bigBang.library.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.shared.Address;

public class AddressFormField extends FormField<Address> {

	private final String COUNTRY_DEFAULT_VALUE = "Portugal";
	
	private TextBox street1;
	private TextBox street2;
	private TextBox country;
	private TextBox zipCode1;
	private TextBox zipCode2;

	public AddressFormField(FieldValidator<Address> validator) {
		this();
		setValidator(validator);
	}
	
	public AddressFormField(String label){
		this();
	}
	
	public AddressFormField(){
		street1 = new TextBox();
		street2 = new TextBox();
		country = new TextBox();
		zipCode1 = new TextBox();
		zipCode2 = new TextBox();
		
		street1.setWidth("100%");
		street2.setWidth("100%");
		country.setText(COUNTRY_DEFAULT_VALUE);
		zipCode1.setMaxLength(4);
		zipCode1.setWidth("50px");
		zipCode2.setMaxLength(3);
		zipCode2.setWidth("30px");
		
		Grid wrapper = new Grid(4, 3);
		wrapper.getColumnFormatter().setWidth(0, "100px");
		
		wrapper.setWidget(0, 0, new Label("Rua:"));
		wrapper.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(0, 1, street1);
		
		wrapper.setWidget(1, 1, street2);
		
		wrapper.setWidget(2, 0, new Label("Código postal:"));
		wrapper.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		HorizontalPanel zipCodeWrapper = new HorizontalPanel();
		zipCodeWrapper.add(zipCode1);
		zipCodeWrapper.add(new Label("-"));
		zipCodeWrapper.add(zipCode2);
		wrapper.setWidget(2, 1, zipCodeWrapper);
		
		wrapper.setWidget(3, 0, new Label("País:"));
		wrapper.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(3, 1, country);
		country.getElement().getStyle().setMargin(0, Unit.PX);
		
		wrapper.setWidth("550px");
		
		initWidget(wrapper);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		street1.setReadOnly(readOnly);
		street2.setReadOnly(readOnly);
		zipCode1.setReadOnly(readOnly);
		zipCode2.setReadOnly(readOnly);
		country.setReadOnly(readOnly);
		
		if(readOnly){
			street1.addStyleName("readonly");
			street2.addStyleName("readonly");
			zipCode1.addStyleName("readonly");
			zipCode2.addStyleName("readonly");
			country.addStyleName("readonly");
		}else{
			street1.removeStyleName("readonly");
			street2.removeStyleName("readonly");
			zipCode1.removeStyleName("readonly");
			zipCode2.removeStyleName("readonly");
			country.removeStyleName("readonly");
		}
	}

	@Override
	public boolean isReadOnly() {
		return this.street1.isReadOnly();
	}
	
	@Override
	public void setValue(Address address){
		street1.setValue(address.street1);
		street2.setValue(address.street2);
		zipCode1.setValue(address.zipCode.zip1);
		zipCode2.setValue(address.zipCode.zip2);
		country.setValue(address.country);
	}
	
	@Override
	public Address getValue(){
		Address result = new Address();
		result.street1 = street1.getValue();
		result.street2 = street2.getValue();
		result.zipCode.zip1 = zipCode1.getValue();
		result.zipCode.zip2 = zipCode2.getValue();
		result.country = country.getValue();
		return result;
	}
	
	public void clear(){
		street1.setValue("");
		street2.setValue("");
		zipCode1.setValue("");
		zipCode2.setValue("");
		country.setValue(COUNTRY_DEFAULT_VALUE);
	}

}
