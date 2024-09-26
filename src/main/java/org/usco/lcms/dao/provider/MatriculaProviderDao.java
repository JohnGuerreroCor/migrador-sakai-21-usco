package org.usco.lcms.dao.provider;

import java.util.List;

import org.usco.lcms.modelo.Matricula;

public interface MatriculaProviderDao {
    public List<Matricula> listarMatriculados(String calendario, boolean sincronizacion, int minutos);
}
