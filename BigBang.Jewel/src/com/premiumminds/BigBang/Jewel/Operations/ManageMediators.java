package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;

public class ManageMediators
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public class MediatorData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public String mstrISPNumber;
		public String mstrFiscalNumber;
		public String mstrBankID;
		public UUID midProfile;
		public String mstrAddress1;
		public String mstrAddress2;
		public UUID midZipCode;
		public ContactOps mobjContactOps;
		public MediatorData mobjPrevValues;
	}

	public MediatorData[] marrCreate;
	public MediatorData[] marrModify;
	public MediatorData[] marrDelete;
	public ContactOps mobjContactOps;

	public ManageMediators(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageMediators;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		int i;
		Mediator lobjAux;
		Entity lrefMediators;
		Contact[] larrContacts;
		int j;

		try
		{
			if ( marrCreate != null )
			{
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(0, marrCreate[i].mstrName);
					lobjAux.setAt(1, marrCreate[i].mstrISPNumber);
					lobjAux.setAt(2, marrCreate[i].mstrFiscalNumber);
					lobjAux.setAt(3, marrCreate[i].mstrBankID);
					lobjAux.setAt(4, marrCreate[i].midProfile);
					lobjAux.setAt(5, marrCreate[i].mstrAddress1);
					lobjAux.setAt(6, marrCreate[i].mstrAddress2);
					lobjAux.setAt(7, marrCreate[i].midZipCode);
					lobjAux.SaveToDb(pdb);
					if ( marrCreate[i].mobjContactOps != null )
						marrCreate[i].mobjContactOps.RunSubOp(pdb, Constants.ObjID_Mediator, lobjAux.getKey());
					marrCreate[i].mid = lobjAux.getKey();
					marrCreate[i].mobjPrevValues = null;
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					marrModify[i].mobjPrevValues = new MediatorData();
					marrModify[i].mobjPrevValues.mid = lobjAux.getKey();
					marrModify[i].mobjPrevValues.mstrName = (String)lobjAux.getAt(0);
					marrModify[i].mobjPrevValues.mstrISPNumber = (String)lobjAux.getAt(1);
					marrModify[i].mobjPrevValues.mstrFiscalNumber = (String)lobjAux.getAt(2);
					marrModify[i].mobjPrevValues.mstrBankID = (String)lobjAux.getAt(3);
					marrModify[i].mobjPrevValues.midProfile = (UUID)lobjAux.getAt(4);
					marrModify[i].mobjPrevValues.mstrAddress1 = (String)lobjAux.getAt(5);
					marrModify[i].mobjPrevValues.mstrAddress2 = (String)lobjAux.getAt(6);
					marrModify[i].mobjPrevValues.midZipCode = (UUID)lobjAux.getAt(7);
					marrModify[i].mobjContactOps = null;
					marrModify[i].mobjPrevValues.mobjPrevValues = null;
					lobjAux.setAt(0, marrModify[i].mstrName);
					lobjAux.setAt(1, marrModify[i].mstrISPNumber);
					lobjAux.setAt(2, marrModify[i].mstrFiscalNumber);
					lobjAux.setAt(3, marrModify[i].mstrBankID);
					lobjAux.setAt(4, marrModify[i].midProfile);
					lobjAux.setAt(5, marrModify[i].mstrAddress1);
					lobjAux.setAt(6, marrModify[i].mstrAddress2);
					lobjAux.setAt(7, marrModify[i].midZipCode);
					lobjAux.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				lrefMediators = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Mediator));

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAux = Mediator.GetInstance(Engine.getCurrentNameSpace(), marrDelete[i].mid);
					marrDelete[i].mstrName = (String)lobjAux.getAt(0);
					marrDelete[i].mstrISPNumber = (String)lobjAux.getAt(1);
					marrDelete[i].mstrFiscalNumber = (String)lobjAux.getAt(2);
					marrDelete[i].mstrBankID = (String)lobjAux.getAt(3);
					marrDelete[i].midProfile = (UUID)lobjAux.getAt(4);
					marrDelete[i].mstrAddress1 = (String)lobjAux.getAt(5);
					marrDelete[i].mstrAddress2 = (String)lobjAux.getAt(6);
					marrDelete[i].midZipCode = (UUID)lobjAux.getAt(7);
					larrContacts = lobjAux.GetCurrentContacts();
					if ( (larrContacts == null) || (larrContacts.length == 0) )
						marrDelete[i].mobjContactOps = null;
					else
					{
						marrDelete[i].mobjContactOps = new ContactOps();
						marrDelete[i].mobjContactOps.marrDelete = new ContactOps.ContactData[larrContacts.length];
						for ( j = 0; j < larrContacts.length; j++ )
						{
							marrDelete[i].mobjContactOps.marrDelete[j] = marrDelete[i].mobjContactOps.new ContactData();
							marrDelete[i].mobjContactOps.marrDelete[j].mid = larrContacts[j].getKey();
						}
						marrDelete[i].mobjContactOps.RunSubOp(pdb, null, null);
					}
					marrDelete[i].mobjPrevValues = null;
					lrefMediators.Delete(pdb, marrDelete[i].mid);
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, Constants.ObjID_Company, null);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
