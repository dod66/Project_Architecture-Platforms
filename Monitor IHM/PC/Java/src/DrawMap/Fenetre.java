package DrawMap;

import java.awt.Color;

import javax.swing.*;
import frameTreatment.Area;

//classe representant la fenetre a afficher qui herite d un JFrame
public class Fenetre extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Paint pan;

	public Fenetre(Area area){
		//creation d un objet JFrame
		JFrame frame = new JFrame();
		//creation d un objet JPanel
		JPanel panel = new JPanel();
		//creation d un objet Paint prenant un area en argument
		pan = new Paint(area);
		//le panel contient un Paint
		panel.add(pan);
		//le Frame contient un Panel
		frame.add(panel);
		//definition du titre de la taille et affichage de la fenetre
		frame.setTitle("Draw");
		frame.setSize(1440,900);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pan.setBackground(Color.WHITE); 
		frame.setContentPane(pan); 
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);

		pan.repaint();

	}
}