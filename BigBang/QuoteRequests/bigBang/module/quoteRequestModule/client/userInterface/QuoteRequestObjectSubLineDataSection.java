package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.QuoteRequestObject.ColumnHeader;
import bigBang.definitions.shared.QuoteRequestObject.CoverageData;
import bigBang.definitions.shared.QuoteRequestObject.HeaderData;
import bigBang.definitions.shared.QuoteRequestObject.HeaderData.FixedField;
import bigBang.definitions.shared.QuoteRequestObject.SubLineData;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.TwoKeyTable.Type;
import bigBang.library.client.userInterface.TwoKeyTableView;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class QuoteRequestObjectSubLineDataSection extends CollapsibleFormViewSection {

	protected SubLineData value;
	protected String ownerId;

	protected TwoKeyTableView table;
	protected Map<String, GenericFormField> headerFields, extraFields;

	public QuoteRequestObjectSubLineDataSection(String ownerId, SubLineData data, boolean collapsed) {
		super("", collapsed);
		table = new TwoKeyTableView();
		headerFields = new HashMap<String, GenericFormField>();
		extraFields = new HashMap<String, GenericFormField>();
		setSubLineData(data);
		setOwnerId(ownerId);
	}

	public void setSubLineData(SubLineData data){
		this.value = data;

		clearHeaderData();
		clearTableData();
		clearExtraData();

		if(this.value != null){
			setHeaderText(data.headerText);
			setHeaderFields(data.headerData);
			if(data.coverageData.length > 0 && data.columnHeaders.length > 0){
				this.table = new TwoKeyTableView();
				this.addWidget(this.table);
				setTableData(data.columnHeaders, data.coverageData);
			}
			setExtraFields(data.coverageData);
		}
	}

	public SubLineData getSubLineData(){
		SubLineData result = this.value;

		if(result != null){
			result.headerData.fixedFields = getHeaderFields();
			if(this.table != null) {
				result.coverageData = getTableData();
			}
			result.coverageData = getExtraFields();
		}

		return result;
	}

	public void setHeaderFields(HeaderData headerData){
		this.headerFields.clear();

		FixedField[] headerFields = headerData.fixedFields;

		for(int i = 0; i < headerFields.length; i++) {
			FixedField headerField = headerFields[i];

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
			formField.setReadOnly(this.readOnly);
			this.headerFields.put(headerField.fieldId, formField);
			addFormField(formField, i < (headerFields.length - 1));
		}
	}

	public FixedField[] getHeaderFields(){
		FixedField[] headerFields = this.value.headerData.fixedFields;

		if(headerFields != null) {
			for(FixedField headerField : headerFields) {
				String fieldId = headerField.fieldId;
				GenericFormField formField = this.headerFields.get(fieldId);
				headerField.value = formField == null ? null : formField.getValue();
			}
		}

		return headerFields;
	}

	public void clearHeaderData(){
		for(GenericFormField field : headerFields.values()) {
			unregisterFormField(field);
		}
		this.headerFields.clear();
	}

	public void setExtraFields(CoverageData[] coverageData){
		this.extraFields.clear();

		for(CoverageData coverage : coverageData){
			FixedField[] extraFields = coverage.fixedFields;

			for(FixedField extraField : extraFields) {
				if(extraField.columnIndex == -1){
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
					formField.setReadOnly(readOnly);
					this.extraFields.put(extraField.fieldId, formField);
					addFormField(formField, true);
				}
			}
		}
	}

	public CoverageData[] getExtraFields(){
		CoverageData[] result = this.value.coverageData;

		for(CoverageData coverageData : result){
			for(FixedField fixedField : coverageData.fixedFields){
				GenericFormField formField = this.extraFields.get(fixedField.fieldId);
				if(fixedField.columnIndex == -1){
					fixedField.value = formField.getValue();
				}
			}
		}

		return result;
	}

	public void clearExtraData(){
		for(GenericFormField field : extraFields.values()) {
			unregisterFormField(field);
		}
		this.extraFields.clear();
	}

	public void setTableData(ColumnHeader[] columnHeaders, CoverageData[] coverageData){
		HeaderCell[] coverageHeaders = new HeaderCell[coverageData.length];

		for(int i = 0; i < coverageData.length; i++) {
			HeaderCell coverageHeaderCell = new HeaderCell();
			coverageHeaderCell.id = coverageData[i].coverageId;
			coverageHeaderCell.text = coverageData[i].coverageLabel;
			coverageHeaders[i] = coverageHeaderCell;
		}

		HeaderCell[] fieldHeaders = new HeaderCell[columnHeaders.length];
		for(int i = 0; i < columnHeaders.length; i++) {
			HeaderCell columnCell = new HeaderCell();
			columnCell.id = columnHeaders[i].index+"";
			columnCell.text =  columnHeaders[i].label + " (" + columnHeaders[i].unitsLabel + ")";
			fieldHeaders[i] = columnCell;
		}

		this.table.setHeaders(coverageHeaders, fieldHeaders);

		for(CoverageData coverage : coverageData) {
			for(FixedField fixedField : coverage.fixedFields) {
				if(fixedField.columnIndex != -1){
					Field realTableField = new Field();
					realTableField.id = fixedField.fieldId;
					realTableField.value = fixedField.value;

					for(ColumnHeader columnHeader : columnHeaders) {
						if(columnHeader.index == fixedField.columnIndex){
							switch(columnHeader.type) {
							case TEXT:
								realTableField.type = Type.TEXT;
								break;
							case BOOLEAN:
								realTableField.type = Type.BOOLEAN;
								break;
							case DATE:
								realTableField.type = Type.DATE;
								break;
							case LIST:
								realTableField.type = Type.LIST;
								break;
							case NUMERIC:
								realTableField.type = Type.NUMERIC;
								break;
							case REFERENCE:
								realTableField.type = Type.REFERENCE;
								break;
							}

							table.setValue(coverage.coverageId, fixedField.columnIndex+"", realTableField);
							break;
						}
					}
				}
			}
		}

		this.table.setReadOnly(this.readOnly);
		this.table.render();
	}

	public CoverageData[] getTableData(){
		CoverageData[] result = this.value == null ? null : this.value.coverageData;

		if(result != null && this.table != null) {
			Field[] tableFields = this.table.getAllValues();

			for(Field tableField : tableFields) {
				for(CoverageData coverageData : result){
					for(FixedField fixedField : coverageData.fixedFields) {
						if(tableField != null && fixedField.fieldId.equalsIgnoreCase(tableField.id)) {
							fixedField.value = tableField.value;
							break;
						}
					}
				}
			}
		}

		return result;
	}

	public void clearTableData(){
		if(this.table != null) {
			this.table.removeFromParent();
		}
		this.table = null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		if(this.headerFields != null) {
			for(FormField<?> formField : this.headerFields.values()) {
				formField.setReadOnly(readOnly);
			}
		}

		if(this.table != null) {
			this.table.setReadOnly(readOnly);
		}

		if(this.extraFields != null) {
			for(FormField<?> formField : this.extraFields.values()) {
				formField.setReadOnly(readOnly);
			}
		}
	}

	public void setOwnerId(String id) {
		this.ownerId = id;
	}

}
