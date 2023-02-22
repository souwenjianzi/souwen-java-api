package cn.sowjz.souwen.v1.query.highlight;

import java.util.Comparator;

class SearchKeyComparator implements Comparator<SearchKey>
{

	public int compare(SearchKey key1, SearchKey key2)
	{
		if (key1.getFirstPos() > key2.getFirstPos())
			return 1;
		else if (key1.getFirstPos() < key2.getFirstPos())
			return -1;
		return 0;
	}

}
