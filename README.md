# Importante
En el ejercicio usuarios, al correr el index dará un error, y es que se están intentando registrar usuarios sin especificar la columna 'usuario' la cual es NOT NULL.  
Para solucionar el problema opté por modificar la base de datos con el siguiente comando:

```sql
ALTER TABLE usuarios 
MODIFY COLUMN usuario VARCHAR(255) NULL;
