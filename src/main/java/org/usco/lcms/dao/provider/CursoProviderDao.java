package org.usco.lcms.dao.provider;

import java.util.List;

import org.usco.lcms.modelo.ConfiguracionParcial;
import org.usco.lcms.modelo.Curso;

public interface CursoProviderDao {

    public List<Curso> listarCursos(String calendario, boolean sincronizacion, int minutos);

    public List<Curso> listarCursosTeachingAssitant(String calendario, boolean sincronizacion, int minutos);

    public List<ConfiguracionParcial> listarConfiguraionParcialCursos(String calendario, boolean sincronizacion,
            int minutos);

}
