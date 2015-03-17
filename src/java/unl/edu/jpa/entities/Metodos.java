/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.jpa.entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ERIKA
 */
public class Metodos {

    
    /**
     * Este método permite generar una contraseña al capacitador cuando se
     * registra además de generar una nueva en caso de que el capacitador olvide
     * la contraseña.
     *
     * @return contraseña generada
     */
    public static String generarContrasenia() {
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@!#$";
        int LargoContrasena = 8;
        String contrasena = "";
        int longitud = base.length();
        for (int i = 0; i < LargoContrasena; i++) { // 1
            int numero = (int) (Math.random() * (longitud)); // 2
            String caracter = base.substring(numero, numero + 1); // 3
            contrasena = contrasena + caracter; // 4
        }
        return contrasena;
    }

    /**
     * Método que permite encriptar la contraseña
     *
     * @param message contraseña a encriptar
     * @return contraseña encriptada
     */
    public static String encripta(String message) {
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
    }

    private static String toHexadecimal(byte[] digest) {
        String hash = "";
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    /**
     * Este método permite enviar la contraseña del capacitador a su correo
     * electrónico por seguridad luego el usuario podra cambiarla
     *
     * @param to correo al que se enviará la contraseña
     * @param contrasenaEnviar contraseña a enviar
     * @return boolean
     */
    public static boolean enviarMail(String to, String contrasenaEnviar) {

        try {
            // Propiedades de la conexion 
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            //Recoger los datos
            String str_De = "m.erika.05.22@gmail.com";
            String str_PwRemitente = "katy1438";
            String str_Para = to;
            String str_Asunto = "CapacitySoft";
            String str_Mensaje = "CAPACITYSOFT \n correo electrónico: " + str_Para
                    + " \n contraseña: " + contrasenaEnviar;
            //Obtenemos los destinatarios
            String destinos[] = str_Para.split(",");

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(str_De));

            //Otra forma de especificar las direcciones de email 
            //a quienes se enviar el correo electronico
            Address[] receptores = new Address[destinos.length];
            int j = 0;
            while (j < destinos.length) {
                receptores[j] = new InternetAddress(destinos[j]);
                j++;
            }

            //receptores.
            message.addRecipients(Message.RecipientType.TO, receptores);
            message.setSubject(str_Asunto);
            message.setText(str_Mensaje);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect(str_De, str_PwRemitente);
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

            // Cierre de la conexion.
            t.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean enviarMailCapacitador(String para, String de, String contrasenia, String mensaje) {

        try {
            // Propiedades de la conexion 
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            //Recoger los datos
            String str_De = de;
            String str_PwRemitente = contrasenia;
            String str_Para = para;
            String str_Asunto = "CapacitySoft";
            String str_Mensaje = mensaje;
            //Obtenemos los destinatarios
            String destinos[] = str_Para.split(",");

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(str_De));

            //Otra forma de especificar las direcciones de email 
            //a quienes se enviar el correo electronico
            Address[] receptores = new Address[destinos.length];
            int j = 0;
            while (j < destinos.length) {
                receptores[j] = new InternetAddress(destinos[j]);
                j++;
            }

            //receptores.
            message.addRecipients(Message.RecipientType.TO, receptores);
            message.setSubject(str_Asunto);
            message.setText(str_Mensaje);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect(str_De, str_PwRemitente);
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

            // Cierre de la conexion.
            t.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método permite verificar que la cédula ingresada por el capacitador
     * sea válida.
     *
     * @param cedula
     * @return boolean
     */
    public static boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
                    // Coeficientes de validacion cedula
                    // El decimo digito se lo considera digito verificador
                    int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    int verificador = Integer.parseInt(cedula.substring(9, 10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    } else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validacion de la cédula del capacitador");
            cedulaCorrecta = false;
        }

        if (!cedulaCorrecta) {
//            System.out.println("La cedula ingresada es Incorrecta");
        }
        return cedulaCorrecta;
    }

//    //---- contrasenia ----//
//    private static String keyBuffer = "56af65d2";
//
//    public static byte[] encode(byte[] b) throws Exception {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        OutputStream b64os = MimeUtility.encode(baos, "base64");
//        b64os.write(b);
//        b64os.close();
//        return baos.toByteArray();
//    }
//
//    public static byte[] decode(byte[] b) throws Exception {
//        ByteArrayInputStream bais = new ByteArrayInputStream(b);
//        InputStream b64is = MimeUtility.decode(bais, "base64");
//        byte[] tmp = new byte[b.length];
//        int n = b64is.read(tmp);
//        byte[] res = new byte[n];
//        System.arraycopy(tmp, 0, res, 0, n);
//        return res;
//    }
//
//    private static SecretKeySpec getKey() {
////        keyBuffer = keyBuffer.substring(0, 8);
//        SecretKeySpec key = new SecretKeySpec(keyBuffer.getBytes(), "DES");
//        return key;
//    }
//
//    /**
//     * Método que permite desencriptar la contraseña del capacitador
//     *
//     * @param s contraseña a desencriptar
//     * @return contraseña desencriptada
//     * @throws Exception
//     */
//    public static String desencripta(String s) throws Exception {
//        String s1 = null;
//        if (s.indexOf("{DES}") != -1) {
//            String s2 = s.substring("{DES}".length());
//            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//            cipher.init(2, getKey());
//            byte abyte0[] = cipher.doFinal(decode(s2.getBytes()));
//            s1 = new String(abyte0);
//        } else {
//            s1 = s;
//        }
//        return s1;
//    }
    /**
     * Método que permite encriptar la contraseña
     *
     * @param s contraseña a encriptar
     * @return contraseña encriptada
     * @throws Exception
     */
//    public static String encripta(String s) throws Exception {
//        byte abyte0[];
//        SecureRandom securerandom = new SecureRandom();
//        securerandom.nextBytes(keyBuffer.getBytes());
//        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//        cipher.init(1, getKey());
//        abyte0 = encode(cipher.doFinal(s.getBytes())); // antes
//        return new String(abyte0);
//    }
}
