package ch.usi.inf.sape.blastopt.controller.analyzer;

import ch.usi.inf.sape.blastopt.controller.json.BytecodeValueVisitor;
import ch.usi.inf.sape.tracer.analyzer.EventI;
import ch.usi.inf.sape.tracer.analyzer.Trace;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractEventI;
import ch.usi.inf.sape.tracer.analyzer.abstractops.AbstractionUtils.AbstractHistory;
import ch.usi.inf.sape.tracer.analyzer.bytecodeops.BytecodePureEvent;
import ch.usi.inf.sape.tracer.analyzer.slicing.AbstractSliceAction;
import ch.usi.inf.sape.tracer.analyzer.slicing.AbstractSlicedEvent;
import ch.usi.inf.sape.tracer.analyzer.slicing.AccumulationSortedAction;
import ch.usi.inf.sape.tracer.analyzer.slicing.DfsNavigator;
import ch.usi.inf.sape.tracer.analyzer.slicing.Focuser;
import ch.usi.inf.sape.tracer.analyzer.slicing.Navigator;
import ch.usi.inf.sape.tracer.analyzer.slicing.PostFilterNavigator;
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
		
		final AbstractEventI criterion = CriterionPrototype.find(trace, history);
		final Predicate slicePredicate = /*s...*/.find();
		final Predicate queryPredicate = /*q...*/.find();
		
		System.out.println("Criteiron set to :\t" + criterion);
		Slice slice = new Slice(criterion, history, slicePredicate);
		Navigator dfsNav = new DfsNavigator(slice, Focuser.EARLIEST_FIRST);
		
		Navigator postFilterNav = new PostFilterNavigator(dfsNav, queryPredicate);
		
		SliceStepAction<AbstractEventI[]> abstractSlicedCollectAction = new AccumulationSortedAction();
		return Traversal.traverse(postFilterNav, abstractSlicedCollectAction);
	}

}