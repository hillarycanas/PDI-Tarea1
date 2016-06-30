<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea1;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Hillary
 */
public class bitmap {
        //lectura del archivo
        InputStream archivo;
        int PosicionActual = 0;
        
        //lectura de la imagen- bitmap header
        int tamRealBitmap;
        int tamScan;
        int cantColores;
        int bmpOffset;               //posicion inicial de los datos de la imagen
        int ancho;                              
        int alto;                            
        short bitsPorPixel;             
        int comprimido;                
        
        //lectura de la imagen- paleta
        byte r[], g[], b[];             
        int entradas;
        //lectura de la imagen- contenido pxl
        byte[] dataEnByte;                
        int[] dataEnInt;                 
        boolean leer;
        int[] ndata8; 

        
        public void read(InputStream archivo) throws IOException, Exception {
                this.archivo = archivo;
                //14 BYTES FILE HEADER
                short tipoArchivo = 0x4d42;//"BM"
                int tamArchivo;          
                short reservado1 = 0;    
                short reservado2 = 0;    

                tipoArchivo = leerShort();
                tamArchivo = leerEnteros();
                reservado1 = leerShort();
                reservado2 = leerShort();
                bmpOffset = leerEnteros();
                
                //BMP HEADER 40 BYTES
                // Actual contents (40 bytes):
                int size;                               
                short planos;                   
                int tamBMP;               
                int ResHor;             
                int ResVert;             
                int coloresPaleta;                
                int coloresImportantes;    
                int cantPixl;

                size = leerEnteros();
                ancho = leerEnteros();
                alto = leerEnteros();
                planos = leerShort();
                bitsPorPixel = leerShort();
                comprimido = leerEnteros();
                tamBMP = leerEnteros();
                ResHor = leerEnteros();
                ResVert = leerEnteros();
                coloresPaleta = leerEnteros();
                coloresImportantes = leerEnteros();
                
                    if (bitsPorPixel==24)
                        coloresPaleta = coloresImportantes = 0;

                    leer = (alto < 0);
                    if (leer) alto = -alto;
                    cantPixl = ancho * alto;
                    System.out.println("TIENE bpp " + bitsPorPixel);  //ruta
                    // Scan line archivo con padding de 4
                    tamScan = ((ancho * bitsPorPixel + 31) / 32) * 4;

                    tamRealBitmap = tamScan * alto;
                     System.out.println("colores usados" + coloresPaleta); 
                     System.out.println("colores importantes" + coloresImportantes); 

                    if (coloresPaleta != 0)
                            cantColores = coloresPaleta;
                    else
                           //los colores son los bits por pixel
                            if (bitsPorPixel < 16)
                                    cantColores = 1 << bitsPorPixel;
                            else
                                    cantColores = 0;   

                    //fin del header

                    //si esta comprimido
                    if (comprimido!=0)
                            throw new Exception("Error: Archivo comprimido");

                    //PALETA DE COLORES  para todos menos el de 24
                    //System.out.println("la paleta tiene colores" + cantColores);
                
                    entradas = cantColores;
                    //si el numero de colores es 0, no hay paleta
                    if (entradas>0) {
                            r = new byte[entradas];
                            g = new byte[entradas];
                            b = new byte[entradas];

                            int reserved;
                            for (int i = 0; i < entradas; i++) {
                                    b[i] = (byte)archivo.read();
                                    g[i] = (byte)archivo.read();
                                    r[i] = (byte)archivo.read();
                                    reserved = archivo.read();
                                    PosicionActual += 4;
                            }
                    }
                

                    //Arreglo para la data de la imagen
                    byte[] Data;                 
                    // saltar a la data
                    long salto = bmpOffset - PosicionActual;
                    if (salto > 0) {
                            archivo.skip(salto);
                            PosicionActual += salto;
                    }

                    int len = tamScan;
                    if (bitsPorPixel > 8)
                            dataEnInt = new int[ancho * alto];
                            else
                                dataEnByte = new byte[ancho * alto];
                    Data = new byte[tamRealBitmap];
                    int dataOffset = 0;
                    int offset = (alto - 1) * ancho;
                    for (int i = alto - 1; i >= 0; i--) {
                            int n = archivo.read(Data, dataOffset, len);
                            if (n < len) throw new Exception("Error de linea");
                            if (bitsPorPixel==24 ){//24 bits
                                    int j = offset;
                                    int ro = dataOffset;
                                    int mascara = 0xff;
                                    for (int g = 0; g < ancho; g++) {
                                            int b0 = (((int)(Data[ro++])) & mascara);
                                            int b1 = (((int)(Data[ro++])) & mascara) << 8;
                                            int b2 = (((int)(Data[ro++])) & mascara) << 16;
                                            dataEnInt[j] = 0xff000000 | b0 | b1 | b2;
                                            j++;
                                    }
                            }
                                    // 8 bits y menos
                            else {
                                    extraerDatos(Data, dataOffset, bitsPorPixel, dataEnByte, offset, ancho);
                                    
                                }
                            dataOffset += len;
                            offset -= ancho;
                    }
                         
                
        }


        public MemoryImageSource crearImageSource() {
                MemoryImageSource futuraimagen;
                ColorModel model;
                //necesario para poder desplegarlo despues en el label

                // Con paleta
                if (entradas>0 && bitsPorPixel!=24) {
                    model = new IndexColorModel(bitsPorPixel, entradas, r, g, b);
                } else {
                        model = ColorModel.getRGBdefault(); //sin
                }

                if (bitsPorPixel > 8) {
                        // use one int per pixel
                        futuraimagen = new MemoryImageSource(ancho,
                                alto, model, dataEnInt, 0, ancho);
                }  
                else {
                            // use one byte per pixel
                            futuraimagen = new MemoryImageSource(ancho,
                                alto, model, dataEnByte, 0, ancho);
                    }
                return futuraimagen;      
        }


        private int leerEnteros() throws IOException {
            int bit1 = archivo.read();
            int bit2 = archivo.read();
            int bit3 = archivo.read();
            int bit4 = archivo.read();
            PosicionActual += 4;
            return ((bit4 << 24) + (bit3 << 16) + (bit2 << 8) + (bit1 << 0));
        }

        private short leerShort() throws IOException {
            int bit1 = archivo.read();
            int bit2 = archivo.read();
            PosicionActual += 2;
            return (short)((bit2 << 8) + bit1);
        }

        void extraerDatos(byte[] Data, int dataOffset, int bpp, byte[] dataEnByte, int byteOffset, int w) throws Exception {
            byte mascara = 0;
            int pixPerByte = 0;
            int j = byteOffset;
            int k = dataOffset;
                

            switch (bpp) {
                case 1:{
                    mascara = (byte)0x01;
                    pixPerByte = 8; 
                    }break; 
                    
                case 4: {
                    mascara = (byte)0x0f;
                    pixPerByte = 2;
                    } break;
                    
                case 8:{
                    mascara = (byte)0xff;
                    pixPerByte = 1;
                } break;
                
               
             }
            int ite = 0;
            while(true){
                 int mover = 8 - bpp;
                    for (int h = 0; h < pixPerByte; h++) {
                            byte byte1 = Data[k];
                            byte1 >>= mover;
                            dataEnByte[j] = (byte)(byte1 & mascara);
                            j++;
                            ite++;
                            if (ite == w) return;
                                mover =mover- bpp;
                        }
                        k++;
                }
        }
}
=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea1;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Hillary
 */
public class bitmap {
        //lectura del archivo
        InputStream archivo;
        int PosicionActual = 0;
        
        //lectura de la imagen- bitmap header
        int tamRealBitmap;
        int tamScan;
        int cantColores;
        int bmpOffset;               //posicion inicial de los datos de la imagen
        int ancho;                              
        int alto;                            
        short bitsPorPixel;             
        int comprimido;                
        
        //lectura de la imagen- paleta
        byte r[], g[], b[];             
        int entradas;
        //lectura de la imagen- contenido pxl
        byte[] dataEnByte;                
        int[] dataEnInt;                 
        boolean leer;
        int[] ndata8; 

        
        public void read(InputStream archivo) throws IOException, Exception {
                this.archivo = archivo;
                //14 BYTES FILE HEADER
                short tipoArchivo = 0x4d42;//"BM"
                int tamArchivo;          
                short reservado1 = 0;    
                short reservado2 = 0;    

                tipoArchivo = leerShort();
                tamArchivo = leerEnteros();
                reservado1 = leerShort();
                reservado2 = leerShort();
                bmpOffset = leerEnteros();
                
                //BMP HEADER 40 BYTES
                // Actual contents (40 bytes):
                int size;                               
                short planos;                   
                int tamBMP;               
                int ResHor;             
                int ResVert;             
                int coloresPaleta;                
                int coloresImportantes;    
                int cantPixl;

                size = leerEnteros();
                ancho = leerEnteros();
                alto = leerEnteros();
                planos = leerShort();
                bitsPorPixel = leerShort();
                comprimido = leerEnteros();
                tamBMP = leerEnteros();
                ResHor = leerEnteros();
                ResVert = leerEnteros();
                coloresPaleta = leerEnteros();
                coloresImportantes = leerEnteros();
                
                    if (bitsPorPixel==24)
                        coloresPaleta = coloresImportantes = 0;

                    leer = (alto < 0);
                    if (leer) alto = -alto;
                    cantPixl = ancho * alto;
                    System.out.println("TIENE bpp " + bitsPorPixel);  //ruta
                    // Scan line archivo con padding de 4
                    tamScan = ((ancho * bitsPorPixel + 31) / 32) * 4;

                    tamRealBitmap = tamScan * alto;
                     System.out.println("colores usados" + coloresPaleta); 
                     System.out.println("colores importantes" + coloresImportantes); 

                    if (coloresPaleta != 0)
                            cantColores = coloresPaleta;
                    else
                           //los colores son los bits por pixel
                            if (bitsPorPixel < 16)
                                    cantColores = 1 << bitsPorPixel;
                            else
                                    cantColores = 0;   

                    //fin del header

                    //si esta comprimido
                    if (comprimido!=0)
                            throw new Exception("Error: Archivo comprimido");

                    //PALETA DE COLORES  para todos menos el de 24
                    //System.out.println("la paleta tiene colores" + cantColores);
                
                    entradas = cantColores;
                    //si el numero de colores es 0, no hay paleta
                    if (entradas>0) {
                            r = new byte[entradas];
                            g = new byte[entradas];
                            b = new byte[entradas];

                            int reserved;
                            for (int i = 0; i < entradas; i++) {
                                    b[i] = (byte)archivo.read();
                                    g[i] = (byte)archivo.read();
                                    r[i] = (byte)archivo.read();
                                    reserved = archivo.read();
                                    PosicionActual += 4;
                            }
                    }
                

                    //Arreglo para la data de la imagen
                    byte[] Data;                 
                    // saltar a la data
                    long salto = bmpOffset - PosicionActual;
                    if (salto > 0) {
                            archivo.skip(salto);
                            PosicionActual += salto;
                    }

                    int len = tamScan;
                    if (bitsPorPixel > 8)
                            dataEnInt = new int[ancho * alto];
                            else
                                dataEnByte = new byte[ancho * alto];
                    Data = new byte[tamRealBitmap];
                    int dataOffset = 0;
                    int offset = (alto - 1) * ancho;
                    for (int i = alto - 1; i >= 0; i--) {
                            int n = archivo.read(Data, dataOffset, len);
                            if (n < len) throw new Exception("Error de linea");
                            if (bitsPorPixel==24 ){//24 bits
                                    int j = offset;
                                    int ro = dataOffset;
                                    int mascara = 0xff;
                                    for (int g = 0; g < ancho; g++) {
                                            int b0 = (((int)(Data[ro++])) & mascara);
                                            int b1 = (((int)(Data[ro++])) & mascara) << 8;
                                            int b2 = (((int)(Data[ro++])) & mascara) << 16;
                                            dataEnInt[j] = 0xff000000 | b0 | b1 | b2;
                                            j++;
                                    }
                            }
                                    // 8 bits y menos
                            else {
                                    extraerDatos(Data, dataOffset, bitsPorPixel, dataEnByte, offset, ancho);
                                    
                                }
                            dataOffset += len;
                            offset -= ancho;
                    }
                         
                
        }


        public MemoryImageSource crearImageSource() {
                MemoryImageSource futuraimagen;
                ColorModel model;
                //necesario para poder desplegarlo despues en el label

                // Con paleta
                if (entradas>0 && bitsPorPixel!=24) {
                    model = new IndexColorModel(bitsPorPixel, entradas, r, g, b);
                } else {
                        model = ColorModel.getRGBdefault(); //sin
                }

                if (bitsPorPixel > 8) {
                        // use one int per pixel
                        futuraimagen = new MemoryImageSource(ancho,
                                alto, model, dataEnInt, 0, ancho);
                }  
                else {
                            // use one byte per pixel
                            futuraimagen = new MemoryImageSource(ancho,
                                alto, model, dataEnByte, 0, ancho);
                    }
                return futuraimagen;      
        }


        private int leerEnteros() throws IOException {
            int bit1 = archivo.read();
            int bit2 = archivo.read();
            int bit3 = archivo.read();
            int bit4 = archivo.read();
            PosicionActual += 4;
            return ((bit4 << 24) + (bit3 << 16) + (bit2 << 8) + (bit1 << 0));
        }

        private short leerShort() throws IOException {
            int bit1 = archivo.read();
            int bit2 = archivo.read();
            PosicionActual += 2;
            return (short)((bit2 << 8) + bit1);
        }

        void extraerDatos(byte[] Data, int dataOffset, int bpp, byte[] dataEnByte, int byteOffset, int w) throws Exception {
            byte mascara = 0;
            int pixPerByte = 0;
            int j = byteOffset;
            int k = dataOffset;
                

            switch (bpp) {
                case 1:{
                    mascara = (byte)0x01;
                    pixPerByte = 8; 
                    }break; 
                    
                case 4: {
                    mascara = (byte)0x0f;
                    pixPerByte = 2;
                    } break;
                    
                case 8:{
                    mascara = (byte)0xff;
                    pixPerByte = 1;
                } break;
                
               
             }
            int ite = 0;
            while(true){
                 int mover = 8 - bpp;
                    for (int h = 0; h < pixPerByte; h++) {
                            byte byte1 = Data[k];
                            byte1 >>= mover;
                            dataEnByte[j] = (byte)(byte1 & mascara);
                            j++;
                            ite++;
                            if (ite == w) return;
                                mover =mover- bpp;
                        }
                        k++;
                }
        }
}
>>>>>>> origin/master
