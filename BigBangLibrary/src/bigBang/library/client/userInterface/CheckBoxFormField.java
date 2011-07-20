package bigBang.library.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

public class CheckBoxFormField extends FormField<Boolean> {
	
	protected Label label;
	protected HorizontalPanel wrapper;
	
	public CheckBoxFormField(String label,FieldValidator<Boolean> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}
	
	public CheckBoxFormField(FieldValidator<Boolean> validator) {
		this();
		setValidator(validator);
	}
	
	public CheckBoxFormField(String label) {
		this();
		setLabel(label);
	}
	
	public CheckBoxFormField(){
		super();
		wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		this.field = new CheckBox();
		((UIObject) this.field).getElement().getStyle().setMargin(0, Unit.PX);
		wrapper.add((Widget) this.field);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);
		initWidget(wrapper);
	}

	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			wrapper.setCellWidth(this.label, "0px");
			this.label.setText("");
			
		}else{
			this.label.setText(label + ":");
			wrapper.setCellWidth(this.label, "100px");
		}
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		if(!editable)
			return;
		CheckBox field = ((CheckBox)this.field);
		field.setEnabled(!readOnly);
		mandatoryIndicatorLabel.setVisible(!readOnly);
	}
	
	@Override
	public void setValue(Boolean value, boolean fireEvents) {
		super.setValue(value, fireEvents);
	}
	
	@Override
	public boolean isReadOnly() {
		return !((CheckBox)this.field).isEnabled();
	}

	@Override
	public void clear() {
		this.field.setValue(false);
	}

}
