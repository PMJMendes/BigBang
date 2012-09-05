package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.TwoKeyTableView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;

public abstract class PolicyFormTable extends TwoKeyTableView {

	protected Coverage[] coverages;
	protected Map<HasValue<String>, String> coverageRadioButtons;

	public PolicyFormTable(){
		super();
		coverageRadioButtons = new HashMap<HasValue<String>, String>();
	}

	public void setHeaders(Coverage[] coverages, ColumnHeader[] headers){
		this.coverages = coverages;

		HeaderCell[] headerCells = new HeaderCell[headers.length];
		for(int i = 0; i < headers.length; i++) {
			headerCells[i] = new HeaderCell();
			headerCells[i].id = i+"";
			String unit = headers[i].unitsLabel;
			headerCells[i].text = headers[i].label + (unit == null ? "" : " ("+unit+")");
		}

		HeaderCell[] rowCells = new HeaderCell[coverages.length];
		for(int i = 0; i < coverages.length; i++) {
			rowCells[i] = new HeaderCell();
			rowCells[i].id = coverages[i].coverageId;
			rowCells[i].text = coverages[i].coverageName;
		}

		this.table.setHeaders(rowCells, headerCells);
	}

	public void render(){
		String[] rowHeaders = this.table.getRowHeaders();
		String[] columnHeaders = this.table.getColumnHeaders();
		this.grid.resize(rowHeaders.length + 1, columnHeaders.length + 2);

		ValueChangeHandler<String> handler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				coverageRadioButtonChanged(event.getSource(), event.getValue());
			}
		};

		Coverage[] newCoverages = getCoverages();
		if(newCoverages != null) {
			this.coverages = newCoverages;
		}
		this.coverageRadioButtons.clear();

//		this.table.clearValues();
		
		for(int i = 0; i < rowHeaders.length + 1; i++){
			for(int j = 0; j < columnHeaders.length + 2; j++) {
				if(i == 0){
					if(j > 1){
						grid.setText(i, j, table.getColumnText(columnHeaders[j-2]));
					}
					grid.getCellFormatter().setHorizontalAlignment(i, j, HasHorizontalAlignment.ALIGN_CENTER);
				}
				if(i > 0 && j == 0) {
					RadioButtonFormField radioButton = new RadioButtonFormField();
					radioButton.addOption("1", "Sim");
					radioButton.addOption("0", "Não");
					String present = coverages[i-1].presentInPolicy == null ? null : (coverages[i-1].presentInPolicy ? "1" : "0");
					radioButton.setValue(present);
					radioButton.addValueChangeHandler(handler);
					radioButton.setReadOnly(coverages[i-1].mandatory);
					coverageRadioButtons.put(radioButton, coverages[i-1].coverageId);
					grid.setWidget(i, j, radioButton);
				}
				if(i == 0 && j == 0) {
					grid.setText(i, j, "Incluir");
				}
				if(i == 0 && j == 1) {
					grid.setText(i, j, "Cobertura");
				}
				if(i > 0 && j == 1) {
					grid.setText(i, j, table.getRowText(rowHeaders[i-1]));
					grid.getCellFormatter().setHorizontalAlignment(i, j, HasHorizontalAlignment.ALIGN_CENTER);
				}
				if(i > 0 && j > 1){
					FormField<String> formField = getFormField(table.getValue(rowHeaders[i-1], columnHeaders[j-2]));
					formField.setFieldWidth("100px");
					grid.setWidget(i, j, formField);
					grid.getCellFormatter().setHorizontalAlignment(i, j, HasHorizontalAlignment.ALIGN_LEFT);
					Field field = table.getValue(rowHeaders[i-1], columnHeaders[j-2]);
					formField.setValue(field == null ? null : field.value);
					putField(rowHeaders[i-1], columnHeaders[j-2], formField);
				}
				if((i % 2) != 0){
					grid.getRowFormatter().getElement(i).getStyle().setBackgroundColor("#CCC");
				}
			}
		}
		for(HasValue<String> radio : coverageRadioButtons.keySet()) {
			radio.setValue(radio.getValue(), true);
		}
		this.setReadOnly(this.readOnly);
		this.grid.getColumnFormatter().setWidth(0, "150px");
	}

	protected void coverageRadioButtonChanged(Object object, String value){
		String coverageId = this.coverageRadioButtons.get(object);
		boolean active = value != null && value.equals("1");

		if(active) {
			enableCoverage(coverageId);
		}else{
			disableCoverage(coverageId);
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		for(HasValue<String> field : this.coverageRadioButtons.keySet()) {
		((FormField<?>)field).setReadOnly(isMandatoryCoverage(this.coverageRadioButtons.get(field)) || readOnly);
			if(!readOnly){
				field.setValue(field.getValue(), true);
			}
		}
	}

	protected boolean isMandatoryCoverage(String coverageId){
		for(int i = 0; i < coverages.length; i++) {
			if(coverages[i].coverageId.equalsIgnoreCase(coverageId)){
				return coverages[i].mandatory;
			}
		}
		return false;
	}
	
	protected void enableCoverage(String coverageId){
		if(this.fields.get(coverageId) != null){
			Collection<FormField<String>> fields = this.fields.get(coverageId).values();
			for(FormField<?> field : fields) {
				if(!readOnly){
					field.setReadOnly(false);
				}
			}
		}
		onCoverageEnabled(coverageId);
	}

	protected void disableCoverage(String coverageId) {
		if(this.fields.get(coverageId) != null){
			Collection<FormField<String>> fields = this.fields.get(coverageId).values();
			for(FormField<?> field : fields) {
				if(field != null){
					field.setReadOnly(true);
				}
			}
		}
		onCoverageDisabled(coverageId);
	}

	public Coverage[] getCoverages(){
		for(HasValue<?> radio : this.coverageRadioButtons.keySet()) {
			String coverageId = coverageRadioButtons.get(radio);

			for(Coverage coverage : this.coverages) {
				if(coverageId.equalsIgnoreCase(coverage.coverageId)) {
					coverage.presentInPolicy = radio.getValue() == null ? null : radio.getValue().equals("1") ? true : false;
					break;
				}
			}
		}
		return this.coverages;
	}
	
	public abstract void onCoverageEnabled(String coverageId);
	
	public abstract void onCoverageDisabled(String coverageId);

	public void putField(int coverageIndex, int columnIndex,
			Field realTableField) {
		String coverageId = null;
		for(int i = 0; i<coverages.length; i++){
			if(coverageIndex == i){
				coverageId = coverages[i].coverageId;
				break;
			}
		}
		table.setValue(coverageId, columnIndex+"", realTableField);
	}

	

}
