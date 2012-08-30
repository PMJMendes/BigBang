package com.premiumminds.BigBang.Jewel.Operations;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SubOperation;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.DocInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.SysObjects.DocuShareConnector;

public class DocOps
	extends SubOperation
{
	private static final long serialVersionUID = 1L;

	public DocumentData[] marrCreate;
	public DocumentData[] marrModify;
	public DocumentData[] marrDelete;

	public void LongDesc(StringBuilder pstrResult, String pstrLineBreak)
	{
		int i;

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				pstrResult.append("Foi criado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrCreate[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram criados ");
				pstrResult.append(marrCreate.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrCreate[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				pstrResult.append("Foi modificado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrModify[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram modificados ");
				pstrResult.append(marrModify.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrModify[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				pstrResult.append("Foi apagado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrDelete[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram apagados ");
				pstrResult.append(marrDelete.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrDelete[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}
	}

	public void RunSubOp(SQLServer pdb, UUID pidOwner)
		throws JewelPetriException
	{
		int i;

		try
		{
			if ( marrCreate != null )
			{
				for ( i = 0; i < marrCreate.length; i++ )
				{
					if ( pidOwner == null )
						CreateDocument(pdb, marrCreate[i], marrCreate[i].midOwnerId);
					else
						CreateDocument(pdb, marrCreate[i], pidOwner);
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
					ModifyDocument(pdb, marrModify[i]);
			}

			if ( marrDelete != null )
			{
				for ( i = 0; i < marrDelete.length; i++ )
					DeleteDocument(pdb, marrDelete[i]);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public void UndoDesc(StringBuilder pstrResult, String pstrLineBreak)
	{
		int i;

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
				pstrResult.append("O documento criado será apagado.");
			else
				pstrResult.append("Os documentos criados serão apagados.");
			pstrResult.append(pstrLineBreak);
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			pstrResult.append("Serão repostos os valores anteriores:");
			pstrResult.append(pstrLineBreak);
			if ( marrModify.length == 1 )
				marrModify[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrModify[i].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
				pstrResult.append("O documento apagado será reposto.");
			else
				pstrResult.append("Os documentos apagados serão repostos.");
			pstrResult.append(pstrLineBreak);
		}
	}

	public void UndoLongDesc(StringBuilder pstrResult, String pstrLineBreak)
	{
		int i;

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				pstrResult.append("Foi apagado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrCreate[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram apagados ");
				pstrResult.append(marrCreate.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrCreate[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				pstrResult.append("Foi reposta a definição de 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrModify[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram repostas as definições de ");
				pstrResult.append(marrModify.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrModify[i].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				pstrResult.append("Foi reposto 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrDelete[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram repostos ");
				pstrResult.append(marrDelete.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrDelete[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}
	}

	public void UndoSubOp(SQLServer pdb, UUID pidOwner)
		throws JewelPetriException
	{
			int i;

			try
			{
				if ( marrCreate != null )
				{
					for ( i = 0; i < marrCreate.length; i++ )
						UndoCreateDocument(pdb, marrCreate[i]);
				}

				if ( marrModify != null )
				{
					for ( i = 0; i < marrModify.length; i++ )
						UndoModifyDocument(pdb, marrModify[i]);
				}

				if ( marrDelete != null )
				{
					for ( i = 0; i < marrDelete.length; i++ )
					{
						if ( pidOwner == null )
							UndoDeleteDocument(pdb, marrDelete[i], marrCreate[i].midOwnerId);
						else
							UndoDeleteDocument(pdb, marrDelete[i], pidOwner);
					}
				}
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
	}

	public UndoableOperation.UndoSet[] GetSubSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		UndoableOperation.UndoSet[] larrResult;
		int i;

		llngCreates = ( marrCreate == null ? 0 : marrCreate.length );
		llngModifies = ( marrModify == null ? 0 : marrModify.length );
		llngDeletes = ( marrDelete == null ? 0 : marrDelete.length );

		if ( llngCreates + llngModifies + llngDeletes == 0 )
			return new UndoableOperation.UndoSet[0];

		larrResult = new UndoableOperation.UndoSet[1];
		larrResult[0] = new UndoableOperation.UndoSet();
		larrResult[0].midType = Constants.ObjID_Document;
		larrResult[0].marrDeleted = new UUID[llngCreates];
		larrResult[0].marrChanged = new UUID[llngModifies];
		larrResult[0].marrCreated = new UUID[llngDeletes];

		for ( i = 0; i < llngCreates; i ++ )
			larrResult[0].marrDeleted[i] = marrCreate[0].mid;

		for ( i = 0; i < llngModifies; i ++ )
			larrResult[0].marrChanged[i] = marrModify[0].mid;

		for ( i = 0; i < llngDeletes; i ++ )
			larrResult[0].marrCreated[i] = marrDelete[0].mid;

		return larrResult;
	}

	private void CreateDocument(SQLServer pdb, DocumentData pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;

		if ( pobjData.mobjDSBridge != null )
		{
			pobjData.mobjFile = DocuShareConnector.getItemAsFile(pobjData.mobjDSBridge.mstrDSHandle).GetVarData();
			pobjData.mobjDSBridge.mstrDSTitle = DocuShareConnector.getItemTitle(pobjData.mobjDSBridge.mstrDSHandle);
		}

		lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
		pobjData.midOwnerId = pidOwner;
		if ( pobjData.mdtRefDate == null )
			pobjData.mdtRefDate = new Timestamp(new java.util.Date().getTime());
		pobjData.ToObject(lobjAux);
		try
		{
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		pobjData.mid = lobjAux.getKey();

		if ( pobjData.marrInfo != null )
		{
			for ( i = 0; i < pobjData.marrInfo.length; i++ )
			{
				lobjAuxInfo = DocInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				pobjData.marrInfo[i].midOwner = pobjData.mid;
				pobjData.marrInfo[i].ToObject(lobjAuxInfo);
				try
				{
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
				pobjData.marrInfo[i].mid = lobjAuxInfo.getKey();
			}
		}

		pobjData.mobjPrevValues = null;

		if ( pobjData.mobjDSBridge != null )
		{
			if ( pobjData.mobjDSBridge.mbDelete )
				DocuShareConnector.deleteItem(pobjData.mobjDSBridge.mstrDSHandle);
			else
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, pobjData.mobjDSBridge.mstrDSLoc, null);
		}
	}

	private void ModifyDocument(SQLServer pdb, DocumentData pobjData)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;
		Entity lrefDocInfo;
		DocInfo[] larrCIAux;

		if ( pobjData.mobjDSBridge != null )
		{
			pobjData.mobjFile = DocuShareConnector.getItemAsFile(pobjData.mobjDSBridge.mstrDSHandle).GetVarData();
			pobjData.mobjDSBridge.mstrDSTitle = DocuShareConnector.getItemTitle(pobjData.mobjDSBridge.mstrDSHandle);
		}

		try
		{
			lrefDocInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_DocInfo));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

		pobjData.mobjPrevValues = new DocumentData();
		pobjData.mobjPrevValues.FromObject(lobjAux);
		pobjData.mobjPrevValues.mobjPrevValues = null;

		larrCIAux = lobjAux.getCurrentInfo();
		pobjData.mobjPrevValues.marrInfo = new DocInfoData[larrCIAux.length];
		for ( i = 0; i < larrCIAux.length; i++ )
		{
			pobjData.mobjPrevValues.marrInfo[i] = new DocInfoData();
			pobjData.mobjPrevValues.marrInfo[i].FromObject(larrCIAux[i]);
			try
			{
				lrefDocInfo.Delete(pdb, larrCIAux[i].getKey());
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		if ( pobjData.mdtRefDate == null )
			pobjData.mdtRefDate = new Timestamp(new java.util.Date().getTime());
		pobjData.ToObject(lobjAux);
		try
		{
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( pobjData.marrInfo != null )
		{
			for ( i = 0; i < pobjData.marrInfo.length; i++ )
			{
				lobjAuxInfo = DocInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				pobjData.marrInfo[i].midOwner = pobjData.mid;
				pobjData.marrInfo[i].ToObject(lobjAuxInfo);
				try
				{
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
				pobjData.marrInfo[i].mid = lobjAuxInfo.getKey();
			}
		}

		if ( pobjData.mobjDSBridge != null )
		{
			if ( pobjData.mobjDSBridge.mbDelete )
				DocuShareConnector.deleteItem(pobjData.mobjDSBridge.mstrDSHandle);
			else
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, pobjData.mobjDSBridge.mstrDSLoc, null);
		}
	}

	private void DeleteDocument(SQLServer pdb, DocumentData pobjData)
		throws BigBangJewelException
	{
		Document lobjAux;
		Entity lrefDocuments;
		Entity lrefDocInfo;
		int i;
		DocInfo[] larrCIAux;

		try
		{
			lrefDocuments = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Document));
			lrefDocInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_DocInfo));

			lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

			larrCIAux = lobjAux.getCurrentInfo();
			pobjData.marrInfo = new DocInfoData[larrCIAux.length];
			for ( i = 0; i < larrCIAux.length; i++ )
			{
				pobjData.marrInfo[i] = new DocInfoData();
				pobjData.marrInfo[i].FromObject(larrCIAux[i]);
				lrefDocInfo.Delete(pdb, larrCIAux[i].getKey());
			}

			pobjData.FromObject(lobjAux);
			pobjData.mobjPrevValues = null;

			lrefDocuments.Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoCreateDocument(SQLServer pdb, DocumentData pobjData)
		throws BigBangJewelException
	{
		Document lobjAux;
		Entity lrefDocuments;
		Entity lrefDocInfo;
		int i;

		if ( pobjData.mobjDSBridge != null )
		{
			if ( pobjData.mobjDSBridge.mbDelete )
				DocuShareConnector.createItem(new FileXfer(pobjData.mobjFile), pobjData.mobjDSBridge.mstrDSTitle,
						pobjData.mobjDSBridge.mstrDSLoc);
			else
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, null, pobjData.mobjDSBridge.mstrDSLoc);
		}

		try
		{
			lrefDocuments = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Document));
			lrefDocInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_DocInfo));

			lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

			if ( pobjData.marrInfo != null )
			{
				for ( i = 0; i < pobjData.marrInfo.length; i++ )
					lrefDocInfo.Delete(pdb, pobjData.marrInfo[i].mid);
			}

			lrefDocuments.Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoModifyDocument(SQLServer pdb, DocumentData pobjData)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;
		Entity lrefDocInfo;

		if ( pobjData.mobjDSBridge != null )
		{
			if ( pobjData.mobjDSBridge.mbDelete )
				DocuShareConnector.createItem(new FileXfer(pobjData.mobjFile), pobjData.mobjDSBridge.mstrDSTitle,
						pobjData.mobjDSBridge.mstrDSLoc);
			else
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, null, pobjData.mobjDSBridge.mstrDSLoc);
		}

		try
		{
			lrefDocInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_DocInfo));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

		for ( i = 0; i < pobjData.marrInfo.length; i++ )
		{
			try
			{
				lrefDocInfo.Delete(pdb, pobjData.marrInfo[i].mid);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		pobjData.mobjPrevValues.ToObject(lobjAux);
		try
		{
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( pobjData.mobjPrevValues.marrInfo != null )
		{
			for ( i = 0; i < pobjData.mobjPrevValues.marrInfo.length; i++ )
			{
				lobjAuxInfo = DocInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				pobjData.mobjPrevValues.marrInfo[i].midOwner = pobjData.mid; 
				pobjData.mobjPrevValues.marrInfo[i].ToObject(lobjAuxInfo);
				try
				{
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
				pobjData.mobjPrevValues.marrInfo[i].mid = lobjAuxInfo.getKey(); 
			}
		}
	}

	private void UndoDeleteDocument(SQLServer pdb, DocumentData pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;

		lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
		pobjData.midOwnerId = pidOwner;
		pobjData.ToObject(lobjAux);
		try
		{
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		pobjData.mid = lobjAux.getKey();

		if ( pobjData.marrInfo != null )
		{
			for ( i = 0; i < pobjData.marrInfo.length; i++ )
			{
				lobjAuxInfo = DocInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				pobjData.marrInfo[i].midOwner = pobjData.mid;
  				pobjData.marrInfo[i].ToObject(lobjAuxInfo);
				try
				{
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
				pobjData.marrInfo[i].mid = lobjAuxInfo.getKey(); 
			}
		}
	}
}
