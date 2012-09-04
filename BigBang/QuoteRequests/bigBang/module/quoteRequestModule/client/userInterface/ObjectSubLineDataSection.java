package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.QuoteRequest.ColumnHeader;
import bigBang.definitions.shared.QuoteRequest.Coverage;
import bigBang.definitions.shared.QuoteRequest.ExtraField;
import bigBang.definitions.shared.QuoteRequest.HeaderField;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.definitions.shared.QuoteRequest.TableSection;
import bigBang.definitions.shared.QuoteRequest.TableSection.TableField;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.GenericFormField;
import bigBang.library.client.userInterface.GenericFormField.TYPE;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.Type;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;
import bigBang.module.quoteRequestModule.client.dataAccess.QuoteRequestTypifiedListBroker;

public class ObjectSubLineDataSection extends CollapsibleFormViewSection {

	protected RequestSubLine value;
	protected String requestId;
	protected TableSection currentTableSection;

	protected Button removeButton;
	protected SubLineSectionTable table;
	protected Map<String, GenericFormField> headerFields, extraFields;
	protected ExpandableListBoxFormField insuredObjectsList;

	public ObjectSubLineDataSection(String quoteRequestId, RequestSubLine data) {
		super("");
		this.expand();
		insuredObjectsList = new ExpandableListBoxFormField("Unidade de Risco");
		insuredObjectsList.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onInsuredObjectFilterChanged(event.getValue());
			}
		});
		table = new SubLineSectionTable();
		headerFields = new HashMap<String, GenericFormField>();
		extraFields = new HashMap<String, GenericFormField>();
		removeButton = new Button("Remover Modalidade");
		removeButton.getElement().getStyle().setMarginTop(20, Unit.PX);
		setSubLineData(data);
		setQuoteRequestId(quoteRequestId);
	}

	public void setSubLineData(RequestSubLine data){
		this.value = data;

		clearHeaderData();
		clearTableData();
		clearExtraData();

		if(this.value != null){
			setHeaderText(data.headerText);
			setHeaderFields(data.headerFields);
			addFormField(this.insuredObjectsList);
			TableSection section = data.tableData.length > 0 ? data.tableData[0] : null;
			if(section != null){
				setTableData(data.coverages, data.columns, section);
			}else{
				clearTableData();
			}
			this.insuredObjectsList.setVisible(section != null);
			setExtraFields(data.extraData);
		}

		addWidget(removeButton);
	}

	public RequestSubLine getSubLineData(){
		RequestSubLine result = this.value;

		if(result != null){
			onInsuredObjectFilterChanged(insuredObjectsList.getValue());
			result.headerFields = getHeaderFields();
			if(this.table != null) {
				result.tableData = getTableSections();
			}
			result.extraData = getExtraFields();
			result.coverages = getCoverages();
		}

		return result;
	}

	public void setHeaderFields(HeaderField[] headerFields){
		this.headerFields.clear();
		for(int i = 0; i < headerFields.length; i++) {
			HeaderField headerField = headerFields[i];

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

	public HeaderField[] getHeaderFields(){
		for(HeaderField headerField : this.value.headerFields) {
			String fieldId = headerField.fieldId;
			GenericFormField formField = this.headerFields.get(fieldId);
			headerField.value = formField == null ? null : formField.getValue();
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
		this.extraFields.clear();
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
			formField.setReadOnly(readOnly);
			this.extraFields.put(extraField.fieldId, formField);
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

	public void setTableData(Coverage[] coverages, ColumnHeader[] headers, TableSection tableSection){
		this.table = new SubLineSectionTable();

		Coverage[] coverageHeaders = getCoverages() == null ? coverages : getCoverages();

		this.table.setHeaders(coverageHeaders, headers);
		setTableData(headers, tableSection);

		addWidget(this.table);
	}

	public  void setTableData(ColumnHeader[] headers, TableSection tableSection){
		for(TableField tableField : tableSection.data) {
			Field realTableField = new Field();
			realTableField.id = tableField.fieldId;
			realTableField.value = tableField.value;

			switch(headers[tableField.columnIndex].type) {
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

			table.setValue(tableField.coverageId, tableField.columnIndex+"", realTableField);
		}
		this.currentTableSection = tableSection;
		this.table.setReadOnly(this.readOnly);
		this.table.render();
	}

	public TableSection getCurrentTableSection() {
		TableSection result = this.currentTableSection;

		for(TableField tableField : this.currentTableSection.data) {
			Field realTableField = table.getValue(tableField.coverageId, tableField.columnIndex+"");
			tableField.value = realTableField.value;
		}

		return result;
	}

	public TableSection[] getTableSections(){
		//TODO return null;
		return null;
	}

	public void clearTableData(){
		if(this.table != null) {
			this.table.removeFromParent();
		}
		this.table = null;
	}

	public Coverage[] getCoverages(){
		return this.table != null ? this.table.getCoverages() : null;
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

		this.insuredObjectsList.setReadOnly(false);

		this.removeButton.setVisible(!readOnly);
	}

	public HasClickHandlers getRemoveButton() {
		return this.removeButton;
	}

	public void setQuoteRequestId(String id) {
		this.requestId = id;
		QuoteRequestBroker broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);

		if(broker.isTemp(id)) {
			QuoteRequestTypifiedListBroker quoteRequestTypifiedListBroker = QuoteRequestTypifiedListBroker.Util.getInstance();
			this.insuredObjectsList.setTypifiedDataBroker((TypifiedListBroker) quoteRequestTypifiedListBroker);
			this.insuredObjectsList.setListId(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT + "/" + broker.getEffectiveId(id), new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}else{
			this.insuredObjectsList.setTypifiedDataBroker(BigBangTypifiedListBroker.Util.getInstance());
			this.insuredObjectsList.setListId(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT + "/" + id, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}

	protected void onInsuredObjectFilterChanged(String newValue) {
		if(this.table != null) {
			newValue = newValue == null ? null : newValue.isEmpty() ? null : newValue;
			QuoteRequestBroker broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);

			if(broker.isTemp(requestId)) {
				broker.saveCoverageDetailsPage(requestId, value.qrslId, newValue, getCurrentTableSection(), new ResponseHandler<TableSection>() {

					@Override
					public void onResponse(TableSection response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}

			broker.openCoverageDetailsPage(this.requestId, this.value.qrslId, newValue, new ResponseHandler<TableSection>() {

				@Override
				public void onResponse(TableSection response) {
					table.clearValues();
					setTableData(value.columns, response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}

			});
		}
	}

}
