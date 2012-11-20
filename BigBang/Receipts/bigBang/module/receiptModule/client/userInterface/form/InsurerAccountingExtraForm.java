package bigBang.module.receiptModule.client.userInterface.form;

import java.util.ArrayList;
import java.util.List;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurerAccountingExtra;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;

public class InsurerAccountingExtraForm extends
FormView<InsurerAccountingExtra[]> {

	public class InsurerAccountingExtraFormItemSection extends CollapsibleFormViewSection{

		protected NumericTextBoxFormField value;
		protected TextBoxFormField description;
		protected CheckBoxFormField isCommission, isTax;
		protected ExpandableListBoxFormField insurerId;
		protected Button remove;

		public InsurerAccountingExtraFormItemSection() {
			super("Informação Adicional");

			insurerId = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
			value = new NumericTextBoxFormField("Valor", true);
			value.setUnitsLabel("€");
			description = new TextBoxFormField("Descrição");
			description.setFieldWidth("350px");
			isCommission = new CheckBoxFormField("Comissão");
			isTax = new CheckBoxFormField("Imposto de Selo");

			remove = new Button("Remover");
			
			addFormField(insurerId);
			addFormField(value);
			addFormField(description);
			addFormField(isCommission, true);
			addFormField(isTax, true);
			addLineBreak();
			addWidget(remove, false);

			isCommission.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					isTax.setVisible(event.getValue() != null && event.getValue());
				}
			});
			isCommission.setValue(false, true);
			
		}
		
		
		public HasClickHandlers getRemoveButton(){
			return remove;
		}

		public InsurerAccountingExtra getItem(){
			InsurerAccountingExtra newItem = new InsurerAccountingExtra();

			newItem.text = description.getValue();
			newItem.value = value.getValue();
			newItem.hasTax = isTax.getValue();
			newItem.isCommissions = isCommission.getValue();
			newItem.insurerId = insurerId.getValue();

			return newItem;
		}

	}

	protected List<InsurerAccountingExtraFormItemSection> extras;
	protected Button addExtra;


	public InsurerAccountingExtraForm(){

		final FormViewSection addSection = new FormViewSection("");
		
		addExtra = new Button("Acrescentar Informação Adicional");
		extras = new ArrayList<InsurerAccountingExtraFormItemSection>();
		
		addExtra.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				collapseAll();
				final InsurerAccountingExtraFormItemSection section = new InsurerAccountingExtraFormItemSection();
				section.expand();
				addSection(section);
				addSection.setHeaderText("");
				addSection(addSection);
				section.setReadOnly(false);
				
				section.getRemoveButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						removeSection(section);
						extras.remove(section);
					}
				});
				extras.add(section);
				InsurerAccountingExtraForm.this.validate();
			}
		});
		
		addSection.addWidget(addExtra);
		addSection(addSection);
		addSection.setHeight("100%");
		setValidator(new InsurerAccountingExtraFormValidator(this));
	}


	protected void collapseAll() {
		for(InsurerAccountingExtraFormItemSection section : extras){
			section.collapse();
		}
	}


	@Override
	public InsurerAccountingExtra[] getInfo() {
		InsurerAccountingExtra[] extrasArray = new InsurerAccountingExtra[extras.size()];

		for(int i = 0; i<extrasArray.length; i++){
			extrasArray[i] = extras.get(i).getItem();
		}

		return extrasArray;
	}

	@Override
	public void setInfo(InsurerAccountingExtra[] info) {
		super.setValue(info);
		return;
	}

}
