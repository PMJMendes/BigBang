package bigBang.library.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

public class CheckBoxFormField extends FormField<Boolean> {
	
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
		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);
		mainWrapper.add(this.label);

		wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.field = new CheckBox();
		this.field.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setValue(event.getValue(), true);
			}
		});
		((UIObject) this.field).getElement().getStyle().setMargin(0, Unit.PX);
		wrapper.add((Widget) this.field);
		wrapper.add(unitsLabel);
		wrapper.add(mandatoryIndicatorLabel);
		wrapper.add(errorMessageLabel);
	}

	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			this.label.setText("");
			
		}else{
			this.label.setText(label);
		}
	}
	
	@Override
	public void setLabelWidth(String width) {
		return;
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
