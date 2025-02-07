<?php

class Buscaminas {
    private $filas;
    private $columnas;
    private $minas;
    public $posicion_minas; // Arreglo que guarda los valores [i][j] de las minas

    public function __construct($nivel) {
        // Definir las opciones de acuerdo al nivel seleccionado
        switch($nivel) {
            case 'facil':
                $this->filas = 8;
                $this->columnas = 8;
                $this->minas = 10;
                break;
            case 'medio':
                $this->filas = 16;
                $this->columnas = 16;
                $this->minas = 40;
                break;
            case 'dificil':
                $this->filas = 16;
                $this->columnas = 30;
                $this->minas = 99;
                break;
            default:
                $this->filas = 8;
                $this->columnas =8;
                $this->minas = 10;
                break;
        }

        $this->posicion_minas = [];
    }

    public function generarTablero() {
        // Inicializar el tablero
        $tablero = array_fill(0, $this->filas, array_fill(0, $this->columnas, 0));

        // Colocar las minas en posiciones aleatorias
        for($i = 0; $i < $this->minas; $i++) {
            do {
                $fila = rand(0, $this->filas - 1);
                $columna = rand(0, $this->columnas - 1);
            } while ($tablero[$fila][$columna] == -1); // Si ya hay una mina, volver a generar la posicion

            $tablero[$fila][$columna] = -1;
            
            $posicion_mina = ($fila * $this->columnas) + $columna; // Convertir la posicion matriz a una posicion de un array lineal
            array_push($this->posicion_minas, $posicion_mina);

            // Incrementar los valores alrededor de la mina
            for($j = $fila - 1; $j <= $fila + 1; $j++) {
                for($k = $columna - 1; $k <= $columna + 1; $k++) {
                    if($j >= 0 && $j < $this->filas && $k >= 0 && $k < $this->columnas && $tablero[$j][$k] !== -1) {
                        $tablero[$j][$k]++;
                    }
                }
            }
        }

        return $tablero;
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents('php://input'));
    $nivel = $data->nivel;

    $buscaminas = new Buscaminas($nivel);
    $tablero = $buscaminas->generarTablero();
    $minas = $buscaminas->posicion_minas;

    // Guarda el tablero en una sesión para que esté disponible en el siguiente script
    session_start();
    $_SESSION['tablero'] = $tablero;
    $_SESSION['minas'] = $minas;

    echo json_encode([
        'nivel' => $nivel,
        'table' => $tablero,
    ]);
}