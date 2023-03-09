<?php
require_once "db.inc.php";
require_once "steampunked-inc.php";

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo "1111";
    exit;
}

// Process in a function
getRoomState($_GET['user'], $_GET['pw'], $_GET['index']);

/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function getRoomState($user, $password, $index) {
    // Connect to the database
    $pdo = pdo_connect();

    $roomid = getRoom($pdo, $user, $password);

    $query = "SELECT userid1, userid2, size from steampunkedroom where id='$roomid'";

    $rows = $pdo->query($query);
    if($row = $rows->fetch()) {
        // We found the record in the database
        // Check the password

        if ($index == '3')
        {
            $size = $row['size'];
            echo $size;
            exit;
        }
        else if ($index == '1')
        {
            $player = getUserName($pdo, $row['userid1']);
            echo $player;
            exit;
        }
        else
        {
            $player = getUserName($pdo, $row['userid2']);
            echo $player;
            exit;
        }
        echo "33333";
        exit;
    }
    echo "2222";
    exit;
}

function getUserName($pdo, $id) {
    $pdo = pdo_connect();

    $query = "SELECT user from steampunkeduser where id = '$id'";

    $rows = $pdo->query($query);
    if($row = $rows->fetch()) {
        return $row['user'];
    }
}

?>