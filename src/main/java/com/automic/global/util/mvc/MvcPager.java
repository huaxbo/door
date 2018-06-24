package com.automic.global.util.mvc;

import com.automic.global.util.ConfigGloUtil;

public class MvcPager {

    /**
     * 当前页数
     */
    @MvcProAnno(MvcAct.con_pro)
    private Integer currPage = 1;
    
    /**
     * 每页记录数
     */
    @MvcProAnno(MvcAct.con_pro)
    private Integer perPage = Integer.parseInt(ConfigGloUtil.getElementText("sys_perPage"));
    
    /**
     * 总页数
     */
    private Integer totalPage = 1;
    
    /**
     * 总记录数
     */
    private Long totalRec;
    
    /**
     * 设置总记录数
     * @param count
     */
    public void setTotalRec(Long count){
        totalRec = count;
        int tp = getTotalPage();
        if(currPage > tp){
            currPage = tp;
        }
    }
    
    /**
     * 重置每页记录数
     * @param per
     */
    public void rePerPage(int per){
        perPage = per;
    }
    
    /**
     * 获取当前页首条记录数
     * @return
     */
    public Integer firstRec(){
        
        return (currPage - 1) * perPage;
    }
    /**
     * 当前页记录数
     * @return
     */
    public Integer maxRec(){
        
        return perPage;
    }
    /**
     * 查询总页数
     * @return
     */
    public Integer getTotalPage(){
        if(totalRec == null){
            
            return 0;
        }
        totalPage = totalRec.intValue()/perPage;
        if(totalRec.intValue()%perPage>0){
            totalPage +=1;
        }
        
        return totalPage;
    }
    
    /**
     * 自定义每页记录数
     * @param perPage
     */
    public void customPerPage(Integer perPage){
    	this.perPage = perPage;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }    
}
