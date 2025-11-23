package com.simpfi.object;

public class Phase {
	private double duration;
	private String state;
	
	public Phase(double duration, String state) {
		// TODO Auto-generated constructor stub
		this.duration = duration;
		this.state = state;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Phase [duration=" + duration + ", state=" + state + "]";
	}
	
	
	
}
