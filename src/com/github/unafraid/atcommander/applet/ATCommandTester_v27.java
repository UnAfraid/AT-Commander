package com.github.unafraid.atcommander.applet;

import com.siemens.icm.io.ATStringConverter;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.applet.Applet;
import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATCommandTester_v27 extends JApplet
{
  public String version = "27";
  public JLabel lblPort;
  public JComboBox comboBox;
  public JLabel lblBaudRatekpbs;
  public JComboBox comboBox_1;
  public JButton btnFindPorts;
  public JButton btnConnect;
  public JButton btnDisconnect;
  public JTextField textField;
  public JButton btnSubmit;
  public String passed_cmd;
  public String msg;
  public int connect_status = 0;
  public int attach_status = 0;
  public int num_profiles_connected = 0;
  public String connected_profile = null;
  public String ring_acknowledged = "FALSE";
  public String phone_number = "";
  public String voice_call_connected = "FALSE";
  public String refresh_bearer_list = "FALSE";
  public String bearer_cid = "";
  public String global_bearer_cid = "";
  public String global_bearer_state = "";
  public String global_bearer_ip = "";
  public String httppara_set = "FAIL";
  public String httpaction_result = "FAIL";
  public String httpread_result = "FAIL";
  public String httpterm_result = "FAIL";
  public String httpinit_response = "FAIL";
  public String ftp_set = "FAIL";
  public String ftp_get_result = "FAIL";
  public String ftp_get_data = "FAIL";
  public String ftp_put_data = "";
  public int ftp_put_data_len = 0;
  public double ftp_blocks = 0.0D;
  public int max_ftp_data_len = 0; public int ftp_start_byte = 0; public int ftp_end_byte = 0; public int data_to_send = 0;
  public String ftp_data_over = "FALSE";
  public String global_model_no = "";
  public String global_manufacturer_name = "";
  public String global_gps_status = "";
  public String tcp_local_ip_address = "";
  public String ciicr_status = "FAIL";
  public String cstt_status = "FAIL";
  public String cip_start_status = "FAIL";
  public String tcp_connection_status = "NONE";
  public String global_at_response = "";
  static final String ALL = "All";
  static final String SIMCOM = "Simcom";
  static final String TELIT = "Telit";
  static final String QUECTEL = "Quectel";
  static final String HUAWEI = "Huawei";
  String[] module_int = { "Simcom", "Telit", "Quectel" };
  String[] module_sim_tel = { "Simcom", "Telit" };
  String[] module_int_simcom = { "Simcom" };
  String[] module_int_telit = { "Telit" };
  String[] module_all = { "All", "Simcom", "Huawei" };

  public String[] port_speed = { "300", "1200", "2400", "9600", "19200", "38400", "57600", "115200", "230400" };
  public String[] port_speed_1 = { "4800", "9600", "19200", "38400", "57600" };
  String scfg_status = "FALSE";
  public int num_socket_profiles = 0;
  public String[] connId = { "", "", "", "", "", "" };
  public String[] pdpId = { "", "", "", "", "", "" };
  public String[] socketState = { "", "", "", "", "", "" };
  public String[] socketDataSent = { "", "", "", "", "", "" };
  public String[] socketDataReceived = { "", "", "", "", "", "" };
  public String[] socketPdpStatus = { "", "", "", "", "", "" };
  public String[] socketPdpId = { "", "", "", "", "", "" };
  public String tcp_mod_sel = "Simcom";
  public String http_mod_sel = "Simcom";
  public String gps_mod_sel = "Simcom";
  public String ftp_mod_sel = "Simcom";
  public String email_mod_sel = "Telit";
  public String socket_pdp_activation_status = "FAIL";
  public String socket_pdp_deactivation_status = "FAIL";
  public String[] socketIds = { "1", "2", "3", "4", "5", "6" };
  public String[] server_addr_type = { "IP", "Domain Name" };
  public String[] protocol = { "TCP", "UDP" };
  public String[] socket_dial_mode = { "Online", "Command" };
  public String telit_ftp_status = "FAIL";
  public String telit_email_status = "FAIL";
  public Long time;
  public String cgact_action = "none";
  public int huawei_ip_init = 0;
  public int huawei_ip_open = 0;
  public String default_apn = "";
  public int gbl_sms_mode = 0;

  public String[] at_command_scripts = { "Get Device Info", 
    "Get Registration Status", 
    "Check SIM Card Status", 
    "Initiate Voice Call", 
    "Initiate Data Call", 
    "Telit - HTTP", 
    "Telit - FTP Get", 
    "Telit - FTP Put", 
    "Telit - GPS", 
    "Telit - TCP", 
    "Simcom - HTTP", 
    "Simcom - FTP Get", 
    "Simcom - FTP Put", 
    "Simcom - TCP", 
    "Simcom - GPS", 
    "Quectel HTTP" };

  public String[] at_command_script_content = { "//Check Modem communciation\nAT\n\n//Get Product name\nATI\n\n//Get manufacturer Info\nAT+GSV\n\n//Get device phone number\nAT+CIMI\n\n//Get the device serial number\nAT+GSN\n", 
    "//Get current operator that network is registered\nAT+COPS?\n\n//Display the Operator names\nAT+COPN\n\n//Get the registration status\nAt+CREG?\n", 
    "//Check the SIM card status. If it returns READY, then further operations can continue. If it returns PIN code, then SIM should be unlocked\nAT+CPIN?\n", 
    "//Get the registration status of the device. If the status is '1', the device is registered and in home network\nAT+CGREG?\n\n//Dial the number. Change the number in the example below\nATD7767788888;\n", 
    "//Get the registration status of the device. If the status is '1', the device is registered and in home network\nAT+CGREG?\n\n//Perform a GPRS Attach\nAT+CGATT=1\n\n//Check the status of attach\nAT+CGATT?\n\n//Set up PDP context. Refer to the service provider for APN info\nAT+CGDCONT=1,\"IP\",\"apn1.globalm2m.net\"\n\n//Activate the PDP context\nAT+CGACT=1,1\n", 
    "//Check registration status\nAT+CREG?\n\n//Get the configuration of the sockets\nAT#SCFG?\n\n//Check if any socket has been activated\nAT#SGACT?\n\n//Activate the socket 1\nAT#SGACT=1,1\n\n//Wait for socket activation\nWAIT=4\n\n//Dial the socket. Port 80 is TCP connection.\nAT#SD=1,0,80,\"www.m2msupport.net\"\n\n//Wait for the CONNECT message\nWAIT=4\n\n//Send the HTTP formatted data\n<cr><lf>GET /m2msupport/http_get_test.php HTTP/1.1<cr><lf>Host:www.m2msupport.net<cr><lf>Connection:keep-alive<cr><lf>", 
    "//Check registration status\nAT+CREG?\n\n//Get the configuration of the sockets\nAT#SCFG?\n\n//Check if any socket has been activated\nAT#SGACT?\n\n//Activate the socket 1\nAT#SGACT=1,1\n\n//Wait till Socket is activated.\nWAIT=5\n\n//Set FTP timeout\nAT#FTPTO=1000\n\n//Open the FTP connection\nAT#FTPOPEN=\"ftp.m2msupport.net\",\"xxxxx\",\"xxxxxx\",0\n\n//Wait for FTP session to open\nWAIT=6\n\n//Set the FTP transfer type\nAT#FTPTYPE=0\n\n//Change directory\nAT#FTPCWD=\"/www/m2msupport\"\n\n//Get the FTP file\nAT#FTPGET=\"ftptest.txt\"\n\n//Wait till FTP get is finished\nWAIT=5\n\n//Close the FTP connection\nAT#FTPCLOSE\n", 
    "//Check registration status\nAT+CREG?\n\n//Get the configuration of the sockets\nAT#SCFG?\n\n//Check if any socket has been activated\nAT#SGACT?\n\n//Activate the socket 1\nAT#SGACT=1,1\n\n//Wait till Socket is activated.\nWAIT=5\n\n//Set FTP timeout\nAT#FTPTO=1000\n\n//Open the FTP connection\nAT#FTPOPEN=\"ftp.m2msupport.net\",\"xxxxx\",\"xxxxxx\",0\n\n//Wait for FTP session to open\nWAIT=6\n\n//Set the FTP transfer type\nAT#FTPTYPE=0\n//Change directory\nAT#FTPCWD=\"/www/m2msupport\"\n\n//Put the FTP file\nAT#FTPPUT=\"test2.txt\"\n\n//Wait for CONNECT response\nWAIT=6\n\n//Send the data\nftpdata is being\n\n//+++ to indicate end of data\n//<!crlf> is to tell the script processor not to send <cr><lf>\n+++<!crlf>\n\n//Wait till FTP get is finished\nWAIT=5\n\n//Close the FTP connection\nAT#FTPCLOSE\n", 
    "//Turn on the GPS\nAT$GPSP=1\n\n//Do a GPS cold reset\nAT$GPSR=1\n\n//Wait for the reset\nWAIT=5\n\n//Get the GPS fix data\nAT$GPSACP\n\n", 
    "//Check registration status\nAT+CREG?\n\n//Get the configuration of the sockets\nAT#SCFG?\n\n//Check if any socket has been activated\nAT#SGACT?\n\n//Activate the socket 1\nAT#SGACT=1,1\n\n//Wait till Socket is activated.\nWAIT=5\n\n//Set up a TCP connection to google.\n//Port 80 is for TCP\nAT#SD=1,0,80,\"www.google.com\",0,0,0\n", 
    "//Check the registration status\nAT+CREG?\n\n//Check whether bearer 1 is open.\nAT+SAPBR=2,1\n\n//Enable bearer 1\nAT+SAPBR=1,1\n\n//Wait untial bearer is activated\nWAIT=6\n\n//Initialize HTTP service\nAT+HTTPINIT\n\n//Set the HTTP URL\nAT+HTTPPARA=\"URL\",\"http://www.m2msupport.net/m2msupport/http_get_test.php\"\n\n//Set the context ID\nAT+HTTPPARA=\"CID\",1\n\n//Set up the HTTP action\nAT+HTTPACTION=0\n\n//Do a HTTP read\nAT+HTTPREAD\n\n//Wait for the HTTP response\nWAIT=6\n\n//Terminate the HTTP service\nAT+HTTPTERM\n\n", 
    "//Get the registration status\nAT+CREG?\n\n//Query the bearer state\nAT+SAPBR=2,1\n\n//Open Bearer 1\nAT+SAPBR=1,1\n\n//Wait for the bearer to be opened\nWAIT=6\n\n//Set the the CID for FTP session\nAT+FTPCID=1\n\n//Set the FTP server name\nAT+FTPSERV=\"ftp.m2msupport.net\"\n\n//Set the FTP user name\nAT+FTPUN=\"xxxxx\"\n\n//Set the FTP password\nAT+FTPPW=\"xxxx\"\n\n//Set the FTP filename to get\nAT+FTPGETNAME=\"ftptest.txt\"\n\n//Set the FTP directory\nAT+FTPGETPATH=\"/www/m2msupport/\"\n\n//Perform a FTP get\nAT+FTPGET=1\n\n//Wait\nWAIT=6\n\n//Get the data\nAT+FTPGET=2,1024\n", 
    "//Checking registration status\nAT+CREG?\n\n//Querying bearer 1\nAT+SAPBR=2,1\n\n//Opening Bearer 1\nAT+SAPBR=1,1\n\n//Setting up FTP parameters\nAT+FTPCID=1\n\n//Set up FTP server\nAT+FTPSERV=\"ftp.m2msupport.net\"\n\n//Set up FTP username\nAT+FTPUN=\"xxxxx\"\n\n//Set up FTP password\nAT+FTPPW=\"xxxxxxx\"\n\n//Set up FTP file to upload\nAT+FTPPUTNAME=\"ftpputtest.txt\"\n\n//Set up FTP directory\nAT+FTPPUTPATH=\"/www/m2msupport/\"\n\n//Start FTP session\nAT+FTPPUT=1\n\n//Wait\nWAIT=5\n\n//Set for 10 bytes to transfer. Change the byte counter accordingly\nAT+FTPPUT=2,10\n\n//Wait for acknowledgement\nWAIT=7\n\n//Send the data\n0123456789\n\n//Wait to send data\nWAIT=5\n\n//Indicate that no more data to send\nAT+FTPPUT=2,0\n", 
    "//Check the registration status\nAT+CREG?\n\n//Check attach status\nAT+CGACT?\n\n//Attach to the network\nAT+CGATT=1\n\n//Wait for Attach\nWAIT=7\n\n//Start task ans set the APN. Check your carrier APN\nAT+CSTT=\"bluevia.movistar.es\"\n\n//Bring up the wireless connection\nAT+CIICR\n\n//Wait for bringup\nWAIT=6\n\n//Get the local IP address\nAT+CIFSR\n\n//Start a TCP connection to remote address. Port 80 is TCP.\nAT+CIPSTART=\"TCP\",\"74.124.194.252\",\"80\"\n", 
    "//Turn GPS on\nAT+CGPSPWR=1\n\n//Reset the GPS in autonomy mode\nAT+CGPSRST=0\n\n//Wait for the GPS reset\nWAIT=15\n\n//Get the current GPS location\nAT+CGPSINF=0\n\n", 
    "//Checking registration status\nAT+CREG?\n\n//Configure the context as foreground\nAT+QIFGCNT=0\n\n//Set the APN\nAT+QICSGP=1,\"bluevia.movistar.es\"\n\n//Disable MUXIP\nAT+QIMUX=0\n\n//Set session mode to non-transparent\nAT+QIMODE=0\n\n//Setting server address is Domain Name\nAT+QIDNSIP=1\n\n//Register the TCP/IP stack\nAT+QIREGAPP\n\n//Activate Foreground context\nAT+QIACT\n\n//Wait for contect activation\nWAIT=5\n\n//Show the received IP address\nAT+qishowra=1\n\n//Get local IP address\nAT+QILOCIP\n\n//Connect to the remote server\nAT+QIOPEN=\"TCP\",\"www.m2msupport.net\",80\n\n//Wait for the CONNECT prompt\nWAIT=5\n\n//Send the data\nAT+QISEND\n\n//Wait for the > prompt\nWAIT=2\n\n//Send the HTTP formatted data\n<cr><lf>GET /m2msupport/http_get_test.php HTTP/1.1<cr><lf>Host:www.m2msupport.net<cr><lf>Connection:keep-alive<cr><lf>\n\n//Wait and sent ctrl+z to complete sending\nWAIT=2\n\n//Send the CtrlZ character\n^z" };

  public String[] at_commands = { "AT", 
    "AT+CACM", 
    "AT+CALA", 
    "AT+CALD", 
    "AT+CALM", 
    "AT+CAOC", 
    "AT+CAPD", 
    "AT+CASIMS", 
    "AT+CAVIMS", 
    "AT+CBC", 
    "AT+CBCAP", 
    "AT+CBCHG", 
    "AT+CBKLT", 
    "AT+CBSD", 
    "AT+CBST", 
    "AT+CCHC", 
    "AT+CCHO", 
    "AT+CCLK", 
    "AT+CCWA", 
    "AT+CDIS", 
    "AT+CEAP", 
    "AT+CECALL", 
    "AT+CEER", 
    "AT+CEN", 
    "AT+CEREG", 
    "AT+CERP", 
    "AT+CESQ", 
    "AT+CFCS", 
    "AT+CFUN", 
    "AT+CGACT", 
    "AT+CGANS", 
    "AT+CGATT", 
    "AT+CGAUTH", 
    "AT+CGAUTO", 
    "AT+CGCLASS", 
    "AT+CGCMOD", 
    "AT+CGCONTRDP", 
    "AT+CGDATA", 
    "AT+CGDCONT", 
    "AT+CGDEL", 
    "AT+CGDSCONT", 
    "AT+CGEQMIN", 
    "AT+CGEQREQ", 
    "AT+CGEREP", 
    "AT+CGLA", 
    "AT+CGMI", 
    "AT+CGMM", 
    "AT+CGMR", 
    "AT+CGPADDR", 
    "AT+CGPIAF", 
    "AT+CGQMIN", 
    "AT+CGQREQ", 
    "AT+CGREG", 
    "AT+CGSCONTRDP", 
    "AT+CGSMS", 
    "AT+CGSN", 
    "AT+CHUP", 
    "AT+CIMI", 
    "AT+CIND", 
    "AT+CIPCA", 
    "AT+CIREG", 
    "AT+CISRVCC", 
    "AT+CISRVCC", 
    "AT+CKPD", 
    "AT+CLAC", 
    "AT+CLAE", 
    "AT+CLAN", 
    "AT+CLCK", 
    "AT+CLIP", 
    "AT+CMAR", 
    "AT+CMEE", 
    "AT+CMOD", 
    "AT+CMOLR", 
    "AT+CMOLRE", 
    "AT+CMUT", 
    "AT+CMUX", 
    "AT+CNAP", 
    "AT+CNMPSD", 
    "AT+CNUM", 
    "AT+COLP", 
    "AT+COPN", 
    "AT+COPS", 
    "AT+CPAS", 
    "AT+CPBF", 
    "AT+CPBR", 
    "AT+CPBW", 
    "AT+CPIN", 
    "AT+CPINR", 
    "AT+CPLS", 
    "AT+CPNET", 
    "AT+CPOL", 
    "AT+CPOS", 
    "AT+CPOSR", 
    "AT+CPSB", 
    "AT+CPWC", 
    "AT+CPWD", 
    "AT+CR", 
    "AT+CRC", 
    "AT+CREG", 
    "AT+CRLA", 
    "AT+CRLP", 
    "AT+CRMC", 
    "AT+CRMP", 
    "AT+CRSL", 
    "AT+CRSM", 
    "AT+CSCC", 
    "AT+CSCON", 
    "AT+CSCS", 
    "AT+CSDF", 
    "AT+CSGT", 
    "AT+CSIL", 
    "AT+CSIM", 
    "AT+CSNS", 
    "AT+CSO", 
    "AT+CSQ", 
    "AT+CSS", 
    "AT+CSTA", 
    "AT+CSTF", 
    "AT+CSUS", 
    "AT+CSVM", 
    "AT+CTFR", 
    "AT+CTSA", 
    "AT+CTZR", 
    "AT+CTZU", 
    "AT+CUAD", 
    "AT+CVHU", 
    "AT+CVIB", 
    "AT+CVMOD", 
    "AT+FCLASS", 
    "AT+HTTPACTION", 
    "AT+HTTPINIT", 
    "AT+HTTPPARA", 
    "AT+HTTPREAD", 
    "AT+HTTPTERM", 
    "AT+IPR", 
    "AT+LVL", 
    "AT+WS46", 
    "ATA", 
    "ATD", 
    "ATDL", 
    "ATH" };

  public String[] at_command_examples = { 
    "Description:\r\nAT command returns OK which implies that the communication between the device and the application has been verified.\r\n\r\nExamples:\r\nAT\r\nOK", 
    "Description:\r\nAT+CACM AT command resets the accumulated call meter value on the SIM.\r\n\r\nExamples:\r\nAT+CACM\r\nOK", 
    "Description:\r\nAT+CALA AT command sets the alarm on the device.\r\n\r\nExamples:\r\nAT+CALA=”00/06/09,07:30”\r\nOK\r\nAT+CALA?\r\n+CALA: “00/06/09,07:30:00”,1", 
    "Description:\r\nAT+CALD AT command deletes alarm on the mobile device.\r\n\r\nExamples:\r\nAT+CALD=1\r\nOK", 
    "Description:\r\nAT+CALM AT command is used to set the alert mode. Possibl1 values are,\r\n\r\n0          normal mode\r\n1          silent mode (all sounds from MT are prevented)\r\n2…      manufacturer specific\r\n\r\nExamples:\r\nAT+CALM=1\r\nOK", 
    "Description:\r\nAT+CAOC AT command enables subscriber to get information about cost of calls.\r\n\r\nExamples:\r\nAT+CAOC=2\r\nOK", 
    "Description:\r\nAT+CAPD postpones or dismisses a current active alarm.\r\n\r\nExamples:\r\nAT+CAPD=2\r\nOK", 
    "Description:\r\nAT+CASIMS AT command sets whether SMS over IMS is available or not.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CAVIMS AT command configures whether voice calls with IMS are available or not.\r\n\r\nExamples:\r\nAT+CAVIMS=1\r\nOK", 
    "Description:\r\nAT+CBC AT command returns the battery status of the device.Possible values are,\r\n0 MT is powered by the battery\r\n1 MT has a battery connected, but is not powered by it\r\n2 MT does not have a battery connected\r\n3 Recognized power fault, calls inhibited\r\n\r\nExamples:\r\nAT+CBC\r\n+CBC: 0,0\r\n\r\nOK\r\nAT+CBC=?\r\n+CBC: (0-3),(0-100)\r\n\r\nOK", 
    "Description:\r\nAT+CBCAP AT commands enables reporting of battery capacity level.\r\n\r\nExamples:\r\nAT+CBCAP=1\r\nOK", 
    "Description:\r\nAT+CBCHG AT command enables reporting upon change in battery charger status\r\n\r\nExamples:\r\nAT+CBCHG=1\r\nOK", 
    "Description:\r\nAT+CBKLT AT command is used to enable or disable the backlight of the device.\r\n\r\nExamples:\r\nAT+CBKLT=1\r\nOK", 
    "Description:\r\nAT+CBSD AT command will identify the boundary between a display area and a non-display area of the ME’s (touch) screen.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CBST selects the bearer service of the data call.\r\nPossible values of connection element are,\r\n0          data circuit asynchronous (UDI or 3.1 kHz modem)\r\n1          data circuit synchronous (UDI or 3.1 kHz modem)\r\n2          PAD Access (asynchronous) (UDI)\r\n3          Packet Access (synchronous) (UDI)\r\n4          data circuit asynchronous (RDI)\r\n5          data circuit synchronous (RDI)\r\n6          PAD Access (asynchronous) (RDI)\r\n7          Packet Access (synchronous) (RDI)\r\n\r\nExamples:\r\nAT+CBST?\r\n+CBST: 7,0,1\r\n\r\nOK\r\nAT+CBST=?\r\n+CBST: (0-7,12,14,65,66,68,70,71,75),(0),(0-3)\r\n\r\nOK", 
    "Description:\r\nAT+CCHC AT command closes the session with UICC.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CCHO At command is used to open a session with UICC so that commmands can be sent over the UICC logical channels.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CCLK AT command sets the clock of the device.\r\n\r\nExamples:\r\nAT+CCLK?\r\n+CCLK: \"09/10/15,19:33:42+00\"", 
    "Description:\r\nAT+CCWA AT command enables the control of call waiting supplementary service.\r\n\r\nExamples:\r\nAT+CCWA=1\r\nOK", 
    "Description:\r\nAT+CDIS AT command command is used to write the contents of MT text type display elements.\r\n\r\nExamples:\r\nAT+CDIS?\r\n+CDIS: \"RADIOLINJA\",\"\",\"\",\"Menu\",\"Memory\"\r\nOK", 
    "Description:\r\nAT+CEAP AT command allows teh device to exchange EAP packets with the UICC.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CECALL initiates eCall to the network.\r\n\r\nExamples:\r\nAT+ecall=2\r\nOK", 
    "Description:\r\nAT+CEER AT command returns error report for,\r\n- the failure in the last unsuccessful call setup (originating or answering) or in call modification;\r\n- the last call release;\r\n- the last unsuccessful GPRS attach or unsuccessful PDP context activation;\r\n- the last GPRS detach or PDP context deactivation.\r\n\r\nExamples:\r\nAT+CEER\r\n+CEER: 0,0,5,16,normal call clearing\r\n\r\nOK", 
    "Description:\r\n\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CEREG AT command returns the EPS registration status.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CERP AT command gets the EAP session parameters after +CEAP command has been executed.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CESQ AT command returns signal quality parameters such as channel bit error rate, received signal code power, ratio of the received energy per PN chip and reference signal received quality.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CFCS  AT command sets the status of the priority level for fast call set-up stored in the SIM.\r\n\r\nPossible values are\r\n0 disable for fast call set-up\r\n1 enable for fast call set-up\r\n\r\nExamples:\r\nAT+CFCS=1\r\nOK", 
    "Description:\r\nAT+CFUN AT command  sets the level of functionality in the MT. Level \"full functionality\" is where the highest level of power is drawn. \"Minimum functionality\" is where minimum power is drawn.\r\nPossible values are,\r\n0 minimum functionality\r\n1 full functionality\r\n2 disable phone transmit RF circuits only\r\n3 disable phone receive RF circuits only\r\n4 disable phone both transmit and receive RF circuits\r\n\r\nExamples:\r\nFind out what is supported by the device\r\nAT+CFUN=?\r\n+CFUN: (0,1,4),(0-1)\r\n\r\nOK\r\nAbove indicates that devices supports minimum functionality ('0'), Full functionality('1') and ability to disable Rx and Tx path ('4')\r\n\r\n// Disable the Tx and Rx Path\r\nAT+CFUN=4\r\n\r\nOK\r\n\r\nGet the current status\r\nAT+CFUN?\r\n+CFUN: 4\r\n\r\nOK", 
    "Description:\r\nAT+CGACT AT command is used to activate ot deactivate the PDP context.\r\n\r\nExamples:\r\nAT+CGACT?\r\n+CGACT:1,0\r\nOK\r\nAT+CGACT=0,1\r\nOK", 
    "Description:\r\nAT+CGANS AT command is used to respond to nework initiated call.\r\n\r\nExamples:\r\nAT+CGANS=1\r\nOK", 
    "Description:\r\nAT+CGATT AT command is used to attach or detach the device to packet domain service.\r\n\r\nExamples:\r\nCheck the status of Packet service attach. '0' implies device is not attached and '1' implies device is attached.\r\nAT+CGATT?\r\n+CGATT:0\r\nOK\r\n\r\nPerform a GPRS Attach. The device should be attached to the GPRS network before a PDP context can be established\r\nAT+CGATT=1\r\nOK\r\n\r\nPerform a GPRS Detach. This is ensure that that the device doesn't lock up any netwrok resources.\r\nAT+CGATT=0\r\nOK", 
    "Description:\r\nAT+CGAUTH AT command sets up PDP context  authentication parameters such as user name, password and authentication protocol(PAP or CHAP)\r\n\r\nExamples:\r\nAT+CGAUTH=1,1,”TEST”,”123”\r\nOK", 
    "Description:\r\nAT+CGAUTO AT command configures the device disables or enables an automatic positive or negative response (auto-answer) to the receipt of a NW-initiated Request PDP Context Activation message from the network in UMTS/GPRS.\r\n\r\nExamples:\r\nAT+CGAUTO=1\r\nOK", 
    "Description:\r\nAT+CGCLASS AT command is used to set the device to operate according to specified GPRS mobile class.\r\n\r\nExamples:\r\nAT+CGCLASS=?\r\n+CGCLASS: (\"A\")\r\nOK", 
    "Description:\r\nAT+CGCMOD AT command is used to modify the PDP context.\r\n\r\nExamples:\r\nAT+CGCMOD=?\r\n+CGCMOD:0,1\r\nOK", 
    "Description:\r\nAT+CGCONTRDP AT command returns active PDP parameters such as APN, IP address, subnet mask, gateway addres, primary and secondary DNS address etc.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CGDATA AT command causes the device to establish a connection with the network. Commands following +CGDATA command in the AT command line shall not be processed by the device.\r\n\r\nExamples:\r\nAT+CGDATA=?\r\n+CGDATA:(\"PPP\")\r\nOK\r\nAT+CGDATA=\"PPP\",1\r\nCONNECT", 
    "Description:\r\nAT+CGDCONT AT commands sets the PDP context parameters such as PDP type (IP, IPV6, PPP, X.25 etc), APN, data compression, header compression etc.\r\n\r\nExamples:\r\nWhat are the supported PDP contexts?\r\nAT+CDGCONT=?\r\n\r\n+CGDCONT: (1-10),(\"IP\"),,,(0-1),(0-1)\r\n+CGDCONT: (1-10),(\"IPV6\"),,,(0-1),(0-1)\r\n\r\nOK\r\n\r\nDefine a PDP Context. First parameter is the Context ID, second is the type of IP connection and third is the APN\r\nAT+CDGCONT=1,\"IP\",\"epc.tmobile.com\"\r\nOK\r\n\r\nGet the list of currently defined PDP Contexts\r\nAT+CDGCONT?\r\n\r\n+CGDCONT: 1,\"IP\",\"epc.tmobile.com\",\"0.0.0.0\",0,0\r\n+CGDCONT: 2,\"IP\",\"isp.cingular\",\"0.0.0.0\",0,0\r\n+CGDCONT: 3,\"IP\",\"\",\"0.0.0.0\",0,0\r\n\r\nOK", 
    "Description:\r\nAT+CGDEL AT command removes the non active PDP context data.\r\n\r\nExamples:\r\nAT+CGDEL=1\r\nOK", 
    "Description:\r\nAT+CGDSCONT AT command sets PDP context parameter values for a secondary PDP connection.\r\n\r\nExamples:\r\nAT+CGDSCONT=2,1\r\nOK", 
    "Description:\r\nAT+CGEQMIN AT command sets the minimum acceptable Quality of Service parameters for UMTS connections.\r\n\r\nExamples:\r\nAT+CGEQMIN?\r\n+CGEQMIN;\r\nOK", 
    "Description:\r\nAT+CGEQREQ AT command configures the UMTS Qualcity of serice parameters such as traffic class, maximum bitrates, guaranteed bitrates, maximum SDU size, SDU error ratio, residual bit error rate, transfer delay etc.\r\n\r\nExamples:\r\nAT+CGQREQ=?\r\n+CGQREQ: \"IP\",(0-3),(0-4),(0-5),(0-9),(0-18,31)\r\n+CGQREQ: \"PPP\",(0-3),(0-4),(0-5),(0-9),(0-18,31)\r\nOK", 
    "Description:\r\nAT+CGEREP AT command enables or disables sending of unsolicited error codes.\r\n\r\nExamples:\r\nAT+CGEREP=?\r\n+CGEREP: (0-2),(0-1)\r\nOK", 
    "Description:\r\nAT+CGLA AT command allows a direct control of the currently selected UICC by a distant application on the TE.\r\n\r\nExamples:\r\n", 
    "Description:\r\nThis AT command returns information about device manufacturer.\r\n\r\nExamples:\r\nAT+CGMI\r\nManufacturer ABC\r\nOK", 
    "Description:\r\nThis AT command returns information about the model of the device.\r\n\r\nExamples:\r\nAT+CGMM\r\n3G HSPA+ Module\r\nOK", 
    "Description:\r\nThis AT command returns the revision information of the mobile termina.\r\n\r\nExamples:\r\nAT+CGMR\r\n3.5.2\r\nOK", 
    "Description:\r\nAT+CGPADDR AT command returns the IP address(es) of the PDP context(s).\r\n\r\nExamples:\r\nAT+CGPADDR =?\r\n+CGPADDR: ( 1)\r\nOK\r\nAT+CGPADDR=1\r\n+CGPADDR:1,\"0.0.0.0\"\r\nOK", 
    "Description:\r\nAT+CGPIAF AT command sets the format to print IPV6 address parameters of other AT commands.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CGQMIN AT command sets the minimum accepted profile for quality of service parameters such as precedence, delay, reliability, peak throughput and mean throughput.\r\n\r\nExamples:\r\nAT+CGQMIN?\r\n+CGQMIN: 1,0,0,0,0,0", 
    "Description:\r\nAT+CGREQ AT command sets the Quality of service parameters of a PDP connection such as delay, reliability, peak throughput, mean throughput etc.\r\n\r\nExamples:\r\nAT+CGQREQ=1,0,0,3,0,0\r\nOK", 
    "Description:\r\nAT+CGREG AT command returns the registration status of the device.\r\n\r\nExamples:\r\nAT+CGREG?\r\n+CGREG: 0,0\r\nOK", 
    "Description:\r\nAT+CGSCONTRDP AT command returns the cid, bearer id information about the secondary PDP connection.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CGSMS AT command specifies the service or service preference that the MT will use to send MO SMS messages.\r\n\r\nExamples:\r\nAT+CGSMS=?\r\n+CGSMS:(0-3)\r\nOK", 
    "Description:\r\nThis command returns the IMEI (International Mobile station Equipment Identity) of the mobile terminal.\r\n\r\nExamples:\r\nAT+GSN\r\n123456789 887\r\nOK", 
    "Description:\r\nAT+CHUP command causes the mobile terminal to hangup the current call.\r\n\r\nExamples:\r\nAT+CHUP\r\nOK", 
    "Description:\r\nThis AT command returns IMSI (International Mobile Subscriber Identity) of the mobile terminal.\r\n\r\nExamples:\r\nAT+CIMI?\r\n123456789\r\nOK", 
    "Description:\r\nAT+CIND AT command sets the values of device indicators. Possibel values are,\r\n\"battchg\" battery charge level (0 5)\r\n\"signal\" signal quality (0 5)\r\n\"service\" service availability (0 1)\r\n\"sounder\" sounder activity (0 1)\r\n\"message\" message received (0 1)\r\n\"call\" call in progress (0 1)\r\n\"vox\" transmit activated by voice activity (0 1)\r\n\"roam\" roaming indicator (0 1)\r\n\"smsfull\" a short message memory storage in the MT has become full and a short message has been rejected (2), has become full (1), or memory locations are available (0); i.e. the range is (0 2)\r\n\"inputstatus\" keypad/touch screen status (0-1)\r\n\r\nExamples:\r\nAT+CIND?\r\n+CIND: 1,0,0,0,0,1,0,0,0,3,1,0,0,0,5\r\nOK", 
    "Description:\r\nAT+CIPCA AT command controls whether an initial PDP context shall be established automatically following an attach procedure when the UE is attached to GERAN or UTRAN RATs.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CIREG AT command returns IMS registration status.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CISRVCC AT command enables reporting of SRVCC handover and d IMSVOPS (IMS Over PS) sessions.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CISRVCC AT command sets the support status of SVRCC feature in the device.\r\n\r\nExamples:\r\nAT+CISRVCC=1\r\nOK", 
    "Description:\r\nAT+CKPD AT command emulates the keystroke on the device.\r\n\r\nExamples:\r\nAT+CKPD=9,0,0\r\nOK", 
    "Description:\r\nAT+CLAC AT command lists all AT commands supported by the mobile.\r\n\r\nExamples:\r\nAT+CLAC?\r\nAT+CACM\r\nAT+CAMM\r\nAT+CAOC\r\nAT+CBC\r\nAT+CBST\r\nAT+CCFC\r\nAT+CCUG\r\nAT+CCWA\r\nAT+CCWE\r\nAT+CEER\r\nAT+CFUN\r\nAT+CGACT\r\nAT+CGANS\r\nAT+CGATT\r\nAT+CGAUTO\r\nAT+CGCLASS\r\nAT+CGDATA\r\nAT+CGDCONT\r\nAT+CGEREP\r\nAT+CGMI\r\nAT+CGMM\r\nAT+CGMR\r\nAT+CGPADDR\r\nAT+CGQMIN\r\nAT+CGQREQ\r\nAT+CGREG\r\nAT+CGSMS", 
    "Description:\r\nAT+CLAE AT command is used to enable/disable unsolicited result code when the language in the device is changed.\r\n\r\nExamples:\r\nAT+CLAE=1\r\nOK", 
    "Description:\r\nAT+CLAN AT comamnd is used to set the language on the device. Possible values are,\r\n\"AUTO\" Read language from SIM-card /UICC. \"Auto\" is not returned by the read-command.\r\n\"sw\" Swedish\r\n\"fi\" Finnish\r\n\"da\" Danish\r\n\"no\" Norwegian\r\n\"de\" German\r\n\"fr\" French\r\n\"es\" Spanish\r\n\"it\" Italian\r\n\"en\" English\r\n\r\nExamples:\r\nAT+CLAN=\"en\"\r\nOK", 
    "Description:\r\nExecute command is used to lock, unlock or interrogate a MT or a network facility. Password is normally needed to do such actions.\r\n\r\nExamples:\r\nAT+CLCK=\"OI\",2\r\n+CLCK: 0,7\r\nOK\r\nAT+CLCK=\"OI\",1,\"1234\"\r\nOK", 
    "Description:\r\nThis command refers to the GSM/UMTS supplementary service CLIP (Calling Line Identification Presentation) that enables a called subscriber to get the calling line identity (CLI) of the calling party when receiving a mobile terminated call. Set command enables or disables the presentation of the CLI at the TE.\r\n\r\nExamples:\r\nAT+CLIP=1\r\nOK", 
    "Description:\r\nAT+CMAR AT command is used to reset the device to default values. Phone lock code required for this command.\r\n\r\nExamples:\r\nAT+CMAR=\"lockcode\"\r\nOK", 
    "Description:\r\nAT+CMEE AT command enable or disable the use of result code\r\n+CME ERROR:  as an indication of an error relating to the functionality of the MT.\r\n\r\nExamples:\r\nAT+CMEE=1\r\nOK", 
    "Description:\r\nSet command selects the call mode of further dialling commands (D) or for next answering command (A). Possible values,\r\n\r\n0    single mode\r\n\r\n1    alternating voice/fax (teleservice 61)\r\n2    alternating voice/data (bearer service 61)\r\n3    voice followed by data (bearer service 81)\r\n\r\nExamples:\r\nAT+CMOD=? +CMOD: (0-3)\r\n\r\nOK", 
    "Description:\r\nAT+CMOLR AT command is used to get the location information from the device. Location information is available through NMEA strings or GAD shapes. Location information can be requested through different methods such as Assisted GPS, Assisted GANSS, basic self location etc.\r\n\r\nExamples:\r\nAT+CMOLR?\r\n+CMOLRN: \"$GPRMC,235947.000,V,0000.0000,N,00000.0000,E,,,041299,,*1D&\"", 
    "Description:\r\nAT+CMOLRE AT command disables or enables the verbose format of unsolicited result code.\r\n\r\nExamples:\r\nAT+CMLORE=1\r\nOK", 
    "Description:\r\nAT+CMUT AT command is used to enable/disbale muting of voice on the device.\r\n\r\nExamples:\r\nAT+CMUT=1\r\nOK", 
    "Description:\r\nThis AT command enable/disable multiplexing protocol control channel. If no parameters are passed, default values are used.\r\n\r\nExamples:\r\nAT+CMUX=?\r\n+CMUX: (1),(0),(1-5),(10-100),(1-255),(0-100),(2-255),(1-255),(1-7)\r\n\r\nOK", 
    "Description:\r\nAT+CNAP AT command enables the called subscriber to get the calling name identificaiton of the calling subscriber.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CNMPSD AT command indicates that no application on the MT is expected to exchange data.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CNUM AT command returns the subscriber phone number.\r\n\r\nExamples:\r\nAT+CNUM\r\n+CNUM: ,\"0123456789\",122\r\n\r\nOK", 
    "Description:\r\nAT+COLP enables the calling subscriber to get the connected line identity (COL) of teh called party.\r\n\r\nExamples:\r\nAT+COPL=1\r\nOK", 
    "Description:\r\nAT+COPN AT command returns the list of operators from the mobile terminal.\r\n\r\nExamples:\r\nAT+COPN\r\n\r\n+COPN: \"20205\",\"vodafone GR\"\r\n+COPN: \"20209\",\"GR Q-TELECOM\"\r\n+COPN: \"20210\",\"TIM GR\"", 
    "Description:\r\nAT+COPS AT command forces the mobile terminal to select and register the GSM/UMTS/EPS network.\r\nPossible values for mode are,\r\n0 automatic ( field is ignored)\r\n1 manual ( field shall be present, and optionally)\r\n2 deregister from network\r\n3 set only (for read command +COPS?), do not attempt registration/deregistration ( and fields are ignored); this value is not applicable in read command response\r\n4 manual/automatic ( field shall be present); if manual selection fails, automatic mode (=0) is entered\r\n\r\nPossible values for access technology,\r\n0 GSM\r\n1 GSM Compact\r\n2 UTRAN\r\n3 GSM w/EGPRS\r\n4 UTRAN w/HSDPA\r\n5 UTRAN w/HSUPA\r\n6 UTRAN w/HSDPA and HSUPA\r\n7 E-UTRAN\r\n\r\nExamples:\r\nAT+COPS=?\r\n+COPS: (2,\"RADIOLINJA\",\"RL\",\"24405\"),(0,\"TELE\",\"TELE\",\"24491\")\r\nOK\r\nAT+COPS?\r\n+COPS: 0,0,\"RADIOLINJA\"\r\nOK\r\nAT+COPS=1,0,\"TELE\"\r\n+CME ERROR: 3", 
    "Description:\r\nAT+CPAS AT command returns of the module device status. Possible values are,\r\n0 ready\r\n1 unavailable\r\n2 unknown\r\n3 ringing\r\n4 call in progress\r\n5 asleep\r\n\r\nExamples:\r\nAT+CPAS\r\n+CPAS: 0\r\n\r\nOK\r\nAT+CPAS=?\r\n+CPAS: (0-5)\r\n\r\nOK", 
    "Description:\r\nAT+CPBF AT command returns phobebook entries from the SIM based on the search parameter.\r\n\r\nExamples:\r\nTo search a phone book with a name,\r\n\r\nAT+CPBF=\"Daniel\"\r\n\r\n+CPBF: 1,\"1391812\",129,\"Daniel\",\"\",0\r\n\r\nOK", 
    "Description:\r\nAT+CPBR AT command returns entries from the device's phonebook.\r\n\r\nExamples:\r\nRead all entries but only the ones set are returned\r\nAT+CPBR=1,99 \r\n+CPBR: 1,\"931123456\",129,\"Ilkka\"\r\n+CPBR: 2,\"9501234567\",129,\"\"\r\n+CPBR: 4,\"901234567\",129,\"Hesari\"\r\nOK\r\n\r\nRead the phone book entry 1\r\nAT+CPBR=1 \r\n+CPBR: 1,\"931123456\",129,\"Ilkka\"", 
    "Description:\r\nAT+CPBW AT command writes an entry in to the SIM's phonebook.\r\n\r\nExamples:\r\nWhat is supported for the phone book write command?\r\nAT+CPBW=?\r\n+CPBW: (1-254),40,(129,145,161,177),20\r\n\r\nOK\r\n\r\nWrite a phonebook entry to the next available location\r\nAT+CPBW=,”6187759088\",129,”Adam”\r\n\r\nOK", 
    "Description:\r\nAT+CPIN AT command sets the password of teh mobile device.\r\n\r\nExamples:\r\nAT+CPIN?\r\n+CPIN:READY\r\nOK", 
    "Description:\r\nAT+CPINR AT command returns the number of remaining PIN retries.\r\n\r\nExamples:\r\nAT+CPINR=\"SIM*\"\r\n+CPINR: SIM PIN,2,4\r\n+CPINR: SIM PUK2,4\r\n+CPINR: SIM PIN22,4\r\n+CPINR: SIM PUK2,2,4", 
    "Description:\r\nAT+CPLS AT command is used to select the preferred PLMN\r\nlist. Possible values are,\r\n\r\n0 User controlled PLMN selector with Access Technology EFPLMNwAcT, if not found in the SIM/UICC then PLMN preferred list EFPLMNsel (this file is only available in SIM card or GSM application selected in UICC)\r\n1 Operator controlled PLMN selector\r\n2 HPLMN selector with Access Technology EFHPLMNwAcT\r\n\r\nExamples:\r\nAT+CPLS=1\r\nOK", 
    "Description:\r\nAT+CPNET AT command is used to set the preferred network. Possible values are,\r\n0 GERAN/UTRAN/E-UTRAN shall be used. The terminal uses GERAN/UTRAN/E-UTRAN coverage only.\r\n1 GAN shall be used. The terminal used GAN coverage only.\r\n2 GERAN/UTRAN/E-UTRAN preferred. The terminal prefers to stay in GERAN/UTRAN/E-UTRAN rather than GAN.\r\n3 GAN preferred. The terminal prefers to stay in GAN rather than GERAN/UTRAN/E-UTRAN.\r\n\r\nExamples:\r\nAT+CPNET=1\r\nOK", 
    "Description:\r\nAT+CPOL AT command is used to set the PLMN selector with Access Technology lists in the SIM card.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CPOS AT command causes the device to enter a transparent mode for sending XML formatted data.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CPOSR AT command enables or disables the sending of unsolicited positioning data in XML format.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CPSB AT command returns the current packet switch bearer type.Possible values are GPRS, EGPRS, Non-HSUPA, HSUPA, EPS\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CPWC AT command is used to set the power class of the device.\r\n\r\nExamples:\r\nAT+CPWC=?\r\n+CPWC: (0,(0,4,5)),(1,(0-2))\r\nOK", 
    "Description:\r\nAT+CPWD AT command sets a new password for the facility lock function.\r\n\r\nExamples:\r\nAT+CPWD=\"P2\",\"OLD\",\"NEW\"\r\nOK", 
    "Description:\r\nAT+CR AT command enables or disables service repoting of the mobile terminal. Set 'mode' to '0' to disable and '1' to enable service reporting.\r\n\r\nExamples:\r\nAT+CR?\r\n+CR: 0\r\nOK\r\nAT+CR=?\r\n+CR: (0,1)\r\nOK", 
    "Description:\r\nSet command controls whether or not the extended format of incoming call indication or GPRS network request for PDP context activation or notification for VBS/VGCS calls is used.\r\n\r\nExamples:\r\nAT+CRC=1\r\nOK\r\n\r\n+CRING: VOICE", 
    "Description:\r\nAT+CREG AT command gives information about the registration status and access technology of the serving cell.\r\n\r\nPossible values of registration status are,\r\n0 not registered, MT is not currently searching a new operator to register to\r\n1 registered, home network\r\n2 not registered, but MT is currently searching a new operator to register to\r\n3 registration denied\r\n4 unknown (e.g. out of GERAN/UTRAN/E-UTRAN coverage)\r\n5 registered, roaming\r\n6 registered for \"SMS only\", home network (applicable only when indicates E-UTRAN)\r\n7 registered for \"SMS only\", roaming (applicable only when indicates E-UTRAN)\r\n8 attached for emergency bearer services only (see NOTE 2) (not applicable)\r\n9 registered for \"CSFB not preferred\", home network (applicable only when indicates E-UTRAN)\r\n10 registered for \"CSFB not preferred\", roaming (applicable only when indicates E-UTRAN)\r\n\r\nPossible values for access technology are,\r\n0 GSM\r\n1 GSM Compact\r\n2 UTRAN\r\n3 GSM w/EGPRS\r\n4 UTRAN w/HSDPA\r\n5 UTRAN w/HSUPA\r\n6 UTRAN w/HSDPA and HSUPA\r\n7 E-UTRAN\r\n\r\nExamples:\r\n//enable +CREG: unsolicited result code\r\nAT+CREG=1\r\nOK\r\n\r\n//MT is registered in home PLMN\r\nAT+CREG?\r\n+CREG: 1,1 \r\nOK\r\n\r\n//Registration is denied\r\nAT+CREG?\r\n+CREG: 3,1 \r\nOK\r\n\r\n//MT is registered in home PLMN\r\nAT+CREG?\r\n+CREG: 1,1 \r\nOK\r\n\r\n//Registered for SMS only\r\nAT+CREG?\r\n+CREG: 6,1 \r\nOK", 
    "Description:\r\nAT+CRLA At command provides limited control of the currently selected UICC by a distant application on the TE.\r\n\r\nExamples:\r\n", 
    "Description:\r\nAT+CRLP sets the Radio Link Parameter (RLP) parameters.\r\n\r\nExamples:\r\nAT+CRLP?\r\n+CRLP: 61,61,48,6\r\nOK\r\nAT+CRLP=?\r\n+CRLP: (0-61),(0-61),(39-255),(1-255)\r\nOK", 
    "Description:\r\nAT+CRMC AT command sets the ring melody and volume.\r\n\r\nExamples:\r\nAT+CRMC=2,3\r\nOK", 
    "Description:\r\nAT+CRMP AT command is used to tell the device playback a specific ring type.\r\n\r\nExamples:\r\nAT+CRMP=1\r\nOK", 
    "Description:\r\nAT+CRSL AT command is used to set the ringer sound level of the device.\r\n\r\nExamples:\r\nAT+CSRL=5\r\nOK", 
    "Description:\r\nAT+CRSM AT comman provides restricted access to the SIM.\r\n\r\nExamples:\r\nAT+CRSM=176,28512,0,0,123\r\nSTR=`AT+CRSM=176,28512,0,0,123'\r\nRSTR=`+CRSM: 148,4,00000000000000000000000000000000000000000000'", 
    "Description:\r\nAT+CSCC AT command is used to enable/disable access to protected commands within the device.\r\n\r\nExamples:\r\nAT+CSCC=1\r\nOK", 
    "Description:\r\nAT+CSCON AT command returns the signalling state of the device. Possible values are,\r\n0 UTRAN URA_PCH state\r\n1 UTRAN Cell_PCH state\r\n2 UTRAN Cell_FACH state\r\n3 UTRAN Cell_DCH state\r\n\r\nExamples:\r\n", 
    "Description:\r\nThis AT command selects the character set of the mobile equipment. Some possible values are \"GSM\", \"HEX\".\"IRA\", \"PCDN\", \"UCS2\",\"UTF-8\" etc.\r\n\r\nExamples:\r\nAT+CSCS=GSM\r\nOK", 
    "Description:\r\nAT+CSDF sets the date format of the device. Possible values are,\r\n\r\n1    DD-MMM-YYYY\r\n\r\n2    DD-MM-YY\r\n\r\n3    MM/DD/YY\r\n\r\n4    DD/MM/YY\r\n\r\n5    DD.MM.YY\r\n\r\n6    YYMMDD\r\n\r\n7    YY-MM-DD\r\n\r\n8-255         Manufacturer specific\r\n\r\nExamples:\r\nAT+CSDF=2\r\nOK", 
    "Description:\r\nAT+CSGT AT command sets the greeting text on the device.\r\n\r\nExamples:\r\nAT+CGST=\"Hello World\",1\r\nOK", 
    "Description:\r\nAT+CSIL AT command enables/disables the silent mode. Possible values are,\r\n\r\n0    Silent mode off\r\n\r\n1    Silent mode on\r\n\r\nExamples:\r\nAT+CSIL=1\r\nOK", 
    "Description:\r\nAT+CSIM AT command sends commands to the SIM on the device.\r\n\r\nExamples:\r\nAT+CSIM=54,\"008800812210.....\" // USIM commands\r\n+CSIM: 4,6985E\r\n\r\nAT+CSIM=46,\"008800801110FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00\" // USIM AUTHENTICATE command in GSM Context. 0xFF bytes are RAND.\r\n+CSIM: 32,\"04EEEEEEEE08FFFFFFFFFFFFFFFF9000\" // Expected response. 0xEE bytes", 
    "Description:\r\nSet command selects the bearer or teleservice to be used when mobile terminated single numbering scheme call is established\r\n\r\nPossible values are,\r\n\r\n0    voice\r\n\r\n1    alternating voice/fax, voice first (TS 61)\r\n\r\n2    fax (TS 62)\r\n\r\n3    alternating voice/data, voice first (BS 61)\r\n\r\n4    data\r\n\r\n5    alternating voice/fax, fax first (TS 61)\r\n\r\n6    alternating voice/data, data first (BS 61)\r\n\r\n7    voice followed by data (BS 81)\r\n\r\nExamples:\r\nAT+CSNS=?\r\n+CSNS: (0-7)\r\n\r\nOK", 
    "Description:\r\nAT+CSO AT command is used to set or read the orientation of the screen.\r\n\r\nExamples:\r\nAT+CSO=0\r\nOK", 
    "Description:\r\nAT+CSQ AT command returns the signal strength of the device.\r\nPossible values are,\r\n0 113 dBm or less\r\n1 111 dBm\r\n2...30 109... 53 dBm\r\n31 51 dBm or greater\r\n99 not known or not detectable\r\n\r\nExamples:\r\nAT+CSQ?\r\n+CSQ:18,99\r\n\r\nAT+CSQ\r\n\r\n+CSQ: 4,0\r\n\r\nOK\r\n", 
    "Description:\r\nAT+CSS AT command will get the size (in pixels) of teh device's screen.\r\n\r\nExamples:\r\n", 
    "Description:\r\nSet command selects the type of number for further dialling commands (D) according to GSM/UMTS specifications.\r\n\r\nExamples:\r\nAT+CSTA?\r\n+CSTA: 129\r\nOK\r\nAT+CSTA=?\r\n+CSTA: (129,145)\r\nOK", 
    "Description:\r\nAT+CSTF sets the time format. Possible values are,\r\n\r\n: integer type\r\n\r\n1    HH:MM (24 hour clock)\r\n\r\n2    HH:MM a.m./p.m.\r\n\r\n3-7 Manufacturer specific\r\n\r\nExamples:\r\nAT+CSTF=1\r\nOK", 
    "Description:\r\nAT+CSUS AT command selects the SIM/UICC slot.\r\n\r\nExamples:\r\nAT+CSUS=1\r\nOK", 
    "Description:\r\nAT+CSVM AT command is used to set the number to the voicemail server.\r\n\r\nExamples:\r\nAT+CSVM?\r\n+CSVM:1,\"660\",129\r\nOK", 
    "Description:\r\nAT+CFTR At command causes the incoming calls to be forwarded to a specified number.\r\n\r\nExamples:\r\nAT+CFTR=\"1234567890\"\r\nOK", 
    "Description:\r\nAT+CTSA AT command is used to emulate a touch screen action on the device.\r\n\r\nExamples:\r\nAT+CTSA=1,25,45\r\nOK", 
    "Description:\r\nAT+CTZR AT command enables reporting of timezone changes.\r\n\r\nExamples:\r\nAT+CTZR=1\r\nOK", 
    "Description:\r\nAT+CTZU AT command enable/disable automatic timezone update on the device.\r\n\r\nExamples:\r\nAT+CTZU=1\r\nOK", 
    "Description:\r\nAT+CUAD AT command is used to get the list applications that are available on the UICC.\r\n\r\nExamples:\r\n", 
    "Description:\r\nSet command selects whether ATH or \"drop DTR\" shall cause a voice connection to be disconnected or not.\r\n\r\nExamples:\r\nAT+CVHU=2\r\nOK", 
    "Description:\r\nAT+CVIB AT command is used to enable/disable the vibrator feature of the device.\r\n\r\nExamples:\r\nAT+CVIB=1\r\nOK", 
    "Description:\r\nThis AT command sets the mode for mobile originated voice calls.\r\n\r\nPossible values are,\r\n\r\nCS_ONLY\r\n\r\nVOIP_ONLY\r\n\r\nCS_PREFERRED\r\n\r\nVOIP_PREFERRED\r\n\r\nExamples:\r\nAT+CVMOD=0\r\nOK", 
    "Description:\r\nAT+FCLASS AT command sets the device for different modes. Possible values are,\r\n0 data\r\n1 fax class 1 (TIA 578 A)\r\n1.0 fax class 1 (ITU T Recommendation T.31 [11])\r\n2 fax (manufacturer specific)\r\n2.0 fax class 2 (ITU T Recommendation T.32 [12] and TIA 592)\r\n3...7 reserved for other fax modes\r\n8 voice\r\n9...15 reserved for other voice modes\r\n16..79 reserved\r\n80 VoiceView (Radish)\r\n81..255 reserved\r\n\r\nExamples:\r\nAT+FCLASS=?\r\n0,1,2,2.0", 
    "Description:\r\nAT+HTTPACTION AT Command is used perform HTTP actions such HTTP GET or HTTP post. AT+HTTPACTION is a proprietary Simcom AT command.\r\nThe format for AT+HTTPACTION is,\r\nAT+HTTPACTION=Method,StatusCode,DataLen\r\n\r\nFor Method, possible values are,\r\n0:READ\r\n1:POST\r\n2:HEAD\r\n\r\nExamples:\r\nAT+HTTPACTION=0\r\nOK", 
    "Description:\r\nAT+HTTPINIT AT command initializes the HTTP service. This is a proprietary Simcom AT command.This command should be sent first before starting HTTP service.\r\n\r\nExamples:\r\nAT+HTTPINIT\r\nOK", 
    "Description:\r\nAT+HTTPPARA AT command sets up HTTP parameters for the HTTP call. This is a proprietary AT command from SIMCOM.\r\n\r\nThe format is,\r\nAT+HTTPPARA=,\r\n\r\nThe parameters that can be set with AT+HTTPPARA AT command are,\r\nCID, URL, proxy server, port of HTTP proxy server, conetne\r\n\r\nExamples:\r\nAT+HTTPPARA=\"CID\",1\r\nOK\r\nAT+HTTPPARA=\"URL\",%22http://www.cnn.com/%22\r\nOK\r\nAT+HTTPPARA=\"BREAK\",2000\r\nOK", 
    "Description:\r\nAT+HTTPREAD AT command is used to read the HTTP server response. Prior to this AT command, AT+HTTPACTION=0 ot AT+HTTPDATA should be sent. AT+HTTPREAD is a Simcom proprietary AT command.\r\n\r\nThe format for AT+HTTPREAD is,\r\nAT+HTTPREAD=start_address,byte_size\r\n\r\nExamples:\r\nAT+HTTPREAD\r\nOK", 
    "Description:\r\nAT+HTTPTERM terminates the HTTP session.\r\n\r\nExamples:\r\nAT+HTTPTERM\r\nOK", 
    "Description:\r\nAT+IPR sets the baud rate of the serial interface of the device. Although many manufacturers support this AT command, the implementation is manufacturer specific. \r\n\r\nSIMCOM's supports settign values as follows,\r\n0 - Auto-bauding\r\nOther possible values are 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200\r\n\r\nExamples:\r\nAT+IPR=0\r\nOK\r\n\r\nAT+IPR=115200\r\nOK", 
    "Description:\r\nAT+LVL AT command sets the loudspeaker volume of the device.\r\n\r\nExamples:\r\nAT+CLVL=4\r\nOK", 
    "Description:\r\nPCCA STD-101 command to select the cellular network (Wireless Data Service : WDS). Possible values are,\r\n\r\n12  GSM Digital Cellular Systems (GERAN only)\r\n\r\n22  UTRAN only\r\n\r\n25  3GPP Systems (GERAN, UTRAN and E-UTRAN)\r\n\r\n28  E-UTRAN only\r\n\r\n29  GERAN and UTRAN\r\n\r\n30  GERAN and E-UTRAN\r\n\r\n31  UTRAN and E-UTRAN\r\n\r\nExamples:\r\nAT+WS46=?\r\n+WS46: (12)\r\n\r\nOK", 
    "Description:\r\nATA command answers an incoming voice call.\r\n\r\nExamples:\r\nRING\r\nRING\r\n\r\nATA\r\nOK", 
    "Description:\r\nATD AT command dials the phone number. Generally this AT command is used for dialling the voice call.\r\n\r\nWhen the call fails, the following responses can be received from the device,\r\n1. Connection Failure - NO CARRIER or BUSY or NO ANSWER\r\n2. General Failure - ERROR\r\n3. Security reason (such as SIM not present) - OPERATION NOT\r\nALLOWED\r\n4. Unknown reason - UNKNOWN CALLING ERROR\r\n\r\nExamples:\r\nATD1234568876;\r\nOK", 
    "Description:\r\nATDL AT command dials the last dialled number.\r\n\r\nExamples:\r\nATDL\r\nOK", 
    "Description:\r\nATH AT command hangs up the call. This command is used to end the voice call.\r\n\r\nExamples:\r\nATH\r\nNO CARRIER\r\nOK" };
  public TwoWaySerialComm2 mySerial1;
  public JSObject win;
  public JPanel panel;
  public JPanel panel_1;
  public JScrollBar scrollBar;
  public JScrollBar scrollBar_1;
  public int winflag = 1;
  public JTextPane textArea_1;
  public JComboBox comboBox_2;
  public JLabel lblSelectAtCommand;
  public JTextPane textArea_2;
  public JSeparator separator_1;
  public JTabbedPane tabbedPane_1;
  public JTabbedPane tabbedPane;
  public JPanel panel_2;
  public JLabel lblSelectScript;
  public JComboBox comboBox_3;
  public JTextPane textArea_3;
  public JButton btnRunScript;
  public JButton btnClear_1;
  public JPanel panel_3;
  public JButton btnDeviceInfo;
  public JButton btnSignalStrength;
  public JButton btnRegistrationStatus;
  public JButton btnOperatorInfo;
  public JButton btnSimStatus;
  public JButton btnTechnologyCapability;
  public JButton btnDeviceCapability;
  public JButton btnAvaialbleNetworks;
  public JButton btnDateTime;
  public JButton btnAudioSettings;
  public JButton btnClear_2;
  public JTextField txtAtCommandTester;
  public JPanel panel_4;
  public JButton btnSaveScript;
  public JButton btnLoadScript;
  public JButton btnSaveLog;
  private JTextField filename = new JTextField(); private JTextField dir = new JTextField();
  public OpenL fileOpener = new OpenL();
  public SaveL fileSaver = new SaveL();
  public JButton btnSaveLog_1;
  public JButton btnSaveLog_2;
  public JPanel panel_5;
  public JPanel panel_6;
  public JPanel panel_7;
  public JPanel panel_8;
  public JPanel panel_9;
  public JPanel panel_10;
  public JPanel panel_11;
  public JPanel panel_12;
  public JLabel lblSuggestionsquestions;
  public JList list;
  public JTable table;
  public JButton btnGetPdpContexts;
  public JButton btnConnect_1;
  public int reg_status;
  public JButton btnPdpDisconnect;
  public JButton btnPdpEdit;
  public JButton btnPdpAdd;
  public JLabel lblCallNumber;
  public JTextField textField_1;
  public JButton btnDial;
  public JPanel panel_13;
  public JLabel lblPhoneNumber;
  public JTextField textField_2;
  public JButton btnShowPhoneNumber;
  public JPanel panel_14;
  public JTextPane txtrAtCommandTester;
  public JPanel panel_15;
  public JButton btnHangUp;
  public JButton btnHangUp_1;
  public JLabel lblPhoneNumber_1;
  public JTextField textField_3;
  public JLabel lblMessage;
  public JTextPane textArea_9;
  public JScrollPane scrollPane_1;
  public JButton btnSendSms;
  public JButton btnShowPdu;
  public JButton btnShowTextFormat;
  public JTable table_1;
  public JPanel panel_16;
  public JButton btnSaveLog_3;
  public JButton btnClear_3;
  public JButton btnSaveLog_4;
  public JButton btnClear_4;
  public JButton btnSaveLog_5;
  public JButton btnClear_5;
  public JPanel panel_17;
  public JTable table_2;
  public JButton btnFindNetworks;
  public JButton btnSelectNetwork;
  public JButton btnSaveLog_6;
  public JButton btnClear_6;
  public JPanel panel_18;
  public JTable table_3;
  public JButton btnReadPhonebook;
  public JButton btnAddEntry;
  public JButton btnDeleteEntry;
  public JButton btnEditEntry;
  public JPanel panel_19;
  public JPanel panel_20;
  public JPanel panel_21;
  public JPanel panel_22;
  public JTable table_4;
  public JButton btnGetBearerProfiles;
  public JButton btnEdit;
  public JButton btnOpen;
  public JButton btnClose;
  public JButton btnQuery;
  public JLabel lblBearerCid;
  public JLabel lblUrl;
  public JTextField textField_5;
  public JComboBox comboBox_4;
  public JTextPane textArea_13;
  public JScrollPane scrollPane_9;
  public JButton btnGet;
  public JButton btnPost;
  public JTextPane textPane;
  public JScrollPane scrollPane_10;
  public JLabel lblFtpServerAddress;
  public JLabel lblUserName;
  public JLabel lblPassword;
  public JTextField textField_4;
  public JTextField textField_6;
  public JTextField textField_7;
  public JLabel lblBearerId;
  public JComboBox comboBox_5;
  public JTextArea textArea_14;
  public JScrollPane scrollPane_12;
  public JButton btnFtpGet;
  public JLabel lblFileName;
  public JTextField textField_8;
  public JLabel lblDirectory;
  public JTextField textField_9;
  public JButton btnFtpPut;
  public JLabel lblDeviceName;
  public JLabel lblManufacturer;
  public JLabel lblStatus;
  public JPanel panel_23;
  public JLabel lblNotAvailable;
  public JLabel lblNotAvailable_1;
  public JLabel lblNotConnected;
  public JPanel panel_24;
  public JButton btnGpsOn;
  public JButton btnGpsOff;
  public JButton btnGpsReset;
  public JButton btnGpsReset_1;
  public JButton btnGpsStatus;
  public JButton btnGpsNmeaOn;
  public JButton btnGpsNmeaOff;
  public JLabel lblNmeaPortSpeed;
  public JComboBox comboBox_6;
  public JButton btnSet;
  public JPanel panel_25;
  public JTextField textField_10;
  public JLabel lblLatitude;
  public JLabel lblLongitude;
  public JLabel lblAltitude;
  public JLabel lblTimeToFirst;
  public JLabel lblNumSatellites;
  public JLabel lblSpeed;
  public JTextField textField_11;
  public JLabel lblCourse;
  public JTextField textField_12;
  public JTextField textField_13;
  public JTextField textField_14;
  public JTextField textField_15;
  public JTextField textField_16;
  public JButton btnNewButton;
  public JButton btnShowMap;
  public JPanel panel_26;
  public JTextField textField_17;
  public JLabel lblTime;
  public JTextField textField_18;
  public JPanel panel_27;
  public JLabel lblServerIp;
  public JTextField txtWwwmmsupportnet;
  public JLabel lblPort_1;
  public JTextField textField_20;
  public JButton btnConnect_2;
  public JButton btnDisconnect_1;
  public JPanel panel_28;
  public JTextArea txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp;
  public JScrollPane scrollPane_15;
  public JLabel lblClientData;
  public JButton btnSend;
  public JRadioButton rdbtnTcp;
  public JRadioButton rdbtnUdp;
  public JPanel panel_29;
  public JLabel lblApn;
  public JTextField textField_21;
  public JTextPane textPane_4;
  public URLLabel lblHowToUse;
  public JButton btnPostAQuestion;
  public JPanel panel_33;
  public JComboBox comboBox_8;
  public JPanel panel_34;
  public JPanel panel_30;
  public URLLabel module_select_label;
  public JPanel panel_31;
  public JComboBox comboBox_7;
  public JPanel panel_32;
  public URLLabel http_select_label;
  public JPanel panel_35;
  public JComboBox comboBox_9;
  public JPanel panel_36;
  public URLLabel lblFtpTestingFor;
  public JPanel panel_37;
  public JComboBox comboBox_10;
  public URLLabel lblGpsTestingFor;
  public JPanel panel_38;
  public JPanel panel_39;
  public JLabel label_1;
  public JTextField textField_22;
  public JButton button;
  public JButton button_1;
  public JLabel lblNewLabel;
  public JTable table_5;
  public JButton btnRefresh;
  public JButton btnActivate;
  public JButton btnDeactivate;
  public JLabel lblConnid;
  public JLabel lblProtocol;
  public JLabel lblPort_2;
  public JLabel lblIpAddress;
  public JList list_1;
  public JComboBox comboBox_11;
  public JComboBox comboBox_12;
  public JTextField txtWwwgooglecom;
  public JTextField textField_23;
  public JButton btnOpen_1;
  public JButton btnNewButton_1;
  public JLabel lblMode;
  public JComboBox comboBox_13;
  public JLabel lblConnectionId;
  public JComboBox comboBox_14;
  public JPanel panel_40;
  public JLabel label_2;
  public JLabel label_3;
  public JLabel label_4;
  public JTextField textField_19;
  public JTextField textField_24;
  public JTextField textField_25;
  public JLabel lblCid;
  public JComboBox comboBox_15;
  public JButton button_2;
  public JLabel label_6;
  public JTextField textField_26;
  public JLabel label_7;
  public JTextField textField_27;
  public JButton button_3;
  public JPanel panel_41;
  public JPanel panel_42;
  public JButton button_4;
  public JButton button_5;
  public JButton button_6;
  public JButton btnGpsReset_3;
  public JButton button_9;
  public JButton button_10;
  public JLabel label_8;
  public JComboBox comboBox_16;
  public JButton button_11;
  public JPanel panel_43;
  public JTextField textField_29;
  public JLabel label_9;
  public JLabel label_10;
  public JLabel label_11;
  public JLabel label_13;
  public JLabel label_14;
  public JTextField textField_30;
  public JLabel label_15;
  public JTextField textField_31;
  public JTextField textField_33;
  public JTextField textField_34;
  public JTextField textField_35;
  public JButton button_12;
  public JButton button_13;
  public JLabel label_16;
  public JTextField textField_36;
  public JButton btnGpsReset_2;
  public JButton btnGpsReset_4;
  public JTextArea textArea_15;
  public JScrollPane scrollPane_20;
  public JPanel panel_44;
  public JComboBox comboBox_17;
  public JLabel lblEmailTestingFor;
  public JPanel panel_45;
  public JPanel panel_46;
  public JLabel lblNewLabel_1;
  public JTextField textField_28;
  public JLabel lblUserName_1;
  public JTextField textField_32;
  public JLabel lblPassword_1;
  public JTextField textField_37;
  public JLabel lblSenderEmail;
  public JTextField textField_38;
  public JLabel lblMesssage;
  public JTextArea textArea_16;
  public JScrollPane scrollPane_22;
  public JButton btnSend_1;
  public JLabel lblCid_1;
  public JComboBox comboBox_18;
  public JLabel lblReceiverEmail;
  public JTextField textField_39;
  public JLabel lblEmailSubject;
  public JTextField textField_40;
  public JTextPane textPane_10;
  public JScrollPane scrollPane_24;
  public JLabel lblPdpContexts;
  public JButton btnClear;
  public JButton btnSaveLog_7;
  public JPanel panel_47;
  public JPanel panel_48;
  public JLabel label_5;
  public JTextField textField_41;
  public JLabel label_12;
  public JTextField textField_42;
  public JButton button_7;
  public JButton button_8;
  public JPanel panel_49;
  public JRadioButton radioButton;
  public JRadioButton radioButton_1;
  public JLabel label_17;
  public JTextField textField_43;
  public JLabel label_18;
  public JButton button_14;
  public JLabel lblServerAddressType;
  public JComboBox comboBox_19;
  public JTextArea txtpnGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp;
  public JScrollPane scrollPane_2;
  public JPanel panel_50;
  public JLabel label_19;
  public JTextField textField_44;
  public JButton button_15;
  public JButton button_16;
  public JLabel lblApnForTcp;
  public JTextField textField_45;
  public JButton btnAutofind;
  public JLabel lblGetDeviceInfo;
  public JLabel lblSignalStrength;
  public JLabel lblRegistrationStatus;
  public JLabel lblOperatorInfo;
  public JLabel lblSimStatus;
  public JLabel lblAvailableNetworks;
  public JLabel lblTechnology;
  public JLabel lblDeviceCapability;
  public JLabel lblActivityStatus;
  public JButton button_17;
  public JButton button_18;
  public JLabel lblSmsFormat;
  public JLabel lblSmsScAddress;
  public JButton button_20;
  public JLabel lblPreferredOperator;
  public JLabel lblBatteryLevel;
  public JButton button_22;
  public JLabel lblSerialPortSpeed;
  public JButton button_23;
  public JLabel lblNewLabel_2;
  public JButton button_24;
  public JLabel lblErrorReporting;
  public JLabel lblConfiguration;
  public JButton button_27;
  public JLabel lblIndicators;
  public JButton button_28;
  public JLabel lblStationClass;
  public JButton button_29;
  public JLabel lblPdpContexts_1;
  public JButton button_30;
  public JLabel lblQos;
  public JButton button_31;
  public JLabel lblAttachStatus;
  public JButton button_32;
  public JLabel lblPdpAddress;
  public JButton button_33;
  public JLabel lblActivationStatus;
  public JButton button_34;
  public JLabel lblReset;
  public JButton button_35;
  public JLabel lblFlowControl;
  public JButton button_36;
  public JLabel lblClock;
  public JButton button_37;
  public JLabel lblSimIccid;
  public JButton button_38;
  public JLabel lblMultiplexMode;
  public JButton button_39;
  public JLabel lblBearerType;
  public JButton button_40;
  public JLabel lblRlp;
  public JButton button_41;
  public JLabel lblSerReporting;
  public JButton button_42;
  public JLabel lblExtError;
  public JButton button_43;
  public JLabel lblPhoneNumber_2;
  public JButton button_44;
  public JLabel lblFacilityLock_1;
  public JButton button_45;
  public JLabel lblCallForwarding;
  public JLabel lblCallWaiting;
  public JButton button_47;
  public JLabel lblCallHolding;
  public JButton button_48;
  public JLabel lblPhonebook;
  public JButton button_49;
  public JLabel lblAlarms;
  public JButton button_50;
  public JLabel lblCallMeterMax;
  public JButton button_51;
  public JLabel lblPricePerUnit;
  public JButton button_52;
  public JLabel lblAtCommands;
  public JButton button_53;
  public JLabel lblCallMeter;
  public JLabel lblServiceClass;
  public JButton button_55;
  public JLabel lblMuteControl;
  public JLabel lblUssd;
  public JButton button_57;
  public JLabel lblSmsRead;
  public JButton button_58;
  public JLabel lblEchoMode;
  public JButton button_59;
  public JLabel lblCharacterSet;
  public JButton button_60;
  public JLabel lblAddressType;
  public JButton button_61;
  public JLabel lblDeviceInfo;
  public JLabel lblSms;
  public JButton button_25;
  public JLabel lblConnection;
  public JLabel lblNetwork;
  public JLabel lblSim;
  public JButton button_19;
  public JLabel lblMisc;
  public JButton button_54;
  public JCheckBox chckbxApppendOutput;
  public JPanel panel_51;
  public JPanel panel_52;
  public JButton btnDataConnect;
  public JButton btnDisconnect_2;
  public JButton btnShowIpAddress;
  public JLabel lblManufacturer_1;
  public JComboBox comboBox_20;
  public JPanel panel_53;
  public JLabel lblGeneral;
  public JLabel lblMicGain;
  public JLabel lblReadAdc;
  public JLabel lblHeadsetControl;
  public JLabel lblSimInserted;
  public JLabel lblProvider;
  public JLabel lblVoicemailNum;
  public JLabel lblOprBand;
  public JLabel lblHandsFree;
  public JLabel lblEnggMode;
  public JLabel lblIccid;
  public JLabel lblTemperature;
  public JLabel lblMultislot;
  public JLabel lblCallReady;
  public JLabel lblVoiceCoding;
  public JLabel lblEmergencyNos;
  public JLabel lblBuzszerMode;
  public JLabel lblMultiip;
  public JLabel lblLocalPort;
  public JLabel lblTaskInfo;
  public JLabel lblLocalIp;
  public JLabel lblConnStatus;
  public JLabel lblDns;
  public JLabel lblIpHeader;
  public JLabel lblServerMode;
  public JLabel lblConnMode;
  public JLabel lblTcpipMode;
  public JLabel lblTransMode;
  public JLabel lblTcpParm;
  public JLabel lblHttpPara;
  public JLabel lblHttpContext;
  public JLabel lblFtpPort;
  public JLabel lblFtpMode;
  public JLabel lblTranfType;
  public JLabel lblPutType;
  public JLabel lblFtpServer;
  public JLabel lblFtpUserName;
  public JLabel lblFtpPassword;
  public JLabel lblFtpFileNames;
  public JLabel lblFtpPath;
  public JLabel lblFtpContext;
  public JLabel lblFtpState;
  public JLabel lblGps;
  public JLabel lblNewLabel_3;
  public JLabel lblGpsResetMode;
  public JLabel lblGpsLocation;
  public JLabel lblGpsStatus;
  public JLabel lblTcpip;
  public JSeparator separator_2;
  public JLabel lblHttp;
  public JLabel lblFtp;
  public JLabel lblAudio;
  public JButton button_21;
  public JButton button_26;
  public JButton button_46;
  public JButton button_56;
  public JButton button_62;
  public JButton button_63;
  public JButton button_64;
  public JButton button_65;
  public JButton button_66;
  public JButton button_67;
  public JButton button_68;
  public JButton button_69;
  public JButton button_70;
  public JButton button_71;
  public JButton button_72;
  public JButton button_73;
  public JButton button_74;
  public JButton button_75;
  public JButton button_76;
  public JButton button_77;
  public JButton button_78;
  public JButton button_79;
  public JButton button_80;
  public JButton button_81;
  public JButton button_82;
  public JButton button_83;
  public JButton button_84;
  public JButton button_85;
  public JButton button_87;
  public JButton button_88;
  public JButton button_89;
  public JButton button_86;
  public JButton button_90;
  public JButton button_91;
  public JButton button_92;
  public JButton button_93;
  public JButton button_94;
  public JButton button_95;
  public JButton button_96;
  public JButton button_97;
  public JButton button_98;
  public JButton button_99;
  public JButton button_100;
  public JButton button_101;
  public JButton button_102;
  public JPanel panel_54;
  public JTabbedPane tabbedPane_2;
  public JPanel panel_55;
  public JPanel panel_56;
  public JButton button_103;
  public JButton button_104;
  public JButton button_105;
  public JLabel lblSystemInfo;
  public JLabel lblIccid_1;
  public JLabel lblSystemInfo_1;
  public JLabel lblEchoCancel;
  public JButton button_106;
  public JLabel lblImeisv;
  public JButton button_109;
  public JLabel lblGpioSettings;
  public JButton button_110;
  public JLabel lblUssdMode;
  public JButton button_111;
  public JLabel lblWakeupCfg;
  public JButton button_112;
  public JLabel lblConnections;
  public JButton button_113;
  public JLabel lblStationClass_1;
  public JButton button_114;
  public JLabel lblConfig;
  public JButton button_115;
  public JLabel lblGpsMode;
  public JButton button_116;
  public JLabel lblIpStatistics;
  public JButton button_117;
  public JLabel lblListen;
  public JButton button_119;
  public JLabel label_37;
  public JButton button_120;
  public JLabel lblPcmAudio;
  public JButton button_121;
  public JLabel lblSimToolkitInterface;
  public JButton button_122;
  public JButton button_123;
  public JLabel lblGpsSessionType;
  public JButton button_124;
  public JLabel lblQos_1;
  public JButton button_125;
  public JLabel lblSessionLock;
  public JButton button_126;
  public JLabel lblFotaMode;
  public JButton button_128;
  public JLabel lblTemperature_1;
  public JButton button_129;
  public JLabel lblTempProtection;
  public JButton button_130;
  public JLabel label_47;
  public JButton button_131;
  public JLabel lblFotaConnParm;
  public JButton button_132;
  public JLabel lblFotaStatus;
  public JButton button_133;
  public JLabel lblUpgradeStatus;
  public JLabel lblGpsType;
  public JButton button_135;
  public JLabel label_53;
  public JLabel lblGpsOrGnss;
  public JButton button_136;
  public JLabel lblNdisConnStatus;
  public JButton button_137;
  public JLabel lblVoicedataPref;
  public JButton button_138;
  public JLabel label_57;
  public JButton button_139;
  public JLabel lblGeneral_1;
  public JLabel lblNetworkTime;
  public JButton button_140;
  public JButton button_141;
  public JLabel lblTcpip_1;
  public JButton button_149;
  public JLabel lblGps_1;
  public JPanel panel_57;
  public JPanel panel_58;
  public JLabel lblServer;
  public JTextField txtWwwmmsupportnet_1;
  public JLabel label_21;
  public JTextField textField_47;
  public JButton button_107;
  public JButton button_108;
  public JLabel label_22;
  public JTextField textField_48;
  public JLabel label_23;
  public JButton button_118;
  public JPanel panel_60;
  public JPanel panel_61;
  public JPanel panel_62;
  public JTextField textField_50;
  public JLabel label_25;
  public JLabel label_26;
  public JLabel label_27;
  public JTextField textField_51;
  public JTextField textField_52;
  public JButton button_150;
  public JLabel lblFota_1;
  public JLabel lblFotaMode_1;
  public JTextArea txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp_1;
  public JScrollPane scrollPane_5;
  public JButton btnGetApn;
  public JTextArea textArea;
  public JScrollPane scrollPane_7;
  public JLabel lblServerData;
  public JButton btnClear_7;
  public JLabel lblGpsMode_1;
  public JComboBox comboBox_21;
  public JButton btnNewButton_2;
  public JButton btnGet_1;
  public JLabel lblAgpsServerAddress;
  public JTextField txtSuplgooglecom;
  public JButton btnStartPositioning;
  private JPanel panel_59;
  private JLabel lblSmsc;
  private JTextField textField_46;
  private JTextField textField_49;
  private JTextField textField_53;
  private JTextField textField_54;
  private JTextField textField_55;
  private JTextField textField_56;
  private JTextField textField_58;
  private JButton button_142;
  private JButton button_143;
  private JButton button_144;
  private JScrollPane scrollPane_11;
  public JComboBox comboBox_22;
  public String gbl_smsc = "";
  private JPanel panel_63;
  private JTextField textField_59;
  private JTextField textField_60;
  private JTextField textField_61;
  private JLabel lblIncludeSmscIn;
  private JComboBox comboBox_23;
  private JComboBox comboBox_24;
  private JPanel panel_64;
  public Integer print_pdu = Integer.valueOf(0);
  public Integer smsc_length = Integer.valueOf(0);

  public void init()
  {
    String passed_cmd = "AT+CGMI";
    int index = -1;

    for (int i = 0; (i < at_commands.length) && (index == -1); i++) {
      if (at_commands[i].equals(passed_cmd)) {
        index = i;
      }
    }
    if (index == -1)
      comboBox_2.setSelectedIndex(45);
    else
      comboBox_2.setSelectedIndex(index);
    String temp;
    if (index == -1)
      temp = at_command_examples[45];
    else {
      temp = at_command_examples[index];
    }
    textArea_2.setText(temp);
    textArea_3.setText(at_command_script_content[0]);
    if (winflag == 1)
    {
      try
      {
        win = JSObject.getWindow(new Applet());
        win.call("setMessage", new String[] { "Tester", "applet_started" });
      }
      catch (JSException e)
      {
        e.printStackTrace();
      }
    }
  }

  public void insertURL(String name, String url)
  {
    HyperlinkLabel url_link = new HyperlinkLabel(name);

    URL myURL = null;
    try
    {
      myURL = new URL(url);
    }
    catch (MalformedURLException e) {
      e.printStackTrace();
    }
    url_link.setAlignmentY(0.85F);
    url_link.setForeground(Color.blue);
    url_link.setURL(myURL);

    textPane_10.insertComponent(url_link);
  }

  public void writeOutput(String msg)
  {
    StyledDocument doc = textPane_10.getStyledDocument();
    Style style = textPane_10.addStyle("Red", null);
    StyleConstants.setForeground(style, Color.black);
    try
    {
      doc.insertString(doc.getLength(), msg, style);
    }
    catch (BadLocationException e) {
      e.printStackTrace();
    }

    textPane_10.setCaretPosition(textPane_10.getDocument().getLength());
  }

  public ATCommandTester_v27()
  {
    getContentPane().setLayout(null);
    setBounds(0, 0, 900, 825);

    JSeparator separator = new JSeparator();
    separator.setBounds(81, 111, 1, 2);
    getContentPane().add(separator);

    panel_4 = new JPanel();
    panel_4.setBorder(new TitledBorder(null, "JPanel title", 4, 2, null, null));
    panel_4.setBounds(-6, -16, 798, 843);
    getContentPane().add(panel_4);
    panel_4.setLayout(null);

    panel = new JPanel();
    panel.setBounds(16, 47, 458, 101);
    panel_4.add(panel);
    panel.setBorder(new TitledBorder(null, "Port Configuration", 4, 2, null, null));
    panel.setLayout(null);

    lblPort = new JLabel("Port");
    lblPort.setBounds(10, 33, 36, 23);
    panel.add(lblPort);

    comboBox = new JComboBox();
    comboBox.setBounds(43, 33, 83, 23);
    panel.add(comboBox);

    lblBaudRatekpbs = new JLabel("Baud Rate(bps)");
    lblBaudRatekpbs.setBounds(143, 35, 94, 19);
    panel.add(lblBaudRatekpbs);

    comboBox_1 = new JComboBox(port_speed);
    comboBox_1.setSelectedItem("115200");
    comboBox_1.setBounds(233, 34, 94, 21);
    panel.add(comboBox_1);

    btnFindPorts = new JButton("Find Ports");
    btnFindPorts.setBounds(20, 67, 94, 23);
    panel.add(btnFindPorts);

    btnConnect = new JButton("Connect");
    btnConnect.setBounds(124, 67, 89, 23);
    panel.add(btnConnect);

    btnDisconnect = new JButton("Disconnect");
    btnDisconnect.setBounds(223, 67, 103, 23);
    panel.add(btnDisconnect);

    tabbedPane_1 = new JTabbedPane(1);
    tabbedPane_1.setBounds(64, 101, 536, 354);
    panel.add(tabbedPane_1);

    btnAutofind = new JButton("AutoConnect");
    btnAutofind.setBackground(new Color(0, 255, 153));
    btnAutofind.setMargin(new Insets(2, 0, 2, 0));
    btnAutofind.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int i1 = 0;
        if (winflag == 1) {
          win.call("setMessage", "Tester", "AutoConnect");
        }
        Object[] options = { "Continue", "Cancel" };
        String ques = "Please check if your device is connected to your PC. Autoconnection may take some time(less than 2 min).";

        int n = JOptionPane.showOptionDialog(panel_4, ques, "AutoConnect", 0, 
          -1, null, options, options[0]);

        if (n == 1) {
          return;
        }

        comboBox.removeAllItems();
        String ret = mySerial1.disconnect();
        String ports = mySerial1.listPorts();

        if (ports == "")
        {
          String msg = "No serial ports found on the system.";
          writeOutput(msg);
          insertURL("Need Support?", "http://www.m2msupport.net/m2msupport/forums/forum/at-command-tester");
          writeOutput("\r\n\r\n");
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "ports_not_found" });
        }
        else
        {
          String[] temp = ports.split("#");
          String msg = "Found ports :";
          writeOutput(msg + "\n");
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "ports_found" });
          }
          for (int i = 0; i < temp.length; i++)
          {
            if (temp[i] != "")
            {
              System.out.println(temp[i]);
              msg = temp[i];
              writeOutput(msg);
              if (i != temp.length - 1)
                writeOutput(", ");
              comboBox.addItem(temp[i]);
            }
          }

          for (int i = 0; i < temp.length; i++)
          {
            if (temp[i] != "")
            {
              System.out.println(temp[i]);

              int len = port_speed.length;
              for (int k = 3; k < len - 1; k++)
              {
                try
                {
                  int speed = Integer.parseInt(port_speed[k]);
                  msg = "\nConnecting to " + temp[i] + " at speed " + speed + "\r\n";
                  writeOutput(msg);

                  ret = mySerial1.connect(temp[i], speed);

                  Thread thread = new Thread(new Runnable()
                  {
                    public void run() {
                      try {
                        Thread.sleep(3000L);
                      }
                      catch (InterruptedException localInterruptedException)
                      {
                      }
                    }
                  });
                  thread.start();
                  while (thread.isAlive())
                  {
                    writeOutput("test");
                    thread.join();
                  }

                  if (ret.equals("connect_sucess"))
                  {
                    global_model_no = "";
                    global_at_response = "";
                    int temp1 = TwoWaySerialComm2.send_to_parser;
                    TwoWaySerialComm2.send_to_parser = 1;

                    msg = "Sending AT query..\r\n";
                    writeOutput(msg);

                    String cmd = "AT\r\n";
                    ret = mySerial1.ser_write(cmd, 1);

                    thread = new Thread(new Runnable()
                    {
                      public void run() {
                        try {
                          Thread.sleep(3000L);
                        }
                        catch (InterruptedException localInterruptedException)
                        {
                        }
                      }
                    });
                    thread.start();

                    if (global_at_response.equals("OK"))
                    {
                      comboBox.setSelectedItem(temp[i]);
                      comboBox_1.setSelectedItem(port_speed[k]);
                      lblNotConnected.setText("Connected");
                      if (winflag == 1)
                      {
                        win.call("setMessage", new String[] { "Tester", "successful_at_connection" });
                        win.call("setMessage", new String[] { "Tester", "successful_autoconnect" });
                      }
                      cmd = "AT+CGMM\r\n";

                      ret = mySerial1.ser_write(cmd, 1);

                      thread = new Thread(new Runnable()
                      {
                        public void run() {
                          try {
                            Thread.sleep(10000L);
                          }
                          catch (InterruptedException localInterruptedException)
                          {
                          }
                        }
                      });
                      thread.start();
                      thread.join();
                      try
                      {
                        Thread.sleep(5000L);
                      } catch (InterruptedException localInterruptedException) {
                      }
                      if (!global_model_no.equals(""))
                        lblNotAvailable.setText(global_model_no);
                      if (winflag == 1)
                        win.call("setMessage", new String[] { "Model", global_model_no });
                      global_manufacturer_name = "";
                      cmd = "AT+CGMI\r\n";
                      ret = mySerial1.ser_write(cmd, 1);

                      thread = new Thread(new Runnable()
                      {
                        public void run() {
                          try {
                            Thread.sleep(2000L);
                          }
                          catch (InterruptedException localInterruptedException)
                          {
                          }
                        }
                      });
                      thread.start();

                      if (!global_manufacturer_name.equals("")) {
                        lblNotAvailable_1.setText(global_manufacturer_name);
                      }
                      TwoWaySerialComm2.send_to_parser = temp1;
                      break;
                    }

                    msg = "No valid response..\r\n";
                    writeOutput(msg);
                    ret = mySerial1.disconnect();
                    TwoWaySerialComm2.send_to_parser = temp1;
                  }
                  else if (ret.equals("connect_port_in_use"))
                  {
                    lblNotAvailable.setText("Port in Use");
                  }

                }
                catch (Exception et)
                {
                  et.printStackTrace();
                }
              }
            }
          }
          writeOutput("\r\n");
        }
      }
    });
    btnAutofind.setBounds(341, 67, 94, 23);

    tabbedPane = new JTabbedPane(1);
    tabbedPane.setBorder(new LineBorder(new Color(128, 128, 128), 3, true));
    tabbedPane.setBounds(16, 179, 432, 589);
    panel_4.add(tabbedPane);

    panel_51 = new JPanel();
    tabbedPane.addTab("Diagnostics", null, panel_51, null);
    panel_51.setLayout(null);

    panel_52 = new JPanel();
    panel_52.setBounds(0, 43, 421, 496);
    panel_51.add(panel_52);
    panel_52.setLayout(new CardLayout(0, 0));

    panel_3 = new JPanel();
    panel_52.add(panel_3, "All");
    panel_3.setLayout(null);
    btnDeviceInfo = new JButton("R");
    btnDeviceInfo.setPreferredSize(new Dimension(39, 20));
    btnDeviceInfo.setMargin(new Insets(0, 0, 0, 0));
    btnDeviceInfo.setFont(new Font("Tahoma", 0, 9));

    ActionListener atListner = new atParser();

    btnDeviceInfo.addActionListener(atListner);
    btnDeviceInfo.setBounds(113, 34, 24, 23);
    panel_3.add(btnDeviceInfo);

    btnTechnologyCapability = new JButton("R");
    btnTechnologyCapability.setFont(new Font("Tahoma", 0, 9));
    btnTechnologyCapability.setMargin(new Insets(0, 0, 0, 0));
    btnTechnologyCapability.addActionListener(atListner);
    btnTechnologyCapability.setBounds(113, 206, 24, 23);
    panel_3.add(btnTechnologyCapability);

    btnDeviceCapability = new JButton("R");
    btnDeviceCapability.setMargin(new Insets(0, 0, 0, 0));
    btnDeviceCapability.setFont(new Font("Tahoma", 0, 9));
    btnDeviceCapability.addActionListener(atListner);
    btnDeviceCapability.setBounds(112, 92, 24, 23);
    panel_3.add(btnDeviceCapability);

    lblGetDeviceInfo = new JLabel("Get Device Info");
    lblGetDeviceInfo.setFont(new Font("Tahoma", 2, 11));
    lblGetDeviceInfo.setBounds(11, 41, 90, 14);
    panel_3.add(lblGetDeviceInfo);

    lblTechnology = new JLabel("Technology");
    lblTechnology.setFont(new Font("Tahoma", 2, 11));
    lblTechnology.setBounds(11, 211, 76, 14);
    panel_3.add(lblTechnology);

    lblDeviceCapability = new JLabel("Capability");
    lblDeviceCapability.setFont(new Font("Tahoma", 2, 11));
    lblDeviceCapability.setBounds(12, 98, 99, 14);
    panel_3.add(lblDeviceCapability);

    lblActivityStatus = new JLabel("Activity Status");
    lblActivityStatus.setFont(new Font("Tahoma", 2, 11));
    lblActivityStatus.setBounds(13, 264, 109, 14);
    panel_3.add(lblActivityStatus);

    button_17 = new JButton("R");
    button_17.addActionListener(atListner);
    button_17.setMargin(new Insets(0, 0, 0, 0));
    button_17.setFont(new Font("Tahoma", 0, 9));
    button_17.setBounds(113, 260, 24, 23);
    panel_3.add(button_17);

    button_18 = new JButton("R");
    button_18.setMargin(new Insets(0, 0, 0, 0));
    button_18.setFont(new Font("Tahoma", 0, 9));
    button_18.setBounds(389, 207, 24, 23);
    button_18.addActionListener(atListner);
    panel_3.add(button_18);

    lblSmsFormat = new JLabel("SMS Format");
    lblSmsFormat.setFont(new Font("Tahoma", 2, 11));
    lblSmsFormat.setBounds(291, 211, 90, 14);
    panel_3.add(lblSmsFormat);

    lblSmsScAddress = new JLabel("SMS SC Address");
    lblSmsScAddress.setFont(new Font("Tahoma", 2, 11));
    lblSmsScAddress.setBounds(291, 241, 99, 14);
    panel_3.add(lblSmsScAddress);

    button_20 = new JButton("R");
    button_20.setMargin(new Insets(0, 0, 0, 0));
    button_20.setFont(new Font("Tahoma", 0, 9));
    button_20.setBounds(389, 237, 24, 23);
    button_20.addActionListener(atListner);
    panel_3.add(button_20);

    lblBatteryLevel = new JLabel("Battery Level");
    lblBatteryLevel.setFont(new Font("Tahoma", 2, 11));
    lblBatteryLevel.setBounds(12, 155, 99, 14);
    panel_3.add(lblBatteryLevel);

    button_22 = new JButton("R");
    button_22.setMargin(new Insets(0, 0, 0, 0));
    button_22.setFont(new Font("Tahoma", 0, 9));
    button_22.setBounds(113, 151, 24, 23);
    button_22.addActionListener(atListner);
    panel_3.add(button_22);

    lblSerialPortSpeed = new JLabel("Port Speed");
    lblSerialPortSpeed.setFont(new Font("Tahoma", 2, 11));
    lblSerialPortSpeed.setBounds(13, 181, 99, 14);
    panel_3.add(lblSerialPortSpeed);

    button_23 = new JButton("R");
    button_23.setMargin(new Insets(0, 0, 0, 0));
    button_23.setFont(new Font("Tahoma", 0, 9));
    button_23.setBounds(113, 177, 24, 23);
    button_23.addActionListener(atListner);
    panel_3.add(button_23);

    lblNewLabel_2 = new JLabel("Functionality");
    lblNewLabel_2.setFont(new Font("Tahoma", 2, 11));
    lblNewLabel_2.setBounds(10, 238, 109, 14);
    panel_3.add(lblNewLabel_2);

    button_24 = new JButton("R");
    button_24.setMargin(new Insets(0, 0, 0, 0));
    button_24.setFont(new Font("Tahoma", 0, 9));
    button_24.setBounds(113, 234, 24, 23);
    button_24.addActionListener(atListner);
    panel_3.add(button_24);

    lblConfiguration = new JLabel("Configuration");
    lblConfiguration.setFont(new Font("Tahoma", 2, 11));
    lblConfiguration.setBounds(12, 127, 76, 14);
    panel_3.add(lblConfiguration);

    button_27 = new JButton("R");
    button_27.setPreferredSize(new Dimension(39, 20));
    button_27.setMargin(new Insets(0, 0, 0, 0));
    button_27.setFont(new Font("Tahoma", 0, 9));
    button_27.setBounds(113, 122, 24, 23);
    button_27.addActionListener(atListner);
    panel_3.add(button_27);

    lblIndicators = new JLabel("Indicators");
    lblIndicators.setFont(new Font("Tahoma", 2, 11));
    lblIndicators.setBounds(163, 15, 76, 14);
    panel_3.add(lblIndicators);

    button_28 = new JButton("R");

    button_28.setPreferredSize(new Dimension(39, 20));
    button_28.setMargin(new Insets(0, 0, 0, 0));
    button_28.setFont(new Font("Tahoma", 0, 9));
    button_28.setBounds(249, 11, 24, 23);
    button_28.addActionListener(atListner);
    panel_3.add(button_28);

    lblStationClass = new JLabel("Station Class");
    lblStationClass.setFont(new Font("Tahoma", 2, 11));
    lblStationClass.setBounds(161, 42, 76, 14);
    panel_3.add(lblStationClass);

    button_29 = new JButton("R");
    button_29.setPreferredSize(new Dimension(39, 20));
    button_29.setMargin(new Insets(0, 0, 0, 0));
    button_29.setFont(new Font("Tahoma", 0, 9));
    button_29.setBounds(248, 39, 24, 23);
    button_29.addActionListener(atListner);
    panel_3.add(button_29);

    lblPdpContexts_1 = new JLabel("Conn. Profiles");
    lblPdpContexts_1.setFont(new Font("Tahoma", 2, 11));
    lblPdpContexts_1.setBounds(163, 128, 90, 14);
    panel_3.add(lblPdpContexts_1);

    button_30 = new JButton("R");
    button_30.setPreferredSize(new Dimension(39, 20));
    button_30.setMargin(new Insets(0, 0, 0, 0));
    button_30.setFont(new Font("Tahoma", 0, 9));
    button_30.setBounds(249, 93, 24, 23);
    button_30.addActionListener(atListner);
    panel_3.add(button_30);

    lblQos = new JLabel("QoS");
    lblQos.setFont(new Font("Tahoma", 2, 11));
    lblQos.setBounds(163, 211, 46, 14);
    panel_3.add(lblQos);

    button_31 = new JButton("R");
    button_31.setPreferredSize(new Dimension(39, 20));
    button_31.setMargin(new Insets(0, 0, 0, 0));
    button_31.setFont(new Font("Tahoma", 0, 9));
    button_31.setBounds(249, 122, 24, 23);
    button_31.addActionListener(atListner);
    panel_3.add(button_31);

    lblAttachStatus = new JLabel("Attach Status");
    lblAttachStatus.setFont(new Font("Tahoma", 2, 11));
    lblAttachStatus.setBounds(163, 158, 83, 14);
    panel_3.add(lblAttachStatus);

    button_32 = new JButton("R");
    button_32.setPreferredSize(new Dimension(39, 20));
    button_32.setMargin(new Insets(0, 0, 0, 0));
    button_32.setFont(new Font("Tahoma", 0, 9));
    button_32.setBounds(249, 152, 24, 23);
    button_32.addActionListener(atListner);
    panel_3.add(button_32);

    lblPdpAddress = new JLabel("IP Address");
    lblPdpAddress.setFont(new Font("Tahoma", 2, 11));
    lblPdpAddress.setBounds(163, 183, 83, 14);
    panel_3.add(lblPdpAddress);

    button_33 = new JButton("R");
    button_33.setPreferredSize(new Dimension(39, 20));
    button_33.setMargin(new Insets(0, 0, 0, 0));
    button_33.setFont(new Font("Tahoma", 0, 9));
    button_33.setBounds(249, 179, 24, 23);
    button_33.addActionListener(atListner);
    panel_3.add(button_33);

    lblActivationStatus = new JLabel("Conn. Status");
    lblActivationStatus.setFont(new Font("Tahoma", 2, 11));
    lblActivationStatus.setBounds(163, 99, 76, 14);
    panel_3.add(lblActivationStatus);

    button_34 = new JButton("R");
    button_34.addActionListener(atListner);
    button_34.setPreferredSize(new Dimension(39, 20));
    button_34.setMargin(new Insets(0, 0, 0, 0));
    button_34.setFont(new Font("Tahoma", 0, 9));
    button_34.setBounds(249, 207, 24, 23);
    panel_3.add(button_34);

    lblReset = new JLabel("Reset");
    lblReset.setFont(new Font("Tahoma", 2, 11));
    lblReset.setBounds(12, 497, 46, 14);
    panel_3.add(lblReset);

    button_35 = new JButton("R");
    button_35.setPreferredSize(new Dimension(39, 20));
    button_35.setMargin(new Insets(0, 0, 0, 0));
    button_35.setFont(new Font("Tahoma", 0, 9));
    button_35.setBounds(112, 493, 24, 23);
    button_35.addActionListener(atListner);
    panel_3.add(button_35);

    lblFlowControl = new JLabel("Flow Control");
    lblFlowControl.setFont(new Font("Tahoma", 2, 11));
    lblFlowControl.setBounds(13, 289, 90, 14);
    panel_3.add(lblFlowControl);

    button_36 = new JButton("R");
    button_36.setPreferredSize(new Dimension(39, 20));
    button_36.setMargin(new Insets(0, 0, 0, 0));
    button_36.setFont(new Font("Tahoma", 0, 9));
    button_36.setBounds(113, 285, 24, 23);
    button_36.addActionListener(atListner);
    panel_3.add(button_36);

    lblClock = new JLabel("Clock");
    lblClock.setFont(new Font("Tahoma", 2, 11));
    lblClock.setBounds(13, 314, 46, 14);
    panel_3.add(lblClock);

    button_37 = new JButton("R");
    button_37.setPreferredSize(new Dimension(39, 20));
    button_37.setMargin(new Insets(0, 0, 0, 0));
    button_37.setFont(new Font("Tahoma", 0, 9));
    button_37.setBounds(113, 310, 24, 23);
    button_37.addActionListener(atListner);
    panel_3.add(button_37);

    button_38 = new JButton("R");
    button_38.setPreferredSize(new Dimension(39, 20));
    button_38.setMargin(new Insets(0, 0, 0, 0));
    button_38.setFont(new Font("Tahoma", 0, 9));
    button_38.setBounds(249, 287, 24, 23);
    button_38.addActionListener(atListner);
    panel_3.add(button_38);

    lblMultiplexMode = new JLabel("Mux Mode");
    lblMultiplexMode.setFont(new Font("Tahoma", 2, 11));
    lblMultiplexMode.setBounds(163, 241, 76, 14);
    panel_3.add(lblMultiplexMode);

    button_39 = new JButton("R");
    button_39.setPreferredSize(new Dimension(39, 20));
    button_39.setMargin(new Insets(0, 0, 0, 0));
    button_39.setFont(new Font("Tahoma", 0, 9));
    button_39.setBounds(249, 237, 24, 23);
    button_39.addActionListener(atListner);
    panel_3.add(button_39);

    lblBearerType = new JLabel("Bearer Type");
    lblBearerType.setFont(new Font("Tahoma", 2, 11));
    lblBearerType.setBounds(163, 266, 83, 14);
    panel_3.add(lblBearerType);

    button_40 = new JButton("R");
    button_40.setPreferredSize(new Dimension(39, 20));
    button_40.setMargin(new Insets(0, 0, 0, 0));
    button_40.setFont(new Font("Tahoma", 0, 9));
    button_40.setBounds(249, 262, 24, 23);
    button_40.addActionListener(atListner);
    panel_3.add(button_40);

    lblRlp = new JLabel("RLP");
    lblRlp.setFont(new Font("Tahoma", 2, 11));
    lblRlp.setBounds(163, 291, 46, 14);
    panel_3.add(lblRlp);

    button_41 = new JButton("R");
    button_41.setPreferredSize(new Dimension(39, 20));
    button_41.setMargin(new Insets(0, 0, 0, 0));
    button_41.setFont(new Font("Tahoma", 0, 9));
    button_41.setBounds(249, 287, 24, 23);
    button_41.addActionListener(atListner);
    panel_3.add(button_41);

    lblSerReporting = new JLabel("Ser. Report");
    lblSerReporting.setFont(new Font("Tahoma", 2, 11));
    lblSerReporting.setBounds(163, 366, 76, 14);
    panel_3.add(lblSerReporting);

    button_42 = new JButton("R");
    button_42.setPreferredSize(new Dimension(39, 20));
    button_42.setMargin(new Insets(0, 0, 0, 0));
    button_42.setFont(new Font("Tahoma", 0, 9));
    button_42.setBounds(249, 362, 24, 23);
    button_42.addActionListener(atListner);
    panel_3.add(button_42);

    lblExtError = new JLabel("Ext. Error ");
    lblExtError.setFont(new Font("Tahoma", 2, 11));
    lblExtError.setBounds(163, 395, 66, 14);
    panel_3.add(lblExtError);

    button_43 = new JButton("R");
    button_43.setPreferredSize(new Dimension(39, 20));
    button_43.setMargin(new Insets(0, 0, 0, 0));
    button_43.setFont(new Font("Tahoma", 0, 9));
    button_43.setBounds(249, 391, 24, 23);
    button_43.addActionListener(atListner);
    panel_3.add(button_43);

    lblFacilityLock_1 = new JLabel("IMEI");
    lblFacilityLock_1.setFont(new Font("Tahoma", 2, 11));
    lblFacilityLock_1.setBounds(13, 339, 76, 14);
    panel_3.add(lblFacilityLock_1);

    button_45 = new JButton("R");
    button_45.setPreferredSize(new Dimension(39, 20));
    button_45.setMargin(new Insets(0, 0, 0, 0));
    button_45.setFont(new Font("Tahoma", 0, 9));
    button_45.setBounds(113, 335, 24, 23);
    button_45.addActionListener(atListner);
    panel_3.add(button_45);

    lblPhonebook = new JLabel("Phonebook");
    lblPhonebook.setFont(new Font("Tahoma", 2, 11));
    lblPhonebook.setBounds(13, 364, 76, 14);
    panel_3.add(lblPhonebook);

    button_49 = new JButton("R");
    button_49.setPreferredSize(new Dimension(39, 20));
    button_49.setMargin(new Insets(0, 0, 0, 0));
    button_49.setFont(new Font("Tahoma", 0, 9));
    button_49.setBounds(113, 360, 24, 23);
    button_49.addActionListener(atListner);
    panel_3.add(button_49);

    lblAlarms = new JLabel("Alarms");
    lblAlarms.setFont(new Font("Tahoma", 2, 11));
    lblAlarms.setBounds(13, 444, 46, 14);
    panel_3.add(lblAlarms);

    button_50 = new JButton("R");
    button_50.setPreferredSize(new Dimension(39, 20));
    button_50.setMargin(new Insets(0, 0, 0, 0));
    button_50.setFont(new Font("Tahoma", 0, 9));
    button_50.setBounds(113, 440, 24, 23);
    button_50.addActionListener(atListner);
    panel_3.add(button_50);

    lblCallMeterMax = new JLabel("Call Meter Max");
    lblCallMeterMax.setFont(new Font("Tahoma", 2, 11));
    lblCallMeterMax.setBounds(163, 421, 76, 14);
    panel_3.add(lblCallMeterMax);

    button_51 = new JButton("R");
    button_51.setPreferredSize(new Dimension(39, 20));
    button_51.setMargin(new Insets(0, 0, 0, 0));
    button_51.setFont(new Font("Tahoma", 0, 9));
    button_51.setBounds(249, 417, 24, 23);
    button_51.addActionListener(atListner);
    panel_3.add(button_51);

    lblPricePerUnit = new JLabel("Price per unit");
    lblPricePerUnit.setFont(new Font("Tahoma", 2, 11));
    lblPricePerUnit.setBounds(163, 450, 76, 14);
    panel_3.add(lblPricePerUnit);

    button_52 = new JButton("R");
    button_52.setPreferredSize(new Dimension(39, 20));
    button_52.setMargin(new Insets(0, 0, 0, 0));
    button_52.setFont(new Font("Tahoma", 0, 9));
    button_52.setBounds(249, 446, 24, 23);
    button_52.addActionListener(atListner);
    panel_3.add(button_52);

    lblAtCommands = new JLabel("AT Comm. List");
    lblAtCommands.setFont(new Font("Tahoma", 2, 11));
    lblAtCommands.setBounds(291, 366, 76, 14);
    panel_3.add(lblAtCommands);

    button_53 = new JButton("R");
    button_53.setPreferredSize(new Dimension(39, 20));
    button_53.setMargin(new Insets(0, 0, 0, 0));
    button_53.setFont(new Font("Tahoma", 0, 9));
    button_53.setBounds(389, 362, 24, 23);
    button_53.addActionListener(atListner);
    panel_3.add(button_53);

    lblCallMeter = new JLabel("Call Meter");
    lblCallMeter.setFont(new Font("Tahoma", 2, 11));
    lblCallMeter.setBounds(163, 472, 66, 14);
    panel_3.add(lblCallMeter);

    lblServiceClass = new JLabel("Service Class");
    lblServiceClass.setFont(new Font("Tahoma", 2, 11));
    lblServiceClass.setBounds(163, 316, 83, 14);
    panel_3.add(lblServiceClass);

    button_55 = new JButton("R");
    button_55.setPreferredSize(new Dimension(39, 20));
    button_55.setMargin(new Insets(0, 0, 0, 0));
    button_55.setFont(new Font("Tahoma", 0, 9));
    button_55.setBounds(249, 312, 24, 23);
    button_55.addActionListener(atListner);
    panel_3.add(button_55);

    lblMuteControl = new JLabel("Mute Control");
    lblMuteControl.setFont(new Font("Tahoma", 2, 11));
    lblMuteControl.setBounds(13, 470, 90, 14);
    panel_3.add(lblMuteControl);

    lblUssd = new JLabel("USSD");
    lblUssd.setFont(new Font("Tahoma", 2, 11));
    lblUssd.setBounds(163, 341, 46, 14);
    panel_3.add(lblUssd);

    button_57 = new JButton("R");
    button_57.setPreferredSize(new Dimension(39, 20));
    button_57.setMargin(new Insets(0, 0, 0, 0));
    button_57.setFont(new Font("Tahoma", 0, 9));
    button_57.setBounds(249, 337, 24, 23);
    button_57.addActionListener(atListner);
    panel_3.add(button_57);

    lblEchoMode = new JLabel("Echo Mode");
    lblEchoMode.setFont(new Font("Tahoma", 2, 11));
    lblEchoMode.setBounds(13, 389, 66, 14);
    panel_3.add(lblEchoMode);

    button_59 = new JButton("R");
    button_59.setPreferredSize(new Dimension(39, 20));
    button_59.setMargin(new Insets(0, 0, 0, 0));
    button_59.setFont(new Font("Tahoma", 0, 9));
    button_59.setBounds(113, 385, 24, 23);
    button_59.addActionListener(atListner);
    panel_3.add(button_59);

    lblCharacterSet = new JLabel("Character Set");
    lblCharacterSet.setFont(new Font("Tahoma", 2, 11));
    lblCharacterSet.setBounds(13, 419, 90, 14);
    panel_3.add(lblCharacterSet);

    button_60 = new JButton("R");
    button_60.setPreferredSize(new Dimension(39, 20));
    button_60.setMargin(new Insets(0, 0, 0, 0));
    button_60.setFont(new Font("Tahoma", 0, 9));
    button_60.setBounds(113, 415, 24, 23);
    button_60.addActionListener(atListner);
    panel_3.add(button_60);

    lblAddressType = new JLabel("Address Type");
    lblAddressType.setFont(new Font("Tahoma", 2, 11));
    lblAddressType.setBounds(163, 497, 76, 14);
    panel_3.add(lblAddressType);

    button_61 = new JButton("R");
    button_61.setPreferredSize(new Dimension(39, 20));
    button_61.setMargin(new Insets(0, 0, 0, 0));
    button_61.setFont(new Font("Tahoma", 0, 9));
    button_61.setBounds(249, 497, 24, 23);
    button_61.addActionListener(atListner);
    panel_3.add(button_61);

    lblDeviceInfo = new JLabel("Device Info");
    lblDeviceInfo.setForeground(new Color(51, 102, 204));
    lblDeviceInfo.setFont(new Font("Tahoma", 1, 11));
    lblDeviceInfo.setBounds(13, 13, 90, 14);
    panel_3.add(lblDeviceInfo);

    lblPhoneNumber_2 = new JLabel("Phone Number");
    lblPhoneNumber_2.setFont(new Font("Tahoma", 2, 11));
    lblPhoneNumber_2.setBounds(11, 68, 99, 14);
    panel_3.add(lblPhoneNumber_2);

    button_44 = new JButton("R");
    button_44.setBounds(113, 64, 24, 23);
    panel_3.add(button_44);
    button_44.setPreferredSize(new Dimension(39, 20));
    button_44.setMargin(new Insets(0, 0, 0, 0));
    button_44.setFont(new Font("Tahoma", 0, 9));
    button_44.addActionListener(atListner);
    lblSms = new JLabel("SMS");
    lblSms.setForeground(new Color(51, 102, 204));
    lblSms.setFont(new Font("Tahoma", 1, 11));
    lblSms.setBounds(291, 183, 46, 14);
    panel_3.add(lblSms);

    button_25 = new JButton("R");

    button_25.setPreferredSize(new Dimension(39, 20));
    button_25.setMargin(new Insets(0, 0, 0, 0));
    button_25.setFont(new Font("Tahoma", 0, 9));
    button_25.setBounds(113, 465, 24, 23);
    button_25.addActionListener(atListner);
    panel_3.add(button_25);

    lblConnection = new JLabel("Connection");
    lblConnection.setForeground(new Color(51, 102, 204));
    lblConnection.setFont(new Font("Tahoma", 1, 11));
    lblConnection.setBounds(163, 69, 76, 14);
    panel_3.add(lblConnection);

    lblNetwork = new JLabel("Network");
    lblNetwork.setForeground(new Color(51, 102, 204));
    lblNetwork.setFont(new Font("Tahoma", 1, 11));
    lblNetwork.setBounds(291, 15, 76, 14);
    panel_3.add(lblNetwork);

    lblSignalStrength = new JLabel("Signal Strength");
    lblSignalStrength.setFont(new Font("Tahoma", 2, 11));
    lblSignalStrength.setBounds(288, 42, 99, 14);
    panel_3.add(lblSignalStrength);

    btnSignalStrength = new JButton("R");
    btnSignalStrength.setBounds(389, 36, 24, 23);
    panel_3.add(btnSignalStrength);
    btnSignalStrength.setMargin(new Insets(0, 0, 0, 0));
    btnSignalStrength.setFont(new Font("Tahoma", 0, 9));
    btnSignalStrength.addActionListener(atListner);

    btnSignalStrength.setAlignmentX(1.0F);
    btnSignalStrength.setAlignmentY(1.0F);

    lblOperatorInfo = new JLabel("Operator Info");
    lblOperatorInfo.setFont(new Font("Tahoma", 2, 11));
    lblOperatorInfo.setBounds(291, 69, 76, 14);
    panel_3.add(lblOperatorInfo);

    btnOperatorInfo = new JButton("R");
    btnOperatorInfo.setBounds(389, 65, 24, 23);
    panel_3.add(btnOperatorInfo);
    btnOperatorInfo.setFont(new Font("Tahoma", 0, 9));
    btnOperatorInfo.setMargin(new Insets(0, 0, 0, 0));
    btnOperatorInfo.addActionListener(atListner);

    lblRegistrationStatus = new JLabel("Registration ");
    lblRegistrationStatus.setFont(new Font("Tahoma", 2, 11));
    lblRegistrationStatus.setBounds(291, 99, 109, 14);
    panel_3.add(lblRegistrationStatus);

    btnRegistrationStatus = new JButton("R");
    btnRegistrationStatus.setBounds(389, 95, 24, 23);
    panel_3.add(btnRegistrationStatus);
    btnRegistrationStatus.setFont(new Font("Tahoma", 0, 9));
    btnRegistrationStatus.setMargin(new Insets(0, 0, 0, 0));
    btnRegistrationStatus.addActionListener(atListner);

    lblAvailableNetworks = new JLabel("Available Net.");
    lblAvailableNetworks.setFont(new Font("Tahoma", 2, 11));
    lblAvailableNetworks.setBounds(291, 128, 109, 14);
    panel_3.add(lblAvailableNetworks);

    btnAvaialbleNetworks = new JButton("R");
    btnAvaialbleNetworks.setBounds(389, 124, 24, 23);
    panel_3.add(btnAvaialbleNetworks);
    btnAvaialbleNetworks.setFont(new Font("Tahoma", 0, 9));
    btnAvaialbleNetworks.setMargin(new Insets(0, 0, 0, 0));
    btnAvaialbleNetworks.addActionListener(atListner);

    lblPreferredOperator = new JLabel("Pref. Operators");
    lblPreferredOperator.setFont(new Font("Tahoma", 2, 11));
    lblPreferredOperator.setBounds(291, 158, 109, 14);
    panel_3.add(lblPreferredOperator);

    btnSimStatus = new JButton("R");
    btnSimStatus.setBounds(389, 154, 24, 23);
    panel_3.add(btnSimStatus);
    btnSimStatus.setFont(new Font("Tahoma", 0, 9));
    btnSimStatus.setMargin(new Insets(0, 0, 0, 0));
    btnSimStatus.addActionListener(atListner);

    lblSmsRead = new JLabel("SMS Support");
    lblSmsRead.setFont(new Font("Tahoma", 2, 11));
    lblSmsRead.setBounds(291, 266, 66, 14);
    panel_3.add(lblSmsRead);

    button_58 = new JButton("R");
    button_58.setBounds(389, 262, 24, 23);
    panel_3.add(button_58);
    button_58.setPreferredSize(new Dimension(39, 20));
    button_58.setMargin(new Insets(0, 0, 0, 0));
    button_58.setFont(new Font("Tahoma", 0, 9));
    button_58.addActionListener(atListner);

    lblSim = new JLabel("SIM");
    lblSim.setForeground(new Color(0, 102, 204));
    lblSim.setFont(new Font("Tahoma", 1, 11));
    lblSim.setBounds(291, 291, 46, 14);
    panel_3.add(lblSim);

    lblSimStatus = new JLabel("SIM Status");
    lblSimStatus.setFont(new Font("Tahoma", 2, 11));
    lblSimStatus.setBounds(291, 316, 90, 14);
    panel_3.add(lblSimStatus);

    button_19 = new JButton("R");
    button_19.setPreferredSize(new Dimension(39, 20));
    button_19.setMargin(new Insets(0, 0, 0, 0));
    button_19.setFont(new Font("Tahoma", 0, 9));
    button_19.setBounds(389, 312, 24, 23);
    button_19.addActionListener(atListner);
    panel_3.add(button_19);

    lblMisc = new JLabel("Misc");
    lblMisc.setForeground(new Color(0, 102, 204));
    lblMisc.setFont(new Font("Tahoma", 1, 11));
    lblMisc.setBounds(291, 341, 46, 14);
    panel_3.add(lblMisc);

    button_54 = new JButton("R");
    button_54.setMargin(new Insets(0, 0, 0, 0));
    button_54.setFont(new Font("Tahoma", 0, 9));
    button_54.setBounds(249, 472, 24, 23);
    button_54.addActionListener(atListner);
    panel_3.add(button_54);

    panel_53 = new JPanel();
    panel_53.setMaximumSize(new Dimension(39, 18));
    panel_52.add(panel_53, "Simcom");
    panel_53.setLayout(null);

    lblMicGain = new JLabel("Mic gain");
    lblMicGain.setFont(new Font("Tahoma", 2, 11));
    lblMicGain.setBounds(10, 354, 46, 14);
    panel_53.add(lblMicGain);

    lblReadAdc = new JLabel("Read ADC");
    lblReadAdc.setFont(new Font("Tahoma", 2, 11));
    lblReadAdc.setBounds(10, 111, 65, 14);
    panel_53.add(lblReadAdc);

    lblHeadsetControl = new JLabel("Headset Control");
    lblHeadsetControl.setFont(new Font("Tahoma", 2, 11));
    lblHeadsetControl.setBounds(10, 379, 83, 14);
    panel_53.add(lblHeadsetControl);

    lblSimInserted = new JLabel("SIM inserted?");
    lblSimInserted.setFont(new Font("Tahoma", 2, 11));
    lblSimInserted.setBounds(10, 168, 74, 14);
    panel_53.add(lblSimInserted);

    lblProvider = new JLabel("Provider");
    lblProvider.setFont(new Font("Tahoma", 2, 11));
    lblProvider.setBounds(10, 197, 46, 14);
    panel_53.add(lblProvider);

    lblVoicemailNum = new JLabel("Voicemail Num.");
    lblVoicemailNum.setFont(new Font("Tahoma", 2, 11));
    lblVoicemailNum.setBounds(10, 222, 95, 14);
    panel_53.add(lblVoicemailNum);

    lblOprBand = new JLabel("Opr. Band");
    lblOprBand.setFont(new Font("Tahoma", 2, 11));
    lblOprBand.setBounds(10, 79, 83, 14);
    panel_53.add(lblOprBand);

    lblHandsFree = new JLabel("Hands Free");
    lblHandsFree.setFont(new Font("Tahoma", 2, 11));
    lblHandsFree.setBounds(10, 454, 65, 14);
    panel_53.add(lblHandsFree);

    lblEnggMode = new JLabel("Engg. Mode");
    lblEnggMode.setFont(new Font("Tahoma", 2, 11));
    lblEnggMode.setBounds(10, 49, 83, 14);
    panel_53.add(lblEnggMode);

    lblIccid = new JLabel("ICCID");
    lblIccid.setFont(new Font("Tahoma", 2, 11));
    lblIccid.setBounds(10, 247, 46, 14);
    panel_53.add(lblIccid);

    lblTemperature = new JLabel("Temperature");
    lblTemperature.setFont(new Font("Tahoma", 2, 11));
    lblTemperature.setBounds(10, 269, 83, 14);
    panel_53.add(lblTemperature);

    lblMultislot = new JLabel("Multislot");
    lblMultislot.setFont(new Font("Tahoma", 2, 11));
    lblMultislot.setBounds(10, 294, 46, 14);
    panel_53.add(lblMultislot);

    lblCallReady = new JLabel("Call Ready?");
    lblCallReady.setFont(new Font("Tahoma", 2, 11));
    lblCallReady.setBounds(10, 143, 74, 14);
    panel_53.add(lblCallReady);

    lblVoiceCoding = new JLabel("Voice Coding");
    lblVoiceCoding.setFont(new Font("Tahoma", 2, 11));
    lblVoiceCoding.setBounds(10, 402, 83, 14);
    panel_53.add(lblVoiceCoding);

    lblEmergencyNos = new JLabel("Emergency Nos.");
    lblEmergencyNos.setFont(new Font("Tahoma", 2, 11));
    lblEmergencyNos.setBounds(10, 320, 83, 14);
    panel_53.add(lblEmergencyNos);

    lblBuzszerMode = new JLabel("Buzzer Mode");
    lblBuzszerMode.setFont(new Font("Tahoma", 2, 11));
    lblBuzszerMode.setBounds(10, 429, 83, 14);
    panel_53.add(lblBuzszerMode);

    lblMultiip = new JLabel("Multi-IP");
    lblMultiip.setFont(new Font("Tahoma", 2, 11));
    lblMultiip.setBounds(151, 79, 46, 14);
    panel_53.add(lblMultiip);

    lblLocalPort = new JLabel("Local Port");
    lblLocalPort.setFont(new Font("Tahoma", 2, 11));
    lblLocalPort.setBounds(151, 111, 65, 14);
    panel_53.add(lblLocalPort);

    lblTaskInfo = new JLabel("Task Info");
    lblTaskInfo.setFont(new Font("Tahoma", 2, 11));
    lblTaskInfo.setBounds(151, 143, 46, 14);
    panel_53.add(lblTaskInfo);

    lblLocalIp = new JLabel("Local IP");
    lblLocalIp.setFont(new Font("Tahoma", 2, 11));
    lblLocalIp.setBounds(151, 168, 46, 14);
    panel_53.add(lblLocalIp);

    lblConnStatus = new JLabel("Conn. Status");
    lblConnStatus.setFont(new Font("Tahoma", 2, 11));
    lblConnStatus.setBounds(151, 197, 65, 14);
    panel_53.add(lblConnStatus);

    lblDns = new JLabel("DNS");
    lblDns.setFont(new Font("Tahoma", 2, 11));
    lblDns.setBounds(151, 222, 46, 14);
    panel_53.add(lblDns);

    lblIpHeader = new JLabel("IP Header");
    lblIpHeader.setFont(new Font("Tahoma", 2, 11));
    lblIpHeader.setBounds(151, 247, 65, 14);
    panel_53.add(lblIpHeader);

    lblServerMode = new JLabel("Server Mode");
    lblServerMode.setFont(new Font("Tahoma", 2, 11));
    lblServerMode.setBounds(151, 269, 74, 14);
    panel_53.add(lblServerMode);

    lblConnMode = new JLabel("Conn. Mode");
    lblConnMode.setFont(new Font("Tahoma", 2, 11));
    lblConnMode.setBounds(151, 294, 74, 14);
    panel_53.add(lblConnMode);

    lblTcpipMode = new JLabel("TCPIP Mode");
    lblTcpipMode.setFont(new Font("Tahoma", 2, 11));
    lblTcpipMode.setBounds(151, 320, 65, 14);
    panel_53.add(lblTcpipMode);

    lblTransMode = new JLabel("Trans. Mode");
    lblTransMode.setFont(new Font("Tahoma", 2, 11));
    lblTransMode.setBounds(151, 354, 74, 14);
    panel_53.add(lblTransMode);

    lblTcpParm = new JLabel("TCP Parm.");
    lblTcpParm.setFont(new Font("Tahoma", 2, 11));
    lblTcpParm.setBounds(151, 49, 74, 14);
    panel_53.add(lblTcpParm);

    lblHttpPara = new JLabel("HTTP Para");
    lblHttpPara.setFont(new Font("Tahoma", 2, 11));
    lblHttpPara.setBounds(151, 402, 74, 14);
    panel_53.add(lblHttpPara);

    lblHttpContext = new JLabel("HTTP Context");
    lblHttpContext.setFont(new Font("Tahoma", 2, 11));
    lblHttpContext.setBounds(151, 429, 74, 14);
    panel_53.add(lblHttpContext);

    lblFtpPort = new JLabel("FTP Port");
    lblFtpPort.setFont(new Font("Tahoma", 2, 11));
    lblFtpPort.setBounds(290, 79, 46, 14);
    panel_53.add(lblFtpPort);

    lblFtpMode = new JLabel("FTP Mode");
    lblFtpMode.setFont(new Font("Tahoma", 2, 11));
    lblFtpMode.setBounds(290, 49, 74, 14);
    panel_53.add(lblFtpMode);

    lblTranfType = new JLabel("Tranf. Type");
    lblTranfType.setFont(new Font("Tahoma", 2, 11));
    lblTranfType.setBounds(290, 111, 65, 14);
    panel_53.add(lblTranfType);

    lblPutType = new JLabel("PUT Type");
    lblPutType.setFont(new Font("Tahoma", 2, 11));
    lblPutType.setBounds(290, 143, 46, 14);
    panel_53.add(lblPutType);

    lblFtpServer = new JLabel("FTP Server");
    lblFtpServer.setFont(new Font("Tahoma", 2, 11));
    lblFtpServer.setBounds(290, 168, 65, 14);
    panel_53.add(lblFtpServer);

    lblFtpUserName = new JLabel("FTP User Name");
    lblFtpUserName.setFont(new Font("Tahoma", 2, 11));
    lblFtpUserName.setBounds(290, 197, 74, 14);
    panel_53.add(lblFtpUserName);

    lblFtpPassword = new JLabel("FTP Password");
    lblFtpPassword.setFont(new Font("Tahoma", 2, 11));
    lblFtpPassword.setBounds(290, 222, 74, 14);
    panel_53.add(lblFtpPassword);

    lblFtpFileNames = new JLabel("FTP  File Names");
    lblFtpFileNames.setFont(new Font("Tahoma", 2, 11));
    lblFtpFileNames.setBounds(290, 246, 83, 14);
    panel_53.add(lblFtpFileNames);

    lblFtpPath = new JLabel("FTP Path");
    lblFtpPath.setFont(new Font("Tahoma", 2, 11));
    lblFtpPath.setBounds(290, 269, 46, 14);
    panel_53.add(lblFtpPath);

    lblFtpContext = new JLabel("FTP Context");
    lblFtpContext.setFont(new Font("Tahoma", 2, 11));
    lblFtpContext.setBounds(290, 293, 83, 14);
    panel_53.add(lblFtpContext);

    lblFtpState = new JLabel("FTP State");
    lblFtpState.setFont(new Font("Tahoma", 2, 11));
    lblFtpState.setBounds(290, 319, 74, 14);
    panel_53.add(lblFtpState);

    lblGps = new JLabel("GPS");
    lblGps.setForeground(new Color(51, 102, 204));
    lblGps.setFont(new Font("Tahoma", 1, 11));
    lblGps.setBounds(290, 334, 46, 14);
    panel_53.add(lblGps);

    lblNewLabel_3 = new JLabel("GPS Power?");
    lblNewLabel_3.setFont(new Font("Tahoma", 2, 11));
    lblNewLabel_3.setBounds(290, 354, 65, 14);
    panel_53.add(lblNewLabel_3);

    lblGpsResetMode = new JLabel("GPS Reset  Mode");
    lblGpsResetMode.setFont(new Font("Tahoma", 2, 11));
    lblGpsResetMode.setBounds(290, 379, 83, 14);
    panel_53.add(lblGpsResetMode);

    lblGpsLocation = new JLabel("GPS Location");
    lblGpsLocation.setFont(new Font("Tahoma", 2, 11));
    lblGpsLocation.setBounds(290, 402, 74, 14);
    panel_53.add(lblGpsLocation);

    lblGpsStatus = new JLabel("GPS Status");
    lblGpsStatus.setFont(new Font("Tahoma", 2, 11));
    lblGpsStatus.setBounds(290, 433, 74, 14);
    panel_53.add(lblGpsStatus);

    lblTcpip = new JLabel("TCP/IP");
    lblTcpip.setForeground(new Color(51, 102, 204));
    lblTcpip.setFont(new Font("Tahoma", 1, 11));
    lblTcpip.setBounds(151, 24, 46, 14);
    panel_53.add(lblTcpip);

    lblGeneral = new JLabel("General");
    lblGeneral.setForeground(new Color(51, 102, 204));
    lblGeneral.setBounds(10, 24, 46, 14);
    panel_53.add(lblGeneral);
    lblGeneral.setFont(new Font("Tahoma", 1, 11));

    lblHttp = new JLabel("HTTP");
    lblHttp.setForeground(new Color(51, 102, 204));
    lblHttp.setFont(new Font("Tahoma", 1, 11));
    lblHttp.setBounds(151, 379, 46, 14);
    panel_53.add(lblHttp);

    lblFtp = new JLabel("FTP");
    lblFtp.setForeground(new Color(51, 100, 204));
    lblFtp.setFont(new Font("Tahoma", 1, 11));
    lblFtp.setBounds(290, 24, 46, 14);
    panel_53.add(lblFtp);

    lblAudio = new JLabel("Audio");
    lblAudio.setForeground(new Color(51, 102, 204));
    lblAudio.setFont(new Font("Tahoma", 1, 11));
    lblAudio.setBounds(10, 335, 46, 14);
    panel_53.add(lblAudio);

    button_21 = new JButton("R");
    button_21.setMaximumSize(new Dimension(39, 20));
    button_21.setPreferredSize(new Dimension(39, 20));
    button_21.setMargin(new Insets(0, 0, 0, 0));
    button_21.setFont(new Font("Tahoma", 0, 9));
    button_21.setBounds(94, 45, 24, 23);
    panel_53.add(button_21);
    button_21.addActionListener(atListner);

    button_26 = new JButton("R");
    button_26.setMaximumSize(new Dimension(39, 18));
    button_26.setPreferredSize(new Dimension(39, 20));
    button_26.setMargin(new Insets(0, 0, 0, 0));
    button_26.setFont(new Font("Tahoma", 0, 9));
    button_26.setBounds(94, 77, 24, 23);
    panel_53.add(button_26);
    button_26.addActionListener(atListner);

    button_46 = new JButton("R");
    button_46.setMaximumSize(new Dimension(39, 18));
    button_46.setPreferredSize(new Dimension(39, 20));
    button_46.setMargin(new Insets(0, 0, 0, 0));
    button_46.setFont(new Font("Tahoma", 0, 9));
    button_46.setBounds(94, 109, 24, 23);
    panel_53.add(button_46);
    button_46.addActionListener(atListner);

    button_56 = new JButton("R");
    button_56.setMaximumSize(new Dimension(39, 18));
    button_56.setPreferredSize(new Dimension(39, 20));
    button_56.setMargin(new Insets(0, 0, 0, 0));
    button_56.setFont(new Font("Tahoma", 0, 9));
    button_56.setBounds(94, 139, 24, 23);
    panel_53.add(button_56);
    button_56.addActionListener(atListner);

    button_62 = new JButton("R");
    button_62.setMaximumSize(new Dimension(39, 18));
    button_62.setPreferredSize(new Dimension(39, 20));
    button_62.setMargin(new Insets(0, 0, 0, 0));
    button_62.setFont(new Font("Tahoma", 0, 9));
    button_62.setBounds(94, 164, 24, 23);
    panel_53.add(button_62);
    button_62.addActionListener(atListner);

    button_63 = new JButton("R");
    button_63.setMaximumSize(new Dimension(39, 18));
    button_63.setPreferredSize(new Dimension(39, 20));
    button_63.setMargin(new Insets(0, 0, 0, 0));
    button_63.setFont(new Font("Tahoma", 0, 9));
    button_63.setBounds(94, 193, 24, 23);
    panel_53.add(button_63);
    button_63.addActionListener(atListner);

    button_64 = new JButton("R");
    button_64.setMaximumSize(new Dimension(39, 18));
    button_64.setPreferredSize(new Dimension(39, 20));
    button_64.setMargin(new Insets(0, 0, 0, 0));
    button_64.setFont(new Font("Tahoma", 0, 9));
    button_64.setBounds(94, 218, 24, 23);
    panel_53.add(button_64);
    button_64.addActionListener(atListner);

    button_65 = new JButton("R");
    button_65.setMaximumSize(new Dimension(39, 18));
    button_65.setPreferredSize(new Dimension(39, 20));
    button_65.setMargin(new Insets(0, 0, 0, 0));
    button_65.setFont(new Font("Tahoma", 0, 9));
    button_65.setBounds(94, 243, 24, 23);
    panel_53.add(button_65);
    button_65.addActionListener(atListner);

    button_66 = new JButton("R");
    button_66.setMaximumSize(new Dimension(39, 18));
    button_66.setPreferredSize(new Dimension(39, 20));
    button_66.setMargin(new Insets(0, 0, 0, 0));
    button_66.setFont(new Font("Tahoma", 0, 9));
    button_66.setBounds(94, 266, 24, 23);
    panel_53.add(button_66);
    button_66.addActionListener(atListner);

    button_67 = new JButton("R");
    button_67.setMaximumSize(new Dimension(39, 18));
    button_67.setPreferredSize(new Dimension(39, 20));
    button_67.setMargin(new Insets(0, 0, 0, 0));
    button_67.setFont(new Font("Tahoma", 0, 9));
    button_67.setBounds(94, 290, 24, 23);
    panel_53.add(button_67);
    button_67.addActionListener(atListner);

    button_68 = new JButton("R");
    button_68.setMaximumSize(new Dimension(39, 18));
    button_68.setPreferredSize(new Dimension(39, 20));
    button_68.setMargin(new Insets(0, 0, 0, 0));
    button_68.setFont(new Font("Tahoma", 0, 9));
    button_68.setBounds(94, 316, 24, 23);
    panel_53.add(button_68);
    button_68.addActionListener(atListner);

    button_69 = new JButton("R");
    button_69.setMaximumSize(new Dimension(39, 18));
    button_69.setPreferredSize(new Dimension(39, 20));
    button_69.setMargin(new Insets(0, 0, 0, 0));
    button_69.setFont(new Font("Tahoma", 0, 9));
    button_69.setBounds(94, 350, 24, 23);
    panel_53.add(button_69);
    button_69.addActionListener(atListner);

    button_70 = new JButton("R");
    button_70.setMaximumSize(new Dimension(39, 18));
    button_70.setPreferredSize(new Dimension(39, 20));
    button_70.setMargin(new Insets(0, 0, 0, 0));
    button_70.setFont(new Font("Tahoma", 0, 9));
    button_70.setBounds(94, 375, 24, 23);
    panel_53.add(button_70);
    button_70.addActionListener(atListner);

    button_71 = new JButton("R");
    button_71.setMaximumSize(new Dimension(39, 18));
    button_71.setPreferredSize(new Dimension(39, 20));
    button_71.setMargin(new Insets(0, 0, 0, 0));
    button_71.setFont(new Font("Tahoma", 0, 9));
    button_71.setBounds(94, 400, 24, 23);
    panel_53.add(button_71);
    button_71.addActionListener(atListner);

    button_72 = new JButton("R");
    button_72.setMaximumSize(new Dimension(39, 18));
    button_72.setPreferredSize(new Dimension(39, 20));
    button_72.setMargin(new Insets(0, 0, 0, 0));
    button_72.setFont(new Font("Tahoma", 0, 9));
    button_72.setBounds(94, 425, 24, 23);
    panel_53.add(button_72);
    button_72.addActionListener(atListner);

    button_73 = new JButton("R");
    button_73.setMaximumSize(new Dimension(39, 18));
    button_73.setPreferredSize(new Dimension(39, 20));
    button_73.setMargin(new Insets(0, 0, 0, 0));
    button_73.setFont(new Font("Tahoma", 0, 9));
    button_73.setBounds(94, 450, 24, 23);
    panel_53.add(button_73);
    button_73.addActionListener(atListner);

    button_74 = new JButton("R");
    button_74.setMinimumSize(new Dimension(39, 18));
    button_74.setMaximumSize(new Dimension(39, 18));

    button_74.setPreferredSize(new Dimension(39, 20));
    button_74.setMargin(new Insets(0, 0, 0, 0));
    button_74.setFont(new Font("Tahoma", 0, 9));
    button_74.setBounds(246, 42, 24, 23);
    panel_53.add(button_74);
    button_74.addActionListener(atListner);

    button_75 = new JButton("R");
    button_75.setMaximumSize(new Dimension(39, 18));
    button_75.setPreferredSize(new Dimension(39, 20));
    button_75.setMargin(new Insets(0, 0, 0, 0));
    button_75.setFont(new Font("Tahoma", 0, 9));
    button_75.setBounds(246, 75, 24, 23);
    panel_53.add(button_75);
    button_75.addActionListener(atListner);

    button_76 = new JButton("R");
    button_76.setMaximumSize(new Dimension(39, 18));
    button_76.setPreferredSize(new Dimension(39, 20));
    button_76.setMargin(new Insets(0, 0, 0, 0));
    button_76.setFont(new Font("Tahoma", 0, 9));
    button_76.setBounds(246, 108, 24, 23);
    panel_53.add(button_76);
    button_76.addActionListener(atListner);

    button_77 = new JButton("R");
    button_77.setMaximumSize(new Dimension(39, 18));
    button_77.setPreferredSize(new Dimension(39, 20));
    button_77.setMargin(new Insets(0, 0, 0, 0));
    button_77.setFont(new Font("Tahoma", 0, 9));
    button_77.setBounds(246, 139, 24, 23);
    panel_53.add(button_77);
    button_77.addActionListener(atListner);

    button_78 = new JButton("R");
    button_78.setMaximumSize(new Dimension(39, 18));
    button_78.setPreferredSize(new Dimension(39, 20));
    button_78.setMargin(new Insets(0, 0, 0, 0));
    button_78.setFont(new Font("Tahoma", 0, 9));
    button_78.setBounds(246, 164, 24, 23);
    panel_53.add(button_78);
    button_78.addActionListener(atListner);

    button_79 = new JButton("R");
    button_79.setMaximumSize(new Dimension(39, 18));
    button_79.setPreferredSize(new Dimension(39, 20));
    button_79.setMargin(new Insets(0, 0, 0, 0));
    button_79.setFont(new Font("Tahoma", 0, 9));
    button_79.setBounds(246, 193, 24, 23);
    panel_53.add(button_79);
    button_79.addActionListener(atListner);

    button_80 = new JButton("R");
    button_80.setMaximumSize(new Dimension(39, 18));
    button_80.setPreferredSize(new Dimension(39, 20));
    button_80.setMargin(new Insets(0, 0, 0, 0));
    button_80.setFont(new Font("Tahoma", 0, 9));
    button_80.setBounds(246, 218, 24, 23);
    panel_53.add(button_80);
    button_80.addActionListener(atListner);

    button_81 = new JButton("R");
    button_81.setMaximumSize(new Dimension(39, 18));
    button_81.setPreferredSize(new Dimension(39, 20));
    button_81.setMargin(new Insets(0, 0, 0, 0));
    button_81.setFont(new Font("Tahoma", 0, 9));
    button_81.setBounds(246, 243, 24, 23);
    panel_53.add(button_81);
    button_81.addActionListener(atListner);

    button_82 = new JButton("R");
    button_82.setMaximumSize(new Dimension(39, 18));
    button_82.setPreferredSize(new Dimension(39, 20));
    button_82.setMargin(new Insets(0, 0, 0, 0));
    button_82.setFont(new Font("Tahoma", 0, 9));
    button_82.setBounds(246, 269, 24, 23);
    panel_53.add(button_82);
    button_82.addActionListener(atListner);

    button_83 = new JButton("R");
    button_83.setMaximumSize(new Dimension(39, 18));
    button_83.setPreferredSize(new Dimension(39, 20));
    button_83.setMargin(new Insets(0, 0, 0, 0));
    button_83.setFont(new Font("Tahoma", 0, 9));
    button_83.setBounds(246, 294, 24, 23);
    panel_53.add(button_83);
    button_83.addActionListener(atListner);

    button_84 = new JButton("R");
    button_84.setMaximumSize(new Dimension(39, 18));
    button_84.setPreferredSize(new Dimension(39, 20));
    button_84.setMargin(new Insets(0, 0, 0, 0));
    button_84.setFont(new Font("Tahoma", 0, 9));
    button_84.setBounds(246, 320, 24, 23);
    panel_53.add(button_84);
    button_84.addActionListener(atListner);

    button_85 = new JButton("R");
    button_85.setMaximumSize(new Dimension(39, 18));
    button_85.setPreferredSize(new Dimension(39, 20));
    button_85.setMargin(new Insets(0, 0, 0, 0));
    button_85.setFont(new Font("Tahoma", 0, 9));
    button_85.setBounds(246, 350, 24, 23);
    panel_53.add(button_85);
    button_85.addActionListener(atListner);

    button_87 = new JButton("R");
    button_87.setMaximumSize(new Dimension(39, 18));
    button_87.setPreferredSize(new Dimension(39, 20));
    button_87.setMargin(new Insets(0, 0, 0, 0));
    button_87.setFont(new Font("Tahoma", 0, 9));
    button_87.setBounds(246, 398, 24, 23);
    panel_53.add(button_87);
    button_87.addActionListener(atListner);

    button_88 = new JButton("R");
    button_88.setMaximumSize(new Dimension(39, 18));
    button_88.setPreferredSize(new Dimension(39, 20));
    button_88.setMargin(new Insets(0, 0, 0, 0));
    button_88.setFont(new Font("Tahoma", 0, 9));
    button_88.setBounds(246, 425, 24, 23);
    panel_53.add(button_88);
    button_88.addActionListener(atListner);

    button_89 = new JButton("R");
    button_89.setMaximumSize(new Dimension(39, 18));
    button_89.setPreferredSize(new Dimension(39, 20));
    button_89.setMargin(new Insets(0, 0, 0, 0));
    button_89.setFont(new Font("Tahoma", 0, 9));
    button_89.setBounds(374, 429, 24, 23);
    panel_53.add(button_89);
    button_89.addActionListener(atListner);

    button_86 = new JButton("R");
    button_86.setMinimumSize(new Dimension(39, 18));
    button_86.setPreferredSize(new Dimension(39, 20));
    button_86.setMargin(new Insets(0, 0, 0, 0));
    button_86.setFont(new Font("Tahoma", 0, 9));
    button_86.setBounds(374, 42, 24, 23);
    panel_53.add(button_86);
    button_86.addActionListener(atListner);

    button_90 = new JButton("R");
    button_90.setMaximumSize(new Dimension(39, 18));
    button_90.setMinimumSize(new Dimension(39, 18));
    button_90.setPreferredSize(new Dimension(39, 20));
    button_90.setMargin(new Insets(0, 0, 0, 0));
    button_90.setFont(new Font("Tahoma", 0, 9));
    button_90.setBounds(374, 75, 24, 23);
    panel_53.add(button_90);
    button_90.addActionListener(atListner);

    button_91 = new JButton("R");
    button_91.setMaximumSize(new Dimension(39, 18));
    button_91.setMinimumSize(new Dimension(39, 18));
    button_91.setPreferredSize(new Dimension(39, 20));
    button_91.setMargin(new Insets(0, 0, 0, 0));
    button_91.setFont(new Font("Tahoma", 0, 9));
    button_91.setBounds(374, 107, 24, 23);
    panel_53.add(button_91);
    button_91.addActionListener(atListner);

    button_92 = new JButton("R");
    button_92.setMaximumSize(new Dimension(39, 18));
    button_92.setMinimumSize(new Dimension(39, 18));
    button_92.setPreferredSize(new Dimension(39, 20));
    button_92.setMargin(new Insets(0, 0, 0, 0));
    button_92.setFont(new Font("Tahoma", 0, 9));
    button_92.setBounds(374, 139, 24, 23);
    panel_53.add(button_92);
    button_92.addActionListener(atListner);

    button_93 = new JButton("R");
    button_93.setMaximumSize(new Dimension(39, 18));
    button_93.setMinimumSize(new Dimension(39, 18));
    button_93.setPreferredSize(new Dimension(39, 20));
    button_93.setMargin(new Insets(0, 0, 0, 0));
    button_93.setFont(new Font("Tahoma", 0, 9));
    button_93.setBounds(374, 164, 24, 23);
    panel_53.add(button_93);
    button_93.addActionListener(atListner);

    button_94 = new JButton("R");
    button_94.setMaximumSize(new Dimension(39, 18));
    button_94.setMinimumSize(new Dimension(39, 18));
    button_94.setPreferredSize(new Dimension(39, 20));
    button_94.setMargin(new Insets(0, 0, 0, 0));
    button_94.setFont(new Font("Tahoma", 0, 9));
    button_94.setBounds(374, 193, 24, 23);
    panel_53.add(button_94);
    button_94.addActionListener(atListner);

    button_95 = new JButton("R");
    button_95.setMaximumSize(new Dimension(39, 18));
    button_95.setMinimumSize(new Dimension(39, 18));
    button_95.setPreferredSize(new Dimension(39, 20));
    button_95.setMargin(new Insets(0, 0, 0, 0));
    button_95.setFont(new Font("Tahoma", 0, 9));
    button_95.setBounds(374, 218, 24, 23);
    panel_53.add(button_95);
    button_95.addActionListener(atListner);

    button_96 = new JButton("R");
    button_96.setMaximumSize(new Dimension(39, 18));
    button_96.setMinimumSize(new Dimension(39, 18));
    button_96.setPreferredSize(new Dimension(39, 20));
    button_96.setMargin(new Insets(0, 0, 0, 0));
    button_96.setFont(new Font("Tahoma", 0, 9));
    button_96.setBounds(374, 243, 24, 23);
    panel_53.add(button_96);
    button_96.addActionListener(atListner);

    button_97 = new JButton("R");
    button_97.setMaximumSize(new Dimension(39, 18));
    button_97.setMinimumSize(new Dimension(39, 18));
    button_97.setPreferredSize(new Dimension(39, 20));
    button_97.setMargin(new Insets(0, 0, 0, 0));
    button_97.setFont(new Font("Tahoma", 0, 9));
    button_97.setBounds(374, 267, 24, 23);
    panel_53.add(button_97);
    button_97.addActionListener(atListner);

    button_98 = new JButton("R");
    button_98.setMaximumSize(new Dimension(39, 18));
    button_98.setMinimumSize(new Dimension(39, 18));
    button_98.setPreferredSize(new Dimension(39, 20));
    button_98.setMargin(new Insets(0, 0, 0, 0));
    button_98.setFont(new Font("Tahoma", 0, 9));
    button_98.setBounds(374, 292, 24, 23);
    panel_53.add(button_98);
    button_98.addActionListener(atListner);

    button_99 = new JButton("R");
    button_99.setMaximumSize(new Dimension(39, 18));
    button_99.setMinimumSize(new Dimension(39, 18));
    button_99.setPreferredSize(new Dimension(39, 20));
    button_99.setMargin(new Insets(0, 0, 0, 0));
    button_99.setFont(new Font("Tahoma", 0, 9));
    button_99.setBounds(374, 316, 24, 23);
    panel_53.add(button_99);
    button_99.addActionListener(atListner);

    button_100 = new JButton("R");
    button_100.setMaximumSize(new Dimension(39, 18));
    button_100.setPreferredSize(new Dimension(39, 20));
    button_100.setMargin(new Insets(0, 0, 0, 0));
    button_100.setFont(new Font("Tahoma", 0, 9));
    button_100.setBounds(374, 350, 24, 23);
    panel_53.add(button_100);
    button_100.addActionListener(atListner);

    button_101 = new JButton("R");
    button_101.setMaximumSize(new Dimension(39, 18));
    button_101.setPreferredSize(new Dimension(39, 20));
    button_101.setMargin(new Insets(0, 0, 0, 0));
    button_101.setFont(new Font("Tahoma", 0, 9));
    button_101.setBounds(374, 375, 24, 23);
    panel_53.add(button_101);
    button_101.addActionListener(atListner);

    button_102 = new JButton("R");
    button_102.setMaximumSize(new Dimension(39, 18));
    button_102.setPreferredSize(new Dimension(39, 20));
    button_102.setMargin(new Insets(0, 0, 0, 0));
    button_102.setFont(new Font("Tahoma", 0, 9));
    button_102.setBounds(374, 398, 24, 23);
    panel_53.add(button_102);
    button_102.addActionListener(atListner);

    panel_56 = new JPanel();
    panel_52.add(panel_56, "Huawei");
    panel_56.setLayout(null);

    button_103 = new JButton("R");
    button_103.setPreferredSize(new Dimension(39, 20));
    button_103.setMargin(new Insets(0, 0, 0, 0));
    button_103.setFont(new Font("Tahoma", 0, 9));
    button_103.setBounds(113, 34, 24, 23);
    button_103.addActionListener(atListner);
    panel_56.add(button_103);

    button_104 = new JButton("R");
    button_104.setMargin(new Insets(0, 0, 0, 0));
    button_104.setFont(new Font("Tahoma", 0, 9));
    button_104.setBounds(113, 206, 24, 23);
    button_104.addActionListener(atListner);
    panel_56.add(button_104);

    button_105 = new JButton("R");
    button_105.setMargin(new Insets(0, 0, 0, 0));
    button_105.setFont(new Font("Tahoma", 0, 9));
    button_105.setBounds(112, 92, 24, 23);
    button_105.addActionListener(atListner);
    panel_56.add(button_105);

    lblSystemInfo = new JLabel("System Info");
    lblSystemInfo.setFont(new Font("Tahoma", 2, 11));
    lblSystemInfo.setBounds(11, 41, 90, 14);
    panel_56.add(lblSystemInfo);

    lblIccid_1 = new JLabel("ICCID");
    lblIccid_1.setFont(new Font("Tahoma", 2, 11));
    lblIccid_1.setBounds(11, 211, 76, 14);
    panel_56.add(lblIccid_1);

    lblSystemInfo_1 = new JLabel("Network Time");
    lblSystemInfo_1.setFont(new Font("Tahoma", 2, 11));
    lblSystemInfo_1.setBounds(12, 98, 99, 14);
    panel_56.add(lblSystemInfo_1);

    lblEchoCancel = new JLabel("Echo Cancel Mode");
    lblEchoCancel.setFont(new Font("Tahoma", 2, 11));
    lblEchoCancel.setBounds(13, 264, 109, 14);
    panel_56.add(lblEchoCancel);

    button_106 = new JButton("R");
    button_106.setMargin(new Insets(0, 0, 0, 0));
    button_106.setFont(new Font("Tahoma", 0, 9));
    button_106.setBounds(113, 260, 24, 23);
    button_106.addActionListener(atListner);
    panel_56.add(button_106);

    lblImeisv = new JLabel("IMEISV");
    lblImeisv.setFont(new Font("Tahoma", 2, 11));
    lblImeisv.setBounds(12, 155, 99, 14);
    panel_56.add(lblImeisv);

    button_109 = new JButton("R");
    button_109.setMargin(new Insets(0, 0, 0, 0));
    button_109.setFont(new Font("Tahoma", 0, 9));
    button_109.setBounds(113, 151, 24, 23);
    button_109.addActionListener(atListner);
    panel_56.add(button_109);

    lblGpioSettings = new JLabel("GPIO Settings");
    lblGpioSettings.setFont(new Font("Tahoma", 2, 11));
    lblGpioSettings.setBounds(13, 181, 99, 14);
    panel_56.add(lblGpioSettings);

    button_110 = new JButton("R");
    button_110.setMargin(new Insets(0, 0, 0, 0));
    button_110.setFont(new Font("Tahoma", 0, 9));
    button_110.setBounds(113, 177, 24, 23);
    button_110.addActionListener(atListner);
    panel_56.add(button_110);

    lblUssdMode = new JLabel("USSD Mode");
    lblUssdMode.setFont(new Font("Tahoma", 2, 11));
    lblUssdMode.setBounds(10, 238, 109, 14);
    panel_56.add(lblUssdMode);

    button_111 = new JButton("R");
    button_111.setMargin(new Insets(0, 0, 0, 0));
    button_111.setFont(new Font("Tahoma", 0, 9));
    button_111.setBounds(113, 234, 24, 23);
    button_111.addActionListener(atListner);
    panel_56.add(button_111);

    lblWakeupCfg = new JLabel("WakeUp Config");
    lblWakeupCfg.setFont(new Font("Tahoma", 2, 11));
    lblWakeupCfg.setBounds(12, 127, 76, 14);
    panel_56.add(lblWakeupCfg);

    button_112 = new JButton("R");
    button_112.setPreferredSize(new Dimension(39, 20));
    button_112.setMargin(new Insets(0, 0, 0, 0));
    button_112.setFont(new Font("Tahoma", 0, 9));
    button_112.setBounds(113, 122, 24, 23);
    button_112.addActionListener(atListner);
    panel_56.add(button_112);

    lblConnections = new JLabel("Connections");
    lblConnections.setFont(new Font("Tahoma", 2, 11));
    lblConnections.setBounds(162, 69, 76, 14);
    panel_56.add(lblConnections);

    button_113 = new JButton("R");
    button_113.setPreferredSize(new Dimension(39, 20));
    button_113.setMargin(new Insets(0, 0, 0, 0));
    button_113.setFont(new Font("Tahoma", 0, 9));
    button_113.setBounds(248, 65, 24, 23);
    button_113.addActionListener(atListner);
    panel_56.add(button_113);

    lblStationClass_1 = new JLabel("TCP/IP Init.");
    lblStationClass_1.setFont(new Font("Tahoma", 2, 11));
    lblStationClass_1.setBounds(161, 42, 76, 14);
    panel_56.add(lblStationClass_1);

    button_114 = new JButton("R");
    button_114.setPreferredSize(new Dimension(39, 20));
    button_114.setMargin(new Insets(0, 0, 0, 0));
    button_114.setFont(new Font("Tahoma", 0, 9));
    button_114.setBounds(248, 39, 24, 23);
    button_114.addActionListener(atListner);
    panel_56.add(button_114);

    lblConfig = new JLabel("Config");
    lblConfig.setFont(new Font("Tahoma", 2, 11));
    lblConfig.setBounds(163, 128, 90, 14);
    panel_56.add(lblConfig);

    button_115 = new JButton("R");
    button_115.setPreferredSize(new Dimension(39, 20));
    button_115.setMargin(new Insets(0, 0, 0, 0));
    button_115.setFont(new Font("Tahoma", 0, 9));
    button_115.setBounds(249, 93, 24, 23);
    button_115.addActionListener(atListner);
    panel_56.add(button_115);

    lblGpsMode = new JLabel("GPS Mode");
    lblGpsMode.setFont(new Font("Tahoma", 2, 11));
    lblGpsMode.setBounds(163, 211, 75, 14);
    panel_56.add(lblGpsMode);

    button_116 = new JButton("R");
    button_116.setPreferredSize(new Dimension(39, 20));
    button_116.setMargin(new Insets(0, 0, 0, 0));
    button_116.setFont(new Font("Tahoma", 0, 9));
    button_116.setBounds(249, 122, 24, 23);
    button_116.addActionListener(atListner);
    panel_56.add(button_116);

    lblIpStatistics = new JLabel("Link Statistics");
    lblIpStatistics.setFont(new Font("Tahoma", 2, 11));
    lblIpStatistics.setBounds(163, 158, 83, 14);
    panel_56.add(lblIpStatistics);

    button_117 = new JButton("R");
    button_117.setPreferredSize(new Dimension(39, 20));
    button_117.setMargin(new Insets(0, 0, 0, 0));
    button_117.setFont(new Font("Tahoma", 0, 9));
    button_117.setBounds(249, 152, 24, 23);
    button_117.addActionListener(atListner);
    panel_56.add(button_117);

    lblListen = new JLabel("Listen");
    lblListen.setFont(new Font("Tahoma", 2, 11));
    lblListen.setBounds(163, 99, 76, 14);
    panel_56.add(lblListen);

    button_119 = new JButton("R");
    button_119.setPreferredSize(new Dimension(39, 20));
    button_119.setMargin(new Insets(0, 0, 0, 0));
    button_119.setFont(new Font("Tahoma", 0, 9));
    button_119.setBounds(249, 207, 24, 23);
    button_119.addActionListener(atListner);
    panel_56.add(button_119);

    label_37 = new JLabel("Reset");
    label_37.setFont(new Font("Tahoma", 2, 11));
    label_37.setBounds(12, 497, 46, 14);
    panel_56.add(label_37);

    button_120 = new JButton("R");
    button_120.setPreferredSize(new Dimension(39, 20));
    button_120.setMargin(new Insets(0, 0, 0, 0));
    button_120.setFont(new Font("Tahoma", 0, 9));
    button_120.setBounds(112, 493, 24, 23);
    button_120.addActionListener(atListner);
    panel_56.add(button_120);

    lblPcmAudio = new JLabel("PCM Audio");
    lblPcmAudio.setFont(new Font("Tahoma", 2, 11));
    lblPcmAudio.setBounds(13, 289, 90, 14);
    panel_56.add(lblPcmAudio);

    button_121 = new JButton("R");
    button_121.setPreferredSize(new Dimension(39, 20));
    button_121.setMargin(new Insets(0, 0, 0, 0));
    button_121.setFont(new Font("Tahoma", 0, 9));
    button_121.setBounds(113, 285, 24, 23);
    button_121.addActionListener(atListner);
    panel_56.add(button_121);

    lblSimToolkitInterface = new JLabel("SIM Toolkit ");
    lblSimToolkitInterface.setFont(new Font("Tahoma", 2, 11));
    lblSimToolkitInterface.setBounds(13, 314, 98, 14);
    panel_56.add(lblSimToolkitInterface);

    button_122 = new JButton("R");
    button_122.setPreferredSize(new Dimension(39, 20));
    button_122.setMargin(new Insets(0, 0, 0, 0));
    button_122.setFont(new Font("Tahoma", 0, 9));
    button_122.setBounds(113, 310, 24, 23);
    button_122.addActionListener(atListner);
    panel_56.add(button_122);

    button_123 = new JButton("R");
    button_123.setPreferredSize(new Dimension(39, 20));
    button_123.setMargin(new Insets(0, 0, 0, 0));
    button_123.setFont(new Font("Tahoma", 0, 9));
    button_123.setBounds(249, 287, 24, 23);
    button_123.addActionListener(atListner);
    panel_56.add(button_123);

    lblGpsSessionType = new JLabel("GPS Session");
    lblGpsSessionType.setFont(new Font("Tahoma", 2, 11));
    lblGpsSessionType.setBounds(163, 241, 90, 14);
    panel_56.add(lblGpsSessionType);

    button_124 = new JButton("R");
    button_124.setPreferredSize(new Dimension(39, 20));
    button_124.setMargin(new Insets(0, 0, 0, 0));
    button_124.setFont(new Font("Tahoma", 0, 9));
    button_124.setBounds(249, 237, 24, 23);
    button_124.addActionListener(atListner);
    panel_56.add(button_124);

    lblQos_1 = new JLabel("QoS");
    lblQos_1.setFont(new Font("Tahoma", 2, 11));
    lblQos_1.setBounds(163, 266, 83, 14);
    panel_56.add(lblQos_1);

    button_125 = new JButton("R");
    button_125.setPreferredSize(new Dimension(39, 20));
    button_125.setMargin(new Insets(0, 0, 0, 0));
    button_125.setFont(new Font("Tahoma", 0, 9));
    button_125.setBounds(249, 262, 24, 23);
    button_125.addActionListener(atListner);
    panel_56.add(button_125);

    lblSessionLock = new JLabel("Session Lock");
    lblSessionLock.setFont(new Font("Tahoma", 2, 11));
    lblSessionLock.setBounds(163, 291, 75, 14);
    panel_56.add(lblSessionLock);

    button_126 = new JButton("R");
    button_126.setPreferredSize(new Dimension(39, 20));
    button_126.setMargin(new Insets(0, 0, 0, 0));
    button_126.setFont(new Font("Tahoma", 0, 9));
    button_126.setBounds(249, 287, 24, 23);
    button_126.addActionListener(atListner);
    panel_56.add(button_126);

    lblFotaMode = new JLabel("FOTA Mode");

    panel_56.add(lblFotaMode);

    button_128 = new JButton("R");
    button_128.setPreferredSize(new Dimension(39, 20));
    button_128.setMargin(new Insets(0, 0, 0, 0));
    button_128.setFont(new Font("Tahoma", 0, 9));
    button_128.setBounds(387, 40, 24, 23);
    button_128.addActionListener(atListner);
    panel_56.add(button_128);

    lblTemperature_1 = new JLabel("Temperature");
    lblTemperature_1.setFont(new Font("Tahoma", 2, 11));
    lblTemperature_1.setBounds(13, 339, 76, 14);
    panel_56.add(lblTemperature_1);

    button_129 = new JButton("R");
    button_129.setPreferredSize(new Dimension(39, 20));
    button_129.setMargin(new Insets(0, 0, 0, 0));
    button_129.setFont(new Font("Tahoma", 0, 9));
    button_129.setBounds(113, 335, 24, 23);
    button_129.addActionListener(atListner);
    panel_56.add(button_129);

    lblTempProtection = new JLabel("Temp. Protection");
    lblTempProtection.setFont(new Font("Tahoma", 2, 11));
    lblTempProtection.setBounds(13, 364, 98, 14);
    panel_56.add(lblTempProtection);

    button_130 = new JButton("R");
    button_130.setPreferredSize(new Dimension(39, 20));
    button_130.setMargin(new Insets(0, 0, 0, 0));
    button_130.setFont(new Font("Tahoma", 0, 9));
    button_130.setBounds(113, 360, 24, 23);
    button_130.addActionListener(atListner);
    panel_56.add(button_130);

    label_47 = new JLabel("Alarms");
    label_47.setFont(new Font("Tahoma", 2, 11));
    label_47.setBounds(13, 444, 46, 14);
    panel_56.add(label_47);

    button_131 = new JButton("R");
    button_131.setPreferredSize(new Dimension(39, 20));
    button_131.setMargin(new Insets(0, 0, 0, 0));
    button_131.setFont(new Font("Tahoma", 0, 9));
    button_131.setBounds(113, 440, 24, 23);
    button_131.addActionListener(atListner);
    panel_56.add(button_131);

    lblFotaConnParm = new JLabel("Conn. Parms");
    lblFotaConnParm.setFont(new Font("Tahoma", 2, 11));
    lblFotaConnParm.setBounds(301, 70, 76, 14);
    panel_56.add(lblFotaConnParm);

    button_132 = new JButton("R");
    button_132.setPreferredSize(new Dimension(39, 20));
    button_132.setMargin(new Insets(0, 0, 0, 0));
    button_132.setFont(new Font("Tahoma", 0, 9));
    button_132.setBounds(387, 66, 24, 23);
    button_132.addActionListener(atListner);
    panel_56.add(button_132);

    lblFotaStatus = new JLabel("FOTA Status");
    lblFotaStatus.setFont(new Font("Tahoma", 2, 11));
    lblFotaStatus.setBounds(301, 99, 76, 14);
    panel_56.add(lblFotaStatus);

    button_133 = new JButton("R");
    button_133.setPreferredSize(new Dimension(39, 20));
    button_133.setMargin(new Insets(0, 0, 0, 0));
    button_133.setFont(new Font("Tahoma", 0, 9));
    button_133.setBounds(387, 95, 24, 23);
    button_133.addActionListener(atListner);
    panel_56.add(button_133);

    lblUpgradeStatus = new JLabel("Upgrade Status");
    lblUpgradeStatus.setFont(new Font("Tahoma", 2, 11));
    lblUpgradeStatus.setBounds(301, 127, 83, 14);
    panel_56.add(lblUpgradeStatus);

    lblGpsType = new JLabel("GPS Type");
    lblGpsType.setFont(new Font("Tahoma", 2, 11));
    lblGpsType.setBounds(163, 316, 83, 14);
    panel_56.add(lblGpsType);

    button_135 = new JButton("R");
    button_135.setPreferredSize(new Dimension(39, 20));
    button_135.setMargin(new Insets(0, 0, 0, 0));
    button_135.setFont(new Font("Tahoma", 0, 9));
    button_135.setBounds(249, 312, 24, 23);
    button_135.addActionListener(atListner);
    panel_56.add(button_135);

    label_53 = new JLabel("Mute Control");
    label_53.setFont(new Font("Tahoma", 2, 11));
    label_53.setBounds(13, 470, 90, 14);
    panel_56.add(label_53);

    lblGpsOrGnss = new JLabel("GPS or GNSS?");
    lblGpsOrGnss.setFont(new Font("Tahoma", 2, 11));
    lblGpsOrGnss.setBounds(163, 341, 75, 14);
    panel_56.add(lblGpsOrGnss);

    button_136 = new JButton("R");
    button_136.setPreferredSize(new Dimension(39, 20));
    button_136.setMargin(new Insets(0, 0, 0, 0));
    button_136.setFont(new Font("Tahoma", 0, 9));
    button_136.setBounds(249, 337, 24, 23);
    button_136.addActionListener(atListner);
    panel_56.add(button_136);

    lblNdisConnStatus = new JLabel("NDIS Conn. Status");
    lblNdisConnStatus.setFont(new Font("Tahoma", 2, 11));
    lblNdisConnStatus.setBounds(13, 389, 98, 14);
    panel_56.add(lblNdisConnStatus);

    button_137 = new JButton("R");
    button_137.setPreferredSize(new Dimension(39, 20));
    button_137.setMargin(new Insets(0, 0, 0, 0));
    button_137.setFont(new Font("Tahoma", 0, 9));
    button_137.setBounds(113, 385, 24, 23);
    button_137.addActionListener(atListner);
    panel_56.add(button_137);

    lblVoicedataPref = new JLabel("Voice/Data Pref.");
    lblVoicedataPref.setFont(new Font("Tahoma", 2, 11));
    lblVoicedataPref.setBounds(13, 419, 90, 14);
    panel_56.add(lblVoicedataPref);

    button_138 = new JButton("R");
    button_138.setPreferredSize(new Dimension(39, 20));
    button_138.setMargin(new Insets(0, 0, 0, 0));
    button_138.setFont(new Font("Tahoma", 0, 9));
    button_138.setBounds(113, 415, 24, 23);
    button_138.addActionListener(atListner);
    panel_56.add(button_138);

    label_57 = new JLabel("Address Type");
    label_57.setFont(new Font("Tahoma", 2, 11));
    label_57.setBounds(163, 497, 76, 14);
    panel_56.add(label_57);

    button_139 = new JButton("R");
    button_139.setPreferredSize(new Dimension(39, 20));
    button_139.setMargin(new Insets(0, 0, 0, 0));
    button_139.setFont(new Font("Tahoma", 0, 9));
    button_139.setBounds(249, 497, 24, 23);
    button_139.addActionListener(atListner);
    panel_56.add(button_139);

    lblGeneral_1 = new JLabel("General");
    lblGeneral_1.setForeground(new Color(51, 102, 204));
    lblGeneral_1.setFont(new Font("Tahoma", 1, 11));
    lblGeneral_1.setBounds(13, 13, 90, 14);
    panel_56.add(lblGeneral_1);

    lblNetworkTime = new JLabel("System Config");
    lblNetworkTime.setFont(new Font("Tahoma", 2, 11));
    lblNetworkTime.setBounds(11, 68, 99, 14);
    panel_56.add(lblNetworkTime);

    button_140 = new JButton("R");
    button_140.setPreferredSize(new Dimension(39, 20));
    button_140.setMargin(new Insets(0, 0, 0, 0));
    button_140.setFont(new Font("Tahoma", 0, 9));
    button_140.setBounds(113, 64, 24, 23);
    button_140.addActionListener(atListner);
    panel_56.add(button_140);

    button_141 = new JButton("R");
    button_141.setPreferredSize(new Dimension(39, 20));
    button_141.setMargin(new Insets(0, 0, 0, 0));
    button_141.setFont(new Font("Tahoma", 0, 9));
    button_141.setBounds(113, 465, 24, 23);
    panel_56.add(button_141);

    lblTcpip_1 = new JLabel("TCP/IP");
    lblTcpip_1.setForeground(new Color(51, 102, 204));
    lblTcpip_1.setFont(new Font("Tahoma", 1, 11));
    lblTcpip_1.setBounds(162, 13, 76, 14);
    panel_56.add(lblTcpip_1);

    button_149 = new JButton("R");
    button_149.setMargin(new Insets(0, 0, 0, 0));
    button_149.setFont(new Font("Tahoma", 0, 9));
    button_149.setBounds(387, 123, 24, 23);
    button_149.addActionListener(atListner);
    panel_56.add(button_149);

    lblGps_1 = new JLabel("GPS");
    lblGps_1.setForeground(new Color(51, 102, 204));
    lblGps_1.setFont(new Font("Tahoma", 1, 11));
    lblGps_1.setBounds(162, 181, 46, 14);
    panel_56.add(lblGps_1);

    lblFota_1 = new JLabel("FOTA");
    lblFota_1.setForeground(new Color(51, 102, 204));
    lblFota_1.setFont(new Font("Tahoma", 1, 11));
    lblFota_1.setBounds(301, 13, 76, 14);
    panel_56.add(lblFota_1);

    lblFotaMode_1 = new JLabel("FOTA Mode");
    lblFotaMode_1.setFont(new Font("Tahoma", 2, 11));
    lblFotaMode_1.setBounds(301, 41, 76, 14);
    panel_56.add(lblFotaMode_1);

    chckbxApppendOutput = new JCheckBox("Apppend Output");
    chckbxApppendOutput.setBounds(289, 7, 126, 23);
    panel_51.add(chckbxApppendOutput);

    lblManufacturer_1 = new JLabel("Manufacturer");
    lblManufacturer_1.setBounds(10, 11, 92, 14);
    panel_51.add(lblManufacturer_1);

    comboBox_20 = new JComboBox(module_all);
    comboBox_20.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout)panel_52.getLayout();
        cl.show(panel_52, (String)e.getItem());
      }
    });
    comboBox_20.setBounds(93, 8, 103, 20);
    panel_51.add(comboBox_20);

    separator_2 = new JSeparator();
    separator_2.setBounds(0, 37, 421, 2);
    panel_51.add(separator_2);

    panel_1 = new JPanel();
    tabbedPane.addTab("Command Mode", null, panel_1, null);
    panel_1.setBorder(new TitledBorder(null, "AT Commands", 4, 2, null, null));
    panel_1.setLayout(null);

    textField = new JTextField(passed_cmd);
    textField.setBounds(10, 59, 297, 23);
    panel_1.add(textField);
    textField.setColumns(10);

    btnSubmit = new JButton("Submit");
    btnSubmit.setBounds(317, 58, 89, 24);
    panel_1.add(btnSubmit);

    comboBox_2 = new JComboBox(at_commands);

    comboBox_2.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ie) {
        String str = (String)comboBox_2.getSelectedItem();
        int idx = comboBox_2.getSelectedIndex();
        String exp = at_command_examples[idx];
        textArea_2.setText(exp);
        textField.setText(str);
      }
    });
    comboBox_2.setBounds(147, 25, 112, 23);
    panel_1.add(comboBox_2);

    lblSelectAtCommand = new JLabel("Select AT Command");
    lblSelectAtCommand.setBounds(10, 29, 127, 14);
    panel_1.add(lblSelectAtCommand);

    separator_1 = new JSeparator();
    separator_1.setBounds(331, 92, -24, 144);
    panel_1.add(separator_1);

    textArea_2 = new JTextPane();
    textArea_2.setBounds(10, 62, 247, 318);
    textArea_2.setBackground(UIManager.getColor("Button.background"));
    textArea_2.setEditable(false);
    textArea_2.setContentType("text/html");

    JScrollPane sp2 = new JScrollPane(textArea_2);
    sp2.setBounds(10, 92, 407, 198);

    panel_1.add(sp2);

    btnSubmit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String port1 = (String)comboBox.getSelectedItem();

        if (port1 == null)
        {
          String msg = "Port not selected. Press 'Find Ports'  or 'AutoConnect'.\r\n\r\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "submit_port_not_selected" });
        }
        else
        {
          String ctz = Character.toString('\032');
          String s = textField.getText();
          s = s.replace("^z", ctz);
          s = s + "\r\n";
          mySerial1.setOutputArea(textPane_10);
          String ret = mySerial1.ser_write(s, 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
        }
      }
    });
    panel_2 = new JPanel();
    tabbedPane.addTab("Script Mode", null, panel_2, null);
    panel_2.setLayout(null);

    lblSelectScript = new JLabel("Select Script");
    lblSelectScript.setBounds(27, 51, 79, 14);
    panel_2.add(lblSelectScript);

    comboBox_3 = new JComboBox(at_command_scripts);
    comboBox_3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String str = (String)comboBox_3.getSelectedItem();
        int idx = comboBox_3.getSelectedIndex();
        String exp = at_command_script_content[idx];
        textArea_3.setText(exp);
      }
    });
    comboBox_3.setBounds(116, 48, 137, 20);
    panel_2.add(comboBox_3);

    textArea_3 = new JTextPane();
    JScrollPane sp3 = new JScrollPane(textArea_3);
    sp3.setBounds(27, 94, 358, 370);
    panel_2.add(sp3);

    btnRunScript = new JButton("Run Script");
    btnRunScript.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String port1 = (String)comboBox.getSelectedItem();
            if (port1 == null)
            {
              msg = "Port not selected. Press 'Find Ports'  or 'AutoConnect'.<a href=\"http://m2msupport.net/m2msupport/forums/topic/at-command-tester-troubleshooting-guide/\">AT Command Tester Troubleshooting Guide</a>\r\n\r\n";

              writeOutput(msg);
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "submit_port_not_selected" });
            }
            else
            {
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "script_commands_submit" });
              String s = textArea_3.getText();

              String[] commands = s.split("\n");
              for (int j = 0; j < commands.length; j++)
              {
                String cmdToSend = commands[j];
                if (!cmdToSend.equals(""))
                {
                  String tmp = cmdToSend.substring(0, 2);

                  if (!tmp.equals("//"))
                  {
                    try
                    {
                      Thread.sleep(2000L);
                    } catch (InterruptedException ex) {
                      Thread.currentThread().interrupt();
                    }

                    if (cmdToSend.contains("WAIT="))
                    {
                      String[] tmp2 = cmdToSend.split("WAIT=");
                      int wait_time = Integer.parseInt(tmp2[1]);
                      try {
                        Thread.sleep(wait_time * 1000);
                      } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                      }

                    }
                    else
                    {
                      String ctz = Character.toString('\032');

                      cmdToSend = cmdToSend.replace("^z", ctz);
                      cmdToSend = cmdToSend.replace("<cr>", "\r");
                      cmdToSend = cmdToSend.replace("<lf>", "\n");

                      if (cmdToSend.contains("<!crlf>"))
                      {
                        cmdToSend = cmdToSend.replace("<!crlf>", "");
                      }
                      else
                      {
                        cmdToSend = cmdToSend + "\r\n";
                      }
                      mySerial1.setOutputArea(textPane_10);

                      String ret = mySerial1.ser_write(cmdToSend, 1);

                      if (winflag == 1)
                        win.call("setMessage", new String[] { "Tester", ret }); 
                    }
                  }
                }
              }
            }
            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnRunScript.setBounds(278, 47, 107, 23);
    panel_2.add(btnRunScript);

    btnClear_1 = new JButton("Clear");
    btnClear_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textPane_10.setText("");
      }
    });
    btnClear_1.setBounds(502, 490, 89, 23);
    panel_2.add(btnClear_1);

    btnSaveScript = new JButton("Save Script");
    btnSaveScript.addActionListener(fileSaver);
    btnSaveScript.setBounds(172, 530, 107, 23);
    panel_2.add(btnSaveScript);

    btnLoadScript = new JButton("Load Script");
    btnLoadScript.addActionListener(fileOpener);
    btnLoadScript.setBounds(37, 530, 107, 23);
    panel_2.add(btnLoadScript);

    URLLabel label = new URLLabel("Tutorial: How to use script mode?", "http://m2msupport.net/m2msupport/how-to-use-script-mode-in-at-command-tester/");
    label.setText("Tutorial: How to use script mode?");
    label.setBounds(27, 11, 212, 14);
    panel_2.add(label);

    panel_5 = new JPanel();
    tabbedPane.addTab("Voice Call", null, panel_5, null);
    panel_5.setLayout(null);

    panel_13 = new JPanel();
    panel_13.setBorder(new TitledBorder(null, "Outgoing", 4, 2, null, null));
    panel_13.setBounds(10, 36, 393, 94);
    panel_5.add(panel_13);
    panel_13.setLayout(null);

    lblCallNumber = new JLabel("Call Number");
    lblCallNumber.setBounds(10, 32, 85, 14);
    panel_13.add(lblCallNumber);

    textField_1 = new JTextField();
    textField_1.setBounds(105, 29, 153, 20);
    panel_13.add(textField_1);
    textField_1.setColumns(10);

    btnDial = new JButton("Dial");
    btnDial.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            int connection_exists = 0;
            if (winflag == 1)
              win.call("setMessage", new String[] { "Tester", "Voicecall_Dial_Button" });
            String ret1 = is_device_connected();
            if (ret1.equals("NULL"))
              return Integer.valueOf(1);
            String num = textField_1.getText();
            if (num.equals(""))
            {
              msg = "Dialed number cannot be empty.\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "call_registraiton_ok" });
              msg = ("Dialing number " + num + "\n\n");
              writeOutput(msg);
              String t2 = "ATD" + num + ";\r\n";
              ret = mySerial1.ser_write(t2, 1);
              try {
                Thread.sleep(1000L);
              }
              catch (InterruptedException localInterruptedException1)
              {
              }
            }
            else {
              msg = "Cannot make voice call without device being registered\n\n";
              writeOutput(msg);
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "call_registration_check_fail" });
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnDial.setBounds(40, 57, 89, 23);
    panel_13.add(btnDial);

    btnHangUp = new JButton("Hang Up");
    btnHangUp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (voice_call_connected == "FALSE")
        {
          msg = "No call is connected..\n\n";
          writeOutput(msg);
        }
        else
        {
          msg = "Hanging up the call..\n\n";
          writeOutput(msg);
          String ret = mySerial1.ser_write("ATH\r\n", 1);
          try {
            Thread.sleep(1000L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
      }
    });
    btnHangUp.setBounds(145, 57, 89, 23);
    panel_13.add(btnHangUp);

    panel_14 = new JPanel();
    panel_14.setBorder(new TitledBorder(null, "Incoming", 4, 2, null, null));
    panel_14.setBounds(10, 141, 393, 164);
    panel_5.add(panel_14);
    panel_14.setLayout(null);

    lblPhoneNumber = new JLabel("Phone Number");
    lblPhoneNumber.setBounds(10, 29, 88, 14);
    panel_14.add(lblPhoneNumber);

    textField_2 = new JTextField();
    textField_2.setBounds(108, 26, 139, 20);
    panel_14.add(textField_2);
    textField_2.setEditable(false);
    textField_2.setColumns(10);

    btnShowPhoneNumber = new JButton("Show Phone Number");
    btnShowPhoneNumber.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret = mySerial1.ser_write("AT+CNUM\r\n", 1);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if (!phone_number.equals(""))
        {
          textField_2.setText(phone_number);
        }
      }
    });
    btnShowPhoneNumber.setBounds(20, 54, 128, 23);
    panel_14.add(btnShowPhoneNumber);
    btnShowPhoneNumber.setMargin(new Insets(2, 0, 2, 0));

    txtrAtCommandTester = new JTextPane();
    txtrAtCommandTester.setBackground(UIManager.getColor("CheckBox.background"));

    txtrAtCommandTester.setEditable(false);
    txtrAtCommandTester.setFont(new Font("Monospaced", 0, 11));

    txtrAtCommandTester.setText("AT Command Tester will automatically detect incoming call and confirm with user whether to answer the call or not.");
    txtrAtCommandTester.setBounds(10, 85, 373, 54);
    panel_14.add(txtrAtCommandTester);

    btnHangUp_1 = new JButton("Hang Up");
    btnHangUp_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (voice_call_connected == "FALSE")
        {
          msg = "No call is connected..\n\n";
          writeOutput(msg);
        }
        else
        {
          msg = "Hanging up the call..\n\n";
          writeOutput(msg);
          String ret = mySerial1.ser_write("ATH\r\n", 1);
          try {
            Thread.sleep(1000L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
      }
    });
    btnHangUp_1.setBounds(158, 54, 89, 23);
    panel_14.add(btnHangUp_1);

    URLLabel label_20 = new URLLabel("Tutorial: How to test voice call?", "http://m2msupport.net/m2msupport/voice-call-at-commands-to-set-up-voice-call/");
    label_20.setText("Tutorial: How to test voice call?");
    label_20.setBounds(10, 11, 212, 14);
    panel_5.add(label_20);

    panel_6 = new JPanel();
    panel_6.setAlignmentX(0.0F);
    tabbedPane.addTab("Data Call", null, panel_6, null);
    String[] columnNames = { "CID", 
      "PDP Type", 
      "APN", 
      "PDP Address", 
      "Data Comp", 
      "Header Comp" };

    Object[][] data = null;
    panel_6.setLayout(null);
    DefaultTableModel model = new DefaultTableModel(data, columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    table = new JTable(model);
    table.setSelectionMode(0);

    JScrollPane scrollPane = new JScrollPane(table);
    table.getColumnModel().getColumn(2).setPreferredWidth(100);
    TableColumn column = null;
    for (int i = 0; i < 5; i++) {
      column = table.getColumnModel().getColumn(i);
      if (i == 0) {
        column.setPreferredWidth(10);
      }
      else if (i == 2)
      {
        column.setPreferredWidth(50);
      }
      else {
        column.setPreferredWidth(50);
      }
    }
    scrollPane.setBounds(10, 91, 404, 97);
    panel_6.add(scrollPane);

    btnGetPdpContexts = new JButton("Get PDP Contexts");
    btnGetPdpContexts.setToolTipText("List the PDP Contexts");
    btnGetPdpContexts.setMargin(new Insets(2, 0, 2, 0));
    btnGetPdpContexts.addActionListener(atListner);
    btnGetPdpContexts.setBounds(10, 199, 107, 23);
    panel_6.add(btnGetPdpContexts);

    btnConnect_1 = new JButton("Connect");
    btnConnect_1.setToolTipText("Connect to selected PDP context");
    btnConnect_1.setMargin(new Insets(2, 0, 2, 0));
    btnConnect_1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            int num_rows = table.getRowCount();
            int connection_exists = 0;
            if (winflag == 1)
              win.call("setMessage", new String[] { "Tester", "PDP_Connect_button" });
            if (num_rows < 1)
            {
              msg = "No profile avaialble.Press 'Get Profile' button get connection profiles.\r\n\r\n";
              writeOutput(msg);
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "callconnect_no_profiles" });
            }
            else
            {
              int selectedRowIndex = table.getSelectedRow();

              String apn = (String)table.getModel().getValueAt(selectedRowIndex, 2);
              String cid = (String)table.getModel().getValueAt(selectedRowIndex, 0);
              String ques = "Do you want to connect with CID " + cid + " with APN " + apn + "?";
              int n = JOptionPane.showConfirmDialog(
                panel_4, 
                ques, 
                "Confirm connection profile", 
                0);
              if (n == 0)
              {
                if (winflag == 1) {
                  win.call("setMessage", new String[] { "Tester", "PDP_call_started" });
                }
                msg = "Checking registration status...\n\n";
                writeOutput(msg);
                reg_status = -1;
                String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
                try {
                  Thread.sleep(1000L);
                } catch (InterruptedException localInterruptedException) {
                }
                if ((reg_status == 1) || (reg_status == 5))
                {
                  msg = "Checking if device is already connected...\n\n";
                  writeOutput(msg);
                  num_profiles_connected = 0;
                  connected_profile = null;
                  ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
                  try {
                    Thread.sleep(1000L);
                  } catch (InterruptedException localInterruptedException1) {
                  }
                  if (num_profiles_connected > 0)
                  {
                    if (connected_profile.equals(cid))
                    {
                      msg = ("Profile " + cid + " with APN " + apn + " is already connected\n");
                      writeOutput(msg);
                      connection_exists = 1;
                      if (winflag == 1) {
                        win.call("setMessage", new String[] { "Tester", "PDP_already_connected" });
                      }
                    }
                    else
                    {
                      msg = ("Disconnecting profile " + connected_profile + "\n");
                      writeOutput(msg);
                      cgact_action = "deactivate";
                      String t5 = "AT+CGACT=0," + connected_profile + "\r\n";
                      ret = mySerial1.ser_write(t5, 1);
                      try {
                        Thread.sleep(5000L); } catch (InterruptedException localInterruptedException2) {
                      }
                      connection_exists = 0;
                    }
                  }
                }

                if (((reg_status == 1) || (reg_status == 5)) && (connection_exists == 0))
                {
                  if (winflag == 1) {
                    win.call("setMessage", new String[] { "Tester", "call_registraiton_ok" });
                  }
                  cgact_action = "activate";
                  String t2 = "AT+CGACT=1," + cid + "\r\n";
                  ret = mySerial1.ser_write("AT+CMEE=1\r\n", 1);
                  try {
                    Thread.sleep(1000L);
                  }
                  catch (InterruptedException localInterruptedException3) {
                  }
                  msg = "Attaching to network...\n";
                  writeOutput(msg);
                  ret = mySerial1.ser_write("AT+CGATT=1\r\n", 1);
                  try {
                    Thread.sleep(3000L); } catch (InterruptedException localInterruptedException4) {
                  }
                  if (attach_status == 1)
                  {
                    if (winflag == 1)
                      win.call("setMessage", new String[] { "Tester", "call_attach_success" });
                    connect_status = 0;
                    msg = "Connecting...\n\n";
                    writeOutput(msg);
                    ret = mySerial1.ser_write(t2, 1);
                    try
                    {
                      Thread.sleep(2000L); } catch (InterruptedException localInterruptedException5) {
                    }
                    if (connect_status == 1) {
                      if (winflag == 1)
                        win.call("setMessage", new String[] { "Tester", "call_connect_success" });
                      String t3 = "AT+CGPADDR=" + cid + "\r\n";
                      ret = mySerial1.ser_write(t3, 1);
                    }

                  }
                  else
                  {
                    if (winflag == 1)
                      win.call("setMessage", new String[] { "Tester", "call_connect_unsucessful" });
                    msg = "Unable to attach to the network...\n\n";
                    writeOutput(msg);
                  }

                }
                else if (connection_exists == 0)
                {
                  msg = "Cannot connect without device being registered\n\n";
                  writeOutput(msg);
                  if (winflag == 1) {
                    win.call("setMessage", new String[] { "Tester", "call_registration_check_fail" });
                  }
                }

              }

            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnConnect_1.setBounds(127, 199, 60, 23);
    panel_6.add(btnConnect_1);

    btnPdpDisconnect = new JButton("Disconnect");
    btnPdpDisconnect.setToolTipText("Disconnect the selected PDP context");
    btnPdpDisconnect.setMargin(new Insets(2, 0, 2, 0));
    btnPdpDisconnect.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int num_rows = table.getRowCount();
        int connection_exists = 0;
        if (num_rows < 1)
        {
          msg = "No PDP contexts avaialble.Press 'Get PDP Contexts' button get connection profiles.\r\n\r\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "callconnect_no_profiles" });
        }
        else
        {
          int selectedRowIndex = table.getSelectedRow();

          String apn = (String)table.getModel().getValueAt(selectedRowIndex, 2);
          String cid = (String)table.getModel().getValueAt(selectedRowIndex, 0);
          String ques = "Do you want to dis-connect with CID " + cid + " with APN " + apn + "?";
          int n = JOptionPane.showConfirmDialog(
            panel_4, 
            ques, 
            "Confirm dis-connect profile", 
            0);
          if (n == 0)
          {
            msg = "Checking if device is already connected...\n";
            writeOutput(msg);
            num_profiles_connected = 0;
            connected_profile = null;
            String ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if (num_profiles_connected > 0)
            {
              if (connected_profile.equals(cid))
              {
                msg = ("Profile " + cid + " with APN " + apn + " is already connected\n");
                writeOutput(msg);
                cgact_action = "deactivate";
                String t5 = "AT+CGACT=0," + cid + "\r\n";
                ret = mySerial1.ser_write(t5, 1);
                try {
                  Thread.sleep(5000L); } catch (InterruptedException localInterruptedException1) {
                }
                connection_exists = 0;
              }
              else
              {
                msg = (ATCommandTester_v27.this.msg = "Profile " + cid + " with APN " + apn + " is NOT connected\n");
                writeOutput(msg);
              }

            }
            else
            {
              msg = (ATCommandTester_v27.this.msg = "No PDP Context profiles are connected now\n\n");
              writeOutput(msg);
            }
          }
        }
      }
    });
    btnPdpDisconnect.setBounds(197, 199, 73, 23);
    panel_6.add(btnPdpDisconnect);
    ActionListener pdpList = new pdpListner();
    btnPdpEdit = new JButton("Edit");
    btnPdpEdit.setMargin(new Insets(2, 0, 2, 0));
    btnPdpEdit.setToolTipText("Edit the selected PDP context");
    btnPdpEdit.addActionListener(pdpList);

    btnPdpEdit.setBounds(280, 199, 51, 23);
    panel_6.add(btnPdpEdit);

    btnPdpAdd = new JButton("Add");
    btnPdpAdd.setMargin(new Insets(2, 0, 2, 0));
    btnPdpAdd.setToolTipText("Add new PDP context");
    btnPdpAdd.setBounds(341, 199, 51, 23);
    btnPdpAdd.addActionListener(pdpList);
    panel_6.add(btnPdpAdd);

    lblPdpContexts = new JLabel("PDP Contexts");
    lblPdpContexts.setFont(new Font("Tahoma", 1, 11));
    lblPdpContexts.setBounds(10, 66, 107, 14);
    panel_6.add(lblPdpContexts);

    btnDataConnect = new JButton("Data Connect");
    btnDataConnect.setMargin(new Insets(2, 0, 2, 0));
    btnDataConnect.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            int connection_exists = 0;
            if (winflag == 1) {
              win.call("setMessage", new String[] { "Tester", "Data_call_connect" });
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = "Checking if device is already connected...\n\n";
              writeOutput(msg);
              num_profiles_connected = 0;
              connected_profile = null;
              ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException1) {
              }
              if (num_profiles_connected > 0)
              {
                msg = "Device is already connected\n";
                writeOutput(msg);
                connection_exists = 1;
                if (winflag == 1) {
                  win.call("setMessage", new String[] { "Tester", "PDP_already_connected" });
                }
              }
            }

            if (((reg_status == 1) || (reg_status == 5)) && (connection_exists == 0))
            {
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "call_registraiton_ok" });
              }
              ret = mySerial1.ser_write("AT+CMEE=1\r\n", 1);
              try {
                Thread.sleep(1000L);
              }
              catch (InterruptedException localInterruptedException2) {
              }
              msg = "Attaching to network...\n";
              writeOutput(msg);
              ret = mySerial1.ser_write("AT+CGATT=1\r\n", 1);
              try {
                Thread.sleep(3000L); } catch (InterruptedException localInterruptedException3) {
              }
              if (attach_status == 1)
              {
                if (winflag == 1)
                  win.call("setMessage", new String[] { "Tester", "call_attach_success" });
                connect_status = 0;
                msg = "Connecting to PDP profile 1..\n\n";
                writeOutput(msg);
                cgact_action = "activate";
                String t2 = "AT+CGACT=1,1\r\n";
                ret = mySerial1.ser_write(t2, 1);
                try
                {
                  Thread.sleep(5000L);
                }
                catch (InterruptedException localInterruptedException4)
                {
                }
                if (connect_status == 1)
                {
                  if (winflag == 1)
                    win.call("setMessage", new String[] { "Tester", "call_connect_success" });
                  String t3 = "AT+CGPADDR=1\r\n";
                  ret = mySerial1.ser_write(t3, 1);
                }

              }
              else
              {
                if (winflag == 1)
                  win.call("setMessage", new String[] { "Tester", "call_connect_unsucessful" });
                msg = "Unable to attach to the network...\n\n";
                writeOutput(msg);
              }

            }
            else if (connection_exists == 0)
            {
              msg = "Cannot connect without device being registered\n\n";
              writeOutput(msg);
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "call_registration_check_fail" });
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnDataConnect.setBounds(10, 29, 107, 23);
    panel_6.add(btnDataConnect);

    btnDisconnect_2 = new JButton("Disconnect");
    btnDisconnect_2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        num_profiles_connected = 0;
        connected_profile = null;
        String ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if (num_profiles_connected > 0)
        {
          writeOutput("Device is connected.\n");
          cgact_action = "deactivate";
          String t3 = "AT+CGACT=0," + connected_profile + "\r\n";
          ret = mySerial1.ser_write(t3, 1);
        }
        else
        {
          writeOutput("\nDevice is NOT connected, so no IP address is available.\n\n");
        }
      }
    });
    btnDisconnect_2.setMargin(new Insets(2, 0, 2, 0));
    btnDisconnect_2.setBounds(127, 29, 89, 23);
    panel_6.add(btnDisconnect_2);

    btnShowIpAddress = new JButton("Show IP Address");
    btnShowIpAddress.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        num_profiles_connected = 0;
        connected_profile = null;
        String ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if (num_profiles_connected > 0)
        {
          writeOutput("Device is connected.\n");
          String t3 = "AT+CGPADDR=" + connected_profile + "\r\n";
          ret = mySerial1.ser_write(t3, 1);
        }
        else
        {
          writeOutput("\nDevice is NOT connected, so no IP address is available.\n\n");
        }
      }
    });
    btnShowIpAddress.setMargin(new Insets(2, 0, 2, 0));
    btnShowIpAddress.setBounds(226, 29, 105, 23);
    panel_6.add(btnShowIpAddress);

    URLLabel label_24 = new URLLabel("Tutorial: How to test data call?", "http://m2msupport.net/m2msupport/data-call-at-commands-to-set-up-gprsedgeumtslte-data-call/");
    label_24.setText("Tutorial: How to test data call?");
    label_24.setBounds(10, 4, 212, 14);
    panel_6.add(label_24);

    panel_15 = new JPanel();
    tabbedPane.addTab("SMS", null, panel_15, null);
    panel_15.setLayout(null);

    tabbedPane_2 = new JTabbedPane(1);
    tabbedPane_2.setBounds(0, 36, 427, 460);
    panel_15.add(tabbedPane_2);

    panel_54 = new JPanel();
    tabbedPane_2.addTab("Message", null, panel_54, null);
    panel_54.setLayout(null);

    panel_16 = new JPanel();
    panel_16.setBounds(10, 11, 407, 158);
    panel_54.add(panel_16);
    panel_16.setBorder(new TitledBorder(null, "", 4, 2, null, null));
    panel_16.setLayout(null);

    lblPhoneNumber_1 = new JLabel("Phone Number");
    lblPhoneNumber_1.setBounds(6, 20, 95, 14);
    panel_16.add(lblPhoneNumber_1);

    textField_3 = new JTextField();
    textField_3.setBounds(100, 17, 143, 20);
    panel_16.add(textField_3);
    textField_3.setColumns(10);

    lblMessage = new JLabel("Message");
    lblMessage.setBounds(6, 45, 63, 14);
    panel_16.add(lblMessage);

    scrollPane_1 = new JScrollPane();
    scrollPane_1.setBounds(6, 63, 390, 52);
    panel_16.add(scrollPane_1);

    textArea_9 = new JTextPane();
    scrollPane_1.setViewportView(textArea_9);

    btnSendSms = new JButton("Send SMS");
    btnSendSms.setBounds(6, 124, 95, 23);
    panel_16.add(btnSendSms);

    ActionListener smsHandle = new smsHandler();

    btnShowPdu = new JButton("Show PDU Format");
    btnShowPdu.setMargin(new Insets(2, 0, 2, 0));
    btnShowPdu.setBounds(111, 124, 124, 23);
    btnShowPdu.addActionListener(smsHandle);
    panel_16.add(btnShowPdu);

    btnShowTextFormat = new JButton("Show Text Format");
    btnShowTextFormat.setMargin(new Insets(2, 0, 2, 0));
    btnShowTextFormat.setBounds(245, 124, 124, 23);
    btnShowTextFormat.addActionListener(smsHandle);
    panel_16.add(btnShowTextFormat);

    String[] SMS_columnNames = { "Index", 
      "Status", 
      "From", 
      "Date", 
      "Message" };

    Object[][] sms_data = null;
    DefaultTableModel sms_model = new DefaultTableModel(sms_data, SMS_columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    table_1 = new JTable(sms_model);
    table_1.setSelectionMode(0);

    TableColumn column_1 = null;
    for (int i = 0; i < 5; i++) {
      column_1 = table_1.getColumnModel().getColumn(i);
      if (i == 0) {
        column_1.setPreferredWidth(25);
        column_1.setWidth(25);
      }
      else if (i == 2)
      {
        column_1.setPreferredWidth(50);
      }
      else {
        column_1.setPreferredWidth(50);
      }
    }

    panel_55 = new JPanel();
    tabbedPane_2.addTab("Inbox", null, panel_55, null);
    panel_55.setLayout(null);

    button_142 = new JButton("List");
    button_142.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "SMS_Read" });
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Reading SMS Messages..\n\n";
        writeOutput(msg);

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "sms_message_read" });
        }
        String ret = mySerial1.ser_write("AT+CMGL=\"ALL\"\r\n", 1);
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_142.setBounds(10, 190, 77, 23);
    panel_55.add(button_142);

    button_143 = new JButton("View");
    button_143.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        int num_rows = table_1.getRowCount();
        if (num_rows < 1)
        {
          msg = "No SMS messages available.Press 'List' button to get all SMS messages.\r\n\r\n";
          writeOutput(msg);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "smsview_list_empty" });
          }
        }
        Object[] options = { "CLOSE" };
        JTextField index = new JTextField();
        JTextField status = new JTextField();
        JTextField from = new JTextField();
        JTextField date = new JTextField();
        JTextArea message = new JTextArea("", 4, 50);

        int selectedRowIndex = table_1.getSelectedRow();

        index.setText((String)table_1.getModel().getValueAt(selectedRowIndex, 0));
        status.setText((String)table_1.getModel().getValueAt(selectedRowIndex, 1));
        from.setText((String)table_1.getModel().getValueAt(selectedRowIndex, 2));
        date.setText((String)table_1.getModel().getValueAt(selectedRowIndex, 3));
        message.setText((String)table_1.getModel().getValueAt(selectedRowIndex, 4));

        JComponent[] inputs = { 
          new JLabel("Index"), 
          index, 
          new JLabel("Status"), 
          status, 
          new JLabel("From"), 
          from, 
          new JLabel("Date"), 
          date, 
          new JLabel("Message"), 
          message };

        int n = JOptionPane.showOptionDialog(panel_4, inputs, "View SMS Message", 0, 
          -1, null, options, options[0]);
      }
    });
    button_143.setBounds(93, 190, 77, 23);
    panel_55.add(button_143);

    button_144 = new JButton("Delete");
    button_144.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL"))
          return;
        Object[] options = { "Delete", "Cancel" };

        int num_rows = table_1.getRowCount();
        if (num_rows < 1)
        {
          msg = "No SMS messages available.Press 'List' button to get all SMS messages.\r\n\r\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "smsdelete_list_empty" });
          return;
        }

        int selectedRowIndex = table_1.getSelectedRow();
        String index = (String)table_1.getModel().getValueAt(selectedRowIndex, 0);

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "sms_message_delete" });
        }
        String ques = "Do you want to delete message " + index + "?";
        int n = JOptionPane.showConfirmDialog(
          panel_4, 
          ques, 
          "Delete SMS Message", 
          0);

        if (n == 0)
        {
          String str1 = mySerial1.ser_write("AT+CMGD=" + index + "\r\n", 1);
        }
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException1)
        {
        }
      }
    });
    button_144.setBounds(182, 190, 77, 23);
    panel_55.add(button_144);

    scrollPane_11 = new JScrollPane(table_1);
    scrollPane_11.setBounds(10, 22, 405, 143);
    panel_55.add(scrollPane_11);

    panel_59 = new JPanel();
    tabbedPane_2.addTab("Settings", null, panel_59, null);
    panel_59.setLayout(null);

    panel_63 = new JPanel();
    panel_63.setBorder(new TitledBorder(null, "", 4, 2, null, null));
    panel_63.setBounds(16, 11, 371, 83);
    panel_59.add(panel_63);
    panel_63.setLayout(null);

    JLabel lblNewLabel_4 = new JLabel("SMS Mode");
    lblNewLabel_4.setBounds(6, 21, 70, 14);
    panel_63.add(lblNewLabel_4);

    String[] sms_mode = { "Text", "PDU" };
    comboBox_22 = new JComboBox(sms_mode);
    comboBox_22.setBounds(120, 19, 75, 20);
    panel_63.add(comboBox_22);

    JButton btnGet_2 = new JButton("Get");
    btnGet_2.setBounds(243, 17, 56, 23);
    panel_63.add(btnGet_2);

    JButton btnSet_1 = new JButton("Set");
    btnSet_1.setBounds(309, 16, 56, 23);
    panel_63.add(btnSet_1);

    lblSmsc = new JLabel("SMS Service Center");
    lblSmsc.setBounds(6, 57, 117, 14);
    panel_63.add(lblSmsc);

    textField_46 = new JTextField();
    textField_46.setBounds(119, 55, 114, 20);
    panel_63.add(textField_46);
    textField_46.setColumns(10);

    JButton button_127 = new JButton("Get");
    button_127.setBounds(243, 53, 56, 23);
    panel_63.add(button_127);

    JButton button_134 = new JButton("Set");
    button_134.setBounds(309, 52, 56, 23);
    panel_63.add(button_134);
    button_134.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        String ret1 = is_device_connected();

        if (ret1.equals("NULL")) {
          return;
        }
        String smsc = textField_46.getText();
        if (smsc.equals(""))
        {
          writeOutput("SMSC field is empty");
          return;
        }
        String cmd = "AT+CSCA=\"" + smsc + "\"\r\n";

        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_127.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();

        if (ret1.equals("NULL")) {
          return;
        }

        String cmd = "AT+CSCA?\r\n";

        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        textField_46.setText(gbl_smsc);
      }
    });
    btnSet_1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL"))
          return;
        String cmd = "";
        if (((String)comboBox_22.getSelectedItem()).equals("Text"))
          cmd = "AT+CMGF=1\r\n";
        else {
          cmd = "AT+CMGF=0\r\n";
        }
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnGet_2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }

        String cmd = "AT+CMGF?\r\n";

        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if (gbl_sms_mode == 0)
          comboBox_22.setSelectedItem("PDU");
        else
          comboBox_22.setSelectedItem("Text");
      }
    });
    panel_64 = new JPanel();
    panel_64.setBorder(new TitledBorder(null, "PDU Settings", 4, 2, null, null));
    panel_64.setBounds(16, 105, 371, 306);
    panel_59.add(panel_64);
    panel_64.setLayout(null);

    JLabel lblValidityPeriodFormat = new JLabel("Validity Period Format (VPF)");
    lblValidityPeriodFormat.setBounds(10, 29, 189, 14);
    panel_64.add(lblValidityPeriodFormat);

    JLabel lblUserDataHeader = new JLabel("User Data Header Indication (UDHI)");
    lblUserDataHeader.setBounds(10, 54, 203, 14);
    panel_64.add(lblUserDataHeader);

    JLabel lblStatusReportRequest = new JLabel("Status Report Request (SRR)");
    lblStatusReportRequest.setBounds(10, 79, 168, 14);
    panel_64.add(lblStatusReportRequest);

    JLabel lblMessageIdmr = new JLabel("Message ID (MR)");
    lblMessageIdmr.setBounds(10, 101, 116, 14);
    panel_64.add(lblMessageIdmr);

    JLabel lblProtocolIdentifierpid = new JLabel("Protocol Identifier (PID)");
    lblProtocolIdentifierpid.setBounds(10, 126, 155, 14);
    panel_64.add(lblProtocolIdentifierpid);

    JLabel lblDataCodingScheme = new JLabel("Data Coding Scheme (DCS)");
    lblDataCodingScheme.setBounds(10, 151, 171, 14);
    panel_64.add(lblDataCodingScheme);

    JLabel lblValidityPeriodvp = new JLabel("Validity Period (VP)");
    lblValidityPeriodvp.setBounds(10, 181, 116, 14);
    panel_64.add(lblValidityPeriodvp);

    textField_49 = new JTextField();
    textField_49.setBounds(209, 26, 50, 20);
    panel_64.add(textField_49);
    textField_49.setText("10");
    textField_49.setColumns(10);

    textField_53 = new JTextField();
    textField_53.setBounds(209, 51, 50, 20);
    panel_64.add(textField_53);
    textField_53.setText("0");
    textField_53.setColumns(10);

    textField_54 = new JTextField();
    textField_54.setBounds(209, 76, 50, 20);
    panel_64.add(textField_54);
    textField_54.setText("0");
    textField_54.setColumns(10);

    textField_55 = new JTextField();
    textField_55.setBounds(209, 98, 50, 20);
    panel_64.add(textField_55);
    textField_55.setText("0");
    textField_55.setColumns(10);

    textField_56 = new JTextField();
    textField_56.setBounds(209, 123, 50, 20);
    panel_64.add(textField_56);
    textField_56.setText("00");
    textField_56.setColumns(10);

    textField_58 = new JTextField();
    textField_58.setBounds(209, 178, 50, 20);
    panel_64.add(textField_58);
    textField_58.setText("170");
    textField_58.setColumns(10);

    JLabel lblMessageTypemti = new JLabel("Message Type (MTI)");
    lblMessageTypemti.setBounds(10, 206, 128, 14);
    panel_64.add(lblMessageTypemti);

    textField_59 = new JTextField();
    textField_59.setBounds(209, 203, 50, 20);
    panel_64.add(textField_59);
    textField_59.setText("01");
    textField_59.setColumns(10);

    JLabel lblRejectDuplicatesrd = new JLabel("Reject Duplicates (RD)");
    lblRejectDuplicatesrd.setBounds(10, 228, 137, 14);
    panel_64.add(lblRejectDuplicatesrd);

    textField_60 = new JTextField();
    textField_60.setBounds(209, 225, 50, 20);
    panel_64.add(textField_60);
    textField_60.setText("0");
    textField_60.setColumns(10);

    JLabel lblReplyPathrp = new JLabel("Reply Path (RP)");
    lblReplyPathrp.setBounds(10, 253, 116, 14);
    panel_64.add(lblReplyPathrp);

    String[] coding = { "7-bit", "UCS-2" };

    String[] smsc_include = { "No", "Yes" };
    textField_61 = new JTextField();
    textField_61.setBounds(209, 250, 50, 20);
    panel_64.add(textField_61);
    textField_61.setText("0");
    textField_61.setColumns(10);
    comboBox_23 = new JComboBox(coding);
    comboBox_23.setBounds(209, 153, 79, 20);
    panel_64.add(comboBox_23);

    lblIncludeSmscIn = new JLabel("Include SMSC in PDU?");
    lblIncludeSmscIn.setBounds(10, 278, 168, 14);
    panel_64.add(lblIncludeSmscIn);
    comboBox_24 = new JComboBox(smsc_include);
    comboBox_24.setBounds(209, 275, 69, 20);
    panel_64.add(comboBox_24);

    URLLabel lblTutorialHow = new URLLabel("Tutorial : How to test SMS?", "http://m2msupport.net/m2msupport/sms-at-commands/");
    lblTutorialHow.setText("Tutorial : How to test SMS?");
    lblTutorialHow.setBounds(0, 11, 212, 14);
    panel_15.add(lblTutorialHow);
    btnSendSms.addActionListener(smsHandle);

    panel_17 = new JPanel();
    tabbedPane.addTab("Network Selection", null, panel_17, null);
    panel_17.setLayout(null);

    String[] network_selection_columnNames = { "Network Name", 
      "Network ID", 
      "Status" };

    Object[][] network_data = null;
    DefaultTableModel network_selection_model = new DefaultTableModel(network_data, network_selection_columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    table_2 = new JTable(network_selection_model);
    table_2.setSelectionMode(0);

    JScrollPane scrollPane_4 = new JScrollPane(table_2);

    TableColumn column_2 = null;
    for (int i = 0; i < 3; i++) {
      column_2 = table_2.getColumnModel().getColumn(i);
      if (i == 0) {
        column_2.setPreferredWidth(25);
        column_2.setWidth(25);
      }
      else if (i == 2)
      {
        column_2.setPreferredWidth(50);
      }
      else {
        column_2.setPreferredWidth(50);
      }
    }
    scrollPane_4.setBounds(10, 34, 407, 147);
    panel_17.add(scrollPane_4);

    btnFindNetworks = new JButton("Find Networks");
    btnFindNetworks.setMargin(new Insets(2, 0, 2, 0));
    btnFindNetworks.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            msg = "Finding Networks. Please wait..\n\n";
            writeOutput(msg);

            if (winflag == 1) {
              win.call("setMessage", new String[] { "Tester", "net_sel_find" });
            }
            String ret = mySerial1.ser_write("AT+COPS=?\r\n", 1);
            try {
              Thread.sleep(5000L);
            } catch (InterruptedException localInterruptedException) {
            }
            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnFindNetworks.setBounds(10, 192, 104, 23);
    panel_17.add(btnFindNetworks);

    btnSelectNetwork = new JButton("Select Network");
    btnSelectNetwork.setMargin(new Insets(2, 0, 2, 0));
    btnSelectNetwork.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        int num_rows = table_2.getRowCount();
        if (num_rows < 1)
        {
          msg = "No networks available.Press 'Find Networks' button to find avaialble networks.\r\n\r\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "network_select_list_empty" });
          return;
        }

        int selectedRowIndex = table_2.getSelectedRow();
        String name = (String)table_2.getModel().getValueAt(selectedRowIndex, 0);
        String net_id = (String)table_2.getModel().getValueAt(selectedRowIndex, 1);

        net_id = "\"" + net_id + "\"";

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "sms_network_select" });
        }
        String ques = "Do you want to select " + name + "?";
        int n = JOptionPane.showConfirmDialog(
          panel_4, 
          ques, 
          "Network Selection", 
          0);

        if (n == 0)
        {
          String ret = mySerial1.ser_write("AT+COPS=4,2," + net_id + "\r\n", 1);
          try
          {
            Thread.sleep(1000L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
      }
    });
    btnSelectNetwork.setBounds(124, 192, 95, 23);
    panel_17.add(btnSelectNetwork);

    URLLabel label_28 = new URLLabel("Tutorial: How to test Network Selection?", "http://m2msupport.net/m2msupport/network-information-automaticmanual-selection/");
    label_28.setText("Tutorial: How to test Network Selection?");
    label_28.setBounds(7, 11, 212, 14);
    panel_17.add(label_28);

    panel_18 = new JPanel();
    tabbedPane.addTab("Phone Book", null, panel_18, null);
    panel_18.setLayout(null);

    String[] phonebook_columnNames = { "Index", 
      "Number", 
      "Name", 
      "Type" };

    Object[][] phonebook_data = null;
    DefaultTableModel phonebook_model = new DefaultTableModel(phonebook_data, phonebook_columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    table_3 = new JTable(phonebook_model);
    table_3.setSelectionMode(0);

    JScrollPane scrollPane_6 = new JScrollPane(table_3);

    TableColumn column_3 = null;
    for (int i = 0; i < 3; i++) {
      column_3 = table_3.getColumnModel().getColumn(i);
      if (i == 0) {
        column_3.setPreferredWidth(25);
        column_3.setWidth(25);
      }
      else if (i == 2)
      {
        column_3.setPreferredWidth(50);
      }
      else {
        column_3.setPreferredWidth(50);
      }
    }
    scrollPane_6.setBounds(10, 45, 407, 192);
    panel_18.add(scrollPane_6);

    btnReadPhonebook = new JButton("Read Phonebook");
    btnReadPhonebook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Getting phonebook entries..\n\n";
        writeOutput(msg);

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "phonebook_read" });
        }
        String ret = mySerial1.ser_write("AT+CPBR=1,99\r\n", 1);
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnReadPhonebook.setMargin(new Insets(2, 0, 2, 0));
    btnReadPhonebook.setBounds(10, 255, 104, 23);
    panel_18.add(btnReadPhonebook);

    ActionListener pbList = new pbListner();

    btnAddEntry = new JButton("Add Entry");
    btnAddEntry.setMargin(new Insets(2, 0, 2, 0));
    btnAddEntry.setBounds(124, 255, 89, 23);
    btnAddEntry.addActionListener(pbList);
    panel_18.add(btnAddEntry);

    btnDeleteEntry = new JButton("Delete Entry");
    btnDeleteEntry.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL"))
          return;
        Object[] options = { "Delete", "Cancel" };

        int num_rows = table_3.getRowCount();
        if (num_rows < 1)
        {
          msg = "No phonebook entries available.Press 'Read Phonebook' button to get all phonebook entries.\n\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "pbdelete_list_empty" });
          return;
        }

        int selectedRowIndex = table_3.getSelectedRow();
        String index = (String)table_3.getModel().getValueAt(selectedRowIndex, 0);

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "phonebook_delete" });
        }
        String ques = "Do you want to delete entry " + index + "?";
        int n = JOptionPane.showConfirmDialog(
          panel_4, 
          ques, 
          "Delete Phonebook Entry", 
          0);

        if (n == 0)
        {
          String str1 = mySerial1.ser_write("AT+CPBW=" + index + "\r\n", 1);
        }
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException1)
        {
        }
      }
    });
    btnDeleteEntry.setMargin(new Insets(2, 0, 2, 0));
    btnDeleteEntry.setBounds(322, 255, 89, 23);
    panel_18.add(btnDeleteEntry);

    btnEditEntry = new JButton("Edit Entry");
    btnEditEntry.setMargin(new Insets(2, 0, 2, 0));
    btnEditEntry.setBounds(223, 255, 89, 23);
    btnEditEntry.addActionListener(pbList);
    panel_18.add(btnEditEntry);

    URLLabel label_29 = new URLLabel("Tutorial: How to use phonebook?", "http://m2msupport.net/m2msupport/sim-phonebook-at-commands/");
    label_29.setText("Tutorial: How to use phonebook?");
    label_29.setBounds(10, 20, 212, 14);
    panel_18.add(label_29);

    panel_31 = new JPanel();
    tabbedPane.addTab("HTTP", null, panel_31, null);
    panel_31.setLayout(null);

    comboBox_7 = new JComboBox(module_int);
    comboBox_7.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout)panel_32.getLayout();

        http_mod_sel = ((String)e.getItem());
        mySerial1.setOutputArea(textPane_10);

        String txt = "HTTP tutorial for " + http_mod_sel + " modules";
        cl.show(panel_32, (String)e.getItem());
        http_select_label.setText(txt);

        if (http_mod_sel.equals("Simcom"))
          http_select_label.setURL("http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
        else if (http_mod_sel.equals("Telit"))
          http_select_label.setURL("http://m2msupport.net/m2msupport/at-command-tester-tutorial-for-gsm-gps-playground/");
        else if (http_mod_sel.equals("Huawei"))
          http_select_label.setURL("http://m2msupport.net/m2msupport/tutorial-for-huawei-modules/");
      }
    });
    comboBox_7.setBounds(10, 11, 74, 20);
    panel_31.add(comboBox_7);

    panel_32 = new JPanel();
    panel_32.setBounds(0, 42, 427, 511);
    panel_31.add(panel_32);
    panel_32.setLayout(new CardLayout(0, 0));

    panel_19 = new JPanel();
    panel_32.add(panel_19, "Simcom");
    panel_19.setLayout(null);
    String[] bearer_columnNames = { 
      "CID", 
      "Connection Type", 
      "APN", 
      "User Name", 
      "Password", 
      "Phone Number", 
      "Rate" };

    Object[][] bearer_data = null;

    DefaultTableModel bearer_model = new DefaultTableModel(bearer_data, bearer_columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    table_4 = new JTable(bearer_model);
    table_4.setSelectionMode(0);

    JScrollPane scrollPane_8 = new JScrollPane(table_4);
    scrollPane_8.setBounds(10, 11, 407, 67);
    panel_19.add(scrollPane_8);

    btnGetBearerProfiles = new JButton("List Bearers");
    btnGetBearerProfiles.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "http_get_bearer_list_button" });
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Getting Bearer profiles..\n\n";
        writeOutput(msg);

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "sms_message_read" });
        }
        refresh_bearer_list = "TRUE";
        bearer_cid = "1";
        String ret = mySerial1.ser_write("AT+SAPBR=4,1\r\n", 1);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        refresh_bearer_list = "FALSE";
        bearer_cid = "2";
        ret = mySerial1.ser_write("AT+SAPBR=4,2\r\n", 1);
        try {
          Thread.sleep(1000L); } catch (InterruptedException localInterruptedException1) {
        }
        bearer_cid = "3";
        ret = mySerial1.ser_write("AT+SAPBR=4,3\r\n", 1);
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException2)
        {
        }
      }
    });
    btnGetBearerProfiles.setMargin(new Insets(2, 0, 2, 0));
    btnGetBearerProfiles.setBounds(10, 89, 76, 23);
    panel_19.add(btnGetBearerProfiles);

    btnEdit = new JButton("Edit");
    btnEdit.setMargin(new Insets(2, 0, 2, 0));
    btnEdit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL"))
          return;
        JTextField d_cont_type = new JTextField();
        JTextField d_apn = new JTextField();
        JTextField d_uname = new JTextField();
        JTextField d_pwd = new JTextField();
        JTextField d_pn_no = new JTextField();
        JTextField d_rate = new JTextField();

        Object[] options = { "Edit", "Cancel" };
        String dialog_header = "Edit Bearer Profile";

        int num_rows = table_4.getRowCount();
        if (num_rows < 1)
        {
          msg = "No Bearer entries in the device.\n\n";
          writeOutput(msg);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "phonebook_edit_empty_list" });
          }

        }
        else
        {
          int selectedRowIndex = table_4.getSelectedRow();

          String cid = (String)table_4.getModel().getValueAt(selectedRowIndex, 0);
          String conn_type = (String)table_4.getModel().getValueAt(selectedRowIndex, 1);
          String apn = (String)table_4.getModel().getValueAt(selectedRowIndex, 2);
          String user_n = (String)table_4.getModel().getValueAt(selectedRowIndex, 3);
          String pass = (String)table_4.getModel().getValueAt(selectedRowIndex, 4);
          String pn_number = (String)table_4.getModel().getValueAt(selectedRowIndex, 5);
          String csd_rate = (String)table_4.getModel().getValueAt(selectedRowIndex, 6);

          d_cont_type.setText(conn_type);
          d_apn.setText(apn);
          d_uname.setText(user_n);
          d_pwd.setText(pass);
          d_pn_no.setText(pn_number);
          d_rate.setText(csd_rate);

          options[0] = "Update";
          options[1] = "Cancel";
          dialog_header = "Edit Phonebook Entry";

          JComponent[] inputs1 = { 
            new JLabel("Connection Type"), 
            d_cont_type, 
            new JLabel("APN"), 
            d_apn, 
            new JLabel("User Name"), 
            d_uname, 
            new JLabel("Password"), 
            d_pwd, 
            new JLabel("Phone Number"), 
            d_pn_no, 
            new JLabel("Rate"), 
            d_rate };

          int n = JOptionPane.showOptionDialog(panel_4, inputs1, "Edit Phonebook Entry", 0, 
            -1, null, options, options[1]);

          if (n == 0)
          {
            String new_cont = d_cont_type.getText();

            if (!new_cont.equals(conn_type))
            {
              new_cont = new_cont.trim();
              String ret = mySerial1.ser_write("AT+SAPBR=3," + cid + ",\"Contype\"" + ",\"" + new_cont + "\"" + "\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException) {
              }
              msg = ("Connection type of CID " + cid + " updated\n\n");
              writeOutput(msg);
            }

            String new_apn = d_apn.getText();
            if (!new_apn.equals(apn))
            {
              new_apn = new_apn.trim();
              String ret = mySerial1.ser_write("AT+SAPBR=3," + cid + ",\"APN\"" + ",\"" + new_apn + "\"" + "\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException1) {
              }
              msg = ("APN of CID " + cid + " updated\n\n");
              writeOutput(msg);
            }

            String new_user_name = d_uname.getText();
            if (!new_user_name.equals(user_n))
            {
              new_user_name = new_user_name.trim();
              String ret = mySerial1.ser_write("AT+SAPBR=3," + cid + ",\"USER\"" + ",\"" + new_user_name + "\"" + "\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException2) {
              }
              msg = ("User name of CID " + cid + " updated\n\n");
              writeOutput(msg);
            }

            String new_passwd = d_pwd.getText();
            if (!new_passwd.equals(pass))
            {
              new_passwd = new_passwd.trim();
              String ret = mySerial1.ser_write("AT+SAPBR=3," + cid + ",\"PWD\"" + ",\"" + new_passwd + "\"" + "\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException3) {
              }
              msg = ("Passwd of CID " + cid + " updated\n\n");
              writeOutput(msg);
            }

            String new_phone_num = d_pn_no.getText();
            if (!new_phone_num.equals(pn_number))
            {
              new_phone_num = new_phone_num.trim();
              String ret = mySerial1.ser_write("AT+SAPBR=3," + cid + ",\"PHONENUM\"" + ",\"" + new_phone_num + "\"" + "\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException4) {
              }
              msg = ("Phone number of CID " + cid + " updated\n\n");
              writeOutput(msg);
            }

            String new_rate = d_rate.getText();
            if (!new_rate.equals(csd_rate))
            {
              new_rate = new_rate.trim();
              String ret = mySerial1.ser_write("AT+SAPBR=3," + cid + ",\"RATE\"" + ",\"" + new_rate + "\"" + "\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException5) {
              }
              msg = ("Rate of CID " + cid + " updated\n\n");
              writeOutput(msg);
            }

            refresh_bearer_list = "TRUE";
            bearer_cid = "1";
            String ret = mySerial1.ser_write("AT+SAPBR=4,1\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException6) {
            }
            refresh_bearer_list = "FALSE";
            bearer_cid = "2";
            ret = mySerial1.ser_write("AT+SAPBR=4,2\r\n", 1);
            try {
              Thread.sleep(1000L); } catch (InterruptedException localInterruptedException7) {
            }
            bearer_cid = "3";
            ret = mySerial1.ser_write("AT+SAPBR=4,3\r\n", 1);
            try {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException8)
            {
            }
          }
        }
      }
    });
    btnEdit.setBounds(96, 89, 56, 23);
    panel_19.add(btnEdit);

    btnOpen = new JButton("Open");
    btnOpen.setMargin(new Insets(2, 0, 2, 0));
    btnOpen.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        int num_rows = table_4.getRowCount();
        if (num_rows < 1)
        {
          msg = "No Bearer entries in the device.\n\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "bearer_empty_list" });
        }
        else
        {
          int selectedRowIndex = table_4.getSelectedRow();

          String cid = (String)table_4.getModel().getValueAt(selectedRowIndex, 0);
          msg = ("Opening bearer " + cid + " .\n\n");
          writeOutput(msg);
          String ret = mySerial1.ser_write("AT+SAPBR=1," + cid + "\r\n", 1);
          try {
            Thread.sleep(500L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
      }
    });
    btnOpen.setBounds(162, 89, 56, 23);
    panel_19.add(btnOpen);

    btnClose = new JButton("Close");
    btnClose.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        int num_rows = table_4.getRowCount();
        if (num_rows < 1)
        {
          msg = "No Bearer entries in the device.\n\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "bearer_empty_list" });
        }
        else
        {
          int selectedRowIndex = table_4.getSelectedRow();

          String cid = (String)table_4.getModel().getValueAt(selectedRowIndex, 0);
          msg = ("Closing bearer " + cid + " .\n\n");
          writeOutput(msg);
          String ret = mySerial1.ser_write("AT+SAPBR=0," + cid + "\r\n", 1);
          try {
            Thread.sleep(500L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
      }
    });
    btnClose.setMargin(new Insets(2, 0, 2, 0));
    btnClose.setBounds(231, 89, 56, 23);
    panel_19.add(btnClose);

    btnQuery = new JButton("Query");
    btnQuery.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        int num_rows = table_4.getRowCount();
        if (num_rows < 1)
        {
          msg = "No Bearer entries in the device.\n\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "bearer_empty_list" });
        }
        else
        {
          int selectedRowIndex = table_4.getSelectedRow();

          String cid = (String)table_4.getModel().getValueAt(selectedRowIndex, 0);
          msg = ("Querying bearer " + cid + " .\n\n");
          writeOutput(msg);
          String ret = mySerial1.ser_write("AT+SAPBR=2," + cid + "\r\n", 1);
          try {
            Thread.sleep(500L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
      }
    });
    btnQuery.setMargin(new Insets(2, 0, 2, 0));
    btnQuery.setBounds(297, 89, 63, 23);
    panel_19.add(btnQuery);

    lblBearerCid = new JLabel("Bearer CID");
    lblBearerCid.setBounds(10, 126, 63, 14);
    panel_19.add(lblBearerCid);

    lblUrl = new JLabel("URL");
    lblUrl.setBounds(10, 151, 46, 14);
    panel_19.add(lblUrl);

    textField_5 = new JTextField("http://www.m2msupport.net/m2msupport/http_get_test.php");
    textField_5.setBounds(96, 148, 321, 20);
    panel_19.add(textField_5);
    textField_5.setColumns(10);
    String[] b_cids = { "1", "2", "3" };
    comboBox_4 = new JComboBox(b_cids);
    comboBox_4.setBounds(96, 123, 46, 20);
    panel_19.add(comboBox_4);

    scrollPane_9 = new JScrollPane();
    scrollPane_9.setBounds(10, 180, 407, 69);
    panel_19.add(scrollPane_9);

    textArea_13 = new JTextPane();
    scrollPane_9.setViewportView(textArea_13);

    btnGet = new JButton("GET");
    btnGet.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String url = textField_5.getText();
            if (url.equals(""))
            {
              msg = "URL cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String cid = (String)comboBox_4.getSelectedItem();

            reg_status = -1;

            if (winflag == 1) {
              win.call("setMessage", new String[] { "Tester", "http_get_start" });
            }
            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;

            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = ("Querying bearer " + cid + " .\n\n");
              writeOutput(msg);
              ret = mySerial1.ser_write("AT+SAPBR=2," + cid + "\r\n", 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException1) {
              }
              msg = ("Bearer " + cid + " is " + global_bearer_state + ".\n\n");
              writeOutput(msg);

              if (global_bearer_state.equals("Closed"))
              {
                msg = ("Opening Bearer " + cid + "..." + "\n\n");
                writeOutput(msg);

                ret = mySerial1.ser_write("AT+SAPBR=1," + cid + "\r\n", 1);
                try {
                  Thread.sleep(1500L);
                } catch (InterruptedException localInterruptedException2) {
                }
              }
              msg = "Initializing HTTP service...\n\n";
              writeOutput(msg);

              String cmd = "AT+HTTPINIT\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              }
              catch (InterruptedException localInterruptedException3)
              {
              }

              msg = "Setting up HTTP parameters..\n\n";
              writeOutput(msg);

              httppara_set = "FAIL";
              url = "\"" + url + "\"";
              cmd = "AT+HTTPPARA=\"URL\"," + url + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException4) {
              }
              if (!httppara_set.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }

              cmd = "AT+HTTPPARA=\"CID\"," + cid + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException5) {
              }
              if (!httppara_set.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }
              httpaction_result = "FAIL";
              cmd = "AT+HTTPACTION=0\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(5000L);
              } catch (InterruptedException localInterruptedException6) {
              }
              if (!httpaction_result.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }

              cmd = "AT+HTTPREAD\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(2000L);
              } catch (InterruptedException localInterruptedException7) {
              }
              if (!httpread_result.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }

              msg = "Terminating HTTP session..\n\n";
              writeOutput(msg);

              httpterm_result = "FAIL";
              cmd = "AT+HTTPTERM\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException8) {
              }
              if (!httpterm_result.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "http_get_success" });
              }

            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnGet.setBounds(10, 256, 63, 23);
    panel_19.add(btnGet);

    btnPost = new JButton("POST");
    btnPost.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String url = textField_5.getText();
            if (url.equals(""))
            {
              msg = "URL cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String post_data = textArea_13.getText();
            if (post_data.equals(""))
            {
              msg = "POST data cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String cid = (String)comboBox_4.getSelectedItem();

            if (winflag == 1) {
              win.call("setMessage", new String[] { "Tester", "http_post_start" });
            }
            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;

            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = ("Querying bearer " + cid + " .\n\n");
              writeOutput(msg);
              ret = mySerial1.ser_write("AT+SAPBR=2," + cid + "\r\n", 1);
              try {
                Thread.sleep(500L);
              } catch (InterruptedException localInterruptedException1) {
              }
              msg = ("Bearer " + cid + " is " + global_bearer_state + ".\n\n");
              writeOutput(msg);

              if (global_bearer_state.equals("Closed"))
              {
                msg = ("Opening Bearer " + cid + "..." + "\n\n");
                writeOutput(msg);

                ret = mySerial1.ser_write("AT+SAPBR=1," + cid + "\r\n", 1);
                try {
                  Thread.sleep(1500L);
                } catch (InterruptedException localInterruptedException2) {
                }
              }
              msg = "Initializing HTTP service...\n\n";
              writeOutput(msg);

              String cmd = "AT+HTTPINIT\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              }
              catch (InterruptedException localInterruptedException3)
              {
              }

              msg = "Setting up HTTP parameters..\n\n";
              writeOutput(msg);

              httppara_set = "FAIL";
              url = "\"" + url + "\"";
              cmd = "AT+HTTPPARA=\"URL\"," + url + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException4) {
              }
              if (!httppara_set.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }

              cmd = "AT+HTTPPARA=\"CID\"," + cid + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException5) {
              }
              if (!httppara_set.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }

              int data_size = post_data.length();

              cmd = "AT+HTTPDATA=" + data_size + ",10000\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(2000L);
              }
              catch (InterruptedException localInterruptedException6) {
              }
              ret = mySerial1.ser_write(post_data, 1);
              try {
                Thread.sleep(3000L);
              } catch (InterruptedException localInterruptedException7) {
              }
              httpaction_result = "FAIL";
              cmd = "AT+HTTPACTION=1\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(3000L);
              } catch (InterruptedException localInterruptedException8) {
              }
              if (!httpaction_result.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }

              msg = "Terminating HTTP session..\n\n";
              writeOutput(msg);

              httpterm_result = "FAIL";
              cmd = "AT+HTTPTERM\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException9) {
              }
              if (!httpterm_result.equals("SUCCESS"))
              {
                return Integer.valueOf(1);
              }
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "http_post_success" });
              }

            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnPost.setBounds(80, 256, 76, 23);
    panel_19.add(btnPost);

    panel_39 = new JPanel();
    panel_32.add(panel_39, "Telit");
    panel_39.setLayout(null);

    label_1 = new JLabel("URL");
    label_1.setBounds(10, 36, 46, 14);
    panel_39.add(label_1);

    textField_22 = new JTextField("http://www.m2msupport.net/m2msupport/http_get_test.php");
    textField_22.setColumns(10);
    textField_22.setBounds(96, 33, 321, 20);
    panel_39.add(textField_22);

    button = new JButton("GET");
    button.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String server_ip = textField_22.getText();
            if (server_ip.equals(""))
            {
              msg = "URL is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            if (winflag == 1) {
              win.call("setMessage", new String[] { "Tester", "Check registration" });
            }
            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              int selected_connId = Integer.parseInt((String)comboBox_14.getSelectedItem());

              ret = mySerial1.ser_write("AT#SCFG?\r\n", 1);
              try {
                Thread.sleep(2000L);
              } catch (InterruptedException localInterruptedException1) {
              }
              if (scfg_status.equals("FALSE"))
              {
                msg = "This is only supported for Telit modules\n\n";
                writeOutput(msg);
                return Integer.valueOf(1);
              }

              String selected_cid = pdpId[(selected_connId - 1)];

              msg = "Checking if socket connection is activated...\n\n";
              writeOutput(msg);

              ret = mySerial1.ser_write("AT#SGACT?\r\n", 1);
              try {
                Thread.sleep(2000L);
              }
              catch (InterruptedException localInterruptedException2)
              {
              }

              for (int k = 0; k < 6; k++)
              {
                if (selected_cid.equals(socketPdpId[k]))
                {
                  if (socketPdpStatus[k].equals("Active"))
                  {
                    msg = ("cid " + selected_cid + " is already active" + "\n\n");
                    writeOutput(msg);
                  }
                  else
                  {
                    msg = ("cid " + selected_cid + " is not active Activating now.." + "\n\n");
                    writeOutput(msg);

                    ret = mySerial1.ser_write("AT#SGACT=" + selected_cid + ",1" + "\r\n", 1);
                    try {
                      Thread.sleep(2000L);
                    } catch (InterruptedException localInterruptedException3) {
                    }
                    if (socket_pdp_activation_status.equals("FAIL"))
                    {
                      writeOutput("Error activating PDP context " + selected_cid);
                      return Integer.valueOf(1);
                    }

                  }

                  msg = ("Connecting to" + server_ip + "\n\n");
                  writeOutput(msg);

                  URL aURL = null;
                  try {
                    aURL = new URL(server_ip);
                  }
                  catch (MalformedURLException e1) {
                    e1.printStackTrace();
                  }

                  String host = aURL.getHost();
                  String query = aURL.getQuery();
                  String path = aURL.getPath();
                  server_ip = "\"" + host + "\"";

                  ret = mySerial1.ser_write("AT#SD=" + selected_connId + "," + 
                    "0" + "," + 
                    "80" + "," + 
                    server_ip + "\r\n", 1);
                  try {
                    Thread.sleep(2000L);
                  } catch (InterruptedException localInterruptedException4) {
                  }
                  String http_data = "";
                  http_data = "\r\nGET " + path + " HTTP/1.1\r\n" + "Host:" + host + "\r\n" + "Connection:keep-alive\r\n" + "\r\n";

                  writeOutput("Sending HTTP data: \n" + http_data);

                  ret = mySerial1.ser_write(http_data, 1);
                  try {
                    Thread.sleep(2000L);
                  }
                  catch (InterruptedException localInterruptedException5)
                  {
                  }
                }
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    button.setBounds(96, 75, 63, 23);
    panel_39.add(button);

    button_1 = new JButton("POST");
    button_1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String server_ip = textField_22.getText();
            if (server_ip.equals(""))
            {
              msg = "URL is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              int selected_connId = Integer.parseInt((String)comboBox_14.getSelectedItem());

              ret = mySerial1.ser_write("AT#SCFG?\r\n", 1);
              try {
                Thread.sleep(2000L);
              } catch (InterruptedException localInterruptedException1) {
              }
              if (scfg_status.equals("FALSE"))
              {
                msg = "This is only supported for Telit modules\n\n";
                writeOutput(msg);
                return Integer.valueOf(1);
              }

              String selected_cid = pdpId[(selected_connId - 1)];

              msg = "Checking if socket connection is activated...\n\n";
              writeOutput(msg);

              ret = mySerial1.ser_write("AT#SGACT?\r\n", 1);
              try {
                Thread.sleep(2000L);
              }
              catch (InterruptedException localInterruptedException2)
              {
              }

              for (int k = 0; k < 6; k++)
              {
                if (selected_cid.equals(socketPdpId[k]))
                {
                  if (socketPdpStatus[k].equals("Active"))
                  {
                    msg = ("cid " + selected_cid + " is already active" + "\n\n");
                    writeOutput(msg);
                  }
                  else
                  {
                    msg = ("cid " + selected_cid + " is not active Activating now.." + "\n\n");
                    writeOutput(msg);

                    ret = mySerial1.ser_write("AT#SGACT=" + selected_cid + ",1" + "\r\n", 1);
                    try {
                      Thread.sleep(2000L);
                    } catch (InterruptedException localInterruptedException3) {
                    }
                    if (socket_pdp_activation_status.equals("FAIL"))
                    {
                      writeOutput("Error activating PDP context " + selected_cid);
                      return Integer.valueOf(1);
                    }

                  }

                  msg = ("Connecting to" + server_ip + "\n\n");
                  writeOutput(msg);

                  URL aURL = null;
                  try {
                    aURL = new URL(server_ip);
                  }
                  catch (MalformedURLException e1) {
                    e1.printStackTrace();
                  }

                  String host = aURL.getHost();
                  String query = aURL.getQuery();
                  String path = aURL.getPath();
                  server_ip = "\"" + host + "\"";

                  ret = mySerial1.ser_write("AT#SD=" + selected_connId + "," + 
                    "0" + "," + 
                    "80" + "," + 
                    server_ip + "\r\n", 1);
                  try {
                    Thread.sleep(4000L);
                  } catch (InterruptedException localInterruptedException4) {
                  }
                  int query_len = query.length();
                  String int_str = Integer.toString(query_len);
                  String http_data = "";
                  http_data = "POST " + path + " HTTP/1.1\r\n" + "Host:" + host + "\r\n" + "Content-length:" + int_str + "\r\n" + "\r\n" + query + "\r\n";

                  writeOutput("Sending HTTP data: \n" + http_data);

                  ret = mySerial1.ser_write(http_data, 1);
                  try {
                    Thread.sleep(2000L);
                  }
                  catch (InterruptedException localInterruptedException5)
                  {
                  }
                }
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    button_1.setBounds(169, 75, 76, 23);
    panel_39.add(button_1);

    lblConnectionId = new JLabel("connId");
    lblConnectionId.setBounds(10, 11, 46, 14);
    panel_39.add(lblConnectionId);

    comboBox_14 = new JComboBox(socketIds);
    comboBox_14.setBounds(96, 8, 46, 20);
    panel_39.add(comboBox_14);

    panel_50 = new JPanel();
    panel_32.add(panel_50, "Quectel");
    panel_50.setLayout(null);

    label_19 = new JLabel("URL");
    label_19.setBounds(10, 36, 46, 14);
    panel_50.add(label_19);

    textField_44 = new JTextField("http://www.m2msupport.net/m2msupport/http_get_test.php");
    textField_44.setColumns(10);
    textField_44.setBounds(41, 33, 348, 20);
    panel_50.add(textField_44);

    button_15 = new JButton("GET");
    button_15.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String apn = textField_45.getText();
            if (apn.equals(""))
            {
              msg = "APN is not set. You can get the APN from the 'Data Call' tab\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_ip = textField_44.getText();
            if (server_ip.equals(""))
            {
              msg = "URL is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = "Device is registered.. \n\n";
              writeOutput(msg);
            }
            else
            {
              msg = "Device is NOT registered.. \n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Configure the context as foreground...\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIFGCNT=0\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException1) {
            }
            msg = "Set the APN..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QICSGP=1,\"" + apn + "\"" + "\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException2) {
            }
            msg = "Disable MUXIP..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIMUX=0\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException3) {
            }
            msg = "Set session mode to non-transparent..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIMODE=0\r\n", 1);
            try {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException4) {
            }
            String server_add_type = (String)comboBox_19.getSelectedItem();

            msg = "Setting server address is Domain Name..\n\n";
            writeOutput(msg);

            ret = mySerial1.ser_write("AT+QIDNSIP=1\r\n", 1);
            try {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException5) {
            }
            msg = "Register the TCP/IP stack..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIREGAPP\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException6) {
            }
            msg = "Activate Foreground context..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIACT\r\n", 1);
            try {
              Thread.sleep(6000L);
            } catch (InterruptedException localInterruptedException7) {
            }
            msg = "Get local IP address..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QILOCIP\r\n", 1);
            try {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException8) {
            }
            URL aURL = null;
            try {
              aURL = new URL(server_ip);
            }
            catch (MalformedURLException e1) {
              e1.printStackTrace();
            }

            String host = aURL.getHost();
            String query = aURL.getQuery();
            String path = aURL.getPath();
            server_ip = "\"" + host + "\"";

            msg = "Connect to the remote server..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIOPEN=\"TCP\"," + server_ip + "," + "80" + "\r\n", 1);
            try {
              Thread.sleep(5000L);
            } catch (InterruptedException localInterruptedException9) {
            }
            ret = mySerial1.ser_write("AT+QISEND\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException10) {
            }
            String ctz = Character.toString('\032');

            String http_data = "";
            http_data = "\r\nGET " + path + " HTTP/1.1\r\n" + "Host:" + host + "\r\n" + "Connection:keep-alive\r\n\r\n";

            writeOutput("Sending HTTP data: \n" + http_data);

            ret = mySerial1.ser_write(http_data, 1);
            try {
              Thread.sleep(2000L);
            } catch (InterruptedException localInterruptedException11) {
            }
            ret = mySerial1.ser_write(ctz, 1);
            try {
              Thread.sleep(2000L);
            } catch (InterruptedException localInterruptedException12) {
            }
            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    button_16 = new JButton("POST");
    button_16.setBounds(113, 61, 76, 23);
    panel_50.add(button_16);
    button_15.setBounds(40, 61, 63, 23);
    panel_50.add(button_15);

    lblApnForTcp = new JLabel("APN for TCP Connection");
    lblApnForTcp.setBounds(10, 11, 179, 14);
    panel_50.add(lblApnForTcp);

    textField_45 = new JTextField();
    textField_45.setBounds(152, 8, 169, 20);
    panel_50.add(textField_45);
    textField_45.setColumns(10);

    http_select_label = new URLLabel("HTTP tutorial for Simcom modules", "http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
    http_select_label.setBounds(94, 14, 225, 14);
    panel_31.add(http_select_label);

    panel_35 = new JPanel();
    tabbedPane.addTab("FTP", null, panel_35, null);
    panel_35.setLayout(null);

    comboBox_9 = new JComboBox(module_sim_tel);
    comboBox_9.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout)panel_36.getLayout();

        ftp_mod_sel = ((String)e.getItem());

        String txt = "FTP tutorial for " + ftp_mod_sel + " modules";
        cl.show(panel_36, (String)e.getItem());
        lblFtpTestingFor.setText(txt);

        if (ftp_mod_sel.equals("Simcom"))
          lblFtpTestingFor.setURL("http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
        else if (ftp_mod_sel.equals("Telit"))
          lblFtpTestingFor.setURL("http://m2msupport.net/m2msupport/at-command-tester-tutorial-for-gsm-gps-playground/");
        else if (ftp_mod_sel.equals("Huawei"))
          lblFtpTestingFor.setURL("http://m2msupport.net/m2msupport/tutorial-for-huawei-modules/");
      }
    });
    comboBox_9.setBounds(20, 16, 101, 20);
    panel_35.add(comboBox_9);

    panel_36 = new JPanel();
    panel_36.setBounds(0, 47, 427, 517);
    panel_35.add(panel_36);
    panel_36.setLayout(new CardLayout(0, 0));

    panel_21 = new JPanel();
    panel_36.add(panel_21, "Simcom");
    panel_21.setLayout(null);

    lblFtpServerAddress = new JLabel("FTP Server ");
    lblFtpServerAddress.setBounds(20, 11, 119, 14);
    panel_21.add(lblFtpServerAddress);

    lblUserName = new JLabel("User Name");
    lblUserName.setBounds(20, 36, 75, 14);
    panel_21.add(lblUserName);

    lblPassword = new JLabel("Password");
    lblPassword.setBounds(20, 61, 75, 14);
    panel_21.add(lblPassword);

    textField_4 = new JTextField();
    textField_4.setBounds(119, 8, 170, 20);
    panel_21.add(textField_4);
    textField_4.setColumns(10);

    textField_6 = new JTextField();
    textField_6.setBounds(119, 33, 86, 20);
    panel_21.add(textField_6);
    textField_6.setColumns(10);

    textField_7 = new JTextField();
    textField_7.setBounds(119, 58, 86, 20);
    panel_21.add(textField_7);
    textField_7.setColumns(10);

    lblBearerId = new JLabel("Bearer ID");
    lblBearerId.setBounds(20, 145, 75, 14);
    panel_21.add(lblBearerId);

    comboBox_5 = new JComboBox(b_cids);
    comboBox_5.setBounds(119, 139, 39, 20);
    panel_21.add(comboBox_5);

    scrollPane_12 = new JScrollPane();
    scrollPane_12.setBounds(16, 184, 401, 83);
    panel_21.add(scrollPane_12);

    textArea_14 = new JTextArea();
    textArea_14.setWrapStyleWord(true);
    textArea_14.setLineWrap(true);
    scrollPane_12.setViewportView(textArea_14);

    btnFtpGet = new JButton("FTP Get");
    btnFtpGet.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String ftp_address = textField_4.getText();
            if (ftp_address.equals(""))
            {
              msg = "FTP address cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String uname = textField_6.getText();
            if (uname.equals(""))
            {
              msg = "User name cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String passwd = textField_7.getText();
            if (passwd.equals(""))
            {
              msg = "Password cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String fname = textField_8.getText();
            if (fname.equals(""))
            {
              msg = "File name cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String dname = textField_9.getText();
            if (dname.equals(""))
            {
              dname = "/";
            }

            String cid = (String)comboBox_5.getSelectedItem();

            if (winflag == 1) {
              win.call("setMessage", new String[] { "Tester", "ftp_get_start" });
            }
            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;

            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = ("Querying bearer " + cid + " .\n\n");
              writeOutput(msg);
              ret = mySerial1.ser_write("AT+SAPBR=2," + cid + "\r\n", 1);
              try {
                Thread.sleep(500L);
              } catch (InterruptedException localInterruptedException1) {
              }
              msg = ("Bearer " + cid + " is " + global_bearer_state + ".\n\n");
              writeOutput(msg);

              if (global_bearer_state.equals("Closed"))
              {
                msg = ("Opening Bearer " + cid + "..." + "\n\n");
                writeOutput(msg);

                ret = mySerial1.ser_write("AT+SAPBR=1," + cid + "\r\n", 1);
                try {
                  Thread.sleep(1500L);
                }
                catch (InterruptedException localInterruptedException2) {
                }
              }
              msg = "Setting up FTP parameters..\n\n";
              writeOutput(msg);

              String cmd = "AT+FTPCID=" + cid + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              }
              catch (InterruptedException localInterruptedException3)
              {
              }
              ftp_set = "FAIL";
              ftp_address = "\"" + ftp_address + "\"";
              cmd = "AT+FTPSERV=" + ftp_address + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException4) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPUN=\"" + uname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException5) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPPW=\"" + passwd + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException6) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPGETNAME=\"" + fname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException7) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPGETPATH=\"" + dname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException8) {
              }
              ftp_set.equals("SUCCESS");

              ftp_get_result = "FAIL";
              cmd = "AT+FTPGET=1\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException9) {
              }
              ftp_get_result.equals("SUCCESS");

              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "ftp_get_success" });
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnFtpGet.setBounds(20, 288, 89, 23);
    panel_21.add(btnFtpGet);

    lblFileName = new JLabel("File Name");
    lblFileName.setBounds(20, 95, 75, 14);
    panel_21.add(lblFileName);

    textField_8 = new JTextField();
    textField_8.setBounds(119, 89, 181, 20);
    panel_21.add(textField_8);
    textField_8.setColumns(10);

    lblDirectory = new JLabel("Directory");
    lblDirectory.setBounds(20, 120, 75, 14);
    panel_21.add(lblDirectory);

    textField_9 = new JTextField();
    textField_9.setBounds(119, 114, 181, 20);
    panel_21.add(textField_9);
    textField_9.setColumns(10);

    btnFtpPut = new JButton("FTP Put");
    btnFtpPut.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String ftp_address = textField_4.getText();
            if (ftp_address.equals(""))
            {
              msg = "FTP address cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String uname = textField_6.getText();
            if (uname.equals(""))
            {
              msg = "User name cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String passwd = textField_7.getText();
            if (passwd.equals(""))
            {
              msg = "Password cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String fname = textField_8.getText();
            if (fname.equals(""))
            {
              msg = "File name cannot be empty\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }
            String dname = textField_9.getText();
            if (dname.equals(""))
            {
              dname = "/";
            }

            String cid = (String)comboBox_5.getSelectedItem();

            msg = "FTP put start\n\n";
            writeOutput(msg);
            if (winflag == 1) {
              win.call("setMessage", new String[] { "Tester", "ftp_put_start" });
            }
            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;

            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = ("Querying bearer " + cid + " .\n\n");
              writeOutput(msg);
              ret = mySerial1.ser_write("AT+SAPBR=2," + cid + "\r\n", 1);
              try {
                Thread.sleep(500L);
              } catch (InterruptedException localInterruptedException1) {
              }
              msg = ("Bearer " + cid + " is " + global_bearer_state + ".\n\n");
              writeOutput(msg);

              if (global_bearer_state.equals("Closed"))
              {
                msg = ("Opening Bearer " + cid + "..." + "\n\n");
                writeOutput(msg);

                ret = mySerial1.ser_write("AT+SAPBR=1," + cid + "\r\n", 1);
                try {
                  Thread.sleep(1500L);
                }
                catch (InterruptedException localInterruptedException2) {
                }
              }
              msg = "Setting up FTP parameters..\n\n";
              writeOutput(msg);

              String cmd = "AT+FTPCID=" + cid + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              }
              catch (InterruptedException localInterruptedException3)
              {
              }
              ftp_set = "FAIL";
              ftp_address = "\"" + ftp_address + "\"";
              cmd = "AT+FTPSERV=" + ftp_address + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException4) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPUN=\"" + uname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException5) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPPW=\"" + passwd + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException6) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPPUTNAME=\"" + fname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException7) {
              }
              ftp_set.equals("SUCCESS");

              ftp_set = "FAIL";
              cmd = "AT+FTPPUTPATH=\"" + dname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException8) {
              }
              ftp_set.equals("SUCCESS");

              ftp_put_data = textArea_14.getText();

              ftp_put_data_len = ftp_put_data.length();
              ftp_blocks = 0.0D;

              ftp_blocks = Math.ceil(ftp_put_data_len / 1280);

              ftp_data_over = "FALSE";
              ftp_get_result = "FAIL";
              cmd = "AT+FTPPUT=1\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(400L);
              } catch (InterruptedException localInterruptedException9) {
              }
              ftp_get_result.equals("SUCCESS");

              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "ftp_get_success" });
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnFtpPut.setBounds(129, 288, 89, 23);
    panel_21.add(btnFtpPut);

    panel_40 = new JPanel();
    panel_40.setLayout(null);
    panel_36.add(panel_40, "Telit");

    label_2 = new JLabel("FTP Server ");
    label_2.setBounds(20, 11, 119, 14);
    panel_40.add(label_2);

    label_3 = new JLabel("User Name");
    label_3.setBounds(20, 36, 75, 14);
    panel_40.add(label_3);

    label_4 = new JLabel("Password");
    label_4.setBounds(20, 61, 75, 14);
    panel_40.add(label_4);

    textField_19 = new JTextField();
    textField_19.setColumns(10);
    textField_19.setBounds(119, 8, 170, 20);
    panel_40.add(textField_19);

    textField_24 = new JTextField();
    textField_24.setColumns(10);
    textField_24.setBounds(119, 33, 86, 20);
    panel_40.add(textField_24);

    textField_25 = new JTextField();
    textField_25.setColumns(10);
    textField_25.setBounds(119, 58, 86, 20);
    panel_40.add(textField_25);

    lblCid = new JLabel("CID");
    lblCid.setBounds(20, 136, 75, 14);
    panel_40.add(lblCid);

    comboBox_15 = new JComboBox(socketIds);
    comboBox_15.setBounds(119, 133, 39, 20);
    panel_40.add(comboBox_15);

    button_2 = new JButton("FTP Get");
    button_2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String ftp_address = textField_19.getText();
        if (ftp_address.equals(""))
        {
          msg = "FTP address cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String uname = textField_24.getText();
        if (uname.equals(""))
        {
          msg = "User name cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String passwd = textField_25.getText();
        if (passwd.equals(""))
        {
          msg = "Password cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String fname = textField_26.getText();
        if (fname.equals(""))
        {
          msg = "File name cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String dname = textField_27.getText();
        if (dname.equals(""))
        {
          dname = "/";
        }

        String cid = (String)comboBox_15.getSelectedItem();

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "ftp_get_start" });
        }
        msg = "Checking registration status...\n\n";
        writeOutput(msg);
        reg_status = -1;

        String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if ((reg_status == 1) || (reg_status == 5))
        {
          int selected_connId = Integer.parseInt((String)comboBox_14.getSelectedItem());

          ret = mySerial1.ser_write("AT#SCFG?\r\n", 1);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException1) {
          }
          if (scfg_status.equals("FALSE"))
          {
            msg = "This is only supported for Telit modules\n\n";
            writeOutput(msg);
            return;
          }

          String selected_cid = pdpId[(selected_connId - 1)];

          msg = "Checking if socket connection is activated...\n\n";
          writeOutput(msg);

          ret = mySerial1.ser_write("AT#SGACT?\r\n", 1);
          try {
            Thread.sleep(2000L);
          }
          catch (InterruptedException localInterruptedException2)
          {
          }

          for (int k = 0; k < 6; k++)
          {
            if (selected_cid.equals(socketPdpId[k]))
            {
              if (socketPdpStatus[k].equals("Active"))
              {
                msg = ("cid " + selected_cid + " is already active" + "\n\n");
                writeOutput(msg);
              }
              else
              {
                msg = ("cid " + selected_cid + " is not active Activating now.." + "\n\n");
                writeOutput(msg);

                ret = mySerial1.ser_write("AT#SGACT=" + selected_cid + ",1" + "\r\n", 1);
                try {
                  Thread.sleep(2000L);
                } catch (InterruptedException localInterruptedException3) {
                }
                if (socket_pdp_activation_status.equals("FAIL"))
                {
                  writeOutput("Error activating PDP context " + selected_cid);
                  return;
                }

              }

              msg = "Open the FTP connection..\n\n";
              writeOutput(msg);

              telit_ftp_status = "FAIL";
              String cmd = "AT#FTPTO=1000\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException4) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPOPEN=\"" + ftp_address + "\"," + "\"" + uname + "\"," + "\"" + passwd + "\",0" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(12000L);
              } catch (InterruptedException localInterruptedException5) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPTYPE=0\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException6) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPCWD=\"" + dname + "\"" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException7) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPGET=\"" + fname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(10000L);
              }
              catch (InterruptedException localInterruptedException8)
              {
              }

              cmd = "AT#FTPCLOSE\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException9) {
              }
              if (winflag != 1) break;
              win.call("setMessage", new String[] { "Tester", "ftp_get_success" });

              break;
            }
          }
        }
      }
    });
    button_2.setBounds(20, 261, 89, 23);
    panel_40.add(button_2);

    label_6 = new JLabel("File Name");
    label_6.setBounds(20, 86, 75, 14);
    panel_40.add(label_6);

    textField_26 = new JTextField();
    textField_26.setColumns(10);
    textField_26.setBounds(119, 83, 181, 20);
    panel_40.add(textField_26);

    label_7 = new JLabel("Directory");
    label_7.setBounds(20, 111, 75, 14);
    panel_40.add(label_7);

    textField_27 = new JTextField();
    textField_27.setColumns(10);
    textField_27.setBounds(119, 108, 181, 20);
    panel_40.add(textField_27);

    button_3 = new JButton("FTP Put");
    button_3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String ftp_address = textField_19.getText();
        if (ftp_address.equals(""))
        {
          msg = "FTP address cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String uname = textField_24.getText();
        if (uname.equals(""))
        {
          msg = "User name cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String passwd = textField_25.getText();
        if (passwd.equals(""))
        {
          msg = "Password cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String fname = textField_26.getText();
        if (fname.equals(""))
        {
          msg = "File name cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String dname = textField_27.getText();
        if (dname.equals(""))
        {
          dname = "/";
        }

        String ftp_put_data = textArea_15.getText();

        if (ftp_put_data.equals(""))
        {
          msg = "FTP put data cannot be empty\n\n";
          writeOutput(msg);
          return;
        }

        String cid = (String)comboBox_15.getSelectedItem();

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "ftp_put_start" });
        }
        msg = "Checking registration status...\n\n";
        writeOutput(msg);
        reg_status = -1;

        String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if ((reg_status == 1) || (reg_status == 5))
        {
          int selected_connId = Integer.parseInt((String)comboBox_14.getSelectedItem());

          ret = mySerial1.ser_write("AT#SCFG?\r\n", 1);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException1) {
          }
          if (scfg_status.equals("FALSE"))
          {
            msg = "This is only supported for Telit modules\n\n";
            writeOutput(msg);
            return;
          }

          String selected_cid = pdpId[(selected_connId - 1)];

          msg = "Checking if socket connection is activated...\n\n";
          writeOutput(msg);

          ret = mySerial1.ser_write("AT#SGACT?\r\n", 1);
          try {
            Thread.sleep(2000L);
          }
          catch (InterruptedException localInterruptedException2)
          {
          }

          for (int k = 0; k < 6; k++)
          {
            if (selected_cid.equals(socketPdpId[k]))
            {
              if (socketPdpStatus[k].equals("Active"))
              {
                msg = ("cid " + selected_cid + " is already active" + "\n\n");
                writeOutput(msg);
              }
              else
              {
                msg = ("cid " + selected_cid + " is not active Activating now.." + "\n\n");
                writeOutput(msg);

                ret = mySerial1.ser_write("AT#SGACT=" + selected_cid + ",1" + "\r\n", 1);
                try {
                  Thread.sleep(2000L);
                } catch (InterruptedException localInterruptedException3) {
                }
                if (socket_pdp_activation_status.equals("FAIL"))
                {
                  writeOutput("Error activating PDP context " + selected_cid);
                  return;
                }

              }

              msg = "Open the FTP connection..\n\n";
              writeOutput(msg);

              telit_ftp_status = "FAIL";
              String cmd = "AT#FTPTO=1000\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException4) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPOPEN=\"" + ftp_address + "\"," + "\"" + uname + "\"," + "\"" + passwd + "\",0" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(12000L);
              } catch (InterruptedException localInterruptedException5) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPTYPE=0\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException6) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPCWD=\"" + dname + "\"" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException7) {
              }
              if (!telit_ftp_status.equals("SUCCESS"))
              {
                writeOutput("Error Opening FTP connection\r\n");
                return;
              }

              telit_ftp_status = "FAIL";
              cmd = "AT#FTPPUT=\"" + fname + "\"\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(4000L);
              }
              catch (InterruptedException localInterruptedException8)
              {
              }

              writeOutput("Transferring data: " + ftp_put_data + "\r\n");
              ret = mySerial1.ser_write(ftp_put_data, 1);
              try {
                Thread.sleep(10000L);
              } catch (InterruptedException localInterruptedException9) {
              }
              writeOutput("Upload complete..\r\n");
              ret = mySerial1.ser_write("+++", 1);
              try {
                Thread.sleep(2000L);
              } catch (InterruptedException localInterruptedException10) {
              }
              writeOutput("Closing FTP session..\r\n");
              cmd = "AT#FTPCLOSE\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              }
              catch (InterruptedException localInterruptedException11) {
              }
              if (winflag != 1) break;
              win.call("setMessage", new String[] { "Tester", "ftp_put_success" });

              break;
            }
          }
        }
      }
    });
    button_3.setBounds(129, 261, 89, 23);
    panel_40.add(button_3);

    scrollPane_20 = new JScrollPane();
    scrollPane_20.setBounds(20, 176, 385, 74);
    panel_40.add(scrollPane_20);

    textArea_15 = new JTextArea();
    scrollPane_20.setViewportView(textArea_15);

    lblFtpTestingFor = new URLLabel("FTP tutorial for Simcom modules", "http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
    lblFtpTestingFor.setBounds(131, 19, 250, 14);
    panel_35.add(lblFtpTestingFor);

    panel_37 = new JPanel();
    tabbedPane.addTab("GPS", null, panel_37, null);
    panel_37.setLayout(null);

    String[] gps_support = { "Simcom", "Telit", "Huawei" };
    comboBox_10 = new JComboBox(gps_support);
    comboBox_10.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout)panel_38.getLayout();
        gps_mod_sel = ((String)e.getItem());

        String txt = "GPS tutorial for " + gps_mod_sel + " modules";
        cl.show(panel_38, (String)e.getItem());
        lblGpsTestingFor.setText(txt);

        if (gps_mod_sel.equals("Simcom"))
          lblGpsTestingFor.setURL("http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
        else if (gps_mod_sel.equals("Telit"))
          lblGpsTestingFor.setURL("http://m2msupport.net/m2msupport/at-command-tester-tutorial-for-gsm-gps-playground/");
        else if (gps_mod_sel.equals("Huawei"))
          lblGpsTestingFor.setURL("http://m2msupport.net/m2msupport/tutorial-for-huawei-modules/");
      }
    });
    comboBox_10.setBounds(21, 11, 74, 20);
    panel_37.add(comboBox_10);

    lblGpsTestingFor = new URLLabel("GPS tutorial for Simcom modules", "http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
    lblGpsTestingFor.setBounds(105, 14, 219, 14);
    panel_37.add(lblGpsTestingFor);

    panel_38 = new JPanel();
    panel_38.setBounds(0, 42, 427, 567);
    panel_37.add(panel_38);
    panel_38.setLayout(new CardLayout(0, 0));

    panel_24 = new JPanel();
    panel_38.add(panel_24, "Simcom");
    panel_24.setLayout(null);

    panel_25 = new JPanel();
    panel_25.setBorder(new TitledBorder(null, "GPS Control", 4, 2, null, null));
    panel_25.setBounds(16, 11, 278, 266);
    panel_24.add(panel_25);
    panel_25.setLayout(null);

    btnGpsOn = new JButton("GPS On Test");
    btnGpsOn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Turning on GPS..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSPWR=1\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnGpsOn.setBounds(10, 33, 110, 23);
    panel_25.add(btnGpsOn);

    btnGpsOff = new JButton("GPS Off");
    btnGpsOff.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Turning off GPS..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSPWR=0\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnGpsOff.setBounds(141, 33, 110, 23);
    panel_25.add(btnGpsOff);

    btnGpsReset = new JButton("GPS Reset - Cold");
    btnGpsReset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "GPS Reset in COLD start mode..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSRST=0\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnGpsReset.setBounds(10, 79, 110, 23);
    panel_25.add(btnGpsReset);
    btnGpsReset.setMargin(new Insets(2, 0, 2, 0));

    btnGpsReset_1 = new JButton("GPS Reset - Auto");
    btnGpsReset_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "GPS Reset in autonomy mode..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSRST=1\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnGpsReset_1.setBounds(141, 79, 110, 23);
    panel_25.add(btnGpsReset_1);
    btnGpsReset_1.setMargin(new Insets(2, 0, 2, 0));

    btnGpsStatus = new JButton("GPS Status");
    btnGpsStatus.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Getting GPS Status..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSSTATUS?\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnGpsStatus.setBounds(10, 170, 110, 23);
    panel_25.add(btnGpsStatus);

    btnGpsNmeaOn = new JButton("GPS NMEA On");
    btnGpsNmeaOn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Enabling NMEA output..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSOUT=255\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnGpsNmeaOn.setBounds(10, 125, 110, 23);
    panel_25.add(btnGpsNmeaOn);
    btnGpsNmeaOn.setMargin(new Insets(2, 0, 2, 0));

    btnGpsNmeaOff = new JButton("GPS NMEA Off");
    btnGpsNmeaOff.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Disabling NMEA output..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSOUT=0\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnGpsNmeaOff.setBounds(141, 125, 110, 23);
    panel_25.add(btnGpsNmeaOff);
    btnGpsNmeaOff.setMargin(new Insets(2, 0, 2, 0));

    lblNmeaPortSpeed = new JLabel("NMEA Port Speed");
    lblNmeaPortSpeed.setBounds(10, 219, 110, 14);
    panel_25.add(lblNmeaPortSpeed);

    comboBox_6 = new JComboBox(port_speed);
    comboBox_6.setBounds(125, 216, 59, 20);
    panel_25.add(comboBox_6);

    btnSet = new JButton("Set");
    btnSet.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Setting NMEA port speed..\n\n";
        writeOutput(msg);

        String speed = (String)comboBox_6.getSelectedItem();

        String cmd = "AT+CGPSIPR=" + speed + "\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    btnSet.setBounds(194, 213, 59, 23);
    panel_25.add(btnSet);

    textField_17 = new JTextField();
    textField_17.setEditable(false);
    textField_17.setBounds(141, 171, 110, 20);
    panel_25.add(textField_17);
    textField_17.setColumns(10);

    panel_26 = new JPanel();
    panel_26.setBorder(new TitledBorder(null, "GPS Location", 4, 2, null, null));
    panel_26.setBounds(16, 288, 290, 263);
    panel_24.add(panel_26);
    panel_26.setLayout(null);

    textField_10 = new JTextField();
    textField_10.setBounds(128, 23, 114, 20);
    panel_26.add(textField_10);
    textField_10.setEditable(false);
    textField_10.setColumns(10);

    lblLatitude = new JLabel("Latitude");
    lblLatitude.setBounds(25, 26, 55, 14);
    panel_26.add(lblLatitude);

    lblLongitude = new JLabel("Longitude");
    lblLongitude.setBounds(25, 51, 66, 14);
    panel_26.add(lblLongitude);

    lblAltitude = new JLabel("Altitude");
    lblAltitude.setBounds(25, 76, 46, 14);
    panel_26.add(lblAltitude);

    lblTimeToFirst = new JLabel("Time to first Fix");
    lblTimeToFirst.setBounds(25, 101, 96, 14);
    panel_26.add(lblTimeToFirst);

    lblNumSatellites = new JLabel("Num Satellites");
    lblNumSatellites.setBounds(25, 126, 93, 14);
    panel_26.add(lblNumSatellites);

    lblSpeed = new JLabel("Speed");
    lblSpeed.setBounds(25, 151, 46, 14);
    panel_26.add(lblSpeed);

    textField_11 = new JTextField();
    textField_11.setBounds(128, 48, 114, 20);
    panel_26.add(textField_11);
    textField_11.setEditable(false);
    textField_11.setColumns(10);

    lblCourse = new JLabel("Course");
    lblCourse.setBounds(25, 176, 46, 14);
    panel_26.add(lblCourse);

    textField_12 = new JTextField();
    textField_12.setBounds(128, 73, 114, 20);
    panel_26.add(textField_12);
    textField_12.setEditable(false);
    textField_12.setColumns(10);

    textField_13 = new JTextField();
    textField_13.setBounds(128, 98, 114, 20);
    panel_26.add(textField_13);
    textField_13.setEditable(false);
    textField_13.setColumns(10);

    textField_14 = new JTextField();
    textField_14.setBounds(128, 123, 114, 20);
    panel_26.add(textField_14);
    textField_14.setEditable(false);
    textField_14.setColumns(10);

    textField_15 = new JTextField();
    textField_15.setBounds(128, 148, 114, 20);
    panel_26.add(textField_15);
    textField_15.setEditable(false);
    textField_15.setColumns(10);

    textField_16 = new JTextField();
    textField_16.setBounds(128, 173, 114, 20);
    panel_26.add(textField_16);
    textField_16.setEditable(false);
    textField_16.setColumns(10);

    btnNewButton = new JButton("Get GPS Location");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Getting GPS Status..\n\n";
        writeOutput(msg);

        String cmd = "AT+CGPSSTATUS?\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(400L);
        } catch (InterruptedException localInterruptedException) {
        }
        if ((global_gps_status.equals("gps_3D_fix")) || (global_gps_status.equals("gps_2D_fix")))
        {
          cmd = "AT+CGPSINF=0\r\n";
          ret = mySerial1.ser_write(cmd, 1);
        }
        else
        {
          msg = "GPS information not available. Please check whether GPS is turned on and reset. Also try testing GPS in outdoor.\n\n";
          writeOutput(msg);
        }
      }
    });
    btnNewButton.setMargin(new Insets(2, 0, 2, 0));
    btnNewButton.setBounds(25, 229, 124, 23);
    panel_26.add(btnNewButton);

    btnShowMap = new JButton("Show Map");
    btnShowMap.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String lat = textField_10.getText();
        String lon = textField_11.getText();
        lat = "37.81888";
        lon = "122.4784";
        if ((!lat.equals("")) && (!lon.equals("")))
        {
          String url = "http://maps.google.com/?q=";

          String loc_inf = lat + "," + lon;
          url = url + loc_inf;

          Desktop dt = Desktop.getDesktop();
          URI uri = null;
          try {
            uri = new URI(url);
          }
          catch (URISyntaxException e1) {
            e1.printStackTrace();
          }
          try {
            dt.browse(uri.resolve(uri));
          }
          catch (IOException e1) {
            e1.printStackTrace();
          }
        }
        else
        {
          msg = "Location information is not available..\n\n";
          writeOutput(msg);
        }
      }
    });
    btnShowMap.setBounds(159, 229, 102, 23);
    panel_26.add(btnShowMap);

    lblTime = new JLabel("Time");
    lblTime.setBounds(25, 201, 46, 14);
    panel_26.add(lblTime);

    textField_18 = new JTextField();
    textField_18.setEditable(false);
    textField_18.setBounds(128, 204, 114, 20);
    panel_26.add(textField_18);
    textField_18.setColumns(10);

    panel_41 = new JPanel();
    panel_41.setLayout(null);
    panel_38.add(panel_41, "Telit");

    panel_42 = new JPanel();
    panel_42.setLayout(null);
    panel_42.setBorder(new TitledBorder(null, "GPS Control", 4, 2, null, null));
    panel_42.setBounds(16, 11, 278, 216);
    panel_41.add(panel_42);

    button_4 = new JButton("GPS On");
    button_4.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS On..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSP=1\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_4.setBounds(10, 33, 117, 23);
    panel_42.add(button_4);

    button_5 = new JButton("GPS Off");
    button_5.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS off..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSP=0\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_5.setBounds(141, 33, 117, 23);
    panel_42.add(button_5);

    button_6 = new JButton("GPS Reset - Cold");
    button_6.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS Reset Cold..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSR=1\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_6.setMargin(new Insets(2, 0, 2, 0));
    button_6.setBounds(10, 67, 117, 23);
    panel_42.add(button_6);

    btnGpsReset_3 = new JButton("GPS Reset - Warm");
    btnGpsReset_3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS Reset Warm..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSR=2\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnGpsReset_3.setMargin(new Insets(2, 0, 2, 0));
    btnGpsReset_3.setBounds(141, 67, 117, 23);
    panel_42.add(btnGpsReset_3);

    button_9 = new JButton("GPS NMEA On");
    button_9.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS NMEA On.\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSNMUN=1\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_9.setMargin(new Insets(2, 0, 2, 0));
    button_9.setBounds(10, 133, 117, 23);
    panel_42.add(button_9);

    button_10 = new JButton("GPS NMEA Off");
    button_10.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS NMEA Off..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSNMUN=0\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_10.setMargin(new Insets(2, 0, 2, 0));
    button_10.setBounds(141, 133, 117, 23);
    panel_42.add(button_10);

    label_8 = new JLabel("NMEA Port Speed");
    label_8.setBounds(10, 181, 110, 14);
    panel_42.add(label_8);

    comboBox_16 = new JComboBox(port_speed_1);
    comboBox_16.setBounds(120, 178, 59, 20);
    panel_42.add(comboBox_16);

    button_11 = new JButton("Set");
    button_11.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Setting NMEA port speed..\n\n";
        writeOutput(msg);

        String speed = (String)comboBox_16.getSelectedItem();

        String cmd = "AT$GPSS=speed\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
      }
    });
    button_11.setBounds(189, 177, 59, 23);
    panel_42.add(button_11);

    btnGpsReset_2 = new JButton("GPS Reset Factory");
    btnGpsReset_2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS Reset Factory..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSR=0\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnGpsReset_2.setMargin(new Insets(2, 0, 2, 0));
    btnGpsReset_2.setBounds(10, 101, 117, 23);
    panel_42.add(btnGpsReset_2);

    btnGpsReset_4 = new JButton("GPS Reset - Hot");
    btnGpsReset_4.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Telit - GPS Reset Hot..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSR=3\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnGpsReset_4.setMargin(new Insets(2, 0, 2, 0));
    btnGpsReset_4.setBounds(141, 101, 117, 23);
    panel_42.add(btnGpsReset_4);

    panel_43 = new JPanel();
    panel_43.setLayout(null);
    panel_43.setBorder(new TitledBorder(null, "GPS Location", 4, 2, null, null));
    panel_43.setBounds(16, 254, 290, 263);
    panel_41.add(panel_43);

    textField_29 = new JTextField();
    textField_29.setEditable(false);
    textField_29.setColumns(10);
    textField_29.setBounds(128, 23, 114, 20);
    panel_43.add(textField_29);

    label_9 = new JLabel("Latitude");
    label_9.setBounds(25, 26, 55, 14);
    panel_43.add(label_9);

    label_10 = new JLabel("Longitude");
    label_10.setBounds(25, 51, 66, 14);
    panel_43.add(label_10);

    label_11 = new JLabel("Altitude");
    label_11.setBounds(25, 76, 46, 14);
    panel_43.add(label_11);

    label_13 = new JLabel("Num Satellites");
    label_13.setBounds(25, 104, 93, 14);
    panel_43.add(label_13);

    label_14 = new JLabel("Speed");
    label_14.setBounds(25, 129, 46, 14);
    panel_43.add(label_14);

    textField_30 = new JTextField();
    textField_30.setEditable(false);
    textField_30.setColumns(10);
    textField_30.setBounds(128, 48, 114, 20);
    panel_43.add(textField_30);

    label_15 = new JLabel("Course");
    label_15.setBounds(25, 154, 46, 14);
    panel_43.add(label_15);

    textField_31 = new JTextField();
    textField_31.setEditable(false);
    textField_31.setColumns(10);
    textField_31.setBounds(128, 73, 114, 20);
    panel_43.add(textField_31);

    textField_33 = new JTextField();
    textField_33.setEditable(false);
    textField_33.setColumns(10);
    textField_33.setBounds(128, 101, 114, 20);
    panel_43.add(textField_33);

    textField_34 = new JTextField();
    textField_34.setEditable(false);
    textField_34.setColumns(10);
    textField_34.setBounds(128, 126, 114, 20);
    panel_43.add(textField_34);

    textField_35 = new JTextField();
    textField_35.setEditable(false);
    textField_35.setColumns(10);
    textField_35.setBounds(128, 151, 114, 20);
    panel_43.add(textField_35);

    button_12 = new JButton("Get GPS Location");
    button_12.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Getting GPS Status..\n\n";
        writeOutput(msg);

        String cmd = "AT$GPSACP\r\n";
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_12.setMargin(new Insets(2, 0, 2, 0));
    button_12.setBounds(25, 229, 124, 23);
    panel_43.add(button_12);

    button_13 = new JButton("Show Map");
    button_13.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String lat = textField_29.getText();
        String lon = textField_30.getText();

        if ((!lat.equals("")) && (!lon.equals("")))
        {
          String url = "http://maps.google.com/?q=";

          String loc_inf = lat + "," + lon;
          url = url + loc_inf;

          Desktop dt = Desktop.getDesktop();
          URI uri = null;
          try {
            uri = new URI(url);
          }
          catch (URISyntaxException e1) {
            e1.printStackTrace();
          }
          try {
            dt.browse(uri.resolve(uri));
          }
          catch (IOException e1) {
            e1.printStackTrace();
          }
        }
        else
        {
          msg = "Location information is not available..\n\n";
          writeOutput(msg);
        }
      }
    });
    button_13.setBounds(159, 229, 102, 23);
    panel_43.add(button_13);

    label_16 = new JLabel("Time");
    label_16.setBounds(25, 179, 46, 14);
    panel_43.add(label_16);

    textField_36 = new JTextField();
    textField_36.setEditable(false);
    textField_36.setColumns(10);
    textField_36.setBounds(128, 182, 114, 20);
    panel_43.add(textField_36);

    panel_60 = new JPanel();
    panel_60.setLayout(null);
    panel_38.add(panel_60, "Huawei");

    panel_61 = new JPanel();
    panel_61.setLayout(null);
    panel_61.setBorder(new TitledBorder(null, "GPS Control", 4, 2, null, null));
    panel_61.setBounds(16, 11, 382, 145);
    panel_60.add(panel_61);

    lblGpsMode_1 = new JLabel("GPS Mode");
    lblGpsMode_1.setBounds(10, 29, 73, 14);
    panel_61.add(lblGpsMode_1);

    String[] huawei_gps_mode = { "Standalone", "MS-Assisted", "gpsOneXTRA" };
    comboBox_21 = new JComboBox(huawei_gps_mode);
    comboBox_21.setBounds(81, 26, 100, 20);
    panel_61.add(comboBox_21);

    btnNewButton_2 = new JButton("Set");
    btnNewButton_2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String tcp_gps_sel = (String)comboBox_21.getSelectedItem();
        String cmd = "";
        if (tcp_gps_sel.equals("Standalone"))
          cmd = "AT^WPDOM=0\r\n";
        else if (tcp_gps_sel.equals("MS-Assisted"))
          cmd = "AT^WPDOM=1\r\n";
        else if (tcp_gps_sel.equals("gpsOneXTRA")) {
          cmd = "AT^WPDOM=6\r\n";
        }
        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnNewButton_2.setBounds(198, 25, 56, 23);
    panel_61.add(btnNewButton_2);

    btnGet_1 = new JButton("Get");
    btnGet_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }

        String cmd = "AT^WPDOM?\r\n";

        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnGet_1.setBounds(264, 25, 56, 23);
    panel_61.add(btnGet_1);

    lblAgpsServerAddress = new JLabel("A-GPS Server address");
    lblAgpsServerAddress.setBounds(10, 64, 137, 14);
    panel_61.add(lblAgpsServerAddress);

    txtSuplgooglecom = new JTextField();
    txtSuplgooglecom.setText("SUPL.GOOGLE.COM:7276");
    txtSuplgooglecom.setBounds(144, 61, 186, 20);
    panel_61.add(txtSuplgooglecom);
    txtSuplgooglecom.setColumns(10);

    btnStartPositioning = new JButton("Start Positioning");
    btnStartPositioning.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }

        msg = "Set session type to positioning...\n";
        writeOutput(msg);

        String cmd = "AT^WPDST=0\r\n";

        String ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        msg = "Set number of positions and interval between each trakcing...\n";
        writeOutput(msg);

        cmd = "AT^WPDFR=65535,1\r\n";

        ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException1) {
        }
        msg = "Set service quality...\n";
        writeOutput(msg);

        cmd = "AT^WPQOS=255,500\r\n";

        ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException2) {
        }
        String tcp_gps_sel = (String)comboBox_21.getSelectedItem();

        if (tcp_gps_sel.equals("MS-Assisted"))
        {
          msg = "GPS mode is MS-Assisted, setting the A-GPS server..\n";
          writeOutput(msg);

          String agps_server = txtSuplgooglecom.getText();

          if (agps_server.equals(""))
          {
            msg = "A-GPS server address is needed for MS-Assisted and MS-based mode..\n";
            writeOutput(msg);
            return;
          }

          cmd = "AT^WPURL=" + agps_server + "\r\n";
          ret = mySerial1.ser_write(cmd, 1);
          try
          {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException3) {
          }
          msg = "Set up PDP context for A-GPS server access..\n";
          writeOutput(msg);

          ret = mySerial1.ser_write("AT+CGDCONT?\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException4) {
          }
          if (default_apn.equals("")) {
            writeOutput("No APNs available. Please setup connection profile in 'Data' tab\n");
            return;
          }

          ret = mySerial1.ser_write("AT+CGDCONT=15,\"IP\",\"" + default_apn + "\"\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
          try {
            Thread.sleep(2000L);
          }
          catch (InterruptedException localInterruptedException5)
          {
          }

        }

        textField_50.setText("");
        textField_51.setText("");

        msg = "Start positioning..\n";
        writeOutput(msg);

        cmd = "AT^WPDGP\r\n";

        ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException6)
        {
        }
      }
    });
    btnStartPositioning.setBounds(10, 104, 137, 23);
    panel_61.add(btnStartPositioning);

    panel_62 = new JPanel();
    panel_62.setLayout(null);
    panel_62.setBorder(new TitledBorder(null, "GPS Location", 4, 2, null, null));
    panel_62.setBounds(16, 179, 290, 139);
    panel_60.add(panel_62);

    textField_50 = new JTextField();
    textField_50.setEditable(false);
    textField_50.setColumns(10);
    textField_50.setBounds(75, 23, 114, 20);
    panel_62.add(textField_50);

    label_25 = new JLabel("Latitude");
    label_25.setBounds(10, 29, 55, 14);
    panel_62.add(label_25);

    label_26 = new JLabel("Longitude");
    label_26.setBounds(10, 54, 66, 14);
    panel_62.add(label_26);

    label_27 = new JLabel("Altitude");
    label_27.setBounds(10, 79, 46, 14);
    panel_62.add(label_27);

    textField_51 = new JTextField();
    textField_51.setEditable(false);
    textField_51.setColumns(10);
    textField_51.setBounds(75, 48, 114, 20);
    panel_62.add(textField_51);

    textField_52 = new JTextField();
    textField_52.setEditable(false);
    textField_52.setColumns(10);
    textField_52.setBounds(75, 73, 114, 20);
    panel_62.add(textField_52);

    button_150 = new JButton("Show Map");
    button_150.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String lat = textField_50.getText();
        lat = lat.replace("d", "");

        String lon = textField_51.getText();
        lon = lon.replace("d", "");

        if ((!lat.equals("")) && (!lon.equals("")))
        {
          String url = "http://maps.google.com/?q=";

          String loc_inf = lat + "," + lon;
          url = url + loc_inf;

          Desktop dt = Desktop.getDesktop();
          URI uri = null;
          try {
            uri = new URI(url);
          }
          catch (URISyntaxException e1) {
            e1.printStackTrace();
          }
          try {
            dt.browse(uri.resolve(uri));
          }
          catch (IOException e1) {
            e1.printStackTrace();
          }
        }
        else
        {
          msg = "Location information is not available..\n\n";
          writeOutput(msg);
        }
      }
    });
    button_150.setBounds(10, 105, 102, 23);
    panel_62.add(button_150);

    panel_33 = new JPanel();
    tabbedPane.addTab("TCP/UDP", null, panel_33, null);
    panel_33.setLayout(null);

    String[] tcp_support = { "Simcom", "Telit", "Quectel", "Huawei" };
    comboBox_8 = new JComboBox(tcp_support);
    comboBox_8.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout)panel_34.getLayout();
        tcp_mod_sel = ((String)e.getItem());

        String txt = "TCP/UDP tutorial for " + tcp_mod_sel + " modules";
        cl.show(panel_34, (String)e.getItem());
        module_select_label.setText(txt);

        if (tcp_mod_sel.equals("Simcom"))
          module_select_label.setURL("http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
        else if (tcp_mod_sel.equals("Telit"))
          module_select_label.setURL("http://m2msupport.net/m2msupport/at-command-tester-tutorial-for-gsm-gps-playground/");
        else if (tcp_mod_sel.equals("Huawei"))
          module_select_label.setURL("http://m2msupport.net/m2msupport/tutorial-for-huawei-modules/");
      }
    });
    comboBox_8.setBounds(10, 6, 91, 20);
    panel_33.add(comboBox_8);

    panel_34 = new JPanel();
    panel_34.setBounds(0, 35, 427, 529);
    panel_33.add(panel_34);
    panel_34.setLayout(new CardLayout(0, 0));

    panel_27 = new JPanel();
    panel_34.add(panel_27, "Simcom");
    panel_27.setLayout(null);

    panel_28 = new JPanel();
    panel_28.setBorder(new TitledBorder(null, "Connection", 4, 2, null, null));
    panel_28.setBounds(10, 11, 407, 212);
    panel_27.add(panel_28);
    panel_28.setLayout(null);

    lblServerIp = new JLabel("Server IP");
    lblServerIp.setBounds(10, 117, 69, 14);
    panel_28.add(lblServerIp);

    txtWwwmmsupportnet = new JTextField();
    txtWwwmmsupportnet.setText("74.124.194.252");
    txtWwwmmsupportnet.setBounds(89, 114, 108, 20);
    panel_28.add(txtWwwmmsupportnet);
    txtWwwmmsupportnet.setColumns(10);

    lblPort_1 = new JLabel("Port");
    lblPort_1.setBounds(10, 145, 46, 14);
    panel_28.add(lblPort_1);

    textField_20 = new JTextField();
    textField_20.setText("80");
    textField_20.setBounds(89, 142, 46, 20);
    panel_28.add(textField_20);
    textField_20.setColumns(10);

    btnConnect_2 = new JButton("Connect");
    btnConnect_2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String apn = textField_21.getText();
            if (apn.equals(""))
            {
              msg = "APN is not set. You can get the APN from the 'Data Call' tab\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_ip = txtWwwmmsupportnet.getText();
            if (server_ip.equals(""))
            {
              msg = "Server IP is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_port = textField_20.getText();
            if (server_port.equals(""))
            {
              msg = "Server Port is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = "Checking if device is already connected...\n\n";
              writeOutput(msg);
              num_profiles_connected = 0;
              connected_profile = null;
              ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException1) {
              }
              if (num_profiles_connected > 0)
              {
                msg = ("Disconnected profile " + connected_profile + "\n");
                writeOutput(msg);
                cgact_action = "deactivate";
                String t5 = "AT+CGACT=0," + connected_profile + "\r\n";
                ret = mySerial1.ser_write(t5, 1);
                try {
                  Thread.sleep(5000L);
                }
                catch (InterruptedException localInterruptedException2) {
                }
              }
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "call_registraiton_ok" });
              ret = mySerial1.ser_write("AT+CMEE=1\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException3) {
              }
              msg = "Attaching to network...\n";
              writeOutput(msg);
              ret = mySerial1.ser_write("AT+CGATT=1\r\n", 1);
              try {
                Thread.sleep(3000L); } catch (InterruptedException localInterruptedException4) {
              }
              if (attach_status == 1)
              {
                if (winflag == 1)
                  win.call("setMessage", new String[] { "Tester", "call_attach_success" });
                connect_status = 0;
                msg = "Setting up APN for TCP connection...\n\n";
                writeOutput(msg);
                String t2 = "AT+CSTT=\"" + apn + "\"\r\n";
                ret = mySerial1.ser_write(t2, 1);
                try
                {
                  Thread.sleep(500L);
                } catch (InterruptedException localInterruptedException5) {
                }
                if (!cstt_status.equals("OK"))
                {
                  msg = "Unable to set APN for TCP/UDP connection...\n\n";
                  writeOutput(msg);
                  if (winflag == 1)
                    win.call("setMessage", new String[] { "Tester", "tcpudp_apn_set_fail" });
                  return Integer.valueOf(1);
                }

                msg = "Bring up GPRS Connection...\n\n";
                writeOutput(msg);
                t2 = "AT+CIICR\r\n";
                ret = mySerial1.ser_write(t2, 1);
                try
                {
                  Thread.sleep(2000L);
                } catch (InterruptedException localInterruptedException6) {
                }
                if (ciicr_status.equals("OK"))
                {
                  if (winflag == 1) {
                    win.call("setMessage", new String[] { "Tester", "tcpudp_call_connect_success" });
                  }
                  tcp_local_ip_address = "";
                  String t3 = "AT+CIFSR\r\n";
                  ret = mySerial1.ser_write(t3, 1);
                  try
                  {
                    Thread.sleep(500L);
                  } catch (InterruptedException localInterruptedException7) {
                  }
                  if (tcp_local_ip_address.equals("ERROR"))
                  {
                    msg = "Unable to get local IP address for TCP/UDP connection...\n\n";
                    writeOutput(msg);
                    if (winflag == 1)
                      win.call("setMessage", new String[] { "Tester", "tcpudp_local_ip_get_fail" });
                    return Integer.valueOf(1);
                  }

                  t3 = "AT+CIPSTART=\"TCP\",\"" + server_ip + "\",\"" + server_port + "\"\r\n";
                  ret = mySerial1.ser_write(t3, 1);
                  try
                  {
                    Thread.sleep(500L);
                  }
                  catch (InterruptedException localInterruptedException8)
                  {
                  }
                }
                else
                {
                  msg = "Unable to bring up GPRS Connection for TCP/UDP connection...\n\n";
                  writeOutput(msg);
                  if (winflag == 1)
                    win.call("setMessage", new String[] { "Tester", "tcpudp_call_connect_fail" });
                  return Integer.valueOf(1);
                }

              }
              else
              {
                if (winflag == 1)
                  win.call("setMessage", new String[] { "Tester", "call_connect_unsucessful" });
                msg = "Unable to attach to the network...\n\n";
                writeOutput(msg);
              }

            }
            else
            {
              msg = "Cannot connect without device being registered\n\n";
              writeOutput(msg);
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "call_registration_check_fail" });
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnConnect_2.setBounds(10, 173, 89, 23);
    panel_28.add(btnConnect_2);
    btnConnect_2.setMargin(new Insets(2, 0, 2, 0));

    btnDisconnect_1 = new JButton("Dis-Connect");
    btnDisconnect_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL"))
          return;
        if (tcp_connection_status.equals("CONNECT OK"))
        {
          String t3 = "AT+CIPCLOSE=0\r\n";
          String ret = mySerial1.ser_write(t3, 1);
          try
          {
            Thread.sleep(500L);
          } catch (InterruptedException localInterruptedException) {
          }
        }
        else {
          msg = "No TCP session is currently open..\n\n";
          writeOutput(msg);
        }
      }
    });
    btnDisconnect_1.setBounds(108, 173, 89, 23);
    panel_28.add(btnDisconnect_1);
    btnDisconnect_1.setMargin(new Insets(2, 0, 2, 0));

    panel_29 = new JPanel();
    panel_29.setBorder(new TitledBorder(null, "Type", 4, 2, null, null));
    panel_29.setBounds(10, 25, 244, 47);
    panel_28.add(panel_29);
    panel_29.setLayout(null);

    rdbtnTcp = new JRadioButton("TCP");
    rdbtnTcp.setSelected(true);
    rdbtnTcp.setBounds(6, 16, 69, 23);
    panel_29.add(rdbtnTcp);

    rdbtnUdp = new JRadioButton("UDP");
    rdbtnUdp.setEnabled(false);
    rdbtnUdp.setBounds(77, 16, 125, 23);
    panel_29.add(rdbtnUdp);

    lblApn = new JLabel("APN");
    lblApn.setBounds(10, 87, 46, 14);
    panel_28.add(lblApn);

    textField_21 = new JTextField();
    textField_21.setBounds(89, 83, 108, 20);
    panel_28.add(textField_21);
    textField_21.setColumns(10);

    scrollPane_15 = new JScrollPane();
    scrollPane_15.setBounds(10, 270, 389, 122);
    panel_27.add(scrollPane_15);

    txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp = new JTextArea();
    txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp.setText("\r\nGET http://www.m2msupport.net/m2msupport/tcp_udp_test.php HTTP/1.0\r\n\r\n");
    scrollPane_15.setViewportView(txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp);

    lblClientData = new JLabel("Client Data");
    lblClientData.setBounds(10, 234, 81, 14);
    panel_27.add(lblClientData);

    btnSend = new JButton("Send");
    btnSend.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        if (tcp_connection_status.equals("CONNECT OK"))
        {
          msg = "Sending TCP data\n\n";
          writeOutput(msg);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "tcp_data_send" });
          }

          String t3 = "AT+CIPSEND\r\n";
          String ret = mySerial1.ser_write(t3, 1);
          try {
            Thread.sleep(500L);
          } catch (InterruptedException localInterruptedException) {
          }
          String ctz = Character.toString('\032');
          String message = txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp.getText();
          t3 = message + ctz;

          ret = mySerial1.ser_write(t3, 0);
          try {
            Thread.sleep(500L);
          } catch (InterruptedException localInterruptedException1) {
          }
        }
        else {
          msg = "No TCP or UDP connection.\n\n";
          writeOutput(msg);
        }
      }
    });
    btnSend.setBounds(310, 403, 89, 23);
    panel_27.add(btnSend);

    panel_47 = new JPanel();
    panel_47.setLayout(null);
    panel_34.add(panel_47, "Quectel");

    panel_48 = new JPanel();
    panel_48.setLayout(null);
    panel_48.setBorder(new TitledBorder(null, "Connection", 4, 2, null, null));
    panel_48.setBounds(10, 11, 407, 235);
    panel_47.add(panel_48);

    label_5 = new JLabel("Server IP");
    label_5.setBounds(10, 117, 69, 14);
    panel_48.add(label_5);

    textField_41 = new JTextField();
    textField_41.setText("74.124.194.252");
    textField_41.setColumns(10);
    textField_41.setBounds(89, 114, 108, 20);
    panel_48.add(textField_41);

    label_12 = new JLabel("Port");
    label_12.setBounds(10, 145, 46, 14);
    panel_48.add(label_12);

    textField_42 = new JTextField();
    textField_42.setText("80");
    textField_42.setColumns(10);
    textField_42.setBounds(89, 142, 46, 20);
    panel_48.add(textField_42);

    button_7 = new JButton("Connect");
    button_7.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String apn = textField_43.getText();
            if (apn.equals(""))
            {
              msg = "APN is not set. You can get the APN from the 'Data Call' tab\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_ip = textField_41.getText();
            if (server_ip.equals(""))
            {
              msg = "Server Address is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_port = textField_42.getText();
            if (server_port.equals(""))
            {
              msg = "Server Port is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = "Device is registered.. \n\n";
              writeOutput(msg);
            }
            else
            {
              msg = "Device is NOT registered.. \n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Configure the context as foreground...\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIFGCNT\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException1) {
            }
            msg = "Set the APN..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QICSGP=1,\"" + apn + "\"" + "\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException2) {
            }
            msg = "Disable MUXIP..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIMUX=0\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException3) {
            }
            msg = "Set session mode to non-transparent..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIMODE=0\r\n", 1);
            try {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException4) {
            }
            String server_add_type = (String)comboBox_19.getSelectedItem();

            String serv_flag = "";
            if (server_add_type.equals("IP"))
            {
              serv_flag = "0";
              msg = "Setting server address is IP..\n\n";
            }
            else
            {
              serv_flag = "1";
              msg = "Setting server address is Domain Name..\n\n";
            }
            writeOutput(msg);

            ret = mySerial1.ser_write("AT+QIMODE=" + serv_flag + "\r\n", 1);
            try {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException5) {
            }
            msg = "Register the TCP/IP stack..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIREGAPP\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException6) {
            }
            msg = "Activate Foreground context..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIACT\r\n", 1);
            try {
              Thread.sleep(6000L);
            } catch (InterruptedException localInterruptedException7) {
            }
            msg = "Get local IP address..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QILOCIP\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException8) {
            }
            msg = "Connect to the remote server..\n\n";
            writeOutput(msg);
            ret = mySerial1.ser_write("AT+QIOPEN=\"TCP\",\"" + server_ip + "\"," + server_port + "\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException9) {
            }
            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    button_7.setMargin(new Insets(2, 0, 2, 0));
    button_7.setBounds(10, 201, 89, 23);
    panel_48.add(button_7);

    button_8 = new JButton("Dis-Connect");
    button_8.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        msg = "Close the TCP connection..\n\n";
        writeOutput(msg);
        String ret = mySerial1.ser_write("AT+QICLOSE\r\n", 1);
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    button_8.setMargin(new Insets(2, 0, 2, 0));
    button_8.setBounds(109, 201, 89, 23);
    panel_48.add(button_8);

    panel_49 = new JPanel();
    panel_49.setLayout(null);
    panel_49.setBorder(new TitledBorder(null, "Type", 4, 2, null, null));
    panel_49.setBounds(10, 25, 244, 47);
    panel_48.add(panel_49);

    radioButton = new JRadioButton("TCP");
    radioButton.setSelected(true);
    radioButton.setBounds(6, 16, 69, 23);
    panel_49.add(radioButton);

    radioButton_1 = new JRadioButton("UDP");
    radioButton_1.setEnabled(false);
    radioButton_1.setBounds(77, 16, 125, 23);
    panel_49.add(radioButton_1);

    label_17 = new JLabel("APN");
    label_17.setBounds(10, 87, 46, 14);
    panel_48.add(label_17);

    textField_43 = new JTextField();
    textField_43.setColumns(10);
    textField_43.setBounds(89, 83, 108, 20);
    panel_48.add(textField_43);

    lblServerAddressType = new JLabel("Server Address Type");
    lblServerAddressType.setBounds(10, 176, 125, 14);
    panel_48.add(lblServerAddressType);

    comboBox_19 = new JComboBox(server_addr_type);
    comboBox_19.setBounds(137, 173, 90, 20);
    panel_48.add(comboBox_19);

    label_18 = new JLabel("Client Data");
    label_18.setBounds(10, 267, 81, 14);
    panel_47.add(label_18);

    button_14 = new JButton("Send");
    button_14.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String client_data = txtpnGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp.getText();

        if (client_data.equals(""))
        {
          msg = "Client data is empty..\n\n";
          writeOutput(msg);
          return;
        }

        String ret = mySerial1.ser_write("AT+QISEND\r\n", 1);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        String ctz = Character.toString('\032');
        String t3 = client_data + ctz;

        ret = mySerial1.ser_write(t3, 0);
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException1)
        {
        }
      }
    });
    button_14.setBounds(298, 394, 89, 23);
    panel_47.add(button_14);

    scrollPane_2 = new JScrollPane();
    scrollPane_2.setBounds(10, 292, 407, 91);
    panel_47.add(scrollPane_2);

    txtpnGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp = new JTextArea();
    txtpnGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp.setText("\r\nGET /m2msupport/http_get_test.php HTTP/1.1\r\nHost:www.m2msupport.net\r\nConnection:keep-alive\r\n\r\n");
    scrollPane_2.setViewportView(txtpnGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp);

    panel_30 = new JPanel();
    panel_34.add(panel_30, "Telit");
    panel_30.setLayout(null);

    lblNewLabel = new JLabel("Sockets");
    lblNewLabel.setFont(new Font("Tahoma", 1, 13));
    lblNewLabel.setBounds(10, 11, 54, 14);
    panel_30.add(lblNewLabel);
    String[] columnNames2 = { "connId", 
      "cid", 
      "cid Status", 
      "State" };

    Object[][] data2 = null;
    DefaultTableModel model2 = new DefaultTableModel(data2, columnNames2)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    TableColumn column2 = null;

    table_5 = new JTable(model2);
    table_5.setSelectionMode(0);
    JScrollPane scrollPane_2_1 = new JScrollPane(table_5);
    scrollPane_2_1.setBounds(10, 36, 407, 122);
    panel_30.add(scrollPane_2_1);

    btnRefresh = new JButton("Refresh");
    btnRefresh.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "telit_referesh_socket_info" });
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }

        String ret = mySerial1.ser_write("AT#SCFG?\r\n", 1);
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if (scfg_status.equals("FALSE"))
        {
          msg = "This is only supported for Telit modules\n\n";
          writeOutput(msg);
          return;
        }

        ret = mySerial1.ser_write("AT#SGACT?\r\n", 1);
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException localInterruptedException1) {
        }
        ret = mySerial1.ser_write("AT#SS\r\n", 1);
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException localInterruptedException2) {
        }
        DefaultTableModel dm = (DefaultTableModel)table_5.getModel();
        for (int i = dm.getRowCount() - 1; i >= 0; i--) {
          dm.removeRow(i);
        }
        String temp1 = "Not Available";

        for (int j = 0; j < 6; j++)
        {
          temp1 = "Not Available";

          for (int k = 0; k < 6; k++)
          {
            String t8 = pdpId[j];
            String t9 = socketPdpId[k];
            if (t8.equals(t9))
            {
              temp1 = socketPdpStatus[k];
              break;
            }
          }
          dm.addRow(new Object[] { connId[j], pdpId[j], temp1, socketState[j] });
        }

        table_5.setRowSelectionInterval(0, 0);
      }
    });
    btnRefresh.setBounds(10, 169, 89, 23);
    panel_30.add(btnRefresh);

    btnActivate = new JButton("Activate");
    btnActivate.setMargin(new Insets(2, 0, 2, 0));
    btnActivate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String cid = null;
        int num_rows = table_5.getRowCount();
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "Telit_Socket_Activate_button" });
        if (num_rows < 1)
        {
          msg = "No sockets avaialble.Press 'Refresh' button get socket profiles.\r\n\r\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "telit_socket_no_profiles" });
        }
        else
        {
          int selectedRowIndex = table_5.getSelectedRow();

          cid = (String)table_5.getModel().getValueAt(selectedRowIndex, 1);
        }

        String ret = mySerial1.ser_write("AT#SGACT=" + cid + ",1" + "\r\n", 1);
        try {
          Thread.sleep(2000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnActivate.setBounds(109, 169, 70, 23);
    panel_30.add(btnActivate);

    btnDeactivate = new JButton("Deactivate");
    btnDeactivate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String cid = null;
        int num_rows = table_5.getRowCount();
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "Telit_Socket_Deactivate_button" });
        if (num_rows < 1)
        {
          msg = "No sockets avaialble.Press 'Refresh' button get socket profiles.\r\n\r\n";
          writeOutput(msg);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "telit_socket_no_profiles" });
        }
        else
        {
          int selectedRowIndex = table_5.getSelectedRow();

          cid = (String)table_5.getModel().getValueAt(selectedRowIndex, 1);
        }

        String ret = mySerial1.ser_write("AT#SGACT=" + cid + ",0" + "\r\n", 1);
        try {
          Thread.sleep(2000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    });
    btnDeactivate.setMargin(new Insets(0, 0, 2, 0));
    btnDeactivate.setBounds(189, 170, 89, 23);
    panel_30.add(btnDeactivate);

    lblConnid = new JLabel("cid");
    lblConnid.setBounds(10, 225, 46, 14);
    panel_30.add(lblConnid);

    lblProtocol = new JLabel("Protocol");
    lblProtocol.setBounds(10, 250, 70, 14);
    panel_30.add(lblProtocol);

    lblPort_2 = new JLabel("Port");
    lblPort_2.setBounds(10, 300, 46, 14);
    panel_30.add(lblPort_2);

    lblIpAddress = new JLabel("IP Address");
    lblIpAddress.setBounds(10, 275, 80, 14);
    panel_30.add(lblIpAddress);

    comboBox_11 = new JComboBox(socketIds);
    comboBox_11.setBounds(90, 222, 46, 20);
    panel_30.add(comboBox_11);

    comboBox_12 = new JComboBox(protocol);
    comboBox_12.setBounds(90, 247, 70, 20);
    panel_30.add(comboBox_12);

    txtWwwgooglecom = new JTextField();
    txtWwwgooglecom.setText("www.google.com");
    txtWwwgooglecom.setBounds(90, 272, 114, 20);
    panel_30.add(txtWwwgooglecom);
    txtWwwgooglecom.setColumns(10);

    textField_23 = new JTextField();
    textField_23.setText("80");
    textField_23.setBounds(90, 297, 54, 20);
    panel_30.add(textField_23);
    textField_23.setColumns(10);

    btnOpen_1 = new JButton("Open");
    btnOpen_1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }

            String server_ip = txtWwwgooglecom.getText();
            if (server_ip.equals(""))
            {
              msg = "Server IP is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_port = textField_23.getText();
            if (server_port.equals(""))
            {
              msg = "Server Port is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              int selected_connId = Integer.parseInt((String)comboBox_11.getSelectedItem());
              String selected_protocol = (String)comboBox_12.getSelectedItem();
              String socket_dial_mode = (String)comboBox_13.getSelectedItem();

              ret = mySerial1.ser_write("AT#SCFG?\r\n", 1);
              try {
                Thread.sleep(2000L);
              } catch (InterruptedException localInterruptedException1) {
              }
              if (scfg_status.equals("FALSE"))
              {
                msg = "This is only supported for Telit modules\n\n";
                writeOutput(msg);
                return Integer.valueOf(1);
              }

              String selected_cid = pdpId[(selected_connId - 1)];

              msg = "Checking if socket connection is activated...\n\n";
              writeOutput(msg);

              ret = mySerial1.ser_write("AT#SGACT?\r\n", 1);
              try {
                Thread.sleep(2000L);
              }
              catch (InterruptedException localInterruptedException2)
              {
              }

              for (int k = 0; k < 6; k++)
              {
                if (selected_cid.equals(socketPdpId[k]))
                {
                  if (socketPdpStatus[k].equals("Active"))
                  {
                    msg = ("cid " + selected_cid + " is already active" + "\n\n");
                    writeOutput(msg);
                  }
                  else
                  {
                    msg = ("cid " + selected_cid + " is not active Activating now.." + "\n\n");
                    writeOutput(msg);

                    ret = mySerial1.ser_write("AT#SGACT=" + selected_cid + ",1" + "\r\n", 1);
                    try {
                      Thread.sleep(2000L);
                    } catch (InterruptedException localInterruptedException3) {
                    }
                    if (socket_pdp_activation_status.equals("FAIL"))
                    {
                      writeOutput("Error activating PDP context " + selected_cid);
                      return Integer.valueOf(1);
                    }

                  }

                  msg = ("Connecting to" + server_ip + "\n\n");
                  writeOutput(msg);
                  int protocol_id = 0;
                  int socket_mode = 0;

                  if (selected_protocol.equals("TCP"))
                    protocol_id = 0;
                  else {
                    protocol_id = 1;
                  }
                  if (socket_dial_mode.equals("Online"))
                    socket_mode = 0;
                  else {
                    socket_mode = 1;
                  }
                  server_ip = "\"" + server_ip + "\"";

                  ret = mySerial1.ser_write("AT#SD=" + selected_connId + "," + 
                    protocol_id + "," + 
                    server_port + "," + 
                    server_ip + ",0,0," + socket_mode + "\r\n", 1);
                  try {
                    Thread.sleep(2000L);
                  }
                  catch (InterruptedException localInterruptedException4)
                  {
                  }
                }
              }

            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnOpen_1.setBounds(10, 357, 70, 23);
    panel_30.add(btnOpen_1);

    btnNewButton_1 = new JButton("Close");
    btnNewButton_1.setBounds(90, 357, 89, 23);
    panel_30.add(btnNewButton_1);

    lblMode = new JLabel("Mode");
    lblMode.setBounds(10, 325, 46, 14);
    panel_30.add(lblMode);

    comboBox_13 = new JComboBox(socket_dial_mode);
    comboBox_13.setBounds(90, 322, 89, 20);
    panel_30.add(comboBox_13);

    panel_57 = new JPanel();
    panel_57.setLayout(null);
    panel_34.add(panel_57, "Huawei");

    panel_58 = new JPanel();
    panel_58.setLayout(null);
    panel_58.setBorder(new TitledBorder(null, "Connection", 4, 2, null, null));
    panel_58.setBounds(10, 0, 407, 147);
    panel_57.add(panel_58);

    lblServer = new JLabel("Server");
    lblServer.setBounds(10, 57, 69, 14);
    panel_58.add(lblServer);

    txtWwwmmsupportnet_1 = new JTextField();
    txtWwwmmsupportnet_1.setText("www.m2msupport.net");
    txtWwwmmsupportnet_1.setColumns(10);
    txtWwwmmsupportnet_1.setBounds(89, 54, 153, 20);
    panel_58.add(txtWwwmmsupportnet_1);

    label_21 = new JLabel("Port");
    label_21.setBounds(10, 85, 46, 14);
    panel_58.add(label_21);

    textField_47 = new JTextField();
    textField_47.setText("80");
    textField_47.setColumns(10);
    textField_47.setBounds(89, 82, 46, 20);
    panel_58.add(textField_47);

    button_107 = new JButton("Connect");
    button_107.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret1 = is_device_connected();
            if (ret1.equals("NULL")) {
              return Integer.valueOf(1);
            }
            String apn = textField_48.getText();
            if (apn.equals(""))
            {
              msg = "APN is not set. You can get the APN from the 'Data Call' tab\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_ip = txtWwwmmsupportnet_1.getText();
            if (server_ip.equals(""))
            {
              msg = "Server IP is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            String server_port = textField_47.getText();
            if (server_port.equals(""))
            {
              msg = "Server Port is not set...\n\n";
              writeOutput(msg);
              return Integer.valueOf(1);
            }

            msg = "Checking registration status...\n\n";
            writeOutput(msg);
            reg_status = -1;
            String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              msg = "Checking if device is already connected...\n\n";
              writeOutput(msg);
              num_profiles_connected = 0;
              connected_profile = null;
              ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException1) {
              }
              if (num_profiles_connected > 0)
              {
                msg = ("Disconnected profile " + connected_profile + "\n");
                writeOutput(msg);
                cgact_action = "deactivate";
                String t5 = "AT+CGACT=0," + connected_profile + "\r\n";
                ret = mySerial1.ser_write(t5, 1);
                try {
                  Thread.sleep(5000L);
                }
                catch (InterruptedException localInterruptedException2) {
                }
              }
            }
            if ((reg_status == 1) || (reg_status == 5))
            {
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "call_registraiton_ok" });
              ret = mySerial1.ser_write("AT+CMEE=1\r\n", 1);
              try {
                Thread.sleep(1000L); } catch (InterruptedException localInterruptedException3) {
              }
              msg = "Attaching to network...\n";
              writeOutput(msg);
              ret = mySerial1.ser_write("AT+CGATT=1\r\n", 1);
              try {
                Thread.sleep(4000L); } catch (InterruptedException localInterruptedException4) {
              }
              if (attach_status == 1)
              {
                if (winflag == 1)
                  win.call("setMessage", new String[] { "Tester", "call_attach_success" });
                connect_status = 0;
                msg = "Initializing the TCP connection...\r\n";
                writeOutput(msg);
                String t2 = "AT^IPINIT=\"" + apn + "\"" + "\r\n";
                ret = mySerial1.ser_write(t2, 1);
                try
                {
                  Thread.sleep(8000L);
                }
                catch (InterruptedException localInterruptedException5)
                {
                }

                huawei_ip_init = 0;
                msg = "\nChecking initialization status...\n\n";
                writeOutput(msg);
                t2 = "AT^IPINIT?\r\n";
                ret = mySerial1.ser_write(t2, 1);
                try {
                  Thread.sleep(2000L);
                }
                catch (InterruptedException localInterruptedException6) {
                }
                if (huawei_ip_init == 0)
                {
                  if (winflag == 1)
                    win.call("setMessage", new String[] { "Tester", "huawei_ipinit_fail" });
                  return Integer.valueOf(1);
                }

                msg = "Opening the TCP connection...\n\n";
                writeOutput(msg);

                t2 = "AT^IPOPEN=1,\"TCP\",\"" + server_ip + "\"," + server_port + "\r\n";
                ret = mySerial1.ser_write(t2, 1);
                try
                {
                  Thread.sleep(3000L);
                } catch (InterruptedException localInterruptedException7) {
                }
                huawei_ip_open = 0;

                t2 = "AT^IPOPEN?\r\n";
                ret = mySerial1.ser_write(t2, 1);
                try
                {
                  Thread.sleep(3000L);
                }
                catch (InterruptedException localInterruptedException8) {
                }
                if (huawei_ip_open == 0)
                {
                  if (winflag == 1)
                    win.call("setMessage", new String[] { "Tester", "huawei_ipopen_unsucessful" });
                  return Integer.valueOf(1);
                }

                if (winflag == 1) {
                  win.call("setMessage", new String[] { "Tester", "huawei_ipopen_sucessful" });
                }

              }
              else
              {
                if (winflag == 1)
                  win.call("setMessage", new String[] { "Tester", "call_connect_unsucessful" });
                msg = "Unable to attach to the network...\n\n";
                writeOutput(msg);
              }

            }
            else
            {
              msg = "Cannot connect without device being registered\n\n";
              writeOutput(msg);
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "call_registration_check_fail" });
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    button_107.setMargin(new Insets(2, 0, 2, 0));
    button_107.setBounds(10, 113, 89, 23);
    panel_58.add(button_107);

    button_108 = new JButton("Dis-Connect");
    button_108.setMargin(new Insets(2, 0, 2, 0));
    button_108.setBounds(108, 113, 89, 23);
    panel_58.add(button_108);

    label_22 = new JLabel("APN");
    label_22.setBounds(10, 27, 46, 14);
    panel_58.add(label_22);

    textField_48 = new JTextField();
    textField_48.setColumns(10);
    textField_48.setBounds(89, 23, 108, 20);
    panel_58.add(textField_48);

    btnGetApn = new JButton("Get APN");
    btnGetApn.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        default_apn = "";

        String ret = mySerial1.ser_write("AT+CGDCONT?\r\n", 1);
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", ret });
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if (default_apn.equals("")) {
          writeOutput("No APNs available. Please setup connection profile in 'Data' tab\n");
          return;
        }

        writeOutput("Using APN from Connection Profile 1\n");
        textField_48.setText(default_apn);
      }
    });
    btnGetApn.setBounds(225, 23, 89, 23);
    panel_58.add(btnGetApn);

    label_23 = new JLabel("Client Data");
    label_23.setBounds(10, 158, 81, 14);
    panel_57.add(label_23);

    button_118 = new JButton("Send");
    button_118.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        huawei_ip_open = 0;

        String t2 = "AT^IPOPEN?\r\n";
        String ret = mySerial1.ser_write(t2, 1);
        try
        {
          Thread.sleep(3000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
        if (huawei_ip_open == 1)
        {
          msg = "Sending TCP data\n\n";
          writeOutput(msg);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "tcp_data_send" });
          }

          String message = txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp_1.getText();
          message = "\"" + message + "\"";
          String t3 = "AT^IPSEND=1," + message + "\r\n";
          ret = mySerial1.ser_write(t3, 1);
          try {
            Thread.sleep(3000L);
          }
          catch (InterruptedException localInterruptedException1)
          {
          }
        }
        else {
          writeOutput("No IP connection exists. Cannot send client data. Set up IP connection above and retry.\n");
          return;
        }
      }
    });
    button_118.setBounds(328, 299, 89, 23);
    panel_57.add(button_118);

    scrollPane_5 = new JScrollPane();
    scrollPane_5.setBounds(10, 177, 407, 111);
    panel_57.add(scrollPane_5);

    txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp_1 = new JTextArea();
    txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp_1.setText("\nGET /m2msupport/http_get_test.php HTTP/1.1\nHost:www.m2msupport.net\nConnection:keep-alive\n\n");
    scrollPane_5.setViewportView(txtrGetHttpwwwmmsupportnetmmsupporttcpudptestphpHttp_1);

    scrollPane_7 = new JScrollPane();
    scrollPane_7.setBounds(10, 330, 407, 137);
    panel_57.add(scrollPane_7);

    textArea = new JTextArea();
    scrollPane_7.setViewportView(textArea);
    textArea.setEditable(false);

    lblServerData = new JLabel("Server Data");
    lblServerData.setBounds(10, 308, 69, 14);
    panel_57.add(lblServerData);

    btnClear_7 = new JButton("Clear");
    btnClear_7.setBounds(328, 476, 89, 23);
    panel_57.add(btnClear_7);

    module_select_label = new URLLabel("TCP/UDP testing for Simcom modules", "http://m2msupport.net/m2msupport/tutorial-for-simcom-m2m-modules/");
    module_select_label.setBounds(110, 9, 345, 14);
    panel_33.add(module_select_label);

    panel_44 = new JPanel();
    tabbedPane.addTab("Email", null, panel_44, null);
    panel_44.setLayout(null);

    comboBox_17 = new JComboBox(module_int_telit);
    comboBox_17.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout)panel_45.getLayout();

        email_mod_sel = ((String)e.getItem());

        String txt = "Email testing for " + email_mod_sel + " modules";
        cl.show(panel_45, (String)e.getItem());
        lblEmailTestingFor.setText(txt);
      }
    });
    comboBox_17.setBounds(10, 11, 74, 20);
    panel_44.add(comboBox_17);

    lblEmailTestingFor = new JLabel("Email testing for Telit modules");
    lblEmailTestingFor.setBounds(97, 11, 225, 24);
    panel_44.add(lblEmailTestingFor);

    panel_45 = new JPanel();
    panel_45.setBounds(10, 42, 417, 511);
    panel_44.add(panel_45);
    panel_45.setLayout(new CardLayout(0, 0));

    panel_46 = new JPanel();
    panel_45.add(panel_46, "name_170168941369850");
    panel_46.setLayout(null);

    lblNewLabel_1 = new JLabel("SMTP Server");
    lblNewLabel_1.setBounds(10, 11, 83, 14);
    panel_46.add(lblNewLabel_1);

    textField_28 = new JTextField();
    textField_28.setBounds(103, 8, 161, 20);
    panel_46.add(textField_28);
    textField_28.setColumns(10);

    lblUserName_1 = new JLabel("User Name");
    lblUserName_1.setBounds(10, 138, 73, 14);
    panel_46.add(lblUserName_1);

    textField_32 = new JTextField();
    textField_32.setBounds(103, 135, 161, 20);
    panel_46.add(textField_32);
    textField_32.setColumns(10);

    lblPassword_1 = new JLabel("Password");
    lblPassword_1.setBounds(10, 169, 73, 14);
    panel_46.add(lblPassword_1);

    textField_37 = new JTextField();
    textField_37.setBounds(103, 166, 161, 20);
    panel_46.add(textField_37);
    textField_37.setColumns(10);

    lblSenderEmail = new JLabel("Sender Email");
    lblSenderEmail.setBounds(10, 42, 83, 27);
    panel_46.add(lblSenderEmail);

    textField_38 = new JTextField();
    textField_38.setBounds(103, 45, 161, 20);
    panel_46.add(textField_38);
    textField_38.setColumns(10);

    lblMesssage = new JLabel("Messsage");
    lblMesssage.setBounds(10, 236, 73, 14);
    panel_46.add(lblMesssage);

    scrollPane_22 = new JScrollPane();
    scrollPane_22.setBounds(10, 261, 397, 81);
    panel_46.add(scrollPane_22);

    textArea_16 = new JTextArea();
    scrollPane_22.setViewportView(textArea_16);

    btnSend_1 = new JButton("Send");
    btnSend_1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        String ret1 = is_device_connected();
        if (ret1.equals("NULL")) {
          return;
        }
        String smtp_server = textField_28.getText();
        if (smtp_server.equals(""))
        {
          msg = "SMTP address cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String uname = textField_32.getText();
        if (uname.equals(""))
        {
          msg = "User name cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String passwd = textField_37.getText();
        if (passwd.equals(""))
        {
          msg = "Password cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String sender_email = textField_38.getText();
        if (sender_email.equals(""))
        {
          msg = "Sender email cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String email_message = textArea_16.getText();
        if (email_message.equals(""))
        {
          msg = "Email message cannot be empty\n\n";
          writeOutput(msg);
          return;
        }
        String email_to = textField_39.getText();
        if (email_to.equals(""))
        {
          msg = "Receiver email cannot be empty\n\n";
          writeOutput(msg);
          return;
        }

        String email_subject = textField_40.getText();
        if (email_subject.equals(""))
        {
          msg = "Email subject cannot be empty\n\n";
          writeOutput(msg);
          return;
        }

        String cid = (String)comboBox_15.getSelectedItem();

        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "telit_email_send" });
        }
        msg = "Checking registration status...\n\n";
        writeOutput(msg);
        reg_status = -1;

        String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException) {
        }
        if ((reg_status == 1) || (reg_status == 5))
        {
          int selected_connId = Integer.parseInt((String)comboBox_18.getSelectedItem());

          ret = mySerial1.ser_write("AT#SCFG?\r\n", 1);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException1) {
          }
          if (scfg_status.equals("FALSE"))
          {
            msg = "This is only supported for Telit modules\n\n";
            writeOutput(msg);
            return;
          }

          String selected_cid = pdpId[(selected_connId - 1)];

          msg = "Checking if socket connection is activated...\n\n";
          writeOutput(msg);

          ret = mySerial1.ser_write("AT#SGACT?\r\n", 1);
          try {
            Thread.sleep(2000L);
          }
          catch (InterruptedException localInterruptedException2)
          {
          }

          for (int k = 0; k < 6; k++)
          {
            if (selected_cid.equals(socketPdpId[k]))
            {
              if (socketPdpStatus[k].equals("Active"))
              {
                msg = ("cid " + selected_cid + " is already active" + "\n\n");
                writeOutput(msg);
              }
              else
              {
                msg = ("cid " + selected_cid + " is not active Activating now.." + "\n\n");
                writeOutput(msg);

                ret = mySerial1.ser_write("AT#SGACT=" + selected_cid + ",1" + "\r\n", 1);
                try {
                  Thread.sleep(2000L);
                } catch (InterruptedException localInterruptedException3) {
                }
                if (socket_pdp_activation_status.equals("FAIL"))
                {
                  writeOutput("Error activating PDP context " + selected_cid);
                  return;
                }

              }

              msg = "Set up email parameters..\n\n";
              writeOutput(msg);

              telit_email_status = "FAIL";
              String cmd = "AT#GPRS=0\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(2000L);
              } catch (InterruptedException localInterruptedException4) {
              }
              if (!telit_email_status.equals("SUCCESS"))
              {
                writeOutput("Error setting email parameter\r\n");
                return;
              }

              telit_email_status = "FAIL";
              cmd = "AT#ESMTP=\"" + smtp_server + "\"" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException5) {
              }
              if (!telit_email_status.equals("SUCCESS"))
              {
                writeOutput("Error setting email parameter\r\n");
                return;
              }

              String and_symbol = Character.toString('@');
              uname = uname.replace('@', '@');
              telit_email_status = "FAIL";
              cmd = "AT#EUSER=\"" + uname + "\"" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException6) {
              }
              if (!telit_email_status.equals("SUCCESS"))
              {
                writeOutput("Error setting email parameter\r\n");
                return;
              }

              telit_email_status = "FAIL";
              cmd = "AT#EPASSW=\"" + passwd + "\"" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException7) {
              }
              if (!telit_email_status.equals("SUCCESS"))
              {
                writeOutput("Error setting email parameter\r\n");
                return;
              }

              telit_email_status = "FAIL";
              cmd = "AT#EADDR=\"" + sender_email + "\"" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException8) {
              }
              if (!telit_email_status.equals("SUCCESS"))
              {
                writeOutput("Error setting email parameter\r\n");
                return;
              }

              telit_email_status = "FAIL";
              cmd = "AT#ESAV\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(1000L);
              } catch (InterruptedException localInterruptedException9) {
              }
              if (!telit_email_status.equals("SUCCESS"))
              {
                writeOutput("Error setting email parameter\r\n");
                return;
              }

              telit_email_status = "FAIL";
              cmd = "AT#SEMAIL=\"" + email_to + "\"," + "\"" + email_subject + "\",0" + "\r\n";
              ret = mySerial1.ser_write(cmd, 1);
              try {
                Thread.sleep(3000L);
              } catch (InterruptedException localInterruptedException10) {
              }
              String ctz = Character.toString('\032');
              ret = mySerial1.ser_write(email_message + ctz + "\r\n", 1);
              try {
                Thread.sleep(1000L);
              }
              catch (InterruptedException localInterruptedException11)
              {
              }
              if (winflag != 1) break;
              win.call("setMessage", new String[] { "Tester", "telit_email_success" });

              break;
            }
          }
        }
      }
    });
    btnSend_1.setBounds(10, 353, 89, 23);
    panel_46.add(btnSend_1);

    lblCid_1 = new JLabel("CID");
    lblCid_1.setBounds(10, 201, 46, 14);
    panel_46.add(lblCid_1);

    comboBox_18 = new JComboBox(socketIds);
    comboBox_18.setBounds(103, 197, 35, 20);
    panel_46.add(comboBox_18);

    lblReceiverEmail = new JLabel("Receiver Email");
    lblReceiverEmail.setBounds(10, 79, 102, 14);
    panel_46.add(lblReceiverEmail);

    textField_39 = new JTextField();
    textField_39.setBounds(103, 76, 161, 20);
    panel_46.add(textField_39);
    textField_39.setColumns(10);

    lblEmailSubject = new JLabel("Email Subject");
    lblEmailSubject.setBounds(10, 110, 89, 14);
    panel_46.add(lblEmailSubject);

    textField_40 = new JTextField();
    textField_40.setBounds(103, 107, 161, 20);
    panel_46.add(textField_40);
    textField_40.setColumns(10);

    String[] comboBoxItems = { "Simcom", "Telit" };

    txtAtCommandTester = new JTextField();
    txtAtCommandTester.setBounds(6, 16, 792, 27);
    panel_4.add(txtAtCommandTester);
    txtAtCommandTester.setEditable(false);
    txtAtCommandTester.setHorizontalAlignment(0);
    txtAtCommandTester.setFont(new Font("Tahoma", 1, 12));
    txtAtCommandTester.setForeground(new Color(255, 255, 255));
    txtAtCommandTester.setBackground(new Color(51, 102, 153));
    txtAtCommandTester.setText("AT Command Tester V" + version);
    txtAtCommandTester.setColumns(10);

    panel_23 = new JPanel();
    panel_23.setBorder(new TitledBorder(null, "Connection", 4, 2, null, null));
    panel_23.setBounds(521, 47, 251, 101);
    panel_4.add(panel_23);
    panel_23.setLayout(null);

    lblDeviceName = new JLabel("Device Model");
    lblDeviceName.setBounds(10, 26, 75, 14);
    panel_23.add(lblDeviceName);

    lblManufacturer = new JLabel("Manufacturer");
    lblManufacturer.setBounds(10, 51, 93, 14);
    panel_23.add(lblManufacturer);

    lblStatus = new JLabel("Status");
    lblStatus.setBounds(10, 76, 46, 14);
    panel_23.add(lblStatus);

    lblNotAvailable = new JLabel("Not Available");
    lblNotAvailable.setForeground(Color.BLUE);
    lblNotAvailable.setBounds(113, 26, 128, 14);
    panel_23.add(lblNotAvailable);

    lblNotAvailable_1 = new JLabel("Not Available");
    lblNotAvailable_1.setForeground(Color.BLUE);
    lblNotAvailable_1.setBounds(113, 51, 128, 14);
    panel_23.add(lblNotAvailable_1);

    lblNotConnected = new JLabel("Not Connected");
    lblNotConnected.setForeground(Color.BLUE);
    lblNotConnected.setBounds(113, 76, 128, 14);
    panel_23.add(lblNotConnected);

    lblHowToUse = new URLLabel("How to use with Arduino shields?", "http://m2msupport.net/m2msupport/using-at-command-tester-with-arduino-boards/");
    lblHowToUse.setText("How to use with Arduino shields?");
    lblHowToUse.setBounds(16, 153, 212, 14);
    panel_4.add(lblHowToUse);

    btnPostAQuestion = new JButton("Need Support?");
    btnPostAQuestion.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "post_message_button_press" });
        URL myURL = null;
        String my_url = "http://m2msupport.net/m2msupport/forums/forum/at-command-tester/";
        try {
          myURL = new URL(my_url);
        }
        catch (MalformedURLException e1) {
          e1.printStackTrace();
        }
        if (Desktop.isDesktopSupported()) {
          Desktop desktop = Desktop.getDesktop();
          if (desktop.isSupported(Action.BROWSE))
            try {
              desktop.browse(myURL.toURI());
              return;
            }
            catch (Exception localException)
            {
            }
        }
      }
    });
    btnPostAQuestion.setMargin(new Insets(2, 0, 2, 0));
    btnPostAQuestion.setBounds(403, 149, 115, 23);
    panel_4.add(btnPostAQuestion);

    scrollPane_24 = new JScrollPane();
    scrollPane_24.setBounds(458, 179, 319, 589);
    panel_4.add(scrollPane_24);

    textPane_10 = new JTextPane();

    textPane_10.setBackground(new Color(255, 255, 204));
    scrollPane_24.setViewportView(textPane_10);
    LineBorder lineBorder = new LineBorder(Color.blue, 3, true);
    scrollPane_24.setBorder(new LineBorder(Color.GRAY, 3, true));

    mySerial1 = new TwoWaySerialComm2(textPane_10, win);

    btnClear = new JButton("Clear Log");
    btnClear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textPane_10.setText("");
      }
    });
    btnClear.setMargin(new Insets(2, 0, 2, 0));
    btnClear.setBounds(614, 149, 74, 23);
    panel_4.add(btnClear);

    btnSaveLog_7 = new JButton("Save Log");
    btnSaveLog_7.addActionListener(fileSaver);
    btnSaveLog_7.setMargin(new Insets(2, 0, 2, 0));
    btnSaveLog_7.setBounds(698, 149, 74, 23);
    panel_4.add(btnSaveLog_7);

    tabbedPane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JTabbedPane sourceTabbedPane = (JTabbedPane)e.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        if (index == 0)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "tab_dignostics_selected" });
          }

        }
        else if (index == 1)
        {
          TwoWaySerialComm2.send_to_parser = 0;
        }
        else if (index == 2)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 0;
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "tab_script_selected" });
          }
        }
        else if (index == 4)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_datacall_selected" });
        }
        else if (index == 3)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_voicecall_selected" });
        }
        else if (index == 5)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_sms_selected" });
        }
        else if (index == 6)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_network_sel_selected" });
        }
        else if (index == 7)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_phonebook_selected" });
        }
        else if (index == 8)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_http_selected" });
        }
        else if (index == 9)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_ftp_selected" });
        }
        else if (index == 10)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_gps_selected" });
        }
        else if (index == 11)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_tcp_udp_selected" });
        }
        else if (index == 12)
        {
          mySerial1.setOutputArea(textPane_10);
          TwoWaySerialComm2.send_to_parser = 1;
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "tab_email_selected" });
        }
      }
    });
    btnDisconnect.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        String ret = mySerial1.disconnect();
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", ret });
        lblNotAvailable.setText("Not Available");
        lblNotAvailable_1.setText("Not Available");
        lblNotConnected.setText("Not Connected");
      }
    });
    btnConnect.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ret = mySerial1.disconnect();
            if (winflag == 1)
              win.call("setMessage", new String[] { "Tester", ret });
            lblNotAvailable.setText("Not Available");
            lblNotAvailable_1.setText("Not Available");
            lblNotConnected.setText("Not Connected");

            String port1 = (String)comboBox.getSelectedItem();
            String speed = (String)comboBox_1.getSelectedItem();
            int sp1 = Integer.parseInt(speed);
            if (port1 == null)
            {
              String msg = "Port not selected. Press 'Find Ports'  or 'AutoConnect'.";

              writeOutput(msg);
              insertURL("AT Command Tester Troubleshooting Guide", "http://m2msupport.net/m2msupport/forums/topic/at-command-tester-troubleshooting-guide/");
              writeOutput("\r\n\r\n");
              if (winflag == 1) {
                win.call("setMessage", new String[] { "Tester", "connect_port_not_selected" });
              }
            }
            else
            {
              try
              {
                ret = mySerial1.connect(port1, sp1);
                try {
                  Thread.sleep(3000L);
                } catch (InterruptedException localInterruptedException) {
                }
                if (winflag == 1)
                  win.call("setMessage", new String[] { "Tester", ret });
                if (ret.equals("connect_sucess"))
                {
                  global_model_no = "";
                  global_at_response = "";
                  int temp = TwoWaySerialComm2.send_to_parser;
                  TwoWaySerialComm2.send_to_parser = 1;

                  String msg = "\nSending AT query..\r\n";
                  writeOutput(msg);

                  String cmd = "AT\r\n";
                  ret = mySerial1.ser_write(cmd, 1);
                  try {
                    Thread.sleep(1000L);
                  } catch (InterruptedException localInterruptedException1) {
                  }
                  if (global_at_response.equals("OK"))
                  {
                    lblNotConnected.setText("Connected");
                    if (winflag == 1)
                      win.call("setMessage", new String[] { "Tester", "successful_at_connection" });
                    cmd = "AT+CGMM\r\n";

                    ret = mySerial1.ser_write(cmd, 1);
                    try {
                      Thread.sleep(1000L); } catch (InterruptedException localInterruptedException2) {
                    }
                    if (!global_model_no.equals(""))
                      lblNotAvailable.setText(global_model_no);
                    if (winflag == 1)
                      win.call("setMessage", new String[] { "Model", global_model_no });
                    global_manufacturer_name = "";
                    cmd = "AT+CGMI\r\n";
                    ret = mySerial1.ser_write(cmd, 1);
                    try {
                      Thread.sleep(1000L); } catch (InterruptedException localInterruptedException3) {
                    }
                    if (!global_manufacturer_name.equals(""))
                      lblNotAvailable_1.setText(global_manufacturer_name);
                  }
                  else
                  {
                    if (winflag == 1) {
                      win.call("setMessage", new String[] { "Tester", "unsuccessful_at_connection" });
                    }
                    msg = "\nInvalid or no response from the device. Please check the modem port and the baud rate.";
                    insertURL("AT Command Tester Troubleshooting Guide", "http://m2msupport.net/m2msupport/forums/topic/at-command-tester-troubleshooting-guide/");
                    writeOutput("\r\n\r\n");
                    writeOutput(msg);

                    ret = mySerial1.disconnect();
                    if (winflag == 1) {
                      win.call("setMessage", new String[] { "Tester", ret });
                    }
                  }

                  TwoWaySerialComm2.send_to_parser = temp;
                }
                if (ret.equals("connect_port_in_use"))
                {
                  lblNotAvailable.setText("Port in Use");
                }

              }
              catch (Exception et)
              {
                et.printStackTrace();
              }
            }

            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    btnFindPorts.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new SwingWorker()
        {
          protected Integer doInBackground()
            throws Exception
          {
            String ports = mySerial1.listPorts();

            comboBox.removeAllItems();
            if (ports == "")
            {
              String msg = "No serial ports found on the system.";
              writeOutput(msg);
              insertURL("AT Command Tester Troubleshooting Guide", "http://m2msupport.net/m2msupport/forums/topic/at-command-tester-troubleshooting-guide/");
              writeOutput("\r\n\r\n");
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "ports_not_found" });
            }
            else
            {
              String[] temp = ports.split("#");
              String msg = "Found ports :";
              writeOutput(msg);
              if (winflag == 1)
                win.call("setMessage", new String[] { "Tester", "ports_found" });
              for (int i = 0; i < temp.length; i++)
              {
                if (temp[i] != "")
                {
                  System.out.println(temp[i]);
                  msg = temp[i];
                  writeOutput(msg);
                  if (i != temp.length - 1)
                    writeOutput(", ");
                  comboBox.addItem(temp[i]);
                }
              }
              writeOutput("\r\n\r\n");
            }
            return Integer.valueOf(1);
          }
        }
        .execute();
      }
    });
    TwoWaySerialComm2.atTester = this;
  }

  public String is_device_connected()
  {
    String ret = "NULL";

    if (comboBox.getItemCount() == 0)
    {
      String str = "No ports found. Press 'Find Ports' button to find avaialble ports to connect.\n\n";
      writeOutput(str);
      if (winflag == 1)
        win.call("setMessage", new String[] { "Tester", "generic_ports_not_avaialble" });
      return ret;
    }

    ret = mySerial1.check_port_connected();
    if (ret == "NULL")
    {
      msg = "Port not connected.\r\n\r\n";
      writeOutput(msg);
      if (winflag == 1)
        win.call("setMessage", new String[] { "Tester", "generic_port_not_connected" });
      return ret;
    }
    return ret;
  }

  public void at_parser(String cmd, ArrayList response)
  {
    String item = ""; String str = "";
    String[] t1 = null;
    String[] t2 = null;
    String[] tmp = new String[2];

    if (cmd.equals("AT+CGMI"))
    {
      String resp2 = (String)response.get(0);

      str = "Manufacturer : " + resp2 + "\n";
      global_manufacturer_name = resp2;

      writeOutput(str);
    }

    if (cmd.equals("AT+CIMI"))
    {
      String resp = (String)response.get(1);
      String resp2 = (String)response.get(0);
      if (resp.equals("OK"))
      {
        str = "Phone Number : " + resp2 + "\n";
        phone_number = resp2;
      }
      else
      {
        str = "Phone number information not avaialble";
      }

      writeOutput(str);
    }
    else if (cmd.equals("AT+CGMM"))
    {
      String resp = (String)response.get(1);
      String resp2 = (String)response.get(0);
      if (resp.equals("OK"))
      {
        str = "Model Number : " + resp2 + "\n";
        global_model_no = resp2;
      }
      else
      {
        str = "Model Number information not avaialble";
      }

      writeOutput(str);
    }
    else if (cmd.contains("RING"))
    {
      if (ring_acknowledged.equals("FALSE"))
      {
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "incoming_call" });
        int n = JOptionPane.showConfirmDialog(
          panel_4, 
          "Do you want to answer the incoming call?", 
          "Confirm incoming call", 
          0);
        if (n == 0)
        {
          String ret = mySerial1.ser_write("ATA\r\n", 1);

          ring_acknowledged = "TRUE";
          writeOutput("Voice call answered.\n\n");
        }

      }

    }
    else if (cmd.equals("ATA"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        str = "Call Connected\n\n";
        ring_acknowledged = "FALSE";
        voice_call_connected = "TRUE";
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "incoming_call_answered" });
      }
      else
      {
        str = "Call Failure\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.equals("NO CARRIER"))
    {
      voice_call_connected = "FALSE";
      if (winflag == 1)
        win.call("setMessage", new String[] { "Tester", "no_carrier" });
      str = "NO Carrier: Call dis-connected\n\n";
      writeOutput(str);
    }
    else if (cmd.contains("ATD"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        str = "Voice call successfull\n\n";
        voice_call_connected = "TRUE";
      }
      else
      {
        str = "Unable to connect to the dialed number\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.equals("AT"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        str = "Successful response for AT query..\n\n";
        global_at_response = "OK";
      }
      else
      {
        str = "No valid response for AT query.\n\n";
        global_at_response = "FAIL";
      }

      writeOutput(str);
    }
    else if (cmd.equals("ATH"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        str = "Call sucessfully dis-connected..\n\n";
        voice_call_connected = "FALSE";
      }
      else
      {
        str = "Unable to disconnect the call\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.contains("AT#FTPTYPE"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        telit_ftp_status = "SUCCESS";
      }
      else
      {
        telit_ftp_status = "FAIL";
      }
    }
    else if ((cmd.contains("AT#ESMTP")) || 
      (cmd.contains("AT#EUSER")) || 
      (cmd.contains("AT#EPASSW")) || 
      (cmd.contains("AT#EADDR")) || 
      (cmd.contains("AT#ESAV")) || 
      (cmd.contains("AT#GPRS")))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        telit_email_status = "SUCCESS";
      }
      else
      {
        telit_email_status = "FAIL";
      }
    }
    else if (cmd.contains("AT#FTPTO"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        telit_ftp_status = "SUCCESS";
      }
      else
      {
        telit_ftp_status = "FAIL";
      }
    }
    else if (cmd.contains("AT#FTPOPEN"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        telit_ftp_status = "SUCCESS";
      }
      else
      {
        telit_ftp_status = "FAIL";
      }
    }
    else if (cmd.contains("AT#FTPCWD"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        telit_ftp_status = "SUCCESS";
      }
      else
      {
        telit_ftp_status = "FAIL";
      }
    }
    else if (cmd.contains("AT#FTPGET"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.contains("ERROR"))
      {
        telit_ftp_status = "FAIL";
      }
      else
      {
        telit_ftp_status = "SUCCESS";
      }
    }
    else if (cmd.contains("AT+CGPSPWR"))
    {
      if (cmd.equals("AT+CGPSPWR?"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.contains("ERROR")) {
          writeOutput("Please check if the module supports GPS feature.\n");
        }
        else {
          tmp = resp2.split(":");
          tmp[1] = tmp[1].trim();
          if (tmp[1].equals("0"))
            writeOutput("GPS is turned off.\n");
          else
            writeOutput("GPS is turned on.\n");
        }
        return;
      }
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        if (cmd.equals("AT+CGPSPWR=1"))
          str = "GPS Turned on\n\n";
        else {
          str = "GPS Turned off\n\n";
        }
      }
      else {
        str = "Command error.. Currently GPS commands only supported for SIMCOM modules\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.contains("AT+CGPSRST"))
    {
      if (cmd.equals("AT+CGPSRST?"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.contains("ERROR")) {
          writeOutput("Please check if the module supports GPS feature.\n");
        }
        else {
          tmp = resp2.split(":");
          tmp[1] = tmp[1].trim();
          if (tmp[1].equals("0"))
            writeOutput("GPS reset in COLD start mode.\n");
          else
            writeOutput("GPS reset in autonomy mode.\n");
        }
        return;
      }
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        if (cmd.equals("AT+CGPSRST=0"))
          str = "GPS COLD start sucessful\n\n";
        else {
          str = "GPS Autonomy start sucessful\n\n";
        }
      }
      else {
        str = "Command error.. Currently GPS commands only supported for SIMCOM modules\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.contains("AT+CGPSOUT"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.equals("OK"))
      {
        if (cmd.equals("AT+CGPSOUT=0"))
          str = "NMEA output is disabled\n\n";
        else {
          str = "NMEA port is enabled\n\n";
        }
      }
      else {
        str = "Command error.. Currently GPS commands only supported for SIMCOM modules\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.contains("AT+CGPSIPR="))
    {
      String resp2 = (String)response.get(1);
      if (resp2.equals("OK"))
      {
        str = "NMEA port setting sucessful\n\n";
      }
      else
      {
        str = "Command error.. Currently GPS commands only supported for SIMCOM modules\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.contains("AT+CGPSSTATUS?"))
    {
      String resp2 = (String)response.get(1);
      if (resp2.equals("OK"))
      {
        resp2 = (String)response.get(0);
        tmp = resp2.split("CGPSSTATUS: ");
        if (tmp[1].equals("Location Unknown"))
        {
          global_gps_status = "gps_not_run";
          textField_17.setText("GPS Not Run");
          writeOutput("GPS is not running\n");
        }
        else if (tmp[1].equals("Location Not Fix"))
        {
          global_gps_status = "gps_run_no_fix";
          textField_17.setText("GPS run, NO Fix");
          writeOutput("No GPS fix\n");
        }
        else if (tmp[1].equals("Location 2D Fix"))
        {
          global_gps_status = "gps_2d_fix";
          textField_17.setText("GPS 2D Fix");
          writeOutput("GPS 2D fix\n");
        }
        else if (tmp[1].equals("Location 3D Fix"))
        {
          global_gps_status = "gps_3D_fix";
          textField_17.setText("GPS 3D Fix");
          writeOutput("GPS 3D fix\n");
        }
      }
      else
      {
        str = "Command error.. Currently GPS commands only supported for SIMCOM modules\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.equals("AT+CGPSINF=0"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.contains("ERROR"))
      {
        writeOutput("Please check if the module supports GPS feature.\n");
        return;
      }
      resp2 = (String)response.get(1);

      if (resp2.equals("OK"))
      {
        tmp = resp2.split(",");
        lblLongitude.setText(tmp[1]);
        lblLatitude.setText(tmp[2]);
        lblTime.setText(tmp[3]);
        lblTimeToFirst.setText(tmp[4]);
        lblNumSatellites.setText(tmp[5]);
        lblSpeed.setText(tmp[6]);
        lblCourse.setText(tmp[7]);
        writeOutput("Longitude->" + tmp[1] + "\n" + 
          "Latitude->" + tmp[2] + "\n" + 
          "Time->" + tmp[2] + "\n" + 
          "TimeToFirstFix->" + tmp[4] + "\n" + 
          "NumSatellites->" + tmp[5] + "\n" + 
          "Speed->" + tmp[6] + "\n" + 
          "Course->" + tmp[7] + "\n");
      }
      else
      {
        str = "Command error.. Currently GPS commands only supported for SIMCOM modules\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.equals("AT$GPSACP"))
    {
      String resp2 = (String)response.get(1);
      if (resp2.equals("OK"))
      {
        resp2 = (String)response.get(0);
        tmp = resp2.split(",");
        String fix = tmp[5];
        if (fix.equals("0"))
        {
          writeOutput("Invalid GPS Fix");
        }
        else
        {
          if (fix.equals("2"))
            writeOutput("2D GPS Fix");
          else if (fix.equals("3")) {
            writeOutput("3D GPS Fix");
          }
          String my_new_str = tmp[2].replaceAll("E", "");
          my_new_str = my_new_str.replaceAll("W", "");

          Double rawValue = Double.valueOf(Double.parseDouble(my_new_str));
          Double degrees = Double.valueOf(rawValue.doubleValue() / 100.0D);
          Double minutes = Double.valueOf(rawValue.doubleValue() - degrees.doubleValue() * 100.0D);
          degrees = Double.valueOf(-(degrees.doubleValue() + minutes.doubleValue() / 60.0D));

          textField_30.setText(Double.toString(degrees.doubleValue()));

          my_new_str = tmp[1].replaceAll("N", "");
          my_new_str = my_new_str.replaceAll("S", "");

          rawValue = Double.valueOf(Double.parseDouble(my_new_str));
          degrees = Double.valueOf(rawValue.doubleValue() / 100.0D);
          minutes = Double.valueOf(rawValue.doubleValue() - degrees.doubleValue() * 100.0D);
          degrees = Double.valueOf(degrees.doubleValue() + minutes.doubleValue() / 60.0D);

          textField_29.setText(Double.toString(degrees.doubleValue()));
          textField_31.setText(tmp[4]);
          textField_33.setText(tmp[10]);
          textField_34.setText(tmp[7]);
          textField_35.setText(tmp[6]);
        }
      }
      else
      {
        str = "Command error.. Currently GPS commands only supported for SIMCOM modules\n\n";
      }

      writeOutput(str);
    }
    else if (cmd.contains("AT+CGACT"))
    {
      if (cmd.contains("AT+CGACT="))
      {
        String resp2 = (String)response.get(0);
        if (resp2.equals("OK"))
        {
          if (cgact_action.equals("activate"))
          {
            connect_status = 1;
            writeOutput("Connection is successful\n");
          }
          else if (cgact_action.equals("deactivate"))
          {
            connect_status = 0;
            writeOutput("Dis-connect is successful\n");
          }
        }
        else
        {
          tmp = resp2.split(":");
          str = "Connect Failure -" + tmp[1] + "\n\n";
          connect_status = 0;
        }

        writeOutput(str);
      }
      else if (cmd.equals("AT+CGACT?"))
      {
        num_profiles_connected = 0;
        connected_profile = null;
        int num_profiles = response.size();
        if (num_profiles <= 1)
        {
          str = "No connection profiles stored in the device.\n";
        }
        else
        {
          for (int j = 0; j < num_profiles; j++)
          {
            t1 = null;
            String resp = (String)response.get(j);
            if (resp.equals("OK"))
              break;
            if (resp != "")
            {
              tmp = resp.split(":");
              t1 = tmp[1].split(",");
              t1[1] = t1[1].trim();
              if (t1[1].equals("1"))
              {
                num_profiles_connected += 1;
                connected_profile = t1[0];
              }
            }

          }

        }

      }

    }
    else if (cmd.contains("AT+CMGS="))
    {
      String resp2 = (String)response.get(2);

      if (resp2.contains("ERROR"))
      {
        tmp = resp2.split(":");
        str = "SMS Send Failure -" + tmp[1] + "\n\n";
        writeOutput(str);
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "sms_send_failure" });
      }
      else
      {
        String resp = (String)response.get(3);
        if (resp.contains("OK"))
        {
          str = "SMS Send successful\n\n";
          writeOutput(str);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", "sms_send_success" });
          }
        }
      }

    }
    else if (cmd.contains("AT+CMGD="))
    {
      String resp2 = (String)response.get(0);

      if (resp2.contains("ERROR"))
      {
        tmp = resp2.split(":");
        str = "SMS Delete Failure -" + tmp[1] + "\n\n";
        writeOutput(str);
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "sms_delete_failure" });
      }
      else if (resp2.contains("OK"))
      {
        str = "SMS Delete successful\n\n";
        writeOutput(str);
        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "sms_delete_success" });
        }
        String ret = mySerial1.ser_write("AT+CMGL=\"ALL\"\r\n", 1);
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException) {
        }
      }
    }
    else if (cmd.contains("AT#SCFG?"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.contains("ERROR"))
      {
        scfg_status = "FALSE";
        return;
      }

      num_socket_profiles = response.size();
      if (num_socket_profiles <= 1)
      {
        str = "No socket profiles stored in the device.\n";
      }
      else
      {
        scfg_status = "TRUE";
        String[] t3 = null;
        String[] tmp1 = null;
        for (int j = 0; j < num_socket_profiles; j++)
        {
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            System.out.println(resp);
            tmp1 = resp.split(":");
            System.out.println(Arrays.toString(tmp1));
            t3 = tmp1[1].split(",");
            System.out.println("t3:");
            System.out.println(Arrays.toString(t3));
            connId[j] = t3[0].trim();
            pdpId[j] = t3[1].trim();
          }
        }
        System.out.println("in parser:");
        System.out.println(Arrays.toString(pdpId));
      }

    }
    else if (cmd.contains("AT#SGACT?"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.contains("ERROR"))
      {
        scfg_status = "FALSE";
        return;
      }

      String[] tmp2 = null;
      Integer act_profs = Integer.valueOf(response.size());
      if (act_profs.intValue() <= 1)
      {
        str = "No socket profiles stored in the device.\n";
      }
      else
      {
        for (int j = 0; j < act_profs.intValue(); j++)
        {
          t1 = null;

          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp2 = resp.split(":");
            t1 = tmp2[1].split(",");
            String pdp_status = "";
            t1[1] = t1[1].trim();
            if (t1[1].equals("0"))
              pdp_status = "Inactive";
            else if (t1[1].equals("1"))
              pdp_status = "Active";
            socketPdpId[j] = t1[0].trim();
            socketPdpStatus[j] = pdp_status;
          }

        }

      }

    }
    else if (cmd.contains("AT#SGACT="))
    {
      String resp2 = (String)response.get(0);

      if (resp2.contains("ERROR"))
      {
        socket_pdp_activation_status = "FAIL";
        writeOutput("Error activating/deactivating PDP context\n");
        return;
      }
      if (resp2.equals("OK"))
      {
        socket_pdp_deactivation_status = "SUCCESS";
        writeOutput("Success deactivating PDP context\n");
        return;
      }

      t1 = resp2.split("#SGACT:");
      String ip_address = t1[1];
      socket_pdp_activation_status = "SUCCESS";
      writeOutput("PDP activation sucess. IP address is" + ip_address + "\n");
    }
    else if (cmd.contains("AT#SI"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.contains("ERROR"))
      {
        scfg_status = "FALSE";
        return;
      }

      num_socket_profiles = response.size();
      if (num_socket_profiles <= 1)
      {
        str = "No socket profiles stored in the device.\n";
      }
      else
      {
        for (int j = 0; j < num_socket_profiles; j++)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split(":");
            t1 = tmp[1].split(",");
            socketDataSent[j] = t1[1];
            socketDataReceived[j] = t1[2];
          }

        }

      }

    }
    else if (cmd.contains("AT#SS"))
    {
      String resp2 = (String)response.get(0);
      if (resp2.contains("ERROR"))
      {
        scfg_status = "FALSE";
        return;
      }

      num_socket_profiles = response.size();
      if (num_socket_profiles <= 1)
      {
        str = "No socket profiles stored in the device.\n";
      }
      else
      {
        for (int j = 0; j < num_socket_profiles; j++)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split(":");
            t1 = tmp[1].split(",");
            t1[1] = t1[1].trim();
            String state = "";
            if (t1[1].equals("0"))
              state = "Closed";
            else if (t1[1].equals("1"))
              state = "Active";
            else if (t1[1].equals("2"))
              state = "Suspended";
            else if (t1[1].equals("3"))
              state = "Pending";
            else if (t1[1].equals("4"))
              state = "Listening";
            else if (t1[1].equals("5")) {
              state = "Incoming";
            }

            socketState[j] = state;
          }

        }

      }

    }
    else if (cmd.contains("AT+CGQREQ?"))
    {
      int num_profiles = response.size();
      if (num_profiles <= 1)
      {
        str = "No QoS profiles stored on the device.\n";
        return;
      }

      if (num_profiles >= 1)
        writeOutput("Following Quality of Service (QoS) profiles are available,\r\n");
      for (int j = 0; j < num_profiles; j++)
      {
        t1 = null;
        String resp = (String)response.get(j);
        if (resp.equals("OK"))
          break;
        if (resp != "")
        {
          tmp = resp.split(":");
          t1 = tmp[1].split(",");

          writeOutput("\r\n");
          writeOutput("CID->" + t1[0] + "\n" + "Precedence->" + t1[1] + "\n" + "Delay->" + t1[2] + "\n" + "Reliability->" + t1[3] + "\n" + "Peak->" + t1[4] + "\n" + "Mean->" + t1[5] + "\r\n");
        }

      }

    }
    else if (cmd.contains("AT+CGDCONT"))
    {
      DefaultTableModel dm;
      if (cmd.equals("AT+CGDCONT?"))
      {
        int num_profiles = response.size();
        if (num_profiles <= 1)
        {
          str = "No connection profiles stored in the device.\n";
          return;
        }

        dm = (DefaultTableModel)table.getModel();
        for (int i = dm.getRowCount() - 1; i >= 0; i--) {
          dm.removeRow(i);
        }
        if (num_profiles >= 1)
          writeOutput("Following connection profiles are available,\r\n");
        for (int j = 0; j < num_profiles; j++)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split(":");
            t1 = tmp[1].split(",");
            t1[1] = t1[1].replace("\"", "");
            t1[2] = t1[2].replace("\"", "");
            t1[3] = t1[3].replace("\"", "");
            dm.addRow(new Object[] { t1[0], t1[1], t1[2], t1[3], t1[4], t1[5] });
            writeOutput("\r\n");
            writeOutput("CID->" + t1[0] + "\n" + "PDP Type->" + t1[1] + "\n" + "APN->" + t1[2] + "\n" + "PDP Address->" + t1[3] + "\n" + "Data Compression->" + t1[4] + "\n" + "Header Compression->" + t1[5] + "\r\n");
            if (j == 0) {
              default_apn = t1[2].replaceAll("\"", "");
            }

          }

        }

        table.setRowSelectionInterval(0, 0);
      }
      else if (cmd.contains("AT+CGDCONT="))
      {
        String resp2 = (String)response.get(0);
        if (resp2.equals("OK"))
        {
          str = "PDP Context list updated sucessfully\n";
          writeOutput(mySerial1.ser_write("AT+CGDCONT?\r\n", 1));
        }
        else if (resp2.equals("ERROR"))
        {
          str = "Error updating PDP context profile.Mobile error codes are not enabled.\n";
        }
        else
        {
          tmp = resp2.split(":");
          str = "Error updating PDP context profile - " + tmp[1] + "\n";
        }

        writeOutput(str + "\n");
      }

    }
    else if (cmd.contains("AT+CMGL"))
    {
      int num_profiles = response.size();
      if (num_profiles <= 1)
      {
        str = "No messages stored in the device.\n\n";
      }
      else
      {
        DefaultTableModel dm = (DefaultTableModel)table_1.getModel();
        for (int i = dm.getRowCount() - 1; i >= 0; i--) {
          dm.removeRow(i);
        }
        for (int j = 0; j < num_profiles; j += 2)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split("CMGL:");
            t1 = tmp[1].split(",");
            t1[1] = t1[1].replace("\"", "");
            t1[2] = t1[2].replace("\"", "");
            t1[4] = t1[4].replace("\"", "");
            t1[5] = t1[5].replace("\"", "");
            String sms_msg = (String)response.get(j + 1);
            dm.addRow(new Object[] { t1[0], t1[1], t1[2], t1[4] + " " + t1[5], sms_msg });
          }
        }

      }

      table_1.setRowSelectionInterval(0, 0);
    }
    else if (cmd.equals("AT+CGMR"))
    {
      String resp = (String)response.get(1);
      String resp2 = (String)response.get(0);
      if (resp.equals("OK"))
      {
        str = "Revision : " + resp2 + "\n";
      }
      else
      {
        str = "Revision information not avaialble";
      }

      writeOutput(str + "\n");
    }
    else if (cmd.contains("AT+CGPADDR="))
    {
      String resp = (String)response.get(1);
      String resp2 = (String)response.get(0);
      tmp = resp2.split(":");
      t1 = tmp[1].split(",");
      if (t1[1] != "")
      {
        str = "IP Address of the connected profile is " + t1[1] + "\n";
      }

      writeOutput(str + "\n");
    }
    else
    {
      if (cmd.equals("AT+FTPGET"))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error with FTP GET session\n";
        }

        writeOutput(str + "\n");
        return;
      }

      if (cmd.equals("AT+FTPPUT"))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error with FTP PUT session\n";
        }

        writeOutput(str + "\n");
        return;
      }

      if (cmd.contains("AT+CSTT"))
      {
        String resp2 = (String)response.get(0);
        if (cmd.equals("AT+CSTT?"))
        {
          tmp = resp2.split("CSTT:");

          t1 = tmp[1].split(",");

          t1[0] = t1[0].trim();
          t1[1] = t1[1].trim();
          t1[2] = t1[2].trim();

          writeOutput("APN->" + t1[0] + "\n" + "USER->" + t1[1] + "\n" + "PASSWORD->" + t1[2] + "\n");
        }

        if (resp2.equals("OK"))
        {
          cstt_status = "OK";
          str = "APN setup for TCP connection successful..\n";
          writeOutput(str + "\n");
        }

        return;
      }

      if (cmd.contains("AT+CIICR"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.equals("OK"))
        {
          ciicr_status = "OK";
          str = "GPRS Connection bring up sucessful..\n";
        }

        writeOutput(str + "\n");
        return;
      }

      if (cmd.contains("AT+CIFSR"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.contains("ERROR"))
        {
          tcp_local_ip_address = "ERROR";
          writeOutput("IP not avaialble. Check if the PDP context has been activated\n");
        }
        else
        {
          tcp_local_ip_address = resp2;
          writeOutput("Local IP address is " + resp2 + "\n");
        }

        return;
      }

      if (cmd.contains("AT+CIPSTART"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.contains("ERROR"))
        {
          cip_start_status = "ERROR";
        }
        else if (resp2.equals("ALREADY CONNECT"))
        {
          cip_start_status = "ALREADY_CONNECT";
        }
        else if (resp2.equals("OK"))
        {
          cip_start_status = "OK";
        }

        return;
      }

      if (cmd.equals("CONNECT OK"))
      {
        tcp_connection_status = "CONNECT OK";
        str = "TCP connection success\n";
        writeOutput(str + "\n");
        return;
      }

      if (cmd.equals("CLOSE"))
      {
        tcp_connection_status = "CLOSE";
        str = "TCP connection is closed by remote server\n";
        writeOutput(str + "\n");
        return;
      }

      if (cmd.contains("+FTPGET:"))
      {
        String data = "";
        String resp2 = (String)response.get(0);

        t1 = resp2.split(",");
        if ((t1[0].equals("1")) && (t1[1].equals("1")))
        {
          str = "FTP session sucessfully started\n";
          writeOutput(str + "\n");

          ftp_get_data = "FAIL";
          cmd = "AT+FTPGET=2,1024\r\n";
          String ret = mySerial1.ser_write(cmd, 1);
          try {
            Thread.sleep(400L);
          } catch (InterruptedException localInterruptedException1) {
          }
          ftp_get_data.equals("SUCCESS");
        }
        else if ((t1[0].equals("1")) && (t1[1].equals("0")))
        {
          str = "FTP session end\n";
          writeOutput(str + "\n");
        }
        else if ((t1[0].equals("1")) && (!t1[1].equals("0")) && (!t1[1].equals("1")))
        {
          str = "Error starting FTP session: Error code is :" + t1[1] + "\n";
          writeOutput(str + "\n");
        }
        else if (t1[0].equals("2"))
        {
          if (t1[1].equals("0"))
          {
            str = "FTP data transfer is complete\n";
            writeOutput(str + "\n");
            return;
          }
          int size = response.size();
          data = "";
          for (int k = 1; k < size - 1; k++)
          {
            if (data.equals("OK"))
              break;
            data = data + (String)response.get(k);
          }
          if (data != "") {
            textArea_14.append(data);
          }
          ftp_get_data = "FAIL";
          cmd = "AT+FTPGET=2,1024\r\n";
          String ret = mySerial1.ser_write(cmd, 1);
          try {
            Thread.sleep(1000L);
          }
          catch (InterruptedException localInterruptedException2)
          {
          }
        }

      }
      else if (cmd.contains("+FTPPUT:"))
      {
        String data = "";
        String t3 = (String)response.get(0);
        String t4 = (String)response.get(1);
        if ((t3.equals("1")) && (t4.equals("0")))
        {
          str = "FTP Session is over\n";
          writeOutput(str + "\n");
          return;
        }

        if ((t3.equals("1")) && (!t4.equals("1")))
        {
          str = "FTP put error: Error code: " + t4 + "\n";
          writeOutput(str + "\n");
          return;
        }

        if ((t3.equals("1")) && (t4.equals("1")))
        {
          String t5 = (String)response.get(2);
          max_ftp_data_len = Integer.parseInt(t5);
          str = "FTP put session sucessfully started\n";
          writeOutput(str + "\n");

          if (ftp_data_over.equals("FALSE"))
          {
            ftp_start_byte = ftp_end_byte;
            this.data_to_send = (ftp_put_data_len - ftp_end_byte);
            if (this.data_to_send > max_ftp_data_len)
            {
              ftp_end_byte = (ftp_start_byte + max_ftp_data_len);
            }
            else
            {
              ftp_end_byte = (ftp_start_byte + this.data_to_send);
              ftp_data_over = "TRUE";
            }

            cmd = "AT+FTPPUT=2," + (ftp_end_byte - ftp_start_byte) + "\r\n";
          }
          else
          {
            cmd = "AT+FTPPUT=2,0\r\n";
            ftp_data_over = "FALSE";
          }
          String ret = mySerial1.ser_write(cmd, 1);
          try {
            Thread.sleep(400L);
          } catch (InterruptedException localInterruptedException3) {
          }
          return;
        }

        if (t3.equals("2"))
        {
          t4 = (String)response.get(1);
          int ftp_conf_data_len = Integer.parseInt(t4);

          char[] data_to_send = new char[ftp_end_byte - ftp_start_byte];

          ftp_put_data.getChars(ftp_start_byte, ftp_end_byte, data_to_send, 0);
          String b = new String(data_to_send);

          str = "Uploading FTP content\n";
          writeOutput(b + "\n");

          String ret = mySerial1.ser_write(b, 1);
          try {
            Thread.sleep(400L);
          } catch (InterruptedException localInterruptedException4) {
          }
          ftp_get_data.equals("SUCCESS");
        }

      }
      else if (cmd.contains("AT+CNUM"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        t1 = tmp[1].split(",");
        if (t1[1] != "")
        {
          t1[1] = t1[1].replace("\"", "");
          phone_number = t1[1];
          writeOutput("The Phone number is " + phone_number + "\r\n");
        }
        else
        {
          writeOutput("The Phone number is not available\r\n");
        }

      }
      else if (cmd.contains("AT+CNUM?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        t1 = tmp[1].split(",");
        if (t1[1] != "")
        {
          t1[1] = t1[1].replace("\"", "");
          phone_number = t1[1];
          writeOutput("The Phone number is " + phone_number + "\r\n");
        }
        else
        {
          writeOutput("The Phone number is not available\r\n");
        }

      }
      else if (cmd.contains("AT+CBC"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        t1 = tmp[1].split(",");
        if (t1[0] != "")
        {
          t1[0] = t1[0].trim();
          if (t1[0].equals("0"))
            writeOutput("Device is powered by battery.");
          else if (t1[0].equals("1"))
            writeOutput("Device is powered by battery and charger pin is being powered.");
          else if (t1[0].equals("2"))
            writeOutput("Device is not connected to battery.");
          else if (t1[0].equals("3"))
            writeOutput("Power Fault.");
        }
        if (t1[1] != "")
        {
          t1[1] = t1[1].trim();
          if (t1[1].equals("0"))
            writeOutput("Battery is exhausted.\r\n");
          else if (t1[1].equals("25"))
            writeOutput("Battery charge remained is estimated to be 25%.");
          else if (t1[1].equals("50"))
            writeOutput("Battery charge remained is estimated to be 50%.");
          else if (t1[1].equals("75"))
            writeOutput("Battery charge remained is estimated to be 75%.");
          else if (t1[1].equals("100")) {
            writeOutput("Battery is fully charged.");
          }
        }
        writeOutput("\n");
      }
      else if (cmd.contains("AT+GCAP"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");

        tmp[1] = tmp[1].trim();
        t1 = tmp[1].split(",");

        int n = t1.length;
        writeOutput("Device supports following features:");
        for (int p = 0; p < n; p++)
        {
          t1[p] = t1[p].trim();
          if (t1[p].equals("+FCLASS"))
            writeOutput("Fax,");
          if (t1[p].equals("+CGSM"))
            writeOutput("GSM,");
          if (t1[p].equals("+DS"))
            writeOutput("Data Service,");
          if (t1[p].equals("+MS")) {
            writeOutput("Mobile Specifc Features,");
          }
        }

        writeOutput("\n");
      }
      else if (cmd.contains("AT+IPR?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");

        tmp[1] = tmp[1].trim();
        if (tmp[1] != "")
        {
          if (tmp[1].equals("0"))
            writeOutput("Serial port speed is set to auto-baud.");
          else {
            writeOutput("Serial port speed is set to " + tmp[1] + ".");
          }
        }

        writeOutput("\n");
      }
      else if (cmd.contains("AT+CFUN?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");

        tmp[1] = tmp[1].trim();
        if (tmp[1] != "")
        {
          if (tmp[1].equals("0"))
            writeOutput("Device has minimun functionality.");
          else if (tmp[1].equals("1"))
            writeOutput("Device has Full functionality.");
          else if (tmp[1].equals("4")) {
            writeOutput("RX and TX circuits are disabled.");
          }
        }

        writeOutput("\n");
      }
      else if (cmd.contains("AT+CPAS"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");

        tmp[1] = tmp[1].trim();
        if (tmp[1] != "")
        {
          if (tmp[1].equals("0"))
            writeOutput("Device is Ready.");
          else if (tmp[1].equals("2"))
            writeOutput("Device status is unknown.");
          else if (tmp[1].equals("3"))
            writeOutput("Device is ringing.");
          else if (tmp[1].equals("4")) {
            writeOutput("Call in progress.");
          }
        }

        writeOutput("\n");
      }
      else if (cmd.contains("AT+IFC?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[0].equals("0"))
          writeOutput("No flow control on Terminal Equipment(TE) side ie Host Device.");
        else if (t1[0].equals("1"))
          writeOutput("Software flow control on Terminal Equipment(TE) side ie Host Device.");
        else if (t1[0].equals("2"))
          writeOutput("Hardware flow control on Terminal Equipment(TE) side ie Host Device.");
        writeOutput("\n");
        if (t1[1].equals("0"))
          writeOutput("No flow control on Terminal Adapter(TA) side ie Modem.");
        else if (t1[1].equals("1"))
          writeOutput("Software flow control on Terminal Adapter(TA) side ie Modem.");
        else if (t1[1].equals("2")) {
          writeOutput("Hardware flow control on Terminal Adapter(TA) side ie Modem.");
        }

        writeOutput("\n");
      }
      else if (cmd.contains("AT+CCLK?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CCLK:");

        tmp[1] = tmp[1].trim();

        writeOutput("Current time is" + tmp[1] + "\n");
      }
      else if (cmd.contains("AT+CGSN"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);

        tmp[1] = tmp[1].trim();

        writeOutput("The IMEI(International Mobile Equipment IdentityI is " + resp2 + "\n");
      }
      else if (cmd.contains("AT+CR?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CR:");
        tmp[1] = tmp[1].trim();

        if (tmp[1].equals("0"))
          writeOutput("Service reporting control is disabled\n");
        else {
          writeOutput("Service reporting control is enabled\n");
        }

      }
      else if (cmd.contains("AT+CSCS?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CSCS:");

        tmp[1] = tmp[1].trim();

        writeOutput("Character set used by Terminal Equipment is " + tmp[1] + "\n");
      }
      else if (cmd.contains("AT+CMUT?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CMUT:");

        tmp[1] = tmp[1].trim();

        if (tmp[1].equals("0"))
          writeOutput("Mute of Off\n");
        else {
          writeOutput("Mute of On\n");
        }

      }
      else if (cmd.contains("AT+CEER?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CEER:");

        tmp[1] = tmp[1].trim();

        if (tmp[1].equals("0"))
          writeOutput("The reason for last call release will be returned as text code\n");
        else {
          writeOutput("The reason for last call release will be returned as number code\n");
        }

      }
      else if (cmd.contains("AT+CGCLASS?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CGCLASS:");

        tmp[1] = tmp[1].trim();

        writeOutput("The device supports station class " + tmp[1] + "\n");
      }
      else if (cmd.contains("AT+CENG?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CENG:");

        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[0].equals("0"))
          writeOutput("Engineering mode is OFF. To turn on Engg mode, set AT+CENG=1,1.\n");
        else {
          writeOutput("Engineering mode is ON." + tmp[1] + "\n");
        }
      }
      else if (cmd.contains("AT+CDNSCFG?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        writeOutput("Primary DNS IP address is " + tmp[1] + "\n");
        tmp = resp.split(":");
        writeOutput("Secondary DNS IP address is " + tmp[1] + "\n");
      }
      else if (cmd.contains("AT+FTPMODE?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FTPMODE:");
        if (tmp[1].equals("0"))
          writeOutput("FTP is configured for Active mode.\n");
        else {
          writeOutput("FTP is configured for Passive mode.\n");
        }
      }
      else if (cmd.contains("AT+FTPSTATE"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FTPSTATE:");
        if (tmp[1].equals("0"))
          writeOutput("FTP in idle state.\n");
        else {
          writeOutput("FTP session in progress.\n");
        }
      }
      else if (cmd.contains("AT+FTPPORT?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FTPPORT:");

        writeOutput("FTP port is " + tmp[1] + "\n");
      }
      else if (cmd.contains("AT+FTPTYPE?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FTPTYPE:");

        if (tmp[1].equals("0"))
          writeOutput("FTP transfer type is ASCII.\n");
        else {
          writeOutput("FTP transfer type is binary.\n");
        }
      }
      else if (cmd.contains("AT+FTPPUTOPT?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FTPPUTOPT:");

        if (tmp[1].equals("\"APPE\""))
          writeOutput("FTP PUT will append file.\n");
        else if (tmp[1].equals("\"STOU\""))
          writeOutput("FTP PUT will store unique file.\n");
        else if (tmp[1].equals("\"STOR\"")) {
          writeOutput("FTP PUT will store file.\n");
        }
      }
      else if (cmd.contains("AT+CIPCSGP?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CIPCSGP:");
        t1 = tmp[1].split(",");
        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[0].equals("1"))
          writeOutput("Connection mode is set for GPRS\n");
        else {
          writeOutput("Connection mode is set for CSD\n");
        }

      }
      else if (cmd.contains("AT+CIPCCFG?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CIPCCFG:");
        t1 = tmp[1].split(",");
        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();
        t1[2] = t1[2].trim();
        t1[3] = t1[3].trim();

        writeOutput("TCP transfer configuration:\n");
        writeOutput("Number of retries->" + t1[0] + "\n" + "WaitTime->" + t1[1] + "\n" + "SendSize->" + t1[2] + "\n" + "Escape Sequence->" + t1[3] + "\n");
      }
      else if (cmd.contains("AT+CIPMODE?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CIPMODE:");

        if (tmp[1].equals("0"))
          writeOutput("TCP connection is configured for normal mode (non-transparent)\n");
        else {
          writeOutput("TCP conenction is configured for transparent mode");
        }

      }
      else if (cmd.contains("AT+CIPSERVER?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CIPSERVER:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();

        if (t1[0].equals("0"))
          writeOutput("Device is not configured in server mode\n");
        else {
          writeOutput("Device is configured for server mode\n");
        }

      }
      else if (cmd.contains("AT+CIPHEAD?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CIPHEAD:");

        if (tmp[1].equals("0"))
          writeOutput("No IP header is added to the received data\n");
        else {
          writeOutput("IP header is added, format is \"+IPD,data length\"\n");
        }
      }
      else if (cmd.contains("AT+CBAND?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CBAND:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        writeOutput("Device is currently on " + t1[0] + "\n");
      }
      else if (cmd.contains("AT+CIPMUX?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CIPMUX:");

        if (tmp[0].equals("0"))
          writeOutput("Device configured for single IP connection \n");
        else {
          writeOutput("Device configured for multi IP connection \n");
        }

        writeOutput("Device is currently on " + t1[0] + "\n");
      }
      else if (cmd.contains("AT+CBUZZERRING?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CBUZZERRING:");

        if (tmp[1].equals("0"))
          writeOutput("Buzzer sound is disabled for incoming ring\n");
        else {
          writeOutput("Buzzer sound is enabled for incoming ring\n");
        }

      }
      else if (cmd.contains("AT+CHF?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CHF:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[1].equals("0"))
          writeOutput("Main audio handset channel");
        else if (t1[1].equals("1"))
          writeOutput("Aux audio handset channel");
        else if (t1[1].equals("2"))
          writeOutput("Main audio handsfree channel");
        else if (t1[1].equals("3")) {
          writeOutput("Aux audio handsfree channel");
        }
      }
      else if (cmd.contains("AT+CSPN?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CSPN:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        writeOutput("Service provider is " + t1[0] + "\n");
      }
      else if (cmd.contains("AT+CEMNL?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CEMNL:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        writeOutput("Emergency numbers are :");
        for (int q = 0; q <= Integer.parseInt(t1[1]); q++)
          writeOutput(t1[(q + 2)] + ",");
        writeOutput("\n");
      }
      else if (cmd.contains("AT+CEXTHS?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CEXTHS:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[1].equals("1"))
          writeOutput("Headset is attached\n");
        else {
          writeOutput("Headset is not attached\n");
        }
      }
      else if (cmd.contains("AT+SVR?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("SVR:");

        if (tmp[1].equals("16"))
          writeOutput("Voice rate coding is set to AMR-HR/AMR-FR/EFR/FR/HR");
        else {
          writeOutput("Voice rate coding set to " + tmp[1] + "\n");
        }
      }
      else if (cmd.contains("AT+CMTE?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CMTE:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[0].equals(Integer.valueOf(1)))
          writeOutput("Temperature detection is enabled.Temperature is " + t1[1] + "\n");
        else {
          writeOutput("Temperature detection is disabled\n");
        }

      }
      else if (cmd.contains("AT+CGMSCLASS?"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CLASS:");

        writeOutput("Device configured for GPRS multislot class of  " + tmp[1] + "\n");
      }
      else if (cmd.contains("AT+CADC?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CADC:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[0].equals("1"))
          writeOutput("ADC read is successful. ADC value is  " + t1[1] + "\n");
        else {
          writeOutput("ADC read is unsucessful\n");
        }
      }
      else if (cmd.contains("AT+CCALR?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CCALR:");

        if (tmp[0].equals("0"))
          writeOutput("Device is not ready for phone call." + t1[1] + "\n");
        else {
          writeOutput("Device is ready for phone call.\n");
        }
      }
      else if (cmd.contains("AT+CSMINS?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CSMINS:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        if (t1[1].equals("1"))
          writeOutput("SIM is inserted.\n");
        else {
          writeOutput("SIM is not inserted.\n");
        }
      }
      else if (cmd.contains("AT+CCID"))
      {
        String resp2 = (String)response.get(0);
        writeOutput("ICCID of the SIM is " + resp2 + "\n");
      }
      else if (cmd.contains("AT+CCVM?"))
      {
        String resp2 = (String)response.get(0);

        if (resp2.equals("OK"))
        {
          writeOutput("Voicemail number is not set.\n");
          return;
        }
        String resp = (String)response.get(1);

        tmp = resp2.split("CCVM:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        writeOutput("Voicemail number is " + t1[0] + "\n");
      }
      else if (cmd.contains("AT+CBST?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();
        t1[2] = t1[2].trim();

        String drate3 = "";
        String drate;
        if (t1[0].equals("0"))
          drate = "Speed ->autobauding\n";
        else
          drate = "Speed -> " + t1[0] + "\n";
        String drate2;
        if (t1[1].equals("0"))
          drate2 = "Bearer Service -> Data circuit asynchronous\n";
        else {
          drate2 = "Bearer Service -> Unknown\n";
        }
        if (t1[2].equals("0"))
          drate3 = "Connection Element -> Transparent\n";
        else {
          drate3 = "Connection Element -> Non transparent\n";
        }

        writeOutput(drate + drate2 + drate3 + "\n");
      }
      else if (cmd.contains("AT+CSMS?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();
        t1[2] = t1[2].trim();
        t1[3] = t1[3].trim();

        if (t1[0].equals("0"))
          writeOutput("Service-> GSM 27.005 Compatible\n");
        else {
          writeOutput("Service-> Unkown type\n");
        }
        if (t1[1].equals("0"))
          writeOutput("Mobile Terminated SMS -> NOT supported\n");
        else {
          writeOutput("Mobile Terminated SMS -> Supported\n");
        }
        if (t1[2].equals("0"))
          writeOutput("Mobile Originated SMS -> NOT supported\n");
        else {
          writeOutput("Mobile Originated SMS -> Supported\n");
        }
        if (t1[3].equals("0"))
          writeOutput("Broadcast type SMS messages -> NOT supported\n");
        else {
          writeOutput("Broadcast SMS messages -> Supported\n");
        }

      }
      else if (cmd.contains("AT+CRLP?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();
        t1[2] = t1[2].trim();
        t1[3] = t1[3].trim();
        t1[4] = t1[4].trim();

        writeOutput("Radio Link Protocol (RLP) Configuration paramaters:\n");

        writeOutput("IWF Window Dimension ->" + t1[0] + "\n" + "MS Window Dimension->" + t1[1] + "\n" + 
          "Acknowledge Timer->" + t1[2] + "\n" + "Retransmission Attempts->" + t1[3] + "\n" + "Protocol Version->" + t1[4] + "\n");
      }
      else if (cmd.contains("AT+CMGF?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split(":");

        tmp[1] = tmp[1].trim();

        if (tmp[1].equals("0"))
        {
          gbl_sms_mode = 0;
          writeOutput("SMS message for is configured for PDU mode\n");
        }
        else
        {
          gbl_sms_mode = 1;
          writeOutput("SMS message for is configured for Text mode\n");
        }

      }
      else if (cmd.contains("AT+CSCA?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CSCA:");
        t1 = tmp[1].split(",");

        t1[0] = t1[0].trim();
        t1[1] = t1[1].trim();

        gbl_smsc = t1[0];

        gbl_smsc = gbl_smsc.replace("\"", "");
        writeOutput("SMS service center address is " + gbl_smsc + "\n");
      }
      else if (cmd.contains("AT+FCLASS?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);

        if (resp2.equals("0"))
          writeOutput("The device is configured for data\n");
        else if (resp2.equals("1"))
          writeOutput("The device is configured for Fax\n");
        else if (resp2.equals("8")) {
          writeOutput("The device is configured for voice\n");
        }

      }
      else if (cmd.contains("AT+CGATT"))
      {
        if (cmd.equals("AT+CGATT?"))
        {
          String resp = (String)response.get(1);
          String resp2 = (String)response.get(0);
          tmp = resp2.split("CGATT:");
          t1 = tmp[1].split(",");

          t1[0] = t1[0].trim();
          if (t1[0].equals("1"))
            writeOutput("Device is attached to the network\r\n");
          else {
            writeOutput("Device is NOT attached to the network\r\n");
          }
        }
        String resp2 = (String)response.get(0);
        if (resp2.equals("OK"))
        {
          attach_status = 1;
        }
        else
        {
          attach_status = 0;
        }

        writeOutput(str + "\n");
      }
      else if (cmd.contains("AT+CPBW"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.equals("OK"))
        {
          str = "Phonebook sucessfully updated\n";
          String ret = mySerial1.ser_write("AT+CPBR=1,99\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException5) {
          }
        }
        else {
          str = "Error updating phonebook\n";
        }

        writeOutput(str + "\n");
      }
      else if (cmd.equals("AT+COPS?"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.equals("ERROR"))
        {
          str = "Unable to get operator information. It is likely device is not registered on any operator network.\n";
          writeOutput(str + "\n");
        }
        else
        {
          String resp = (String)response.get(1);

          if (resp.equals("OK"))
          {
            if (resp2.contains(":"))
            {
              tmp = resp2.split(":");
              t1 = tmp[1].split(",");
            }
            else
            {
              t1 = resp2.split(",");
            }

            t1[0] = t1[0].trim();
            str = "Device is currently on " + t1[2] + " network.\n";

            writeOutput(str + "\n");
          }
        }
      }
      else if (cmd.contains("AT+COPS="))
      {
        if (cmd.equals("AT+COPS=?"))
        {
          String signal_condition = "Unknown";
          String resp = (String)response.get(0);

          if (resp.contains("ERROR"))
          {
            str = "Unable to find networks.\n";
            writeOutput(str + "\n");
          }
          else
          {
            Pattern pattern = Pattern.compile("(\\([^\\)]*)");
            Matcher matcher = pattern.matcher(resp);
            DefaultTableModel dm = (DefaultTableModel)table_2.getModel();
            for (int i = dm.getRowCount() - 1; i >= 0; i--) {
              dm.removeRow(i);
            }
            String opr_status = "";

            writeOutput("Available Networks:\n");
            while (matcher.find())
            {
              String tmp2 = matcher.group();
              tmp2 = tmp2.replace("(", "");
              String[] tmp3 = tmp2.split(",");
              if (tmp3[0].equals("0"))
                opr_status = "Unknown";
              else if (tmp3[0].equals("1"))
                opr_status = "Available";
              else if (tmp3[0].equals("2"))
                opr_status = "Current";
              else if (tmp3[0].equals("3"))
                opr_status = "Forbidden";
              if (tmp3[1].contains("\""))
              {
                tmp3[1] = tmp3[1].replace("\"", "");
                tmp3[3] = tmp3[3].replace("\"", "");
                dm.addRow(new Object[] { tmp3[1], tmp3[3], opr_status });
                writeOutput("Network Name->" + tmp3[1] + "\n" + "Network ID->" + tmp3[3] + "\n" + "Network Status->" + opr_status + "\n\n");
              }
            }

            str = "Networks found\n\n";
            writeOutput(str);
          }
        }
        else
        {
          String resp2 = (String)response.get(0);

          if (resp2.contains("ERROR"))
          {
            t1 = resp2.split(":");
            str = "Unable to select the network. Error code is " + t1[1] + "\n\n";
            writeOutput(str);
          }
          else if (resp2.equals("OK"))
          {
            str = "Network selection sucessful.\n\n";
            writeOutput(str);

            str = "Updating the list...\n\n";
            writeOutput(str);

            String ret = mySerial1.ser_write("AT+COPS=?\r\n", 1);
            try {
              Thread.sleep(5000L);
            } catch (InterruptedException localInterruptedException6) {
            }
          }
        }
      }
      else if (cmd.contains("AT+CPBR="))
      {
        String resp2 = (String)response.get(0);

        if (resp2.contains("ERROR"))
        {
          str = "Unable to read phonebook entries.\n";
          writeOutput(str + "\n");
          return;
        }

        int num_profiles = response.size();
        if (num_profiles <= 1)
        {
          str = "No phonebook entries found in the device.\n\n";
          writeOutput(str + "\n");
          return;
        }

        DefaultTableModel dm = (DefaultTableModel)table_3.getModel();
        for (int i = dm.getRowCount() - 1; i >= 0; i--) {
          dm.removeRow(i);
        }
        for (int j = 0; j < num_profiles; j++)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split("CPBR:");
            t1 = tmp[1].split(",");
            t1[1] = t1[1].replace("\"", "");

            t1[3] = t1[3].replace("\"", "");

            dm.addRow(new Object[] { t1[0], t1[1], t1[3], t1[2] });
          }

        }

        table_3.setRowSelectionInterval(0, 0);
      }
      else if (cmd.contains("AT+SAPBR=3"))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error updating bearer profile.\n";
          writeOutput(str + "\n");
        }

      }
      else if (cmd.contains("AT+SAPBR=0"))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error closing bearer profile.\n";
          writeOutput(str + "\n");
        }

      }
      else if (cmd.contains("AT+HTTPINIT"))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error initializing HTTP service.\n";
          writeOutput(str + "\n");
          httpinit_response = "FAIL";
          return;
        }
        httpinit_response = "SUCCESS";
      }
      else if (cmd.contains("AT+HTTPPARA="))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error setting up HTTP parameters..\n\n";
          writeOutput(str + "\n");
          httppara_set = "FAIL";
          return;
        }
        httppara_set = "SUCCESS";
      }
      else if (cmd.contains("AT+HTTPREAD"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("HTTPREAD:");
        int size = Integer.parseInt(tmp[1]);
        if (size <= 0)
        {
          str = "No data received from remote server..\n\n";
          writeOutput(str + "\n");
          httpread_result = "FAIL";
          return;
        }
        resp2 = (String)response.get(1);
        textArea_13.setText(resp2);

        httpread_result = "SUCCESS";
      }
      else if (cmd.contains("AT+HTTPTERM"))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error terminating HTTP session..\n\n";
          writeOutput(str + "\n");
          httpterm_result = "FAIL";
          return;
        }
        httpterm_result = "SUCCESS";
      }
      else if (cmd.contains("AT+HTTPACTION="))
      {
        String resp2 = (String)response.get(0);
        if (resp2.equals("OK"))
        {
          return;
        }
        String resp = (String)response.get(0);
        tmp = resp2.split("HTTPACTION:");
        t1 = tmp[1].split(",");

        if (t1[1].equals("200"))
        {
          if (t1[0].equals("0"))
            str = "HTTP GET is sucessful\n\n";
          else
            str = "HTTP POST is sucessful\n\n";
          writeOutput(str);
          httpaction_result = "SUCCESS";
        }
        else
        {
          if (t1[0].equals("0"))
            str = "Error getting HTTP data: Error code - " + t1[0] + "\n\n";
          else
            str = "Error putting HTTP data: Error code - " + t1[0] + "\n\n";
          writeOutput(str);
          httpaction_result = "FAIL";
        }

      }
      else if ((cmd.contains("AT+FTPSERV")) || (cmd.contains("AT+FTPUN")) || (cmd.equals("AT+FTPPW")) || 
        (cmd.contains("AT+FTPGETNAME")) || (cmd.contains("AT+FTPGETPATH")))
      {
        if (cmd.contains("?"))
        {
          String resp2 = (String)response.get(0);
          tmp = resp2.split(":");

          tmp[1] = tmp[1].trim();

          if (tmp[1].equals("\"\""))
            tmp[1] = "not set";
          if (cmd.equals("AT+FTPSERV?"))
            writeOutput("FTP Server name is " + tmp[1] + "\n");
          else if (cmd.equals("AT+FTPUN?"))
            writeOutput("FTP user name is " + tmp[1] + "\n");
          else if (cmd.equals("AT+FTPPW?"))
            writeOutput("FTP password is " + tmp[1] + "\n");
          else if (cmd.equals("AT+FTPGETNAME?"))
            writeOutput("FTP GET file name is " + tmp[1] + "\n");
          else if (cmd.equals("AT+FTPGETPATH?"))
            writeOutput("FTP GET Path name is " + tmp[1] + "\n");
          return;
        }
        ftp_set = "FAIL";
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error setting FTP parameter.\n";
          writeOutput(str + "\n");
          return;
        }

        ftp_set = "SUCCESS";
      }
      else if (cmd.contains("AT+SAPBR=1"))
      {
        String resp2 = (String)response.get(0);
        if (!resp2.equals("OK"))
        {
          str = "Error opening bearer profile.\n";
          writeOutput(str + "\n");
        }

      }
      else if (cmd.contains("AT+SAPBR=2"))
      {
        String resp2 = (String)response.get(0);
        String resp = (String)response.get(1);
        if (resp.equals("OK"))
        {
          tmp = resp2.split(":");
          t1 = tmp[1].split(",");

          global_bearer_cid = t1[0];
          if (t1[1].equals("0"))
            global_bearer_state = "Connecting";
          else if (t1[1].equals("1"))
            global_bearer_state = "Connected";
          else if (t1[1].equals("2"))
            global_bearer_state = "Closing";
          else if (t1[1].equals("3"))
            global_bearer_state = "Closed";
          global_bearer_ip = t1[2];

          str = "Bearer " + global_bearer_cid + " is " + global_bearer_state;
          if (global_bearer_state.equals("Connected"))
            str = str + ".IP address is " + global_bearer_ip + "\n";
          else {
            str = str + "\n";
          }
          writeOutput(str + "\n");
        }

      }
      else if (cmd.contains("AT+SAPBR=4"))
      {
        String resp2 = (String)response.get(0);

        if (resp2.contains("ERROR"))
        {
          str = "Unable to get bearer profiles.\n";
          writeOutput(str + "\n");
          return;
        }

        DefaultTableModel dm = (DefaultTableModel)table_4.getModel();

        String cont_type = ""; String apn = ""; String user = ""; String pwd = ""; String pn_num = ""; String rate = "";
        if (refresh_bearer_list.equals("TRUE"))
        {
          dm = (DefaultTableModel)table_4.getModel();
          for (int i = dm.getRowCount() - 1; i >= 0; i--) {
            dm.removeRow(i);
          }
        }

        String resp = (String)response.get(1);

        if (resp != "")
        {
          tmp = resp.split("CONTYPE:");
          cont_type = tmp[1];
        }

        resp = (String)response.get(2);

        if (resp != "")
        {
          tmp = resp.split("APN:");
          apn = tmp[1];
        }

        resp = (String)response.get(3);
        if (resp != "")
        {
          tmp = resp.split("PHONENUM:");
          pn_num = tmp[1];
        }

        resp = (String)response.get(4);
        if (resp != "")
        {
          tmp = resp.split("USER:");
          user = tmp[1];
        }

        resp = (String)response.get(5);
        if (resp != "")
        {
          tmp = resp.split("PWD:");
          pwd = tmp[1];
        }

        resp = (String)response.get(6);
        if (resp != "")
        {
          tmp = resp.split("RATE:");
          rate = tmp[1];
        }

        dm.addRow(new Object[] { bearer_cid, cont_type, apn, user, pwd, pn_num, rate });

        table_4.setRowSelectionInterval(0, 0);
      }
      else if (cmd.equals("AT+CPIN?"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.equals("ERROR"))
        {
          str = "Unable to SIM Status infomration.\n";
          writeOutput(str + "\n");
        }
        else
        {
          String resp = (String)response.get(1);

          if (resp.equals("OK"))
          {
            if (resp2.contains(":"))
            {
              tmp = resp2.split(":");
              t1 = tmp[1].split(",");
            }
            else
            {
              t1 = resp2.split(",");
            }

            t1[0] = t1[0].trim();
            if (t1[0].equals("READY"))
              str = "SIM is ready.\n";
            else {
              str = "SIM is not ready. Possible issues - SIM is not inserted, SIM PIN required, SIM PUK code required.\n";
            }
            writeOutput(str + "\n");
          }
          else
          {
            str = "SIM is not ready. Possible issues - SIM is not inserted, SIM PIN required, SIM PUK code required.\n";
            writeOutput(str + "\n");
          }
        }

      }
      else if (cmd.equals("AT+CREG?"))
      {
        String signal_condition = "Unknown";
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        if (resp.equals("OK"))
        {
          t1 = resp2.split(":");
          t2 = t1[1].split(",");

          reg_status = Integer.parseInt(t2[1]);
          if (reg_status == 0)
            str = "The device is not registered and is currently NOT searching for a new operator to which to register.\n";
          else if (reg_status == 1)
            str = "The device is registered in home network.\n";
          else if (reg_status == 2)
            str = "The device is not registered and is currently searching for a new operator to which to register.\n";
          else if (reg_status == 3)
            str = "Registration is denied. Please check the SIM card.\n";
          else if (reg_status == 4)
            str = "Registration status is unknown.\n";
          else if (reg_status == 5) {
            str = "Device is registered and is roaming.\n";
          }
          str = str + "\n";
          writeOutput(str);
        }
      }
      else if (cmd.equals("AT+CSQ"))
      {
        String signal_condition = "Unknown";
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        if (resp.equals("OK"))
        {
          if (resp2.contains(":"))
          {
            tmp = resp2.split(":");
            t1 = tmp[1].split(",");
          }
          else
          {
            t1 = resp2.split(",");
          }

          t1[0] = t1[0].trim();
          if (t1[0] == "99") {
            str = "Signal Strength is unknown. Device is not able to acquire any network. Check if device has a valid SIM.\n";
          }
          else {
            int dbm = 99;
            try {
              dbm = Integer.parseInt(t1[0]);
            } catch (NumberFormatException e) {
              dbm = 50;
            }
            dbm = 109 - (dbm - 2) * 2;
            if ((dbm >= 95) && (dbm <= 109))
              signal_condition = "Signal condition is marginal.";
            else if ((dbm >= 85) && (dbm <= 94))
              signal_condition = "Signal condition is OK.";
            else if ((dbm >= 75) && (dbm <= 84))
              signal_condition = "Signal condition is good.";
            else if ((dbm >= 30) && (dbm <= 74)) {
              signal_condition = "Signal condition is excellent.";
            }
            str = "Signal level is -" + dbm + " dbm. " + signal_condition + "The signal strength range is -53 dbm (Excellent) to -109 dbm (Marginal).\n";
          }

          str = str + "\n";
          writeOutput(str);
        }

      }
      else if (cmd.contains("AT^SYSINFO"))
      {
        String[] srv_status = { "No Services", "Restricted Services", "Valid Services", "Restricted Regional Services", "Power saving or hibernate" };
        String[] srv_domain = { "No Services", "CS Service Only", "PS Service Only", "PS+CS Service Only", "Not Registered to CS or PS, searching now" };
        String[] roam_status = { "Not Roaming", "Roaming" };
        String[] sys_mode = { "No services", "AMPS mode", "CDMA Mode", "GSM/GPRS mode", "HDR mode", "WCDMA Mode", "GPS Mode", "GSM/WCDMA", "CDMA/HDR Hybrid" };
        String[] sim_state = { "Invalid SIM card", "Valid SIM card", "Invalid SIM card in CS", "Invalid SIM card in PS", "Invalid SIM in PS and CS" };
        String[] lock_state = { "SIM card is not locked by the CardLock feature", "SIM card is locked by CardLock feature" };
        String[] sys_submode = { "No service", "GSM Mode", "GPRS Mode", "EDGE Mode", "WCDMA Mode", "HSDPA Mode", "HSUPA Mode", "HSUPA and HSDPA Mode", "TD_SCDMA Mode", "HSPA+ Mode", "EVDO Rev.0", "EVDO Rev.A", "EVDO Rev.B", "1xRTT", "UMB", "1xEVDV", "3xRTT", "HSPA+(64QAM)", "HSPA+(MIMO)" };
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("SYSINFO:");
        t1 = tmp[1].split(",");
        int srv_status_int = Integer.parseInt(t1[0].trim());
        writeOutput("Serving Status -> " + srv_status[srv_status_int] + "\n");
        int srv_domain_int = Integer.parseInt(t1[1].trim());
        if (srv_domain_int == 255)
          writeOutput("Serving Domain -> CDMA:not supported\n");
        else
          writeOutput("Serving Domain -> " + srv_domain[srv_domain_int] + "\n");
        int roam_status_int = Integer.parseInt(t1[2].trim());
        writeOutput("Roaming Status -> " + roam_status[roam_status_int] + "\n");
        int sys_mode_int = Integer.parseInt(t1[3].trim());
        if (sys_mode_int == 15)
          writeOutput("System Mode-> TD-SCDMA Mode\n");
        else
          writeOutput("System Mode-> " + sys_mode[sys_mode_int] + "\n");
        int sim_state_int = Integer.parseInt(t1[4].trim());
        if (sim_state_int == 250)
          writeOutput("SIM State -> No SIM card is found\n");
        else {
          writeOutput("SIM State -> " + sim_state[sim_state_int] + "\n");
        }

        int sys_submode_int = Integer.parseInt(t1[6].trim());
        writeOutput("System submode -> " + sys_submode[sys_submode_int] + "\n");
      }
      else if (cmd.contains("AT^SYSCFG?"))
      {
        String[] mode = { "N/A", "N/A", "Automatic", "CDMA", "HDR", "N/A", "N/A", "CDMA/HDR", "N/A", "N/A", "N/A", "N/A", "GSM ONLY", "WCDMA ONLY", "TD_SCDMA ONLY", "No Change" };
        String[] acq_order = { "Automatic", "GSM > WCDMA ", "WCDMA > GSM", "No Change" };
        String[] roam = { "Not supported", "Supported", "No Change" };
        String[] srv_domain = { "CS Service Only", "PS Service Only", "PS+CS Service Only", "ANY", "No Change" };
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("SYSCFG:");
        t1 = tmp[1].split(",");
        int mode_int = Integer.parseInt(t1[0].trim());
        writeOutput("Mode -> " + mode[mode_int] + "\n");
        int acq_order_int = Integer.parseInt(t1[1].trim());
        if (acq_order_int == 255)
          writeOutput("Acquisition order -> Not supported\n");
        else {
          writeOutput("Acquisition Order -> " + acq_order[acq_order_int] + "\n");
        }
        writeOutput("Band -> " + t1[2].trim() + "\n");

        int roam_int = Integer.parseInt(t1[3].trim());
        writeOutput("Roaming-> " + roam[roam_int] + "\n");

        int srv_domain_int = Integer.parseInt(t1[4].trim());
        if (srv_domain_int == 255)
          writeOutput("Serving Domain -> Not supported\n");
        else {
          writeOutput("Serving Domain -> " + srv_domain[srv_domain_int] + "\n");
        }

      }
      else if (cmd.contains("AT^NWTIME?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("NWTIME:");
        t1 = tmp[1].split(",");
        writeOutput("Date -> " + t1[0] + "\n");
        writeOutput("Time -> " + t1[1] + "\n");
        writeOutput("Daylight Saving -> " + t1[2] + "\n");
      }
      else if (cmd.contains("AT^WAKEUPCFG?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("WAKEUPCFG:");
        t1 = tmp[1].split(",");
        writeOutput("Wakeup Enable/Diable -> " + t1[0] + "\n");
        writeOutput("Channel -> " + t1[1] + "\n");
        writeOutput("Source -> " + t1[2] + "\n");
      }
      else if (cmd.contains("AT^IMEISV?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("IMEISV:");
        t1 = tmp[1].split(",");
        writeOutput("IMEISV -> " + t1[0] + "\n");
      }
      else if (cmd.contains("AT^IOCTRL?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("IOCTRL:");
        t1 = tmp[1].split(",");
        writeOutput("Mapped as GPIO5,GPIO4,GPIO3,GPIO2,GPIO1. '0' is input, '1' is output\n");
        writeOutput("Input/Output -> " + t1[0] + "\n");
        writeOutput("Value -> " + t1[1] + "\n");
      }
      else if (cmd.contains("AT^ICCID?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("ICCID:");
        writeOutput("ICCID of the SIM card -> " + tmp[1] + "\n");
      }
      else if (cmd.contains("AT^USSDMODE?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("USSDMODE:");
        tmp[1] = tmp[1].trim();
        if (tmp[1].equals("0"))
          writeOutput("Configured for non-transparent USSD mode\n");
        else
          writeOutput("Configured for transparent USSD mode\n");
      }
      else if (cmd.contains("AT^ECHO?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("ECHO:");
        tmp[1] = tmp[1].trim();
        if (tmp[1].equals("0"))
          writeOutput("Echo canceller is closed\n");
        else if (tmp[1].equals("1"))
          writeOutput("Echo Cancel -> Handset mode, mild echo, short delay\n");
        else if (tmp[1].equals("2"))
          writeOutput("Echo Cancel -> Headset mode, moderate echo, short delay\n");
        else if (tmp[1].equals("3"))
          writeOutput("Echo Cancel -> Carkit mode, loud echo, long delay\n");
        else if (tmp[1].equals("4"))
          writeOutput("Echo Cancel -> Speakerphone mode, loud echo, long delay\n");
        else if (tmp[1].equals("5"))
          writeOutput("Echo Cancel -> Bluetooth headset mode\n");
      }
      else if (cmd.contains("AT^CPCM?"))
      {
        String[] cpcm_mode = { "MASTER_PRIM", "N/A", "MASTER_AUX", "SLAVE" };
        String[] data_format = { "Linear", "u-law", "A-law" };
        String[] clock_signal = { "2.048 MHz", "1.024 MHz", "512 kHz", "256 kHz" };
        String[] frame = { "Offset cleared", "Short sync offset", "Long sync offset" };
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CPCM:");
        t1 = tmp[1].split(",");

        int cpcm_mode_int = Integer.parseInt(t1[0].trim());
        writeOutput("PCM Working Mode -> " + cpcm_mode[cpcm_mode_int] + "\n");

        int data_format_int = Integer.parseInt(t1[1].trim());
        writeOutput("Data Format -> " + data_format[data_format_int] + "\n");

        int clock_signal_int = Integer.parseInt(t1[2].trim());
        writeOutput("Clock Signal-> " + clock_signal[clock_signal_int] + "\n");

        int frame_int = Integer.parseInt(t1[3].trim());
        writeOutput("Frame -> " + frame[frame_int] + "\n");
      }
      else if (cmd.contains("AT^STSF?"))
      {
        String[] stk_mode = { "STK disabled", "STK Active" };
        String[] stk_rawmode = { "Raw Data Mode", "Common Mode", "Standard raw data mode" };

        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("STSF:");
        t1 = tmp[1].split(",");

        int stk_mode_int = Integer.parseInt(t1[0].trim());
        writeOutput("SIM Tool Kit Mode -> " + stk_mode[stk_mode_int] + "\n");

        int stk_rawmode_int = Integer.parseInt(t1[1].trim());
        writeOutput("RawMode -> " + stk_rawmode[stk_rawmode_int] + "\n");
      }
      else if (cmd.contains("AT^CHIPTEMP?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("CHIPTEMP:");
        t1 = tmp[1].split(",");

        int gsm_pa_temp = Integer.parseInt(t1[0].trim());

        writeOutput("The temperature unit is 0.1°C. For example, if the returned value range is (–200,1000), the temperature ranges from –20°C to 100°C.\n\n");

        if (gsm_pa_temp == 65535)
          writeOutput("GSM PA temperature is not supported\n");
        else {
          writeOutput("GSM PA temperature ->" + gsm_pa_temp + "\n");
        }
        int wcdma_pa_temp = Integer.parseInt(t1[1].trim());

        if (wcdma_pa_temp == 65535)
          writeOutput("WCDMA PA temperature is not supported\n");
        else {
          writeOutput("WCDMA PA temperature ->" + wcdma_pa_temp + "\n");
        }
        int LTE_pa_temp = Integer.parseInt(t1[2].trim());

        if (LTE_pa_temp == 65535)
          writeOutput("LTE PA temperature is not supported\n");
        else {
          writeOutput("LTE PA temperature ->" + LTE_pa_temp + "\n");
        }
        int sim_temp = Integer.parseInt(t1[3].trim());

        if (sim_temp == 65535)
          writeOutput("SIM card temperature is not supported\n");
        else {
          writeOutput("SIM card temperature ->" + sim_temp + "\n");
        }
        int battery_temp = Integer.parseInt(t1[4].trim());

        if (battery_temp == 65535)
          writeOutput("Battery temperature is not supported\n");
        else {
          writeOutput("Battery temperature ->" + battery_temp + "\n");
        }
        int crystal_temp = Integer.parseInt(t1[5].trim());

        if (crystal_temp == 65535)
          writeOutput("Crystal temperature is not supported\n");
        else
          writeOutput("Crystal temperature ->" + crystal_temp + "\n");
      }
      else if (cmd.contains("AT^THERMFUN?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("THERMFUN:");
        tmp[1] = tmp[1].trim();
        if (tmp[1].equals("1"))
          writeOutput("Temperature protection is enabled\n");
        else {
          writeOutput("Temperature protection is disabled\n");
        }

      }
      else if (cmd.contains("AT^NDISSTATQRY?"))
      {
        String[] stat = { "Disconnected", "Connected", "In Connection", "Disconnected" };

        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("NDISSTATQRY:");
        t1 = tmp[1].split(",");

        int stat_int = Integer.parseInt(t1[0].trim());
        writeOutput("Connection Status -> " + stat[stat_int] + "\n");

        int error_code = Integer.parseInt(t1[1].trim());
        writeOutput("Error code -> " + error_code + "\n");

        writeOutput("PDP Type-> " + t1[3] + "\n");
      }
      else if (cmd.contains("AT^DVCFG?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("DVCFG:");
        tmp[1] = tmp[1].trim();
        if (tmp[1].equals("0"))
          writeOutput("Voice call is preferred over data.\n");
        else
          writeOutput("Data service is preferred over voice.\n");
      }
      else if (cmd.contains("AT^IPINIT?"))
      {
        String resp2 = (String)response.get(0);
        if (resp2.contains("ERROR"))
        {
          tmp = resp2.split("ERROR:");
          writeOutput("IP initialization failed with error " + tmp[1] + "\n");
          huawei_ip_init = 0;
          return;
        }

        String resp = (String)response.get(1);

        tmp = resp2.split("IPINIT:");
        t1 = tmp[1].split(",");
        t1[0] = t1[0].trim();
        if (t1[0].equals("0"))
        {
          huawei_ip_init = 0;
          writeOutput("The IP connection is not initialized, or network connection fails to be established..\n");
        }
        else
        {
          huawei_ip_init = 1;
          writeOutput("The network connection is sucessfully initialized.\n");
          writeOutput("IP Address -> " + t1[1] + "\n");
          writeOutput("APN -> " + t1[2] + "\n");
          writeOutput("Primary DNS Address -> " + t1[3] + "\n");
          writeOutput("Secondary DNS Address -> " + t1[4] + "\n");
        }

      }
      else if (cmd.contains("AT^IPOPEN?"))
      {
        int num_profiles = response.size();
        if (num_profiles <= 1)
        {
          huawei_ip_open = 0;
          writeOutput("No connection has been opened.\n");
          return;
        }

        if (num_profiles >= 1)
          writeOutput("Following connections are opened,\r\n");
        huawei_ip_open = 1;
        for (int j = 0; j < num_profiles; j++)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split("IPOPEN:");
            t1 = tmp[1].split(",");
            writeOutput("\r\n");
            writeOutput("Link ID->" + t1[0] + "\n" + "Type->" + t1[1] + "\n" + "Local Port->" + t1[2] + "\n" + "Remote IP->" + t1[3] + "\n" + "Remote Port->" + t1[4] + "\n" + "SIO Port>" + t1[5] + "\n" + "MSS Port->" + t1[6] + "\r\n");
          }

        }

      }
      else if (cmd.contains("AT^IPLISTEN?"))
      {
        String resp = (String)response.get(1);
        String resp2 = (String)response.get(0);
        tmp = resp2.split("IPLISTEN:");
        t1 = tmp[1].split(",");

        writeOutput("Link Type->" + t1[0] + "\n");
        writeOutput("Listen Port -> " + t1[1] + "\n");
        writeOutput("Idle Links -> " + t1[2] + "\n");
      }
      else if (cmd.contains("AT^IPCFL?"))
      {
        int num_profiles = response.size();
        if (num_profiles <= 1)
        {
          writeOutput("No connection has been opened.\n");
          return;
        }

        if (num_profiles >= 1)
          writeOutput("Following parameters are configured,\r\n");
        for (int j = 0; j < num_profiles; j++)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split("IPCFL:");
            t1 = tmp[1].split(",");
            writeOutput("\r\n");
            t1[0] = t1[0].trim();
            if (t1[0].equals("5"))
              writeOutput("Timer for transparent transmission-> " + t1[1] + "\n");
            else if (t1[0].equals("10"))
              writeOutput("Length of TCP/IP data packet-> " + t1[1] + "\n");
            if (t1[0].equals("12")) {
              writeOutput("Transmission Mode -> " + t1[1] + "\n");
            }

          }

        }

      }
      else if (cmd.contains("AT^IPFLOWQ?"))
      {
        int num_profiles = response.size();
        if (num_profiles <= 1)
        {
          writeOutput("No connection has been opened.\n");
          return;
        }

        if (num_profiles >= 1)
          writeOutput("Link statistics information:\r\n");
        for (int j = 0; j < num_profiles; j++)
        {
          t1 = null;
          String resp = (String)response.get(j);
          if (resp.equals("OK"))
            break;
          if (resp != "")
          {
            tmp = resp.split("IPFLOWQ:");
            t1 = tmp[1].split(",");
            writeOutput("\r\n");
            writeOutput("Link ID->" + t1[0] + "\n" + "Tx from User->" + t1[1] + "\n" + "Tx to Socket->" + t1[2] + "\n" + "Tx Acknowledged->" + t1[3] + "\n" + "Rx from Socket->" + t1[4] + "\n" + "Rx to user->" + t1[5] + "\r\n");
          }

        }

      }
      else if (cmd.contains("AT^WPDOM?"))
      {
        String[] gps_mode = { "Standalone", "Network only", "Speed Optimal", "Accuracy optimal", 
          "Data Optimal", "MS-based only", "gpsOneXTRA", "Accuracy optimal", "Low Accuracy MSA" };
        String resp2 = (String)response.get(0);
        tmp = resp2.split("WPDOM:");

        int gps_int = Integer.parseInt(tmp[1].trim());
        writeOutput("GPS Operation Mode -> " + gps_mode[gps_int] + "\n");
      }
      else if (cmd.contains("AT^WPDST?"))
      {
        String[] gps_session = { "Single position", "Tracing position", "Last Position", "Data Download" };
        String resp2 = (String)response.get(0);
        tmp = resp2.split("WPDST:");

        int gps_session_int = Integer.parseInt(tmp[1].trim());
        writeOutput("Connection Status -> " + gps_session[gps_session_int] + "\n");
      }
      else if (cmd.contains("AT^WPQOS?"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("WPQOS:");

        t1 = tmp[1].split(",");
        writeOutput("\r\n");
        writeOutput("Performance->" + t1[0] + " seconds\n" + "Accuracy->" + t1[1] + " meters" + "\r\n");
      }
      else if (cmd.contains("AT^WPDGL?"))
      {
        String[] gps_mode = { "Enable Mobile-initiated and Enable Mobile-terminiated session", 
          "Disable Mobile-initiated and Enable Mobile-terminiated session", 
          "Enable Mobile-initiated and Disable Mobile-terminiated session", 
          "Disable  Mobile-initiated and Disable Mobile-terminiated session" };

        String resp2 = (String)response.get(0);
        tmp = resp2.split("WPDGL:");

        int gps_int = Integer.parseInt(tmp[1].trim());
        writeOutput("GPS Session Lock -> " + gps_mode[gps_int] + "\n");
      }
      else if (cmd.contains("AT^GPSTYPE?"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("GPSTYPE:");

        int gps_int = Integer.parseInt(tmp[1].trim());
        int tr = 1;
        tr = gps_int & 0x1;
        writeOutput("Following GPS types are supported - ");
        if (tr == 1)
          writeOutput("Standalone,");
        tr = gps_int & 0x2;
        if (tr == 2)
          writeOutput("Control plane,");
        tr = gps_int & 0x4;
        if (tr == 4)
          writeOutput("User plane,");
        tr = gps_int & 0x8;
        if (tr == 8)
          writeOutput("gpsOneXTRA");
        writeOutput("\n");
      }
      else if (cmd.contains("AT^WGNSS?"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("WGNSS:");

        tmp[1] = tmp[1].trim();

        if (tmp[1].equals("0"))
          writeOutput("Positioning system type is GPS");
        else {
          writeOutput("Positioning system type is GNSS");
        }
      }
      else if (cmd.contains("AT^FOTAMODE?"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FOTAMODE:");

        t1 = tmp[1].split(",");
        t1[0] = t1[0].trim();
        if (t1[0].equals("0"))
          writeOutput("Detect Mode -> Manual\n");
        else {
          writeOutput("Detect Mode -> Automatic\n");
        }
        t1[1] = t1[1].trim();
        if (t1[1].equals("0"))
          writeOutput("Download Mode -> Manual\n");
        else {
          writeOutput("Download Mode -> Automatic\n");
        }
        t1[2] = t1[2].trim();
        if (t1[2].equals("0"))
          writeOutput("Update Mode -> Manual\n");
        else {
          writeOutput("Update Mode -> Automatic\n");
        }
        t1[3] = t1[3].trim();
        if (t1[3].equals("0"))
          writeOutput("Resumable Transfer -> Disable\n");
        else {
          writeOutput("Resumable Transfer -> Enable\n");
        }
        t1[4] = t1[4].trim();

        writeOutput("Days between version detection ->" + t1[4] + "\n");
      }
      else if (cmd.contains("AT^FOTACFG?"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FOTACFG:");

        t1 = tmp[1].split(",");
        t1[0] = t1[0].trim();
        writeOutput("APN -> " + t1[0] + "\n" + "Username -> " + t1[1] + "\n" + "Password -> " + t1[2] + "\n" + "Authentication Type -> " + t1[3] + "\n");
      }
      else if (cmd.contains("IPDATA:"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("IPDATA: \\d,\\d*,");

        textArea.setText(tmp[1]);
      }
      else if (cmd.contains("POSITION:"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("POSITION:");

        t1 = tmp[1].split(",");
        t1[0] = t1[0].trim();
        textField_50.setText(t1[1]);
        textField_51.setText(t1[0]);
        textField_52.setText(t1[2]);
        writeOutput("Latitide -> " + t1[1] + "\n" + "Longitude -> " + t1[0] + "\n" + "Altitide-> " + t1[2] + "\n");
      }
      else if (cmd.contains("AT^FOTASTATE?"))
      {
        String resp2 = (String)response.get(0);
        tmp = resp2.split("FOTASTATE:");

        tmp[1] = tmp[1].trim();
        if (tmp[1].equals("10"))
          writeOutput("FOTA State -> Idle\n");
        else if (tmp[1].equals("11"))
          writeOutput("FOTA State -> Querying\n");
        else if (tmp[1].equals("12"))
          writeOutput("FOTA State -> New version found\n");
        else if (tmp[1].equals("13"))
          writeOutput("FOTA State -> New version query failed\n");
        else if (tmp[1].equals("14"))
          writeOutput("FOTA State -> No version found\n");
        else if (tmp[1].equals("20"))
          writeOutput("FOTA State -> Download failed\n");
        else if (tmp[1].equals("30"))
          writeOutput("FOTA State -> Download progressing\n");
        else if (tmp[1].equals("31"))
          writeOutput("FOTA State -> Download pending\n");
        else if (tmp[1].equals("40"))
          writeOutput("FOTA State -> Download complete\n");
        else if (tmp[1].equals("50"))
          writeOutput("FOTA State -> Ready to update\n");
        else if (tmp[1].equals("60"))
          writeOutput("FOTA State -> Update progressing\n");
        else if (tmp[1].equals("80"))
          writeOutput("FOTA State -> Update failed\n");
        else if (tmp[1].equals("60"))
          writeOutput("FOTA State -> Update sucessful\n");
      }
    }
  }

  public class HyperlinkLabel extends JLabel
    implements MouseListener
  {
    private static final long serialVersionUID = 5167616594614061634L;
    private URL url = null;

    public HyperlinkLabel(String label)
    {
      super();
      addMouseListener(this);
    }
    public HyperlinkLabel(String label, URL url) {
      this(label);
      this.url = url;
      setText("<html><a href=\"\">" + label + "</a></html>");
      setToolTipText("Go to: " + url.getRef());
    }
    public HyperlinkLabel(String label, String tip, URL url) {
      this(label, url);
      setToolTipText(tip);
    }

    public void setURL(URL url) {
      this.url = url;
    }
    public URL getURL() { return url; }

    public void mouseClicked(MouseEvent e) {
      HyperlinkLabel self = (HyperlinkLabel)e.getSource();
      if (self.url == null)
        return;
      if (Desktop.isDesktopSupported()) {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Action.BROWSE))
          try {
            desktop.browse(url.toURI());
            return;
          } catch (Exception localException) {
          }
      }
      JOptionPane.showMessageDialog(this, "Cannot launch browser...\n Please, visit\n" + url.getRef(), "", 1);
    }

    public void mouseEntered(MouseEvent e)
    {
      e.getComponent().setCursor(Cursor.getPredefinedCursor(12));
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }
  }

  class OpenL
    implements ActionListener
  {
    OpenL()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      JFileChooser c = new JFileChooser();

      c.setFileFilter(new FileNameExtensionFilter("AT Command Script File", new String[] { "at" }));

      int rVal = c.showOpenDialog(ATCommandTester_v27.this);
      if (rVal == 0) {
        filename.setText(c.getSelectedFile().getName());
        dir.setText(c.getCurrentDirectory().toString());
        BufferedReader br = null;
        String fname = c.getSelectedFile().getName();
        String path = c.getCurrentDirectory().toString();
        String myFile = path + "\\" + fname;
        try
        {
          br = new BufferedReader(new FileReader(myFile));
          textArea_3.setText("");
          String sCurrentLine;
          while ((sCurrentLine = br.readLine()) != null)
          {
            textArea_3.setText(textArea_3.getText() + "\n" + sCurrentLine + "\n");
          }

          writeOutput("Loaded script " + myFile + "\n");
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "Script Loaded" });
        }
        catch (IOException i) {
          writeOutput("Cannot load script" + myFile + "\n");
          try
          {
            if (br != null) br.close(); 
          }
          catch (IOException ex) { ex.printStackTrace(); }

        }
        finally
        {
          try
          {
            if (br != null) br.close(); 
          }
          catch (IOException ex) { ex.printStackTrace(); }

        }
      }
      if (rVal == 1) {
        filename.setText("You pressed cancel");
        dir.setText("");
      }
    }
  }
  class SaveL implements ActionListener {
    SaveL() {  }


    public void actionPerformed(ActionEvent e) { String content = null;
      String saved_content = null;
      Random rand = new Random();
      int min = 1000; int max = 9999;

      int randomNum = rand.nextInt(max - min + 1) + min;

      if (e.getSource() == btnSaveScript)
      {
        content = textArea_3.getText();
        saved_content = "Script";
      }
      else if (e.getSource() == btnSaveLog_7)
      {
        content = textPane_10.getText();
        saved_content = "Log";
      }

      String fn = "";
      if (saved_content == "Log")
        fn = "log" + randomNum + ".log";
      else {
        fn = "script" + randomNum + ".at";
      }
      JFileChooser c = new JFileChooser();
      c.setSelectedFile(new File(fn));

      int rVal = c.showSaveDialog(ATCommandTester_v27.this);
      if (rVal == 0) {
        String fname = c.getSelectedFile().getName();
        String path = c.getCurrentDirectory().toString();
        filename.setText(c.getSelectedFile().getName());
        dir.setText(c.getCurrentDirectory().toString());
        try
        {
          String myFile = path + "/" + fname;
          File file = new File(myFile);

          if (!file.exists()) {
            file.createNewFile();
          }
          String tmp = file.getAbsolutePath();
          FileWriter fw = new FileWriter(file.getAbsoluteFile());
          BufferedWriter bw = new BufferedWriter(fw);
          bw.write(content);
          bw.close();

          writeOutput(saved_content + " saved in " + tmp + "\n");
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", saved_content + " Saved" });
        }
        catch (IOException i) {
          i.printStackTrace();
        }
      }
      if (rVal == 1) {
        filename.setText("You pressed cancel");
        dir.setText("");
      }
    }
  }

  public class atParser
    implements ActionListener
  {
    public atParser()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      String port1 = (String)comboBox.getSelectedItem();
      String ret1 = is_device_connected();
      if (ret1.equals("NULL"))
        return;
      if (port1 == null)
      {
        msg = "Port not selected. Press 'Find Ports'  or 'AutoConnect'.\r\n\r\n";
        writeOutput(msg);
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "submit_port_not_selected" });
      }
      else
      {
        TwoWaySerialComm2.send_to_parser = 1;
        if (winflag == 1) {
          win.call("setMessage", new String[] { "Tester", "diagnostics_command_sent" });
        }
        if (!chckbxApppendOutput.isSelected())
        {
          textPane_10.setText("");
        }

        if (e.getSource() == btnDeviceInfo) {
          String ret = mySerial1.ser_write("AT+CGMI\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
          try {
            Thread.sleep(500L);
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }
          ret = mySerial1.ser_write("AT+CGMM\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
          try {
            Thread.sleep(500L);
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }
          ret = mySerial1.ser_write("AT+CGMR\r\n", 1);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", ret });
          }
        }
        else if (e.getSource() == btnSignalStrength) {
          String ret = mySerial1.ser_write("AT+CSQ\r\n", 1);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", ret });
          }
        }
        else if (e.getSource() == btnGetPdpContexts) {
          String ret = mySerial1.ser_write("AT+CGDCONT?\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
          try {
            Thread.sleep(3000L);
          } catch (InterruptedException localInterruptedException1) {
          }
        }
        else if (e.getSource() == btnRegistrationStatus) {
          String ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
          if (winflag == 1) {
            win.call("setMessage", new String[] { "Tester", ret });
          }
        }
        else if (e.getSource() == btnAvaialbleNetworks) {
          String ret = mySerial1.ser_write("AT+COPS=?\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
          try {
            Thread.sleep(5000L);
          } catch (InterruptedException localInterruptedException2) {
          }
          writeOutput("Please wait as this could take sometime....\n");
        }
        else if (e.getSource() == btnOperatorInfo) {
          String ret = mySerial1.ser_write("AT+COPS?\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException3) {
          }
        }
        else if (e.getSource() == btnSimStatus) {
          writeOutput("The preferred operator list:\n");
          String ret = mySerial1.ser_write("AT+CPOL?\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
        }
        else if (e.getSource() == button_58)
        {
          String ret = mySerial1.ser_write("AT+CSMS?\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
        }
        else if (e.getSource() == button_19)
        {
          String ret = mySerial1.ser_write("AT+CPIN?\r\n", 1);
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", ret });
        }
        else if (e.getSource() == button_30) {
          num_profiles_connected = 0;
          connected_profile = null;
          String ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException4) {
          }
          if (num_profiles_connected > 0)
          {
            writeOutput("Device is connected.\n");
            String t3 = "AT+CGPADDR=" + connected_profile + "\r\n";
            ret = mySerial1.ser_write(t3, 1);
          }
          else
          {
            writeOutput("Device is NOT connected.\n\n");
          }
        }
        else if (e.getSource() == button_44) {
          String ret = mySerial1.ser_write("AT+CNUM\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException5) {
          }
        } else if (e.getSource() == btnDeviceCapability) {
          String ret = mySerial1.ser_write("AT+GCAP\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException6) {
          }
        } else if (e.getSource() == button_22) {
          String ret = mySerial1.ser_write("AT+CBC\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException7) {
          }
        } else if (e.getSource() == button_23) {
          String ret = mySerial1.ser_write("AT+IPR?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException8) {
          }
        } else if (e.getSource() == button_27) {
          String ret = mySerial1.ser_write("AT&V\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException9) {
          }
        } else if (e.getSource() == button_24) {
          String ret = mySerial1.ser_write("AT+CFUN?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException10) {
          }
        } else if (e.getSource() == button_17) {
          String ret = mySerial1.ser_write("AT+CPAS\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException11) {
          }
        } else if (e.getSource() == button_36) {
          String ret = mySerial1.ser_write("AT+IFC?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException12) {
          }
        } else if (e.getSource() == button_45) {
          String ret = mySerial1.ser_write("AT+CGSN\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException13) {
          }
        } else if (e.getSource() == button_42) {
          String ret = mySerial1.ser_write("AT+CR?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException14) {
          }
        } else if (e.getSource() == button_51) {
          String ret = mySerial1.ser_write("AT+CAMM?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException15) {
          }
        } else if (e.getSource() == button_52) {
          String ret = mySerial1.ser_write("AT+CPUC?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException16) {
          }
        } else if (e.getSource() == button_35) {
          String ret = mySerial1.ser_write("ATZ\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException17) {
          }
        } else if (e.getSource() == button_54) {
          String ret = mySerial1.ser_write("AT+CAOC=0?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException18) {
          }
        } else if (e.getSource() == button_37) {
          String ret = mySerial1.ser_write("AT+CCLK?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException19) {
          }
        } else if (e.getSource() == button_49) {
          String ret = mySerial1.ser_write("AT+CPBR=1,99\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException20) {
          }
        } else if (e.getSource() == button_60) {
          String ret = mySerial1.ser_write("AT+CSCS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException21) {
          }
        } else if (e.getSource() == button_50) {
          String ret = mySerial1.ser_write("AT+CALA?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException22) {
          }
        } else if (e.getSource() == button_25) {
          String ret = mySerial1.ser_write("AT+CMUT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException23) {
          }
        }
        else if (e.getSource() == button_28) {
          writeOutput("The following indicators types are supported by the device,\r\n");
          String ret = mySerial1.ser_write("AT+CIND=?\r\n", 1);
          try {
            Thread.sleep(1000L); } catch (InterruptedException localInterruptedException24) {
          }
          writeOutput("\r\nThe indicator values are,\r\n");

          ret = mySerial1.ser_write("AT+CIND?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException25) {
          }
        } else if (e.getSource() == button_29) {
          String ret = mySerial1.ser_write("AT+CGCLASS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException26) {
          }
        }
        else if (e.getSource() == button_31) {
          String ret = mySerial1.ser_write("AT+CGDCONT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException27) {
          }
        }
        else if (e.getSource() == button_32) {
          String ret = mySerial1.ser_write("AT+CGATT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException28) {
          }
        }
        else if (e.getSource() == button_34) {
          String ret = mySerial1.ser_write("AT+CGQREQ?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException29) {
          }
        }
        else if (e.getSource() == button_40) {
          String ret = mySerial1.ser_write("AT+CBST?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException30) {
          }
        }
        else if (e.getSource() == button_53) {
          writeOutput("Following AT Commands are supported by the device\n");
          String ret = mySerial1.ser_write("AT+CLAC\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException31) {
          }
        }
        else if (e.getSource() == button_38) {
          String ret = mySerial1.ser_write("AT+CRLP?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException32) {
          }
        }
        else if (e.getSource() == button_55) {
          String ret = mySerial1.ser_write("AT+FCLASS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException33) {
          }
        }
        else if (e.getSource() == button_57) {
          String ret = mySerial1.ser_write("AT+CUSD?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException34) {
          }
        }
        else if (e.getSource() == button_43) {
          String ret = mySerial1.ser_write("AT+CEER\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException35) {
          }
        }
        else if (e.getSource() == button_18) {
          String ret = mySerial1.ser_write("AT+CMGF?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException36) {
          }
        }
        else if (e.getSource() == button_20) {
          String ret = mySerial1.ser_write("AT+CSCA?\r\n", 1);
          try {
            Thread.sleep(1000L);
          }
          catch (InterruptedException localInterruptedException37) {
          }
        }
        else if (e.getSource() == button_21) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CENG?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException38) {
          }
        } else if (e.getSource() == button_26) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CBAND?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException39) {
          }
        } else if (e.getSource() == button_46) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CADC?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException40) {
          }
        } else if (e.getSource() == button_56) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CCALR?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException41) {
          }
        } else if (e.getSource() == button_62) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CSMINS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException42) {
          }
        } else if (e.getSource() == button_63) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CSPN?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException43) {
          }
        } else if (e.getSource() == button_64) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CCVM?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException44) {
          }
        } else if (e.getSource() == button_65) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CCID\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException45) {
          }
        } else if (e.getSource() == button_66) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CMTE?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException46) {
          }
        } else if (e.getSource() == button_67) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CGMSCLASS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException47) {
          }
        } else if (e.getSource() == button_68) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CEMNL?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException48) {
          }
        } else if (e.getSource() == button_69) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          writeOutput("Mic gain values for for channels:\n");
          String ret = mySerial1.ser_write("AT+CMIC?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException49) {
          }
        }
        else if (e.getSource() == button_70) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CEXTHS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException50) {
          }
        } else if (e.getSource() == button_71) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+SVR?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException51) {
          }
        } else if (e.getSource() == button_72) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CBUZZRRING?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException52) {
          }
        } else if (e.getSource() == button_73) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CHF?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException53) {
          }
        } else if (e.getSource() == button_74) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          writeOutput("This is current configuration TCP context:\n");
          String ret = mySerial1.ser_write("AT+CIPSCONT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException54) {
          }
        } else if (e.getSource() == button_75) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CIPMUX?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException55) {
          }
        } else if (e.getSource() == button_76) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          writeOutput("TCP and UDP are configured to use following ports\n");
          String ret = mySerial1.ser_write("AT+CLPORT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException56) {
          }
        } else if (e.getSource() == button_77) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CSTT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException57) {
          }
        } else if (e.getSource() == button_78) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CIFSR\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException58) {
          }
        } else if (e.getSource() == button_79) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          writeOutput("The connection status is:\n");
          String ret = mySerial1.ser_write("AT+CIPSTATUS\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException59) {
          }
        } else if (e.getSource() == button_80) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CDNSCFG?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException60) {
          }
        } else if (e.getSource() == button_81) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CIPHEAD?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException61) {
          }
        } else if (e.getSource() == button_82) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CIPSERVER?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException62) {
          }
        } else if (e.getSource() == button_83) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CIPCSGP?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException63) {
          }
        } else if (e.getSource() == button_84) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CIPMODE?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException64) {
          }
        } else if (e.getSource() == button_85) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CIPCCFG?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException65) {
          }
        } else if (e.getSource() == button_87) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          writeOutput("HTTP configuration parameters:\n");
          String ret = mySerial1.ser_write("AT+HTTPPARA?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException66) {
          }
        } else if (e.getSource() == button_88) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          writeOutput("Current HTTP context:\n");
          String ret = mySerial1.ser_write("AT+HTTPSCONT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException67) {
          }
        } else if (e.getSource() == button_86) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPMODE?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException68) {
          }
        } else if (e.getSource() == button_90) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPPORT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException69) {
          }
        } else if (e.getSource() == button_91) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPTYPE?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException70) {
          }
        } else if (e.getSource() == button_92) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPPUTOPT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException71) {
          }
        } else if (e.getSource() == button_93) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPSERV?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException72) {
          }
        } else if (e.getSource() == button_94) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPUN?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException73) {
          }
        } else if (e.getSource() == button_95) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPPW?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException74) {
          }
        } else if (e.getSource() == button_96) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPGETNAME?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException75) {
          }
        } else if (e.getSource() == button_97) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPGETPATH?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException76) {
          }
        } else if (e.getSource() == button_98) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          writeOutput("Current FTP application context:\n");
          String ret = mySerial1.ser_write("AT+FTPSCONT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException77) {
          }
        } else if (e.getSource() == button_99) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+FTPSTATE\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException78) {
          }
        } else if (e.getSource() == button_100) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CGPSPWR?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException79) {
          }
        } else if (e.getSource() == button_101) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CGPSRST?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException80) {
          }
        } else if (e.getSource() == button_102) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CGPSINF=0\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException81) {
          }
        } else if (e.getSource() == button_89) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "sim_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT+CGPSSTATUS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException82) {
          }
        } else if (e.getSource() == button_33) {
          num_profiles_connected = 0;
          connected_profile = null;
          String ret = mySerial1.ser_write("AT+CGACT?\r\n", 1);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException83) {
          }
          if (num_profiles_connected > 0)
          {
            writeOutput("Device is connected.\n");
            String t3 = "AT+CGPADDR=" + connected_profile + "\r\n";
            ret = mySerial1.ser_write(t3, 1);
          }
          else
          {
            writeOutput("\nDevice is NOT connected, so no IP address is available.\n\n");
          }

        }
        else if (e.getSource() == button_103) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^SYSINFO\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException84) {
          }
        } else if (e.getSource() == button_140) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^SYSCFG?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException85) {
          }
        } else if (e.getSource() == button_105) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^NWTIME?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException86) {
          }
        } else if (e.getSource() == button_112) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^WAKEUPCFG?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException87) {
          }
        } else if (e.getSource() == button_109) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^IMEISV?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException88) {
          }
        } else if (e.getSource() == button_110) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^IOCTRL?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException89) {
          }
        } else if (e.getSource() == button_104) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^ICCID?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException90) {
          }
        } else if (e.getSource() == button_111) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^USSDMODE?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException91) {
          }
        } else if (e.getSource() == button_106) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^ECHO?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException92) {
          }
        } else if (e.getSource() == button_121) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^CPCM?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException93) {
          }
        } else if (e.getSource() == button_122) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^STSF?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException94) {
          }
        } else if (e.getSource() == button_129) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^CHIPTEMP?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException95) {
          }
        } else if (e.getSource() == button_130) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^THERMFUN?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException96) {
          }
        } else if (e.getSource() == button_137) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^NDISSTATQRY?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException97) {
          }
        } else if (e.getSource() == button_138) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^DVCFG?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException98) {
          }
        } else if (e.getSource() == button_114) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^IPINIT?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException99) {
          }
        } else if (e.getSource() == button_113) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^IPOPEN?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException100) {
          }
        } else if (e.getSource() == button_115) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^IPLISTEN?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException101) {
          }
        } else if (e.getSource() == button_116) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^IPCFL?\r\n", 1);
          try {
            Thread.sleep(3000L);
          } catch (InterruptedException localInterruptedException102) {
          }
        } else if (e.getSource() == button_117) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^IPFLOWQ?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException103) {
          }
        } else if (e.getSource() == button_119) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^WPDOM?\r\n", 1);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException104) {
          }
        } else if (e.getSource() == button_124) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^WPDST?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException105) {
          }
        } else if (e.getSource() == button_125) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^WPQOS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException106) {
          }
        } else if (e.getSource() == button_123) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^WPDGL?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException107) {
          }
        } else if (e.getSource() == button_135) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^GPSTYPE?\r\n", 1);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException localInterruptedException108) {
          }
        } else if (e.getSource() == button_136) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^WGNSS?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException109) {
          }
        } else if (e.getSource() == button_128) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^FOTAMODE?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException110) {
          }
        } else if (e.getSource() == button_132) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^FOTACFG?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException111) {
          }
        } else if (e.getSource() == button_133) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^FOTASTATE?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException112) {
          }
        } else if (e.getSource() == button_149) {
          if (winflag == 1)
            win.call("setMessage", new String[] { "Tester", "huawei_diagnostics_command_sent" });
          String ret = mySerial1.ser_write("AT^FOTADLQ?\r\n", 1);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException localInterruptedException113) {
          }
        }
        else {
          writeOutput("To be implemented.. Stay Tuned!!!\n\n");
        }
      }
    }
  }

  public class pbListner
    implements ActionListener
  {
    public pbListner()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      JTextField d_index = new JTextField();
      JTextField d_number = new JTextField();
      JTextField d_name = new JTextField();
      JTextField d_type = new JTextField("129");
      Object[] options = { "Add", "Cancel" };
      String dialog_header = "Add Phonebook Entry";

      String ret1 = is_device_connected();
      if (ret1.equals("NULL")) {
        return;
      }
      int num_rows = table_3.getRowCount();
      if ((num_rows < 1) && (e.getSource() == btnEditEntry))
      {
        msg = "No phonebook entries available.Press 'Read Phonebook' button.\r\n\r\n";
        writeOutput(msg);
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "phonebook_edit_empty_list" });
      }
      else
      {
        int n;
        JComponent[] inputs2;
        if (e.getSource() == btnEditEntry)
        {
          int selectedRowIndex = table_3.getSelectedRow();

          String index = (String)table_3.getModel().getValueAt(selectedRowIndex, 0);
          String number = (String)table_3.getModel().getValueAt(selectedRowIndex, 1);
          String name = (String)table_3.getModel().getValueAt(selectedRowIndex, 2);
          String type = (String)table_3.getModel().getValueAt(selectedRowIndex, 3);
          d_index.setText(index);
          d_number.setText(number);
          d_name.setText(name);
          d_type.setText(type);
          options[0] = "Update";
          options[1] = "Cancel";
          dialog_header = "Edit Phonebook Entry";

          JComponent[] inputs1 = { 
            new JLabel("Index"), 
            d_index, 
            new JLabel("Name"), 
            d_name, 
            new JLabel("Number"), 
            d_number };

          n = JOptionPane.showOptionDialog(panel_4, inputs1, "Edit Phonebook Entry", 0, 
            -1, null, options, options[1]);
        }
        else
        {
          inputs2 = new JComponent[] { 
            new JLabel("Name"), 
            d_name, 
            new JLabel("Number"), 
            d_number };

          n = JOptionPane.showOptionDialog(panel_4, inputs2, "Add Phonebook Entry", 0, 
            -1, null, options, options[1]);
        }

        if (n == 0) {
          if (e.getSource() == btnEditEntry)
          {
            // inputs2 = d_index.getText();
          }
          String new_number = d_number.getText();
          new_number = "\"" + new_number + "\"";
          String new_name = d_name.getText();
          String new_index = d_index.getText();
          new_name = "\"" + new_name + "\"";
          String new_type = d_type.getText();

          if ((new_name == "") || (new_number == ""))
          {
            msg = "Name and Number cannot be empty\r\n\r\n";
            writeOutput(msg);
          }
          else
          {
            String t6;
            if (e.getSource() == btnEditEntry)
            {
              t6 = "AT+CPBW=" + new_index + "," + new_number + "," + new_type + "," + new_name + "\r\n";
            }
            else
            {
              t6 = "AT+CPBW=," + new_number + "," + new_type + "," + new_name + "\r\n";
            }
            String ret = mySerial1.ser_write(t6, 1);
            try {
              Thread.sleep(500L);
            }
            catch (InterruptedException localInterruptedException)
            {
            }
          }
        }
      }
    }
  }

  public class pdpListner implements ActionListener
  {
    public pdpListner() {
    }

    public void actionPerformed(ActionEvent e) {
      JTextField d_cid = new JTextField();
      JTextField d_apn = new JTextField();
      JTextField d_ip = new JTextField("0.0.0.0");
      JTextField d_pdp_type = new JTextField("IP");
      JTextField d_data_comp = new JTextField("0");
      JTextField d_header_comp = new JTextField("0");
      Object[] options = { "Add", "Cancel" };
      String dialog_header = "Add PDP Context";

      int num_rows = table.getRowCount();
      int connection_exists = 0;
      if ((num_rows < 1) && (e.getSource() == btnPdpEdit))
      {
        msg = "No PDP contexts available.Press 'Get PDP Contexts' button get connection profiles.\r\n\r\n";
        writeOutput(msg);
        if (winflag == 1)
          win.call("setMessage", new String[] { "Tester", "callconnect_no_profiles" });
        return;
      }

      if (e.getSource() == btnPdpEdit)
      {
        int selectedRowIndex = table.getSelectedRow();

        String apn = (String)table.getModel().getValueAt(selectedRowIndex, 2);
        String cid = (String)table.getModel().getValueAt(selectedRowIndex, 0);
        String ip = (String)table.getModel().getValueAt(selectedRowIndex, 3);
        String pdp_type = (String)table.getModel().getValueAt(selectedRowIndex, 1);
        String data_comp = (String)table.getModel().getValueAt(selectedRowIndex, 4);
        String header_comp = (String)table.getModel().getValueAt(selectedRowIndex, 5);

        d_cid.setText(cid);
        d_apn.setText(apn);
        d_ip.setText(ip);
        d_pdp_type.setText(pdp_type);
        d_data_comp.setText(data_comp);
        d_header_comp.setText(header_comp);
        options[0] = "Update";
        options[1] = "Cancel";
        dialog_header = "Edit PDP Context";
      }

      JComponent[] inputs = { 
        new JLabel("CID"), 
        d_cid, 
        new JLabel("APN"), 
        d_apn, 
        new JLabel("IP Address"), 
        d_ip, 
        new JLabel("PDP Type"), 
        d_pdp_type, 
        new JLabel("Data Compression"), 
        d_data_comp, 
        new JLabel("Header Compression"), 
        d_header_comp };

      int n = JOptionPane.showOptionDialog(panel_4, inputs, dialog_header, 0, 
        3, null, options, options[1]);

      if (n == 0) {
        String new_cid = d_cid.getText();
        String new_apn = d_apn.getText();
        new_apn = "\"" + new_apn;
        new_apn = new_apn + "\"";
        String new_ip = d_ip.getText();
        new_ip = "\"" + new_ip;
        new_ip = new_ip + "\"";
        String new_pdp_type = d_pdp_type.getText();
        new_pdp_type = "\"" + new_pdp_type;
        new_pdp_type = new_pdp_type + "\"";
        String new_data_comp = d_data_comp.getText();
        String new_header_comp = d_header_comp.getText();
        if ((new_cid == "") || (new_apn == ""))
        {
          msg = "CID and APN cannot be empty.\r\n\r\n";
          writeOutput(msg);
        }
        else
        {
          String t6 = "AT+CGDCONT=" + new_cid + "," + new_pdp_type + "," + new_apn + "," + new_ip + "," + new_data_comp + "," + new_header_comp + "\r\n";
          String ret = mySerial1.ser_write(t6, 1);
          try {
            Thread.sleep(500L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
      }
    }
  }

  public class smsHandler implements ActionListener {
    public smsHandler() {
    }

    public void actionPerformed(ActionEvent e) {
      String ret = "";

      String ctz = Character.toString('\032');
      if ((e.getSource() != btnShowPdu) && (e.getSource() != btnShowTextFormat))
      {
        ret = is_device_connected();
        if (ret.equals("NULL"))
          return;
      }
      String phone_number = textField_3.getText();
      String message = textArea_9.getText();
      if (phone_number.equals(""))
      {
        msg = "Please enter destination phone number.\n\n";
        writeOutput(msg);
        return;
      }

      if (message.equals(""))
      {
        msg = "Please enter message.\n\n";
        writeOutput(msg);
        return;
      }

      if (winflag == 1)
      {
        if (e.getSource() == btnSendSms)
          win.call("setMessage", new String[] { "Tester", "SMS_send" });
        else if (e.getSource() == btnShowTextFormat)
          win.call("setMessage", new String[] { "Tester", "SMS_ShowText" });
        else {
          win.call("setMessage", new String[] { "Tester", "SMS_ShowPDU" });
        }
      }
      if (e.getSource() == btnShowPdu)
      {
        print_pdu = Integer.valueOf(1);
        String PDUText = MakePDUTextGSM(phone_number, message);

        print_pdu = Integer.valueOf(0);
        return;
      }

      if (e.getSource() == btnShowTextFormat)
      {
        writeOutput("Text Mode Formatted Message\r\n");
        String msg_fmt = "AT+CMGS=" + phone_number + "\r\n" + message + ctz + "\r\n";
        writeOutput(msg_fmt + "\r\n");
        return;
      }

      msg = "Checking registration status...\n\n";
      writeOutput(msg);
      reg_status = -1;
      ret = mySerial1.ser_write("AT+CREG?\r\n", 1);
      try {
        Thread.sleep(1000L); } catch (InterruptedException localInterruptedException) {
      }
      reg_status = 1;
      if ((reg_status == 1) || (reg_status == 5))
      {
        msg = "Checking SMS Mode...\n\n";
        writeOutput(msg);
        String cmd = "AT+CMGF?\r\n";

        ret = mySerial1.ser_write(cmd, 1);
        try
        {
          Thread.sleep(1000L);
        } catch (InterruptedException localInterruptedException1) {
        }
        if (gbl_sms_mode == 1)
        {
          msg = "Device is configured for Text mode for SMS.\n\n";
          writeOutput(msg);

          phone_number = "\"" + phone_number + "\"";

          cmd = "AT+CMGS=" + phone_number + "\r";
          ret = mySerial1.ser_write(cmd, 1);
          try {
            Thread.sleep(200L); } catch (InterruptedException localInterruptedException2) {
          }
          cmd = message + ctz + "\r\n";
          ret = mySerial1.ser_write(cmd, 0);
        }
        else
        {
          writeOutput(msg);

          String PDUText = MakePDUTextGSM(phone_number, message);
          cmd = "AT+CMGS=" + (PDUText.length() / 2 - 1 - smsc_length.intValue()) + "\r\n";

          ret = mySerial1.ser_write(cmd, 1);
          try {
            Thread.sleep(500L); } catch (InterruptedException localInterruptedException3) {
          }
          cmd = PDUText + "\032" + "\r\n";
          ret = mySerial1.ser_write(cmd, 0);
        }
      }
    }

    private String MakePDUTextGSM(String num, String msg)
    {
      String smsc_field = "";
      String msg_content = "";

      String hexSeptets = gsmToSeptetsToHex(msg, null);
      Integer first_octet = Integer.valueOf(0);
      String pdu = "";
      Integer rp = Integer.valueOf(Integer.parseInt(textField_61.getText(), 2));
      first_octet = Integer.valueOf(first_octet.intValue() | rp.intValue() << 7);
      Integer uhdi = Integer.valueOf(Integer.parseInt(textField_53.getText(), 2));
      first_octet = Integer.valueOf(first_octet.intValue() | uhdi.intValue() << 6);
      Integer srr = Integer.valueOf(Integer.parseInt(textField_54.getText(), 2));
      first_octet = Integer.valueOf(first_octet.intValue() | srr.intValue() << 5);
      Integer vpf = Integer.valueOf(Integer.parseInt(textField_49.getText(), 2));
      first_octet = Integer.valueOf(first_octet.intValue() | vpf.intValue() << 3);
      Integer rd = Integer.valueOf(Integer.parseInt(textField_60.getText(), 2));
      first_octet = Integer.valueOf(first_octet.intValue() | rd.intValue() << 2);
      Integer mti = Integer.valueOf(Integer.parseInt(textField_59.getText(), 2));
      first_octet = Integer.valueOf(first_octet.intValue() | mti.intValue());

      String t2 = Integer.toHexString(first_octet.intValue());
      String mr = textField_55.getText();

      String t3 = "";
      if (mr.equals("0"))
        t3 = "00";
      else {
        t3 = Utilities.intToHexFixedWidth(mr.length(), 2);
      }

      String encoding = (String)comboBox_23.getSelectedItem();

      String smsc_include = (String)comboBox_24.getSelectedItem();

      if (smsc_include.equals("No1")) {
        pdu = "00";
      }
      else {
        smsc_field = textField_46.getText();

        smsc_field = smsc_field.replace("+", "");
        if (smsc_field.equals(""))
        {
          writeOutput("SMS Service center entry is empty.Not including this field in the PDU\r\n");
          pdu = "00";
          smsc_length = Integer.valueOf(0);
        }
        else
        {
          String t5 = "91" + AddrToPDU(smsc_field);
          smsc_length = Integer.valueOf(t5.length() / 2);
          String t6 = '0' + Integer.toHexString(smsc_length.intValue());
          pdu = t6 + "91" + AddrToPDU(smsc_field);
        }

      }

      pdu = pdu + t2 + t3 + Utilities.intToHexFixedWidth(num.length(), 2) + "81" + AddrToPDU(num) + "00";

      if (encoding.equals("7-bit"))
        pdu = pdu + "00";
      else
        pdu = pdu + "08";
      if (vpf.intValue() != 0)
      {
        Integer validity_period = Integer.valueOf(Integer.parseInt(textField_58.getText()));
        String t4 = Integer.toHexString(validity_period.intValue());
        pdu = pdu + t4;
      }
      if (encoding.equals("7-bit")) {
        msg_content = hexSeptets;
        pdu = pdu + Utilities.intToHexFixedWidth(msg.length(), 2) + msg_content;
      }
      else
      {
        msg_content = ATStringConverter.Java2UCS2Hex(msg);
        pdu = pdu + Utilities.intToHexFixedWidth(msg.length() * 2, 2) + msg_content;
      }
      if (print_pdu.intValue() == 1)
      {
        writeOutput("PDU Mode Formatted Message\r\n");
        writeOutput("Validity Period Format (VPF) -> " + vpf + "\r\n");
        writeOutput("User Data Header Indication (UDHI) ->" + uhdi + "\r\n");
        writeOutput("Status Report Request (SRR) -> " + srr + "\r\n");
        writeOutput("Message ID (MR) ->" + mr + "\r\n");
        writeOutput("Data Coding Scheme (DCS) -> " + encoding + "\r\n");
        writeOutput("Validity Period (VP) ->" + textField_58.getText() + "\r\n");
        writeOutput("Message Type (MTI) ->" + mti + "\r\n");
        writeOutput("Reject Duplicates (RD) ->" + rd + "\r\n");
        writeOutput("Reply Path (RP) ->" + rp + "\r\n");
        if (smsc_field.equals(""))
          writeOutput("SMS Service Center -> Not Included\r\n");
        else
          writeOutput("SMS Service Center -> " + AddrToPDU(smsc_field) + "\r\n");
        writeOutput("Message Content ->" + msg_content + "\r\n");
        writeOutput("PDU ->" + pdu + "\r\n");
        String pdu_cmd = "AT+CMGS=" + (pdu.length() / 2 - 1) + "\r\n" + pdu + "\032";
        writeOutput(pdu_cmd + "\r\n\r\n");
      }
      return pdu;
    }

    private String AddrToPDU(String s)
    {
      String r = new String();
      int i = 0;
      s = s.substring(0, s.length());
      if (s.length() % 2 > 0) {
        s = s + 'F';
      }
      while (i < s.length()) {
        char t = s.charAt(i + 1);
        char t2 = s.charAt(i);

        r = r + t;
        r = r + t2;
        i += 2;
      }
      return r;
    }

    private String gsmToSeptetsToHex(String gsmString, String UDH) {
      boolean DEBUG = false;
      if (DEBUG) {
        System.out.println("Trying to convert gsmToSeptetsToHex: gsmString=" + gsmString + ", UDH=" + UDH);
      }
      int skipBits = 0;
      if ((UDH != null) && 
        (UDH.length() != 0)) {
        int udhLength = UDH.length() / 2;
        skipBits = (udhLength + 1) * 8 + (7 - (udhLength + 1) * 8 % 7) % 7;
      }

      if (DEBUG) {
        System.out.println("skipBits=" + skipBits);
      }
      boolean[] udBool = new boolean[skipBits + gsmString.length() * 7 + (8 - (skipBits + gsmString.length() * 7) % 8) % 8];
      if (DEBUG) {
        System.out.println("gsmString.length()=" + gsmString.length() + "; udBool[].length=" + udBool.length);
      }
      if ((UDH != null) && 
        (UDH.length() != 0)) {
        for (int i = 0; i < UDH.length() / 2; i++) {
          boolean[] t = Utilities.intToBinaryArrayFixedWidth(Integer.parseInt(UDH.substring(i * 2, (i + 1) * 2), 16), 8);
          System.arraycopy(t, 0, udBool, i * 8, 8);
        }
      }

      for (int i = 0; i < gsmString.length(); i++) {
        boolean[] t = Utilities.intToBinaryArrayFixedWidth(gsmString.charAt(i), 7);

        String boolArrToString = "";
        for (int j = 0; j < t.length; j++) {
          boolArrToString = boolArrToString + (t[j] ? '1' : '0');
        }
        if (DEBUG) {
          System.out.println("t=" + boolArrToString);
        }

        System.arraycopy(t, 0, udBool, skipBits + i * 7, 7);
      }
      int length = udBool.length / 8;
      if (DEBUG) {
        System.out.println("length=" + length);
      }
      String hexResult = "";
      for (int i = 0; i < length; i++) {
        boolean[] t = new boolean[8];
        System.arraycopy(udBool, i * 8, t, 0, 8);
        hexResult = hexResult + Utilities.intToHexFixedWidth(Utilities.binaryArrayToInt(t), 2);
      }
      if (DEBUG) {
        System.out.println("hexResult=" + hexResult);
      }
      return hexResult;
    }
  }
}