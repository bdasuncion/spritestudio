package multiSprite.Frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import componentInterface.CanvasInterface;
import componentInterface.UpdateFramesInterface;
import fileRW.SpriteStudioFileReader;
import spriteMaker.Frames.SpriteFramePanel;
import tools.ImageTools;

public class ImportFrameSelectionFrame extends JInternalFrame implements ActionListener {
	SpriteFramePanel spriteFramePanel;
	UpdateFramesInterface updateFramesInterface;
	JScrollPane frameView;
	public ImportFrameSelectionFrame(SpriteStudioFileReader file, String filePath, String fileName, 
			CanvasInterface ci, UpdateFramesInterface ufi) {
    	super();
    	
    	setTitle(fileName);
    	setClosable(true);
    	updateFramesInterface = ufi;
    	//TODO fix
    	spriteFramePanel = new SpriteFramePanel(file.getNumOfFrames() , file.getWidth(),
    			file.getHeight(), file.getPixelSize(), ci);
    	
    	BufferedImage[] images = ImageTools.createMultiImages(file.getWidth(), file.getHeight(), 
    			file.getNumOfFrames(), file.getPalette(), file.getData());
    	
    	this.setLayout(new FlowLayout());
    	JPanel buttonPanel = new JPanel();
    	JButton reloadButton = new JButton("RELOAD");
    	reloadButton.addActionListener(this);
    	buttonPanel.add(reloadButton);
    	//add(buttonPanel);

    	
    	spriteFramePanel.setFramePanel(images.length, images);
    	spriteFramePanel.setFileName(fileName);
    	spriteFramePanel.setSourceFile(filePath);
    	//add(spriteFramePanel);
    	//add(frameView);
    	//frameView = new JScrollPane();
    	//frameView.setViewportView(spriteFramePanel);
    	//spriteFramePanel.setPreferredSize(new Dimension(100, 100));
    	
    	this.add(buttonPanel, BorderLayout.WEST);
    	this.add(spriteFramePanel, BorderLayout.CENTER);
    	//this.add(frameView, BorderLayout.CENTER);
    	
		setVisible(true);
		setResizable(true);
		pack();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		spriteFramePanel.reload();
		updateFramesInterface.updateFrames(spriteFramePanel.getAllSpriteFrames());
	}
}
