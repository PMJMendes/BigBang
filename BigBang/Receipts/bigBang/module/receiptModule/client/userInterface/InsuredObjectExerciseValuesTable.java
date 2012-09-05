package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.ui.Grid;

import bigBang.definitions.shared.InsuredObjectOLD.Exercise;
import bigBang.definitions.shared.InsuredObjectOLD.HeaderData;
import bigBang.library.client.userInterface.view.View;

public class InsuredObjectExerciseValuesTable extends View {
	
	protected final int ROW_OFFSET = 1;
	protected final int COLUMN_OFFSET = 1;

	protected Grid grid;

	public InsuredObjectExerciseValuesTable(){
		grid = new Grid();
		initWidget(grid);
		grid.setSize("100%", "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	public void setHeaders(HeaderData headers){
		clearAll();
		grid.resize(ROW_OFFSET, COLUMN_OFFSET + headers.variableFields.length);
		
		for(int i = 0; i < headers.variableFields.length; i++) {
			grid.setText(ROW_OFFSET + i, 0, headers.variableFields[i].fieldName + "("+headers.variableFields[i].unitsLabel+")");
		}
	}
	
	public void setValues(){
		
	}
	
	public Exercise getCoverageValues(){
		return null; 
	}

	public void clearValues(){
		
	}
	
	public void clearGrid(){
		
	}
	
	public void clearAll(){
		clearValues();
		grid.resize(0, 0);
	}
	
}
