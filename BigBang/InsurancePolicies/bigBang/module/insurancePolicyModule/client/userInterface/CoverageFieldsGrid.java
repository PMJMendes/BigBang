package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TableLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;

public class CoverageFieldsGrid extends Grid implements HasValue<ColumnField[]>{


	class Field extends GenericFormField{

		private String id;

		public Field(TYPE type) {
			super(type);
		}
		public Field(TYPE type, String id){
			super(type);
			this.id = id;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}
	}

	protected Field[][] fields;
	protected Grid grid;
	private ColumnField[] value;

	public CoverageFieldsGrid() {
		super(1,1);
		fields = new Field[0][0];
		grid = new Grid();
		ScrollPanel p = new ScrollPanel();
		p.setWidget(grid);
		p.getElement().getStyle().setOverflowY(Overflow.VISIBLE);
		p.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		this.setWidget(0, 0, p);
		this.getElement().getStyle().setTableLayout(TableLayout.FIXED);
		this.setSize("100%", "100%");
	}

	public void setHeaders(Coverage[] coverages, ColumnHeader[] headers){

		fields = new Field[coverages.length+1][headers.length+2];

		grid.clear();
		grid.resize(coverages.length+1, headers.length+2);

		fields[0][0] = new Field(TYPE.TEXT);
		fields[0][0].setEditable(false);
		fields[0][0].setValue("Incluir");
		grid.setWidget(0, 0, fields[0][0]);
		fields[0][0].setFieldWidth("100px");
		fields[0][0].setTextAlignment(TextAlignment.CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid.getCellFormatter().setWidth(0, 0, "150px");

		fields[0][1] = new Field(TYPE.TEXT);
		fields[0][1].setEditable(false);
		fields[0][1].setValue("Coberturas");
		grid.setWidget(0, 1, fields[0][1]);
		fields[0][1].setFieldWidth("100px");
		fields[0][1].setTextAlignment(TextAlignment.CENTER);
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);

		for(int i = 1; i<fields.length; i++){
			fields[i][0] = new Field(TYPE.BOOLEAN);
			fields[i][0].id = coverages[i-1].coverageId;
			fields[i][0].setValue(coverages[i-1].mandatory ? "1" : "0");
			fields[i][0].setEditable(coverages[i-1].mandatory ? false : true);
			fields[i][0].getElement().getStyle();
			fields[i][1] = new Field(TYPE.TEXT);
			fields[i][1].setEditable(false);
			fields[i][1].setValue(coverages[i-1].coverageName);

			grid.setWidget(i, 0, fields[i][0]);
			grid.setWidget(i, 1, fields[i][1]);
			if((i % 2) != 0){
				grid.getRowFormatter().getElement(i).getStyle().setBackgroundColor("#CCC");
			}
			fields[i][1].setTextAlignment(TextAlignment.CENTER);
			fields[i][1].setFieldWidth("200px");
			grid.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);
			grid.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
		}
		for(int j = 2; j<fields[0].length; j++){
			fields[0][j] = new Field(TYPE.TEXT);
			fields[0][j].setValue(headers[j-2].label + (headers[j-2].unitsLabel == null ? "" : " ("+headers[j-2].unitsLabel+")"));
			fields[0][j].setEditable(false);
			fields[0][j].setTextAlignment(TextAlignment.CENTER);
			fields[0][j].setFieldWidth("140px");
			grid.setWidget(0, j, fields[0][j]);
			grid.getCellFormatter().setHorizontalAlignment(0, j, HasHorizontalAlignment.ALIGN_CENTER);
		}

	}


	private void fillTable(ColumnField[] formFields) {
		int row;
		int column;
		for(int i = 0; i<formFields.length; i++){
			row = formFields[i].coverageIndex+1;
			column = formFields[i].columnIndex+2;
			switch(formFields[i].type){
			case BOOLEAN:
				fields[row][column] = new Field(TYPE.BOOLEAN);
				break;
			case DATE:
				fields[row][column] = new Field(TYPE.DATE);
				break;
			case LIST:
				fields[row][column] = new Field(TYPE.LIST);
				fields[row][column].setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+formFields[i].fieldId);
				break;
			case NUMERIC:
				fields[row][column] = new Field(TYPE.NUMBER);
				break;
			case REFERENCE:
				fields[row][column] = new Field(TYPE.REFERENCE);
				fields[row][column].setListId(formFields[i].refersToId);
				break;
			case TEXT:
				fields[row][column] = new Field(TYPE.TEXT);
				break;
			}
			fields[row][column].setValue(formFields[i].value);
			fields[row][column].setFieldWidth("100px");
			fields[row][column].setWidth("130px");
			fields[row][column].setReadOnly(formFields[i].readOnly);
			grid.setWidget(row, column, fields[row][column]);
			grid.getCellFormatter().setHorizontalAlignment(row, column, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ColumnField[]> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public ColumnField[] getValue() {
		ColumnField[] result = value;
		for(int i = 0; i<result.length; i++){
			result[i].value = fields[result[i].coverageIndex+1][result[i].columnIndex+2].getValue();
		}
		return result;
	}

	@Override
	public void setValue(ColumnField[] value) {
		setValue(value, true);
	}

	@Override
	public void setValue(ColumnField[] value, boolean fireEvents) {
		this.value = value;
		fillTable(value);

		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		
	}

}
