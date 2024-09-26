package org.usco.lcms.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.usco.lcms.dao.consumer.CursoConsumerDao;
import org.usco.lcms.dao.consumer.MatriculaConsumerDao;
import org.usco.lcms.dao.provider.MatriculaProviderDao;
import org.usco.lcms.modelo.Curso;
import org.usco.lcms.modelo.Matricula;

@Service
public class MatriculaServiceImpl implements MatriculaService {

	@Autowired
	MatriculaProviderDao matriculaProviderDao;

	@Autowired
	MatriculaConsumerDao matriculaDao;

	@Autowired
	CursoConsumerDao cursoDao;

	@Value("${SAKAI_STUDENT}")
	private int studentRole;

	@Value("${LOG_RESULT_FILE}")
	private String ruta;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

	public int migrarCursosMatriculados(String periodo, boolean sincronizacion, int minutos) {

		System.out.println("[+][+][+][+][+][+][+] Listando cursos matriculados por estudiantes...");
		List<Matricula> matriculas = matriculaProviderDao.listarMatriculados(periodo, sincronizacion, minutos);
		System.out.println("[+][+][+][+][+][+][+] Cantidad de cursos matriculados por estudiantes: " + matriculas.size());
		int insertados = 0;

		File file = new File(ruta + sdf.format(new Date()) + "-migracion.log");
		try {
			FileWriter fileWriter = new FileWriter(file);

			for (Matricula matricula : matriculas) {
				try {
					Curso curso = cursoDao.buscarCurso(matricula.getCurso() + "");
					switch (matricula.getTipo()) {
					case 1:
						System.out.println("caso 1");
						System.out.println("Primera condicion::: matricula.getEstudiante" + (matricula.getEstudiante() == null) + " || curso" + (curso == null));
						System.out.println("Segunda condicion::: cursoDao.buscarParticipanteEnCurso(matricula.getEstudiante() ||" + cursoDao.buscarParticipanteEnCurso(matricula.getEstudiante(), curso.getRealmKey(), studentRole).equals(""));
						if (cursoDao.buscarParticipanteEnCurso(matricula.getEstudiante(), curso.getRealmKey(), studentRole).equals("")) {
							fileWriter.write("ADICION: No existia Agregando estudiante[" + matricula.getEstudiante()
									+ "] curso[" + matricula.getCurso() + "]\n");
							matriculaDao.matricularEstudiante(matricula.getCurso(), matricula.getEstudiante());
						} else {
							cursoDao.activarDesactivarUsuarioACurso(matricula.getEstudiante(), curso.getRealmKey(),
									true);
							fileWriter.write("ADICION ya existia:  estudiante[" + matricula.getEstudiante() + "] curso["
									+ matricula.getCurso() + "]\n");
						}
						break;
					case 2:
						System.out.println("caso 2");
						System.out.println("PARAMETROS::: " + matricula.getEstudiante() + " || " + curso.getRealmKey() + " || " + studentRole);
						System.out.println("CONDICION||| " + cursoDao.buscarParticipanteEnCurso(matricula.getEstudiante(), curso.getRealmKey(), studentRole).equals(""));
						System.out.println((matricula.getEstudiante() == null) + " " + (curso == null));
						if (cursoDao.buscarParticipanteEnCurso(matricula.getEstudiante(), curso.getRealmKey(), studentRole).equals("")) {
							fileWriter.write("Agregando estudiante[" + matricula.getEstudiante() + "] curso["
									+ matricula.getCurso() + "]\n");
							matriculaDao.matricularEstudiante(matricula.getCurso(), matricula.getEstudiante());
						} else {
							fileWriter.write("Ya agregado estudiante[" + matricula.getEstudiante() + "] curso["
									+ matricula.getCurso() + "]\n");
						}
						break;
					case 4:
						System.out.println("caso 4");
						cursoDao.activarDesactivarUsuarioACurso(matricula.getEstudiante(), curso.getRealmKey(), false);
						break;
					case 5:
						System.out.println("caso 5");
						cursoDao.activarDesactivarUsuarioACurso(matricula.getEstudiante(), curso.getRealmKey(), false);
						break;
					}
					insertados = insertados + 1;
				} catch (Exception e) {
					e.printStackTrace();
					fileWriter.write("Error: " + e.toString() + "\n");
				}
			}
			fileWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return insertados;
	}
}