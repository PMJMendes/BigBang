
package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

public class PasswordTextBoxFormField extends FormField<String> {

	private Label label;

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

	public PasswordTextBoxFormField(){
		super();
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		this.field = new PasswordTextBox();
		wrapper.add((Widget) this.field);
		wrapper.add(mandatoryIndicatorLabel);
		initWidget(wrapper);

		setFieldWidth("400px");
	}

	@Override
	public void setReadOnly(boolean readOnly) {
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

}

