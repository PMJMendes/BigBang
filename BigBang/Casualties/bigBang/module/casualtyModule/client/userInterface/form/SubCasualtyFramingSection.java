package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyFraming;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

import com.google.gwt.user.client.ui.Label;

/**
 * The section where the framing is defined
 */

public class SubCasualtyFramingSection extends CollapsibleFormViewSection {

	protected SubCasualty.SubCasualtyFraming currentFraming;
	
	protected DatePickerFormField analysisDate;
	protected RadioButtonFormField difficultFraming;
	protected RadioButtonFormField validPolicy;
	protected TextAreaFormField validityNotes;
	protected RadioButtonFormField generalExclusions;
	protected TextAreaFormField generalExclusionsNotes;
	protected RadioButtonFormField relevantCoverage;
	protected TextAreaFormField coverageRelevancyNotes;
	protected NumericTextBoxFormField coverageValue;
	protected NumericTextBoxFormField baseSalary;
	protected NumericTextBoxFormField feedAllowance;
	protected NumericTextBoxFormField otherFees12;
	protected NumericTextBoxFormField otherFees14;
	protected RadioButtonFormField coverageExclusions;
	protected TextAreaFormField coverageExclusionsNotes;
	protected NumericTextBoxFormField franchise;
	protected ExpandableListBoxFormField deductibleType;
	protected TextAreaFormField franchiseNotes;
	protected Label evaluationLabel;
	protected ExpandableListBoxFormField insurerEvaluation;
	protected TextAreaFormField insurerEvaluationNotes;
	protected ExpandableListBoxFormField expertEvaluation;
	protected TextAreaFormField expertEvaluationNotes;
	protected TextAreaFormField coverageNotes;
	
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
		validityNotes.setFieldWidth("650px");
		validityNotes.setFieldHeight("50px");
		
		generalExclusions = new RadioButtonFormField(false);
		generalExclusions.setLabelText("Exclusões Gerais Aplicáveis?");
		generalExclusions.addOption("true", "Sim");
		generalExclusions.addOption("false", "Não");
		
		generalExclusionsNotes = new TextAreaFormField("Se sim, quais?");
		generalExclusionsNotes.setFieldWidth("650px");
		generalExclusionsNotes.setFieldHeight("50px");
		
		relevantCoverage = new RadioButtonFormField(false);
		relevantCoverage.setLabelText("Cobertura Aplicável?");
		relevantCoverage.addOption("true", "Sim");
		relevantCoverage.addOption("false", "Não");
		
		coverageRelevancyNotes = new TextAreaFormField("Se não, porquê?");
		coverageRelevancyNotes.setFieldWidth("650px");
		coverageRelevancyNotes.setFieldHeight("50px");
		
		coverageValue = new NumericTextBoxFormField("Capital de Cobertura", true);
		
		baseSalary = new NumericTextBoxFormField("Salário Base", true);
		feedAllowance = new NumericTextBoxFormField("Subsidio de Alimentação", true);
		otherFees12 = new NumericTextBoxFormField("Outras Remunerações (12)", true);
		otherFees14 = new NumericTextBoxFormField("Outras Remunerações (14)", true);
		baseSalary.setVisible(false);
		feedAllowance.setVisible(false);
		otherFees12.setVisible(false);
		otherFees14.setVisible(false);
		
		coverageNotes = new TextAreaFormField("Observações Capital.");
		coverageNotes.setFieldWidth("650px");
		coverageNotes.setFieldHeight("50px");
		
		coverageExclusions = new RadioButtonFormField(false);
		coverageExclusions.setLabelText("Exclusões de Cobertura Aplicáveis?");
		coverageExclusions.addOption("true", "Sim");
		coverageExclusions.addOption("false", "Não");
		
		coverageExclusionsNotes = new TextAreaFormField("Se sim, quais?");
		coverageExclusionsNotes.setFieldWidth("650px");
		coverageExclusionsNotes.setFieldHeight("50px");
		
		franchise = new NumericTextBoxFormField("Franquia Aplicável", false);
		
		deductibleType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DEDUCTIBLE_TYPE, "Tipo de Franquia");
		deductibleType.allowEdition(false);
		
		franchiseNotes = new TextAreaFormField("Observações Franquia.");
		franchiseNotes.setFieldWidth("650px");
		franchiseNotes.setFieldHeight("50px");
		
		insurerEvaluation = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.EVALUATION_TIPIFICATION, "Avaliação Segurador");
		insurerEvaluation.allowEdition(false);
		insurerEvaluationNotes = new TextAreaFormField("Observações.");
		insurerEvaluationNotes.setFieldWidth("650px");
		insurerEvaluationNotes.setFieldHeight("50px");
		
		expertEvaluation = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.EVALUATION_TIPIFICATION, "Avaliação Perito");
		expertEvaluation.allowEdition(false);
		expertEvaluationNotes = new TextAreaFormField("Observações.");
		expertEvaluationNotes.setFieldWidth("650px");
		expertEvaluationNotes.setFieldHeight("50px");
		
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
		addFormField(coverageNotes, true);
		
		addLineBreak();
		
		addFormField(baseSalary, true);
		addFormField(feedAllowance, true);
		addFormField(otherFees12, true);
		addFormField(otherFees14, true);
		
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
	}
	
	public void setFraming(final SubCasualtyFraming framing) {
		this.currentFraming = framing;

		if (framing != null) {
			/*if (framing.id != null) {
				analysisDate.setValue(framing.analysisDate);
			} else {
				analysisDate.setValue(new Date(), false);
			}*/ 
			//TODO: I believe it does not make sense to auto-set the date for it will create a framing even if the user does not wish to do so
			analysisDate.setValue(framing.analysisDate);
			difficultFraming.setValue(framing.id==null ? null : framing.framingDifficulty?"true":"false");
			validPolicy.setValue(framing.id==null ? null : framing.validPolicy?"true":"false");
			validityNotes.setValue(framing.validityNotes);
			generalExclusions.setValue(framing.id==null ? null : framing.generalExclusions?"true":"false");
			generalExclusionsNotes.setValue(framing.generalExclusionNotes);
			relevantCoverage.setValue(framing.id==null ? null : framing.relevantCoverages?"true":"false");
			coverageRelevancyNotes.setValue(framing.coverageRelevancyNotes);
			coverageValue.setValue(framing.coverageValue);
			coverageExclusions.setValue(framing.id==null ? null : framing.coverageExclusions?"true":"false");
			coverageExclusionsNotes.setValue(framing.coverageExclusionsNotes);
			franchise.setValue(framing.franchise);
			deductibleType.setValue(framing.deductibleTypeId);
			franchiseNotes.setValue(framing.franchiseNotes);
			insurerEvaluation.setValue(framing.insurerEvaluationId);
			insurerEvaluationNotes.setValue(framing.insurerEvaluationNotes);
			expertEvaluation.setValue(framing.expertEvaluationId);
			expertEvaluationNotes.setValue(framing.expertEvaluationNotes);
			coverageNotes.setValue(framing.coverageNotes);
			
			if (framing.headings != null) {
				baseSalary.setValue(framing.headings.baseSalary);
				feedAllowance.setValue(framing.headings.feedAllowance);
				otherFees12.setValue(framing.headings.otherFees12);
				otherFees14.setValue(framing.headings.otherFees14);
			}
		}
	}
	
	public SubCasualtyFraming getFraming() {
		
		SubCasualtyFraming result = this.currentFraming;
		
		if (result == null) {
			result = new SubCasualtyFraming();
			this.currentFraming = result;
		}

		if (result != null) {

			result.analysisDate = analysisDate.getStringValue();
			if(difficultFraming.getValue() != null){
				result.framingDifficulty = "true".equalsIgnoreCase(difficultFraming.getValue());
			}
			if(validPolicy.getValue() != null){
				result.validPolicy = "true".equalsIgnoreCase(validPolicy.getValue());
			}
			result.validityNotes = validityNotes.getValue();
			if(generalExclusions.getValue() != null){
				result.generalExclusions = "true".equalsIgnoreCase(generalExclusions.getValue());
			}
			result.generalExclusionNotes = generalExclusionsNotes.getValue();
			if(relevantCoverage.getValue() != null){
				result.relevantCoverages = "true".equalsIgnoreCase(relevantCoverage.getValue());
			}
			result.coverageRelevancyNotes = coverageRelevancyNotes.getValue(); 
			result.coverageValue = coverageValue.getValue();
			if(coverageExclusions.getValue() != null){
				result.coverageExclusions = "true".equalsIgnoreCase(coverageExclusions.getValue());
			}
			result.coverageExclusionsNotes = coverageExclusionsNotes.getValue();
			result.franchise = franchise.getValue();
			result.deductibleTypeId = deductibleType.getValue();
			result.franchiseNotes = franchiseNotes.getValue();
			result.insurerEvaluationId = insurerEvaluation.getValue();
			result.insurerEvaluationNotes = insurerEvaluationNotes.getValue(); 
			result.expertEvaluationId = expertEvaluation.getValue();
			result.expertEvaluationNotes = expertEvaluationNotes.getValue();
			result.coverageNotes = coverageNotes.getValue();
			
			if (result.headings != null) {
				result.headings.baseSalary = baseSalary.getValue();
				result.headings.feedAllowance = feedAllowance.getValue();
				result.headings.otherFees12 = otherFees12.getValue();
				result.headings.otherFees14 = otherFees14.getValue();
			}
		}

		return result;
	}
	
	public void setIsWorkAccidents(boolean isClear, boolean isPolicy, String reference){
		
		if(isClear) {
			hideExtraCoverageFields();
			return;
		}
		
		if (isPolicy) {
			InsurancePolicyBroker policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
			policyBroker.getPolicy(reference, new ResponseHandler<InsurancePolicy>() {
				@Override
				public void onResponse(InsurancePolicy response) {
					String categoryId = response.categoryId;
					if (categoryId!=null && categoryId.equalsIgnoreCase(BigBangConstants.PolicyCategories.WORK_ACCIDENTS)) {
						showExtraCoverageFields();
					} else {
						hideExtraCoverageFields();
					}
				}
				@Override
				public void onError(Collection<ResponseError> errors) {
					// TODO Auto-generated method stub
					
				}
			});
		} else {
			InsuranceSubPolicyBroker subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
			subPolicyBroker.getSubPolicy(reference, new ResponseHandler<SubPolicy>() {
				@Override
				public void onResponse(SubPolicy response) {
					String categoryId = response.inheritCategoryId;
					if (categoryId!=null && categoryId.equalsIgnoreCase(BigBangConstants.PolicyCategories.WORK_ACCIDENTS)) {
						showExtraCoverageFields();
					} else {
						hideExtraCoverageFields();
					}
				}
				@Override
				public void onError(Collection<ResponseError> errors) {
					// TODO Auto-generated method stub
				}
			});			
		}
		
	}
	
	private void hideExtraCoverageFields() {
		baseSalary.setVisible(false);
		feedAllowance.setVisible(false);
		otherFees12.setVisible(false);
		otherFees14.setVisible(false);
		baseSalary.setValue(null);
		feedAllowance.setValue(null);
		otherFees12.setValue(null);
		otherFees14.setValue(null);
	}

	private void showExtraCoverageFields() {
		baseSalary.setVisible(true);
		feedAllowance.setVisible(true);
		otherFees12.setVisible(true);
		otherFees14.setVisible(true);
	}
	
	public boolean isReadOnly(){
		return this.readOnly;
	}
}
