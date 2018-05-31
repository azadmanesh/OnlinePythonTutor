import java.util.List;

import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;
import ch.usi.inf.sape.tracer.analyzer.locations.MemoryLocation.LocationType;

public abstract class CriterionPrototype {
	
	public static EventI find(final Trace trace) {
		EventI[] events = trace.getAllBytecodeEvents();
		
		int index = /*...*/;
		if (index == -1) {
			index = events.length - 1; 
		}
		
		return events[index]; 
	}
}
