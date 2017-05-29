package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingHeadings;

/**
 * This class stores and transforms data from the BD object to the object used
 * by upper tiers
 * 
 */
public class SubCasualtyFramingHeadingsData implements DataBridge {
	
	private static final long serialVersionUID = 1L;

	// Table columns' corresponding data
	public UUID id;
	public BigDecimal baseSalary;
	public BigDecimal feedAllowance;
	public BigDecimal otherFees12;
	public BigDecimal otherFees14;
	
	public UUID framingId;
	
	public SubCasualtyFramingHeadingsData prevValues;
	
	public boolean isNew;
	public boolean isDeleted;
	
	public void FromObject(ObjectBase source) {
		id = source.getKey();
		baseSalary = (BigDecimal) source.getAt(SubCasualtyFramingHeadings.I.BASESALARY);
		feedAllowance = (BigDecimal) source.getAt(SubCasualtyFramingHeadings.I.FEEDALLOWANCE);
		otherFees12 = (BigDecimal) source.getAt(SubCasualtyFramingHeadings.I.OTHERFEES12);
		otherFees14 = (BigDecimal) source.getAt(SubCasualtyFramingHeadings.I.OTHERFEES14);
		framingId = (UUID) source.getAt(SubCasualtyFramingHeadings.I.SUBFRAMING);
	}

	public void ToObject(ObjectBase dest) throws BigBangJewelException {
		try {
			dest.setAt(SubCasualtyFramingHeadings.I.BASESALARY, baseSalary);
			dest.setAt(SubCasualtyFramingHeadings.I.FEEDALLOWANCE, feedAllowance);
			dest.setAt(SubCasualtyFramingHeadings.I.OTHERFEES12, otherFees12);
			dest.setAt(SubCasualtyFramingHeadings.I.OTHERFEES14, otherFees14);
			dest.setAt(SubCasualtyFramingHeadings.I.SUBFRAMING, framingId);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak) {
		
		pstrBuilder.append("Divisão de verbas para Acidentes de Trabalho: ");
		
		pstrBuilder.append("Salário Base: ");
		if (baseSalary  != null) {
			pstrBuilder.append(baseSalary);
		} else {
			pstrBuilder.append("(não definido)");
		}
		
		pstrBuilder.append("Subsídio de Alimentação: ");
		if (feedAllowance  != null) {
			pstrBuilder.append(feedAllowance);
		} else {
			pstrBuilder.append("(não definido)");
		}
		
		pstrBuilder.append("Outras Remunerações (12): ");
		if (otherFees12  != null) {
			pstrBuilder.append(otherFees12);
		} else {
			pstrBuilder.append("(não definidas)");
		}
		
		pstrBuilder.append("Outras Remunerações (14): ");
		if (otherFees12  != null) {
			pstrBuilder.append(otherFees14);
		} else {
			pstrBuilder.append("(não definidas)");
		}
	}
}