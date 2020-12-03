/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julio
 */
@Entity
@Table(name = "socio_event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SocioEvent.findAll", query = "SELECT s FROM SocioEvent s"),
    @NamedQuery(name = "SocioEvent.findBySocioEventId", query = "SELECT s FROM SocioEvent s WHERE s.socioEventId = :socioEventId"),
    @NamedQuery(name = "SocioEvent.findByPaymentDate", query = "SELECT s FROM SocioEvent s WHERE s.paymentDate = :paymentDate")})
public class SocioEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "socio_event_id")
    private Integer socioEventId;
    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @JoinColumn(name = "administrator_id", referencedColumnName = "administrator_id")
    @ManyToOne(optional = false)
    private Administrator administratorId;
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    @ManyToOne(optional = false)
    private Event eventId;
    @JoinColumn(name = "socio_id", referencedColumnName = "socio_id")
    @ManyToOne(optional = false)
    private Socio socioId;

    public SocioEvent() {
    }

    public SocioEvent(Integer socioEventId) {
        this.socioEventId = socioEventId;
    }

    public Integer getSocioEventId() {
        return socioEventId;
    }

    public void setSocioEventId(Integer socioEventId) {
        this.socioEventId = socioEventId;
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

    public Event getEventId() {
        return eventId;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
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
        hash += (socioEventId != null ? socioEventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SocioEvent)) {
            return false;
        }
        SocioEvent other = (SocioEvent) object;
        if ((this.socioEventId == null && other.socioEventId != null) || (this.socioEventId != null && !this.socioEventId.equals(other.socioEventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.com.csj.agua.entity.SocioEvent[ socioEventId=" + socioEventId + " ]";
    }
    
}
