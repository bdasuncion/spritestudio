package spriteMaker.Animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import componentInterface.ViewTypeInterface;


public class TileViewOption extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6289141806052924073L;
	private boolean isButtonPressed = false;
	private boolean isTileView = false;
	ViewTypeInterface viewType;
	
	public static final String SPRITE = "sprite";
	public static final String TILE = "tile";
	public static final String AUTOTILE = "autotile";
	
	public TileViewOption(ViewTypeInterface vtype) {
		super();
		
		viewType = vtype;
		
		JRadioButton spriteView = new JRadioButton("Sprite View");
		spriteView.setActionCommand(SPRITE);
		spriteView.setSelected(true);
		JRadioButton tileView = new JRadioButton("Tile View");
		tileView.setActionCommand(TILE);
		JRadioButton autoTileView = new JRadioButton("Autotile View");
		autoTileView.setActionCommand(AUTOTILE);
		
		spriteView.addActionListener(this);
		tileView.addActionListener(this);
		autoTileView.addActionListener(this);
		
		ButtonGroup group = new ButtonGroup();
		group.add(spriteView);		
		group.add(tileView);
		group.add(autoTileView);
		
	/*	this.setLayout(new GridLayout(3, 1));
		
		this.add(new JLabel("Select View"));
		this.add(layer0);
		this.add(layer1);	*/	
		JLabel label = new JLabel("Select View");
		JPanel mainPanel = new JPanel();
		GroupLayout mainLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainLayout);
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup()
			.addComponent(label)
			.addComponent(spriteView)
			.addComponent(tileView)
			.addComponent(autoTileView));
		
		mainLayout.setVerticalGroup(mainLayout.createSequentialGroup()
			.addComponent(label)
			.addComponent(spriteView)
			.addComponent(tileView)
			.addComponent(autoTileView));
		
		this.add(mainPanel);
	}
	
	public boolean hasNewView() {
		if(isButtonPressed == true)
		{
			isButtonPressed = false;
			return true;
		}
		
		return false;
	}
	
	public boolean isTileView() {
		return isTileView;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == SPRITE) {
			viewType.setNormalView();
		}
		else if (e.getActionCommand() == TILE){
			viewType.setTileView();
		} else {
			viewType.setAutoTileView();
		}
		//viewType.setTileView(isTileView());
		isButtonPressed = true;
	}
}