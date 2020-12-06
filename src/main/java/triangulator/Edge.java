package triangulator;

import java.awt.Point;

public class Edge implements Comparable<Edge> {

	public final Point p1;
	public final Point p2;

	public Edge(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public boolean isConnectedTo(Edge edge) {
		return p1.equals(edge.p1) || p1.equals(edge.p2) || p2.equals(edge.p1) || p2.equals(edge.p2);
	}

	public boolean isToCW(Point p) {
		return ((p2.x - p1.x) * (p.y - p1.y) - (p2.y - p1.y) * (p.x - p1.x)) > 0;
	}

	public boolean intersects(Edge edge) {
		if (p1.equals(edge.p1)) {
			return p2.equals(edge.p2);
		}
		if (p2.equals(edge.p2)) {
			return p1.equals(edge.p1);
		}
		if (p1.equals(edge.p2)) {
			return p2.equals(edge.p1);
		}
		if (p2.equals(edge.p1)) {
			return p1.equals(edge.p2);
		}
		return (isToCW(edge.p1) == !isToCW(edge.p2)) && (edge.isToCW(p1) == !edge.isToCW(p2));
	}

	public int getLengthSq() {
		return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
	}

	@Override
	public int compareTo(Edge edge) {
		return getLengthSq() - edge.getLengthSq();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + p1.x;
		result = prime * result + p2.x;
		result = prime * result + p1.y;
		result = prime * result + p2.y;
		return 31;
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
		return ((p1.equals(edge.p1) && p2.equals(edge.p2)) || (p1.equals(edge.p2) && p2.equals(edge.p1)));
	}

	@Override
	public String toString() {
		return "Edge [p1.x=" + p1.x + ", p1.y=" + p1.y + ", p2.x=" + p2.x + ", p2.y=" + p2.y + "]";
	}
}
