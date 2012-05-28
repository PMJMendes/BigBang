package bigBang.library.client.userInterface;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DayMonthDatePickerFormField extends DatePickerFormField{
	
	public DayMonthDatePickerFormField() {
		this("");
		
	
	}
	public DayMonthDatePickerFormField(String label){
		this(label, DEFAULT_FORMAT);
	}
	
	public DayMonthDatePickerFormField(String label, String format){
		super(label, format);
		secondSlash.setVisible(false);
		year.setVisible(false);
	}
	
	@Override
	public void setValue(Date value) {
		super.setValue(value);
	}
	
	
	@Override
	public String getStringValue() {
		return getValue() == null ? null : DateTimeFormat.getFormat("MM-dd").format(getValue());
	}
	
	@Override
	public Date getValue(){
		String day = this.day.getValue();
		String month = this.month.getValue();
		
		if(day.equals("") || month.equals("")){
			return null;
		}
		
		return DateTimeFormat.getFormat("MM-dd").parse(month+"-"+day);
	}

}
