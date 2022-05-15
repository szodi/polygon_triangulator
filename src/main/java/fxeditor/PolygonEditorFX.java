package fxeditor;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;

import mesh.MeshBuilder;

// Usage: draw a polygon, and hit 'H', then modify the dots
public class PolygonEditorFX extends Application implements EventHandler<MouseEvent> {

	private static String IMAGE_FILE_1 = "<image-file-path-here>";

	public Canvas canvas = new Canvas();
	private AnchorPane anchorPane = new AnchorPane();

	TriangleMesh mesh = new TriangleMesh();
	Image image = new Image(IMAGE_FILE_1);
	ImageView imageView = new ImageView(image);
	PhongMaterial material = new PhongMaterial();

	MeshView meshView = new MeshView(mesh);

	protected GraphicsContext gc;

	private static final int DOT_RADIUS = 9;

	List<Point> points = new LinkedList<>();
	MeshBuilder mb = new MeshBuilder(mesh);

	Point activePoint;

	// boolean showTriangleEdges = false;
	boolean showMesh = false;

	public void paintGraphics(GraphicsContext gc) {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.setFill(Color.BLACK);
		// if (points != null) {
		// if (showTriangleEdges) {
		// gc.setFill(Color.GREEN);
		// drawEdges(gc, PolygonTriangulator.triangularize(points));
		// }
		// }
		int i = 0;
		for (Point point : points) {
			gc.setFill(point.equals(activePoint) ? Color.PINK : Color.RED);
			drawDot(gc, point.x, point.y, String.valueOf(i++));
		}
	}

	// public void drawEdges(GraphicsContext gc, Collection<Edge> edges) {
	// for (Edge edge : edges) {
	// drawDashedLine(gc, edge.p1, edge.p2);
	// }
	// }

	// private void drawDashedLine(GraphicsContext gc, Point p1, Point p2) {
	// gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
	// }

	private void drawDot(GraphicsContext g, int x, int y, String caption) {
		g.fillRect(x - DOT_RADIUS, y - DOT_RADIUS, 2 * DOT_RADIUS + 1, 2 * DOT_RADIUS + 1);
		g.setFill(Color.YELLOW);
	}

	private Point getPointAt(Point cursor) {
		for (Point point : points) {
			if ((cursor.x >= point.x - DOT_RADIUS && cursor.x <= point.x + 2 * DOT_RADIUS + 1) && (cursor.y >= point.y - DOT_RADIUS && cursor.y <= point.y + 2 * DOT_RADIUS + 1)) {
				return point;
			}
		}
		return null;
	}

	@Override
	public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
			handleMouseMoved(mouseEvent);
		}
		if (mouseEvent.getButton() == MouseButton.PRIMARY) {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				handlePrimaryMousePressed(mouseEvent);
			}
			else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				handlePrimaryMouseDragged(mouseEvent);
			}
		}
		else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				handleSecondaryMousePressed(mouseEvent);
			}
		}
	}

	protected void handleMouseMoved(MouseEvent event) {
		activePoint = getPointAt(new Point((int)event.getSceneX(), (int)event.getSceneY()));
		paintGraphics(gc);
	}

	protected void handlePrimaryMousePressed(MouseEvent event) {
		if (activePoint == null) {
			activePoint = new Point((int)event.getSceneX(), (int)event.getSceneY());
			points.add(activePoint);
			paintGraphics(gc);
		}
	}

	protected void handleSecondaryMousePressed(MouseEvent event) {
		points.remove(activePoint);
		activePoint = null;
		paintGraphics(gc);
	}

	protected void handlePrimaryMouseDragged(MouseEvent event) {
		if (activePoint != null) {
			activePoint.setLocation(new Point((int)event.getSceneX(), (int)event.getSceneY()));
			if (showMesh) {
				float[] meshPoints = new float[points.size() * 3];
				for (int i = 0; i < points.size(); i++) {
					Point point = points.get(i);
					meshPoints[(i * 3) + 0] = (float)point.getX();
					meshPoints[(i * 3) + 1] = (float)point.getY();
					meshPoints[(i * 3) + 2] = 0f;
				}
				mesh.getPoints().setAll(meshPoints);
			}
			paintGraphics(gc);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		anchorPane.setOnMouseMoved(this);
		anchorPane.setOnMousePressed(this);
		anchorPane.setOnMouseDragged(this);
		anchorPane.getChildren().add(imageView);
		anchorPane.getChildren().add(meshView);
		anchorPane.getChildren().add(canvas);
		material.setDiffuseMap(image);
		meshView.setMaterial(material);
		Scene scene = new Scene(anchorPane, 1920, 1080, true, SceneAntialiasing.BALANCED);
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.H) {
				// showTriangleEdges = !showTriangleEdges;
				showMesh = !showMesh;
				TriangleMesh triangleMesh = mb.buildMesh(points);
				mesh.getPoints().setAll(triangleMesh.getPoints());
				mesh.getTexCoords().setAll(triangleMesh.getTexCoords());
				mesh.getFaces().setAll(triangleMesh.getFaces());
				paintGraphics(gc);
			}
			else if (e.getCode() == KeyCode.R) {
				points.clear();
				paintGraphics(gc);
			}
		});

		canvas.setWidth(5000);
		canvas.setHeight(5000);

		gc = canvas.getGraphicsContext2D();

		paintGraphics(gc);

		primaryStage.setScene(scene);
		// primaryStage.setFullScreen(true);
		// primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
