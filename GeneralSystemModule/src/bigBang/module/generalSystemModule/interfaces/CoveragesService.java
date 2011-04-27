package bigBang.module.generalSystemModule.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;
import bigBang.module.generalSystemModule.shared.Tax;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("CoveragesService")
public interface CoveragesService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static CoveragesServiceAsync instance;
		public static CoveragesServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(CoveragesService.class);
			}
			return instance;
		}
	}

	public Line[] getLines() throws SessionExpiredException, BigBangException;

	public Line createLine(Line b) throws SessionExpiredException, BigBangException;
	public Line saveLine(Line b) throws SessionExpiredException, BigBangException;
	public void deleteLine(String id) throws SessionExpiredException, BigBangException;

	public SubLine createSubLine(SubLine m) throws SessionExpiredException, BigBangException;
	public SubLine saveSubLine(SubLine m) throws SessionExpiredException, BigBangException;
	public void deleteSubLine(String id) throws SessionExpiredException, BigBangException;

	public Coverage createCoverage(Coverage b) throws SessionExpiredException, BigBangException;
	public Coverage saveCoverage(Coverage b) throws SessionExpiredException, BigBangException;
	public void deleteCoverage(String id) throws SessionExpiredException, BigBangException;
	
	public Tax createTax(Tax b) throws SessionExpiredException, BigBangException;
	public Tax saveTax(Tax b) throws SessionExpiredException, BigBangException;
	public void deleteTax(String id) throws SessionExpiredException, BigBangException;
}
