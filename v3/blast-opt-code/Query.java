package ch.usi.inf.sape.blastopt.controller.analyzer;

import java.util.*;

import ch.usi.inf.sape.blastopt.controller.json.BytecodeValueVisitor;
import ch.usi.inf.sape.tracer.analyzer.EventI;
import ch.usi.inf.sape.tracer.analyzer.Trace;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractEventI;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractionUtils.AbstractHistory;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.BytecodeEventI;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.BytecodePureEvent;
import ch.usi.inf.sape.tracer.analyzer.slicing.AbstractSliceAction;
import ch.usi.inf.sape.tracer.analyzer.slicing.AbstractSlicedEvent;
import ch.usi.inf.sape.tracer.analyzer.slicing.AccumulationSortedAction;
import ch.usi.inf.sape.tracer.analyzer.slicing.BytecodeSlicedEvent;
import ch.usi.inf.sape.tracer.analyzer.slicing.DfsNavigator;
import ch.usi.inf.sape.tracer.analyzer.slicing.Focuser;
import ch.usi.inf.sape.tracer.analyzer.slicing.Navigator;
import ch.usi.inf.sape.tracer.analyzer.slicing.PostAnalysisFilter;
import ch.usi.inf.sape.tracer.analyzer.slicing.PostFilterNavigator;
import ch.usi.inf.sape.tracer.analyzer.slicing.PostPredicate;
import ch.usi.inf.sape.tracer.analyzer.slicing.Predicate;
import ch.usi.inf.sape.tracer.analyzer.slicing.Slice;
import ch.usi.inf.sape.tracer.analyzer.slicing.SlicePredicate;
import ch.usi.inf.sape.tracer.analyzer.slicing.SliceStepAction;
import ch.usi.inf.sape.tracer.analyzer.slicing.Traversal;


public class Query implements BlastOptQueryAnalyzer {

	public enum PredicateShortName {Data(SlicePredicate.DATA), 
									Control(SlicePredicate.CONTROL), 
									DC(SlicePredicate.DATAwithCONTROL), 
									Thin(SlicePredicate.THIN), 
									Full(SlicePredicate.FullHistory);
		Predicate predicate;

		PredicateShortName(Predicate p) {
			this.predicate = p;
		}

		Predicate getPredicate() {
			return this.predicate;
		}
	}

	@Override
	public AbstractEventI[] analyze(Trace trace, 
								AbstractHistory history,
								BytecodePureEvent[] bcEvents) {
		
		final BytecodeEventI criterion = CriterionPrototype.find(trace, history);
		final Predicate slicePredicate = /*s...*/.find();

		
		System.out.println("Criteiron set to :\t" + criterion);
		Slice slice = new Slice(criterion, history, slicePredicate);
		Navigator dfsNav = new DfsNavigator(slice, Focuser.EARLIEST_FIRST);
		
		
		AbstractSliceAction abstractSlicedCollectAction = new AbstractSliceAction(history);
		AbstractSlicedEvent[] slicedEvents = Traversal.traverse(dfsNav, abstractSlicedCollectAction);
		BytecodeSlicedEvent[] slicedBcEvents = abstractSlicedCollectAction.getSlicedBytecodes();
		
		System.out.println("[1 of 3]\tSliced events:\t");
		for (AbstractSlicedEvent e : slicedEvents) {
			System.out.println(e);
		}
		
		QueryBooleanPredicatePrototype query = new QueryBooleanPredicatePrototype(Arrays.asList(slicedEvents), Arrays.asList(slicedBcEvents), history);
		
		System.out.println("[2 of 3]\tRunning user query...");
		List<?> l = query.find();
		for (Object o : l) {
			System.out.println(o);
		}
		
		System.out.println("[3 of 3]\tAppying final filter w.r.t. user query results...");
		AbstractEventI[] filteredEvents = PostAnalysisFilter.filter(slicedEvents, l);
		
		System.out.println("Filtered events:\t");
		for (AbstractEventI e : filteredEvents) {
			System.out.println(e);
		}
		
		return filteredEvents;
		
	}

}