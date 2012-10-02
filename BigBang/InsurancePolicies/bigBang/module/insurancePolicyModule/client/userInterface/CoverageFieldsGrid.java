package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TableLayout;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

//EXTENDS GRID por causa do layout da form (de outra forma a scrollbar nao aparece)
public abstract class CoverageFieldsGrid extends Grid implements HasValue<FieldContainer.ColumnField[]>{ 
	


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
	private FieldContainer.ColumnField[] value;
	private boolean readOnly;
	private Coverage[] coverages;

	public CoverageFieldsGrid() {
		super(1,1);
		fields = new Field[0][0];
		grid = new Grid();
		ScrollPanel p = new ScrollPanel();
		p.setWidget(grid);
		p.getElement().getStyle().setOverflowY(Overflow.VISIBLE);
		p.getElement().getStyle().setOverflowX(Overflow.AUTO);
		p.getElement().getStyle().setMarginRight(20, Unit.PX);
		this.setWidget(0,0,p);
		this.getElement().getStyle().setTableLayout(TableLayout.FIXED);
		this.setSize("100%", "100%");
	}

	public StructuredFieldContainer.Coverage[] getPresentCoverages(){

		for(int i = 1; i<fields.length; i++){
			coverages[i-1].presentInPolicy = fields[i][0].getValue() == null ? null : fields[i][0].getValue().equalsIgnoreCase("1");
		}

		return coverages;
	}

	public void setHeaders(StructuredFieldContainer.Coverage[] coverages, StructuredFieldContainer.ColumnHeader[] headers){

		//TODO VERIFICAR VISIBILIDADE AQUI
		
		this.coverages = coverages;
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
			if(coverages[i-1].presentInPolicy == null){
				if(coverages[i-1].mandatory){
					fields[i][0].setValue("1");
				}
			}
			else{
				fields[i][0].setValue(coverages[i-1].presentInPolicy ? "1" : "0");
			}
			fields[i][0].addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					setEnabledRows();
				}
			});
			fields[i][0].setEditable(coverages[i-1].mandatory ? false : true);
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


	protected void setEnabledRows() {

		boolean enable = false;

		for(int i = 1; i<fields.length; i++){
			enable = fields[i][0].getValue() == null ? false : !fields[i][0].getValue().equalsIgnoreCase("0");
			enableExtraFields(i-1, enable);
			for(int j = 2; j<fields[0].length; j++){
				if(fields[i][j] != null){
					fields[i][j].setReadOnly(readOnly || !enable);
					if(!enable){
						fields[i][j].clear();
					}
				}
			}
		}

	}

	public abstract void enableExtraFields(int i, boolean b);

	public void fillTable(FieldContainer.ColumnField[] formFields) {
		int row;
		int column;
		
		if(formFields == null){
			return;
		}

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
			fields[row][column].setEditable(!formFields[i].readOnly);
			fields[row][column].setReadOnly(this.readOnly);
			grid.setWidget(row, column, fields[row][column]);
			grid.getCellFormatter().setHorizontalAlignment(row, column, HasHorizontalAlignment.ALIGN_CENTER);
		}

	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<FieldContainer.ColumnField[]> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public FieldContainer.ColumnField[] getValue() {
		FieldContainer.ColumnField[] result = value;
		for(int i = 0; i<result.length; i++){
			result[i].value = fields[result[i].coverageIndex+1][result[i].columnIndex+2].getValue();
		}
		return result;
	}

	@Override
	public void setValue(FieldContainer.ColumnField[] value) {
		setValue(value, true);
	}

	@Override
	public void setValue(FieldContainer.ColumnField[] value, boolean fireEvents) {
		this.value = value;

		if(value == null){
			clearContent();
			grid.clear();
			grid.setVisible(false);
			return;
		}

		if(fields!=null){
			clearContent();
		}

		fillTable(value);
		setEnabledRows();

		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
		grid.setVisible(true);
	}

	private void clearContent() {
		for(int i = 1; i<fields.length; i++){
			for(int j = 2; j<fields[0].length; j++){
				if(fields[i][j] != null){
					grid.setWidget(i, j, null);
					fields[i][j] = null;
				}
			}
		}
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;

		setFirstColumnReadOnly();
		setEnabledRows();
	}

	private void setFirstColumnReadOnly() {
		for(int i = 1; i<fields.length; i++){	
			fields[i][0].setReadOnly(readOnly);
		}
	}

}
