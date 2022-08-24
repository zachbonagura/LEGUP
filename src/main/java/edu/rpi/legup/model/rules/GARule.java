package edu.rpi.legup.model.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.GABoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.GeneralAlgorithm;

public abstract class GARule extends BasicRule {
    protected CaseRule caseRule;
    protected ContradictionRule contradictionRule;

    public GARule(String ruleID, String ruleName, String description, String imgName, CaseRule caseRule, ContradictionRule contradictionRule) {
        super(ruleID, ruleName, description, imgName);
        this.caseRule = caseRule;
        this.contradictionRule = contradictionRule;
    }

    public abstract GABoard getGABoard(Board board);

    public CaseRule getCaseRule() {
        return caseRule;
    }

    public ContradictionRule getContradictionRule() {
        return contradictionRule;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition, PuzzleElement reference) {
        return GeneralAlgorithm.newRunBasic(this.caseRule, this.contradictionRule, transition, reference);
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }

    @Override
    protected String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement, PuzzleElement reference) {
        return null;
    }
}
