package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyFraming;
import bigBang.definitions.shared.SubCasualty.SubCasualtyFraming.SubCasualtyFramingEntity;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.casualtyModule.client.userInterface.NewSubCasualtyFramingEntitySection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class SubCasualtyFramingSection extends FormViewSection {

	protected SubCasualty.SubCasualtyFraming currentFraming;
	
	private boolean isReadOnly;
	
	protected DatePickerFormField analysisDate;
	protected RadioButtonFormField difficultFraming;
	protected RadioButtonFormField validPolicy;
	protected TextAreaFormField validityNotes;
	protected RadioButtonFormField generalExclusions;
	protected TextAreaFormField generalExclusionsNotes;
	protected RadioButtonFormField relevantCoverage;
	protected TextAreaFormField coverageRelevancyNotes;
	protected NumericTextBoxFormField coverageValue;
	protected RadioButtonFormField coverageExclusions;
	protected TextAreaFormField coverageExclusionsNotes;
	protected NumericTextBoxFormField franchise;
	protected ListBoxFormField deductibleType;
	protected TextAreaFormField franchiseNotes;
	protected Label evaluationLabel;
	protected ListBoxFormField insurerEvaluation;
	protected TextAreaFormField insurerEvaluationNotes;
	protected ListBoxFormField expertEvaluation;
	protected TextAreaFormField expertEvaluationNotes;
	
	protected Collection<SubCasualtyFramingEntitySection> aditionalEntitiesSection;
	protected NewSubCasualtyFramingEntitySection newEntitySection;
	
	public SubCasualtyFramingSection(String title) {
		
		super(title);
		
		analysisDate = new DatePickerFormField(
				"Análise à Data");
		
		difficultFraming = new RadioButtonFormField(false);
		difficultFraming.setLabelText("Dificuldade no Enquadramento?");
		difficultFraming.addOption("true", "Sim");
		difficultFraming.addOption("false", "Não");
		
		validPolicy = new RadioButtonFormField(false);
		validPolicy.setLabelText("Apólice Válida?");
		validPolicy.addOption("true", "Sim");
		validPolicy.addOption("false", "Não");
		
		validityNotes = new TextAreaFormField("Se não, porquê?");
		
		generalExclusions = new RadioButtonFormField(false);
		generalExclusions.setLabelText("Exclusões Gerais Aplicáveis?");
		generalExclusions.addOption("true", "Sim");
		generalExclusions.addOption("false", "Não");
		
		generalExclusionsNotes = new TextAreaFormField("Se sim, quais?");
		
		relevantCoverage = new RadioButtonFormField(false);
		relevantCoverage.setLabelText("Cobertura Aplicável?");
		relevantCoverage.addOption("true", "Sim");
		relevantCoverage.addOption("false", "Não");
		
		coverageRelevancyNotes = new TextAreaFormField("Se não, porquê?");
		
		coverageValue = new NumericTextBoxFormField("Capital de Cobertura", true);
		
		coverageExclusions = new RadioButtonFormField(false);
		coverageExclusions.setLabelText("Exclusões de Cobertura Aplicáveis?");
		coverageExclusions.addOption("true", "Sim");
		coverageExclusions.addOption("false", "Não");
		
		coverageExclusionsNotes = new TextAreaFormField("Se sim, quais?");
		
		franchise = new NumericTextBoxFormField("Capital da Franquia", false);
		
		deductibleType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DEDUCTIBLE_TYPE, "Tipo de Franquia");
		
		franchiseNotes = new TextAreaFormField("Observações Franquia.");
		
		insurerEvaluation = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.EVALUATION_TIPIFICATION, "Avaliação Segurador");
		insurerEvaluationNotes = new TextAreaFormField("Observações.");
		
		expertEvaluation = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.EVALUATION_TIPIFICATION, "Avaliação Perito");
		expertEvaluationNotes = new TextAreaFormField("Observações.");
		
		addFormField(analysisDate, false);
		
		addFormField(difficultFraming, false);
		
		addFormField(validPolicy, true);		
		addFormField(validityNotes, true);
		
		addLineBreak();
		
		addFormField(generalExclusions, true);		
		addFormField(generalExclusionsNotes, true);
		
		addLineBreak();
		
		addFormField(relevantCoverage, true);		
		addFormField(coverageRelevancyNotes, true);
		
		addLineBreak();
		
		addFormField(coverageValue, true);
		
		addLineBreak();
		
		addFormField(coverageExclusions, true);		
		addFormField(coverageExclusionsNotes, true);
		
		addLineBreak();
		
		addFormField(franchise, true);
		addFormField(deductibleType, true);
		addFormField(franchiseNotes, true);
		
		addLineBreak();
		
		addFormField(insurerEvaluation, true);
		addFormField(insurerEvaluationNotes, true);
		
		addLineBreak();
		
		addFormField(expertEvaluation, true);
		addFormField(expertEvaluationNotes, true);
		
		addLineBreak();
		
		this.aditionalEntitiesSection = new ArrayList<SubCasualtyFramingEntitySection>();

		this.newEntitySection = new NewSubCasualtyFramingEntitySection();
		this.newEntitySection.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addSubCasualtyFramingEntitySection(new SubCasualtyFramingEntity());
			}
		});
	}
	
	public void setFraming(final SubCasualtyFraming framing) {
		this.currentFraming = framing;

		if (framing != null) {
			analysisDate.setValue(framing.analysisDate);
			difficultFraming.setValue(framing.framingDifficulty?"true":"false");
			validPolicy.setValue(framing.validPolicy?"true":"false");
			validityNotes.setValue(framing.validityNotes);
			generalExclusions.setValue(framing.generalExclusions?"true":"false");
			generalExclusionsNotes.setValue(framing.generalExclusionNotes);
			relevantCoverage.setValue(framing.relevantCoverages?"true":"false");
			coverageRelevancyNotes.setValue(framing.coverageRelevancyNotes);
			coverageValue.setValue(framing.coverageValue);
			coverageExclusions.setValue(framing.coverageExclusions?"true":"false");
			coverageExclusionsNotes.setValue(framing.coverageExclusionsNotes);
			franchise.setValue(framing.franchise);
			deductibleType.setValue(framing.deductibleTypeId);
			franchiseNotes.setValue(framing.franchiseNotes);
			insurerEvaluation.setValue(framing.insurerEvaluationId);
			insurerEvaluationNotes.setValue(framing.insurerEvaluationNotes);
			expertEvaluation.setValue(framing.expertEvaluationId);
			expertEvaluationNotes.setValue(framing.expertEvaluationNotes);
			setSubCasualtyFramingEntities(framing.framingEntities);
		}
	}
	
	public SubCasualtyFraming getFraming() {
		
		SubCasualtyFraming result = this.currentFraming;

		if (result != null) {

			result.analysisDate = analysisDate.getStringValue();
			result.framingDifficulty = difficultFraming.getValue() == null ? null : difficultFraming.getValue().equals("true") ? true : false; 
			result.validPolicy = validPolicy.getValue() == null ? null : difficultFraming.getValue().equals("true") ? true : false; ;
			result.validityNotes = validityNotes.getValue();
			result.generalExclusions = generalExclusions.getValue() == null ? null : difficultFraming.getValue().equals("true") ? true : false; 
			result.generalExclusionNotes = generalExclusionsNotes.getValue();
			result.relevantCoverages = relevantCoverage.getValue() == null ? null : difficultFraming.getValue().equals("true") ? true : false; ;
			result.coverageRelevancyNotes = coverageRelevancyNotes.getValue(); 
			result.coverageValue = coverageValue.getValue();
			result.coverageExclusions = coverageExclusions.getValue() == null ? null : difficultFraming.getValue().equals("true") ? true : false; ;
			result.coverageExclusionsNotes = coverageExclusionsNotes.getValue();
			result.franchise = franchise.getValue();
			result.deductibleTypeId = deductibleType.getValue();
			result.franchiseNotes = franchiseNotes.getValue();
			result.insurerEvaluationId = insurerEvaluation.getValue();
			result.insurerEvaluationNotes = insurerEvaluationNotes.getValue(); 
			result.expertEvaluationId = expertEvaluation.getValue();
			result.expertEvaluationNotes = expertEvaluationNotes.getValue();
			result.framingEntities = getSubCasualtyFramingEntities();
		}

		return result;
	}
	
	protected void setSubCasualtyFramingEntities(SubCasualtyFramingEntity[] entities) {
		for(FormViewSection section : this.aditionalEntitiesSection) {
			//removeSection(section); TODO
		}
		this.aditionalEntitiesSection.clear();

		if(entities != null) {
			for(SubCasualtyFramingEntity entity : entities) {
				addSubCasualtyFramingEntitySection(entity);
			}
		}
	}

	protected SubCasualtyFramingEntity[] getSubCasualtyFramingEntities(){
		SubCasualtyFramingEntity[] result = new SubCasualtyFramingEntity[aditionalEntitiesSection.size()];

		int i = 0;
		for(SubCasualtyFramingEntitySection section : aditionalEntitiesSection) {
			result[i] = section.getFramingEntity();
			i++;
		}

		return result;
	}
	
	protected void addSubCasualtyFramingEntitySection(SubCasualtyFramingEntity entity){
		if(!entity.deleted) {

			final SubCasualtyFramingEntitySection section = new SubCasualtyFramingEntitySection(entity);
			section.setReadOnly(this.isReadOnly());
			this.aditionalEntitiesSection.add(section);
			
			// addSection(section); TODO

			section.getRemoveButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					removeAditionalEntityAndSection(section);
				}
			});
		} 
	}
	
	protected void removeAditionalEntityAndSection(SubCasualtyFramingEntitySection section){
		section.setVisible(false);
		SubCasualtyFramingEntity entity = section.getFramingEntity();
		entity.deleted = true;
		section.setFramingEntity(entity);
	}
	
	public boolean isReadOnly(){
		return this.isReadOnly;
	}
}
