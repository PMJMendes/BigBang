package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.PolicyExerciseData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;

public class OpenNewExercise
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public PolicyData mobjData;

	public OpenNewExercise(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_OpenNewExercise;
	}

	public String ShortDesc()
	{
		return "Abertura de Novo Exercício";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			if ( mobjData.marrExercises != null )
			{
				for ( i = 0; i < mobjData.marrExercises.length; i++ )
				{
					if ( mobjData.marrExercises[i].mbNew )
					{
						lstrResult.append("Exercício acrescentado:").append(pstrLineBreak);
						mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
						lstrResult.append(pstrLineBreak);
					}
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
		Policy lobjAux;
		PolicyExercise[] larrExercises;
		PolicyExercise lobjExercise;
		Calendar ldtAux2;
		int i;

		try
		{
			lobjAux = Policy.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);
			larrExercises = lobjAux.GetCurrentExercises(pdb);
			mobjData.marrExercises = new PolicyExerciseData[larrExercises.length + 1];
			for ( i = 0; i < larrExercises.length; i++ )
			{
				mobjData.marrExercises[i + 1] = new PolicyExerciseData();
				mobjData.marrExercises[i + 1].FromObject(larrExercises[i]);
			}
			mobjData.marrExercises[0] = new PolicyExerciseData();
			mobjData.marrExercises[0].mbNew = true;

			for ( i = mobjData.marrExercises.length - 1; i >= 0 ; i-- )
			{
				if ( mobjData.marrExercises[i].mbNew )
				{
					if ( mobjData.marrExercises[i].mdtStart == null )
					{
						if ( i == mobjData.marrExercises.length - 1 )
							mobjData.marrExercises[i].mdtStart = mobjData.mdtBeginDate;
						else
						{
							if ( mobjData.marrExercises[i + 1].mdtEnd == null )
							{
								if ( Constants.ExID_Variable.equals(lobjAux.GetSubLine().getExerciseType()) )
									throw new BigBangJewelException("Erro: Não pode abrir uma prorrogação sem especificar o fim do período anterior.");

						    	ldtAux2 = Calendar.getInstance();
						    	ldtAux2.setTimeInMillis(mobjData.marrExercises[i + 1].mdtStart.getTime());
								mobjData.marrExercises[i + 1].mdtEnd = Timestamp.valueOf("" + ldtAux2.get(Calendar.YEAR) +
										"-12-31 00:00:00.0");
								lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrExercises[i + 1].mid);
								mobjData.marrExercises[i + 1].ToObject(lobjExercise);
								lobjExercise.SaveToDb(pdb);
							}

					    	ldtAux2 = Calendar.getInstance();
					    	ldtAux2.setTimeInMillis(mobjData.marrExercises[i + 1].mdtEnd.getTime());
					    	ldtAux2.add(Calendar.DAY_OF_MONTH, 1);
							mobjData.marrExercises[i].mdtStart = new Timestamp(ldtAux2.getTimeInMillis());
						}
					}

					if ( Constants.ExID_Variable.equals(lobjAux.GetSubLine().getExerciseType()) )
						mobjData.marrExercises[i].mstrLabel = "Prorrogação n. " + (mobjData.marrExercises.length - 1);
					else
					{
				    	ldtAux2 = Calendar.getInstance();
				    	ldtAux2.setTimeInMillis(mobjData.marrExercises[i].mdtStart.getTime());
						mobjData.marrExercises[i].mstrLabel = "Ano de " + ldtAux2.get(Calendar.YEAR);
					}

					mobjData.marrExercises[i].midOwner = mobjData.mid;
					lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrExercises[i].ToObject(lobjExercise);
					lobjExercise.SaveToDb(pdb);
					mobjData.marrExercises[i].mid = lobjExercise.getKey();
				}
			}
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

		for ( i = 0; i < mobjData.marrExercises.length; i++ )
		{
			if ( mobjData.marrExercises[i].mbNew )
			{
				lstrResult.append("O seguinte exercício será removido:").append(pstrLineBreak);
				mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		for ( i = 0; i < mobjData.marrExercises.length; i++ )
		{
			if ( mobjData.marrExercises[i].mbNew )
			{
				lstrResult.append("Exercício removido:").append(pstrLineBreak);
				mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		PolicyExercise lobjExercise;
		int i;

		try
		{
			for ( i = 0; i < mobjData.marrExercises.length; i++ )
			{
				if ( mobjData.marrExercises[i].mbNew )
				{
					lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrExercises[i].mid);
					lobjExercise.getDefinition().Delete(pdb, lobjExercise.getKey());
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;

		larrResult = new UndoSet[] { new UndoSet() };
		larrResult[0].midType = Constants.ObjID_Policy;
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrChanged = new UUID[] { mobjData.mid };
		larrResult[0].marrCreated = new UUID[0];

		return larrResult;
	}
}
