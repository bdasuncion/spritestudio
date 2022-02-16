package fileRW;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class KnightsOfTheRoundFileFilter extends FileFilterBase {
    public KnightsOfTheRoundFileFilter() {
    	super("kotr", "Knights of the round files(*.kotr)");
    }
}
