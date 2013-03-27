
public class Cube {
//represent a singular cube, one of 27 in this game of rubicks cube
	private Color  color0; //x
	private Color  color1; //y
	private Color  color2; //z
	private int[][] position = new int[3][3]; //where the cube is initially placed
	
	public Cube(){
		this.color0 = null;
		this.color1 = null;
		this.color2 = null;
		position = null;
	}
	
	public Cube (Color color0 ,Color color1 ,Color color2){
		this.color0 = color0;
		this.color1=  color1;
		this.color2 = color2;
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
}
