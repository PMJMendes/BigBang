package bigBang.library.client.userInterface;

import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TextBoxFormField extends FormField<String> {
	
	private Label label;
	
	public TextBoxFormField(String label,FieldValidator<String> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}
	
	public TextBoxFormField(FieldValidator<String> validator) {
		this();
		setValidator(validator);
	}
	
	public TextBoxFormField(String label) {
		this();
		setLabel(label);
	}
	
	private void setLabel(String label) {
		this.label.setText(label + ":");
	}
	
	public TextBoxFormField(){
		super();
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		this.field = new TextBox();
		wrapper.add((Widget) this.field);
		wrapper.add(mandatoryIndicatorLabel);
		initWidget(wrapper);
		
		setFieldWidth("400px");
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		TextBox field = ((TextBox)this.field);
		if(field.isReadOnly() != readOnly){
			if(field.getValue().equals("")) field.setValue("-"); else
			if(field.getValue().equals("-")) field.setValue(""); 
		}
		field.setReadOnly(readOnly);
		field.getElement().getStyle().setBorderColor(readOnly ? "transparent" : "black");
		field.getElement().getStyle().setBackgroundColor(readOnly ? "transparent" : "white");
		mandatoryIndicatorLabel.setVisible(!readOnly);
	}
	
	@Override
	public boolean isReadOnly() {
		return ((TextBox)this.field).isReadOnly();
	}

}
