package bigBang.library.client.userInterface;

import com.google.gwt.user.client.ui.Grid;

import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.view.View;

public class TwoKeyTableView extends View {

	protected TwoKeyTable table; 
	
	protected Grid grid;
	
	public TwoKeyTableView(){
		table = new TwoKeyTable();
		grid = new Grid();
		initWidget(grid);
	}
	
	public void setHeaders(HeaderCell[] rows, HeaderCell[] columns){
		table.setHeaders(rows, columns);
	}
	
	public void setValue(String rowId, String columnId, Field value){
		table.setValue(rowId, columnId, value);
	}

	public Field getValue(String rowId, String columnId) {
		return table.getValue(rowId, columnId);
	}
	
	public Field[] getAllValues(){
		return this.table.getAllValues();
	}

	public void clearValues(){
		table.clearValues();
	}
	
	public void render(){
		this.grid.setSize("100%", "100%");
		String[] rowHeaders = this.table.getRowHeaders();
		String[] columnHeaders = this.table.getColumnHeaders();
		this.grid.resize(rowHeaders.length + 1, columnHeaders.length + 1);

		for(int i = 0; i < rowHeaders.length; i++){
			for(int j = 0; j < columnHeaders.length; j++) {
				if(i == 0 && j > 0){
					grid.setText(i, j, columnHeaders[j-1]);
				}
				if(i > 0 && j == 0) {
					grid.setText(i, j, rowHeaders[i-1]);
				}
				if(i < 0 && j > 0){
					//TODO
					grid.setText(i, j, table.getValue(rowHeaders[i-1], columnHeaders[j-1]).value);
				}
			}
		}
	}
	
}
