package com.premiumminds.BigBang.Jewel.Operations.MedicalFile;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MedicalAppointmentData;
import com.premiumminds.BigBang.Jewel.Data.MedicalDetailData;
import com.premiumminds.BigBang.Jewel.Data.MedicalFileData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MedicalAppointment;
import com.premiumminds.BigBang.Jewel.Objects.MedicalDetail;
import com.premiumminds.BigBang.Jewel.Objects.MedicalFile;

public class ManageData
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public MedicalFileData mobjData;

	public ManageData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MedicalFile_ManageData;
	}

	public String ShortDesc()
	{
		return "Alteração de Dados";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Novos dados do processo:");
			lstrResult.append(pstrLineBreak);
			mobjData.Describe(lstrResult, pstrLineBreak);
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
		MedicalFile lobjAux;
		MedicalDetail lobjDetail;
		MedicalAppointment lobjAppt;
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjItem;
		int i;

		try
		{
			if ( mobjData != null )
			{
				lobjAux = MedicalFile.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

				mobjData.mobjPrevValues = new MedicalFileData();
				mobjData.mobjPrevValues.FromObject(lobjAux);

				mobjData.midManager = GetProcess().GetManagerID();
				mobjData.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);

				if ( mobjData.marrDetails != null )
				{
					for ( i = 0; i < mobjData.marrDetails.length; i++ )
					{
						if ( mobjData.marrDetails[i] == null )
							continue;

						if ( mobjData.marrDetails[i].mbDeleted )
						{
							lobjDetail = MedicalDetail.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrDetails[i].mid);
							mobjData.marrDetails[i].FromObject(lobjDetail);
							lobjDetail.getDefinition().Delete(pdb, lobjDetail.getKey());
						}
						else if ( mobjData.marrDetails[i].mbNew )
						{
							mobjData.marrDetails[i].midFile = mobjData.mid;

							lobjDetail = MedicalDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrDetails[i].ToObject(lobjDetail);
							lobjDetail.SaveToDb(pdb);
							mobjData.marrDetails[i].mid = lobjDetail.getKey();
						}
						else
						{
							lobjDetail = MedicalDetail.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrDetails[i].mid);
							mobjData.marrDetails[i].mobjPrevValues = new MedicalDetailData();
							mobjData.marrDetails[i].mobjPrevValues.FromObject(lobjDetail);
							mobjData.marrDetails[i].ToObject(lobjDetail);
							lobjDetail.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrAppts != null )
				{
					for ( i = 0; i < mobjData.marrAppts.length; i++ )
					{
						if ( mobjData.marrAppts[i] == null )
							continue;

						if ( mobjData.marrAppts[i].mbDeleted )
						{
							lobjAppt = MedicalAppointment.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrAppts[i].mid);
							mobjData.marrAppts[i].FromObject(lobjAppt);
							lobjAppt.getDefinition().Delete(pdb, lobjAppt.getKey());
						}
						else if ( mobjData.marrAppts[i].mbNew )
						{
							mobjData.marrAppts[i].midFile = mobjData.mid;

							lobjAppt = MedicalAppointment.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrAppts[i].ToObject(lobjAppt);
							lobjAppt.SaveToDb(pdb);
							mobjData.marrAppts[i].mid = lobjAppt.getKey();
						}
						else
						{
							lobjAppt = MedicalAppointment.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrAppts[i].mid);
							mobjData.marrAppts[i].mobjPrevValues = new MedicalAppointmentData();
							mobjData.marrAppts[i].mobjPrevValues.FromObject(lobjAppt);
							mobjData.marrAppts[i].ToObject(lobjAppt);
							lobjAppt.SaveToDb(pdb);
						}
					}
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

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
			if ( mobjData.mdtNextDate != null )
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Revisão de Processo de Baixa ou Internamento");
				lobjItem.setAt(1, GetProcess().GetManagerID());
				lobjItem.setAt(2, Constants.ProcID_MedicalFile);
				lobjItem.setAt(3, new Timestamp(new java.util.Date().getTime()));
				lobjItem.setAt(4, mobjData.mdtNextDate);
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_MedicalFile_ManageData}, pdb);
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

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Os dados anteriores serão repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Os dados anteriores foram repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		MedicalFile lobjAux;
		MedicalDetail lobjDetail;
		MedicalAppointment lobjAppt;
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjItem;
		int i;

		try
		{
			if ( mobjData != null )
			{
				lobjAux = MedicalFile.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

				mobjData.mobjPrevValues.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);

				if ( mobjData.marrDetails != null )
				{
					for ( i = 0; i < mobjData.marrDetails.length; i++ )
					{
						if ( mobjData.marrDetails[i] == null )
							continue;

						if ( mobjData.marrDetails[i].mbDeleted )
						{
							lobjDetail = MedicalDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrDetails[i].ToObject(lobjDetail);
							lobjDetail.SaveToDb(pdb);
						}
						else if ( mobjData.marrDetails[i].mbNew )
						{
							lobjDetail = MedicalDetail.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrDetails[i].mid);
							lobjDetail.getDefinition().Delete(pdb, lobjDetail.getKey());
						}
						else
						{
							lobjDetail = MedicalDetail.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrDetails[i].mid);
							mobjData.marrDetails[i].mobjPrevValues.ToObject(lobjDetail);
							lobjDetail.SaveToDb(pdb);
						}
					}
				}

				if ( mobjData.marrAppts != null )
				{
					for ( i = 0; i < mobjData.marrAppts.length; i++ )
					{
						if ( mobjData.marrAppts[i] == null )
							continue;

						if ( mobjData.marrAppts[i].mbDeleted )
						{
							lobjAppt = MedicalAppointment.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrAppts[i].ToObject(lobjAppt);
							lobjAppt.SaveToDb(pdb);
						}
						else if ( mobjData.marrAppts[i].mbNew )
						{
							lobjAppt = MedicalAppointment.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrAppts[i].mid);
							lobjAppt.getDefinition().Delete(pdb, lobjAppt.getKey());
						}
						else
						{
							lobjAppt = MedicalAppointment.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrAppts[i].mid);
							mobjData.marrAppts[i].mobjPrevValues.ToObject(lobjAppt);
							lobjAppt.SaveToDb(pdb);
						}
					}
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

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
			if ( mobjData.mobjPrevValues.mdtNextDate != null )
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Revisão de Processo de Baixa ou Internamento");
				lobjItem.setAt(1, GetProcess().GetManagerID());
				lobjItem.setAt(2, Constants.ProcID_MedicalFile);
				lobjItem.setAt(3, new Timestamp(new java.util.Date().getTime()));
				lobjItem.setAt(4, mobjData.mobjPrevValues.mdtNextDate);
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_MedicalFile_ManageData}, pdb);
			}
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
