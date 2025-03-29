<?php
include 'Usuario.php';

$usuarioDAO = new UsuarioDAO();

$usuario = new Usuario();
$usuario->setNombres('Bugs');
$usuario->setApellidos('Bunny');
$usuario->setCorreo('bugsbunny@wb.com');

$usuario2 = new Usuario();
$usuario2->setNombres('Lola');
$usuario2->setApellidos('Bunny');
$usuario2->setCorreo('lolabunny@wb.com');

$usuarioDAO->insertar($usuario);
$usuarioDAO->insertar($usuario2);

$lucas = new Usuario();
$lucas->setNombres('Daffy');
$lucas->setApellidos('Duck');
$lucas->setCorreo('patolucas@wb.com');
$lucas->guardar();

$porky = new Usuario();
$porky->setNombres('Porky');
$porky->setApellidos('Pig');
$porky->setCorreo('porkypigs@wb.com');
$porky->guardar();


echo '<br>';
$porky->setCorreo('porkypig@wb.com');
$porky->actualizar();
$usuario->eliminar();

$usuarios = $usuarioDAO->buscarTodos();

foreach ($usuarios as $usuario) {
    echo $usuario->getNombres() . ' ' . $usuario->getApellidos() . ' ' . $usuario->getCorreo() . '<br>';
}