package spriteMaker.InternalFrame;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import componentInterface.ResizeInterface;
import spriteMaker.Canvas.CanvasButtonPanel;
import spriteMaker.Canvas.SpriteCanvas;

public class SpriteEditorFrame extends JInternalFrame implements ResizeInterface {
	private SpriteCanvas spritecanvas;
	private JScrollPane canvasScrollView;
	private CanvasButtonPanel buttonPanel;
	
    public SpriteEditorFrame(int width, int height, int pixelSize) {
    	super("", true, true);
    	setTitle("" + width + "x" + height);
    	setLayout(new BorderLayout());
		setVisible(true);
		show();
		
        spritecanvas = new SpriteCanvas(width, height, pixelSize, this);
		
		canvasScrollView = new JScrollPane(spritecanvas);
		canvasScrollView.setWheelScrollingEnabled(false);
		
		add(canvasScrollView, BorderLayout.CENTER);
		
		buttonPanel = new CanvasButtonPanel(spritecanvas);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		setLocation(50, 200);
    }
    
    public SpriteCanvas getSpriteCanvas() {
    	return spritecanvas;
    }

	@Override
	public void onResize(int newWidth, int newHeight) {
		this.setTitle(newWidth + "x" + newHeight);
	}
}
