package bigBang.library.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import bigBang.definitions.shared.ZipCode;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.FormField;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.interfaces.ZipCodeService;
import bigBang.library.interfaces.ZipCodeServiceAsync;

public class ZipCodeFormField extends FormField<ZipCode>{

	private ZipCodeEditPanel managementPanel;
	private TextBox code;
	private TextBox city;
	private TextBox county;
	private TextBox district;
	private TextBox country;
	protected Image expandImage;
	private boolean readOnly;
	private Grid wrapper;
	PopupPanel popup;
	private ZipCodeServiceAsync service;

	public ZipCodeFormField() {

		wrapper = new Grid(5,3);
		initWidget(wrapper);

		service = ZipCodeService.Util.getInstance();

		code = new TextBox();
		city = new TextBox();
		county = new TextBox();
		district = new TextBox();
		country = new TextBox();

		wrapper.getElement().getStyle().setMargin(0, Unit.PX);

		Resources r = GWT.create(Resources.class);
		expandImage = new Image(r.listExpandIcon());
		expandImage.getElement().getStyle().setCursor(Cursor.POINTER);

		wrapper.setWidget(0, 0, new Label("Código postal:"));
		wrapper.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.getCellFormatter().setStyleName(0, 0, "bigBangFormFieldLabel");
		wrapper.setWidget(0, 1, code);
		wrapper.setWidget(0, 2, expandImage);

		wrapper.setWidget(1, 0, new Label("Localidade:"));
		wrapper.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.getCellFormatter().setStyleName(1, 0, "bigBangFormFieldLabel");
		wrapper.setWidget(1, 1, city);

		wrapper.setWidget(2, 0, new Label("Concelho:"));
		wrapper.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.getCellFormatter().setStyleName(2, 0, "bigBangFormFieldLabel");
		wrapper.setWidget(2, 1, county);

		wrapper.setWidget(3, 0, new Label("Distrito:"));
		wrapper.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.getCellFormatter().setStyleName(3, 0, "bigBangFormFieldLabel");
		wrapper.setWidget(3, 1, district);

		wrapper.setWidget(4, 0, new Label("País:"));
		wrapper.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.getCellFormatter().setStyleName(4, 0, "bigBangFormFieldLabel");
		wrapper.setWidget(4, 1, country);
		country.getElement().getStyle().setMargin(0, Unit.PX);

		code.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchForCode();
			}


		});


		city.setEnabled(false);
		country.setEnabled(false);
		county.setEnabled(false);
		district.setEnabled(false);
		city.addStyleName("gwt-TextBox-readonly");
		county.addStyleName("gwt-TextBox-readonly");
		district.addStyleName("gwt-TextBox-readonly");
		country.addStyleName("gwt-TextBox-readonly");
		code.getElement().getStyle().setBackgroundColor("transparent");
		city.getElement().getStyle().setBorderColor("transparent");
		country.getElement().getStyle().setBorderColor("transparent");
		district.getElement().getStyle().setBorderColor("transparent");
		county.getElement().getStyle().setBorderColor("transparent");
		city.getElement().getStyle().setBackgroundColor("transparent");
		country.getElement().getStyle().setBackgroundColor("transparent");
		county.getElement().getStyle().setBackgroundColor("transparent");
		district.getElement().getStyle().setBackgroundColor("transparent");
		code.getElement().getStyle().setBackgroundColor("transparent");
		
		code.setMaxLength(8);
		code.setVisibleLength(8);

		popup = new PopupPanel();
		managementPanel = new ZipCodeEditPanel();
		popup.add(managementPanel);

		expandImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				managementPanel.getForm().setValue(getValue(), false);
				popup.center();
			}
		});

		managementPanel.addValueChangeHandler(new ValueChangeHandler<ZipCode>() {

			@Override
			public void onValueChange(ValueChangeEvent<ZipCode> event) {
				if(event.getValue() == null){
					popup.hidePopup();
				}
				else{
					setValue(event.getValue());
					popup.hidePopup();
				}

			}
		});
	}

	protected void searchForCode() {

		service.getZipCode(code.getValue(), new BigBangAsyncCallback<ZipCode>() {

			@Override
			public void onResponseSuccess(ZipCode result) {
				if(result == null){
					result = new ZipCode();
					result.code = code.getValue();
				}

				setValue(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				super.onResponseFailure(caught);
			}


		});


	}

	@Override
	protected void onAttach() {
		super.onAttach();
		getElement().getStyle().setMargin(0, Unit.PX);
	}

	@Override
	public void setValue(ZipCode value) {

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

	@Override
	public ZipCode getValue() {
		ZipCode newZip = new ZipCode();

		newZip.city = city.getValue();
		newZip.code = code.getValue();
		newZip.country = country.getValue();
		newZip.county = county.getValue();
		newZip.district = district.getValue();

		return newZip;
	}

	@Override
	public void clear() {

		code.setValue("");
		city.setValue("");
		country.setValue("");
		county.setValue("");
		district.setValue("");
	}

	@Override
	public void setReadOnly(boolean readonly) {
		readOnly = readonly;

		code.setReadOnly(readonly);
		expandImage.setVisible(!readonly);

		code.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "gray");

	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setLabelWidth(String width) {
		wrapper.getColumnFormatter().setWidth(0, width);
	}
	@Override
	public void focus() {
		code.getElement().focus();
	}

}
