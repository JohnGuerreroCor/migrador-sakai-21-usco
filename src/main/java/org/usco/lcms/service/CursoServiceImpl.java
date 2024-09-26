package org.usco.lcms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.usco.lcms.dao.consumer.CursoConsumerDao;
import org.usco.lcms.dao.consumer.UsuarioConsumerDao;
import org.usco.lcms.dao.provider.CursoProviderDao;
import org.usco.lcms.modelo.ConfiguracionParcial;
import org.usco.lcms.modelo.Curso;
import org.usco.lcms.modelo.Usuario;

@Service
public class CursoServiceImpl implements CursoService {

	@Autowired
	CursoProviderDao cursoProviderDao;

	@Autowired
	CursoConsumerDao cursoDao;

	@Autowired
	UsuarioConsumerDao usuarioDao;

	@Value("${SAKAI_STUDENT}")
	private int studentRole;

	@Value("${SAKAI_INSTRUCTOR}")
	private int instructorRole;

	@Value("${SAKAI_TEACHING_ASSISTANT}")
	private int teachingAssitantRole;

	public int[] migrarCursos(String calendario, boolean sincronizacion, int minutos) {

		List<Curso> cursos = cursoProviderDao.listarCursos(calendario, sincronizacion, minutos);
		int[] cantidad = { 0, 0 };
		try {
			for (Curso curso : cursos) {
				// Buscar el curso en sakai para verificar si ya existe o no
				Curso cursoAnterior = cursoDao.buscarCurso(curso.getCodigo() + "");
				if (cursoAnterior == null) {
					// SI el curso no existe en SAKAI primero valido que el
					// docente del curso no sea un NN CATEDRA y luego creo el
					// curso si el docente es un nn no se le agrega docente al
					// curso
					if (curso.getPersonaApellido().toLowerCase().matches("^nn.*")) {
						cursoDao.agregarCurso(curso, false);
					} else {
						cursoDao.agregarCurso(curso, true);
					}
					cantidad[0] += 1;
				} else {
					//CURSO: ES ACADEMIA
					//CUSROANTERIOR: ES SAKAI
					// Actualizo el nombre del curso pasando el objeto del curso
					// y el curso anterior
					cursoDao.actualizarCurso(curso, cursoAnterior);
					// si el docente del curso(ACADEMIA) no es igual al docente
					// del curso anterior(SAKAI) y el docente del
					// curso(ACADEMIA) no es un NN catedra
					if (!curso.getPersonaIdentificacion().equals(cursoAnterior.getPersonaIdentificacion())
							&& !curso.getPersonaIdentificacion().toLowerCase().matches("^nn.*")) {

						// Paso el docente del curso anterior(SAKAI) y o
						// inactivo en el curso (SAKAI)
						cursoDao.activarDesactivarUsuarioACurso(cursoAnterior.getPersonaIdentificacion(),
								cursoAnterior.getRealmKey(), false);

						// Busco el docente en sakai 
						if (usuarioDao.buscarUsuario(curso.getPersonaIdentificacion()) == null) {
							// si el docente no se encuentra registrado en sakai
							// lo agrego
							usuarioDao.agregarUsuario(new Usuario(curso.getPersonaIdentificacion(),
									curso.getPersonaIdentificacion(), curso.getPersonaApellido(),
									curso.getPersonaNombre(), curso.getPersonaEmail(), calendario));
						}
						// Agrego el docente de curso(ACADEMIA) al curso
						// anterior (SAKAI) como docente activo
						cursoDao.activarDesactivarUsuarioACurso(curso.getPersonaIdentificacion(),
								cursoAnterior.getRealmKey(), true);
					}
					cantidad[1] += 1;
				}
			}

			if (cursos.size() > 0 && cantidad[0] > 0) {
				cursoDao.generarItemDeCalificacion();
			}

			List<ConfiguracionParcial> configuracionesParciales = cursoProviderDao
					.listarConfiguraionParcialCursos(calendario, sincronizacion, minutos);

			for (ConfiguracionParcial configuracionParcial : configuracionesParciales) {
				cursoDao.actualizarItemDeCalificacion(configuracionParcial);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cantidad;
	}

	public int[] agregarTeachingAssitant(String calendario) {

		int[] cantidad = new int[2];

		List<Curso> cursos = cursoProviderDao.listarCursosTeachingAssitant(calendario, false, 0);
		try {
			for (Curso curso : cursos) {
				Curso cursoAnterior = cursoDao.buscarCurso(curso.getCodigo() + "");
				if (cursoAnterior != null) {
					cantidad[0] += 1;
					if (usuarioDao.buscarUsuario(curso.getPersonaIdentificacion()) == null) {
						System.out.println("No está creado como usuario!\nCreando usuario docente["
								+ curso.getPersonaIdentificacion() + "]...");
						usuarioDao.agregarUsuario(
								new Usuario(curso.getPersonaIdentificacion(), curso.getPersonaIdentificacion(),
										curso.getPersonaApellido(), curso.getPersonaNombre(), curso.getPersonaEmail(), calendario));
					} else {
						// System.out.println("Usuario existente!");
					}

					if (!cursoDao.buscarParticipanteEnCurso(curso.getPersonaIdentificacion(),
							cursoAnterior.getRealmKey(), instructorRole).equals(
									"")
							|| !cursoDao.buscarParticipanteEnCurso(curso.getPersonaIdentificacion(),
									cursoAnterior.getRealmKey(), studentRole).equals("")
							|| !cursoDao.buscarParticipanteEnCurso(curso.getPersonaIdentificacion(),
									cursoAnterior.getRealmKey(), teachingAssitantRole).equals("")) {
						// System.out.println("Está como participante");

					} else {
						System.out.println("No está como participante");
						System.out.println("Agregando usuario[" + curso.getPersonaIdentificacion() + "] al curso["
								+ curso.getCodigo() + "]");
						cursoDao.agregarUsuarioACurso(curso.getPersonaIdentificacion(), cursoAnterior.getRealmKey(),
								teachingAssitantRole);

					}

				} else {
					cantidad[1] += 1;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return cantidad;
	}
}