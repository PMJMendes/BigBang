package bigBang.module.generalSystemModule.server;

import java.util.UUID;

import bigBang.library.server.EngineImplementor;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;
import bigBang.module.generalSystemModule.shared.Tax;

public class CoveragesServiceImpl
	extends EngineImplementor
	implements CoveragesService
{
	private static final long serialVersionUID = 1L;

	public Line[] getLines()
	{
		return null;
	}

	public Line createLine(Line b)
	{
		return null;
	}

	public Line saveLine(Line b)
	{
		return null;
	}

	public void deleteLine(String id)
	{
	}

	public SubLine createSubLine(SubLine m)
	{
		return null;
	}

	public SubLine saveSubLine(SubLine m)
	{
		return null;
	}

	public void deleteSubLine(String id)
	{
	}

	public Coverage createCoverage(Coverage b)
	{
		return null;
	}

	public Coverage saveCoverage(Coverage b)
	{
		return null;
	}

	public void deleteCoverage(String id)
	{
	}

	private SubLine[] getSubLinesForLine(UUID pidLine)
	{
		return null;
	}
	
	private Coverage[] getCoveragesForSubLine(UUID pidSubLine)
	{
		return null;
	}

	private Tax[] getTaxesForCoverage(UUID pidCoverage)
	{
		return null;
	}
}
