import java.util.List;

import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;
import ch.usi.inf.sape.tracer.analyzer.locations.MemoryLocation.LocationType;

public abstract class PredicatePrototype {

	public static Predicate find() {
		return predicate(/*...*/);
	}
	
	private static Predicate predicate(Predicate p) {
		if (p == null) {
			return defaultPredicate();
		}
		
		return p;
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
