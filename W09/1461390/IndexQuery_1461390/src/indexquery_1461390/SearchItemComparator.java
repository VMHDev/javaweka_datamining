/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexquery_1461390;

import java.util.Comparator;

/**
 *
 * @author 1461390
 */
public class SearchItemComparator implements Comparator<SearchItem> {
    @Override
    public int compare(SearchItem lhs, SearchItem rhs) {
        return lhs.count > rhs.count ? -1 : (lhs.count < rhs.count) ? 1 : 0;
    }
}
