package bigBang.library.client.userInterface;

import java.util.Calendar;
import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
	protected ListBox day, month, year;	
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

		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.setText(label);
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.add(new Label("Dia:"));
		wrapper.add(day);
		wrapper.add(new Label("Mês:"));
		wrapper.add(month);
		wrapper.add(new Label("Ano:"));
		wrapper.add(year);
		
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);
		initWidget(wrapper);
		
		day.addItem("-", "");
		for(int i = 1; i <= 31; i++) {
			this.day.addItem(i+"", i+"");
		}
		month.addItem("-", "");
		for(int i = 1; i <= 12; i++) {
			String monthStr = new String();
			switch(i){
			case 1:
				monthStr = "Jan";
				break;
			case 2:
				monthStr = "Fev";
				break;
			case 3:
				monthStr = "Mar";
				break;
			case 4:
				monthStr = "Abr";
				break;
			case 5:
				monthStr = "Mai";
				break;
			case 6:
				monthStr = "Jun";
				break;
			case 7:
				monthStr = "Jul";
				break;
			case 8:
				monthStr = "Ago";
				break;
			case 9:
				monthStr = "Set";
				break;
			case 10:
				monthStr = "Out";
				break;
			case 11:
				monthStr = "Nov";
				break;
			case 12:
				monthStr = "Dez";
				break;
			}
			this.day.addItem(monthStr, i+"");
		}
		year.addItem("-", "");
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for(int i = 1900; i <= currentYear; i++){
			year.addItem(i+"", i+"");
		}
		
		setReadOnly(false);
	}
	
	@Override
	public void clear() {
		this.day.setSelectedIndex(0);
		this.month.setSelectedIndex(0);
		this.year.setSelectedIndex(0);
	}
	
	@Override
	public void setValue(Date value, boolean fireEvents) {
		
		super.setValue(value, fireEvents);
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
