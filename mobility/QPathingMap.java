package tryan.inq.mobility;

import java.awt.Color;
import java.awt.Graphics2D;

import tryan.inq.overhead.QGameConstants;

public class QPathingMap {
	private int gridWidth;
	private int gridHeight;
	private int cellSize;
	private PathingGridCell[][] pathingMap;
	
	public QPathingMap(int width, int height) {
		gridWidth = width;
		gridHeight = height;
		cellSize = QGameConstants.UNIT_DISTANCE;
		pathingMap = new PathingGridCell[gridHeight/cellSize][gridWidth/cellSize];
		
		init();
	}
	
	private void init() {
		// Building pathing cell matrix
		for(int i = 0; i < pathingMap.length; i++) {
			for(int j = 0; j < pathingMap[0].length; j++) {
				pathingMap[i][j] = new PathingGridCell(cellSize, i, j, QPathType.ALL);
			}
		}
	}
	
	private PathingGridCell findCell(int x, int y) {
		int row = 0;
		int col = 0;

		// Note: Later on, change this to check for partial blocks (like .25 or .5 of a cell)
		for(int i = 0; i < pathingMap[0].length; i++) {
			if(pathingMap[0][i].getX() >= x) {
				if(i != 0) {
					col = pathingMap[0][i-1].getCol();
				} else {
					col = pathingMap[0][i].getCol();
				}
				
				break;
			}
		}
		
		for(int j = 0; j < pathingMap.length; j++) {
			if(pathingMap[j][0].getY() >= y) {
				if(j != 0) {
					row = pathingMap[j-1][0].getRow();
				} else {
					row = pathingMap[j][0].getRow();
				}
				break;
			}
		}		
		
		return pathingMap[row][col];
	}
	
	// Note: There may be a bug here when moving in negative x direction
	public boolean isValidMoveX(QBounds bounds, QPathType pathingType, QDirection direction) {
		PathingGridCell cell = null;
		boolean isValid = false;

		// Finding the first cell to check
		if(direction.xDirection() == 1) {
			cell = findCell(bounds.getX() + bounds.getWidth(), bounds.getY());
		} else if(direction.xDirection() == -1) {
			cell = findCell(bounds.getX(), bounds.getY());
		}

		if(cell != null) {
			// Setting isValid so only need to clear if move is invalid
			isValid = true;
			
			for(int i = 0; i < bounds.getUnitHeight(); i++) {
				if(!pathingMap[cell.getRow() + i][cell.getCol()].isPathable(pathingType)) {
					isValid = false;
					break;
				}
			}
		}
		
		return isValid;
	}
	
	// Note: Revisit pathing validation algorithm, sometimes player moves partially through invalid cells
	public boolean isValidMoveY(QBounds bounds, QPathType pathingType, QDirection direction) {
		PathingGridCell cell = null;
		boolean isValid = false;

		// Finding the first cell to check
		if(direction.yDirection() == -1) {
			cell = findCell(bounds.getX(), bounds.getY());
		} else if(direction.yDirection() == 1) {
			cell = findCell(bounds.getX(), bounds.getY() + bounds.getHeight());
		}

		if(cell != null) {
			// Setting isValid so only need to clear if move is invalid
			isValid = true;
			
			for(int i = 0; i < bounds.getUnitWidth(); i++) {
				if(!pathingMap[cell.getRow()][cell.getCol() + i].isPathable(pathingType)) {
					isValid = false;
					break;
				}
			}
		}
		
		return isValid;
	}
	
	public void setPathingCellType(int x, int y, QPathType pathType) {
		pathingMap[x][y].setPathType(pathType);
	}
	
	public int getRowCount() {
		return pathingMap.length;
	}

	public int getColCount() {
		return pathingMap[0].length;
	}
	
	//////////////////////////
	// Pathing element class
	///////////////////////
	
	public static class PathingGridCell {
		private int row;
		private int col;
		private int x;
		private int y;
		private int cellWidth;
		private QPathType pathType;
		
		public PathingGridCell(int width, int row, int col, QPathType pathingType) {
			cellWidth = width;
			x = col * cellWidth;
			y = row * cellWidth;
			this.row = row;
			this.col = col;
			this.pathType = pathingType;
		}
		
		public boolean isPathable(QPathType pathingType) {
			return (pathType.pathTypeId() == pathingType.pathTypeId() || pathType.pathTypeId() == QPathType.ALL.pathTypeId()) ? true : false;
		}
		
		public QPathType getPathType() {
			return pathType;
		}
		
		public void setPathType(QPathType pathingType) {
			pathType = pathingType;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getCol() {
			return col;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getCellWidth() {
			return cellWidth;
		}
	}
		
	//////////////
	// Debugging
	///////////
	/*
	 * Note: Debugging should eventually exist in its own class so its easier to remove
	 * 		 May still need some hooks in individual components, but can document them
	 * 
	 * Note: Taking a huge FPS hit for this overlay, might be alpha related
	 */
	
	public void draw(Graphics2D g2) {
		for(PathingGridCell row[] : pathingMap) {
			for(PathingGridCell cell : row){
				if(cell.getPathType().equals(QPathType.ALL) || cell.getPathType().equals(QPathType.GROUND)) {
					g2.setColor(new Color(43, 255, 98, 40));
					g2.fillRect(cell.getX(), cell.getY(), cell.getCellWidth(), cell.getCellWidth());
					g2.setColor(new Color(43, 255, 98));
					g2.drawRect(cell.getX(), cell.getY(), cell.getCellWidth(), cell.getCellWidth());
				} else {
					g2.setColor(new Color(255, 78, 51, 40));
					g2.fillRect(cell.getX(), cell.getY(), cell.getCellWidth(), cell.getCellWidth());
					g2.setColor(new Color(255, 78, 51));
					g2.drawRect(cell.getX(), cell.getY(), cell.getCellWidth(), cell.getCellWidth());
				}
			}
		}
	}
}
