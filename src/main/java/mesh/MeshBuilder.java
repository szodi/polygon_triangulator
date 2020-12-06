package mesh;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.shape.TriangleMesh;

import triangulator.Edge;
import triangulator.PolygonTriangulator;

public class MeshBuilder {

	List<Point> points;
	Set<Edge> edges;

	TriangleMesh mesh;

	public MeshBuilder(TriangleMesh mesh) {
		this.mesh = mesh;
	}

	public TriangleMesh buildMesh(List<Point> points) {
		this.points = points;
		this.edges = PolygonTriangulator.triangularize(points);
		float minX = 0.0f;
		float minY = 0.0f;
		float maxX = 1920.0f;
		float maxY = 1080.0f;
		float[] meshPoints = new float[points.size() * 3];
		float[] texCoords = new float[points.size() * 2];
		Set<Drill> faceDrills = new HashSet<>();
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			meshPoints[(i * 3) + 0] = (float)point.getX();
			meshPoints[(i * 3) + 1] = (float)point.getY();
			meshPoints[(i * 3) + 2] = 0f;

			texCoords[(i * 2) + 0] = (float)((point.getX() - minX) / (maxX - minX));
			texCoords[(i * 2) + 1] = (float)((point.getY() - minY) / (maxY - minY));

			Set<Point> neighbourPoints = getNeighbours(point);

			for (Point neighbourPoint : neighbourPoints) {
				Set<Point> neighbourNeighbourPoints = getNeighbours(neighbourPoint);
				for (Point neighbourNeighbourPoint : neighbourNeighbourPoints) {
					if (neighbourPoints.contains(neighbourNeighbourPoint)) {
						int index2 = points.indexOf(neighbourPoint);
						int index3 = points.indexOf(neighbourNeighbourPoint);
						int number1 = i;
						int number2 = index2;
						int number3 = index3;
						Edge edge = new Edge(points.get(i), points.get(index2));
						Point p3 = points.get(index3);
						if (edge.isToCW(p3)) {
							number2 = index3;
							number3 = index2;
						}
						faceDrills.add(new Drill(number1, number2, number3));
					}
				}
			}
		}
		int[] faces = new int[faceDrills.size() * 6];
		int j = 0;
		for (Drill drill : faceDrills) {
			faces[j++] = drill.number1;
			faces[j++] = drill.number1;
			faces[j++] = drill.number2;
			faces[j++] = drill.number2;
			faces[j++] = drill.number3;
			faces[j++] = drill.number3;
		}
		mesh.getPoints().setAll(meshPoints);
		mesh.getTexCoords().setAll(texCoords);
		mesh.getFaces().setAll(faces);
		return mesh;
	}

	private Set<Point> getNeighbours(Point point) {
		return edges.parallelStream().filter(edge -> edge.p1.equals(point) || edge.p2.equals(point)).map(edge -> edge.p1.equals(point) ? edge.p2 : edge.p1).collect(Collectors.toSet());
	}

	private static final class Drill {
		int number1;
		int number2;
		int number3;

		private Set<Integer> drill = new LinkedHashSet<>();

		public Drill(int number1, int number2, int number3) {
			this.number1 = number1;
			this.number2 = number2;
			this.number3 = number3;
			drill.add(number1);
			drill.add(number2);
			drill.add(number3);
		}

		public Set<Integer> getDrill() {
			return drill;
		}

		@Override
		public int hashCode() {
			return -1;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Drill other = (Drill)obj;
			return drill.equals(other.getDrill());
		}
	}
}
