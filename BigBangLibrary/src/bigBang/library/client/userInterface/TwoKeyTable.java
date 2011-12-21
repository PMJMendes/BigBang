package bigBang.library.client.userInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class TwoKeyTable {

	public static class Key {
		public String k1;
		public String k2;
	}
	
	public static class HeaderCell {
		public String text;
		public String id;
	}
	
	public static class Field{
		public String id;
		public String value;
	}

	protected Map<String, Integer> rows;
	protected Map<String, Integer> columns;
	protected Field[][] fields;
	
	public TwoKeyTable(){
		rows = new HashMap<String, Integer>();
		columns = new HashMap<String, Integer>();
		this.fields = new Field[0][0];
	}

	public void setHeaders(HeaderCell[] rows, HeaderCell[] columns){
		clearValues();
		this.rows.clear();
		this.columns.clear();
		this.fields = new Field[rows.length][columns.length];

		for(int i = 0; i < rows.length; i++) {
			this.rows.put(rows[i].id, i);
		}
		for(int i = 0; i < columns.length; i++) {
			this.columns.put(columns[i].id, i);
		}
	}

	public void setValue(String rowId, String columnId, Field value){
		int rowIndex = getRowIndex(rowId);
		int columnIndex = getColumnIndex(columnId);
		this.fields[rowIndex][columnIndex] = value;
	}
	
	public Field getValue(String rowId, String columnId) {
		int rowIndex = getRowIndex(rowId);
		int columnIndex = getColumnIndex(columnId);
		return this.fields[rowIndex][columnIndex];
	}
	
	public Field[] getAllValues(){
		if(this.fields.length == 0 || this.fields[0].length == 0){
			return new Field[0];
		}
		Field[] result = new Field[this.fields.length * this.fields[0].length];
		for(int i = 0; i < this.fields.length; i++) {
			for(int j = 0; j < this.fields[0].length; i++) {
				result[(i * this.fields[0].length) + j] = this.fields[i][j];
			}
		}
		return result;
	}

	public void clearValues(){
		for(int i = 0; i < this.fields.length; i++) {
			for(int j = 0; j < this.fields[i].length; i++){
				this.fields[i][j] = null; //TODO
			}
		}
	}
	
	public String[] getColumnHeaders(){
		Set<String> keys = this.columns.keySet();
		String[] result = new String[keys.size()];
		int i = 0;
		for(String s : keys){
			result[i] = s;
			i++;
		}
		return result;
	}
	
	public String[] getRowHeaders(){
		Set<String> keys = this.rows.keySet();
		String[] result = new String[keys.size()];
		int i = 0;
		for(String s : keys){
			result[i] = s;
			i++;
		}
		return result;
	}
	
	protected Integer getColumnIndex(String columnId) {
		return columns.get(columnId);
	}

	protected Integer getRowIndex(String rowId){
		return columns.get(rowId);
	}

}
