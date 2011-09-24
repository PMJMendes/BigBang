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
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;

public class ManageLines
	extends UndoableOperation
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

	public UUID GetExternalProcess()
	{
		return null;
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

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreateLines != null) && (marrCreateLines.length > 0) )
		{
			if ( marrCreateLines.length == 1 )
			{
				lstrResult.append("Foi apagado 1 ramo:");
				lstrResult.append(pstrLineBreak);
				DescribeLine(lstrResult, marrCreateLines[0], pstrLineBreak, true);
			}
			else
			{
				lstrResult.append("Foram apagados ");
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
				lstrResult.append("Foi reposta a definição de 1 ramo:");
				lstrResult.append(pstrLineBreak);
				DescribeLine(lstrResult, marrModifyLines[0].mobjPrevValues, pstrLineBreak, false);
			}
			else
			{
				lstrResult.append("Foram repostas as definições de ");
				lstrResult.append(marrModifyLines.length);
				lstrResult.append(" ramos:");
				lstrResult.append(pstrLineBreak);
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
			{
				lstrResult.append("Foi reposto 1 ramo:");
				lstrResult.append(pstrLineBreak);
				DescribeLine(lstrResult, marrDeleteLines[0], pstrLineBreak, true);
			}
			else
			{
				lstrResult.append("Foram repostos ");
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
				lstrResult.append("Foi apagada 1 modalidade:");
				lstrResult.append(pstrLineBreak);
				DescribeSubLine(lstrResult, marrCreateSubLines[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram apagadas ");
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
				lstrResult.append("Foi reposta a definição de 1 modalidade:");
				lstrResult.append(pstrLineBreak);
				DescribeSubLine(lstrResult, marrModifySubLines[0].mobjPrevValues, pstrLineBreak, null, false);
			}
			else
			{
				lstrResult.append("Foram repostas as definições de ");
				lstrResult.append(marrModifySubLines.length);
				lstrResult.append(" modalidades:");
				lstrResult.append(pstrLineBreak);
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
			{
				lstrResult.append("Foi reposta 1 modalidade:");
				lstrResult.append(pstrLineBreak);
				DescribeSubLine(lstrResult, marrDeleteSubLines[0], pstrLineBreak, null, true);
			}
			else
			{
				lstrResult.append("Foram repostas ");
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
				lstrResult.append("Foi apagada 1 cobertura:");
				lstrResult.append(pstrLineBreak);
				DescribeCoverage(lstrResult, marrCreateCoverages[0], pstrLineBreak, null);
			}
			else
			{
				lstrResult.append("Foram apagadas ");
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
				lstrResult.append("Foi reposta a definição de 1 cobertura:");
				lstrResult.append(pstrLineBreak);
				DescribeCoverage(lstrResult, marrModifyCoverages[0].mobjPrevValues, pstrLineBreak, null);
			}
			else
			{
				lstrResult.append("Foram repostas as definições de ");
				lstrResult.append(marrModifyCoverages.length);
				lstrResult.append(" coberturas:");
				lstrResult.append(pstrLineBreak);
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
			{
				lstrResult.append("Foi reposta 1 cobertura:");
				lstrResult.append(pstrLineBreak);
				DescribeCoverage(lstrResult, marrDeleteCoverages[0], pstrLineBreak, null);
			}
			else
			{
				lstrResult.append("Foram repostas ");
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

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		try
		{
			if ( marrCreateLines != null )
				UndoCreateLines(pdb, marrCreateLines);

			if ( marrModifyLines != null )
				UndoModifyLines(pdb, marrModifyLines);

			if ( marrDeleteLines != null )
				UndoDeleteLines(pdb, marrDeleteLines);

			if ( marrCreateSubLines != null )
				UndoCreateSubLines(pdb, marrCreateSubLines);

			if ( marrModifySubLines != null )
				UndoModifySubLines(pdb, marrModifySubLines);

			if ( marrDeleteSubLines != null )
				UndoDeleteSubLines(pdb, marrDeleteSubLines, null);

			if ( marrCreateCoverages != null )
				UndoCreateCoverages(pdb, marrCreateCoverages);

			if ( marrModifyCoverages != null )
				UndoModifyCoverages(pdb, marrModifyCoverages);

			if ( marrDeleteCoverages != null )
				UndoDeleteCoverages(pdb, marrDeleteCoverages, null);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		int llngCreateLines, llngModifyLines, llngDeleteLines;
		int llngCreateSubLines, llngModifySubLines, llngDeleteSubLines;
		int llngCreateCoverages, llngModifyCoverages, llngDeleteCoverages;
		int llngSize;
		UndoSet[] larrResult;
		int i, j;

		llngCreateLines = CountLines(marrCreateLines);
		llngModifyLines = CountLines(marrModifyLines);
		llngDeleteLines = CountLines(marrDeleteLines);

		llngCreateSubLines = CountSubLines(marrCreateLines) + CountSubLines(marrCreateSubLines);
		llngModifySubLines = CountSubLines(marrModifyLines) + CountSubLines(marrModifySubLines);
		llngDeleteSubLines = CountSubLines(marrDeleteLines) + CountSubLines(marrDeleteSubLines);

		llngCreateCoverages = CountCoverages(marrCreateLines) + CountCoverages(marrCreateSubLines) +
				CountCoverages(marrCreateCoverages);
		llngModifyCoverages = CountCoverages(marrModifyLines) + CountCoverages(marrModifySubLines) +
				CountCoverages(marrModifyCoverages);
		llngDeleteCoverages = CountCoverages(marrDeleteLines) + CountCoverages(marrDeleteSubLines) +
				CountCoverages(marrDeleteCoverages);

		llngSize = 0;
		if ( llngCreateLines + llngModifyLines + llngDeleteLines > 0 )
			llngSize++;
		if ( llngCreateSubLines + llngModifySubLines + llngDeleteSubLines > 0 )
			llngSize++;
		if ( llngCreateCoverages + llngModifyCoverages + llngDeleteCoverages > 0 )
			llngSize++;
		if ( llngSize == 0 )
			return new UndoSet[0];

		larrResult = new UndoSet[llngSize];
		i = 0;

		if ( llngCreateLines + llngModifyLines + llngDeleteLines > 0 )
		{
			larrResult[i] = new UndoSet();
			larrResult[i].midType = Constants.ObjID_Line;
			larrResult[i].marrDeleted = new UUID[llngCreateLines];
			larrResult[i].marrChanged = new UUID[llngModifyLines];
			larrResult[i].marrCreated = new UUID[llngDeleteLines];

			j = 0;
			j = IdLines(larrResult[i].marrDeleted, j, marrCreateLines);

			j = 0;
			j = IdLines(larrResult[i].marrChanged, j, marrModifyLines);

			j = 0;
			j = IdLines(larrResult[i].marrCreated, j, marrDeleteLines);

			i++;
		}

		if ( llngCreateSubLines + llngModifySubLines + llngDeleteSubLines > 0 )
		{
			larrResult[i] = new UndoSet();
			larrResult[i].midType = Constants.ObjID_SubLine;
			larrResult[i].marrDeleted = new UUID[llngCreateSubLines];
			larrResult[i].marrChanged = new UUID[llngModifySubLines];
			larrResult[i].marrCreated = new UUID[llngDeleteSubLines];

			j = 0;
			j = IdSubLines(larrResult[i].marrDeleted, j, marrCreateLines);
			j = IdSubLines(larrResult[i].marrDeleted, j, marrCreateSubLines);

			j = 0;
			j = IdSubLines(larrResult[i].marrChanged, j, marrModifyLines);
			j = IdSubLines(larrResult[i].marrChanged, j, marrModifySubLines);

			j = 0;
			j = IdSubLines(larrResult[i].marrCreated, j, marrDeleteLines);
			j = IdSubLines(larrResult[i].marrCreated, j, marrDeleteSubLines);

			i++;
		}

		if ( llngCreateCoverages + llngModifyCoverages + llngDeleteCoverages > 0 )
		{
			larrResult[i] = new UndoSet();
			larrResult[i].midType = Constants.ObjID_Coverage;
			larrResult[i].marrDeleted = new UUID[llngCreateCoverages];
			larrResult[i].marrChanged = new UUID[llngModifyCoverages];
			larrResult[i].marrCreated = new UUID[llngDeleteCoverages];

			j = 0;
			j = IdCoverages(larrResult[i].marrDeleted, j, marrCreateLines);
			j = IdCoverages(larrResult[i].marrDeleted, j, marrCreateSubLines);
			j = IdCoverages(larrResult[i].marrDeleted, j, marrCreateCoverages);

			j = 0;
			j = IdCoverages(larrResult[i].marrChanged, j, marrModifyLines);
			j = IdCoverages(larrResult[i].marrChanged, j, marrModifySubLines);
			j = IdCoverages(larrResult[i].marrChanged, j, marrModifyCoverages);

			j = 0;
			j = IdCoverages(larrResult[i].marrCreated, j, marrDeleteLines);
			j = IdCoverages(larrResult[i].marrCreated, j, marrDeleteSubLines);
			j = IdCoverages(larrResult[i].marrCreated, j, marrDeleteCoverages);

			i++;
		}

		return larrResult;
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

	private void UndoCreateLines(SQLServer pdb, LineData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefLines;
		int i;

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
			if ( parrData[i].marrSubLines != null )
				UndoCreateSubLines(pdb, parrData[i].marrSubLines);

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

	private void UndoCreateSubLines(SQLServer pdb, SubLineData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefSubLines;
		int i;

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
			if ( parrData[i].marrCoverages != null )
				UndoCreateCoverages(pdb, parrData[i].marrCoverages);

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

	private void UndoCreateCoverages(SQLServer pdb, CoverageData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefCoverages;
		int i;

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

	private void UndoModifyLines(SQLServer pdb, LineData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Line lobjAuxLine;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxLine = Line.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);

			try
			{
				lobjAuxLine.setAt(0, parrData[i].mobjPrevValues.mstrName);
				lobjAuxLine.setAt(1, parrData[i].mobjPrevValues.midCategory);
				lobjAuxLine.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void UndoModifySubLines(SQLServer pdb, SubLineData[] parrData)
		throws BigBangJewelException
	{
		int i;
		SubLine lobjAuxSubLine;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);

			try
			{
				lobjAuxSubLine.setAt(0, parrData[i].mobjPrevValues.mstrName);
				lobjAuxSubLine.setAt(1, parrData[i].mobjPrevValues.midLine);
				lobjAuxSubLine.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void UndoModifyCoverages(SQLServer pdb, CoverageData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Coverage lobjAuxCoverage;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxCoverage = Coverage.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);

			try
			{
				lobjAuxCoverage.setAt(0, parrData[i].mobjPrevValues.mstrName);
				lobjAuxCoverage.setAt(1, parrData[i].mobjPrevValues.midSubLine);
				lobjAuxCoverage.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void UndoDeleteLines(SQLServer pdb, LineData[] parrData)
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
				parrData[i].mid = lobjAuxLine.getKey();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( parrData[i].marrSubLines != null )
				UndoDeleteSubLines(pdb, parrData[i].marrSubLines, lobjAuxLine.getKey());
		}
	}

	private void UndoDeleteSubLines(SQLServer pdb, SubLineData[] parrData, UUID pidParent)
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
				parrData[i].mid = lobjAuxSubLine.getKey();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( parrData[i].marrCoverages != null )
				UndoDeleteCoverages(pdb, parrData[i].marrCoverages, lobjAuxSubLine.getKey());
		}
	}

	private void UndoDeleteCoverages(SQLServer pdb, CoverageData[] parrData, UUID pidParent)
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
				parrData[i].mid = lobjAuxCoverage.getKey();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private int CountLines(LineData[] parrData)
	{
		return ( parrData == null ? 0 : parrData.length);
	}

	private int CountSubLines(LineData[] parrData)
	{
		int llngTotal;
		int i;

		if ( parrData == null )
			return 0;

		llngTotal = 0;

		for ( i = 0; i < parrData.length; i++ )
			llngTotal += CountSubLines(parrData[i].marrSubLines);

		return llngTotal;
	}

	private int CountSubLines(SubLineData[] parrData)
	{
		return ( parrData == null ? 0 : parrData.length);
	}

	private int CountCoverages(LineData[] parrData)
	{
		int llngTotal;
		int i;

		llngTotal = 0;

		if ( parrData == null )
			return 0;

		for ( i = 0; i < parrData.length; i++ )
			llngTotal += CountCoverages(parrData[i].marrSubLines);

		return llngTotal;
	}

	private int CountCoverages(SubLineData[] parrData)
	{
		int llngTotal;
		int i;

		if ( parrData == null )
			return 0;

		llngTotal = 0;

		for ( i = 0; i < parrData.length; i++ )
			llngTotal += CountCoverages(parrData[i].marrCoverages);

		return llngTotal;
	}

	private int CountCoverages(CoverageData[] parrData)
	{
		return ( parrData == null ? 0 : parrData.length);
	}

	private int IdLines(UUID[] parrBuffer, int plngStart, LineData[] parrData)
	{
		int i;

		if ( parrData == null )
			return plngStart;

		for ( i = 0; i < parrData.length; i++ )
		{
			parrBuffer[plngStart] = parrData[i].mid;
			plngStart++;
		}

		return plngStart;
	}

	private int IdSubLines(UUID[] parrBuffer, int plngStart, LineData[] parrData)
	{
		int i;

		if ( parrData == null )
			return plngStart;

		for ( i = 0; i < parrData.length; i++ )
			plngStart = IdSubLines(parrBuffer, plngStart, parrData[i].marrSubLines);

		return plngStart;
	}

	private int IdSubLines(UUID[] parrBuffer, int plngStart, SubLineData[] parrData)
	{
		int i;

		if ( parrData == null )
			return plngStart;

		for ( i = 0; i < parrData.length; i++ )
		{
			parrBuffer[plngStart] = parrData[i].mid;
			plngStart++;
		}

		return plngStart;
	}

	private int IdCoverages(UUID[] parrBuffer, int plngStart, LineData[] parrData)
	{
		int i;

		if ( parrData == null )
			return plngStart;

		for ( i = 0; i < parrData.length; i++ )
			plngStart = IdCoverages(parrBuffer, plngStart, parrData[i].marrSubLines);

		return plngStart;
	}

	private int IdCoverages(UUID[] parrBuffer, int plngStart, SubLineData[] parrData)
	{
		int i;

		if ( parrData == null )
			return plngStart;

		for ( i = 0; i < parrData.length; i++ )
			plngStart = IdCoverages(parrBuffer, plngStart, parrData[i].marrCoverages);

		return plngStart;
	}

	private int IdCoverages(UUID[] parrBuffer, int plngStart, CoverageData[] parrData)
	{
		int i;

		if ( parrData == null )
			return plngStart;

		for ( i = 0; i < parrData.length; i++ )
		{
			parrBuffer[plngStart] = parrData[i].mid;
			plngStart++;
		}

		return plngStart;
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
