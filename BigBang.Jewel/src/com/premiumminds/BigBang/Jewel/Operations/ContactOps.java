package com.premiumminds.BigBang.Jewel.Operations;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SubOperation;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.ContactInfoData;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;

public class ContactOps
	extends SubOperation
{
	private static final long serialVersionUID = 1L;

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
				marrCreate[0].Describe(pstrResult, pstrLineBreak);
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
					marrCreate[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				pstrResult.append("Foi modificado 1 contacto:");
				pstrResult.append(pstrLineBreak);
				marrModify[0].Describe(pstrResult, pstrLineBreak);
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
					marrModify[i].Describe(pstrResult, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				pstrResult.append("Foi apagado 1 contacto:");
				pstrResult.append(pstrLineBreak);
				marrDelete[0].Describe(pstrResult, pstrLineBreak);
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
				marrModify[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					pstrResult.append("Contacto ");
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
				pstrResult.append("O contacto apagado será reposto.");
			else
				pstrResult.append("Os contactos apagados serão repostos.");
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
				pstrResult.append("Foi apagado 1 contacto:");
				pstrResult.append(pstrLineBreak);
				marrCreate[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram apagados ");
				pstrResult.append(marrCreate.length);
				pstrResult.append(" contactos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					pstrResult.append("Contacto ");
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
				pstrResult.append("Foi reposta a definição de 1 contacto:");
				pstrResult.append(pstrLineBreak);
				marrModify[0].mobjPrevValues.Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram repostas as definições de ");
				pstrResult.append(marrModify.length);
				pstrResult.append(" contactos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					pstrResult.append("Contacto ");
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
				pstrResult.append("Foi reposto 1 contacto:");
				pstrResult.append(pstrLineBreak);
				marrDelete[0].Describe(pstrResult, pstrLineBreak);
			}
			else
			{
				pstrResult.append("Foram repostos ");
				pstrResult.append(marrDelete.length);
				pstrResult.append(" contactos:");
				pstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					pstrResult.append("Contacto ");
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
					UndoCreateContact(pdb, marrCreate[i]);
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
					UndoModifyContact(pdb, marrModify[i]);
			}

			if ( marrDelete != null )
			{
				for ( i = 0; i < marrDelete.length; i++ )
				{
					if ( pidOwner == null )
						UndoDeleteContact(pdb, marrDelete[i], marrDelete[i].midOwnerId);
					else
						UndoDeleteContact(pdb, marrDelete[i], pidOwner);
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

		llngCreates = 0;
		if ( marrCreate != null )
		{
			for ( i = 0; i < marrCreate.length; i++ )
				llngCreates += CountCreateContacts(marrCreate[i]);
		}

		llngModifies = 0;
		if ( marrModify != null )
		{
			for ( i = 0; i < marrModify.length; i++ )
				llngModifies += CountModifyContacts(marrModify[i]);
		}

		llngDeletes = 0;
		if ( marrDelete != null )
		{
			for ( i = 0; i < marrDelete.length; i++ )
				llngModifies += CountDeleteContacts(marrDelete[i]);
		}

		if ( llngCreates + llngModifies + llngDeletes == 0 )
			return new UndoableOperation.UndoSet[0];

		larrResult = new UndoableOperation.UndoSet[1];
		larrResult[0].midType = Constants.ObjID_Contact;
		larrResult[0].marrDeleted = new UUID[llngCreates];
		larrResult[0].marrChanged = new UUID[llngModifies];
		larrResult[0].marrCreated = new UUID[llngDeletes];

		if ( marrCreate != null )
		{
			j = 0;
			for ( i = 0; i < marrCreate.length; i ++ )
				j = IdCreateContacts(larrResult[0].marrDeleted, j, marrCreate[i]);
		}

		if ( marrModify != null )
		{
			j = 0;
			for ( i = 0; i < marrModify.length; i ++ )
				j = IdModifyContacts(larrResult[0].marrChanged, j, marrModify[i]);
		}

		if ( marrDelete != null )
		{
			j = 0;
			for ( i = 0; i < marrDelete.length; i ++ )
				j = IdDeleteContacts(larrResult[0].marrCreated, j, marrDelete[i]);
		}

		return larrResult;
	}

	private void CreateContact(SQLServer pdb, ContactData pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Contact lobjAux;
		ContactInfo lobjAuxInfo;
		int i;

		lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
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
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
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

		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				CreateContact(pdb, pobjData.marrSubContacts[i], pobjData.mid);
		}

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
		pobjData.mobjPrevValues.FromObject(lobjAux);
		pobjData.mobjPrevValues.marrSubContacts = null;
		pobjData.mobjPrevValues.mobjPrevValues = null;

		larrCIAux = lobjAux.getCurrentInfo(pdb);
		pobjData.mobjPrevValues.marrInfo = new ContactInfoData[larrCIAux.length];
		for ( i = 0; i < larrCIAux.length; i++ )
		{
			pobjData.mobjPrevValues.marrInfo[i] = new ContactInfoData();
			pobjData.mobjPrevValues.marrInfo[i].FromObject(larrCIAux[i]);
			try
			{
				lrefContactInfo.Delete(pdb, larrCIAux[i].getKey());
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

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
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
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

			larrCAux = lobjAux.getCurrentSubContacts(pdb);
			pobjData.marrSubContacts = new ContactData[larrCAux.length];
			for ( i = 0; i < larrCAux.length; i++ )
			{
				pobjData.marrSubContacts[i] = new ContactData();
				pobjData.marrSubContacts[i].mid = larrCAux[i].getKey();
				DeleteContact(pdb, pobjData.marrSubContacts[i]);
			}

			larrCIAux = lobjAux.getCurrentInfo(pdb);
			pobjData.marrInfo = new ContactInfoData[larrCIAux.length];
			for ( i = 0; i < larrCIAux.length; i++ )
			{
				pobjData.marrInfo[i] = new ContactInfoData();
				pobjData.marrInfo[i].FromObject(larrCIAux[i]);
				lrefContactInfo.Delete(pdb, larrCIAux[i].getKey());
			}

			pobjData.FromObject(lobjAux);
			pobjData.mobjPrevValues = null;

			lrefContacts.Delete(pdb, pobjData.mid);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoCreateContact(SQLServer pdb, ContactData pobjData)
		throws BigBangJewelException
	{
		Contact lobjAux;
		Entity lrefContacts;
		Entity lrefContactInfo;
		int i;

		try
		{
			lrefContacts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Contact));
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_ContactInfo));

			lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), pobjData.mid);

			if ( pobjData.marrSubContacts != null )
			{
				for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
					UndoCreateContact(pdb, pobjData.marrSubContacts[i]);
			}

			for ( i = 0; i < pobjData.marrInfo.length; i++ )
				lrefContactInfo.Delete(pdb, pobjData.marrInfo[i].mid);

			lrefContacts.Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void UndoModifyContact(SQLServer pdb, ContactData pobjData)
		throws BigBangJewelException
	{
		Contact lobjAux;
		ContactInfo lobjAuxInfo;
		int i;
		Entity lrefContactInfo;

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

		for ( i = 0; i < pobjData.marrInfo.length; i++ )
		{
			try
			{
				lrefContactInfo.Delete(pdb, pobjData.marrInfo[i].mid);
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
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
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

	private void UndoDeleteContact(SQLServer pdb, ContactData pobjData, UUID pidOwner)
		throws BigBangJewelException
	{
		Contact lobjAux;
		ContactInfo lobjAuxInfo;
		int i;

		lobjAux = Contact.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
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
				lobjAuxInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
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

		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				UndoDeleteContact(pdb, pobjData.marrSubContacts[i], pobjData.mid);
		}
	}

	private int CountCreateContacts(ContactData pobjData)
	{
		int llngTotal;
		int i;

		llngTotal = 1;

		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				llngTotal += CountCreateContacts(pobjData.marrSubContacts[i]);
		}

		return llngTotal;
	}

	private int CountModifyContacts(ContactData pobjData)
	{
		return 1;
	}

	private int CountDeleteContacts(ContactData pobjData)
	{
		int llngTotal;
		int i;

		llngTotal = 1;

		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				llngTotal += CountDeleteContacts(pobjData.marrSubContacts[i]);
		}

		return llngTotal;
	}

	private int IdCreateContacts(UUID[] parrBuffer, int plngStart, ContactData pobjData)
	{
		int i;

		parrBuffer[plngStart] = pobjData.mid;
		plngStart++;
		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				plngStart = IdCreateContacts(parrBuffer, plngStart, pobjData.marrSubContacts[i]);
		}

		return plngStart;
	}

	private int IdModifyContacts(UUID[] parrBuffer, int plngStart, ContactData pobjData)
	{
		parrBuffer[plngStart] = pobjData.mid;
		return plngStart + 1;
	}

	private int IdDeleteContacts(UUID[] parrBuffer, int plngStart, ContactData pobjData)
	{
		int i;

		parrBuffer[plngStart] = pobjData.mid;
		plngStart++;
		if ( pobjData.marrSubContacts != null )
		{
			for ( i = 0; i < pobjData.marrSubContacts.length; i++ )
				plngStart = IdDeleteContacts(parrBuffer, plngStart, pobjData.marrSubContacts[i]);
		}

		return plngStart;
	}
}
