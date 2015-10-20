package test;

import java.awt.MouseInfo;

public class test {
public static void main(String[] args) throws InterruptedException{
    while(true){
    	
    	int pointX = MouseInfo.getPointerInfo().getLocation().x;
    	int pointY = MouseInfo.getPointerInfo().getLocation().y;
        
    	System.out.println(pointX+pointY);
        
    }
}
}