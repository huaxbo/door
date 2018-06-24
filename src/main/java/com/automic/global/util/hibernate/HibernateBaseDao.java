package com.automic.global.util.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateBaseDao {
            
    @Autowired
    @Resource(name="hbSessionFactory")
    private SessionFactory sessionFac;
            
    /**
     * 获取当前session
     * @return
     */
    private Session getSession(){
        
        return sessionFac.getCurrentSession();
    }
    
    /**
     * 自定义数据库操作
     * @param callback
     * @return
     */
    public Object customQuery(BaseHibernateCall callback){
        Session se = getSession();
        if(se != null){
            
            return callback.execute(se);
        }
        
        return null;
    }
    
    /**
     * HQL统计数量
     * @param hql
     * @return
     */
    public Long queryCount(String hql){
        Session se = getSession();
        if(se != null){
            
            return (Long)se.createQuery(hql).uniqueResult();
        }
        
        return 0L;
    }
    
    /**
     * SQL统计数量
     * @param sql
     * @return
     */
    public Long sqlCount(String sql){
        Session se = getSession();
        if(se != null){
            
            return (Long)se.createSQLQuery(sql).uniqueResult();
        }
        
        return 0L;
    }
    
    /**
     * SQL查询
     * @param sql
     * @return
     */
    public Object sqlQuery(String sql){
    	Session se = getSession();
    	if(se != null){
    		
    		return se.createSQLQuery(sql).uniqueResult();
    	}
    	
    	return null;
    }
    /**
     * HQL查询记录
     * 分页触发：firstRec>=0 && maxRec>0
     * @param firstRec
     * @param maxRec
     * @param hql
     * @return
     */
    public List<?> queryList(int firstRec,int maxRec, String hql,boolean cacheable){
        List<?> lt = null;
        Session se = getSession();
        if(se != null){
            Query q = se.createQuery(hql);
            q.setCacheable(cacheable);
            if(firstRec >= 0 && maxRec > 0){
                q.setFirstResult(firstRec);
                q.setMaxResults(maxRec);
            }
            
            lt = q.list();
        }
        
        return lt;
    }

    /**
     * SQL查询记录
     * 分页触发：firstRec>=0 && maxRec>0 
     * @param firstRec
     * @param maxRec
     * @param sql
     * @return
     */
    public List<?> sqlList(int firstRec,int maxRec, String sql,boolean cacheable){
        List<?> lt = null;
        Session se = getSession();
        if(se != null){
            SQLQuery q = se.createSQLQuery(sql);
            q.setCacheable(cacheable);
            if(firstRec > 0 && maxRec > 0){
                q.setFirstResult(firstRec);
                q.setMaxResults(maxRec);
            }
            
            lt = q.list();
        }
        
        return lt;
    }
    
    /**
     * HQL查询对象
     * @param t：对象class
     * @param id：对象主键id值
     * @return
     */
    public Object getById(Class<?> t,String id){
        Session se = getSession();
        if(se != null){
            List<?> lt = se.createQuery("from " + t.getSimpleName() + " where id = '" + id + "'").list();
            if(lt.size() > 0){
                
                return lt.get(0);
            }
        }    
        
        return null;
    }
    
    /**
     * HQL根据属性查询对象
     * @param t：对象class
     * @param id：对象主键id值
     * @return
     */
    public List<?> getByProperty(Class<?> t,String property,String value){
    	Session se = getSession();
    	List<?> lt = new ArrayList<>();
    	if(se != null){
    		lt = se.createQuery("from " + t.getSimpleName() + " where "+property+" = '" + value + "'").list();
    	}    
    	return lt;
    }
    
    /**
     * HQL删除对象
     * @param t：对象class
     * @param id：对象主键id
     * @return
     */
    public void delById(Class<?> t,String id){
        Session se = getSession();
        if(se != null){
        	StringBuilder hql = new StringBuilder();
        	hql.append(" delete from");
        	hql.append(t.getSimpleName());
        	hql.append(" where id = '");
        	hql.append(id);
        	hql.append("'");
        	        	
            se.createQuery(hql.toString()).executeUpdate();            
        }    
    }
    
    /**
     * hibernate保存对象
     * @param po
     */
    public void save(Object po){
        Session se = getSession();
        if(se != null){            
            se.save(po);
        }        
    }
    /**
     * hibernate保存更新对象
     * @param po
     */
    public void saveOrUpdate(Object po){
        Session se = getSession();
        if(se != null){            
            se.saveOrUpdate(po);
        }        
    }
    /**
     * hibernate删除对象
     * @param po
     */
    public void del(Object po){
        Session se = getSession();
        if(se != null){            
            se.delete(po);
        }        
    }
    /**
     * hibernate更新对象
     * @param po
     */
    public void update(Object po){
        Session se = getSession();
        if(se != null){            
            se.update(po);
        }        
    }
    
    /**
     * hibernate获取对象
     * @param po
     */
    public Object get(Class<?> t,String id){
        Session se = getSession();
        if(se != null){
            
            return se.get(t, id);
        }    
        
        return null;
    }
    
    /**
     * hibernate load对象
     * @param po
     */
    public Object load(Class<?> t,String id){
        Session se = getSession();
        if(se != null){            
            return se.load(t,id);
        }        
        
        return null;
    }
    
}
