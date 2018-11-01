package com.desarrollo.poderjudicial.santafe.model.entidades;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "Daper.findAll", query = "select o from Daper o order by o.dpapynom asc") })
public class Daper implements Serializable {
    private static final long serialVersionUID = -5760740279573275506L;
    @Column(length = 50)
    private String dpapynom;
    private Long dpartesa;
    private Long dpcarbaj;
    private Long dpcaring;
    private Long dpcarmed;
    private Long dpcodtit;
    private Long dpcosexo;
    @Column(length = 15)
    private String dpcuil;
    @Column(length = 50)
    private String dpdomici;
    @Column(length = 1)
    private String dpdpatri;
    private Long dpecivil;
    @Temporal(TemporalType.DATE)
    private Date dpfebaca;
    @Temporal(TemporalType.DATE)
    private Date dpfecing;
    @Temporal(TemporalType.DATE)
    private Date dpfecnac;
    @Temporal(TemporalType.DATE)
    private Date dpfectit;
    private Long dpforjur;
    @Column(length = 3)
    private String dpgrusan;
    @Column(length = 20)
    private String dplocali;
    @Column(length = 30)
    private String dplugnac;
    @Column(length = 30)
    private String dpmotiba;
    @Column(length = 30)
    private String dpnconyu;
    @Column(length = 30)
    private String dpnmadre;
    @Column(length = 30)
    private String dpnpadre;
    private Long dpnrodoc;
    private Long dpnroleg;
    @Column(length = 30)
    private String dpprofes;
    private Long dpresult;
    @Column(length = 30)
    private String dpsermil;
    private Long dpsituac;
    @Column(length = 15)
    private String dptelefo;
    private Long dptipdoc;
    @Column(length = 6)
    private String filler;
    @Id
    @Column(name = "ID_DAPER", nullable = false)
    private Long idDaper;

    public Daper() {
    }

    public Daper(String dpapynom, Long dpartesa, Long dpcarbaj, Long dpcaring, Long dpcarmed, Long dpcodtit,
                 Long dpcosexo, String dpcuil, String dpdomici, String dpdpatri, Long dpecivil, Date dpfebaca,
                 Date dpfecing, Date dpfecnac, Date dpfectit, Long dpforjur, String dpgrusan, String dplocali,
                 String dplugnac, String dpmotiba, String dpnconyu, String dpnmadre, String dpnpadre, Long dpnrodoc,
                 Long dpnroleg, String dpprofes, Long dpresult, String dpsermil, Long dpsituac, String dptelefo,
                 Long dptipdoc, String filler, Long idDaper) {
        this.dpapynom = dpapynom;
        this.dpartesa = dpartesa;
        this.dpcarbaj = dpcarbaj;
        this.dpcaring = dpcaring;
        this.dpcarmed = dpcarmed;
        this.dpcodtit = dpcodtit;
        this.dpcosexo = dpcosexo;
        this.dpcuil = dpcuil;
        this.dpdomici = dpdomici;
        this.dpdpatri = dpdpatri;
        this.dpecivil = dpecivil;
        this.dpfebaca = dpfebaca;
        this.dpfecing = dpfecing;
        this.dpfecnac = dpfecnac;
        this.dpfectit = dpfectit;
        this.dpforjur = dpforjur;
        this.dpgrusan = dpgrusan;
        this.dplocali = dplocali;
        this.dplugnac = dplugnac;
        this.dpmotiba = dpmotiba;
        this.dpnconyu = dpnconyu;
        this.dpnmadre = dpnmadre;
        this.dpnpadre = dpnpadre;
        this.dpnrodoc = dpnrodoc;
        this.dpnroleg = dpnroleg;
        this.dpprofes = dpprofes;
        this.dpresult = dpresult;
        this.dpsermil = dpsermil;
        this.dpsituac = dpsituac;
        this.dptelefo = dptelefo;
        this.dptipdoc = dptipdoc;
        this.filler = filler;
        this.idDaper = idDaper;
    }


    public String getDpapynom() {
        return dpapynom;
    }

    public void setDpapynom(String dpapynom) {
        this.dpapynom = dpapynom;
    }

    public Long getDpartesa() {
        return dpartesa;
    }

    public void setDpartesa(Long dpartesa) {
        this.dpartesa = dpartesa;
    }

    public Long getDpcarbaj() {
        return dpcarbaj;
    }

    public void setDpcarbaj(Long dpcarbaj) {
        this.dpcarbaj = dpcarbaj;
    }

    public Long getDpcaring() {
        return dpcaring;
    }

    public void setDpcaring(Long dpcaring) {
        this.dpcaring = dpcaring;
    }

    public Long getDpcarmed() {
        return dpcarmed;
    }

    public void setDpcarmed(Long dpcarmed) {
        this.dpcarmed = dpcarmed;
    }

    public Long getDpcodtit() {
        return dpcodtit;
    }

    public void setDpcodtit(Long dpcodtit) {
        this.dpcodtit = dpcodtit;
    }

    public Long getDpcosexo() {
        return dpcosexo;
    }

    public void setDpcosexo(Long dpcosexo) {
        this.dpcosexo = dpcosexo;
    }

    public String getDpcuil() {
        return dpcuil;
    }

    public void setDpcuil(String dpcuil) {
        this.dpcuil = dpcuil;
    }

    public String getDpdomici() {
        return dpdomici;
    }

    public void setDpdomici(String dpdomici) {
        this.dpdomici = dpdomici;
    }

    public String getDpdpatri() {
        return dpdpatri;
    }

    public void setDpdpatri(String dpdpatri) {
        this.dpdpatri = dpdpatri;
    }

    public Long getDpecivil() {
        return dpecivil;
    }

    public void setDpecivil(Long dpecivil) {
        this.dpecivil = dpecivil;
    }

    public Date getDpfebaca() {
        return dpfebaca;
    }

    public void setDpfebaca(Date dpfebaca) {
        this.dpfebaca = dpfebaca;
    }

    public Date getDpfecing() {
        return dpfecing;
    }

    public void setDpfecing(Date dpfecing) {
        this.dpfecing = dpfecing;
    }

    public Date getDpfecnac() {
        return dpfecnac;
    }

    public void setDpfecnac(Date dpfecnac) {
        this.dpfecnac = dpfecnac;
    }

    public Date getDpfectit() {
        return dpfectit;
    }

    public void setDpfectit(Date dpfectit) {
        this.dpfectit = dpfectit;
    }

    public Long getDpforjur() {
        return dpforjur;
    }

    public void setDpforjur(Long dpforjur) {
        this.dpforjur = dpforjur;
    }

    public String getDpgrusan() {
        return dpgrusan;
    }

    public void setDpgrusan(String dpgrusan) {
        this.dpgrusan = dpgrusan;
    }

    public String getDplocali() {
        return dplocali;
    }

    public void setDplocali(String dplocali) {
        this.dplocali = dplocali;
    }

    public String getDplugnac() {
        return dplugnac;
    }

    public void setDplugnac(String dplugnac) {
        this.dplugnac = dplugnac;
    }

    public String getDpmotiba() {
        return dpmotiba;
    }

    public void setDpmotiba(String dpmotiba) {
        this.dpmotiba = dpmotiba;
    }

    public String getDpnconyu() {
        return dpnconyu;
    }

    public void setDpnconyu(String dpnconyu) {
        this.dpnconyu = dpnconyu;
    }

    public String getDpnmadre() {
        return dpnmadre;
    }

    public void setDpnmadre(String dpnmadre) {
        this.dpnmadre = dpnmadre;
    }

    public String getDpnpadre() {
        return dpnpadre;
    }

    public void setDpnpadre(String dpnpadre) {
        this.dpnpadre = dpnpadre;
    }

    public Long getDpnrodoc() {
        return dpnrodoc;
    }

    public void setDpnrodoc(Long dpnrodoc) {
        this.dpnrodoc = dpnrodoc;
    }

    public Long getDpnroleg() {
        return dpnroleg;
    }

    public void setDpnroleg(Long dpnroleg) {
        this.dpnroleg = dpnroleg;
    }

    public String getDpprofes() {
        return dpprofes;
    }

    public void setDpprofes(String dpprofes) {
        this.dpprofes = dpprofes;
    }

    public Long getDpresult() {
        return dpresult;
    }

    public void setDpresult(Long dpresult) {
        this.dpresult = dpresult;
    }

    public String getDpsermil() {
        return dpsermil;
    }

    public void setDpsermil(String dpsermil) {
        this.dpsermil = dpsermil;
    }

    public Long getDpsituac() {
        return dpsituac;
    }

    public void setDpsituac(Long dpsituac) {
        this.dpsituac = dpsituac;
    }

    public String getDptelefo() {
        return dptelefo;
    }

    public void setDptelefo(String dptelefo) {
        this.dptelefo = dptelefo;
    }

    public Long getDptipdoc() {
        return dptipdoc;
    }

    public void setDptipdoc(Long dptipdoc) {
        this.dptipdoc = dptipdoc;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public Long getIdDaper() {
        return idDaper;
    }

    public void setIdDaper(Long idDaper) {
        this.idDaper = idDaper;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName() + "@" + Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("dpapynom=");
        buffer.append(getDpapynom());
        buffer.append(',');
        buffer.append("dpartesa=");
        buffer.append(getDpartesa());
        buffer.append(',');
        buffer.append("dpcarbaj=");
        buffer.append(getDpcarbaj());
        buffer.append(',');
        buffer.append("dpcaring=");
        buffer.append(getDpcaring());
        buffer.append(',');
        buffer.append("dpcarmed=");
        buffer.append(getDpcarmed());
        buffer.append(',');
        buffer.append("dpcodtit=");
        buffer.append(getDpcodtit());
        buffer.append(',');
        buffer.append("dpcosexo=");
        buffer.append(getDpcosexo());
        buffer.append(',');
        buffer.append("dpcuil=");
        buffer.append(getDpcuil());
        buffer.append(',');
        buffer.append("dpdomici=");
        buffer.append(getDpdomici());
        buffer.append(',');
        buffer.append("dpdpatri=");
        buffer.append(getDpdpatri());
        buffer.append(',');
        buffer.append("dpecivil=");
        buffer.append(getDpecivil());
        buffer.append(',');
        buffer.append("dpfebaca=");
        buffer.append(getDpfebaca());
        buffer.append(',');
        buffer.append("dpfecing=");
        buffer.append(getDpfecing());
        buffer.append(',');
        buffer.append("dpfecnac=");
        buffer.append(getDpfecnac());
        buffer.append(',');
        buffer.append("dpfectit=");
        buffer.append(getDpfectit());
        buffer.append(',');
        buffer.append("dpforjur=");
        buffer.append(getDpforjur());
        buffer.append(',');
        buffer.append("dpgrusan=");
        buffer.append(getDpgrusan());
        buffer.append(',');
        buffer.append("dplocali=");
        buffer.append(getDplocali());
        buffer.append(',');
        buffer.append("dplugnac=");
        buffer.append(getDplugnac());
        buffer.append(',');
        buffer.append("dpmotiba=");
        buffer.append(getDpmotiba());
        buffer.append(',');
        buffer.append("dpnconyu=");
        buffer.append(getDpnconyu());
        buffer.append(',');
        buffer.append("dpnmadre=");
        buffer.append(getDpnmadre());
        buffer.append(',');
        buffer.append("dpnpadre=");
        buffer.append(getDpnpadre());
        buffer.append(',');
        buffer.append("dpnrodoc=");
        buffer.append(getDpnrodoc());
        buffer.append(',');
        buffer.append("dpnroleg=");
        buffer.append(getDpnroleg());
        buffer.append(',');
        buffer.append("dpprofes=");
        buffer.append(getDpprofes());
        buffer.append(',');
        buffer.append("dpresult=");
        buffer.append(getDpresult());
        buffer.append(',');
        buffer.append("dpsermil=");
        buffer.append(getDpsermil());
        buffer.append(',');
        buffer.append("dpsituac=");
        buffer.append(getDpsituac());
        buffer.append(',');
        buffer.append("dptelefo=");
        buffer.append(getDptelefo());
        buffer.append(',');
        buffer.append("dptipdoc=");
        buffer.append(getDptipdoc());
        buffer.append(',');
        buffer.append("filler=");
        buffer.append(getFiller());
        buffer.append(',');
        buffer.append("idDaper=");
        buffer.append(getIdDaper());
        buffer.append(']');
        return buffer.toString();
    }
}
