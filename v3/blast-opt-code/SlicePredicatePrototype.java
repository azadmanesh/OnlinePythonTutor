package ch.usi.inf.sape.blastopt.controller.analyzer; 

import java.util.List;

import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;
import ch.usi.inf.sape.tracer.analyzer.locations.MemoryLocation.LocationType;

import ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.Data;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.Control;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.DC;
import static ch.usi.inf.sape.blastopt.controller.analyzer.Q1.PredicateShortName.Thin;

public abstract class SlicePredicatePrototype{

	public static Predicate find() {
		return predicate(/*...*/);
	}
	
	private static Predicate predicate(PredicateShortName p) {
		if (p == null) {
			return defaultPredicate();
		}
		
		return p.getPredicate();
	}

	private static Predicate predicate(final boolean cond) {
		return new Predicate() {
			
			@Override
			public boolean test(Dependency dependency) {
				return cond;
			}
		};
	}
	
	private static Predicate defaultPredicate() {
		return new Predicate() {
			
			@Override
			public boolean test(Dependency dependency) {
				return true;
			}
		};
	}

}
