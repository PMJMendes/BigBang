package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class NumericTextBoxFormField extends FormField<Double>{

	public static final String DUMMY_VALUE = "-";

	public class NumericWrapper implements HasValue<Double>{

		protected TextBox field;
		protected String curr = "";
		private HandlerManager handlerManager;

		public NumericWrapper() {
			super();
			
			this.handlerManager = new HandlerManager(this);
			field = new TextBox();
			field.addKeyPressHandler(new KeyPressHandler() {

				@Override
				public void onKeyPress(KeyPressEvent event) {					

					if(!Character.isDigit((char)event.getUnicodeCharCode()) && event.getCharCode() != KeyCodes.KEY_ENTER){
						if(event.getUnicodeCharCode() == '-' && !field.getValue().contains("-") && field.getCursorPos() == 0){
							return;
						}
						event.preventDefault();
						if((event.getCharCode() == LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator().charAt(0) || event.getCharCode() == 46) && !field.getValue().contains(LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator())){
							if(field.getValue() != null){
								int curPos = field.getCursorPos();
								String newS = field.getValue().substring(0,curPos)+LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator()+field.getValue().substring(curPos,field.getValue().length());
								field.setValue(newS);
								field.setCursorPos(curPos+1);
							}else{
								field.setText(LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator());
							}

						}

					}
				}
			});

			field.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					if(event.getValue() != null && !event.getValue().isEmpty()){
						try{
							setValue(nf.parse(event.getValue()));
							curr = event.getValue();
							ValueChangeEvent.fire(NumericTextBoxFormField.this, nf.parse(curr));
						}catch(NumberFormatException e){
							field.setValue(curr);
						}
					}
				}
			});

		}
		@Override
		public HandlerRegistration addValueChangeHandler(
				ValueChangeHandler<Double> handler) {
			return null;
		}

		@Override
		public void fireEvent(GwtEvent<?> event) {
			this.handlerManager.fireEvent(event);
		}

		@Override
		public Double getValue(){
			try{
				return (field.getValue() != null && !field.getValue().isEmpty()) ? nf.parse(field.getValue()) : null;
			}catch(NumberFormatException e){
				return null;
			}
		}

		@Override
		public void setValue(Double value) {
			hasDummyValue = false;
			this.setValue(value, true);
		}

		@Override
		public void setValue(Double value, boolean fireEvents) {
			hasDummyValue = false;
			if(value == null) {
				field.setValue(null);
			}else{
				String formatted = nf.format(value);
				field.setValue(formatted);
			}
			if(fireEvents){
				ValueChangeEvent.fire(this, value);
			}
		}

		public TextBox getField(){
			return field;
		}
	}

	protected HorizontalPanel wrapper;
	protected NumberFormat nf;
	protected boolean showDecimal;
	private boolean hasDummyValue;

	public NumberFormat getNumberFormat() {
		return nf;
	}

	public NumericTextBoxFormField(String label, FieldValidator<Double> validator, boolean isMoney){
		this(isMoney);
		setLabel(label);
		setValidator(validator);
	}

	public NumericTextBoxFormField(FieldValidator<Double> validator, boolean isMoney){
		this(isMoney);
		setValidator(validator);
	}

	public NumericTextBoxFormField(String label, boolean isMoney){
		this(isMoney);
		setLabel(label);
	}

	protected void setLabel(String label){
		if(label == null || label.equals("")){
			this.label.setText("");
		}else{
			this.label.setText(label);
		}
	}


	public NumericTextBoxFormField(boolean isMoney){
		this();
		this.setAsMoney(isMoney);
		

	}

	public NumericTextBoxFormField() {
		super();
		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);
		mainWrapper.setHeight("45px");

		mainWrapper.add(this.label);
		wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.field = new NumericWrapper();
		setTextAligment(TextAlignment.RIGHT);
		wrapper.add(((NumericWrapper)field).getField());
		wrapper.setCellWidth(((NumericWrapper)field).getField(), "100%");
		wrapper.add(unitsLabel);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);

		setFieldWidth("175px");
	}

	public void setAsMoney(boolean isMoney){
		if(isMoney){
			nf = NumberFormat.getFormat("#,##0.00");
		}
		else{
			nf = NumberFormat.getDecimalFormat();
		}
	}


	@Override
	public void clear() {
		((NumericWrapper)field).getField().setValue(((NumericWrapper)field).getField().isReadOnly() ? "-" : "");
		if(((NumericWrapper)field).getField().isReadOnly())
			hasDummyValue = true;
	}

	@Override
	public void setValue(Double value) {	
		if(value == null){
			clear();
			return;
		}
		super.setValue(value);
	}
	
	@Override
	public void setReadOnly(boolean readonly) {
		if(!editable){
			 return;
		}
		TextBox field = ((TextBox)getTextBox());
		if(field.isReadOnly() != readonly){
			if(readonly){
				if(field.getValue().equals("")){
					field.setValue(DUMMY_VALUE, false);
					hasDummyValue = true;
				}
			}else{
				if(hasDummyValue){
					field.setValue("", false);
					hasDummyValue = false;
				}
			}
		}
		((NumericWrapper)this.field).getField().setEnabled(!readonly);
		((NumericWrapper)this.field).getField().setReadOnly(readonly);
		((NumericWrapper)this.field).getField().getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
		mandatoryIndicatorLabel.setVisible(!readonly && this.isMandatory());
	}

	public void showDecimal(boolean show){
		showDecimal = show;
	}

	@Override
	public boolean isReadOnly() {
		return ((NumericWrapper)field).getField().isReadOnly();
	}

	@Override
	public void setLabelWidth(String width) {
		label.setWidth(width);
	}

	@Override
	public void setFieldWidth(String width) {
		((NumericWrapper)field).getField().setWidth(width);

		if(width.equals("100%")){
			this.wrapper.setWidth("100%");
		}
		else
			this.wrapper.setWidth("");
	};

	public void setTextAligment(TextAlignment alignment){
		((NumericWrapper)this.field).getField().setAlignment(alignment);
	}

	public TextBox getTextBox() {
		return ((NumericWrapper)field).getField();
	}

	@Override
	public void focus() {
		getTextBox().setFocus(true);
	}

	public void setStringValue(String value){
		((NumericWrapper)field).curr = value;
		getTextBox().setValue(value, true);
	}

	public String getStringValue(){
		String value = getTextBox().getValue();
		if((value != null && value.isEmpty()) || (value != null && value.equals(DUMMY_VALUE))){
			value = null;
		}
		return value;
	}

	public void setStringValue(String value, boolean fireEvents) {
		((NumericWrapper)field).curr = value;
		getTextBox().setValue(value, fireEvents);
	}

}
