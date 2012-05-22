package bigBang.library.client.userInterface;



import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

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

	public class NumericWrapper implements HasValue<Double>{

		protected TextBox field;
		protected String curr = "";

		private HandlerManager handlerManager;

		public NumericWrapper() {
			this.handlerManager = new HandlerManager(this);
			field = new TextBox();

			field.addKeyPressHandler(new KeyPressHandler() {

				@Override
				public void onKeyPress(KeyPressEvent event) {

					if(!Character.isDigit((char)event.getUnicodeCharCode()) && field.getValue().contains(LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator()))
						event.preventDefault();
				}
			});

			field.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					if(!event.getValue().isEmpty()){
						try{
							setValue(nf.parse(event.getValue()));
							curr = event.getValue();
						}catch(NumberFormatException e){
							field.setValue(curr);
						}
					}
				}
			});


			nf = NumberFormat.getDecimalFormat();
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

			return nf.parse(field.getValue());
		}

		@Override
		public void setValue(Double value) {
			this.setValue(value, true);
		}

		@Override
		public void setValue(Double value, boolean fireEvents) {

			String formatted = nf.format(value);
			field.setValue(formatted);
		}

		public TextBox getField(){
			return field;
		}


	}

	protected HorizontalPanel wrapper;
	protected NumberFormat nf;

	public NumericTextBoxFormField(String label, FieldValidator<Double> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}

	public NumericTextBoxFormField(FieldValidator<Double> validator){
		this();
		setValidator(validator);
	}

	public NumericTextBoxFormField(String label){
		this();
		setLabel(label);
	}

	protected void setLabel(String label){
		if(label == null || label.equals("")){
			this.label.setText("");
		}else{
			this.label.setText(label);
		}
	}


	public NumericTextBoxFormField(){
		super();

		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		mainWrapper.add(this.label);
		wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.field = new NumericWrapper();
		setTextAligment(TextAlignment.RIGHT);
		nf = NumberFormat.getDecimalFormat();
		wrapper.add(((NumericWrapper)field).getField());
		wrapper.setCellWidth(((NumericWrapper)field).getField(), "100%");
		wrapper.add(unitsLabel);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);

		setFieldWidth("175px");

	}

	public void setNumberFormat(NumberFormat nf){
		this.nf = nf;
	}


	@Override
	public void clear() {
		((NumericWrapper)field).getField().setValue("");
	}

	@Override
	public void setReadOnly(boolean readonly) {
		if(!editable){
			return;
		}
		((NumericWrapper)field).getField().setReadOnly(readonly);
		((NumericWrapper)field).getField().getElement().getStyle().setBorderColor(readonly ? "transparent" : "gray");
		((NumericWrapper)field).getField().getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
		mandatoryIndicatorLabel.setVisible(!readonly&& this.isMandatory());

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
		super.setFieldWidth(width);

		if(width.equals("100%")){
			this.wrapper.setWidth("100%");
		}
		else
			this.wrapper.setWidth("");
	};

	public void setTextAligment(TextAlignment alignment){
		((NumericWrapper)this.field).getField().setAlignment(alignment);
	}

}
