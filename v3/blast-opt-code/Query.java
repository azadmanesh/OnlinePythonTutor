import java.util.List;

import ch.usi.inf.sape.tracer.analyzer.*;
import ch.usi.inf.sape.tracer.analyzer.slicing.*;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.*;
import ch.usi.inf.sape.tracer.analyzer.locations.*;
import ch.usi.inf.sape.tracer.analyzer.locations.MemoryLocation.LocationType;

public class Query implements BytecodeAnalyzerBlastOpt {


	@Override
	public BytecodeEventI[] analyze(final Trace trace) {
		
		final EventI criterion = CriterionPrototype.criterion(trace);
		final Predicate slicePredicate = PredicatePrototype.find();
		final Predicate queryPredicate = PredicatePrototype.find();
		
		Slice slice = new Slice(criterion, slicePredicate);
		Navigator dfsNav = new DfsNavigator(slice, Focuser.EARLIEST_FIRST);
		
		Navigator postFilterNav = new PostFilterNavigator(dfsNav, queryPredicate);
		
		SliceStepAction<BytecodeEventI[]> collectAction = new AccumulationSortedAction();
		BytecodeEventI[] events = Traversal.traverse(postFilterNav, collectAction);
		return events;
	}

	
}