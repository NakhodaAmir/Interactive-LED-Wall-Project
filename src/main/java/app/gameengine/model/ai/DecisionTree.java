package app.gameengine.model.ai;

import app.gameengine.Level;
import app.gameengine.model.datastructures.BinaryTreeNode;

public class DecisionTree {
    private BinaryTreeNode<Decision> bst;

    public DecisionTree(BinaryTreeNode<Decision> bst) {
        this.bst = bst;
    }

    public BinaryTreeNode<Decision> getTree() {
        return bst;
    }

    public void setTree(BinaryTreeNode<Decision> bst) {
        this.bst = bst;
    }

    public void traverse(double time, Level level) {
        Decision decision = traverse(bst, time, level);
        if (decision == null) { return; }
        decision.doAction(time, level);
    }

    public Decision traverse(BinaryTreeNode<Decision> node, double time, Level level) {
        if (node == null) { return null; }
        return traverseHelper(node, time, level);
    }

    private Decision traverseHelper(BinaryTreeNode<Decision> node, double time, Level level) {
        if (node.getRight() == null && node.getLeft() == null) { return node.getValue();}

        if (node.getValue().decide(time, level)) {
            if (node.getRight() == null) { return null; }
            return traverseHelper(node.getRight(), time, level);
        }
        else {
            if (node.getLeft() == null) { return null; }
            return traverseHelper(node.getLeft(), time, level);
        }
    }
}
