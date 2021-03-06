package com.diego.FinDeCicloDGM;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import com.diego.FinDeCiclo.pojos.Informacion;
import com.diego.FinDeCiclo.pojos.Musica;
import com.diego.FinDeCicloDGM.dao.MusicaDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class NuevoAlbumControlador extends ControladorConNavegabilidad implements Initializable {

	@FXML
    private ImageView imagenCaratula;

    @FXML
    private TextField ean;

    @FXML
    private TextField titulo;

    @FXML
    private TextField artista;

    @FXML
    private TextField precio;

    @FXML
    private TextField duracion;

    @FXML
    private ComboBox<String> genero;

    @FXML
    private TextField caratula;

    @FXML
    private Button btnComprobarCaratula;

    @FXML
    private Button guardarAlbum;

    @FXML
    private ComboBox<String> formato;

    @FXML
    private DatePicker fechaPublicacion;

    @FXML
    private TextField discografica;
    
    private boolean caratulaValida = false;
    
    private Path rutaGeneros = Path.of("ficherosInfo\\generosAlbumes.csv");
    private Path rutaFormatos = Path.of("ficherosInfo\\formatosAlbumes.csv");
	
	public void guardarAlbum() {
		
		Musica albumBuscar = MusicaDao.buscarAlbumEAN(ean.getText());
		
		if(albumBuscar.getEan() == null) {
			
			Musica album;
			
			String directorioCaratula = descagarCaratula();
			
			album = new Musica(Long.parseLong(ean.getText()), titulo.getText(), artista.getText(), genero.getValue(), formato.getValue(), Integer.parseInt(duracion.getText()), 
					directorioCaratula, Date.valueOf(fechaPublicacion.getValue()), Double.parseDouble(precio.getText()), discografica.getText());
			
			if(MusicaDao.insertarAlbum(album)) {
				MusicaDao.anhadirAlbumUsuario(album, Informacion.usuario);
				
				if(Informacion.usuario.getRango() == 1) {
					
					Alert popup = Popup.lanzarPopup("??lbum a??adido a la colecci??n", "El ??lbum ??" + album.getTitulo() + "?? se ha a??adido a tu colecci??n "
	        				+ "correctamente", 1);
					cargarEstilosPopup(popup);
	        		popup.showAndWait();
					
				} else {
					
					Alert popup = Popup.lanzarPopup("??lbum a??adido a la base de datos", "El ??lbum ??" + album.getTitulo() + "?? se ha a??adido base de datos "
	        				+ "correctamente", 1);
					cargarEstilosPopup(popup);
	        		popup.showAndWait();
					
				}

				Informacion.dialogoAnhadirAlbum.close();
				
			} else {
				
				Alert popup = Popup.lanzarPopup("Error", "Error al guardar el ??lbum ??" + album.getTitulo() + "??", 2);
				cargarEstilosPopup(popup);
        		popup.showAndWait();
				
			}
			
			
		} else {
			
			Alert popup = Popup.lanzarPopup("Error", "Ya existe un ??lbum con el EAN introducido", 2);
			cargarEstilosPopup(popup);
    		popup.showAndWait();
			
		}
		
	}
	
	private String descagarCaratula() {
		
		// M??todo para descargar la imagen introducida en el campo de portada y guardarla en el directorio pertinente para luego poder acceder a ellas, 
		// simulando que es el servidor privado de la aplicaci??n.
		BufferedImage image;
		String ruta = null;
		
		try {
			
			image = ImageIO.read(new URL(caratula.getText()));
			// Con .replaceAll elimino todos los espacios en blanco, las comas, los puntos, las interrogaciones y admiraciones del t??tulo
			ruta = "caratulas\\" + ean.getText() + "_" + titulo.getText().replaceAll("\\s+", "").replaceAll("\\,", "").replaceAll("\\.", "").replaceAll("\\?", "")
					.replaceAll("\\??", "").replaceAll("??", "").replaceAll("!", "") +".png";
			ImageIO.write(image , "png", new File(ruta));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ruta;
		
	}

	public void limpiarCampos() {
		
		// M??todo que limpia los campos de la ventana
		ean.clear();
		titulo.clear();
		artista.clear();
		genero.setValue(null);
		duracion.clear();
		formato.setValue(null);
		fechaPublicacion.setValue(null);
		precio.clear();
		discografica.clear();
		caratula.clear();
		try {
			imagenCaratula.setImage(new Image(getClass().getResource("../img/caratula_imagen.png").toURI().toString()));
		} catch (URISyntaxException e) {}
		
		guardarAlbum.setDisable(true);
		btnComprobarCaratula.setDisable(true);
		
	}
	
	public void comprobarCaratula() {
		
		// M??todo que comprueba si la url de una imagen es v??lida
		
	    try {  
	        BufferedImage image = ImageIO.read(new URL(caratula.getText()));  
	        caratulaValida = true;
	        imagenCaratula.setImage(new Image(caratula.getText()));
	        imagenCaratula.setDisable(true);
	        activarGuardarAlbum();
	    } catch (MalformedURLException e) {
	    	caratulaValida = false;
	    	activarGuardarAlbum();
	    	// Cargamos la imagen de caratula no v??lida en el ImageView
	    	try {
	    		imagenCaratula.setImage(new Image(getClass().getResource("../img/caratula_no_valida.png").toURI().toString()));
			} catch (URISyntaxException e1) {}
	    	
	    } catch (IOException e) {
	    	caratulaValida = false;
	    	activarGuardarAlbum();
	    	// Cargamos la imagen de caratula no v??lida en el ImageView
	    	try {
	    		imagenCaratula.setImage(new Image(getClass().getResource("../img/caratula_no_valida.png").toURI().toString()));
			} catch (URISyntaxException e1) {}
	    }
		
	}
	
	public void activarGuardarAlbum() {
		
		if((!ean.getText().isEmpty()) && (!titulo.getText().isEmpty()) && (!artista.getText().isEmpty()) && (genero.getValue() != null)
				&& (!duracion.getText().isEmpty()) && (formato.getValue() != null) && (fechaPublicacion.getValue() != null) && (!precio.getText().isEmpty()) 
				&& (!discografica.getText().isEmpty()) && (!caratula.getText().isEmpty()) && caratulaValida == true) {
			
			guardarAlbum.setDisable(false);
		} else {
			guardarAlbum.setDisable(true);
		}
		
		
		
	}
	
	public void activarComprobarCaratula() {
		
		if(!caratula.getText().isEmpty()) {
			btnComprobarCaratula.setDisable(false);
		} else {
			btnComprobarCaratula.setDisable(true);
		}
		
	}
	
	private void cargarEstilosPopup(Alert popup) {
		
		DialogPane dialogPane = popup.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("../estilos/popup.css").toExternalForm());
		dialogPane.getStyleClass().add("popup");
		
		Stage stage = (Stage) dialogPane.getScene().getWindow();
		
		try {
			stage.getIcons().add(new Image(getClass().getResource("../img/icono.png").toURI().toString()));
		} catch (URISyntaxException e) {}
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		genero.getItems().removeAll(genero.getItems());
		formato.getItems().removeAll(genero.getItems());
		List<String> generos = null;
		List<String> formatos = null;
	    
		try {
			generos = Files.lines(rutaGeneros).collect(Collectors.toList());
			formatos = Files.lines(rutaFormatos).collect(Collectors.toList());
		} catch (IOException e) {}
		
	    genero.setItems(FXCollections.observableArrayList(generos));
	    formato.setItems(FXCollections.observableArrayList(formatos));
		
		guardarAlbum.setDisable(true);
		btnComprobarCaratula.setDisable(true);
		
		// Bloquear valores no numericos en el campo del ISBN-13
	    ean.textProperty().addListener(new ChangeListener<String>() {
	    	@Override
	    	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	    		if (!newValue.matches("\\d*")) {
	    			ean.setText(newValue.replaceAll("[^\\d]", ""));
	    		}
	    	}
	     });
	    
	    // Bloquear valores no numericos en el campo duraci??n
	    duracion.textProperty().addListener(new ChangeListener<String>() {
	    	@Override
	    	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	    		if (!newValue.matches("\\d*")) {
	    			duracion.setText(newValue.replaceAll("[^\\d]", ""));
	    		}
	    	}
	     });
	    
	    // Bloquear valores no numericos en el campo del precio
	    precio.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*(\\.\\d*)?")) {
	            	precio.setText(oldValue);
	            }
	        }
	    });
	    
	    // Bloquea las fechas mayores que la fecha actual
	    fechaPublicacion.setDayCellFactory(param -> new DateCell() {
	    	@Override
	    	public void updateItem(LocalDate date, boolean empty) {
	    		super.updateItem(date, empty);
	    		setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
	    	}
	    });
	       
	 }
		
}


