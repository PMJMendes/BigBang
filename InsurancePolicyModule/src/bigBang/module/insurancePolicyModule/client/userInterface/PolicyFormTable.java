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

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PolicyFormTable extends View {
	
	protected static final int DATA_COLUMN_OFFSET = 2;
	protected static final int DATA_ROW_OFFSET = 1;
	
	protected Coverage[] coverages;	
	protected RadioButtonFormField[] radioFields;
	protected ExpandableListBoxFormField insuredObjectField;
	protected ExpandableListBoxFormField exerciseField;
	protected Widget filtersWrapper;
	protected boolean forNewPolicy;
	
	protected ColumnHeader[] columnDefinitions;
	protected Map<String, Integer> coverageIndexes;
	protected String currentPageId;
	
	protected Grid grid;

	public PolicyFormTable(){
		this(true);
	}
	
	public PolicyFormTable(boolean forNewPolicy){
		this.forNewPolicy = forNewPolicy;
		
		columnDefinitions = new ColumnHeader[0];
		coverageIndexes = new HashMap<String, Integer>();
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		this.coverages = new Coverage[0];
		
		HorizontalPanel filtersWrapper = new HorizontalPanel();
		wrapper.add(filtersWrapper);
		this.filtersWrapper = filtersWrapper;
		
		this.insuredObjectField = new ExpandableListBoxFormField("Unidade de Risco");
		filtersWrapper.add(insuredObjectField);
		
		this.exerciseField = new ExpandableListBoxFormField("Exercício");
		filtersWrapper.add(exerciseField);
		
		grid = new Grid();
		wrapper.add(grid);
		grid.setSize("100%", "100%");
	}
	
	public String getInsuredObjectFilterValue(){
		return this.insuredObjectField.getValue();
	}
	
	public String getExerciseFilterValue(){
		return this.exerciseField.getValue();
	}

	public void setInsuredObjectFilterValue(String value){
		this.insuredObjectField.setValue(value);
	}

	public void setExerciseFilterValue(String value) {
		this.exerciseField.setValue(value);
	}

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
		this.grid.clear();
	}

	public void setCoverages(Coverage[] coverages){
		this.clearRows();
		this.coverageIndexes.clear();
		this.coverages = coverages;
		this.radioFields = new RadioButtonFormField[coverages.length];
		for(int i = 0;  i < coverages.length; i++) {
			addCoverageLine(coverages[i]);
			this.coverageIndexes.put(coverages[i].coverageId, i);
		}
	}
	
	public void clearRows(){
		this.grid.resizeRows(DATA_ROW_OFFSET);
	}
	
	protected void addCoverageLine(Coverage coverage) {
		int rowCount = this.grid.getRowCount()+1;
		this.grid.resizeRows(rowCount);
		int rowIndex = rowCount - 1;
		
		if(rowIndex % 2 == 1){
			this.grid.getRowFormatter().getElement(rowIndex).getStyle().setBackgroundColor("#CCC");
		}

		RadioButtonFormField radio = new RadioButtonFormField();
		radio.addOption("YES", "Sim");
		radio.addOption("NO", "Não");
		if(coverage.presentInPolicy != null && coverage.presentInPolicy){
			radio.setValue("YES");
			if(this.forNewPolicy){
				radio.setEditable(false);
			}
		}
		
		this.grid.setWidget(rowIndex, 0, radio);
		this.grid.setText(rowIndex, 1, coverage.coverageName);
	}
	
	public void setData(TableSection section){
		if(section == null){return;}
		this.currentPageId = section.pageId;
		for(int i = 0; i < section.data.length; i++) {
			TableField tableField = section.data[i];
			ColumnHeader column = columnDefinitions[tableField.columnIndex];
			int coverageIndex = coverageIndexes.get(tableField.coverageId);
			
			FormField<?> field = null;
			
			switch(column.type) {
			case LIST:
				ExpandableListBoxFormField listField = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+tableField.fieldId, "");
				field = listField;
				break;
			case REFERENCE:
				ExpandableListBoxFormField referenceListField = new ExpandableListBoxFormField(column.refersToId, "");
				field = referenceListField;
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
				field = new TextBoxFormField();
				break;
			case BOOLEAN:
				RadioButtonFormField radioField = new RadioButtonFormField();
				radioField.addOption("1", "Sim");
				radioField.addOption("0", "Não");
				field = radioField;
				break;
			case DATE:
				DatePickerFormField dateField = new DatePickerFormField();
				field = dateField;
				break;
			default:
				break;
			}
			field.setWidth("100%");
			field.setFieldWidth("100%");
			this.grid.setWidget(DATA_ROW_OFFSET + coverageIndex, DATA_COLUMN_OFFSET + tableField.columnIndex, field);
		}
	}
	
	public void setFilterable(boolean filterable) {
		this.filtersWrapper.setVisible(filterable);
	}
	
}
