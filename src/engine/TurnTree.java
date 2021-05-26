package engine;

import java.util.ArrayList;
import java.util.Iterator;

public class TurnTree{
    private Node root;

    public TurnTree(Turn rootData) {
        root = new Node();
        root.data = rootData;
        root.children = new ArrayList<Node>();
    }

    public static class Node{
        private Turn data;
        private Node parent;
        private ArrayList<Node> children;
    }
    
    //Returns true if the tree contains 
    public boolean contains(Turn object) {
    	return false;
    }
    
    public ArrayList<String> getLabels(){
    	ArrayList<String> labels = new ArrayList<String>();
    	labels.add(root.data.toString());
    	for(int i = 1; i < root.children.size() ; i++) {
    		labels.addAll(getLabels(root.children.get(i)));
    	}
    	labels.addAll(getLabels(root.children.get(0)));
    	return labels;
    }
    
    public static ArrayList<String> getLabels(Node currentNode){
    	ArrayList<String> labels = new ArrayList<String>();
    	labels.add(currentNode.data.toString());
    	for(int i = 1; i < currentNode.children.size() ; i++) {
    		labels.addAll(getLabels(currentNode.children.get(i)));
    	}
    	labels.addAll(getLabels(currentNode.children.get(0)));
    	return labels;
    }
    
    public ArrayList<Turn> getTurnsLinear(){
    	ArrayList<Turn> labels = new ArrayList<Turn>();
    	labels.add(root.data);
    	for(int i = 1; i < root.children.size() ; i++) {
    		labels.addAll(getTurnsLinear(root.children.get(i)));
    	}
    	labels.addAll(getTurnsLinear(root.children.get(0)));
    	return labels;
    }
    
    public static ArrayList<Turn> getTurnsLinear(Node currentNode){
    	ArrayList<Turn> labels = new ArrayList<Turn>();
    	labels.add(currentNode.data);
    	for(int i = 1; i < currentNode.children.size() ; i++) {
    		labels.addAll(getTurnsLinear(currentNode.children.get(i)));
    	}
    	labels.addAll(getTurnsLinear(currentNode.children.get(0)));
    	return labels;
    }
}