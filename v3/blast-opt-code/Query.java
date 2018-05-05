public class Query implements BytecodeAnalyzer{
	
	@Override
	public void analyze(final Trace trace) {
		final EventI criterion = slicingCriterion(trace);
	}
	
    /*
     * Criterion query for: first event
	 */
	private EventI slicingCriterion(final Trace trace) {
		return trace.getAllBytecodeEvents()[0]; 
	}
}