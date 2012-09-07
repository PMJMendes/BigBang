package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.library.client.userInterface.view.FormViewSection;

public class TableFieldsSection extends FormViewSection implements HasValue<ColumnField[]>{

	CoverageFieldsGrid table = new CoverageFieldsGrid();
	
	public TableFieldsSection() {
		super("Coberturas");
		addWidget(table);

	}
	
	public void setHeaders(Coverage[] coverages, ColumnHeader[] columns){
		table.setHeaders(coverages, columns);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ColumnField[]> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public ColumnField[] getValue() {
		return table.getValue();
	}

	@Override
	public void setValue(ColumnField[] value) {
		setValue(value, true);		
	}

	@Override
	public void setValue(ColumnField[] value, boolean fireEvents) {
		table.setValue(value, fireEvents);
		
		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}
	
}
