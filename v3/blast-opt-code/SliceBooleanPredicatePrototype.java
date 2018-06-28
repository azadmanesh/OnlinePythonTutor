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

public abstract class SliceBooleanPredicatePrototype{

	public static Predicate find() {
		return new Predicate() {

			@Override
			public boolean test(Dependency dependency) {
				Binding<?> binding = dependency.depends();
				if (binding == null) {		//criterion dependency; ignore
					return false;
				}
				
				MemoryLocation crtValLoc = binding.getMemoryLocation();
				Value crtVal = binding.getValue();
				EventI crtEvent = dependency.dependent();
				EventI orgEvent = dependency.dependee();
				
				return /*...*/;
			}
		};
	}
}
