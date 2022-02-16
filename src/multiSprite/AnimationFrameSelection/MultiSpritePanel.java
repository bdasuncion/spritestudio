package multiSprite.AnimationFrameSelection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import componentInterface.ButtonCommandsInterface;
import componentInterface.MultiSpriteFrameInterface;
import componentInterface.MultispriteFrameSetInterface;
import infoObjects.MultiSpriteAnimation;
import infoObjects.SpriteFrameSet;

public class MultiSpritePanel extends JPanel implements MultispriteFrameSetInterface,
    ButtonCommandsInterface {
	MultiSpriteFrameInterface multiSpriteFrameInterface;
	FlowLayout flowLayout;
	int currentSelected;
	MultiSpriteAnimation multiSpriteAnimation;
	
	private static int BUTTON_WIDTH = 200;
	private static int BUTTON_HEIGHT = 200;
	private static int SPACE = 50;
    public MultiSpritePanel(MultiSpriteFrameInterface msi, MultiSpriteAnimation msa) {
    	super();
    	multiSpriteFrameInterface = msi;
    	multiSpriteAnimation = msa;
    	//flowLayout = new FlowLayout();	
    	//this.setLayout(flowLayout);
        addSet();
    }
    
    public void addSet() {
        MultiSpriteButton button = new MultiSpriteButton(multiSpriteFrameInterface, this,
        		multiSpriteAnimation.createSpriteFrameSet());
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        int count = this.getComponentCount();
        button.setActionCommand(Integer.toString(count));
    	this.add(button);
    	button.doClick();
    	
    	//this.setPreferredSize(new Dimension((BUTTON_WIDTH*count + SPACE), BUTTON_HEIGHT + SPACE));
    	revalidate();
    	repaint();
    }

	@Override
	public void addFrameSet() {
		addSet();
	}
	
	public void deleteSet() {
		int count = this.getComponentCount();
		if (count <= 1) {
			return;
		}
		this.remove(currentSelected);
		multiSpriteAnimation.deleteSpriteFrameSet(currentSelected);   
		
		count = this.getComponentCount();
		if (count >= currentSelected) {
			currentSelected = count - 1;
		}
		
		Component[] components = getComponents();
	    for (int i = 0; i < components.length; ++i) {
	    	((JButton)components[i]).setActionCommand(Integer.toString(i));
	    }
	    
	    ((JButton)this.getComponent(currentSelected)).doClick();
	    
	    this.revalidate();
	    this.repaint();
	}

	@Override
	public void onButtonPressed(String command) {
		currentSelected = Integer.parseInt(command);
	    Component[] components = getComponents();
	    for (int i = 0; i < components.length; ++i) {
	    	components[i].setBackground(Color.GRAY);
	    }
	    components[currentSelected].setBackground(Color.BLUE);
	}

	@Override
	public void deleteFrameSet() {
		deleteSet();
	}

	@Override
	public void updateView() {
		this.revalidate();
		this.repaint();
	}

	@Override
	public void setFrameSet() {
		removeAll();
		
		Vector<SpriteFrameSet> spriteFrameSets = multiSpriteAnimation.getAnimation();
		System.out.println("COUNT:" + spriteFrameSets.size());
		for (int i = 0; i < spriteFrameSets.size(); ++i) {
			MultiSpriteButton button = new MultiSpriteButton(multiSpriteFrameInterface, this,
					spriteFrameSets.get(i));
			button.setSize(200, 200);
	        button.setActionCommand(Integer.toString(this.getComponentCount()));
	    	this.add(button);
	    	button.doClick();
		}
	}
}
