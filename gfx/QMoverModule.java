package ryan.tom.inq.gfx;

public interface QMoverModule {
	public boolean move(QDirection direction);
	public QDirection getDirection();
	public void setDirection(QDirection direction);
	public void attachMoverSystem(QMoverSystem moverSys);
	public int getTiming();
	public void setTiming(int timing);
	public boolean isTimed();
}
