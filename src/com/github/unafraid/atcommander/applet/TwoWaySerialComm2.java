package com.github.unafraid.atcommander.applet;

import gnu.io.*;
import netscape.javascript.JSObject;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

public class TwoWaySerialComm2
{
  public static JTextPane t_area;
  public InputStream in1;
  public OutputStream out1;
  public JSObject win;
  public CommPort commPort;
  public SerialPort serialPort;
  public String port_name;
  public int winflag = 0;
  public static ATCommandTester_v27 atTester;
  public static int send_to_parser;
  public static String last_cmd;
  public StyledDocument doc;
  public Style style;

  public TwoWaySerialComm2(JTextPane txt_area, JSObject win)
  {
    t_area = txt_area;
    this.win = win;
    doc = t_area.getStyledDocument();
    style = t_area.addStyle("Red", null);
    StyleConstants.setForeground(style, Color.black);
  }

  public void setOutputArea(JTextPane txt_area)
  {
    t_area = txt_area;
    doc = t_area.getStyledDocument();
    style = t_area.addStyle("Red", null);
    StyleConstants.setForeground(style, Color.black);
  }

  public String connect(String portName, int speed)
    throws Exception
  {
    String ret = "connect_fail";

    CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
    System.out.println("PortIdentifier is " + portIdentifier + "\r\n\r\n");
    if (portIdentifier.isCurrentlyOwned())
    {
      System.out.println("Error: Port is currently in use");
      StyleConstants.setForeground(style, Color.black);

      String s1 = "\nError: " + portName + " port is currently in use.\n";
      try {
        doc.insertString(doc.getLength(), s1, style);
      }
      catch (BadLocationException e) {
        e.printStackTrace();
      }

      ret = "connect_port_in_use";
    }
    else
    {
      String s = "Before opening portIdentifier open\r\n";
      try
      {
        commPort = portIdentifier.open(getClass().getName(), 2000);
      }
      catch (Exception e) {
        System.out.println(e.getMessage());

        StyleConstants.setForeground(style, Color.black);

        String s1 = portName + "  is currently in use. Please close the application that is using this port and connect again." + "\r\n";
        try {
          doc.insertString(doc.getLength(), s1, style);
        }
        catch (BadLocationException e1) {
          e.printStackTrace();
        }
        return "connect_port_in_use";
      }

      System.out.println("commPort is " + commPort + "\r\n");

      if ((commPort instanceof SerialPort))
      {
        serialPort = ((SerialPort)commPort);
        port_name = portIdentifier.getName();

        serialPort.setSerialPortParams(speed, 8, 1, 0);

        InputStream in = serialPort.getInputStream();
        OutputStream out = serialPort.getOutputStream();
        in1 = in;
        out1 = out;

        serialPort.addEventListener(new SerialReader(in1, win));
        serialPort.notifyOnDataAvailable(true);

        StyleConstants.setForeground(style, Color.black);

        String s1 = "Connected to port " + portName + " at baud rate " + speed + " bps.\r\n\r\n";

        ret = "connect_sucess";
      }
      else
      {
        System.out.println("Error: Only serial ports are handled by this example.");

        ret = "connect_error";

        StyleConstants.setForeground(style, Color.black);
        String s1 = "Error connecting to " + portName + ".\r\n";
        try {
          doc.insertString(doc.getLength(), s1, style);
        }
        catch (BadLocationException e) {
          e.printStackTrace();
        }

      }

    }

    return ret;
  }

  public String listPorts()
  {
    ArrayList port_name = new ArrayList();
    String tmp = "";

    int i = 0;
    Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

    Enumeration portList = CommPortIdentifier.getPortIdentifiers();
    while (portEnum.hasMoreElements())
    {
      CommPortIdentifier portIdentifier = (CommPortIdentifier)portEnum.nextElement();
      if (getPortTypeName(portIdentifier.getPortType()) == "Serial")
      {
        port_name.add(portIdentifier.getName());
        String tmp2 = portIdentifier.getName() + "#";

        tmp = tmp + tmp2;
      }
      System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
    }
    i = port_name.size();

    return tmp;
  }

  public String disconnect()
  {
    String ret = "disconnect_fail";
    if (serialPort != null)
    {
      try {
        in1.close();
        out1.close();
      }
      catch (IOException localIOException)
      {
      }

      StyleConstants.setForeground(style, Color.black);
      String s1 = "Disconnecting port " + port_name + ".\r\n\r\n";
      try {
        doc.insertString(doc.getLength(), s1, style);
      }
      catch (BadLocationException e) {
        e.printStackTrace();
      }
      serialPort.close();
      serialPort = null;

      ret = "disconnect_success";
    }
    else
    {
      StyleConstants.setForeground(style, Color.black);
      String s1 = "No port is connected.\r\n\r\n";
      try {
        doc.insertString(doc.getLength(), s1, style);
      }
      catch (BadLocationException e) {
        e.printStackTrace();
      }

      ret = "disconnect_port_not_selected";
    }
    return ret;
  }

  static String getPortTypeName(int portType)
  {
    switch (portType)
    {
    case 3:
      return "I2C";
    case 2:
      return "Parallel";
    case 5:
      return "Raw";
    case 4:
      return "RS485";
    case 1:
      return "Serial";
    }
    return "unknown type";
  }

  public String ser_write(String str, int type)
  {
    String ret = "submit_fail";
    if (serialPort != null)
    {
      try
      {
        System.out.println(str);
        out1.write(str.getBytes());

        if (type == 1)
        {
          String[] t5 = str.split("\r\n");
          last_cmd = t5[0];
        }
        ret = "submit_data_written";
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }

    }
    else
    {
      StyleConstants.setForeground(style, Color.black);
      String s1 = "No port is connected.\r\n\r\n";
      try {
        doc.insertString(doc.getLength(), s1, style);
      }
      catch (BadLocationException e) {
        e.printStackTrace();
      }
      ret = "submit_port_not_connected";
    }

    return ret;
  }

  public String check_port_connected()
  {
    String ret = "NULL";
    if (serialPort != null)
    {
      ret = serialPort.getName();
    }

    return ret;
  }

  public static class SerialReader
    implements SerialPortEventListener
  {
    private InputStream in;
    private byte[] portBuffer = new byte[1024];
    private String fileBuffer;
    public JSObject win;
    public int winflag = 1;
    static int j;
    static ArrayList resp_array = new ArrayList();

    public SerialReader(InputStream in, JSObject win) {
      this.in = in;
      this.win = win;
    }

    public void serialEvent(SerialPortEvent arg0)
    {
      String delimiter = "\r\n";
      try
      {
        int len = 0;
        int data;
        while ((data = in.read()) > -1)
        {
          portBuffer[(len++)] = ((byte)data);
          if (data == 10)
          {
            break;
          }
        }
        fileBuffer = new String(portBuffer, 0, len);
        Date today = new Date();
        String date = new Timestamp(today.getTime()).toString();

        System.out.print(fileBuffer.toString());
        String tstr = fileBuffer.toString();
        StyledDocument doc = TwoWaySerialComm2.t_area.getStyledDocument();
        Style style = TwoWaySerialComm2.t_area.addStyle("Red", null);
        StyleConstants.setForeground(style, Color.blue);
        StyleConstants.setItalic(style, true);
        StyleConstants.setFontSize(style, 10);
        if (tstr.contains("RING"))
        {
          TwoWaySerialComm2.atTester.at_parser("RING", null);
          return;
        }
        if (tstr.contains("CONNECT OK"))
        {
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }
          TwoWaySerialComm2.atTester.at_parser("CONNECT OK", null);
          return;
        }
        if (tstr.contains("CLOSE"))
        {
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }
          TwoWaySerialComm2.atTester.at_parser("CLOSE", null);
          return;
        }
        if (tstr.contains("NO CARRIER"))
        {
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }
          TwoWaySerialComm2.atTester.at_parser("NO CARRIER", null);
          return;
        }
        if (tstr.contains("+HTTPACTION:0"))
        {
          String[] temp = tstr.split(delimiter);
          resp_array.clear();
          resp_array.add(temp[0]);
          TwoWaySerialComm2.atTester.at_parser("AT+HTTPACTION=", resp_array);
          return;
        }
        if (tstr.contains("POSITION:"))
        {
          String[] temp = tstr.split(delimiter);
          resp_array.clear();
          resp_array.add(temp[0]);
          TwoWaySerialComm2.atTester.at_parser("POSITION:", resp_array);
          return;
        }
        if (tstr.contains("IPDATA:"))
        {
          resp_array.clear();
          while ((data = in.read()) > -1)
          {
            portBuffer[(len++)] = ((byte)data);
          }

          fileBuffer = new String(portBuffer, 0, len);
          System.out.print(fileBuffer.toString());
          tstr = fileBuffer.toString();
          resp_array.add(tstr);
          TwoWaySerialComm2.atTester.at_parser("IPDATA:", resp_array);
          return;
        }
        if (tstr.contains("+FTPPUT:1,"))
        {
          String[] temp = tstr.split(delimiter);
          String[] temp1 = temp[0].split(":");
          temp1 = temp1[1].split(",");
          TwoWaySerialComm2.last_cmd = "+FTPPUT:";
          resp_array.clear();
          resp_array.add(temp1[0]);
          resp_array.add(temp1[1]);
          if (temp1[1].equals("1"))
            resp_array.add(temp1[2]);
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }
          TwoWaySerialComm2.atTester.at_parser(TwoWaySerialComm2.last_cmd, resp_array);
          return;
        }
        if (tstr.contains("+FTPPUT:2,"))
        {
          String[] temp = tstr.split(delimiter);
          String[] temp1 = temp[0].split(":");
          temp1 = temp1[1].split(",");
          TwoWaySerialComm2.last_cmd = "+FTPPUT:";
          resp_array.clear();
          resp_array.add(temp1[0]);
          resp_array.add(temp1[1]);
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }
          TwoWaySerialComm2.atTester.at_parser(TwoWaySerialComm2.last_cmd, resp_array);
          return;
        }
        if (tstr.contains("+FTPGET:1,"))
        {
          String[] temp = tstr.split(delimiter);
          String[] temp1 = temp[0].split(":");
          TwoWaySerialComm2.last_cmd = "+FTPGET:";
          resp_array.clear();
          resp_array.add(temp1[1]);
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }
          TwoWaySerialComm2.atTester.at_parser(TwoWaySerialComm2.last_cmd, resp_array);
          return;
        }
        if (tstr.contains("+FTPGET:2,"))
        {
          String[] temp = tstr.split(delimiter);
          String[] temp1 = temp[0].split(":");
          temp = tstr.split(":");
          TwoWaySerialComm2.last_cmd = "+FTPGET:";
          resp_array.clear();
          resp_array.add(temp1[1]);
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }
          return;
        }

        if (tstr.equals("AT"))
        {
          resp_array.clear();
          return;
        }
        if (TwoWaySerialComm2.send_to_parser == 1)
        {
          try
          {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }

          if ((tstr.equals(TwoWaySerialComm2.last_cmd + "\r\r\n")) || (tstr.equals(TwoWaySerialComm2.last_cmd + "\r\n")) || 
            (tstr.equals(TwoWaySerialComm2.last_cmd + "\r")))
          {
            resp_array.clear();
          }
          else
          {
            String[] temp = tstr.split(delimiter);
            if (temp[0].contains("ERROR"))
            {
              resp_array.add(temp[0]);
              TwoWaySerialComm2.atTester.at_parser(TwoWaySerialComm2.last_cmd, resp_array);
              resp_array.clear();
            }
            else if (temp[0].equals("OK"))
            {
              resp_array.add(temp[0]);
              TwoWaySerialComm2.atTester.at_parser(TwoWaySerialComm2.last_cmd, resp_array);
              resp_array.clear();
            }
            else if (temp[0] != "")
            {
              resp_array.add(temp[0]);
            }
          }
        }
        else
        {
          try {
            doc.insertString(doc.getLength(), tstr, style);
          }
          catch (BadLocationException e) {
            e.printStackTrace();
          }

        }

      }
      catch (IOException e)
      {
        e.printStackTrace();
        System.exit(-1);
      }
    }
  }

  public static class SerialWriter
    implements Runnable
  {
    OutputStream out;

    public SerialWriter(OutputStream out)
    {
      this.out = out;
    }

    public void run()
    {
      try
      {
        int c = 0;
        while ((c = System.in.read()) > -1)
        {
          out.write(c);
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
        System.exit(-1);
      }
    }
  }
}