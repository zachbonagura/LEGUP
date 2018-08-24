package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;

public class FinishWithBulbsBasicRule extends BasicRule
{

    public FinishWithBulbsBasicRule()
    {
        super("Finish with Bulbs",
                "The remaining unknowns around a block must be bulbs to satisfy the number.",
                "edu/rpi/legup/images/lightup/rules/FinishWithBulbs.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement index of the puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        LightUpBoard initialBoard = (LightUpBoard)transition.getParents().get(0).getBoard();
        LightUpBoard finalBoard = (LightUpBoard)transition.getBoard();
        LightUpCell cell = (LightUpCell)finalBoard.getPuzzleElement(puzzleElement);
        if(cell.getType() != LightUpCellType.BULB)
        {
            return "Modified cells must be bulbs";
        }

        if(isForced(initialBoard, cell.getLocation()))
        {
            return null;
        }
        return "Bulb is not forced";
    }

    private boolean isForced(LightUpBoard board, Point location) {
        return isForcedBulb(board, new Point(location.x + 1, location.y)) ||
                isForcedBulb(board, new Point(location.x, location.y + 1)) ||
                isForcedBulb(board, new Point(location.x - 1, location.y)) ||
                isForcedBulb(board, new Point(location.x, location.y - 1));
    }

    private boolean isForcedBulb(LightUpBoard board, Point location)
    {
        int availableSpots = 0;

        LightUpCell cell = board.getCell(location.x, location.y);
        if(cell == null || cell.getType() != LightUpCellType.NUMBER)
        {
            return false;
        }

        int bulbs = cell.getData();
        cell = board.getCell(location.x + 1, location.y);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(location.x, location.y + 1);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(location.x - 1, location.y);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(location.x, location.y - 1);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        return bulbs == availableSpots;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard initialBoard = (LightUpBoard)node.getBoard();
        LightUpBoard lightUpBoard = (LightUpBoard)node.getBoard().copy();
        for(PuzzleElement element : lightUpBoard.getPuzzleElements()) {
            LightUpCell cell = (LightUpCell)element;
            if(cell.getType() == LightUpCellType.UNKNOWN && isForced(initialBoard, cell.getLocation())) {
                cell.setData(LightUpCellType.BULB.value);
                lightUpBoard.addModifiedData(cell);
            }
        }
        if(lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return lightUpBoard;
        }
    }
}
