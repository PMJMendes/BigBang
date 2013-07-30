package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.MessageDataOld;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class StaticFunctions
{
	public static void DoLogin(UUID pidNameSpace, UUID pidUser, boolean pbNested)
		throws BigBangJewelException
	{
		String lstrPrinter;
		UserDecoration lobjDeco;
		HashMap<String, String> larrParams;
		UUID lidParams;
        MasterDB ldb;
        ResultSet lrs;
		ObjectBase lobjParam;

		if ( pbNested )
			return;

    	larrParams = new HashMap<String, String>();
    	lobjDeco = null;

    	ldb = null;
    	lrs = null;
		try
		{
			ldb = new MasterDB();

			lidParams = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AppParams);
			lrs = Entity.GetInstance(lidParams).SelectAll(ldb);
			while (lrs.next())
			{
				lobjParam = Engine.GetWorkInstance(lidParams, lrs);
				larrParams.put((String)lobjParam.getAt(2), (String)lobjParam.getAt(1));
			}
			lrs.close();
	    	lrs = null;

	        ldb.Disconnect();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			if ( ldb != null ) try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		Engine.getUserData().put("MailServer", larrParams.get("SERVER"));

		Engine.getUserData().put("Printer", larrParams.get("PRINTER"));

    	lobjDeco = UserDecoration.GetByUserID(pidNameSpace, pidUser);
		if ( lobjDeco == null )
			return;

		lstrPrinter = (String)lobjDeco.getAt(UserDecoration.I.PRINTERNAME);
		if(lstrPrinter != null)
		{
			Engine.getUserData().put("Printer", lstrPrinter);
		}
	}

	public static void DoStartup(UUID pidNameSpace)
		throws BigBangJewelException
	{
		throw new BigBangJewelException("Esta funcionalidade está desactivada.");

//		try
//		{
//			PetriEngine.StartupByScript(pidNameSpace, Constants.ProcID_SubPolicy);
//			PetriEngine.StartupAllProcesses(pidNameSpace);
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
	}

	public static void AcctMovGen(UUID pidNameSpace)
		throws BigBangJewelException
	{
		if ( !Constants.NSID_BigBang.equals(pidNameSpace) )
			return;

		try
		{
			Engine.pushNameSpace(Constants.NSID_CredEGS);
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			AccountingExporter.Export();
		}
		catch (Throwable e)
		{
			try { Engine.popNameSpace(); } catch (JewelEngineException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			Engine.popNameSpace();
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			Engine.pushNameSpace(Constants.NSID_AMartins);
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			AccountingExporter.Export();
		}
		catch (Throwable e)
		{
			try { Engine.popNameSpace(); } catch (JewelEngineException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			Engine.popNameSpace();
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return;
	}

	public static void DoSpecial(UUID pidNameSpace)
		throws BigBangJewelException
	{
		try
		{
			Engine.pushNameSpace(Constants.NSID_CredEGS);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			InnerDoSpecial();
		}
		catch (Throwable e)
		{
			try { Engine.popNameSpace(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			Engine.popNameSpace();

			Engine.pushNameSpace(Constants.NSID_AMartins);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			InnerDoSpecial();
		}
		catch (Throwable e)
		{
			try { Engine.popNameSpace(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			Engine.popNameSpace();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void InnerDoSpecial()
		throws BigBangJewelException
	{
		DoDocuments();
		DoPrintSetDocuments();
		DoPrintSetDetails();
		DoMessages();
		DoDocLogs();
		DoOtherLogs();
	}

	private static void DoDocuments()
		throws BigBangJewelException
	{
		IEntity lrefDocs;
		MasterDB ldb;
		ResultSet lrs;
		Document lobjDoc;
		FileXfer lobjFile;

		try
		{
			lrefDocs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Document));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs = lrefDocs.SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
			{
				lobjDoc = Document.GetInstance(Engine.getCurrentNameSpace(), lrs);
				lobjFile = lobjDoc.getFile();
				if ( (lobjFile == null) || lobjFile.wasCompacted() )
				{
					Engine.GetCache(false).DeleteAt(lrefDocs.getKey(), lobjDoc.getKey());
					continue;
				}
				lobjDoc.setAt(Document.I.FILE, lobjFile);
				lobjDoc.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefDocs.getKey(), lobjDoc.getKey());
			}
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPrintSetDocuments()
		throws BigBangJewelException
	{
		IEntity lrefDocs;
		MasterDB ldb;
		ResultSet lrs;
		PrintSetDocument lobjDoc;
		FileXfer lobjFile;

		try
		{
			lrefDocs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PrintSetDocument));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs = lrefDocs.SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
			{
				lobjDoc = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), lrs);
				lobjFile = lobjDoc.getFile();
				if ( (lobjFile == null) || lobjFile.wasCompacted() )
				{
					Engine.GetCache(false).DeleteAt(lrefDocs.getKey(), lobjDoc.getKey());
					continue;
				}
				lobjDoc.setAt(PrintSetDocument.I.FILE, lobjFile);
				lobjDoc.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefDocs.getKey(), lobjDoc.getKey());
			}
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPrintSetDetails()
		throws BigBangJewelException
	{
		IEntity lrefDocs;
		MasterDB ldb;
		ResultSet lrs;
		PrintSetDetail lobjDoc;
		FileXfer lobjFile;

		try
		{
			lrefDocs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PrintSetDetail));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs = lrefDocs.SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
			{
				lobjDoc = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), lrs);
				lobjFile = lobjDoc.getFile();
				if ( (lobjFile == null) || lobjFile.wasCompacted() )
				{
					Engine.GetCache(false).DeleteAt(lrefDocs.getKey(), lobjDoc.getKey());
					continue;
				}
				lobjDoc.setAt(PrintSetDetail.I.FILE, lobjFile);
				lobjDoc.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefDocs.getKey(), lobjDoc.getKey());
			}
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoMessages()
		throws BigBangJewelException
	{
		IEntity lrefMsgs;
		MasterDB ldb;
		ResultSet lrs;
		Message lobjMsg;
		String lstrTxt;

		try
		{
			lrefMsgs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Message));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs = lrefMsgs.SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
			{
				lobjMsg = Message.GetInstance(Engine.getCurrentNameSpace(), lrs);
				lstrTxt = lobjMsg.getText();
				if ( lstrTxt == null )
				{
					Engine.GetCache(false).DeleteAt(lrefMsgs.getKey(), lobjMsg.getKey());
					continue;
				}
				lobjMsg.setText(lstrTxt);
				lobjMsg.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefMsgs.getKey(), lobjMsg.getKey());
			}
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoDocLogs()
		throws BigBangJewelException
	{
		DoPolicyManageData();
		DoReceiptTriggerImageOnCreate();
		DoReceiptReceiveImage();
		DoReceiptValidateReceipt();
		DoExpenseReceiveReception();
		DoPolicyCreateExpense();
		DoReceiptCreatePaymentNotice();
		DoExpenseSendNotification();
		DoPolicyCreateConversation();
		DoClientManageData();
		DoReceiptSendReceipt();
		DoReceiptManageData();
		DoConversationReceiveMessage();
		DoSubPolicyCreateExpense();
		DoClientExternDeletePolicy();
		DoReceiptReturnToInsurer();
		DoExpenseNotifyClient();
		DoGeneralManageInsurers();
		DoPolicyCreateReceipt();
		DoReceiptCreateSignatureRequest();
		DoSubCasualtyManageData();
		DoReceiptSendPayment();
		DoClientCreatePolicy();
		DoClientCreateConversation();
		DoCasualtyCreateSubCasualty();
		DoGeneralManageMediators();
		DoExpenseReturnToClient();
		DoClientCreateCasualty();
		DoCasualtyManageData();
		DoSubPolicyManageData();
		DoReceiptExternForceInternalDebitNote();
		DoReceiptCreateInternalReceipt();
		DoReceiptCreateDASRequest();
		DoExpenseManageData();
		DoPolicyExternDeleteReceipt();
		DoConversationSendMessage();
		DoGeneralCreateClient();
		DoReceiptCreateSecondPaymentNotice();
		DoCasualtyCreateConversation();
		DoSubCasualtyCreateConversation();
		DoSubCasualtyCreateReceipt();
		DoPolicyExternDeleteExpense();
		DoSubPolicyCreateReceipt();
		DoSubCasualtyExternDeleteReceipt();
		DoPolicyCreateSubPolicy();
		DoConversationManageData();
		DoClientExternMergeOtherHere();
		DoPolicyExternDeleteSubPolicy();
		DoSubPolicyExternDeleteExpense();
		DoClientCreateQuoteRequest();
		DoClientExternDeleteCasualty();
		DoConversationReSendMessage();
		DoGeneralExternDeleteClient();
		DoGeneralManageOtherEntities();
		DoQuoteRequestCreateNegotiation();
		DoCasualtyExternDeleteSubCasualty();
		DoClientExternDeleteQuoteRequest();
		DoNegotiationManageData();
		DoPolicyCreateDebitNote();
		DoPolicyCreateNegotiation();
		DoPolicyExternDeleteNegotiation();
		DoQuoteRequestManageData();
		DoReceiptReturnPayment();
		DoSubPolicyExternDeleteReceipt();
	}

	private static void DoOtherLogs()
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		UUID[] larrIDs;
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		Operation lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrIDs = new UUID[] {Constants.OPID_Policy_ManageData, Constants.OPID_Receipt_TriggerImageOnCreate, Constants.OPID_Receipt_ReceiveImage,
				Constants.OPID_Receipt_ValidateReceipt, Constants.OPID_Expense_ReceiveReception, Constants.OPID_Policy_CreateHealthExpense,
				Constants.OPID_Receipt_CreatePaymentNotice, Constants.OPID_Expense_SendNotification, Constants.OPID_Policy_CreateConversation,
				Constants.OPID_Client_ManageData, Constants.OPID_Receipt_SendReceipt, Constants.OPID_Receipt_ManageData,
				Constants.OPID_Conversation_ReceiveMessage, Constants.OPID_SubPolicy_CreateHealthExpense, Constants.OPID_Client_ExternDeletePolicy,
				Constants.OPID_Receipt_ReturnToInsurer, Constants.OPID_Expense_NotifyClient, Constants.OPID_General_ManageCompanies,
				Constants.OPID_Policy_CreateReceipt, Constants.OPID_Receipt_CreateSignatureRequest, Constants.OPID_SubCasualty_ManageData,
				Constants.OPID_Receipt_SendPayment, Constants.OPID_Client_CreatePolicy, Constants.OPID_Client_CreateConversation,
				Constants.OPID_Casualty_CreateSubCasualty, Constants.OPID_General_ManageMediators, Constants.OPID_Expense_ReturnToClient,
				Constants.OPID_Client_CreateCasualty, Constants.OPID_Casualty_ManageData, Constants.OPID_SubPolicy_ManageData,
				Constants.OPID_Receipt_ExternForceInternalDebitNote, Constants.OPID_Receipt_CreateInternalReceipt, Constants.OPID_Receipt_CreateDASRequest,
				Constants.OPID_Expense_ManageData, Constants.OPID_Policy_ExternDeleteReceipt, Constants.OPID_Conversation_SendMessage,
				Constants.OPID_General_CreateClient, Constants.OPID_Receipt_CreateSecondPaymentNotice, Constants.OPID_Casualty_CreateConversation,
				Constants.OPID_SubCasualty_CreateConversation, Constants.OPID_SubCasualty_CreateReceipt, Constants.OPID_Policy_ExternDeleteHealthExpense,
				Constants.OPID_SubPolicy_CreateReceipt, Constants.OPID_SubCasualty_ExternDeleteReceipt, Constants.OPID_Policy_CreateSubPolicy,
				Constants.OPID_Conversation_ManageData, Constants.OPID_Client_ExternMergeOtherHere, Constants.OPID_Policy_ExternDeleteSubPolicy,
				Constants.OPID_SubPolicy_ExternDeleteHealthExpense, Constants.OPID_Client_CreateQuoteRequest, Constants.OPID_Client_ExternDeleteCasualty,
				Constants.OPID_Conversation_ReSendMessage, Constants.OPID_General_TriggerDeleteClient, Constants.OPID_General_ManageOtherEntities,
				Constants.OPID_QuoteRequest_CreateNegotiation, Constants.OPID_Casualty_ExternDeleteSubCasualty,
				Constants.OPID_Client_ExternDeleteQuoteRequest, Constants.OPID_Negotiation_ManageData, Constants.OPID_Policy_CreateDebitNote,
				Constants.OPID_Policy_CreateNegotiation, Constants.OPID_Policy_ExternDeleteNegotiation, Constants.OPID_QuoteRequest_ManageData,
				Constants.OPID_Receipt_ReturnPayment, Constants.OPID_SubPolicy_ExternDeleteReceipt};

		lstrSQL = new StringBuilder();
		lstrSQL.append("SELECT * FROM (").append(lrefLogs.SQLForSelectAll()).append(") [AuxLogs] WHERE [Operation] NOT IN ('");
		for (i = 0; i < larrIDs.length; i++)
		{
			if ( i > 0)
				lstrSQL.append("', '");
			lstrSQL.append(larrIDs[i]);
		}
		lstrSQL.append("')");

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptTriggerImageOnCreate()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.TriggerImageOnCreate lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_TriggerImageOnCreate}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.TriggerImageOnCreate)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptReceiveImage()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.ReceiveImage lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_ReceiveImage}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.ReceiveImage)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptValidateReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.ValidateReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_ValidateReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.ValidateReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoExpenseReceiveReception()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Expense.ReceiveReception lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Expense_ReceiveReception}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Expense.ReceiveReception)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyCreateExpense()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_CreateHealthExpense}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptCreatePaymentNotice()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNotice lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_CreatePaymentNotice}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				try
				{
					lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNotice)lobjLog.GetOperationData();
				}
				catch (JewelPetriException e1)
				{
					Exception ex;

					ex = (Exception)e1.getCause();

					while ( (ex != null) && !(ex instanceof InvalidClassException) )
						ex = (Exception)ex.getCause();

					if ( ex != null )
					{
						FileXfer x;
				        ByteArrayInputStream lstream;
				        ObjectInputStream lreader;
				        com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNoticeOld lopOld;

						x = (lobjLog.getAt(6) instanceof FileXfer ? (FileXfer)lobjLog.getAt(6) : new FileXfer((byte[])lobjLog.getAt(6)));
			            lstream = new ByteArrayInputStream(x.getData());
						lreader = new com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNotice.CPNInputStream(lstream);
				        lopOld = (com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNoticeOld)lreader.readObject();
				        lreader.close();
				        lop = new com.premiumminds.BigBang.Jewel.Operations.Receipt.CreatePaymentNotice(lopOld);
					}
					else
						throw e1;
				}
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoExpenseSendNotification()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Expense.SendNotification lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Expense_SendNotification}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Expense.SendNotification)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyCreateConversation()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.CreateConversation lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_CreateConversation}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				try
				{
					lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.CreateConversation)lobjLog.GetOperationData();
				}
				catch (JewelPetriException e1)
				{
					Exception ex;

					ex = (Exception)e1.getCause();

					while ( (ex != null) && !(ex instanceof ClassCastException) )
						ex = (Exception)ex.getCause();

					if ( ex != null )
					{
						FileXfer x;
				        ByteArrayInputStream lstream;
				        ObjectInputStream lreader;
				        MessageData[] larrAux2;

						x = (lobjLog.getAt(6) instanceof FileXfer ? (FileXfer)lobjLog.getAt(6) : new FileXfer((byte[])lobjLog.getAt(6)));
			            lstream = new ByteArrayInputStream(x.getData());
						lreader = new com.premiumminds.BigBang.Jewel.Data.MessageData.MDInputStream(lstream);
				        lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.CreateConversation)lreader.readObject();
				        lreader.close();
						if ( (lop.mobjData != null) && (lop.mobjData.marrMessages != null) )
						{
							larrAux2 = new MessageData[lop.mobjData.marrMessages.length];
							for ( i = 0; i < lop.mobjData.marrMessages.length; i++ )
							{
								if ( (lop.mobjData.marrMessages[i] != null) && (lop.mobjData.marrMessages[i] instanceof MessageDataOld) )
								{
									larrAux2[i] = new MessageData();
									larrAux2[i].fromOld((MessageDataOld)lop.mobjData.marrMessages[i]);
								}
							}
							lop.mobjData.marrMessages = larrAux2;
						}
					}
					else
						throw e1;
				}
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.marrMessages != null) )
				{
					for ( i = 0; i < lop.mobjData.marrMessages.length; i++ )
					{
						if ( (lop.mobjData.marrMessages[i] != null) && (lop.mobjData.marrMessages[i].mobjDocOps != null) ) 
							lop.mobjData.marrMessages[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptSendReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.SendReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_SendReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.SendReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoConversationReceiveMessage()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Conversation.ReceiveMessage lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Conversation_ReceiveMessage}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Conversation.ReceiveMessage)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.mobjDocOps != null) ) 
					lop.mobjData.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubPolicyCreateExpense()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateExpense lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubPolicy_CreateHealthExpense}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateExpense)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientExternDeletePolicy()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeletePolicy lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_ExternDeletePolicy}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeletePolicy)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptReturnToInsurer()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnToInsurer lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_ReturnToInsurer}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnToInsurer)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoExpenseNotifyClient()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Expense.NotifyClient lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Expense_NotifyClient}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Expense.NotifyClient)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoGeneralManageInsurers()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_General_ManageCompanies}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				if ( lop.marrCreate != null )
				{
					for ( i = 0; i < lop.marrCreate.length; i++ )
					{
						if ( (lop.marrCreate[i] != null) && (lop.marrCreate[i].mobjDocOps != null) )
							lop.marrCreate[i].mobjDocOps.fromOld();
					}
				}
				if ( lop.marrModify != null )
				{
					for ( i = 0; i < lop.marrModify.length; i++ )
					{
						if ( (lop.marrModify[i] != null) && (lop.marrModify[i].mobjDocOps != null) )
							lop.marrModify[i].mobjDocOps.fromOld();
					}
				}
				if ( lop.marrDelete != null )
				{
					for ( i = 0; i < lop.marrDelete.length; i++ )
					{
						if ( (lop.marrDelete[i] != null) && (lop.marrDelete[i].mobjDocOps != null) )
							lop.marrDelete[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyCreateReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_CreateReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptCreateSignatureRequest()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateSignatureRequest lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_CreateSignatureRequest}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateSignatureRequest)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubCasualtyManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubCasualty_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptSendPayment()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.SendPayment lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_SendPayment}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.SendPayment)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientCreatePolicy()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.CreatePolicy lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_CreatePolicy}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.CreatePolicy)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientCreateConversation()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_CreateConversation}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.marrMessages != null) )
				{
					for ( i = 0; i < lop.mobjData.marrMessages.length; i++ )
					{
						if ( (lop.mobjData.marrMessages[i] != null) && (lop.mobjData.marrMessages[i].mobjDocOps != null) ) 
							lop.mobjData.marrMessages[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoCasualtyCreateSubCasualty()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateSubCasualty lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Casualty_CreateSubCasualty}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateSubCasualty)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoGeneralManageMediators()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_General_ManageMediators}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				if ( lop.marrCreate != null )
				{
					for ( i = 0; i < lop.marrCreate.length; i++ )
					{
						if ( (lop.marrCreate[i] != null) && (lop.marrCreate[i].mobjDocOps != null) )
							lop.marrCreate[i].mobjDocOps.fromOld();
					}
				}
				if ( lop.marrModify != null )
				{
					for ( i = 0; i < lop.marrModify.length; i++ )
					{
						if ( (lop.marrModify[i] != null) && (lop.marrModify[i].mobjDocOps != null) )
							lop.marrModify[i].mobjDocOps.fromOld();
					}
				}
				if ( lop.marrDelete != null )
				{
					for ( i = 0; i < lop.marrDelete.length; i++ )
					{
						if ( (lop.marrDelete[i] != null) && (lop.marrDelete[i].mobjDocOps != null) )
							lop.marrDelete[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoExpenseReturnToClient()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Expense.ReturnToClient lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Expense_ReturnToClient}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Expense.ReturnToClient)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientCreateCasualty()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.CreateCasualty lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_CreateCasualty}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.CreateCasualty)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoCasualtyManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Casualty_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubPolicyManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubPolicy_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptExternForceInternalDebitNote()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternForceInternalDebitNote lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_ExternForceInternalDebitNote}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.ExternForceInternalDebitNote)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptCreateInternalReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateInternalReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_CreateInternalReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateInternalReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptCreateDASRequest()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateDASRequest lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_CreateDASRequest}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateDASRequest)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoExpenseManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Expense_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyExternDeleteReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_ExternDeleteReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoConversationSendMessage()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Conversation.SendMessage lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Conversation_SendMessage}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				try
				{
					lop = (com.premiumminds.BigBang.Jewel.Operations.Conversation.SendMessage)lobjLog.GetOperationData();
				}
				catch (JewelPetriException e1)
				{
					Exception ex;

					ex = (Exception)e1.getCause();

					while ( (ex != null) && !(ex instanceof ClassCastException) )
						ex = (Exception)ex.getCause();

					if ( ex != null )
					{
						FileXfer x;
				        ByteArrayInputStream lstream;
				        ObjectInputStream lreader;
				        MessageData lobjAux;

						x = (lobjLog.getAt(6) instanceof FileXfer ? (FileXfer)lobjLog.getAt(6) : new FileXfer((byte[])lobjLog.getAt(6)));
			            lstream = new ByteArrayInputStream(x.getData());
						lreader = new com.premiumminds.BigBang.Jewel.Data.MessageData.MDInputStream(lstream);
				        lop = (com.premiumminds.BigBang.Jewel.Operations.Conversation.SendMessage)lreader.readObject();
				        lreader.close();
						if ( (lop.mobjData != null) && (lop.mobjData instanceof MessageDataOld) )
						{
							lobjAux = new MessageData();
							lobjAux.fromOld((MessageDataOld)lop.mobjData);
							lop.mobjData = lobjAux;
						}
					}
					else
						throw e1;
				}
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.mobjDocOps != null) ) 
					lop.mobjData.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoGeneralCreateClient()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.General.CreateClient lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_General_CreateClient}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.General.CreateClient)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptCreateSecondPaymentNotice()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateSecondPaymentNotice lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_CreateSecondPaymentNotice}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateSecondPaymentNotice)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoCasualtyCreateConversation()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateConversation lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Casualty_CreateConversation}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateConversation)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.marrMessages != null) )
				{
					for ( i = 0; i < lop.mobjData.marrMessages.length; i++ )
					{
						if ( (lop.mobjData.marrMessages[i] != null) && (lop.mobjData.marrMessages[i].mobjDocOps != null) ) 
							lop.mobjData.marrMessages[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubCasualtyCreateConversation()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateConversation lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubCasualty_CreateConversation}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateConversation)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.marrMessages != null) )
				{
					for ( i = 0; i < lop.mobjData.marrMessages.length; i++ )
					{
						if ( (lop.mobjData.marrMessages[i] != null) && (lop.mobjData.marrMessages[i].mobjDocOps != null) ) 
							lop.mobjData.marrMessages[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubCasualtyCreateReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubCasualty_CreateReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyExternDeleteExpense()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteExpense lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_ExternDeleteHealthExpense}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteExpense)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubPolicyCreateReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubPolicy_CreateReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubCasualtyExternDeleteReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternDeleteReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubCasualty_ExternDeleteReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternDeleteReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyCreateSubPolicy()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.CreateSubPolicy lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_CreateSubPolicy}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.CreateSubPolicy)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoConversationManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Conversation.ManageData lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Conversation_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Conversation.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.marrMessages != null) )
				{
					for ( i = 0; i < lop.mobjData.marrMessages.length; i++ )
					{
						if ( (lop.mobjData.marrMessages[i] != null) && (lop.mobjData.marrMessages[i].mobjDocOps != null) ) 
							lop.mobjData.marrMessages[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientExternMergeOtherHere()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.ExternMergeOtherHere lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_ExternMergeOtherHere}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.ExternMergeOtherHere)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyExternDeleteSubPolicy()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteSubPolicy lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_ExternDeleteSubPolicy}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteSubPolicy)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubPolicyExternDeleteExpense()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteExpense lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubPolicy_ExternDeleteHealthExpense}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteExpense)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientCreateQuoteRequest()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.CreateQuoteRequest lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_CreateQuoteRequest}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.CreateQuoteRequest)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientExternDeleteCasualty()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeleteCasualty lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_ExternDeleteCasualty}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeleteCasualty)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoConversationReSendMessage()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Conversation.ReSendMessage lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Conversation_ReSendMessage}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Conversation.ReSendMessage)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( (lop.mobjData != null) && (lop.mobjData.mobjDocOps != null) ) 
					lop.mobjData.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoGeneralExternDeleteClient()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.General.ExternDeleteClient lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_General_TriggerDeleteClient}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.General.ExternDeleteClient)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoGeneralManageOtherEntities()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities lop;
		int i;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_General_ManageOtherEntities}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				if ( lop.marrCreate != null )
				{
					for ( i = 0; i < lop.marrCreate.length; i++ )
					{
						if ( (lop.marrCreate[i] != null) && (lop.marrCreate[i].mobjDocOps != null) )
							lop.marrCreate[i].mobjDocOps.fromOld();
					}
				}
				if ( lop.marrModify != null )
				{
					for ( i = 0; i < lop.marrModify.length; i++ )
					{
						if ( (lop.marrModify[i] != null) && (lop.marrModify[i].mobjDocOps != null) )
							lop.marrModify[i].mobjDocOps.fromOld();
					}
				}
				if ( lop.marrDelete != null )
				{
					for ( i = 0; i < lop.marrDelete.length; i++ )
					{
						if ( (lop.marrDelete[i] != null) && (lop.marrDelete[i].mobjDocOps != null) )
							lop.marrDelete[i].mobjDocOps.fromOld();
					}
				}
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoQuoteRequestCreateNegotiation()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CreateNegotiation lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_QuoteRequest_CreateNegotiation}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CreateNegotiation)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoCasualtyExternDeleteSubCasualty()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternDeleteSubCasualty lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Casualty_ExternDeleteSubCasualty}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternDeleteSubCasualty)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoClientExternDeleteQuoteRequest()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeleteQuoteRequest lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Client_ExternDeleteQuoteRequest}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeleteQuoteRequest)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoNegotiationManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Negotiation_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyCreateDebitNote()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.CreateDebitNote lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_CreateDebitNote}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.CreateDebitNote)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyCreateNegotiation()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.CreateNegotiation lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_CreateNegotiation}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.CreateNegotiation)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoPolicyExternDeleteNegotiation()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteNegotiation lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Policy_ExternDeleteNegotiation}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteNegotiation)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoQuoteRequestManageData()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_QuoteRequest_ManageData}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoReceiptReturnPayment()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnPayment lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_ReturnPayment}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnPayment)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void DoSubPolicyExternDeleteReceipt()
		throws BigBangJewelException
	{
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrs;
		PNLog lobjLog;
		ArrayList<UUID> larrAux;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteReceipt lop;

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrAux = new ArrayList<UUID>();
		try
		{
			lrs = lrefLogs.SelectByMembers(ldb, new int[] {Jewel.Petri.Constants.FKOperation_In_Log},
					new java.lang.Object[] {Constants.OPID_SubPolicy_ExternDeleteReceipt}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while (lrs.next())
				larrAux.add(UUID.fromString(lrs.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();

			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			for ( UUID lid : larrAux )
			{
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
				lop = (com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteReceipt)lobjLog.GetOperationData();
				if ( lop == null )
				{
					Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
					continue;
				}
				if ( lop.mobjDocOps != null )
					lop.mobjDocOps.fromOld();
				lobjLog.replaceOperationData(lop);
				lobjLog.SaveToDb(ldb);
				Engine.GetCache(false).DeleteAt(lrefLogs.getKey(), lobjLog.getKey());
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
