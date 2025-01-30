<?php
// 0: Piedra, 1: Papel, 2: Tijeras, 3: Lagarto, 4: Spock
$opciones = ['piedra', 'papel', 'tijeras', 'lagarto', 'Spock'];

$matrizResultados = [
    [0, -1, 1, 1, -1],
    [1, 0, -1, -1, 1],
    [-1, 1, 0, 1, -1],
    [-1, 1, -1, 0, 1],
    [1, -1, 1, -1, 0]
];

$mensajesGanador = [
    [2 => 'aplasta a', 4 => 'aplasta a'],
    [0 => 'tapa a', 4 => 'desautoriza a'],
    [1 => 'cortan', 3 => 'decapitan'],
    [1 => 'devora', 4 => 'envenena a'],
    [0 => 'vaporiza', 2 => 'rompe']
];

function argumentosValidos($valor1, $valor2) {
    return is_numeric($valor1) && is_numeric($valor2) && 
        $valor1 >= 0 && $valor1 <= 4 && $valor2 >= 0 && $valor2 <= 4;
}

function juego($jugador1, $jugador2) {
    global $matrizResultados;   // Es necesario usar global para acceder a variables de fuera de la funcion
    global $opciones;
    global $mensajesGanador;

    if($matrizResultados[$jugador1][$jugador2] == 0 ) {
        return "Empate!\n";
    } else if( $matrizResultados[$jugador1][$jugador2] == 1 ) {
        $ganador = $jugador1;   // para php, la variable sigue existiendo fuera del boque else
        $perdedor = $jugador2;
        $numGanador = 1;
    } else {
        $ganador = $jugador2;
        $perdedor = $jugador1;
        $numGanador = 2;
    }

    return ucfirst($opciones[$ganador]) . " {$mensajesGanador[$ganador][$perdedor]} {$opciones[$perdedor]} \nGana el jugador {$numGanador} \n";
}


if(isset($argv[1]) && isset($argv[2])) {
    $jugador1 = intval($argv[1]);
    $jugador2 = intval($argv[2]);

    if(!argumentosValidos($jugador1, $jugador2)) {
        echo 'Por favor, ingresa argumentos válidos.';
        return;
    }

    $resultado = juego($jugador1, $jugador2);
    echo $resultado;
}