/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julio
 */
@Entity
@Table(name = "fine")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fine.findAll", query = "SELECT f FROM Fine f"),
    @NamedQuery(name = "Fine.findByFineId", query = "SELECT f FROM Fine f WHERE f.fineId = :fineId"),
    @NamedQuery(name = "Fine.findByAmount", query = "SELECT f FROM Fine f WHERE f.amount = :amount"),
    @NamedQuery(name = "Fine.findByFineDate", query = "SELECT f FROM Fine f WHERE f.fineDate = :fineDate"),
    @NamedQuery(name = "Fine.findByFineDatePayment", query = "SELECT f FROM Fine f WHERE f.fineDatePayment = :fineDatePayment"),
    @NamedQuery(name = "Fine.findByCode", query = "SELECT f FROM Fine f WHERE f.code = :code")})
public class Fine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "fine_id")
    private Integer fineId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fine_date")
    @Temporal(TemporalType.DATE)
    private Date fineDate;
    @Column(name = "fine_date_payment")
    @Temporal(TemporalType.DATE)
    private Date fineDatePayment;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code")
    private int code;
    @JoinColumn(name = "administrator_id", referencedColumnName = "administrator_id")
    @ManyToOne(optional = false)
    private Administrator administratorId;
    @JoinColumn(name = "socio_id", referencedColumnName = "socio_id")
    @ManyToOne(optional = false)
    private Socio socioId;

    public Fine() {
    }

    public Fine(Integer fineId) {
        this.fineId = fineId;
    }

    public Fine(Integer fineId, BigDecimal amount, Date fineDate, String description, int code) {
        this.fineId = fineId;
        this.amount = amount;
        this.fineDate = fineDate;
        this.description = description;
        this.code = code;
    }

    public Integer getFineId() {
        return fineId;
    }

    public void setFineId(Integer fineId) {
        this.fineId = fineId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getFineDate() {
        return fineDate;
    }

    public void setFineDate(Date fineDate) {
        this.fineDate = fineDate;
    }

    public Date getFineDatePayment() {
        return fineDatePayment;
    }

    public void setFineDatePayment(Date fineDatePayment) {
        this.fineDatePayment = fineDatePayment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Administrator getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(Administrator administratorId) {
        this.administratorId = administratorId;
    }

    public Socio getSocioId() {
        return socioId;
    }

    public void setSocioId(Socio socioId) {
        this.socioId = socioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fineId != null ? fineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fine)) {
            return false;
        }
        Fine other = (Fine) object;
        if ((this.fineId == null && other.fineId != null) || (this.fineId != null && !this.fineId.equals(other.fineId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.com.csj.agua.entity.Fine[ fineId=" + fineId + " ]";
    }
    
}
