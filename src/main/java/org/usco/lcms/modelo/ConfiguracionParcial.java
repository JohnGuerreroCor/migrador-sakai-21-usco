package org.usco.lcms.modelo;

public class ConfiguracionParcial {

    private int codigoCurso;
    private int categoria;
    private float porcentaje;

    /**
     * @return the codigoCurso
     */
    public int getCodigoCurso() {
        return codigoCurso;
    }

    /**
     * @param codigoCurso
     *            the codigoCurso to set
     */
    public void setCodigoCurso(int codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    /**
     * @return the categoria
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * @param categoria
     *            the categoria to set
     */
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the porcentaje
     */
    public float getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje
     *            the porcentaje to set
     */
    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

}
