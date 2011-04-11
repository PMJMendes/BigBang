package bigBang.module.generalSystemModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.CoverageBranch;
import bigBang.module.generalSystemModule.shared.CoverageCategory;
import bigBang.module.generalSystemModule.shared.CoverageModality;
import bigBang.module.generalSystemModule.shared.CoverageTax;

public interface CoveragesServiceAsync extends Service {

	void createBranch(CoverageBranch b, AsyncCallback<String> callback);

	void createCategory(CoverageCategory c, AsyncCallback<String> callback);

	void createModality(CoverageModality m, AsyncCallback<String> callback);
	
	void createTax(CoverageTax t, AsyncCallback<String> callback);

	void deleteBranch(String id, AsyncCallback<Void> callback);

	void deleteCategory(String id, AsyncCallback<Void> callback);

	void deleteModality(String id, AsyncCallback<Void> callback);

	void deleteTax(String id, AsyncCallback<Void> callback);

	void getCoverages(AsyncCallback<CoverageCategory[]> callback);

}
