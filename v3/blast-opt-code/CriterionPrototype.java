package ch.usi.inf.sape.blastopt.controller.analyzer;

import java.util.List;

import ch.usi.inf.sape.blastopt.controller.json.BytecodeValueVisitor;
import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractEventI;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractPureEvent;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractionUtils.AbstractHistory;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;

public abstract class CriterionPrototype {
	
	public static AbstractEventI find(final Trace trace, AbstractHistory history) {
		BytecodePureEvent[] bcEvents = trace.getAllBytecodeEvents();
		List<AbstractPureEvent> absEvents = history.getAbstractEvents(); 
		
		int absEventIndex = /*aeidx...*/;
		int bcIndex = /*bcidx...*/;
		
		if (absEventIndex == -1) {
			absEventIndex = absEvents.size() - 1; 
		}
		
		BytecodeEventI bc = bcEvents[bcIndex];
		BytecodeValueVisitor bcValueVistor = new BytecodeValueVisitor();
		int defIndex = -1;
		try {
			bc.accept(bcValueVistor);
		} catch (VisitorException e) {
			e.printStackTrace();
		}
		
		defIndex = bcValueVistor.getDefIdx();
		
		AbstractPureEvent pureCriterion = absEvents.get(absEventIndex);
		System.out.println("***********" + pureCriterion);
		AbstractEventI criterion = pureCriterion;
		if (defIndex != -1) {
			criterion = pureCriterion.asSliceViewForDefs(bc.getDefs()[defIndex]);
		}
		
		return criterion; 
	}
}
