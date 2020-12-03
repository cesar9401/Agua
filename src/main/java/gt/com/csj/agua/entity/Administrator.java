/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author julio
 */
@Entity
@Table(name = "administrator")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a"),
    @NamedQuery(name = "Administrator.findByAdministratorId", query = "SELECT a FROM Administrator a WHERE a.administratorId = :administratorId"),
    @NamedQuery(name = "Administrator.findByPassword", query = "SELECT a FROM Administrator a WHERE a.password = :password")})
public class Administrator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "administrator_id")
    private Integer administratorId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "socio_id", referencedColumnName = "socio_id")
    @ManyToOne(optional = false)
    private Socio socioId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administratorId")
    private List<SocioEvent> socioEventList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administratorId")
    private List<SocioPayment> socioPaymentList;

    public Administrator() {
    }

    public Administrator(Integer administratorId) {
        this.administratorId = administratorId;
    }

    public Administrator(Integer administratorId, String password) {
        this.administratorId = administratorId;
        this.password = password;
    }

    public Integer getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(Integer administratorId) {
        this.administratorId = administratorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socio getSocioId() {
        return socioId;
    }

    public void setSocioId(Socio socioId) {
        this.socioId = socioId;
    }

    @XmlTransient
    public List<SocioEvent> getSocioEventList() {
        return socioEventList;
    }

    public void setSocioEventList(List<SocioEvent> socioEventList) {
        this.socioEventList = socioEventList;
    }

    @XmlTransient
    public List<SocioPayment> getSocioPaymentList() {
        return socioPaymentList;
    }

    public void setSocioPaymentList(List<SocioPayment> socioPaymentList) {
        this.socioPaymentList = socioPaymentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (administratorId != null ? administratorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Administrator)) {
            return false;
        }
        Administrator other = (Administrator) object;
        if ((this.administratorId == null && other.administratorId != null) || (this.administratorId != null && !this.administratorId.equals(other.administratorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.com.csj.agua.test.Administrator[ administratorId=" + administratorId + " ]";
    }
    
}
