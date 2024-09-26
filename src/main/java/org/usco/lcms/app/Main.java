package org.usco.lcms.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.usco.lcms.configuracion.AppConfiguration;
import org.usco.lcms.dao.provider.CursoProviderDao;
import org.usco.lcms.dao.provider.UsuarioProviderDao;
import org.usco.lcms.modelo.Curso;
import org.usco.lcms.modelo.TipoUsuario;
import org.usco.lcms.service.CursoService;
import org.usco.lcms.service.MatriculaService;
import org.usco.lcms.service.UsuarioService;

public class Main {

    public static void main(String[] args) {            

            ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
            /*CursoService cursoService = ctx.getBean(CursoService.class);
            UsuarioService usuarioService = ctx.getBean(UsuarioService.class);
            MatriculaService matriculaService = ctx.getBean(MatriculaService.class);
            CursoProviderDao cursoProviderDao = ctx.getBean(CursoProviderDao.class);
            UsuarioProviderDao usuarioProviderDao = ctx.getBean(UsuarioProviderDao.class);
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            System.out.println(usuarioProviderDao.listarUsuarios(TipoUsuario.ESTUDIANTE, "20171", false, 0).size());
            System.out.println(sdf.format(new Date()));
            //int[] result = cursoService.agregarTeachingAssitant("20171");
            //int[] result = cursoService.migrarCursos("20171", false, 0);
            //int[] result = usuarioService.migrarUsuario(TipoUsuario.ESTUDIANTE, "20171", false, 0);
            //int[] result = usuarioService.migrarUsuario(TipoUsuario.DOCENTE, "20171", false, 0);
            //int result = matriculaService.migrarCursosMatriculados("20171", false, 0);
            //System.out.println("Agregados["+result+"]");
            //System.out.println("Agregados["+result[0]+"] modificados["+result[1]+"]");
            //System.out.println("Agregados["+result[0]+"] cursos no encontrados["+result[1]+"]");
            //System.out.println(cantidad);
            System.out.println(sdf.format(new Date()));*/
        
        //System.out.println("ginnadsa".matches("^nn.*"));
    }
}
