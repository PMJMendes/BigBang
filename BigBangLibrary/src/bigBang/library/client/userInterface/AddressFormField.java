package bigBang.library.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import bigBang.definitions.shared.Address;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

public class AddressFormField extends FormField<Address> {

	private final String COUNTRY_DEFAULT_VALUE = "Portugal";
	
	private TextBox street1;
	private TextBox street2;	
	private TextBox code;
	private TextBox city;
	private TextBox county;
	private TextBox district;
	private TextBox country;

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
		code = new TextBox();
		city = new TextBox();
		county = new TextBox();
		district = new TextBox();
		country = new TextBox();
		
		code.setMaxLength(8);
		code.setVisibleLength(8);
		street1.setWidth("100%");
		street2.setWidth("100%");
		country.setText(COUNTRY_DEFAULT_VALUE);
		
		Grid wrapper = new Grid(7, 3);
		wrapper.getColumnFormatter().setWidth(1, "100%");
		
		wrapper.setWidget(0, 0, new Label("Rua:"));
		wrapper.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(0, 1, street1);
		wrapper.setWidget(1, 1, street2);
		
		wrapper.setWidget(2, 0, new Label("Código postal:"));
		wrapper.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(2, 1, code);
		
		wrapper.setWidget(3, 0, new Label("Cidade:"));
		wrapper.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(3, 1, city);
		
		wrapper.setWidget(4, 0, new Label("Concelho:"));
		wrapper.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(4, 1, county);
		
		wrapper.setWidget(5, 0, new Label("Distrito:"));
		wrapper.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(5, 1, district);
		
		wrapper.setWidget(6, 0, new Label("País:"));
		wrapper.getCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(6, 1, country);
		country.getElement().getStyle().setMargin(0, Unit.PX);
		
		wrapper.setWidth("550px");
		
		initWidget(wrapper);
	}

	@Override
	public void setReadOnly(boolean readOnly) {				
		street1.setReadOnly(readOnly);
		street2.setReadOnly(readOnly);
		code.setReadOnly(readOnly);
		city.setReadOnly(readOnly);
		county.setReadOnly(readOnly);
		district.setReadOnly(readOnly);
		country.setReadOnly(readOnly);
		
		if(readOnly){
			street1.addStyleName("readonly");
			street2.addStyleName("readonly");
			code.addStyleName("readonly");
			city.addStyleName("readonly");
			county.addStyleName("readonly");
			district.addStyleName("readonly");
			country.addStyleName("readonly");
		}else{
			street1.removeStyleName("readonly");
			street2.removeStyleName("readonly");
			code.removeStyleName("readonly");
			city.removeStyleName("readonly");
			county.removeStyleName("readonly");
			district.removeStyleName("readonly");
			country.removeStyleName("readonly");
		}
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
		code.setValue(address.zipCode == null ? "": address.zipCode.code);
		city.setValue(address.zipCode == null || address.zipCode.city == null ? "" : address.zipCode.city);
		county.setValue(address.zipCode == null || address.zipCode.county == null ? "" : address.zipCode.county);
		district.setValue(address.zipCode == null || address.zipCode.district == null ? "" : address.zipCode.district);
		country.setValue(address.zipCode == null || address.zipCode.country == null ? "" : address.zipCode.country);
	}
	
	@Override
	public Address getValue(){
		Address result = new Address();
		result.street1 = street1.getValue();
		result.street2 = street2.getValue();
		result.zipCode.code = code.getValue();
		result.zipCode.city = city.getValue();
		result.zipCode.county = county.getValue();
		result.zipCode.district = district.getValue();
		result.zipCode.country = country.getValue();
		return result;
	}
	
	public void clear(){
		street1.setValue("");
		street2.setValue("");
		code.setValue("");
		city.setValue("");
		county.setValue("");
		district.setValue("");
		country.setValue(COUNTRY_DEFAULT_VALUE);
		getValue();
	}

	@Override
	public void setLabelWidth(String width) {
		// TODO Auto-generated method stub
		
	}

}
