package com.simpfi.object;

// TODO: Auto-generated Javadoc
/**
 * Creates Vehicle Type class.
 */
public class VehicleType {

	/** The id. */
	private String id;
	
	/** The v class. */
	private String vClass;

	/**
	 * Instantiates a new vehicle type.
	 *
	 * @param id the id
	 * @param vClass the v class
	 */
	public VehicleType(String id, String vClass) {
		this.id = id;
		this.vClass = vClass;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the v class.
	 *
	 * @return the v class
	 */
	public String getvClass() {
		return vClass;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Vehicle Type.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "VehicleType [id=" + id + ", vClass=" + vClass + "]";
	}

}
