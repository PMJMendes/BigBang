package bigBang.module.casualtyModule.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyItem;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class SubCasualtyForm extends FormView<SubCasualty> {

	protected TextBoxFormField number;
	protected TextBoxFormField reference;
	protected TextBoxFormField insurerProcessNumber;
	protected CheckBoxFormField hasJudicial;
	protected TextBoxFormField status;
	protected TextAreaFormField notes, internalNotes;

	protected FormViewSection notesSection, internalNotesSection;

	protected Collection<SubCasualtyItemSection> subCasualtyItemSections;

	public SubCasualtyForm(){
		number = new TextBoxFormField("Número");
		number.setEditable(false);
		reference = new TextBoxFormField("Apólice");
		reference.setEditable(false);
		insurerProcessNumber = new TextBoxFormField("Número de Processo na Seguradora");
		status = new TextBoxFormField("Estado");
		notes = new TextAreaFormField();
		internalNotes = new TextAreaFormField();
		hasJudicial = new CheckBoxFormField("Tem processo Judicial");

		addSection("Informação Geral");
		addFormField(reference, false);

		addFormField(number, true);
		addFormField(insurerProcessNumber, true);
		addFormField(status, true);
		addFormField(hasJudicial);

		this.subCasualtyItemSections = new ArrayList<SubCasualtyItemSection>();

		setValue(new SubCasualty());
	}

	@Override
	public SubCasualty getInfo() {
		SubCasualty result = getValue();

		if(result != null) {
			result.number = number.getValue();
			result.insurerProcessNumber = insurerProcessNumber.getValue();
			result.hasJudicial = hasJudicial.getValue();
			result.text = notes.getValue();
			result.internalNotes = internalNotes.getValue();
			result.items = getSubCasualtyItems();
		}

		return result;
	}

	@Override
	public void setInfo(SubCasualty info) {
		if(info != null) {
			number.setValue(info.number);
			reference.setLabelText(info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? "Apólice" : "Apólice Adesão");
			reference.setValue("#" + info.referenceNumber + " - " + info.categoryName + " / " + info.lineName + " / " + info.subLineName);
			insurerProcessNumber.setValue(info.insurerProcessNumber);
			status.setValue(info.isOpen ? "Aberto" : "Fechado");
			hasJudicial.setValue(info.hasJudicial);
			notes.setValue(info.text);
			internalNotes.setValue(info.internalNotes);

			setSubCasualtyItems(info.items);
		}
	}

	protected void setSubCasualtyItems(SubCasualtyItem[] items) {
		for(FormViewSection section : this.subCasualtyItemSections) {
			removeSection(section);
		}

		if(items != null) {
			for(SubCasualtyItem item : items) {
				addSubCasualtyItemSection(item);
			}
		}
	}

	protected SubCasualtyItem[] getSubCasualtyItems(){
		SubCasualtyItem[] result = new SubCasualtyItem[subCasualtyItemSections.size()];
		
		int i = 0;
		for(SubCasualtyItemSection section : subCasualtyItemSections) {
			result[i] = section.getItem();
		}
		
		return result;
	}

	protected void addSubCasualtyItemSection(SubCasualtyItem item){
		SubCasualtyItemSection section = new SubCasualtyItemSection(item);
		this.subCasualtyItemSections.add(section);
		addSection(section);
	}
	
	protected void addLastFields() {
		addSection(notesSection);
		addSection(internalNotesSection);
	}

}
