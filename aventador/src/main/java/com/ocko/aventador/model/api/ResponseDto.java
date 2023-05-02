/**
 * 
 */
package com.ocko.aventador.model.api;

/**
 * @author ok
 *
 */
public class ResponseDto {

	private String state;
	private String message;
	/**
	 * @return {@link #state}
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state {@link #state}
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return {@link #message}
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message {@link #message}
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
