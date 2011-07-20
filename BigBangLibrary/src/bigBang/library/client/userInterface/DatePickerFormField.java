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

import bigBang.library.client.FormField;


public class DatePickerFormField extends FormField<Date> {
	
	private static final String DEFAULT_PATTERN = "d-M-y";
	
	private Label label;
	@SuppressWarnings("unused")
	private String datePattern;
	private boolean readonly;
	
	public DatePickerFormField(){
		this(DEFAULT_PATTERN);
	}
	
	public DatePickerFormField(String pattern){
		super();
		
		this.datePattern = pattern;
		this.field = new DateBox();
		
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
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
