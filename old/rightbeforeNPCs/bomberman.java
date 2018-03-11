//note: this is the 2D JApplet version of Bomberman.
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.awt.image.BufferedImage;

import java.io.File;

import java.awt.event.*;


public class bomberman extends JApplet implements KeyListener
{
private Graphics2D g, g2;
private BufferedImage bi;
private int tileSize, numRows, numCols;

private Map map;

private Keyboard k;

private Player p;

private ArrayList<Bomb> bombs;
private ArrayList<Fire> fires;

public boolean radiusShow = true;

public ArrayList<Player> players = new ArrayList<Player>();

public bomberman()
{
bombs = new ArrayList<Bomb>();
fires = new ArrayList<Fire>();
//tileSize = 42; numRows = 20; numCols = 20;
p = new Player();
players.add(p);
k = new Keyboard();
map = new Map();
map = map.randomGenerate();
}
public void keyPressed(KeyEvent e)
{
String key = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
k.keyPress(key);
}
public void keyReleased(KeyEvent e)
{
k.keyRelease(KeyEvent.getKeyText(e.getKeyCode()).toLowerCase());
}
public void keyTyped(KeyEvent e)
{
}
public static float toFloat(double d)
{
return (float)d;
}
public static Color RC()
{
return new Color(
	(float)Math.random(),
	(float)Math.random(),
	(float)Math.random());
}
public void handleKeyPresses()
{
double speed = 10;
p.lx = p.x; p.ly = p.y;
if (k.k("w"))
p.y -= speed;
if (k.k("s"))
p.y += speed;
if (k.k("a"))
p.x -= speed;
if (k.k("d"))
p.x += speed;

this.doCollisions();

if (k.k("space") && map.tiles[getRow(p.y)][getCol(p.x)].walkable)
{

//new
bombs.add(new Bomb(getCol(p.x)*map.tileSize + map.tileSize*0.5,getRow(p.y)*map.tileSize + map.tileSize*0.5, p));
map.tiles[getRow(p.y)][getCol(p.x)].walkable = false;
//end new

}


}
public void doCollisions()//gonna add slidy walls later...
{
boolean collide = !this.walkable(getRow(p.y),getCol(p.x));//(map.tiles[getRow(p.y)][getCol(p.x)].walkable == false);
if ((!collide) || (getRow(p.y)==getRow(p.ly)&&getCol(p.x)==getCol(p.lx))) /* takes care of 
			if you are standing on a bomb you just placed, you can freely move in it.*/
return;

p.x = p.lx; p.y = p.ly;

}
private void loadMap()
{

}
private void loadMap(String fileName)
{
try{
this.map = new Map(fileName);
numRows = map.numRows; numCols = map.numCols;
}catch(Exception e){e.printStackTrace();};

}
public void start()
{
//this.loadMap("testNPC.txt");
this.loadMap("level3.txt");
numRows = map.numRows; numCols = map.numCols; tileSize = map.tileSize;

NPC temp = new NPC();
temp.x = numCols*tileSize-tileSize/2; temp.y = numRows*tileSize-tileSize/2;
temp.color = Color.BLACK;
players.add(temp);

bi = new BufferedImage(getSize().width,getSize().height, 5);
g = bi.createGraphics();
g2 = (Graphics2D)(this.getGraphics());

this.addKeyListener(this);
this.setFocusable(true);
this.requestFocusInWindow();
long LT; long CT = System.currentTimeMillis();
while(true)
{
try{Thread.sleep(100);}catch(Exception e){};
LT = CT;
CT = System.currentTimeMillis();

this.handleKeyPresses();//this.doCollisions(); -- called within handleKeyPresses()
this.tic(CT - LT);
while(this.newBombAffected());
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
this.reactToFires();

this.paint();
}


}
private boolean newBombAffected()
{
boolean newBomb = false;
for (int i = 0; i < bombs.size(); i++)
{
Bomb b = bombs.get(i);
if (b.timeLeft < 1)
{
this.createFires(b);
bombs.remove(i); i--;
}

for (Fire f : fires)
{
if (fireContainsABomb(f))
{
newBomb = true;
}
 
}

}

return newBomb;
}
public boolean fireContainsABomb(Fire f)//not only returns intersection, but also triggers bomb.
{
boolean any = false;
for (Bomb b : bombs)
{
if (getRow(f.y) == getRow(b.y) && getCol(f.x) == getCol(b.x))
{
any = true;
b.timeLeft = 0; //will explode next time around while newBombAffected loop.
}
}
return any;
}

public void reactToFires()
{
for (Fire f : fires)
{
int row = getRow(f.y), column = getCol(f.x);
if (row < 0 || column < 0 || row > numRows-1 || column > numCols-1)
continue;

for (int play = 0; play < players.size(); play++)
{
Player player = players.get(play);
if (getRow(player.y) == row && getCol(player.x) == column)
{players.remove(play); play--;}

}
//kill players
/////////to be done
//destroy destroyable walls
if (map.tiles[row][column].name.equals("wall"))
{
//item drop here
map.tiles[row][column] = new Tile(false);
}

//trigger other bombs is handled elsewhere.


}

}

public void createFires(Bomb b)
{
map.tiles[getRow(b.y)][getCol(b.x)].walkable = true;//map.tiles[getRow(bombs.get(i).y)][getCol(bombs.get(i).x)].walkable = true;

//explode
int row = getRow(b.y), col = getCol(b.x);

fires.add(new Fire(col*tileSize + tileSize/2, row*tileSize + tileSize/2, b.owner));

for (int r = row+1; r <= row+p.firePower; r++)
{
fires.add(new Fire(col*tileSize + tileSize/2, r*tileSize + tileSize/2, b.owner));
if (!walkable(r,col))
break;
}
for (int r = row-1; r >= row-p.firePower; r--)
{
fires.add(new Fire(col*tileSize + tileSize/2, r*tileSize + tileSize/2, b.owner));
if (!walkable(r,col))
break;
}
for (int c = col+1; c <= col+p.firePower; c++)
{
fires.add(new Fire(c*tileSize + tileSize/2, row*tileSize + tileSize/2, b.owner));
if (!walkable(row,c))
break;
}
for (int c = col-1; c >= col-p.firePower; c--)
{
fires.add(new Fire(c*tileSize + tileSize/2, row*tileSize + tileSize/2, b.owner));
if (!walkable(row,c))
break;
}

//explode
return;
}
public void tic(double time)
{
for (int i = 0 ; i < bombs.size(); i++)
{
bombs.get(i).tic(time);
if (bombs.get(i).timeLeft <= 0)
{
this.createFires(bombs.get(i));


bombs.remove(i);

i--;
}
}

for (int i = 0; i < fires.size(); i++)
{
fires.get(i).tic(time);
if (fires.get(i).timeLeft < 0)
{
////
fires.remove(i);i--;
////
}

}

}
public boolean walkable(int row, int column)
{
//@ TODO : does not consider if there is a bomb at row, column.
if (row < 0 || column < 0 || row > map.tiles.length-1 || column > map.tiles[0].length-1)
return false;

return map.tiles[row][column].walkable;

}
public void paint()
{
this.drawGrid();
map.draw(g);

for (Player p : players) p.draw(g);

for (Bomb b : bombs) 
{
b.draw(g);
if (radiusShow)
this.drawBlastRadii(b);
}
for (Fire f : fires) f.draw(g);

g2.drawImage(bi, null, 0, 0);
}
private void fill(int row, int column)
{
g.fillRect(column*tileSize,row*tileSize,tileSize,tileSize);
}
private void drawBlastRadii(Bomb b)
{

g.setPaint(new Color(0f, 0.24f, 1f, 0.1023f));

int row = getRow(b.y), col = getCol(b.x);

fill(row,col);

for (int r = row+1; r <= row+p.firePower; r++)
{
fill(r,col);
if (!walkable(r,col))
break;
}
for (int r = row-1; r >= row-p.firePower; r--)
{
fill(r,col);
if (!walkable(r,col))
break;
}
for (int c = col+1; c <= col+p.firePower; c++)
{
fill(row,c);
if (!walkable(row,c))
break;
}
for (int c = col-1; c >= col-p.firePower; c--)
{
fill(row,c);
if (!walkable(row,c))
break;
}

}

private void drawGrid()
{
g.setPaint(Color.RED);
for (int r = 0; r <= numRows*tileSize; r+=tileSize)
{
g.draw(new Line2D.Double(0,r,numCols*tileSize,r));
}
for (int c = 0; c <= numCols*tileSize; c+=tileSize)
{
g.draw(new Line2D.Double(c,0,c,numRows*tileSize));
}
}

private int getRow(double y)
{
return (int)(y / tileSize);
}
private int getCol(double x)
{
return (int)(x / tileSize);
}

public static void main(String[]args)
{
JFrame f = new JFrame("B o m b e r m a n !");
f.setSize((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight());
f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
bomberman b = new bomberman();
//f.setFocusable(true);

f.add(b);
f.setVisible(true);
f.setFocusable(false);
b.start();

}

class Fire
{
public double x, y;
public double initialTime;
public double timeLeft;
public Player owner;

public Fire(double x,double y, Player owner)
{
this.x=x;this.y=y; initialTime = 2; timeLeft = initialTime; this.owner=owner;
}
public void tic(double time)
{
timeLeft -= time / 1000;
}
public void draw(Graphics2D g)
{
g.setPaint(new Color((float)(timeLeft / initialTime), 0.420f, 0f, (float)(timeLeft / initialTime)));
g.fillRect((int)x-tileSize/2,(int)y-tileSize/2,tileSize,tileSize);
}

}

class Bomb
{
public double x, y;
public double initialTime;
public double timeLeft;
public Player owner;
public Bomb(double X, double Y, Player owner)
{
x=X;y=Y;timeLeft = 5;initialTime = 5; this.owner=owner;
}
public void tic(double t)
{
timeLeft -= t / 1000;
}
public void draw(Graphics2D g)
{
g.setPaint(new Color((float)(timeLeft / initialTime), 0f, 0f));//0.420f, (float)(timeLeft / initialTime), (float)(timeLeft / initialTime)));
g.fillOval((int)x-tileSize/4,(int)y-tileSize/4,tileSize/2,tileSize/2);
}

}
class Map
{
public int tileSize, numRows, numCols;
public Tile[][] tiles;
public Map()
{
tileSize = 42; numRows = 20; numCols = 20;
tiles = new Tile[numRows][numCols];

}
public Map randomGenerate()
{
Tile[][] tiles = new Tile[numRows][numCols];
for (int r = 0; r < tiles.length; r++)
for (int c = 0; c < tiles[0].length; c++)
{
tiles[r][c] = new Tile(Math.random() < 0.5);
}

//starting corners
tiles[0][0] = new Tile(false); tiles[1][0] = new Tile(false);tiles[0][1] = new Tile(false);
tiles[0][numCols-1] = new Tile(false); tiles[1][numCols-1] = new Tile(false); tiles[0][numCols-2] = new Tile(false);
tiles[numRows-1][0] = new Tile(false); tiles[numRows-2][0] = new Tile(false); tiles[numRows-1][1] = new Tile(false);
tiles[numRows-1][numCols-1] = new Tile(false); tiles[numRows-2][numCols-1] = new Tile(false); tiles[numRows-1][numCols-2] = new Tile(false);

Map out = new Map();
out.tiles = tiles;
return out;

}
public Map(String fileName)
{
try
{
Scanner scan = new Scanner(new File("maps\\" + fileName));
numRows = scan.nextInt(); scan.nextLine();
numCols = scan.nextInt(); scan.nextLine();
tileSize = 42;
this.tiles = new Tile[numRows][numCols];
for (int r = 0; r < numRows; r++)
{
String line = scan.nextLine();
for (int c = 0; c < numCols; c++)
{
tiles[r][c] = new Tile(line.charAt(c) == '1');
}
}



}catch(Exception e){e.printStackTrace();};
}
public void draw(Graphics2D g)
{
for (int r = 0; r < tiles.length; r++)
for (int c = 0; c < tiles[0].length; c++)
{
g.setPaint(tiles[r][c].getColor());
g.fillRect(tileSize*c, tileSize*r, tileSize, tileSize);
}
}


}
class Tile
{
public String name = "";
public boolean destroyable, walkable;
public Color color;
public Color getColor()
{
return color;
}
public Tile()
{

}
public Tile(boolean d)
{

destroyable = d; walkable = !d;
if (d)
{
name = "wall";
color = new Color(128f/255,128f/255,128f/255);
}
else
color = new Color(230f/255,32f/255,32f/255);
}



}
class Player
{
public double x, y;
public double lx, ly;
public Color color;
public int firePower, bombLimit;//@TODO this is yet to be implemented, because if i want multiple
								//players it might mean storing the bomb array in the player class.
public double speed;

public Player()
{
firePower = 1; bombLimit = 1;
x = 20; y = 20; color=color.WHITE;//color = new Color(0.52f, 0.34f, 0.42f);
}

public void draw(Graphics2D g)
{
g.setPaint(color);
g.fillRect((int)x - 10, (int)y - 10, 20, 20);
}


}
class NPC extends Player
{
public int[][] mapcopy;//0 = walkable, 1 = unwalkable

//public void 
/*public NPC()
{
//calls super automatically, i think?... no maybe i should erase this method
}*/





}
class Keyboard
{
public String[] keyNames = "w a s d space".split(" ");
public boolean[] keyDown;
public Keyboard()
{
keyDown = new boolean[keyNames.length];
}

public void keyPress(String name)
{
for (int i = 0; i < keyNames.length; i++)
if (keyNames[i].equals(name))
keyDown[i] = true;
}
public void keyRelease(String name)
{
for (int i = 0; i < keyNames.length; i++)
if (keyNames[i].equals(name))
keyDown[i] = false;
}

public boolean k(String name)
{
for (int i = 0; i < keyNames.length; i++)
if (keyNames[i].equals(name))
return keyDown[i];

return false;
}



}

}


