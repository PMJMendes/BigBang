package bigBang.library.client;

public interface HasMetaData<T> {
	
	public void setMetaData(T[] data);
	
	public T[] getMetaData();
	
}
