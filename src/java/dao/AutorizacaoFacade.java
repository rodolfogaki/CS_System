/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidades.Autorizacao;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Danilo_2
 */
@Stateless
public class AutorizacaoFacade extends AbstractFacade<Autorizacao> {

    @PersistenceContext(unitName = "sistemaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AutorizacaoFacade() {
        super(Autorizacao.class);
    }

}

