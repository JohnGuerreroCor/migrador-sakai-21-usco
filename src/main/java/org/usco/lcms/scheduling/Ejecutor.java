package org.usco.lcms.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.usco.lcms.dao.ProviderUtilDao;
import org.usco.lcms.modelo.TipoUsuario;
import org.usco.lcms.service.CursoService;
import org.usco.lcms.service.MatriculaService;
import org.usco.lcms.service.UsuarioService;

@Component
public class Ejecutor {

	@Value("${intervalo.corto}")
	private int intervaloCorto;

	@Value("${intervalo.largo}")
	private int intervaloLargo;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	CursoService cursoService;

	@Autowired
	MatriculaService matriculaConsumerDao;

	@Autowired
	ProviderUtilDao providerUtilDao;

	@Scheduled(fixedDelayString = "${intervalo.corto}")
	public void migracionIntervaloCorto() {
		System.out.println("***********CORTO***************");
		if (providerUtilDao.isEncendido())
			sincronizar(intervaloCorto * 8 / 1000 / 60, true);
	}

	@Scheduled(fixedDelayString = "${intervalo.largo}")
	public void migracionIntervaloLargo() {
		System.out.println("***********LARGO***************");
		if (providerUtilDao.isEncendido())
			sincronizar(intervaloLargo / 1000 / 60, true);
	}

	@Scheduled(fixedDelayString = "86400000")
	public void migracionCompleta() {
		System.out.println("***********MIGRADOR***************");
		if (providerUtilDao.isMigracion()) {
			sincronizar(0, false);
		}
	}

	private void sincronizar(int minutos, boolean sincronizacion) {
		String calendario = providerUtilDao.getCalendario();
		System.out.println("minutos" + minutos);
		System.out.println("El calendario es:" + calendario);
		// Sincronizamos usuarios

		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("DOCENTES");
		System.out.println("==========================================================");
		usuarioService.migrarUsuario(TipoUsuario.DOCENTE, calendario, sincronizacion, minutos);

		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("ESTUDIANTES");
		usuarioService.migrarUsuario(TipoUsuario.ESTUDIANTE, calendario, sincronizacion, minutos);

		// Sincronizamos cursos

		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("CURSO");
		cursoService.migrarCursos(calendario, sincronizacion, minutos);

		// Sincronizamos matrículas

		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("MATRICULAS");
		matriculaConsumerDao.migrarCursosMatriculados(calendario, sincronizacion, minutos);

		// Teaching assistants

		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("ASISTENTES");
		cursoService.agregarTeachingAssitant(calendario);

		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");

		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("============================FINNNNNNN=====================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");
		System.out.println("==========================================================");

	}
}