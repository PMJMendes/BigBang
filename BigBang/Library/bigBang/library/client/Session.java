package bigBang.library.client;

public class Session {
	private static String userId;
	private static String username;
	private static String displayName;
	private static String domain;
	private static boolean isRoot;
	private static boolean isAgent;
	private static boolean hasDocuShare;

	public static void setUserId(String userId) {
		Session.userId = userId;
	}
	
	public static void setUsername(String username) {
		Session.username = username;
	}

	public static void setDisplayName(String displayName) {
		Session.displayName = displayName;
	}

	public static void setDomain(String domain) {
		Session.domain = domain;
	}
	
	public static void setIsRoot(boolean isRoot) {
		Session.isRoot = isRoot;
	}
	
	public static void setIsAgent(boolean isAgent) {
		Session.isAgent = isAgent;
	}

	public static void setHasDocushare(boolean hasDocuShare) {
		Session.hasDocuShare = hasDocuShare;
	}

	public static String getUserId() {
		return Session.userId;
	}
	
	public static String getUsername() {
		return Session.username;
	}

	public static String getDisplayName() {
		return Session.displayName;
	}
	
	public static String getDomain() {
		return Session.domain;
	}
	
	public static boolean isRoot() {
		return Session.isRoot;
	}

	public static boolean isAgent() {
		return Session.isAgent;
	}

	public static boolean hasDocuShare() {
		return Session.hasDocuShare;
	}

	public static void invalidate(){
		setUserId(null);
		setUsername(null);
		setDisplayName(null);
		setDomain(null);
		setIsRoot(false);
		setIsAgent(true);
	}

	public static boolean isValid() {
		return Session.userId != null;
	}
}
