package com.premiumminds.BigBang.Jewel.Operations.TotalLoss;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

public class CloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public CloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_TotalLoss_CloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O processo de gestão de perda total foi encerrado.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtAux;
		StringBuilder lstrLabel;
		SubCasualty lobjSubCasualty;
		UUID lidScript;
		SubPolicy lobjSubP;
		Policy lobjP;
		IProcess lrefProc;
		AgendaItem lobjItem;

		ldtAux = new Timestamp(new java.util.Date().getTime());

    	lstrLabel = new StringBuilder();
    	lstrLabel.append("O objecto seguro ");

		lobjSubCasualty = (SubCasualty)GetProcess().GetParent().GetData();

    	try
    	{
    		lstrLabel.append(lobjSubCasualty.GetObjectName());

    		if ( lobjSubCasualty.getAt(SubCasualty.I.SUBPOLICY) != null )
    		{
    			lidScript = Constants.ProcID_SubPolicy;
    			lstrLabel.append(" da apólice-adesão n. ");
    			lobjSubP = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubCasualty.getAt(SubCasualty.I.SUBPOLICY));
    			lstrLabel.append(lobjSubP.getLabel());
    			lrefProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjSubP.GetProcessID());
    		}
    		else
    		{
    			lidScript = Constants.ProcID_Policy;
    			lstrLabel.append(" da apólice n. ");
    			lobjP = Policy.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubCasualty.getAt(SubCasualty.I.POLICY));
    			lstrLabel.append(lobjP.getLabel());
    			lrefProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjP.GetProcessID());
    		}

    		lstrLabel.append(" foi dado por Perda Total no processo de sinistro n. ");
    		lstrLabel.append(lobjSubCasualty.GetCasualty().getLabel());

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Gestão de Apólice: Perda Total");
			lobjItem.setAt(1, lrefProc.GetManagerID());
			lobjItem.setAt(2, lidScript);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, ldtAux);
			lobjItem.setAt(5, Constants.UrgID_Completed);
			lobjItem.setAt(6, lstrLabel.toString());
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lrefProc.getKey()}, new UUID[] {}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo será reaberto.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo de gestão de perda total foi reaberto.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
