package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Client.ManageClientData;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ManagePolicyData;

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
			larrResult[i].mstrText = parrDocuments[i].text;
			if ( parrDocuments[i].fileStorageId != null )
				larrResult[i].mobjFile = FileServiceImpl.GetFileXferStorage().
						get(UUID.fromString(parrDocuments[i].fileStorageId)).GetVarData();
			else
				larrResult[i].mobjFile = null;
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
						.GetInstance(Engine.getCurrentNameSpace(), lrsDocs)));
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

		lobjOp = BuildOuterOp(GetOwnerProc(UUID.fromString(document.ownerTypeId), UUID.fromString(document.ownerId)),
				GetOpType(UUID.fromString(document.ownerTypeId)), lopDOps);

		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		WalkDocTree(lopDOps.marrCreate, larrAux);
		return larrAux[0];
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

		lobjOp = BuildOuterOp(GetOwnerProc(UUID.fromString(document.ownerTypeId), UUID.fromString(document.ownerId)),
				GetOpType(UUID.fromString(document.ownerTypeId)), lopDOps);
		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux[0];
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

		lobjOp = BuildOuterOp(GetOwnerProc(lobjData.midOwnerType, lobjData.midOwnerId),
				GetOpType(lobjData.midOwnerType), lopDOps);

		try
		{
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private Document fromServer(com.premiumminds.BigBang.Jewel.Objects.Document pobjDocument)
		throws BigBangException
	{
		Document lobjAux;
		com.premiumminds.BigBang.Jewel.Objects.DocInfo[] larrInfo;
		int i;
		FileXfer laux;
		UUID lidAux;

		lobjAux = new Document();

		lobjAux.id = pobjDocument.getKey().toString();
		lobjAux.name = (String)pobjDocument.getAt(0);
		lobjAux.ownerTypeId = ((UUID)pobjDocument.getAt(1)).toString();
		lobjAux.ownerId = ((UUID)pobjDocument.getAt(2)).toString();
		lobjAux.docTypeId = ((UUID)pobjDocument.getAt(3)).toString();
		lobjAux.text = (String)pobjDocument.getAt(4);
		if ( pobjDocument.getAt(5) == null )
		{
			lobjAux.mimeType = null;
			lobjAux.fileName = null;
			lobjAux.fileStorageId = null;
		}
		else
		{
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

	private DocInfo fromServer(com.premiumminds.BigBang.Jewel.Objects.DocInfo pobjDocInfo)
	{
		DocInfo lobjAux;

		lobjAux = new DocInfo();

		lobjAux.name = (String)pobjDocInfo.getAt(1);
		lobjAux.value = (String)pobjDocInfo.getAt(2);
		return lobjAux;
	}

	private UUID GetOwnerProc(UUID pidOwnerType, UUID pidOwner)
		throws BigBangException
	{
		ObjectBase lobjOwner;

		try
		{
			if ( Constants.ObjID_Company.equals(pidOwnerType) || Constants.ObjID_Mediator.equals(pidOwnerType) )
				return GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID();

			lobjOwner = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), pidOwnerType), pidOwner);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjOwner instanceof ProcessData )
			return ((ProcessData)lobjOwner).GetProcessID();

		throw new BigBangException("Erro: O tipo de objecto indicado não suporta processos.");
	}

	private UUID GetOpType(UUID pidOwnerType)
		throws BigBangException
	{
		if ( Constants.ObjID_Company.equals(pidOwnerType) )
			return Constants.OPID_ManageCompanies;

		if ( Constants.ObjID_Mediator.equals(pidOwnerType) )
			return Constants.OPID_ManageMediators;

		if ( Constants.ObjID_Client.equals(pidOwnerType) )
			return Constants.OPID_ManageClientData;

		if ( Constants.ObjID_Policy.equals(pidOwnerType) )
			return Constants.OPID_ManagePolicyData;

		throw new BigBangException("Erro: O objecto indicado não permite movimentos de Documentos.");
	}

	private Operation BuildOuterOp(UUID pidProc, UUID pidOp, DocOps pobjInner)
		throws BigBangException
	{
		IOperation lobjOp;
		Operation lobjResult;
		boolean lbFound;

		try
		{
			lobjOp = (IOperation)PNOperation.GetInstance(Engine.getCurrentNameSpace(), pidOp);
			lobjResult = lobjOp.GetNewInstance(pidProc);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lbFound = false;

		if ( lobjResult instanceof ManageInsurers )
		{
			((ManageInsurers)lobjResult).marrCreate = null;
			((ManageInsurers)lobjResult).marrModify = null;
			((ManageInsurers)lobjResult).marrDelete = null;
			((ManageInsurers)lobjResult).mobjContactOps = null;
			((ManageInsurers)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof ManageMediators )
		{
			((ManageMediators)lobjResult).marrCreate = null;
			((ManageMediators)lobjResult).marrModify = null;
			((ManageMediators)lobjResult).marrDelete = null;
			((ManageMediators)lobjResult).mobjContactOps = null;
			((ManageMediators)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof ManageClientData )
		{
			((ManageClientData)lobjResult).mobjData = null;
			((ManageClientData)lobjResult).mobjContactOps = null;
			((ManageClientData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( lobjResult instanceof ManagePolicyData )
		{
			((ManagePolicyData)lobjResult).mobjData = null;
			((ManagePolicyData)lobjResult).mobjContactOps = null;
			((ManagePolicyData)lobjResult).mobjDocOps = pobjInner;
			lbFound = true;
		}

		if ( !lbFound )
			throw new BigBangException("Erro: A operação pretendida não permite movimentos de Documentos.");

		return lobjResult;
	}
}
