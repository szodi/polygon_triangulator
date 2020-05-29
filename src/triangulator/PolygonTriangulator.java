package triangulator;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PolygonTriangulator {

	private Polygon polygon;
	private List<Point> points = new ArrayList<>();
	private Set<Edge> edges = new HashSet<>();

	public PolygonTriangulator(Polygon polygon) {
		this.polygon = polygon;
		Point lastPoint = new Point(polygon.xpoints[polygon.npoints - 1], polygon.ypoints[polygon.npoints - 1]);
		for (int i = 0; i < polygon.npoints; i++) {
			Point point = new Point(polygon.xpoints[i], polygon.ypoints[i]);
			points.add(point);
			edges.add(new Edge(lastPoint, point));
			lastPoint = point;
		}
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	public Set<Edge> triangularize() {
		Set<Edge> allEdges = edges.parallelStream().collect(Collectors.toSet());
		List<Edge> allPossibleEdges = new ArrayList<>();
		for (int i = 0; i < points.size() - 1; i++) {
			for (int j = i; j < points.size(); j++) {
				allPossibleEdges.add(new Edge(points.get(i), points.get(j)));
			}
		}
		Collections.sort(allPossibleEdges);
		for (Edge edge : allPossibleEdges) {
			if (allEdges.stream().noneMatch(edge::intersects) && polygon.contains(edge.getMiddlePoint())) {
				allEdges.add(edge);
			}
		}
		return allEdges;
	}
}
