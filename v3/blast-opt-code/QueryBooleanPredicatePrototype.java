package ch.usi.inf.sape.blastopt.controller.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractEventI;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;
import ch.usi.inf.sape.tracer.analyzer.locations.MemoryLocation.LocationCategory;

import ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.Data;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.Control;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.DC;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.Thin;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.Full;

public class QueryBooleanPredicatePrototype {
	
	private List<AbstractSlicedEvent> events;
	
	public QueryBooleanPredicatePrototype(List<AbstractSlicedEvent> events) {
		this.events = events;
	}
	
	public List<?> find() {
		try {

			/*...*/ 
		} catch(Throwable t) {
			t.printStackTrace(System.err);
			return new ArrayList();
		}

	}
	
	List<AbstractSlicedEvent> events() {
		List<AbstractSlicedEvent> l = new ArrayList<>();
		l.add(events.get(0));
		return l;
	}

	Value[] rhs() {
//		Binding<?>[] uses = currEvent.getUses();
//		List<Value> values = new ArrayList<>();
//		for (int i = 0; i < uses.length; i++) {
//			if (uses[i].getMemoryLocation().getCategory() == LocationCategory.STACK_SLOT) {
//				continue;
//			}else {
//				values.add(uses[i].getValue());
//			}
//		}
//		return values.toArray(new Value[values.size()]);
		return null;
	}

	List<BytecodeEventI> ops() {
//		return Arrays.asList(currEvent.getBytecodes());
		return null;
	}



	int[] integer(Value[] vals) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < vals.length; i++) {
			Value val = vals[i];
			if (val.getType() == MemoryLocationInfo.Type.INT) {
				res.add(Integer.parseInt(val.getValueString()));
			}
		}

		int[] array = new int[res.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = res.get(i);
		}

		return array;
	}

	int[] gt0(int[] ts) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < ts.length; i++) {
			int t = ts[i];
			if (t > 0) {
				res.add(t);
			}
		}

		int[] array = new int[res.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = res.get(i);
		}

		return array;
	}

	int[] lt0(int[] ts) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < ts.length; i++) {
			int t = ts[i];
			if (t < 0) {
				res.add(t);
			}
		}

		int[] array = new int[res.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = res.get(i);
		}

		return array;
	}

	int size(int[] vals) {
		return vals.length;
	}

	int size(Object[] vals) {
		return vals.length;
	}

	int integer(Value value) {
		//TODO: add logic for java.lang.Integer
		if (value.getType() != MemoryLocationInfo.Type.INT) { 
			throw new IllegalStateException();
		}

		return Integer.parseInt(value.getValueString());
	}

	boolean isInvokingMethod(String methodName) {
//		boolean result = currEvent.getEventClassCode() == EventConstants.INVOKEARGS_EVENT &&
//				((INVOKEARGS_Operation) currEvent.getAbstractionCriterion()).getCalleeFrame().getMethod().getName().equals(methodName);
//		System.out.println("isInvoking " + methodName + ":\t" + result);
//		return result;
		return false;
	}

	boolean isInvocationOn(int id) {
//		boolean result = currEvent.getEventClassCode() == EventConstants.INVOKEARGS_EVENT && 
//				((INVOKEARGS_Operation)currEvent.getAbstractionCriterion()).getReceiver().getId() == id;
//		System.out.println("isInvocation on " + id+ ":\t" + result);
//		return result;
		return false;
	}

}


