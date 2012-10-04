package bigBang.library.client.userInterface.form;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import bigBang.definitions.shared.ZipCode;
import bigBang.library.client.userInterface.view.FormView;

public class ZipCodeForm extends FormView<ZipCode>{

	
	private TextBox code;
	private TextBox city;
	private TextBox county;
	private TextBox district;
	private TextBox country;
	private Grid wrapper;

	
	public ZipCodeForm() {

		addSection("Detalhes do Código Postal");
		code = new TextBox();
		city = new TextBox();
		county = new TextBox();
		district = new TextBox();
		country = new TextBox();
		
		wrapper = new Grid(5,2);
		
		wrapper.setWidget(0, 0, new Label("Código postal:"));
		wrapper.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(0, 1, code);
		code.setReadOnly(true);
		code.addStyleName("readonly");

		wrapper.setWidget(1, 0, new Label("Localidade:"));
		wrapper.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(1, 1, city);

		wrapper.setWidget(2, 0, new Label("Concelho:"));
		wrapper.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(2, 1, county);

		wrapper.setWidget(3, 0, new Label("Distrito:"));
		wrapper.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(3, 1, district);

		wrapper.setWidget(4, 0, new Label("País:"));
		wrapper.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.setWidget(4, 1, country);
		country.getElement().getStyle().setMargin(0, Unit.PX);
	
		
		addWidget(wrapper);
	}
	
	@Override
	public ZipCode getInfo() {
		ZipCode newZip = new ZipCode();

		newZip.city = city.getValue();
		newZip.code = code.getValue();
		newZip.country = country.getValue();
		newZip.county = county.getValue();
		newZip.district = district.getValue();

		return newZip;
	}

	@Override
	public void setInfo(ZipCode value) {

		if(value == null){
			clear();
			return;
		}

		code.setValue(value.code);
		city.setValue(value.city);
		country.setValue(value.country);
		county.setValue(value.county);
		district.setValue(value.district);

	}
	
	public void clear() {

		code.setValue("");
		city.setValue("");
		country.setValue("");
		county.setValue("");
		district.setValue("");


	}
	

}
