package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.DocInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;

public class DocOps
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public class DocumentData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public class DocInfoData
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public String mstrType;
			public String mstrValue;
		}

		public UUID mid;
		public String mstrName;
		public UUID midOwnerType;
		public UUID midOwnerId;
		public UUID midDocType;
		public String mstrText;
		public byte[] mobjFile;
		public DocInfoData[] marrInfo;
		public DocumentData mobjPrevValues;
	}

	public DocumentData[] marrCreate;
	public DocumentData[] marrModify;
	public DocumentData[] marrDelete;

	public void RunSubOp(SQLServer pdb, UUID pidOwner)
		throws BigBangJewelException
	{
		int i;

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

	private void CreateDocument(SQLServer pdb, DocumentData pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;

		lobjAux = Document.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
		try
		{
			lobjAux.setAt(0, pobjData.mstrName);
			lobjAux.setAt(1, pobjData.midOwnerType);
			lobjAux.setAt(2, pidOwner);
			lobjAux.setAt(3, pobjData.midDocType);
			lobjAux.setAt(4, pobjData.mstrText);
			lobjAux.setAt(5, pobjData.mobjFile);
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
				try
				{
					lobjAuxInfo.setAt(0, lobjAux.getKey());
					lobjAuxInfo.setAt(1, pobjData.marrInfo[i].mstrType);
					lobjAuxInfo.setAt(2, pobjData.marrInfo[i].mstrValue);
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
		}

		pobjData.mid = lobjAux.getKey();
		pobjData.midOwnerId = pidOwner;
		pobjData.mobjPrevValues = null;
	}

	private void ModifyDocument(SQLServer pdb, DocumentData pobjData)
		throws BigBangJewelException
	{
		Document lobjAux;
		DocInfo lobjAuxInfo;
		int i;
		Entity lrefDocInfo;
		DocInfo[] larrCIAux;

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

		pobjData.mobjPrevValues.mid = lobjAux.getKey();
		pobjData.mobjPrevValues.mstrName = (String)lobjAux.getAt(0);
		pobjData.mobjPrevValues.midOwnerType = (UUID)lobjAux.getAt(1);
		pobjData.mobjPrevValues.midOwnerId = (UUID)lobjAux.getAt(2);
		pobjData.mobjPrevValues.midDocType = (UUID)lobjAux.getAt(3);
		pobjData.mobjPrevValues.mstrText = (String)lobjAux.getAt(4);
		if ( lobjAux.getAt(5) instanceof FileXfer )
			pobjData.mobjPrevValues.mobjFile = ((FileXfer)lobjAux.getAt(5)).GetVarData();
		else
			pobjData.mobjPrevValues.mobjFile = (byte [])lobjAux.getAt(5);
		pobjData.mobjPrevValues.mobjPrevValues = null;

		larrCIAux = lobjAux.getCurrentInfo();
		pobjData.mobjPrevValues.marrInfo = new DocumentData.DocInfoData[larrCIAux.length];
		for ( i = 0; i < larrCIAux.length; i++ )
		{
			pobjData.mobjPrevValues.marrInfo[i].mstrType = (String)larrCIAux[i].getAt(0);
			pobjData.mobjPrevValues.marrInfo[i].mstrValue = (String)larrCIAux[i].getAt(1);
			try
			{
				lrefDocInfo.Delete(pdb, larrCIAux[i].getKey());
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		try
		{
			lobjAux.setAt(0, pobjData.mstrName);
			lobjAux.setAt(1, pobjData.midOwnerType);
			lobjAux.setAt(2, pobjData.midOwnerId);
			lobjAux.setAt(3, pobjData.midDocType);
			lobjAux.setAt(4, pobjData.mstrText);
			lobjAux.setAt(5, pobjData.mobjFile);
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
				try
				{
					lobjAuxInfo.setAt(0, lobjAux.getKey());
					lobjAuxInfo.setAt(1, pobjData.marrInfo[i].mstrType);
					lobjAuxInfo.setAt(2, pobjData.marrInfo[i].mstrValue);
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
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
			pobjData.marrInfo = new DocumentData.DocInfoData[larrCIAux.length];
			for ( i = 0; i < larrCIAux.length; i++ )
			{
				pobjData.marrInfo[i].mstrType = (String)larrCIAux[i].getAt(1);
				pobjData.marrInfo[i].mstrValue = (String)larrCIAux[i].getAt(2);
				lrefDocInfo.Delete(pdb, larrCIAux[i].getKey());
			}

			pobjData.mstrName = (String)lobjAux.getAt(0);
			pobjData.midOwnerType = (UUID)lobjAux.getAt(1);
			pobjData.midOwnerId = (UUID)lobjAux.getAt(2);
			pobjData.midDocType = (UUID)lobjAux.getAt(3);
			pobjData.mstrText = (String)lobjAux.getAt(4);
			if ( lobjAux.getAt(5) instanceof FileXfer )
				pobjData.mobjFile = ((FileXfer)lobjAux.getAt(5)).GetVarData();
			else
				pobjData.mobjFile = (byte [])lobjAux.getAt(5);
			pobjData.mobjPrevValues = null;

			lrefDocuments.Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
