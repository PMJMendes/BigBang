package bigBang.module.generalSystemModule.shared;

public class SessionGeneralSystem extends GeneralSystem {

	private static final long serialVersionUID = 1L;
	
	protected static GeneralSystem instance;
	
	public static GeneralSystem getInstance() {
		return SessionGeneralSystem.instance;
	}
	
	public static void setGeneralSystem(GeneralSystem instance) {
		SessionGeneralSystem.instance = instance;
	}

}
