package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingEntity;

public class MarkForClosing
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midReviewer;
	private String mstrReviewer;
	private String mstrScheduler;

	public MarkForClosing(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_MarkForClosing;
	}

	public String ShortDesc()
	{
		return "Marcação para Encerramento";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O sub-sinistro foi marcado para revisão e encerramento por " + mstrScheduler + "." + pstrLineBreak +
				"Revisor indicado: " + mstrReviewer + ".";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;
		Timestamp ldtFinal;
		SubCasualty lobjSubCasualty;

//		if ( midReviewer.equals(Engine.getCurrentUser()) )
//			throw new JewelPetriException("Erro: Não se pode indicar a si próprio para rever o seu próprio processo.");

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);
    	ldtFinal = new Timestamp(ldtAux2.getTimeInMillis());

		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
			while ( lrs.next() )
			{
				lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
				larrItems.put((UUID)lobjAgendaProc.getAt(0),
						AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
			}
			lrs.close();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			for ( AgendaItem lobjAg: larrItems.values() )
			{
				lobjAg.ClearData(pdb);
				lobjAg.getDefinition().Delete(pdb, lobjAg.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			mstrScheduler = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser()).getDisplayName();
			mstrReviewer = User.GetInstance(Engine.getCurrentNameSpace(), midReviewer).getDisplayName();

			lobjSubCasualty = (SubCasualty)GetProcess().GetData();
			validateFraming(lobjSubCasualty);
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWER, midReviewer);
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWDATE, ldtFinal);
			lobjSubCasualty.SaveToDb(pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Revisão de Processo de Sub-Sinistro");
			lobjItem.setAt(1, midReviewer);
			lobjItem.setAt(2, Constants.ProcID_SubCasualty);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, ldtFinal);
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()},
					new UUID[] {Constants.OPID_SubCasualty_CloseProcess, Constants.OPID_SubCasualty_RejectClosing}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	/** 
	 * This method validates if all the framing fields have the needed info
	 * when a sub-casualty is marked for closing.
	 */
	private void validateFraming(SubCasualty lobjSubCasualty) throws JewelPetriException {
		
		// It only validates if the casualty was open after 01/05/2017
		Casualty casualty;
		
		try {
			casualty = lobjSubCasualty.GetCasualty();
		} catch (BigBangJewelException e1) {
			throw new JewelPetriException("Não foi possível obter o sinistro", e1);
		}
		
		if (casualty == null) {
			throw new JewelPetriException("Não foi possível obter o sinistro");
		}
		
		Timestamp casDate = ((Timestamp)casualty.getAt(Casualty.I.DATE));
		Timestamp cutDate = Timestamp.valueOf("2017-05-01" + " 00:00:00.0");
		
		if(casDate.after(cutDate)) {
			boolean isWrong = false;
			
			String errors = "";
			
			SubCasualtyFraming framing;
			
			try {
				framing = lobjSubCasualty.GetFraming();
			} catch (BigBangJewelException e) {
				throw new JewelPetriException("Não foi possível obter o enquadramento", e);
			}
			
			if (framing == null) {
				throw new JewelPetriException("Não foi possível obter o enquadramento");
			}
			
			if (framing.getAt(SubCasualtyFraming.I.ANALYSISDATE) == null) {
				isWrong = true;
				errors = errors + "Deve definir-se uma data de análise. ";
			}		
			if (framing.getAt(SubCasualtyFraming.I.FRAMINGDIFFICULTY) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se se existiram dificuldades no enquadramento. ";
			}		
			if (framing.getAt(SubCasualtyFraming.I.VALIDPOLICY) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se se a apólice é válida. ";
			} else {
				if ((Boolean)framing.getAt(SubCasualtyFraming.I.VALIDPOLICY) == false) {
					if (framing.getAt(SubCasualtyFraming.I.VALIDITYNOTES) == null) {
						isWrong = true;
						errors = errors + "Se a apólice não for válida, devem-se indicar os motivos. ";
					}
				}
			}		
			if (framing.getAt(SubCasualtyFraming.I.GENERALEXCLUSIONS) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se se existem exclusões aplicáveis. ";
			} else {
				if ((Boolean)framing.getAt(SubCasualtyFraming.I.GENERALEXCLUSIONS) == true) {
					if (framing.getAt(SubCasualtyFraming.I.GENERALEXCLUSIONSNOTES) == null) {
						isWrong = true;
						errors = errors + "Se existirem exclusões aplicáveis, devem indicar-se quais. ";
					}
				}
			}		
			if (framing.getAt(SubCasualtyFraming.I.RELEVANTCOVERAGE) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se se a cobertura é aplicável. ";
			} else {
				if ((Boolean)framing.getAt(SubCasualtyFraming.I.RELEVANTCOVERAGE) == false) {
					if (framing.getAt(SubCasualtyFraming.I.GENERALEXCLUSIONSNOTES) == null) {
						isWrong = true;
						errors = errors + "Se a cobertura não for aplicável, deve indicar-se o porquê. ";
					}
				}
			}		
			if (framing.getAt(SubCasualtyFraming.I.COVERAGEVALUE) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se um capital de cobertura. ";
			}		
			if (framing.getAt(SubCasualtyFraming.I.COVERAGEEXCLUSIONS) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se se existem exclusões de cobertura aplicáveis. ";
			} else {
				if ((Boolean)framing.getAt(SubCasualtyFraming.I.COVERAGEEXCLUSIONS) == true) {
					if (framing.getAt(SubCasualtyFraming.I.COVERAGEEXCLUSIONSNOTES) == null) {
						isWrong = true;
						errors = errors + "Se existirem exclusões de cobertura aplicáveis, deve indicar-se quais. ";
					}
				}
			}		
			if (framing.getAt(SubCasualtyFraming.I.FRANCHISE) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se um capital de franquia. ";
			}		
			if (framing.getAt(SubCasualtyFraming.I.DEDUCTIBLETYPE) == null) {
				isWrong = true;
				errors = errors + "Deve indicar-se um tipo de franquia. ";
			}		
			if (framing.getAt(SubCasualtyFraming.I.INSUREREVALUATION) == null) {
				isWrong = true;
				errors = errors + "Deve definir-se uma avaliação para o segurador. ";
			}		
			if (framing.getAt(SubCasualtyFraming.I.INSUREREVALUATION) == null) {
				isWrong = true;
				errors = errors + "Deve definir-se uma avaliação para o perito. ";
			}
			
			// And now for the framing entities
			try {
				for (SubCasualtyFramingEntity entity : framing.GetCurrentFramingEntities()) {
					if (entity.getAt(SubCasualtyFramingEntity.I.ENTITYTYPE) == null) {
						isWrong = true;
						errors = errors + "Deve definir-se o tipo de entidade envolvida para todas as entidades. ";
						break;
					}
					if (entity.getAt(SubCasualtyFramingEntity.I.EVALUATION) == null) {
						isWrong = true;
						errors = errors + "Deve definir-se uma pontuação para todas as entidades. ";
						break;
					}
				}
			} catch (BigBangJewelException e) {
				throw new JewelPetriException("Não foi possível obter os outros intervenientes envolvidos no enquadramento ", e);
			}
			
			// If anything is incorrect, returns all error messages.
			if (isWrong) {
				throw new JewelPetriException(errors);
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A marcação para encerramento será retirada sem revisão.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A marcação para encerramento foi retirada sem revisão.";
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		SubCasualty lobjSubCasualty;

		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
			while ( lrs.next() )
			{
				lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
				larrItems.put((UUID)lobjAgendaProc.getAt(0),
						AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
			}
			lrs.close();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			for ( AgendaItem lobjItem: larrItems.values() )
			{
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}

			lobjSubCasualty = (SubCasualty)GetProcess().GetData();
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWER, null);
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWDATE, null);
			lobjSubCasualty.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
