package multiSprite.AnimationFrameSelection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import componentInterface.MultiSpriteFrameInterface;
import componentInterface.MultispriteFrameSetInterface;

public class MultiSpriteFrameSelectionControl extends JPanel implements ActionListener {
	
	MultispriteFrameSetInterface frameSetInterface;
	JButton addButton;
	JButton deleteButton;
    public MultiSpriteFrameSelectionControl(MultispriteFrameSetInterface msfi) {
    	super();
    	frameSetInterface = msfi;
    	
    	addButton = new JButton("Add set");
    	addButton.addActionListener(this);
    	
    	deleteButton = new JButton("Remove Set");
    	deleteButton.addActionListener(this);
    	this.add(addButton);
    	this.add(deleteButton);
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			frameSetInterface.addFrameSet();	
		} else if (e.getSource() == deleteButton) {
			frameSetInterface.deleteFrameSet();
		}
		
	}
}
