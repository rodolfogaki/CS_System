/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.UsuarioFacade;
import entidades.Usuario;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ManagedBean
@SessionScoped
public class UsuarioLogado implements Serializable{
    
    @EJB
    private UsuarioFacade usuarioFacade;
    
    private Usuario usuarioLogado = new Usuario(); 
    
    public UsuarioLogado() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                //usuarioLogado.setUsuario(((User) authentication.getPrincipal()).getUsername());
                System.out.println((authentication.getPrincipal()));
                this.usuarioLogado.setUsuario(((User) authentication.getPrincipal()).getUsername().toString());
            }
        }
    }

    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

}
