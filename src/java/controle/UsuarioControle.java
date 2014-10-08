/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.AutorizacaoFacade;
import dao.UsuarioFacade;
import entidades.Autorizacao;
import entidades.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import util.Util;

/**
 *
 * @author Danilo_2
 */
@ManagedBean
@SessionScoped
public class UsuarioControle implements Serializable {

    private Usuario usuario;
    private String  senhaAtual;
    private String  novaSenha;
    private String  confirmaSenha;

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private AutorizacaoFacade autorizacaoFacade;
    private List<Usuario> lista = new ArrayList<Usuario>();
    private String filtro = "";
    private Usuario usuarioselecionada;
    private boolean editando;

 public String salvar() {
        String retorno = "usuarioedita";
        if (validaCampos()) {
            if (!editando) {
                this.usuarioselecionada = usuarioLogin(usuario.getUsuario());
                if ( usuarioselecionada == null ) {
                    Autorizacao auto = new Autorizacao(1L, "ROLE_USER");
                    usuario.getAutorizacoes().add(auto);
                    usuario.setEnable(true);
                    usuario.setSenha(Util.md5("123mudar"));
                    usuarioFacade.save(usuario);
                    retorno = "usuariolista";
                } else {
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"O usuário informado já existe!", "O usuário informado já existe!"));  
                }              
            } else {
                usuarioFacade.save(usuario);
                retorno = "usuariolista";
            }
            return retorno;
        } else {
            return retorno;
        }
    }

    public void novo() {
        this.usuario = new Usuario();
        editando = false;
    }

    public void editar(ActionEvent e) {
        setUsuario((Usuario) e.getComponent().getAttributes().get("usuario"));
        editando = true;
    }

    public void alterarSenha() {
        FacesContext context        = FacesContext.getCurrentInstance();
        ExternalContext external    = context.getExternalContext();
        this.usuario = usuarioLogin(external.getRemoteUser().toString());
        if (this.novaSenha.equals(this.confirmaSenha)) {     
            if (Util.md5(this.senhaAtual.toString()).equals(this.usuario.getSenha().toString()) ){
                this.usuario.setSenha(Util.md5(this.novaSenha.toString()));
                usuarioFacade.save(usuario);   
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Senha alterada com sucesso.", "Senha alterada com sucesso."));           
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Senha atual incorreta!", "Senha atual incorreta!"));           
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Senha confirmada incorretamente!", "Senha confirmada incorretamente!"));           
        }
    }
    
    public void excluir(ActionEvent e) {
        usuario = (Usuario) e.getComponent().getAttributes().get("usuario");
        usuarioFacade.remove(usuario);
    }
    
    public void reset(ActionEvent e) {
        usuario = (Usuario) e.getComponent().getAttributes().get("usuario");
        usuario.setSenha(Util.md5("123mudar"));
        usuarioFacade.save(usuario);
    }
    
    public void limpadados() {
        lista = new ArrayList<Usuario>();
        filtro = "";
    }

    public List<Usuario> getLista() {
        //return null;
        return usuarioFacade.findAll();
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }

    public UsuarioFacade getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Usuario getUsuarioselecionada() {
        return usuarioselecionada;
    }

    public void setUsuarioselecionada(Usuario usuarioselecionada) {
        this.usuarioselecionada = usuarioselecionada;
    }

    public Boolean validaCampos() {
        Boolean retorno = true;
        if (usuario.getNome().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo nome Ã© obrigatÃ³rio!", "O campo nome Ã© obrigatÃ³rio!"));
            retorno = false;
        }
        if (usuario.getUsuario().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo usuÃ¡rio Ã© obrigatÃ³rio!", "O campo nome Ã© obrigatÃ³rio!"));
            retorno = false;
        }
        return retorno;
    }

    public UsuarioControle() {
        usuario      = new Usuario();
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof Authentication) {
                usuario.setUsuario(((User) authentication.getPrincipal()).getUsername());

            }
        }
    }
    
    public Usuario usuarioLogin(String login){
        return usuarioFacade.usuarioLogin(login);
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }
    
}

