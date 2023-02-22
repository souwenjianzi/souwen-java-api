package cn.sowjz.souwen.v1.query.highlight;

class SearchKey
{
	private String key;

	private int length;

	private int firstPos;

	public int getFirstPos()
	{
		return firstPos;
	}

	public void setFirstPos(int firstPos)
	{
		this.firstPos = firstPos;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
		this.length = key.length();
	}

	public int length()
	{
		return length;
	}
}
