package bigBang.library.client.userInterface;

public interface Refreshable <T> {

	void refresh();
	
	void refresh(T value);
	
	void refreshAndKeepState(boolean keepstate);
	
	void refreshAndKeepState(boolean keepstate, T value);
	
}
