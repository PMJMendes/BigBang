package bigBang.module.generalSystemModule.interfaces;

import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;

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

	public Line[] getLines();
	public Line createLine(Line b);
	public Line saveLine(Line b);
	public void deleteLine(String id);

	public SubLine createSubLine(SubLine m);
	public SubLine saveSubLine(SubLine m);
	public void deleteSubLine(String id);

	public Coverage createCoverage(Coverage b);
	public Coverage saveCoverage(Coverage b);
	public void deleteCoverage(String id);
}
