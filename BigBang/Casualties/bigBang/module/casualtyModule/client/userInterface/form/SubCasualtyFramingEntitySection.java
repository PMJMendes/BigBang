package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyFraming.SubCasualtyFramingEntity;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

public class SubCasualtyFramingEntitySection extends CollapsibleFormViewSection {
	
	protected SubCasualty.SubCasualtyFraming.SubCasualtyFramingEntity currentEntity;
	
	protected ExpandableListBoxFormField entityType;
	protected ExpandableListBoxFormField evaluation;
	protected TextAreaFormField notes;
	
	protected Button removeButton;
	
	public SubCasualtyFramingEntitySection(SubCasualtyFramingEntity framingEntity) {
		
		super("");

		removeButton = new Button("Remover");
		
		entityType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.FRAMING_ENTITY_TYPE, "Oficina/Outra Entidade");
		evaluation = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.EVALUATION_TIPIFICATION, "Pontuação");
		evaluation.allowEdition(false);
		notes = new TextAreaFormField("Observações");
		
		addFormField(entityType, true);
		addFormField(evaluation, true);
		addFormField(notes, true);
		notes.setFieldWidth("650px");
		notes.setFieldHeight("50px");
		
		SimplePanel buttonWrapper = new SimplePanel();
		buttonWrapper.add(removeButton);
		buttonWrapper.setWidth("100%");

		addWidget(buttonWrapper, false);

		setFramingEntity(framingEntity);
	}
	
	public void setFramingEntity(final SubCasualtyFramingEntity framingEntity) {
		this.currentEntity = framingEntity;

		if (framingEntity != null) {
			this.headerLabel.setText("Enquadramento - Outros Intervenientes");

			entityType.setValue(framingEntity.entityTypeId);
			evaluation.setValue(framingEntity.evaluationId);
			notes.setValue(framingEntity.evaluationNotes);
		}
	}
	
	public SubCasualtyFramingEntity getFramingEntity() {
		SubCasualtyFramingEntity result = this.currentEntity;

		if (result != null) {

			result.entityTypeId = entityType.getValue();
			result.evaluationId = evaluation.getValue();
			result.evaluationNotes = notes.getValue();
		}

		return result;
	}
	
	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}

	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		removeButton.setVisible(!readOnly);
	}

}
