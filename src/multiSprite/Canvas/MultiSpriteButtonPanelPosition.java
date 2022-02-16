package multiSprite.Canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import componentInterface.ButtonCommandsInterface;
import componentInterface.MultiSpriteFrameInterface;
import spriteMaker.Frames.Layer.SpriteLayerPanel;

public class MultiSpriteButtonPanelPosition extends JPanel implements ActionListener {
	
	private JButton up;
	private JButton down;
	private JButton left;
	private JButton right;
	private JButton showArrangement;
	private JButton showLayers;
	private ButtonCommandsInterface buttonCommandsCallback;
	
	public static final String CMD_UP = "UP";
	public static final String CMD_DOWN = "DOWN";
	public static final String CMD_LEFT = "LEFT";
	public static final String CMD_RIGHT = "RIGHT";
	
    public MultiSpriteButtonPanelPosition(ButtonCommandsInterface buttoncommands) {
    	super();
       	buttonCommandsCallback = buttoncommands;
    	up = new JButton("+Y");
		up.setActionCommand(CMD_UP);
		up.addActionListener(this);
		down = new JButton("-Y");
		down.setActionCommand(CMD_DOWN);
		down.addActionListener(this);
		left = new JButton("-X");
		left.setActionCommand(CMD_LEFT);
		left.addActionListener(this);
		right = new JButton("+X");
		right.setActionCommand(CMD_RIGHT);
		right.addActionListener(this);
		
		showLayers = new JButton("<<");
		showLayers.setActionCommand(MultiSpriteButtonPanel.CMD_SHOW_LAYERS);
		showLayers.addActionListener(this);
		
		showArrangement = new JButton(">>");
		showArrangement.setActionCommand(MultiSpriteButtonPanel.CMD_SHOW_ARRANGEMENT);
		showArrangement.addActionListener(this);
		
		GroupLayout buttonPanelPositionLayout = new GroupLayout(this);
		this.setLayout(buttonPanelPositionLayout);
		
		buttonPanelPositionLayout.setHorizontalGroup(
				buttonPanelPositionLayout.createSequentialGroup()
				.addComponent(left)
				.addGroup(
				    buttonPanelPositionLayout.createParallelGroup()
				    .addComponent(up)
				    .addComponent(down)
				    .addGroup(buttonPanelPositionLayout.createSequentialGroup()
				    		.addComponent(showLayers)
				            .addComponent(showArrangement)))
				.addComponent(right));
		
		buttonPanelPositionLayout.setVerticalGroup(
				buttonPanelPositionLayout.createSequentialGroup()
				.addComponent(up)
				.addGroup(
				    buttonPanelPositionLayout.createParallelGroup()
				    .addComponent(left)
				    .addComponent(right))
				.addComponent(down)
				.addGroup(buttonPanelPositionLayout.createParallelGroup()
						.addComponent(showLayers)
						.addComponent(showArrangement)));
		this.setVisible(false);

    }

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		buttonCommandsCallback.onButtonPressed(actionEvent.getActionCommand());
	}
}
