package ch.usi.inf.sape.blastopt.controller.analyzer;

import java.util.List;

import ch.usi.inf.sape.tracer.analyzer.*;
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
			EventI currEvent;
			EventI orgEvent;
			Value currVal;
			MemoryLocation currValLoc; 
			
			@Override
			public boolean test(Dependency dependency) {
				Binding<?> binding = dependency.depends();
				
				currValLoc = binding != null ? binding.getMemoryLocation() : null;
				currVal = binding != null ? binding.getValue() : null;
				currEvent = dependency.dependent();
				orgEvent = dependency.dependee();
				
				return /*...*/;
			}
			
			boolean isInvokingMethod(String methodName) {
				return isInvokingMethod(currEvent, methodName);
			}
			
			boolean isInvocationOn(int id) {
				return isInvocationOn(currEvent, id);
			}
			
			boolean isInvokingMethod(EventI currEvent, String methodName) {
				return currEvent.getEventClassCode() == EventConstants.INVOKEARGS_EVENT &&
						((INVOKEARGS_Operation) currEvent.getPureEvent()).getCalleeFrame().getMethod().getName().equals(methodName);
			}
			
			boolean isInvocationOn(EventI currEvent, int id) {
				return currEvent.getEventClassCode() == EventConstants.INVOKEARGS_EVENT && 
						((INVOKEARGS_Operation)currEvent.getPureEvent()).getReceiver().getId() == id;
			}
		};
	}

}
