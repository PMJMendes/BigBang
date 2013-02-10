package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;
import bigBang.module.insurancePolicyModule.client.userInterface.CoverageFieldsGrid;
import bigBang.module.insurancePolicyModule.client.userInterface.ExtraFieldsSection;
import bigBang.module.insurancePolicyModule.client.userInterface.HeaderFieldsSection;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;

public class QuoteRequestSublineFormSection extends CollapsibleFormViewSection implements HasValue<CompositeFieldContainer.SubLineFieldContainer>{

	HeaderFieldsSection headerFieldSection;
	CoverageFieldsGrid table;
	ExtraFieldsSection extraFieldsSection;
	Button deleteSubline;
	private CompositeFieldContainer.SubLineFieldContainer value;

	public QuoteRequestSublineFormSection(String title) {
		super(title);

		deleteSubline = new Button("Apagar Modalidade");

		headerFieldSection = new HeaderFieldsSection();
		table = new CoverageFieldsGrid() {

			@Override
			public void enableExtraFields(int i, boolean b) {
				extraFieldsSection.enableFields(i, b);
			}
		};
		table.setSize("100%", "100%");

		extraFieldsSection = new ExtraFieldsSection();
		extraFieldsSection.setSize("100%", "100%");
		addWidget(deleteSubline);
		addWidget(headerFieldSection);
		headerFieldSection.setSize("100%", "100%");
		addWidget(table);
		addWidget(extraFieldsSection);
		
		this.disclosurePanel.setSize("100%", "100%");
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<CompositeFieldContainer.SubLineFieldContainer> handler) {
		return null;
	}

	@Override
	public CompositeFieldContainer.SubLineFieldContainer getValue() {
		CompositeFieldContainer.SubLineFieldContainer fields = this.value;

		fields.columnFields = table.getValue();
		fields.headerFields = headerFieldSection.getValue();
		fields.extraFields = extraFieldsSection.getValue();

		return fields;
	}

	@Override
	public void setValue(CompositeFieldContainer.SubLineFieldContainer value) {
		this.value = value;

		table.setHeaders(value.coverages, value.columns);
		extraFieldsSection.setCoveragesExtraFields(value.coverages);

		headerFieldSection.setValue(value.headerFields);
		table.setValue(value.columnFields);
		extraFieldsSection.setValue(value.extraFields);


	}

	public void setData(FieldContainer data) {
		headerFieldSection.setValue(data.headerFields);
		table.setValue(data.columnFields);
		extraFieldsSection.setValue(data.extraFields);
	}

	@Override
	public void setValue(CompositeFieldContainer.SubLineFieldContainer value, boolean fireEvents) {
		this.value = value;
		headerFieldSection.setValue(value.headerFields, fireEvents);
		table.setValue(value.columnFields, fireEvents);
		extraFieldsSection.setValue(value.extraFields, fireEvents);
	}

	public HasClickHandlers getDeleteButton(){
		return deleteSubline;
	}

}
