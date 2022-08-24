package edu.rpi.legup.puzzle;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import edu.rpi.legup.puzzle.lightup.rules.SatisfyNumberCaseRule;
import edu.rpi.legup.puzzle.lightup.rules.TooFewBulbsContradictionRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GeneralAlgorithm {

    public static String newRunBasic(
            CaseRule caseRule,
            ContradictionRule contradictionRule,
            TreeTransition transition,
            PuzzleElement refPoint
    ) {
        LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();

        Set<PuzzleElement> modifiedElements = finalBoard.getModifiedData();

        ArrayList<Board> boards = new ArrayList<>();
        boards.add(initialBoard);

        Set<LightUpCell> adjCells = initialBoard.getAdj((LightUpCell) refPoint);

        //Set<Board> boards = new HashSet<>();

        for (LightUpCell c : adjCells) {
            ArrayList<Board> newBoards = new ArrayList<>();
            for (Board board : boards) {
                newBoards.addAll(caseRule.getCases(board, c));
            }
            boards.clear();
            boards.addAll(newBoards);
        }

        boards.addAll(caseRule.getCases(initialBoard, refPoint));

        ArrayList<Board> newBoards = new ArrayList<>();
        for (Board board : boards) {
            if (contradictionRule.checkContradiction(board) != null) {
                newBoards.add(board);
            }
//            if (contradictionRule.checkContradictionAt(board, refPoint) != null) {
//                newBoards.add(board);
//            }
        }

//        if (refPoint != null) {
//            for (Board board : caseRule.getCases(initialBoard, refPoint)) {
//                if (contradictionRule.checkContradictionAt(board, refPoint) != null) {
//                    boards.add(board);
//                }
//            }
//        }
//        else {
//            ArrayList<Board> tmpBoards = new ArrayList<>();
//            boards.add(initialBoard);
//            for (PuzzleElement element : modifiedElements) {
//                for (Board board : boards) {
//                    tmpBoards.addAll(caseRule.getCases(board, element));
//                }
//                boards.clear();
//                boards.addAll(tmpBoards);
//                tmpBoards.clear();
//            }
//            for (PuzzleElement element : modifiedElements) {
//                for (Board board : boards) {
//                    if (contradictionRule.checkContradictionAt(board, element) != null) {
//                        tmpBoards.add(board);
//                    }
//                }
//            }
//            boards.clear();
//            boards.addAll(tmpBoards);
//        }

        for (Board board : boards) {
            if (board.equalsBoard(finalBoard)) {
                return null;
            }
        }
        return "invalid";
        //boolean check = finalBoard.equalsBoard(boards.get(0));

//        if (boards.size() != 1) {
//            return "invalid2";
//        }
//
//        Board board = boards.get(0);
//
//        boolean contains = false;
//        Set<PuzzleElement> modified = board.getModifiedData();
//
//        if (finalBoard.getModifiedData().size() != modified.size()) {
//            return "invalid3";
//        }
//        for (PuzzleElement pe : modified) {
//            boolean innerContains = true;
//            for (PuzzleElement pe2 : modifiedElements) {
//                if (!pe2.equalsData(pe)) {
//                    innerContains = false;
//                    break;
//                }
//            }
//            if (innerContains) {
//                contains = true;
//                break;
//            }
//        }
//        if (!contains) {
//            return "invalid4";
//        }
//
//        return null;



        //return null;
    }

    public static String runBasic(
            CaseRule caseRule,
            ContradictionRule contradictionRule,
            TreeTransition transition,
            PuzzleElement puzzleElement,
            boolean noCasesSuccess
    ) {
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

//        SatisfyNumberCaseRule satisfyNumberCaseRule = new SatisfyNumberCaseRule(); // Case rule
//        TooFewBulbsContradictionRule tooFewBulbsContradictionRule = new TooFewBulbsContradictionRule(); // Contradiction rule

        ArrayList<Board> boards = new ArrayList<>();


        for (Board board : caseRule.getCases(initialBoard, refPoint)) {
            if (contradictionRule.checkContradictionAt(board, refPoint) != null) {
                // there is a contradiction
                boards.add(board);
            }
        }

        if (noCasesSuccess && boards.size() == 0) {
            return null;
        }

        if (boards.size() != 1) {
            return "invalid2";
        }

        Board board = boards.get(0);

        boolean contains = false;
        Set<PuzzleElement> modified = board.getModifiedData();

        if (finalBoard.getModifiedData().size() != modified.size()) {
            return "invalid3";
        }
        for (PuzzleElement pe : board.getModifiedData()) {
            if (pe.equalsData(finalCell)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            return "invalid4";
        }

        return null;
    }
}
