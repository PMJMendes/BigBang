package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.FieldContainer;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;
import bigBang.module.insurancePolicyModule.client.userInterface.CoverageFieldsGrid;
import bigBang.module.insurancePolicyModule.client.userInterface.ExtraFieldsSection;
import bigBang.module.insurancePolicyModule.client.userInterface.HeaderFieldsSection;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;

public class QuoteRequestSublineFormSection extends CollapsibleFormViewSection implements HasValue<FieldContainer>{

	HeaderFieldsSection headerFieldSection;
	CoverageFieldsGrid table;
	ExtraFieldsSection extraFieldsSection;
	
	Button deleteSubline;
	private FieldContainer value;
	
	public QuoteRequestSublineFormSection(String title) {
		super(title);
		
		
		deleteSubline = new Button();
		addWidget(deleteSubline);
		addWidget(headerFieldSection);
		addWidget(table);
		addWidget(extraFieldsSection);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<FieldContainer> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldContainer getValue() {
		FieldContainer fields = this.value;
		
		fields.columnFields = table.getValue();
		fields.headerFields = headerFieldSection.getValue();
		fields.extraFields = extraFieldsSection.getValue();
		
		return fields;
	}

	@Override
	public void setValue(FieldContainer value) {
		this.value = value;
		headerFieldSection.setValue(value.headerFields);
		table.setValue(value.columnFields);
		extraFieldsSection.setValue(value.extraFields);
	}

	@Override
	public void setValue(FieldContainer value, boolean fireEvents) {
		this.value = value;
		headerFieldSection.setValue(value.headerFields, fireEvents);
		table.setValue(value.columnFields, fireEvents);
		extraFieldsSection.setValue(value.extraFields, fireEvents);
	}

}
