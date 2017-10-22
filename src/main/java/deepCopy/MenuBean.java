package deepCopy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 此处是菜单的数据结构： 如果采用纵向菜单，使用内部包含树的形式完成 如果采用横向菜单，采用自包含的方式完成了多层结构
 * 
 * id：唯一表征菜单标记，自动根据text与url的连接串的MD5值表达，该id用于页面上定位节点 text：是树节点的文字描述 url：
 * 是节点链接，表征该节点的功能 line：是否为分割线（横向菜单专用） children： 循环包含子菜单（横向菜单专用）
 * childrenTree：包含的树状菜单项（纵向菜单）
 * */
public class MenuBean implements Serializable
{
	private static final long serialVersionUID = -5366263165963264508L;
	private String id = "";
	private String text = "";
	private String url = "";
	private boolean line = false;
	private List<MenuBean> children;
	private List<TreeBean> childrenTree;

	public MenuBean()
	{
	};

	public MenuBean(String text, String url)
	{
		this.text = text;
		this.url = url;
	};

	public List<TreeBean> getChildrenTree()
	{
		return childrenTree;
	}

	public void setChildrenTree(List<TreeBean> childrenTree)
	{
		this.childrenTree = childrenTree;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public boolean isLine()
	{
		return line;
	}

	public MenuBean setLine(boolean line)
	{
		this.line = line;
		return this;
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

	public List<MenuBean> getChildren()
	{
		return children;
	}

	public void setChildren(List<MenuBean> children)
	{
		this.children = children;
	}

	public MenuBean addChild(MenuBean child)
	{
		if (children == null)
			children = new ArrayList<MenuBean>();
		children.add(child);

		return this;
	}

	public MenuBean addChildTree(TreeBean childTree)
	{
		if (childrenTree == null)
			childrenTree = new ArrayList<TreeBean>();
		childrenTree.add(childTree);

		return this;
	}
}
