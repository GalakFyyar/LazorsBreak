class Cell{
	boolean plbl;	//placeable
	boolean occu;	//occupied
	boolean rflc;	//reflects
	boolean mvbl;	//movable
	//lazorDeploy
	boolean lazd;
	boolean dpos[] = new boolean[4];
	boolean cwis[] = new boolean[4];	//Lazor is moving clockwise
	//half hole
	boolean hole;
	boolean hpos[] = new boolean[4];	//half hole position
	//lazors
	boolean zero, frst, thrd, scnd;
	
	Cell(){
		plbl = true;
	}
	
	void addLazorDeployer(byte pos, boolean leftAtTop){
		lazd = true;
		dpos[pos] = true;
		cwis[pos] = leftAtTop;
	}
	/**
	 * Adds a block to this cell. The cell's placeability changes to the value of movable.
	 * So adding an unmovable block will make the cell unplaceable.
	 * Behavior is undefined if used on an unplaceable cell.
	 * @param reflect if true, this block will reflect lazors
	 * @param movable if true, this block can be moved
	 */
	void addBlock(boolean reflect, boolean movable){
		occu = true;
		rflc = reflect;
		mvbl = movable;
		plbl = movable;
	}
	
	//Clears this cell, after this call this cell is not occupied by a block, thus rflc and mvbl (reflect and moveable) have no meaning
	void clearBlock(){
		occu = false;
	}
	
	//Adds half a hole
	void addHalfHole(int pos){
		hole = true;
		hpos[pos] = true;
	}
	
	//Adds a lazor at position pos, refer to second chart below
	void addLazor(int pos){
		switch(pos){
			case 0: zero = true;
					break;
			case 1: frst = true;
					break;
			case 2: scnd = true;
					break;
			case 3: thrd = true;
		}
	}

	//Clears lazors
	void clearLazors(){
		zero = frst = scnd = thrd = false;
	}
	
	//If a cell is unplaceable no blocks can be placed there
	void makeUnplaceable(){
		plbl = false;
	}
}
/*
   Lazer Deployer with cwis true
	    \
	 |¯¯0¯¯|
	 |     |
	/|1   2|/
	 |     |
	 |__3__|
	    \

	    \
	 |¯¯0¯¯|
	 |     |
	/|1   2|/
	 |     |
	 |__3__|
	    \
	    
 Lazer positions
╔═══╗
║0 1║
║3 2║
╚═══╝
*/