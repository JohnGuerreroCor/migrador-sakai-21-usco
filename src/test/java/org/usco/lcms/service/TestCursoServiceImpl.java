package org.usco.lcms.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.usco.lcms.JUnitSpringTestBase;

public class TestCursoServiceImpl extends JUnitSpringTestBase {

	@Autowired
	CursoService cursoService;

	@Test
	public void testMigrarCursos() {
		int[] result = cursoService.migrarCursos("20171", true, -20);
		for (int i : result) {
			System.out.println(i);
		}
	}

}
