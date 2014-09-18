/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Danilo_2
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;
    
    public Object recuperar(Class entidade, Object id) {
        return getEntityManager().find(entidade, id);
    }

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void save(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        Query q=getEntityManager().createQuery("from "+entityClass.getSimpleName());
        return q.getResultList();
    }

    public List<T> findRange(int[] range) {
        Query q=getEntityManager().createQuery("from "+entityClass.getSimpleName());
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        Query q=getEntityManager().createQuery("from "+entityClass.getSimpleName());
        return q.getResultList().size();
    }
    
    public List<T> listaFiltrando(String s, String... atributos) {
        String hql = "from " + entityClass.getSimpleName() + " obj where ";
        for (String atributo : atributos) {
            hql += "lower(obj." + atributo + ") like :filtro OR ";
        }
        hql = hql.substring(0, hql.length() - 3);
        Query q = getEntityManager().createQuery(hql);
        q.setParameter("filtro", "%" + s.toLowerCase() + "%");
        return q.getResultList();
    }
    
        
    public T usuarioLogin(String login) {
        Query q = getEntityManager().createQuery(" from Usuario usu where usu.usuario = :usuario");
        q.setParameter("usuario", login);
        q.setMaxResults(1);   
        return (T) q.getSingleResult();    
    }
}

