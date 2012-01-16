package bigBang.library.client;

public class Session {

	private static String userId;
	private static String username;
	private static String displayName;
	private static String domain;
	
	public static void setUserId(String userId){
		Session.userId = userId;
	}
	
	public static void setUsername(String username){
		Session.username = username;
	}

	public static void setDisplayName(String displayName){
		Session.displayName = displayName;
	}

	public static void setDomain(String domain){
		Session.domain = domain;
	}
	
	public static String getUserId(){
		return Session.userId;
	}
	
	public static String getUsername(){
		return Session.username;
	}

	public static String getDisplayName(){
		return Session.displayName;
	}
	
	public static String getDomain(){
		return Session.domain;
	}
	
}
