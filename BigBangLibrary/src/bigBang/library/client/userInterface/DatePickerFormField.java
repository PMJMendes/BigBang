package bigBang.library.client.userInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;


public class DatePickerFormField extends FormField<Date> {
	
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

	private Label label;
	protected ListBox day, month, year;	
	private boolean readonly;
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
		
		this.setValidator(validator);
		
		this.format = DateTimeFormat.getFormat(format);

		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.setText(label);
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		
		day = new ListBox();
		month = new ListBox();
		year = new ListBox();
		
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
			this.month.addItem(monthStr, i+"");
		}
		year.addItem("-", "");
		int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		for(int j = currentYear; j >= 1900; j--){
			year.addItem(j+"", j+"");
		}
	}
	
	@Override
	public void clear() {
		this.day.setSelectedIndex(0);
		this.month.setSelectedIndex(0);
		this.year.setSelectedIndex(0);
	}
	
	@Override
	public void setValue(Date value, boolean fireEvents) {
		String day = new SimpleDateFormat("d").format(value);
		String month = new SimpleDateFormat("M").format(value);
		String year = new SimpleDateFormat("yyyy").format(value);
		
		int count = this.day.getItemCount();
		for(int i = 0; i < count; i++){
			if(this.day.getValue(i).equals(day+"")){
				this.day.setSelectedIndex(i);
				break;
			}
		}
		
		count = this.month.getItemCount();
		for(int i = 0; i < count; i++){
			if(this.month.getValue(i).equals(month+"")){
				this.month.setSelectedIndex(i);
				break;
			}
		}
		
		count = this.year.getItemCount();
		for(int i = 0; i < count; i++){
			if(this.year.getValue(i).equals(year+"")){
				this.year.setSelectedIndex(i);
				break;
			}
		}

		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}
	
	@Override
	public Date getValue() {
		String day = this.day.getValue(this.day.getSelectedIndex());
		String month = this.month.getValue(this.month.getSelectedIndex());
		String year = this.year.getValue(this.year.getSelectedIndex());
		
		if(day.equals("") || month.equals("") || year.equals(""))
			return null;
		
		return this.format.parse(year+"-"+month+"-"+day);
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
		day.setEnabled(!readonly);
		month.setEnabled(!readonly);
		year.setEnabled(!readonly);
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
