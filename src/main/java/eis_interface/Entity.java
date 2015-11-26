package eis_interface;

import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JTextArea;

import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import pal.TECS.PALClient;
import pal.TECS.PALConstants;
import pal.TECS.PALMessageEvent;
import pal.TECS.PALMessageListener;

public class Entity implements PALMessageListener{
	
	private PALClient thePalClient;
	private HashMap<String,LinkedList<String>> history = new HashMap<String,LinkedList<String>>();
	private JTextArea textWindow;
	boolean receivePercept = false;
	private String message = "";
	public Entity(JTextArea area) {
		textWindow = area;
		thePalClient = new PALClient(PALConstants.PALTECSServer, "childsimulator", PALConstants.PALMessage, this, true);
		System.out.println("Entity created");
	}

	
	private boolean destinationExists(String dest){
		return history.containsKey(dest.toLowerCase());
	}
	
	@AsPercept(name = "receive")
	public String receivePercept() {
		System.out.println("there we go!");
		if(!message.equals("")){
			receivePercept = false;
			System.out.println("done");
			return message;
		}
		return null;
	}
	@Override
	public void receive(PALMessageEvent event) {
		message = event.getText();
		textWindow.append("Message received: " + event.getText() + System.getProperty("line.separator"));
		receivePercept = true;
	}
	@AsAction(name = "sendNexus")
	public void sendNexus(String destination, String message) {
		if(destinationExists(destination)){
			history.get(destination).add(message);
		}else{
			LinkedList<String> messagesForNewDestination = new LinkedList<String>();
			messagesForNewDestination.add(message);
			history.put(destination, messagesForNewDestination);
		}
		thePalClient.sendMessage(destination, message);
		textWindow.append("Sending message to: " +destination + System.getProperty("line.separator") + "Message: " + message + System.getProperty("line.separator"));
	}
}
