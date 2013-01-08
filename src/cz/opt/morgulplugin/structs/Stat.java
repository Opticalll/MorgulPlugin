package cz.opt.morgulplugin.structs;

public class Stat
{
	private int xp;
	private int level;
	private String statName;
	
	public Stat(int defXP, int defLevel, String statName)
	{
		this.xp = defXP;
		this.level = defLevel;
		this.statName = statName;
	}
	
	public String getName()
	{
		return statName;
	}
	
	public int getXP()
	{
		return xp;
	}
	
	public void setXP(int xp)
	{
		this.xp = xp;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
}
