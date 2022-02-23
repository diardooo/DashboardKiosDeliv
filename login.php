<?php

$server = "localhost";
$usern = "root";
$password = "";
$database = "skripsi";
$koneksi = mysqli_connect($server, $usern, $password, $database);

if(mysqli_connect_errno()){
    echo "Gagal melakukan koneksi dengan server" . mysqli_connect_errno();
}

$email = $_POST["post_email"];
$pass = $_POST["post_password"];

$query = "SELECT * FROM login where email='$email' AND password='$pass'";
$obj_query = mysqli_query($koneksi, $query);
$data = mysqli_fetch_assoc($obj_query);

if($data){
    echo json_encode(
        array(
            'response' => true,
            'payload' => array(
                "nama" => $data["nama"],
                "email" => $data["email"],
                "level_user" => $data["level_user"],
                "foto" => $data["foto"]
            )
        )
    );

} else {
    echo json_encode(
        array(
            'response' => false,
            'payload' => null
        )
    );
}

//setup json preview
header('Content-Type: application/json');