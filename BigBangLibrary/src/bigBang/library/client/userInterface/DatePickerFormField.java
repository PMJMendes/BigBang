package bigBang.library.client.userInterface;

import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;


public class DatePickerFormField extends FormField<Date> {
	
	private static final String DEFAULT_FORMAT = "d-M-y";
	
	private Label label;
	@SuppressWarnings("unused")
	private String datePattern;
	private boolean readonly;
	
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
		
		this.setValidator(validator);
		
		this.datePattern = format;
		this.field = new DateBox();
		
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.setText(label);
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.add((Widget) this.field);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);
		initWidget(wrapper);
		
		setReadOnly(false);
	}
	
	@Override
	public void clear() {
		this.field.setValue(null);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		if(!editable)
			return;
		((DateBox)this.field).setEnabled(!readonly);
		this.readonly = readonly;
		mandatoryIndicatorLabel.setVisible(!readonly);
		((UIObject) field).getElement().getStyle().setBorderColor(readonly ? "transparent" : "gray");
		((UIObject) field).getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
	}

	@Override
	public boolean isReadOnly() {
		return readonly;
	}

}
