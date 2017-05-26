package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming;

/**
 * This class stores and transforms data from the BD object to the object used
 * by upper tiers
 * 
 */
public class SubCasualtyFramingData implements DataBridge {

	private static final long serialVersionUID = 1L;
	
	// Table columns, corresponding data
	public UUID id;
	public Timestamp analysisDate;
	public boolean wasDifficult;
	public boolean policyValid;
	public String validityNotes;
	public boolean areGeneralExclusions;
	public String generalExclusionsNotes;
	public boolean isCoverageRelevant;
	public String coverageRelevancyNotes;
	public BigDecimal coverageValue;
	public boolean areCoverageExclusions;
	public String coverageExclusionsNotes;
	public BigDecimal franchise;
	public UUID franchiseType;
	public String franchiseNotes;
	public UUID insurerEvaluation;
	public String insurerEvaluationNotes;
	public UUID expertEvaluation;
	public String expertEvaluationNotes;
	public String coverageNotes;
	
	public UUID subCasualtyId;
	
	public SubCasualtyFramingEntitiesData[] framingEntities;
	
	public SubCasualtyFramingHeadingsData framingHeadings;
	
	public boolean isNew;
	public boolean isDeleted;

	public SubCasualtyFramingData prevValues;

	public void FromObject(ObjectBase source) {
		id = source.getKey();
		analysisDate = (Timestamp) source.getAt(SubCasualtyFraming.I.ANALYSISDATE);
		wasDifficult = (Boolean) source.getAt(SubCasualtyFraming.I.FRAMINGDIFFICULTY);
		policyValid = (Boolean) source.getAt(SubCasualtyFraming.I.VALIDPOLICY);
		validityNotes = (String) source.getAt(SubCasualtyFraming.I.VALIDITYNOTES);
		areGeneralExclusions = (Boolean) source.getAt(SubCasualtyFraming.I.GENERALEXCLUSIONS);
		generalExclusionsNotes = (String) source.getAt(SubCasualtyFraming.I.GENERALEXCLUSIONSNOTES);
		isCoverageRelevant = (Boolean) source.getAt(SubCasualtyFraming.I.RELEVANTCOVERAGE);
		coverageRelevancyNotes = (String) source.getAt(SubCasualtyFraming.I.COVERAGERELEVANCYNOTES);
		coverageValue = (BigDecimal) source.getAt(SubCasualtyFraming.I.COVERAGEVALUE);
		areCoverageExclusions = (Boolean) source.getAt(SubCasualtyFraming.I.COVERAGEEXCLUSIONS);
		coverageExclusionsNotes = (String) source.getAt(SubCasualtyFraming.I.COVERAGEEXCLUSIONSNOTES);
		franchise = (BigDecimal) source.getAt(SubCasualtyFraming.I.FRANCHISE);
		franchiseType = (UUID) source.getAt(SubCasualtyFraming.I.DEDUCTIBLETYPE);
		franchiseNotes = (String) source.getAt(SubCasualtyFraming.I.FRANCHISENOTES);
		insurerEvaluation = (UUID) source.getAt(SubCasualtyFraming.I.INSUREREVALUATION);
		insurerEvaluationNotes = (String) source.getAt(SubCasualtyFraming.I.INSUREREVALUATIONNOTES);
		expertEvaluation = (UUID) source.getAt(SubCasualtyFraming.I.EXPERTEVALUATION);
		expertEvaluationNotes = (String) source.getAt(SubCasualtyFraming.I.EXPERTEVALUATIONNOTES);		
		subCasualtyId = (UUID) source.getAt(SubCasualtyFraming.I.SUBCASUALTY);
		coverageNotes = (String) source.getAt(SubCasualtyFraming.I.COVERAGENOTES);	
	}

	public void ToObject(ObjectBase dest) throws BigBangJewelException {
		try {
			dest.setAt(SubCasualtyFraming.I.ANALYSISDATE, analysisDate);
			dest.setAt(SubCasualtyFraming.I.FRAMINGDIFFICULTY, wasDifficult); 
			dest.setAt(SubCasualtyFraming.I.VALIDPOLICY, policyValid);
			dest.setAt(SubCasualtyFraming.I.VALIDITYNOTES, validityNotes);
			dest.setAt(SubCasualtyFraming.I.GENERALEXCLUSIONS, areGeneralExclusions);
			dest.setAt(SubCasualtyFraming.I.GENERALEXCLUSIONSNOTES, generalExclusionsNotes);
			dest.setAt(SubCasualtyFraming.I.RELEVANTCOVERAGE, isCoverageRelevant);
			dest.setAt(SubCasualtyFraming.I.COVERAGERELEVANCYNOTES, coverageRelevancyNotes);
			dest.setAt(SubCasualtyFraming.I.COVERAGEVALUE, coverageValue);
			dest.setAt(SubCasualtyFraming.I.COVERAGEEXCLUSIONS, areCoverageExclusions);
			dest.setAt(SubCasualtyFraming.I.COVERAGEEXCLUSIONSNOTES, coverageExclusionsNotes);
			dest.setAt(SubCasualtyFraming.I.FRANCHISE, franchise);
			dest.setAt(SubCasualtyFraming.I.DEDUCTIBLETYPE, franchiseType);
			dest.setAt(SubCasualtyFraming.I.FRANCHISENOTES, franchiseNotes);
			dest.setAt(SubCasualtyFraming.I.INSUREREVALUATION, insurerEvaluation);
			dest.setAt(SubCasualtyFraming.I.INSUREREVALUATIONNOTES, insurerEvaluationNotes);
			dest.setAt(SubCasualtyFraming.I.EXPERTEVALUATION, expertEvaluation);
			dest.setAt(SubCasualtyFraming.I.EXPERTEVALUATIONNOTES, expertEvaluationNotes);
			dest.setAt(SubCasualtyFraming.I.COVERAGENOTES, coverageNotes);
			dest.setAt(SubCasualtyFraming.I.SUBCASUALTY, subCasualtyId);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak) {
		
		ObjectBase franchiseTypeObj;
		ObjectBase evaluation;
		
		pstrBuilder.append("Data de Análise: ");
		if (analysisDate  != null) {
			pstrBuilder.append(analysisDate .toString().substring(0, 19));
		} else {
			pstrBuilder.append("Não indicada.");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (wasDifficult) {
			pstrBuilder.append("Existiram dificuldades no enquadramento");
		} else {
			pstrBuilder.append("Não existiram dificuldades no enquadramento");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (policyValid) {
			pstrBuilder.append("A apólice foi dada como válida");
		} else {
			pstrBuilder.append("A apólice foi dada como inválida");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (validityNotes != null) {
			pstrBuilder.append("Notas sobre validade: ");
			pstrBuilder.append(validityNotes);
		}
		pstrBuilder.append(pstrLineBreak);		
		
		pstrBuilder.append(pstrLineBreak);
		if (areGeneralExclusions ) {
			pstrBuilder.append("Existem exclusões gerais aplicáveis");
		} else {
			pstrBuilder.append("Não existem exclusões gerais aplicáveis");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (generalExclusionsNotes  != null) {
			pstrBuilder.append("Notas sobre Exclusões Gerais Aplicáveis: ");
			pstrBuilder.append(generalExclusionsNotes);
		}
		pstrBuilder.append(pstrLineBreak);		
		
		pstrBuilder.append(pstrLineBreak);
		if (isCoverageRelevant) {
			pstrBuilder.append("A cobertura é aplicável");
		} else {
			pstrBuilder.append("A cobertura não é aplicável");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (coverageRelevancyNotes   != null) {
			pstrBuilder.append("Notas sobre aplicabilidade da cobertura: ");
			pstrBuilder.append(coverageRelevancyNotes);
		}
		
		pstrBuilder.append(pstrLineBreak);		
		pstrBuilder.append("Capital cobertura: ");
		if (coverageValue  != null) {
			pstrBuilder.append(coverageValue);
		} else {
			pstrBuilder.append("(não definido)");
		}
		
		if (framingHeadings != null) {
			framingHeadings.Describe(pstrBuilder, pstrLineBreak);
		}
		
		if (coverageNotes != null) {
			pstrBuilder.append("Notas sobrecobertura: ");
			pstrBuilder.append(coverageNotes );
		}

		pstrBuilder.append(pstrLineBreak);
		if (areCoverageExclusions) {
			pstrBuilder.append("Existem exclusões da cobertura aplicáveis");
		} else {
			pstrBuilder.append("Não existem exclusões da cobertura aplicáveis");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (coverageExclusionsNotes    != null) {
			pstrBuilder.append("Notas sobre exclusões da cobertura: ");
			pstrBuilder.append(coverageExclusionsNotes);
		}
		
		pstrBuilder.append(pstrLineBreak);		
		pstrBuilder.append("Capital da franquia: ");
		if (coverageValue  != null) {
			pstrBuilder.append(franchise);
			
			if (franchiseType != null) {
				try {
					franchiseTypeObj = Engine.GetWorkInstance(Engine.FindEntity(
							Engine.getCurrentNameSpace(),
							Constants.ObjID_DeductibleType), franchiseType);
					pstrBuilder.append(" " + franchiseTypeObj.getLabel());
				} catch (Throwable e) {
					pstrBuilder
							.append("(Erro a obter a unidade da franquia.)");
				}
			}
		} else {
			pstrBuilder.append("(não definido)");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (validityNotes != null) {
			pstrBuilder.append("Notas sobre franquia: ");
			pstrBuilder.append(franchiseNotes);
		}
		pstrBuilder.append(pstrLineBreak);	
		
		pstrBuilder.append(pstrLineBreak);		
		pstrBuilder.append("Avaliação segurador: ");
		if (insurerEvaluation != null) {
			try {
				evaluation = Engine.GetWorkInstance(Engine.FindEntity(
							Engine.getCurrentNameSpace(),
							Constants.ObjID_EvaluationValues), insurerEvaluation);
				pstrBuilder.append(evaluation.getLabel());
			} catch (Throwable e) {
					pstrBuilder.append("(Erro a obter a avaliação de segurador.)");
			}
		} else {
			pstrBuilder.append("(não definida)");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (insurerEvaluationNotes  != null) {
			pstrBuilder.append("Notas sobre avaliação do segurador: ");
			pstrBuilder.append(insurerEvaluationNotes);
		}
		
		pstrBuilder.append(pstrLineBreak);		
		pstrBuilder.append("Avaliação Perito: ");
		if (expertEvaluation  != null) {
			try {
				evaluation = Engine.GetWorkInstance(Engine.FindEntity(
							Engine.getCurrentNameSpace(),
							Constants.ObjID_EvaluationValues), expertEvaluation );
				pstrBuilder.append(evaluation.getLabel());
			} catch (Throwable e) {
					pstrBuilder.append("(Erro a obter a avaliação de perito.)");
			}
		} else {
			pstrBuilder.append("(não definida)");
		}
		
		pstrBuilder.append(pstrLineBreak);
		if (expertEvaluationNotes   != null) {
			pstrBuilder.append("Notas sobre avaliação de perito: ");
			pstrBuilder.append(expertEvaluationNotes );
		}
		
		if (framingEntities != null && framingEntities.length > 0) {
			pstrBuilder.append(pstrLineBreak).append("Outras entidades envolvidas:").append(pstrLineBreak).append(pstrLineBreak);
			for (int i=0; i<framingEntities.length; i++) {
				framingEntities[i].Describe(pstrBuilder, pstrLineBreak);
				pstrBuilder.append(pstrLineBreak);
			}
		}
	}
}
