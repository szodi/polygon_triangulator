package triangulator;

import java.awt.Point;

public class Edge {

	private final Point start;
	private final Point end;

	public Edge(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public boolean isToCW(Point point) {
		return ((end.x - start.x) * (point.y - start.y) - (end.y - start.y) * (point.x - start.x)) > 0;
	}

	public Point getMiddlePoint() {
		int middleX = (start.x + end.x) / 2;
		int middleY = (start.y + end.y) / 2;
		return new Point(middleX, middleY);
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	public boolean intersects(Edge edge) {
		if (equals(edge)) {
			return true;
		}
		if (start.equals(edge.getStart()) || start.equals(edge.getEnd()) || end.equals(edge.getStart()) || end.equals(edge.getEnd())) {
			return false;
		}
		return (isToCW(edge.getStart()) == !isToCW(edge.getEnd())) && (edge.isToCW(start) == !edge.isToCW(end));
	}

	public double getLengthSq() {
		return start.distanceSq(end.x, end.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		Edge other = (Edge)obj;
		if (end == null) {
			if (other.end != null)
				return false;
		}
		else if (!end.equals(other.end) && !end.equals(other.start))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		}
		else if (!start.equals(other.start) && !start.equals(other.end))
			return false;
		return true;
	}
}
