package com.simpfi.object;

/**
 * Creates Vehicle Type class.
 */
public class VehicleType {

	private String id;
	private String vClass;

	public VehicleType(String id, String vClass) {
		this.id = id;
		this.vClass = vClass;
	}

	public String getId() {
		return id;
	}

	public String getvClass() {
		return vClass;
	}

	/**
	 * Overrides the built-in method toString() to provide a human-readable
	 * representation of Vehicle Type.
	 */
	@Override
	public String toString() {
		return "VehicleType [id=" + id + ", vClass=" + vClass + "]";
	}

}
