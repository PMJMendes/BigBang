package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.definitions.shared.InsurancePolicy.FieldType;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicy.TableSection.TableField;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PolicyFormTable extends View implements HasValue<TableSection> {

	protected class Field extends FormField<String> {
		public String id;
		protected FormField<?> field;
		protected TableField tableField;
		protected String coverageId;
		protected FieldType type;

		public Field(TableField tableField, ColumnHeader columnDefinition) {
			super();
			this.id = tableField.fieldId;
			this.tableField = tableField;
			this.type = columnDefinition.type;

			switch(columnDefinition.type) {
			case LIST:
				ExpandableListBoxFormField listField = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+tableField.fieldId, "");
				this.field = listField;
				break;
			case REFERENCE:
				ExpandableListBoxFormField referenceListField = new ExpandableListBoxFormField(columnDefinition.refersToId, "");
				this.field = referenceListField;
				break;
			case NUMERIC:
				field = new TextBoxFormField(new FieldValidator<String>() {

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
			if(this.type == FieldType.DATE){
				((DatePickerFormField) this.field).setValue(value);
			}else{
				((FormField<String>)this.field).setValue(value, fireEvents);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getValue() {
			if(this.type == FieldType.DATE){
				return ((DatePickerFormField) this.field).getStringValue();
			}else{
				return ((FormField<String>)this.field).getValue();
			}
		}
	}


	protected static final int DATA_COLUMN_OFFSET = 2;
	protected static final int DATA_ROW_OFFSET = 1;

	protected boolean forNewPolicy;

	protected Coverage[] coverages;	
	protected RadioButtonFormField[] radioFields;
	protected ColumnHeader[] columnDefinitions;
	protected Map<String, Integer> coverageIndexes;
	protected String currentPageId;
	protected boolean readOnly;

	protected Map<String, Map<String, Field>> tableFields;

	protected TableSection section;

	protected Grid grid;

	public PolicyFormTable(){
		this(true);
	}

	public PolicyFormTable(boolean forNewPolicy){
		this.forNewPolicy = forNewPolicy;

		columnDefinitions = new ColumnHeader[0];
		coverageIndexes = new HashMap<String, Integer>();
		tableFields = new HashMap<String, Map<String,Field>>();

		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);

		this.coverages = new Coverage[0];

		HorizontalPanel filtersWrapper = new HorizontalPanel();
		wrapper.add(filtersWrapper);

		grid = new Grid();
		wrapper.add(grid);

		this.setReadOnly(false);
	}

	@Override
	protected void initializeView() {}

	public void setColumnDefinitions(ColumnHeader[] columns) {
		clear();
		this.columnDefinitions = new ColumnHeader[columns.length];
		this.grid.resize(1, DATA_COLUMN_OFFSET + columns.length);
		this.grid.setText(0, 0, "Incluir");
		this.grid.setText(0, 1, "Nome");

		for(int i = 0; i < columns.length; i++) {
			this.grid.setText(0, DATA_COLUMN_OFFSET + i, columns[i].label + " (" + columns[i].unitsLabel + ")");
			this.columnDefinitions[i] = columns[i];
		}

		this.grid.getColumnFormatter().setWidth(0, "110px");
	}

	public void clear() {
		//this.columnDefinitions = new 
	}
	
	public void clearFields(){
		for(String key : this.tableFields.keySet()){
			Map<String, Field> coverageFields = this.tableFields.get(key);
			for(Field f : coverageFields.values()) {
				f.removeFromParent();
			}
			this.tableFields.get(key).clear();
		}
	}

	public void setCoverages(Coverage[] coverages){
		this.clearRows();
		this.tableFields.clear();
		this.coverageIndexes.clear();
		this.coverages = coverages;
		this.radioFields = new RadioButtonFormField[coverages.length];
		for(int i = 0;  i < coverages.length; i++) {
			addCoverageLine(coverages[i], i);
			this.coverageIndexes.put(coverages[i].coverageId, i);
		}
	}

	public void clearRows(){
		this.grid.resizeRows(DATA_ROW_OFFSET);
	}

	protected void addCoverageLine(Coverage coverage, final int rfIndex) {
		Map<String, Field> coverageFields = new HashMap<String, Field>();
		this.tableFields.put(coverage.coverageId, coverageFields);

		int rowCount = this.grid.getRowCount()+1;
		this.grid.resizeRows(rowCount);
		int rowIndex = rowCount - 1;

		if(rowIndex % 2 == 1){
			this.grid.getRowFormatter().getElement(rowIndex).getStyle().setBackgroundColor("#CCC");
		}

		RadioButtonFormField radio = new RadioButtonFormField();
		radio.addOption("1", "Sim");
		radio.addOption("0", "Não");

		if((coverage.mandatory && coverage.presentInPolicy == null) || (coverage.presentInPolicy != null && coverage.presentInPolicy)){
			radio.setValue("1");
		}else if(coverage.presentInPolicy != null && !coverage.presentInPolicy){
			radio.setValue("0");
		}
		radio.allowEdition((!coverage.mandatory) || (coverage.presentInPolicy != null && !coverage.presentInPolicy));

		radio.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equalsIgnoreCase("1")){
					enableCoverageAtIndex(rfIndex);
				}else{
					disableCoverageAtIndex(rfIndex);
				}
				refreshFields();
			}
		});
		this.grid.setWidget(rowIndex, 0, radio);
		radio.setReadOnly(this.readOnly);
		this.grid.setText(rowIndex, 1, coverage.coverageName);
		this.radioFields[rfIndex] = radio;
	}

	public void setData(TableSection section){
		if(section == null){return;}
		this.section = section;

		clearFields();
		
		for(int i = 0; i < section.data.length; i++) {
			TableField tableField = section.data[i];
			ColumnHeader column = columnDefinitions[tableField.columnIndex];
			int coverageIndex = coverageIndexes.get(tableField.coverageId);

			Field field = new Field(tableField, column);
			field.setValue(tableField.value);
			Map<String, Field> coverageColumns = this.tableFields.get(tableField.coverageId);
			coverageColumns.put(tableField.columnIndex+"", field);

			field.setFieldWidth("175px");
			field.setReadOnly(readOnly);
			this.grid.setWidget(DATA_ROW_OFFSET + coverageIndex, DATA_COLUMN_OFFSET + tableField.columnIndex, field);
		}
		refreshFields();
	}

	public TableSection getData(){
		for(int i = 0; this.section != null && i < this.section.data.length; i++) {
			Field field = this.tableFields.get(this.section.data[i].coverageId).get(this.section.data[i].columnIndex+"");
			this.section.data[i].value = field == null ? null : field.getValue();
		}

		return this.section;
	}

	public Coverage[] getCoveragesData(){
		for(int i = 0; i < this.radioFields.length; i++) {
			String value = this.radioFields[i].getValue();
			this.coverages[i].presentInPolicy = value == null ? null : value.equalsIgnoreCase("1") ? true : value.equalsIgnoreCase("0") ? false : null;
		}
		return this.coverages;
	}

	protected void refreshFields(){
		for(int i = 0; i < this.radioFields.length; i++) {
			RadioButtonFormField field = this.radioFields[i];
			String value = field.getValue();
			if(value == null) {
				nullifyCoverageAtIndex(i);
			}else if(value.equalsIgnoreCase("0")){
				disableCoverageAtIndex(i);
			}else if(value.equalsIgnoreCase("1") && !readOnly){
				enableCoverageAtIndex(i);
			}
		}
	}

	protected void disableCoverageAtIndex(int index){
		this.radioFields[index].setValue("0", false);
		Coverage coverage = coverages[index];
		Map<String, Field> fields = this.tableFields.get(coverage.coverageId);

		for(Field f : fields.values()) {
			f.clear();
			f.setReadOnly(true);
		}
	}

	protected void enableCoverageAtIndex(int index){
		this.radioFields[index].setValue("1", false);
		Coverage coverage = coverages[index];
		Map<String, Field> fields = this.tableFields.get(coverage.coverageId);

		for(Field f : fields.values()) {
			f.setReadOnly(false);
		}
	}

	protected void nullifyCoverageAtIndex(int index){
		this.radioFields[index].setValue(null, false);
		Coverage coverage = coverages[index];
		Map<String, Field> fields = this.tableFields.get(coverage.coverageId);

		for(Field f : fields.values()) {
			f.clear();
			f.setReadOnly(true);
		}
	}

	public void setReadOnly(boolean readOnly) {
		if(this.radioFields != null){
			
			for(int i = 0; i < this.radioFields.length; i++) {
				RadioButtonFormField f = this.radioFields[i];
				f.setReadOnly(readOnly);
				
				boolean coverageIsEnabled = f.getValue() != null && f.getValue().equals("1");
				
				Coverage coverage = this.coverages[i];
				
				for(Field cf : this.tableFields.get(coverage.coverageId).values()){
					cf.setReadOnly(!(!readOnly && coverageIsEnabled));
				}
			}
		}
		this.readOnly = readOnly;
	}

	public boolean isReadOnly(){
		return this.readOnly;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<TableSection> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public TableSection getValue() {
		return getData();
	}

	@Override
	public void setValue(TableSection value) {
		setValue(value, true);
	}

	@Override
	public void setValue(TableSection value, boolean fireEvents) {
		setData(value);
		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}

}
