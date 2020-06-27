package editor;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BufferedFrame extends Frame {

	private static final long serialVersionUID = -762680475060347570L;

	Image offscreen;
	Graphics og;

	public BufferedFrame() {
		setLayout(null);
		setBounds(0, 0, 1920, 1080);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void invalidate() {
		offscreen = null;
		og = null;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void repaint() {
		offscreen = null;
		super.repaint();
	}

	public void paint(Graphics g) {
		if (offscreen == null) {
			offscreen = createImage(getWidth(), getHeight());
			og = offscreen.getGraphics();
			paintGraphics(og);
		}
		g.drawImage(offscreen, 0, 0, null);
	}

	public abstract void paintGraphics(Graphics g);
}
