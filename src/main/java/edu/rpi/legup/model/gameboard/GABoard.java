package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.GARule;

import java.util.HashSet;
import java.util.Set;

public class GABoard extends Board {
    private Board baseBoard;
    private Set<PuzzleElement> pickableElements;
    private CaseRule caseRule;
    private ContradictionRule contradictionRule;
    private GARule gaRule;

    public GABoard(Board board, CaseRule caseRule, ContradictionRule contradictionRule, GARule gaRule) {
        super();
        this.pickableElements = new HashSet<>();
        this.baseBoard = board;
        this.caseRule = caseRule;
        this.contradictionRule = contradictionRule;
        this.gaRule = gaRule;
    }

    public Board getBaseBoard() {
        return baseBoard;
    }

    public void addPickableElement(PuzzleElement element) {
        this.pickableElements.add(element);
    }

    public boolean isPickable(PuzzleElement element) {
        return this.pickableElements.contains(baseBoard.getPuzzleElement(element));
    }

    @Override
    public Board copy() {
        return null;
    }

    public GARule getGaRule() {
        return gaRule;
    }
}
