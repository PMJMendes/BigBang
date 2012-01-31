package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TwoKeyTable {

	public static enum Type {
		NUMERIC,
		TEXT,
		LIST,
		REFERENCE,
		BOOLEAN,
		DATE
	}
	
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
		public String reference;
		public Type type;
		public String value;
	}

	protected Map<String, Integer> rows;
	protected Map<String, Integer> columns;
	protected Map<String, String> rowNames;
	protected Map<String, String> columnNames;
	protected Field[][] fields;
	
	public TwoKeyTable(){
		rows = new HashMap<String, Integer>();
		rowNames = new HashMap<String, String>();
		columns = new HashMap<String, Integer>();
		columnNames = new HashMap<String, String>();
		this.fields = new Field[0][0];
	}

	public void setHeaders(HeaderCell[] rows, HeaderCell[] columns){
		clearValues();
		this.rows.clear();
		this.columns.clear();
		this.fields = new Field[rows.length][columns.length];

		for(int i = 0; i < rows.length; i++) {
			this.rows.put(rows[i].id, new Integer(i));
			this.rowNames.put(rows[i].id, rows[i].text);
		}
		for(int i = 0; i < columns.length; i++) {
			this.columns.put(columns[i].id, new Integer(i));
			this.columnNames.put(columns[i].id, columns[i].text);
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
			for(int j = 0; j < this.fields[i].length; j++){
				this.fields[i][j] = null; //TODO
			}
		}
	}
	
	public String[] getColumnHeaders(){
		Collection<String> keys = this.columns.keySet();
		String[] result = new String[keys.size()];
		int i = 0;
		for(String s : keys){
			result[i] = s;
			i++;
		}
		return result;
	}
	
	public String[] getRowHeaders(){
		Collection<String> keys = this.rows.keySet();
		String[] result = new String[keys.size()];
		int i = 0;
		for(String s : keys){
			result[i] = s;
			i++;
		}
		return result;
	}
	
	public String getColumnText(String columnId){
		return this.columnNames.get(columnId);
	}
	
	public String getRowText(String rowId){
		return this.rowNames.get(rowId);
	}
	
	protected Integer getColumnIndex(String columnId) {
		Integer result = columns.get(columnId); 
		return result;
	}

	protected Integer getRowIndex(String rowId){
		Integer result = rows.get(rowId);
		return result;
	}

}
