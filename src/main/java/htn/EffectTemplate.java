package htn;

import java.util.Vector;

import org.metacsp.framework.VariablePrototype;

import fluentSolver.FluentNetworkSolver;
import hybridDomainParsing.HybridDomain;

public class EffectTemplate {
	
	private final String key;
	private final String name;
	private final String[] args;
	
	private static final String ACTIVITY_STR = "Activity";
	
	private final VariablePrototype prototype;
	private final Vector<AdditionalConstraintTemplate> additionalConstraints = 
			new Vector<AdditionalConstraintTemplate>();
	
	public EffectTemplate(String key, String name, String[] args, int maxArgs, FluentNetworkSolver groundSolver, String component) {
		this.key = key;
		this.name = name;
		this.args = args;
		String comp;
		if (component.equals("Task") && name.startsWith("!")) {
			comp = ACTIVITY_STR;
		} else {
			comp = component;
		}
		this.prototype = createPrototype(comp, maxArgs, groundSolver);
	}
	
	private VariablePrototype createPrototype(String component, int maxArgs, FluentNetworkSolver groundSolver) {
		// fill arguments array up to maxargs
		String[] filledArgs = new String[maxArgs];
		for (int i = 0; i < args.length; i++) {
			filledArgs[i] = args[i];
		}
		for (int i = args.length; i < maxArgs; i++) {
			filledArgs[i] = HybridDomain.EMPTYSTRING;
		}
		return new VariablePrototype(groundSolver, component, name, filledArgs);
	}

	public VariablePrototype getPrototype() {
		return prototype;
	}

	public void addAdditionalConstraint(AdditionalConstraintTemplate con) {
		this.additionalConstraints.add(con);
	}

	public Vector<AdditionalConstraintTemplate> getAdditionalConstraints() {
		return additionalConstraints;
	}
	
	public boolean hasAdditionalConstraints() {
		return additionalConstraints.size() > 0;
	}
	
	public String getKey() {
		return key;
	}

}
