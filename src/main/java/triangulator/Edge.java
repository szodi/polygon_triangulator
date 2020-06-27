package triangulator;

public class Edge implements Comparable<Edge> {

	public final int x1;
	public final int y1;
	public final int x2;
	public final int y2;

	public Edge(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public boolean isToCW(int px, int py) {
		return ((x2 - x1) * (py - y1) - (y2 - y1) * (px - x1)) > 0;
	}

	public boolean intersects(Edge edge) {
		if (x1 == edge.x1 && y1 == edge.y1) {
			return x2 == edge.x2 && y2 == edge.y2;
		}
		if (x2 == edge.x2 && y2 == edge.y2) {
			return x1 == edge.x1 && y1 == edge.y1;
		}
		if (x1 == edge.x2 && y1 == edge.y2) {
			return x2 == edge.x1 && y2 == edge.y1;
		}
		if (x2 == edge.x1 && y2 == edge.y1) {
			return x1 == edge.x2 && y1 == edge.y2;
		}
		return (isToCW(edge.x1, edge.y1) == !isToCW(edge.x2, edge.y2)) && (edge.isToCW(x1, y1) == !edge.isToCW(x2, y2));
	}

	public int getLengthSq() {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}

	@Override
	public int compareTo(Edge edge) {
		return getLengthSq() - edge.getLengthSq();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x1;
		result = prime * result + x2;
		result = prime * result + y1;
		result = prime * result + y2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge edge = (Edge)obj;
		return (((x1 == edge.x1 && y1 == edge.y1) && (x2 == edge.x2 && y2 == edge.y2)) || ((x1 == edge.x2 && y1 == edge.y2) && (x2 == edge.x1 && y2 == edge.y1)));
	}
}
