package model;

import java.io.Serializable;

public class FXImage implements Serializable {
	private static final long serialVersionUID = 12L;
	private byte[] imgBuffer;

    public FXImage(byte[] imgBuffer) {
        this.imgBuffer = imgBuffer;
    }
    
    public byte[] returnBytes() {
        return imgBuffer;
    }

}
