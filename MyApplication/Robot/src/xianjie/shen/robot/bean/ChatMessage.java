package xianjie.shen.robot.bean;

import java.util.Date;

public class ChatMessage
{
	private String	name;
	private String	text;
	private Type	type;
	private Date	date;

	public ChatMessage(String text, Type type, Date date)
	{
		super();
		this.text = text;
		this.type = type;
		this.date = date;
	}

	public ChatMessage()
	{
	}

	public enum Type
	{
		SEND, RECEIVE
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

}
