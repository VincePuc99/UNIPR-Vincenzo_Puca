<?php
$servername = "192.168.1.233";
$username = "admin";
$password = "";
$dbname = "hotelbooking";

$conn = new mysqli($servername, $username, $password, $dbname); // Create connection
if ($conn->connect_error) {     // Check connection
    die("Connessione fallita: " . $conn->connect_error);
} 

$checkin = mysqli_real_escape_string($conn, $_POST['checkin']);
$checkout = mysqli_real_escape_string($conn, $_POST['checkout']);
$name = mysqli_real_escape_string($conn, $_POST['name']);
$surname = mysqli_real_escape_string($conn, $_POST['surname']);
$contacts = mysqli_real_escape_string($conn, $_POST['contacts']);
$adultnum = mysqli_real_escape_string($conn, $_POST['adultnum']);
$childnum = mysqli_real_escape_string($conn, $_POST['childnum']);
$servicetype = mysqli_real_escape_string($conn, $_POST['servicetype']);
$notes = mysqli_real_escape_string($conn, $_POST['notes']);

$sql = "INSERT INTO hotelbookingtable (checkin,checkout,name,surname,contacts,adultnum,childnum,servicetype,notes)
VALUES ('$checkin','$checkout','$name','$surname','$contacts','$adultnum','$childnum','$servicetype','$notes')";

if ($conn->query($sql) === TRUE) {
    echo "Record salvato!";
} else {
    echo "Errore: " . $sql . "<br>" . $conn->error;
}
$conn->close();
?>