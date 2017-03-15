package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
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
	}
	
	public void ToObject(ObjectBase dest) throws BigBangJewelException {
		try {
			dest.setAt(SubCasualtyInsurerRequest.I.TYPE,        typeId);
			dest.setAt(SubCasualtyInsurerRequest.I.CONFORMITY,        conforms);
		}
		catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
