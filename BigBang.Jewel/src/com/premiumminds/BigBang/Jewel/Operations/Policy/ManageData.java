package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyCoInsurerData;
import com.premiumminds.BigBang.Jewel.Data.PolicyCoverageData;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.PolicyExerciseData;
import com.premiumminds.BigBang.Jewel.Data.PolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.PolicyValueData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoInsurer;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ManageData
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public PolicyData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public transient UUID[] marrObjectIDs;
	public transient UUID[] marrExerciseIDs;

	public ManageData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ManageData;
	}

	public String ShortDesc()
	{
		return "Alteração de Dados";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;
		boolean b;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			if ( mobjData.mbModified )
			{
				lstrResult.append("Novos dados da apólice:").append(pstrLineBreak);
				mobjData.Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}

			if ( mobjData.marrCoInsurers != null )
			{
				lstrResult.append(pstrLineBreak).append("Co-Seguro:").append(pstrLineBreak);
				b = false;
				for ( i = 0; i < mobjData.marrCoInsurers.length; i++ )
				{
					if ( mobjData.marrCoInsurers[i].mbDeleted )
					{
						b = true;
						continue;
					}
					lstrResult.append("- ");
					mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
					if ( mobjData.marrValues[i].mbNew )
						lstrResult.append(" (Nova)");
					lstrResult.append(pstrLineBreak);
				}
				if ( b )
				{
					lstrResult.append("Co-Seguradoras removidas:").append(pstrLineBreak);
					for ( i = 0; i < mobjData.marrCoInsurers.length; i++ )
					{
						if ( !mobjData.marrCoInsurers[i].mbDeleted )
							continue;
						lstrResult.append("- ");
						mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
						lstrResult.append(pstrLineBreak);
					}
				}
				lstrResult.append(pstrLineBreak);
			}

			if ( mobjData.marrCoverages != null )
			{
				for ( i = 0; i < mobjData.marrCoverages.length; i++ )
				{
					if ( mobjData.marrCoverages[i].mbNew )
						lstrResult.append("Cobertura acrescentada:").append(pstrLineBreak);
					else
						lstrResult.append("Cobertura modificada:").append(pstrLineBreak);
					mobjData.marrCoverages[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					if ( mobjData.marrObjects[i].mbDeleted )
						lstrResult.append("Objecto removido:").append(pstrLineBreak);
					else if ( mobjData.marrObjects[i].mbNew )
						lstrResult.append("Objecto acrescentado:").append(pstrLineBreak);
					else
						lstrResult.append("Objecto modificado:").append(pstrLineBreak);
					mobjData.marrObjects[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrExercises != null )
			{
				for ( i = 0; i < mobjData.marrExercises.length; i++ )
				{
					if ( mobjData.marrExercises[i].mbDeleted )
						lstrResult.append("Exercício removido:").append(pstrLineBreak);
					else if ( mobjData.marrExercises[i].mbNew )
						lstrResult.append("Exercício acrescentado:").append(pstrLineBreak);
					else
						lstrResult.append("Exercício modificado:").append(pstrLineBreak);
					mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrValues != null )
			{
				for ( i = 0; i < mobjData.marrValues.length; i++ )
				{
					if ( mobjData.marrValues[i].mbDeleted )
						lstrResult.append("(Removido) ");
					if ( mobjData.marrValues[i].mbNew )
						lstrResult.append("(Novo) ");
					else
						lstrResult.append("(Modificado) ");
					mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

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
		UUID lidOwner;
		PolicyCoInsurer lobjCoInsurer;
		PolicyCoverage lobjCoverage;
		PolicyObject lobjObject;
		PolicyExercise lobjExercise;
		PolicyValue lobjValue;
		int i;

		lidOwner = null;
		try
		{
			if ( mobjData != null )
			{
				lidOwner = mobjData.mid;

				if ( mobjData.mbModified )
				{
					lobjAux = Policy.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

					mobjData.mobjPrevValues = new PolicyData();
					mobjData.mobjPrevValues.FromObject(lobjAux);
					mobjData.mobjPrevValues.mobjPrevValues = null;

					mobjData.midManager = GetProcess().GetManagerID();
					if ( (Integer)Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
							mobjData.midStatus).getAt(1) > 0 )
						mobjData.midCompany = mobjData.mobjPrevValues.midCompany;
					mobjData.midSubLine = mobjData.mobjPrevValues.midSubLine;
					mobjData.ToObject(lobjAux);
					lobjAux.SaveToDb(pdb);
				}

				if ( mobjData.marrCoInsurers != null )
				{
					for ( i = 0; i < mobjData.marrCoInsurers.length; i++ )
					{
						if ( mobjData.marrCoInsurers[i].mbDeleted )
						{
							if ( mobjData.marrCoInsurers[i].mid == null )
								continue;
							lobjCoInsurer = PolicyCoInsurer.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrCoInsurers[i].mid);
							mobjData.marrCoInsurers[i].FromObject(lobjCoInsurer);
							mobjData.marrCoInsurers[i].mobjPrevValues = null;
							lobjCoInsurer.getDefinition().Delete(pdb, lobjCoInsurer.getKey());
						}
						else if ( mobjData.marrCoInsurers[i].mbNew )
						{
							mobjData.marrCoInsurers[i].midPolicy = mobjData.mid;
							lobjCoInsurer = PolicyCoInsurer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrCoInsurers[i].ToObject(lobjCoInsurer);
							lobjCoInsurer.SaveToDb(pdb);
							mobjData.marrCoInsurers[i].mid = lobjCoInsurer.getKey();
						}
						else
						{
							lobjCoInsurer = PolicyCoInsurer.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrCoInsurers[i].mid);
							mobjData.marrCoInsurers[i].mobjPrevValues = new PolicyCoInsurerData();
							mobjData.marrCoInsurers[i].mobjPrevValues.FromObject(lobjCoInsurer);
							mobjData.marrCoInsurers[i].mobjPrevValues.mobjPrevValues = null;
							mobjData.marrCoInsurers[i].ToObject(lobjCoInsurer);
							lobjCoInsurer.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrCoverages != null )
				{
					for ( i = 0; i < mobjData.marrCoverages.length; i++ )
					{
						if ( mobjData.marrCoverages[i].mbNew )
						{
							mobjData.marrCoverages[i].midOwner = mobjData.mid;
							lobjCoverage = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrCoverages[i].ToObject(lobjCoverage);
							lobjCoverage.SaveToDb(pdb);
							mobjData.marrCoverages[i].mid = lobjCoverage.getKey();
						}
						else
						{
							lobjCoverage = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrCoverages[i].mid);
							mobjData.marrCoverages[i].mobjPrevValues = new PolicyCoverageData();
							mobjData.marrCoverages[i].mobjPrevValues.FromObject(lobjCoverage);
							mobjData.marrCoverages[i].mobjPrevValues.mobjPrevValues = null;
							mobjData.marrCoverages[i].ToObject(lobjCoverage);
							lobjCoverage.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i].mbDeleted )
						{
							//Aqui não há código. Ver mais abaixo.
						}
						else if ( mobjData.marrObjects[i].mbNew )
						{
							mobjData.marrObjects[i].midOwner = mobjData.mid;
							lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrObjects[i].ToObject(lobjObject);
							lobjObject.SaveToDb(pdb);
							mobjData.marrObjects[i].mid = lobjObject.getKey();
						}
						else
						{
							lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrObjects[i].mid);
							mobjData.marrObjects[i].mobjPrevValues = new PolicyObjectData();
							mobjData.marrObjects[i].mobjPrevValues.FromObject(lobjObject);
							mobjData.marrObjects[i].mobjPrevValues.mobjPrevValues = null;
							mobjData.marrObjects[i].ToObject(lobjObject);
							lobjObject.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrExercises != null )
				{
					for ( i = 0; i < mobjData.marrExercises.length; i++ )
					{
						if ( mobjData.marrExercises[i].mbDeleted )
						{
							//Aqui não há código. Ver mais abaixo.
						}
						else if ( mobjData.marrExercises[i].mbNew )
						{
							mobjData.marrExercises[i].midOwner = mobjData.mid;
							lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrExercises[i].ToObject(lobjExercise);
							lobjExercise.SaveToDb(pdb);
							mobjData.marrExercises[i].mid = lobjExercise.getKey();
						}
						else
						{
							lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrExercises[i].mid);
							mobjData.marrExercises[i].mobjPrevValues = new PolicyExerciseData();
							mobjData.marrExercises[i].mobjPrevValues.FromObject(lobjExercise);
							mobjData.marrExercises[i].mobjPrevValues.mobjPrevValues = null;
							mobjData.marrExercises[i].ToObject(lobjExercise);
							lobjExercise.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrValues != null )
				{
					for ( i = 0; i < mobjData.marrValues.length; i++ )
					{
						if ( mobjData.marrValues[i].mbDeleted )
						{
							if ( mobjData.marrValues[i].mid == null )
								continue;
							lobjValue = PolicyValue.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrValues[i].mid);
							mobjData.marrValues[i].FromObject(lobjValue);
							mobjData.marrValues[i].mobjPrevValues = null;
							lobjValue.getDefinition().Delete(pdb, lobjValue.getKey());
						}
						else if ( mobjData.marrValues[i].mbNew )
						{
							mobjData.marrValues[i].midOwner = mobjData.mid;
							mobjData.marrValues[i].midObject = ( mobjData.marrValues[i].mlngObject < 0 ? null :
									mobjData.marrObjects[mobjData.marrValues[i].mlngObject].mid );
							mobjData.marrValues[i].midExercise = ( mobjData.marrValues[i].mlngExercise < 0 ? null :
									mobjData.marrExercises[mobjData.marrValues[i].mlngExercise].mid );
							lobjValue = PolicyValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrValues[i].ToObject(lobjValue);
							lobjValue.SaveToDb(pdb);
							mobjData.marrValues[i].mid = lobjValue.getKey();
						}
						else
						{
							lobjValue = PolicyValue.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrValues[i].mid);
							mobjData.marrValues[i].mobjPrevValues = new PolicyValueData();
							mobjData.marrValues[i].mobjPrevValues.FromObject(lobjValue);
							mobjData.marrValues[i].mobjPrevValues.mobjPrevValues = null;
							mobjData.marrValues[i].midObject =
									mobjData.marrValues[i].mobjPrevValues.midObject;
							mobjData.marrValues[i].midExercise =
									mobjData.marrValues[i].mobjPrevValues.midExercise;
							mobjData.marrValues[i].ToObject(lobjValue);
							lobjValue.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i].mbDeleted )
						{
							if ( mobjData.marrObjects[i].mid == null )
								continue;
							lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrObjects[i].mid);
							mobjData.marrObjects[i].FromObject(lobjObject);
							mobjData.marrObjects[i].mobjPrevValues = null;
							lobjObject.getDefinition().Delete(pdb, lobjObject.getKey());
						}
					}
				}

				if ( mobjData.marrExercises != null )
				{
					for ( i = 0; i < mobjData.marrExercises.length; i++ )
					{
						if ( mobjData.marrExercises[i].mbDeleted )
						{
							if ( mobjData.marrExercises[i].mid == null )
								continue;
							lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrExercises[i].mid);
							mobjData.marrExercises[i].FromObject(lobjExercise);
							mobjData.marrExercises[i].mobjPrevValues = null;
							lobjExercise.getDefinition().Delete(pdb, lobjExercise.getKey());
						}
					}
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lidOwner);
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lidOwner);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		TriggerOp(new ForceValidatePolicy(this.GetProcess().getKey()), pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Os dados anteriores serão repostos:");
			lstrResult.append(pstrLineBreak);

			if ( mobjData.mbModified )
			{
				mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}

			if ( mobjData.marrCoverages != null )
			{
				for ( i = 0; i < mobjData.marrCoverages.length; i++ )
				{
					if ( mobjData.marrCoverages[i].mbNew )
					{
						lstrResult.append("A informação sobre esta cobertura será apagada:").append(pstrLineBreak);
						mobjData.marrCoverages[i].Describe(lstrResult, pstrLineBreak);
					}
					else
					{
						lstrResult.append("A informação sobre esta cobertura será reposta:").append(pstrLineBreak);
						mobjData.marrCoverages[i].mobjPrevValues.Describe(lstrResult, pstrLineBreak);
					}
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					if ( mobjData.marrObjects[i].mbDeleted )
					{
						lstrResult.append("O seguinte objecto será reposto:").append(pstrLineBreak);
						mobjData.marrObjects[i].Describe(lstrResult, pstrLineBreak);
					}
					else if ( mobjData.marrObjects[i].mbNew )
					{
						lstrResult.append("O seguinte objecto será removido:").append(pstrLineBreak);
						mobjData.marrObjects[i].Describe(lstrResult, pstrLineBreak);
					}
					else
					{
						lstrResult.append("A informação sobre este objecto será reposta:").append(pstrLineBreak);
						mobjData.marrObjects[i].mobjPrevValues.Describe(lstrResult, pstrLineBreak);
					}
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrExercises != null )
			{
				for ( i = 0; i < mobjData.marrExercises.length; i++ )
				{
					if ( mobjData.marrExercises[i].mbDeleted )
					{
						lstrResult.append("O seguinte exercício será reposto:").append(pstrLineBreak);
						mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
					}
					else if ( mobjData.marrExercises[i].mbNew )
					{
						lstrResult.append("O seguinte exercício será removido:").append(pstrLineBreak);
						mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
					}
					else
					{
						lstrResult.append("A informação sobre este exercício será reposta:").append(pstrLineBreak);
						mobjData.marrExercises[i].mobjPrevValues.Describe(lstrResult, pstrLineBreak);
					}
					mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrValues != null )
			{
				for ( i = 0; i < mobjData.marrValues.length; i++ )
				{
					if ( mobjData.marrValues[i].mbDeleted )
					{
						lstrResult.append("(A repôr) ");
						mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
					}
					if ( mobjData.marrValues[i].mbNew )
					{
						lstrResult.append("(A remover) ");
						mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
					}
					else
					{
						lstrResult.append("(A repôr informação) ");
						mobjData.marrValues[i].mobjPrevValues.Describe(lstrResult, pstrLineBreak);
					}
				}
			}
		}

		if ( mobjContactOps != null )
			mobjContactOps.UndoDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			if ( mobjData.mbModified )
			{
				lstrResult.append("Os dados anteriores foram repostos:");
				lstrResult.append(pstrLineBreak);
				mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
			}

			if ( mobjData.marrCoInsurers != null )
			{
				lstrResult.append(pstrLineBreak).append("Co-Seguro:").append(pstrLineBreak);
				for ( i = 0; i < mobjData.marrCoInsurers.length; i++ )
				{
					if ( mobjData.marrValues[i].mbNew )
						lstrResult.append("Valor removido - ");
					else
						lstrResult.append("Valor reposto - ");
					mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
				lstrResult.append(pstrLineBreak);
			}

			if ( mobjData.marrCoverages != null )
			{
				for ( i = 0; i < mobjData.marrCoverages.length; i++ )
				{
					if ( mobjData.marrCoverages[i].mbNew )
						lstrResult.append("Cobertura removida:").append(pstrLineBreak);
					else
						lstrResult.append("Dados de cobertura repostos:").append(pstrLineBreak);
					mobjData.marrCoverages[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					if ( mobjData.marrObjects[i].mbDeleted )
						lstrResult.append("Objecto reposto:").append(pstrLineBreak);
					else if ( mobjData.marrObjects[i].mbNew )
						lstrResult.append("Objecto removido:").append(pstrLineBreak);
					else
						lstrResult.append("Dados do objecto repostos:").append(pstrLineBreak);
					mobjData.marrObjects[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrExercises != null )
			{
				for ( i = 0; i < mobjData.marrExercises.length; i++ )
				{
					if ( mobjData.marrExercises[i].mbDeleted )
						lstrResult.append("Exercício reposto:").append(pstrLineBreak);
					else if ( mobjData.marrExercises[i].mbNew )
						lstrResult.append("Exercício removido:").append(pstrLineBreak);
					else
						lstrResult.append("Dados do exercício repostos:").append(pstrLineBreak);
					mobjData.marrExercises[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);
				}
			}

			if ( mobjData.marrValues != null )
			{
				for ( i = 0; i < mobjData.marrValues.length; i++ )
				{
					if ( mobjData.marrValues[i].mbDeleted )
						lstrResult.append("(Reposto) ");
					if ( mobjData.marrValues[i].mbNew )
						lstrResult.append("(Removido) ");
					else
						lstrResult.append("(Valor reposto) ");
					mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
				}
			}
		}

		if ( mobjContactOps != null )
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjAux;
		UUID lidOwner;
		PolicyCoInsurer lobjCoInsurer;
		PolicyCoverage lobjCoverage;
		PolicyObject lobjObject;
		PolicyExercise lobjExercise;
		PolicyValue lobjValue;
		int i;

		lidOwner = null;
		try
		{
			if ( mobjData != null )
			{
				lidOwner = mobjData.mid;

				if ( mobjData.mbModified )
				{
					lobjAux = Policy.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

					mobjData.mobjPrevValues.ToObject(lobjAux);
					lobjAux.SaveToDb(pdb);
				}

				if ( mobjData.marrCoInsurers != null )
				{
					for ( i = 0; i < mobjData.marrCoInsurers.length; i++ )
					{
						if ( mobjData.marrCoInsurers[i].mbDeleted )
						{
							if ( mobjData.marrCoInsurers[i].mid == null )
								continue;
							lobjCoInsurer = PolicyCoInsurer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrCoInsurers[i].ToObject(lobjCoInsurer);
							lobjCoInsurer.SaveToDb(pdb);
						}
						else if ( mobjData.marrCoInsurers[i].mbNew )
						{
							lobjCoInsurer = PolicyCoInsurer.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrCoInsurers[i].mid);
							lobjCoInsurer.getDefinition().Delete(pdb, lobjCoInsurer.getKey());
						}
						else
						{
							lobjCoInsurer = PolicyCoInsurer.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrCoInsurers[i].mid);
							mobjData.marrCoInsurers[i].mobjPrevValues.ToObject(lobjCoInsurer);
							lobjCoInsurer.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrCoverages != null )
				{
					for ( i = 0; i < mobjData.marrCoverages.length; i++ )
					{
						if ( mobjData.marrCoverages[i].mbNew )
						{
							lobjCoverage = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrCoverages[i].mid);
							lobjCoverage.getDefinition().Delete(pdb, lobjCoverage.getKey());
						}
						else
						{
							lobjCoverage = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrCoverages[i].mid);
							mobjData.marrCoverages[i].mobjPrevValues.ToObject(lobjCoverage);
							lobjCoverage.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i].mbDeleted )
						{
							if ( mobjData.marrObjects[i].mid == null )
								continue;
							lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID) null);
							mobjData.marrObjects[i].ToObject(lobjObject);
							lobjObject.SaveToDb(pdb);
							mobjData.marrObjects[i].mid = lobjObject.getKey();
						}
						else if ( mobjData.marrObjects[i].mbNew )
						{
							//Aqui não há código. Ver mais abaixo.
						}
						else
						{
							lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrObjects[i].mid);
							mobjData.marrObjects[i].mobjPrevValues.ToObject(lobjObject);
							lobjObject.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrExercises != null )
				{
					for ( i = 0; i < mobjData.marrExercises.length; i++ )
					{
						if ( mobjData.marrExercises[i].mbDeleted )
						{
							if ( mobjData.marrExercises[i].mid == null )
								continue;
							lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrExercises[i].ToObject(lobjExercise);
							lobjExercise.SaveToDb(pdb);
							mobjData.marrExercises[i].mid = lobjExercise.getKey();
						}
						else if ( mobjData.marrExercises[i].mbNew )
						{
							//Aqui não há código. Ver mais abaixo.
						}
						else
						{
							lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrExercises[i].mid);
							mobjData.marrExercises[i].mobjPrevValues.ToObject(lobjExercise);
							lobjExercise.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrValues != null )
				{
					for ( i = 0; i < mobjData.marrValues.length; i++ )
					{
						if ( mobjData.marrValues[i].mbDeleted )
						{
							if ( mobjData.marrValues[i].mid == null )
								continue;
							lobjValue = PolicyValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrValues[i].midObject = ( mobjData.marrValues[i].mlngObject < 0 ? null :
									mobjData.marrObjects[mobjData.marrValues[i].mlngObject].mid );
							mobjData.marrValues[i].midExercise = ( mobjData.marrValues[i].mlngExercise < 0 ? null :
								mobjData.marrExercises[mobjData.marrValues[i].mlngExercise].mid );
							mobjData.marrValues[i].ToObject(lobjValue);
							lobjValue.SaveToDb(pdb);
						}
						else if ( mobjData.marrValues[i].mbNew )
						{
							lobjValue = PolicyValue.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrValues[i].mid);
							lobjValue.getDefinition().Delete(pdb, lobjValue.getKey());
						}
						else
						{
							lobjValue = PolicyValue.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrValues[i].mid);
							mobjData.marrValues[i].mobjPrevValues.ToObject(lobjValue);
							lobjValue.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i].mbDeleted )
						{
						}
						else if ( mobjData.marrObjects[i].mbNew )
						{
							lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrObjects[i].mid);
							lobjObject.getDefinition().Delete(pdb, lobjObject.getKey());
						}
					}
				}

				if ( mobjData.marrExercises != null )
				{
					for ( i = 0; i < mobjData.marrExercises.length; i++ )
					{
						if ( mobjData.marrExercises[i].mbDeleted )
						{
						}
						else if ( mobjData.marrExercises[i].mbNew )
						{
							lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrExercises[i].mid);
							lobjExercise.getDefinition().Delete(pdb, lobjExercise.getKey());
						}
					}
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.UndoSubOp(pdb, lidOwner);
			if ( mobjDocOps != null )
				mobjDocOps.UndoSubOp(pdb, lidOwner);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		TriggerOp(new ForceValidatePolicy(this.GetProcess().getKey()), pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		UndoSet lobjContacts, lobjDocs;
		int llngSize;
		int i;

		lobjContacts = GetContactSet();
		lobjDocs = GetDocSet();

		llngSize = 0;
		if ( mobjData != null )
			llngSize++;
		if ( lobjContacts != null )
			llngSize++;
		if ( lobjDocs != null )
			llngSize++;

		larrResult = new UndoSet[llngSize];
		i = 0;

		if ( mobjData != null )
		{
			larrResult[0] = new UndoSet();
			larrResult[0].midType = Constants.ObjID_Policy;
			larrResult[0].marrDeleted = new UUID[0];
			larrResult[0].marrChanged = new UUID[1];
			larrResult[0].marrChanged[0] = mobjData.mid;
			larrResult[0].marrCreated = new UUID[0];
			i++;
		}

		if ( lobjContacts != null )
		{
			larrResult[i] = lobjContacts;
			i++;
		}

		if ( lobjDocs != null )
		{
			larrResult[i] = lobjDocs;
			i++;
		}

		return larrResult;
	}

	private UndoSet GetContactSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjContactOps != null )
		{
			larrAux = mobjContactOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Contact.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Contact;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}

	private UndoSet GetDocSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjDocOps != null )
		{
			larrAux = mobjDocOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Document.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Document;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}
}
