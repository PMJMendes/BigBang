package bigBang.library.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.TwoKeyTable.Type;
import bigBang.library.client.userInterface.view.View;

public class TwoKeyTableView extends View {

	public class DynFormField extends FormField<String> {

		protected FormField<?> field;
		protected Field headerField;
		protected String coverageId;

		public DynFormField(Field field){
			super();
			this.headerField = field;

			if(field != null){
				switch(field.type) {
				case LIST:
					ExpandableListBoxFormField listField = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+field.id);
					listField.allowEdition(true);
					this.field = listField;
					break;
				case REFERENCE:
					ExpandableListBoxFormField referenceListField = new ExpandableListBoxFormField(field.reference);
					referenceListField.allowEdition(true);
					this.field = referenceListField;
					break;
				case NUMERIC:
					this.field = new TextBoxFormField(new FieldValidator<String>() {

						@Override
						public boolean isValid(String value) {
							try{
								Integer.parseInt(value);
							}catch(Exception e){
								return false;
							}
							return true;
						}

						@Override
						public boolean isMandatory() {
							return false;
						}

						@Override
						public String getErrorMessage() {
							return "Apenas valores numéricos";
						}
					});
					break;
				case TEXT:
					this.field = new TextBoxFormField();
					break;
				case BOOLEAN:
					RadioButtonFormField radioField = new RadioButtonFormField();
					radioField.addOption("1", "Sim");
					radioField.addOption("0", "Não");
					this.field = radioField;
					break;
				case DATE:
					DatePickerFormField dateField = new DatePickerFormField();
					this.field = dateField;
					break;
				default:
					break;
				}
			}else{
				TextBoxFormField textField = new TextBoxFormField();
				textField.setEditable(false);
				textField.setReadOnly(false);
				this.field = textField;
			}
			initWidget(this.field);
		}

		@Override
		public void clear() {
			this.field.clear();
		}

		@Override
		public void setReadOnly(boolean readonly) {
			this.field.setReadOnly(readonly);
		}

		@Override
		public boolean isReadOnly() {
			return this.field.isReadOnly();
		}

		@Override
		public void setLabelWidth(String width) {
			this.field.setLabelWidth(width);
		}

		@Override
		public void setFieldWidth(String width) {
			this.field.setFieldWidth(width);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValue(String value, boolean fireEvents) {
			if(this.headerField == null) {
				return;
			} else if(this.headerField.type == Type.DATE){
				((DatePickerFormField) this.field).setValue(value);
			}else{
				((FormField<String>)this.field).setValue(value, fireEvents);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getValue() {
			if(this.headerField == null) {
				return null;
			} else if(this.headerField.type == Type.DATE){	
				return ((DatePickerFormField)field).getStringValue();
			}else{
				return ((FormField<String>)field).getValue();
			}
		}
	}

	protected TwoKeyTable table; 
	protected Grid grid;
	protected Map<String, Map<String, FormField<String>>> fields;
	protected boolean readOnly = false;

	public TwoKeyTableView(){
		ScrollPanel wrapper = new ScrollPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		fields = new HashMap<String, Map<String,FormField<String>>>();
		table = new TwoKeyTable();
		grid = new Grid();
		wrapper.add(grid);
	}

	@Override
	protected void initializeView() {
		return;
	}

	public void setHeaders(HeaderCell[] rows, HeaderCell[] columns){
		table.setHeaders(rows, columns);
	}

	public void setValue(String rowId, String columnId, Field value){
		table.setValue(rowId, columnId, value);
	}

	public Field getValue(String rowId, String columnId) {
		Field field = table.getValue(rowId, columnId);
		FormField<String> formField = getField(rowId, columnId);		
		String value = formField == null ? (field != null ? field.value : null) : formField.getValue();
		if(field != null){
			field.value = value;
		}
		table.setValue(rowId, columnId, field);
		return table.getValue(rowId, columnId);
	}

	public Field[] getAllValues(){
		return this.table.getAllValues();
	}

	public void clearValues(){
		table.clearValues();
	}

	public void render(){
		String[] rowHeaders = this.table.getRowHeaders();
		String[] columnHeaders = this.table.getColumnHeaders();
		this.grid.resize(rowHeaders.length + 1, columnHeaders.length + 1);

		for(int i = 0; i < rowHeaders.length + 1; i++){
			for(int j = 0; j < columnHeaders.length + 1; j++) {
				if(i == 0){
					if(j > 0){
						grid.setText(i, j, table.getColumnText(columnHeaders[j-1]));
					}
					grid.getCellFormatter().setHorizontalAlignment(i, j, HasHorizontalAlignment.ALIGN_CENTER);
				}
				if(i > 0 && j == 0) {
					grid.setText(i, j, table.getRowText(rowHeaders[i-1]));
					grid.getCellFormatter().setHorizontalAlignment(i, j, HasHorizontalAlignment.ALIGN_CENTER);
				}
				if(i > 0 && j > 0){
					FormField<String> formField = getFormField(table.getValue(rowHeaders[i-1], columnHeaders[j-1]));
					formField.setFieldWidth("175px");
					grid.setWidget(i, j, formField);
					grid.getCellFormatter().setHorizontalAlignment(i, j, HasHorizontalAlignment.ALIGN_LEFT);
					Field field = getValue(rowHeaders[i-1], columnHeaders[j-1]);
					formField.setValue(field == null ? null : field.value);
					putField(rowHeaders[i-1], columnHeaders[j-1], formField);
				}
				if((i % 2) != 0){
					grid.getRowFormatter().getElement(i).getStyle().setBackgroundColor("#CCC");
				}
			}
		}
		this.grid.getColumnFormatter().setWidth(0, "150px");
	}

	protected void clearFieldMaps(){
		this.fields.clear();
	}

	protected void putField(String rowId, String columnId,
			FormField<String> formField) {
		Map<String, FormField<String>> columnFields = this.fields.get(rowId);
		if(columnFields == null){
			columnFields = new HashMap<String, FormField<String>>();
		}
		columnFields.put(columnId, formField);
		this.fields.put(rowId, columnFields);
	}

	protected FormField<String> getField(String rowId, String columnId){
		Map<String, FormField<String>> columnFields = this.fields.get(rowId);
		if(columnFields == null){
			return null;
		}
		FormField<String> result = columnFields.get(columnId);
		return result;
	}

	protected FormField<String> getFormField(Field value) {
		return new DynFormField(value);
	}

	public void setReadOnly(boolean readOnly){
		this.readOnly = readOnly;
		for(Map<String,FormField<String>> m: this.fields.values()){

			for(FormField<String> field: m.values()){

				field.setReadOnly(readOnly);

			}

		}
	}

}
