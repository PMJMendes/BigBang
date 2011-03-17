package bigBang.library.shared.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.shared.FormField;

public class ListBoxFormField extends FormField<String> {

	private class MockField implements HasValue<String> {

		private String value;
		
		public HandlerRegistration addValueChangeHandler(
				ValueChangeHandler<String> handler) {
			return addValueChangeHandler(handler);
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			setValue(value, true);
		}

		public void setValue(String value, boolean fireEvents) {
			this.value = value;
			if(fireEvents)
				fireEvent(null);
		}

		public void fireEvent(GwtEvent<?> event) {
			ValueChangeEvent.fire(this, value);
		}		
	}
	
	private ListBox listBox;
	private Label label;
	
	public ListBoxFormField(){
		this.listBox = new ListBox();
		this.field = new MockField();
		
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.label = new Label();
		this.label.getElement().getStyle().setMarginRight(5, Unit.PX);
		wrapper.add(this.label);
		wrapper.setCellWidth(this.label, "100px");
		wrapper.setCellHorizontalAlignment(this.label, HasHorizontalAlignment.ALIGN_RIGHT);
		this.field = new TextBox();
		wrapper.add((Widget) this.listBox);
		initWidget(wrapper);
		
		setFieldWidth("250px");
	}
	
	@Override
	public void setReadOnly(boolean readonly) {
		this.listBox.setEnabled(readonly);
	}

	@Override
	public boolean isReadOnly() {
		return this.listBox.isEnabled();
	}

}
