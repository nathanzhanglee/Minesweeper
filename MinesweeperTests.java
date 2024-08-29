import org.junit.Test;

import static org.junit.Assert.*;

public class MinesweeperTests {

    @Test
    public void numSurrondingBombsEdge() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        currBoard[0][1].placeBomb();
        currBoard[1][0].placeBomb();
        currBoard[1][1].placeBomb();
        assertEquals(3, ms.numSurrondingBombs(0, 0));
    }

    @Test
    public void flag() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        ms.addFlag(0, 0);
        assertEquals(11, ms.getNumFlags());
    }

    @Test
    public void flagAndUnflag() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        ms.addFlag(0, 1);
        ms.addFlag(0, 1);
        assertEquals(12, ms.getNumFlags());
    }

    @Test
    public void flagWhenOutOfFlags() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        ms.addFlag(0, 1);
        ms.addFlag(0, 2);
        ms.addFlag(0, 3);
        ms.addFlag(0, 4);
        ms.addFlag(0, 5);
        ms.addFlag(0, 6);
        ms.addFlag(0, 7);
        ms.addFlag(0, 8);
        ms.addFlag(0, 9);
        ms.addFlag(1, 0);
        ms.addFlag(1, 1);
        ms.addFlag(1, 2);
        assertEquals(0, ms.getNumFlags());
        ms.addFlag(9, 9);
        assertEquals(0, ms.getNumFlags());
        assertFalse(currBoard[9][9].getFlagStatus());
    }

    @Test
    public void saveAndLoadSameBoard() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        currBoard[0][0].reveal();
        Space[][] oldBoard = ms.getBoard();
        ms.saveGame();
        currBoard[0][1].reveal();
        ms.loadGame();
        Space[][] newBoard = ms.getBoard();
        assertArrayEquals(newBoard, oldBoard);
    }

    @Test
    public void saveAndLoadNumFlags() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        currBoard[0][0].flag();
        Space[][] oldBoard = ms.getBoard();
        ms.saveGame();
        currBoard[1][0].flag();
        ms.loadGame();
        Space[][] newBoard = ms.getBoard();
        assertEquals(11, ms.getNumFlags());
    }

    @Test
    public void noWinForOnlyFlags() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        currBoard[0][0].placeBomb();
        ms.addFlag(0, 0);
        assertFalse(ms.checkWin());
    }

    @Test
    public void gameLost() {
        Minesweeper ms = new Minesweeper();
        Space[][] currBoard = ms.getBoard();
        currBoard[0][0].placeBomb();
        assertFalse(ms.hasGameLost());
        ms.uncoverSpace(0, 0);
        assertTrue(ms.hasGameLost());

    }
}
