/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author julio
 */
@Entity
@Table(name = "configuration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c"),
    @NamedQuery(name = "Configuration.findByConfigurationId", query = "SELECT c FROM Configuration c WHERE c.configurationId = :configurationId"),
    @NamedQuery(name = "Configuration.findByItem", query = "SELECT c FROM Configuration c WHERE c.item = :item"),
    @NamedQuery(name = "Configuration.findByValue", query = "SELECT c FROM Configuration c WHERE c.value = :value")})
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "configuration_id")
    private Integer configurationId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "item")
    private String item;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "value")
    private String value;

    public Configuration() {
    }

    public Configuration(Integer configurationId) {
        this.configurationId = configurationId;
    }

    public Configuration(Integer configurationId, String item, String value) {
        this.configurationId = configurationId;
        this.item = item;
        this.value = value;
    }

    public Integer getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Integer configurationId) {
        this.configurationId = configurationId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configurationId != null ? configurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration) object;
        if ((this.configurationId == null && other.configurationId != null) || (this.configurationId != null && !this.configurationId.equals(other.configurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.com.csj.agua.test.Configuration[ configurationId=" + configurationId + " ]";
    }
    
}
