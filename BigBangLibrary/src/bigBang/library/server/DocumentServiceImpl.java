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
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Objects.PNOperation;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.interfaces.DocumentService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Client.ManageClientData;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators;

public class DocumentServiceImpl
	extends EngineImplementor
	implements DocumentService
{
	private static final long serialVersionUID = 1L;

	public static DocumentData[] BuildDocTree(DocOps prefAux, Document[] parrDocuments, UUID pidParentType)
		throws BigBangJewelException
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
			larrResult[i].midOwnerType = pidParentType;
			larrResult[i].midOwnerId = (parrDocuments[i].ownerId == null ? null : UUID.fromString(parrDocuments[i].ownerId));
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

	public Document createDocument(String procId, String opId, Document document)
		throws SessionExpiredException, BigBangException
	{
		Document[] larrAux;
		DocOps lopDOps;
		Operation lobjOp;

		larrAux = new Document[1];
		larrAux[0] = document;

		lopDOps = new DocOps();
		try
		{
			lopDOps.marrCreate = BuildDocTree(lopDOps, larrAux, GetParentType(UUID.fromString(procId), UUID.fromString(opId)));
			lopDOps.marrModify = null;
			lopDOps.marrDelete = null;

			lobjOp = BuildOuterOp(UUID.fromString(procId), UUID.fromString(opId), lopDOps);
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		WalkDocTree(lopDOps.marrCreate, larrAux);
		return larrAux[0];
	}

	public Document saveDocument(String procId, String opId, Document document)
		throws SessionExpiredException, BigBangException
	{
		Document[] larrAux;
		DocOps lopDOps;
		Operation lobjOp;

		larrAux = new Document[1];
		larrAux[0] = document;

		lopDOps = new DocOps();
		try
		{
			lopDOps.marrModify = BuildDocTree(lopDOps, larrAux, GetParentType(UUID.fromString(procId), UUID.fromString(opId)));
			lopDOps.marrCreate = null;
			lopDOps.marrDelete = null;

			lobjOp = BuildOuterOp(UUID.fromString(procId), UUID.fromString(opId), lopDOps);
			lobjOp.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux[0];
	}

	public void deleteDocument(String procId, String opId, String id)
		throws SessionExpiredException, BigBangException
	{
		Document[] larrAux;
		DocOps lopDOps;
		Operation lobjOp;

		larrAux = new Document[1];
		larrAux[0] = new Document();
		larrAux[0].id = id;

		lopDOps = new DocOps();
		try
		{
			lopDOps.marrDelete = BuildDocTree(lopDOps, larrAux, GetParentType(UUID.fromString(procId), UUID.fromString(opId)));
			lopDOps.marrCreate = null;
			lopDOps.marrModify = null;

			lobjOp = BuildOuterOp(UUID.fromString(procId), UUID.fromString(opId), lopDOps);
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

	private UUID GetParentType(UUID pidProc, UUID pidOp)
		throws JewelPetriException
	{
		IOperation lobjOp;
		Operation lobjAux;
		UUID lidResult;

		lobjOp = (IOperation)PNOperation.GetInstance(Engine.getCurrentNameSpace(), pidOp);
		lobjAux = lobjOp.GetNewInstance(pidProc);

		lidResult = null;

		if ( lobjAux instanceof ManageInsurers )
			lidResult = Constants.ObjID_Company;

		if ( lobjAux instanceof ManageMediators )
			lidResult = Constants.ObjID_Mediator;

		if ( lobjAux instanceof ManageClientData )
			lidResult = Constants.ObjID_Client;

		if ( lidResult == null )
			throw new JewelPetriException("Erro: A operação pretendida não permite movimentos de Documentos.");

		return lidResult;
	}

	private Operation BuildOuterOp(UUID pidProc, UUID pidOp, DocOps pobjInner)
		throws JewelPetriException
	{
		IOperation lobjOp;
		Operation lobjResult;
		boolean lbFound;

		lobjOp = (IOperation)PNOperation.GetInstance(Engine.getCurrentNameSpace(), pidOp);
		lobjResult = lobjOp.GetNewInstance(pidProc);

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

		if ( !lbFound )
			throw new JewelPetriException("Erro: A operação pretendida não permite movimentos de Documentos.");

		return lobjResult;
	}
}
