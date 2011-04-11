package bigBang.module.generalSystemModule.server;

import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.shared.CoverageBranch;
import bigBang.module.generalSystemModule.shared.CoverageCategory;
import bigBang.module.generalSystemModule.shared.CoverageModality;
import bigBang.module.generalSystemModule.shared.CoverageTax;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CoveragesServiceImpl extends RemoteServiceServlet implements CoveragesService {
	
	private static final long serialVersionUID = 1L;

	@Override
	public CoverageCategory[] getCoverages() {
		CoverageCategory[] categories = new CoverageCategory[1];
		CoverageBranch[] branches = new CoverageBranch[1];
		CoverageModality[] modalities = new CoverageModality[1];
		CoverageTax[] taxes = new CoverageTax[1];
		
		CoverageCategory c = new CoverageCategory();
		c.name = "categoria";
		c.branches = branches;
		categories[0] = c;
		
		CoverageBranch b = new CoverageBranch();
		b.name = "ramo";
		b.modalities = modalities;
		branches[0] = b;
		
		CoverageModality m = new CoverageModality();
		m.name = "mod";
		m.taxes = taxes;
		modalities[0] = m;
		
		CoverageTax t = new CoverageTax();
		t.name = "IVA";
		t.value = 12;
		t.currency ="€";
		taxes[0] = t;
		
		return categories;
	}

	@Override
	public String createCategory(CoverageCategory c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createModality(CoverageModality m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createBranch(CoverageBranch b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createTax(CoverageTax t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCategory(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteModality(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBranch(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTax(String id) {
		// TODO Auto-generated method stub
		
	}
}
