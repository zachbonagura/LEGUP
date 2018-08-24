package edu.rpi.legup.controller;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.history.*;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.rules.*;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.rulesview.RuleButton;
import edu.rpi.legup.ui.treeview.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class RuleController implements ActionListener {
    protected Object lastSource;

    /**
     * RuleController Constructor - creates a edu.rpi.legup.controller object to listen
     * to edu.rpi.legup.ui events from a RulePanelView
     */
    public RuleController() {
        super();
    }

    /**
     * Button Pressed event - occurs a when a rule button has been pressed
     *
     * @param rule rule of the button that was pressed
     */
    public void buttonPressed(Rule rule) {
        TreePanel treePanel = GameBoardFacade.getInstance().getLegupUI().getTreePanel();
        TreeView treeView = treePanel.getTreeView();
        Puzzle puzzle = getInstance().getPuzzleModule();
        TreeViewSelection selection = treeView.getSelection();
        List<TreeElementView> selectedViews = selection.getSelectedViews();

        String updateErrorString = "";
        if (rule.getRuleType() == RuleType.CASE) {
            CaseRule caseRule = (CaseRule)rule;
            if (selectedViews.size() == 1) {
                TreeElementView elementView = selection.getFirstSelection();
                TreeElement element = elementView.getTreeElement();
                if (element.getType() == TreeElementType.TRANSITION) {
                    ICommand caseRuleCommand = new ValidateCaseRuleCommand(selection, caseRule);
                    if (caseRuleCommand.canExecute()) {
                        caseRuleCommand.execute();
                        getInstance().getHistory().pushChange(caseRuleCommand);
                    } else {
                        updateErrorString = caseRuleCommand.getError();
                    }
                } else {
                    if(LegupPreferences.getInstance().getUserPref(LegupPreferences.AUTO_GENERATE_CASES).equalsIgnoreCase(Boolean.toString(true))) {
                        CaseBoard caseBoard = caseRule.getCaseBoard(element.getBoard());

                        if(caseBoard.getCount() > 0) {
                            puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(caseBoard));
                        } else {
                            updateErrorString = "This board cannot be applied with this case rule.";
                        }
                    } else {
                        updateErrorString = "Auto generated case rules are turned off in preferences.";
                    }
                }
            } else {
                ICommand caseRuleCommand = new ValidateCaseRuleCommand(selection, caseRule);
                if (caseRuleCommand.canExecute()) {
                    caseRuleCommand.execute();
                    getInstance().getHistory().pushChange(caseRuleCommand);
                } else {
                    updateErrorString = caseRuleCommand.getError();
                }
            }
        } else if (rule.getRuleType() == RuleType.CONTRADICTION) {
            ICommand validate = new ValidateContradictionRuleCommand(selection, rule);
            if (validate.canExecute()) {
                getInstance().getHistory().pushChange(validate);
                validate.execute();
            } else {
                updateErrorString = validate.getError();
            }
        } else {
            boolean def = LegupPreferences.getInstance().getUserPref(LegupPreferences.ALLOW_DEFAULT_RULES).equalsIgnoreCase(Boolean.toString(true));
            ICommand validate = def ? new ApplyDefaultBasicRuleCommand(selection, (BasicRule) rule) : new ValidateBasicRuleCommand(selection, rule);
            if (validate.canExecute()) {
                getInstance().getHistory().pushChange(validate);
                validate.execute();
            } else {
                updateErrorString = validate.getError();
            }
        }
        GameBoardFacade.getInstance().getLegupUI().getTreePanel().updateError(updateErrorString);
    }

    /**
     * ICommand Performed event - occurs when a rule button has been pressed
     *
     * @param e action event object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        lastSource = e.getSource();
        RuleButton button = (RuleButton) lastSource;
        buttonPressed(button.getRule());
    }
}
