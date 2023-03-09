<?php
require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo '<user status="no" msg="magic" />';
    exit;
}

// Process in a function
getRoomState($_GET['user'], $_GET['pw']);

/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function getRoomState($user, $password) {
    // Connect to the database
    $pdo = pdo_connect();

    $roomid = getRoom($pdo, $user, $password);

    $query = "SELECT currentTurn from steampunkedroom where id='$roomid'";

    $rows = $pdo->query($query);
    if($row = $rows->fetch()) {
        // We found the record in the database
        // Check the password
        if($row['currentTurn'] != '-1') {
            echo '<room status="yes" msg="ready"/>';
            exit;
        }
        echo '<user status="no" msg="waiting for player 2"/>';
        exit;
    }
    echo '<user status="no" msg="error"/>';
    exit;
}
?>