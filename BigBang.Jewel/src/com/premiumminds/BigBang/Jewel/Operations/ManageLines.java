package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
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

	public String ShortDesc()
	{
		return "Gestão de Ramos, Modalidades e Coberturas"; 
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreateLines != null) && (marrCreateLines.length > 0) )
		{
			if ( marrCreateLines.length == 1 )
			{
				lstrResult.append("Foi criado 1 ramo:");
				lstrResult.append(pstrLineBreak);
				DescribeLine(lstrResult, marrCreateLines[0], pstrLineBreak, true);
			}
			else
			{
				lstrResult.append("Foram criados ");
				lstrResult.append(marrCreateLines.length);
				lstrResult.append(" ramos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreateLines.length; i++ )
				{
					lstrResult.append("Ramo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeLine(lstrResult, marrCreateLines[i], pstrLineBreak, true);
				}
			}
		}

		if ( (marrModifyLines != null) && (marrModifyLines.length > 0) )
		{
			if ( marrModifyLines.length == 1 )
			{
				lstrResult.append("Foi modificado 1 ramo:");
				lstrResult.append(pstrLineBreak);
				DescribeLine(lstrResult, marrModifyLines[0], pstrLineBreak, false);
			}
			else
			{
				lstrResult.append("Foram modificados ");
				lstrResult.append(marrModifyLines.length);
				lstrResult.append(" ramos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModifyLines.length; i++ )
				{
					lstrResult.append("Ramo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeLine(lstrResult, marrModifyLines[i], pstrLineBreak, false);
				}
			}
		}

		if ( (marrDeleteLines != null) && (marrDeleteLines.length > 0) )
		{
			if ( marrDeleteLines.length == 1 )
			{
				lstrResult.append("Foi apagado 1 ramo:");
				lstrResult.append(pstrLineBreak);
				DescribeLine(lstrResult, marrDeleteLines[0], pstrLineBreak, true);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrDeleteLines.length);
				lstrResult.append(" ramos:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDeleteLines.length; i++ )
				{
					lstrResult.append("Ramo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeLine(lstrResult, marrDeleteLines[i], pstrLineBreak, true);
				}
			}
		}

		if ( (marrCreateSubLines != null) && (marrCreateSubLines.length > 0) )
		{
			if ( marrCreateSubLines.length == 1 )
			{
				lstrResult.append("Foi criada 1 modalidade:");
				lstrResult.append(pstrLineBreak);
				DescribeSubLine(lstrResult, marrCreateSubLines[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram criadas ");
				lstrResult.append(marrCreateSubLines.length);
				lstrResult.append(" modalidades:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreateSubLines.length; i++ )
				{
					lstrResult.append("Modalidade ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeSubLine(lstrResult, marrCreateSubLines[i], pstrLineBreak, null, true);
				}
			}
		}

		if ( (marrModifySubLines != null) && (marrModifySubLines.length > 0) )
		{
			if ( marrModifySubLines.length == 1 )
			{
				lstrResult.append("Foi modificada 1 modalidade:");
				lstrResult.append(pstrLineBreak);
				DescribeSubLine(lstrResult, marrModifySubLines[0], pstrLineBreak, null, false);
			}
			else
			{
				lstrResult.append("Foram modificadas ");
				lstrResult.append(marrModifySubLines.length);
				lstrResult.append(" modalidades:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModifySubLines.length; i++ )
				{
					lstrResult.append("Modalidade ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeSubLine(lstrResult, marrModifySubLines[i], pstrLineBreak, null, false);
				}
			}
		}

		if ( (marrDeleteSubLines != null) && (marrDeleteSubLines.length > 0) )
		{
			if ( marrDeleteSubLines.length == 1 )
			{
				lstrResult.append("Foi apagada 1 modalidade:");
				lstrResult.append(pstrLineBreak);
				DescribeSubLine(lstrResult, marrDeleteSubLines[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram apagadas ");
				lstrResult.append(marrDeleteSubLines.length);
				lstrResult.append(" modalidades:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDeleteSubLines.length; i++ )
				{
					lstrResult.append("Modalidade ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeSubLine(lstrResult, marrDeleteSubLines[i], pstrLineBreak, null, true);
				}
			}
		}

		if ( (marrCreateCoverages != null) && (marrCreateCoverages.length > 0) )
		{
			if ( marrCreateCoverages.length == 1 )
			{
				lstrResult.append("Foi criada 1 cobertura:");
				lstrResult.append(pstrLineBreak);
				DescribeCoverage(lstrResult, marrCreateCoverages[0], pstrLineBreak, null);
			}
			else
			{
				lstrResult.append("Foram criadas ");
				lstrResult.append(marrCreateCoverages.length);
				lstrResult.append(" coberturas:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreateCoverages.length; i++ )
				{
					lstrResult.append("Cobertura ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeCoverage(lstrResult, marrCreateCoverages[i], pstrLineBreak, null);
				}
			}
		}

		if ( (marrModifyCoverages != null) && (marrModifyCoverages.length > 0) )
		{
			if ( marrModifyCoverages.length == 1 )
			{
				lstrResult.append("Foi modificada 1 cobertura:");
				lstrResult.append(pstrLineBreak);
				DescribeCoverage(lstrResult, marrModifyCoverages[0], pstrLineBreak, null);
			}
			else
			{
				lstrResult.append("Foram modificadas ");
				lstrResult.append(marrModifyCoverages.length);
				lstrResult.append(" coberturas:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModifyCoverages.length; i++ )
				{
					lstrResult.append("Cobertura ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeCoverage(lstrResult, marrModifyCoverages[i], pstrLineBreak, null);
				}
			}
		}

		if ( (marrDeleteCoverages != null) && (marrDeleteCoverages.length > 0) )
		{
			if ( marrDeleteCoverages.length == 1 )
			{
				lstrResult.append("Foi apagada 1 cobertura:");
				lstrResult.append(pstrLineBreak);
				DescribeCoverage(lstrResult, marrDeleteCoverages[0], pstrLineBreak, null);
			}
			else
			{
				lstrResult.append("Foram apagadas ");
				lstrResult.append(marrDeleteCoverages.length);
				lstrResult.append(" coberturas:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDeleteCoverages.length; i++ )
				{
					lstrResult.append("Cobertura ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeCoverage(lstrResult, marrDeleteCoverages[i], pstrLineBreak, null);
				}
			}
		}

		return lstrResult.toString();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreateLines != null) && (marrCreateLines.length > 0) )
		{
			if ( marrCreateLines.length == 1 )
				lstrResult.append("O ramo criado será apagado.");
			else
				lstrResult.append("Os ramos criados serão apagados.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModifyLines != null) && (marrModifyLines.length > 0) )
		{
			lstrResult.append("Serão repostos os valores anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModifyLines.length == 1 )
				DescribeLine(lstrResult, marrModifyLines[0].mobjPrevValues, pstrLineBreak, false);
			else
			{
				for ( i = 0; i < marrModifyLines.length; i++ )
				{
					lstrResult.append("Ramo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeLine(lstrResult, marrModifyLines[i].mobjPrevValues, pstrLineBreak, false);
				}
			}
		}

		if ( (marrDeleteLines != null) && (marrDeleteLines.length > 0) )
		{
			if ( marrDeleteLines.length == 1 )
				lstrResult.append("O ramo apagado será reposto.");
			else
				lstrResult.append("Os ramos apagados serão repostos.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrCreateSubLines != null) && (marrCreateSubLines.length > 0) )
		{
			if ( marrCreateSubLines.length == 1 )
				lstrResult.append("A modalidade criada será apagada.");
			else
				lstrResult.append("As modalidades criadas serão apagadas.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModifySubLines != null) && (marrModifySubLines.length > 0) )
		{
			lstrResult.append("Serão repostos os valores anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModifySubLines.length == 1 )
				DescribeSubLine(lstrResult, marrModifySubLines[0].mobjPrevValues, pstrLineBreak, null, false);
			else
			{
				for ( i = 0; i < marrModifySubLines.length; i++ )
				{
					lstrResult.append("Modalidade ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeSubLine(lstrResult, marrModifySubLines[i].mobjPrevValues, pstrLineBreak, null, false);
				}
			}
		}

		if ( (marrDeleteSubLines != null) && (marrDeleteSubLines.length > 0) )
		{
			if ( marrDeleteSubLines.length == 1 )
				lstrResult.append("A modalidade apagada será reposta.");
			else
				lstrResult.append("As modalidades apagadas serão repostas.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrCreateCoverages != null) && (marrCreateCoverages.length > 0) )
		{
			if ( marrCreateCoverages.length == 1 )
				lstrResult.append("A cobertura criada será apagada.");
			else
				lstrResult.append("As coberturas criadas serão apagadas.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModifyCoverages != null) && (marrModifyCoverages.length > 0) )
		{
			lstrResult.append("Serão repostos os valores anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModifyCoverages.length == 1 )
				DescribeCoverage(lstrResult, marrModifyCoverages[0].mobjPrevValues, pstrLineBreak, null);
			else
			{
				for ( i = 0; i < marrModifyCoverages.length; i++ )
				{
					lstrResult.append("Cobertura ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					DescribeCoverage(lstrResult, marrModifyCoverages[i].mobjPrevValues, pstrLineBreak, null);
				}
			}
		}

		if ( (marrDeleteCoverages != null) && (marrDeleteCoverages.length > 0) )
		{
			if ( marrDeleteCoverages.length == 1 )
				lstrResult.append("A cobertura apagada será reposta.");
			else
				lstrResult.append("As coberturas apagadas serão repostas.");
			lstrResult.append(pstrLineBreak);
		}

		return lstrResult.toString();
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

	private void DescribeLine(StringBuilder pstrString, LineData pobjData, String pstrLineBreak, boolean pbRecurse)
	{
		ObjectBase lobjCategory;
		int i;

		pstrString.append("Categoria: ");

		try
		{
			lobjCategory = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_LineCategory), pobjData.midCategory);
			pstrString.append((String)lobjCategory.getAt(0));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter a categoria.)");
		}
		pstrString.append(pstrLineBreak);

		pstrString.append("Ramo: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);

		if ( pbRecurse && (pobjData.marrSubLines != null) && (pobjData.marrSubLines.length > 0) )
		{
			pstrString.append("Modalidades:");
			pstrString.append(pstrLineBreak);
			for ( i = 0; i < pobjData.marrSubLines.length; i++ )
				DescribeSubLine(pstrString, pobjData.marrSubLines[i], pstrLineBreak, ">", true);
		}
	}

	private void DescribeSubLine(StringBuilder pstrString, SubLineData pobjData, String pstrLineBreak, String pstrPrefix, boolean pbRecurse)
	{
		Line lobjOwner;
		String lstrSubs;
		int i;

		if ( pstrPrefix == null )
		{
			pstrString.append("Ramo: ");
			try
			{
				lobjOwner = Line.GetInstance(Engine.getCurrentNameSpace(), pobjData.midLine);
				pstrString.append(lobjOwner.getLabel());
			}
			catch (Throwable e)
			{
				pstrString.append("(Erro a obter o ramo.)");
			}
			pstrString.append(pstrLineBreak);
			pstrPrefix = "Modalidade:";
			lstrSubs = ">";
		}
		else
			lstrSubs = ">>";

		pstrString.append(pstrPrefix);
		pstrString.append(" ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);

		if ( pbRecurse && (pobjData.marrCoverages != null) && (pobjData.marrCoverages.length > 0) )
		{
			pstrString.append(lstrSubs);
			pstrString.append(" ");
			pstrString.append("Coberturas:");
			pstrString.append(pstrLineBreak);
			for ( i = 0; i < pobjData.marrCoverages.length; i++ )
				DescribeCoverage(pstrString, pobjData.marrCoverages[i], pstrLineBreak, lstrSubs);
		}
	}

	private void DescribeCoverage(StringBuilder pstrString, CoverageData pobjData, String pstrLineBreak, String pstrPrefix)
	{
		SubLine lobjOwner;
		Line lobjOwnerOwner;

		if ( pstrPrefix == null )
		{
			try
			{
				pstrString.append("Ramo: ");
				lobjOwner = SubLine.GetInstance(Engine.getCurrentNameSpace(), pobjData.midSubLine);
				try
				{
					lobjOwnerOwner = Line.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjOwner.getAt(1));
					pstrString.append(lobjOwnerOwner.getLabel());
				}
				catch (Throwable e)
				{
					pstrString.append(" (Erro a obter o ramo.)");
				}
				pstrString.append(pstrLineBreak);
				pstrString.append("Modalidade: ");
				pstrString.append(lobjOwner.getLabel());
			}
			catch (Throwable e)
			{
				pstrString.append("(Erro a obter a modalidade.)");
			}
			pstrString.append(pstrLineBreak);
			pstrPrefix = "Cobertura:";
		}

		pstrString.append(pstrPrefix);
		pstrString.append(" ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);
	}
}
