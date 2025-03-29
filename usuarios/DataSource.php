<?php

class DataSource {
    private $cadenaParaConexion;
    private $conexion;

    public function __construct() {
        try {
            $this->cadenaParaConexion = "mysql:host=127.0.0.1;dbname=prueba";
            $this->conexion = new PDO($this->cadenaParaConexion, "root", "mint");
        } catch (PDOException $e) {
            echo "Error: " . $e->getMessage();
        }
    }

    /**
     * Permite realizar traer un registro de la base de datos
     * 
     * @param string $sql   Sentencia SQL
     * @param array $values Valores para la consulta
     * @return $tabla_datos Devuelve un registro de la base de datos
     */
    public function ejecutarConsulta($sql = "", $values = []) {
        if ($sql != "") {
            $consulta = $this->conexion->prepare($sql);
            $consulta->execute($values);

            return $consulta->fetchAll(PDO::FETCH_ASSOC);
        } else {
            return 0;
        }
    }

    /**
     * Permite obtener un entero de las tablas afectadas,
     * 0 indica que no se realizo ninguna accion
     * 
     * @param string $sql               Sentencia SQL
     * @param array $values             Valores para la consulta
     * @return $numero_tablas_afectadas Devuelve un entero de las tablas afectadas
     */
    public function ejecutarActualizacion ($sql = "", $values = []) {
        if($sql != "") {
            $consulta = $this->conexion->prepare($sql);
            $consulta->execute($values);
            
            return $consulta->rowCount();
        } else {
            return 0;
        }
    }

    public function getConexion() {
        return $this->conexion;
    }
}