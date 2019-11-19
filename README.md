
Run CAS SERVER

1) Generate the certificate files in cas-overlay-template-5.3/src/main/resources/etc/cas/thekeystore and cas-overlay-template-5.3/src/main/resources/etc/cas/thekeystore.crt using the below commands

   keytool -genkey -keyalg RSA -alias thekeystore -keystore thekeystore -storepass changeit -validity 360 -keysize 2048

   keytool -export -alias thekeystore -file thekeystore.crt -keystore thekeystore

   keytool -import -alias thekeystore -storepass changeit -file thekeystore.crt -keystore "<JAVA_HOME>\jre\lib\security\cacerts"

   keytool -import -alias thekeystore -storepass changeit -file thekeystore.crt -keystore "<JRE_PATH>\lib\security\cacerts"
 
2) Create table users in database mysql with cas-overlay-template-5.3/src/main/resourcesssodbuser.sql
 
3) build run - to start the cas server

Run Web Applications webapp1, webapp2 and webapp3

webpp1, webapp2 and webapp3 are maven spring boot projects


   