<?php
require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo "no";
    exit;
}

processXml($_GET['user'], $_GET['pw']);

/**
 * Process the XML query
 * @param $xmltext the provided XML
 */
function processXml($user, $password) {

    // Connect to the database
    $pdo = pdo_connect();

    $roomid = getRoom($pdo, $user, $password);
    $query = "SELECT currentTurn from steampunkedroom where id='$roomid'";

    $rows = $pdo->query($query);
    if($row = $rows->fetch()) {
        // We found the record in the database
        // Check the password
        $currentTurn = $row['currentTurn'];
    }

    if ($currentTurn == "1")
        $nextTurn = "2";
    else
        $nextTurn = "1";
    $query = "UPDATE steampunkedroom SET currentTurn='$nextTurn' WHERE id='$roomid'";

    if (!$pdo->query($query)) {
        echo "no";
        exit;
    }

    echo "yes";
    exit;
}
