package spriteMaker.InternalFrame;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import infoObjects.SpriteAnimationSet;
import spriteMaker.Animation.SpriteAnimationPanel;
import spriteMaker.Animation.TileViewOption;
import spriteMaker.Canvas.SpriteCanvas;
import spriteMaker.Frames.SpriteFrameControlPanel;
import spriteMaker.Frames.SpriteFramePanel;

public class SpriteAnimationFrameSelectionFrame extends JInternalFrame {
   //private SpriteFrames spriteframes;
   private SpriteAnimationPanel spriteanimation;
   
	private JScrollPane frameView; 
	private GroupLayout framePanelLayout;
	private SpriteFramePanel spriteFramePanel;
    private SpriteFrameControlPanel spriteFrameControlPanel;

   
   public SpriteAnimationFrameSelectionFrame(int width, int height, int pixelSize, 
		  SpriteAnimationSet spriteAnimationSet, SpriteCanvas spritecanvas, JFrame owner) {
	   
	   super("Frames", true, true);
	   /*spriteframes = new SpriteFrames(1, width, height, pixelSize, 
			   spriteAnimationSet, spritecanvas, owner);
		
		setSize(500, 100 + height);
		add(spriteframes);
		setVisible(true);
		setLocation(300, 50);
		pack();*/
	   
	   //framePanelLayout = new GroupLayout(this);
	   //this.setLayout(framePanelLayout);
		
	   frameView = new JScrollPane();
	   spriteFramePanel = new SpriteFramePanel(1, width, height, pixelSize, 
				spriteAnimationSet, spritecanvas);
	   spriteFramePanel.setCanvas(spritecanvas);
	   frameView.setViewportView(spriteFramePanel);
	   
	   spriteFrameControlPanel = new SpriteFrameControlPanel(spriteFramePanel, owner);
	   
	   JPanel framePanel = new JPanel();
	   framePanel.setLayout(new BorderLayout());
	   framePanel.add(frameView, BorderLayout.CENTER);
	   framePanel.add(spriteFrameControlPanel, BorderLayout.WEST);
	   this.add(framePanel);

	   setVisible(true);
		setLocation(300, 50);
		pack();
   }
   
   //public SpriteFrames getSpriteFrames() {
	//   return spriteframes;
   //}
   
   public SpriteFramePanel getSpriteFramePanel() {
	   return spriteFramePanel;
   }
}
