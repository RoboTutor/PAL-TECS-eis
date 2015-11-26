package eis_interface;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import eis.eis2java.annotation.AsPercept;
import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;

@SuppressWarnings("serial")
public class EnvironmentInterface extends AbstractEnvironment {

	private JFrame outputWindow;
	private JTextArea area;
	private Entity entity;
	String message = "";
	boolean receivePercept = false;

	@AsPercept(name = "receive")
	public String receivePercert() {
		System.out.println("yuhjuu");
		if (receivePercept) {
			System.out.println("I am about the send the percept!");
			receivePercept = false;
			return message;
		}
		return null;
	}

	@Override
	public void init(Map<String, Parameter> parameters) throws ManagementException {
		super.init(parameters);
		area = initWindow();
		System.out.println("I am here!");

		setState(EnvironmentState.PAUSED);
		// Try creating and registering an entity.
		try {
			entity = new Entity(area);
			registerEntity("entity1", entity);
			System.out.println("Registered");
		} catch (EntityException e) {
			throw new ManagementException("Could not create an entity", e);
		}
	}

	@Override
	public void kill() throws ManagementException {

		outputWindow.setVisible(false);
		outputWindow = null;
		setState(EnvironmentState.KILLED);
	}

	@Override
	public boolean isStateTransitionValid(EnvironmentState oldState, EnvironmentState newState) {
		return true;
	}

	@Override
	protected boolean isSupportedByEnvironment(Action action) {
		return getState() == EnvironmentState.RUNNING;
	}

	@Override
	protected boolean isSupportedByType(Action action, String type) {
		return true;
	}

	private JTextArea initWindow() {
		JTextArea textArea;

		outputWindow = new JFrame();
		outputWindow.setLayout(new BorderLayout());
		textArea = new JTextArea();
		outputWindow.add(new JScrollPane(textArea));
		outputWindow.setSize(600, 400);
		outputWindow.setLocation(0, 0);
		outputWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		outputWindow.setVisible(true);
		return textArea;
	}
}
