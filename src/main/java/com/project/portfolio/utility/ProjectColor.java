package com.project.portfolio.utility;

public enum ProjectColor {

	ProjectColor1(0, "#ff6666"),
	ProjectColor2(1, "#66ffff"),
	ProjectColor3(2, "#6666ff"),
	ProjectColor4(3, "#910808"),
	ProjectColor5(4, "#8c66ff"),
	ProjectColor6(5, "#00e6b8"),
	ProjectColor7(6, "#990099"),
	ProjectColor8(7, "#ccccff"),
	ProjectColor9(8, "#00cc99"),
	ProjectColor10(9, "#669900"),
	ProjectColor11(10, "#ff8080"),
	ProjectColor12(11, "#ff66ff"),
	ProjectColor13(12, "#b3b3ff"),
	ProjectColor14(13, "#b3b3b3"),
	ProjectColor15(14, "#80ffaa"),
	ProjectColor16(15, "#a3c2c2"),
	ProjectColor17(16, "#8585ad"),
	ProjectColor18(17, "#ccb3ff"),
	ProjectColor19(18, "#009999"),
	ProjectColor20(19, "#a3a375"),
	ProjectColor21(20, "#c653c6"),
	ProjectColor22(21, "#00cccc"),
	ProjectColor23(22, "#00e6e6"),
	ProjectColor24(23, "#ff8000"),
	ProjectColor25(24, "#1ac6ff"),
	ProjectColor26(25, "#85e0e0"),
	ProjectColor27(26, "#53c68c"),
	ProjectColor28(27, "#cc6699"),
	ProjectColor29(28, "#00bfff"),
	ProjectColor30(29, "#79d279"),
	ProjectColor31(30, "#aaaa55"),
	ProjectColor32(31, "#ff884d"),
	ProjectColor33(32, "#cccc00"),
	ProjectColor34(33, "#b3c6ff"),
	ProjectColor35(34, "#ff66a3"),
	ProjectColor36(35, "#00cc44"),
	ProjectColor37(36, "#33ccff"),
	ProjectColor38(37, "#c6538c"),
	ProjectColor39(38, "#ff3333"),
	ProjectColor40(39, "#999900");
	
	
	private final int index;
	private final String color;
	
	ProjectColor(int index, String color) {
		this.index = index;
		this.color = color;
	}
	
	private int getIndex() {
		return this.index;
	}
	private String getColor() {
		return this.color;
	}
	
	public static String getProjectColor(int index) {
		String color = null;
		for(ProjectColor ProjectColor : ProjectColor.values()) {
			if(ProjectColor.getIndex() == index) {
				color = ProjectColor.getColor();
			}
		}
		return color;
	}
	
	
	
	

}