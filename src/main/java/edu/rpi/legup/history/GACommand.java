package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.GABoard;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;

import java.awt.event.MouseEvent;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class GACommand extends PuzzleCommand {

    private ElementView elementView;
    private TreeViewSelection selection;
    private GABoard board;
    private MouseEvent mouseEvent;

    public GACommand(ElementView elementView, TreeViewSelection selection, GABoard board, MouseEvent mouseEvent) {
        this.elementView = elementView;
        this.selection = selection;
        this.board = board;
        this.mouseEvent = mouseEvent;
    }

    @Override
    public String getErrorString() {
        return null;
    }

    @Override
    public void executeCommand() {
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        final TreeViewSelection newSelection = new TreeViewSelection();

        TreeNode node = (TreeNode) selection.getFirstSelection().getTreeElement();

    }

    @Override
    public void undoCommand() {

    }
}
