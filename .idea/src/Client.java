import java.io.*;

import game.*;

import java.net.*;

import logic.*;

public class Client
{
    public static void main(String[] args)
    {
        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[8][8];

        try {
            MyClient = new Socket("localhost", 8888);
            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            
            while(1 < 2){
                char cmd = 0;

                cmd = (char)input.read();

                // Début de la partie en joueur blanc
                
                if(cmd == '1'){  
                    byte[] aBuffer = new byte[1024];
                    Board.getInstance().setPlayerIsBlack(false);
                    
                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    String[] boardValues;
                    boardValues = s.split(" ");
                  
                    
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        //remplir le board du AI
                        Board.getInstance().setCase(x,y,Integer.parseInt(boardValues[i]));
                        y++;
                        if(y == 8){
                            x = 0;
                            x++;
                        }
                    }

                    System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }

                // Début de la partie en joueur Noir
                if(cmd == '2'){

                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    byte[] aBuffer = new byte[1024];
                    Board.getInstance().setPlayerIsBlack(true);
                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x][y] = Integer.parseInt(boardValues[i]);
                      //remplir le board du AI
                        Board.getInstance().setCase(x,y,Integer.parseInt(boardValues[i]));
                        y++;
                        if(y == 8){
                            y = 0;
                            x++;
                        }
                    }
                    
                    Board.getInstance().printBoard();
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joué.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);                
                    ActionGenerator gen = new ActionGenerator();
                    Action action = new Action(s, Board.getInstance().isPlayerIsBlack());
                    Board.getInstance().updateBoard(action);
             
                    /*
                    System.out.println("Entrez votre coup : ");
                    move = console.readLine();
                    */
                    //action = new Action(move, Board.getInstance().isPlayerIsBlack());
                    Action playerAction = gen.generateAction(Board.getInstance().isPlayerIsBlack());
                    System.out.println(playerAction.getX() + " - " + playerAction.getY());
                    System.out.println(playerAction.getX2() + " - " + playerAction.getY2());
                 
                    
                    Board.getInstance().updateBoard(playerAction);
                    String move = null;
                    move = playerAction.getMoveAsString(playerAction);
            
                    System.out.println(move);
                    
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}