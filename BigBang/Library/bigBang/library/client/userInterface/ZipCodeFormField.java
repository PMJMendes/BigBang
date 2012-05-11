package bigBang.library.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import bigBang.definitions.shared.ZipCode;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.FormField;
import bigBang.library.client.resources.Resources;
import bigBang.library.interfaces.ZipCodeService;
import bigBang.library.interfaces.ZipCodeServiceAsync;

public class ZipCodeFormField extends FormField<ZipCode>{
	
//	private ZipCodeEditPanel managementPanel;
	private TextBox code;
	private TextBox city;
	private TextBox county;
	private TextBox district;
	private TextBox country;
	protected Image expandImage;
	private boolean readOnly;
	private Grid wrapper;
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
		wrapper.setWidget(0, 1, code);
		wrapper.setWidget(0, 2, expandImage);
		
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
		
		code.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchForCode();
			}
		});
		
		
//		city.addStyleName("readonly");
//		county.addStyleName("readonly");
//		district.addStyleName("readonly");
//		country.addStyleName("readonly");
		
		code.setMaxLength(8);
		code.setVisibleLength(8);
	
//		expandImage.addClickHandler(new ClickHandler() {
//
//			final PopupPanel popup = new PopupPanel(true, "");
//
//			@Override
//			public void onClick(ClickEvent event) {
//				//TODO
//				
//				//popup.clear();
//				//popup.add(managementPanel);
//				
//			}
//		});
		
	}
	
	protected void searchForCode() {
		
		service.getZipCode(code.getValue(), new BigBangAsyncCallback<ZipCode>() {

			@Override
			public void onResponseSuccess(ZipCode result) {
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
		city.setReadOnly(readonly);
		country.setReadOnly(readonly);
		county.setReadOnly(readonly);
		district.setReadOnly(readonly);
		
		if(readonly){
			code.addStyleName("readonly");
			city.addStyleName("readonly");
			county.addStyleName("readonly");
			district.addStyleName("readonly");
			country.addStyleName("readonly");
		}
		else{
			code.removeStyleName("readonly");
			city.removeStyleName("readonly");
			county.removeStyleName("readonly");
			district.removeStyleName("readonly");
			country.removeStyleName("readonly");
		}
		
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setLabelWidth(String width) {
		wrapper.getColumnFormatter().setWidth(0, width);
	}

}
