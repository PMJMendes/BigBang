package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class CreatePaymentNoticeOld
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	public Boolean mbTryEmail;
	public UUID midClient;

	public CreatePaymentNoticeOld(UUID pidProcess) {
		super(pidProcess);
	}

	public String UndoDesc(String pstrLineBreak) {
		return null;
	}

	public String UndoLongDesc(String pstrLineBreak) {
		return null;
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException {
	}

	public UndoSet[] GetSets() {
		return null;
	}

	protected UUID OpID() {
		return null;
	}

	public String ShortDesc() {
		return null;
	}

	public String LongDesc(String pstrLineBreak) {
		return null;
	}

	public UUID GetExternalProcess() {
		return null;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException {
	}
}
