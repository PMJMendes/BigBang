package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingEntity;

/**
 * This class stores and transforms data from the BD object to the object used
 * by upper tiers
 * 
 */
public class SubCasualtyFramingEntitiesData implements DataBridge {
	
	private static final long serialVersionUID = 1L;

	// Table columns' corresponding data
	public UUID id;
	public UUID entityType;
	public UUID evaluation;
	public String notes;
	
	public boolean isNew;
	public boolean isDeleted;
	
	public SubCasualtyFramingEntitiesData prevValues;

	public void FromObject(ObjectBase source) {
		id = source.getKey();
		entityType = (UUID) source.getAt(SubCasualtyFramingEntity.I.ENTITYTYPE);
		evaluation = (UUID) source.getAt(SubCasualtyFramingEntity.I.EVALUATION);
		notes = (String) source.getAt(SubCasualtyFramingEntity.I.EVALUATIONNOTES);
	}

	public void ToObject(ObjectBase dest) throws BigBangJewelException {
		try {
			dest.setAt(SubCasualtyFramingEntity.I.ENTITYTYPE, entityType);
			dest.setAt(SubCasualtyFramingEntity.I.EVALUATION, evaluation);
			dest.setAt(SubCasualtyFramingEntity.I.EVALUATIONNOTES, notes);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak) {
		
		ObjectBase entityTypeObj;
		ObjectBase evaluationObj;
		
		pstrBuilder.append("Entidade extra avaliada: ");
		if (entityType != null) {
			try {
				entityTypeObj = Engine.GetWorkInstance(Engine.FindEntity(
							Engine.getCurrentNameSpace(),
							Constants.ObjID_FramingEntityTypes), entityType);
				pstrBuilder.append(entityTypeObj.getLabel());
			} catch (Throwable e) {
					pstrBuilder.append("(Erro a obter a entidade extra avaliada.)");
			}
		} else {
			pstrBuilder.append("(não definida)");
		}
		
		pstrBuilder.append("Avaliação Entidade: ");
		if (evaluation != null) {
			try {
				evaluationObj = Engine.GetWorkInstance(Engine.FindEntity(
							Engine.getCurrentNameSpace(),
							Constants.ObjID_EvaluationValues), evaluation);
				pstrBuilder.append(evaluationObj.getLabel());
			} catch (Throwable e) {
					pstrBuilder.append("(Erro a obter a avaliação.)");
			}
		} else {
			pstrBuilder.append("(não definida)");
		}
	}

}
