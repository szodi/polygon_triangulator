package triangulator;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolygonTriangulator {

	public static Set<Edge> triangularize(Polygon polygon) {
		Set<Edge> allEdges = new HashSet<>();
		List<Edge> allPossibleInternalEdges = new ArrayList<>();
		for (int i = 0; i < polygon.npoints; i++) {
			// stores the polygon sides as edges (current point as edge-start, next point as edge-end)
			allEdges.add(new Edge(polygon.xpoints[i], polygon.ypoints[i], polygon.xpoints[(i + 1) % polygon.npoints], polygon.ypoints[(i + 1) % polygon.npoints]));

			// creates every single point-pairs as edges. i is the current point's index, i + 1 is the next one, which is already stored in allEdges
			for (int j = i + 2; j < polygon.npoints; j++) {
				allPossibleInternalEdges.add(new Edge(polygon.xpoints[i], polygon.ypoints[i], polygon.xpoints[j], polygon.ypoints[j]));
			}
		}

		// sorts by edge length in descending order
		Collections.sort(allPossibleInternalEdges);

		allPossibleInternalEdges.stream().filter(edge ->
		// the next two conditions shows if the current edge is totally inside the polygon: no intersections, is any of its points inside
				allEdges.stream().noneMatch(edge::intersects) && // checks if current edge intersects any other edges in allEdges. If not, then OK
				polygon.contains((edge.x1 + edge.x2) / 2, (edge.y1 + edge.y2) / 2)) // checks if current edge's middle point is inside the polygon
				.forEach(allEdges::add);
		return allEdges;
	}
}
