
public class CubeFace {
//represents one face of the 6 faced rubicks cube
	private Cube[][] face;

	public Cube[][] getFace() {
		return face;
	}

	public void setFace(Cube[][] face) {
		this.face = face;
	}
	
	public void setFace(int x, int y, Cube c){
		this.face[x][y] = c;
	}
}
