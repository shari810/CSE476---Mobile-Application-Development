<?php
function getUser($pdo, $user, $password) {
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
        return $row['id'];
    }
    echo '<user status="no" msg="user error"/>';
    exit;
}

function getRoom($pdo, $user, $password) {
    $userid = getUser($pdo, $user, $password);

    $query = "SELECT id from steampunkedroom where userid1='$userid' or userid2='$userid'";

    $rows = $pdo->query($query);
    if ($row = $rows->fetch()) {
        return $row['id'];
    }
    echo '<room status="no" msg="get room id error">';
    exit;
}
?>