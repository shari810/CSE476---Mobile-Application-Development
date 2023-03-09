<?php

require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

//if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
//    echo '<hatter status="no" msg="magic" />';
//    exit;
//}

// Process in a function
process();

/**
 * Process the query
 * @param $id the id to look for
 */
function process() {
    // Connect to the database
    $pdo = pdo_connect();

    $query = "select userid2 from steampunkedroom WHERE userid2 !='-1'";
    $rows = $pdo->query($query);

    if ($rows->rowCount() == 0) {
        echo "<hatter status=\"no\"msg='waiting for player 2'>\r\n";
        exit;
    }
    else {
        echo "<hatter status=\"yes\" msg='player 2 has joined'>\r\n";
        exit;
    }

    echo "</hatter>";
}