package triangulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolygonTriangulator {

	public static Set<Edge> triangularize(int[] xpoints, int[] ypoints, int npoints) {
		Set<Edge> allEdges = new HashSet<>();
		List<Edge> allPossibleInternalEdges = new ArrayList<>();
		for (int i = 0; i < npoints; i++) {
			// stores the polygon sides as edges (current point as edge-start, next point as edge-end)
			allEdges.add(new Edge(xpoints[i], ypoints[i], xpoints[(i + 1) % npoints], ypoints[(i + 1) % npoints]));

			// creates every single point-pairs as edges. i is the current point's index, i + 1 is the next one, which is already stored in allEdges
			for (int j = i + 2; j < npoints; j++) {
				allPossibleInternalEdges.add(new Edge(xpoints[i], ypoints[i], xpoints[j], ypoints[j]));
			}
		}

		// sorts by edge length in descending order
		Collections.sort(allPossibleInternalEdges);

		allPossibleInternalEdges.stream().filter(edge ->
		// the next two conditions shows if the current edge is totally inside the polygon: no intersections, is any of its points inside
		allEdges.stream().noneMatch(edge::intersects) && // checks if current edge intersects any other edges in allEdges. If not, then OK
				contains((edge.x1 + edge.x2) / 2, (edge.y1 + edge.y2) / 2, xpoints, ypoints, npoints)) // checks if current edge's middle point is inside the polygon
				.forEach(allEdges::add);
		return allEdges;
	}

	public static boolean contains(int px, int py, int[] xpoints, int[] ypoints, int npoints) {
		boolean inside = false;
		for (int i = 0; i < npoints; i++) {
			if ((ypoints[i] > py) != (ypoints[(i + 1) % npoints] > py) &&
				px < (xpoints[(i + 1) % npoints] - xpoints[i]) * (py - ypoints[i]) / (ypoints[(i + 1) % npoints] - ypoints[i]) + xpoints[i]) {
				inside = !inside;
			}
		}
		return inside;
	}
}
