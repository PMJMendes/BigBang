package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyInsurerRequest;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;

public class SubCasualtyInsurerRequestData implements DataBridge {
	
	public UUID id;

	private static final long serialVersionUID = 1L;
	public UUID typeId;
	public boolean conforms;
	public Timestamp requestDate;
	public Timestamp acceptanceDate;
	public Timestamp resendDate;
	public Timestamp clarificationDate;
	public UUID subCasualtyId;
	
	public boolean isNew;
	public boolean isDeleted;

	public SubCasualtyInsurerRequestData prevValues;
	
	public void FromObject(ObjectBase source) {
		
		id = source.getKey();

		typeId				= (UUID) source.getAt(SubCasualtyInsurerRequest.I.TYPE);
		conforms           	= (Boolean)source.getAt(SubCasualtyInsurerRequest.I.CONFORMITY);
		requestDate   		= (Timestamp)source.getAt(SubCasualtyInsurerRequest.I.REQUESTDATE);
		acceptanceDate   	= (Timestamp)source.getAt(SubCasualtyInsurerRequest.I.ACCEPTANCEDATE);
		resendDate   		= (Timestamp)source.getAt(SubCasualtyInsurerRequest.I.RESENDDATE);
		clarificationDate   = (Timestamp)source.getAt(SubCasualtyInsurerRequest.I.CLARIFICATIONDATE);
		subCasualtyId       = (UUID)source.getAt(SubCasualtyInsurerRequest.I.SUBCASUALTY);
	}
	
	public void ToObject(ObjectBase dest) throws BigBangJewelException {
		try {
			dest.setAt(SubCasualtyInsurerRequest.I.TYPE,        		typeId);
			dest.setAt(SubCasualtyInsurerRequest.I.CONFORMITY,        	conforms);
			dest.setAt(SubCasualtyInsurerRequest.I.REQUESTDATE,        	requestDate);
			dest.setAt(SubCasualtyInsurerRequest.I.ACCEPTANCEDATE,      acceptanceDate);
			dest.setAt(SubCasualtyInsurerRequest.I.RESENDDATE,        	resendDate);
			dest.setAt(SubCasualtyInsurerRequest.I.CLARIFICATIONDATE,  	clarificationDate);
			dest.setAt(SubCasualtyInsurerRequest.I.SUBCASUALTY,         subCasualtyId);
		}
		catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak) {
		ObjectBase requestType;
		
		pstrBuilder.append("Tipologia: ");
		if ( typeId != null ) {
			try {
				requestType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualtyInsurerRequest), typeId);
				pstrBuilder.append(requestType.getLabel());
			} catch (Throwable e) {
				pstrBuilder.append("(Erro a obter o tipo de pedido de segurador.)");
			}
		} else {
			pstrBuilder.append("Não indicada.");
		}
		pstrBuilder.append(pstrLineBreak);
		
		pstrBuilder.append("Data de Pedido: ");
		if ( requestDate != null ) {
			pstrBuilder.append(requestDate.toString().substring(0, 19));	
		} else {
			pstrBuilder.append("Não indicada.");
		}
		pstrBuilder.append(pstrLineBreak);
		
		pstrBuilder.append("Data de Aceitação: ");
		if ( acceptanceDate != null ) {
			pstrBuilder.append(acceptanceDate.toString().substring(0, 19));	
		} else {
			pstrBuilder.append("Não indicada.");
		}
		pstrBuilder.append(pstrLineBreak);
		
		if (conforms) {
			pstrBuilder.append("Pedido Conforme");
			pstrBuilder.append(pstrLineBreak);
			pstrBuilder.append("Data de Reenvio: ");
			if ( resendDate != null ) {
				pstrBuilder.append(resendDate.toString().substring(0, 19));	
			} else {
				pstrBuilder.append("Não indicada.");
			}
			pstrBuilder.append(pstrLineBreak);
		} else {
			pstrBuilder.append("Pedido Não Conforme");
			pstrBuilder.append(pstrLineBreak);
			pstrBuilder.append("Data de Pedido de Clarificação: ");
			if ( clarificationDate != null ) {
				pstrBuilder.append(clarificationDate.toString().substring(0, 19));	
			} else {
				pstrBuilder.append("Não indicada.");
			}
			pstrBuilder.append(pstrLineBreak);
		}
	}
}
