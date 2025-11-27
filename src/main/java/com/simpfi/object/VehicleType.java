package com.simpfi.object;

/**
 * Creates Vehicle Type class.
 */
public class VehicleType {

	/** The vehicle type id. */
	private String id;

	/** The vehicle class. */
	private String vClass;

	/**
	 * Instantiates a new vehicle type.
	 *
	 * @param id     the id
	 * @param vClass the vehicle class
	 */
	public VehicleType(String id, String vClass) {
		this.id = id;
		this.vClass = vClass;
	}

	/**
	 * Returns the vehicle type id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the vehicle class.
	 *
	 * @return the vehicle class
	 */
	public String getvClass() {
		return vClass;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Vehicle Type.
	 *
	 * @return the representation of Vehicle Type
	 */
	@Override
	public String toString() {
		return "VehicleType [id=" + id + "]";
	}

}
