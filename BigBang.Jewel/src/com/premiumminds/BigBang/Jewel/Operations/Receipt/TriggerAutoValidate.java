package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class TriggerAutoValidate
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReceipt;
	private UUID midPrevManager;
	private boolean mbShort;
	
	// TODO: When changing the way the premiums are updated upon receipt validation
	// remember it is also mandatory to change the 
	// class com.premiumminds.BigBang.Jewel.Operations.Receipt.ValidateReceipt
	// Source related with this functionality is marked as /* premium_update */
	public BigDecimal prevPremium;
	public BigDecimal prevTotalPremium;

	public TriggerAutoValidate(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TriggerAutoValidate;
	}

	public String ShortDesc()
	{
		return "Validação Automática";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;
		
		lstrBuilder = new StringBuilder("O recibo foi validado automaticamente pelo sistema.");
		
		/* premium_update */
		if ((prevPremium != null) || (prevTotalPremium != null)) {
			lstrBuilder.append(pstrLineBreak)
				.append("Foram actualizados os prémios total e comercial da apólice.")
				.append(pstrLineBreak);
		}	
		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		Receipt lobjRec;
		UUID lidProfile;

		lobjProc = GetProcess();
		lobjRec = (Receipt)lobjProc.GetData();
		try
		{
			lidProfile = lobjRec.getProfile();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		midReceipt = lobjProc.GetDataKey();
		midPrevManager = lobjProc.GetManagerID();

		lobjProc.SetManagerID(Engine.getCurrentUser(), pdb);

		if ( Constants.ProfID_Simple.equals(lidProfile) && !lobjRec.isReverseCircuit() )
		{
			mbShort = true;
			TriggerOp(new TriggerForceShortCircuit(lobjProc.getKey()), pdb);
		}
		
		/* premium_update */
		try {
			UpdatePremium(pdb, lobjRec);
		} catch (BigBangJewelException e) {
			throw new JewelPetriException(e.getMessage(), e);
		} 
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A validação será retirada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;
		
		lstrBuilder = new StringBuilder("A validação foi retirada.");
		
		/* premium_update */
		if ((prevPremium != null) || (prevTotalPremium != null)) {
			lstrBuilder.append(pstrLineBreak)
				.append("Foram repostos os valores anteriores para os prémios total e comercial da apólice.")
				.append(pstrLineBreak);
		}
		return lstrBuilder.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		Receipt lobjRec;

		lobjProc = GetProcess();
		lobjProc.SetManagerID(midPrevManager, pdb);
		lobjRec = (Receipt)lobjProc.GetData();

		if ( mbShort )
			TriggerOp(new TriggerUndoShortCircuit(lobjProc.getKey()), pdb);
		
		/* premium_update */
		try {
			UnUpdatePremium(pdb, lobjRec);
		} catch (BigBangJewelException e) {
			throw new JewelPetriException(e.getMessage(), e);
		} 		
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Receipt;
		lobjSet.marrChanged = new UUID[]{midReceipt};

		return new UndoSet[]{lobjSet};
	}
	
	/* premium_update */
	private void UpdatePremium(SQLServer pdb, Receipt lobjReceipt) 
			throws BigBangJewelException {
		prevPremium = null;
		prevTotalPremium = null;

		if ( lobjReceipt.getAt(Receipt.I.TYPE).equals(Constants.RecType_Continuing) ) {
			Policy policy = lobjReceipt.getDirectPolicy();
			if (policy != null) {
				// JMMM - These functions already return NULL when the values don't change
				BigDecimal newPremium = policy.CheckSalesPremium((BigDecimal)lobjReceipt.getAt(Receipt.I.COMMERCIALPREMIUM));
				BigDecimal newTotalPr = policy.CheckTotalPremium((BigDecimal)lobjReceipt.getAt(Receipt.I.TOTALPREMIUM));

				if ((newPremium != null) || (newTotalPr != null)) {
					prevPremium = (BigDecimal)policy.getAt(Policy.I.PREMIUM);
					prevTotalPremium = (BigDecimal)policy.getAt(Policy.I.TOTALPREMIUM);
					try {
						policy.setAt(Policy.I.PREMIUM, newPremium);
						policy.setAt(Policy.I.TOTALPREMIUM, newTotalPr);
						policy.SaveToDb(pdb);
					} catch(Throwable e) {
						throw new BigBangJewelException(e.getMessage(), e);
					}
				}
			}
		}
	}

	/* premium_update */
	private void UnUpdatePremium(SQLServer pdb, Receipt lobjReceipt) 
			throws BigBangJewelException {
		if ((prevPremium != null) || (prevTotalPremium != null)) {
			Policy policy = lobjReceipt.getDirectPolicy();
			try {
				policy.setAt(Policy.I.PREMIUM, prevPremium);
				policy.setAt(Policy.I.TOTALPREMIUM, prevTotalPremium);
				policy.SaveToDb(pdb);
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}
}
