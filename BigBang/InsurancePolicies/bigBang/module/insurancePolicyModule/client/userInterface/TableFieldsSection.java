package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.library.client.userInterface.view.FormViewSection;

public class TableFieldsSection extends FormViewSection implements HasValue<FieldContainer.ColumnField[]>{

	CoverageFieldsGrid table = new CoverageFieldsGrid();
	
	public TableFieldsSection() {
		super("Coberturas");
		SimplePanel panel = new SimplePanel();
		panel.setWidget(table);
		panel.getElement().getStyle().setMarginRight(20, Unit.PX);
		addWidget(panel);
	}
	
	public void setHeaders(StructuredFieldContainer.Coverage[] coverages, StructuredFieldContainer.ColumnHeader[] columns){
		table.setHeaders(coverages, columns);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<FieldContainer.ColumnField[]> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public FieldContainer.ColumnField[] getValue() {
		return table.getValue();
	}

	@Override
	public void setValue(FieldContainer.ColumnField[] value) {
		setValue(value, true);		
	}

	@Override
	public void setValue(FieldContainer.ColumnField[] value, boolean fireEvents) {
		table.setValue(value, fireEvents);
		
		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}
	
}
