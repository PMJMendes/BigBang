package bigBang.library.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.client.FormField;

public class ExpandableSelectionFormField extends FormField<String> {
	
	protected Label valueDisplayName;
	protected ExpandableSelectionFormFieldPanel selectionPanel;
	
	public ExpandableSelectionFormField(){
		this.selectionPanel = new TypifiedListSelectionPanel();
	}
	
	public ExpandableSelectionFormField(ExpandableSelectionFormFieldPanel selectionPanel){
		super();
		this.selectionPanel = selectionPanel;
	}
	
	public String getValue() {
		return field.getValue();
	}

	public void setValue(String value) {
		this.setValue(value, true);
	}

	public void setValue(String value, boolean fireEvents) {
		field.setValue(value, false);
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReadOnly(boolean readonly) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLabelWidth(String width) {
		// TODO Auto-generated method stub

	}

}
