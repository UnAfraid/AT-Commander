package com.github.unafraid.atcommander;

import com.github.unafraid.atcommander.applet.ATCommandTester_v27;

import java.applet.Applet;
import java.awt.*;

/**
 * Created by UnAfraid on 7.6.2015 y..
 */
public class Main {

    public static void main(String[] args) {
        Applet myApplet = new ATCommandTester_v27();
        Frame myFrame = new Frame("Applet Holder"); // create frame with title
        // Call applet's init method (since Java App does not
        // call it as a browser automatically does)
        myApplet.init();
        // add applet to the frame
        myFrame.add(myApplet, BorderLayout.CENTER);
        myFrame.setVisible(true); // usual step to make frame visible

    }
}
