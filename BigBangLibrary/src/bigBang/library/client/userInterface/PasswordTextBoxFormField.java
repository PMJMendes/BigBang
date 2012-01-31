
package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PasswordTextBoxFormField extends FormField<String> {

	public PasswordTextBoxFormField(String label,FieldValidator<String> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}

	public PasswordTextBoxFormField(FieldValidator<String> validator) {
		this();
		setValidator(validator);
	}

	public PasswordTextBoxFormField(String label) {
		this();
		setLabel(label + ":");
	}

	private void setLabel(String label) {
		this.label.setText(label);
	}
	
	@Override
	public void setLabelWidth(String width) {
		this.label.setWidth(width);
	}

	public PasswordTextBoxFormField(){
		super();
		
		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		mainWrapper.add(this.label);
		this.label = new Label();
		mainWrapper.add(this.label);
		
		HorizontalPanel wrapper = new HorizontalPanel();
		mainWrapper.add(wrapper);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		this.field = new PasswordTextBox();
		wrapper.add((Widget) this.field);
		wrapper.add(mandatoryIndicatorLabel);
		setFieldWidth("400px");
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(!editable)
			return;
		PasswordTextBox field = ((PasswordTextBox)this.field); 
		field.setReadOnly(readOnly);
		field.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "gray");
		field.getElement().getStyle().setBackgroundColor(readOnly ? "transparent" : "white");
		mandatoryIndicatorLabel.setVisible(!readOnly);
	}

	@Override
	public boolean isReadOnly() {
		return ((PasswordTextBox)this.field).isReadOnly();
	}

	@Override
	public void clear() {
		this.setValue("");
	}

}

