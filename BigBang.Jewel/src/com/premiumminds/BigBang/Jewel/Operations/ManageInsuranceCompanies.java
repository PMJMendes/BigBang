package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Company;

public class ManageInsuranceCompanies
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public class CompanyData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
	
		public UUID mid;
		public String mstrName;
		public String mstrAcronym;
		public String mstrISPNumber;
		public String mstrMedCode;
		public String mstrFiscalNumber;
		public String mstrBankID;
		public String mstrAddress1;
		public String mstrAddress2;
		public UUID midZipCode;
	}

	public CompanyData[] marrCreate;
	public CompanyData[] marrModify;
	public CompanyData[] marrDelete;

	public UUID[] marrNewIDs;

	public ManageInsuranceCompanies(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageCompanies;
	}

	protected void Run()
		throws JewelPetriException
	{
		int i;
		MasterDB ldb;
		Company lobjAux;
		Entity lrefCostCenters;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			if ( marrCreate != null )
			{
				marrNewIDs = new UUID[marrCreate.length];

				for ( i = 0; i < marrCreate.length; i++ )
				{
					lobjAux = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(0, marrCreate[i].mstrName);
					lobjAux.setAt(1, marrCreate[i].mstrAcronym);
					lobjAux.setAt(2, marrCreate[i].mstrISPNumber);
					lobjAux.setAt(3, marrCreate[i].mstrMedCode);
					lobjAux.setAt(4, marrCreate[i].mstrFiscalNumber);
					lobjAux.setAt(5, marrCreate[i].mstrBankID);
					lobjAux.setAt(6, marrCreate[i].mstrAddress1);
					lobjAux.setAt(7, marrCreate[i].mstrAddress2);
					lobjAux.setAt(8, marrCreate[i].midZipCode);
					lobjAux.SaveToDb(ldb);
					marrNewIDs[i] = lobjAux.getKey();
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = Company.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					lobjAux.setAt(0, marrModify[i].mstrName);
					lobjAux.setAt(1, marrModify[i].mstrAcronym);
					lobjAux.setAt(2, marrModify[i].mstrISPNumber);
					lobjAux.setAt(3, marrModify[i].mstrMedCode);
					lobjAux.setAt(4, marrModify[i].mstrFiscalNumber);
					lobjAux.setAt(5, marrModify[i].mstrBankID);
					lobjAux.setAt(6, marrModify[i].mstrAddress1);
					lobjAux.setAt(7, marrModify[i].mstrAddress2);
					lobjAux.setAt(8, marrModify[i].midZipCode);
					lobjAux.SaveToDb(ldb);
				}
			}

			if ( marrDelete != null )
			{
				lrefCostCenters = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Company));

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lrefCostCenters.Delete(ldb, marrDelete[i].mid);
				}
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
