package bigBang.library.client;

import com.google.gwt.user.client.ui.HasValue;

public interface HasEditableValue<T> extends HasValue<T>, Editable {
	
	/**
	 * Sets the value info currently held
	 * @param info The info to be set
	 */
	public void setInfo(T info);
	
	/**
	 * Gets the info currently held
	 */
	public T getInfo();
	
	/**
	 * Sets the value to the info being held
	 */
	public void commit();
	
	/**
	 * Restores the info to the value
	 */
	public void revert();
	
}
