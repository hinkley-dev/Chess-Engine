import java.util.*;

class Game
{

  int maxDepthLight;
  int maxDepthDark;
  Random r;
  Scanner in;
  static int INFINITY = 100000;

  Game()
  {
    r = new Random();
    in = new Scanner(System.in);
  }


//-------------------------------------------------------------------
//-------------------------------------------------------------------
//              GAMEPLAY FUNCTION
//-------------------------------------------------------------------
//-------------------------------------------------------------------
  void run(int light, int dark) throws Exception
  {
    maxDepthDark = dark;
    maxDepthLight = light;
    ChessState board = new ChessState();
    ChessState.ChessMove nextMove = new ChessState.ChessMove();

    board.printBoard(System.out);

//---------------GAME LOOP-----------------------
    while(!board.gameOver)
    {

      System.out.println("White Move");
      if(light == 0)
        playerMove(board, true);
      else
      {
        nextMove = bestMoveLight(board);
        board.move(nextMove.xSource, nextMove.ySource, nextMove.xDest, nextMove.yDest);
      }

      board.printBoard(System.out);
      if(board.gameOver) break;

      System.out.println("Black Move");
      if(dark == 0)
        playerMove(board, false);
      else
      {
        nextMove = bestMoveDark(board);
        board.move(nextMove.xSource, nextMove.ySource, nextMove.xDest, nextMove.yDest);
      }
      board.printBoard(System.out);
    }
  }

  void playerMove(ChessState board, boolean isWhite) throws Exception
  {
    int xi, yi, xf, yf;
    System.out.println("Enter the cordinates(using numbers) of the piece you'd like to move followed by the cordinates of the location you'd like to move it to.");
    xi = in.nextInt(); yi = in.nextInt(); xf = in.nextInt(); yf = in.nextInt();

    //---input validity check---
    while(!board.isValidMove(xi,yi,xf,yf) || !(board.isWhite(xi, yi) == isWhite))
    {
      System.out.println("Invalid move, please enter a new move. Make sure you're moveing your own piece.");
      System.out.println("Enter the cordinates(using numbers) of the piece you'd like to move followed by the cordinates of the location you'd like to move it to.");
      xi = in.nextInt(); yi = in.nextInt(); xf = in.nextInt(); yf = in.nextInt();
    }
    board.move(xi,yi,xf,yf);
  }


  int minimaxAB(ChessState board, int depth, int alpha, int beta, boolean isLight) throws Exception
  {
     //----return leaf node huristic----
     if(depth <= 0 || board.gameOver)
       return board.heuristic(r);

     ChessState.ChessMove m = new ChessState.ChessMove();
     if(isLight)
     {
       int best = -1*INFINITY;
       //passing true because computer is white
       ChessState.ChessMoveIterator it = board.iterator(true);
       while(it.hasNext())
       {
           m = it.next();
           ChessState newBoard = new ChessState(board);
           newBoard.move(m.xSource, m.ySource, m.xDest, m.yDest);
           best = Math.max(best, minimaxAB(newBoard, depth-1, alpha, beta, false));
           alpha = Math.max(alpha, best);
           if(beta <= alpha) break;
       }
       return best;
     }
     else
     {
        int best = INFINITY;
        ChessState.ChessMoveIterator it = board.iterator(false);
        while(it.hasNext())
        {
            m = it.next();
            ChessState newBoard = new ChessState(board);
            newBoard.move(m.xSource, m.ySource, m.xDest, m.yDest);
            best = Math.min(best, minimaxAB(newBoard, depth-1, alpha, beta, true));
            beta = Math.min(beta, best);
            if(beta <= alpha) break;
        }
        return best;
     }
  }

  ChessState.ChessMove bestMoveLight(ChessState board) throws Exception
  {
    int maxUtility= -1*INFINITY;
    int moveUtility = 0;
    ChessState.ChessMove maxMove = new ChessState.ChessMove();
    ChessState.ChessMove m;
    ChessState.ChessMoveIterator it = board.iterator(true);
    while(it.hasNext())
    {
      m = it.next();

      ChessState newBoard = new ChessState(board);
      newBoard.move(m.xSource, m.ySource, m.xDest, m.yDest);
      moveUtility = minimaxAB(newBoard, maxDepthLight, -1*INFINITY, INFINITY, false);

      if(moveUtility > maxUtility)
      {
        maxMove = m;
        maxUtility = moveUtility;
      }
    }
    return maxMove;
  }

  ChessState.ChessMove bestMoveDark(ChessState board) throws Exception
  {
    int maxUtility= INFINITY;
    int moveUtility = 0;
    ChessState.ChessMove maxMove = new ChessState.ChessMove();
    ChessState.ChessMove m;
    ChessState.ChessMoveIterator it = board.iterator(false);

    while(it.hasNext())
    {
        m = it.next();
        ChessState newBoard = new ChessState(board);
        newBoard.move(m.xSource, m.ySource, m.xDest, m.yDest);
        moveUtility = minimaxAB(newBoard, maxDepthDark, -1*INFINITY, INFINITY, true);

      if(moveUtility < maxUtility)
      {
        maxMove = m;
        maxUtility = moveUtility;
      }
    }
    return maxMove;
  }


  public static void main(String[] args) throws Exception
  {
    if(args.length !=2 )
    {
      System.out.println("Must enter two arguments. First argument is how many moves the light player can look ahead and the second is how many moves the light player can look ahead.");
        return;
    }

    Game g = new Game();
    int arg0 = Integer.parseInt(args[0]);
    int arg1 = Integer.parseInt(args[1]);

    g.run(arg0, arg1);
	}
}
