package org.codenobi.weave.Networking;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class MagicNumber {
    char[] magicNumbs = new char[4];

    // Set the Magic Numbers for Your Program
    public MagicNumber(char a, char b, char c, char e) {
        magicNumbs[0] = a;
        magicNumbs[1] = b;
        magicNumbs[2] = c;
        magicNumbs[3] = e;
    }

    public MagicNumber(ByteBuffer buffer){
        magicNumbs[0] = buffer.getChar();
        magicNumbs[1] = buffer.getChar();
        magicNumbs[2] = buffer.getChar();
        magicNumbs[3] = buffer.getChar();
    }

    public char[] GetMagicNumber(){
        return magicNumbs;
    }

    public String GetMagicNumberString(){
        return Arrays.toString(magicNumbs);
    }

}
