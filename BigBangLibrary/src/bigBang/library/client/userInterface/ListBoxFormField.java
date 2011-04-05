package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ListBoxFormField extends FormField<String> {
	
	private ListBox listBox;
	private Label label;
	
	public ListBoxFormField(String label,FieldValidator<String> validator){
		this();
		setValidator(validator);
		setLabel(label);
	}
	
	public ListBoxFormField(FieldValidator<String> validator) {
		this();
		setValidator(validator);
	}
	
	public ListBoxFormField(String label){
		this();
		setLabel(label);
	}
	
	public ListBoxFormField(){
		super();
		
		this.listBox = new ListBox();
		this.listBox.setHeight("20px");
		
		//this.field = new MockField();
		
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		wrapper.add((Widget) this.listBox);
		wrapper.add(mandatoryIndicatorLabel);
		initWidget(wrapper);
		setFieldWidth("150px");
		
		clearValues();
	}
	
	private void setLabel(String label) {
		this.label.setText(label + ":");
	}
	
	@Override
	public void setReadOnly(boolean readonly) {
		this.listBox.setEnabled(!readonly);
		mandatoryIndicatorLabel.setVisible(!readonly);
	}

	@Override
	public boolean isReadOnly() {
		return !this.listBox.isEnabled();
	}
	
	@Override 
	public void setFieldWidth(String width){
		this.listBox.setWidth(width);
	}
	
	public void addItem(String item, String value){
		this.listBox.addItem(item, value);
	}
	
	public void removeItem(int index){
		this.listBox.removeItem(index);
	}
	
	public boolean hasItem(String item, String value) {
		int nItems = this.listBox.getItemCount();
		for(int i = 0; i < nItems; i++){
			if(this.listBox.getItemText(i).equals(item) && this.listBox.getValue(i).equals(value))
				return true;
		}
		return false;
	}
	
	public int getSelectedIndex(){
		return this.listBox.getSelectedIndex();
	}
	
	@Override
	public String getValue(){
		return this.listBox.getValue(this.listBox.getSelectedIndex());
	}
	
	
	public String getSelectedItemText(){
		return this.listBox.getItemText(this.listBox.getSelectedIndex());
	}
	
	@Override
	public void setValue(String value){
		for(int i = 0; i < this.listBox.getItemCount(); i++) {
			if(this.listBox.getValue(i).equals(value)){
				this.listBox.setSelectedIndex(i);
				return;
			}
		}
		GWT.log("Could not select list box value because value does not exist.");
	}

	@Override
	public void clear() {
		setValue("");
	}
	
	public void clearValues() {
		this.listBox.clear();
		this.addItem("-", "");
	}

}
