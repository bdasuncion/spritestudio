package spriteMaker.Animation;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;

import componentInterface.ViewTypeInterface;
import componentInterface.SpriteLayerPanelInterface;
import infoObjects.SpriteAnimationSet;
import infoObjects.SpriteFrame;
//import infoObjects.SpriteFrameInformation;

public class SpriteAnimationPanel extends JPanel implements ViewTypeInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6889339906110001191L;
	private SpriteAnimationSet animation;
	private Timer delay;
	private boolean tileView = false;
	private int viewType = NORMAL;
	private int currentAnimationFrame;
	private int scale = 1;
	int tileWidth = 3, tileHeight = 3;
	
	final static int NORMAL = 0;
	final static int TILE = 1;
	final static int AUTOTILE = 2;

	public SpriteAnimationPanel(SpriteAnimationSet sas) {
		super();
		this.enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK|AWTEvent.KEY_EVENT_MASK);
		delay = new Timer(100, new updateAnimation());
		this.animation = sas;

		currentAnimationFrame = 0;
		delay.start();
		
		int width = animation.get(0).getImage().getWidth(), height = animation.get(0).getImage().getHeight();
		setPreferredSize(new Dimension(width*scale, height*scale));
	}
	
	private class updateAnimation implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateFrame();
		}
	}
	
	private void updateFrame() {		
		if (animation.getSize() < 1) {
			return;
		}
		
		++currentAnimationFrame;
		
		if (currentAnimationFrame >= animation.getSize()) {
			currentAnimationFrame = 0;
		}
		
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
		
		if (animation.getSize() < 1) {
			return;
		}
		
		SpriteFrame currentFrame = animation.get(currentAnimationFrame);
		if (currentFrame == null) {
			return;
		}
		BufferedImage display = currentFrame.getImage();
		if(viewType  == NORMAL) {
			drawNormal(g2D, display);
		} else if (viewType == TILE){
			drawTiles(g2D, display);
		} else if (viewType == AUTOTILE) {
			drawAutotile(g2D, display);
		}
		
		g2D.dispose();
	}
	
	private void drawAutotile(Graphics2D g2D, BufferedImage displayableImage) {
		BufferedImage disp;
		
		disp = g2D.getDeviceConfiguration().
				createCompatibleImage(displayableImage.getHeight(), displayableImage.getWidth());
		disp = displayableImage;
				
		g2D.drawImage(disp, 0, 0, 
				disp.getWidth()*scale,disp.getHeight()*scale, this);
	}
	
	private void drawNormal(Graphics2D g2D, BufferedImage displayableImage) {
		BufferedImage disp;
		
		disp = g2D.getDeviceConfiguration().
				createCompatibleImage(displayableImage.getHeight(), displayableImage.getWidth());
		disp = displayableImage;
				
		g2D.drawImage(disp, 0, 0, 
				disp.getWidth()*scale,disp.getHeight()*scale, this);
	}
	
	private void drawTiles(Graphics2D g2D, BufferedImage displayableImage) {
		int i, x = 0, y = 0;
		BufferedImage disp;
		disp = g2D.getDeviceConfiguration().
			createCompatibleImage(displayableImage.getHeight(),
			displayableImage.getWidth());
		disp = displayableImage;
					
		for(i = 0; i<9; i++) {
			g2D.drawImage(disp, 
					disp.getWidth()*x*scale, 
					disp.getHeight()*y*scale, 
					disp.getWidth()*scale,disp.getHeight()*scale, this);
		
			x++;
			if(x >= tileWidth) {
				x = 0;
				y++;
			}
			if(y>=tileHeight) {
				y = 0;
			}
		}
	}

	public void setTileView(boolean a) {
		tileView = a;
		
		this.revalidate();
		this.repaint();		
	}
	
	public void processMouseWheelEvent(MouseWheelEvent e) {
		if(e.getWheelRotation()>0 && scale > 1) {
			scale--;
		} else if((e.getWheelRotation() < 0)) {
			scale++;
		}
		
		int width = animation.get(0).getImage().getWidth(), height = animation.get(0).getImage().getHeight();
		setPreferredSize(new Dimension(width*scale, height*scale));
		
		this.revalidate();
		this.repaint();
	}
	
	public void processKeyEvent(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			scale++;
		} if(e.getKeyCode() == KeyEvent.VK_DOWN && scale > 1) {
			scale--;
		}
		
		int width = animation.get(0).getImage().getWidth(), height = animation.get(0).getImage().getHeight();
		setPreferredSize(new Dimension(width*scale, height*scale));
		
		this.revalidate();		
		this.repaint();
		
	}

	@Override
	public void setNormalView() {
		viewType = NORMAL;
		
	}

	@Override
	public void setTileView() {
		viewType = TILE;
	}

	@Override
	public void setAutoTileView() {
		viewType = AUTOTILE;
	}

	/*@Override
	public void setAnimation(Vector<SpriteFrame> framesAnimation) {
		currentAnimationFrame = 0;
		animation.clear();
		for(int i = 0; i<framesAnimation.size(); i++) {
			animation.addAll(framesAnimation);
		}
	}*/
}