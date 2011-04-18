package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;

public class ManageLines
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public class LineData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public UUID midCategory;
		public SubLineData[] marrSubLines;
		public LineData mobjPrevValues;
	}

	public class SubLineData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public UUID midLine;
		public CoverageData[] marrCoverages;
		public SubLineData mobjPrevValues;
	}

	public class CoverageData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public UUID midSubLine;
		public CoverageData mobjPrevValues;
	}

	public LineData[] marrCreateLines;
	public LineData[] marrModifyLines;
	public LineData[] marrDeleteLines;
	public SubLineData[] marrCreateSubLines;
	public SubLineData[] marrModifySubLines;
	public SubLineData[] marrDeleteSubLines;
	public CoverageData[] marrCreateCoverages;
	public CoverageData[] marrModifyCoverages;
	public CoverageData[] marrDeleteCoverages;

	public ManageLines(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageLines;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		try
		{
			if ( marrCreateLines != null )
				CreateLines(pdb, marrCreateLines);

			if ( marrModifyLines != null )
				ModifyLines(pdb, marrModifyLines);

			if ( marrDeleteLines != null )
				DeleteLines(pdb, marrDeleteLines);

			if ( marrCreateSubLines != null )
				CreateSubLines(pdb, marrCreateSubLines, null);

			if ( marrModifySubLines != null )
				ModifySubLines(pdb, marrModifySubLines);

			if ( marrDeleteSubLines != null )
				DeleteSubLines(pdb, marrDeleteSubLines);

			if ( marrCreateCoverages != null )
				CreateCoverages(pdb, marrCreateCoverages, null);

			if ( marrModifyCoverages != null )
				ModifyCoverages(pdb, marrModifyCoverages);

			if ( marrDeleteCoverages != null )
				DeleteCoverages(pdb, marrDeleteCoverages);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private void CreateLines(SQLServer pdb, LineData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Line lobjAuxLine;

		for ( i = 0; i < parrData.length; i++ )
		{
			try
			{
				lobjAuxLine = Line.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjAuxLine.setAt(0, parrData[i].mstrName);
				lobjAuxLine.setAt(1, parrData[i].midCategory);
				lobjAuxLine.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( parrData[i].marrSubLines != null )
				CreateSubLines(pdb, parrData[i].marrSubLines, lobjAuxLine.getKey());

			parrData[i].mid = lobjAuxLine.getKey();
			parrData[i].mobjPrevValues = null;
		}
	}

	private void CreateSubLines(SQLServer pdb, SubLineData[] parrData, UUID pidParent)
		throws BigBangJewelException
	{
		int i;
		SubLine lobjAuxSubLine;

		for ( i = 0; i < parrData.length; i++ )
		{
			if ( pidParent != null )
				parrData[i].midLine = pidParent;

			try
			{
				lobjAuxSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjAuxSubLine.setAt(0, parrData[i].mstrName);
				lobjAuxSubLine.setAt(1, parrData[i].midLine);
				lobjAuxSubLine.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( parrData[i].marrCoverages != null )
				CreateCoverages(pdb, parrData[i].marrCoverages, lobjAuxSubLine.getKey());

			parrData[i].mid = lobjAuxSubLine.getKey();
			parrData[i].mobjPrevValues = null;
		}
	}

	private void CreateCoverages(SQLServer pdb, CoverageData[] parrData, UUID pidParent)
		throws BigBangJewelException
	{
		int i;
		Coverage lobjAuxCoverage;

		for ( i = 0; i < parrData.length; i++ )
		{
			if ( pidParent != null )
				parrData[i].midSubLine = pidParent;

			try
			{
				lobjAuxCoverage = Coverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjAuxCoverage.setAt(0, parrData[i].mstrName);
				lobjAuxCoverage.setAt(1, parrData[i].midSubLine);
				lobjAuxCoverage.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			parrData[i].mid = lobjAuxCoverage.getKey();
			parrData[i].mobjPrevValues = null;
		}
	}

	private void ModifyLines(SQLServer pdb, LineData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Line lobjAuxLine;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxLine = Line.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mobjPrevValues = new LineData();
			parrData[i].mobjPrevValues.mid = lobjAuxLine.getKey();
			parrData[i].mobjPrevValues.mstrName = (String)lobjAuxLine.getAt(0);
			parrData[i].mobjPrevValues.midCategory = (UUID)lobjAuxLine.getAt(1);
			parrData[i].mobjPrevValues.marrSubLines = null;
			parrData[i].mobjPrevValues.mobjPrevValues = null;

			try
			{
				lobjAuxLine.setAt(0, parrData[i].mstrName);
				lobjAuxLine.setAt(1, parrData[i].midCategory);
				lobjAuxLine.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void ModifySubLines(SQLServer pdb, SubLineData[] parrData)
		throws BigBangJewelException
	{
		int i;
		SubLine lobjAuxSubLine;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mobjPrevValues = new SubLineData();
			parrData[i].mobjPrevValues.mid = lobjAuxSubLine.getKey();
			parrData[i].mobjPrevValues.mstrName = (String)lobjAuxSubLine.getAt(0);
			parrData[i].mobjPrevValues.midLine = (UUID)lobjAuxSubLine.getAt(1);
			parrData[i].mobjPrevValues.marrCoverages = null;
			parrData[i].mobjPrevValues.mobjPrevValues = null;

			try
			{
				lobjAuxSubLine.setAt(0, parrData[i].mstrName);
				lobjAuxSubLine.setAt(1, parrData[i].midLine);
				lobjAuxSubLine.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void ModifyCoverages(SQLServer pdb, CoverageData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Coverage lobjAuxCoverage;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxCoverage = Coverage.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mobjPrevValues = new CoverageData();
			parrData[i].mobjPrevValues.mid = lobjAuxCoverage.getKey();
			parrData[i].mobjPrevValues.mstrName = (String)lobjAuxCoverage.getAt(0);
			parrData[i].mobjPrevValues.midSubLine = (UUID)lobjAuxCoverage.getAt(1);
			parrData[i].mobjPrevValues.mobjPrevValues = null;

			try
			{
				lobjAuxCoverage.setAt(0, parrData[i].mstrName);
				lobjAuxCoverage.setAt(1, parrData[i].midSubLine);
				lobjAuxCoverage.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void DeleteLines(SQLServer pdb, LineData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefLines;
		int i;
		Line lobjAuxLine;
		SubLine[] larrSubLines;
		int j;

		try
		{
			lrefLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Line));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxLine = Line.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mstrName = (String)lobjAuxLine.getAt(0);
			parrData[i].midCategory = (UUID)lobjAuxLine.getAt(1);
			parrData[i].mobjPrevValues = null;
			larrSubLines = lobjAuxLine.GetCurrentSubLines();
			if ( larrSubLines == null )
				parrData[i].marrSubLines = null;
			else
			{
				parrData[i].marrSubLines = new SubLineData[larrSubLines.length];
				for ( j = 0; j < larrSubLines.length; j++ )
				{
					parrData[i].marrSubLines[j] = new SubLineData();
					parrData[i].marrSubLines[j].mid = larrSubLines[j].getKey();
				}
				DeleteSubLines(pdb, parrData[i].marrSubLines);
			}

			try
			{
				lrefLines.Delete(pdb, parrData[i].mid);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void DeleteSubLines(SQLServer pdb, SubLineData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefSubLines;
		int i;
		SubLine lobjAuxSubLine;
		Coverage[] larrCoverages;
		int j;

		try
		{
			lrefSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_SubLine));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mstrName = (String)lobjAuxSubLine.getAt(0);
			parrData[i].midLine = (UUID)lobjAuxSubLine.getAt(1);
			parrData[i].mobjPrevValues = null;
			larrCoverages = lobjAuxSubLine.GetCurrentCoverages();
			if ( larrCoverages == null )
				parrData[i].marrCoverages = null;
			else
			{
				parrData[i].marrCoverages = new CoverageData[larrCoverages.length];
				for ( j = 0; j < larrCoverages.length; j++ )
				{
					parrData[i].marrCoverages[j] = new CoverageData();
					parrData[i].marrCoverages[j].mid = larrCoverages[j].getKey();
				}
				DeleteCoverages(pdb, parrData[i].marrCoverages);
			}

			try
			{
				lrefSubLines.Delete(pdb, parrData[i].mid);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void DeleteCoverages(SQLServer pdb, CoverageData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefCoverages;
		int i;
		Coverage lobjAuxCoverage;

		try
		{
			lrefCoverages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Coverage));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxCoverage = Coverage.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mstrName = (String)lobjAuxCoverage.getAt(0);
			parrData[i].midSubLine = (UUID)lobjAuxCoverage.getAt(1);
			parrData[i].mobjPrevValues = null;

			try
			{
				lrefCoverages.Delete(pdb, parrData[i].mid);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}
}
