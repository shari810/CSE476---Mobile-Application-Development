<?php
$title = "CSE476 Project 2 Test Page";
//
//  The user and password below are those you used in your
//  android application.
//
//$user = "test";  // verify that your userid is correct
//$password = "qqq"; // change this to use your application password

$user = "test";  // verify that your userid is correct
$password = "q"; // change this to use your application password

//$base_url = "https://webdev.cse.msu.edu/~lizongy1/cse476/project2/"; // verify that this is the correct path to your web site
$base_url = "https://webdev.cse.msu.edu/~lizongy1/cse476/project2/"; // verify that this is the correct path to your web site
$magic = "NechAtHa6RuzeR8x";
$index = "3";
?>
<html>
<head>
    <title><?php echo $title; ?></title>
</head>
<body>
<h1><?php echo $title; ?></h1>

<h2>Gridpipe Save Testing</h2>
<p>Paste the valid xml created to save a hatting in the input box below,
    then click the "Test Save" Button. On the new page that appears use the
    browser "View page source" option to see the results.</p>
<form method="post" target="_blank" action="<?php echo $base_url; ?>steampunked-gridpipesave.php">
    <p>XML: <textarea name="xml"></textarea></p>
    <p><input type="submit" value="Test Save" /></p>
</form>

<h2>Gridpipe Load Testing</h2>
<p>Enter the id for a hatting in the input box below then click the "Test Load" button to test.</p>
<form method="get" target="_blank" action="<?php echo $base_url; ?>steampunked-gridpipeload.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <input type="hidden" name="user" value="<?php echo $user; ?>" />
    <input type="hidden" name="pw" value="<?php echo $password; ?>" />
    <p> <input type="submit" value="Test Load" /></p>
</form>

<h2>Room create Testing</h2>
<p>Enter the id for a hatting in the input box below then click the "Test Load" button to test.</p>
<form method="post" target="_blank" action="<?php echo $base_url; ?>steampunked-createroom.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <p>XML: <textarea name="xml"></textarea></p>
    <p> <input type="submit" value="Test Save" /></p>
</form>

<h2>Room join Testing</h2>
<p>Enter the id for a hatting in the input box below then click the "Test Load" button to test.</p>
<form method="post" target="_blank" action="<?php echo $base_url; ?>steampunked-joinroom.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <p>XML: <textarea name="xml"></textarea></p>
    <p> <input type="submit" value="Test Save" /></p>
</form>

<h2>Room Ready Testing</h2>
<p>Enter the id for a hatting in the input box below then click the "Test Load" button to test.</p>
<form method="get" target="_blank" action="<?php echo $base_url; ?>steampunked-roomstate.php">
    <p>XML: <textarea name="xml"></textarea></p>
    <p> <input type="submit" value="Test Save" /></p>
</form>

<h2>Hatter Catalog Testing</h2>
<form method="get" target="_blank" action="<?php echo $base_url; ?>steampunked-login.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <input type="hidden" name="user" value="<?php echo $user; ?>" />
    <input type="hidden" name="pw" value="<?php echo $password; ?>" />
    <input type="submit" value="Test Catalog" />
</form>

<h2>register Testing</h2>
<form method="post" target="_blank" action="<?php echo $base_url; ?>steampunked-register.php">
    <p>XML: <textarea name="xml"></textarea></p>
    <p><input type="submit" value="Test Save" /></p>
</form>


<hr />
<h2>get info Testing</h2>
<p>Enter the id for a hatting in the input box below then click the "Test Load" button to test. Looking at the results of this
    is different than the other pages. If you View Page Source with the delete you will probably see the status as No and the messages
    as failed. This is because the view source causes sends another request which tries to delete a hatting that has already been deleted. Instead use the "Inspect Element" instead.</p>
<form method="get" target="_blank" action="<?php echo $base_url; ?>steampunked-roominfo.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <input type="hidden" name="user" value="<?php echo $user; ?>" />
    <input type="hidden" name="pw" value="<?php echo $password; ?>" />
    <input type="hidden" name="index" value="<?php echo $index; ?>" />
    <p>Hatter ID: <input type="text" name="id" value="" /></p>
    <p> <input type="submit" value="Test Delete" /></p>
</form>


<hr />
<h2>get turn Testing</h2>
<p>Enter the id for a hatting in the input box below then click the "Test Load" button to test. Looking at the results of this
    is different than the other pages. If you View Page Source with the delete you will probably see the status as No and the messages
    as failed. This is because the view source causes sends another request which tries to delete a hatting that has already been deleted. Instead use the "Inspect Element" instead.</p>
<form method="get" target="_blank" action="<?php echo $base_url; ?>steampunked-turninfo.php">
    <input type="hidden" name="magic" value="<?php echo $magic; ?>" />
    <input type="hidden" name="user" value="<?php echo $user; ?>" />
    <input type="hidden" name="pw" value="<?php echo $password; ?>" />
    <p> <input type="submit" value="Test Delete" /></p>
</form>

</body>
</html>