<?php

require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo '<user status="no" msg="magic" />';
    exit;
}

// Process in a function
login($_GET['user'], $_GET['pw']);

/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function login($user, $password) {
    // Connect to the database
    $pdo = pdo_connect();

    $userQ = $pdo->quote($user);
    $query = "SELECT id, password from steampunkeduser where user=$userQ";

    $rows = $pdo->query($query);
    if($row = $rows->fetch()) {
        // We found the record in the database
        // Check the password
        if($row['password'] != $password) {
            echo '<user status="no" msg="password error"/>';
            exit;
        }
        echo '<user status="yes" msg="logged in"/>';
        exit;
    }
    echo '<user status="no" msg="user error"/>';
    exit;
}
?>