<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.MemoryImageSource;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author Hillary
 */
public class controlador  {
    static public interfaz inicio;
    String ruta;
    //int cw = 0;
    //int ccw = 0;
    BufferedImage img = null;
    Boolean neg = false;
  
    public controlador()
    {     
        inicio= new interfaz();
        inicio.show();   
        inicio.setTitle("PDI: Tarea 1");
        inicio.setVisible(true);
        inicio.setLocationRelativeTo(null);  
    }
   
   
    public void seleccionOpcion (int z) throws IOException
   {
           switch (z)
           {
               case 1:
               {   
                  //ELEGIR UN ARCHIVO//
                   //EN CASO DE QUERER CAMBIAR EL TIPO DE ARCHIVO.
                   FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "bmp");  
                   JFileChooser abrir = new JFileChooser();
                   abrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
                   abrir.setFileFilter(filter);
                   abrir.setCurrentDirectory(new File(System.getProperty("user.home")));
                   int result = abrir.showOpenDialog(inicio);
                   if (result == JFileChooser.APPROVE_OPTION) {
                        // se seleciona el archivo de imagen original
                        File selectedFile = abrir.getSelectedFile();
                        ruta = selectedFile.getAbsolutePath();
                        System.out.println("El archivo es: " + ruta);  //ruta
                        
                      
                        img = ImageIO.read(new File(ruta)); //se lee el archivo
                        //img.getScaledInstance( inicio.jLabel3.getWidth(),  inicio.jLabel3.getHeight(), Image.SCALE_SMOOTH);
                        inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                        ImageIcon icon = new ImageIcon(img); //se despliega la imagen en el jlabel3
                        inicio.jLabel3.setIcon(icon);

                        
                        //int height = img.getHeight();
                        //int width = img.getWidth();
                        neg = false;
                        
                        //para crear el negativo.
                        // BufferedImage imgNeg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
                               
                        //Other stuff

                        //System.out.println(height + "  " + width + " " + img.getRGB(54, 30));
                  
                   }
                }
                break;//end case 1
                
                case 2: //imagen en negativo
                {
                    
                    if(!neg){
                            
                            //ancho y alto
                            int ancho = img.getWidth();
                            int alto = img.getHeight();
                            //se crea un buffer
                            BufferedImage imagenNegativa = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
                            //se convierten los colores a negativo y se va guardando en el buffer
                            for(int y = 0; y < alto; y++){
                              for(int x = 0; x < ancho; x++){
                                int p = img.getRGB(x,y);
                                //obtenermos el valor r g b a de cada pixel
                                int a = (p>>24)&0xff;
                                int r = (p>>16)&0xff;
                                int g = (p>>8)&0xff;
                                int b = p&0xff;
                                //se resta el rbg
                                r = 255 - r;
                                g = 255 - g;
                                b = 255 - b;
                                //se guarda el rgb
                                p = (a<<24) | (r<<16) | (g<<8) | b;
                                imagenNegativa.setRGB(x, y, p);
                              }
                            }
                            //se reemplaza la imagen original cargada por esta
                            img= imagenNegativa;
                            neg = true;
                    }
                    inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img); //se despliega la imagen en el jlabel3
                    inicio.jLabel3.setIcon(icon);
                } 
                break;//end case 2
                
                case 3: //flip imagen vertical
                {
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                        //buffer para la imagen
                        BufferedImage mirrorimgV = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                        //recorremos pixel a pixel tooooooooooooodo el buffer
                        for(int i = 0; i < height; i++){
                          for(int izquierda = 0, derecha = width - 1; izquierda  < width; izquierda ++, derecha--){
                            int p = img.getRGB(izquierda , i);
                            mirrorimgV.setRGB(derecha, i, p);
                          }
                        }
                        img = mirrorimgV;
                        inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                        ImageIcon icon = new ImageIcon(img); 
                        inicio.jLabel3.setIcon(icon);
                
                }
                break;//end case 3
                
                
                case 4://flip imagen horizontal
                {
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                        BufferedImage mirrorimgH = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
              
                        for(int i = 0; i < width; i++){
                            for(int arriba = 0, abajo = height - 1; arriba < height; arriba++, abajo--){
                                int p = img.getRGB(i, arriba);
                                mirrorimgH.setRGB(i, abajo, p);
                          }
                        }
                        img = mirrorimgH;
                        inicio.jLabel3.setBounds(0,0,mirrorimgH.getWidth(),mirrorimgH.getHeight());
                        ImageIcon icon = new ImageIcon(mirrorimgH); 
                        inicio.jLabel3.setIcon(icon);
                
                }
                break;//end case 4
                
                case 5:{ //boton de reset
                    
                    //RESET
                    File f = null;
                    //leer image
                        try{
                          f = new File(ruta);
                          img = ImageIO.read(f);
                        }catch(IOException e){
                          System.out.println(e);
                        } 
                    neg = false;
                    //cw = 0;
                    //ccw = 0;
                    inicio.jLabel3.setBounds(10,10,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img); 
                    inicio.jLabel3.setIcon(icon);
                
                
                }break; //end case 5
                
                
                case 6:{ //leer en formato binario
                    
                   FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "bmp");  
                   JFileChooser abrir = new JFileChooser();
                   abrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
                   abrir.setFileFilter(filter);
                   abrir.setCurrentDirectory(new File(System.getProperty("user.home")));
                   int result = abrir.showOpenDialog(inicio);
                   if (result == JFileChooser.APPROVE_OPTION) {
                        
                       try {
                           File selectedFile = abrir.getSelectedFile();
                           ruta = selectedFile.getAbsolutePath();
                           
                           bitmap bmp = new bitmap();
                           FileInputStream  is = null;
                           
                           is = new FileInputStream(ruta);
                           bmp.read(is);
                           System.out.println("aqui");
                           MemoryImageSource mis = bmp.crearImageSource();
                           System.out.println("hola");
                           Image im = Toolkit.getDefaultToolkit().createImage(mis);
                           //Para poder colorcarlo en el label
                           //Image image = createImage(new MemoryImageSource(bmp.crearImageSource()));
                           BufferedImage newImage = new BufferedImage(im.getWidth(null), im.getHeight(null),BufferedImage.TYPE_INT_ARGB);
                           //obtenemos la imagen que si se puede desplgar
                           Graphics2D g = newImage.createGraphics();
                           g.drawImage(im, 0, 0, null);
                           g.dispose();
                           
                           img = newImage;
                           inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                           ImageIcon icon = new ImageIcon(img); 
                           inicio.jLabel3.setIcon(icon);
                           neg = false;
                           
                        } 
                    catch (Exception ex) {
                           Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }//end approval if
                }break; //end case 6
                
                //girar CW
                case 7:{ 
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                    BufferedImage new_Image = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_3BYTE_BGR);
                            for (int i = 0; i < width; i++) {
                                    for (int j = 0; j < height; j++) {
                                            int p = img.getRGB(i, j);
                                            new_Image.setRGB(height - j - 1, i, p);
                                            
                                    }
                            }
                            
                    img= new_Image;
                    inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img);
                    inicio.jLabel3.setIcon(icon);

                   
                }break;//end case 7
                
                 //girar CCW
                case 8:{ 
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                    BufferedImage new_Image = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_3BYTE_BGR);
                            for (int i = 0; i < width; i++) {
                                    for (int j = 0; j < height; j++) {
                                             int p = img.getRGB(i, j);
                                           new_Image.setRGB(j,width - i - 1, p);
                                            
                                    }
                            }
                            
                    img= new_Image;
                    inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img); 
                    inicio.jLabel3.setIcon(icon);
                   
                }//end case 8
                
            } //end switch
           
           
    
   }//end function 

  
}

=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.MemoryImageSource;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author Hillary
 */
public class controlador  {
    static public interfaz inicio;
    String ruta;
    //int cw = 0;
    //int ccw = 0;
    BufferedImage img = null;
    Boolean neg = false;
  
    public controlador()
    {     
        inicio= new interfaz();
        inicio.show();   
        inicio.setTitle("PDI: Tarea 1");
        inicio.setVisible(true);
        inicio.setLocationRelativeTo(null);  
    }
   
   
    public void seleccionOpcion (int z) throws IOException
   {
           switch (z)
           {
               case 1:
               {   
                  //ELEGIR UN ARCHIVO//
                   //EN CASO DE QUERER CAMBIAR EL TIPO DE ARCHIVO.
                   FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "bmp");  
                   JFileChooser abrir = new JFileChooser();
                   abrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
                   abrir.setFileFilter(filter);
                   abrir.setCurrentDirectory(new File(System.getProperty("user.home")));
                   int result = abrir.showOpenDialog(inicio);
                   if (result == JFileChooser.APPROVE_OPTION) {
                        // se seleciona el archivo de imagen original
                        File selectedFile = abrir.getSelectedFile();
                        ruta = selectedFile.getAbsolutePath();
                        System.out.println("El archivo es: " + ruta);  //ruta
                        
                      
                        img = ImageIO.read(new File(ruta)); //se lee el archivo
                        //img.getScaledInstance( inicio.jLabel3.getWidth(),  inicio.jLabel3.getHeight(), Image.SCALE_SMOOTH);
                        inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                        ImageIcon icon = new ImageIcon(img); //se despliega la imagen en el jlabel3
                        inicio.jLabel3.setIcon(icon);

                        
                        //int height = img.getHeight();
                        //int width = img.getWidth();
                        neg = false;
                        
                        //para crear el negativo.
                        // BufferedImage imgNeg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
                               
                        //Other stuff

                        //System.out.println(height + "  " + width + " " + img.getRGB(54, 30));
                  
                   }
                }
                break;//end case 1
                
                case 2: //imagen en negativo
                {
                    
                    if(!neg){
                            
                            //ancho y alto
                            int ancho = img.getWidth();
                            int alto = img.getHeight();
                            //se crea un buffer
                            BufferedImage imagenNegativa = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
                            //se convierten los colores a negativo y se va guardando en el buffer
                            for(int y = 0; y < alto; y++){
                              for(int x = 0; x < ancho; x++){
                                int p = img.getRGB(x,y);
                                //obtenermos el valor r g b a de cada pixel
                                int a = (p>>24)&0xff;
                                int r = (p>>16)&0xff;
                                int g = (p>>8)&0xff;
                                int b = p&0xff;
                                //se resta el rbg
                                r = 255 - r;
                                g = 255 - g;
                                b = 255 - b;
                                //se guarda el rgb
                                p = (a<<24) | (r<<16) | (g<<8) | b;
                                imagenNegativa.setRGB(x, y, p);
                              }
                            }
                            //se reemplaza la imagen original cargada por esta
                            img= imagenNegativa;
                            neg = true;
                    }
                    inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img); //se despliega la imagen en el jlabel3
                    inicio.jLabel3.setIcon(icon);
                } 
                break;//end case 2
                
                case 3: //flip imagen vertical
                {
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                        //buffer para la imagen
                        BufferedImage mirrorimgV = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                        //recorremos pixel a pixel tooooooooooooodo el buffer
                        for(int i = 0; i < height; i++){
                          for(int izquierda = 0, derecha = width - 1; izquierda  < width; izquierda ++, derecha--){
                            int p = img.getRGB(izquierda , i);
                            mirrorimgV.setRGB(derecha, i, p);
                          }
                        }
                        img = mirrorimgV;
                        inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                        ImageIcon icon = new ImageIcon(img); 
                        inicio.jLabel3.setIcon(icon);
                
                }
                break;//end case 3
                
                
                case 4://flip imagen horizontal
                {
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                        BufferedImage mirrorimgH = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
              
                        for(int i = 0; i < width; i++){
                            for(int arriba = 0, abajo = height - 1; arriba < height; arriba++, abajo--){
                                int p = img.getRGB(i, arriba);
                                mirrorimgH.setRGB(i, abajo, p);
                          }
                        }
                        img = mirrorimgH;
                        inicio.jLabel3.setBounds(0,0,mirrorimgH.getWidth(),mirrorimgH.getHeight());
                        ImageIcon icon = new ImageIcon(mirrorimgH); 
                        inicio.jLabel3.setIcon(icon);
                
                }
                break;//end case 4
                
                case 5:{ //boton de reset
                    
                    //RESET
                    File f = null;
                    //leer image
                        try{
                          f = new File(ruta);
                          img = ImageIO.read(f);
                        }catch(IOException e){
                          System.out.println(e);
                        } 
                    neg = false;
                    //cw = 0;
                    //ccw = 0;
                    inicio.jLabel3.setBounds(10,10,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img); 
                    inicio.jLabel3.setIcon(icon);
                
                
                }break; //end case 5
                
                
                case 6:{ //leer en formato binario
                    
                   FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "bmp");  
                   JFileChooser abrir = new JFileChooser();
                   abrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
                   abrir.setFileFilter(filter);
                   abrir.setCurrentDirectory(new File(System.getProperty("user.home")));
                   int result = abrir.showOpenDialog(inicio);
                   if (result == JFileChooser.APPROVE_OPTION) {
                        
                       try {
                           File selectedFile = abrir.getSelectedFile();
                           ruta = selectedFile.getAbsolutePath();
                           
                           bitmap bmp = new bitmap();
                           FileInputStream  is = null;
                           
                           is = new FileInputStream(ruta);
                           bmp.read(is);
                           System.out.println("aqui");
                           MemoryImageSource mis = bmp.crearImageSource();
                           System.out.println("hola");
                           Image im = Toolkit.getDefaultToolkit().createImage(mis);
                           //Para poder colorcarlo en el label
                           //Image image = createImage(new MemoryImageSource(bmp.crearImageSource()));
                           BufferedImage newImage = new BufferedImage(im.getWidth(null), im.getHeight(null),BufferedImage.TYPE_INT_ARGB);
                           //obtenemos la imagen que si se puede desplgar
                           Graphics2D g = newImage.createGraphics();
                           g.drawImage(im, 0, 0, null);
                           g.dispose();
                           
                           img = newImage;
                           inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                           ImageIcon icon = new ImageIcon(img); 
                           inicio.jLabel3.setIcon(icon);
                           neg = false;
                           
                        } 
                    catch (Exception ex) {
                           Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }//end approval if
                }break; //end case 6
                
                //girar CW
                case 7:{ 
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                    BufferedImage new_Image = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_3BYTE_BGR);
                            for (int i = 0; i < width; i++) {
                                    for (int j = 0; j < height; j++) {
                                            int p = img.getRGB(i, j);
                                            new_Image.setRGB(height - j - 1, i, p);
                                            
                                    }
                            }
                            
                    img= new_Image;
                    inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img);
                    inicio.jLabel3.setIcon(icon);

                   
                }break;//end case 7
                
                 //girar CCW
                case 8:{ 
                    
                    int width = img.getWidth();
                    int height = img.getHeight();
                    BufferedImage new_Image = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_3BYTE_BGR);
                            for (int i = 0; i < width; i++) {
                                    for (int j = 0; j < height; j++) {
                                             int p = img.getRGB(i, j);
                                           new_Image.setRGB(j,width - i - 1, p);
                                            
                                    }
                            }
                            
                    img= new_Image;
                    inicio.jLabel3.setBounds(0,0,img.getWidth(),img.getHeight());
                    ImageIcon icon = new ImageIcon(img); 
                    inicio.jLabel3.setIcon(icon);
                   
                }//end case 8
                
            } //end switch
           
           
    
   }//end function 

  
}

>>>>>>> origin/master
