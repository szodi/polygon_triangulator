package triangulator;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PolygonTriangulator {

	public static Set<Edge> triangularize(List<Point> points) {
		Set<Edge> allEdges = new HashSet<>();

		// edges will be sorted by their length in descending order
		Set<Edge> allPossibleInternalEdges = new TreeSet<>();
		for (int i = 0; i < points.size(); i++) {
			// stores the polygon sides as edges (current point as edge-start, next point as edge-end)
			allEdges.add(new Edge(points.get(i), points.get((i + 1) % points.size())));

			// creates every single point-pairs as edges. i is the current point's index, i + 1 is the next one, which is already stored in allEdges
			for (int j = i + 2; j < points.size(); j++) {
				allPossibleInternalEdges.add(new Edge(points.get(i), points.get(j)));
			}
		}

		allPossibleInternalEdges.stream().filter(edge ->
		// the next two conditions shows if the current edge is totally inside the polygon: no intersections, is any of its points inside
		allEdges.stream().noneMatch(edge::intersects) && // checks if current edge intersects any other edges in allEdges. If not, then OK
				contains((edge.p1.x + edge.p2.x) / 2, (edge.p1.y + edge.p2.y) / 2, points)) // checks if current edge's middle point is inside the polygon
				.forEach(allEdges::add);
		return allEdges;
	}

	public static boolean contains(int px, int py, List<Point> points) {
		boolean inside = false;
		for (int i = 0; i < points.size(); i++) {
			if ((points.get(i).y > py) != (points.get((i + 1) % points.size()).y > py)
					&& px < (points.get((i + 1) % points.size()).x - points.get(i).x) * (py - points.get(i).y) / (points.get((i + 1) % points.size()).y - points.get(i).y) + points.get(i).x) {
				inside = !inside;
			}
		}
		return inside;
	}
}
