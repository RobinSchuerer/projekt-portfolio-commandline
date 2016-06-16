package com.project.portfolio.utility;

public enum TeamColor {
	
	TeamColor1(0, "#00802b"),
	TeamColor2(1, "#e68a00"),
	TeamColor3(2, "#5c8a8a"),
	TeamColor4(3, "#ff3399"),
	TeamColor5(4, "#993333"),
	TeamColor6(5, "#660033"),
	TeamColor7(6, "#990099"),
	TeamColor8(7, "#730099"),
	TeamColor9(8, "#bf80ff"),
	TeamColor10(9, "#806000"),
	TeamColor11(10, "#334d4d"),
	TeamColor12(11, "#5c5c8a"),
	TeamColor13(12, "#862d59"),
	TeamColor14(13, "#ac3939"),
	TeamColor15(14, "#77773c");
	
	private final int index;
	private final String color;
	
	TeamColor(int index, String color) {
		this.index = index;
		this.color = color;
	}
	
	private int getIndex() {
		return this.index;
	}
	private String getColor() {
		return this.color;
	}
	
	public static String getTeamColor(int index) {
		String color = null;
		for(TeamColor teamColor : TeamColor.values()) {
			if(teamColor.getIndex() == index) {
				color = teamColor.getColor();
			}
		}
		return color;
	}
	
	
	
	

}
