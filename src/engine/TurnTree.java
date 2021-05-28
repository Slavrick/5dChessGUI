package engine;

import java.util.ArrayList;

public class TurnTree{
    public Node root;
    public static int globalIDCTR = 0;

    public TurnTree(Turn rootData) {
        root = new Node(rootData,null);
    }

    public static class Node{
    	public Node(Turn data, Node parent) {
    		this.data = data;
    		children = new ArrayList<Node>();
    		this.parent = parent;
    		nodeID = globalIDCTR++;
    	}
    	
    	public Node addChild(Turn data) {
    		Node newNode = new Node(data, this);
    		children.add(newNode);
    		return newNode;
    	}
    	
    	public Turn data;
        private Node parent;
        public ArrayList<Node> children;
        //FIXME fix this based on searching for id instead
        private int nodeID;
		public Node getParent() {
			return this.parent;
		}

		public int getNodeID() {
			return nodeID;
		}
		
		public String toString() {
			return nodeID + ", " +  data.toString();
		}
    }
    
    //All of these functions are depth first searches btw.
    public boolean contains(Turn target) {
    	if(root.data.equals(target)) {
    		return true;
    	}
    	for(int i = 0; i < root.children.size(); i++) {
    		if(contains(root.children.get(i), target)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    //Returns true if the tree contains 
    public static boolean contains(Node n, Turn target) {
    	if(n.data.equals(target)) {
    		return true;
    	}
    	if(target.turnNum <= n.data.turnNum) {
    		return false;
    	}
    	for(int i = 0; i < n.children.size(); i++) {
    		if(contains(n.children.get(i), target)) {
    			return true;
    		}
    	}
    	return false;
    }
   
    public boolean contains(int targetNodeID) {
    	if(root.nodeID == targetNodeID) {
    		return true;
    	}
    	for(int i = 0; i < root.children.size(); i++) {
    		if(contains(root.children.get(i), targetNodeID)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    //Returns true if the tree contains 
    public static boolean contains(Node n, int targetNodeID) {
    	if(n.nodeID == targetNodeID) {
    		return true;
    	}
    	for(int i = 0; i < n.children.size(); i++) {
    		if(contains(n.children.get(i), targetNodeID)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public Node findNode(Turn target) {
    	if(root.data.equals(target)) {
    		return root;
    	}
    	for(int i = 0; i < root.children.size(); i++) {
    		Node n = findNode(root.children.get(i), target);
    		if(n != null) {
    			return n;
    		}
    	}
    	return null;
    }
    
    //Returns true if the tree contains 
    public static Node findNode(Node n, Turn target) {
    	if(n.data.equals(target)) {
    		return n;
    	}
    	if(target.turnNum <= n.data.turnNum) {
    		return null;
    	}
    	for(int i = 0; i < n.children.size(); i++) {
    		Node find = findNode(n.children.get(i), target);
    		if(find != null) {
    			return find;
    		}
    	}
    	return null;
    }
    
    public static ArrayList<Integer> navPath(Node n, int targetNodeID) {
    	if(n.nodeID == targetNodeID) {
    		return new ArrayList<Integer>();
    	}
    	ArrayList<Integer> path = new ArrayList<Integer>();
    	for(int i = 0; i < n.children.size(); i++) {
    		ArrayList<Integer> temp =  navPath(n.children.get(i),targetNodeID);
    		if(temp != null) {
    			path.add(i);
    			path.addAll(temp);
    			return path;
    		}
    	}
    	return null;
    }
    
    public ArrayList<String> getLabels(){
    	ArrayList<String> labels = new ArrayList<String>();
    	labels.add(root.data.toString());
    	if(root.children.size() > 0) {
	    	for(int i = 1; i < root.children.size() ; i++) {
	    		labels.addAll(getLabels(root.children.get(i),1));
	    	}
	    	labels.addAll(getLabels(root.children.get(0),0));
    	}
    	return labels;
    }
    
    public static ArrayList<String> getLabels(Node currentNode, int nesting){
    	ArrayList<String> labels = new ArrayList<String>();
    	String label = "";
    	for(int i = 0; i < nesting; i++) {
    		label += "    ";
    	}
    	labels.add(label + currentNode.data.toString());
    	if(currentNode.children.size() > 0) {
	    	for(int i = 1; i < currentNode.children.size() ; i++) {
	    		labels.addAll(getLabels(currentNode.children.get(i),nesting+1));
	    	}
	    	labels.addAll(getLabels(currentNode.children.get(0),nesting));
    	}
    	return labels;
    }
    
    public ArrayList<Turn> getTurnsLinear(){
    	ArrayList<Turn> labels = new ArrayList<Turn>();
    	labels.add(root.data);
    	if(root.children.size() > 0) {
    		for(int i = 1; i < root.children.size() ; i++) {
    			labels.addAll(getTurnsLinear(root.children.get(i)));
    		}
    		labels.addAll(getTurnsLinear(root.children.get(0)));
    	}
    	return labels;
    }
    
    public static ArrayList<Turn> getTurnsLinear(Node currentNode){
    	ArrayList<Turn> labels = new ArrayList<Turn>();
    	labels.add(currentNode.data);
    	if(currentNode.children.size() > 0) {
	    	for(int i = 1; i < currentNode.children.size() ; i++) {
	    		labels.addAll(getTurnsLinear(currentNode.children.get(i)));
	    	}
	    	labels.addAll(getTurnsLinear(currentNode.children.get(0)));
    	}
    	return labels;
    }
    
    
    public ArrayList<Node> getNodesLinear(){
    	ArrayList<Node> nodes = new ArrayList<Node>();
    	nodes.add(root);
    	if(root.children.size() > 0) {
    		for(int i = 1; i < root.children.size() ; i++) {
    			nodes.addAll(getNodesLinear(root.children.get(i)));
    		}
    		nodes.addAll(getNodesLinear(root.children.get(0)));
    	}
    	return nodes;
    }
    
    public static ArrayList<Node> getNodesLinear(Node currentNode){
    	ArrayList<Node> nodes = new ArrayList<Node>();
    	nodes.add(currentNode);
    	if(currentNode.children.size() > 0) {
	    	for(int i = 1; i < currentNode.children.size() ; i++) {
	    		nodes.addAll(getNodesLinear(currentNode.children.get(i)));
	    	}
	    	nodes.addAll(getNodesLinear(currentNode.children.get(0)));
    	}
    	return nodes;
    }
}