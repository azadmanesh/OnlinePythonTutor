package ch.usi.inf.sape.blastopt.controller.analyzer;

import java.util.List;

import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;
import ch.usi.inf.sape.tracer.analyzer.locations.MemoryLocation.LocationType;
import ch.usi.inf.sape.blastopt.controller.analyzer.*;

public class Query implements BytecodeAnalyzerBlastOpt {

	public enum PredicateShortName {Data(SlicePredicate.DATA), Control(SlicePredicate.CONTROL), DC(SlicePredicate.DATAwithCONTROL), Thin(SlicePredicate.THIN);
		Predicate predicate;

		PredicateShortName(Predicate p) {
			this.predicate = p;
		}

		Predicate getPredicate() {
			return this.predicate;
		}
	}

	@Override
	public BytecodeEventI[] analyze(final Trace trace) {
		
		final EventI criterion = CriterionPrototype.find(trace);
		final Predicate slicePredicate = SlicePredicatePrototype.find();
		final Predicate queryPredicate = QueryPredicatePrototype.find();
		
		Slice slice = new Slice(criterion, slicePredicate);
		Navigator dfsNav = new DfsNavigator(slice, Focuser.EARLIEST_FIRST);
		
		Navigator postFilterNav = new PostFilterNavigator(dfsNav, queryPredicate);
		
		SliceStepAction<BytecodeEventI[]> collectAction = new AccumulationSortedAction();
		BytecodeEventI[] events = Traversal.traverse(postFilterNav, collectAction);
		return events;
	}

	
}