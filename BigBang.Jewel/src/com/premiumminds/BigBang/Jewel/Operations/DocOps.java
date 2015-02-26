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
import com.premiumminds.BigBang.Jewel.Data.DocDataFull;
import com.premiumminds.BigBang.Jewel.Data.DocDataHeavy;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.DocInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.SysObjects.DocuShareConnector;

public class DocOps
	extends SubOperation
{
	private static final long serialVersionUID = 1L;

	private DocumentData[] marrCreate;
	private DocumentData[] marrModify;
	private DocumentData[] marrDelete;

	public DocDataLight[] marrCreate2;
	public DocDataFull[] marrModify2;
	public DocDataHeavy[] marrDelete2;

	public void fromOld()
	{
		int i;

		if ( (marrCreate != null) && (marrCreate2 == null) )
		{
			marrCreate2 = new DocDataLight[marrCreate.length];
			for ( i = 0; i < marrCreate.length; i++ )
			{
				marrCreate2[i] = new DocDataLight();
				marrCreate2[i].FromOld(marrCreate[i]);
			}
			marrCreate = null;
		}

		if ( (marrModify != null) && (marrModify2 == null)  )
		{
			marrModify2 = new DocDataFull[marrModify.length];
			for ( i = 0; i < marrModify.length; i++ )
			{
				marrModify2[i] = new DocDataFull();
				marrModify2[i].FromOld(marrModify[i]);
			}
			marrModify = null;
		}

		if ( (marrDelete != null) && (marrDelete2 == null)  )
		{
			marrDelete2 = new DocDataHeavy[marrDelete.length];
			for ( i = 0; i < marrDelete.length; i++ )
			{
				marrDelete2[i] = new DocDataHeavy();
				marrDelete2[i].FromOld(marrDelete[i]);
			}
			marrDelete = null;
		}
	}
	public void LongDesc(StringBuilder pstrResult, String pstrLineBreak)
	{
		int i;

		fromOld();

//		if ( (marrCreate != null) && (marrCreate.length > 0) )
//		{
//			if ( marrCreate.length == 1 )
//			{
//				pstrResult.append("Foi criado 1 documento:");
//				pstrResult.append(pstrLineBreak);
//				marrCreate[0].Describe(pstrResult, pstrLineBreak);
//			}
//			else
//			{
//				pstrResult.append("Foram criados ");
//				pstrResult.append(marrCreate.length);
//				pstrResult.append(" documentos:");
//				pstrResult.append(pstrLineBreak);
//				for ( i = 0; i < marrCreate.length; i++ )
//				{
//					pstrResult.append("Documento ");
//					pstrResult.append(i + 1);
//					pstrResult.append(":");
//					pstrResult.append(pstrLineBreak);
//					marrCreate[i].Describe(pstrResult, pstrLineBreak);
//				}
//			}
//		}

		if ( (marrCreate2 != null) && (marrCreate2.length > 0) )
		{
			if ( marrCreate2.length == 1 )
			{
				pstrResult.append("Foi criado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrCreate2[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram criados ");
				pstrResult.append(marrCreate2.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate2.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrCreate2[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

//		if ( (marrModify != null) && (marrModify.length > 0) )
//		{
//			if ( marrModify.length == 1 )
//			{
//				pstrResult.append("Foi modificado 1 documento:");
//				pstrResult.append(pstrLineBreak);
//				marrModify[0].Describe(pstrResult, pstrLineBreak);
//			}
//			else
//			{
//				pstrResult.append("Foram modificados ");
//				pstrResult.append(marrModify.length);
//				pstrResult.append(" documentos:");
//				pstrResult.append(pstrLineBreak);
//				for ( i = 0; i < marrModify.length; i++ )
//				{
//					pstrResult.append("Documento ");
//					pstrResult.append(i + 1);
//					pstrResult.append(":");
//					pstrResult.append(pstrLineBreak);
//					marrModify[i].Describe(pstrResult, pstrLineBreak);
//				}
//			}
//		}

		if ( (marrModify2 != null) && (marrModify2.length > 0) )
		{
			if ( marrModify2.length == 1 )
			{
				pstrResult.append("Foi modificado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrModify2[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram modificados ");
				pstrResult.append(marrModify2.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify2.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrModify2[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

//		if ( (marrDelete != null) && (marrDelete.length > 0) )
//		{
//			if ( marrDelete.length == 1 )
//			{
//				pstrResult.append("Foi apagado 1 documento:");
//				pstrResult.append(pstrLineBreak);
//				marrDelete[0].Describe(pstrResult, pstrLineBreak);
//			}
//			else
//			{
//				pstrResult.append("Foram apagados ");
//				pstrResult.append(marrDelete.length);
//				pstrResult.append(" documentos:");
//				pstrResult.append(pstrLineBreak);
//				for ( i = 0; i < marrDelete.length; i++ )
//				{
//					pstrResult.append("Documento ");
//					pstrResult.append(i + 1);
//					pstrResult.append(":");
//					pstrResult.append(pstrLineBreak);
//					marrDelete[i].Describe(pstrResult, pstrLineBreak);
//				}
//			}
//		}

		if ( (marrDelete2 != null) && (marrDelete2.length > 0) )
		{
			if ( marrDelete2.length == 1 )
			{
				pstrResult.append("Foi apagado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrDelete2[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram apagados ");
				pstrResult.append(marrDelete2.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete2.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrDelete2[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}
	}

	public void RunSubOp(SQLServer pdb, UUID pidOwner)
		throws JewelPetriException
	{
		int i;

		fromOld();

		try
		{
//			if ( marrCreate != null )
//			{
//				for ( i = 0; i < marrCreate.length; i++ )
//				{
//					if ( pidOwner == null )
//						CreateDocument(pdb, marrCreate[i], marrCreate[i].midOwnerId);
//					else
//						CreateDocument(pdb, marrCreate[i], pidOwner);
//				}
//			}

			if ( marrCreate2 != null )
			{
				for ( i = 0; i < marrCreate2.length; i++ )
				{
					if ( pidOwner == null )
						CreateDocument(pdb, marrCreate2[i], marrCreate2[i].midOwnerId);
					else
						CreateDocument(pdb, marrCreate2[i], pidOwner);
				}
			}

//			if ( marrModify != null )
//			{
//				for ( i = 0; i < marrModify.length; i++ )
//					ModifyDocument(pdb, marrModify[i]);
//			}

			if ( marrModify2 != null )
			{
				for ( i = 0; i < marrModify2.length; i++ )
					ModifyDocument(pdb, marrModify2[i]);
			}

//			if ( marrDelete != null )
//			{
//				for ( i = 0; i < marrDelete.length; i++ )
//					DeleteDocument(pdb, marrDelete[i]);
//			}

			if ( marrDelete2 != null )
			{
				for ( i = 0; i < marrDelete2.length; i++ )
					DeleteDocument(pdb, marrDelete2[i]);
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

		fromOld();

//		if ( (marrCreate != null) && (marrCreate.length > 0) )
//		{
//			if ( marrCreate.length == 1 )
//				pstrResult.append("O documento criado será apagado.");
//			else
//				pstrResult.append("Os documentos criados serão apagados.");
//			pstrResult.append(pstrLineBreak);
//		}

		if ( (marrCreate2 != null) && (marrCreate2.length > 0) )
		{
			if ( marrCreate2.length == 1 )
				pstrResult.append("O documento criado será apagado.");
			else
				pstrResult.append("Os documentos criados serão apagados.");
			pstrResult.append(pstrLineBreak);
		}

//		if ( (marrModify != null) && (marrModify.length > 0) )
//		{
//			pstrResult.append("Serão repostos os valores anteriores:");
//			pstrResult.append(pstrLineBreak);
//			if ( marrModify.length == 1 )
//				marrModify[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
//			else
//			{
//				for ( i = 0; i < marrModify.length; i++ )
//				{
//					pstrResult.append("Documento ");
//					pstrResult.append(i + 1);
//					pstrResult.append(":");
//					pstrResult.append(pstrLineBreak);
//					marrModify[i].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
//				}
//			}
//		}

		if ( (marrModify2 != null) && (marrModify2.length > 0) )
		{
			pstrResult.append("Serão repostos os valores anteriores:");
			pstrResult.append(pstrLineBreak);
			if ( marrModify2.length == 1 )
				marrModify2[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify2.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrModify2[i].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
				}
			}
		}

//		if ( (marrDelete != null) && (marrDelete.length > 0) )
//		{
//			if ( marrDelete.length == 1 )
//				pstrResult.append("O documento apagado será reposto.");
//			else
//				pstrResult.append("Os documentos apagados serão repostos.");
//			pstrResult.append(pstrLineBreak);
//		}

		if ( (marrDelete2 != null) && (marrDelete2.length > 0) )
		{
			if ( marrDelete2.length == 1 )
				pstrResult.append("O documento apagado será reposto.");
			else
				pstrResult.append("Os documentos apagados serão repostos.");
			pstrResult.append(pstrLineBreak);
		}
	}

	public void UndoLongDesc(StringBuilder pstrResult, String pstrLineBreak)
	{
		int i;

		fromOld();

//		if ( (marrCreate != null) && (marrCreate.length > 0) )
//		{
//			if ( marrCreate.length == 1 )
//			{
//				pstrResult.append("Foi apagado 1 documento:");
//				pstrResult.append(pstrLineBreak);
//				marrCreate[0].Describe(pstrResult, pstrLineBreak);
//			}
//			else
//			{
//				pstrResult.append("Foram apagados ");
//				pstrResult.append(marrCreate.length);
//				pstrResult.append(" documentos:");
//				pstrResult.append(pstrLineBreak);
//				for ( i = 0; i < marrCreate.length; i++ )
//				{
//					pstrResult.append("Documento ");
//					pstrResult.append(i + 1);
//					pstrResult.append(":");
//					pstrResult.append(pstrLineBreak);
//					marrCreate[i].Describe(pstrResult, pstrLineBreak);
//				}
//			}
//		}

		if ( (marrCreate2 != null) && (marrCreate2.length > 0) )
		{
			if ( marrCreate2.length == 1 )
			{
				pstrResult.append("Foi apagado 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrCreate2[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram apagados ");
				pstrResult.append(marrCreate2.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate2.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrCreate2[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

//		if ( (marrModify != null) && (marrModify.length > 0) )
//		{
//			if ( marrModify.length == 1 )
//			{
//				pstrResult.append("Foi reposta a definição de 1 documento:");
//				pstrResult.append(pstrLineBreak);
//				marrModify[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
//			}
//			else
//			{
//				pstrResult.append("Foram repostas as definições de ");
//				pstrResult.append(marrModify.length);
//				pstrResult.append(" documentos:");
//				pstrResult.append(pstrLineBreak);
//				for ( i = 0; i < marrModify.length; i++ )
//				{
//					pstrResult.append("Documento ");
//					pstrResult.append(i + 1);
//					pstrResult.append(":");
//					pstrResult.append(pstrLineBreak);
//					marrModify[i].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
//				}
//			}
//		}

		if ( (marrModify2 != null) && (marrModify2.length > 0) )
		{
			if ( marrModify2.length == 1 )
			{
				pstrResult.append("Foi reposta a definição de 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrModify2[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram repostas as definições de ");
				pstrResult.append(marrModify2.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify2.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrModify2[i].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
				}
			}
		}

//		if ( (marrDelete != null) && (marrDelete.length > 0) )
//		{
//			if ( marrDelete.length == 1 )
//			{
//				pstrResult.append("Foi reposto 1 documento:");
//				pstrResult.append(pstrLineBreak);
//				marrDelete[0].Describe(pstrResult, pstrLineBreak);
//			}
//			else
//			{
//				pstrResult.append("Foram repostos ");
//				pstrResult.append(marrDelete.length);
//				pstrResult.append(" documentos:");
//				pstrResult.append(pstrLineBreak);
//				for ( i = 0; i < marrDelete.length; i++ )
//				{
//					pstrResult.append("Documento ");
//					pstrResult.append(i + 1);
//					pstrResult.append(":");
//					pstrResult.append(pstrLineBreak);
//					marrDelete[i].Describe(pstrResult, pstrLineBreak);
//				}
//			}
//		}

		if ( (marrDelete2 != null) && (marrDelete2.length > 0) )
		{
			if ( marrDelete2.length == 1 )
			{
				pstrResult.append("Foi reposto 1 documento:");
				pstrResult.append(pstrLineBreak);
				marrDelete2[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram repostos ");
				pstrResult.append(marrDelete2.length);
				pstrResult.append(" documentos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete2.length; i++ )
				{
					pstrResult.append("Documento ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					marrDelete2[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}
	}

	public void UndoSubOp(SQLServer pdb, UUID pidOwner)
		throws JewelPetriException
	{
			int i;

			fromOld();

			try
			{
//				if ( marrCreate != null )
//				{
//					for ( i = 0; i < marrCreate.length; i++ )
//						UndoCreateDocument(pdb, marrCreate[i]);
//				}

				if ( marrCreate2 != null )
				{
					for ( i = 0; i < marrCreate2.length; i++ )
						UndoCreateDocument(pdb, marrCreate2[i]);
				}

//				if ( marrModify != null )
//				{
//					for ( i = 0; i < marrModify.length; i++ )
//						UndoModifyDocument(pdb, marrModify[i]);
//				}

				if ( marrModify2 != null )
				{
					for ( i = 0; i < marrModify2.length; i++ )
						UndoModifyDocument(pdb, marrModify2[i]);
				}

//				if ( marrDelete != null )
//				{
//					for ( i = 0; i < marrDelete.length; i++ )
//					{
//						if ( pidOwner == null )
//							UndoDeleteDocument(pdb, marrDelete[i], marrDelete[i].midOwnerId);
//						else
//							UndoDeleteDocument(pdb, marrDelete[i], pidOwner);
//					}
//				}

				if ( marrDelete2 != null )
				{
					for ( i = 0; i < marrDelete2.length; i++ )
					{
						if ( pidOwner == null )
							UndoDeleteDocument(pdb, marrDelete2[i], marrDelete2[i].midOwnerId);
						else
							UndoDeleteDocument(pdb, marrDelete2[i], pidOwner);
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
		int i, j;

		fromOld();

		llngCreates = /*( marrCreate == null ? 0 : marrCreate.length ) +*/ ( marrCreate2 == null ? 0 : marrCreate2.length );
		llngModifies = /*( marrModify == null ? 0 : marrModify.length ) +*/ ( marrModify2 == null ? 0 : marrModify2.length );
		llngDeletes = /*( marrDelete == null ? 0 : marrDelete.length ) +*/ ( marrDelete2 == null ? 0 : marrDelete2.length );

		if ( llngCreates + llngModifies + llngDeletes == 0 )
			return new UndoableOperation.UndoSet[0];

		larrResult = new UndoableOperation.UndoSet[1];
		larrResult[0] = new UndoableOperation.UndoSet();
		larrResult[0].midType = Constants.ObjID_Document;
		larrResult[0].marrDeleted = new UUID[llngCreates];
		larrResult[0].marrChanged = new UUID[llngModifies];
		larrResult[0].marrCreated = new UUID[llngDeletes];

		i = 0;
//		if ( marrCreate != null )
//		{
//			for ( j = 0; j < marrCreate.length; i++, j++ )
//				larrResult[0].marrDeleted[i] = marrCreate[j].mid;
//		}
		if ( marrCreate2 != null )
		{
			for ( j = 0; j < marrCreate2.length; i++, j++ )
				larrResult[0].marrDeleted[i] = marrCreate2[j].mid;
		}

		i = 0;
//		if ( marrModify != null )
//		{
//			for ( j = 0; j < marrModify.length; i++, j++ )
//				larrResult[0].marrChanged[i] = marrModify[j].mid;
//		}
		if ( marrModify2 != null )
		{
			for ( j = 0; j < marrModify2.length; i++, j++ )
				larrResult[0].marrChanged[i] = marrModify2[j].mid;
		}

		i = 0;
//		if ( marrDelete != null )
//		{
//			for ( j = 0; j < marrDelete.length; i++, j++ )
//				larrResult[0].marrCreated[i] = marrDelete[j].mid;
//		}
		if ( marrDelete2 != null )
		{
			for ( j = 0; j < marrDelete2.length; i++, j++ )
				larrResult[0].marrCreated[i] = marrDelete2[j].mid;
		}

		return larrResult;
	}
/*
	private void CreateDocument(SQLServer pdb, DocumentData pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;

		if ( (pobjData.mobjDSBridge != null) && !pobjData.mobjDSBridge.mbHandled )
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

		if ( (pobjData.mobjDSBridge != null)  && !pobjData.mobjDSBridge.mbHandled )
		{
			if ( pobjData.mobjDSBridge.mbDelete )
				DocuShareConnector.deleteItem(pobjData.mobjDSBridge.mstrDSHandle);
			else
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, pobjData.mobjDSBridge.mstrDSLoc, null, false);
			pobjData.mobjDSBridge.mbHandled = true;
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
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, pobjData.mobjDSBridge.mstrDSLoc, null, false);
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
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, null, pobjData.mobjDSBridge.mstrDSLoc, true);
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
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, null, pobjData.mobjDSBridge.mstrDSLoc, true);
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
*/
	private void CreateDocument(SQLServer pdb, DocDataLight pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;

		if ( (pobjData.mobjDSBridge != null) && !pobjData.mobjDSBridge.mbHandled )
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

		if ( (pobjData.mobjDSBridge != null)  && !pobjData.mobjDSBridge.mbHandled )
		{
			if ( pobjData.mobjDSBridge.mbDelete )
				DocuShareConnector.deleteItem(pobjData.mobjDSBridge.mstrDSHandle);
			else
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, pobjData.mobjDSBridge.mstrDSLoc, null, false);
			pobjData.mobjDSBridge.mbHandled = true;
		}
	}

	private void ModifyDocument(SQLServer pdb, DocDataFull pobjData)
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

		pobjData.mobjPrevValues = new DocDataHeavy();
		pobjData.mobjPrevValues.FromObject(lobjAux);

		larrCIAux = lobjAux.getCurrentInfo(pdb);
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
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, pobjData.mobjDSBridge.mstrDSLoc, null, false);
		}
	}

	private void DeleteDocument(SQLServer pdb, DocDataHeavy pobjData)
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

			larrCIAux = lobjAux.getCurrentInfo(pdb);
			pobjData.marrInfo = new DocInfoData[larrCIAux.length];
			for ( i = 0; i < larrCIAux.length; i++ )
			{
				pobjData.marrInfo[i] = new DocInfoData();
				pobjData.marrInfo[i].FromObject(larrCIAux[i]);
				lrefDocInfo.Delete(pdb, larrCIAux[i].getKey());
			}

			pobjData.FromObject(lobjAux);

			lrefDocuments.Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoCreateDocument(SQLServer pdb, DocDataLight pobjData)
		throws BigBangJewelException
	{
		Document lobjAux;
		FileXfer lobjFile;
		Entity lrefDocuments;
		Entity lrefDocInfo;
		int i;

		lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

		if ( pobjData.mobjDSBridge != null )
		{
			if ( pobjData.mobjDSBridge.mbDelete )
			{
				if ( pobjData.mobjFile == null )
					lobjFile = lobjAux.getFile();
				else
					lobjFile = new FileXfer(pobjData.mobjFile);
				if ( lobjFile != null )
					DocuShareConnector.createItem(lobjFile, pobjData.mobjDSBridge.mstrDSTitle, pobjData.mobjDSBridge.mstrDSLoc);
			}
			else
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, null, pobjData.mobjDSBridge.mstrDSLoc, true);
		}

		try
		{
			lrefDocuments = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Document));
			lrefDocInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_DocInfo));

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

	private void UndoModifyDocument(SQLServer pdb, DocDataFull pobjData)
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
				DocuShareConnector.moveItem(pobjData.mobjDSBridge.mstrDSHandle, null, pobjData.mobjDSBridge.mstrDSLoc, true);
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

	private void UndoDeleteDocument(SQLServer pdb, DocDataHeavy pobjData, UUID pidOwner)
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
