package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PolicyData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midClient;
	public String mstrNumber;
	public UUID midCompany;
	public UUID midSubLine;
	public Timestamp mdtBeginDate;
	public UUID midDuration;
	public UUID midFractioning;
	public Integer mlngMaturityDay;
	public Integer mlngMaturityMonth;
	public Timestamp mdtEndDate;
	public String mstrNotes;
	public UUID midMediator;
	public Boolean mbCaseStudy;
	public UUID midStatus;
	public BigDecimal mdblPremium;
	public String mstrDocuShare;
	public BigDecimal mdblRetrocession;
	public UUID midProfile;

	public UUID midManager;
	public UUID midProcess;

	public PolicyCoInsurerData[] marrCoInsurers;
	public PolicyCoverageData[] marrCoverages;
	public PolicyObjectData[] marrObjects;
	public PolicyExerciseData[] marrExercises;
	public PolicyValueData[] marrValues;

	public boolean mbModified;

	public PolicyData mobjPrevValues;

	public void Clone(PolicyData pobjSource)
	{
		mid = pobjSource.mid;
		midClient = pobjSource.midClient;
		mstrNumber = pobjSource.mstrNumber;
		midCompany = pobjSource.midCompany;
		midSubLine = pobjSource.midSubLine;
		mdtBeginDate = pobjSource.mdtBeginDate;
		midDuration = pobjSource.midDuration;
		midFractioning = pobjSource.midFractioning;
		mlngMaturityDay = pobjSource.mlngMaturityDay;
		mlngMaturityMonth = pobjSource.mlngMaturityMonth;
		mdtEndDate = pobjSource.mdtEndDate;
		mstrNotes = pobjSource.mstrNotes;
		midMediator = pobjSource.midMediator;
		mbCaseStudy = pobjSource.mbCaseStudy;
		midManager = pobjSource.midManager;
		midProcess = pobjSource.midProcess;
		midStatus = pobjSource.midStatus;
		mdblPremium = pobjSource.mdblPremium;
		mstrDocuShare = pobjSource.mstrDocuShare;
		mdblRetrocession = pobjSource.mdblRetrocession;
		midProfile = pobjSource.midProfile;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber = (String)pobjSource.getAt(0);
		midProcess = (UUID)pobjSource.getAt(1);
		midCompany = (UUID)pobjSource.getAt(2);
		midSubLine = (UUID)pobjSource.getAt(3);
		mdtBeginDate = (Timestamp)pobjSource.getAt(4);
		midDuration = (UUID)pobjSource.getAt(5);
		midFractioning = (UUID)pobjSource.getAt(6);
		mlngMaturityDay = (Integer)pobjSource.getAt(7);
		mlngMaturityMonth = (Integer)pobjSource.getAt(8);
		mdtEndDate = (Timestamp)pobjSource.getAt(9);
		mstrNotes = (String)pobjSource.getAt(10);
		midMediator = (UUID)pobjSource.getAt(11);
		mbCaseStudy = (Boolean)pobjSource.getAt(12);
		midStatus = (UUID)pobjSource.getAt(13);
		mdblPremium = (BigDecimal)pobjSource.getAt(14);
		mstrDocuShare = (String)pobjSource.getAt(15);
//		unusedMigrationID = (Integer)pobjSource.getAt(16);
		midClient = (UUID)pobjSource.getAt(17);
		mdblRetrocession = (BigDecimal)pobjSource.getAt(18);
		midProfile = (UUID)pobjSource.getAt(19);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt( 0, mstrNumber);
			pobjDest.setAt( 1, midProcess);
			pobjDest.setAt( 2, midCompany);
			pobjDest.setAt( 3, midSubLine);
			pobjDest.setAt( 4, mdtBeginDate);
			pobjDest.setAt( 5, midDuration);
			pobjDest.setAt( 6, midFractioning);
			pobjDest.setAt( 7, mlngMaturityDay);
			pobjDest.setAt( 8, mlngMaturityMonth);
			pobjDest.setAt( 9, mdtEndDate);
			pobjDest.setAt(10, mstrNotes);
			pobjDest.setAt(11, midMediator);
			pobjDest.setAt(12, mbCaseStudy);
			pobjDest.setAt(13, midStatus);
			pobjDest.setAt(14, mdblPremium);
//			pobjDest.setAt(15, mstrDocuShare); JMMM: Nunca gravar por cima disto
//			pobjDest.setAt(16, unusedMigrationID); JMMM: Isto não é usado pela aplicação
			pobjDest.setAt(17, midClient);
			pobjDest.setAt(18, mdblRetrocession);
			pobjDest.setAt(19, midProfile);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux, lobjAux1, lobjAux2;

		pstrBuilder.append("Número: ");
		pstrBuilder.append(mstrNumber);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Seguradora: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Company), midCompany);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter a seguradora.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Ramo e Modalidade: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubLine), midSubLine);
			try
			{
				lobjAux1 = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Line),
						(UUID)lobjAux.getAt(1));
				try
				{
					lobjAux2 = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_LineCategory),
							(UUID)lobjAux1.getAt(1));
					pstrBuilder.append("(").append((String)lobjAux2.getAt(0)).append(") ");
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter a categoria de ramo.) ");
				}
				pstrBuilder.append((String)lobjAux1.getAt(0)).append(" - ");
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o ramo.) - ");
			}
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter a modalidade.)");
		}
		pstrBuilder.append(pstrLineBreak);

		if ( midMediator != null )
		{
			pstrBuilder.append("Mediador: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Mediator),
						midMediator);
				pstrBuilder.append((String)lobjAux.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o mediador.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( midProfile != null )
		{
			pstrBuilder.append("Perfil Operacional: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OpProfile),
						midProfile);
				pstrBuilder.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o perfil operacional da apólice.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Data de Início: ");
		if ( mdtBeginDate != null )
			pstrBuilder.append(mdtBeginDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Duração: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Durations),
					midDuration);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter a duração.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Fraccionamento: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Fractioning),
					midFractioning);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o fraccionamento.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Vencimento (d/m): ");
		if ( (mlngMaturityDay != 0) && (mlngMaturityMonth != 0) )
			pstrBuilder.append(mlngMaturityDay).append("/").append(mlngMaturityMonth);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data de Fim: ");
		if ( mdtEndDate != null )
			pstrBuilder.append(mdtEndDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Prémio Comercial: ");
		if ( mdblPremium != null )
			pstrBuilder.append(mdblPremium);
		else
			pstrBuilder.append("(não definido)");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Percentagem de Retrocessão: ");
		if ( mdblRetrocession != null )
			pstrBuilder.append(mdblRetrocession);
		else
			pstrBuilder.append("(não definida)");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Observações: ");
		if ( mstrNotes != null )
			pstrBuilder.append(mstrNotes);
		pstrBuilder.append(pstrLineBreak);

		if ( (mbCaseStudy != null) && (boolean)mbCaseStudy )
			pstrBuilder.append("Case Study!").append(pstrLineBreak);
	}
}
