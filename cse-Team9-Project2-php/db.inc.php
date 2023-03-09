<?php

function pdo_connect() {
    try {
        // Production server
//        $dbhost="mysql:host=mysql-user.cse.msu.edu;dbname=lizongy1";
//        $user = "lizongy1";
//        $password = "A59947781";

        $dbhost="mysql:host=mysql-user.cse.msu.edu;dbname=singhk12";
        $user = "singhk12";
        $password = "password123";


        return new PDO($dbhost, $user, $password);
    } catch(PDOException $e) {
        echo '<user status="no" msg="Unable to select database"/>';
        exit;
    }
}