package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;


public class CoverageCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String id;
	public String name;
	
	public CoverageBranch[] branches;

}
