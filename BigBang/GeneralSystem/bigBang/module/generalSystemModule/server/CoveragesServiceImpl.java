package bigBang.module.generalSystemModule.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageCoefficients;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageLines;

public class CoveragesServiceImpl
	extends EngineImplementor
	implements CoveragesService
{
	private static final long serialVersionUID = 1L;

	public Line[] getLines()
		throws SessionExpiredException, BigBangException
	{
		ArrayList<Line> larrAux;
		Entity lrefLines;
		UUID lidLineCats;
        MasterDB ldb;
        ResultSet lrsLines;
		com.premiumminds.BigBang.Jewel.Objects.Line lobjLine;
		Line lobjTmp;
		ObjectBase lobjLineCat;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<Line>();

		try
		{
			lrefLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Line));
			lidLineCats = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_LineCategory);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsLines = lrefLines.SelectAllSort(ldb, new int[] {1, 0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsLines.next())
	        {
	        	lobjLine = com.premiumminds.BigBang.Jewel.Objects.Line.GetInstance(Engine.getCurrentNameSpace(), lrsLines);
	        	lobjTmp = new Line();
	        	lobjTmp.id = lobjLine.getKey().toString();
	        	lobjTmp.name = (String)lobjLine.getAt(0);
	        	lobjTmp.categoryId = ((UUID)lobjLine.getAt(1)).toString();

	        	lobjLineCat = Engine.GetWorkInstance(lidLineCats, (UUID)lobjLine.getAt(1));
	        	lobjTmp.categoryName = (String)lobjLineCat.getAt(0);

	        	lobjTmp.subLines = getSubLinesForLine(lobjLine);
	        	larrAux.add(lobjTmp);
	        }
        }
		catch (BigBangException e)
        {
			try { lrsLines.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw e;
        }
        catch (Throwable e)
        {
			try { lrsLines.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsLines.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new Line[larrAux.size()]);
	}

	public Line createLine(Line b)
		throws SessionExpiredException, BigBangException
	{
		Line[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Line[1];
		larrAux[0] = b;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrCreateLines = BuildLineArray(lopML, larrAux, true);

			lopML.marrModifyLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrModifyCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		TagLines(lopML.marrCreateLines, larrAux);

		return b;
	}

	public Line saveLine(Line b)
		throws SessionExpiredException, BigBangException
	{
		Line[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Line[1];
		larrAux[0] = b;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrModifyLines = BuildLineArray(lopML, larrAux, false);

			lopML.marrCreateLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrModifyCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return b;
	}

	public void deleteLine(String id)
		throws SessionExpiredException, BigBangException
	{
		Line[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Line[1];
		larrAux[0] = new Line();
		larrAux[0].id = id;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrDeleteLines = BuildLineArray(lopML, larrAux, false);

			lopML.marrCreateLines = null;
			lopML.marrModifyLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrModifyCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public SubLine createSubLine(SubLine m)
		throws SessionExpiredException, BigBangException
	{
		SubLine[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new SubLine[1];
		larrAux[0] = m;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrCreateSubLines = BuildSubLineArray(lopML, larrAux, UUID.fromString(m.lineId), true);

			lopML.marrCreateLines = null;
			lopML.marrModifyLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrModifyCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		TagSubLines(lopML.marrCreateSubLines, larrAux);

		return m;
	}

	public SubLine saveSubLine(SubLine m)
		throws SessionExpiredException, BigBangException
	{
		SubLine[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new SubLine[1];
		larrAux[0] = m;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrModifySubLines = BuildSubLineArray(lopML, larrAux, UUID.fromString(m.lineId), false);

			lopML.marrCreateLines = null;
			lopML.marrModifyLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrModifyCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return m;
	}

	public void deleteSubLine(String id)
		throws SessionExpiredException, BigBangException
	{
		SubLine[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new SubLine[1];
		larrAux[0] = new SubLine();
		larrAux[0].id = id;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrDeleteSubLines = BuildSubLineArray(lopML, larrAux, null, false);

			lopML.marrCreateLines = null;
			lopML.marrModifyLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrModifyCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public Coverage createCoverage(Coverage b)
		throws SessionExpiredException, BigBangException
	{
		Coverage[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Coverage[1];
		larrAux[0] = b;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrCreateCoverages = BuildCoverageArray(lopML, larrAux, UUID.fromString(b.subLineId));

			lopML.marrCreateLines = null;
			lopML.marrModifyLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrModifyCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		TagCoverages(lopML.marrCreateCoverages, larrAux);

		return b;
	}

	public Coverage saveCoverage(Coverage b)
		throws SessionExpiredException, BigBangException
	{
		Coverage[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Coverage[1];
		larrAux[0] = b;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrModifyCoverages = BuildCoverageArray(lopML, larrAux, UUID.fromString(b.subLineId));

			lopML.marrCreateLines = null;
			lopML.marrModifyLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrDeleteCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return b;
	}

	public void deleteCoverage(String id)
		throws SessionExpiredException, BigBangException
	{
		Coverage[] larrAux;
		ManageLines lopML;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Coverage[1];
		larrAux[0] = new Coverage();
		larrAux[0].id = id;

		try
		{
			lopML = new ManageLines(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopML.marrDeleteCoverages = BuildCoverageArray(lopML, larrAux, null);

			lopML.marrCreateLines = null;
			lopML.marrModifyLines = null;
			lopML.marrDeleteLines = null;
			lopML.marrCreateSubLines = null;
			lopML.marrModifySubLines = null;
			lopML.marrDeleteSubLines = null;
			lopML.marrCreateCoverages = null;
			lopML.marrModifyCoverages = null;

			lopML.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public Tax createTax(Tax b)
		throws SessionExpiredException, BigBangException
	{
		Tax[] larrAux;
		ManageCoefficients lopMT;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Tax[1];
		larrAux[0] = b;

		try
		{
			lopMT = new ManageCoefficients(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopMT.marrCreate = BuildTaxArray(lopMT, larrAux);

			lopMT.marrModify = null;
			lopMT.marrDelete = null;

			lopMT.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		TagTaxes(lopMT.marrCreate, larrAux);

		return b;
	}

	public Tax saveTax(Tax b)
		throws SessionExpiredException, BigBangException
	{
		Tax[] larrAux;
		ManageCoefficients lopMT;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Tax[1];
		larrAux[0] = b;

		try
		{
			lopMT = new ManageCoefficients(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopMT.marrModify = BuildTaxArray(lopMT, larrAux);

			lopMT.marrCreate = null;
			lopMT.marrDelete = null;

			lopMT.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return b;
	}

	public void deleteTax(String id)
		throws SessionExpiredException, BigBangException
	{
		Tax[] larrAux;
		ManageCoefficients lopMT;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new Tax[1];
		larrAux[0] = new Tax();
		larrAux[0].id = id;

		try
		{
			lopMT = new ManageCoefficients(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());

			lopMT.marrDelete = BuildTaxArray(lopMT, larrAux);

			lopMT.marrCreate = null;
			lopMT.marrModify = null;

			lopMT.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private SubLine[] getSubLinesForLine(com.premiumminds.BigBang.Jewel.Objects.Line pobjLine)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubLine[] larrSubLines;
		SubLine[] larrResult;
		int i;

		try
		{
			larrSubLines = pobjLine.GetCurrentSubLines();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( larrSubLines == null )
			return new SubLine[0];

		larrResult = new SubLine[larrSubLines.length];
		for ( i = 0; i < larrSubLines.length; i++ )
		{
			larrResult[i] = new SubLine();
			larrResult[i].id = larrSubLines[i].getKey().toString();
			larrResult[i].name = (String)larrSubLines[i].getAt(0);
			larrResult[i].lineId = ((UUID)larrSubLines[i].getAt(1)).toString();
			larrResult[i].objectTypeId = ((UUID)larrSubLines[i].getAt(2)).toString();
			larrResult[i].exercisePeriodId = ((UUID)larrSubLines[i].getAt(3)).toString();
			larrResult[i].commissionPercent = (larrSubLines[i].getAt(5) == null ? null : ((BigDecimal)larrSubLines[i].getAt(5)).doubleValue());
			larrResult[i].isLife = (Boolean)larrSubLines[i].getAt(6);
			larrResult[i].description = (String)larrSubLines[i].getAt(7);
			larrResult[i].isHR = (Boolean)larrSubLines[i].getAt(8);
			larrResult[i].coverages = getCoveragesForSubLine(larrSubLines[i]);
		}

		return larrResult;
	}

	private Coverage[] getCoveragesForSubLine(com.premiumminds.BigBang.Jewel.Objects.SubLine pobjSubLine)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Coverage[] larrCoverages;
		Coverage[] larrResult;
		int i;

		try
		{
			larrCoverages = pobjSubLine.GetCurrentCoverages();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( larrCoverages == null )
			return new Coverage[0];

		larrResult = new Coverage[larrCoverages.length];
		for ( i = 0; i < larrCoverages.length; i++ )
		{
			larrResult[i] = new Coverage();
			larrResult[i].id = larrCoverages[i].getKey().toString();
			larrResult[i].name = (String)larrCoverages[i].getAt(0);
			larrResult[i].subLineId = ((UUID)larrCoverages[i].getAt(1)).toString();
			larrResult[i].isMandatory = (Boolean)larrCoverages[i].getAt(2);
			larrResult[i].isHeader = (Boolean)larrCoverages[i].getAt(3);
			larrResult[i].tag = (String)larrCoverages[i].getAt(4);
			larrResult[i].order = (Integer)larrCoverages[i].getAt(5);
			larrResult[i].taxes = getTaxesForCoverage(larrCoverages[i]);
		}

		return larrResult;
	}

	private Tax[] getTaxesForCoverage(com.premiumminds.BigBang.Jewel.Objects.Coverage pobjCoverage)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Tax[] larrTaxes;
		Tax[] larrResult;
		int i;

		try
		{
			larrTaxes = pobjCoverage.GetCurrentTaxes();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( larrTaxes == null )
			return new Tax[0];

		larrResult = new Tax[larrTaxes.length];
		for ( i = 0; i < larrTaxes.length; i++ )
		{
			larrResult[i] = new Tax();
			larrResult[i].id = larrTaxes[i].getKey().toString();
			larrResult[i].name = (String)larrTaxes[i].getAt(0);
			larrResult[i].coverageId = ((UUID)larrTaxes[i].getAt(1)).toString();
			larrResult[i].fieldTypeId = ((UUID)larrTaxes[i].getAt(2)).toString();
			larrResult[i].unitsLabel = (String)larrTaxes[i].getAt(3);
			larrResult[i].defaultValue = (String)larrTaxes[i].getAt(4);
			larrResult[i].variesByObject = (Boolean)larrTaxes[i].getAt(5);
			larrResult[i].variesByExercise = (Boolean)larrTaxes[i].getAt(6);
			larrResult[i].refersToEntityId = (larrTaxes[i].getAt(7) == null ? null : ((UUID)larrTaxes[i].getAt(7)).toString());
			larrResult[i].columnOrder = (Integer)larrTaxes[i].getAt(8);
			larrResult[i].mandatory = (Boolean)larrTaxes[i].getAt(9);
			larrResult[i].tag = (String)larrTaxes[i].getAt(10);
			larrResult[i].visible = (Boolean)larrTaxes[i].getAt(11);
		}

		return larrResult;
	}

	private ManageLines.LineData[] BuildLineArray(ManageLines prefOp, Line[] parrLines, boolean pbRecurse)
	{
		ManageLines.LineData[] larrResult;
		int i;

		larrResult = new ManageLines.LineData[parrLines.length];
		for ( i = 0; i < parrLines.length; i++ )
		{
			larrResult[i] = new ManageLines.LineData();
			larrResult[i].mid = (parrLines[i].id == null ? null : UUID.fromString(parrLines[i].id));
			larrResult[i].mstrName = parrLines[i].name;
			larrResult[i].midCategory = (parrLines[i].categoryId == null ? null : UUID.fromString(parrLines[i].categoryId));
			larrResult[i].marrSubLines = (pbRecurse && parrLines[i].subLines != null ?
					BuildSubLineArray(prefOp, parrLines[i].subLines, larrResult[i].mid, pbRecurse) : null);
			larrResult[i].mobjPrevValues = null;
		}

		return larrResult;
	}

	private ManageLines.SubLineData[] BuildSubLineArray(ManageLines prefOp, SubLine[] parrSubLines, UUID pidParent, 
			boolean pbRecurse)
	{
		ManageLines.SubLineData[] larrResult;
		int i;

		larrResult = new ManageLines.SubLineData[parrSubLines.length];
		for ( i = 0; i < parrSubLines.length; i++ )
		{
			larrResult[i] = new ManageLines.SubLineData();
			larrResult[i].mid = (parrSubLines[i].id == null ? null : UUID.fromString(parrSubLines[i].id));
			larrResult[i].mstrName = parrSubLines[i].name;
			larrResult[i].midLine = pidParent;
			larrResult[i].midObjectType = (parrSubLines[i].objectTypeId == null ? null :
					UUID.fromString(parrSubLines[i].objectTypeId));
			larrResult[i].midExercisePeriod = (parrSubLines[i].exercisePeriodId == null ? null :
					UUID.fromString(parrSubLines[i].exercisePeriodId));
			larrResult[i].mdblPercent = (parrSubLines[i].commissionPercent == null ? null :
					new BigDecimal(parrSubLines[i].commissionPercent + ""));
			larrResult[i].mbIsLife = parrSubLines[i].isLife;
			larrResult[i].mstrDescription = parrSubLines[i].description;
			larrResult[i].mbIsHR = parrSubLines[i].isHR;
			larrResult[i].marrCoverages = (pbRecurse && parrSubLines[i].coverages != null ?
					BuildCoverageArray(prefOp, parrSubLines[i].coverages, larrResult[i].mid) : null);
			larrResult[i].mobjPrevValues = null;
		}

		return larrResult;
	}

	private ManageLines.CoverageData[] BuildCoverageArray(ManageLines prefOp, Coverage[] parrCoverages, UUID pidParent)
	{
		ManageLines.CoverageData[] larrResult;
		int i;

		larrResult = new ManageLines.CoverageData[parrCoverages.length];
		for ( i = 0; i < parrCoverages.length; i++ )
		{
			larrResult[i] = new ManageLines.CoverageData();
			larrResult[i].mid = (parrCoverages[i].id == null ? null : UUID.fromString(parrCoverages[i].id));
			larrResult[i].mstrName = parrCoverages[i].name;
			larrResult[i].midSubLine = pidParent;
			larrResult[i].mbMandatory = parrCoverages[i].isMandatory;
			larrResult[i].mbHeader = parrCoverages[i].isHeader;
			larrResult[i].mstrTag = parrCoverages[i].tag;
			larrResult[i].mlngOrder = parrCoverages[i].order;
			larrResult[i].mobjPrevValues = null;
		}

		return larrResult;
	}

	private ManageCoefficients.TaxData[] BuildTaxArray(ManageCoefficients prefOp, Tax[] parrTaxes)
	{
		ManageCoefficients.TaxData[] larrResult;
		int i;

		larrResult = new ManageCoefficients.TaxData[parrTaxes.length];
		for ( i = 0; i < parrTaxes.length; i++ )
		{
			larrResult[i] = prefOp.new TaxData();
			larrResult[i].mid = (parrTaxes[i].id == null ? null : UUID.fromString(parrTaxes[i].id));
			larrResult[i].mstrName = parrTaxes[i].name;
			larrResult[i].midCoverage = (parrTaxes[i].coverageId == null ? null : UUID.fromString(parrTaxes[i].coverageId));
			larrResult[i].midType = (parrTaxes[i].fieldTypeId == null ? null : UUID.fromString(parrTaxes[i].fieldTypeId));
			larrResult[i].mstrUnits = parrTaxes[i].unitsLabel;
			larrResult[i].mstrDefault = parrTaxes[i].defaultValue;
			larrResult[i].mbVariesByObject = parrTaxes[i].variesByObject;
			larrResult[i].mbVariesByExercise = parrTaxes[i].variesByExercise;
			larrResult[i].midReferenceTo = (parrTaxes[i].refersToEntityId == null ? null :
					UUID.fromString(parrTaxes[i].refersToEntityId));
			larrResult[i].mlngColumn = parrTaxes[i].columnOrder;
			larrResult[i].mbMandatory = parrTaxes[i].mandatory;
			larrResult[i].mstrTag = parrTaxes[i].tag;
			larrResult[i].mbVisible = parrTaxes[i].visible;
			larrResult[i].mobjPrevValues = null;
		}

		return larrResult;
	}

	private void TagLines(ManageLines.LineData[] parrSource, Line[] parrLines)
	{
		int i;

		for ( i = 0; i < parrSource.length; i++ )
		{
			parrLines[i].id = parrSource[i].mid.toString();
			if ( (parrSource[i].marrSubLines != null) && (parrLines[i].subLines != null) )
				TagSubLines(parrSource[i].marrSubLines, parrLines[i].subLines);
		}
	}

	private void TagSubLines(ManageLines.SubLineData[] parrSource, SubLine[] parrSubLines)
	{
		int i;

		for ( i = 0; i < parrSource.length; i++ )
		{
			parrSubLines[i].id = parrSource[i].mid.toString();
			if ( (parrSource[i].marrCoverages != null) && (parrSubLines[i].coverages != null) )
				TagCoverages(parrSource[i].marrCoverages, parrSubLines[i].coverages);
		}
	}

	private void TagCoverages(ManageLines.CoverageData[] parrIDs, Coverage[] parrCoverages)
	{
		int i;

		for ( i = 0; i < parrIDs.length; i++ )
			parrCoverages[i].id = parrIDs[i].mid.toString();
	}

	private void TagTaxes(ManageCoefficients.TaxData[] parrIDs, Tax[] parrTaxes)
	{
		int i;

		for ( i = 0; i < parrIDs.length; i++ )
			parrTaxes[i].id = parrIDs[i].mid.toString();
	}
}
