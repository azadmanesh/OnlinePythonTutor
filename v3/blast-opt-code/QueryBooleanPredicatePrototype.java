package ch.usi.inf.sape.blastopt.controller.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractEventI;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;
import ch.usi.inf.sape.tracer.analyzer.locations.MemoryLocation.LocationCategory;
import java.util.stream.*;

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
		return events;
	}
	
	Set<Binding<?>> rhs() {
		Set<Binding<?>> values = new HashSet<>();
		for (AbstractSlicedEvent e : events) {
			Binding<?>[] uses = e.getUses();
			for (int i = 0; i < uses.length; i++) {
				values.add(uses[i]);
			}
		}
		return values;
	}
	
	Set<Binding<?>> lhs() {
		Set<Binding<?>> values = new HashSet<>();
		for (AbstractSlicedEvent e : events) {
			Binding<?>[] defs = e.getDefs();
			for (int i = 0; i < defs.length; i++) {
				values.add(defs[i]);
			}
		}
		return values;
	}
	
	Binding<?> def(AbstractSlicedEvent e) {
		return e.getDefs()[0];
	}

	List<BytecodeEventI> ops(AbstractSlicedEvent e) {
		return Arrays.asList(e.getBytecodes());
	}
	
	int intValue(Binding<?> val) {
		return val.getValue().intValue();
	}

	List<Binding<?>> rhs(EventI e) {
		Binding<?>[] uses = e.getUses();
		List<Binding<?>> values = new ArrayList<>();
		for (int i = 0; i < uses.length; i++) {
				values.add(uses[i]);
		}
		return values;
	}

	MemoryLocation memLocMap(Binding<?> b) {
		return b.getMemoryLocation();
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


