//import java.io.File;
import java.io.*;
import java.util.Scanner;

public class convertMazeToLevel
{
public static void main(String[]args)
{
//System.out.println(args.length);
if (args.length != 1)
{
System.out.println("You have to give a filename to convert (go ahead and add the file extension)");
System.exit(-1);
}
try{
int[][] level;
Scanner scan = new Scanner(new File(args[0]));
int nr = scan.nextInt();scan.nextLine();
int nc = scan.nextInt();scan.nextLine();
level = new int[nr][nc];

for (int r= 0 ; r<  nr; r++)
{
String line = scan.nextLine();
for (int c = 0; c < nc; c++)
{
int x=  Integer.parseInt(line.substring(c,c+1));
if (x == 5 || x == 2 || x == 3)
x = 0;

level[r][c] = x; // 2 = entrance, 3 = exit?

}
}

scan.close();

String fileName = args[0];
FileWriter fstream = new FileWriter(fileName);
BufferedWriter write = new BufferedWriter(fstream);
write.write(nr + "\r\n");
write.write(nc + "\r\n");
for (int r = 0; r < nr; r++)
{
String line = "";
for (int c = 0; c < nc; c++)
{
line += "" + level[r][c];

}//end single for
write.write(line + "\r\n");
}//end double for
write.close();
fstream.close();

System.exit(0);


}catch(Exception e){e.printStackTrace();}
}
}