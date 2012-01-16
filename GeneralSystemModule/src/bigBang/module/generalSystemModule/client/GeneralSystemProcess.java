package bigBang.module.generalSystemModule.client;

public class GeneralSystemProcess {

	public static class Util {
		private static GeneralSystemProcess instance;
		
		public static GeneralSystemProcess getInstance(){
			if(instance == null){
				instance = new GeneralSystemProcess();
			}
			return instance;
		}
	}
	
	private String[] permissions;
	
	public void setPermissions(String[] permissions){
		this.permissions = permissions;
	}
	
	public String[] getPermissions(){
		return this.permissions;
	}
	
	public boolean hasPermission(String permission) {
//		for(int i = 0; i < permissions.length; i++) {
//			if(permissions[i].equalsIgnoreCase(permission)){
//				return true;
//			}
//		}
//		return false;
		//TODO
		return true;
	}
	
	public static GeneralSystemProcess getInstance(){
		return GeneralSystemProcess.Util.getInstance();
	}
	
}
