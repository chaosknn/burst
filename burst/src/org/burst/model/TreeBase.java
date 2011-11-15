package org.burst.model;

import java.util.ArrayList;

import org.burst.tools.UniqueStringGenerator;


public class TreeBase {
	
	public static final String SEP = "_";
	
	protected String id = UniqueStringGenerator.getUniqueString();
	protected boolean isRoot = true;
	
	protected int level = 0;
	protected String path = "0";
	protected Object attachment = null;
	protected TreeBase parent = null;
	protected ArrayList<TreeBase> children = new ArrayList<TreeBase>();
	
	public TreeBase(){
		
	}
	
	public boolean hasChildren(){
		if(children != null && children.size()>0){
			return true;
		}
		return false;
	}
	
	public void addChild(TreeBase tree){
		if(children == null){
			children = new ArrayList<TreeBase>();
		}
		children.add(tree);
		tree.setParent(this);
		tree.setRoot(false);
		tree.resetLevel(level, path, children.size()-1);
	}
	
	public void addChildAt(TreeBase tree, int index){
		if(children == null){
			children = new ArrayList<TreeBase>();
		}
		children.add(index, tree);
		tree.setParent(this);
		tree.setRoot(false);
		tree.resetLevel(level, path, children.size()-1);
	}
	
	public void resetLevel(int parentLevel, String parentPath, int sort){
		level = parentLevel + 1;
		path = parentPath + SEP + sort;
		
		if(children == null){
			return;
		}
		
		TreeBase childTree;
		for(int i=0;i<children.size();i++){
			childTree = children.get(i);
			childTree.resetLevel(level, path, i);
		}
	}
	
	public void removeChildAt(int index){
		children.remove(index);
	}
	
	public TreeBase getNodeById(String id){
		
		if(this.id.equals(id)){
			return this;
		}
		
		if(children == null || children.size() == 0){
			return null;
		}
		
		TreeBase tree;
		TreeBase subTree;
		for(int i=0;i<children.size();i++){
			tree = children.get(i);
			subTree = tree.getNodeById(id);
			
			if(subTree != null){
				return subTree;
			}
		}
		
		return null;
	}
	
	public void printTree(){
		System.out.println(toString());
		if(children != null && children.size() > 0){
			for(int i=0;i<children.size();i++){
				TreeBase child = (TreeBase)children.get(i);
				child.printTree();
			}
		}
	}
	
	public String toString(){
		return "id:" + id + ";level:" + level + ";path:" + path; 
	}

	public void getAllParents(ArrayList<TreeBase> list){
		if(parent != null){
			list.add(parent);
			parent.getAllParents(list);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	public TreeBase getParent() {
		return parent;
	}
	
	public String getParentId() {
		if(parent == null){
			return null;
		}else{
			return parent.getId();
		}
	}

	public void setParent(TreeBase parent) {
		this.parent = parent;
	}

	public void setChildren(ArrayList<TreeBase> children) {
		this.children = children;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<TreeBase> getChildren() {
		return children;
	}
	
}
