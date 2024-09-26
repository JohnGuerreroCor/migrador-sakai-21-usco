package org.usco.lcms.dao.consumer;

import org.usco.lcms.modelo.ConfiguracionParcial;
import org.usco.lcms.modelo.Curso;

public interface CursoConsumerDao {

    /**
     * Agrega un nuevo curso a SAKAI
     * 
     * @param curso
     *            - Curso a agregar
     * @param crearInstructor
     *            - Indica si se crea o no el curso con instructor
     */
    public void agregarCurso(Curso curso, boolean crearInstructor);

    /**
     * Busca el curso por su código
     * 
     * @param curCodigo
     *            - Código del curso
     * @return Si no encuentra el curso devuelve null, en caso contrario el
     *         curso encontrado
     */
    public Curso buscarCurso(String curCodigo);

    /**
     * 
     * @param cursoNuevo
     * @param cursoAnterior
     */

    public void actualizarCurso(Curso cursoNuevo, Curso cursoAnterior);

    /**
     * 
     * @param realmKey
     * @return
     */
    public String buscarInstructor(int realmKey);
    
    /**
     * 
     * @param userId
     * @param realmKey
     * @param roleId
     * @return
     */    
    public String buscarParticipanteEnCurso(String userId, int realmKey, int roleId);
    
    /**
     * 
     * @param userId
     * @param realmKey
     * @param activar
     */
    public void activarDesactivarUsuarioACurso(String userId, int realmKey, boolean activar);
    
    /**
     * 
     * @param userId
     * @param realmKey
     * @param roleId
     */
    public void agregarUsuarioACurso(String userId, int realmKey, int roleId);
    
    /**
     * 
     */
    public void generarItemDeCalificacion();
    
    /**
     * 
     */
    public void actualizarItemDeCalificacion(ConfiguracionParcial configuracionParcial);
}
