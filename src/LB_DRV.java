import java.util.ArrayList;
import java.util.HashSet;

public class LB_DRV{
	private static final short W = 5;
	private static final short H = 5;
	private static Cell[][] grid;
	private static ArrayList<Coordinate> lazorDeployers;
	private static ArrayList<DoubleCoordinate> holes;
	private static ArrayList<Coordinate> placeable;
	private static byte blocks;

	public static void main(String[] args){
		init();
		loadElements();
		loadPlaceable();
		int len = placeable.size();
		
		HashSet<boolean[]> combs = CombGen.generate(len, blocks);
		System.out.println(combs.size());
		
		runCombinations(combs, len, false);
		
		System.out.println("END");
	}
	
	private static void init(){
		grid = new Cell[W][H];
		lazorDeployers = new ArrayList<Coordinate>();
		holes = new ArrayList<DoubleCoordinate>();
		placeable = new ArrayList<Coordinate>();
		
		//Populate grid w/ default cells
		for(int i = 0; i < W; i++)
			for(int j = 0; j < H; j++)
				grid[i][j] = new Cell();
		
		Drawer.init(W, H);
	}
	
	private static void loadElements2(){
		grid[0][1].makeUnplaceable();
		grid[1][2].makeUnplaceable();
		grid[2][2].makeUnplaceable();
		grid[0][3].makeUnplaceable();
		grid[2][4].makeUnplaceable();
		grid[4][5].makeUnplaceable();

		addLazorDeployer(1, 1, 'l', false);
		
		addHole(4, 0, 'b');
		addHole(1, 1, 'b');
		addHole(3, 2, 'r');
		
		grid[4][2].addBlock(true, false);
		
		blocks = 6;
	}
	
	private static void loadElements(){
		grid[4][2].makeUnplaceable();
		
		addLazorDeployer(1, 0, 'l', false);
		addLazorDeployer(4, 2, 't', false);
		
		addHole(2, 1, 'r');
		addHole(2, 2, 'r');
		addHole(2 ,3, 'r');
		addHole(4, 3, 't');
		addHole(0, 4, 'r');
		
		blocks = 6;
	}
	
	private static void loadPlaceable(){
		for(int i = 0; i < W; i++){
			for(int j = 0; j < H; j++){
				if(grid[i][j].plbl){
					placeable.add(new Coordinate(i, j, (byte) 0));
				}
			}
		}
	}
	
	@SuppressWarnings("SameParameterValue")
	private static void addLazorDeployer(int x, int y, char orientation, boolean cwis){
		byte pos = 0;				//assume top
		switch(orientation){
			case 'l': 	pos = 1;	//left
						break;
			case 'r': 	pos = 2;	//right
						break;
			case 'b': 	pos = 3;	//bottom
		}
		grid[x][y].addLazorDeployer(pos, cwis);
		lazorDeployers.add(new Coordinate((byte)x, (byte)y, pos));
	}
	
	private static void addHole(int x, int y, char orientation){
		int pos = 0;		//assume top orientation
		int ox = x;
		int oy = y;
		switch(orientation){
			case 'l': 	pos = 1;		//orientation left
						ox--;
						break;
			case 'r': 	pos = 2;		//orientation right
						ox++;
						break;
			case 'b': 	pos = 3;		//orientation bottom
						oy++;
						break;
			default:	oy--;
		}
		grid[x][y].addHalfHole(pos);
		if(!checkOffGrid(ox, oy))
			grid[ox][oy].addHalfHole((byte)(3 - pos));		//add second half if it's within the grid
		holes.add(new DoubleCoordinate(x, y, ox, oy, pos));
	}
	
	@SuppressWarnings("SameParameterValue")
	private static void runCombinations(HashSet<boolean[]> combs, int len, boolean printAndDrawAllConfigs){
		int configuration = 1;
		for(boolean[] p : combs){
			int placed = 0;
			for(int i = 0; i < len; i++){
				Coordinate c = placeable.get(i);
				if(placed == blocks)
					break;
				if(p[i]){
					placed++;
					grid[c.x][c.y].addBlock(true, true);
				}
			}
			deployLazors();
			if(checkHoles()){
				System.out.println("Configuration " + configuration + " yields valid solution");
				Drawer.draw(grid);
				break;
			}else if(printAndDrawAllConfigs){
				System.out.println("Tried Configuration " + configuration);
				Drawer.draw(grid);
			}
			configuration++;
			
			clearBlocks();
			clearLazors();
		}
	}

	private static void deployLazors(){
		for(Coordinate ld : lazorDeployers){
			int cx = ld.x;							//Current X
			int cy = ld.y;							//Current Y
			int nx = cx;							//Next X
			int ny = cy;							//Next Y
			int pos = ld.pos;						//Position of lazor Deployer
			boolean cwis = grid[cx][cy].cwis[pos];	//Left at Top
			int clzr = -1; 							//Current lazor orientation
			int nlzr = -1; 							//Next lazor
			boolean fwd = false;					//Forward
			
			int index = pos | (cwis ? 1 : 0) << 2;
			switch(index){
				case 0:
					clzr = 0;
					fwd = false;
					break;
				case 1:
					clzr = 3;
					fwd = true;
					break;
				case 2:
					clzr = 1;
					fwd = false;
					break;
				case 3:
					clzr = 2;
					fwd = true;
					break;
				case 4:
					clzr = 1;
					fwd = true;
					break;
				case 5:
					clzr = 0;
					fwd = true;
					break;
				case 6:
					clzr = 2;
					fwd = false;
					break;
				case 7:
					clzr = 3;
					fwd = false;
					break;
			}
			
			//Check if deployer blocked
			boolean isBlocked = false;
			if(grid[cx][cy].occu){
				if(grid[cx][cy].rflc){
					isBlocked = true;
					if(pos % 3 == 0){ 
						clzr = 3 - clzr;
						if(pos == 0)
							cy--;
						else
							cy++;
					}else{
						fwd = !fwd;
						clzr = (5 - clzr) % 4;
						if(pos == 1)
							cx--;
						else
							cx++;
					}
					nx = cx; ny = cy;
				}
				else
					continue;
			}
			if(isBlocked){
				if(checkOffGrid(cx, cy))
					continue;
				else
					if(grid[cx][cy].occu)		//Lazor is trapped, sandwiched between two reflective blocks.
						continue;
			}
			
			grid[cx][cy].addLazor(clzr);
			
			//Where the Magic happens (Calculate lazor propagation)
			while(true){
				//Update position and lazor orientation
				if(clzr % 2 == 0){
					if(clzr == 0){
						if(fwd)
							ny--;
						else
							nx--;
					}else{
						if(fwd)
							nx++;
						else
							ny++;
					}
					nlzr = (byte)(2 - clzr);
				}else{
					if(clzr == 1){
						if(fwd)
							nx++;
						else
							ny--;
					}else{
						if(fwd)
							ny++;
						else
							nx--;
					}
					nlzr = (byte)(4 - clzr);
				}
				
				if(checkOffGrid(nx, ny))
					break;
				
				//Check next cell for reflection or absorption
				if(grid[nx][ny].occu){
					if(grid[nx][ny].rflc){
						if(fwd){
							if(clzr % 3 != 0)
								fwd = false;
							clzr = clzr % 2 + 1;
						}
						else{
							if(clzr % 3 == 0)
								fwd = true;
							clzr = 3 - 3 * (clzr % 2);//find out if using nlzr helps
						}
						nx = cx; ny = cy;
						grid[cx][cy].addLazor(clzr);
					}else
						break;
				}else{
					cx = nx; cy = ny;
					clzr = nlzr;
					grid[cx][cy].addLazor(clzr);
				}
			}
		}
	}
	
	private static boolean checkHoles(){
		boolean allOn = true;
		
		for(DoubleCoordinate co : holes){
			int x = co.x;
			int y = co.y;
			int ox = co.ox;
			int oy = co.oy;
			//byte dx = (byte)(x - ox);
			//byte dy = (byte)(y - oy);
			
			//System.out.println(grid[x][y].hlon[co.pos] + " " + grid[ox][oy].hlon[3 - co.pos]);
			
			if(checkOffGrid(ox, oy)){
				if(!checkHole(x, y, co.pos)){
					allOn = false;
					break;
				}
			}
			else{
				if(!checkHole(x, y, co.pos) && !checkHole(ox, oy, (byte)(3 - co.pos))){
					allOn = false;
					break;
				}
			}
		}
		
		return allOn;
	}
	
	private static boolean checkHole(int x, int y, int pos){
		Cell c = grid[x][y];
		switch(pos){
			case 0: if(c.zero || c.frst)
						return true;
					break;
			case 1: if(c.zero || c.thrd)
						return true;
					break;
			case 2: if(c.frst || c.scnd)
						return true;
					break;
			default:if(c.thrd || c.scnd)
						return true;
		}
		return false;
	}

	private static boolean checkOffGrid(int x, int y){
		return (x < 0 || x >= W || y < 0 || y >= H);
	}
	
	private static void clearBlocks(){
		for(Coordinate c : placeable){
			grid[c.x][c.y].clearBlock();
		}
	}
	
	private static void clearLazors(){
		for(short i = 0; i < W; i++)
			for(short j = 0; j < H; j++)
				grid[i][j].clearLazors();
	}
	
	static class Coordinate{
		public int x;
		public int y;
		int pos;
		
		Coordinate(int ax, int ay, int aPos){
			x = ax;
			y = ay;
			pos = aPos;
		}
	}
	
	static class DoubleCoordinate{
		int x;
		int y;
		int ox;
		int oy;
		int pos;
		
		DoubleCoordinate(int ax, int ay, int otherX, int otherY, int posOfaXaY){
			x = ax;
			y = ay;
			ox = otherX;
			oy = otherY;
			pos = posOfaXaY;
		}
	}
}