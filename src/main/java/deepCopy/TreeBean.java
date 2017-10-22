package deepCopy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 此处是树的数据结构，采用自包含的方式完成了多层树状结构
 * 
 * text：是树节点的文字描述 url： 是树节点链接，表征该节点的功能（菜单的时候使用）
 * id：唯一表征树的标记，自动根据text与url的连接串的MD5值表达，该id用于页面上定位节点 isexpand： 节点是否展开 children：
 * 子节点，如果是页的话为null level: 该节点所在的层级 type: 节点类别
 * */
public class TreeBean implements Serializable
{
	private static final long serialVersionUID = 4216184548474878409L;
	private String text = "";
	private String url = "";
	private String id = "";
	private boolean isexpand = false;
	private List<TreeBean> children;

	private int level = 0;
	private int type;

	public TreeBean()
	{
	}

	public TreeBean(String text, String url)
	{
		this.text = text;
		this.url = url;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public boolean isIsexpand()
	{
		return isexpand;
	}

	public TreeBean setIsexpand(boolean isexpand)
	{
		this.isexpand = isexpand;
		return this;
	}

	public List<TreeBean> getChildren()
	{
		return children;
	}

	public void setChildren(List<TreeBean> children)
	{
		this.children = children;
	}

	public TreeBean addChild(TreeBean child)
	{
		if (children == null)
			children = new ArrayList<TreeBean>();
		children.add(child);

		return this;
	}
}
