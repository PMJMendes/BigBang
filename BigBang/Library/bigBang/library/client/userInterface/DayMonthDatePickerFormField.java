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
		String date = null;
		
		try{
			date = DateTimeFormat.getFormat("MM-dd").format(getValue());
		}
		catch(Exception e){}
		
		
		return date;
	}
	
	@Override
	public Date getValue(){
		String day = this.day.getValue();
		String month = this.month.getValue();
		
		if(day.equals("") || month.equals("") || day.equals("-") || month.equals("-")){
			return null;
		}
		
		return DateTimeFormat.getFormat("MM-dd").parse(month+"-"+day);
	}
	
	public String getDay(){
		return this.day.getValue();
	}
	
	public String getMonth(){
		return this.month.getValue();
	}
	
	@Override
	public void clear() {
		if(this.readonly){
			this.day.setValue("-");
			this.month.setValue("-");
		}else{
			this.day.setValue("");
			this.month.setValue("");
		}
	}
	
	@Override
	public void setReadOnlyInternal(boolean readonly) {
		if(!editable)
			return;
		if(readonly){
			if(day.getValue().isEmpty()){
				day.setValue(EMPTY_VALUE_PLACEHOLDER);
			}
			if(month.getValue().isEmpty()){
				month.setValue(EMPTY_VALUE_PLACEHOLDER);
			}
		}else{
			if(day.getValue().equalsIgnoreCase(EMPTY_VALUE_PLACEHOLDER)){
				day.setValue("");
			}
			if(month.getValue().equalsIgnoreCase(EMPTY_VALUE_PLACEHOLDER)){
				month.setValue("");
			}
		}
		day.setReadOnly(readonly);
		day.setEnabled(!readonly);
		day.getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
		
		month.setReadOnly(readonly);
		month.setEnabled(!readonly);
		month.getElement().getStyle().setBackgroundColor(readonly ? "transparent" : "white");
		
		this.readonly = readonly;
		mandatoryIndicatorLabel.setVisible(!readonly);
	}

}
