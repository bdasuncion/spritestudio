package componentInterface;

import java.awt.image.IndexColorModel;

public interface SpritePaletteInterface {
	public void updatePalette(int index, int r, int g, int b);
	public void updatePalette(IndexColorModel icm);
	public IndexColorModel getPallete();
}
