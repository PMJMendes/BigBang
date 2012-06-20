package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.dom.client.Style.Overflow;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Exercise.CoverageData;
import bigBang.definitions.shared.Exercise.HeaderData;
import bigBang.definitions.shared.Exercise.HeaderData.FixedField;
import bigBang.definitions.shared.Exercise.HeaderData.VariableField;
import bigBang.definitions.shared.Exercise.HeaderData.VariableField.VariableValue;
import bigBang.definitions.shared.Exercise.InsuredObject;
import bigBang.definitions.shared.InsurancePolicy.FieldType;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.TwoKeyTable.Type;
import bigBang.library.client.userInterface.TwoKeyTableView;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class ExerciseForm extends FormView<Exercise> {

	public class DynFormField extends FormField<String> { 

		public String id;
		protected FormField<?> field;
		protected FixedField headerField;
		protected String coverageId;

		public DynFormField(FixedField fixedFields){
			super();
			id = fixedFields.fieldId;
			headerField = fixedFields;

			switch(fixedFields.type) {
			case LIST:
				ExpandableListBoxFormField listField = new ExpandableListBoxFormField(fixedFields.fieldName);
				listField.setEditable(true);
				listField.setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+fixedFields.fieldId, null);
				field = listField;
				break;
			case REFERENCE:
				ExpandableListBoxFormField referenceListField = new ExpandableListBoxFormField(fixedFields.fieldName);
				referenceListField.setEditable(true);
				referenceListField.setListId(fixedFields.refersToId, null);
				field = referenceListField;
				break;
			case NUMERIC:
				field = new NumericTextBoxFormField(fixedFields.fieldName);
				break;
			case TEXT:
				field = new TextBoxFormField(fixedFields.fieldName);
				break;
			case BOOLEAN:
				RadioButtonFormField radioField = new RadioButtonFormField(fixedFields.fieldName);
				radioField.addOption("1", "Sim");
				radioField.addOption("0", "Não");
				field = radioField;
				break;
			case DATE:
				DatePickerFormField dateField = new DatePickerFormField(fixedFields.fieldName);
				field = dateField;
				break;
			default:
				break;
			}
			field.setUnitsLabel(fixedFields.unitsLabel);
			initWidget(field);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValue(String value){
			if(headerField.type == FieldType.DATE){	
				((DatePickerFormField)field).setValue(value);		//this.dynamicVariableHeaderDataTable.removeFromParent();
			}else if(headerField.type == FieldType.NUMERIC){	
				((NumericTextBoxFormField)field).setStringValue(value);		//this.dynamicVariableHeaderDataTable.removeFromParent();
			}
			else
				((FormField<String>)field).setValue(value);
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getValue() {
			if(headerField.type == FieldType.DATE){	
				return ((DatePickerFormField)field).getStringValue();
			}else if(headerField.type == FieldType.NUMERIC){	
				return ((NumericTextBoxFormField)field).getStringValue();
			}
			else{
				return ((FormField<String>)field).getValue();
			}
		}

		@Override
		public void clear() {
			field.clear();
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
		public void focus() {
			field.getElement().focus();
		}

	}
	private String ownerId;
	private String id;
	private TextBoxFormField label;
	private DatePickerFormField startDate;
	private DatePickerFormField endDate;

	//HEADER DATA
	private FormViewSection commonSection;
	private DynFormField[] fixedFormFields;
	private HeaderData headerData;
	private InsuredObject[] objects;

	//COVERAGES
	private CoverageData[] coverageData;
	private FormViewSection[] coverageSections;
	private DynFormField[][] coverageFixedFormFields;

	//VARIABLE
	private TwoKeyTableView dynamicVariableHeaderDataTable;
	private FormViewSection dynamicHeaderSection;

	private TwoKeyTableView[] dynamicVariableDataTable;
	private FormViewSection[] dynamicSection;


	public ExerciseForm(){
		super();
		this.scrollWrapper.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		
		//COMMON SECTION
		commonSection = new FormViewSection("Informação Geral");
		label = new TextBoxFormField("Identificação");
		label.setFieldWidth("300px");
		startDate = new DatePickerFormField("Data de Início");
		endDate = new DatePickerFormField("Data de Fim");
		commonSection.addFormField(label, true);
		commonSection.addFormField(startDate, true);
		commonSection.addFormField(endDate,true);
		addSection(commonSection);

		dynamicVariableHeaderDataTable = new TwoKeyTableView();
		dynamicHeaderSection = new FormViewSection("Header");
	}

	@Override
	public Exercise getInfo() {


		Exercise result = this.value;
		result.ownerId = ownerId;
		result.id = id;
		result.label = label.getValue();
		result.startDate = startDate.getStringValue();
		result.endDate = endDate.getStringValue();

		//FIXED FIELDS
		result.headerData.fixedFields = getFixedFields();

		//DYNAMIC HEADER DATA
		result.headerData.variableFields = getVariableFields(headerData.variableFields, dynamicVariableHeaderDataTable, headerData.variableFields.length);

		//COVERAGES
		result.coverageData = getCoverageData();

		for (int i = 0; i<coverageData.length; i++){
			result.coverageData[i].variableFields = getVariableFields(coverageData[i].variableFields, dynamicVariableDataTable[i], coverageData[i].variableFields.length);
		}

		result.objects = objects;		

		return result;
	}

	private VariableField[] getVariableFields(VariableField[] variables, TwoKeyTableView table, int ammountVar) {
		Field[] fields = table.getAllValues();
		
		for(int i = 0; i < ammountVar; i++){
			for(int j = 0; j<fields.length/ammountVar; j++){
				variables[i].data[j].value = table.getValue(variables[i].fieldId, objects[j].id).value;
			}
		}
		return variables;
	}

	private CoverageData[] getCoverageData() {
		int coverageCount = coverageFixedFormFields.length;
		CoverageData[] coverages = coverageData;

		for(int i = 0; i < coverageCount; i++){
			for(int j = 0; j < coverageData[i].fixedFields.length; j++){
				coverages[i].fixedFields[j].value = coverageFixedFormFields[i][j].getValue();
			}
		}
		return coverages;
	}

	private FixedField[] getFixedFields() {
		int numFixedFields = fixedFormFields.length;
		FixedField[] fixedFields = new FixedField[numFixedFields];

		for (int i = 0; i<numFixedFields; i++){
			fixedFields[i] = fixedFormFields[i].headerField;
			fixedFields[i].value = fixedFormFields[i].getValue();
		}
		return fixedFields;
	}


	@Override
	public void setInfo(Exercise info) {
		clearFormFields();

		ownerId = info.ownerId;	
		id = info.id;
		label.setValue(info.label);
		startDate.setValue(info.startDate);
		endDate.setValue(info.endDate);

		headerData = info.headerData;
		objects = info.objects;

		//FIXEDFIELDS
		int numFixedFields = headerData.fixedFields.length;
		fixedFormFields = new DynFormField[numFixedFields];

		for (int i = 0; i<numFixedFields; i++){

			fixedFormFields[i] = new DynFormField(headerData.fixedFields[i]);
			fixedFormFields[i].setReadOnly(this.isReadOnly());
			fixedFormFields[i].setValue(headerData.fixedFields[i].value);
			addFormField(fixedFormFields[i]);
		}

		setDynamicHeaderData(info.objects, info.headerData);
		dynamicVariableHeaderDataTable.setReadOnly(this.isReadOnly());
		commonSection.addWidget(dynamicVariableHeaderDataTable, false);

		//COVERAGES    
		coverageData = info.coverageData; 
		int numCoverages = coverageData.length;

		coverageSections = new FormViewSection[numCoverages];
		coverageFixedFormFields = new DynFormField[numCoverages][];
		dynamicVariableDataTable = new TwoKeyTableView[numCoverages];
		dynamicSection = new FormViewSection[numCoverages];

		if(numCoverages > 0){

			for(int i = 0; i<numCoverages; i++){

				coverageSections[i] = new FormViewSection(coverageData[i].coverageLabel);
				dynamicVariableDataTable[i] = new TwoKeyTableView();
				dynamicSection[i] = new FormViewSection("Coverage "+ i);
				numFixedFields = coverageData[i].fixedFields.length;		
				coverageFixedFormFields[i] = new DynFormField[numFixedFields];
				addSection(coverageSections[i]);

				for(int j = 0; j < numFixedFields; j++){
					coverageFixedFormFields[i][j] = new DynFormField(coverageData[i].fixedFields[j]);
					coverageFixedFormFields[i][j].setValue(coverageData[i].fixedFields[j].value);
					coverageFixedFormFields[i][j].setReadOnly(this.isReadOnly());
					addFormField(coverageFixedFormFields[i][j]);
				}

				setDynamicVariableData(info.objects, coverageData[i].variableFields, i);
				coverageSections[i].addWidget(dynamicVariableDataTable[i], false);
			}
		}
	}

	private void setDynamicVariableData(InsuredObject[] objects2,
			VariableField[] fields, int k) {
		if(objects == null){
			return;
		}
		if(fields == null){
			return;
		}

		HeaderCell[] rowHeaders = new HeaderCell[fields.length];
		HeaderCell[] columnHeaders = new HeaderCell[objects.length];

		for(int i = 0; i < fields.length; i++) {
			HeaderCell header = new HeaderCell();
			header.id = fields[i].fieldId;
			header.text = fields[i].fieldName;
			rowHeaders[i] = header;
		}
		for(int i = 0; i < objects.length; i++) {
			HeaderCell header = new HeaderCell();		
			header.id = objects[i].id;
			header.text = objects[i].label;
			columnHeaders[i] = header;
		}
		dynamicVariableDataTable[k].setHeaders(rowHeaders, columnHeaders);

		for(int i = 0; i < fields.length; i++){
			VariableField field = fields[i];

			for(int j = 0; j < field.data.length; j++) {
				VariableValue fieldValue = field.data[j];
				String exerciseId = objects[j].id;
				Field tableField = new Field();
				tableField.value = fieldValue.value;
				switch(field.type){
				case BOOLEAN:
					tableField.type = Type.BOOLEAN;
					break;
				case DATE:
					tableField.type = Type.DATE;
					break;
				case LIST:
					tableField.type = Type.LIST;
					tableField.id = field.fieldId;
					break;
				case NUMERIC:
					tableField.type = Type.NUMERIC;
					break;
				case REFERENCE:
					tableField.type = Type.REFERENCE;
					tableField.reference = field.refersToId;
					break;		//this.dynamicVariableHeaderDataTable.removeFromParent();
				case TEXT:
					tableField.type = Type.TEXT;
					break;
				}
				tableField.value = fieldValue.value;

				this.dynamicVariableDataTable[k].setValue(field.fieldId, exerciseId, tableField);
			}
		}

		this.dynamicSection[k].addWidget(this.dynamicVariableDataTable[k], false);
		this.dynamicVariableDataTable[k].render();
		this.dynamicVariableDataTable[k].setReadOnly(this.isReadOnly());
	}

	private void clearFormFields() {


		if (fixedFormFields != null && fixedFormFields.length > 0){

			for (int i = 0; i<fixedFormFields.length; i++){
				commonSection.unregisterFormField(fixedFormFields[i]);
			}

		}

		if(dynamicHeaderSection != null){

			dynamicVariableHeaderDataTable.removeFromParent();
			dynamicHeaderSection.unregisterAllFormFields();
		}

		if (coverageFixedFormFields != null && coverageFixedFormFields.length > 0){

			for (int i = 0; i<coverageSections.length; i++){

				coverageSections[i].unregisterAllFormFields();
				removeSection(coverageSections[i]);

			}

		}
	}

	protected void setDynamicVariableHeaderData(InsuredObject[] objects, VariableField[] fields){
		dynamicVariableHeaderDataTable = new TwoKeyTableView();

		if(objects == null){
			return;
		}
		if(fields == null){
			return;
		}

		HeaderCell[] rowHeaders = new HeaderCell[fields.length];
		HeaderCell[] columnHeaders = new HeaderCell[objects.length];

		for(int i = 0; i < fields.length; i++) {
			HeaderCell header = new HeaderCell();
			header.id = fields[i].fieldId;
			header.text = fields[i].fieldName;
			rowHeaders[i] = header;
		}
		for(int i = 0; i < objects.length; i++) {
			HeaderCell header = new HeaderCell();
			header.id = objects[i].id;
			header.text = objects[i].label;
			columnHeaders[i] = header;
		}
		dynamicVariableHeaderDataTable.setHeaders(rowHeaders, columnHeaders);

		for(int i = 0; i < fields.length; i++){
			VariableField field = fields[i];

			for(int j = 0; j < field.data.length; j++) {
				VariableValue fieldValue = field.data[j];
				String exerciseId = objects[j].id;
				Field tableField = new Field();
				tableField.value = fieldValue.value;
				switch(field.type){
				case BOOLEAN:
					tableField.type = Type.BOOLEAN;
					break;
				case DATE:
					tableField.type = Type.DATE;
					break;
				case LIST:
					tableField.type = Type.LIST;
					tableField.id = field.fieldId;
					break;
				case NUMERIC:
					tableField.type = Type.NUMERIC;
					break;
				case REFERENCE:
					tableField.type = Type.REFERENCE;
					tableField.reference = field.refersToId;
					break;
				case TEXT:
					tableField.type = Type.TEXT;
					break;
				}
				tableField.value = fieldValue.value;
				this.dynamicVariableHeaderDataTable.setValue(field.fieldId, exerciseId, tableField);
			}
		}

		this.dynamicHeaderSection.addWidget(this.dynamicVariableHeaderDataTable, false);
		this.dynamicVariableHeaderDataTable.render();
		this.dynamicVariableHeaderDataTable.setReadOnly(this.isReadOnly());
	}

	protected void setDynamicHeaderData(InsuredObject[] objects, HeaderData headerData){
		if(headerData == null) {return;}
		setDynamicVariableHeaderData(objects, headerData.variableFields);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(this.coverageFixedFormFields != null){
			for(int i = 0; this.coverageFixedFormFields != null && i < this.coverageFixedFormFields.length; i++){
				for(int j = 0; j < this.coverageFixedFormFields[i].length; j++) {
					this.coverageFixedFormFields[i][j].setReadOnly(readOnly);
				}
			}
		}
		if(this.dynamicVariableHeaderDataTable != null) {
			this.dynamicVariableHeaderDataTable.setReadOnly(readOnly);
		}
		if(this.dynamicVariableDataTable != null) {
			for(int i = 0; this.dynamicVariableDataTable != null && i < this.dynamicVariableDataTable.length; i++){
				this.dynamicVariableDataTable[i].setReadOnly(readOnly);
			}
		}
	}

}
