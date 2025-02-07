<?php
if($_SERVER['REQUEST_METHOD'] === 'GET') {
    session_start();
    $posicion_minas = $_SESSION['minas'];
    
    echo json_encode( $posicion_minas );
}