package bigBang.library.client.userInterface;

import bigBang.library.client.FormField;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NavigationFormField extends FormField<NavigationHistoryItem> {

	protected Label valueName;
	protected NavigationHistoryItem value;
	protected Image linkImage;
	protected boolean readOnly = true;

	public NavigationFormField(String label){
		super();

		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		label = label == null ? "" : label;
		this.label.setText(label);

		HorizontalPanel innerWrapper = new HorizontalPanel();

		this.valueName = new Label();
		innerWrapper.add(this.valueName);

		ClickHandler expandClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onGo();
			}
		};

		this.valueName.addClickHandler(expandClickHandler);

		Resources r = GWT.create(Resources.class);
		linkImage = new Image(r.linkIcon());
		linkImage.getElement().getStyle().setCursor(Cursor.POINTER);
		linkImage.addClickHandler(expandClickHandler);
		innerWrapper.add(linkImage);
		innerWrapper.add(this.unitsLabel);
		innerWrapper.add(mandatoryIndicatorLabel);

		wrapper.add(this.label);
		wrapper.add(innerWrapper);

		setValue(null, false);
		setReadOnly(false);
	}

	public NavigationHistoryItem getValue() {
		return this.value;
	}

	public void setValue(NavigationHistoryItem value) {
		this.setValue(value, true);
	}

	public void setValue(NavigationHistoryItem value, final boolean fireEvents) {
		if(value == null) {
			valueName.getElement().getStyle().setTextDecoration(TextDecoration.NONE);
			valueName.getElement().getStyle().setCursor(Cursor.AUTO);
			this.value = null;
			this.valueName.setText("-");
			if(fireEvents){
				ValueChangeEvent.fire(this, value);
			}
			this.linkImage.setVisible(false);
		} else {
			valueName.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
			valueName.getElement().getStyle().setCursor(Cursor.POINTER);
			this.value = value;
			if(fireEvents){
				ValueChangeEvent.fire(NavigationFormField.this, NavigationFormField.this.value);
			}
			this.linkImage.setVisible(true);
		}
	}

	@Override
	public void clear() {
		setValue(null);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		return;
	}

	@Override
	public boolean isReadOnly() {
		return this.readOnly;
	}

	@Override
	public void setLabelWidth(String width) {
		this.label.setWidth(width);
	}

	@Override
	public void setFieldWidth(String width) {
		this.valueName.setWidth(width);
	}

	protected void onGo(){
		if(this.value != null){
			NavigationHistoryManager.getInstance().go(this.value);
		}
	}

	public void setValueName(String name) {
		this.valueName.setText(name == null ? "" : name);
	}

	@Override
	public void focus() {
		return;
	}

}
