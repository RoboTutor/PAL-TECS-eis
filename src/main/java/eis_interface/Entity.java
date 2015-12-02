package eis_interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JTextArea;

import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import pal.TECS.PALClient;
import pal.TECS.PALConstants;
import pal.TECS.PALMessageEvent;
import pal.TECS.PALMessageListener;

public class Entity implements PALMessageListener {

	private PALClient thePalClient;
	private HashMap<String, LinkedList<String>> history = new HashMap<String, LinkedList<String>>();
	private JTextArea textWindow;
	private LinkedList<String> subscriptions;
	boolean receivePercept = false;
	private String message = "";
	private String type = "";

	public Entity(JTextArea area, LinkedList<String> subscriptions) {
		textWindow = area;
		this.subscriptions = subscriptions;
		thePalClient = new PALClient(PALConstants.PALTECSServer, "childsimulator", PALConstants.PALMessage, this, true);
		System.out.println("Entity created");
	}

	private boolean destinationExists(String dest) {
		return history.containsKey(dest.toLowerCase());
	}

	@AsPercept(name = "receive", multipleArguments = true)
	public ArrayList<String> receivePercept() {
		if (receivePercept) {
			receivePercept = false;
			ArrayList<String> ret = new ArrayList<String>();
			ret.add(type);
			ret.add(message);
			type = "";
			message = "";
			return ret;
		}
		return null;
	}

	@Override
	public void receive(PALMessageEvent event) {
		message = event.getText();
		type = "TEST";
		if(subscriptions.contains(type)){
			textWindow.append("Message received: " + event.getText() + System.getProperty("line.separator"));
			receivePercept = true;
		}

	}

	@AsAction(name = "sendNexus")
	public void sendNexus(String type, String message) {
		if (destinationExists(type)) {
			history.get(type).add(message);
		} else {
			LinkedList<String> messagesForNewDestination = new LinkedList<String>();
			messagesForNewDestination.add(message);
			history.put(type, messagesForNewDestination);
		}
		thePalClient.sendMessage(type, message);
		textWindow.append("Sending message of the type: " + type + System.getProperty("line.separator") + "Message: "
				+ message + System.getProperty("line.separator"));
	}
}
