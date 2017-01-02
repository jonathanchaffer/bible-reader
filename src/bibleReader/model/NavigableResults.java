package bibleReader.model;

/**
 * A class that holds results from multiple Bibles and allows getting only some
 * of the results at time. It does so by defining a "window size", which is the
 * number of references that you want to deal with at a time. The methods are
 * not well-documented, but if you spend a little time studying the class, it
 * should be fairly straightforward to see how to use it.
 * 
 * @author Chuck Cusack.
 */
public class NavigableResults {

	private ReferenceList		results;
	private String				queryPhrase;
	private ResultType			type				= ResultType.NONE;

	private static final int	DEFAULT_WINDOW_SIZE	= 20;
	private int					windowSize			= DEFAULT_WINDOW_SIZE;
	private int					windowStartIndex	= 0;
	private int					windowEndIndex		= DEFAULT_WINDOW_SIZE;

	public NavigableResults(ReferenceList references, String queryPhrase, ResultType type) {
		results = new ReferenceList(references);
		this.queryPhrase = queryPhrase;
		this.type = type;
		setWindowStart(0);
	}

	public String getQueryPhrase() {
		return queryPhrase;
	}

	public void setQueryPhrase(String stats) {
		this.queryPhrase = stats;
	}

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public int size() {
		return results.size();
	}

	public int getNumberPages() {
		return (size() + windowSize - 1) / windowSize;
	}

	public int getPageNumber() {
		return 1 + windowStartIndex / windowSize;
	}

	/**
	 * Slide the view window to the beginning.
	 */
	public void toStart() {
		setWindowStart(0);
	}

	/**
	 * Slide the window to the end.
	 */
	public void toEnd() {
		int newIndex = (results.size() / windowSize) * windowSize;
		if (results.size() % windowSize == 0) {
			newIndex--;
		}
		setWindowStart(newIndex);
	}

	/**
	 * @return the number of results that the various methods should return each time they are called.
	 */
	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
		// We call this method so that both windowStart and windowEnd get updated
		setWindowStart(windowStartIndex);
	}

	public boolean hasNextResults() {
		return (windowStartIndex + windowSize < results.size());
	}

	public boolean hasPreviousResults() {
		return windowStartIndex > 0;
	}

	/**
	 * Get the "viewable window" of results.
	 * 
	 * @return a ReferenceList containing the currently "viewable" results.
	 */
	public ReferenceList currentResults() {
		return new ReferenceList(results.subList(windowStartIndex, windowEndIndex));
	}

	/**
	 * Shift the "viewing window" up and return the next set of results.
	 * 
	 * @return a ReferenceList containing the currently "viewable" results, after the "viewing window" has been slid up,
	 *         or null if there are no more results.
	 */
	public ReferenceList nextResults() {
		if (hasNextResults()) {
			slideWindowUp();
			return currentResults();
		} else {
			return null;
		}
	}

	/**
	 * Shift the "viewing window" down and return the previous set of results.
	 * 
	 * @return a ReferenceList containing the currently "viewable" results, after the "viewing window" has been slid
	 *         down, or null if there are no previous results.
	 */
	public ReferenceList previousResults() {
		if (hasPreviousResults()) {
			slideWindowDown();
			return currentResults();
		} else {
			return null;
		}
	}

	// -------------------------------------------------
	// Private helper methods
	private void slideWindowUp() {
		setWindowStart(windowStartIndex + windowSize);
	}

	private void slideWindowDown() {
		setWindowStart(windowStartIndex - windowSize);
	}

	/*
	 * This sets the starting location of the "viewable window", and then
	 * sets the ending location accordingly.  This is a bit tricky because
	 * the final window doesn't always have the full number of results.
	 */
	private void setWindowStart(int start) {
		if (results.size() == 0) {
			windowStartIndex = 0;
			windowEndIndex = 0;
		}
		if (start >= 0 && start < results.size()) {
			windowStartIndex = start;
			windowEndIndex = windowStartIndex + windowSize;
			if (windowEndIndex > results.size()) {
				windowEndIndex = results.size();
			}
		}
	}

}
