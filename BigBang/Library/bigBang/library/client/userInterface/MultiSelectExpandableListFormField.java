package bigBang.library.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

public class MultiSelectExpandableListFormField extends FormField<String[]> {

	protected class MockField extends Widget implements HasValue<String[]> {

		protected String[] value;
		
		@Override
		public HandlerRegistration addValueChangeHandler(
				ValueChangeHandler<String[]> handler) {
			return addHandler(handler, ValueChangeEvent.getType());
		}

		@Override
		public String[] getValue() {
			return this.value;
		}

		@Override
		public void setValue(String[] value) {
			setValue(value, true);
		}

		@Override
		public void setValue(String[] value, boolean fireEvents) {
			this.value = value;
			ValueChangeEvent.fire(this, getValue());
		}
		
	}
	
	protected HorizontalPanel wrapper;
	protected TextBox textBox;
	
	protected Image expandImage;
	//protected TypifiedListManagementPanel list;
	
	public MultiSelectExpandableListFormField(String label,FieldValidator<String[]> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}
	
	public MultiSelectExpandableListFormField(FieldValidator<String[]> validator) {
		this();
		setValidator(validator);
	}
	
	public MultiSelectExpandableListFormField(String label) {
		this();
		setLabel(label);
	}
	
	public MultiSelectExpandableListFormField(){
		super();
		wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		this.field = new MockField();
		this.textBox = new TextBox();
		this.textBox.setEnabled(false);
		
		setFieldWidth("400px");
	}
	
	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			this.label.setText("");
			
		}else{
			this.label.setText(label + ":");
		}
	}

	@Override
	public void setLabelWidth(String width) {
		wrapper.setCellWidth(this.label, width);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReadOnlyInternal(boolean readonly) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void focus() {
		textBox.setFocus(true);
	}

}
