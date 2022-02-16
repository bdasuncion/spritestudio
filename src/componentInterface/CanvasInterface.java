package componentInterface;

import infoObjects.SpriteFrame;

public interface CanvasInterface {
	public void setSpriteFrame(SpriteFrame sp);
	//public void setRGBIdx(int idx);
	public void setCanvasButtonPanelInterface(CanvasButtonPanelInterface cbpInterface);
	public CanvasButtonPanelInterface getCanvasButtonPanelInterface();
}
