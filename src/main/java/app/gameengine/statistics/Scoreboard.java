package app.gameengine.statistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import app.gameengine.Game;
import app.gameengine.model.datastructures.BinaryTreeNode;
import app.gameengine.model.datastructures.Comparator;
import app.gameengine.model.datastructures.LinkedListNode;

/**
 * A scoreboard for sorting scores of specific games.
 */
public class Scoreboard {

    private static String STATS_DIRECTORY = "data/stats/";
    private static String STATS_POSTFIX = "_stats.csv";

    private String statsPath;
    private BinaryTreeNode<GameStat> root;

    private Comparator<GameStat> comparator;
    private boolean calledStats;

    public Scoreboard(String gameName, Comparator<GameStat> comparator) {
        this.statsPath = STATS_DIRECTORY + gameName.toLowerCase() + STATS_POSTFIX;
        this.comparator = comparator;
        this.calledStats = false;
    }

    public Comparator<GameStat> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<GameStat> comparator) {
        this.comparator = comparator;
    }

    public void addScore(GameStat score) {
        if (root == null) {
            root = new BinaryTreeNode<>(score, null, null);
        }
        else {
            addScoreHelper(root, score);
        }
    }

    private void addScoreHelper(BinaryTreeNode<GameStat> node, GameStat toInsert) {
        if (comparator.compare(toInsert, node.getValue())) {
            if (node.getLeft() == null) {
                node.setLeft(new BinaryTreeNode<>(toInsert, null, null));
            } else {
                addScoreHelper(node.getLeft(), toInsert);
            }
        } else {
            if (node.getRight() == null) {
                node.setRight(new BinaryTreeNode<>(toInsert, null, null));
            }
            else {
                addScoreHelper(node.getRight(), toInsert);
            }
        }
    }

    public LinkedListNode<GameStat> getScoreList() {
        if (root == null) { return null;}
        LinkedListNode<GameStat> head = new LinkedListNode<>(null, null);
        inOrderTraversal(root, head);
        return head;
    }

    public LinkedListNode<GameStat> getScoreList(BinaryTreeNode<GameStat> binaryTreeNode) {
        if (binaryTreeNode == null) { return null; }
        LinkedListNode<GameStat> head = new LinkedListNode<>(null, null);
        inOrderTraversal(binaryTreeNode, head);
        return head;
    }

    private void inOrderTraversal(BinaryTreeNode<GameStat> binaryTreeNode, LinkedListNode<GameStat> linkedListNode) {
        if (binaryTreeNode != null) {
            inOrderTraversal(binaryTreeNode.getLeft(), linkedListNode);
            if (linkedListNode.getValue() == null) {
                linkedListNode.setValue(binaryTreeNode.getValue());
            } else {
                linkedListNode.append(binaryTreeNode.getValue());
            }
            inOrderTraversal(binaryTreeNode.getRight(), linkedListNode);
        }
    }

    public BinaryTreeNode<GameStat> getScoreTree() {
        return root;
    }

    public void  setScoreTree(BinaryTreeNode<GameStat> root) {
        this.root = root;
    }

    public void loadStats() {
        if (calledStats) { return; }
        calledStats = true;
        try {
            ArrayList<String> csvFile = new ArrayList<>(Files.readAllLines(Paths.get(statsPath)));
            for (String s : csvFile) {
                ArrayList<String> lines = new ArrayList<>(Arrays.asList(s.split(",")));
                if (lines.isEmpty()) {
                    continue;
                }
                GameStat gameStat = new GameStat(lines.get(0), Double.parseDouble(lines.get(1)), Double.parseDouble(lines.get(2)));
                addScore(gameStat);
            }
        } catch (IOException e) { }
    }

    public void saveStats() {
        this.loadStats();
        if (this.root == null) {
            return;
        }
        System.out.println("** Saving statistics to " + this.statsPath + " **");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.statsPath))) {
            for (LinkedListNode<GameStat> stats = this.getScoreList(); stats != null; stats = stats.getNext()) {
                writer.write(stats.getValue().toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("** Could not save statistics to " + this.statsPath + " **");
            e.printStackTrace();
        }
    }

}
