package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GABoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.GARule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.GeneralAlgorithm;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FinishWithBulbsBasicRule extends GARule {

    public FinishWithBulbsBasicRule() {
        super("LTUP-BASC-0004", "Finish with Bulbs",
                "The remaining unknowns around a block must be bulbs to satisfy the number.",
                "edu/rpi/legup/images/lightup/rules/FinishWithBulbs.png",
                new LightOrEmptyCaseRule(),
                new TooFewBulbsContradictionRule());
    }

    @Override
    public GABoard getGABoard(Board board) {
        LightUpBoard lightUpBoard = (LightUpBoard) board;
        GABoard gaBoard = new GABoard(lightUpBoard, this.caseRule, this.contradictionRule, this);
        for (PuzzleElement data : lightUpBoard.getPuzzleElements()) {
            if (((LightUpCell) data).getType() == LightUpCellType.NUMBER) {
                gaBoard.addPickableElement(data);
            }
        }
        return gaBoard;
    }

//    @Override
//    public String checkRuleRaw(TreeTransition transition, PuzzleElement reference) {
//        LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
//        //LightUpCell initialCell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
//        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();
//        //LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);
//
//        Set<LightUpCell> adjCells = new HashSet<>();
//
//        boolean first = true;
//        for (PuzzleElement element : finalBoard.getModifiedData()) {
//            if (first) {
//                adjCells.addAll(finalBoard.getAdj((LightUpCell) element));
//                first = false;
//                continue;
//            }
//            adjCells.retainAll(finalBoard.getAdj((LightUpCell) element));
//        }
//
//        adjCells.removeIf(cell -> cell.getType() != LightUpCellType.NUMBER);
//
//        // get reference point - would be the number cell in this case
//        //Set<LightUpCell> adjCells = initialBoard.getAdj(initialCell);
//        //adjCells.removeIf(cell -> cell.getType() != LightUpCellType.NUMBER);
////        if (adjCells.isEmpty()) {
////            return "invalid1";
////        }
//        LightUpCell refPoint;
//        if (adjCells.size() == 1) {
//            refPoint = (LightUpCell) adjCells.toArray()[0];
//        }
//        else if (adjCells.size() > 1) {
//            return "bad";
//        }
//        else {
//            refPoint = null;
//        }
//
//        return GeneralAlgorithm.newRunBasic(
//                new LightOrEmptyCaseRule(),
//                new TooFewBulbsContradictionRule(),
//                transition,
//                refPoint
//        );
//    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement, PuzzleElement reference) {
        /*LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        LightUpCell initialCell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();
        LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);

        // get reference point - would be the number cell in this case
        Set<LightUpCell> adjCells = initialBoard.getAdj(initialCell);
        adjCells.removeIf(cell -> cell.getType() != LightUpCellType.NUMBER);
        if (adjCells.isEmpty()) {
            return super.getInvalidUseOfRuleMessage() + "invalid1";
        }

        LightUpCell refPoint = (LightUpCell) adjCells.toArray()[0];

        SatisfyNumberCaseRule satisfyNumberCaseRule = new SatisfyNumberCaseRule(); // Case rule
        TooFewBulbsContradictionRule tooFewBulbsContradictionRule = new TooFewBulbsContradictionRule(); // Contradiction rule

        ArrayList<Board> boards = new ArrayList<>();


        for (Board board : satisfyNumberCaseRule.getCases(initialBoard, refPoint)) {
            if (tooFewBulbsContradictionRule.checkContradictionAt(board, refPoint) != null) {
                // there is a contradiction
                boards.add(board);
            }
        }

        if (boards.size() != 1) {
            return super.getInvalidUseOfRuleMessage() + "invalid2";
        }

        Board board = boards.get(0);

        boolean contains = false;
        Set<PuzzleElement> modified = board.getModifiedData();

        if (finalBoard.getModifiedData().size() != modified.size()) {
            return super.getInvalidUseOfRuleMessage() + "invalid3";
        }
        for (PuzzleElement pe : board.getModifiedData()) {
            if (pe.equalsData(finalCell)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            return super.getInvalidUseOfRuleMessage() + "invalid4";
        }

        return null;*/
        LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        LightUpCell initialCell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();
        LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);

        // get reference point - would be the number cell in this case
        Set<LightUpCell> adjCells = initialBoard.getAdj(initialCell);
        adjCells.removeIf(cell -> cell.getType() != LightUpCellType.NUMBER);
        if (adjCells.isEmpty()) {
            return "invalid1";
        }

        LightUpCell refPoint = (LightUpCell) adjCells.toArray()[0];
        return GeneralAlgorithm.runBasic(new LightOrEmptyCaseRule(), new TooFewBulbsContradictionRule(), transition, puzzleElement, false);
    }

    private boolean isForced(LightUpBoard board, LightUpCell cell) {
        Set<LightUpCell> adjCells = board.getAdj(cell);
        adjCells.removeIf(c -> c.getType() != LightUpCellType.NUMBER);
        if (adjCells.isEmpty()) {
            return false;
        }

        LightUpBoard emptyCase = board.copy();
        emptyCase.getPuzzleElement(cell).setData(LightUpCellType.EMPTY.value);
        TooFewBulbsContradictionRule tooFew = new TooFewBulbsContradictionRule();
        for (LightUpCell c : adjCells) {
            if (tooFew.checkContradictionAt(emptyCase, c) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard initialBoard = (LightUpBoard) node.getBoard();
        LightUpBoard lightUpBoard = (LightUpBoard) node.getBoard().copy();
        for (PuzzleElement element : lightUpBoard.getPuzzleElements()) {
            LightUpCell cell = (LightUpCell) element;
            if (cell.getType() == LightUpCellType.UNKNOWN && isForced(initialBoard, cell)) {
                cell.setData(LightUpCellType.BULB.value);
                lightUpBoard.addModifiedData(cell);
            }
        }
        if (lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return lightUpBoard;
        }
    }
}
