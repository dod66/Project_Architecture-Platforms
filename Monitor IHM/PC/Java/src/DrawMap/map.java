package DrawMap;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

class map extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public map(){
		JFrame frame = new JFrame();
        JPanel panel=new JPanel();
        getContentPane().add(panel);
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setVisible(true);
        
        setSize(450,450);
        
        JButton button =new JButton("press");
        panel.add(button);
        
        
    }

    public void paint(Graphics g) {
    	//System.out.println(MAXIMIZED_BOTH);
        Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width_frame = size.getWidth();
        double height_frame = size.getHeight();
        int width_frame_int = (int)width_frame;
        int height_frame_int = (int)height_frame;
        System.out.println(width_frame_int+"x"+height_frame_int);
    	
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Line2D lin = new Line2D.Float(100, 100, 250, 260);
        Line2D lin2 = new Line2D.Float(300, 190, 210, 160);

        g2.draw(lin);
        g2.draw(lin2);
        
    }

    public static void main(String []args){
        map s=new map();
        s.setVisible(true);
    }
}