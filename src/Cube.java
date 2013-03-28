
public class Cube {
//represent a singular cube, one of 27 in this game of rubicks cube
	private Color color0;  //front
	private Color color1;  //right
	private Color color2;  //left
	private Color color3;  //top
	private Color color4;  //bottom
	private Color color5;  //back
	
	public Cube (Color color0 ,Color color1 ,Color color2, Color color3,
				 Color color4, Color color5){
		this.color0 = color0;
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.color4 = color4;
		this.color5 = color5;
	}

	public Color getColor0() {
		return color0;
	}

	public void setColor0(Color color0) {
		this.color0 = color0;
	}

	public Color getColor1() {
		return color1;
	}

	public void setColor1(Color color1) {
		this.color1 = color1;
	}

	public Color getColor2() {
		return color2;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
	}

	public Color getColor3() {
		return color3;
	}

	public void setColor3(Color color3) {
		this.color3 = color3;
	}

	public Color getColor4() {
		return color4;
	}

	public void setColor4(Color color4) {
		this.color4 = color4;
	}

	public Color getColor5() {
		return color5;
	}

	public void setColor5(Color color5) {
		this.color5 = color5;
	}

}
