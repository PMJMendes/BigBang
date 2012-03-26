package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.QuoteRequest.ColumnHeader;
import bigBang.definitions.shared.QuoteRequest.Coverage;
import bigBang.definitions.shared.QuoteRequest.ExtraField;
import bigBang.definitions.shared.QuoteRequest.HeaderField;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.definitions.shared.QuoteRequest.TableSection;
import bigBang.definitions.shared.QuoteRequest.TableSection.TableField;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.TwoKeyTableView;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class SubLineDataSection extends CollapsibleFormViewSection {

	protected RequestSubLine value;

	protected TwoKeyTableView table;
	protected Map<String, GenericFormField> headerFields, extraFields;

	public SubLineDataSection(RequestSubLine data) {
		super("");
		this.expand();
		table = new TwoKeyTableView();
		headerFields = new HashMap<String, GenericFormField>();
		extraFields = new HashMap<String, GenericFormField>();
		setSubLineData(data);
	}

	public void setSubLineData(RequestSubLine data){
		this.value = data;

		clearHeaderData();
		clearTableData();
		clearExtraData();

		if(this.value != null){
			setHeaderText(data.headerText);
			setHeaderFields(data.headerFields);
			setTableData(data.coverages, data.columns, data.tableData);
			setExtraFields(data.extraData);
		}
	}

	public RequestSubLine getSubLineData(){
		RequestSubLine result = this.value;

		if(result != null){
			result.headerFields = getHeaderFields();
			result.tableData = getTableSections();
			result.extraData = getExtraFields();
		}

		return null;
	}

	public void setHeaderFields(HeaderField[] headerFields){

		for(HeaderField headerField : headerFields) {
			GenericFormField formField = null;

			switch(headerField.type) {
			case TEXT:
				formField = new GenericFormField(TYPE.TEXT);
				break;
			case LIST:
				formField = new GenericFormField(TYPE.LIST);
				formField.setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+headerField.fieldId);
				break;
			case REFERENCE:
				formField = new GenericFormField(TYPE.REFERENCE);
				formField.setListId(headerField.refersToId);
				break;
			case NUMERIC:
				formField = new GenericFormField(TYPE.NUMBER);
				break;
			case BOOLEAN:
				formField = new GenericFormField(TYPE.BOOLEAN);
				break;
			case DATE:
				formField = new GenericFormField(TYPE.DATE);
				break;
			}

			formField.setFieldWidth("175px");
			formField.setUnitsLabel(headerField.unitsLabel);
			formField.setValue(headerField.value);
			formField.setLabel(headerField.fieldName);
			addFormField(formField, true);
		}
	}

	public HeaderField[] getHeaderFields(){
		for(HeaderField headerField : this.value.headerFields) {
			String fieldId = headerField.fieldId;
			GenericFormField formField = this.headerFields.get(fieldId);
			headerField.value = formField.getValue();
		}
		return this.value.headerFields;
	}

	public void clearHeaderData(){
		for(GenericFormField field : headerFields.values()) {
			unregisterFormField(field);
		}
		this.headerFields.clear();
	}

	public void setExtraFields(ExtraField[] extraFields){

		for(ExtraField extraField : extraFields) {
			GenericFormField formField = null;

			switch(extraField.type) {
			case TEXT:
				formField = new GenericFormField(TYPE.TEXT);
				break;
			case LIST:
				formField = new GenericFormField(TYPE.LIST);
				formField.setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+extraField.fieldId);
				break;
			case REFERENCE:
				formField = new GenericFormField(TYPE.REFERENCE);
				formField.setListId(extraField.refersToId);
				break;
			case NUMERIC:
				formField = new GenericFormField(TYPE.NUMBER);
				break;
			case BOOLEAN:
				formField = new GenericFormField(TYPE.BOOLEAN);
				break;
			case DATE:
				formField = new GenericFormField(TYPE.DATE);
				break;
			}

			formField.setFieldWidth("175px");
			formField.setUnitsLabel(extraField.unitsLabel);
			formField.setValue(extraField.value);
			formField.setLabel(extraField.fieldName);
			addFormField(formField, true);
		}
	}

	public ExtraField[] getExtraFields(){
		for(ExtraField extraField : this.value.extraData) {
			String fieldId = extraField.fieldId;
			GenericFormField formField = this.headerFields.get(fieldId);
			extraField.value = formField.getValue();
		}
		return this.value.extraData;
	}

	public void clearExtraData(){
		for(GenericFormField field : extraFields.values()) {
			unregisterFormField(field);
		}
		this.extraFields.clear();
	}

	public void setTableData(Coverage[] coverages, ColumnHeader[] headers, TableSection[] tableSections){
		this.table = new TwoKeyTableView();
		
		HeaderCell[] headerCells = new HeaderCell[headers.length];
		for(int i = 0; i < headers.length; i++) {
			headerCells[i].id = i+"";
			String unit = headers[i].unitsLabel;
			headerCells[i].text = headers[i].label + (unit == null ? "" : " ("+unit+")");
		}
		
		HeaderCell[] rowCells = new HeaderCell[coverages.length];
		for(int i = 0; i < coverages.length; i++) {
			rowCells[i].id = coverages[i].coverageId;
		}
		
		this.table.setHeaders(rowCells, headerCells);
		
		for(TableSection tableSection : tableSections) {
			for(TableField tableField : tableSection.data) {
				Field realTableField = new Field();
				realTableField.id = tableField.fieldId;
//				realTableField.type = 0
				
				table.setValue(tableField.coverageId, tableField.columnIndex+"", realTableField);
			}
		}
	}

	public TableSection[] getTableSections(){
		return null; //TODO
	}

	public void clearTableData(){
		if(this.table != null) {
			this.table.removeFromParent();
		}
		this.table = null;
	}

	public ColumnHeader[] getColumnHeaders() {
		return null;
	}

	public void clearColumnHeaders(){
		//TODO
	}

	public Coverage[] getCoverages(){
		//TODO
		return null;
	}

	public void clearCoverages(){
		//TODO
	}

}
