package com.bing.common.ecxcption;

public class AllPortBeenOccupied extends Exception {
	public AllPortBeenOccupied() {
		super("All port in range of 5555 to 65500 have been occpuied");
	}
}
