import java.util.Comparator;


public class SearchItemComparator implements Comparator<SearchItem>{

    public int compare(SearchItem lhs, SearchItem rhs) {
    	return lhs.count > rhs.count ? -1 : (lhs.count < rhs.count ) ? 1 : 0;
    }
	
}
