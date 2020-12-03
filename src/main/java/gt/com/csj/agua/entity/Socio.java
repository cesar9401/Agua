/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "socio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Socio.findAll", query = "SELECT s FROM Socio s"),
    @NamedQuery(name = "Socio.findBySocioId", query = "SELECT s FROM Socio s WHERE s.socioId = :socioId"),
    @NamedQuery(name = "Socio.findByCode", query = "SELECT s FROM Socio s WHERE s.code = :code"),
    @NamedQuery(name = "Socio.findByName", query = "SELECT s FROM Socio s WHERE s.name = :name"),
    @NamedQuery(name = "Socio.findByLastName", query = "SELECT s FROM Socio s WHERE s.lastName = :lastName"),
    @NamedQuery(name = "Socio.findByDpi", query = "SELECT s FROM Socio s WHERE s.dpi = :dpi"),
    @NamedQuery(name = "Socio.findByPhone", query = "SELECT s FROM Socio s WHERE s.phone = :phone"),
    @NamedQuery(name = "Socio.findByAddress", query = "SELECT s FROM Socio s WHERE s.address = :address"),
    @NamedQuery(name = "Socio.findByType", query = "SELECT s FROM Socio s WHERE s.type = :type"),
    @NamedQuery(name = "Socio.findByExonerated", query = "SELECT s FROM Socio s WHERE s.exonerated = :exonerated")})
public class Socio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "socio_id")
    private Integer socioId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 13)
    @Column(name = "dpi")
    private String dpi;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 8)
    @Column(name = "phone")
    private String phone;
    @Size(max = 100)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "type")
    private String type;
    @Column(name = "exonerated")
    private Short exonerated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socioId")
    private Collection<Administrator> administratorCollection;
    @OneToMany(mappedBy = "socioSocioId")
    private Collection<Socio> socioCollection;
    @JoinColumn(name = "socio_socio_id", referencedColumnName = "socio_id")
    @ManyToOne
    private Socio socioSocioId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socioId")
    private Collection<SocioEvent> socioEventCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "socioId")
    private Collection<SocioPayment> socioPaymentCollection;

    public Socio() {
    }

    public Socio(Integer socioId) {
        this.socioId = socioId;
    }

    public Socio(Integer socioId, String code, String name, String lastName, String type) {
        this.socioId = socioId;
        this.code = code;
        this.name = name;
        this.lastName = lastName;
        this.type = type;
    }

    public Integer getSocioId() {
        return socioId;
    }

    public void setSocioId(Integer socioId) {
        this.socioId = socioId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Short getExonerated() {
        return exonerated;
    }

    public void setExonerated(Short exonerated) {
        this.exonerated = exonerated;
    }

    @XmlTransient
    public Collection<Administrator> getAdministratorCollection() {
        return administratorCollection;
    }

    public void setAdministratorCollection(Collection<Administrator> administratorCollection) {
        this.administratorCollection = administratorCollection;
    }

    @XmlTransient
    public Collection<Socio> getSocioCollection() {
        return socioCollection;
    }

    public void setSocioCollection(Collection<Socio> socioCollection) {
        this.socioCollection = socioCollection;
    }

    public Socio getSocioSocioId() {
        return socioSocioId;
    }

    public void setSocioSocioId(Socio socioSocioId) {
        this.socioSocioId = socioSocioId;
    }

    @XmlTransient
    public Collection<SocioEvent> getSocioEventCollection() {
        return socioEventCollection;
    }

    public void setSocioEventCollection(Collection<SocioEvent> socioEventCollection) {
        this.socioEventCollection = socioEventCollection;
    }

    @XmlTransient
    public Collection<SocioPayment> getSocioPaymentCollection() {
        return socioPaymentCollection;
    }

    public void setSocioPaymentCollection(Collection<SocioPayment> socioPaymentCollection) {
        this.socioPaymentCollection = socioPaymentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (socioId != null ? socioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Socio)) {
            return false;
        }
        Socio other = (Socio) object;
        if ((this.socioId == null && other.socioId != null) || (this.socioId != null && !this.socioId.equals(other.socioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.com.csj.agua.entity.Socio[ socioId=" + socioId + " ]";
    }
    
}
