Indoor Location Finder
======================
This is the client and server for an indoor localiser based on wireless (802.11), gyroscope, and linear accelerometer sensors.

The client is an android app that reads sensor data and feeds it to a server.

The server is written using Flask and processes the sensor data and displays it on a web page.

There is also a DropboxWifiAnalyzer android app that is used for sensor testing



Dropbox
-------
This project was created at Dropbox Hack Week 2013 (August 5-9) by
 - Szymon Sidor @nivwusquorum
 - Albert Wang @albertyw
 - Pedro Amaral @pjamaral
 - Austin Collins @austindcollins

Also note that several files have been purged and readded to the repository because they contained private Dropbox data.  The
git history (and therefore the `git log` and `git blame`) may not necessarily be correct.

We took many ideas from a former Hack Week project.  Their code is at
https://github.com/zviadm/locator

Thanks to Dropbox, Szymon, and our hosts for inviting us

Discussion
----------
A large problem with using WiFi to do geolocation is that WiFi signals, at
least in the 2.4 GHz range, easily reflect off of surfaces.  This means WiFi is
capable of things like http://www.kurzweilai.net/wi-fi-signal-used-to-track-moving-humans-even-behind-walls
but it also means it's hard to do geolocation without an accurate map of your building.

Another problem is that the orientation of your antenna (plus design of antenna
and receiver) affects signal levels.  This can conceivably be normalized using
a dictionary of different mobile device models' properties and gyroscope data.

You're welcome to use the code in this Github repository, though note that it
didn't result in very accurate results.  We last worked on combining a map of
known walls against inferred movement to narrow down the list of possible
locations (i.e. a hidden markov model) but didn't finish it.

Precision
---------
This project is not the only way to do geolocation.  If your accuracy
requirements are at least 10 meters, and you know the locations and IP
addresses of routers, you can use traceroute magic to find which router your
device client is connected to.  This will allow you to guess approximately
where your device is.  This has the added benefit of not requiring any special
wifi receiver firmware nor any special permissions so something like Javascript
in a web browser can perform this geolocation.

(If you're outside, you may as well use GPS)
