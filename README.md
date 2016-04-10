# Project-JAR

Project Description
This repository will contain the required procedures and code to setup a torrent box on the raspberry pi with GCM.

## Project Build Instructions

#### Setting up client application with android studio (done on windows 10):

1.	Download Project-JAR as a zip and extract the Client Application folder.
2.	Import the forked client application into android studio and make sure all the dependences for client application are met
3.	Go to https://developers.google.com/mobile/add?platform=android&cntapi=gcm&cnturl=https:%2F%2Fdevelopers.google.com%2Fcloud-messaging%2Fandroid%2Fclient&cntlbl=Continue%20Adding%20GCM%20Support&%3Fconfigured%3Dtrue and generate
4.	Fill in App name: GCMImplementation and android package name: com.example.rooshan.gcmimplementation
5.	Select enable GCM (check box) for my application and hit next where you will be asked to ‘Continue to generate configuration files ->’, click generate and download the google-services.json
6.	Move google-services.json into the root directory of the client application which is located where you saved the extracted Client Application folder/app/ in that directory. For example if your Client Application is in downloads you would put the google-services.json into ~/Download/Client/\ Application.
7.	Next build the application using android studio and run the application (either on your android phone or the build in simulator in android studio).

#### Setting up transmission on the application server (done on a raspberry pi running debian):

1.	Install transmission dependences below with apt-get in on Linux debian based distributions
2.	After transmission has been installed successfully, removed RPC authentication by accessing the settings.json file.
3.	Access the settings.json file found in the directory /var/lib/transmission-daemon/info/settings.json, make sure to use admin privileges or the changes will not write.
4.	After accessing settings.json file, seach for rpc-authentication-required and set that paramemter to false like so: rpc-authentication-required: false.
5.	After that start the transmission daemon: sudo service transmission-daemon start

**NOTE**: When making changes to settings.json, make sure the daemon is stopped “sudo service transmission-daemon stop” or the changes will not save in the settings.json file. And start it again once the changes have been saved “sudo service transmission-daemon start”.


#### Setting up the XMPP application server (done on a raspberry pi running debian):
1.	From the same downloaded Project-JAR folder, extract application server files.
2.	Make sure python and the correct application server dependences are met, they can all be installed with an apt-get on Linux debian based distributions.
3.	Simply run the python script with the following command python gcm_script.py.

## Project use instructions
1. Run the client application in android studio.
2. Run XMPP application server script with python gcm_script.py.
3. Copy paste a magnet link into the appropriate field "Send message: " on the android application. For example: a debian ISO: “magnet:?xt=urn:btih:411ace6e776f8da73619a57f2bed1d084e973b8d&dn=debian-update-8.4.0-arm64-CD-1.iso"
4. Once copied correctly, hit the send button and the python script should pick up the torrent and put it on download.

## Project Dependences
The project dependences are split into the application server (which in this case is the raspberry pi running the torrent box) and client application (the android application that will send the torrent magnet links to the raspberry pi).

#### Application server:

* Python >= 2.7.9
* SimpleJson >= 3.8.2
* xmpp >= 0.4.1
* transmission-remote >= 2.84
* transmission-daemon >= 2.84
* transmission-cli >= 2.84

#### Client Application:
* Andriod Studio >= 1.5.1
* Gradle >= 1.5.0
* gms:google-services >= 8.4.0
* appcompat >= v7:23.1.1
* jdk >= 1.8.0_66
* Compile-SDK-Version = Andriod 6.0 (Marshmallow)  - Android 4.0.3 (Icecream Sandwich)

## Help
#### GCM related issues with generating the google-services.json:
https://developers.google.com/cloud-messaging/android/client

#### Transmission related issues with modifying settings.json file:
https://help.ubuntu.com/community/TransmissionHowTo

https://trac.transmissionbt.com/wiki/ConfigurationParameters

#### XMPP application server issues:
http://www.androiddocs.com/google/gcm/ccs.html
