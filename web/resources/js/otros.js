/**
 * 
 * obtenerSubCadena
 */
function obtenerCadena(cadena) {
    alert("Un mensaje de prueba");
    var texto = cadena.toString()
    var fin = texto.lastIndexOf("&")
}
/* 
 * Validar letras que ingresa
 */

function validarLetras(e) { // 1
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 8)
        return true; // backspace
    if (tecla == 32)
        return true; // espacio
//    if (tecla == 35)
//        return true; // Inicio
//    if (tecla == 36)
//        return true; // Fin
//    if (tecla == 37)
//        return true; // Flecha izquierda
//    if (tecla == 0)
//        return true; // Felcha Derecha
    if (tecla == 192)
        return true; // letra ñ

    patron = /[a-zA-Z\ñ\Ñ\s\Á\á\É\é\Í\í\Ó\ó\Ú\ú]/; //patron
    te = String.fromCharCode(tecla);
    return patron.test(te); // prueba de patron
    //codigo de letras http://help.adobe.com/es_ES/AS2LCR/Flash_10.0/help.html?content=00000525.html
}

function validarNumeros(e) { // 1
    tecla = (document.all) ? e.keyCode : e.which; // 2
    if (tecla == 8)
        return true; // backspace 
//    if (tecla == 35)
//        return true; // Inicio
//    if (tecla == 36)
//        return true; // Fin
//    if (tecla == 37)
//        return true; // Flecha izquierda
//    if (tecla == 0)
//        return true; // Felcha Derecha
    if (tecla >= 96 && tecla <= 105) {
        return true;
    } //numpad

    patron = /[0-9]/; // patron

    te = String.fromCharCode(tecla);
    return patron.test(te); // prueba
}

function validarDireccion(e){
    tecla = (document.all) ? e.keyCode : e.which; // 2
    if (tecla == 8)
        return true; // backspace 
//    if (tecla == 35)
//        return true; // Inicio
//    if (tecla == 36)
//        return true; // Fin
//    if (tecla == 37)
//        return true; // Flecha izquierda
//    if (tecla == 0)
//        return true; // Felcha Derecha
    if (tecla == 46)
        return true; // .
    if (tecla == 45)
        return true; // -
    if (tecla >= 96 && tecla <= 105) {
        return true;
    } //numpad

    patron = /[0-9a-zA-Z\ñ\Ñ\s\Á\á\É\é\Í\í\Ó\ó\Ú\ú]/; // patron

    te = String.fromCharCode(tecla);
    return patron.test(te); // prueba
}

function validarHistoriaClinica(e){
    tecla = (document.all) ? e.keyCode : e.which; // 2
    if (tecla == 8)
        return true; // backspace 
    if (tecla == 46)
        return true; // .
    if (tecla == 45)
        return true; // -
    if (tecla >= 96 && tecla <= 105) {
        return true;
    } //numpad

    patron = /[0-9a-zA-Z\s]/; // patron

    te = String.fromCharCode(tecla);
    return patron.test(te); // prueba
}

var emailCorrecto = false;

function validarEC(e) { // 1
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 8)
        return true; // backspace
    if (tecla == 64)
        return true; // @
    if (tecla == 46)
        return true; // .
    if (tecla == 45)
        return true; // -
    if (tecla == 95)
        return true; // _

    patron = /[a-z0-9]/; //patron
    te = String.fromCharCode(tecla);
    return patron.test(te); // prueba de patron
    //codigo de letras http://help.adobe.com/es_ES/AS2LCR/Flash_10.0/help.html?content=00000525.html
}

function validarEmail(element) {
    valor = element.value;
    expr = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (expr.test(valor)) {
        emailCorrecto=true;
    } else {        
        alert("La dirección de email es incorrecta.");
    }
}