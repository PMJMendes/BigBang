package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;

public class ManageClientGroups
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public class GroupData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public UUID midParent;
		public GroupData[] marrSubGroups;
		public UUID midMediator;

		public GroupData mobjPrevValues;
	}

	public GroupData[] marrCreateGroups;
	public GroupData[] marrModifyGroups;
	public GroupData[] marrDeleteGroups;

	public ManageClientGroups(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_ManageGroups;
	}

	public String ShortDesc()
	{
		return "Gestão de Grupos de Clientes";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreateGroups != null) && (marrCreateGroups.length > 0) )
		{
			if ( marrCreateGroups.length == 1 )
			{
				lstrResult.append("Foi criado 1 grupo:");
				lstrResult.append(pstrLineBreak);
				DescribeGroup(lstrResult, marrCreateGroups[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram criados ");
				lstrResult.append(marrCreateGroups.length);
				lstrResult.append(" grupos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreateGroups.length; i++ )
				{
					lstrResult.append("Grupo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeGroup(lstrResult, marrCreateGroups[i], pstrLineBreak, null, true);
				}
			}
		}

		if ( (marrModifyGroups != null) && (marrModifyGroups.length > 0) )
		{
			if ( marrModifyGroups.length == 1 )
			{
				lstrResult.append("Foi modificado 1 grupo:");
				lstrResult.append(pstrLineBreak);
				DescribeGroup(lstrResult, marrModifyGroups[0], pstrLineBreak, null, false);
			}
			else
			{
				lstrResult.append("Foram modificados ");
				lstrResult.append(marrModifyGroups.length);
				lstrResult.append(" grupos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModifyGroups.length; i++ )
				{
					lstrResult.append("Grupo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeGroup(lstrResult, marrModifyGroups[i], pstrLineBreak, null, false);
				}
			}
		}

		if ( (marrDeleteGroups != null) && (marrDeleteGroups.length > 0) )
		{
			if ( marrDeleteGroups.length == 1 )
			{
				lstrResult.append("Foi apagado 1 grupo:");
				lstrResult.append(pstrLineBreak);
				DescribeGroup(lstrResult, marrDeleteGroups[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrDeleteGroups.length);
				lstrResult.append(" grupos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDeleteGroups.length; i++ )
				{
					lstrResult.append("Grupo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeGroup(lstrResult, marrDeleteGroups[i], pstrLineBreak, null, true);
				}
			}
		}

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		try
		{
			if ( marrCreateGroups != null )
				CreateGroups(pdb, marrCreateGroups, null);

			if ( marrModifyGroups != null )
				ModifyGroups(pdb, marrModifyGroups);

			if ( marrDeleteGroups != null )
				DeleteGroups(pdb, marrDeleteGroups,
						Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup)));
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreateGroups != null) && (marrCreateGroups.length > 0) )
		{
			if ( marrCreateGroups.length == 1 )
				lstrResult.append("O grupo criado será apagado.");
			else
				lstrResult.append("Os grupos criados serão apagados.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModifyGroups != null) && (marrModifyGroups.length > 0) )
		{
			lstrResult.append("Serão repostos os valores anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModifyGroups.length == 1 )
				DescribeGroup(lstrResult, marrModifyGroups[0].mobjPrevValues, pstrLineBreak, null, false);
			else
			{
				for ( i = 0; i < marrModifyGroups.length; i++ )
				{
					lstrResult.append("Grupo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeGroup(lstrResult, marrModifyGroups[i].mobjPrevValues, pstrLineBreak, null, false);
				}
			}
		}

		if ( (marrDeleteGroups != null) && (marrDeleteGroups.length > 0) )
		{
			if ( marrDeleteGroups.length == 1 )
				lstrResult.append("O grupo apagado será reposto.");
			else
				lstrResult.append("Os grupos apagados serão repostos.");
			lstrResult.append(pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreateGroups != null) && (marrCreateGroups.length > 0) )
		{
			if ( marrCreateGroups.length == 1 )
			{
				lstrResult.append("Foi apagado 1 grupo:");
				lstrResult.append(pstrLineBreak);
				DescribeGroup(lstrResult, marrCreateGroups[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrCreateGroups.length);
				lstrResult.append(" grupos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreateGroups.length; i++ )
				{
					lstrResult.append("Grupo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeGroup(lstrResult, marrCreateGroups[i], pstrLineBreak, null, true);
				}
			}
		}

		if ( (marrModifyGroups != null) && (marrModifyGroups.length > 0) )
		{
			if ( marrModifyGroups.length == 1 )
			{
				lstrResult.append("Foram repostos os valores de 1 grupo:");
				lstrResult.append(pstrLineBreak);
				DescribeGroup(lstrResult, marrModifyGroups[0].mobjPrevValues, pstrLineBreak, null, false);
			}
			else
			{
				lstrResult.append("Foram repostos os valores de ");
				lstrResult.append(marrModifyGroups.length);
				lstrResult.append(" grupos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModifyGroups.length; i++ )
				{
					lstrResult.append("Grupo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeGroup(lstrResult, marrModifyGroups[i].mobjPrevValues, pstrLineBreak, null, false);
				}
			}
		}

		if ( (marrDeleteGroups != null) && (marrDeleteGroups.length > 0) )
		{
			if ( marrDeleteGroups.length == 1 )
			{
				lstrResult.append("Foi reposto 1 grupo:");
				lstrResult.append(pstrLineBreak);
				DescribeGroup(lstrResult, marrDeleteGroups[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram repostos ");
				lstrResult.append(marrDeleteGroups.length);
				lstrResult.append(" grupos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDeleteGroups.length; i++ )
				{
					lstrResult.append("Grupo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeGroup(lstrResult, marrDeleteGroups[i], pstrLineBreak, null, true);
				}
			}
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		try
		{
			if ( marrCreateGroups != null )
			{
				UndoCreateGroups(pdb, marrCreateGroups,
						Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup)));
			}

			if ( marrModifyGroups != null )
				UndoModifyGroups(pdb, marrModifyGroups);

			if ( marrDeleteGroups != null )
				UndoDeleteGroups(pdb, marrDeleteGroups, null);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		int llngCreates, llngModifies, llngDeletes;

		llngCreates = ( marrCreateGroups == null ? 0 : CountCreateGroups(marrCreateGroups) );
		llngModifies = ( marrModifyGroups == null ? 0 : CountModifyGroups(marrModifyGroups) );
		llngDeletes = ( marrDeleteGroups == null ? 0 : CountDeleteGroups(marrDeleteGroups) );

		if ( llngCreates + llngModifies + llngDeletes == 0 )
			return new UndoSet[0];

		larrResult = new UndoSet[1];
		larrResult[0] = new UndoSet();
		larrResult[0].midType = Constants.ObjID_ClientGroup;

		larrResult[0].marrDeleted = new UUID[llngCreates];
		IdCreateGroups(larrResult[0].marrDeleted, 0, marrCreateGroups);

		larrResult[0].marrChanged = new UUID[llngModifies];
		IdModifyGroups(larrResult[0].marrChanged, 0, marrModifyGroups);

		larrResult[0].marrCreated = new UUID[llngDeletes];
		IdDeleteGroups(larrResult[0].marrCreated, 0, marrDeleteGroups);

		return larrResult;
	}

	private void CreateGroups(SQLServer pdb, GroupData[] parrData, UUID pidParent)
		throws BigBangJewelException
	{
		int i;
		ClientGroup lobjAuxGroup;

		for ( i = 0; i < parrData.length; i++ )
		{
			if ( pidParent != null )
				parrData[i].midParent = pidParent;

			try
			{
				lobjAuxGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjAuxGroup.setAt(0, parrData[i].mstrName);
				lobjAuxGroup.setAt(1, parrData[i].midParent);
				lobjAuxGroup.setAt(3, parrData[i].midMediator);
				lobjAuxGroup.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( parrData[i].marrSubGroups != null )
				CreateGroups(pdb, parrData[i].marrSubGroups, lobjAuxGroup.getKey());

			parrData[i].mid = lobjAuxGroup.getKey();
			parrData[i].mobjPrevValues = null;
		}
	}

	private void ModifyGroups(SQLServer pdb, GroupData[] parrData)
		throws BigBangJewelException
	{
		int i;
		ClientGroup lobjAuxGroup;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mobjPrevValues = new GroupData();
			parrData[i].mobjPrevValues.mid = lobjAuxGroup.getKey();
			parrData[i].mobjPrevValues.mstrName = (String)lobjAuxGroup.getAt(0);
			parrData[i].mobjPrevValues.midParent = (UUID)lobjAuxGroup.getAt(1);
			parrData[i].mobjPrevValues.midMediator = (UUID)lobjAuxGroup.getAt(3);
			parrData[i].mobjPrevValues.marrSubGroups = null;
			parrData[i].mobjPrevValues.mobjPrevValues = null;

			try
			{
				lobjAuxGroup.setAt(0, parrData[i].mstrName);
				lobjAuxGroup.setAt(1, parrData[i].midParent);
				lobjAuxGroup.setAt(3, parrData[i].midMediator);
				lobjAuxGroup.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void DeleteGroups(SQLServer pdb, GroupData[] parrData, Entity prefGroups)
		throws BigBangJewelException
	{
		int i;
		ClientGroup lobjAuxGroup;
		ClientGroup[] larrSubGroups;
		int j;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mstrName = (String)lobjAuxGroup.getAt(0);
			parrData[i].midParent = (UUID)lobjAuxGroup.getAt(1);
			parrData[i].midMediator = (UUID)lobjAuxGroup.getAt(3);
			parrData[i].mobjPrevValues = null;
			larrSubGroups = lobjAuxGroup.GetCurrentSubGroups();
			if ( larrSubGroups == null )
				parrData[i].marrSubGroups = null;
			else
			{
				parrData[i].marrSubGroups = new GroupData[larrSubGroups.length];
				for ( j = 0; j < larrSubGroups.length; j++ )
				{
					parrData[i].marrSubGroups[j] = new GroupData();
					parrData[i].marrSubGroups[j].mid = larrSubGroups[j].getKey();
				}
				DeleteGroups(pdb, parrData[i].marrSubGroups, prefGroups);
			}

			try
			{
				prefGroups.Delete(pdb, parrData[i].mid);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void UndoCreateGroups(SQLServer pdb, GroupData[] parrData, Entity prefGroups)
		throws BigBangJewelException
	{
		int i;

		for ( i = 0; i < parrData.length; i++ )
		{
			if ( parrData[i].marrSubGroups != null )
				UndoCreateGroups(pdb, parrData[i].marrSubGroups, prefGroups);

			try
			{
				prefGroups.Delete(pdb, parrData[i].mid);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void UndoModifyGroups(SQLServer pdb, GroupData[] parrData)
		throws BigBangJewelException
	{
		int i;
		ClientGroup lobjAuxGroup;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);

			try
			{
				lobjAuxGroup.setAt(0, parrData[i].mobjPrevValues.mstrName);
				lobjAuxGroup.setAt(1, parrData[i].mobjPrevValues.midParent);
				lobjAuxGroup.setAt(3, parrData[i].mobjPrevValues.midMediator);
				lobjAuxGroup.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void UndoDeleteGroups(SQLServer pdb, GroupData[] parrData, UUID pidParent)
		throws BigBangJewelException
	{
		int i;
		ClientGroup lobjAuxGroup;

		for ( i = 0; i < parrData.length; i++ )
		{
			try
			{
				lobjAuxGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjAuxGroup.setAt(0, parrData[i].mstrName);
				lobjAuxGroup.setAt(1, parrData[i].midParent);
				lobjAuxGroup.setAt(3, parrData[i].midMediator);
				lobjAuxGroup.SaveToDb(pdb);
				parrData[i].mid = lobjAuxGroup.getKey();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( parrData[i].marrSubGroups != null )
				UndoDeleteGroups(pdb, parrData[i].marrSubGroups, lobjAuxGroup.getKey());
		}
	}

	private int CountCreateGroups(GroupData[] parrData)
	{
		int llngTotal;
		int i;

		llngTotal = parrData.length;

		for ( i = 0; i < parrData.length; i++ )
		{
			if ( parrData[i].marrSubGroups != null )
				llngTotal += CountCreateGroups(parrData[i].marrSubGroups);
		}

		return llngTotal;
	}

	private int CountModifyGroups(GroupData[] parrData)
	{
		return parrData.length;
	}

	private int CountDeleteGroups(GroupData[] parrData)
	{
		int llngTotal;
		int i;

		llngTotal = parrData.length;

		for ( i = 0; i < parrData.length; i++ )
		{
			if ( parrData[i].marrSubGroups != null )
				llngTotal += CountDeleteGroups(parrData[i].marrSubGroups);
		}

		return llngTotal;
	}

	private int IdCreateGroups(UUID[] parrBuffer, int plngStart, GroupData[] parrData)
	{
		int i;

		for ( i = 0; i < parrData.length; i++ )
		{
			parrBuffer[plngStart] = parrData[i].mid;
			plngStart++;
			if ( parrData[i].marrSubGroups != null )
				plngStart = IdCreateGroups(parrBuffer, plngStart, parrData[i].marrSubGroups);
		}

		return plngStart;
	}

	private int IdModifyGroups(UUID[] parrBuffer, int plngStart, GroupData[] parrData)
	{
		int i;

		for ( i = 0; i < parrData.length; i++ )
		{
			parrBuffer[plngStart] = parrData[i].mid;
			plngStart++;
		}

		return plngStart;
	}

	private int IdDeleteGroups(UUID[] parrBuffer, int plngStart, GroupData[] parrData)
	{
		int i;

		for ( i = 0; i < parrData.length; i++ )
		{
			parrBuffer[plngStart] = parrData[i].mid;
			plngStart++;
			if ( parrData[i].marrSubGroups != null )
				plngStart = IdDeleteGroups(parrBuffer, plngStart, parrData[i].marrSubGroups);
		}

		return plngStart;
	}

	private void DescribeGroup(StringBuilder pstrString, GroupData pobjData, String pstrLineBreak, String pstrPrefix, boolean pbRecurse)
	{
		ObjectBase lobjParent, lobjMed;
		int i;

		pstrString.append("Grupo: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);

		pstrString.append("Agente: ");
		if ( pobjData.midMediator == null )
			pstrString.append("<nenhum>");
		else
		{
			try
			{
				lobjMed = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Mediator), pobjData.midMediator);
				pstrString.append((String)lobjMed.getLabel());
			}
			catch (Throwable e)
			{
				pstrString.append("(Erro a obter o mediador do grupo.)");
			}
		}
		pstrString.append(pstrLineBreak);

		if ( pstrPrefix == null )
		{
			pstrString.append("Grupo Pai: ");
			if ( pobjData.midParent == null )
				pstrString.append("<nenhum>");
			else
			{
				try
				{
					lobjParent = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
							Constants.ObjID_ClientGroup), pobjData.midParent);
					pstrString.append((String)lobjParent.getAt(0));
				}
				catch (Throwable e)
				{
					pstrString.append("(Erro a obter o grupo pai.)");
				}
			}
			pstrString.append(pstrLineBreak);
		}

		if ( pbRecurse && (pobjData.marrSubGroups != null) && (pobjData.marrSubGroups.length > 0) )
		{
			pstrString.append("Grupos filhos:");
			pstrString.append(pstrLineBreak);
			for ( i = 0; i < pobjData.marrSubGroups.length; i++ )
				DescribeGroup(pstrString, pobjData.marrSubGroups[i], pstrLineBreak,
						(pstrPrefix == null ? "" : pstrPrefix) + ">", true);
		}
	}
}
