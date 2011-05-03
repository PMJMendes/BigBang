package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SubOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;

public class ContactOps
	extends SubOperation
{
	private static final long serialVersionUID = 1L;

	public class ContactData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public class ContactInfoData
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public UUID midType;
			public String mstrValue;
		}

		public UUID mid;
		public String mstrName;
		public UUID midOwnerType;
		public UUID midOwnerId;
		public String mstrAddress1;
		public String mstrAddress2;
		public UUID midZipCode;
		public ContactInfoData[] marrInfo;
		public ContactData[] marrSubContacts;
		public ContactData mobjPrevValues;
	}

	public ContactData[] marrCreate;
	public ContactData[] marrModify;
	public ContactData[] marrDelete;

	public void LongDesc(StringBuilder pstrResult, String pstrLineBreak)
	{
		int i;

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				pstrResult.append("Foi criado 1 contacto:");
				pstrResult.append(pstrLineBreak);
				Describe(pstrResult, marrCreate[0], pstrLineBreak, true);
			}
			else
			{
				pstrResult.append("Foram criados ");
				pstrResult.append(marrCreate.length);
				pstrResult.append(" contactos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					pstrResult.append("Contacto ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					Describe(pstrResult, marrCreate[i], pstrLineBreak, true);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				pstrResult.append("Foi modificado 1 contacto:");
				pstrResult.append(pstrLineBreak);
				Describe(pstrResult, marrModify[0], pstrLineBreak, false);
			}
			else
			{
				pstrResult.append("Foram modificados ");
				pstrResult.append(marrModify.length);
				pstrResult.append(" contactos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					pstrResult.append("Contacto ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					Describe(pstrResult, marrModify[i], pstrLineBreak, false);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				pstrResult.append("Foi apagado 1 contacto:");
				pstrResult.append(pstrLineBreak);
				Describe(pstrResult, marrDelete[0], pstrLineBreak, true);
			}
			else
			{
				pstrResult.append("Foram apagados ");
				pstrResult.append(marrDelete.length);
				pstrResult.append(" contactos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					pstrResult.append("Contacto ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					Describe(pstrResult, marrDelete[i], pstrLineBreak, true);
				}
			}
		}
	}

	public void UndoDesc(StringBuilder pstrResult, String pstrLineBreak)
	{
		int i;

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
				pstrResult.append("O contacto criado será apagado.");
			else
				pstrResult.append("Os contactos criados serão apagados.");
			pstrResult.append(pstrLineBreak);
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			pstrResult.append("Serão repostos os valores anteriores:");
			pstrResult.append(pstrLineBreak);
			if ( marrModify.length == 1 )
				Describe(pstrResult, marrModify[0].mobjPrevValues, pstrLineBreak, false);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					pstrResult.append("Contacto ");
					pstrResult.append(i + 1);
					pstrResult.append(":");
					pstrResult.append(pstrLineBreak);
					Describe(pstrResult, marrModify[i].mobjPrevValues, pstrLineBreak, false);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
				pstrResult.append("O contacto apagado será reposto.");
			else
				pstrResult.append("Os contactos apagados serão repostos.");
			pstrResult.append(pstrLineBreak);
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
						CreateContact(pdb, marrCreate[i], marrCreate[i].midOwnerId);
					else
						CreateContact(pdb, marrCreate[i], pidOwner);
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
					ModifyContact(pdb, marrModify[i]);
			}

			if ( marrDelete != null )
			{
				for ( i = 0; i < marrDelete.length; i++ )
					DeleteContact(pdb, marrDelete[i]);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private void CreateContact(SQLServer pdb, ContactData pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Contact lobjAux;
		ContactInfo lobjAuxInfo;
		int i;

		lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
		try
		{
			lobjAux.setAt(0, pobjData.mstrName);
			lobjAux.setAt(1, pobjData.midOwnerType);
			lobjAux.setAt(2, pidOwner);
			lobjAux.setAt(3, pobjData.mstrAddress1);
			lobjAux.setAt(4, pobjData.mstrAddress2);
			lobjAux.setAt(5, pobjData.midZipCode);
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
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				try
				{
					lobjAuxInfo.setAt(0, lobjAux.getKey());
					lobjAuxInfo.setAt(1, pobjData.marrInfo[i].midType);
					lobjAuxInfo.setAt(2, pobjData.marrInfo[i].mstrValue);
					lobjAuxInfo.SaveToDb(pdb);
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
		}

		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				CreateContact(pdb, pobjData.marrSubContacts[i], lobjAux.getKey());
		}

		pobjData.mid = lobjAux.getKey();
		pobjData.midOwnerId = pidOwner;
		pobjData.mobjPrevValues = null;
	}

	private void ModifyContact(SQLServer pdb, ContactData pobjData)
		throws BigBangJewelException
	{
		Contact lobjAux;
		ContactInfo lobjAuxInfo;
		int i;
		Entity lrefContactInfo;
		ContactInfo[] larrCIAux;

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_ContactInfo));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

		pobjData.mobjPrevValues = new ContactData();

		pobjData.mobjPrevValues.mid = lobjAux.getKey();
		pobjData.mobjPrevValues.mstrName = (String)lobjAux.getAt(0);
		pobjData.mobjPrevValues.midOwnerType = (UUID)lobjAux.getAt(1);
		pobjData.mobjPrevValues.midOwnerId = (UUID)lobjAux.getAt(2);
		pobjData.mobjPrevValues.mstrAddress1 = (String)lobjAux.getAt(3);
		pobjData.mobjPrevValues.mstrAddress2 = (String)lobjAux.getAt(4);
		pobjData.mobjPrevValues.midZipCode = (UUID)lobjAux.getAt(5);
		pobjData.mobjPrevValues.marrSubContacts = null;
		pobjData.mobjPrevValues.mobjPrevValues = null;

		larrCIAux = lobjAux.getCurrentInfo();
		pobjData.mobjPrevValues.marrInfo = new ContactData.ContactInfoData[larrCIAux.length];
		for ( i = 0; i < larrCIAux.length; i++ )
		{
			pobjData.mobjPrevValues.marrInfo[i].midType = (UUID)larrCIAux[i].getAt(0);
			pobjData.mobjPrevValues.marrInfo[i].mstrValue = (String)larrCIAux[i].getAt(1);
			try
			{
				lrefContactInfo.Delete(pdb, larrCIAux[i].getKey());
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
			lobjAux.setAt(3, pobjData.mstrAddress1);
			lobjAux.setAt(4, pobjData.mstrAddress2);
			lobjAux.setAt(5, pobjData.midZipCode);
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
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				try
				{
					lobjAuxInfo.setAt(0, lobjAux.getKey());
					lobjAuxInfo.setAt(1, pobjData.marrInfo[i].midType);
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

	private void DeleteContact(SQLServer pdb, ContactData pobjData)
		throws BigBangJewelException
	{
		Contact lobjAux;
		Entity lrefContacts;
		Entity lrefContactInfo;
		int i;
		Contact[] larrCAux;
		ContactInfo[] larrCIAux;

		try
		{
			lrefContacts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Contact));
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_ContactInfo));

			lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

			larrCAux = lobjAux.getCurrentSubContacts();
			pobjData.marrSubContacts = new ContactData[larrCAux.length];
			for ( i = 0; i < larrCAux.length; i++ )
			{
				pobjData.marrSubContacts[i] = new ContactData();
				pobjData.marrSubContacts[i].mid = larrCAux[i].getKey();
				DeleteContact(pdb, pobjData.marrSubContacts[i]);
			}

			larrCIAux = lobjAux.getCurrentInfo();
			pobjData.marrInfo = new ContactData.ContactInfoData[larrCIAux.length];
			for ( i = 0; i < larrCIAux.length; i++ )
			{
				pobjData.marrInfo[i].midType = (UUID)larrCIAux[i].getAt(1);
				pobjData.marrInfo[i].mstrValue = (String)larrCIAux[i].getAt(2);
				lrefContactInfo.Delete(pdb, larrCIAux[i].getKey());
			}

			pobjData.mstrName = (String)lobjAux.getAt(0);
			pobjData.midOwnerType = (UUID)lobjAux.getAt(1);
			pobjData.midOwnerId = (UUID)lobjAux.getAt(2);
			pobjData.mstrAddress1 = (String)lobjAux.getAt(3);
			pobjData.mstrAddress2 = (String)lobjAux.getAt(4);
			pobjData.midZipCode = (UUID)lobjAux.getAt(5);
			pobjData.mobjPrevValues = null;

			lrefContacts.Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void Describe(StringBuilder pstrString, ContactData pobjData, String pstrLineBreak, boolean pbRecurse)
	{
		ObjectBase lobjOwner;
		ObjectBase lobjZipCode;
		ObjectBase lobjInfoType;
		int i;

		pstrString.append("Contacto para: ");
		try
		{
			lobjOwner = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), pobjData.midOwnerType), pobjData.midOwnerId);
			pstrString.append(lobjOwner.getLabel());
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o dono do contacto.)");
		}
		pstrString.append(pstrLineBreak);

		pstrString.append("Nome: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);
		pstrString.append("Morada:");
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");
		pstrString.append(pobjData.mstrAddress1);
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");
		pstrString.append(pobjData.mstrAddress2);
		pstrString.append(pstrLineBreak);
		pstrString.append("- ");

		try
		{
			lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode), pobjData.midZipCode);
			pstrString.append((String)lobjZipCode.getAt(0));
			pstrString.append(" ");
			pstrString.append((String)lobjZipCode.getAt(1));
			pstrString.append(pstrLineBreak);
			pstrString.append("- ");
        	pstrString.append((String)lobjZipCode.getAt(4));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o código postal.)");
		}
		pstrString.append(pstrLineBreak);

		if ( pobjData.marrInfo != null )
		{
			for ( i = 0; i < pobjData.marrInfo.length; i++ )
			{
				try
				{
					lobjInfoType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CInfoType),
							pobjData.marrInfo[i].midType);
					pstrString.append((String)lobjInfoType.getAt(0));
				}
				catch (Throwable e)
				{
					pstrString.append("(Erro a obter o tipo de informação de contacto.)");
				}
				pstrString.append(": ");
				pstrString.append(pobjData.marrInfo[i].mstrValue);
				pstrString.append(pstrLineBreak);
			}
		}

		if ( pbRecurse && (pobjData.marrSubContacts != null) )
		{
			pstrString.append("Sub-Contactos: ");
			pstrString.append(pstrLineBreak);
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				Describe(pstrString, pobjData.marrSubContacts[i], pstrLineBreak, true);
		}
	}
}
