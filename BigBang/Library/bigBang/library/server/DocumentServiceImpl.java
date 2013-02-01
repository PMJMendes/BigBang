package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Objects.PNOperation;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.ProcessData;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.interfaces.DocumentService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.OtherEntity;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class DocumentServiceImpl
	extends EngineImplementor
	implements DocumentService
{
	private static final long serialVersionUID = 1L;

	public static DocumentData[] BuildDocTree(Document[] parrDocuments)
	{
		DocumentData[] larrResult;
		int i, j;

		if ( (parrDocuments == null) || (parrDocuments.length == 0) )
			return null;

		larrResult = new DocumentData[parrDocuments.length];
		for ( i = 0; i < parrDocuments.length; i++ )
		{
			larrResult[i] = new DocumentData();
			larrResult[i].mid = (parrDocuments[i].id == null ? null : UUID.fromString(parrDocuments[i].id));
			larrResult[i].mstrName = parrDocuments[i].name;
			larrResult[i].midOwnerType = UUID.fromString(parrDocuments[i].ownerTypeId);
			larrResult[i].midOwnerId = UUID.fromString(parrDocuments[i].ownerId);
			larrResult[i].midDocType = (parrDocuments[i].docTypeId == null ? null : UUID.fromString(parrDocuments[i].docTypeId));
			larrResult[i].mdtRefDate = (parrDocuments[i].creationDate == null ? null :
					Timestamp.valueOf(parrDocuments[i].creationDate + " 00:00:00.0"));

			if ( parrDocuments[i].source != null )
			{
				larrResult[i].mstrText = null;
				larrResult[i].mobjDSBridge = new DSBridgeData();
				larrResult[i].mobjDSBridge.mstrDSHandle = parrDocuments[i].source.handle;
				larrResult[i].mobjDSBridge.mstrDSLoc = parrDocuments[i].source.locationHandle;
				larrResult[i].mobjDSBridge.mstrDSTitle = null;
				larrResult[i].mobjDSBridge.mbDelete = false;
			}
			else if ( parrDocuments[i].fileStorageId != null )
			{
				larrResult[i].mobjDSBridge = null;
				larrResult[i].mstrText = null;
				larrResult[i].mobjFile = FileServiceImpl.GetFileXferStorage().
						get(UUID.fromString(parrDocuments[i].fileStorageId)).GetVarData();
			}
			else
			{
				larrResult[i].mobjDSBridge = null;
				larrResult[i].mstrText = parrDocuments[i].text;
				larrResult[i].mobjFile = null;
			}
			if ( parrDocuments[i].parameters != null )
			{
				larrResult[i].marrInfo = new DocInfoData[parrDocuments[i].parameters.length];
				for ( j = 0; j < parrDocuments[i].parameters.length; j++ )
				{
					larrResult[i].marrInfo[j] = new DocInfoData();
					larrResult[i].marrInfo[j].mstrType = parrDocuments[i].parameters[j].name;
					larrResult[i].marrInfo[j].mstrValue = parrDocuments[i].parameters[j].value;
				}
			}
			else
				larrResult[i].marrInfo = null;
		}

		return larrResult;
	}

	public static void WalkDocTree(DocumentData[] parrResults, Document[] parrDocuments)
	{
		int i;
		
		for ( i = 0; i < parrResults.length; i++ )
			parrDocuments[i].id = parrResults[i].mid.toString();
	}

	public static Document sGetDocument(UUID pidDocument)
		throws BigBangException
	{
		try
		{
			return fromServer(com.premiumminds.BigBang.Jewel.Objects.Document
					.GetInstance(Engine.getCurrentNameSpace(), pidDocument), false);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public Document[] getDocuments(String ownerId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<Document> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefDocuments;
        MasterDB ldb;
        ResultSet lrsDocs;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<Document>();

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKOwner_In_Document;
		larrParams = new java.lang.Object[1];
		larrParams[0] = UUID.fromString(ownerId);

		try
		{
			lrefDocuments = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Document)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsDocs = lrefDocuments.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDocs.next() )
				larrAux.add(fromServer(com.premiumminds.BigBang.Jewel.Objects.Document
						.GetInstance(Engine.getCurrentNameSpace(), lrsDocs), true));
		}
		catch (BigBangException e)
		{
			try { lrsDocs.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsDocs.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsDocs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new Document[larrAux.size()]);
	}

	public Document getDocument(String docId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetDocument(UUID.fromString(docId));
	}

	public Document createDocument(Document document)
		throws SessionExpiredException, BigBangException
	{
		Document[] larrAux;
		DocOps lopDOps;
		Operation lobjOp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Document[1];
		larrAux[0] = document;

		lopDOps = new DocOps();
		lopDOps.marrCreate = BuildDocTree(larrAux);
		lopDOps.marrModify = null;
		lopDOps.marrDelete = null;

		lobjOp = BuildOuterOp(UUID.fromString(document.ownerTypeId), UUID.fromString(document.ownerId), lopDOps);

		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetDocument(lopDOps.marrCreate[0].mid);
	}

	public Document saveDocument(Document document)
		throws SessionExpiredException, BigBangException
	{
		Document[] larrAux;
		DocOps lopDOps;
		Operation lobjOp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Document[1];
		larrAux[0] = document;

		lopDOps = new DocOps();
		lopDOps.marrModify = BuildDocTree(larrAux);
		lopDOps.marrCreate = null;
		lopDOps.marrDelete = null;

		lobjOp = BuildOuterOp(UUID.fromString(document.ownerTypeId), UUID.fromString(document.ownerId), lopDOps);
		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetDocument(lopDOps.marrModify[0].mid);
	}

	public void deleteDocument(String id)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Document lobjAuxDoc;
		DocumentData lobjData;
		DocOps lopDOps;
		Operation lobjOp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAuxDoc = com.premiumminds.BigBang.Jewel.Objects.Document.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(id));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjData = new DocumentData();
		lobjData.mid = lobjAuxDoc.getKey();
		lobjData.midOwnerType = lobjAuxDoc.getOwnerType();
		lobjData.midOwnerId = lobjAuxDoc.getOwnerID();

		lopDOps = new DocOps();
		lopDOps.marrDelete = new DocumentData[] {lobjData};
		lopDOps.marrCreate = null;
		lopDOps.marrModify = null;

		lobjOp = BuildOuterOp(lobjData.midOwnerType, lobjData.midOwnerId, lopDOps);

		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private static Document fromServer(com.premiumminds.BigBang.Jewel.Objects.Document pobjDocument, boolean pbForList)
		throws BigBangException
	{
		ObjectBase lobjType;
		Document lobjAux;
		com.premiumminds.BigBang.Jewel.Objects.DocInfo[] larrInfo;
		int i;
		FileXfer laux;
		UUID lidAux;

		try
		{
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DocType),
					(UUID)pobjDocument.getAt(3));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		lobjAux = new Document();

		lobjAux.id = pobjDocument.getKey().toString();
		lobjAux.name = (String)pobjDocument.getAt(0);
		lobjAux.ownerTypeId = ((UUID)pobjDocument.getAt(1)).toString();
		lobjAux.ownerId = ((UUID)pobjDocument.getAt(2)).toString();
		lobjAux.docTypeId = lobjType.getKey().toString();
		lobjAux.docTypeLabel = lobjType.getLabel();
		lobjAux.creationDate = (pobjDocument.getAt(6) == null ? null : ((Timestamp)pobjDocument.getAt(6)).toString().substring(0, 10));
		lobjAux.text = (String)pobjDocument.getAt(4);
		if ( pbForList )
		{
			lobjAux.hasFile = (pobjDocument.getAt(5) != null);
		}
		else
		{
			if ( pobjDocument.getAt(5) == null )
			{
				lobjAux.hasFile = false;
				lobjAux.mimeType = null;
				lobjAux.fileName = null;
				lobjAux.fileStorageId = null;
			}
			else
			{
				lobjAux.hasFile = true;
		    	if ( pobjDocument.getAt(5) instanceof FileXfer )
		    		laux = (FileXfer)pobjDocument.getAt(5);
		    	else
		        	laux = new FileXfer((byte[])pobjDocument.getAt(5));
		    	lidAux = UUID.randomUUID();
		    	FileServiceImpl.GetFileXferStorage().put(lidAux, laux);
				lobjAux.mimeType = laux.getContentType();
				lobjAux.fileName = laux.getFileName();
				lobjAux.fileStorageId = lidAux.toString();
			}
		}

		try
		{
			larrInfo = pobjDocument.getCurrentInfo();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		lobjAux.parameters = new DocInfo[larrInfo.length];
		for ( i = 0; i < larrInfo.length; i++ )
			lobjAux.parameters[i] = fromServer(larrInfo[i]);

		return lobjAux;
	}

	private static DocInfo fromServer(com.premiumminds.BigBang.Jewel.Objects.DocInfo pobjDocInfo)
	{
		DocInfo lobjAux;

		lobjAux = new DocInfo();

		lobjAux.name = (String)pobjDocInfo.getAt(1);
		lobjAux.value = (String)pobjDocInfo.getAt(2);
		return lobjAux;
	}

	private Operation BuildOuterOp(UUID pidOwnerType, UUID pidOwner, DocOps pobjInner)
		throws BigBangException
	{
		ObjectBase lobjOwner;
		UUID lidTopType;
		UUID lidProc;
		UUID lidOp;
		IOperation lobjOp;
		Operation lobjResult;
		boolean lbFound;

		try
		{
			lobjOwner = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), pidOwnerType), pidOwner);
			lidTopType = lobjOwner.getDefinition().getDefObject().getKey();

			if ( (lobjOwner instanceof Company) || (lobjOwner instanceof Mediator) || (lobjOwner instanceof OtherEntity) )
				lobjOwner = GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjOwner instanceof ProcessData )
			lidProc = ((ProcessData)lobjOwner).GetProcessID();
		else
			throw new BigBangException("Erro: O tipo de objecto indicado não suporta processos.");

		if ( Constants.ObjID_Company.equals(lidTopType) )
			lidOp = Constants.OPID_General_ManageCompanies;
		else if ( Constants.ObjID_Mediator.equals(lidTopType) )
			lidOp = Constants.OPID_General_ManageMediators;
		else if ( Constants.ObjID_OtherEntity.equals(lidTopType) )
			lidOp = Constants.OPID_General_ManageOtherEntities;
		else if ( Constants.ObjID_Client.equals(lidTopType) )
			lidOp = Constants.OPID_Client_ManageData;
		else if ( Constants.ObjID_QuoteRequest.equals(lidTopType) )
			lidOp = Constants.OPID_QuoteRequest_ManageData;
		else if ( Constants.ObjID_Negotiation.equals(lidTopType) )
			lidOp = Constants.OPID_Negotiation_ManageData;
		else if ( Constants.ObjID_Policy.equals(lidTopType) )
			lidOp = Constants.OPID_Policy_ManageData;
		else if ( Constants.ObjID_SubPolicy.equals(lidTopType) )
			lidOp = Constants.OPID_SubPolicy_ManageData;
		else if ( Constants.ObjID_Receipt.equals(lidTopType) )
			lidOp = Constants.OPID_Receipt_ManageData;
		else if ( Constants.ObjID_Casualty.equals(lidTopType) )
			lidOp = Constants.OPID_Casualty_ManageData;
		else if ( Constants.ObjID_Casualty.equals(lidTopType) )
			lidOp = Constants.OPID_Casualty_ManageData;
		else if ( Constants.ObjID_SubCasualty.equals(lidTopType) )
			lidOp = Constants.OPID_SubCasualty_ManageData;
		else if ( Constants.ObjID_Expense.equals(lidTopType) )
			lidOp = Constants.OPID_Expense_ManageData;
		else
			throw new BigBangException("Erro: O objecto indicado não permite movimentos de Documentos.");

		try
		{
			lobjOp = (IOperation)PNOperation.GetInstance(Engine.getCurrentNameSpace(), lidOp);
			lobjResult = lobjOp.GetNewInstance(lidProc);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lbFound = false;

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers )
		{
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).marrCreate = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).marrModify = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).marrDelete = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators )
		{
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).marrCreate = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).marrModify = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).marrDelete = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities )
		{
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities)lobjResult).marrCreate = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities)lobjResult).marrModify = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities)lobjResult).marrDelete = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.General.ManageOtherEntities)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Client.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Client.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Client.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.Client.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.Negotiation.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.Receipt.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData )
		{
			((com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData)lobjResult).mobjData = null;
			((com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData)lobjResult).mobjContactOps = null;
			((com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( !lbFound )
			throw new BigBangException("Erro: A operação pretendida não permite movimentos de Documentos.");

		return lobjResult;
	}
}
