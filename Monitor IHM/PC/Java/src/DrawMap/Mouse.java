package DrawMap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.eclipse.jetty.websocket.api.Session;

import ClientWebSocket.ClientSocket;

//classe pour le clique souris
public class Mouse implements MouseListener
{
	public int x, y;
	//creation d un objet ClientSocket pour l envoie de la trame json
	private ClientSocket client = new ClientSocket();	

	//fonction de recuperation des coordonnees du clique droit souris
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		//si on clique droit sur le JFrame
		if (e.getButton() == MouseEvent.BUTTON3) {
			System.out.printf("(x = %d,y = %d)",e.getX(),e.getY());
			//recuperation des coordonnees du clique
			x = e.getX();
			y = e.getY(); 
			//appel de la fonction d envoie du json
			sendJsonVertex();
		} 
	}
	//fonction d envoie d une trame json au serveur pour confirmer une demande de course
	public void sendJsonVertex() {
		String json = "{\"cabRequest\":[{\"area\":\"Quartier-Nord\",\"location\":[{\"area\":\"Quartier-Nord\",\"locationType\":\"vertex\",\"location\":\"m\"}]}]}";
		client.sendMessage(json);
		System.out.println(json);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}


}