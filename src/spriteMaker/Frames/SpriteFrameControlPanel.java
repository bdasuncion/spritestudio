package spriteMaker.Frames;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import fileRW.ExportTiles;
import fileRW.TileFileFilter;

//import spriteMaker.Frames.SpriteFrames.addEmptyLayer;

//import spriteMaker.Frames.SpriteFrames.importLayer;
//import spriteMaker.Frames.SpriteFrames.resize;
import tools.ImageTools;

public class SpriteFrameControlPanel extends JPanel {
	JMenuBar menuBar;
	JMenu mainMenu;
	JMenuItem copyCanvas;
	JMenuItem addCanvas;
	JMenuItem deleteCanvas;
	JMenuItem exportToTiles;
	JMenuItem addNewLayer;
	JMenuItem importLayer;
	JMenuItem resize;
	JPanel panelForMenu;
	private SpriteFrameResizeDialog spriteResizeDialog;
	private SpriteFramePanel spriteFramePanel;
	private JFrame owner;
	
    public SpriteFrameControlPanel(SpriteFramePanel sfp, JFrame owner) {
    	super();
    	menuBar = new JMenuBar();
    	
    	spriteFramePanel = sfp;
    	this.owner = owner;
    	
    	mainMenu = new JMenu("Options");
		addCanvas = new JMenuItem("Add");
		addCanvas.addActionListener(new addEmpty());
		copyCanvas = new JMenuItem("Copy");
		copyCanvas.addActionListener(new addCopyFrame());
		deleteCanvas = new JMenuItem("Delete");
		deleteCanvas.addActionListener(new deleteFrame());
		exportToTiles = new JMenuItem("Export Tiles");
		exportToTiles.addActionListener(new exportImageToTiles());
		
		resize = new JMenuItem("Resize");
		resize.addActionListener(new resize());
		mainMenu.add(addCanvas);
		mainMenu.add(copyCanvas);
		mainMenu.add(deleteCanvas);
		mainMenu.add(exportToTiles);
		mainMenu.add(resize);
		menuBar.add(mainMenu);
		
		JPanel mainPanel = new JPanel();
		GroupLayout mainLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainLayout);
		
		panelForMenu = new JPanel();

		panelForMenu.add(menuBar);

		JPanel panelForButtons = new JPanel();
		panelForButtons.setLayout(new BoxLayout(panelForButtons,BoxLayout.X_AXIS));
		JButton shiftL = new JButton("Shift Left");
		shiftL.setActionCommand("LEFT");
		shiftL.addActionListener(new shiftFrame());
		JButton shiftR = new JButton("Shift Right");
		shiftR.setActionCommand("RIGHT");
		shiftR.addActionListener(new shiftFrame());
		panelForButtons.add(shiftL);
		panelForButtons.add(shiftR);
				
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup()
				.addComponent(panelForMenu)
				.addComponent(panelForButtons));
		mainLayout.setVerticalGroup(mainLayout.createSequentialGroup()
				.addComponent(panelForMenu)
				.addComponent(panelForButtons));
		
		this.add(mainPanel);
    }
    
    private void resize() {
		if (spriteResizeDialog == null) {
			spriteResizeDialog = new SpriteFrameResizeDialog(owner);
		}
		BufferedImage currentImage = spriteFramePanel.getCurrentSpriteFrame().getImage();
		spriteResizeDialog.showDialog(currentImage.getWidth(), currentImage.getHeight());
		if (spriteResizeDialog.getOkStatus() == false) {
			return;
		}

		spriteFramePanel.resizeFrames(spriteResizeDialog.getResizeWidth(),
				spriteResizeDialog.getResizeHeight());
		this.repaint();
	}
    
    private void exportImageAsTiles() {
		JFileChooser exporter = new JFileChooser();
		exporter.setAcceptAllFileFilterUsed(false);
		exporter.setFileFilter(new TileFileFilter());
		exporter.showDialog(this, "Export");
		if(exporter.getSelectedFile() == null)
			return;
		//ExportTiles.exportToTiles(exporter.getSelectedFile(), spriteLayerPanel.getActivePanel().getImageAndOffsetOfFrame(), 
		//		(IndexColorModel)spriteLayerPanel.getActivePanel().getImageAndOffsetOfFrame().getColorModel());
		ExportTiles.exportToTiles(exporter.getSelectedFile(), 
				spriteFramePanel.getImageAndOffsetOfFrame(), 
				(IndexColorModel)spriteFramePanel.getImageAndOffsetOfFrame().getColorModel());
	}
    
    class addEmpty implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//TODO this should internally done by spriteFramePanel
			BufferedImage img = spriteFramePanel.getCurrentSpriteFrame().getImage();
			spriteFramePanel.copyFrame(ImageTools.createEmptyImage(img.getWidth(),
					img.getHeight(),(IndexColorModel) img.getColorModel()));
		}
	}
    
    class addCopyFrame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//TODO this should be done internally by spriteFramePanel
			BufferedImage img = spriteFramePanel.getCurrentSpriteFrame().getImage();
			spriteFramePanel.copyFrame(img);
		}
	}
    
    class deleteFrame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			spriteFramePanel.removeFrame();
		}
	}
    
    class shiftFrame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String direction = e.getActionCommand();
			if(direction == "LEFT") {
				spriteFramePanel.shiftLeft();
			} else {
				spriteFramePanel.shiftRight();
			}
		}
	}
    
    class exportImageToTiles implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			exportImageAsTiles();
		}
	}
    
    //private class addEmptyLayer implements ActionListener {
	//	public void actionPerformed(ActionEvent arg0) {
			/*CreateNewFile createlayer = new CreateNewFile(null);
			if(createlayer.getOKStaus() == true){
				//TODO change where empty image is created?
				//SpriteFramePanel newLayer = new SpriteFramePanel(1, spriteCanvas);
				//spriteLayerPanel.addSpriteFramePanel(newLayer);
			}*/
	//	}	
	//}
	
	/*private class importLayer implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			//importLayer();
		}
	}*/
	
	private class resize implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			resize();
		}
		
	}
}
