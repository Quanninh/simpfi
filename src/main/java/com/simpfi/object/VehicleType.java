package com.simpfi.object;

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

	@Override
	public String toString() {
		return "VehicleType [id=" + id + ", vClass=" + vClass + "]";
	}
	
}
