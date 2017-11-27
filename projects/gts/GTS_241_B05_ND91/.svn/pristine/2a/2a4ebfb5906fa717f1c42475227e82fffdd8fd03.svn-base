-----------------------------------------------------------------------------------
Name   : Enfora script programming command-line utility
URL    : http://www.geotelematic.com
File   : README.txt
-----------------------------------------------------------------------------------
Copyright(C) 2007-2010 GeoTelematic Solutions, Inc., All Rights Reserved.
This command-line utility is proprietary software and may not be redestributed
without the written consent of GeoTelematic Solutions, Inc.
-----------------------------------------------------------------------------------
(This Enfora device scripting tools is NOT endorsed or supported by Enfora.)
GeoTelematic Solutions, Inc. is not affiliated with Enfora.  This device  
scripting tool is provided with the hope that it may be of use to you.
This software is provided 'AS IS' and without warranty of any kind, and the
GeoTelematic Solutions, Inc. expressly disclaims all warranties, express and 
implied.  By using this software, you agree that its use is at your own risk.
-----------------------------------------------------------------------------------

-----------------------------------------------------------------------------------
Introduction:

The "Enfora.exe" program is a command-line utility that can be used to program 
Enfora GPS tracking devices.  This utility must be run from the command-prompt and
runs on Windows XP and Windows Vista.

The following files are included in this package:
  Enfora.exe    : Enfora script programming command-line utility.
  Script.enfora : Example Enfora script.
  README.txt    : This README file
  
See also "Appendix B" of the "Enfora Device Communication Server Manual" located at
the following link:
  http://www.geotelematic.com/download.html

This command-line utility program has been tested to work with the Enfora Mini-MT,
MT-Gu, and SA-G+, but is believed to work with any available Enfora device.

To start a Windows "Command Prompt", go to "Start", then click "All Programs",
then "Accessories", then click on "Command Prompt".  Then 'cd' to the location
where the "Enfora.exe" program resides.

-----------------------------------------------------------------------------------
Here is a list of the available command-line options:

  -com <comPort>
  This option is required and specifies the local COM port to which the Enfora
  device is attached.  COM port values can range from COM1 to COM99, depending
  on what which COM port you are using and/or which COM# was assigned to your
  USB/COM adapter.  Specifying "-com list" will cause a list of available COM
  ports to be displayed.

  -config <file>
  This option is required when programming a device.  The specified script file
  will be read and the script contents of the file will be sent to your Enfora
  device.

  -io
  If specified, the program will enter a loop querying the state of the GPIO
  (general purpose digital inputs/outputs) on the device.  This can be used for
  testing/debugging Enfora digital inputs.  To exit this mode, you must stop
  the program with Control-C.

  -netip
  If specified, the program will enter a loop querying the network assigned
  IP address. This can be used to determine network connectivity. To exit this 
  mode, you must stop the program with Control-C.

  -reset
  If specified, the program will clear and reset the Enfora device, then exit.

  -imei
  If specified, the program will query the Enfora IMEI#, then exit.

-----------------------------------------------------------------------------------
Example usage:

To list the available COM ports:
  > Enfora -com list
  Checking for valid COM ports ...
    COM1
    COM3
    COM7
    COM12

To program an Enfora device attached to a Windows laptop serial port COM7:
  > Enfora -com COM7 -config Script.enfora
  
(Note that your serial port connection may be another port # other than COM7.
Replace the "COM7" in the above command with the serial port # that your 
PC has assigned to the RS232 port that you are using to attach to the Enfora
device).
  
Look for any errors that may occur during the scripting process.

--------

To query the network connection status of an Enfora device attached to COM9:
  > Enfora -com COM9 -netip
  
(Replace "COM9" with the serial port number/name that your PC has assigned to
the RS232 port you are using).

This command is an important part of making sure the APN setting are valid
for the SIM card from the wireless service provider you are using.  If the
output display IP address similar to the following, then the modem has 
successfully established a connection to Internet:

  NETIP: "192.168.085.033", "192.168.128.025", "192.168.128.026"
  
It is normal for the output to initially display the following:

  NETIP: "0.0.0.0", "0.0.0.0", "0.0.0.0"

Which means that the modem has not yet established a connection to Internet,
but this output should change to displaying normal IP addresses within a few
seconds if the modem is configured properly.  If the output IP addresses
remain "0.0.0.0", then this means that either the modem APN values are set
incorrectly, or that the modem is unable to connect to the wireless service
provider due to an improper antenna connection, or coverage area issue.

-----------------------------------------------------------------------------------
Enfora script file.

This format of the Enfora script files used by this utility are unique to this 
application, and are likely not directly readable by any other scripting tool
which may exist.  Please see the included "Script.enfora" script file for more
information regarding options and available features.

-----------------------------------------------------------------------------------
