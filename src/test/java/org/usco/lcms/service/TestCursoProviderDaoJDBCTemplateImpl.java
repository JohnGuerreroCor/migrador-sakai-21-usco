package org.usco.lcms.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.usco.lcms.JUnitSpringTestBase;
import org.usco.lcms.dao.provider.CursoProviderDao;
import org.usco.lcms.modelo.Curso;

public class TestCursoProviderDaoJDBCTemplateImpl extends JUnitSpringTestBase {
    
    @Autowired
    CursoProviderDao cursoProviderDao;

    @Test
    public void testListarCursos() {
        List<Curso> cursos = cursoProviderDao.listarCursos("20171", false, 0);
        assertTrue("Cantidad de cursos mayor a 0", cursos.size()>0);
    }

    @Test
    public void testListarCursosTeachingAssitant() {
        fail("Not yet implemented");
    }

    @Test
    public void testListarConfiguraionParcialCursos() {
        fail("Not yet implemented");
    }

}
