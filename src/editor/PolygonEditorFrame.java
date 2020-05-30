package editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import triangulator.Edge;
import triangulator.PolygonTriangulator;

public class PolygonEditorFrame extends BufferedFrame implements MouseListener, MouseMotionListener, KeyListener {

	private static final long serialVersionUID = -8339406863421647963L;

	private static final int DOT_RADIUS = 9;

	List<Point> points = new LinkedList<>();

	Point activePoint;

	boolean showTriangleEdges = false;

	public PolygonEditorFrame() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}

	@Override
	public void paintGraphics(Graphics g) {
		g.setColor(Color.black);
		Polygon polygon = createPolygon();
		if (polygon != null) {
			g.drawPolygon(polygon);
			if (showTriangleEdges) {
				g.setColor(Color.green);
				drawEdges(g, PolygonTriangulator.triangularize(polygon));
			}
		}
		int i = 0;
		for (Point point : points) {
			g.setColor(point == activePoint ? Color.pink : Color.red);
			drawDot(g, point.x, point.y, String.valueOf(i++));
		}
	}

	public void drawEdges(Graphics g, Collection<Edge> edges) {
		for (Edge edge : edges) {
			drawDashedLine(g, edge.x1, edge.y1, edge.x2, edge.y2);
		}
	}

	public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2) {
		Graphics2D g2d = (Graphics2D)g.create();
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9}, 0);
		g2d.setStroke(dashed);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}

	private Polygon createPolygon() {
		if (points.size() > 0) {
			int[] xpoints = new int[points.size()];
			int[] ypoints = new int[points.size()];
			int npoints = 0;
			for (Point point : points) {
				xpoints[npoints] = point.x;
				ypoints[npoints] = point.y;
				npoints++;
			}
			return new Polygon(xpoints, ypoints, npoints);
		}
		return null;
	}

	private void drawDot(Graphics g, int x, int y, String caption) {
		g.fillRect(x - DOT_RADIUS, y - DOT_RADIUS, 2 * DOT_RADIUS + 1, 2 * DOT_RADIUS + 1);
		g.setColor(Color.yellow);
		int textLength = g.getFontMetrics().stringWidth(caption);
		g.drawString(caption, x - textLength / 2, y + DOT_RADIUS / 2 + 1);
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
	public void mouseDragged(MouseEvent e) {
		if (activePoint != null) {
			activePoint.setLocation(e.getPoint());
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		activePoint = getPointAt(e.getPoint());
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if ((e.getButton() == MouseEvent.BUTTON1) && (activePoint == null)) {
			activePoint = e.getPoint();
			points.add(activePoint);
			repaint();
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			points.remove(activePoint);
			activePoint = null;
			repaint();
		}
	}

	public static void main(String[] args) {
		new PolygonEditorFrame();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_H) {
			showTriangleEdges = !showTriangleEdges;
			repaint();
		}
		else if (e.getKeyCode() == KeyEvent.VK_R) {
			points.clear();
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
