/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidades.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Danilo_2
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "sistemaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario usuarioLogin(String login) {
        Query q = getEntityManager().createQuery(" from Usuario usu where usu.usuario = :usuario");
        q.setParameter("usuario", login);
        q.setMaxResults(1);    
        try {  
            return (Usuario) q.getSingleResult();  
        } catch ( NoResultException nre ) {  
            return null;  
        }      
    }
}
