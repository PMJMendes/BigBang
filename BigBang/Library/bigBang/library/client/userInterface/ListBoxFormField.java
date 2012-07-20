package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListBoxFormField extends FormField<String> {

	protected ListBox listBox;
	protected HorizontalPanel wrapper;

	protected String value;

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

		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		this.label = new Label();
		mainWrapper.add(this.label);
		wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.add((Widget) this.listBox);
		wrapper.add(unitsLabel);
		wrapper.add(mandatoryIndicatorLabel);
		setFieldWidth("150px");

		clearValues();

		this.listBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String value = listBox.getValue(listBox.getSelectedIndex());
				setValue(value, true);
			}
		});
	}

	@Override
	protected void initializeView() {
		super.initializeView();

	}

	private void setLabel(String label) {
		this.label.setText(label);
	}

	@Override
	public void setLabelWidth(String width) {
		this.label.setWidth(width);
		this.wrapper.setCellWidth(this.label, width);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		if(!editable)
			return;
		this.listBox.setEnabled(!readonly);
		mandatoryIndicatorLabel.setVisible(!readonly && isMandatory());
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
		int selectedIndex = getSelectedIndex();
		String selectedValue = this.listBox.getValue(selectedIndex);
		setValue(selectedValue, true);
	}

	public boolean hasItem(String item, String value) {
		int nItems = this.listBox.getItemCount();
		for(int i = 0; i < nItems; i++){
			if(this.listBox.getItemText(i).equals(item) && this.listBox.getValue(i).equals(value))
				return true;
		}
		return false;
	}

	public int getItemIndex(String item, String value) {
		int count = this.listBox.getItemCount();

		for(int i = 0; i < count; i++) {
			if(this.listBox.getValue(i).equals(value) && this.listBox.getItemText(i).equals(item))
				return i;
		}
		throw new RuntimeException("The item does not exist in the listbox.");
	}

	public int getSelectedIndex(){
		return this.listBox.getSelectedIndex();
	}

	@Override
	public String getValue(){
		return value == null ? null : value.isEmpty() ? null : value;
	}


	public String getSelectedItemText(){
		return this.listBox.getItemText(this.listBox.getSelectedIndex());
	}

	@Override
	public void setValue(String value, boolean fireEvents){
		if(value == null){
			clear();
			return;
		}
		for(int i = 0; i < this.listBox.getItemCount(); i++) {
			String itemValue = this.listBox.getValue(i);
			if(itemValue != null && itemValue.equalsIgnoreCase(value)){
				if(isDifferentValue(itemValue)){
					this.listBox.setSelectedIndex(i);
					this.value = value;
					if(fireEvents)
						ValueChangeEvent.fire(this, value);
				}
				break;
			}
		}
	}

	@Override
	public void clear() {
		setValue(new String(), true);
	}

	public void clearValues() {
		this.listBox.clear();
		this.addItem("-", new String());
		clear();
	}

	protected boolean isDifferentValue(String value) {
		String currentValue = getValue();
		if(currentValue == null){
			currentValue = new String();
		}
		if(currentValue == value)
			return false;
		if(currentValue != null && currentValue.equalsIgnoreCase(value))
			return false;
		return true;
	}
	
	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
		((ListBox)this.field).setTabIndex(editable ? 0 : -1);
	}

	@Override
	public void focus() {
		listBox.getElement().focus();
	}
}
