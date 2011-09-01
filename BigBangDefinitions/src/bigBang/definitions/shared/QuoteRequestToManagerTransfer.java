package bigBang.definitions.shared;

import java.io.Serializable;

public class QuoteRequestToManagerTransfer implements Serializable {

	public static enum Status implements Serializable {
		ACCEPTED,
		REJECTED,
		PENDING
	}
	
	private static final long serialVersionUID = 1L;

	public String quoteRequestId;
	public String managerId;
	public Status status;
	
}
