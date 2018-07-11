package ch.usi.inf.sape.blastopt.controller.analyzer;

import java.util.ArrayList;
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

public abstract class QueryBooleanPredicatePrototype {
	
	public static Predicate find() {
		return new Predicate() {
			AbstractEventI currEvent;
			AbstractEventI orgEvent;
			Value currVal;
			MemoryLocation currValLoc; 
			Value defVal;
			
			@Override
			public boolean test(Dependency dependency) {
				Binding<?> binding = dependency.depends();
				
				currValLoc = binding != null ? binding.getMemoryLocation() : null;
				currVal = binding != null ? binding.getValue() : null;
				currEvent = dependency.dependent();
				orgEvent = dependency.dependee();
				for (Binding<?> def : currEvent.getDefs()) {
					for (Binding<?> uses4def : currEvent.getUsesForDef(def)) {
						if (uses4def == binding) { 
							defVal = def.getValue();
							break;
						}
					}
				}
				
				try {
					/*...*/
				} catch(Throwable t) {
					t.printStackTrace();
					return false;
				}
				
			}
			
			Value[] rhs() {
				Binding<?>[] uses = currEvent.getUses();
				Value[] values = new Value[uses.length];
				for (int i = 0; i < values.length; i++) {
					Value value = uses[i].getValue();
				}
				return values;
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
				return currEvent.getEventClassCode() == EventConstants.INVOKEARGS_EVENT &&
						((INVOKEARGS_Operation) currEvent.getAbstractionCriterion()).getCalleeFrame().getMethod().getName().equals(methodName);
			}
			
			boolean isInvocationOn(int id) {
				return currEvent.getEventClassCode() == EventConstants.INVOKEARGS_EVENT && 
						((INVOKEARGS_Operation)currEvent.getAbstractionCriterion()).getReceiver().getId() == id;
			}

		};
	}

}
