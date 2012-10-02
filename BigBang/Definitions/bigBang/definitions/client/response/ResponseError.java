package bigBang.definitions.client.response;

public class ResponseError {
	public static enum ErrorLevel {
		TECHNICAL,
		USER
	}

	public ErrorLevel code;
	public String description;
}
