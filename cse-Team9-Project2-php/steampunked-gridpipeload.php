<?php
require_once "db.inc.php";
require_once "steampunked-inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['magic']) || $_GET['magic'] != "NechAtHa6RuzeR8x") {
    echo '<pipes status="no" msg="magic" />';
    exit;
}

// Process in a function
process($_GET['user'], $_GET['pw']);

/**
 * Process the query
 * @param $id the id to look for
 */
function process($user, $password) {
    // Connect to the database
    $pdo = pdo_connect();

    $roomid = getRoom($pdo, $user, $password);
    $query = "select * from steampunkedgridpipes where player='$roomid'";
    $rows = $pdo->query($query);


    if ($rows->rowCount() == 0) {
        echo "<pipes status=\"no\" msg='image'>\r\n";
    }
    else {
        echo "<pipes status=\"yes\">\r\n";
        foreach($rows as $row ) {
            $connect0 = $row['connect0'];
            $connect1 = $row['connect1'];
            $connect2 = $row['connect2'];
            $connect3 = $row['connect3'];
            $originconnect0 = $row['originconnect0'];
            $originconnect1 = $row['originconnect1'];
            $originconnect2 = $row['originconnect2'];
            $originconnect3 = $row['originconnect3'];
            $x = $row['x'];
            $y = $row['y'];
            $id = $row['id'];
            $player = $row['player'];
            $rotation = $row['rotation'];

            echo "<pipe connect0=\"$connect0\" connect1=\"$connect1\" connect2=\"$connect2\" connect3=\"$connect3\" originconnect0=\"$originconnect0\" originconnect1=\"$originconnect1\" 
                originconnect2=\"$originconnect2\" originconnect3=\"$originconnect3\" x=\"$x\" y=\"$y\" id=\"$id\" rotation=\"$rotation\"/>\r\n";

        }
        echo "</pipes>";
        exit;
    }
    echo "<pipes status=\"no\" msg='image'>\r\n";
    exit;
}