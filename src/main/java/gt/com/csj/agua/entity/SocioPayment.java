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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julio
 */
@Entity
@Table(name = "socio_payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SocioPayment.findAll", query = "SELECT s FROM SocioPayment s"),
    @NamedQuery(name = "SocioPayment.findBySocioPaymentId", query = "SELECT s FROM SocioPayment s WHERE s.socioPaymentId = :socioPaymentId"),
    @NamedQuery(name = "SocioPayment.findByPaymentMonth", query = "SELECT s FROM SocioPayment s WHERE s.paymentMonth = :paymentMonth"),
    @NamedQuery(name = "SocioPayment.findByAmount", query = "SELECT s FROM SocioPayment s WHERE s.amount = :amount"),
    @NamedQuery(name = "SocioPayment.findByPaymentDate", query = "SELECT s FROM SocioPayment s WHERE s.paymentDate = :paymentDate")})
public class SocioPayment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "socio_payment_id")
    private Integer socioPaymentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_month")
    @Temporal(TemporalType.DATE)
    private Date paymentMonth;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @JoinColumn(name = "administrator_id", referencedColumnName = "administrator_id")
    @ManyToOne(optional = false)
    private Administrator administratorId;
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    @ManyToOne(optional = false)
    private Payment paymentId;
    @JoinColumn(name = "socio_id", referencedColumnName = "socio_id")
    @ManyToOne(optional = false)
    private Socio socioId;

    public SocioPayment() {
    }

    public SocioPayment(Integer socioPaymentId) {
        this.socioPaymentId = socioPaymentId;
    }

    public SocioPayment(Integer socioPaymentId, Date paymentMonth, BigDecimal amount) {
        this.socioPaymentId = socioPaymentId;
        this.paymentMonth = paymentMonth;
        this.amount = amount;
    }

    public Integer getSocioPaymentId() {
        return socioPaymentId;
    }

    public void setSocioPaymentId(Integer socioPaymentId) {
        this.socioPaymentId = socioPaymentId;
    }

    public Date getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(Date paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Administrator getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(Administrator administratorId) {
        this.administratorId = administratorId;
    }

    public Payment getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Payment paymentId) {
        this.paymentId = paymentId;
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
        hash += (socioPaymentId != null ? socioPaymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SocioPayment)) {
            return false;
        }
        SocioPayment other = (SocioPayment) object;
        if ((this.socioPaymentId == null && other.socioPaymentId != null) || (this.socioPaymentId != null && !this.socioPaymentId.equals(other.socioPaymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.com.csj.agua.entity.SocioPayment[ socioPaymentId=" + socioPaymentId + " ]";
    }
    
}
