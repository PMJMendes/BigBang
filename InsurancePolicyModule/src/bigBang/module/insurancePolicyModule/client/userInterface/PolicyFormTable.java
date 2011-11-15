package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.View;

public class PolicyFormTable extends View {
	
	protected ExpandableListBoxFormField insuredObjectField;
	protected ExpandableListBoxFormField exerciseField;
	
	protected Grid grid;

	public PolicyFormTable(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		HorizontalPanel filtersWrapper = new HorizontalPanel();
		wrapper.add(filtersWrapper);
		
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
		this.grid.resizeColumns(columns.length);
		for(int i = 0; i < columns.length; i++) {
			this.grid.setText(0, i, columns[i].label + " (" + columns[i].unitsLabel + ")");
		}
	}
	
	public void clear() {
		this.grid.clear();
	}
	
}
