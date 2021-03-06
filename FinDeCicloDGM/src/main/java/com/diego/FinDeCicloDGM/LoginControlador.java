package com.diego.FinDeCicloDGM;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import com.diego.FinDeCiclo.hilos.HiloIniciarSesion;
import com.diego.FinDeCiclo.hilos.HiloRegistro;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoginControlador extends ControladorConNavegabilidad implements Initializable {
    
	@FXML
    private Pane root;

    @FXML
    private TextField usuarioRegistro;

    @FXML
    private PasswordField contrasenaRegistro;

    @FXML
    private TextField email;

    @FXML
    private TextField nombre;

    @FXML
    private TextField apellidos;

    @FXML
    private DatePicker fechaNacimiento;

    @FXML
    private PasswordField repetirContrasena;

    @FXML
    private TextField usuarioLogin;

    @FXML
    private PasswordField contrasenaLogin;

    @FXML
    private VBox vbox;
    
    @FXML
    private Button crearCuenta;
    
    @FXML
    private Button iniciarSesion;
    
    @FXML
    private ProgressIndicator procesandoRegistro;
    
    @FXML
    private ProgressIndicator procesandoLogin;
    
    @FXML
    private Label mensajeError;
    
    @FXML
    private Label mensajeNombreUsuario;

    @FXML
    private Label mensajeErrorInsertar;

    @FXML
    private Label mensajeEmail;

    @FXML
    private Label mensajeContrasena;
    
    @FXML
    private Label mensajeObligatorio;
    
    private Parent fxml;
    
    private boolean usuarioRegistroEs = false, contrasenaRegistroEs = false, repetirContrasenaEs = false, emailEs = false, nombreEs = false, apellidosEs = false, 
    		usuarioLoginEs = false, contrasenaLoginEs = false;

    // M??todo que ejecuta la animaci??n y muestra el FXML que acompa??a a la vista del registro
    @FXML
    void mostrarCrearCuenta() {
    	
        TranslateTransition translate = new TranslateTransition(Duration.seconds(2), vbox);
        translate.setToX(vbox.getLayoutX() + (root.getPrefWidth() - vbox.getPrefWidth()));
        translate.play();
        
        translate.setOnFinished((e)->{
        	try {
        		fxml = FXMLLoader.load(getClass().getResource("CrearCuenta.fxml"));
        		vbox.getChildren().removeAll();
        		vbox.getChildren().setAll(fxml);
        		// Llamamos al m??todo que limpia los campos de iniciar sesi??n despu??s de que acabe la animaci??n
        		limpiarCamposLogin();
        	} catch (IOException ex) {
        		ex.printStackTrace();
        	}
        }); 
    	
    }

    // M??todo que ejecuta la animaci??n y muestra el FXML que acompa??a a la vista del login 
    @FXML
    public void mostrarIniciarSesion() {

        TranslateTransition translate = new TranslateTransition(Duration.seconds(2), vbox);
        translate.setToX(root.getLayoutX());
        translate.play();
        
        translate.setOnFinished((e)->{
        	try {
        		fxml = FXMLLoader.load(getClass().getResource("IniciarSesion.fxml"));
        		vbox.getChildren().removeAll();
        		vbox.getChildren().setAll(fxml);
        		// Llamamos al m??todo de limpiar campos cuando la animaci??n termine, para que el usuario no vea desaparecer la informaci??n de repente
        		limpiarCamposRegistro();
        		procesandoRegistro.setVisible(false);
        		
        		// Volvemos a mostrar el mensaje por defecto y ocultamos el resto
        		mensajeObligatorio.setVisible(true);
        		mensajeEmail.setVisible(false);
        		mensajeNombreUsuario.setVisible(false);
        		mensajeErrorInsertar.setVisible(false);
        		mensajeContrasena.setVisible(false);
        		
        		// Volvemos a deshabilitar el boton de crear cuenta
        		camposEsVacios();
        		
        	} catch (IOException ex) {
        		ex.printStackTrace();
        	}
        });
    	
    }
    
    // M??todo que pone las variables que hacen referencia a los campos escritos, es decir, las terminadas en Es, a false
    private void camposEsVacios() {
    	
    	usuarioRegistroEs = false;
    	contrasenaRegistroEs = false;
    	repetirContrasenaEs = false;
    	emailEs = false;
    	nombreEs = false;
    	apellidosEs = false;
    	usuarioLoginEs = false;
        contrasenaLoginEs = false;
    	
    }

    // M??todo que comprueba si el usuario introducido existe y le permite entrar en la aplicaci??n
    public void iniciarSesion() {

    	procesandoLogin.setVisible(true);
    	
    	HiloIniciarSesion hiloSesion = new HiloIniciarSesion(usuarioLogin, contrasenaLogin, procesandoLogin, this, mensajeError);
    	hiloSesion.start();
    	
    	iniciarSesion.setDisable(true);
    	usuarioLoginEs = false;
    	contrasenaLoginEs = false;
  
    }
    
    // M??todo que cambia la pantalla actual por la de selectorColeccion
    public void mostrarSelectorColeccion() {
    	this.layout.mostrarComoPantallaActual("selectorColeccion");
    }
    
    // M??todo para crear un usuario nuevo e insertarlo en la base de datos usando hilos
    public void registro() {
    	
    	procesandoRegistro.setVisible(true);
    	
    	HiloRegistro hiloRegistro = new HiloRegistro(usuarioRegistro, contrasenaRegistro, repetirContrasena, nombre, apellidos, fechaNacimiento, email, this, procesandoRegistro,
    			mensajeContrasena, mensajeEmail, mensajeErrorInsertar, mensajeNombreUsuario, mensajeObligatorio);
    	hiloRegistro.start();
    	
    }

    
    private void limpiarCamposRegistro() {
    	// M??todo que limpia los campos del formulario de registro
    	
    	nombre.clear();
    	apellidos.clear();
    	fechaNacimiento.setValue(null);
    	usuarioRegistro.clear();
    	contrasenaRegistro.clear();
    	repetirContrasena.clear();
    	email.clear();
    	
    	crearCuenta.setDisable(true);
    	
    }
    
    private void limpiarCamposLogin() {
    	// M??todo que limpia los campos del iniciar sesion
    	
    	usuarioLogin.clear();
    	contrasenaLogin.clear();
    }
    
    public void escribirNombre() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de nombre y comprueba si est?? vacio o no
    	nombreEs = !nombre.getText().isEmpty();
    	activarCrearCuenta();
    }
    
    public void escribirApellidos() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de apellidos y comprueba si est?? vacio o no
    	apellidosEs = !apellidos.getText().isEmpty();
    	activarCrearCuenta();
    }
    
    public void escribirUsuarioRegistro() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de usuario y comprueba si est?? vacio o no
    	usuarioRegistroEs = !usuarioRegistro.getText().isEmpty();
    	activarCrearCuenta();
    }
    
    public void escribirContrasenaRegistro() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de contrase??a y comprueba si est?? vacio o no
    	contrasenaRegistroEs = !contrasenaRegistro.getText().isEmpty();
    	activarCrearCuenta();
    }
    
    public void escribirRepetirContrasena() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de repetir contrase??a y comprueba si est?? vacio o no
    	repetirContrasenaEs = !repetirContrasena.getText().isEmpty();
    	activarCrearCuenta();
    }
    
    public void escribirEmail() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de email y comprueba si est?? vacio o no
    	emailEs = !email.getText().isEmpty();
    	activarCrearCuenta();
    }
    
    private void activarCrearCuenta() {
    	// M??todo que comprueba si todos los campos del registro est??n cubiertos y activa o desactiva el bot??n en consecuencia
    	if((nombreEs == true) && (apellidosEs == true) && (usuarioRegistroEs == true) && (contrasenaRegistroEs == true) && (repetirContrasenaEs == true) && (emailEs == true)) {
    		crearCuenta.setDisable(false);
    	} else {
    		crearCuenta.setDisable(true);
    	}
    	
    }
    
    public void escribirUsuarioLogin() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de usuario del login y comprueba si est?? vacio o no
    	usuarioLoginEs = !usuarioLogin.getText().isEmpty();
    	activarIniciarSesion();
    }
    
    public void escribirContrasenaLogin() {
    	// M??todo que escucha cada vez que se pulsa una tecla en el campo de contrase??a del login y comprueba si est?? vacio o no
    	contrasenaLoginEs = !contrasenaLogin.getText().isEmpty();
    	activarIniciarSesion();
    }
    
    private void activarIniciarSesion() {
    	// M??todo que comprueba si todos los campos del login est??n cubiertos y activa o desactiva el bot??n en consecuencia
    	if((usuarioLoginEs == true) && (contrasenaLoginEs == true)) {
    		iniciarSesion.setDisable(false);
    	} else {
    		iniciarSesion.setDisable(true);
    	}
    	
    }
    
   @Override
   public void initialize(URL location, ResourceBundle resources) {
    	
	   mensajeError.setVisible(false);
       
       procesandoLogin.setVisible(false);
       procesandoRegistro.setVisible(false);
       
       crearCuenta.setDisable(true);
       iniciarSesion.setDisable(true);
 
       try {
		fxml = FXMLLoader.load(getClass().getResource("IniciarSesion.fxml"));
		vbox.getChildren().removeAll();
		vbox.getChildren().setAll(fxml);
       } catch (IOException e) {
		e.printStackTrace();
       }
       
       LocalDate fechaBloqueo = LocalDate.now().minusYears(18);
       
       
       // Muestra en el calendario la fecha actual menos 16 a??os
       fechaNacimiento.showingProperty().addListener((observableValue, wasFocused, isNowFocus) -> {
    	    if (isNowFocus && fechaNacimiento.getValue() == null) {
    	    	fechaNacimiento.setValue(fechaBloqueo);
    	        Platform.runLater(()->{
    	        	fechaNacimiento.getEditor().clear();
    	        });
    	    }
    	});
       
    // Bloquea las fechas mayores que la fecha actual menos 16 a??os para evitar que los menores de dicha 
    // edad puedan darse de alta en la aplicaci??n
    fechaNacimiento.setDayCellFactory(param -> new DateCell() {
    	@Override
    	public void updateItem(LocalDate date, boolean empty) {
    		super.updateItem(date, empty);
    		setDisable(empty || date.compareTo(fechaBloqueo) > 0 );
    	}
    });
       
   }
    

}
