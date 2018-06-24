package com.am.cs12.commu.core.remoteCommand.forSM;


public class CommandQueueForSM {
	private CommandNodeForSM head ;
	private CommandNodeForSM tail ;
	private int size ;
	
	public CommandQueueForSM(){
		this.head = new CommandNodeForSM(null, null, null, null) ;
		this.tail = new CommandNodeForSM(null, null, null, null) ;
		this.head.next = this.tail ;
		this.tail.pre = this.head ;
		this.size = 0 ;
	}
	
	protected void addHead(CommandNodeForSM node){
		if(this.head == null){
			return ;
		}
		node.next = this.head.next ;
		node.pre = this.head ;
		this.head.next = node ;
		node.next.pre = node ;
		this.size ++ ;
	}
	
	protected void addTail(CommandNodeForSM node){
		if(this.tail == null){
			return ;
		}
		node.next = this.tail ;
		node.pre = this.tail.pre ;
		this.tail.pre = node ;
		node.pre.next = node ;
		this.size ++ ;
	}
	
	protected CommandNodeForSM get(int index){
		if(index < 0){
			return null ;
		}
		if(index >= this.size){
			return null ;
		}
		int count = 0 ;
		CommandNodeForSM node = this.head.next ;
		while(count < index){
			if(node != null){
				node = node.next ;
				count++ ;
			}else{
				break ;
			}
			
		}
		return node ;
	}
	
	protected int remove(CommandNodeForSM node){
		if(node != null){
			CommandNodeForSM temp = this.head.next ;
			while(true){
				if(temp == null){
					break ;
				}else if(node != temp){
					temp = temp.next ;
				}else{
					CommandNodeForSM temPre = temp.pre ;
					CommandNodeForSM temTail = temp.next ;
					temPre.next = temTail ;
					temTail.pre = temPre ;
					this.size -- ;
					break ;
				}
			}
		}
		return this.size ;
	}
	
	protected int size(){
		return this.size ;
	}
}
