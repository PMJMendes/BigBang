package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestCoverageData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestObjectData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestSubLineData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestValueData;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestCoverage;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestSubLine;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestValue;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ManageData
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public QuoteRequestData mobjData;
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
		return Constants.OPID_QuoteRequest_ManageData;
	}

	public String ShortDesc()
	{
		return "Alteração de Dados";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i, j;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			if ( mobjData.mbModified )
			{
				lstrResult.append("Novos dados da apólice:").append(pstrLineBreak);
				mobjData.Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					if ( mobjData.marrObjects[i] == null )
						continue;

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

			if ( mobjData.marrSubLines != null )
			{
				for ( i = 0; i < mobjData.marrSubLines.length; i++ )
				{
					if ( mobjData.marrSubLines[i] == null )
						continue;

					if ( mobjData.marrSubLines[i].mbDeleted )
						lstrResult.append("Modalidade removida:").append(pstrLineBreak);
					else if ( mobjData.marrSubLines[i].mbNew )
						lstrResult.append("Modalidade acrescentada:").append(pstrLineBreak);
					else
						lstrResult.append("Modalidade modificada:").append(pstrLineBreak);
					mobjData.marrSubLines[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);

					if ( mobjData.marrSubLines[i].marrCoverages != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
						{
							if ( mobjData.marrSubLines[i].marrCoverages[j].mbDeleted )
								lstrResult.append("Cobertura removida:").append(pstrLineBreak);
							else if ( mobjData.marrSubLines[i].marrCoverages[j].mbNew )
								lstrResult.append("Cobertura acrescentada:").append(pstrLineBreak);
							else
								lstrResult.append("Cobertura modificada:").append(pstrLineBreak);
							mobjData.marrSubLines[i].marrCoverages[j].Describe(lstrResult, pstrLineBreak);
							lstrResult.append(pstrLineBreak);
						}
					}

					if ( mobjData.marrSubLines[i].marrValues != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
						{
							if ( mobjData.marrSubLines[i].marrValues[j].mbDeleted )
								lstrResult.append("(Removido) ");
							if ( mobjData.marrSubLines[i].marrValues[j].mbNew )
								lstrResult.append("(Novo) ");
							else
								lstrResult.append("(Modificado) ");
							mobjData.marrSubLines[i].marrValues[j].Describe(lstrResult, pstrLineBreak);
						}
					}
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
		QuoteRequest lobjAux;
		UUID lidOwner;
		QuoteRequestObject lobjObject;
		QuoteRequestSubLine lobjQRSubLine;
		QuoteRequestCoverage lobjCoverage;
		QuoteRequestValue lobjValue;
		int i, j;

		lidOwner = null;
		try
		{
			if ( mobjData != null )
			{
				lidOwner = mobjData.mid;

				if ( mobjData.mbModified )
				{
					lobjAux = QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

					mobjData.mobjPrevValues = new QuoteRequestData();
					mobjData.mobjPrevValues.FromObject(lobjAux);
					mobjData.mobjPrevValues.mobjPrevValues = null;

					mobjData.midManager = GetProcess().GetManagerID();
					mobjData.ToObject(lobjAux);
					lobjAux.SaveToDb(pdb);
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i] == null )
							continue;

						if ( mobjData.marrObjects[i].mbDeleted )
						{
							//Aqui não há código. Ver mais abaixo.
						}
						else if ( mobjData.marrObjects[i].mbNew )
						{
							mobjData.marrObjects[i].midOwner = mobjData.mid;
							lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrObjects[i].ToObject(lobjObject);
							lobjObject.SaveToDb(pdb);
							mobjData.marrObjects[i].mid = lobjObject.getKey();
						}
						else
						{
							lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrObjects[i].mid);
							mobjData.marrObjects[i].mobjPrevValues = new QuoteRequestObjectData();
							mobjData.marrObjects[i].mobjPrevValues.FromObject(lobjObject);
							mobjData.marrObjects[i].mobjPrevValues.mobjPrevValues = null;
							mobjData.marrObjects[i].ToObject(lobjObject);
							lobjObject.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrSubLines != null )
				{
					for ( i = 0; i < mobjData.marrSubLines.length; i++ )
					{
						if ( mobjData.marrSubLines[i] == null )
							continue;

						if ( mobjData.marrSubLines[i].mbDeleted )
						{
							//Aqui não há código. Ver mais abaixo.
						}
						else if ( mobjData.marrSubLines[i].mbNew )
						{
							mobjData.marrSubLines[i].midQuoteRequest = mobjData.mid;
							lobjQRSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrSubLines[i].ToObject(lobjQRSubLine);
							lobjQRSubLine.SaveToDb(pdb);
							mobjData.marrSubLines[i].mid = lobjQRSubLine.getKey();
						}
						else
						{
							lobjQRSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrSubLines[i].mid);
							mobjData.marrSubLines[i].mobjPrevValues = new QuoteRequestSubLineData();
							mobjData.marrSubLines[i].mobjPrevValues.FromObject(lobjQRSubLine);
							mobjData.marrSubLines[i].mobjPrevValues.mobjPrevValues = null;
							mobjData.marrSubLines[i].ToObject(lobjQRSubLine);
							lobjQRSubLine.SaveToDb(pdb);
						}

						if ( mobjData.marrSubLines[i].marrCoverages != null )
						{
							for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
							{
								if ( mobjData.marrSubLines[i].marrCoverages[j].mbDeleted )
								{
									if ( mobjData.marrSubLines[i].marrCoverages[j].mid == null )
										continue;
									lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrCoverages[j].mid);
									mobjData.marrSubLines[i].marrCoverages[j].FromObject(lobjCoverage);
									mobjData.marrSubLines[i].marrCoverages[j].mobjPrevValues = null;
									lobjCoverage.getDefinition().Delete(pdb, lobjCoverage.getKey());
								}
								else if ( mobjData.marrSubLines[i].marrCoverages[j].mbNew )
								{
									mobjData.marrSubLines[i].marrCoverages[j].midQRSubLine = mobjData.marrSubLines[i].mid;
									lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
									mobjData.marrSubLines[i].marrCoverages[j].ToObject(lobjCoverage);
									lobjCoverage.SaveToDb(pdb);
									mobjData.marrSubLines[i].marrCoverages[j].mid = lobjCoverage.getKey();
								}
								else
								{
									lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrCoverages[j].mid);
									mobjData.marrSubLines[i].marrCoverages[j].mobjPrevValues = new QuoteRequestCoverageData();
									mobjData.marrSubLines[i].marrCoverages[j].mobjPrevValues.FromObject(lobjCoverage);
									mobjData.marrSubLines[i].marrCoverages[j].mobjPrevValues.mobjPrevValues = null;
									mobjData.marrSubLines[i].marrCoverages[j].ToObject(lobjCoverage);
									lobjCoverage.SaveToDb(pdb);
								}
							}
						}

						if ( mobjData.marrSubLines[i].marrValues != null )
						{
							for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
							{
								if ( mobjData.marrSubLines[i].marrValues[j].mbDeleted )
								{
									if ( mobjData.marrSubLines[i].marrValues[j].mid == null )
										continue;
									lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrValues[j].mid);
									mobjData.marrSubLines[i].marrValues[j].FromObject(lobjValue);
									mobjData.marrSubLines[i].marrValues[j].mobjPrevValues = null;
									lobjValue.getDefinition().Delete(pdb, lobjValue.getKey());
								}
								else if ( mobjData.marrSubLines[i].marrValues[j].mbNew )
								{
									mobjData.marrSubLines[i].marrValues[j].midQRSubLine = mobjData.marrSubLines[i].mid;
									mobjData.marrSubLines[i].marrValues[j].midObject =
											( mobjData.marrSubLines[i].marrValues[j].mlngObject < 0 ? null :
											mobjData.marrObjects[mobjData.marrSubLines[i].marrValues[j].mlngObject].mid );
									lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
									mobjData.marrSubLines[i].marrValues[j].ToObject(lobjValue);
									lobjValue.SaveToDb(pdb);
									mobjData.marrSubLines[i].marrValues[j].mid = lobjValue.getKey();
								}
								else
								{
									lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrValues[j].mid);
									mobjData.marrSubLines[i].marrValues[j].mobjPrevValues = new QuoteRequestValueData();
									mobjData.marrSubLines[i].marrValues[j].mobjPrevValues.FromObject(lobjValue);
									mobjData.marrSubLines[i].marrValues[j].mobjPrevValues.mobjPrevValues = null;
									mobjData.marrSubLines[i].marrValues[j].midObject =
											mobjData.marrSubLines[i].marrValues[j].mobjPrevValues.midObject;
									mobjData.marrSubLines[i].marrValues[j].ToObject(lobjValue);
									lobjValue.SaveToDb(pdb);
								}
							}
						}

						if ( mobjData.marrSubLines[i].mbDeleted )
						{
							if ( mobjData.marrSubLines[i].mid == null )
								continue;
							lobjQRSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrSubLines[i].mid);
							mobjData.marrSubLines[i].FromObject(lobjQRSubLine);
							mobjData.marrSubLines[i].mobjPrevValues = null;
							lobjQRSubLine.getDefinition().Delete(pdb, lobjQRSubLine.getKey());
						}
					}
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i] == null )
							continue;

						if ( mobjData.marrObjects[i].mbDeleted )
						{
							if ( mobjData.marrObjects[i].mid == null )
								continue;
							lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(),
									mobjData.marrObjects[i].mid);
							mobjData.marrObjects[i].FromObject(lobjObject);
							mobjData.marrObjects[i].mobjPrevValues = null;
							lobjObject.getDefinition().Delete(pdb, lobjObject.getKey());
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
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i, j;

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

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					if ( mobjData.marrObjects[i] == null )
						continue;

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

			if ( mobjData.marrSubLines != null )
			{
				for ( i = 0; i < mobjData.marrSubLines.length; i++ )
				{
					if ( mobjData.marrSubLines[i] == null )
						continue;

					if ( mobjData.marrSubLines[i].mbDeleted )
					{
						lstrResult.append("A seguinte modalidade será reposta:").append(pstrLineBreak);
						mobjData.marrSubLines[i].Describe(lstrResult, pstrLineBreak);
					}
					else if ( mobjData.marrSubLines[i].mbNew )
					{
						lstrResult.append("A seguinte modalidade será removida:").append(pstrLineBreak);
						mobjData.marrSubLines[i].Describe(lstrResult, pstrLineBreak);
					}
					else
					{
						lstrResult.append("A informação sobre esta modalidade será reposta:").append(pstrLineBreak);
						mobjData.marrSubLines[i].mobjPrevValues.Describe(lstrResult, pstrLineBreak);
					}
					lstrResult.append(pstrLineBreak);

					if ( mobjData.marrSubLines[i].marrCoverages != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
						{
							if ( mobjData.marrSubLines[i].marrCoverages[j].mbDeleted )
							{
								lstrResult.append("A seguinte cobertura será reposta:").append(pstrLineBreak);
								mobjData.marrSubLines[i].marrCoverages[j].Describe(lstrResult, pstrLineBreak);
							}
							else if ( mobjData.marrSubLines[i].marrCoverages[j].mbNew )
							{
								lstrResult.append("A informação sobre esta cobertura será apagada:").append(pstrLineBreak);
								mobjData.marrSubLines[i].marrCoverages[j].Describe(lstrResult, pstrLineBreak);
							}
							else
							{
								lstrResult.append("A informação sobre esta cobertura será reposta:").append(pstrLineBreak);
								mobjData.marrSubLines[i].marrCoverages[j].mobjPrevValues.Describe(lstrResult, pstrLineBreak);
							}
							lstrResult.append(pstrLineBreak);
						}
					}

					if ( mobjData.marrSubLines[i].marrValues != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
						{
							if ( mobjData.marrSubLines[i].marrValues[j].mbDeleted )
							{
								lstrResult.append("(A repôr) ");
								mobjData.marrSubLines[i].marrValues[j].Describe(lstrResult, pstrLineBreak);
							}
							if ( mobjData.marrSubLines[i].marrValues[j].mbNew )
							{
								lstrResult.append("(A remover) ");
								mobjData.marrSubLines[i].marrValues[j].Describe(lstrResult, pstrLineBreak);
							}
							else
							{
								lstrResult.append("(A repôr informação) ");
								mobjData.marrSubLines[i].marrValues[j].mobjPrevValues.Describe(lstrResult, pstrLineBreak);
							}
						}
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
		int i, j;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			if ( mobjData.mbModified )
			{
				lstrResult.append("Os dados anteriores foram repostos:");
				lstrResult.append(pstrLineBreak);
				mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
			}

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					if ( mobjData.marrObjects[i] == null )
						continue;

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

			if ( mobjData.marrSubLines != null )
			{
				for ( i = 0; i < mobjData.marrSubLines.length; i++ )
				{
					if ( mobjData.marrSubLines[i] == null )
						continue;

					if ( mobjData.marrSubLines[i].mbDeleted )
						lstrResult.append("Modalidade reposta:").append(pstrLineBreak);
					else if ( mobjData.marrSubLines[i].mbNew )
						lstrResult.append("Modalidade removida:").append(pstrLineBreak);
					else
						lstrResult.append("Dados da modalidade repostos:").append(pstrLineBreak);
					mobjData.marrSubLines[i].Describe(lstrResult, pstrLineBreak);
					lstrResult.append(pstrLineBreak);

					if ( mobjData.marrSubLines[i].marrCoverages != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
						{
							if ( mobjData.marrSubLines[i].marrCoverages[j].mbDeleted )
								lstrResult.append("Cobertura reposta:").append(pstrLineBreak);
							else if ( mobjData.marrSubLines[i].marrCoverages[j].mbNew )
								lstrResult.append("Cobertura removida:").append(pstrLineBreak);
							else
								lstrResult.append("Dados de cobertura repostos:").append(pstrLineBreak);
							mobjData.marrSubLines[i].marrCoverages[j].Describe(lstrResult, pstrLineBreak);
							lstrResult.append(pstrLineBreak);
						}
					}

					if ( mobjData.marrSubLines[i].marrValues != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
						{
							if ( mobjData.marrSubLines[i].marrValues[j].mbDeleted )
								lstrResult.append("(Reposto) ");
							if ( mobjData.marrSubLines[i].marrValues[j].mbNew )
								lstrResult.append("(Removido) ");
							else
								lstrResult.append("(Valor reposto) ");
							mobjData.marrSubLines[i].marrValues[j].Describe(lstrResult, pstrLineBreak);
						}
					}
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
		QuoteRequest lobjAux;
		UUID lidOwner;
		QuoteRequestObject lobjObject;
		QuoteRequestSubLine lobjSubLine;
		QuoteRequestCoverage lobjCoverage;
		QuoteRequestValue lobjValue;
		int i, j;

		lidOwner = null;
		try
		{
			if ( mobjData != null )
			{
				lidOwner = mobjData.mid;

				if ( mobjData.mbModified )
				{
					lobjAux = QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

					mobjData.mobjPrevValues.ToObject(lobjAux);
					lobjAux.SaveToDb(pdb);
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i] == null )
							continue;

						if ( mobjData.marrObjects[i].mbDeleted )
						{
							if ( mobjData.marrObjects[i].mid == null )
								continue;
							lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), (UUID) null);
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
							lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrObjects[i].mid);
							mobjData.marrObjects[i].mobjPrevValues.ToObject(lobjObject);
							lobjObject.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrSubLines != null )
				{
					for ( i = 0; i < mobjData.marrSubLines.length; i++ )
					{
						if ( mobjData.marrSubLines[i] == null )
							continue;

						if ( mobjData.marrSubLines[i].mbDeleted )
						{
							if ( mobjData.marrSubLines[i].mid == null )
								continue;
							lobjSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrSubLines[i].ToObject(lobjSubLine);
							lobjSubLine.SaveToDb(pdb);
							mobjData.marrSubLines[i].mid = lobjSubLine.getKey();
						}
						else if ( mobjData.marrSubLines[i].mbNew )
						{
							//Aqui não há código. Ver mais abaixo.
						}
						else
						{
							lobjSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrSubLines[i].mid);
							mobjData.marrSubLines[i].mobjPrevValues.ToObject(lobjSubLine);
							lobjSubLine.SaveToDb(pdb);
						}

						if ( mobjData.marrSubLines[i].marrCoverages != null )
						{
							for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
							{
								if ( mobjData.marrSubLines[i].marrCoverages[j].mbDeleted )
								{
									if ( mobjData.marrSubLines[i].marrCoverages[j].mid == null )
										continue;
									lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
									mobjData.marrSubLines[i].marrCoverages[j].ToObject(lobjCoverage);
									lobjCoverage.SaveToDb(pdb);
									mobjData.marrSubLines[i].marrCoverages[j].mid = lobjCoverage.getKey();
								}
								else if ( mobjData.marrSubLines[i].marrCoverages[j].mbNew )
								{
									lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrCoverages[j].mid);
									lobjCoverage.getDefinition().Delete(pdb, lobjCoverage.getKey());
								}
								else
								{
									lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrCoverages[j].mid);
									mobjData.marrSubLines[i].marrCoverages[j].mobjPrevValues.ToObject(lobjCoverage);
									lobjCoverage.SaveToDb(pdb);
								}
							}
						}

						if ( mobjData.marrSubLines[i].marrValues != null )
						{
							for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
							{
								if ( mobjData.marrSubLines[i].marrValues[j].mbDeleted )
								{
									if ( mobjData.marrSubLines[i].marrValues[j].mid == null )
										continue;
									lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
									mobjData.marrSubLines[i].marrValues[j].midObject =
											( mobjData.marrSubLines[i].marrValues[j].mlngObject < 0 ? null :
											mobjData.marrObjects[mobjData.marrSubLines[i].marrValues[j].mlngObject].mid );
									mobjData.marrSubLines[i].marrValues[j].ToObject(lobjValue);
									lobjValue.SaveToDb(pdb);
								}
								else if ( mobjData.marrSubLines[i].marrValues[j].mbNew )
								{
									lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrValues[j].mid);
									lobjValue.getDefinition().Delete(pdb, lobjValue.getKey());
								}
								else
								{
									lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(),
											mobjData.marrSubLines[i].marrValues[j].mid);
									mobjData.marrSubLines[i].marrValues[j].mobjPrevValues.ToObject(lobjValue);
									lobjValue.SaveToDb(pdb);
								}
							}
						}

						if ( mobjData.marrSubLines[i].mbDeleted )
						{
						}
						else if ( mobjData.marrSubLines[i].mbNew )
						{
							lobjSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrSubLines[i].mid);
							lobjSubLine.getDefinition().Delete(pdb, lobjSubLine.getKey());
						}
					}
				}

				if ( mobjData.marrObjects != null )
				{
					for ( i = 0; i < mobjData.marrObjects.length; i++ )
					{
						if ( mobjData.marrObjects[i] == null )
							continue;

						if ( mobjData.marrObjects[i].mbDeleted )
						{
						}
						else if ( mobjData.marrObjects[i].mbNew )
						{
							lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrObjects[i].mid);
							lobjObject.getDefinition().Delete(pdb, lobjObject.getKey());
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
			larrResult[0].midType = Constants.ObjID_QuoteRequest;
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
