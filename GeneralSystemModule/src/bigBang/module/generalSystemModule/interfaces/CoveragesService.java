package bigBang.module.generalSystemModule.interfaces;

import bigBang.module.generalSystemModule.shared.CoverageBranch;
import bigBang.module.generalSystemModule.shared.CoverageCategory;
import bigBang.module.generalSystemModule.shared.CoverageModality;
import bigBang.module.generalSystemModule.shared.CoverageTax;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("CoveragesService")
public interface CoveragesService extends RemoteService {
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
	
	public CoverageCategory[] getCoverages();
	
	public String createCategory(CoverageCategory c);
	
	public String createModality(CoverageModality m);
	
	public String createBranch(CoverageBranch b);
	
	public String createTax(CoverageTax t);
	
	public void deleteCategory(String id);
	
	public void deleteModality(String id);
	
	public void deleteBranch(String id);
	
	public void deleteTax(String id);
}
