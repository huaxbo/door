package com.am.cs12.commu.core.remoteCommand.forGprsSerial;


/**
 * 命令队列类
 * 非线程安全
 * @author liu runyu
 *
 */
public class CommandQueueForGprsSerial{
	
	private CommandNodeForGprsSerial head ;
	private CommandNodeForGprsSerial tail ;
	private int size ;
	
	public CommandQueueForGprsSerial(){
		this.head = new CommandNodeForGprsSerial(null, null, null, null, null, null, null, null ) ;
		this.tail = new CommandNodeForGprsSerial(null, null, null, null, null, null, null, null ) ;
		this.head.next = this.tail ;
		this.tail.pre = this.head ;
		this.size = 0 ;
	}
	
	protected void addHead(CommandNodeForGprsSerial node){
		if(this.head == null){
			return ;
		}
		node.next = this.head.next ;
		node.pre = this.head ;
		this.head.next = node ;
		node.next.pre = node ;
		this.size ++ ;
	}
	
	protected void addTail(CommandNodeForGprsSerial node){
		if(this.tail == null){
			return ;
		}
		node.next = this.tail ;
		node.pre = this.tail.pre ;
		this.tail.pre = node ;
		node.pre.next = node ;
		this.size ++ ;
	}
	
	protected CommandNodeForGprsSerial get(int index){
		if(index < 0){
			return null ;
		}
		if(index >= this.size){
			return null ;
		}
		int count = 0 ;
		CommandNodeForGprsSerial node = this.head.next ;
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
	
	protected CommandNodeForGprsSerial getCanDeal(int index){
		if(index < 0){
			return null ;
		}
		if(index >= this.sizeOfCanDeal()){
			return null ;
		}
		CommandNodeForGprsSerial node = this.head.next ;
		while(node != null && !node.canDeal){
			//找到第一个可处理的命令
			node = node.next ;
		}
		int count = 0 ;
		while(count < index){
			//找到指定(index)的可处理的命令
			if(node != null){
				node = node.next ;
				if(node.canDeal){
					count++ ;
				}
			}else{
				break ;
			}
		}
		return node ;
	}
	
	protected int remove(CommandNodeForGprsSerial node){
		if(node != null){
			CommandNodeForGprsSerial temp = this.head.next ;
			while(true){
				if(temp == null){
					break ;
				}else if(node != temp){
					temp = temp.next ;
				}else{
					CommandNodeForGprsSerial temPre = temp.pre ;
					CommandNodeForGprsSerial temTail = temp.next ;
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
	/**
	 * 得到可以处理的命令
	 * @return
	 */
	protected int sizeOfCanDeal(){
		if(this.size == 0){
			return 0 ;
		}
		int sizeOfCanDeal = 0 ;
		int count = 0 ;
		CommandNodeForGprsSerial node = this.head.next ;
		while(node != null && count < this.size){
			if(node != null && node.canDeal){
				sizeOfCanDeal++ ;
			}
			node = node.next ;
			count++ ;
		}
		return sizeOfCanDeal ;
	}
	
	/**
	 * 设置队列中所有命令都是可处理的
	 */
	protected void canDealAll(){
		int count = 0 ;
		CommandNodeForGprsSerial node = this.head.next ;
		while(node != null && count < this.size){
			node.canDeal = true ;		
			node = node.next ;
			count++ ;
		}
	}
}
