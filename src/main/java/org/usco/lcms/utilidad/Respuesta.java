package org.usco.lcms.utilidad;

public class Respuesta {
	
	public static final int EJECUCION_OK = 1;
	public static final int EJECUCION_ERROR = 0;
	
	public static final String OPERACION_EJECUTADA_EXITOSAMENTE = "Operación Ejecutada exitosamente.";
	public static final String ERROR_EJECUTAR_OPERACION = "Error al ejecutar la operación.";
	
	int codigo;
	boolean exito;
	String mensaje;
		
	public Respuesta() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Respuesta(int tipo) {
		super();
		
		if(tipo == EJECUCION_OK){
			this.codigo = EJECUCION_OK;
			this.exito = true;
			this.mensaje = OPERACION_EJECUTADA_EXITOSAMENTE;
		}
		
		if(tipo == EJECUCION_ERROR){
			this.codigo = EJECUCION_ERROR;
			this.exito = false;
			this.mensaje = ERROR_EJECUTAR_OPERACION;
		}
		
	}

	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public boolean isExito() {
		return exito;
	}
	public void setExito(boolean exito) {
		this.exito = exito;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}

