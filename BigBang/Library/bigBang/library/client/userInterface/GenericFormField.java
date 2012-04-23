package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;

public class GenericFormField extends FormField<String> {

	public static enum TYPE {
		TEXT,
		TEXT_BLOCK,
		NUMBER,
		LIST,
		REFERENCE,
		DATE,
		BOOLEAN
	}

	protected FormField<?> myField;
	protected TYPE myType = TYPE.TEXT;

	public GenericFormField(TYPE type){
		super();
		this.myType = type;
		
		switch(myType) {
		case TEXT:
			myField = new TextBoxFormField();
			break;
		case TEXT_BLOCK:
			myField = new TextAreaFormField();
			break;
		case REFERENCE:
			myField = new ExpandableListBoxFormField("");
			break;
		case LIST:
			myField = new ExpandableListBoxFormField("");
			break;
		case NUMBER:
			myField = new TextBoxFormField();
			break;
		case DATE:
			myField = new DatePickerFormField();
			break;
		case BOOLEAN:
			myField = new RadioButtonFormField();
			break;
		}
		
		initWidget(this.myField);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			final ValueChangeHandler<String> handler) {
		HandlerRegistration handlerRegistration = null;
		
		switch(myType) {
		case TEXT:
			@SuppressWarnings("unchecked")
			HasValue<String> textField = (HasValue<String>) this.myField;
			handlerRegistration = textField.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					handler.onValueChange(event);
				}
			});
			break;
		case TEXT_BLOCK:
			@SuppressWarnings("unchecked")
			HasValue<String> textBlockField = (HasValue<String>) this.myField;
			handlerRegistration = textBlockField.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					handler.onValueChange(event);
				}
			});
			break;
		case NUMBER:
			@SuppressWarnings("unchecked")
			HasValue<String> numberField = (HasValue<String>) this.myField;
			handlerRegistration = numberField.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					handler.onValueChange(event);
				}
			});
			break;
		case LIST:
			@SuppressWarnings("unchecked")
			HasValue<String> listField = (HasValue<String>) this.myField;
			handlerRegistration = listField.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					handler.onValueChange(event);
				}
			});
			break;
		case REFERENCE:
			@SuppressWarnings("unchecked")
			HasValue<String> referenceField = (HasValue<String>) this.myField;
			handlerRegistration = referenceField.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					handler.onValueChange(event);
				}
			});
			break;
		case DATE:
			@SuppressWarnings("unchecked")
			HasValue<Date> dateField = (HasValue<Date>) this.myField;
			handlerRegistration = dateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					//handler.onValueChange(handler.onValueChange(new ValueChangeEvent<String>(event.getValue() != null ? e){})); TODO
				}
			});
			break;
		case BOOLEAN:
			@SuppressWarnings("unchecked")
			HasValue<Boolean> booleanField = (HasValue<Boolean>) this.myField;
			handlerRegistration = booleanField.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					handler.onValueChange(new ValueChangeEvent<String>(event.getValue() ? "1" : "0"){});
				}
			});
			break;
		}

		return handlerRegistration;
	}

	public String getValue() {
		switch(myType) {
		case TEXT:
			return (String) myField.getValue();
		case TEXT_BLOCK:
			return (String) myField.getValue();
		case NUMBER:
			return (String) myField.getValue();
		case REFERENCE:
			return (String) myField.getValue();
		case LIST:
			return (String) myField.getValue();
		case DATE:
			return ((DatePickerFormField) myField).getStringValue();
		}
		return null;
	}

	public void setValue(String value) {
		this.setValue(value, true);
	}

	@SuppressWarnings("unchecked")
	public void setValue(String value, boolean fireEvents) {
		switch(myType) {
		case TEXT:
			((FormField<String>)myField).setValue(value);
			break;
		case TEXT_BLOCK:
			((FormField<String>)myField).setValue(value);
			break;
		case NUMBER:
			((FormField<String>)myField).setValue(value);
			break;
		case REFERENCE:
			((FormField<String>)myField).setValue(value);
			break;
		case LIST:
			((FormField<String>)myField).setValue(value);
			break;
		case DATE:
			((DatePickerFormField)myField).setValue(value);
			break;
		}
	}

	public void setListId(String listId){
		ExpandableListBoxFormField myList = (ExpandableListBoxFormField) this.myField;
		myList.setListId(listId, new ResponseHandler<Void>() {
			
			@Override
			public void onResponse(Void response) {
				return;
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	public void setLabel(String labelText){
		myField.setLabelText(labelText);
	}
	
	public void setValidator(FieldValidator<String> validator) {//TODO
		return;
	}

	public void setFieldWidth(String width) {
		myField.setFieldWidth(width);
	}

	public void clear() {
		myField.clear();
	}

	public void setReadOnly(boolean readonly) {
		myField.setReadOnly(readonly);
	}

	public boolean isReadOnly() {
		return myField.isReadOnly();
	}

	public boolean validate() {
		return myField.validate();
	}

	public void setInvalid(boolean invalid){
		myField.setInvalid(invalid);
	}

	public String getErrorMessage(){
		return myField.getErrorMessage();
	}

	public boolean isMandatory(){
		return myField.isMandatory();
	}

	public void allowEdition(boolean editable) {
		myField.allowEdition(editable);
	}

	public void showLabel(boolean show) {
		myField.showLabel(show);
	}

	public void setUnitsLabel(String label){
		myField.setUnitsLabel(label);
	}

	public void setLabelWidth(String width) {
		myField.setLabelWidth(width);
	}

}
