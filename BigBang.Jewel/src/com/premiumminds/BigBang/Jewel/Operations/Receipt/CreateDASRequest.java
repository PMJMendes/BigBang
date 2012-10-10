package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Objects.DASRequest;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.DASFormReport;
import com.premiumminds.BigBang.Jewel.Reports.DASRequestReport;

public class CreateDASRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public int mlngDays;
	private DocOps mobjDocOps;
	private UUID midClient;
	private UUID midExternProcess;

	public CreateDASRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CreateDASRequest;
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Pedido de Declaração de Ausência de Sinistros";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerada uma carta de pedido de assinatura para este recibo.");
		lstrBuilder.append(pstrLineBreak).append(pstrLineBreak);

		mobjDocOps.LongDesc(lstrBuilder, pstrLineBreak);

		lstrBuilder.append(pstrLineBreak).append("Prazo limite de resposta: ").append(mlngDays).append(" dias.").append(pstrLineBreak);

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return midExternProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		Timestamp ldtNow;
		Calendar ldtAux;
		Timestamp ldtLimit;
		FileXfer lobjDASForm;
		PrintSet lobjSet;
		PrintSetDocument lobjSetClient;
		PrintSetDetail lobjSetReceipt;
        DASRequest lobjRequest;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;
		UUID lidSet;
		UUID lidSetDocument;

		larrItems = new Hashtable<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
			while ( lrs.next() )
			{
				lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
				larrItems.put((UUID)lobjAgendaProc.getAt(0),
						AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
			}
			lrs.close();
			lrs = null;

			for ( AgendaItem lobjAgItem: larrItems.values() )
			{
				lobjAgItem.ClearData(pdb);
				lobjAgItem.getDefinition().Delete(pdb, lobjAgItem.getKey());
			}
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, mlngDays);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			midClient = GetProcess().GetParent().GetParent().GetDataKey();
		else
			midClient = (UUID)GetProcess().GetParent().GetData().getAt(2);

		try
		{
			lobjDASForm = generateDocOp(GetProcess().GetDataKey());

			lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjSet.setAt(0, Constants.TID_DASRequest);
			lobjSet.setAt(1, new Timestamp(new java.util.Date().getTime()));
			lobjSet.setAt(2, Engine.getCurrentUser());
			lobjSet.setAt(3, (Timestamp)null);
			lobjSet.SaveToDb(pdb);
			lidSet = lobjSet.getKey();

			lobjSetClient = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjSetClient.setAt(0, lidSet);
			lobjSetClient.setAt(1, mobjDocOps.marrCreate[0].mobjFile);
			lobjSetClient.setAt(2, false);
			lobjSetClient.SaveToDb(pdb);
			lidSetDocument = lobjSetClient.getKey();

			lobjSetReceipt = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjSetReceipt.setAt(0, lidSetDocument);
			lobjSetReceipt.setAt(1, (lobjDASForm == null ? null : lobjDASForm.GetVarData()));
			lobjSetReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());

		try
		{
			lobjRequest = DASRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjRequest.setAt(1, ldtLimit);
			lobjRequest.SaveToDb(pdb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_DASRequest);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjRequest.getKey(), GetProcess().getKey(),
					GetContext(), pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Pedido de Declaração de Ausência de Sinistros");
			lobjItem.setAt(1, Engine.getCurrentUser());
			lobjItem.setAt(2, Constants.ProcID_DASRequest);
			lobjItem.setAt(3, ldtNow);
			lobjItem.setAt(4, ldtLimit);
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_DASRequest_ReceiveReply,
					Constants.OPID_DASRequest_RepeatRequest, Constants.OPID_DASRequest_CancelRequest}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		midExternProcess = lobjProc.getKey();
	}

	private FileXfer generateDocOp(UUID pidReceipt)
		throws BigBangJewelException
	{
		FileXfer lobjFile;
		DASRequestReport lrepDR;
		DocumentData lobjCover;
		DASFormReport lrepDF;
		DocumentData lobjForm;

		lrepDR = new DASRequestReport();
		lrepDR.midClient = midClient;
		lrepDR.midReceipt = pidReceipt;
		lobjFile = lrepDR.Generate();

		lobjCover = new DocumentData();
		lobjCover.mstrName = "Pedido de Assinatura";
		lobjCover.midOwnerType = Constants.ObjID_Receipt;
		lobjCover.midOwnerId = null;
		lobjCover.midDocType = Constants.DocID_DASRequestLetter;
		lobjCover.mstrText = null;
		lobjCover.mobjFile = lobjFile.GetVarData();
		lobjCover.marrInfo = new DocInfoData[0];

		lrepDF = new DASFormReport();
		lrepDF.midClient = midClient;
		lrepDF.midReceipt = pidReceipt;
		lobjFile = lrepDF.Generate();

		lobjForm = new DocumentData();
		lobjForm.mstrName = "DAS para Assinar";
		lobjForm.midOwnerType = Constants.ObjID_Receipt;
		lobjForm.midOwnerId = null;
		lobjForm.midDocType = Constants.DocID_DASForm;
		lobjForm.mstrText = null;
		lobjForm.mobjFile = lobjFile.GetVarData();
		lobjForm.marrInfo = new DocInfoData[0];

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjCover, lobjForm};

		return lobjFile;
	}
}
