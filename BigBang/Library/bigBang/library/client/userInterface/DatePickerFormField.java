package bigBang.library.client.userInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;


public class DatePickerFormField extends FormField<Date> {

	protected static final String DEFAULT_FORMAT = "yyyy-MM-dd";
	private final String EMPTY_VALUE_PLACEHOLDER  = "-";

	protected TextBox day, month, year;	
	private boolean readonly;
	protected Label secondSlash;
	protected DateTimeFormat format;

	public DatePickerFormField(){
		this("");
	}

	public DatePickerFormField(String label){
		this(label, DEFAULT_FORMAT);
	}

	public DatePickerFormField(String label, String format){
		this(label, format, null);
	}

	public DatePickerFormField(String label, String format, FieldValidator<Date> validator){
		super();
		secondSlash = new Label("/");
		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		this.setValidator(validator);

		this.format = DateTimeFormat.getFormat(format);

		mainWrapper.add(this.label);
		this.label.setText(label);
		HorizontalPanel wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		day = new TextBox();
		day.setWidth("20px");
		day.setMaxLength(2);
		day.setAlignment(TextAlignment.CENTER);
		month = new TextBox();
		month.setWidth("20px");
		month.setMaxLength(2);
		month.setAlignment(TextAlignment.CENTER);
		year = new TextBox();
		year.setWidth("35px");
		year.setMaxLength(4);
		year.setAlignment(TextAlignment.CENTER);

		KeyPressHandler keyPressHandler = new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(!Character.isDigit((char)event.getUnicodeCharCode())){
					event.preventDefault();
				}
				if(event.getSource() == day){
					onDayChanged(event.getUnicodeCharCode());
				}else if(event.getSource() == month){
					onMonthChanged(event.getUnicodeCharCode());
				}else if(event.getSource() == year){
					onYearChanged(event.getUnicodeCharCode());
				}
			}
		};

		day.addKeyPressHandler(keyPressHandler);
		month.addKeyPressHandler(keyPressHandler);
		year.addKeyPressHandler(keyPressHandler);

		BlurHandler blurHandler = new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				validateDate();
			}
		};
		
		day.addBlurHandler(blurHandler);
		month.addBlurHandler(blurHandler);
		year.addBlurHandler(blurHandler);
		
		wrapper.add(day);
		wrapper.add(new Label("/"));
		wrapper.add(month);
		wrapper.add(secondSlash);
		wrapper.add(year);

		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);
	}

	protected boolean validateDate(){
		String day = this.day.getValue();
		String month = this.month.getValue();
		String year = this.year.getValue();

		try{
			Date temp = this.format.parse(year+"-"+month+"-"+day);
			ValueChangeEvent.fire(this, temp);
		}catch(Exception e){
			GWT.log("DATA INVALIDA");
			return false;
		}
		return true;
	}
	
	@Override
	public void setLabelWidth(String width) {
		this.label.setWidth(width);
	}

	@Override
	public void clear() {
		if(this.readonly){
			this.day.setValue("-");
			this.month.setValue("-");
			this.year.setValue("-");
		}else{
			this.day.setValue("");
			this.month.setValue("");
			this.year.setValue("");
		}
	}

	protected boolean isValidDate(boolean assumeComplete){
		return true; //TODO
	}

	protected void onDayChanged(int charCode){
		
		if((charCode == '-' || charCode == '/') && !this.day.getValue().isEmpty()){
			this.month.setFocus(true);
		}
		if(this.day.getMaxLength() == day.getCursorPos()+1){
			this.month.setFocus(true);
		}
	}

	protected boolean isValidDay(String value){
		if(value != null && !value.isEmpty() && isValidDate(false)){
			return true;
		}
		return false;
	}

	protected void onMonthChanged(int charCode){
		if((charCode == '-' || charCode == '/') && !this.month.getValue().isEmpty()){
			this.year.setFocus(true);
		}
		if(this.month.getMaxLength() == month.getCursorPos()+1){
			this.year.setFocus(true);
		}
	}

	protected boolean isValidMonth(String value){
		if(value != null && !value.isEmpty() && isValidDate(false)){
			return true;
		}
		return false;
	}

	protected void onYearChanged(int charCode){
		return;
	}

	@Override
	public void setValue(Date value, boolean fireEvents) {
		if(value == null) {
			clear();
			return;
		}
		String day = new SimpleDateFormat("d").format(value);
		String month = new SimpleDateFormat("M").format(value);
		String year = new SimpleDateFormat("yyyy").format(value);

		this.day.setValue(day);
		this.month.setValue(month);
		this.year.setValue(year);
		
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}
	
	public void setValue(String date){
		if(date == null) {
			setValue((Date) null);
		}else{
			setValue(DateTimeFormat.getFormat(DEFAULT_FORMAT).parse(date));
		}
	}

	@Override
	public Date getValue() {
		if(!validateDate()){
			return null;
		}
		
		String day = this.day.getValue();
		String month = this.month.getValue();
		String year = this.year.getValue();

		if(day.equals("") || month.equals("") || year.equals(""))
			return null;

		return this.format.parse(year+"-"+month+"-"+day);
	}

	public String getStringValue(){
		return getValue() == null ? null : DateTimeFormat.getFormat(DEFAULT_FORMAT).format(getValue());
	}
	
	@Override
	public void setInvalid(boolean invalid){
		if(field != null){
			if(invalid){
				day.addStyleName("invalidFormField");
				month.addStyleName("invalidFormField");
				year.addStyleName("invalidFormField");
			}else{
				day.removeStyleName("invalidFormField");
				month.removeStyleName("invalidFormField");
				year.removeStyleName("invalidFormField");
			}
		}
		FieldValidator<?> validator = this.validator;
		String message = validator == null ? null : validator.getErrorMessage();
		this.errorMessageLabel.setText(message == null ? "Valor inválido" : message);
		this.errorMessageLabel.setVisible(invalid);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		if(!editable)
			return;
		if(readonly){
			if(day.getValue().isEmpty()){
				day.setValue(EMPTY_VALUE_PLACEHOLDER);
			}
			if(month.getValue().isEmpty()){
				month.setValue(EMPTY_VALUE_PLACEHOLDER);
			}
			if(year.getValue().isEmpty()){
				year.setValue(EMPTY_VALUE_PLACEHOLDER);
			}
		}else{
			if(day.getValue().equalsIgnoreCase(EMPTY_VALUE_PLACEHOLDER)){
				day.setValue("");
			}
			if(month.getValue().equalsIgnoreCase(EMPTY_VALUE_PLACEHOLDER)){
				month.setValue("");
			}
			if(year.getValue().equalsIgnoreCase(EMPTY_VALUE_PLACEHOLDER)){
				year.setValue("");
			}
		}
		day.setReadOnly(readonly);
		day.getElement().getStyle().setBorderColor(readonly ? "transparent" : "gray");
		day.getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
		
		month.setReadOnly(readonly);
		month.getElement().getStyle().setBorderColor(readonly ? "transparent" : "gray");
		month.getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
		
		year.setReadOnly(readonly);
		year.getElement().getStyle().setBorderColor(readonly ? "transparent" : "gray");
		year.getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
		
		this.readonly = readonly;
		mandatoryIndicatorLabel.setVisible(!readonly);
	}

	@Override
	public void setFieldWidth(String width) {}

	@Override
	public boolean isReadOnly() {
		return readonly;
	}

}
