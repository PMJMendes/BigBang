package bigBang.library.client.userInterface;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.PopupPanel;

public class ExpandableSelectionFormField extends FormField<String> {

	protected Label valueDisplayName;
	protected ExpandableSelectionFormFieldPanel selectionPanel;
	protected TypifiedListBroker broker;
	protected String listId, value;
	protected Image expandImage;
	protected boolean readOnly = false;

	public ExpandableSelectionFormField(String listId){
		this(listId, new String());
	}
	
	public ExpandableSelectionFormField(String listId, String label){
		this(listId, label, new TypifiedListSelectionPanel());
	}
	
	public ExpandableSelectionFormField(String listId, String label, ExpandableSelectionFormFieldPanel selectionPanel){
		super();
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		label = label == null ? "" : label;
		this.label.setText(label);
		
		HorizontalPanel innerWrapper = new HorizontalPanel();
		
		this.valueDisplayName = new Label();
		this.valueDisplayName.setWidth("150px");
		innerWrapper.add(this.valueDisplayName);
		
		this.listId = listId;
		
		this.broker = BigBangTypifiedListBroker.Util.getInstance();
		this.selectionPanel = selectionPanel;
		this.selectionPanel.setListId(listId);
		
		final PopupPanel popup = new PopupPanel();
		popup.add(selectionPanel);
		
		selectionPanel.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String value = event.getValue();
				if(!(value != null && value.equals("CANCELLED_SELECTION"))){
					ExpandableSelectionFormField.this.setValue(event.getValue());
				}
				popup.hidePopup();
			}
		});

		ClickHandler expandClickHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!isReadOnly()){
					popup.center();
				}
			}
		};
		
		this.valueDisplayName.addClickHandler(expandClickHandler);
		
		Resources r = GWT.create(Resources.class);
		expandImage = new Image(r.listExpandIcon());
		expandImage.getElement().getStyle().setCursor(Cursor.POINTER);
		expandImage.addClickHandler(expandClickHandler);
		innerWrapper.add(expandImage);
		innerWrapper.add(this.unitsLabel);
		innerWrapper.add(mandatoryIndicatorLabel);
		
		wrapper.add(this.label);
		wrapper.add(innerWrapper);
		
		setValue(null, false);
		setReadOnlyInternal(false);
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.setValue(value, true);
	}

	public void setValue(String value, final boolean fireEvents) {
		if(value == null) {
			this.value = null;
			this.valueDisplayName.setText("-");
			if(fireEvents){
				ValueChangeEvent.fire(this, value);
			}
		} else {
			broker.getListItem(this.listId, value, new ResponseHandler<TipifiedListItem>() {
				
				@Override
				public void onResponse(TipifiedListItem response) {
					ExpandableSelectionFormField.this.value = response.id;
					ExpandableSelectionFormField.this.valueDisplayName.setText(response.value == null ? "" : response.value);
					if(fireEvents){
						ValueChangeEvent.fire(ExpandableSelectionFormField.this, ExpandableSelectionFormField.this.value);
					}
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					setValue(null, fireEvents);
					GWT.log("Could not get the requested typified list item");
				}
			});
		}
	}
	
	

	@Override
	public void clear() {
		setValue(null);
	}

	@Override
	public void setReadOnlyInternal(boolean readonly) {
		this.readOnly = readonly;
		this.selectionPanel.setReadOnly(readonly);
		this.valueDisplayName.getElement().getStyle().setProperty("border", readonly ? "" : "1px gray dotted");
		this.expandImage.setVisible(!readonly);
		this.valueDisplayName.getElement().getStyle().setCursor(readonly ? Cursor.AUTO : Cursor.POINTER);
		mandatoryIndicatorLabel.setVisible(!readOnly && this.isMandatory());
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
		this.valueDisplayName.setWidth(width);
	}

	@Override
	public void focus() {
		selectionPanel.getElement().focus();
	}
	
	public void setParameters(HasParameters parameters){
		this.selectionPanel.setParameters(parameters);
	}

}
