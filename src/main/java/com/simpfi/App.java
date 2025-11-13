package com.simpfi;

import com.simpfi.ui.Frame;

public class App {
    public static void main(String[] args) {
        Frame myFrame = new Frame();
        while (true) {
        	System.out.println(myFrame.getSize().toString());
        }
    }
}
