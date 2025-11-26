package com.simpfi.object;

import org.geotools.xml.xLink.XLinkSchema.To;

public class Connection {
	private Lane fromLane;
	private Lane toLane;
	
	public Connection(Lane fromLane, Lane toLane)
	{
		this.fromLane = fromLane;
		this.toLane = toLane;
	}

	public Lane getFromLane() {
		return fromLane;
	}

	public void setFromLane(Lane fromLane) {
		this.fromLane = fromLane;
	}

	public Lane getToLane() {
		return toLane;
	}

	public void setToLane(Lane toLane) {
		this.toLane = toLane;
	}
	
	
}
